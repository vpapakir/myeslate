/*
===============================================================================

        FILE:  PrimitiveSpec.java
        
        PROJECT:
        
                Turtle Tracks
        
        CONTENTS:
        
                Primitive identification
        
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


import java.lang.reflect.*;


/**
 * Specification of a primitive definition
 */

public final class PrimitiveSpec
{

        private PrimitiveGroup _group;
        private Method _method;
        private int _defaultArgs;


        /**
         * Constructor
         *
         * @param g the PrimitiveGroup defining the primitive
         * @param m the method within the group
         * @param da the number of default arguments
         */
        public PrimitiveSpec(
                PrimitiveGroup g,
                Method m,
                int da)
        {
                _group = g;
                _method = m;
                _defaultArgs = da;
        }


        /**
         * Extractor for primitive group
         *
         * @return the primitive group
         */
        public final PrimitiveGroup group()
        {
                return _group;
        }


        /**
         * Extractor for method
         *
         * @return the method
         */
        public final Method method()
        {
                return _method;
        }


        /**
         * Extractor for default number of arguments
         *
         * @return the default number of arguments
         */
        public final int numArgs()
        {
                return _defaultArgs;
        }

}



