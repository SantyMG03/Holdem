import java.util.*;

public class Main {
    public static void main(String[] args) {
        Deck deck = new Deck();

        List<Card> playerCards = List.of(deck.draw(), deck.draw());
        List<Card> communityCards = List.of(deck.draw(), deck.draw(), deck.draw(), deck.draw(), deck.draw());

        List<Card> allCards = new ArrayList<>(playerCards);
        allCards.addAll(communityCards);

        PokerHand hand = HandEval.evaluate(allCards);

        System.out.println("Tus cartas: " + playerCards);
        System.out.println("Comunitarias: " + communityCards);
        System.out.println("Mejor jugada: " + hand);
    }
}
