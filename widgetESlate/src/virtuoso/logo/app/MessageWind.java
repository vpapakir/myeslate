/*
===============================================================================

        FILE:  MessageWind.java
        
        PROJECT:
        
                Turtle Tracks
        
        CONTENTS:
        
                Message window used by graphical console
        
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

import java.awt.*;
import java.awt.event.*;


/**
 * A class for message alerts
 */

class MessageWind
extends Dialog
implements
        ActionListener
{
        /**
         * Serialization version.
         */
        final static long serialVersionUID = 1L;
        
        private int _result;


        /**
         * Generate an empty array of strings
         *
         * @return the array
         */
        static String[] genStrings()
        {
                return new String[0];
        }


        /**
         * Generate an array of one string
         *
         * @param str1 a string
         *
         * @return the array
         */
        static String[] genStrings(
                String str1)
        {
                String[] ret = new String[1];
                ret[0] = str1;
                return ret;
        }


        /**
         * Generate an array of two strings
         *
         * @param str1 a string
         * @param str2 a string
         *
         * @return the array
         */
        static String[] genStrings(
                String str1,
                String str2)
        {
                String[] ret = new String[2];
                ret[0] = str1;
                ret[1] = str2;
                return ret;
        }


        /**
         * Generate an array of three strings
         *
         * @param str1 a string
         * @param str2 a string
         * @param str3 a string
         *
         * @return the array
         */
        static String[] genStrings(
                String str1,
                String str2,
                String str3)
        {
                String[] ret = new String[3];
                ret[0] = str1;
                ret[1] = str2;
                ret[2] = str3;
                return ret;
        }


        /**
         * Generate an array of four strings
         *
         * @param str1 a string
         * @param str2 a string
         * @param str3 a string
         * @param str4 a string
         *
         * @return the array
         */
        static String[] genStrings(
                String str1,
                String str2,
                String str3,
                String str4)
        {
                String[] ret = new String[4];
                ret[0] = str1;
                ret[1] = str2;
                ret[2] = str3;
                ret[3] = str4;
                return ret;
        }


        /**
         * Interface method. Puts up a window, waits for it to be dismissed,
         * and returns the number of the button pressed
         *
         * @param message the message to display
         * @param parent parent frame
         */
        static int execute(
                String[] messages,
                Frame parent)
        {
                MessageWind wind = new MessageWind(messages, parent, null);
                wind.setVisible(true);
                return wind.result();
        }


        /**
         * Interface method. Puts up a window, waits for it to be dismissed,
         * and returns the number of the button pressed
         *
         * @param messages the message to display
         * @param buttons the text of the buttons
         * @param parent parent frame
         */
        static int execute(
                String[] messages,
                String[] buttons,
                Frame parent)
        {
                MessageWind wind = new MessageWind(messages, buttons, parent, null);
                wind.setVisible(true);
                return wind.result();
        }


        /**
         * Constructor. Creates one "OK" button.
         *
         * @param message the message to display
         * @param parent parent frame
         * @param actor the ActionListener
         */
        MessageWind(
                String[] messages,
                Frame parent,
                ActionListener actor)
        {
                super(parent, true);
                String[] buttons = new String[1];
                buttons[0] = "OK";
                setup(messages, buttons, actor);
        }


        /**
         * Constructor
         *
         * @param message the message to display
         * @param buttons the text of the buttons
         * @param parent parent frame
         * @param actor the ActionListener
         */
        MessageWind(
                String[] messages,
                String[] buttons,
                Frame parent,
                ActionListener actor)
        {
                super(parent, true);
                setup(messages, buttons, actor);
        }


        /**
         * Constructor helper
         *
         * @param message the message to display
         * @param buttons the text of the buttons
         * @param actor the ActionListener
         */
        private void setup(
                String[] messages,
                String[] buttons,
                ActionListener actor)
        {
                _result = -1;
                if (actor == null)
                {
                        actor = this;
                }
                
                GridBagLayout layout = new GridBagLayout();
                setLayout(layout);
                setBackground(Color.white);
                GridBagConstraints constraints = new GridBagConstraints();
                constraints.weightx = 1.0;
                constraints.weighty = 1.0;
                constraints.gridwidth = GridBagConstraints.REMAINDER;
                constraints.fill = GridBagConstraints.NONE;
                
                if (messages.length == 0)
                {
                        messages = new String[1];
                        messages[0] = "";
                }
                constraints.anchor = GridBagConstraints.WEST;
                constraints.insets = new Insets(5, 10, 0, 10);
                for (int i=0; i<messages.length; i++)
                {
                        if (i==1)
                        {
                                constraints.insets = new Insets(0, 10, 0, 10);
                        }
                        Label label = new Label(messages[i]);
                        layout.setConstraints(label, constraints);
                        add(label);
                }
                
                Panel btnPanel = new Panel();
                btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
                for (int i=0; i<buttons.length; i++)
                {
                        Button btn = new Button(buttons[i]);
                        btn.addActionListener(actor);
                        if (actor == this)
                        {
                                btn.setActionCommand(String.valueOf(i));
                        }
                        else
                        {
                                btn.setActionCommand(buttons[i]);
                        }
                        btnPanel.add(btn);
                }
                constraints.anchor = GridBagConstraints.EAST;
                constraints.insets = new Insets(5, 5, 5, 5);
                constraints.weighty = 0.0;
                layout.setConstraints(btnPanel, constraints);
                add(btnPanel);
                
                pack();
                setResizable(false);
        }


        /**
         * Get result
         *
         * @param message the message to display
         * @param buttons the text of the buttons
         * @param actor the ActionListener
         */
        int result()
        {
                return _result;
        }


        /**
         * Called on action (used internally only)
         *
         * @param event the event
         */
        public void actionPerformed(
                ActionEvent event)
        {
                try
                {
                        _result = Integer.parseInt(event.getActionCommand());
                        dispose();
                }
                catch (NumberFormatException e) {}
        }

}



