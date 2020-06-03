package mob.server.networking.mqtt;

import mob.server.networking.MqttClient;

public class CardDevice extends Device {
    public CardDevice(String id, MqttClient mqttClient) {
        super(id, mqttClient);
    }
}
