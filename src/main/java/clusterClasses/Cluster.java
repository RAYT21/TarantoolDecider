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

    public void delServer(Server server) { this.servers.remove(server);}


    public void createClusterVariation(Config[] configs, int id){
        if(lessStoragesIns > 0){
            for (int i = 0; i < configs.length; i++) {
                Config cfg = configs[i];
                double tmpRAM = cfg.getRAM();
                double tmpCORE = cfg.getCORE();

                int storageBoxes = (int)Math.floor((tmpRAM - (tmpRAM*0.1 >= 10 ? 10 : tmpRAM*0.1) - Nginx.getRam() - Router.getRam() -TarantoolCore.getRam())/storageSize);
                ServerInstances serverInstances = new ServerInstances();

                boolean changed = false;
                if(this.lessRouterIns > 0) {
                    serverInstances.addInstance(new Router(this.routersNumber-this.lessRouterIns));
                    this.lessRouterIns--;
                    changed = true;
                }

                int k = 0;
                while(lessStoragesIns > 0 && k < storageBoxes){
                    serverInstances.addInstance(new Storage(this.storagesNumber-this.lessStoragesIns, storageSize));
                    this.lessStoragesIns--;
                    k++;
                }

                ServerInfo serverInfo = new ServerInfo(
                        tmpRAM - Nginx.getRam() - Router.getRam() - TarantoolCore.getRam() - (tmpRAM*0.1 >= 10 ? 10 : tmpRAM*0.1) - k*storageSize,
                        tmpCORE - Nginx.getCore() - Router.getCore() - TarantoolCore.getCore() - k*Storage.getCore(),
                        storageBoxes,
                        1

                );
                serverInfo.setCoreAvailability();
                serverInfo.setNginxAvailability();

                Server server = new Server(
                        id,
                        new ServerConfig(cfg),
                        serverInfo,
                        serverInstances
                );

                this.addServer(server);
                this.fullClusterData+=tmpRAM;

                for (int j = 0; j < configs.length; j++) {
                    new Cluster(this).createClusterVariation(configs, id+1);
                }


                // if not last

                this.delServer(server);
                if (changed){
                    this.lessRouterIns++;
                }
                this.lessStoragesIns +=k;
                this.fullClusterData-=tmpRAM;
            }

        }
        else {
            if (lessRouterIns > 0){
                this.routerImplementation(id);
            }
            if(ETCD.getFlagNeed()) {
                etcdImplementation(id);
            }
            setPrice();
            ClusterList.getInstance().addClusterToList(this);
        }
    }


    public void setHddMemory(Server server) {
        server.hddSpace();
        server.addHddCost();
        this.hddMemory += server.getServerInfo().getHddMemory();
    }

    public void setPrice() {
        for (Server server: servers) {
            setHddMemory(server);
            this.price+=server.getPrice();
        }
    }

    private void etcdImplementation(int id){
        boolean etcdFlag = true;
        for (Server server: servers) {
            double tmpRAM = server.getServerInfo().getFreeRAM();
            double tmpCORE = server.getServerInfo().getFreeProcess();
            if (tmpCORE >= ETCD.getCore() && tmpRAM >= ETCD.getRam()){
                server.getServerInfo().setEtcdAvailability();
                server.getServerInfo().setFreeProcess(tmpCORE - ETCD.getCore());
                server.getServerInfo().setFreeRAM(tmpRAM - ETCD.getRam());
               etcdFlag = false;
               break;
            }
        }
        if (etcdFlag){
            Config config = ConfigsList.findBestServerForLessInstances(ETCD.getRam(),ETCD.getCore());

            ServerInfo serverInfo = new ServerInfo(
                    config.getRAM() - Nginx.getRam() - (config.getRAM()*0.1 >= 10 ? 10 : config.getRAM()*0.1) ,
                    config.getCORE() - Nginx.getCore(),
                    0,
                    0

            );
            serverInfo.setEtcdAvailability();
            serverInfo.setNginxAvailability();

            Server server = new Server(
                    id,
                    new ServerConfig(config),
                    serverInfo,
                    new ServerInstances()
            );

            this.addServer(server);
        }
    }

    private void routerImplementation(int id){
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

        if (lessRouterIns > 0){
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

            Server server = new Server(
                    id,
                    new ServerConfig(config),
                    serverInfo,
                    serverInstances
            );

            this.addServer(server);
        }
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
        return "clusterClasses.Cluster{" +
                "\n\tservers=\n" + servers +
                "\n\tdataForStorages=" + dataForStorages +
                "\n\tfullClusterData=" + fullClusterData +
                "\n\tstorageSize=" + storageSize +
                "\n\troutersNumber=" + routersNumber +
                "\n\tstoragesNumber=" + storagesNumber +
                "\n\tlessRouterIns=" + lessRouterIns +
                "\n\tlessStoragesIns=" + lessStoragesIns +
                "\n\tprice=" + price +
                "\n}";
    }
}
