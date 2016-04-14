package dominion.tests;

import dominion.Card;
import dominion.GameEngine;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Sam on 14/04/2016.
 */
public class GameEngineTest {
    @Test
    public void testCardList()
    {
        boolean works = false;
        GameEngine ge = null;
        try
        {
            ge = new GameEngine();
        }
        catch (Exception ex)
        {

        }
        ArrayList<Card> cl = ge.getCardList();
        for (Card c : cl)
        {
            if (c.getName().equals("adventurer") && c.getType() == 3 && c.getCost() == 6)
            {
                works = true;
            }
        }
        assertTrue(works);
    }
}