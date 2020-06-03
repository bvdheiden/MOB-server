package mob.server.networking.mqtt;

import mob.server.networking.MqttClient;

public abstract class Device {
    protected final String id;
    protected final MqttClient mqttClient;

    public Device(String id, MqttClient mqttClient) {
        this.id = id;
        this.mqttClient = mqttClient;

        mqttClient.publish(getTopic(), "connected".getBytes());
    }

    public void publish(String payload) {
        mqttClient.publish(String.format("%sdevice/%s", MqttClient.TOPIC_PREFIX, id), payload.getBytes());
    }

    public String getTopic() {
        return String.format("%sdevice/%s", MqttClient.TOPIC_PREFIX, id);
    }
}
