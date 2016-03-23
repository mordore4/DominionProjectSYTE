/**
 * Created by Sam on 23/03/2016.
 */
public class Player {
    private Account profile;
    private Hand hand;
    private DiscardPile discardPile;
    private Deck deck;
    private int actions;
    private int buys;
    private int coins;

    public int getActions() {
        return actions;
    }

    public int getBuys() {
        return buys;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void setBuys(int buys) {
        this.buys = buys;
    }

    public void setActions(int actions) {
        this.actions = actions;
    }
}
