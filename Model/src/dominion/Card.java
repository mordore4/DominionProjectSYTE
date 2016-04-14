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
    private int amount;

    public Card(String cardName, int type, int cost, int amount, Ability[] abilities)
    {
        name = cardName;
        this.type = type;
        this.cost = cost;
        this.amount = amount;
        this.abilities = abilities;
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

    public int getAmount()
    {
        return amount;
    }

    public Ability[] getAbilities()
    {
        return abilities;
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
