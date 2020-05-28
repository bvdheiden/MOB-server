package mob.sdk.cards;

import java.util.*;

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

    public String getRandomId() {
        List<String> cardIds = new ArrayList<>(cardMap.keySet());

        Random random = new Random();

        return cardIds.get(random.nextInt(cardIds.size() - 1));
    }

    public int getSize() {
        return this.cardMap.size();
    }
}
