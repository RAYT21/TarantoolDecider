import clusterClasses.Cluster;
import clusterClasses.ClusterList;
import clusterClasses.OptimalStorageCluster;
import clusterClasses.ReplicationCluster;
import configClasses.Config;
import configClasses.ConfigsList;
import instances.*;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


// refactoring

// сравнение старого алгаритма с новым(новый метод быстрее в разы,
// но не факт что лучше, но если настроить шардирование, то сто процентов будет лучше с размещением ЕТСД)

// продумать репликацию () +

// продумать шардирование +

// РЕФАКТОРИНГ БОЖЕ МОЙ

// добавить все существующие конифгурации

// обговорить об размещении ЕТСД (на отдельном сервере или как) +
// пром и нт

// обговорить про размещение роутеров (сколько задано, столько и размещать) +

// как зависит нагрузка от контуров
// пром = 1.5, нт = 1.5,
// остальные и дев = 1

// РЕФАКТОРИНГ

// Что еще не учли?*?

// Какой интерфейс будет на странице заказа?

// Может быть включать старый алгоритм когда количетсво инстенсов не превышает определенного значения (10-12)?

// РЕФАКТОРИНГ!


// в клдастер добавить значение группы

// Создать новый объект ServerHDDSpace


public class Apt {
    static List<String> list = new ArrayList<>(){{
        add("prod");
        add("nt");
    }};



    public void run(String[] args) {
        double acceptableAmountOfData = 200;  //Integer.parseInt(args[0]);
        double requestPerSecond = 2000;      //Integer.parseInt(args[1]);
        double routerVelocity = 2000;        //Integer.parseInt(args[2]); в зависимости от контура (ентерпрайз 2к, тдк 3-4к)
        int replicationLevel = 4;         //Integer.parseInt(args[3]); Может быть его от контура автоматически заполнять?
        double percent = 0.75; // Double.parseDouble(args[4])
        double coreDepend = list.contains("prod")  ? 1.5 : 1;

        if (list.contains("prod")){
            ETCD.setFlagNeed(true);
        }

        // init core
        Nginx.setCore(coreDepend);
        ETCD.setCore(coreDepend);
        Router.setCore(coreDepend);
        Storage.setCore(coreDepend);
        TarantoolCore.setCore(coreDepend);

        //routers
        int routerNumber = (int) Math.ceil(requestPerSecond/routerVelocity);

        // 100% amount of data
        double dataForStorages = acceptableAmountOfData/percent;

        OptimalStorageCluster optimal = new OptimalStorageCluster();

        optimal.optimalSizeForStorage(dataForStorages);

        Config[] conf = ConfigsList.listOfSuitableConfigurations(optimal.getStorageSize());

        Cluster cluster = new Cluster(optimal,routerNumber);
        if ( optimal.getNumber() > 9)
                cluster.createClusterVariationWithNextStepMethod(conf,0);
        else
                cluster.createClusterVariationRecursively(conf,0);

        cluster = ClusterList.getInstance().getClusterList();

        System.out.println(new ReplicationCluster(cluster,replicationLevel));


    }





}
