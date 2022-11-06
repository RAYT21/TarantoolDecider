package clusterCreator.clusterClasses;

import clusterCreator.server.Server;

import java.util.ArrayList;
import java.util.List;

public class ReplicationCluster {

    private List<Cluster> replCluster = new ArrayList<>();

    private double fullPrice = 0;

    public ReplicationCluster(Cluster tooper, int replicationLevel) {
        for (int i = 0; i < replicationLevel; i++) {
            Cluster cluster = new Cluster(tooper);
            int k = cluster.getServers().size();
            cluster.setClusterGroup(i);
            for (Server server : cluster.getServers()) {
                server.setId(server.getId() + i * k);
            }
            replCluster.add(cluster);
            fullPrice += cluster.getPrice();
        }
        if(replicationLevel > 1){
            Cluster tmpETCD = new Cluster();
            replCluster.add(tmpETCD);
            fullPrice += tmpETCD.getPrice();
        }

    }

    public List<Cluster> getReplCluster() {
        return replCluster;
    }

    public double getFullPrice() {
        return fullPrice;
    }

    @Override
    public String toString() {
        return "ReplicationCluster{" +
                "replCluster=" + replCluster +
                ", \nfullPrice=" + fullPrice +
                "\n}";
    }
}
