import java.util.*;
import java.util.stream.Collectors;

public class HandEval {

    public static PokerHand evaluate(List<Card> cards) {
        if (cards.size() < 5) throw new IllegalArgumentException("Se requieren al menos 5 cartas");

        List<Card> bestHand = new ArrayList<>();
        String handType = "High card";

        // Agrupaciones por palo y valor
        Map<Card.Rank, List<Card>> rankGroups = cards.stream().collect(Collectors.groupingBy(Card::getRank));
        Map<Card.Suit, List<Card>> suitGroups = cards.stream().collect(Collectors.groupingBy(Card::getSuit));

        // Verificar color
        List<Card> flush = suitGroups.values().stream().
                filter(g -> g.size() >= 5).
                findFirst().
                orElse(null);
        if (flush != null) {
            flush.sort(Comparator.comparing(Card::getRank).reversed());
            bestHand = flush.subList(0, 5);
            handType = "Flush";
        }

        // Verificar escaler
        List<Card> sorted = new ArrayList<>(cards);
        sorted.sort(Comparator.comparing(Card::getRank));
        List<Card.Rank> uniqueRanks = sorted.stream().
                map(Card::getRank)
                .distinct()
                .toList();

        List<Card.Rank> ranks = Arrays.asList(Card.Rank.values());
        for (int i = 0; i <= uniqueRanks.size() - 5; i++) {
            int start = ranks.indexOf(uniqueRanks.get(i));
            boolean straight = true;
            for (int j = 1; j < 5; j++) {
                if (i + j >= uniqueRanks.size() || ranks.indexOf(uniqueRanks.get(i + j)) != start + j) {
                    straight = false;
                    break;
                }
            }
            if (straight) {
                handType = "Straight";
                List<Card.Rank> needed = uniqueRanks.subList(i, i + 5);
                bestHand = sorted.stream()
                        .filter(c -> needed.contains(c.getRank()))
                        .limit(5)
                        .toList();
            }
        }

        // Trios y parejas
        List<Card> pairs = new ArrayList<>();
        List<Card> trips = new ArrayList<>();

        for (List<Card> group: rankGroups.values()) {
            if (group.size() == 3) {
                trips.addAll(group);
            } else if (group.size() == 2) {
                pairs.addAll(group);
            }
        }
        if (!trips.isEmpty()) {
            handType = "Three of a Kind";
            bestHand = trips.subList(0, 3);
        } else if (pairs.size() >= 4) {
            handType = "Two Pair";
            bestHand = pairs.subList(0, 4);
        } else if (pairs.size() >=2) {
            handType = "Pair";
            bestHand = pairs.subList(0, 2);
        }

        // Si no hay nada de lo anterior, tomamos la carta mas alta
        if (bestHand.isEmpty()) {
            cards.sort(Comparator.comparing(Card::getRank).reversed());
            bestHand = cards.subList(0, 1);
        }

        return new PokerHand(handType, bestHand);
    }
}
