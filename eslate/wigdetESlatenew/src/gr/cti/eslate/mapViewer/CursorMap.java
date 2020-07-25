package gr.cti.eslate.mapViewer;

import java.awt.Cursor;
import java.awt.Point;

/**
 * Cursor provider class.
 * @author Giorgos Vasiliou
 * @version 1.0, 03-Jun-2002
 */

class CursorMap extends java.util.HashMap {
	/**
	 * This method puts the "family" of cursors in the hashmap the first time
	 * one of the members is needed.
	 */
	public Object get(Object s) {
		Object o=super.get(s);
		if (o==null && s!=null) {
			String pref=((String) s).substring(0,3);
			if (pref.equals("act")) {
				put("activatecursor",MapViewer.createCustomCursor("images/activatecursor.gif",null,p12x9,"activatecursor"));
				put("activateagentpathcursor",MapViewer.createCustomCursor("images/agentpath.gif",null,new Point(8,2),"activateagentpathcursor"));
			} else if (pref.equals("pan")) {
				put("pancursorhand",MapViewer.createCustomCursor("images/pancursorhand.gif",p7x7,p15x9,"pancursorhand"));
				put("pancursor",MapViewer.createCustomCursor("images/pancursor.gif",p7x7,p15x9,"pancursor"));
				put("pancursorW",MapViewer.createCustomCursor("images/pancursorW.gif",p7x7,p15x9,"pancursorW"));
				put("pancursorE",MapViewer.createCustomCursor("images/pancursorE.gif",p7x7,p15x9,"pancursorE"));
				put("pancursorN",MapViewer.createCustomCursor("images/pancursorN.gif",p7x7,p15x9,"pancursorN"));
				put("pancursorS",MapViewer.createCustomCursor("images/pancursorS.gif",p7x7,p15x9,"pancursorS"));
				put("pancursorNS",MapViewer.createCustomCursor("images/pancursorNS.gif",p7x7,p15x9,"pancursorNS"));
				put("pancursorEW",MapViewer.createCustomCursor("images/pancursorEW.gif",p7x7,p15x9,"pancursorEW"));
				put("pancursorSE",MapViewer.createCustomCursor("images/pancursorSE.gif",p7x7,p15x9,"pancursorSE"));
				put("pancursorSW",MapViewer.createCustomCursor("images/pancursorSW.gif",p7x7,p15x9,"pancursorSW"));
				put("pancursorNE",MapViewer.createCustomCursor("images/pancursorNE.gif",p7x7,p15x9,"pancursorNE"));
				put("pancursorNW",MapViewer.createCustomCursor("images/pancursorNW.gif",p7x7,p15x9,"pancursorNW"));
				put("pancursorNSE",MapViewer.createCustomCursor("images/pancursorNSE.gif",p7x7,p15x9,"pancursorNSE"));
				put("pancursorNEW",MapViewer.createCustomCursor("images/pancursorNEW.gif",p7x7,p15x9,"pancursorNEW"));
				put("pancursorNSW",MapViewer.createCustomCursor("images/pancursorNSW.gif",p7x7,p15x9,"pancursorNSW"));
				put("pancursorSEW",MapViewer.createCustomCursor("images/pancursorSEW.gif",p7x7,p15x9,"pancursorSEW"));
			} else if (pref.equals("zoo")) {
				put("zoomcursor",MapViewer.createCustomCursor("images/zoomcursor.gif",p7x7,p15x9,"zoomcursor"));
				put("zoomincursor",MapViewer.createCustomCursor("images/zoomincursor.gif",p7x7,p15x9,"zoomincursor"));
				put("zoomoutcursor",MapViewer.createCustomCursor("images/zoomoutcursor.gif",p7x7,p15x9,"zoomoutcursor"));
				put("zoomrectcursor",MapViewer.createCustomCursor("images/zoomrectcursor.gif",p7x7,new Point(15,14),"zoomrectcursor"));
				put("zoomoutrectcursor",MapViewer.createCustomCursor("images/zoomoutrectcursor.gif",p7x7,new Point(15,14),"zoomoutrectcursor"));
			} else if (pref.equals("rot")) {
				put("rotatecursor",MapViewer.createCustomCursor("images/rotatecursor.gif",p7x7,p15x9,"rotatecursor"));
				put("rotateleftcursor",MapViewer.createCustomCursor("images/rotateleftcursor.gif",p7x7,p15x9,"rotateleftcursor"));
				put("rotaterightcursor",MapViewer.createCustomCursor("images/rotaterightcursor.gif",p7x7,p15x9,"rotaterightcursor"));
				put("rotatecenteredcursor",MapViewer.createCustomCursor("images/rotatecenteredcursor.gif",p7x7,p15x9,"rotatecenteredcursor"));
			} else if (pref.equals("ide")) {
				if (java.util.Locale.getDefault().getISO3Country().equals("GRC")) {
					put("identifycursor",MapViewer.createCustomCursor("images/identifycursorgr.gif",p3x0,p12x9,"identifycursor"));
					put("identifyfoundcursor",MapViewer.createCustomCursor("images/identifycursor2gr.gif",p3x0,p12x9,"identifyfoundcursor"));
				} else {
					put("identifycursor",MapViewer.createCustomCursor("images/identifycursor.gif",p3x0,p12x9,"identifycursor"));
					put("identifyfoundcursor",MapViewer.createCustomCursor("images/identifycursor2.gif",p3x0,p12x9,"identifyfoundcursor"));
				}
			} else if (pref.equals("nav")) {
				put("navigatecursor",MapViewer.createCustomCursor("images/navigatecursor.gif",null,new Point(9,10),"navigatecursor"));
			} else if (pref.equals("pin")) {
				put("pincursor",MapViewer.createCustomCursor("images/pincursor.gif",null,new Point(9,21),"pincursor"));
				put("pincursormove",MapViewer.createCustomCursor("images/pincursormove.gif",null,new Point(9,21),"pincursormove"));
			} else if (pref.equals("res")) {
				put("resizeNE",Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
				put("resizeNW",Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
				put("resizeSE",Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
				put("resizeSW",Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
			} else if (pref.equals("rol")) {
				put("rollupcursor",MapViewer.createCustomCursor("images/rollupcursor.gif",null,new Point(16,28),"rollupcursor"));
				put("rolldowncursor",MapViewer.createCustomCursor("images/rolldowncursor.gif",null,new Point(15,3),"rolldowncursor"));
				put("rollleftcursor",MapViewer.createCustomCursor("images/rollleftcursor.gif",null,new Point(28,15),"rollleftcursor"));
				put("rollrightcursor",MapViewer.createCustomCursor("images/rollrightcursor.gif",null,new Point(3,16),"rollrightcursor"));
			} else {
				put("default",Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				put("crosshair",Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
				put("wait",Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				put("hand",Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
			o=super.get(s);
		}
		return o;
	}
	private Point p7x7=new Point(7,7);
	private Point p15x9=new Point(15,9);
	private Point p3x0=new Point(3,0);
	private Point p12x9=new Point(12,9);
}
