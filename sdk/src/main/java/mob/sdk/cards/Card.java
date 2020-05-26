package mob.sdk.cards;

import java.io.Serializable;

public class Card implements Serializable, Comparable<Card> {
    private final CardType cardType;
    private final CardIdentifier identifier;

    public Card(CardType cardType, CardIdentifier identifier) {
        this.cardType = cardType;
        this.identifier = identifier;
    }

    public CardType getCardType() {
        return cardType;
    }

    public CardIdentifier getIdentifier() {
        return identifier;
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
                    return -1;
                if (o.cardType == CardType.SHIELD)
                    return 1;
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
