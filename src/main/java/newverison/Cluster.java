package newverison;

import newverison.server.Server;

import java.util.ArrayList;
import java.util.List;

public class Cluster {
    private List<Server> servers;
    private final int storagesNumber;
    private final int routersNumber;

    public Cluster(int storagesNumber, int routersNumber) {
        this.servers = new ArrayList<>();
        this.storagesNumber = storagesNumber;
        this.routersNumber = routersNumber;
    }

    public void addServer(Server server){
        servers.add(server);
    }
}
