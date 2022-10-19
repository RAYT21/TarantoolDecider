package clusterClasses;

import java.util.ArrayList;
import java.util.List;

public class ClusterList {


    private List<Cluster> clusterList;

    private static ClusterList INSTANCE;

    private ClusterList() {}

    public static ClusterList getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClusterList();
            INSTANCE.clusterList = new ArrayList<>();
        }
        return INSTANCE;
    }

    public void addClusterToList(Cluster cluster){
        this.clusterList.add(cluster);
    }

    public List<Cluster> getClusterList() {
        return clusterList;
    }

    public Cluster choiceBestCluster(){
        Cluster result = this.getClusterList().get(0);
        double minPrice = result.getPrice();
        for (Cluster cluster : this.getClusterList()) {
            double tmpPrice = cluster.getPrice();
            if (tmpPrice <= minPrice){
                result = cluster;
                minPrice = tmpPrice;
            }
        }
        return result;
    }


}
