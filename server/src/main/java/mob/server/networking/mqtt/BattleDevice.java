package mob.server.networking.mqtt;

import mob.sdk.networking.SocketClient;
import mob.sdk.networking.payloads.BattleRequest;
import mob.server.networking.MQTTClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class BattleDevice extends Device {
    private final AtomicBoolean isPlaying = new AtomicBoolean(false);
    private final Map<BattleRequest.Color, SocketClient> clientMap = new ConcurrentHashMap<>();

    private AbortListener abortListener;
    private FinishListener finishListener;

    public BattleDevice(String id, MQTTClient mqttClient) {
        super(id, mqttClient);

        mqttClient.addSubscription(getTopic(), bytes -> {
            if (!isPlaying.get()) {
                return;
            }

            String[] payload = new String(bytes).split(":");

            String type = payload[0];

            if (type.equals("abort") && abortListener != null) {
                abortListener.onAbort();
            } else if (type.equals("finish") && finishListener != null) {
                int redWins = Integer.parseInt(payload[1]);
                int blueWins = Integer.parseInt(payload[2]);

                SocketClient redClient = clientMap.get(BattleRequest.Color.RED);
                SocketClient blueClient = clientMap.get(BattleRequest.Color.BLUE);

                finishListener.onFinished(redWins, redClient, blueWins, blueClient);
            }
        });
    }

    public boolean setClient(BattleRequest.Color teamColor, SocketClient client) {
        if (clientMap.containsKey(teamColor))
            return false;

        clientMap.put(teamColor, client);
        return true;
    }

    public boolean isReady() {
        return clientMap.size() == BattleRequest.Color.values().length;
    }

    public void reset() {
        isPlaying.set(false);
        clientMap.clear();
        abortListener = null;
        finishListener = null;
    }

    public void sendReady() {
        isPlaying.set(true);

        publish(String.format("ready:%d,%d,%d|%d,%d,%d", 1, 1, 1, 1, 1, 1));
    }

    public AtomicBoolean isPlaying() {
        return isPlaying;
    }

    public void setOnAbort(AbortListener listener) {
        this.abortListener = listener;
    }

    public void setOnFinish(FinishListener listener) {
        this.finishListener = listener;
    }

    @FunctionalInterface
    public interface AbortListener {
        void onAbort();
    }

    @FunctionalInterface
    public interface FinishListener {
        void onFinished(int redWins, SocketClient redClient, int blueWins, SocketClient blueClient);
    }
}
