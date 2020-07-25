/*
===============================================================================

        FILE:  ParseObject.java
        
        PROJECT:
        
                Turtle Tracks
        
        CONTENTS:
        
                Node in a parse tree
        
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
 * General parsetree node
 */

public abstract class ParseObject
implements
        Cloneable
{

        /**
         * Clone the object
         *
         * @return a clone of this object
         */
        public abstract Object clone();


        /**
         * Determine if another object is equal to this one
         *
         * @param obj what to compare with
         *
         * @return true iff equal
         */
        public abstract boolean equals(
                Object obj);


        /**
         * Evaluate this object in the given environment
         *
         * @param interp the environment
         *
         * @return the value
         *
         * @exception virtuoso.logo.LanguageException error thrown
         * @exception virtuoso.logo.ThrowException exception thrown
         */
        abstract LogoObject evaluate(
                InterpEnviron interp)
        throws
                LanguageException,
                ThrowException;


        /**
         * The name of the procedure
         *
         * @return the name
         */
        String procName()
        {
                return new String();
        }

}



