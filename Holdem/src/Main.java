import java.util.*;

public class Main {
    public static void main(String[] args) {
        PokerTrainingHard sim = new PokerTrainingHard(4);
        while (true) {
            sim.playRound();
            System.out.println("\nQuieres jugar otra ronda? (s/n) ");
            Scanner sc = new Scanner(System.in);
            if (!sc.nextLine().trim().equalsIgnoreCase("s")) break;
        }
    }
}
