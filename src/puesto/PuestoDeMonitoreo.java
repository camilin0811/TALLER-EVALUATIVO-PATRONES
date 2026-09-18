package puesto;

import analizador.Analizador;
import configuracion.ConfiguracionEstacion;
import ica.ResultadoIca;
import ica.TablaIca;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class PuestoDeMonitoreo {

    public static final int REQUIRED_VALID_READINGS = 18;
    public static final int HOURS_PER_DAY = 24;

    protected final String name;
    protected final ConfiguracionEstacion configuration;
    private final List<String> correctionTrace = new ArrayList<>();

    protected PuestoDeMonitoreo(String name, ConfiguracionEstacion configuration) {
        this.name = name;
        this.configuration = configuration;
    }

    protected abstract Analizador crearAnalizador(String contaminante);

    public abstract String getType();

    public ResultadoEstacion procesarJornada(List<LecturaCruda> readings) {
        correctionTrace.clear();
        Map<String, List<LecturaCruda>> byPollutant = new LinkedHashMap<>();
        for (String pollutant : configuration.getPollutants()) {
            byPollutant.put(pollutant, new ArrayList<>());
        }
        for (LecturaCruda reading : readings) {
            List<LecturaCruda> bucket = byPollutant.get(reading.pollutant());
            if (bucket != null) {
                bucket.add(reading);
            }
        }

        List<ResultadoContaminante> results = new ArrayList<>();
        for (Map.Entry<String, List<LecturaCruda>> entry : byPollutant.entrySet()) {
            results.add(process(entry.getKey(), entry.getValue()));
        }

        int stationIndex = 0;
        String category = "Sin dato";
        String critical = "N/A";
        for (ResultadoContaminante result : results) {
            if (result.valid() && result.ica().index() > stationIndex) {
                stationIndex = result.ica().index();
                category = result.ica().category();
                critical = result.pollutant();
            }
        }
        return new ResultadoEstacion(name, getType(), results, stationIndex, category, critical);
    }

    private ResultadoContaminante process(String pollutant, List<LecturaCruda> readings) {
        Analizador analyzer = crearAnalizador(pollutant);
        List<Double> corrected = new ArrayList<>();
        int discarded = 0;

        for (LecturaCruda reading : readings) {
            if (!analyzer.isInRange(reading.value())) {
                discarded++;
                continue;
            }
            double value = analyzer.correct(reading.value(), reading.relativeHumidity());
            corrected.add(value);
            if (tracesCorrection() && correctionTrace.size() < 3
                    && pollutant.equals("PM2.5") && reading.hour() >= 7) {
                correctionTrace.add(String.format("   h%02d cruda %.2f HR %.0f %% -> corregida %.2f",
                        reading.hour(), reading.value(), reading.relativeHumidity(), value));
            }
        }

        if (corrected.size() < REQUIRED_VALID_READINGS) {
            return ResultadoContaminante.insufficient(pollutant, corrected.size(), discarded);
        }

        double sum = 0;
        for (double value : corrected) {
            sum += value;
        }
        double average = Math.round((sum / corrected.size()) * 100.0) / 100.0;
        ResultadoIca result = TablaIca.compute(pollutant, average);
        return new ResultadoContaminante(pollutant, corrected.size(), discarded, average, true, result);
    }

    protected boolean tracesCorrection() {
        return false;
    }

    public List<String> getCorrectionTrace() {
        return correctionTrace;
    }

    public String getName() {
        return name;
    }

    public ConfiguracionEstacion getConfiguration() {
        return configuration;
    }

    public String describeAnalyzer() {
        return crearAnalizador(configuration.getPollutants().get(0)).getDescription();
    }
}
