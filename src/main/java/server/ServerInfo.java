package server;

public class ServerInfo {
    private double freeRAM;
    private double freeCore;
    private int storagesNumber;
    private int routersNumber;
    private boolean nginxAvailability = false;
    private boolean etcdAvailability = false;
    private boolean coreAvailability = false;

    public ServerInfo(double freeRAM, double freeCore, int storagesNumber, int routersNumber) {
        this.freeRAM = freeRAM;
        this.freeCore  = freeCore;
        this.storagesNumber = storagesNumber;
        this.routersNumber = routersNumber;
    }

    public double getFreeRAM() {
        return freeRAM;
    }

    public void setFreeRAM(double freeRAM) {
        this.freeRAM = freeRAM;
    }

    public double getFreeProcess() {
        return freeCore;
    }

    public void setFreeProcess(double freeProcess) {
        this.freeCore = freeProcess;
    }

    public int getStoragesNumber() {
        return storagesNumber;
    }

    public int getRoutersNumber() {
        return routersNumber;
    }

    public boolean isNginxAvailability() {
        return nginxAvailability;
    }

    public void setNginxAvailability(boolean nginxAvailability) {
        this.nginxAvailability = nginxAvailability;
    }

    public boolean isEtcdAvailability() {
        return etcdAvailability;
    }

    public void setEtcdAvailability(boolean etcdAvailability) {
        this.etcdAvailability = etcdAvailability;
    }

    public boolean isCoreAvailability() {
        return coreAvailability;
    }

    public void setCoreAvailability(boolean coreAvailability) {
        this.coreAvailability = coreAvailability;
    }

    @Override
    public String toString() {
        return "ServerInfo{" +
                "freeRAM=" + freeRAM +
                ", freeCore=" + freeCore +
                ", storagesNumber=" + storagesNumber +
                ", routersNumber=" + routersNumber +
                ", nginxAvailability=" + nginxAvailability +
                ", etcdAvailability=" + etcdAvailability +
                ", coreAvailability=" + coreAvailability +
                '}';
    }
}
