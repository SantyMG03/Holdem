import java.util.*;

public class Main {
    public static void main(String[] args) {
        Deck deck = new Deck();

        List<Card> player1 = List.of(deck.draw(), deck.draw());
        List<Card> player2 = List.of(deck.draw(), deck.draw());
        List<Card> communityCards = List.of(deck.draw(), deck.draw(), deck.draw(), deck.draw(), deck.draw());

        System.out.println("Cartas comunes: "+communityCards);

        List<Card> all1 = new ArrayList<>(player1);
        all1.addAll(communityCards);
        PokerHand hand1 = HandEval.evaluate(all1);

        List<Card> all2 = new ArrayList<>(player2);
        all2.addAll(communityCards);
        PokerHand hand2 = HandEval.evaluate(all2);

        System.out.println("Jugador 1: " +player1+ " -> " +hand1);
        System.out.println("Jugador 2: " +player2+ " -> " +hand2);

        int res = hand1.compareTo(hand2);

        if (res > 0) System.out.println("Gana el jugador 1");
        else if (res < 0) System.out.println("Gana el jugador 2");
        else System.out.println("Empate");
    }
}
