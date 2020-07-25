//Title:        Canvas
//Version:
//Copyright:    Copyright (c) 1998
//Author:       George Birbilis
//Company:      CTI
//Description:  Avakeeo's Canvas component

package gr.cti.utils;

import java.awt.*;
import javax.swing.*;

public class MyColorChooser extends JColorChooser
{
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
  
    final JColorChooser samePane = new JColorChooser(Color.white);

    /**
     * Shows a modal color-chooser dialog and blocks until the
     * dialog is hidden.  If the user presses the "OK" button, then
     * this method hides/disposes the dialog and returns the selected color.
     * If the user presses the "Cancel" button or closes the dialog without
     * pressing "OK", then this method hides/disposes the dialog and returns
     * null.
     *
     * @param component    the parent Component for the dialog
     * @param title        the String containing the dialog's title
     * @param initialColor the initial Color set when the color-chooser is shown
     * @deprecated  This method used to be implemented using Swing internals that are
     *              no longer applicable. It is now merely a wrapper for
     *              <code>ColorChooser.showDialog</code>.
     */
    public Color showSameDialog(Component component,
                                       String title, Color initialColor) {
      // The implementation of this method relied on swing internals that have changed.
      // Use plain old showDialog.
      return showDialog(component, title, initialColor);
      /*
        if (initialColor!=null) samePane.setColor(initialColor);
        ColorTracker ok = new ColorTracker(samePane);
        JDialog dialog = createDialog(component, title, true, pane, ok, null);
        dialog.addWindowListener(new ColorChooserDialog.Closer());
        dialog.addComponentListener(new ColorChooserDialog.DisposeOnClose());

        dialog.show(); // blocks until user brings dialog down...

        return ok.getColor();
      */
    }


}