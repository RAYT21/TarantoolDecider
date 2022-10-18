package newverison;

public class Config {

    private final double RAM;
    private final double CORE;
    private final double PRICE;

    public Config(double RAM, double CORE, double PRICE) {
        this.RAM = RAM;
        this.CORE = CORE;
        this.PRICE = PRICE;
    }

    public double getRAM() {
        return RAM;
    }

    public double getCORE() {
        return CORE;
    }

    public double getPRICE() {
        return PRICE;
    }

    @Override
    public String toString() {
        return "Config{" +
                "RAM=" + RAM +
                ", CORE=" + CORE +
                ", PRICE=" + PRICE +
                '}';
    }
}
