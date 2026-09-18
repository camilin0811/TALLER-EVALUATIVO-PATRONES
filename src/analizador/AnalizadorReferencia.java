package analizador;

public class AnalizadorReferencia implements Analizador {

    private final double calibrationFactor;
    private final double maxRange;

    public AnalizadorReferencia(double calibrationFactor, double maxRange) {
        this.calibrationFactor = calibrationFactor;
        this.maxRange = maxRange;
    }

    @Override
    public double correct(double rawValue, double relativeHumidity) {
        return rawValue * calibrationFactor;
    }

    @Override
    public double getMaxRange() {
        return maxRange;
    }

    @Override
    public double getUncertainty() {
        return 0.02;
    }

    @Override
    public String getDescription() {
        return String.format("reference method analyzer (factor %.2f)", calibrationFactor);
    }
}
