import java.util.*;

public class HandEval {

    public static PokerHand evaluate(List<Card> cards) {
        Map<Card.Rank, Integer> countByRank = new HashMap<>();

        for (Card card: cards) {
            countByRank.put(card.getRank(), countByRank.getOrDefault(card.getRank(), 0) + 1);
        }

        for (Map.Entry<Card.Rank, Integer> entry: countByRank.entrySet()) {
            if (entry.getValue() == 2) {
                List<Card> pair = new ArrayList<>();
                for (Card c: cards) {
                    if (c.getRank() == entry.getKey()) pair.add(c);
                }
                return new PokerHand("Pair", pair);
            }
        }
        return new PokerHand("High Card", List.of(Collections.max(cards, Comparator.comparing(Card::getRank))));
    }
}
