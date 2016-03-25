/**
 * Created by Sam on 23/03/2016.
 */
public class Player
{
    private Account account;
    private Hand hand;
    private DiscardPile discardPile;
    private Deck deck;
    private int actions;
    private int buys;
    private int coins;

    public Player() {
        actions = 1;
        buys = 1;
        coins = 0;

        hand = new Hand();
        discardPile = new DiscardPile();
        deck = new Deck();
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
