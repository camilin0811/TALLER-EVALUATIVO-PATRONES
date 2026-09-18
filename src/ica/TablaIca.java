package ica;

import java.util.List;
import java.util.Map;

public final class TablaIca {

    private static final Map<String, List<Breakpoint>> TABLE = Map.of(
            "PM2.5", List.of(
                    new Breakpoint(0.0, 12.0, 0, 50, "Buena"),
                    new Breakpoint(12.1, 37.4, 51, 100, "Aceptable"),
                    new Breakpoint(37.5, 55.4, 101, 150, "Danina a grupos sensibles"),
                    new Breakpoint(55.5, 150.4, 151, 200, "Danina a la salud"),
                    new Breakpoint(150.5, 250.4, 201, 300, "Muy danina")),
            "O3", List.of(
                    new Breakpoint(0, 54, 0, 50, "Buena"),
                    new Breakpoint(55, 70, 51, 100, "Aceptable"),
                    new Breakpoint(71, 85, 101, 150, "Danina a grupos sensibles"),
                    new Breakpoint(86, 105, 151, 200, "Danina a la salud"),
                    new Breakpoint(106, 200, 201, 300, "Muy danina")),
            "NO2", List.of(
                    new Breakpoint(0, 53, 0, 50, "Buena"),
                    new Breakpoint(54, 100, 51, 100, "Aceptable"),
                    new Breakpoint(101, 360, 101, 150, "Danina a grupos sensibles"),
                    new Breakpoint(361, 649, 151, 200, "Danina a la salud"),
                    new Breakpoint(650, 1249, 201, 300, "Muy danina")));

    private TablaIca() {
    }

    public static ResultadoIca compute(String pollutant, double concentration) {
        List<Breakpoint> segments = TABLE.get(pollutant);
        if (segments == null) {
            throw new IllegalArgumentException("unknown pollutant: " + pollutant);
        }
        for (Breakpoint segment : segments) {
            if (segment.contains(concentration)) {
                int index = (int) Math.round(segment.interpolate(concentration));
                return new ResultadoIca(pollutant, concentration, index, segment.category());
            }
        }
        Breakpoint last = segments.get(segments.size() - 1);
        return new ResultadoIca(pollutant, concentration, last.indexHigh(), last.category());
    }

    public static boolean isKnown(String pollutant) {
        return TABLE.containsKey(pollutant);
    }
}
