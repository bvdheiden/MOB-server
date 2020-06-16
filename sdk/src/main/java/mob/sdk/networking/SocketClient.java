package mob.sdk.networking;

import mob.sdk.networking.listeners.DisconnectionListener;
import mob.sdk.networking.listeners.TransactionListener;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SocketClient {
    private static LoggingCallback loggingCallback;

    private final Socket socket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    private final List<TransactionListener> transactionListenerList = new CopyOnWriteArrayList<>();
    private final List<DisconnectionListener> disconnectionListenerList = new CopyOnWriteArrayList<>();

    public SocketClient(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());

        new Thread(() -> {
            while (socket.isConnected() && !socket.isClosed()) {
                try {
                    Transaction transaction = (Transaction) in.readObject();

                    printf("Received transaction %s with payload: %s from client: %s.", transaction.getType(), transaction.getPayload(), hashCode());

                    for (TransactionListener listener : transactionListenerList) {
                        listener.onTransaction(transaction);
                    }
                } catch (IOException | ClassNotFoundException exception) {
                    printf("Failed to receive data from client: %s.", hashCode());
                    stop();
                }
            }

            printf("Receiving thread died for client: %s.", hashCode());
        }).start();
    }

    public static void print(String string) {
        if (loggingCallback != null) {
            loggingCallback.print("Network > " + string);
        }
    }

    public static void printf(String string, Object... parameters) {
        if (loggingCallback != null) {
            loggingCallback.printf("Network > " + string, parameters);
        }
    }

    public static void addLoggingCallback(LoggingCallback callback) {
        loggingCallback = callback;
    }

    public void send(Transaction transaction) {
        send(transaction, null, null);
    }

    public void send(Transaction transaction, SuccessListener successListener, FailureListener failureListener) {
        if (!socket.isConnected() || socket.isClosed()) {
            return;
        }

        try {
            out.writeObject(transaction);
            printf("send transaction %s with payload: %s to client: %s.", transaction.getType(), transaction.getPayload(), hashCode());
            if (successListener != null)
                successListener.onSuccess();
        } catch (IOException exception) {
            printf("Failed to send data to client: %s.", hashCode());
            if (failureListener != null)
                failureListener.onFailure();
            stop();
        }
    }

    public void addTransactionListener(TransactionListener listener) {
        transactionListenerList.add(listener);
    }

    public void addDisconnectionListener(DisconnectionListener listener) {
        disconnectionListenerList.add(listener);
    }

    public void stop() {
        if (socket.isClosed()) {
            printf("Socket of client: %s is already closed.", hashCode());
            return;
        }

        printf("Closing socket for client: %s.", hashCode());

        try {
            socket.close();
        } catch (IOException exception) {
            printf("Failed to close socket for client: %s.", hashCode());
        }

        for (DisconnectionListener listener : disconnectionListenerList) {
            listener.onDisconnection();
        }
    }

    @FunctionalInterface
    public interface SuccessListener {
        void onSuccess();
    }

    @FunctionalInterface
    public interface FailureListener {
        void onFailure();
    }
}
