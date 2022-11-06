package clusterCreator.configClasses;

import clusterCreator.instances.Nginx;
import clusterCreator.instances.Router;
import clusterCreator.instances.Storage;
import clusterCreator.instances.TarantoolCore;

import java.util.ArrayList;
import java.util.List;

public class ConfigsList {

    public static final Config[] CONFIGS = new Config[]{
            new Config(16, 128),
            new Config(16, 96),
            new Config(16, 64),
            new Config(16, 32),
            new Config(16, 16),
            new Config(8, 64),
            new Config(8, 48),
            new Config(8, 32),
            new Config(8, 16),
            new Config(4, 32),
            new Config(4, 24),
            new Config(4, 16),
            new Config(4, 8),
            new Config(4, 4),
            new Config(2, 64),
            new Config(2, 16),
            new Config(2, 12),
            new Config(2, 8),
            new Config(2, 6),
            new Config(2, 4),
            new Config(2, 2),
    };

    public static Config[] listOfSuitableConfigurations(double size) {

        List<Config> list = new ArrayList<>();

        for (Config cfg : ConfigsList.CONFIGS) {
            double tmpRAM = cfg.getRAM() - (cfg.getRAM() * 0.1 > 10 ? 10 : cfg.getRAM() * 0.1) - Nginx.getRam() - TarantoolCore.getRam() - Router.getRam();
            double tmpCore = cfg.getCORE() - TarantoolCore.getCore() - Nginx.getCore() - Router.getCore();  //core, nginx,
            if (tmpRAM >= size && tmpCore >= Storage.getCore()) {
                list.add(cfg);
            }
        }

        Config[] result = new Config[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }

        return result;
    }

    public static Config findBestServerForLessInstances(double ramLess, double coreLess) {
        Config bestConfig = ConfigsList.CONFIGS[0];
        for (Config cfg : ConfigsList.CONFIGS) {
            double tmpRAM = cfg.getRAM() - (cfg.getRAM() * 0.1 > 10 ? 10 : cfg.getRAM() * 0.1) - Nginx.getRam();
            double tmpCore = cfg.getCORE() - Nginx.getCore();  //core, nginx,
            if (tmpRAM >= ramLess && tmpCore >= coreLess && bestConfig.getPRICE() >= cfg.getPRICE()) {
                bestConfig = cfg;
            }

        }
        return bestConfig;
    }

    public static Config nextStepConfig(Config[] configs, int lessRouterIns, int lessStoragesIns, double storageSize) {
        Config cfg = configs[0];
        int maxTmpStorages = (int) Math.floor((cfg.getRAM() - (cfg.getRAM() * 0.1 >= 10 ? 10 : cfg.getRAM() * 0.1) -
                Nginx.getRam() - (lessRouterIns > 0 ? Router.getRam() : 0) - TarantoolCore.getRam()) / storageSize);
        for (int i = 1; i < configs.length; i++) {

            double tmpRAM = configs[i].getRAM();
            double tmpCORE = configs[i].getCORE();

            int tmpStorages = (int) Math.floor((tmpRAM - (tmpRAM * 0.1 >= 10 ? 10 : tmpRAM * 0.1) - Nginx.getRam()
                    - (lessRouterIns > 0 ? Router.getRam() : 0) - TarantoolCore.getRam()) / storageSize);
            double tmpFreeCores = tmpCORE - tmpStorages * Storage.getCore() - Nginx.getCore()
                    - (lessRouterIns > 0 ? Router.getCore() : 0) - TarantoolCore.getCore();
            int lessStorages = lessStoragesIns - tmpStorages;
            if (lessStorages > 0 && tmpStorages >= maxTmpStorages && tmpFreeCores >= 0) {
                cfg = configs[i];
                maxTmpStorages = tmpStorages;
            }
            if (lessStorages <= 0 && tmpStorages <= maxTmpStorages && tmpFreeCores >= 0) {
                cfg = configs[i];
                maxTmpStorages = tmpStorages;
            }
        }
        return cfg;
    }


}
