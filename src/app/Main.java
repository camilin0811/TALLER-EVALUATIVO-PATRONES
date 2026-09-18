package app;

import boletin.BoletinDiario;
import boletin.ImpresorBoletin;
import configuracion.ConfiguracionEstacion;
import configuracion.TareaMantenimiento;
import puesto.LecturaCruda;
import puesto.PuestoBajoCosto;
import puesto.PuestoDeMonitoreo;
import puesto.PuestoFijoReferencia;
import puesto.PuestoMovil;
import puesto.ResultadoContaminante;
import puesto.ResultadoEstacion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Main {

    private static final List<String> POLLUTANTS = List.of("PM2.5", "O3", "NO2");
    private static final LocalDate BULLETIN_DATE = LocalDate.of(2026, 8, 14);

    public static void main(String[] args) {
        System.out.println("=== SECRETARIA DE AMBIENTE - RED DE CALIDAD DEL AIRE ===");

        ConfiguracionEstacion model = buildResidentialModel();
        ConfiguracionEstacion parkConfig = model.clone();
        ConfiguracionEstacion schoolConfig = model.clone();
        schoolConfig.setThreshold("PM2.5", 25.0);
        schoolConfig.addMaintenanceTask(new TareaMantenimiento("Limpieza de filtro escolar", "quincenal"));

        System.out.printf("Config modelo 'Zona residencial' %s%n", model.summary());
        System.out.printf("  Estacion Parque Centenario     %s%n", parkConfig.summary());
        System.out.printf("  Estacion Colegio San Jose      %s  <- modificada%n", schoolConfig.summary());
        System.out.printf("  Verificacion del modelo -> %s (intacto)%n", model.summary());
        System.out.printf("  Verificacion del clon hermano -> %s (intacto)%n", parkConfig.summary());
        System.out.printf("  Mapas de umbrales distintos: %b | listas de tareas distintas: %b%n",
                model.getAlertThresholds() != schoolConfig.getAlertThresholds(),
                model.getMaintenancePlan() != schoolConfig.getMaintenancePlan());

        List<PuestoDeMonitoreo> stations = List.of(
                new PuestoFijoReferencia("Parque Centenario", parkConfig, 1.02),
                new PuestoFijoReferencia("Av. Quebradaseca", model.clone(), 1.01),
                new PuestoMovil("Unidad movil - Zona Ind.", model.clone()),
                new PuestoBajoCosto("Colegio San Jose", schoolConfig));

        List<List<LecturaCruda>> days = List.of(
                GeneradorLecturas.day(11, POLLUTANTS, 9.5, 58, 1),
                GeneradorLecturas.partialDay(22, POLLUTANTS, 24.0, 61, "O3", 15),
                GeneradorLecturas.day(33, POLLUTANTS, 12.0, 49, 2),
                GeneradorLecturas.day(44, POLLUTANTS, 44.0, 88, 2));

        System.out.println();
        System.out.println("--- PROCESAMIENTO POR ESTACION ---");

        List<ResultadoEstacion> results = new ArrayList<>();
        for (int i = 0; i < stations.size(); i++) {
            PuestoDeMonitoreo station = stations.get(i);
            ResultadoEstacion result = station.procesarJornada(days.get(i));
            results.add(result);
            printStation(station, result);
        }

        List<ResultadoEstacion> ranked = RedDeMonitoreo.rank(results);
        BoletinDiario bulletin = BoletinDiario.builder()
                .date(BULLETIN_DATE)
                .issuingEntity("Secretaria de Ambiente Municipal")
                .stationResults(ranked)
                .cityIndex(RedDeMonitoreo.cityIndex(ranked))
                .cityCategory(RedDeMonitoreo.cityCategory(ranked))
                .networkCoverage(RedDeMonitoreo.coverage(ranked))
                .sensitiveGroupAdvice("personas con asma y ninos deben evitar actividad fisica al aire libre")
                .nextDayForecast("condiciones estables, sin lluvias significativas")
                .publicationOwner("Direccion de Calidad Ambiental")
                .methodologyNote("ICA interpolado por tramos segun tabla vigente")
                .build();

        ImpresorBoletin.print(bulletin);
        demonstrateBuilderValidation(ranked);
    }

    private static void printStation(PuestoDeMonitoreo station, ResultadoEstacion result) {
        System.out.printf("[%s] tipo %s - %s%n",
                result.stationName(), result.type(), station.describeAnalyzer());

        for (String line : station.getCorrectionTrace()) {
            System.out.println(line);
        }

        for (ResultadoContaminante pollutant : result.pollutantResults()) {
            if (pollutant.valid()) {
                System.out.printf("   %-5s descartadas: %d | validas: %d de 24 -> VALIDO%n",
                        pollutant.pollutant(), pollutant.discardedCount(), pollutant.validCount());
                System.out.printf("   %-5s promedio 24h: %.2f -> ICA %d (%s)%n",
                        pollutant.pollutant(), pollutant.average(),
                        pollutant.ica().index(), pollutant.ica().category());
            } else {
                System.out.printf("   %-5s validas: %d de 24 -> DATO INSUFICIENTE%n",
                        pollutant.pollutant(), pollutant.validCount());
            }
        }
        System.out.println();
    }

    private static void demonstrateBuilderValidation(List<ResultadoEstacion> results) {
        System.out.println();
        System.out.println("--- VALIDACION DEL BUILDER ---");
        try {
            BoletinDiario.builder()
                    .date(BULLETIN_DATE)
                    .issuingEntity("Secretaria de Ambiente Municipal")
                    .stationResults(results)
                    .cityIndex(165)
                    .cityCategory("Danina a la salud")
                    .networkCoverage(90.0)
                    .build();
            System.out.println("no se lanzo la excepcion esperada");
        } catch (IllegalStateException e) {
            System.out.println("IllegalStateException -> " + e.getMessage());
        }
    }

    private static ConfiguracionEstacion buildResidentialModel() {
        Map<String, Double> thresholds = new LinkedHashMap<>();
        thresholds.put("PM2.5", 37.5);
        thresholds.put("O3", 70.0);
        thresholds.put("NO2", 100.0);

        List<TareaMantenimiento> plan = new ArrayList<>(List.of(
                new TareaMantenimiento("Calibracion con gas patron", "mensual"),
                new TareaMantenimiento("Cambio de filtro de entrada", "trimestral"),
                new TareaMantenimiento("Verificacion de flujo", "semanal")));

        return new ConfiguracionEstacion("Zona residencial", 60,
                new ArrayList<>(POLLUTANTS), thresholds, plan);
    }
}
