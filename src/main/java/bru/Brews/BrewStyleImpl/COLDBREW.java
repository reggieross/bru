package bru.Brews.BrewStyleImpl;

import bru.Brews.BrewStyle;
import org.apache.commons.lang3.math.Fraction;

/**
 * Created by rross on 11/18/2016.
 */
public class COLDBREW extends BrewStyle {
    public COLDBREW(Integer size) {
        super(size, Fraction.getFraction(1,1));
    }
}
