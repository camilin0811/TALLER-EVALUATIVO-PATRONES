package boletin;

import puesto.ResultadoEstacion;

public final class ImpresorBoletin {

    private ImpresorBoletin() {
    }

    public static void print(BoletinDiario bulletin) {
        System.out.println();
        System.out.println("--- BOLETIN DIARIO " + bulletin.getDate() + " ---");
        System.out.println("Emite: " + bulletin.getIssuingEntity());
        System.out.printf("%-2s %-26s %4s %-28s %s%n", "#", "ESTACION", "ICA", "CATEGORIA", "CRITICO");

        int position = 1;
        for (ResultadoEstacion result : bulletin.getStationResults()) {
            System.out.printf("%-2d %-26s %4d %-28s %s%n",
                    position++, result.stationName(), result.stationIndex(),
                    result.category(), result.criticalPollutant());
        }

        System.out.printf("ICA de la ciudad: %d (%s)%n",
                bulletin.getCityIndex(), bulletin.getCityCategory());
        System.out.printf("Cobertura de datos validos de la red: %.1f %%%n",
                bulletin.getNetworkCoverage());

        bulletin.getSensitiveGroupAdvice()
                .ifPresent(value -> System.out.println("Recomendaciones: " + value));
        bulletin.getNextDayForecast()
                .ifPresent(value -> System.out.println("Pronostico manana: " + value));
        bulletin.getMissingDataAnnex()
                .ifPresent(value -> System.out.println("Anexo datos faltantes: " + value));
        bulletin.getPublicationOwner()
                .ifPresent(value -> System.out.println("Responsable: " + value));
        bulletin.getMethodologyNote()
                .ifPresent(value -> System.out.println("Nota metodologica: " + value));
    }
}
