package mob.sdk.networking.payloads;

import java.io.Serializable;

public class BattleResult implements Serializable {
    private final int amountWon;
    private final int amountLost;
    private final String cardId;

    public BattleResult(int amountWon, int amountLost, String cardId) {
        this.amountWon = amountWon;
        this.amountLost = amountLost;
        this.cardId = cardId;
    }

    public int getAmountWon() {
        return amountWon;
    }

    public int getAmountLost() {
        return amountLost;
    }

    public String getCardId() {
        return cardId;
    }

    public boolean hasWon() {
        return amountWon > amountLost;
    }

    public boolean hasLost() {
        return amountLost > amountWon;
    }

    public boolean isDraw() {
        return amountWon == amountLost;
    }
}
