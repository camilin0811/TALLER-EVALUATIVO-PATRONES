package app;

import puesto.LecturaCruda;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class GeneradorLecturas {

    private static final int HOURS = 24;

    private GeneradorLecturas() {
    }

    public static List<LecturaCruda> day(long seed, List<String> pollutants,
                                         double baseValue, double humidity,
                                         int invalidReadings) {
        return build(seed, pollutants, baseValue, humidity, invalidReadings, null, HOURS);
    }

    public static List<LecturaCruda> partialDay(long seed, List<String> pollutants,
                                                double baseValue, double humidity,
                                                String scarcePollutant, int scarceHours) {
        return build(seed, pollutants, baseValue, humidity, 0, scarcePollutant, scarceHours);
    }

    private static List<LecturaCruda> build(long seed, List<String> pollutants,
                                            double baseValue, double humidity,
                                            int invalidReadings,
                                            String scarcePollutant, int scarceHours) {
        Random random = new Random(seed);
        List<LecturaCruda> readings = new ArrayList<>();
        String firstPollutant = pollutants.get(0);

        for (String pollutant : pollutants) {
            int hours = pollutant.equals(scarcePollutant) ? scarceHours : HOURS;
            double base = baseValue * scaleFor(pollutant);
            for (int hour = 0; hour < hours; hour++) {
                if (pollutant.equals(firstPollutant) && hour < invalidReadings) {
                    readings.add(new LecturaCruda(hour, pollutant, -5.0 - hour, humidity));
                    continue;
                }
                double value = base + random.nextDouble() * base * 0.35;
                readings.add(new LecturaCruda(hour, pollutant, round(value), humidity));
            }
        }
        return readings;
    }

    private static double scaleFor(String pollutant) {
        return switch (pollutant) {
            case "PM2.5" -> 1.0;
            case "O3" -> 1.4;
            case "NO2" -> 1.8;
            default -> 1.0;
        };
    }

    private static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
