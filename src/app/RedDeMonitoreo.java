package app;

import puesto.PuestoDeMonitoreo;
import puesto.ResultadoEstacion;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class RedDeMonitoreo {

    private RedDeMonitoreo() {
    }

    public static List<ResultadoEstacion> rank(List<ResultadoEstacion> results) {
        List<ResultadoEstacion> ranked = new ArrayList<>(results);
        ranked.sort(Comparator.comparingInt(ResultadoEstacion::stationIndex).reversed()
                .thenComparing(ResultadoEstacion::stationName));
        return ranked;
    }

    public static double coverage(List<ResultadoEstacion> results) {
        int expected = 0;
        int valid = 0;
        for (ResultadoEstacion result : results) {
            expected += result.pollutantResults().size() * PuestoDeMonitoreo.HOURS_PER_DAY;
            valid += result.totalValidReadings();
        }
        if (expected == 0) {
            return 0.0;
        }
        return Math.round((valid * 1000.0 / expected)) / 10.0;
    }

    public static int cityIndex(List<ResultadoEstacion> results) {
        return results.stream().mapToInt(ResultadoEstacion::stationIndex).max().orElse(0);
    }

    public static String cityCategory(List<ResultadoEstacion> results) {
        return results.stream()
                .max(Comparator.comparingInt(ResultadoEstacion::stationIndex))
                .map(ResultadoEstacion::category)
                .orElse("Sin dato");
    }
}
