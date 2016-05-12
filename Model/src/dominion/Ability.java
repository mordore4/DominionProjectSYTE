package dominion;

import java.util.Scanner;

/**
 * Created by Sam on 31/03/2016.
 */
public class Ability
{
    private int id;
    private int amount;
    private Scanner scanner;

    public Ability(int id, int amount)
    {
        this.id = id;
        this.amount = amount;
    }

    public void doAbility(Game game)
    {
        Player currentPlayer = game.findCurrentPlayer();
        switch (id)
        {
            case 1:
                addActions(currentPlayer);
                break;
            case 2:
                addBuys(currentPlayer);
                break;
            case 3:
                addCoins(currentPlayer);
                break;
            case 4:
                addCards(currentPlayer);
                break;
            case 5:

                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
            case 9:
                break;
        }
    }

    private void addActions(Player currentPlayer)
    {
        int currentPlayerActions = currentPlayer.getActions();
        currentPlayer.setActions(currentPlayerActions + amount);
    }

    private void addBuys(Player currentPlayer)
    {
        int currentPlayerBuys = currentPlayer.getBuys();
        currentPlayer.setBuys(currentPlayerBuys + amount);
    }

    private void addCoins(Player currentPlayer)
    {
        int currentPlayerCoins = currentPlayer.getCoins();
        currentPlayer.setCoins(currentPlayerCoins + amount);
    }

    private void addCards(Player currentPlayer)
    {
        Deck discardPile = currentPlayer.getDiscardPile();
        Deck deck = currentPlayer.getDeck();
        for (int i = 0; i < amount; i++)
        {
            if (discardPile.size() + deck.size() != 0)
            {
                currentPlayer.getHand().takeTopCard(deck, discardPile);
            }
        }
    }





    public int getId()
    {
        return id;
    }

    public int getAmount()
    {
        return amount;
    }

    public String toString()
    {
        return id + " " + amount + " ";
    }


}
