package gr.cti.eslate.agent;

import java.util.ListResourceBundle;

/**
 * Agent BeanInfo bundle.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	1.0.0, 09-May-2000
 */
public class BundleAgentBeanInfo extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"componentFace",   "Face"},
        {"componentFaceTip","This is the face of the agent. It is shown on the agent component only."},
        {"phaseImages",     "Representation"},
        {"phaseImagesTip",  "Click the button to change the representation of the agent"},
        {"phases",          "Phases"},
        {"phasebase",       "Base image:"},
        {"faceauto",        "Other images automaticaly generated"},
        {"facemanual",      "Other images are user-defined"},
        {"definephases",    "Customize agent's representation (3 steps)"},
        {"previous",        "< Previous"},
        {"next",            "Next >"},
        {"cancel",          "Cancel"},
        {"finish",          "Finish"},
        {"step1desc",       "STEP 1:\n- Choose how you would like to specify the representation of the agent (the representation of the agent is the image of the agent in the components that show it):"},
        {"step1choice1",    "I will provide one image and all the other phases will be produced automatically."},
        {"step1choice2",    "I will provide all the images of the phases I am intrested in."},
        {"step2desc",       "STEP 2:\n- Define the number of phases. The agent will have as many images as the phases you define, one for each range of tilt angles.\n\nExample:\nIf you define 10 phases, the agent will provide an image every 36 degrees. It will have one image when its tilt angle is from -18 to 18 degrees, another when its tilt angle is from 18 to 54 degrees and so on."},
        {"step3desc",       "STEP 3:\n- Finally, define the base icon.  Pressing the \"Define\" button, you can create a new one or use an existing image."},
        {"step3desc2",      "This image will be used to automatically produce the images for all the agent phases."},
        {"step3define",     "Define"},
        {"step3phase",      "Phase"},
        {"step3icon",       "Image"},
        {"step32desc",      "STEP 3:\n- Finally, define the phase images. Each color region in the circle below denotes an range of angles. When the agent heads between one of this ranges, it uses the same image. In the table on the right you define the images for each angle range (phase)."},
        {"always",          "ALWAYS"},
        {"never",           "NEVER"},
        {"onmouse",         "ON MOUSE OVER"},
        {"travelseverywhere","ANYTHING"},
        {"travelsonroads",  "ROADS"},
        {"travelsonrailways","RAILWAYS"},
        {"travelsonsea",    "SEA"},
        {"travelsonair",    "AIRLANES"},
        {"travelsoncustom", "CUSTOM LAYER (Define)"},
        {"travellingOnMotionLayerID","Travels on"},
        {"travellingOnMotionLayerIDTip","Select where the agent will travel"},
        {"embarksOn",       "Embarks on"},
        {"embarksOnTip",    "Select the agent types where this agent can embark (Use CTRL for multiple)"},
        {"man",             "MAN"},
        {"auto",            "AUTOMOBILE"},
        {"train",           "TRAIN"},
        {"ship",            "SHIP"},
        {"plane",           "AIRPLANE"},
        {"all",             "ALL AGENTS"},
        {"other",           "OTHER (Define)"},
        {"type",            "Is a"},
        {"typeTip",         "Defines what the agent is"},
        {"velocity",        "Velocity of motion"},
        {"velocityTip",     "This is the velocity the agent will have when it starts moving in km/h"},
        {"minVelocity",     "Velocity minimum"},
        {"minVelocityTip",  "The minimum agent velocity in km/h"},
        {"maxVelocity",     "Velocity maximum"},
        {"maxVelocityTip",  "The maximum agent velocity in km/h"},
        {"border",          "Border"},
        {"borderTip",       "Controls the component border"},
        {"statusbarVisible","Status bar visible"},
        {"statusbarVisibleTip","Controls the visibility of the status bar"},
        {"locationChanged", "Location changed"},
        {"agentStopped",    "Agent stopped"},
        {"geographicObjectTouched","Touched geographic object"},
        {"motionObjectChanged","Motion object changed"},
        {"agentMeeting",    "Met another agent"},
        {"initLoc",         " Initial location"},
        {"initLocTip",      "Sets the initial location. Active only when the agent is not positioned."},
        {"initDefined",     "Already defined"},
        {"initUndefined",   "Define"},
        {"initLongt",       "Give initial longitude (lambda) or cartesian x:"},
        {"initLat",         "Give initial latitude (phi) or cartesian y:"},
        {"invalidInit",     "Invalid initial location!"},
        {"alwaysVisible",   "Always visible"},
        {"alwaysVisibleTip","Denotes that the agent would like to be always visible, no matter where it is, in its hosting components."},
        {"unitTolerance",   "\"Hotspot\" in host units around center"},
        {"unitToleranceTip","The tolerance \"hotspot\" in host units around the agent center"},
        {"stopAtCrossings", "Stop at crossings"},
        {"stopAtCrossingsTip","Tells the agent to stop at xings or try to find another road to continue"},
    };
}
