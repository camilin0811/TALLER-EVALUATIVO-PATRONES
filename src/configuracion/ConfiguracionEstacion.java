package configuracion;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ConfiguracionEstacion implements Cloneable {

    private String zoneName;
    private int samplingIntervalMinutes;
    private List<String> pollutants;
    private Map<String, Double> alertThresholds;
    private List<TareaMantenimiento> maintenancePlan;

    public ConfiguracionEstacion(String zoneName, int samplingIntervalMinutes,
                                 List<String> pollutants,
                                 Map<String, Double> alertThresholds,
                                 List<TareaMantenimiento> maintenancePlan) {
        this.zoneName = zoneName;
        this.samplingIntervalMinutes = samplingIntervalMinutes;
        this.pollutants = new ArrayList<>(pollutants);
        this.alertThresholds = new LinkedHashMap<>(alertThresholds);
        this.maintenancePlan = new ArrayList<>(maintenancePlan);
    }

    @Override
    public ConfiguracionEstacion clone() {
        List<TareaMantenimiento> copiedPlan = new ArrayList<>();
        for (TareaMantenimiento task : maintenancePlan) {
            copiedPlan.add(task.clone());
        }
        return new ConfiguracionEstacion(zoneName, samplingIntervalMinutes,
                new ArrayList<>(pollutants),
                new LinkedHashMap<>(alertThresholds),
                copiedPlan);
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public int getSamplingIntervalMinutes() {
        return samplingIntervalMinutes;
    }

    public void setSamplingIntervalMinutes(int samplingIntervalMinutes) {
        this.samplingIntervalMinutes = samplingIntervalMinutes;
    }

    public List<String> getPollutants() {
        return pollutants;
    }

    public Map<String, Double> getAlertThresholds() {
        return alertThresholds;
    }

    public List<TareaMantenimiento> getMaintenancePlan() {
        return maintenancePlan;
    }

    public void setThreshold(String pollutant, double value) {
        alertThresholds.put(pollutant, value);
    }

    public void addMaintenanceTask(TareaMantenimiento task) {
        maintenancePlan.add(task);
    }

    public String summary() {
        return String.format("umbral PM2.5=%.1f tareas: %d",
                alertThresholds.getOrDefault("PM2.5", 0.0), maintenancePlan.size());
    }
}
