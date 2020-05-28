package mob.sdk.networking.payloads;

import java.io.Serializable;

public class BattleResult implements Serializable {
    private final int amountWon;
    private final int amountLost;

    public BattleResult(int amountWon, int amountLost) {
        this.amountWon = amountWon;
        this.amountLost = amountLost;
    }

    public int getAmountWon() {
        return amountWon;
    }

    public int getAmountLost() {
        return amountLost;
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
