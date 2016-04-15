package dominion;

import java.util.ArrayList;

/**
 * Created by Digaly on 23/03/2016.
 */
public class Deck
{
    private ArrayList<Card> cards;
    private int victoryPoints;
    private GameEngine gameEngine;

    public Deck(String type, GameEngine gameEngine)
    {
        this.gameEngine = gameEngine;
        cards = new ArrayList<Card>();

        if (type.equals("hand"))
        {
            Card copper = new Card(gameEngine.findCard("copper"));
            copper.setAmount(7);
            Card estate = new Card(gameEngine.findCard("estate"));
            estate.setAmount(3);

            cards.add(copper);
            cards.add(estate);
        }
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
