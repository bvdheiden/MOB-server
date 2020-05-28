package mob.sdk.networking.payloads;

import java.io.Serializable;

public class CardRequestInvalid implements Serializable {
    private String code;

    public CardRequestInvalid(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
