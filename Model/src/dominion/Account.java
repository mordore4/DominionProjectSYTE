package dominion;

/**
 * Created by Digaly on 23/03/2016.
 */
public class Account
{
    private String name;
    private int rankingScore;
    private String[] friendList;

    public Account(String name, int rankingScore)
    {
        this.name = name;
        this.rankingScore = rankingScore;
        friendList = null;
    }

    public String getName()
    {
        return name;
    }
}
