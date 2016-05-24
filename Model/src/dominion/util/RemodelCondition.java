package dominion.util;

import dominion.Deck;
import dominion.Game;
import dominion.Player;

/**
 * Created by Tom Dobbelaere on 24/05/2016.
 */
public class RemodelCondition extends Condition
{
    private int cardsToRemove;
    private int handSize;
    private Game game;

    public RemodelCondition(Player player, int cardsToRemove, Game game)
    {
        super(player);

        this.cardsToRemove = cardsToRemove;
        this.handSize = player.getHand().size();
        this.game = game;
    }

    @Override
    public boolean isFulfilled()
    {
        Deck playerHand = getPlayer().getHand();

        if ((handSize - playerHand.size() == cardsToRemove) || playerHand.size() == 0)
        {
            GainCardCondition condition = new GainCardCondition(getPlayer(), game, getPlayer().getValueOfLastTrashedCard() + 2);
            game.addCondition(condition);
        }

        return false;
    }
}
