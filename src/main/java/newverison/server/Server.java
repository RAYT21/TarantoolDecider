package newverison.server;

import newverison.Cluster;

public class Server {
    private int id;
    private ServerConfig serverConfig;
    private ServerInfo serverInfo;
    private ServerInstances serverInstances;

    public Server(int id, ServerConfig serverConfig, ServerInfo serverInfo, ServerInstances serverInstances) {
        this.id = id;
        this.serverConfig = serverConfig;
        this.serverInfo = serverInfo;
        this.serverInstances = serverInstances;
    }

    public Server() {
    }

    @Override
    public String toString() {
        return "Server{" +
                "\n\tid=" + id +
                "\n\tserverConfig=" + serverConfig +
                "\n\tserverInfo=" + serverInfo +
                "\n\tserverInstances=" + serverInstances +
                "\n}";
    }

    public Server distribution(){

        return new Server();
    }

    public void fillNewServer(Cluster cluster, int  lessStorages){

        if (lessStorages <=0) return;





    }
}
