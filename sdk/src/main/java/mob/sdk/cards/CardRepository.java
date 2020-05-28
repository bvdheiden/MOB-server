package mob.sdk.cards;

import java.util.HashMap;
import java.util.Map;

public enum CardRepository {
    INSTANCE;

    private final Map<String, Card> cardMap = new HashMap<>();

    CardRepository() {
        Card[] cards = new Card[]{
            new Card(CardType.BOW, "parfoes"),
            new Card(CardType.SWORD, "kevin"),
            new Card(CardType.SHIELD, "barry"),
        };

        for (Card card : cards) {
            cardMap.put(card.getId(), card);
        }
    }

    public Card getCard(String id) {
        return cardMap.get(id);
    }
}
