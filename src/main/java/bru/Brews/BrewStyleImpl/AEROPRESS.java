package bru.Brews.BrewStyleImpl;

import bru.Brews.BrewStyle;
import bru.Brews.Grind;
import org.apache.commons.lang3.math.Fraction;

import java.util.ArrayList;

/**
 * Created by rross on 11/18/2016.
 */
public class AEROPRESS extends BrewStyle {
    public AEROPRESS(Integer size){
        super(size, Fraction.getFraction(14, 235));
        CUPS_TO_MAKE = size;
        GRIND = Grind.MEDIUM_FINE;
        UTENSILS = new ArrayList<>();
        STEPS.put(-1, getStartingInstructions());
        STEPS.put(1, "Add coffee grounds to the inverted" +
                "AeroPress (see diagram). Set on a scale  ");
        STEPS.put(2, "Press ‘tare’ on the scale  ");
        STEPS.put(3, "Start your timer and pour a small " +
                "amount of hot water (195°-205°F) " +
                "into the Aeropress to dampen the " +
                "bed of grounds. Wait 30 seconds, " +
                "allowing coffee to bloom  ");
        STEPS.put(4, "Continue pouring hot water slowly " +
                "into AeroPress until you reach 235g, " +
                "or as much as fits.  ");
        STEPS.put(5, " Gently stir the slurry (coffee and " +
                "water mixture) 2-3 times.  ");
        STEPS.put(6, "Allow to steep until 2:00.  ");
        STEPS.put(7, "Meanwhile, put the paper filter into " +
                "the small black filter cap and wet with " +
                "hot water. Then carefully twist the " +
                "filter basket onto the top of inverted " +
                "Aeropress..  ");
        STEPS.put(8, "Flip the AeroPress back upright onto a " +
                "sturdy empty mug. Push down to plunge.  ");
    }
}
