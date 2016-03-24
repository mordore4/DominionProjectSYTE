/**
 * Created by Digaly on 23/03/2016.
 */
public class Deck {
    private Card[] cards;
    private int victoryPoints;

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public void calculatePoints()
    {
        int points = 0;
        for (int cardnr = 0; cardnr < cards.length; cardnr++)
        {
            points += cards[cardnr].getValue();
        }

        victoryPoints = points;
    }
}
