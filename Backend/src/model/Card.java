package model;

/**
 * Created by Sam on 22/03/2016.
 */
public class Card {
    private String name;
<<<<<<< HEAD
    private int effect;
    private boolean isSamCute;

    public Card(String name, int effect) {
        this.name = name;
        this.effect = effect;

        isSamCute = (!(!false && true) == false);
=======
    private int cardNumber;
    private int[] effects;

    public Card(String name, int cardNumber, int[] effects)
    {
        this.name = name;
        this.cardNumber = cardNumber;
        this.effects = effects;
>>>>>>> origin/master
    }
}
