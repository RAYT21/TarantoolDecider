package newverison;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static final double CORE = 1.5;
    public static final double INSTANCE_RAM = 0.25;

    public static void main(String[] args) {

        double acceptableAmountOfData = 2;  //Integer.parseInt(args[0]);
        double requestPerSecond = 12000;       //Integer.parseInt(args[1]);
        double routerVelocity = 2000;         //Integer.parseInt(args[2]);
        double replicationLevel = 1;          //Integer.parseInt(args[3]);

        //routers
        int routerNumber = (int) Math.ceil(requestPerSecond/routerVelocity);

        // 100% amount of data
        double dataForStorages = 151;//acceptableAmountOfData/0.8;



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
            double tmp = cfg.getRAM() - 14; // 14 ЛИ? может быть 10% но не выше определенного значения
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
