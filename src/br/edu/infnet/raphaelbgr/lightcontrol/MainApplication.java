package br.edu.infnet.raphaelbgr.lightcontrol;

import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;

import java.net.URISyntaxException;

public class MainApplication {

    public static void main(String[] args) {
        try {
            MQTT mqtt = new MQTT();
            mqtt.setKeepAlive((short) 12000);
            mqtt.setHost("tcp://iot.eclipse.org:1883");
            mqtt.setWillQos(QoS.AT_LEAST_ONCE);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}