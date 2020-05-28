package mob.server.networking.mqtt;

import mob.server.networking.MQTTClient;

public abstract class Device {
    private final String id;
    private final MQTTClient mqttClient;

    public Device(String id, MQTTClient mqttClient) {
        this.id = id;
        this.mqttClient = mqttClient;
    }

    public void publish(String payload) {
        mqttClient.publish(String.format("%sdevice/%s", MQTTClient.TOPIC_PREFIX, id), payload.getBytes());
    }

    public String getTopic() {
        return String.format("%sdevice/%s", MQTTClient.TOPIC_PREFIX, id);
    }
}
