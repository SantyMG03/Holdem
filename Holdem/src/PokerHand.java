import java.util.*;

public class PokerHand implements Comparable<PokerHand> {

    private final String handType;
    private final List<Card> cards;

    public PokerHand(String handType, List<Card> cards) {
        this.handType = handType;
        this.cards = cards;
    }

    public String getHandType() {
        return handType;
    }

    public List<Card> getCards() {
        return cards;
    }

    @Override
    public int compareTo(PokerHand other) {
        return this.handType.compareTo(other.handType);
    }

    @Override
    public String toString() {
        return handType + ": " + cards;
    }
}
