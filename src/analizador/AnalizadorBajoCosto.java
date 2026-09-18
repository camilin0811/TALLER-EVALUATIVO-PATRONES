package analizador;

public class AnalizadorBajoCosto implements Analizador {

    private static final double HUMIDITY_THRESHOLD = 75.0;
    private static final double HUMIDITY_COEFFICIENT = 0.012;

    private final double maxRange;

    public AnalizadorBajoCosto(double maxRange) {
        this.maxRange = maxRange;
    }

    @Override
    public double correct(double rawValue, double relativeHumidity) {
        if (relativeHumidity > HUMIDITY_THRESHOLD) {
            return rawValue / (1 + HUMIDITY_COEFFICIENT * (relativeHumidity - HUMIDITY_THRESHOLD));
        }
        return rawValue;
    }

    @Override
    public double getMaxRange() {
        return maxRange;
    }

    @Override
    public double getUncertainty() {
        return 0.20;
    }

    @Override
    public String getDescription() {
        return "low cost sensor with humidity correction";
    }
}
