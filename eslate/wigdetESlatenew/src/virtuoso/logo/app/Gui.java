/*
===============================================================================

        FILE:  Gui.java
        
        PROJECT:
        
                Turtle Tracks
        
        CONTENTS:
        
                Runs logo with an AWT-based console
        
        PROGRAMMERS:
        
                Daniel Azuma (DA)  <dazuma@kagi.com>
        
        COPYRIGHT:
        
                Copyright (C) 1997-1999  Daniel Azuma  (dazuma@kagi.com)
                
                This program is free software; you can redistribute it and/or
                modify it under the terms of the GNU General Public License
                as published by the Free Software Foundation; either version 2
                of the License, or (at your option) any later version.
                
                This program is distributed in the hope that it will be useful,
                but WITHOUT ANY WARRANTY; without even the implied warranty of
                MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
                GNU General Public License for more details.
                
                You should have received a copy of the GNU General Public
                License along with this program. If not, you can obtain a copy
                by writing to:
                        Free Software Foundation, Inc.
                        59 Temple Place - Suite 330,
                        Boston, MA  02111-1307, USA.
        
        VERSION:
        
                Turtle Tracks 1.0  (13 November 1999)
        
        CHANGE HISTORY:
        
                13 November 1999 -- DA -- Released under GNU General Public License

===============================================================================
*/


package virtuoso.logo.app;


/**
 * Runs logo with an AWT-based console
 */

public class Gui
{

        /**
         * No instantiation of this class
         */
        private Gui()
        {
        }


        /**
         * Main program
         *
         * @param args command line arguments
         */
        public static void main(String[] args)
        {
                String[] realArgs = new String[args.length+5];
                realArgs[0] = "-std";
                realArgs[1] = "-p";
                realArgs[2] = "virtuoso.logo.lib.TurtlePrimitives";
                realArgs[3] = "-c";
                realArgs[4] = "virtuoso.logo.app.GuiConsole";
                System.arraycopy(args, 0, realArgs, 5, args.length);
                
                Run.main(realArgs);
        }

}



