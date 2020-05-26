package mob.sdk.cards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

public class CardRepository {
    private ArrayList<Card> cards;
    private static final int PARFOES = 0;

    public CardRepository() {
        this.cards = new ArrayList<>();
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void addCard(Card card) {
        if (card != null && this.cards != null)
            this.cards.add(card);
    }

    public void addCards(Card... cards) {
        List<Card> list = Arrays.asList(cards);
        if (!list.contains(null))
            this.cards.addAll(list);
    }

    public void initCards() {
        this.cards.add(new Card(CardType.BOW, CardIdentifier.PARFOES));
        this.cards.add(new Card(CardType.SWORD,CardIdentifier.KEVIN));
        this.cards.add(new Card(CardType.SHIELD,CardIdentifier.BARRY));
    }

    public Card get(Card card) {
        for (Card card1 : cards) {
            if (card1.equals(card)) {
                return card1;
            }
        }
        return null;
    }

    public Card getCard(CardIdentifier identifier) {
        for (Card card : cards) {
            if (card.getIdentifier() == identifier) {
                return card;
            }
        }
        return null;
    }


}
