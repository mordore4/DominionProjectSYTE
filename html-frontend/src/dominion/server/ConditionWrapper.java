package dominion.server;

import dominion.util.Condition;

/**
 * Created by Tom Dobbelaere on 21/05/2016.
 */
public class ConditionWrapper
{
    private Condition condition;
    private String name;

    public ConditionWrapper(Condition condition)
    {
        this.condition = condition;
        this.name = condition.getClass().getSimpleName();
    }
}
