package configClasses;

import instances.Nginx;
import instances.Router;
import instances.Storage;
import instances.TarantoolCore;

import java.util.ArrayList;
import java.util.List;

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

    public static Config[] listOfSuitableConfigurations(double size){

        List<Config> list = new ArrayList<>();

        for (Config cfg : ConfigsList.CONFIGS) {
            double tmpRAM = cfg.getRAM() - (cfg.getRAM()*0.1 > 10 ? 10 : cfg.getRAM()*0.1) - Nginx.getRam() - TarantoolCore.getRam() - Router.getRam();
            double tmpCore = cfg.getCORE() - TarantoolCore.getCore() - Nginx.getCore() - Router.getCore();  //core, nginx,
            if ( tmpRAM  >= size && tmpCore >= Storage.getCore()){
                list.add(cfg);
            }
        }

        Config[] result = new Config[list.size()];
        for(int i =0;i<list.size();i++){
            result[i] = list.get(i);
        }

        return result;
    }

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

    public static Config nextStepConfig(Config[] configs,int lessRouterIns, int lessStoragesIns, double storageSize){
        Config cfg = configs[0];
        int maxTmpStorages = (int)Math.floor((cfg.getRAM() - (cfg.getRAM()*0.1 >= 10 ? 10 : cfg.getRAM()*0.1) -
                Nginx.getRam() - (lessRouterIns > 0 ? Router.getRam() : 0) - TarantoolCore.getRam())/storageSize);
        for (int i = 1; i < configs.length; i++) {

            double tmpRAM = configs[i].getRAM();
            double tmpCORE = configs[i].getCORE();

            int tmpStorages = (int)Math.floor((tmpRAM - (tmpRAM*0.1 >= 10 ? 10 : tmpRAM*0.1) - Nginx.getRam()
                    - (lessRouterIns > 0 ? Router.getRam() : 0) -TarantoolCore.getRam())/storageSize);
            double tmpFreeCores = tmpCORE - tmpStorages* Storage.getCore()- Nginx.getCore()
                    - (lessRouterIns > 0 ? Router.getCore() : 0) -TarantoolCore.getCore();
            int lessStorages = lessStoragesIns - tmpStorages;
            if(lessStorages > 0 && tmpStorages >= maxTmpStorages && tmpFreeCores >= 0){
                cfg = configs[i];
                maxTmpStorages = tmpStorages;
            }
            if (lessStorages <= 0 && tmpStorages <= maxTmpStorages  && tmpFreeCores >= 0){
                cfg = configs[i];
                maxTmpStorages = tmpStorages;
            }
        }
        return cfg;
    }





}
