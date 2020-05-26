package mob.sdk.cards;

import java.io.Serializable;

public class Card implements Serializable {
    private CardType cardType;
    private CardIdentifier identifier;

    public Card(CardType cardType,CardIdentifier cardIdentifier) {
        this.cardType = cardType;
        this.identifier = identifier;
    }

    /**
     * attacks the other player's card
     * @param other the other player's card
     * @return 0 if it was a draw, 1 if you won and -1 if you lost
     */
    public int attack(Card other) {
        int res = 0;
        if (other != null) {
            if (other.getCardType() != null) {
                if (other.getCardType() == this.getCardType()) {
                    return 0;
                }

                switch (this.cardType) {
                    case BOW -> {
                        if (other.getCardType() == CardType.SWORD) {
                            return -1;
                        } else if (other.getCardType() == CardType.SHIELD) {
                            return 1;
                        }

                    }
                    case SWORD -> {
                        if (other.getCardType() == CardType.BOW) {
                            return 1;
                        } else if (other.getCardType() == CardType.SHIELD) {
                            return -1;
                        }
                    }
                    case SHIELD -> {
                        if (other.getCardType() == CardType.BOW) {
                            return -1;
                        } else if (other.getCardType() == CardType.SWORD) {
                            return 1;
                        }
                    }
                    default -> res = 0;
                }
            }
        }
        return res;
    }


    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public CardIdentifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(CardIdentifier identifier) {
        this.identifier = identifier;
    }
}
