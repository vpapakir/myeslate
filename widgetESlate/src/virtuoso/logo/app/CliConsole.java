/*
===============================================================================

        FILE:  CliConsole.java
        
        PROJECT:
        
                Turtle Tracks
        
        CONTENTS:
        
                Standard CLI console
        
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

import virtuoso.logo.*;
import java.io.*;


/**
 * Standard CLI console
 */

public class CliConsole
extends InteractiveConsole
{

        protected BufferedReader _reader;


        /**
         * Create a CliConsole
         */
        public CliConsole()
        {
        }


        /**
         * Set up console
         */
        protected void setup()
        {
                _reader = new BufferedReader(new InputStreamReader(System.in));
                
                putStatusMessage("Turtle Tracks command line console v1.0");
                putStatusMessage("(Editor not available)");
        }


        /**
         * Has the console encountered EOF?
         *
         * @return true iff eof encountered
         */
        public synchronized boolean eof()
        {
                return false;
        }


        /**
         * Get a line from the console
         *
         * @return the string
         */
        public synchronized String getLine()
        {
                try
                {
                        return _reader.readLine();
                }
                catch (IOException e)
                {
                        putLine("I/O error reading from console!");
                        return "";
                }
        }


        /**
         * Prompt and read a command
         *
         * @param prompt the prompt
         *
         * @exception virtuoso.logo.LanguageException write not allowed, or io closed
         */
        public synchronized String promptGetLine(
                char prompt)
        {
                System.out.print(prompt);
                System.out.print(' ');
                System.out.flush();
                return getLine();
        }


        /**
         * Get all available data from stream
         *
         * @param buf buffer to read into
         *
         * @return how much data was actually read
         *
         * @exception virtuoso.logo.LanguageException read not allowed, or io closed
         */
        public synchronized int getAvailable(
                char[] buf)
        throws
                LanguageException
        {
                try
                {
                        int pos=0;
                        while (_reader.ready() && pos<buf.length)
                        {
                                buf[pos] = (char)(_reader.read());
                                pos++;
                        }
                        return pos;
                }
                catch (IOException e)
                {
                        throw new LanguageException(e.toString());
                }
        }


        /**
         * Write a char to the stream
         *
         * @param c the char
         */
        public synchronized void put(
                char c)
        {
                System.out.print(c);
                System.out.flush();
        }


        /**
         * Write a string to the console, without an EOL
         *
         * @param str the string
         */
        public synchronized void put(
                String str)
        {
                System.out.print(str);
                System.out.flush();
        }


        /**
         * Write a char array to the stream
         *
         * @param buf the buffer
         * @param num number of characters
         */
        public synchronized void put(
                char[] buf,
                int num)
        {
                System.out.print(new String(buf, 0, num));
                System.out.flush();
        }


        /**
         * Write a string to the stream, terminated with a newline
         *
         * @param str the string
         */
        public synchronized void putLine(
                String str)
        {
                System.out.println(str);
        }


        /**
         * Get a character from the console
         *
         * @return char the character
         */
        public synchronized char getChar()
        {
                try
                {
                        return (char)(_reader.read());
                }
                catch (IOException e)
                {
                        putLine("I/O error reading from console!");
                        return '\0';
                }
        }


        /**
         * Character available on console?
         *
         * @return true iff available
         */
        public synchronized boolean charAvail()
        {
                try
                {
                        return _reader.ready();
                }
                catch (IOException e)
                {
                        putLine("I/O error reading from console!");
                        return false;
                }
        }


        /**
         * Request a new editor
         *
         * @param data initial data for editor window
         *
         * @exception virtuoso.logo.LanguageException failed to open editor
         */
        public synchronized void createEditor(
                String data)
        throws
                LanguageException
        {
                throw new LanguageException("Editor not available");
        }

}



