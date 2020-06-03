package mob.server.networking;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class MqttClient {
    public static final String TOPIC_PREFIX = "groep/a3/";

    private final AtomicBoolean connecting = new AtomicBoolean(false);

    private org.eclipse.paho.client.mqttv3.MqttClient client;

    private List<ConnectionListener> connectionListeners = new CopyOnWriteArrayList<>();
    private List<DisconnectionListener> disconnectionListeners = new CopyOnWriteArrayList<>();
    private Map<String, List<SubscriptionListener>> subscriptionListenerMap = new ConcurrentHashMap<>();

    public void start(String broker, String username, String password) {
        if (isRunning()) {
            return;
        }

        connecting.set(true);

        MqttConnectOptions connectOptions = new MqttConnectOptions();
        connectOptions.setUserName(username);
        connectOptions.setPassword(password.toCharArray());
        connectOptions.setCleanSession(true);

        new Thread(() -> {
            try {
                printf("Connecting to MQTT broker: %s.", String.format("tcp://%s", broker));
                this.client = new org.eclipse.paho.client.mqttv3.MqttClient(String.format("tcp://%s", broker), org.eclipse.paho.client.mqttv3.MqttClient.generateClientId(), new MemoryPersistence());
                client.connect(connectOptions);
                for (ConnectionListener listener : connectionListeners)
                    listener.onConnection();
                print("Connected to MQTT broker");
                for (Map.Entry<String, List<SubscriptionListener>> entry : subscriptionListenerMap.entrySet()) {
                    for (SubscriptionListener listener : entry.getValue()) {
                        client.subscribe(entry.getKey(), (topic, message) -> {
                            listener.onMessage(message.getPayload());
                        });
                    }
                }
                connecting.set(false);
            } catch (MqttSecurityException e) {
                print("Failed to connect: not authorized");
            } catch (MqttException e) {
                print("Failed to connect");
            } finally {
                connecting.set(false);
            }
        }).start();
    }

    public void stop() {
        if (!isRunning()) {
            return;
        }

        connecting.set(false);

        if (client != null) {
            try {
                client.disconnect();

                for (DisconnectionListener listener : disconnectionListeners)
                    listener.onDisconnection();

                print("Disconnected MQTT client.");
            } catch (MqttException e) {
                print("Failed to disconnect MQTT client.");
            }
        }
    }

    public boolean isRunning() {
        return connecting.get() || (client != null && client.isConnected());
    }

    public void addConnectionListener(ConnectionListener listener) {
        connectionListeners.add(listener);
    }

    public void addDisconnectionListener(DisconnectionListener listener) {
        disconnectionListeners.add(listener);
    }

    public void addSubscription(String topic, SubscriptionListener listener) {
        if (isRunning()) {
            try {
                client.subscribe(topic, (_topic, message) -> {
                    listener.onMessage(message.getPayload());
                });

                printf("Added listener for topic: %s", topic);
            } catch (MqttException e) {
                printf("Failed to add listener for topic: %s");
            }
        }

        if (!subscriptionListenerMap.containsKey(topic)) {
            subscriptionListenerMap.put(topic, new CopyOnWriteArrayList<>());
        }

        subscriptionListenerMap.get(topic).add(listener);
    }

    public void publish(String topic, byte[] payload) {
        if (!isRunning() || client == null) {
            return;
        }

        try {
            client.publish(topic, new MqttMessage(payload));
        } catch (MqttException e) {
            printf("Could not publish message on topic: %s with message: %s", topic, payload);
        }
    }

    public static void print(String string) {
        System.out.println("MQTT > " + string);
    }

    public static void printf(String string, Object... params) {
        System.out.printf("MQTT > " + string + "%n", params);
    }

    @FunctionalInterface
    public interface ConnectionListener {
        void onConnection();
    }

    @FunctionalInterface
    public interface DisconnectionListener {
        void onDisconnection();
    }

    @FunctionalInterface
    public interface SubscriptionListener {
        void onMessage(byte[] payload);
    }
}
