package mob.sdk.cards;

import java.io.Serializable;

public class Card implements Serializable {
    private final CardType cardType;
    private final String id;
    private final String name;

    public Card(CardType cardType, String id, String name) {
        this.cardType = cardType;
        this.id = id;
        this.name = name;
    }

    public CardType getCardType() {
        return cardType;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Card{" +
                "cardType=" + cardType +
                ", id='" + id + '\'' +
                '}';
    }
}
