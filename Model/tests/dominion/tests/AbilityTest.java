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

    @Before
    public void setUp() throws Exception
    {
        String[] accounts = {"bob", "alice"};
        game = new Game(accounts, "default", TestHelper.getTestCardList());
        game.setCurrentPlayerIndex(0);
    }

    @Test
    public void TestAddActionsAbility()
    {
        Player currentPlayer = game.findCurrentPlayer();
        Card testCard = new Card("testcard", 3, 0, 1, new Ability[]{new Ability(1, 2)});

        currentPlayer.getHand().addCard(testCard);

        game.playCard(testCard.getName());

        assert (currentPlayer.getActions() == 2);
    }

    @Test
    public void TestAddBuysAbility()
    {
        Player currentPlayer = game.findCurrentPlayer();
        Card testCard = new Card("testcard", 3, 0, 1, new Ability[]{new Ability(2, 1)});

        currentPlayer.getHand().addCard(testCard);

        game.playCard(testCard.getName());

        assert (currentPlayer.getBuys() == 2);
    }

    @Test
    public void TestAddCoinsAbility()
    {
        Player currentPlayer = game.findCurrentPlayer();
        Card testCard = new Card("testcard", 3, 0, 1, new Ability[]{new Ability(3, 5)});

        currentPlayer.getHand().addCard(testCard);

        game.playCard(testCard.getName());

        assert (currentPlayer.getCoins() == 5);
    }

    @Test
    public void TestAddCardsAbility()
    {
        Player currentPlayer = game.findCurrentPlayer();
        Card testCard = new Card("testcard", 3, 0, 1, new Ability[]{new Ability(4, 20)});

        currentPlayer.getHand().addCard(testCard);

        game.playCard(testCard.getName());

        assert (currentPlayer.getHand().size() == 10);
    }


}