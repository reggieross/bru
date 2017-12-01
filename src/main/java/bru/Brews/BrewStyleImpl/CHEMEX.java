package bru.Brews.BrewStyleImpl;

import bru.Brews.BrewStyle;
import bru.Brews.Grind;
import org.apache.commons.lang3.math.Fraction;

import java.util.Arrays;

/**
 * Created by rross on 11/18/2016.
 */
public class CHEMEX extends BrewStyle{

     public CHEMEX(Integer size){
        super(size, Fraction.getFraction(14, 235));
        GRIND = Grind.MEDIUM_FINE;
        UTENSILS =  Arrays.asList("round chemex filter");

        STEPS.put(-1, getStartingInstructions());
        STEPS.put(1, "Fold filter and place into the top" +
                 " of the Chemex ");
        STEPS.put(2, "Pour a small amount of hot water through" +
                 " the empty filter. This reduces paper taste" +
                 " and pre-heats the Chemex. When ready," +
                 " empty water out into the sink. " +
                 " and pour the coffee grounds into the filter, shaking" +
                 " gently to flatten the bed of grounds. ");
        STEPS.put(3, "Place Chemex onto the scale. Press ‘tare’."
                 + "into the french press to dampe the bed of grounds.  ");
        STEPS.put(4, "Start your timer. Pour enough hot water" +
                 " (195°-205°F) to dampen the bed of grounds." +
                 " Stir 3 times. ");
        STEPS.put(5, "Wait 30-45 seconds," +
                 " allowing coffee to bloom. ");
        STEPS.put(6, "Starting in the center, pour water" +
                 " consistently in a circular motion to saturate" +
                 " the bed of grounds. Pour enough water to" +
                 " maintain the height of the slurry at about ½" +
                 " below the lip of the Chemex. Avoid pouring" +
                 " down the sides of the filter. Stop when you" +
                 " reach the total weight of water. ");
        STEPS.put(7, "Stir gently 2-3 times. ");
        STEPS.put(8, "Wait for coffee to drain (4-5 minutes" +
                 " total if brewed correctly). Place filter" +
                 " and grounds in compost ");
         STEPS.put(9, "Serve coffee and place grounds " +
                 "into compost. ");
    }

}
