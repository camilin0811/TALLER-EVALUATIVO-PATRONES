package ica;

public record Breakpoint(double concentrationLow, double concentrationHigh,
                         int indexLow, int indexHigh, String category) {

    public boolean contains(double concentration) {
        return concentration >= concentrationLow && concentration <= concentrationHigh;
    }

    public double interpolate(double concentration) {
        return ((indexHigh - indexLow) / (concentrationHigh - concentrationLow))
                * (concentration - concentrationLow) + indexLow;
    }
}
