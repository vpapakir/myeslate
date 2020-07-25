/*
===============================================================================

        FILE:  SetupException.java
        
        PROJECT:
        
                Turtle Tracks
        
        CONTENTS:
        
                Problem during setup
        
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


package virtuoso.logo;


/**
 * Problem during setup. This exception should be thrown by a Console or
 * PrimitiveGroup if a fatal error occurs during initialization.
 */

public final class SetupException
extends Exception
{
        /**
         * Serialization version.
         */
        final static long serialVersionUID = 1L;
        /**
         * Unknown error during setup
         */
        public static final int unknownErr = 0;
        
        /**
         * Unable to find method for reflection
         */
        public static final int noSuchMethodErr = 1;
        
        /**
         * Unable to find primitive for aliasing from another primitive group
         */
        public static final int noSuchPrimitiveErr = 2;
        
        /**
         * Security error
         */
        public static final int securityError = 3;


        private int _code;


        /**
         * Constructor with a message and a code
         *
         * @param s detail message
         * @param c error code
         */
        public SetupException(
                String s,
                int c)
        {
                super(s);
                _code = c;
        }


        /**
         * Constructor with a message
         *
         * @param s detail message
         */
        public SetupException(
                String s)
        {
                super(s);
                _code = unknownErr;
        }


        /**
         * Constructor with a code
         *
         * @param c error code
         */
        public SetupException(
                int c)
        {
                super("SetupException");
                _code = c;
        }


        /**
         * Constructor with no information
         */
        public SetupException()
        {
                super("SetupException");
                _code = unknownErr;
        }


        /**
         * Gets the code
         *
         * @return the error code
         */
        public final int getCode()
        {
                return _code;
        }

}



