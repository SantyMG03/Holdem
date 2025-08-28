import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private List<Card> hand = new ArrayList<>();
    private int chips; // fichas

    public Player(String name, int chips) {
        this.name = name;
        this.chips = chips;
    }

    public String getName() {
        return name;
    }

    public List<Card> getHand() {
        return hand;
    }

    public int getChips() {
        return chips;
    }

    public void addCard(Card card) {
        hand.add(card);
    }

    public void bet(int amount) {
        if (amount > chips) throw new IllegalArgumentException("No tienes suficientes fichas");
        chips -= amount;
    }

    public void win (int amount) {
        chips += amount;
    }

    public String toString() {
        return name + " -> " + hand;
    }
}
