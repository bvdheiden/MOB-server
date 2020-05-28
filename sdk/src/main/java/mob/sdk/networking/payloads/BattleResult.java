package mob.sdk.networking.payloads;

import mob.sdk.cards.Card;

import java.io.Serializable;

public class BattleResult implements Serializable {
    private final int amountWon;
    private final int amountLost;
    private final Card cardWon;

    public BattleResult(int amountWon, int amountLost, Card cardWon) {
        this.amountWon = amountWon;
        this.amountLost = amountLost;
        this.cardWon = cardWon;
    }

    public int getAmountWon() {
        return amountWon;
    }

    public int getAmountLost() {
        return amountLost;
    }

    public Card getCardWon() {
        return cardWon;
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
