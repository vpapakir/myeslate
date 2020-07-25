/*
===============================================================================

        FILE:  IOBase.java
        
        PROJECT:
        
                Turtle Tracks
        
        CONTENTS:
        
                Interface to logo stream objects
        
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
 * IO interface. This is the base interface for all stream objects
 */

public interface IOBase
{

        /**
         * Close the stream
         *
         * @exception virtuoso.logo.LanguageException can't close
         */
        public abstract void close()
        throws
                LanguageException;


        /**
         * Is the stream open?
         *
         * @return true iff can interact with stream
         */
        public abstract boolean isOpen();


        /**
         * Get the name of this object
         *
         * @return the name as a LogoObject
         */
        public abstract LogoObject name();


        /**
         * Get the kind of this object
         *
         * @return the kind as a LogoObject
         */
        public abstract LogoObject kind();


        /**
         * Has the stream encountered EOF?
         *
         * @return true iff eof encountered
         *
         * @exception virtuoso.logo.LanguageException stream closed
         */
        public abstract boolean eof()
        throws
                LanguageException;


        /**
         * Get a line from the stream
         *
         * @return the string
         *
         * @exception virtuoso.logo.LanguageException read not allowed, or io closed
         */
        public abstract String getLine()
        throws
                LanguageException;


        /**
         * Get all available data from stream
         *
         * @param buf buffer to read into
         *
         * @return how much data was actually read
         *
         * @exception virtuoso.logo.LanguageException read not allowed, or io closed
         */
        public abstract int getAvailable(
                char[] buf)
        throws
                LanguageException;


        /**
         * Get a character from the stream
         *
         * @return char the character
         *
         * @exception virtuoso.logo.LanguageException read not allowed, or io closed
         */
        public abstract char getChar()
        throws
                LanguageException;


        /**
         * Character available on stream?
         *
         * @return true iff available
         *
         * @exception virtuoso.logo.LanguageException read not allowed, or io closed
         */
        public abstract boolean charAvail()
        throws
                LanguageException;


        /**
         * Write a string to the stream
         *
         * @param str the string
         *
         * @exception virtuoso.logo.LanguageException write not allowed, or io closed
         */
        public abstract void put(
                String str)
        throws
                LanguageException;


        /**
         * Write a char to the stream
         *
         * @param c the char
         *
         * @exception virtuoso.logo.LanguageException write not allowed, or io closed
         */
        public abstract void put(
                char c)
        throws
                LanguageException;


        /**
         * Write a char array to the stream
         *
         * @param buf the buffer
         * @param num number of characters
         *
         * @exception virtuoso.logo.LanguageException write not allowed, or io closed
         */
        public abstract void put(
                char[] buf,
                int num)
        throws
                LanguageException;


        /**
         * Write a string to the stream, terminated with a newline
         *
         * @param str the string
         *
         * @exception virtuoso.logo.LanguageException write not allowed, or io closed
         */
        public abstract void putLine(
                String str)
        throws
                LanguageException;


        /**
         * Flush the stream
         *
         * @exception virtuoso.logo.LanguageException write not allowed, or io closed
         */
        public abstract void flush()
        throws
                LanguageException;

}



