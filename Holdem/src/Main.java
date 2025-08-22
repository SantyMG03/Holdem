import java.util.*;

public class Main {
    public static void main(String[] args) {
        TexasHoldemGame game = new TexasHoldemGame(3);
        game.showStage(); // preflop
        game.nextStage();
        game.showStage(); // flop
        game.nextStage();
        game.showStage(); // turn
        game.nextStage();
        game.showStage(); // river
        game.nextStage(); // showdown
    }
}
