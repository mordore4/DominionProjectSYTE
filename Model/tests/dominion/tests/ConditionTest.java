package dominion.tests;

import dominion.Game;
import dominion.Player;
import dominion.util.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Digaly on 19/05/2016.
 */
public class ConditionTest
{
    private Game game;
    private Player testPlayer;
    private TestHelper testHelper;

    @Before
    public void setUp()
    {
        testHelper = new TestHelper();

        game = new Game(new String[]{"testPlayer"}, testHelper.getDefaultKingdomCards(), testHelper.getTestCardList());
        testPlayer = game.getPlayer("testPlayer");
    }

    @Test
    public void TestRemoveCardsCondition()
    {
        RemoveCardsCondition newCondition = new RemoveCardsCondition(game.getPlayer("testPlayer"), 3);
        game.addCondition(newCondition);

        Condition condition = game.getConditionsList().conditionsOfPlayer(testPlayer).get(0);

        for (int i = 0; i < 3; i++)
        {
            assert(!condition.isFulfilled());
            testPlayer.getHand().getCards().remove(0);
        }

        assert(condition.isFulfilled());
        assert(game.getConditionsList().conditionsOfPlayer(testPlayer).size() == 0);
    }

}