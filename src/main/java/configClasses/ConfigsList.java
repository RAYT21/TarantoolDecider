package configClasses;

public class ConfigsList {

    public static final Config[] CONFIGS = new Config[]{
            new Config(128,16),
            new Config(64,8),
            new Config(32,8),
            new Config(32,8),
            new Config(16,8),
            new Config(8,4),
            new Config(8,8),
            new Config(16,4),


    };

    /*public Config findServerForLessInstances(int lessInsNumb, double ramIns, double coreIns ){
        Config configOptimal = CONFIGS[0];
        for (int i = 1; i < CONFIGS.length; i++) {
            Config tmpConfig = CONFIGS[i];
            if ()
        }
    }*/




}
