
// Что доделать:
// 1) Разделить ресурсы ядра и тд, чтобы добавлять на сервера по мере свободности
// 2) указывать и на какой серевер было добавлено
// 3) сделать анализ лучших конфигураций
// 4) доделать анализ распределения всех ресурсов, в частности какой номер стораджа идет в какой сервер

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

        int routerNumber = (int) Math.ceil(requestPerSecond/routerVelocity);

        // Processor resources required for routers, Nginx, ETCD and Core
        double processForRNEC =  (routerNumber+3)*CORE;
        double RAMForRNEC =  routerNumber*INSTANCE_RAM+2*INSTANCE_RAM+ETCD_RAM;

        // 100% amount of data
        double dataVolume = acceptableAmountOfData/0.8;
        // Start storage Cores
        double optimalCoreNumbers = Math.ceil(dataVolume/32.)*1.5;
        // подходящие конфигурации
        String[] configVariation = new String[12];
        int configVarCounter =0;

        // проходим по количеству рама, ищем оптимальное значение РАМА и конфигурации для данного значения
        for (int i = 32; i > 20 ; i--) {

            // рассчитываем колличество стороджей
            int tmpN = (int) Math.ceil(dataVolume/(double)i);
            // возможное оптимальное число ядер на стораджи
            double tmpOptimalCoreNumbers = tmpN * 1.5;

            //если оптимальное число требуемых ядер требуется меньше, при данном числе рама, то хорошо
            if(tmpOptimalCoreNumbers <= optimalCoreNumbers){

                optimalCoreNumbers = tmpOptimalCoreNumbers;
                System.out.println("RAMi: " + i + " optimalCoreNumbers: " + optimalCoreNumbers);

                // расчет возможных оптимальных значнеий ресурсов RAM и Core
                double tmpRAMStorage = tmpN*i;
                System.out.println("tmpRAMStorage: " + tmpRAMStorage);
                //требуемое число RAM для размещения всего
                double tmpRAM=tmpRAMStorage+RAMForRNEC;

                double tmpCoreStorage = tmpN*CORE;
                System.out.println("tmpCoreStorage: " + tmpCoreStorage);
                double tmpCore = tmpCoreStorage+processForRNEC;
                System.out.println("tmpRAM: "+ tmpRAM + " tmpCore: "+ tmpCore);

                // временная переменная для отслеживания конфигурации
                int tempConfPlace = 0;
                StringBuilder tRes = new StringBuilder();

                //флаг того, что данные роутеров и тд размещены
                boolean RNECFlag = true;
                while (tempConfPlace < 4 && tmpRAM > 0){
                    boolean flag = true;
                    String[] tConf = configures[tempConfPlace].split(" ");
                    //Core который можно заполнить полезной нагрузкой
                    double tCore = Integer.parseInt(tConf[1]);
                    //RAM который можно заполнить полезной нагрузкой
                    double tRAM = Integer.parseInt(tConf[2]) - RAM_RESERVATION;
                    // сколько можно положить стораджей на один сервер
                    double storageBox = Math.floor(tRAM/i);
                    System.out.println("tCore: " + tCore + " tRAM: " + tRAM +" storageBox: " + storageBox);


                    // размещаем стораджи
                    int tS =0;
                    while(flag){

                        //доступных ресурсов на конкретном сервере
                        double sererRAM = tRAM;
                        double serverCore = tCore;

                        if (tmpCoreStorage >= tCore && tmpRAMStorage >= tRAM && tmpN >= storageBox){
                            sererRAM -= storageBox*i;
                            serverCore -= storageBox*CORE;
                            tmpRAMStorage  -= storageBox*i;
                            tmpCoreStorage -= storageBox*CORE;
                            tmpRAM  -= storageBox*i;
                            tmpCore -= storageBox*CORE;
                            tmpN -= storageBox;
                            tS++;
                        }
                        else{
                            sererRAM -= (tmpN - storageBox)*i;
                            serverCore -= (tmpN - storageBox)*CORE;
                            tmpRAMStorage  -= (tmpN - storageBox)*i;
                            tmpCoreStorage -= (tmpN - storageBox)*CORE;
                            tmpRAM  -= (tmpN - storageBox)*i;
                            tmpCore -= (tmpN - storageBox)*CORE;
                            tS++;

                            flag=false;
                        }

                        System.out.println("Free space: "+ sererRAM + " Free cores: " + serverCore);
                        System.out.println("RAMForRNEC: " + RAMForRNEC + " processForRNEC: " + processForRNEC);
                        System.out.println("Flag: " + flag);

                        //учитываем хватит ли на данном сервере места для размещения роутеров, Nginx, ETCD и Core
                        if (RNECFlag && sererRAM >= RAMForRNEC && serverCore >= processForRNEC){
                            RNECFlag = false;
                            tmpRAM  -= RAMForRNEC;
                            tmpCore -= processForRNEC;

                        }
                    }
                    tRes.append(tConf[0] + " Number: " +tS + "\n");
                    tempConfPlace++;
                }
                System.out.println("RNECFlag: " + RNECFlag);


                if(RNECFlag){
                    for (int j = 3; j > -1; j--) {
                        String[] tConf = configures[j].split(" ");
                        double tCore = Integer.parseInt(tConf[1]);
                        double tRAM = Integer.parseInt(tConf[2]) - RAM_RESERVATION;
                        if (tRAM >= RAMForRNEC && tCore >= processForRNEC){
                            RNECFlag = false;
                            tmpRAM  -= RAMForRNEC;
                            tmpCore -= processForRNEC;
                        }
                    }


                }
                System.out.println(tRes.toString());
                configVariation[++configVarCounter] = tRes.toString();

            }


        }

    }
}
