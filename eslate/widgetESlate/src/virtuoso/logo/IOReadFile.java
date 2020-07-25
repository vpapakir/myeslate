/*
===============================================================================

        FILE:  IOReadFile.java
        
        PROJECT:
        
                Turtle Tracks
        
        CONTENTS:
        
                Read file object
        
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

import java.io.*;


/**
 * Stream that reads from a file
 */

public class IOReadFile
extends IOStream
{

        private static LogoObject _kind;


        static
        {
                _kind = new LogoWord("READFILE");
        }


        /**
         * Constructor
         *
         * @param f the file to open
         *
         * @exception virtuoso.logo.LanguageException unable to open file
         */
        public IOReadFile(
                File f)
        throws
                LanguageException
        {
                try
                {
                        open(new LogoWord(f.getPath()), new BufferedReader(new FileReader(f)));
                }
                catch (IOException e)
                {
                        throw new LanguageException("Couldn't open file: \"" + f.getPath() +
                                "\" I/O: " + e.getMessage());
                }
                catch (SecurityException e)
                {
                        throw new LanguageException("Couldn't open file: \"" + f.getPath() +
                                "\" Security: " + e.toString());
                }
        }


        /**
         * Get the kind of this object
         *
         * @return the kind as a LogoObject
         */
        public synchronized LogoObject kind()
        {
                return _kind;
        }

}



