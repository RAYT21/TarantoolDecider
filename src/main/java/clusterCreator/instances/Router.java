package clusterCreator.instances;

public class Router {

    private int id;
    private static final double ram = 0.25;
    private static double core = 1.5;

    public Router(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static double getRam() {
        return ram;
    }

    public static double getCore() {
        return core;
    }

    public static void setCore(double core) {
        Router.core = core;
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
