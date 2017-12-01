package bru.Brews.BrewStyleImpl;

import bru.Brews.BrewStyle;
import bru.Brews.Grind;
import org.apache.commons.lang3.math.Fraction;

import java.util.ArrayList;

/**
 * Created by rross on 11/18/2016.
 */
public class FRENCHPRESS extends BrewStyle {

    public FRENCHPRESS(Integer size){
        super(size, Fraction.getFraction(20, 340));
        GRIND = Grind.MEDIUM_FINE;
        UTENSILS = new ArrayList<>();
        STEPS.put(-1, getStartingInstructions());
        STEPS.put(1, "Fill empty french press with hot water so that it pre-heats while you prepare ground coffee.");
        STEPS.put(2, "Empty out the water and add ground coffee.");
        STEPS.put(3, "Set on the scale and press ‘tare’.");
        STEPS.put(4, "Start your timer and pour a small amount of hot water (195°-205°F)"
                + "into the french press to dampe the bed of grounds. ");
        STEPS.put(5, "Wait 30 seconds, allowing coffee to bloom. Keep covered with plunger lid  during bloom to hold in heat.");
        STEPS.put(6, "Continue pouring water over the grounds until the total weight of water is reached.");
        STEPS.put(7, "Gently stir the slurry, then place filter" +
                "and plunger onto the french press and " +
                "push down just enough to saturate " +
                "ground coffee, about half an inch " +
                "below the surface.");
        STEPS.put(8, "At 3:30, remove lid and give slurry ");
        STEPS.put(9, "At 5:00, push plunger down slowly, " +
                "stopping and waiting if there is " +
                "resistance.");
        STEPS.put(10, "Serve coffee and place grounds " +
                "into compost.");
    }
}
