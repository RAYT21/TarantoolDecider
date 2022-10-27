package clusterClasses;

public class OptimalStorageCluster {

    private int number;
    private double storageSize;
    private double fullDataSize;


    public void optimalSizeForStorage(double dataForStorages) {

        double optimalNumber = Math.ceil(dataForStorages / 32.);
        int optimalSize = 32;

        for (int i = 31; i >= 1; i--) {
            double tmp = Math.ceil(dataForStorages / (double) i);
            if (tmp <= optimalNumber) {
                optimalNumber = tmp;
                optimalSize = i;
            }
        }

        this.number = (int) optimalNumber;
        this.storageSize = optimalSize;
        this.fullDataSize = dataForStorages;
    }

    public int getNumber() {
        return number;
    }

    public double getStorageSize() {
        return storageSize;
    }

    public double getFullDataSize() {
        return fullDataSize;
    }

    @Override
    public String toString() {
        return "clusterClasses.OptimalStorageCluster{" +
                "number=" + number +
                ", storageSize=" + storageSize +
                ", fullDataSize=" + fullDataSize +
                '}';
    }
}
