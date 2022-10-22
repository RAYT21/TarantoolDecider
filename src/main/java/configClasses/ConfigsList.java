package configClasses;

import instances.Nginx;
import instances.TarantoolCore;

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
            new Config(28,4)


    };

    public static Config findBestServerForLessInstances( double ramLess, double coreLess ){
        Config bestConfig = ConfigsList.CONFIGS[0];
        for (Config cfg : ConfigsList.CONFIGS) {
            double tmpRAM = cfg.getRAM() - (cfg.getRAM()*0.1 > 10 ? 10 : cfg.getRAM()*0.1) - Nginx.getRam() ;
            double tmpCore = cfg.getCORE() - Nginx.getCore();  //core, nginx,
            if ( tmpRAM  >= ramLess && tmpCore >= coreLess && bestConfig.getPRICE() >= cfg.getPRICE()){
                bestConfig = cfg;
            }

        }
        return bestConfig;
    }





}
