public class Classeser {
    public static final double CORE = 1.5;
    public static final double ETCD_RAM = 2;
    public static final double INSTANCE_RAM = 0.25;
    public static final double RAM_RESERVATION = 10;


    private static final String[] configures = new String[]{
            "Config#1 16 128",
            "Config#2 8 64",
            "Config#3 4 32",
            "Config#4 4 16",
    };

    public static void main(String[] args) {
        initial();
        findListConfigs();
        deciderBestConfig();
    }

    public static void initial(){}

    public static void findListConfigs(){
        optimalConfig();
    }

    public static void optimalConfig(){
        configureDist();

    }

    public static void  configureDist(){
        storageDist();
        allResIsDist();
    }

    public static void storageDist(){
        //routersAndOthersDist();
        allResIsDist();
    }

  /*  public static void routersAndOthersDist(){
        //если после размещения стораджей хватает места на размещения роутеров
        while ( tmpRouterNumber > 0 && serverCore >= CORE && serverRAM >= INSTANCE_RAM ){
            tmpRouterNumber--;
            serverCore-=CORE;
            serverRAM-=INSTANCE_RAM;
            tmpRouterRAM-=INSTANCE_RAM;
            tmpRouterProcess-=CORE;

        }
        // если все роутеры размещены, то переключаем флаг

        if (tmpRouterNumber == 0){
            routersFlag = false;
        }

        //если после размещения на сервере хватает места для размещения ETCD
        if (serverRAM >= ETCD_RAM && serverCore >= CORE){
            serverCore-=CORE;
            serverRAM-=ETCD_RAM;
            ETCDFlag= false;
        }


        // если после всез размещений хватает места для размещения nginx
        if (serverRAM >= INSTANCE_RAM && serverCore >= CORE){
            serverCore-=CORE;
            serverRAM-=INSTANCE_RAM;
            nginxFlag= false;
        }

        // если после всез размещений хватает места для размещения core
        if (serverRAM >= INSTANCE_RAM && serverCore >= CORE){
            serverCore-=CORE;
            serverRAM-=INSTANCE_RAM;
            coreFlag= false;
        }
    }*/

    public static void allResIsDist(){}

    public static void deciderBestConfig(){}


}
