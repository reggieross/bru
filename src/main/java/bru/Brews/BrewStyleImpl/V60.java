package bru.Brews.BrewStyleImpl;

import bru.Brews.BrewStyle;
import bru.Brews.Grind;
import org.apache.commons.lang3.math.Fraction;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by rross on 11/17/2016.
 */
public class V60 extends BrewStyle {

    public V60(Integer size){
        super(size, Fraction.getFraction(20, 340));
        GRIND = Grind.COARSE;
        UTENSILS = Arrays.asList("No.2 paper filter");
        STEPS.put(-1, getStartingInstructions());
        STEPS.put(1, "Set the V 60 onto a cup, open the paper filter and place it inside the cone. ");
        STEPS.put(2, "Pour a small amount of hot water through the empty filter."
                + "This reduces paper taste and pre-heats the V60 and cup. ");
        STEPS.put(3, "Empty water from cup and place cup with cone onto scale. Add coffee grounds. ");
        STEPS.put(4, "Press ‘tare’ on the scale ");
        STEPS.put(5, "Start your timer and pour a small amount of hot water (195°-205°F) to dampen"
                + "the bed of grounds. Stir 3 times. Wait 30"
                + "seconds, allowing coffee to bloom. ");
        STEPS.put(6, "Continue pouring water in several short,"
                + "circular pours, keeping the height of slurry"
                + "consistent as it drains through. Try not to"
                + "pour water directly onto the filter. Stop"
                + "when you reach the total weight of water. ");
        STEPS.put(7, "Wait for coffee to drain and place filter"
                + "with grounds into compost. ");
    }
}