/*
===============================================================================

        FILE:  Console.java
        
        PROJECT:
        
                Turtle Tracks
        
        CONTENTS:
        
                Interface to console objects
        
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
 * Console interface. All console objects must extend this abstract class.
 */

public abstract class Console
implements
        IOBase,
        Runnable
{

        private static LogoObject _kind;
        
        /**
         * The machine associated with this console.
         */
        protected Machine _mach;


        static
        {
                _kind = new LogoWord("CONSOLE");
        }


        /**
         * Set machine. Should normally only be called once.
         *
         * @param mach the machine to use
         */
        final void setMachine(
                Machine mach)
        {
                _mach = mach;
        }


        /**
         * This method is called by the machine during setup. A console should override
         * this method and perform any initialization here. If an error occurs,
         * it should throw a SetupException.
         *
         * @exception virtuoso.logo.SetupException couldn't set up the console.
         */
        protected void setup()
        throws
                SetupException
        {
        }


        /**
         * Get machine associated with this console
         *
         * @return the machine
         */
        public final Machine mach()
        {
                return _mach;
        }


        /**
         * Close the stream. The default method throws a LanguageException.
         * Consoles should normally not override this behavior.
         *
         * @exception virtuoso.logo.LanguageException can't close (console)
         */
        public synchronized void close()
        throws
                LanguageException
        {
                throw new LanguageException("Can't close console");
        }


        /**
         * Is the stream open? The default method always returns true. Consoles
         * should normally not override this behavior.
         *
         * @return true iff can interact with stream
         */
        public synchronized boolean isOpen()
        {
                return true;
        }


        /**
         * Get the name of this object. The default method returns the empty
         * LogoList, to indicate the console I/O object. Consoles should normally
         * not override this behavior.
         *
         * @return the name as a LogoObject
         */
        public synchronized LogoObject name()
        {
                return new LogoList();
        }


        /**
         * Get the kind of this object. The default method returns the word
         * "CONSOLE". Consoles should normally not override this behavior.
         *
         * @return the kind as a LogoObject
         */
        public synchronized LogoObject kind()
        {
                return _kind;
        }


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
         * Prompt and read a command
         *
         * @param prompt the prompt
         */
        public abstract String promptGetLine(
                char prompt);


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
         */
        public abstract void putLine(
                String str);


        /**
         * Write a string to the stream, terminated with a newline, as a console
         * status message. Consoles can override this method to ignore the string if
         * they want to suppress status messages. This method is used by the TO
         * and TOMACRO special forms 
         *
         * @param str the string
         */
        public void putStatusMessage(
                String str)
        {
                putLine(str);
        }


  public void putSetupMessage(String str){ //Birb-28Jun2000: provided for compatibility so that existing primitive groups won't break when calling this proc (in TurtleTracks it was renamed to "putStatusMessage")
   putLine(str);
  }


        /**
         * "Flush" the console.
         */
        public synchronized void flush()
        {
        }


        /**
         * Request a new editor
         *
         * @param data initial data for editor window
         *
         * @exception virtuoso.logo.LanguageException Unable to create editor
         */
        public abstract void createEditor(
                String data)
        throws
                LanguageException;


        /**
         * This method is called when the machine is closing down. A console should
         * override this method to perform any cleanup, such as closing windows or files.
         */
        protected void exiting()
        {
        }


        /**
         * This method is called when a "goodbye" exception is thrown. A console should
         * override it to perform any necessary processing. It is called in the thread
         * that threw the exception, but other threads may still be running.
         */
        public void goodbye()
        {
        }


        /**
         * This method is called to actually run the interpreter. The exact behavior of
         * this method should depend on the application. Interactive Logo consoles, for
         * example, would include a loop of acquiring the next command and executing it,
         * until the goodbye() method is called.
         */
        public void run()
        {
        }

}



