package analizador;

public interface Analizador {

    double correct(double rawValue, double relativeHumidity);

    double getMaxRange();

    double getUncertainty();

    String getDescription();

    default boolean isInRange(double rawValue) {
        return rawValue >= 0 && rawValue <= getMaxRange();
    }
}
