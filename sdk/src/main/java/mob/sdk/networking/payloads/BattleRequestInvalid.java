package mob.sdk.networking.payloads;

import java.io.Serializable;

public class BattleRequestInvalid implements Serializable {
    private final REASON reason;

    public BattleRequestInvalid(REASON reason) {
        this.reason = reason;
    }

    public REASON getReason() {
        return reason;
    }

    public enum REASON {
        DEVICE_ID_WRONG,
        TEAM_ALREADY_TAKEN,
        ALREADY_PLAYING
    }
}
