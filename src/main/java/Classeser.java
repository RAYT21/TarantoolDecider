public class Classeser {
    public static final double CORE = 1.5;
    public static final double ETCD_RAM = 2;
    public static final double INSTANCE_RAM = 0.25;
    public static final double RAM_RESERVATION = 10;


    private static final String[] configures = new String[]{
            "Config#1 16 128",
            "Config#2 8 64",
            "Config#3 4 32",
            "Config#4 4 16",
    };

    public static void main(String[] args) {
        initial();
        findListConfigs();
        deciderBestConfig();
    }

    public static void initial(){}

    public static void findListConfigs(){
        optimalConfig();
    }

    public static void optimalConfig(){
        configureDist();

    }

    public static void  configureDist(){
        storageDist();
        allResIsDist();
    }

    public static void storageDist(){
        routersAndOthersDist();
        allResIsDist();
    }

    public static void routersAndOthersDist(){}

    public static void allResIsDist(){}

    public static void deciderBestConfig(){}


}
