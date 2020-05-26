package mob.sdk.cards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

public class CardRepository {
    private static ArrayList<Card> cards;
    private static final int PARFOES = 0;

    public CardRepository() {
        cards = new ArrayList<>();
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public static void initCards() {
        cards.add(new Card(CardType.BOW, CardIdentifier.PARFOES));
        cards.add(new Card(CardType.SWORD,CardIdentifier.KEVIN));
        cards.add(new Card(CardType.SHIELD,CardIdentifier.BARRY));
        //TODO add more cards
    }

    public static Card get(Card card) {
        for (Card card1 : cards) {
            if (card1.equals(card)) {
                return card1;
            }
        }
        return null;
    }

    public static Card getCard(CardIdentifier identifier) {
        for (Card card : cards) {
            if (card.getIdentifier() == identifier) {
                return card;
            }
        }
        return null;
    }


}
