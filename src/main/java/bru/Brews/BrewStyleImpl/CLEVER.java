package bru.Brews.BrewStyleImpl;

import bru.Brews.BrewStyle;
import bru.Brews.Grind;
import org.apache.commons.lang3.math.Fraction;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by rross on 11/18/2016.
 */
public class CLEVER extends BrewStyle {

    public CLEVER(Integer size){
        super(size, Fraction.getFraction(20, 340));
        GRIND = Grind.MEDIUM_FINE;
        UTENSILS = Arrays.asList("No.4 paper filter");
        STEPS.put(1, "Fold paper filter and put it into the " +
                "dripper. Set the dripper on a large cup. ");
        STEPS.put(2, "Pour a small amount of hot water through" +
                " the empty filter. This reduces paper taste" +
                " and pre-heats the Clever and cup. ");
        STEPS.put(3, "Place Clever onto the scale (without the cup!) and add coffee grounds, shaking to ");
        STEPS.put(4, "Press ‘tare’ on the scale. ");
        STEPS.put(5, "Start your timer and pour a small amount " +
                " of hot water (195°-205°F) to dampen the " +
                " bed of grounds. Wait 30 seconds, allowing" +
                " coffee to bloom. ");
        STEPS.put(6, " Continue pouring water, making sure to " +
                " evenly saturate the grounds. Stop when " +
                " you reach the total weight of water. " +
                " 8 Gently stir the slurry. ");
        STEPS.put(7, "Gently stir the slurry. ");
        STEPS.put(8, "Place lid on the Clever. ");
        STEPS.put(9, " At 2:30, stir the slurry gently 2-3 times. " +
                " Empty your cup (if it still has hot water in " +
                " it) and place the Clever on top of your cup " +
                " to drain. ");
        STEPS.put(10, "Wait 1-1.5 minutes for coffee to drain, " +
                " then place filter and grounds in compost. ");
    }

    public static String getGrindInstructions(){
        return "Grind 14 grams of Coffee";
    }
}
