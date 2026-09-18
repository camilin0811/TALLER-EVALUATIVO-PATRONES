package puesto;

import analizador.Analizador;
import analizador.AnalizadorReferencia;
import configuracion.ConfiguracionEstacion;

public class PuestoFijoReferencia extends PuestoDeMonitoreo {

    private final double calibrationFactor;

    public PuestoFijoReferencia(String name, ConfiguracionEstacion configuration, double calibrationFactor) {
        super(name, configuration);
        this.calibrationFactor = calibrationFactor;
    }

    @Override
    protected Analizador crearAnalizador(String contaminante) {
        return new AnalizadorReferencia(calibrationFactor, maxRangeFor(contaminante));
    }

    @Override
    public String getType() {
        return "FIJO_REFERENCIA";
    }

    private double maxRangeFor(String contaminante) {
        return switch (contaminante) {
            case "PM2.5" -> 500.0;
            case "O3" -> 300.0;
            case "NO2" -> 1500.0;
            default -> 1000.0;
        };
    }
}
