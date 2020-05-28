package mob.server.networking.mqtt;

import mob.server.networking.MQTTClient;

public class CardDevice extends Device {
    public CardDevice(String id, MQTTClient mqttClient) {
        super(id, mqttClient);
    }
}
