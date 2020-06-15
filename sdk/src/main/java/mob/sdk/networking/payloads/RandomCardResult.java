package mob.sdk.networking.payloads;

import java.io.Serializable;

public class RandomCardResult implements Serializable {
    private String cardId;
    public RandomCardResult(String cardId) {
        this.cardId = cardId;
    }

    public String getCardId() {
        return cardId;
    }
}
