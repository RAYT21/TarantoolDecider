package instances;

public class Storage {

    private int id;
    private double ram;
    public final double core = 1.5;

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

    @Override
    public String toString() {
        return "Storage{" +
                "id=" + id +
                ", ram=" + ram +
                ", core=" + core +
                '}';
    }
}
