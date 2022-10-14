
// Что доделать:
// 1) Разделить ресурсы ядра и тд, чтобы добавлять на сервера по мере свободности done
// 2) указывать и на какой серевер было добавлено
// 3) сделать анализ лучших конфигураций
// 4) доделать анализ распределения всех ресурсов,
// 5) какой номер стораджа идет в какой сервер

// 6) Разделить роутеры оптимально на

public class Cluster {

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


        double acceptableAmountOfData = 760;  //Integer.parseInt(args[0]);

        double requestPerSecond = 12000;       //Integer.parseInt(args[1]);
        double routerVelocity = 1000;         //Integer.parseInt(args[2]);
        double replicationLevel = 1;          //Integer.parseInt(args[3]);

        //routers
        int routerNumber = (int) Math.ceil(requestPerSecond/routerVelocity);
        double processForRouters = routerNumber*CORE;
        double RAMForRouters = routerNumber*INSTANCE_RAM;
        String routersDistribution = "";

        //Nginx
        double processForNginx = CORE;
        double RAMForNginx = INSTANCE_RAM;
        String nginxDistribution = "";

        //Core
        double processForCore = CORE;
        double RAMForCore = INSTANCE_RAM;
        String coreDistribution = "";

        //Nginx
        double processForETCD = CORE;
        double RAMForETCD = ETCD_RAM;
        String ETCDDistribution = "";

        // 100% amount of data
        double dataVolume = acceptableAmountOfData/0.8;
        // Start storage Cores
        double optimalCoreNumbers = Math.ceil(dataVolume/32.)*1.5;
        // подходящие конфигурации
        String[] configVariation = new String[12];
        int configVarCounter =0;
        // findListConfigs
        // проходим по количеству рама, ищем оптимальное значение РАМА и конфигурации для данного значения
        for (int i = 32; i > 20 ; i--) {

            // рассчитываем колличество стороджей
            int tmpN = (int) Math.ceil(dataVolume/(double)i);
            // возможное оптимальное число ядер на стораджи
            double tmpOptimalCoreNumbers = tmpN * 1.5;

            //если оптимальное число требуемых ядер требуется меньше, при данном числе рама, то хорошо
            if(tmpOptimalCoreNumbers <= optimalCoreNumbers){
                //optimalConfig
                optimalCoreNumbers = tmpOptimalCoreNumbers;
                System.out.println("RAMi: " + i + " optimalCoreNumbers: " + optimalCoreNumbers);

                // расчет возможных оптимальных значнеий ресурсов RAM и Core
                double RAMForStorage = tmpN*i;
                System.out.println("tmpRAMStorage: " + RAMForStorage);
                //требуемое число RAM для размещения всего
                double tmpRAM= RAMForStorage + RAMForCore + RAMForETCD +RAMForCore+RAMForNginx;



                double processForStorage = tmpN*CORE;
                System.out.println("tmpCoreStorage: " + processForStorage);
                double tmpCore = processForStorage+processForCore+processForETCD+processForNginx+processForRouters;
                System.out.println("tmpRAM: "+ tmpRAM + " tmpCore: "+ tmpCore);

                // временная переменная для отслеживания конфигурации
                int tempConfPlace = 0;
                StringBuilder tRes = new StringBuilder();

                //флаги того, что роутеры и тд размещены в памяти
                boolean routersFlag = true;
                boolean nginxFlag = true;
                boolean ETCDFlag = true;
                boolean coreFlag = true;
                //configDis
                while (tempConfPlace < 4 && tmpRAM > 0){
                    boolean flag = true;
                    String[] tConf = configures[tempConfPlace].split(" ");
                    //Core который можно заполнить полезной нагрузкой
                    double tCore = Integer.parseInt(tConf[1]);
                    //RAM который можно заполнить полезной нагрузкой
                    double tRAM = Integer.parseInt(tConf[2]) - RAM_RESERVATION;
                    // сколько можно положить стораджей на один сервер
                    int storageBox = (int )Math.floor(tRAM/i);
                    System.out.println("tCore: " + tCore + " tRAM: " + tRAM +" storageBox: " + storageBox);
                    int tmpRouterNumber = routerNumber;
                    double tmpRouterRAM =RAMForRouters;
                    double tmpRouterProcess = processForRouters;


                    // размещаем стораджи
                    //storageDist
                    int tS =0;
                    while(flag){

                        //доступных ресурсов на конкретном сервере
                        double serverRAM = tRAM;
                        double serverCore = tCore;

                        // если хватает места для размещения максимального колличества стораджей на один сервер
                        if ( processForStorage >= tCore && RAMForStorage >= tRAM && tmpN >= storageBox){
                            serverRAM -= storageBox*i;
                            serverCore -= storageBox*CORE;
                            RAMForStorage  -= storageBox*i;
                            processForStorage -= storageBox*CORE;
                            tmpRAM  -= storageBox*i;
                            tmpCore -= storageBox*CORE;
                            tmpN -= storageBox;
                            tS++;

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


                        }
                        else{
                            //размещение оставшихся
                            serverRAM -= (tmpN - storageBox)*i;
                            serverCore -= (tmpN - storageBox)*CORE;
                            RAMForStorage  -= (tmpN - storageBox)*i;
                            processForStorage -= (tmpN - storageBox)*CORE;
                            tmpRAM  -= (tmpN - storageBox)*i;
                            tmpCore -= (tmpN - storageBox)*CORE;
                            tS++;

                            flag=false;
                        }

                        System.out.println("Free space: "+ serverRAM + " Free cores: " + serverCore);
                        System.out.println("Flag: " + flag);


                    }
                    tRes.append(tConf[0] + " Number: " +tS + "\n");
                    tempConfPlace++;
                }


                //проверить, размещены ли доп ресурсы



                System.out.println(tRes.toString());
                configVariation[++configVarCounter] = tRes.toString();

            }


        }

    }
}



// заново пройтись по алгоритму и выделить процедуры, как пример
// 1) процедура определения свободного пространства на сервер
// 2) разделить грамотно роутеры по серверам
// 3) процедура выделения стораджей на сервера
// 4) процедура проверки размещения всех необходимых ресурсов
