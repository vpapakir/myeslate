package gr.cti.eslate.mapViewer;

import java.util.ListResourceBundle;

public class ToolbarTipBundle extends ListResourceBundle {
	public Object [][] getContents() {
		return contents;
	}

	static final Object[][] contents={
		{"showMenu",    "Show action menu"},
		{"navigate",    "Direct active agent"},
		{"navigatehelp","Click to navigate the active agent."},
		{"pan",         "Scroll the map"},
		{"panhelp",     "Drag the map to the desired position."},
		{"activate",    "Set active object"},
		{"browse",      "Browse map"},
		{"activatehelp","Click on an object."},
		{"select",      "Select using rectangle"},
		{"selectCircle","Select using circle"},
		{"browsehelp",  "Object activation and/or map dragging"},
		{"select",      "Select object on the map"},
		{"selecthelp",  "Drag to define a region."},
		{"selectcontrolhelp","Add to/Remove from current selection."},
		{"rotate",      "Rotate the map"},
		{"rotatehelp",  "Drag to the left or to the right for clockwise or anti-clockwise rotation."},
		{"edit",        "Insert object"},
		{"edithelp",    "You may insert, delete or modify objects."},
		{"zoom",        "Enlarge/Diminish the view"},
		{"goIn",        "Go to another area"},
		{"goInhelp",    "Click inside a rectangle."},
		{"goOut",       "Return to the previous area"},
		{"goOuthelp",   "Click on the map to return to"},
		{"meter",       "Measure distances"},
		{"meterhelp",   "Drag the mouse between two points."},
		{"grid",        "Show/Hide grid"},
		{"miniature",   "See an active miniaturization of the region"},
		{"miniaturehelp","The yellow rectangle shows the area viewed."},
		{"identify",    "Identify an object on the map"},
		{"identifyhelp","Point with the mouse an object and see what it is."},
		{"layerVisib",  "Show/Hide layers, images, paths"},
		{"showhide",    "Show/Hide unselected features"},
		{"legend",      "Show map legend"},
		{"spot",        "Location of this region in the map"},
		{"zoomRect",    "Enlarge a specific area (Hold CTRL to diminish)"},
		{"zoomRecthelp","Define a rectangle with the mouse on the map to zoom in."},
		{"zoomRectcontrolhelp","Define a rectangle with the mouse on the map to zoom out."},
	};
}
