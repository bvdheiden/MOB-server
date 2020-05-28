package mob.sdk.networking.payloads;

import java.io.Serializable;

public class CardRequest implements Serializable {
    private String code;

    public CardRequest(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
