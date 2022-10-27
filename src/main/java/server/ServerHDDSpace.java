package server;

import clusterClasses.Cluster;

public class ServerHDDSpace {

    private double tarantool;
    private double snap;
    private double logs;
    private double etcd;
    private double nginx;
    private double systemMemo;

    private double fullMemo;


    public ServerHDDSpace(ServerInfo serverInfo) {
        double ram = serverInfo.getStoragesNumber() * Cluster.getStorageSize() +
                serverInfo.getRoutersNumber();
        this.tarantool = ram * 0.4;
        this.snap = ram;
        this.logs = (serverInfo.getRoutersNumber() + serverInfo.getStoragesNumber());
        this.etcd = (serverInfo.isEtcdAvailability() ? 5 : 0);
        this.nginx = (serverInfo.isNginxAvailability() ? 5 : 0);
        this.systemMemo = 30;
        this.fullMemo = this.tarantool + this.snap + this.logs + this.etcd + this.nginx + this.systemMemo;
    }

    public double getTarantool() {
        return tarantool;
    }

    public double getSnap() {
        return snap;
    }

    public double getLogs() {
        return logs;
    }

    public double getEtcd() {
        return etcd;
    }

    public double getNginx() {
        return nginx;
    }

    public double getSystemMemo() {
        return systemMemo;
    }

    public double getFullMemo() {
        return fullMemo;
    }

    @Override
    public String toString() {
        return "ServerHDDSpace{" +
                "tarantool=" + tarantool +
                ", snap=" + snap +
                ", logs=" + logs +
                ", etcd=" + etcd +
                ", nginx=" + nginx +
                ", systemMemo=" + systemMemo +
                ", fullMemo=" + fullMemo +
                '}';
    }
}
