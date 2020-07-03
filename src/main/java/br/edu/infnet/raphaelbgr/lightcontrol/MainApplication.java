package br.edu.infnet.raphaelbgr.lightcontrol;

import br.edu.infnet.raphaelbgr.lightcontrol.model.*;
import br.edu.infnet.raphaelbgr.lightcontrol.util.Util;
import com.google.gson.Gson;
import com.pi4j.io.gpio.*;
import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.*;

import java.net.URISyntaxException;
import java.util.*;

public class MainApplication {

    private static MQTT mqtt = new MQTT();
    private static CallbackConnection connection;
    private static MainDataSet mainDataSet;

    private static GpioController gpio;

    private static final HashMap<String, GpioPinDigitalOutput> gpioMap = new HashMap();
    private static List<String> idList;
    private static QoS qos = QoS.AT_LEAST_ONCE;
    private static boolean emulatedMode;

    private static String HOST = "tcp://mqtt.eclipse.org:1883";
    private static boolean masterSwitchOn = true;

    public static void main(String[] args) {

        System.out.println("SERVER> Program start...");
        initMainDataSet();
        initGpio();
        setupMqttClient();
        createMqttCallbackConnection();
        connectMqtt();
        startConnectionReportChecker();
    }

    private static void startConnectionReportChecker() {
        while (true) {
            boolean hasInternet = Util.isReachableByPing("8.8.8.8") || Util.isReachableByHttp("http://www.google.com");
            if (!hasInternet)
                System.out.println("SERVER> Internet conection down...");

            while (!hasInternet) {
                System.out.println("SERVER> Waiting for connection...");
                List<GpioPinDigitalOutput> offlineGpios = getOfflineGpios();
                for (GpioPinDigitalOutput gpio : offlineGpios) {
                    gpio.setState(PinState.HIGH);
                }
                waitOneSec();
                for (GpioPinDigitalOutput gpio : offlineGpios) {
                    gpio.setState(PinState.LOW);
                }
                waitOneSec();
                hasInternet = Util.isReachableByPing("8.8.8.8");
            }
            waitOneSec();
        }
    }

    private static List<GpioPinDigitalOutput> getOfflineGpios() {
        List<GpioPinDigitalOutput> offlineGpios = new ArrayList<>();
        for (GpioPinDigitalOutput gpio : gpioMap.values()) {
            if (gpio.getState() == PinState.LOW) {
                offlineGpios.add(gpio);
            }
        }
        return offlineGpios;
    }

    private static List<GpioPinDigitalOutput> getAllGpios() {
        List<GpioPinDigitalOutput> gpios = new ArrayList<>();
        for (GpioPinDigitalOutput gpio : gpioMap.values()) {
            gpios.add(gpio);
        }
        return gpios;
    }

