package dominion.tests;

import dominion.Ability;
import dominion.Card;
import dominion.Game;
import dominion.Player;
import dominion.exceptions.CardNotAvailableException;
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
    public void TestHasConditionOfType()
    {
        assertFalse(game.getConditionsList().hasConditionOfType(RemoveCardsCondition.class));

        RemoveCardsCondition newCondition = new RemoveCardsCondition(game.getPlayer("testPlayer"), 3);
        game.addCondition(newCondition);

        assertTrue(game.getConditionsList().hasConditionOfType(RemoveCardsCondition.class));
        assertFalse(game.getConditionsList().hasConditionOfType(GainCardCondition.class));
    }

    @Test
    public void TestRemoveCardsCondition()
    {
        RemoveCardsCondition newCondition = new RemoveCardsCondition(game.getPlayer("testPlayer"), 3);
        game.addCondition(newCondition);

        Condition condition = game.getConditionsList().get(testPlayer);

        for (int i = 0; i < 3; i++)
        {
            assert(!condition.isFulfilled());
            testPlayer.getHand().getCards().remove(0);
        }

        assert(condition.isFulfilled());
        assert(game.getConditionsList().get(testPlayer) == null);
    }

    @Test
    public void TestGainCardCondition() throws CardNotAvailableException
    {
        GainCardCondition condition = new GainCardCondition(testPlayer, game, 3);
        Card testCard = new Card("testcard", 3, 3, 1, new Ability[]{new Ability(19, -1)});

        testPlayer.getDiscardPile().addCard(testCard);

        assertTrue(condition.isFulfilled());
    }

    @Test
    public void TestRemodelCondition() throws CardNotAvailableException
    {
        RemodelCondition condition = new RemodelCondition(testPlayer, 1, game);
        game.addCondition(condition);

        assertFalse(condition.isFulfilled());
        assertTrue(game.getConditionsList().hasConditionOfType(RemodelCondition.class));
        //System.out.println(game.getConditionsList().get(testPlayer).getClass().getSimpleName());

        game.trashCardFromPlayer(testPlayer, testPlayer.getHand().getCards().get(0));

        assertTrue(game.getConditionsList().hasConditionOfType(GainCardCondition.class));
    }

}