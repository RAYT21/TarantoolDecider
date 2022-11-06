package clusterCreator.instances;

public class TarantoolCore {
    private static final double ram = 0.25;
    private static double core;

    public static double getRam() {
        return ram;
    }

    public static double getCore() {
        return core;
    }

    public static void setCore(double core) {
        TarantoolCore.core = core;
    }
}
