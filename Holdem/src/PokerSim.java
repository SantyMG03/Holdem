import java.util.ArrayList;
import java.util.List;

public class PokerSim {
    private final int numPlayers;
    private final Deck deck;

    public PokerSim (int numPlayers) {
        if (numPlayers < 2 || numPlayers > 9)
            throw new IllegalArgumentException("El numero de jugadores debe ser entre 2 y 9");
        this.numPlayers = numPlayers;
        this.deck = new Deck();
    }

    public void playRound() {
        deck.shuffle();

        // Reparto de cartas privadas
        List<List<Card>> playerCards = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            List<Card> hand = new ArrayList<>();
            hand.add(deck.draw());
            hand.add(deck.draw());
            playerCards.add(hand);
        }

        // Reparto de cartas comunitarias
        List<Card> communityCards = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            communityCards.add(deck.draw());
        }

        System.out.println("Cartas comunitarias: " + communityCards);

        // Evalua manos de jugadores
        List<PokerHand> hands = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            List<Card> comb = new ArrayList<>(playerCards.get(i));
            comb.addAll(communityCards);
            PokerHand hand = HandEval.evaluate(comb);
            hands.add(hand);
            System.out.println("Jugador " + (i+1) + ": " + playerCards.get(i) + " -> " + hand);
        }

        // Detecta ganador
        int winner = 0;
        PokerHand bestHand = hands.get(0);
        for (int i = 1; i < numPlayers; i++) {
            if (hands.get(i).compareTo(bestHand) > 0) {
                bestHand = hands.get(i);
                winner = i;
            }
        }

        System.out.println("Gana el jugadors " + (winner+1) + " con " + bestHand.getHandType());
    }
}
