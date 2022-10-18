package newverison.instances;

public class Router {

    private int id;
    public final double ram = 0.25;
    public final double core = 1.5;

    public Router(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    @Override
    public String toString() {
        return "Router{" +
                "id=" + id +
                ", ram=" + ram +
                ", core=" + core +
                '}';
    }
}
