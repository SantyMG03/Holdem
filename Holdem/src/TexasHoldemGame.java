import java.util.*;

public class TexasHoldemGame {
    private Deck deck;
    private List<Player> players;
    private List<Card> communityCards;
    private int stage; // preflop=0, flop=1, turn=2, river=3, showdown=3
    private int pot;

    public TexasHoldemGame(int numPlayers) {
        this.deck = new Deck();
        this.players = new ArrayList<>();
        this.communityCards = new ArrayList<>();
        this.stage = 0;

        for (int i = 1; i <= numPlayers; i++) {
            Player p = new Player("Jugador " + i, 100);
            p.addCard(deck.draw());
            p.addCard(deck.draw());
            players.add(p);
        }
        blinds();
    }

    private void placeBet(Player p, int amount) {
        p.bet(amount);
        pot += amount;
        System.out.println(p.getName() + " apuestas " + amount + " fichas.");
    }

    private void blinds() {
        Player small = players.get(0);
        Player big = players.get(1);

        placeBet(small, 10);
        placeBet(big, 20);
    }

    public void showStage() {
        System.out.println("\n=== Etapa: " + stageName() + " ===");

        // cartas comunitarias hasta el momento
        if (!communityCards.isEmpty()) {
            System.out.println("Comunitarias: " + communityCards);
        }

        // cartas privadas de cada jugador
        for (Player p: players) {
            System.out.println(p.getName() + " -> " +p.getHand());
        }
    }

    public void nextStage() {
        switch (stage) {
            case 0: // Flop
                communityCards.add(deck.draw());
                communityCards.add(deck.draw());
                communityCards.add(deck.draw());
                stage = 1;
                break;
            case 1: // Turn
                communityCards.add(deck.draw());
                stage = 2;
                break;
            case 2: // River
                communityCards.add(deck.draw());
                stage = 3;
                break;
            case 3:
                showdown();
                stage = 4;
                break;
            default:
                System.out.println("La partida ha terminado");
        }
    }

    private void showdown() {
        System.out.println("\n=== SHOWDOWN ===");
        System.out.println("Comunitarias: " + communityCards);

        Map<Player, PokerHand> results = new HashMap<>();

        for (Player p: players) {
            List<Card> hand = new ArrayList<>(p.getHand());
            hand.addAll(communityCards);
            PokerHand eval = HandEval.evaluate(hand);
            results.put(p, eval);
            System.out.println(p.getName() + " -> " + eval);
        }

        Player winner = results.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseThrow();

        System.out.println("GANADOR: " + winner.getName());
        winner.win(pot);
        System.out.println(winner.getName() + " gana el bote de " + pot + " fichas!");
        pot = 0; // Para si se juegan varias rondas seguidas
    }

    public String stageName() {
        return switch (stage) {
            case 0 -> "Preflop";
            case 1 -> "Flop";
            case 2 -> "Turn";
            case 3 -> "River";
            case 4 -> "Showdown";
            default -> "Finalizado";
        };
    }
}
