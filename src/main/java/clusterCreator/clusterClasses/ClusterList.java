package clusterCreator.clusterClasses;

public class ClusterList {


    // сразу выбирать лучший кластер,а не хранить их всех
    private Cluster clusterList;

    private static ClusterList INSTANCE;

    private ClusterList() {
    }

    public static ClusterList getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClusterList();
        }
        return INSTANCE;
    }

    public void addClusterToList(Cluster cluster) {
        this.clusterList = cluster;
    }

    public Cluster getClusterList() {
        return clusterList;
    }

    public void addClusterToListInRecursivelyMethod(Cluster cluster) {
        if (this.clusterList == null) {
            this.clusterList = cluster;
            return;
        }
        if (this.clusterList.getPrice() > cluster.getPrice()) {
            this.clusterList = cluster;
        }

    }


}
