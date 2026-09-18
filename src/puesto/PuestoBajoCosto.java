package puesto;

import analizador.Analizador;
import analizador.AnalizadorBajoCosto;
import configuracion.ConfiguracionEstacion;

public class PuestoBajoCosto extends PuestoDeMonitoreo {

    public PuestoBajoCosto(String name, ConfiguracionEstacion configuration) {
        super(name, configuration);
    }

    @Override
    protected Analizador crearAnalizador(String contaminante) {
        return new AnalizadorBajoCosto(maxRangeFor(contaminante));
    }

    @Override
    public String getType() {
        return "BAJO_COSTO";
    }

    @Override
    protected boolean tracesCorrection() {
        return true;
    }

    private double maxRangeFor(String contaminante) {
        return switch (contaminante) {
            case "PM2.5" -> 200.0;
            case "O3" -> 150.0;
            case "NO2" -> 500.0;
            default -> 400.0;
        };
    }
}
