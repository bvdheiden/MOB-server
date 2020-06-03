package mob.sdk.cards;

import java.util.*;

public enum CardRepository {
    INSTANCE;

    private final Map<String, Card> cardMap = new HashMap<>();

    CardRepository() {
        Card[] cards = new Card[]{
                new Card(CardType.BOW, "parfoes", "Parfoes de Nar"),
                new Card(CardType.SWORD, "kevin", "Knecht Kevin"),
                new Card(CardType.SHIELD, "barry", "Barbaar Barry"),
                new Card(CardType.BOW, "bolle", "Volle Bolle"),
                new Card(CardType.SWORD, "avanius", "Koning Avanius"),
                new Card(CardType.SHIELD, "mevrouw", "Mevrouw Madam"),
                new Card(CardType.BOW, "almanak", "Meester Almanak"),
                new Card(CardType.SWORD, "alicia", "Alicia Taart"),
                new Card(CardType.SHIELD, "pent", "Proffesor Pent"),
                new Card(CardType.BOW, "pardaan", "Princes Pardaan"),
                new Card(CardType.SWORD, "rat", "Rik de Rat")
            // Fairy tree
            new Card(CardType.BOW, "midget-ku", "Midget Ku"),
            new Card(CardType.SWORD, "klein-teentje", "Klein teentje"),
            new Card(CardType.SHIELD, "midget-kie", "Midget kie"),
            new Card(CardType.BOW, "midget-ko", "Midget Ko"),
            new Card(CardType.SWORD, "kleinnek", "Kleinnek"),
            new Card(CardType.SHIELD, "sprookjesplant", "Sprookjesplant"),
            new Card(CardType.BOW, "boze-hond", "Boze hond"),
            new Card(CardType.SWORD, "blauwkapje", "Blauwkapje"),
            new Card(CardType.SHIELD, "gigant", "Gigant"),
            new Card(CardType.BOW, "midget-kei", "Midget Kei"),
            new Card(CardType.SWORD, "paard", "Paard"),
            new Card(CardType.SHIELD, "tapijtmeester", "Tapijtmeester"),
            new Card(CardType.BOW, "bassiepoester", "Bassiepoester"),
            new Card(CardType.SWORD, "vliegend-figuur", "Vliegend figuur"),
            new Card(CardType.SHIELD, "konijn-benjamin", "Konijn Benjamin"),
            new Card(CardType.BOW, "tovervrouw", "Tovervrouw"),
            new Card(CardType.SWORD, "midget-ka", "Midget Ka"),
            new Card(CardType.SHIELD, "midget-ke", "Midget Ke"),
            new Card(CardType.BOW, "midget-keu", "Midget Keu"),
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

    public List<String> getCardIds() {
        return new ArrayList<>(cardMap.keySet());
    }

    public int getSize() {
        return this.cardMap.size();
    }
}
