package mob.server.networking;

import mob.sdk.networking.SocketClient;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class MOBServer {
    private final AtomicBoolean connecting = new AtomicBoolean(false);
    private final List<SocketClient> socketClientList = new CopyOnWriteArrayList<>();
    private ServerSocket serverSocket;


    /**
     * Start the server socket and start listening for clients.
     */
    public void start(int port) {
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
}
