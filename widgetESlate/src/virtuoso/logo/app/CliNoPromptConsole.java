/*
===============================================================================

        FILE:  CliNoPromptConsole.java
        
        PROJECT:
        
                Turtle Tracks
        
        CONTENTS:
        
                CLI console without prompts, for non-interactive sessions
        
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

import java.io.*;


/**
 * CLI console without prompts, for non-interactive sessions
 */

public class CliNoPromptConsole
extends CliConsole
{

        /**
         * Write a string to the stream, terminated with a newline, as part of the setup
         * process.
         *
         * @param str the string
         */
        public void putSetupMessage(
                String str)
        {
        }


        /**
         * Has the console encountered EOF?
         *
         * @return true iff eof encountered
         */
        public synchronized boolean eof()
        {
                try
                {
                        _reader.mark(2);
                        int val = _reader.read();
                        _reader.reset();
                        return (val == -1);
                }
                catch (IOException e)
                {
                        return true;
                }
        }


        /**
         * Prompt and read a command
         *
         * @param prompt the prompt
         */
        public synchronized String promptGetLine(
                char prompt)
        {
                return getLine();
        }

}



