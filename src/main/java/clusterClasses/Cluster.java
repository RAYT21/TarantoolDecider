package clusterClasses;

import configClasses.Config;
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
    private double storageSize;

    private final int routersNumber;
    private final int storagesNumber;

    private int lessRouterIns;
    private int lessStoragesIns;


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
        this.servers = new ArrayList<>(cluster.getServers());
        this.storageSize = cluster.getStorageSize();
        this.dataForStorages = cluster.getDataForStorages();
        this.storagesNumber = cluster.getStoragesNumber();
        this.routersNumber = cluster.getRoutersNumber();
        this.lessStoragesIns = cluster.getLessStoragesIns();
        this.lessRouterIns = cluster.getLessRouterIns();
        this.fullClusterData = cluster.getFullClusterData();
        this.price = cluster.getPrice();
    }



    public void addServer(Server server){
        this.servers.add(server);
    }

    public void delServer(Server server) { this.servers.remove(server);}


    public void createClusterVariation(Config[] configs, int id){
        if(lessStoragesIns > 0){
            for (Config cfg: configs) {
                double tmpRAM = cfg.getRAM();
                double tmpCORE = cfg.getCORE();
                double tmpPRICE = cfg.getPRICE();


                int storageBoxes = (int)Math.floor((tmpRAM - Nginx.ram - Router.ram - TarantoolCore.ram - 10)/storageSize);
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
                        tmpRAM - Nginx.ram - Router.ram - TarantoolCore.ram - 10 - k*storageSize,
                        tmpCORE - Nginx.core - Router.core - TarantoolCore.core - k*1.5,
                        storageBoxes,
                        1
                );
                serverInfo.setCoreAvailability(true);
                serverInfo.setNginxAvailability(true);

                Server server = new Server(
                        id,
                        new ServerConfig(cfg),
                        serverInfo,
                        serverInstances
                );

                this.addServer(server);
                this.fullClusterData+=tmpRAM;
                this.price += tmpPRICE;

                for (int i = 0; i < configs.length; i++) {
                    new Cluster(this).createClusterVariation(configs, id+1);
                }

                this.delServer(server);
                if (changed){
                    this.lessRouterIns++;
                }
                this.lessStoragesIns +=k;
                this.fullClusterData-=tmpRAM;
                this.price -= tmpPRICE;

            }

        }
        else {
            if (lessRouterIns > 0){
                this.routerImplementation();
            }
            etcdImplementation();
            ClusterList.getInstance().addClusterToList(this);
        }
    }

    private void etcdImplementation(){
        boolean etcdFlag = false;
        for (Server server: servers) {
            ServerInfo serverInfo = server.getServerInfo();
            double tmpRAM = serverInfo.getFreeRAM();
            double tmpCORE = serverInfo.getFreeProcess();
            if (tmpCORE >= ETCD.core && tmpRAM >= ETCD.ram){
               serverInfo.setEtcdAvailability(true);
               serverInfo.setFreeProcess(tmpCORE - ETCD.core);
               serverInfo.setFreeRAM(tmpRAM - ETCD.ram);
               etcdFlag = true;
               break;
            }
        }
        if (etcdFlag == false){
            //minimal config for etcd
            //add etcd to server
            //add server to list
        }
    }

    private void routerImplementation(){
        boolean flag = true;
        while(flag){
            int startRoundRouters = lessRouterIns;

            for (Server server: servers) {
                if(lessRouterIns == 0) break;

                ServerInfo serverInfo = server.getServerInfo();
                ServerInstances serverInstances = server.getServerInstances();
                double tmpRAM = serverInfo.getFreeRAM();
                double tmpCORE = serverInfo.getFreeProcess();
                if (tmpCORE >= Router.core && tmpRAM >= Router.ram){
                    serverInstances.addInstance(new Router(this.routersNumber-this.lessRouterIns));
                    this.lessRouterIns--;
                    serverInfo.addRoutersNumber();
                    serverInfo.setFreeProcess(tmpCORE - Router.core);
                    serverInfo.setFreeRAM(tmpRAM - Router.ram);
                }
            }

            if(startRoundRouters == lessRouterIns){
                flag = false;
            }
        }

        if (lessRouterIns > 0){
            //minimal config for lessRouters
            //add routers to server
            //add server to list
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

    public double getStorageSize() {
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
