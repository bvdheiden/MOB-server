package mob.server.networking.mqtt;

import mob.server.networking.MQTTClient;

public class BattleDevice extends Device {
    private AbortListener abortListener;
    private FinishListener finishListener;

    public BattleDevice(String id, MQTTClient mqttClient) {
        super(id, mqttClient);

        mqttClient.addSubscription(getTopic(), bytes -> {
            String[] payload = new String(bytes).split(":");

            String type = payload[0];

            if (type.equals("abort") && abortListener != null) {
                abortListener.onAbort();
            } else if (type.equals("finish") && finishListener != null) {
                int p1Wins = Integer.parseInt(payload[1]);
                int p2Wins = Integer.parseInt(payload[2]);

                finishListener.onFinished(p1Wins, p2Wins);
            }
        });
    }

    public void sendReady() {
        publish("ready");
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
        void onFinished(int p1Wins, int p2Wins);
    }
}
