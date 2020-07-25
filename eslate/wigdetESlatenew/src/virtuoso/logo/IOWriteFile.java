/*
===============================================================================

        FILE:  IOWriteFile.java
        
        PROJECT:
        
                Turtle Tracks
        
        CONTENTS:
        
                Write file object
        
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
 * Stream that writes to a file
 */

public class IOWriteFile
extends IOStream
{

        private static LogoObject _kind;


        static
        {
                _kind = new LogoWord("WRITEFILE");
        }


        /**
         * Constructor
         *
         * @param f file to open
         *
         * @exception virtuoso.logo.LanguageException unable to open file
         */
        public IOWriteFile(
                File f)
        throws
                LanguageException
        {
                try
                {
                        open(new LogoWord(f.getPath()), new BufferedWriter(new FileWriter(f)));
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


        public IOWriteFile(File f, boolean flag) throws LanguageException{ //Birb-29Jun2000: added for compatibility with compiled clients
   this(f); //assuming false?
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



