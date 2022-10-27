package clusterClasses;

import configClasses.Config;
import configClasses.ConfigsList;
import instances.*;
import server.Server;
import server.ServerConfig;
import server.ServerInfo;
import server.ServerInstances;

import java.util.ArrayList;
import java.util.List;

public class Cluster {
    private List<Server> servers;

    private double dataForStorages;


    private double fullClusterData;
    private static double storageSize;

    private final int routersNumber;
    private final int storagesNumber;

    private int lessRouterIns;
    private int lessStoragesIns;


    private double hddMemory;
    private double price;

    private int clusterGroup = 0;

    public Cluster(OptimalStorageCluster optimal, int routersNumber) {
        this.servers = new ArrayList<>();
        this.storageSize = optimal.getStorageSize();
        this.dataForStorages = optimal.getFullDataSize();
        this.storagesNumber = optimal.getNumber();
        this.routersNumber = routersNumber;
        this.lessStoragesIns = this.storagesNumber;
        this.lessRouterIns = this.routersNumber;
        this.price =0;
    }

    public Cluster(Cluster cluster){
        this.servers = pullNewList(cluster.getServers());
        this.storageSize = cluster.getStorageSize();
        this.dataForStorages = cluster.getDataForStorages();
        this.storagesNumber = cluster.getStoragesNumber();
        this.routersNumber = cluster.getRoutersNumber();
        this.lessStoragesIns = cluster.getLessStoragesIns();
        this.lessRouterIns = cluster.getLessRouterIns();
        this.fullClusterData = cluster.getFullClusterData();
        this.price = cluster.getPrice();
    }

    private List<Server> pullNewList(List<Server> servers){
        List<Server> serverList = new ArrayList<>();
        for (Server server: servers) {
            serverList.add(new Server(server));
        }
        return serverList;
    }



    public void addServer(Server server){
        this.servers.add(server);
    }



    public void createClusterVariationRecursively(Config[] configs, int id){
        if(lessStoragesIns > 0){

            Config cfg = ConfigsList.nextStepConfig(configs,lessRouterIns,lessStoragesIns,storageSize);

            storageImplementationOnNewServer(cfg, id);

            this.createClusterVariationRecursively(configs, id+1);
        }
        else {
            if (lessRouterIns > 0){
                this.routerImplementation();
            }
            if(ETCD.getFlagNeed()) {
                etcdImplementation(id);
            }
            setPrice();
            ClusterList.getInstance().addClusterToList(this);
        }
    }

    public void createClusterVariationWithNextStepMethod(Config[] configs, int id){
        if(lessStoragesIns > 0){

            Config cfg = ConfigsList.nextStepConfig(configs,lessRouterIns,lessStoragesIns,storageSize);

            storageImplementationOnNewServer(cfg, id);

            this.createClusterVariationWithNextStepMethod(configs, id+1);
        }
        else {
            if (lessRouterIns > 0){
                this.routerImplementation();
            }
            if(ETCD.getFlagNeed()) {
                etcdImplementation(id);
            }
            setPrice();
            ClusterList.getInstance().addClusterToList(this);
        }
    }

    private void storageImplementationOnNewServer(Config cfg, int id){
        double tmpRAM = cfg.getRAM();
        double tmpCORE = cfg.getCORE();

        double ramLessServer = tmpRAM - (tmpRAM*0.1 >= 10 ? 10 : tmpRAM*0.1) - Nginx.getRam() - (lessRouterIns > 0 ? Router.getRam() : 0) -TarantoolCore.getRam();
        double coreLessServer = tmpCORE - Nginx.getCore() - (lessRouterIns > 0 ? Router.getCore() : 0) -TarantoolCore.getCore();
        int storageBoxes = (int)Math.floor(ramLessServer/storageSize);
        ServerInstances serverInstances = new ServerInstances();

        if(this.lessRouterIns > 0) {
            serverInstances.addInstance(new Router(this.routersNumber-this.lessRouterIns));
            this.lessRouterIns--;
        }

        int k = 0;
        while(lessStoragesIns > 0 && k < storageBoxes && coreLessServer >= Storage.getCore()){
            serverInstances.addInstance(new Storage(this.storagesNumber-this.lessStoragesIns, storageSize));
            this.lessStoragesIns--;
            coreLessServer-=Storage.getCore();
            ramLessServer-=storageSize;
            k++;
        }

        ServerInfo serverInfo = new ServerInfo(ramLessServer, coreLessServer, k, 1);

        serverInfo.setCoreAvailability();
        serverInfo.setNginxAvailability();

        Server server = new Server(id, new ServerConfig(cfg), serverInfo, serverInstances);

        this.addServer(server);
        this.fullClusterData+=tmpRAM;
    }


