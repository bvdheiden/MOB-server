package mob.sdk.networking.payloads;

import java.io.Serializable;

public class CardResult implements Serializable {
    private String cardId;

    public CardResult(String cardId) {
        this.cardId = cardId;
    }

    public String getCardId() {
        return cardId;
    }
}
