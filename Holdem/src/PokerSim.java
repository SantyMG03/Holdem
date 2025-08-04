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

        List<List<Card>> playerCards = new ArrayList<>();
    }
}
