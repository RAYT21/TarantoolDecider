package instances;

public class ETCD {
    private static final double ram = 2;
    private static double core;

    private static boolean flagNeed = false;

    public static double getRam() {
        return ram;
    }

    public static double getCore() {
        return core;
    }

    public static void setCore(double core) {
        ETCD.core = core;
    }

    public static void setFlagNeed(boolean flagNeed) {
        ETCD.flagNeed = flagNeed;
    }

    public static boolean getFlagNeed() {
        return flagNeed;
    }


}
