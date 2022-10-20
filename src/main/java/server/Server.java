package server;


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

    public int getId() {
        return id;
    }

    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    public ServerInfo getServerInfo() {
        return serverInfo;
    }

    public ServerInstances getServerInstances() {
        return serverInstances;
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




}
