package newverison;

import newverison.instances.Nginx;
import newverison.instances.Router;
import newverison.instances.Storage;
import newverison.instances.TarantoolCore;
import newverison.server.Server;
import newverison.server.ServerConfig;
import newverison.server.ServerInfo;
import newverison.server.ServerInstances;

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
        this.price = cluster.getPrice();
    }



    public void addServer(Server server){
        this.servers.add(server);
    }


    public void createClusterVariation(Config[] configs, int id){
        if(lessStoragesIns > 0){

            for (Config cfg: configs) {
                double tmpRAM = cfg.getRAM();
                double tmpCORE = cfg.getCORE();
                double tmpPRICE = cfg.getPRICE();


                int storageBoxes = (int)Math.floor((tmpRAM - Nginx.ram - Router.ram - TarantoolCore.ram - 10)/storageSize);

                ServerInfo serverInfo = new ServerInfo(
                        tmpRAM - Nginx.ram - Router.ram - TarantoolCore.ram - 10 - storageBoxes*storageSize,
                        tmpCORE - Nginx.core - Router.core - TarantoolCore.core - storageBoxes*1.5,
                        storageBoxes,
                        1
                );
                serverInfo.setCoreAvailability(true);
                serverInfo.setNginxAvailability(true);

                ServerInstances serverInstances = new ServerInstances();
                serverInstances.addInstance(new Router(0));

                int k = 0;
                while(lessStoragesIns > 0 && k < storageBoxes){
                    serverInstances.addInstance(new Storage(this.storagesNumber-this.lessStoragesIns, storageSize));
                    this.lessStoragesIns--;
                    k++;
                }

                this.addServer(new Server(id, new ServerConfig(cfg), serverInfo, serverInstances));

                this.lessRouterIns--;
                this.fullClusterData+=tmpRAM;
                this.price += tmpPRICE;


                for (int i = 0; i < configs.length; i++) {
                    new Cluster(this).createClusterVariation(configs, id+1);
                }

                this.lessRouterIns++;
                this.lessStoragesIns +=k;
                this.fullClusterData-=tmpRAM;
                this.price -= tmpPRICE;
            }

        }
        else {
            ClusterList.getInstance().addClusterToList(this);
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
        return "Cluster{" +
                "\n\tservers=" + servers +
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