    private void setHddMemory(Server server) {
        server.hddSpace();
        server.addHddCost();
        this.hddMemory += server.getServerInfo().getHddMemory().getFullMemo();
    }

    private void setPrice() {
        for (Server server: servers) {
            setHddMemory(server);
            this.price+=server.getPrice();
        }
    }

    private void etcdImplementation(int id){
        findServerForETCD();
        if (ETCD.getFlagNeed()){
            addServer(createServerForETCD());
        }
    }

    private void findServerForETCD(){
        for (Server server: servers) {
            double tmpRAM = server.getServerInfo().getFreeRAM();
            double tmpCORE = server.getServerInfo().getFreeProcess();
            if (tmpCORE >= ETCD.getCore() && tmpRAM >= ETCD.getRam()){
                server.getServerInfo().setEtcdAvailability();
                server.getServerInfo().setFreeProcess(tmpCORE - ETCD.getCore());
                server.getServerInfo().setFreeRAM(tmpRAM - ETCD.getRam());
                ETCD.setFlagNeed();
                break;
            }
        }
    }

    private Server createServerForETCD(){
        Config config = ConfigsList.findBestServerForLessInstances(ETCD.getRam(),ETCD.getCore());

        ServerInfo serverInfo = new ServerInfo(
                config.getRAM() - Nginx.getRam() - (config.getRAM()*0.1 >= 10 ? 10 : config.getRAM()*0.1) ,
                config.getCORE() - Nginx.getCore(),
                0,
                0
        );
        serverInfo.setEtcdAvailability();
        serverInfo.setNginxAvailability();

        return new Server(servers.size(), new ServerConfig(config), serverInfo, new ServerInstances());
    }

    private void routerImplementation(){
        findServersForLessRouters();
        if (lessRouterIns > 0){
            serverForRouters();
        }
    }

    private void findServersForLessRouters(){
        while(this.lessRouterIns > 0){
            int startRoundRouters = this.lessRouterIns;

            for (Server server: this.servers) {
                double tmpRAM = server.getServerInfo().getFreeRAM();
                double tmpCORE = server.getServerInfo().getFreeProcess();
                if (tmpCORE >= Router.getCore() && tmpRAM >= Router.getRam()){
                    server.getServerInstances().addInstance(new Router(this.routersNumber-this.lessRouterIns));
                    this.lessRouterIns--;
                    server.getServerInfo().addRoutersNumber();
                    server.getServerInfo().setFreeProcess(tmpCORE - Router.getCore());
                    server.getServerInfo().setFreeRAM(tmpRAM - Router.getRam());
                }
                if(this.lessRouterIns == 0) return;
            }

            if(startRoundRouters == this.lessRouterIns){
                break;
            }
        }
    }

    private void serverForRouters(){
        Config config = ConfigsList.findBestServerForLessInstances(Router.getRam()*lessRouterIns,Router.getCore()*(lessRouterIns+1));
        ServerInfo serverInfo = new ServerInfo(
                config.getRAM() -  Nginx.getRam() - (config.getRAM()*0.1 >= 10 ? 10 : config.getRAM()*0.1)  - lessRouterIns*Router.getRam(),
                config.getCORE() - Nginx.getCore() - lessRouterIns*Router.getCore(),
                0,
                lessRouterIns

        );
        serverInfo.setCoreAvailability();
        serverInfo.setNginxAvailability();

        ServerInstances serverInstances = new ServerInstances();
        for (int i = 0; i < lessRouterIns; i++) {
            serverInstances.addInstance(new Router(routersNumber-lessRouterIns));
        }

        Server server = new Server(servers.size(), new ServerConfig(config), serverInfo, serverInstances);
        this.addServer(server);
    }










    public double getPrice() {
        return price;
    }

    public List<Server> getServers() {
        return servers;
    }

    public double getDataForStorages() {
        return dataForStorages;
    }

    public double getFullClusterData() {
        return fullClusterData;
    }

    public static double getStorageSize() {
        return storageSize;
    }

    public int getRoutersNumber() {
        return routersNumber;
    }

    public int getStoragesNumber() {
        return storagesNumber;
    }

    public int getLessRouterIns() {
        return lessRouterIns;
    }

    public int getLessStoragesIns() {
        return lessStoragesIns;
    }

    @Override
    public String toString() {
        return "Cluster{" +
                "servers=" + servers +
                ", dataForStorages=" + dataForStorages +
                ", fullClusterData=" + fullClusterData +
                ", routersNumber=" + routersNumber +
                ", storagesNumber=" + storagesNumber +
                ", lessRouterIns=" + lessRouterIns +
                ", lessStoragesIns=" + lessStoragesIns +
                ", hddMemory=" + hddMemory +
                ", price=" + price +
                '}';
    }
}
