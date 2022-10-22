package server;

public class ServerInfo {
    private double freeRAM;
    private double freeCore;
    private int storagesNumber;
    private int routersNumber;
    private boolean nginxAvailability = false;
    private boolean etcdAvailability = false;
    private boolean coreAvailability = false;

    private double hddMemory;

    public ServerInfo(double freeRAM, double freeCore, int storagesNumber, int routersNumber) {
        this.freeRAM = freeRAM;
        this.freeCore  = freeCore;
        this.storagesNumber = storagesNumber;
        this.routersNumber = routersNumber;
    }

    public ServerInfo(ServerInfo serverInfo){
        this.freeRAM = serverInfo.getFreeRAM();
        this.freeCore = serverInfo.getFreeProcess();
        this.storagesNumber = serverInfo.getStoragesNumber();
        this.routersNumber = serverInfo.getRoutersNumber();
        this.nginxAvailability = serverInfo.isNginxAvailability();;
        this.etcdAvailability = serverInfo.isEtcdAvailability();
        this.coreAvailability = serverInfo.isCoreAvailability();
        this.hddMemory = serverInfo.getHddMemory();
    }

    public double getHddMemory() {
        return hddMemory;
    }

    public void setHddMemory(double hddMemory) {
        this.hddMemory = hddMemory;
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

    public void addRoutersNumber() {
        this.routersNumber++;
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

    public void setNginxAvailability() {
        this.nginxAvailability = true;
    }

    public boolean isEtcdAvailability() {
        return etcdAvailability;
    }

    public void setEtcdAvailability() {
        this.etcdAvailability = true;
    }

    public boolean isCoreAvailability() {
        return coreAvailability;
    }

    public void setCoreAvailability() {
        this.coreAvailability = true;
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
