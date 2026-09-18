package puesto;

import analizador.Analizador;
import analizador.AnalizadorOpticoPortatil;
import configuracion.ConfiguracionEstacion;

public class PuestoMovil extends PuestoDeMonitoreo {

    public PuestoMovil(String name, ConfiguracionEstacion configuration) {
        super(name, configuration);
    }

    @Override
    protected Analizador crearAnalizador(String contaminante) {
        return new AnalizadorOpticoPortatil(maxRangeFor(contaminante));
    }

    @Override
    public String getType() {
        return "MOVIL";
    }

    private double maxRangeFor(String contaminante) {
        return switch (contaminante) {
            case "PM2.5" -> 300.0;
            case "O3" -> 250.0;
            case "NO2" -> 1000.0;
            default -> 800.0;
        };
    }
}
