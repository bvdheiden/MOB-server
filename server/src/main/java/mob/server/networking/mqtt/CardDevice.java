package mob.server.networking.mqtt;

import mob.sdk.networking.SocketClient;
import mob.server.networking.MqttClient;

import java.util.ArrayList;
import java.util.List;

public class CardDevice extends Device {
    private String cardId;
    private String cardCode;
    private final List<SocketClient> clientHistoryList = new ArrayList<>();

    public CardDevice(String id, MqttClient mqttClient) {
        super(id, mqttClient);
    }

    public String getCardId() {
        return cardId;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCard(String cardId, String cardCode) {
        this.cardId = cardId;
        this.cardCode = cardCode;
        clientHistoryList.clear();

        mqttClient.publish(getTopic(), cardCode.getBytes());
    }

    /**
     * Set the current card claimed for client.
     * @param client client
     */
    public void setClaimed(SocketClient client) {
        clientHistoryList.add(client);
    }

    /**
     * Check whether the client has claimed the current card.
     * @param client client
     */
    public boolean isClaimed(SocketClient client) {
        return clientHistoryList.contains(client);
    }
}
