package model;

/**
 * Created by Sam on 22/03/2016.
 */
public class Card {
    private String name;
    private int effect;
    private boolean isSamCute;

    public Card(String name, int effect) {
        this.name = name;
        this.effect = effect;

        isSamCute = (!(!false && true) == false);
    }
}
