package configuracion;

public class TareaMantenimiento implements Cloneable {

    private String name;
    private String frequency;

    public TareaMantenimiento(String name, String frequency) {
        this.name = name;
        this.frequency = frequency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    @Override
    public TareaMantenimiento clone() {
        return new TareaMantenimiento(name, frequency);
    }

    @Override
    public String toString() {
        return name + " (" + frequency + ")";
    }
}
