package analizador;

public class AnalizadorOpticoPortatil implements Analizador {

    private static final double TRANSPORT_GAIN = 1.05;
    private static final double TRANSPORT_OFFSET = 0.8;

    private final double maxRange;

    public AnalizadorOpticoPortatil(double maxRange) {
        this.maxRange = maxRange;
    }

    @Override
    public double correct(double rawValue, double relativeHumidity) {
        return rawValue * TRANSPORT_GAIN + TRANSPORT_OFFSET;
    }

    @Override
    public double getMaxRange() {
        return maxRange;
    }

    @Override
    public double getUncertainty() {
        return 0.08;
    }

    @Override
    public String getDescription() {
        return "portable optical analyzer (transport drift)";
    }
}
