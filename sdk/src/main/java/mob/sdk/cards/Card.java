package mob.sdk.cards;

import java.io.Serializable;

public class Card implements Serializable, Comparable<Card> {
    private final CardType cardType;
    private final String id;

    public Card(CardType cardType, String id) {
        this.cardType = cardType;
        this.id = id;
    }

    public CardType getCardType() {
        return cardType;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Card{" +
                "cardType=" + cardType +
                ", id='" + id + '\'' +
                '}';
    }

    @Override
    public int compareTo(Card o) {
        switch (cardType) {
            case BOW:
                if (o.cardType == CardType.SWORD)
                    return -1;
                if (o.cardType == CardType.SHIELD)
                    return 1;
                break;
            case SWORD:
                if (o.cardType == CardType.BOW)
                    return 1;
                if (o.cardType == CardType.SHIELD)
                    return -1;
                break;
            case SHIELD:
                if (o.cardType == CardType.BOW)
                    return -1;
                if (o.cardType == CardType.SWORD)
                    return 1;
                break;
        }

        return 0;
    }
}
