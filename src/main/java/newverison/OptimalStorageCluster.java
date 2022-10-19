package newverison;

public class OptimalStorageCluster {

    private int number;
    private double storageSize;
    private double fullDataSize;


    public void optimalSizeForStorage(double dataForStorages){

        double optimal = Math.ceil(dataForStorages/32.);
        int optimalSize = 32;

        for (int i = 31; i >= 2; i--) {
            double tmp = Math.ceil(dataForStorages/(double)i);
            if (tmp <= optimal){
                optimal = tmp;
                optimalSize = i;
            }
        }

        this.number = optimalSize;
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
}
