package puesto;

import java.util.List;

public record ResultadoEstacion(String stationName, String type, List<ResultadoContaminante> pollutantResults,
                                int stationIndex, String category, String criticalPollutant) {

    public int validPollutantCount() {
        return (int) pollutantResults.stream().filter(ResultadoContaminante::valid).count();
    }

    public int totalValidReadings() {
        return pollutantResults.stream().mapToInt(ResultadoContaminante::validCount).sum();
    }
}
