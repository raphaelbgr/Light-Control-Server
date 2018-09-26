package br.edu.infnet.raphaelbgr.lightcontrol;

import br.edu.infnet.raphaelbgr.lightcontrol.model.Block;
import br.edu.infnet.raphaelbgr.lightcontrol.model.ControlledLight;
import br.edu.infnet.raphaelbgr.lightcontrol.model.Floor;
import br.edu.infnet.raphaelbgr.lightcontrol.model.MainDataSet;
import com.google.gson.Gson;
import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.*;

import java.net.URISyntaxException;
import java.util.Scanner;

public class MainApplication {

    private static MQTT mqtt = new MQTT();
    private static CallbackConnection connection;
    private static MainDataSet mainDataSet;

    public static void main(String[] args) {
        System.out.println("Server> Program start...");
        initMainDataSet();
        setupMqttClient();
        createMqttCallbackConnection();
        connectMqtt();

        slowDownPolicy();
    }

    private static void initMainDataSet() {
        String data = new Scanner(MainApplication.class.getClassLoader().getResourceAsStream("building_1_initial_state.json"), "UTF-8").useDelimiter("\\A").next();
        mainDataSet = new Gson().fromJson(data, MainDataSet.class);
    }

    private static void slowDownPolicy() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void setupMqttClient() {
        try {
            mqtt.setHost("tcp://iot.eclipse.org:1883");
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
                System.out.println("SERVER> Connected to iot.eclipse.org!");
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
                    System.out.println("SERVER> Received command 'command_turn_light_on'");
                    turnOnLightForId(command.replace("command_turn_light_on_id_", ""));
                    break;
                }
                if (command.contains("command_turn_light_off_id")) {
                    System.out.println("SERVER> Received command 'command_turn_light_off'");
                    turnOffLightForId(command.replace("command_turn_light_off_id_", ""));
                    break;
                }
        }
    }

    private synchronized static void turnOnLightForId(String id) {
        for (Block block : mainDataSet.payload.getBlocks()) {
            for (Floor floor : block.getFloors()) {
                for (ControlledLight controlledLight : floor.getControlledLights()) {
                    if (id.contains(controlledLight.getId())) {
                        if (controlledLight.getState() == 1) {
                            break;
                        } else {
                            controlledLight.setState(1);
                            publishCommand("server_message_" + "Luz ligada para " + controlledLight.getArea());
                            sendBuildingData(1);
                            break;
                        }
                    }
                }
            }
        }
        System.out.println("SERVER> No light found for id '" + id + "1");
    }

    private synchronized static void turnOffLightForId(String id) {
        for (Block block : mainDataSet.payload.getBlocks()) {
            for (Floor floor : block.getFloors()) {
                for (ControlledLight controlledLight : floor.getControlledLights()) {
                    if (id.contains(controlledLight.getId())) {
                        if (controlledLight.getState() == 0) {
                            break;
                        } else {
                            controlledLight.setState(0);
                            publishCommand("server_message_" + "Luz desligada para " + controlledLight.getArea());
                            sendBuildingData(1);
                            break;
                        }
                    }
                }
            }
        }
        System.out.println("SERVER> No light found for id '" + id + "1");
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
        connection.connect(new Callback<Void>() {
            public void onFailure(Throwable value) {
                System.out.println("SERVER> Socket breakdown...");
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

    private static void publishCommand(String command) {
        // Send a message to a topic
        connection.publish("tcc_light_control_infnet", command.getBytes(), QoS.AT_LEAST_ONCE, false, new Callback<Void>() {
            public void onSuccess(Void v) {
                // the pubish operation completed successfully.
                System.out.println("SERVER> Sent command '" + command + "'");
            }

            public void onFailure(Throwable value) {
                System.out.println("SERVER> Failure to publish on topic.");
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