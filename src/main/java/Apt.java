import clusterClasses.Cluster;
import clusterClasses.ClusterList;
import clusterClasses.OptimalStorageCluster;
import configClasses.Config;
import configClasses.ConfigsList;
import instances.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


// ЕТСD учитывается только в определнных сферах (елси не пром) done(tol'co nado uznat' v kakih)

// сделать флаг для етсд, который включает надобность его или ненадобность done

// исправить распределение коров, в оптимальном выборе конфигураций done

// учет размера хдд и стоимости от него done

// учесть в выборе кластера ядра необходимые для размещения основных конфигов done

// change all methods toString


// sdelat' vibor luchey configuracii na cled shag, a ne generaciya kagdogo varianta

// razobrat'sa chto proishodit v routerah i pochemu ih tak mnogo done

// refactoring

public class Apt {
    static Map<String, Double> coreDep = new HashMap<>() {{
        put("prod",1.5);
        put("dev",0.5);
    }};


    public void run(String[] args) {

        double acceptableAmountOfData = 150;  //Integer.parseInt(args[0]);
        double requestPerSecond = 20000;      //Integer.parseInt(args[1]);
        double routerVelocity = 2000;        //Integer.parseInt(args[2]);
        double replicationLevel = 1;         //Integer.parseInt(args[3]);
        double percent = 0.80;
        double coreDepend = 1.5; //coreDep.get(args[4]) != null ? coreDep.get(args[4]) : 1.5;

        if (true /*list.cont(args[4])*/){
            ETCD.setFlagNeed();
        }

        // init core
        Nginx.setCore(coreDepend);
        ETCD.setCore(coreDepend);
        Router.setCore(coreDepend);
        Storage.setCore(coreDepend);
        TarantoolCore.setCore(coreDepend);



        //routers
        int routerNumber = (int) Math.ceil(requestPerSecond/routerVelocity);

        // 100% amount of data
        double dataForStorages = acceptableAmountOfData/percent;



        OptimalStorageCluster optimal = new OptimalStorageCluster();

        optimal.optimalSizeForStorage(dataForStorages);

        System.out.println(optimal);


        Config[] conf = listOfSuitableConfigurations(optimal.getStorageSize());
        for (Config cfg : conf) {
            new Cluster(optimal,routerNumber).createClusterVariation(conf,0);
        }

        Cluster cluster = ClusterList.getInstance().choiceBestCluster();

        System.out.println("Result price: " + cluster.getPrice() +" \nResult cluster: \n" + cluster);

    }



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

}
