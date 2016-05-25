package dominion.util;

import dominion.Player;

/**
 * Created by Sam on 22/05/2016.
 */
public class PlayCardMultipleTimesCondition extends Condition
{
    int amount;


    public PlayCardMultipleTimesCondition(Player player, int amount)
    {
        super(player);
        this.amount = amount;
    }

    @Override
    public boolean isFulfilled()
    {
        return true;
    }
}
