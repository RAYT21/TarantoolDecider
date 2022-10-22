package configClasses;

public class Config {

    private final double RAM;
    private final double CORE;
    private final double PRICE;

    public Config(double RAM, double CORE) {
        this.RAM = RAM;
        this.CORE = CORE;
        this.PRICE = CORE*5.51 + RAM*2.15 + 30*0.24;
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
        return "configClasses.Config{" +
                "RAM=" + RAM +
                ", CORE=" + CORE +
                ", PRICE=" + PRICE +
                '}';
    }
}
