package mob.sdk.networking.payloads;

import java.io.Serializable;

public class CardRequest implements Serializable {
    private final String cardCode;

    public CardRequest(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getCardCode() {
        return cardCode;
    }
}
