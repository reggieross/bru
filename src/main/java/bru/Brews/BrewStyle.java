package bru.Brews;

import org.apache.commons.lang3.math.Fraction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rross on 11/18/2016.
 */
public abstract class BrewStyle implements IBrewStyle {
    public static final double OUNCE_CONVERSION = 0.035;
    protected static Map<Integer, String> STEPS = new HashMap<Integer, String>();
    protected static Fraction COFFE_WATER_RATIO;
    protected static Grind GRIND;
    protected static List<String> UTENSILS;
    protected static Integer CUPS_TO_MAKE;

    public BrewStyle(Integer size, Fraction ratio) {
        CUPS_TO_MAKE = size;
        COFFE_WATER_RATIO = ratio;
    }

    public  String getInstructionsText() {
        StringBuilder speechOutputBuilder = new StringBuilder();
        speechOutputBuilder.append("To Start " + STEPS.get(-1) +  " ");

        for (Map.Entry<Integer, String> step: STEPS.entrySet()) {
            if (step.getKey() != -1) {
                speechOutputBuilder.append("Step " +  step.getKey()+ ". " + step.getValue() +  " \n ");
            }
        }
        return speechOutputBuilder.toString();
    }

    public  ArrayList<String> getInstructions() {
        return new ArrayList<String>(STEPS.values());
    }

    public static String getStartingInstructions(){
        int amountOfCoffee = CUPS_TO_MAKE * COFFE_WATER_RATIO.getNumerator();
        int amountOfWater = (int) Math.round((CUPS_TO_MAKE * COFFE_WATER_RATIO.getDenominator()) * OUNCE_CONVERSION);
        return "To begin you'll need " + amountOfWater +  " ounces of water and "
               + amountOfCoffee + " grams of coffee."
               + " Grind coffee beans to a " + GRIND.toString() + " grind";
    }
}
