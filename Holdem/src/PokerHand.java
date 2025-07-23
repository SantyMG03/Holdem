import java.util.*;

public class PokerHand implements Comparable<PokerHand> {

    private final String handType;
    private final List<Card> cards;

    public PokerHand(String handType, List<Card> cards) {
        this.handType = handType;
        this.cards = new ArrayList<>(cards);
        this.cards.sort((a, b) -> b.getRank().getValue() - a.getRank().getValue());
    }

    public String getHandType() {
        return handType;
    }

    public List<Card> getCards() {
        return cards;
    }

    private static final Map<String, Integer> HAND_RANKS = Map.of(
            "High Card", 1,
            "Pair",2,
            "Two Pair", 3,
            "Three of a Kind", 4,
            "Straight", 5,
            "Flush", 6,
            "Full house", 7,
            "Four of a Kind", 8,
            "Straight Flush", 9,
            "Royal Flush", 10
    );

    @Override
    public int compareTo(PokerHand other) {
        int thisRank = HAND_RANKS.getOrDefault(this.handType, 0);
        int otherRank = HAND_RANKS.getOrDefault(other.handType, 0);

        // Si son 2 manos distintas, entonces gana la mas alta
        if (thisRank != otherRank) {
            return Integer.compare(thisRank, otherRank);
        }

        // En caso de que sean el mismo tipo de mano comparamos carta a carta
        for (int i = 0; i < Math.min(this.cards.size(), other.cards.size()); i++) {
            int cmp = Integer.compare(
                    this.cards.get(i).getRank().getValue(),
                    other.cards.get(i).getRank().getValue()
            );
            if (cmp != 0) return cmp;
        }

        return 0; // Empate total (No se deberia de dar casi nunca)
    }

    @Override
    public String toString() {
        return handType + ": " + cards;
    }
}
