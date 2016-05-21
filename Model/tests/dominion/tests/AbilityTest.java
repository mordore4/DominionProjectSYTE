package dominion.tests;

import dominion.*;
import dominion.exceptions.CardNotAvailableException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Digaly on 12/05/2016.
 */
public class AbilityTest
{
    private Game game;
    private Player currentPlayer;
    private static TestHelper testHelper;

    @BeforeClass
    public static void setUpHelper()
    {
        testHelper = new TestHelper();
    }

    @Before
    public void setUp() throws Exception
    {
        String[] accounts = {"bob", "alice"};
        game = new Game(accounts, testHelper.getDefaultKingdomCards(), testHelper.getTestCardList());
        game.setCurrentPlayerIndex(0);
        currentPlayer = game.findCurrentPlayer();
    }

    @Test
    public void testAddActionsAbility() throws CardNotAvailableException
    {
        Card testCard = new Card("testcard", 3, 0, 1, new Ability[]{new Ability(1, 2)});

        currentPlayer.getHand().addCard(testCard);

        game.playCard(testCard.getName());

        assert (currentPlayer.getActions() == 2);
    }

    @Test
    public void testAddBuysAbility() throws CardNotAvailableException
    {
        Card testCard = new Card("testcard", 3, 0, 1, new Ability[]{new Ability(2, 1)});

        currentPlayer.getHand().addCard(testCard);

        game.playCard(testCard.getName());

        assert (currentPlayer.getBuys() == 2);
    }

    @Test
    public void testAddCoinsAbility() throws CardNotAvailableException
    {
        Card testCard = new Card("testcard", 3, 0, 1, new Ability[]{new Ability(3, 5)});

        currentPlayer.getHand().addCard(testCard);

        game.playCard(testCard.getName());

        assert (currentPlayer.getCoins() == 5);
    }

    @Test
    public void testAddCardsAbility() throws CardNotAvailableException
    {
        Card testCard = new Card("testcard", 3, 0, 1, new Ability[]{new Ability(4, 20)});

        currentPlayer.getHand().addCard(testCard);

        game.playCard(testCard.getName());

        assert (currentPlayer.getHand().size() == 10);
    }

    @Test
    public void testCurseOtherPlayersAbility() throws CardNotAvailableException
    {
        Card testCard = new Card("testcard", 3, 0, 1, new Ability[]{new Ability(12, -1)});

        currentPlayer.getHand().addCard(testCard);

        game.playCard(testCard.getName());

        assert (game.getPlayer("alice").getDiscardPile().findCard("curse") != null);
    }

    @Test
    public void testTrashThisCard() throws CardNotAvailableException
    {
        Card testCard = new Card("testcard", 3, 0, 1, new Ability[]{new Ability(6, -1)});

        currentPlayer.getHand().addCard(testCard);

        game.playCard(testCard.getName());

        assert (currentPlayer.getHand().findCard("testcard") == null);
    }

    @Test
    public void testMoneyLenderSpecialAbility() throws CardNotAvailableException
    {
        Card testCard = new Card("testcard", 3, 0, 1, new Ability[]{new Ability(27, -1)});
        Card copper = game.findCard("copper");
        Deck hand = currentPlayer.getHand();

        hand.getCards().clear();

        hand.addCard(testCard);
        hand.addCard(copper);

        boolean handContainsCopper = hand.findCard("copper") != null;

        game.playCard(testCard.getName());

        boolean handDoesNotContainCopper = hand.findCard("copper") == null;

        assert (handContainsCopper && handDoesNotContainCopper);
    }

    @Test
    public void testMilitiaSpecialAbility() throws CardNotAvailableException
    {
        Card testCard = new Card("testcard", 3, 0, 1, new Ability[]{new Ability(19, -1)});
        currentPlayer.getHand().addCard(testCard);

        game.playCard("testcard");

        assert(game.getConditionsList().conditionsOfPlayer(currentPlayer).size() == 0);
        assert(game.getConditionsList().conditionsOfPlayer(game.getPlayer("alice")).size() == 1);

        while (game.getPlayer("alice").getHand().size() > 3)
        {
            game.getPlayer("alice").getHand().getCards().remove(0);
        }
        
        assert(game.getConditionsList().conditionsOfPlayer(game.getPlayer("alice")).size() == 0);
    }

    @Test
    public void testGainSilver() throws CardNotAvailableException
    {
        Card testCard = new Card("testcard", 3, 0, 1, new Ability[]{new Ability(25, -1)});
        Card copper = game.findCard("copper");
        currentPlayer.getHand().addCard(testCard);
        currentPlayer.getDeck().getCards().clear();
        currentPlayer.getDeck().addCard(copper);
        assertFalse(currentPlayer.getDeck().getTopCard(currentPlayer.getDiscardPile()).getName().equals("silver"));
        //boolean isFirstCardSilver = currentPlayer.getDeck().getTopCard(currentPlayer.getDiscardPile()).getName().equals("silver");
        /*String currentTopCard = currentPlayer.getDeck().getTopCard(currentPlayer.getDiscardPile()).getName();

        if (currentTopCard == "silver")
        {
            isFirstCardSilver = true;
        } Vervangen door regel erboven */

        game.playCard("testcard");
        //boolean isTopCardSilver = currentPlayer.getDeck().getTopCard(currentPlayer.getDiscardPile()).getName().equals("silver");
        assertTrue(currentPlayer.getDeck().getTopCard(currentPlayer.getDiscardPile()).getName().equals("silver"));
        /*String topCard = currentPlayer.getDeck().getTopCard(currentPlayer.getDiscardPile()).getName();
        if (topCard == "silver")
        {
            isTopCardSilver = true;
        } Vervangen door regel erboven*/

        //assert (!isFirstCardSilver && isTopCardSilver);
    }

}