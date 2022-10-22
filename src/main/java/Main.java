import clusterClasses.Cluster;
import clusterClasses.ClusterList;
import clusterClasses.OptimalStorageCluster;
import configClasses.Config;
import configClasses.ConfigsList;
import instances.Nginx;
import instances.TarantoolCore;

import java.util.ArrayList;
import java.util.List;


// ЕТСD учитывается только в определнных сферах (елси не пром)

// сделать флаг для етсд, который включает надобность его или ненадобность

// исправить распределение коров, в оптимальном выборе конфигураций

// учет размера хдд и стоимости от него

// учесть в выборе кластера ядра необходимые для размещения основных конфигов


public class Main {

    public static void main(String[] args) {

        double acceptableAmountOfData = 20;  //Integer.parseInt(args[0]);
        double requestPerSecond = 2000;      //Integer.parseInt(args[1]);
        double routerVelocity = 2000;        //Integer.parseInt(args[2]);
        double replicationLevel = 1;         //Integer.parseInt(args[3]);
        double percent = 0.67;

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
            double tmp = cfg.getRAM() - (cfg.getRAM()*0.1 > 10 ? 10 : cfg.getRAM()*0.1) - Nginx.ram - TarantoolCore.ram;
            if ( tmp >= size){
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
