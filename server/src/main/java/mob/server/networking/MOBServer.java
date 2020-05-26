package mob.server.networking;

import mob.sdk.networking.LoggingCallback;
import mob.sdk.networking.SocketClient;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class MOBServer implements LoggingCallback {
    private final AtomicBoolean connecting = new AtomicBoolean(false);
    private final AtomicBoolean mqttConnected = new AtomicBoolean(false);
    private final List<SocketClient> socketClientList = new CopyOnWriteArrayList<>();
    private final MQTTClient mqttClient;
    private ServerSocket serverSocket;

    public MOBServer(MQTTClient mqttClient) {
        this.mqttClient = mqttClient;

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
