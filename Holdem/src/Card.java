public class Card {
    public enum Suit { HEARTS, DIAMONDS, CLUBS, SPADES }
    public enum Rank {
        TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN,
        JACK, QUEEN, KING, ACE
    }

    private final Suit suit;
    private final Rank rank;

    /**
     * Constructor de cartas
     * @param rank Designado para el valor de la carta (1, ..., 10, J, Q,K)
     * @param suit Designado para el palo (Corazones, Diamantes, Treboles, Picas)
     */
    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public String toString() {
        return rank + " of " + suit;
    }
}
