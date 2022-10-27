import clusterClasses.Cluster;
import clusterClasses.ClusterList;
import clusterClasses.OptimalStorageCluster;
import clusterClasses.ReplicationCluster;
import configClasses.Config;
import configClasses.ConfigsList;
import instances.*;

import java.util.ArrayList;
import java.util.List;


public class Main {
    static List<String> list = new ArrayList<>() {{
        add("prod");
        add("nt");
    }};


    public static void main(String[] args) {
        double acceptableAmountOfData = 200;  //Integer.parseInt(args[0]);
        double requestPerSecond = 2000;      //Integer.parseInt(args[1]);
        double routerVelocity = 2000;        //Integer.parseInt(args[2]); в зависимости от контура (ентерпрайз 2к, тдк 3-4к)
        int replicationLevel = 4;         //Integer.parseInt(args[3]); Может быть его от контура автоматически заполнять?
        double percent = 0.75; // Double.parseDouble(args[4])
        double coreDepend = list.contains("prod") ? 1.5 : 1;

        if (list.contains("prod")) {
            ETCD.setFlagNeed(true);
        }

        // init core
        initCoreValue(coreDepend);

        //routers
        int routerNumber = (int) Math.ceil(requestPerSecond / routerVelocity);

        // 100% amount of data
        double dataForStorages = acceptableAmountOfData / percent;

        OptimalStorageCluster optimal = new OptimalStorageCluster();
        optimal.optimalSizeForStorage(dataForStorages);

        Config[] conf = ConfigsList.listOfSuitableConfigurations(optimal.getStorageSize());

        Cluster cluster = new Cluster(optimal, routerNumber);
        if (optimal.getNumber() >= 9)
            cluster.createClusterVariationWithNextStepMethod(conf, 0);
        else
            cluster.createClusterVariationRecursively(conf, 0);

        cluster = ClusterList.getInstance().getClusterList();

        System.out.println(new ReplicationCluster(cluster, replicationLevel));


    }

    public static void initCoreValue(double coreValue) {
        Nginx.setCore(coreValue);
        ETCD.setCore(coreValue);
        Router.setCore(coreValue);
        Storage.setCore(coreValue);
        TarantoolCore.setCore(coreValue);
    }


}
