package mob.sdk.cards;

import java.io.Serializable;

public class Card implements Serializable {
    private CardType cardType;
    private String title;
    private String fact;
    private int androidImageResource;

    public Card(CardType cardType, String title, String fact, int androidImageResource) {
        this.title = title;
        this.cardType = cardType;
        this.fact = fact;
        this.androidImageResource = androidImageResource;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFact() {
        return fact;
    }

    public void setFact(String fact) {
        this.fact = fact;
    }

    public int getAndroidImageResource() {
        return androidImageResource;
    }

    public void setAndroidImageResource(int androidImageResource) {
        this.androidImageResource = androidImageResource;
    }
}
