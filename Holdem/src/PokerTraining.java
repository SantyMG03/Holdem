import java.util.*;

public class PokerTraining {
    private final int numPlayers;
    private final Deck deck;
    private final Scanner scanner;

    public PokerTraining(int numPlayers) {
        if (numPlayers < 2 || numPlayers > 9)
            throw new IllegalArgumentException("El numero de jugadores debe ser 2 y 9");
        this.numPlayers = numPlayers;
        this.deck = new Deck();
        this.scanner = new Scanner(System.in);
    }

    public void playRound() {
        deck.shuffle();

        // Cartas privadas
        List<List<Card>> playerCards = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            List<Card> hand = new ArrayList<>();
            hand.add(deck.draw());
            hand.add(deck.draw());
            playerCards.add(hand);
        }

        // Cartas comunitarias
        List<Card> communityCards = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            communityCards.add(deck.draw());
        }

        // Mostrar solo tus cartas y comunitarias
        System.out.println("\nTus cartas: " + playerCards.getFirst());
        System.out.println("Cartas comunitarias: " + communityCards);
        System.out.println("Adivina tu mejor jugada (Pair, Straight, Full House): ");
        String guess = scanner.nextLine();

        // Evaluar todas las manos
        List<PokerHand> hands = new ArrayList<>();
        for (int i = 0; i < numPlayers; i ++) {
            List<Card> combined = new ArrayList<>(playerCards.get(i));
            combined.addAll(communityCards);
            PokerHand hand = HandEval.evaluate(combined);
            hands.add(hand);
        }

        // Determinar el ganador
        int winnerIndex = 0;
        PokerHand bestHand = hands.getFirst();
        for (int i = 1; i < numPlayers; i++) {
            if (hands.get(i).compareTo(bestHand) > 0) {
                bestHand = hands.get(i);
                winnerIndex = i;
            }
        }

        // Mostrar resultados
        System.out.println("\n--- RESULTADOS ---");
        for (int i = 0; i < numPlayers; i++) {
            System.out.println("Jugador " + (i + 1) + ": " + playerCards.get(i) + " -> " + hands.get(i));
        }

        System.out.println("Gana el jugador " + (winnerIndex + 1) + " con " + bestHand.getHandType());

        // Evaluar respuesta
        String myHandType = hands.getFirst().getHandType();
        if (myHandType.equalsIgnoreCase(guess.trim())) {
            System.out.println("Correcto! Tu mano es " + myHandType);
        } else {
            System.out.println("Incorrecto. Tu mano real es " + myHandType);
        }
    }
}
