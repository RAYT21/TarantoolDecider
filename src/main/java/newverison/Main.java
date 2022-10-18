package newverison;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static final double CORE = 1.5;
    public static final double INSTANCE_RAM = 0.25;

    public static void main(String[] args) {

        /*double acceptableAmountOfData = 2;  //Integer.parseInt(args[0]);
        double requestPerSecond = 12000;       //Integer.parseInt(args[1]);
        double routerVelocity = 2000;         //Integer.parseInt(args[2]);
        double replicationLevel = 1;          //Integer.parseInt(args[3]);

        //routers
        int routerNumber = (int) Math.ceil(requestPerSecond/routerVelocity);*/

        // 100% amount of data
        double dataForStorages = 151;//acceptableAmountOfData/0.8;

        List<Cluster> clustersArray = new ArrayList<>();

        double optimalStorageSize = optimalSizeForStorage(dataForStorages);
        Config[] conf = listOfSuitableConfigurations(optimalStorageSize);
        for (Config cfg :
                conf) {
            System.out.println(cfg);
        }




        System.out.println(optimalStorageSize);






    }

    public static int optimalSizeForStorage(double dataForStorages){
        double optimal = Math.ceil(dataForStorages/32.);
        int optimalSize = 32;

        for (int i = 31; i >= 2; i--) {
            double tmp = Math.ceil(dataForStorages/(double)i);
            if (tmp <= optimal){
                optimal = tmp;
                optimalSize = i;
            }
        }
        System.out.println(Math.ceil(dataForStorages/(double)optimalSize));
        return optimalSize;

    }

     public static Config[] listOfSuitableConfigurations(double size){

        List<Config> list = new ArrayList<>();

        for (Config cfg : ConfigsList.CONFIGS) {
            double tmp = cfg.getRAM() - cfg.getRAM()*0.15 -10;
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
