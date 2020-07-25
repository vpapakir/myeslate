/*
===============================================================================

        FILE:  LogoVoid.java
        
        PROJECT:
        
                Turtle Tracks
        
        CONTENTS:
        
                Void logo object
        
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
 * Void object. Returned from procedures with no return value.
 */

public class LogoVoid
extends LogoObject
{

        /**
         * The void object. All voids are references to this one object. 
         * Thus, you can test for void by testing for equality with the
         * LogoVoid.obj reference, which should be faster than doing a
         * dynamic typecheck.
         */
        public static final LogoVoid obj = new LogoVoid();


        /**
         * Constructor
         */
        private LogoVoid()
        {
        }


        /**
         * Normally, you should never attempt to clone a LogoVoid.
         * This clone method actually does not clone the object, but instead
         * returns another reference to the global LogoVoid.
         *
         * @return a clone of this object
         */
        public Object clone()
        {
                return obj;
        }


        /**
         * Determine if another object is equal to this one
         *
         * @param obj what to compare with
         *
         * @return true iff equal
         */
        public boolean equals(
                Object obj)
        {
                return (obj instanceof LogoVoid);
        }


        /**
         * Write to a string (for state saving)
         *
         * @return the string
         */
        public String toString()
        {
                return new String();
        }


        /**
         * Unparse void
         *
         * @return the empty string
         */
        public String unparse()
        {
                return new String();
        }


        /**
         * Returns length of the object (i.e. 0)
         *
         * @return the length
         */
        public int length()
        {
                return 0;
        }


}



