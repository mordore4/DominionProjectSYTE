package dominion.util;

import dominion.Deck;
import dominion.Game;
import dominion.Player;

/**
 * Created by Tom Dobbelaere on 24/05/2016.
 */
public class RemodelCondition extends Condition
{
    private int handSize;
    private transient Game game;

    public RemodelCondition(Game game)
    {
        super(game.findCurrentPlayer());
        this.handSize = game.findCurrentPlayer().getHand().size() - 1;
        this.game = game;
    }

    @Override
    public boolean isFulfilled()
    {
        Deck playerHand = getPlayer().getHand();

        if ((handSize - playerHand.size() == 1) || playerHand.size() == 0)
        {
            GainCardCondition condition = new GainCardCondition(getPlayer(), game, getPlayer().getValueOfLastTrashedCard() + 2);
            game.addCondition(condition);
        }

        return false;
    }
}
