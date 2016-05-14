package dominion.tests;

import dominion.Ability;
import dominion.Card;
import dominion.Game;
import dominion.Player;
import dominion.exceptions.CardNotAvailableException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Digaly on 12/05/2016.
 */
public class AbilityTest
{
    private Game game;
    private Player currentPlayer;
    private TestHelper testHelper;

    @Before
    public void setUp() throws Exception
    {
        testHelper = new TestHelper();

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
}