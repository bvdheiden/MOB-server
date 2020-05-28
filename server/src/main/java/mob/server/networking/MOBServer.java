package mob.server.networking;

import mob.sdk.networking.LoggingCallback;
import mob.sdk.networking.SocketClient;
import mob.server.networking.mqtt.BattleDevice;
import mob.server.networking.mqtt.CardDevice;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class MOBServer implements LoggingCallback {
    private final AtomicBoolean connecting = new AtomicBoolean(false);
    private final AtomicBoolean mqttConnected = new AtomicBoolean(false);
    private final List<SocketClient> socketClientList = new CopyOnWriteArrayList<>();

    private final Map<String, BattleDevice> battleDeviceMap = new ConcurrentHashMap<>();
    private final Map<String, CardDevice> cardDeviceMap = new ConcurrentHashMap<>();

    private ServerSocket serverSocket;

    public MOBServer(MQTTClient mqttClient) {
        mqttClient.addSubscription(MQTTClient.TOPIC_PREFIX + "connect", bytes -> {
            String[] payload = new String(bytes).split(":");

            String deviceType = payload[0];
            String deviceId = payload[1];

            switch (deviceType) {
                case "battle":
                    BattleDevice battleDevice = new BattleDevice(deviceId, mqttClient);

                    battleDeviceMap.put(deviceId, battleDevice);
                    break;
                case "card":
                    CardDevice cardDevice = new CardDevice(deviceId, mqttClient);

                    cardDeviceMap.put(deviceId, cardDevice);
                    break;
            }
        });

        mqttClient.addConnectionListener(() -> {
            mqttConnected.set(true);
        });

        mqttClient.addDisconnectionListener(() -> {
            mqttConnected.set(false);
        });
    }

    /**
     * Start the server socket and start listening for clients.
     */
    public void start(int port) {
        SocketClient.addLoggingCallback(this);

        if (isRunning()) {
            return;
        }

        connecting.set(true);

        new Thread(() -> {
            try {
                SocketClient.print("Creating server socket.");
                this.serverSocket = new ServerSocket(port);
                while (connecting.get()) {
                    try {
                        SocketClient.print("Waiting for clients.");
                        Socket socket = serverSocket.accept();
                        SocketClient.print("Client accepted.");
                        SocketClient client = new SocketClient(socket);
                        socketClientList.add(client);

                        client.addTransactionListener((transaction) -> {

                        });

                        client.addDisconnectionListener(() -> {

                        });
                    } catch (IOException exception) {
                        SocketClient.print("Failed to accept and create client.");
                    }
                }
            } catch (IOException exception) {
                SocketClient.print("Failed to create server socket.");
            }
        }).start();
    }

    /**
     * Stop the server socket and stop listening for clients.
     */
    public void stop() {
        if (!isRunning()) {
            return;
        }

        connecting.set(false);

        for (SocketClient serverClient : socketClientList) {
            serverClient.stop();
        }

        try {
            serverSocket.close();
        } catch (IOException exception) {
            SocketClient.print("Failed to close server socket.");
        }
    }

    public boolean isRunning() {
        return connecting.get() || (serverSocket != null && !serverSocket.isClosed());
    }

    @Override
    public void print(String string) {
        System.out.println(string);
    }

    @Override
    public void printf(String string, Object... params) {
        System.out.printf(string + "%n", params);
    }
}
