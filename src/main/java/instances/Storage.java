package instances;

public class Storage {

    private int id;
    private double ram;
    private static double core;

    public Storage(int id, double ram) {
        this.id = id;
        this.ram = ram;
    }

    public int getId() {
        return id;
    }

    public double getRam() {
        return ram;
    }

    public static double getCore() {
        return core;
    }

    public  static void setCore(double core) {
        Storage.core = core;
    }

    @Override
    public String toString() {
        return "Storage{" +
                "id=" + id +
                ", ram=" + ram +
                ", core=" + core +
                '}';
    }
}
