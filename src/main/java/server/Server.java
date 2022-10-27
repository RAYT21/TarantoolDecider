package server;


import clusterClasses.Cluster;

public class Server {
    private int id;
    private ServerConfig serverConfig;
    private ServerInfo serverInfo;
    private ServerInstances serverInstances;

    private double price;

    public Server(int id, ServerConfig serverConfig, ServerInfo serverInfo, ServerInstances serverInstances) {
        this.id = id;
        this.serverConfig = serverConfig;
        this.serverInfo = serverInfo;
        this.serverInstances = serverInstances;

        this.price = serverConfig.getServerPrice();
    }

    public Server() {

    }

    public Server(Server server){
        this.id = server.getId();
        this.serverConfig = new ServerConfig(server.serverConfig);
        this.serverInfo = new ServerInfo(server.getServerInfo());
        this.serverInstances = new ServerInstances(server.getServerInstances());
        this.price = server.getServerConfig().getServerPrice();
    }

    public int getId() {
        return id;
    }

    public void addHddCost(){
        this.price += serverInfo.getHddMemory().getFullMemo() * 0.24;
    }

    public double getPrice() {
        return price;
    }

    public void hddSpace(){
        serverInfo.setHddMemory(new ServerHDDSpace(this.getServerInfo()));
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
