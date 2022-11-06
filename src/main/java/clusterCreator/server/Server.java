package clusterCreator.server;


public class Server {
    private int id;

    private String name = "";
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

    public Server(Server server) {
        this.id = server.getId();
        this.serverConfig = new ServerConfig(server.serverConfig);
        this.serverInfo = new ServerInfo(server.getServerInfo());
        this.serverInstances = new ServerInstances(server.getServerInstances());
        this.price = server.getPrice();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addHddCost() {
        this.price += serverInfo.getHddMemory().getFullMemo() * 0.24;
    }

    public double getPrice() {
        return price;
    }

    public void hddSpace() {
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


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "\n\tServer{" +
                "\n\tid=" + id +
                "\n\tname=" + name +
                "\n\tserverConfig=" + serverConfig +
                "\n\tserverInfo=" + serverInfo +
                "\n\tserverInstances=" + serverInstances +
                "\n\tserverPriceWithHdd=" + price +
                "\n}";
    }


}
