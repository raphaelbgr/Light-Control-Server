package br.edu.infnet.raphaelbgr.lightcontrol;

import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.*;

import java.net.URISyntaxException;

public class MainApplication {

    private static MQTT mqtt = new MQTT();
    private static CallbackConnection connection;

    public static void main(String[] args) {
        System.out.println("Server> Program start...");
        setupMqttClient();
        createMqttCallbackConnection();
        connectMqtt();

        slowDownPolicy();
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
                System.out.println("MQQT> Connected to iot.eclipse.org!");
            }

            @Override
            public void onDisconnected() {
                System.out.println("MQQT> Disconnected socket...");
            }

            @Override
            public void onPublish(UTF8Buffer utf8Buffer, Buffer buffer, Runnable runnable) {
                runnable.run();
                processCommand(buffer.ascii().toString());
            }

            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("MQQT> Failure on socket...");
            }
        });
    }

    private static void processCommand(String command) {
        switch (command) {
            case "command_is_raspberry_alive":
                sendImAliveResponse();
                System.out.println("MQQT> Received command 'command_is_raspberry_alive'");
                break;
            default:
        }
    }

    private static void sendImAliveResponse() {
        publishCommand("raspberry_pi_im_alive");
    }

    private static void connectMqtt() {
        connection.connect(new Callback<Void>() {
            public void onFailure(Throwable value) {
                System.out.println("MQQT> Socket breakdown...");
            }

            public void onSuccess(Void v) {
                System.out.println("MQQT> Connected socket!");
                subscribeToTopic();
            }
        });
    }

    private static void subscribeToTopic() {
        Topic[] topics = {new Topic("tcc_light_control_infnet", QoS.AT_LEAST_ONCE)};
        connection.subscribe(topics, new Callback<byte[]>() {
            public void onSuccess(byte[] qoses) {
                // The result of the subcribe request.
                System.out.println("MQQT> Subscribed succesfully to topic tcc_light_control_infnet");
                publishCommand("raspberry_pi_im_alive");
            }

            public void onFailure(Throwable value) {
                System.out.println("MQQT> Failure to subscribe on topic.");
            }
        });
    }

    private static void publishCommand(String command) {
        // Send a message to a topic
        connection.publish("tcc_light_control_infnet", command.getBytes(), QoS.AT_LEAST_ONCE, false, new Callback<Void>() {
            public void onSuccess(Void v) {
                // the pubish operation completed successfully.
                System.out.println("MQQT> Sent command '" + command + "'");
            }

            public void onFailure(Throwable value) {
                System.out.println("MQQT> Failure to publish on topic.");
            }
        });
    }

    private void disconnect() {
        // To disconnect..
        connection.disconnect(new Callback<Void>() {
            public void onSuccess(Void v) {
                // called once the connection is disconnected.
                System.out.println("MQQT> Disconnected.");
            }

            public void onFailure(Throwable value) {
                // Disconnects never fail.
            }
        });
    }
}