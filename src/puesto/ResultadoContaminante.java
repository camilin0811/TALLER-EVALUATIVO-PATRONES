package puesto;

import ica.ResultadoIca;

public record ResultadoContaminante(String pollutant, int validCount, int discardedCount,
                                    double average, boolean valid, ResultadoIca ica) {

    public static ResultadoContaminante insufficient(String pollutant, int validCount, int discardedCount) {
        return new ResultadoContaminante(pollutant, validCount, discardedCount, 0.0, false, null);
    }
}
