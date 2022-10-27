package server;

import instances.Router;
import instances.Storage;

import java.util.ArrayList;
import java.util.List;

public class ServerInstances {
    private List<Storage> storages;
    private List<Router> routers;

    public ServerInstances() {
        this.storages = new ArrayList<>();
        this.routers = new ArrayList<>();
    }

    public ServerInstances(ServerInstances serverInstances){
        this.storages = new ArrayList<>(serverInstances.storages);
        this.routers = new ArrayList<>(serverInstances.routers);
    }

    public void addInstance(Object instance){
        if(instance instanceof Storage){
            storages.add((Storage) instance);
        }
        if(instance instanceof Router){
            routers.add((Router) instance);
        }
    }

    @Override
    public String toString() {
        return "\n\tserverInstances{" +
                "\n\tstorages=" + storages+
                "\n\trouters=" + routers +
                "\n\t}";
    }
}
