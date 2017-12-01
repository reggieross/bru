package bru.Brews;

/**
 * Created by rross on 11/18/2016.
 */
public enum Grind {
    FINE ("Fine"),
    MEDIUM_FINE ("Medium Fine"),
    MEDIUM ("Medium"),
    COARSE ("Coarse");

    private final String name;

    Grind(String grind) {
        name = grind;
    }

    public String toString() {
        return this.name;
    }
}
