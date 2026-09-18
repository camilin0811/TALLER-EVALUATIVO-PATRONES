package boletin;

import puesto.ResultadoEstacion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class BoletinDiario {

    private static final List<String> SEVERE_CATEGORIES =
            List.of("Danina a la salud", "Muy danina");

    private final LocalDate date;
    private final String issuingEntity;
    private final List<ResultadoEstacion> stationResults;
    private final int cityIndex;
    private final String cityCategory;
    private final String sensitiveGroupAdvice;
    private final String nextDayForecast;
    private final String missingDataAnnex;
    private final String publicationOwner;
    private final String methodologyNote;
    private final double networkCoverage;

    private BoletinDiario(Builder builder) {
        this.date = builder.date;
        this.issuingEntity = builder.issuingEntity;
        this.stationResults = Collections.unmodifiableList(new ArrayList<>(builder.stationResults));
        this.cityIndex = builder.cityIndex;
        this.cityCategory = builder.cityCategory;
        this.sensitiveGroupAdvice = builder.sensitiveGroupAdvice;
        this.nextDayForecast = builder.nextDayForecast;
        this.missingDataAnnex = builder.missingDataAnnex;
        this.publicationOwner = builder.publicationOwner;
        this.methodologyNote = builder.methodologyNote;
        this.networkCoverage = builder.networkCoverage;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getIssuingEntity() {
        return issuingEntity;
    }

    public List<ResultadoEstacion> getStationResults() {
        return stationResults;
    }

    public int getCityIndex() {
        return cityIndex;
    }

    public String getCityCategory() {
        return cityCategory;
    }

    public double getNetworkCoverage() {
        return networkCoverage;
    }

    public Optional<String> getSensitiveGroupAdvice() {
        return Optional.ofNullable(sensitiveGroupAdvice);
    }

    public Optional<String> getNextDayForecast() {
        return Optional.ofNullable(nextDayForecast);
    }

    public Optional<String> getMissingDataAnnex() {
        return Optional.ofNullable(missingDataAnnex);
    }

    public Optional<String> getPublicationOwner() {
        return Optional.ofNullable(publicationOwner);
    }

    public Optional<String> getMethodologyNote() {
        return Optional.ofNullable(methodologyNote);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private LocalDate date;
        private String issuingEntity;
        private final List<ResultadoEstacion> stationResults = new ArrayList<>();
        private Integer cityIndex;
        private String cityCategory;
        private double networkCoverage;
        private String sensitiveGroupAdvice;
        private String nextDayForecast;
        private String missingDataAnnex;
        private String publicationOwner;
        private String methodologyNote;

        public Builder date(LocalDate date) {
            this.date = date;
            return this;
        }

        public Builder issuingEntity(String issuingEntity) {
            this.issuingEntity = issuingEntity;
            return this;
        }

        public Builder stationResults(List<ResultadoEstacion> results) {
            this.stationResults.clear();
            this.stationResults.addAll(results);
            return this;
        }

        public Builder cityIndex(int cityIndex) {
            this.cityIndex = cityIndex;
            return this;
        }

        public Builder cityCategory(String cityCategory) {
            this.cityCategory = cityCategory;
            return this;
        }

        public Builder networkCoverage(double networkCoverage) {
            this.networkCoverage = networkCoverage;
            return this;
        }

        public Builder sensitiveGroupAdvice(String advice) {
            this.sensitiveGroupAdvice = advice;
            return this;
        }

        public Builder nextDayForecast(String forecast) {
            this.nextDayForecast = forecast;
            return this;
        }

        public Builder missingDataAnnex(String annex) {
            this.missingDataAnnex = annex;
            return this;
        }

        public Builder publicationOwner(String owner) {
            this.publicationOwner = owner;
            return this;
        }

        public Builder methodologyNote(String note) {
            this.methodologyNote = note;
            return this;
        }

        public BoletinDiario build() {
            if (date == null) {
                throw new IllegalStateException("falta la fecha del boletin");
            }
            if (issuingEntity == null || issuingEntity.isBlank()) {
                throw new IllegalStateException("falta la entidad emisora");
            }
            if (cityIndex == null) {
                throw new IllegalStateException("falta el ICA maximo de la ciudad");
            }
            if (cityCategory == null || cityCategory.isBlank()) {
                throw new IllegalStateException("falta la categoria de la ciudad");
            }
            if (stationResults.isEmpty()) {
                throw new IllegalStateException("la lista de estaciones esta vacia");
            }
            if (SEVERE_CATEGORIES.contains(cityCategory)
                    && (sensitiveGroupAdvice == null || sensitiveGroupAdvice.isBlank())) {
                throw new IllegalStateException(
                        "categoria '" + cityCategory + "' exige recomendaciones a poblacion sensible");
            }
            return new BoletinDiario(this);
        }
    }
}
