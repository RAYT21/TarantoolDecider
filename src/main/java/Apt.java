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


// refactoring

public class Apt {
    static Map<String, Double> coreDep = new HashMap<>() {{
        put("prod",1.5);
        put("dev",0.5);
    }};

    static List<String> list = new ArrayList<>(){{
        add("prod");
    }};


    public void run(String[] args) {

        double acceptableAmountOfData = 400;  //Integer.parseInt(args[0]);
        double requestPerSecond = 2000;      //Integer.parseInt(args[1]);
        double routerVelocity = 2000;        //Integer.parseInt(args[2]);
        double replicationLevel = 1;         //Integer.parseInt(args[3]);
        double percent = 0.80; // Double.parseDouble(args[4])
        double coreDepend = coreDep.get("prod") != null ? coreDep.get("prod") : 1.5;

        if (list.contains("")){
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
            System.out.printf(cfg+" ");
        }

        new Cluster(optimal,routerNumber).createClusterVariation(conf,0);


        Cluster cluster = ClusterList.getInstance().getClusterList();

        System.out.println("Result price: " + cluster.getPrice() +" \nResult cluster: \n" + cluster);

        System.out.println(ClusterList.variation);

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
