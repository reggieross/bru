/**
 Copyright 2014-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.

 Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with the License. A copy of the License is located at

 http://aws.amazon.com/apache2.0/

 or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package bru;

import java.util.*;

import bru.Brews.BrewStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.OutputSpeech;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SsmlOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;

public class BruSpeechlet implements Speechlet {
    private static final Logger log = LoggerFactory.getLogger(BruSpeechlet.class);

    //Constants for a Slots
    private static final String SLOT_BREW_STYLE = "brew";
    private static final String SLOT_SIZE = "size";
    private static final String SLOT_BEAN = "bean";


    //Constants for a particular session
    private static final String SESSION_INDEX = "index";
    private static final String SESSION_TEXT = "text";
    private static final String SESSION_BREW_STYLE = "brew";
    private static final String SESSION_CUPS = "amount";



    private static final String PROMPT_TEXT = "How may I help you";
    public static final String CLASS_PREFIX = "bru.Brews.BrewStyleImpl.";
    public static final String  REPROMPT_TEXT = "When you need the next instruction just say next instruction";


    //Generic BrewStyle Styles
    private static final String DRIP = "drip";
    private static final String SUBMERSION = "submersion";
    private static final String COLD_BREW = "cold brew";
    private static final String ESSPRESSO = "espresso";

    private static final HashMap<String, String> BREW_STYLES = new HashMap<String, String>();
    private static final HashMap<String, Float> BREW_TIMES = new HashMap<String, Float>();

    static {
        BREW_STYLES.put("V 60", DRIP);
        BREW_STYLES.put("clever", DRIP);
        BREW_STYLES.put("chemex", DRIP);
        BREW_STYLES.put("pour over", DRIP);
        BREW_STYLES.put("Coffee Machine", DRIP);
        BREW_STYLES.put("french press", SUBMERSION);
        BREW_STYLES.put("aero press", SUBMERSION);
        BREW_STYLES.put("cold brew", COLD_BREW);
        BREW_STYLES.put("espresso", ESSPRESSO);

        BREW_TIMES.put(DRIP, (float) 5.0);
        BREW_TIMES.put(SUBMERSION, (float) 3.0);
        BREW_TIMES.put(COLD_BREW,(float) 600.0);
        BREW_TIMES.put(ESSPRESSO,(float)0.30);
    }

    public void onSessionStarted(final SessionStartedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
    }

    public SpeechletResponse onLaunch(final LaunchRequest request, final Session session)
            throws SpeechletException {
        log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        return getWelcomeResponse();
    }

    public SpeechletResponse onIntent(final IntentRequest request, final Session session)
            throws SpeechletException {
        log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        Intent intent = request.getIntent();
        String intentName = intent.getName();

        if ("dialogInstruction".equals(intentName.toLowerCase())) {
            Slot cupSlot = intent.getSlot(SLOT_SIZE);
            Slot brewStyle = intent.getSlot(SLOT_BREW_STYLE);
            if (cupSlot != null && cupSlot.getValue() != null) {
                return handleCupDialogRequest(intent, session);
            } else if (brewStyle != null && brewStyle.getValue() != null) {
                return handleBrewStyleRequest(intent, session);
            } else {
                return handleSupportedBrewTypesRequest(intent, session);
            }
        } else if("instruction".equals(intentName)){
            return handleInstructionRequest(intent, session);
        } else if ("nextInstruction".equals(intentName)) {
            return handleNextInstruction(session);
        } else if ("SupportedBrewTypes".equals(intentName)) {
            return handleSupportedBrewTypesRequest(intent, session);
        } else if ("AMAZON.HelpIntent".equals(intentName)) {
            return handleHelpRequest();
        } else if ("AMAZON.StopIntent".equals(intentName)) {
            PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
            outputSpeech.setText("Goodbye");

            return SpeechletResponse.newTellResponse(outputSpeech);
        } else if ("AMAZON.CancelIntent".equals(intentName)) {
            PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
            outputSpeech.setText("Goodbye");

            return SpeechletResponse.newTellResponse(outputSpeech);
        } else {
            throw new SpeechletException("Invalid Intent " + intentName);
        }
    }

    public void onSessionEnded(final SessionEndedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
    }

    private SpeechletResponse getWelcomeResponse() {
        String speechOutput = "<speak>"
                + "Welcome to Bru. "
                + PROMPT_TEXT
                + "</speak>";
        String repromptText =
                "I can lead you through making the perfect cup of coffee "
                        + " for whatever style you would like. To start just say "
                        + "Can you make me a coffee?, "
                        + "For a list of supported brewing styles, ask what brew styles are supported. "
                        + PROMPT_TEXT;

        return newAskResponse(speechOutput, true, repromptText, false);
    }

    private SpeechletResponse handleHelpRequest() {
        String repromptText = "How May I help you?";
        String speechOutput =
                "I can lead you through making the perfect cup of coffee "
                        + " for whatever style you would like. To start just say "
                        + "Can you make me a coffee?, "
                        + "For a list of supported brewing styles, ask what brew styles are supported. "
                        + " Or you can say exit" + repromptText;

        return newAskResponse(speechOutput, repromptText);
    }

    /**
     * Handles the case where we need to know which BrewStyle Style the user needs instructions information for.
     */
    private SpeechletResponse handleSupportedBrewTypesRequest(final Intent intent, final Session session) {
        // get city re-prompt
        String speechOutput =
                "Currently, How to make these styles of coffee: "
                        + getAllBrewStyles() + PROMPT_TEXT;

        return newAskResponse(speechOutput, PROMPT_TEXT);
    }

    private SpeechletResponse handleCupDialogRequest(final Intent intent, final Session session) {
        Integer size = null;
        try {
            size = getSizeFromIntent(intent, false);
        } catch (Exception e) {
        }

        // if we don't have a brew style yet, go to date. If we have a date, we perform the final request
        if (session.getAttributes().containsKey(SESSION_BREW_STYLE)) {
            String brewStyleName = (String) session.getAttribute(SESSION_BREW_STYLE);
            BrewStyles<String, String> brewStyle =  new BrewStyles<>(brewStyleName, foramtBrewStyle(brewStyleName));
            return getInstructionsForBrewStyle(brewStyle, session, size);
        } else {
            // set city in session and prompt for date
            session.setAttribute(SESSION_CUPS, size);
            String repromptText = "For which brew Style would you like Instructions for " + getAllBrewStyles() + "?";

            return newAskResponse(repromptText, repromptText);
        }
    }

    private SpeechletResponse handleBrewStyleRequest(final Intent intent, final Session session) {
        // Determine city, using default if none provided
        BrewStyles<String, String> bruObject = null;
        try {
            bruObject = getBrewStyleFromIntent(intent, true);
        } catch (Exception e) {

        }

        if (session.getAttributes().containsKey(SESSION_CUPS)) {
            Integer size = (Integer) session.getAttribute(SESSION_CUPS);
            return getInstructionsForBrewStyle(bruObject, session, size);
        } else {
            session.setAttribute(SESSION_BREW_STYLE, bruObject.speechValue);
            String repromptText = "How many cups of Coffee would you like?";

            return newAskResponse(repromptText, repromptText);
        }
    }

    private SpeechletResponse handleNextInstruction(Session session) {
        ArrayList<String> steps = (ArrayList<String>) session.getAttribute(SESSION_TEXT);
        int index = (Integer) session.getAttribute(SESSION_INDEX);
        String speechOutput = "";
        if (steps == null) {
            speechOutput = PROMPT_TEXT;
        } else if (index >= steps.size()) {
            speechOutput =
                    "You have completed all instructions"
                            + " a text version of the the instructions can be found in your alexa app";
        } else {
            StringBuilder speechOutputBuilder = new StringBuilder();
            speechOutputBuilder.append(steps.get(index));
            index++;
            session.setAttribute(SESSION_INDEX, index);
            speechOutput = speechOutputBuilder.toString();
        }

        SpeechletResponse response = newAskResponse("<speak>" + speechOutput + "</speak>", true, REPROMPT_TEXT, false);
        return response;
    }


    /**
     * This handles the one-shot interaction, where the user utters a phrase like: 'Alexa, open BRU
     * and get instructions on how to make a pour over with dark roast beans'. If there is an error in a slot,
     * this will guide the user to the dialog approach.
     */
    private SpeechletResponse handleInstructionRequest(final Intent intent, final Session session) {
        BrewStyles<String, String> bruObject = null;
        Integer size = null;
        try {
            bruObject = getBrewStyleFromIntent(intent, true);
            size = getSizeFromIntent(intent, false);
        } catch (Exception e) {

            log.info("There was an error in the direct question: " + e.getMessage()
                        + "\n moving to an attempt at dialougue");

            String speechOutput =
                    "Currently, I know how to make coffee using these methods: "
                            + getAllBrewStyles()
                            + PROMPT_TEXT;

            return newAskResponse(speechOutput, speechOutput);
        }

        // all slots filled, either from the user or by default values. Move to final request
        return getInstructionsForBrewStyle(bruObject, session, size);
    }

    /**
     * Both the one-shot and dialog based paths lead to this method to issue the request, and
     * respond to the user with the final answer.
     */
    private SpeechletResponse getInstructionsForBrewStyle(BrewStyles<String, String> brewStyle,
                                                          Session session,
                                                          Integer size
                                                         ) {
        String speechOutput = "";
        ArrayList<String> instructions;
        BrewStyle style = null;
        try {
            style = (BrewStyle) Class.forName(CLASS_PREFIX + brewStyle.apiValue)
                        .getConstructor(Integer.class)
                        .newInstance(size);

            instructions = style.getInstructions();
            speechOutput = instructions.get(0);
            session.setAttribute(SESSION_INDEX, 1);
            session.setAttribute(SESSION_TEXT, instructions);

        } catch (Exception e) {
            log.error("Exception occoured while gathering Instructions for " + brewStyle.apiValue , e);
        }


        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("Instructions for " + brewStyle.speechValue);
        String content = style.getInstructionsText();
        card.setContent(content);

        // Create the plain text output
        PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
        outputSpeech.setText(speechOutput);

        //Create Reprompt
        Reprompt reprompt = new Reprompt();
        PlainTextOutputSpeech repromptSpeech = new PlainTextOutputSpeech();
        repromptSpeech.setText(REPROMPT_TEXT);
        reprompt.setOutputSpeech(repromptSpeech);

        return SpeechletResponse.newAskResponse(outputSpeech, reprompt, card);
    }

    /**
     * Gets the Bean from the intent, or throws an error.
     */
    private BrewStyles<String, String> getBrewStyleFromIntent(final Intent intent,
                                                              final boolean assignDefault) throws Exception {
        Slot brewStyleSlot = intent.getSlot(SLOT_BREW_STYLE);
        BrewStyles<String, String> brewStyleObject;

        // slots can be missing, or slots can be provided but with empty value.
        // must test for both.
        if (brewStyleSlot == null || brewStyleSlot.getValue() == null) {
            if (!assignDefault) {
                throw new Exception("There was was no brew style slot specified");
            } else {
                // For sample skill, default to Seattle.
                brewStyleObject =
                        new BrewStyles<String, String>("V 60", "V60");
            }
        } else {
            String brewStyleName = brewStyleSlot.getValue().toLowerCase();
            if (BREW_STYLES.containsKey(brewStyleName)) {
                brewStyleObject = new BrewStyles<String, String>(brewStyleName, foramtBrewStyle(brewStyleName));
            } else {
                throw new Exception("Failed to get: " + brewStyleName + "from map");
            }
        }
        return brewStyleObject;
    }

    private Integer getSizeFromIntent(final Intent intent, final boolean assignDefault) throws Exception {
        Slot size = intent.getSlot(SLOT_SIZE);
        Integer formattedSize = null;

        if (size == null || size.getValue() == null) {
            if (!assignDefault) {
                throw new Exception("There was was no size slot specified");
            } else {
                formattedSize = 1;
            }
        } else {
            String stringSize = size.getValue();
            try {
                formattedSize = Integer.getInteger(stringSize);
            } catch (Exception e) {
                throw new Exception("Failed to get the amount of coffee the user wants");
            }
        }
        return formattedSize;
    }

    private String foramtBrewStyle(String brewStyle) {
        return brewStyle.replace(" ", "").toUpperCase();
    }

    /**
     * Gets the bean type from the intent, defaulting to Columbian if none provided, or returns an error.
     */
    private BrewStyles<String, String> getBeanInformationFromIntent(final Intent intent) {
        Slot beanSlot = intent.getSlot(SLOT_BEAN);
        BrewStyles<String, String> beanOject;

        // slots can be missing, or slots can be provided but with empty value.
        // must test for both
        if (beanSlot == null || beanSlot.getValue() == null) {
            // default to Medium Roast Bean
            beanOject = new BrewStyles<String, String>("Columbian", "bean=medium roast");
            return beanOject;
        } else {
            String bean;
            try {
                bean = BREW_STYLES.get(beanSlot.getValue());
            } catch (Exception e) {
                bean = "Columbian";
            }

            return new BrewStyles<String, String>("Bean", "bean=" + bean);
        }
    }

    private String getAllBrewStyles() {
        StringBuilder stationList = new StringBuilder();
        for (String style : BREW_STYLES.keySet()) {
            stationList.append(style);
            stationList.append(", ");
        }
        return stationList.toString();
    }

    /**
     * Wrapper for creating the Ask response from the input strings with
     * plain text output and reprompt speeches.
     *
     * @param stringOutput
     *            the output to be spoken
     * @param repromptText
     *            the reprompt for if the user doesn't reply or is misunderstood.
     * @return SpeechletResponse the speechlet response
     */
    private SpeechletResponse newAskResponse(String stringOutput, String repromptText) {
        return newAskResponse(stringOutput, false, repromptText, false);
    }

    /**
     * Wrapper for creating the Ask response from the input strings.
     *
     * @param stringOutput
     *            the output to be spoken
     * @param isOutputSsml
     *            whether the output text is of type SSML
     * @param repromptText
     *            the reprompt for if the user doesn't reply or is misunderstood.
     * @param isRepromptSsml
     *            whether the reprompt text is of type SSML
     * @return SpeechletResponse the speechlet response
     */
    private SpeechletResponse newAskResponse(String stringOutput, boolean isOutputSsml,
                                             String repromptText, boolean isRepromptSsml) {
        OutputSpeech outputSpeech, repromptOutputSpeech;
        if (isOutputSsml) {
            outputSpeech = new SsmlOutputSpeech();
            ((SsmlOutputSpeech) outputSpeech).setSsml(stringOutput);
        } else {
            outputSpeech = new PlainTextOutputSpeech();
            ((PlainTextOutputSpeech) outputSpeech).setText(stringOutput);
        }

        if (isRepromptSsml) {
            repromptOutputSpeech = new SsmlOutputSpeech();
            ((SsmlOutputSpeech) repromptOutputSpeech).setSsml(stringOutput);
        } else {
            repromptOutputSpeech = new PlainTextOutputSpeech();
            ((PlainTextOutputSpeech) repromptOutputSpeech).setText(repromptText);
        }

        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(repromptOutputSpeech);
        return SpeechletResponse.newAskResponse(outputSpeech, reprompt);
    }

    /**
     * Encapsulates the speech and api value for size and brew style objects.
     *
     * @param <L>
     *            text that will be spoken to the user
     * @param <R>
     *            text that will be passed in as an input to an API
     */
    public static class BrewStyles<L, R> {
        private final L speechValue;
        private final R apiValue;

        public BrewStyles(L speechValue, R apiValue) {
            this.speechValue = speechValue;
            this.apiValue = apiValue;
        }
    }
}