package mob.sdk.networking.payloads;

import mob.sdk.cards.Card;

import java.io.Serializable;

public class CardResult implements Serializable {
    private Card card;

    public CardResult(Card card) {
        this.card = card;
    }

    public Card getCard() {
        return card;
    }
}
