package dominion;

/**
 * Created by Sam on 23/03/2016.
 */
public class Player
{
    private Account account;
    private Hand hand;
    private Deck discardPile;
    private Deck deck;
    private int actions;
    private int buys;
    private int coins;
    private GameEngine gameEngine;

    public Player(GameEngine gameEngine)
    {
        this.gameEngine = gameEngine;

        actions = 1;
        buys = 1;
        coins = 0;

        hand = new Hand(deck);
        discardPile = new Deck(false, gameEngine);
        deck = new Deck(true, gameEngine);
    }



    public void setAccount(Account account)
    {
        this.account = account;
    }

    public int getActions()
    {
        return actions;
    }

    public int getBuys()
    {
        return buys;
    }

    public int getCoins()
    {
        return coins;
    }

    public Hand getHand() { return hand; }

    public void setCoins(int coins)
    {
        this.coins = coins;
    }

    public void setBuys(int buys)
    {
        this.buys = buys;
    }

    public void setActions(int actions)
    {
        this.actions = actions;
    }
}
