package clusterClasses;

import java.util.ArrayList;
import java.util.List;

public class ClusterList {


    // сразу выбирать лучший кластер,а не хранить их всех
    private Cluster clusterList;

    public static int variation = 0;

    private static ClusterList INSTANCE;

    private ClusterList() {}

    public static ClusterList getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClusterList();
        }
        return INSTANCE;
    }

    public void addClusterToList(Cluster cluster){
        /*System.out.println(variation);*/
        variation++;
        if(this.clusterList == null) {
            this.clusterList = cluster;
            return;
        }
        if (this.clusterList.getPrice() > cluster.getPrice()){
            this.clusterList = cluster;
        }

    }

    public Cluster getClusterList() {
        return clusterList;
    }




}
