package mob.server.networking.mqtt;

import mob.sdk.cards.CardRepository;
import mob.sdk.networking.SocketClient;
import mob.server.networking.MqttClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CardDevice extends Device {
    private static final int CAPACITY = 1;

    private String cardId;
    private String cardCode;
    private final List<SocketClient> clientHistoryList = new ArrayList<>();
    private final AtomicInteger cardsLeft = new AtomicInteger(0);

    public CardDevice(String id, MqttClient mqttClient) {
        super(id, mqttClient);
    }

    public String getCardId() {
        return cardId;
    }

    public String getCardCode() {
        return cardCode;
    }

    public AtomicInteger getCardsLeft() {
        return cardsLeft;
    }

    public void setCard(String cardId, String cardCode) {
        this.cardId = cardId;
        this.cardCode = cardCode;
        clientHistoryList.clear();
        cardsLeft.set(CAPACITY);

        String cardName = CardRepository.INSTANCE.getCard(cardId).getName();

        mqttClient.publish(getTopic(), String.format("new:%s:%s:%d", cardName, cardCode, CAPACITY).getBytes());
    }

    /**
     * Set the current card claimed for client.
     * @param client client
     */
    public void setClaimed(SocketClient client) {
        clientHistoryList.add(client);
        cardsLeft.set(cardsLeft.get() - 1);

        publish("decreased");
    }

    /**
     * Check whether the client has claimed the current card.
     * @param client client
     */
    public boolean isClaimed(SocketClient client) {
        return clientHistoryList.contains(client);
    }
}
