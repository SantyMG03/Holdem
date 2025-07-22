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

        // Detectar escalera
        List<Card> sorted = new ArrayList<>(cards);
        sorted.sort(Comparator.comparing(Card::getRank));
        List<Card.Rank> uniqueRanks = sorted.stream().
                map(Card::getRank)
                .distinct()
                .toList();

        List<Card> straight = findStraight(sorted);

        // Verificar color o royal flush
        if (flush != null) {
            List<Card> flushSorted = new ArrayList<>(flush);
            flushSorted.sort(Comparator.comparingInt(c -> c.getRank().getValue()));
            List<Card> straightFlush = findStraight(flushSorted);

            if (straightFlush != null) {
                boolean isRoyal = straightFlush.stream()
                        .anyMatch(c -> c.getRank() == Card.Rank.ACE) &&
                        straightFlush.getFirst().getRank().getValue() == 10;

                return new PokerHand(isRoyal ? "Royal Flush" : "Straight Flush", straightFlush);
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

    private static List<Card> findStraight(List<Card> cards) {
        List<Card> unique = cards.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Card::getRank))),
                        ArrayList::new));
        if (unique.size() < 5) return null;

        List<Card> straigth = new ArrayList<>();
        for (int i = 0; i < unique.size(); i++) {
            straigth.clear();
            straigth.add(unique.get(i));
            int expected = unique.get(i).getRank().getValue() + 1;

            for (int j = 0; j < unique.size() && straigth.size() < 5; j++) {
                int current = unique.get(j).getRank().getValue();
                if (current == expected) {
                    straigth.add(unique.get(j));
                    expected ++;
                } else if (current > expected) break;
            }
            if (straigth.size() >= 5)
                return straigth.subList(0, 5);
        }

        // Hasta este punto detecta todas las escaleras menos la escalera baja -> A-2-3-4-5
        boolean hasAce = unique.stream().anyMatch(c -> c.getRank() == Card.Rank.ACE);
        boolean hasLowStraight = unique.stream().map(c -> c.getRank().getValue()).
                collect(Collectors.toSet()).
                containsAll(Arrays.asList(2, 3, 4, 5));
        // Si efectivamente tiene el As y los valores 2, .., 5 devuelvo la lista
        if (hasAce && hasLowStraight) {
            List<Card> lowStraight = unique.stream()
                    .filter(c -> c.getRank().getValue() == 2 || c.getRank().getValue() ==3 ||
                                      c.getRank().getValue() == 4 || c.getRank().getValue() == 5 ||
                                      c.getRank() == Card.Rank.ACE)
                    .toList();
            return lowStraight.size() >= 5 ? lowStraight.subList(0, 5) : null;
        }

        return null;
    }
}
