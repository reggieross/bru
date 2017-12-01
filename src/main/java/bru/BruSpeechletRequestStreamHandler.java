package bru;

/**
 * Created by rross on 11/17/2016.
 */
import java.util.HashSet;
import java.util.Set;

import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;

public class BruSpeechletRequestStreamHandler extends SpeechletRequestStreamHandler{
    private static final Set<String> supportedApplicationIds;

    static {
        /*
         * This Id can be found on https://developer.amazon.com/edw/home.html#/ "Edit" the relevant
         * Alexa Skill and put the relevant Application Ids in this Set.
         */
        supportedApplicationIds = new HashSet<String>();
        // supportedApplicationIds.add("amzn1.echo-sdk-ams.app.[unique-value-here]");
    }

    public BruSpeechletRequestStreamHandler() {
        super(new BruSpeechlet(), supportedApplicationIds);
    }

    public BruSpeechletRequestStreamHandler(Speechlet speechlet, Set<String> supportedApplicationIds) {
        super(speechlet, supportedApplicationIds);
    }
}
