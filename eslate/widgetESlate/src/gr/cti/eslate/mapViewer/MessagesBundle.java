package gr.cti.eslate.mapViewer;

import java.util.ListResourceBundle;

/**
 * Messages Bundle.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	3.0.0, 17-Nov-1999
 */
public class MessagesBundle extends ListResourceBundle {
	public Object [][] getContents() {
		return contents;
	}

	static final Object[][] contents={
		{"componentname",   "Map browser"},
		{"wrongtitle",      "Something was wrong!"},
		{"cantconnect",     "Can't connect to this map. Possible flaw."},
		{"mapscale",        "Scale"},
		{"unknown",         "  "},
		{"rotation",        "Rotation:"},
		{"degrees",         "degrees"},
		{"zoom",            "Zoom:"},
		{"mapspot",         "The location of the region in the greater map"},
		{"distance",        "The distance is"},
		{"km",              "km"},
		{"m",               "m"},
		{"and",             "and"},
		{"chooselayer",     "Choose the layer to put the new object:"},
		{"alwaysinsert",    "Always insert with these settings."},
		{"alwaysinsertinfo","(To change settings click the right mouse button to insert an object)"},
		{"defineicon",      "Define the icon that will be used to paint the object:"},
		{"newicon",         "Define"},
		{"newlayer",        "New"},
		{"ok",              "OK"},
		{"cancel",          "Cancel"},
		{"cannotaddobject", "Cannot add the object to the map!"},
		{"noname",          "You must give a name to the layer."},
		{"givelayername",   "New layer name:"},
		{"defaultlayername","My objects"},
		{"showinallregions","Show the layer in all regions."},
		{"showinthisregion","Show the layer in this region only."},
		{"field",           "Field"},
		{"promptnewlayer",  "You have not specified a layer to insert the object.\nYou may define a new one now."},
		{"cannotremoveobject","Could not delete the object! Object placed back in its position."},
		{"cannoteditobject","The object couldn't be edited!"},
		{"agentnotpositioned",  "This agent is not positioned, that is it doesn't know where to stand on the map.\nPlease click on the map to specify its position.\nThis agent can stay to the places where your mouse pointer turns to a cross.\n(If the cursor on the map is your default mouse cursor, click once to activate it first)"},
		{"agentnotconnected","The agent refuses to accept the position you specified."},
		{"agentcantconnect","The agent cannot connect to the map.\nMaybe the map doesn't have a valid coordinate space."},
		{"requestedlayernotpresent","This agent needs information from the map that are not available. Try placing the agent on another map or redefine the map."},
		{"askpositionagent","This agent needs information from the map that are not available but has a valid location.\nDo you still want to add the agent?"},
		{"agentpath",       "Path"},
		{"embark",          "See a list of agents this agent may embark on."},
		{"disembark",       "Click to disembark any agent."},
		{"close",           "Close"},
		{"clearicon",       "Clear"},
		{"scrollpan",       "Go to the neighbouring region"},
		//Legend
		{"leg.legend",          "Legend"},
		{"leg.layers",          "Layers"},
		{"leg.backgrounds",     "Backgrounds"},
		{"leg.paths",           "Paths"},
		{"leg.lamptip",         "Show/Hide the layer"},
		{"leg.lampagenttip",    "Show/Hide the path"},
		{"leg.dragtip",         "Drag upwards/downwards to reorder the layers"},
		{"leg.dragagenttip",    "Drag upwards/downwards to reorder the paths"},
		{"leg.pointdraw",       "Point layer"},
		{"leg.pointicon",       "Point layer with icon"},
		{"leg.pointmultipleicon","Point layer with multiple icons"},
		{"leg.linedraw",        "Line layer"},
		{"leg.polygondraw",     "Polygon layer"},

		{"zoom.value",          "Change zoom..."},
		{"zoom.fifty",          "50%"},
		{"zoom.hundred",        "100%"},
		{"zoom.twohundred",     "200%"},
		{"zoom.fivehundred",    "500%"},
		{"zoom.gvvalue",        "Give a new zoom value (%)"},
		{"zoom.min",            "Change minimum zoom value..."},
		{"zoom.gvmin",          "Set the minimum zoom value"},
		{"zoom.max",            "Change maximum zoom value..."},
		{"zoom.gvmax",          "Set the maximum zoom value"},

		{"LoadTimer",             "MapViewer load"},
		{"SaveTimer",             "MapViewer save"},
		{"ConstructorTimer",      "MapViewer constructor"},
		{"InitESlateAspectTimer", "MapViewer E-Slate part creation"},
		{"ActiveRegionChanged",     "Active region change"},
		{"FullMapRedraw",           "Map layers full redraw"},
		{"PartialMapRedraw",        "Map layers partial redraw"},
		{"FullLabelRedraw",         "Labels full redraw"},
		{"PartialLabelRedraw",      "Labels partial redraw"},
		{"GetLayersFromModel",      "Read layer from model"},
		{"GetBackgroundFromModel",  "Read background image from model"},

	};
}
