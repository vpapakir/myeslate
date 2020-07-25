package gr.cti.eslate.mapModel;

import gr.cti.eslate.protocol.MotionInfo;

import java.awt.Cursor;

import javax.swing.ImageIcon;

class Helpers {
    protected static ImageIcon loadImageIcon(String filename) {
        try {
            return new ImageIcon(Helpers.class.getResource(filename));
        } catch(Exception e) {
            try {
                return new ImageIcon(Helpers.class.getResource("images/nothing.gif"));
            } catch(Exception e1) {
                return new ImageIcon();
            }
        }
    }

    protected static String getHumanReadableMotionKey(String s) {
        if (s==null)
            return s;
        else if (s.equals(MotionInfo.MOTION_ROAD_KEY))
            return MapCreator.bundleCreator.getString(MotionInfo.MOTION_ROAD_KEY);
        else if (s.equals(MotionInfo.MOTION_RAILWAY_KEY))
            return MapCreator.bundleCreator.getString(MotionInfo.MOTION_RAILWAY_KEY);
        else if (s.equals(MotionInfo.MOTION_SEA_KEY))
            return MapCreator.bundleCreator.getString(MotionInfo.MOTION_SEA_KEY);
        else if (s.equals(MotionInfo.MOTION_AIR_KEY))
            return MapCreator.bundleCreator.getString(MotionInfo.MOTION_AIR_KEY);
        else
            return s;
    }


    /**
     * Cursors
     */
    protected static Cursor normalCursor;
    protected static Cursor waitCursor;

    {
        normalCursor=Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
        waitCursor=Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
    }
}