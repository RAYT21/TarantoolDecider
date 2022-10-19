package server;


import configClasses.Config;

public class ServerConfig {
    private final Config config;

    public ServerConfig(Config config) {
        this.config = config;
    }

    public double getServerRam(){
        return config.getRAM();
    }

    public double getServerCore(){
        return config.getCORE();
    }

    public double getServerPrice(){
        return config.getPRICE();
    }

    @Override
    public String toString() {
        return "Server"+config;
    }
}
