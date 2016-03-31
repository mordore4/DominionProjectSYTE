package dominion;

/**
 * Created by Sam on 23/03/2016.
 */
public class Card
{
    private String name;
    private Ability[] abilities; //possibly wrong, Abilities might become class
    private int type;
    private int cost;
    //private int count; Amount still buyable, probably not the right class

    public Card(String cardName, int type, int cost)
    {
        name = cardName;
        this.type = type;
        this.cost = cost;
    }

    public String getName()
    {
        return name;
    }

    public int getType()
    {
        return type;
    }

    public int getCost()
    {
        return cost;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public void setCost(int cost)
    {
        this.cost = cost;
    }
}
