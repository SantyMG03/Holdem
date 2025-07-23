import java.util.*;

public class Main {
    public static void main(String[] args) {
        Deck deck = new Deck();

        List<Card> playerCards = List.of(new Card(Card.Rank.ACE, Card.Suit.SPADES), new Card(Card.Rank.ACE, Card.Suit.CLUBS));
        Card c1 = new Card(Card.Rank.ACE, Card.Suit.HEARTS);
        Card c2 = new Card(Card.Rank.ACE, Card.Suit.DIAMONDS);
        Card c3 = new Card(Card.Rank.THREE, Card.Suit.CLUBS);
        Card c4 = new Card(Card.Rank.EIGHT, Card.Suit.DIAMONDS);
        Card c5 = new Card(Card.Rank.TWO, Card.Suit.HEARTS);
        List<Card> communityCards = List.of(c1,c2,c3,c4,c5);

        List<Card> allCards = new ArrayList<>(playerCards);
        allCards.addAll(communityCards);

        PokerHand hand = HandEval.evaluate(allCards);

        System.out.println("Tus cartas: " + playerCards);
        System.out.println("Comunitarias: " + communityCards);
        System.out.println("Mejor jugada: " + hand);
    }
}
