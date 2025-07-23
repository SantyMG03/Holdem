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

        // Poker
        for (List<Card> group: rankGroups.values()) {
            if (group.size() == 4) {
                List<Card> best = new ArrayList<>(group);
                best.add(highestExcluding(cards, group));
                return new PokerHand("Four of a Kind", best);
            }
        }

        // Full house
        List<List<Card>> trios = rankGroups.values().stream()
                .filter(g -> g.size() == 3)
                .sorted((a, b) -> b.getFirst().getRank().getValue() - a.getFirst().getRank().getValue())
                .toList();

        List<List<Card>> pairs = rankGroups.values().stream()
                .filter(g -> g.size() == 2)
                .sorted((a, b) -> b.getFirst().getRank().getValue() - a.getFirst().getRank().getValue())
                .toList();

        if (!trios.isEmpty() && (!pairs.isEmpty() || trios.size() >= 2)) {
            List<Card> full = new ArrayList<>(trios.getFirst());
            if (trios.size() >= 2) {
                full.addAll(trios.get(1).subList(0, 2));
            } else {
                full.addAll(pairs.getFirst());
            }
            return new PokerHand("Full House", full);
        }

        // Flush (Color) en caso de que no haya escalera de color
        if (flush != null) {
            flush.sort((a, b) -> b.getRank().getValue() - a.getRank().getValue());
            return new PokerHand("Flush", flush.subList(0, 5));
        }

        // Straight (Escalera) en caso de que no hubiese escalera de color
        if (straight != null) {
            return new PokerHand("Straight", straight);
        }

        // Trio
        if (!trios.isEmpty()) {
            List<Card> best = new ArrayList<>(trios.getFirst());
            best.addAll(topKExcluding(cards, best, 2));
            return new PokerHand("Three of a Kind", best);
        }

        // Doble pareja
        if (pairs.size() >= 2) {
            List<Card> best = new ArrayList<>();
            best.addAll(pairs.getFirst());
            best.addAll(pairs.get(1));
            best.add(highestExcluding(cards, best));
            return new PokerHand("Two pair", best);
        }

        // Pareja
        if (pairs.size() >= 1) {
            List<Card> best = new ArrayList<>(pairs.getFirst());
            best.addAll(topKExcluding(cards, best, 3));
            return new PokerHand("Pair", best);
        }

        // Carta mas alta
        List<Card> sortedDesc = new ArrayList<>(cards);
        sortedDesc.sort((a, b) -> b.getRank().getValue() - a.getRank().getValue());
        return new PokerHand("High Card", sortedDesc.subList(0, 1));
    }

    /**
     * Funcion que devuelve una lista con las k-cartas mas altas
     * @param pool total de cartas
     * @param exclude cartas ya usadas
     * @param k longitud de la lista final
     * @return una lista de cartas
     */
    private static List<Card> topKExcluding(List<Card> pool, List<Card> exclude, int k) {
        return pool.stream()
                .filter(c -> !exclude.contains(c))
                .sorted((a, b) -> b.getRank().getValue() - a.getRank().getValue())
                .limit(k)
                .toList();
    }

    /**
     * Funcion que busca la carta mas alta que no esta en exclude
     * @param pool total de cartas
     * @param exclude cartas ya usadas
     * @return la carta mas alta
     *
     * Ejemplo adicional:   Tengo poker de Ases pero se necesita
     *                      una mano de 5 cartas, entonces agrego
     *                      la mas alta restante
     */
    private static Card highestExcluding(List<Card> pool, List<Card> exclude) {
        return pool.stream().
                filter(c -> !exclude.contains(c)).
                max(Comparator.comparingInt(c -> c.getRank().getValue()))
                .orElse(null);
    }

    /**
     * Funcion que devuelve una escalera dado un conjunto de cartas de entrada
     * @param cards conjunto de cartas a comprobar
     * @return la escalera si existe o null en caso contrario
     */
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
        // Si efectivamente tiene el As y los valores 2, 3, 4, 5 -> devuelvo la lista
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
