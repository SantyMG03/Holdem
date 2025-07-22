import java.util.*;

public class Deck {
    private final List<Card> cards = new ArrayList<>();

    /**
     * Constructor para generar un mazo completo
     */
    public Deck() {
        for (Card.Suit suit: Card.Suit.values()) {
            for (Card.Rank rank: Card.Rank.values()) {
                cards.add(new Card(rank, suit));
            }
        }
        shuffle();
    }

    /**
     * Metodo para barajar el mazo
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Metodo para sacar la primera carta y la borra del total del mazo
     * @return la primera carta
     */
    public Card draw() {
        if (cards.isEmpty()) throw new IllegalStateException("No cards left in the deck");
        return cards.removeFirst();
    }
}
