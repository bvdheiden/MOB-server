package mob.sdk.networking.payloads;

import java.io.Serializable;

public class CardRequestInvalid implements Serializable {
    private String cardCode;

    public CardRequestInvalid(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getCardCode() {
        return cardCode;
    }
}
