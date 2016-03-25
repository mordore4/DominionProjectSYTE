package dominion;

import java.util.ArrayList;

/**
 * Created by Digaly on 23/03/2016.
 */
public class Deck
{
    private ArrayList<Card> cards;
    private int victoryPoints;

    public Deck()
    {

    }

    public int getVictoryPoints()
    {
        return victoryPoints;
    }

    public void calculatePoints()
    {
        int points = 0;
        for (int cardnr = 0; cardnr < cards.size(); cardnr++)
        {
            //points += cards[cardnr].getValue();
        }

        victoryPoints = points;
    }
}
