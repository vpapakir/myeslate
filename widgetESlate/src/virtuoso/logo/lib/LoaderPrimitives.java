/*
===============================================================================

        FILE:  LoaderPrimitives.java
        
        PROJECT:
        
                Turtle Tracks
        
        CONTENTS:
        
                Loader primitives
        
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


package virtuoso.logo.lib;

import virtuoso.logo.*;


/**
 * Names of primitives (keywords)
 */

public final class LoaderPrimitives
extends PrimitiveGroup
{

        /**
         * Set up primitive group
         */
        protected void setup(
                Machine mach,
                Console console)
        throws
                SetupException
        {
                registerPrimitive("LOADPRIMITIVES", "pLOADPRIMITIVES", 1);
                registerPrimitive("UNLOADPRIMITIVES", "pUNLOADPRIMITIVES", 1);
                
                console.putStatusMessage("Turtle Tracks dynamic loading primitives v1.0");
        }


        /**
         * Primitive implementation for LOADPRIMITIVES
         */
        public final LogoObject pLOADPRIMITIVES(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                if (!(params[0] instanceof LogoWord))
                {
                        throw new LanguageException("Classname expected");
                }
                interp.mach().loadPrimitives(params[0].toString());
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for UNLOADPRIMITIVES
         */
        public final LogoObject pUNLOADPRIMITIVES(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                if (!(params[0] instanceof LogoWord))
                {
                        throw new LanguageException("Classname expected");
                }
                interp.mach().unloadPrimitives(params[0].toString());
                return LogoVoid.obj;
        }

}



