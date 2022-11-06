package clusterCreator.instances;

public class Nginx {
    private static final double ram = 0.25;
    private static double core = 1.5;

    public static void setCore(double core) {
        Nginx.core = core;
    }

    public static double getRam() {
        return ram;
    }

    public static double getCore() {
        return core;
    }
}