    private static void waitOneSec() {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initGpio() {
        // wPI ports
        if (Raspberry.isPi()) {
            gpio = GpioFactory.getInstance();
            GpioPinDigitalOutput pin0 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_08, "MyLED_0", PinState.LOW);
            GpioPinDigitalOutput pin1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_09, "MyLED_1", PinState.LOW);
            GpioPinDigitalOutput pin2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07, "MyLED_2", PinState.LOW);
            GpioPinDigitalOutput pin3 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "MyLED_3", PinState.LOW);
            GpioPinDigitalOutput pin4 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "MyLED_4", PinState.LOW);
            GpioPinDigitalOutput pin5 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "MyLED_5", PinState.LOW);
            GpioPinDigitalOutput pin6 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_12, "MyLED_6", PinState.LOW);
            GpioPinDigitalOutput pin7 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_13, "MyLED_7", PinState.LOW);
            GpioPinDigitalOutput pin8 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_14, "MyLED_8", PinState.LOW);
            GpioPinDigitalOutput pin9 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_30, "MyLED_9", PinState.LOW);
            GpioPinDigitalOutput pin10 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_21, "MyLED_10", PinState.LOW);
            gpioMap.put(idList.get(0), pin0);
            gpioMap.put(idList.get(1), pin1);
            gpioMap.put(idList.get(2), pin2);
            gpioMap.put(idList.get(3), pin3);
            gpioMap.put(idList.get(4), pin4);
            gpioMap.put(idList.get(5), pin5);
            gpioMap.put(idList.get(6), pin6);
            gpioMap.put(idList.get(7), pin7);
            gpioMap.put(idList.get(8), pin8);
            gpioMap.put(idList.get(9), pin9);
            gpioMap.put(idList.get(10), pin10);
            for (Map.Entry<String, GpioPinDigitalOutput> item : gpioMap.entrySet()) {
                item.getValue().setMode(PinMode.DIGITAL_OUTPUT);
            }
        } else {
            System.out.println("SERVER> Program not running on a RaspBerryPi, using PIN emulated mode!");
            emulatedMode = true;
            gpio = new FakeGpioController();
            gpioMap.put(idList.get(0), new FakeGpioPinDigitalOutput("MyLED_0"));
            gpioMap.put(idList.get(1), new FakeGpioPinDigitalOutput("MyLED_1"));
            gpioMap.put(idList.get(2), new FakeGpioPinDigitalOutput("MyLED_2"));
            gpioMap.put(idList.get(3), new FakeGpioPinDigitalOutput("MyLED_3"));
            gpioMap.put(idList.get(4), new FakeGpioPinDigitalOutput("MyLED_4"));
            gpioMap.put(idList.get(5), new FakeGpioPinDigitalOutput("MyLED_5"));
            gpioMap.put(idList.get(6), new FakeGpioPinDigitalOutput("MyLED_6"));
            gpioMap.put(idList.get(7), new FakeGpioPinDigitalOutput("MyLED_7"));
            gpioMap.put(idList.get(8), new FakeGpioPinDigitalOutput("MyLED_8"));
            gpioMap.put(idList.get(9), new FakeGpioPinDigitalOutput("MyLED_9"));
            gpioMap.put(idList.get(10), new FakeGpioPinDigitalOutput("MyLED_10"));
        }
    }

    private static void initMainDataSet() {
        String data = new Scanner(MainApplication.class.getClassLoader().getResourceAsStream("building_1_initial_state.json"), "UTF-8").useDelimiter("\\A").next();
        mainDataSet = new Gson().fromJson(data, MainDataSet.class);
        mainDataSet.payload.setMasterSwitchState(masterSwitchOn);
        idList = buildControlledLightList();
    }

    private static void setupMqttClient() {
        try {
            mqtt.setHost(HOST);
            mqtt.setWillQos(QoS.AT_LEAST_ONCE);
            connection = mqtt.callbackConnection();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private static void createMqttCallbackConnection() {
        connection.listener(new Listener() {
            @Override
            public void onConnected() {
                System.out.println("SERVER> Connected to " + HOST);
            }

            @Override
            public void onDisconnected() {
                System.out.println("SERVER> Disconnected socket...");
            }

            @Override
            public void onPublish(UTF8Buffer utf8Buffer, Buffer buffer, Runnable runnable) {
                runnable.run();
                processCommand(buffer.utf8().toString());
            }

            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("SERVER> Failure on socket...");
            }
        });
    }

    private static void processCommand(String command) {
        switch (command) {
            case "command_is_raspberry_alive":
                sendImAliveResponse();
                System.out.println("SERVER> Received command 'command_is_raspberry_alive'");
                break;
            case "fetch_building_id_1":
                sendBuildingData(1);
                System.out.println("SERVER> Received command 'fetch_building_id_1'");
                break;
            default:
                if (command.contains("command_turn_light_on_id")) {
                    if (masterSwitchOn) {
                        System.out.println("SERVER> Received command 'command_turn_light_on'");
                        turnOnLightForId(command.replace("command_turn_light_on_id_", ""));
                    } else {
                        System.out.println("SERVER> Received command 'command_turn_light_on' but masterSwitch is: " + masterSwitchOn);
                        publishCommand("server_message_" + "Recebida ordem de ligar o nó porém o interruptor mestre encontra-se desligado.");
                    }
                    break;
                }
                if (command.contains("command_turn_light_off_id")) {
                    if (masterSwitchOn) {
                        System.out.println("SERVER> Received command 'command_turn_light_off'");
                        turnOffLightForId(command.replace("command_turn_light_off_id_", ""));
                    } else {
                        System.out.println("SERVER> Received command 'command_turn_light_off' but masterSwitch is: " + masterSwitchOn);
                        publishCommand("server_message_" + "Recebida ordem de desligar o nó porém o interruptor mestre encontra-se desligado.");
                    }
                    break;
                }
                if (command.contains("command_turn_master_switch_on")) {
                    System.out.println("SERVER> Received command 'command_turn_master_switch_on'");
                    turnMasterSwitchOn();
                    break;
                }
                if (command.contains("command_turn_master_switch_off")) {
                    System.out.println("SERVER> Received command 'command_turn_master_switch_off'");
                    turnMasterSwitchOff();
                    break;
                }
        }
    }

    private static void turnMasterSwitchOn() {
        masterSwitchOn = true;
        mainDataSet.payload.setMasterSwitchState(true);
        sendBuildingData(1);
        publishCommand("server_message_" + "Interruptor mestre ativado!");
    }

    private static void turnMasterSwitchOff() {
        masterSwitchOn = false;
        mainDataSet.payload.setMasterSwitchState(false);
        turnOffAllLights();
        sendBuildingData(1);
        publishCommand("server_message_" + "Interruptor mestre desativado e tódos os nós também.");
    }

    private synchronized static void turnOnLightForId(String id) {
        for (Block block : mainDataSet.payload.getBlocks()) {
            for (Floor floor : block.getFloors()) {
                for (ControlledLight controlledLight : floor.getControlledLights()) {
                    if (id.contains(controlledLight.getId())) {
                        if (controlledLight.getState() == 1) {
                            System.out.println("SERVER> Light '" + controlledLight.getId() + "' already on.");
                        } else {
                            controlledLight.setState(1);
                            publishCommand("server_message_" + "Luz ligada para " + controlledLight.getArea() + "(" + controlledLight.getId() + ")");
                            sendBuildingData(1);
                        }
                        if (gpio != null && gpioMap.containsKey(controlledLight.getId())) {
                            gpioMap.get(controlledLight.getId()).setState(PinState.HIGH);
                            System.out.println("SERVER> GPIO for pin " + gpioMap.get(controlledLight.getId()).getName() + " set to HIGH");
                        } else {
                            System.out.println("SERVER> GPIO not found.");
                        }
                        return;
                    }
                }
            }
        }
    }

    private synchronized static void turnOffLightForId(String id) {
        for (Block block : mainDataSet.payload.getBlocks()) {
            for (Floor floor : block.getFloors()) {
                for (ControlledLight controlledLight : floor.getControlledLights()) {
                    if (id.contains(controlledLight.getId())) {
                        if (controlledLight.getState() == 0) {
                            System.out.println("SERVER> Light '" + controlledLight.getId() + "' already off.");
                        } else {
                            controlledLight.setState(0);
                            publishCommand("server_message_" + "Luz desligada para " + controlledLight.getArea() + "(" + controlledLight.getId() + ")");
                            sendBuildingData(1);
                        }
                        if (gpio != null && gpioMap.containsKey(controlledLight.getId())) {
                            gpioMap.get(controlledLight.getId()).setState(PinState.LOW);
                            System.out.println("SERVER> GPIO for pin " + gpioMap.get(controlledLight.getId()).getName() + " set to LOW");
                        } else {
                            System.out.println("SERVER> GPIO not found.");
                        }
                        break;
                    }
                }
            }
        }
    }

    private synchronized static void turnOnAllLights() {
        for (Block block : mainDataSet.payload.getBlocks()) {
            for (Floor floor : block.getFloors()) {
                for (ControlledLight controlledLight : floor.getControlledLights()) {
                    if (controlledLight.getState() == 0) {
                        controlledLight.setState(1);
                    }
                    if (gpio != null && gpioMap.containsKey(controlledLight.getId())) {
                        gpioMap.get(controlledLight.getId()).setState(PinState.HIGH);
                    } else {
                        System.out.println("SERVER> GPIO not found: " + controlledLight.getId());
                    }
                }
            }
        }
    }

    private synchronized static void turnOffAllLights() {
        for (Block block : mainDataSet.payload.getBlocks()) {
            for (Floor floor : block.getFloors()) {
                for (ControlledLight controlledLight : floor.getControlledLights()) {
                    if (controlledLight.getState() != 0) {
                        controlledLight.setState(0);
                    }
                    if (gpio != null && gpioMap.containsKey(controlledLight.getId())) {
                        gpioMap.get(controlledLight.getId()).setState(PinState.LOW);
                    } else {
                        System.out.println("SERVER> GPIO not found: " + controlledLight.getId());
                    }
                }
            }
        }
    }

    private static List<String> buildControlledLightList() {
        List idList = new ArrayList();
        for (Block block : mainDataSet.payload.getBlocks()) {
            for (Floor floor : block.getFloors()) {
                for (ControlledLight controlledLight : floor.getControlledLights()) {
                    idList.add(controlledLight.getId());
                }
            }
        }
        return idList;
    }

    private static void sendBuildingData(int id) {
        String data = new Gson().toJson(mainDataSet, MainDataSet.class);
        publishCommand("fetch_building_id_response" + data);
    }

    private static void sendFetchBuildingError() {
        publishCommand("fetch_building_error");
    }

    private static void sendImAliveResponse() {
        publishCommand("raspberry_pi_im_alive");
    }

    private static void connectMqtt() {
        System.out.println("SERVER> Connecting to MQTT server...");
        connection.connect(new Callback<Void>() {
            public void onFailure(Throwable value) {
                System.out.println("SERVER> Socket breakdown...");
                connectMqtt();
            }

            public void onSuccess(Void v) {
                System.out.println("SERVER> Connected socket!");
                subscribeToTopic();
            }
        });
    }

    private static void subscribeToTopic() {
        Topic[] topics = {new Topic("tcc_light_control_infnet", QoS.AT_LEAST_ONCE)};
        connection.subscribe(topics, new Callback<byte[]>() {
            public void onSuccess(byte[] qoses) {
                // The result of the subcribe request.
                System.out.println("SERVER> Subscribed succesfully to topic tcc_light_control_infnet");
                publishCommand("raspberry_pi_im_alive");
            }

            public void onFailure(Throwable value) {
                System.out.println("SERVER> Failure to subscribe on topic.");
            }
        });
    }

    private static void publishCommand(final String command) {
        // Send a message to a topic
        connection.publish("tcc_light_control_infnet", command.getBytes(), QoS.AT_LEAST_ONCE, false, new Callback<Void>() {
            public void onSuccess(Void v) {
                // the pubish operation completed successfully.
                String clampedString = command.length() > 100 ? command.substring(0, 100) + "..." : command;
                System.out.println("SERVER> Sent command '" + clampedString + "'");
            }

            public void onFailure(Throwable value) {
                value.printStackTrace();
                System.out.println("SERVER> Failure to publish on topic. " + value.getLocalizedMessage());
            }
        });
    }

    private void disconnect() {
        // To disconnect..
        connection.disconnect(new Callback<Void>() {
            public void onSuccess(Void v) {
                // called once the connection is disconnected.
                System.out.println("SERVER> Disconnected.");
            }

            public void onFailure(Throwable value) {
                // Disconnects never fail.
            }
        });
    }
}