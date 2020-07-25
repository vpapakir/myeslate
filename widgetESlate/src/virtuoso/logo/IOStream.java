/*
===============================================================================

        FILE:  IOStream.java
        
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

import java.io.*;


/**
 * Generic stream using a BufferedReader and a BufferedWriter
 */

public class IOStream
implements
        IOBase
{

        private LogoObject _name;
        private BufferedReader _reader;
        private BufferedWriter _writer;

        private static LogoObject _kind;


        static
        {
                _kind = new LogoWord("UNKNOWN");
        }


        /**
         * Constructor. Should always be followed by an open() call.
         */
        public IOStream()
        {
                _name = LogoVoid.obj;
                _reader = null;
                _writer = null;
        }


        /**
         * Create stream for reading
         *
         * @param reader stream to read
         */
        public IOStream(
                BufferedReader reader)
        {
                _name = new LogoWord();
                _reader = reader;
                _writer = null;
        }


        /**
         * Create stream for writing
         *
         * @param writer stream to write
         */
        public IOStream(
                BufferedWriter writer)
        {
                _name = new LogoWord();
                _reader = null;
                _writer = writer;
        }


        /**
         * Open stream for reading
         *
         * @param name name of stream
         * @param reader stream to read
         */
        public void open(
                LogoObject name,
                BufferedReader reader)
        {
                _name = name;
                _reader = reader;
                _writer = null;
        }


        /**
         * Open stream for writing
         *
         * @param name name of stream
         * @param writer stream to write
         */
        public void open(
                LogoObject name,
                BufferedWriter writer)
        {
                _name = name;
                _reader = null;
                _writer = writer;
        }


        /**
         * Open stream for reading and writing
         *
         * @param name name of stream
         * @param reader stream to read
         * @param writer stream to write
         */
        public void open(
                LogoObject name,
                BufferedReader reader,
                BufferedWriter writer)
        {
                _name = name;
                _reader = reader;
                _writer = writer;
        }


        /**
         * Close the stream
         *
         * @exception virtuoso.logo.LanguageException can't close
         */
        public synchronized void close()
        throws
                LanguageException
        {
                if (_writer == null && _reader == null)
                {
                        throw new LanguageException("Stream already closed");
                }
                try
                {
                        if (_writer != null)
                        {
                                _writer.close();
                        }
                        if (_reader != null)
                        {
                                _reader.close();
                        }
                }
                catch (IOException e) {}
                _writer = null;
                _reader = null;
        }


        /**
         * Is the stream open?
         *
         * @return true iff can interact with stream
         */
        public synchronized boolean isOpen()
        {
                return (_writer != null || _reader != null);
        }


        /**
         * Get the name of this object
         *
         * @return the name as a LogoObject
         */
        public synchronized LogoObject name()
        {
                return _name;
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


        /**
         * Has the stream encountered EOF?
         *
         * @return true iff eof encountered
         *
         * @exception virtuoso.logo.LanguageException stream closed
         */
        public synchronized boolean eof()
        throws
                LanguageException
        {
                if (_reader == null)
                {
                        if (_writer == null)
                        {
                                throw new LanguageException("Stream is closed");
                        }
                        else
                        {
                                throw new LanguageException("Can't read from " + _name.toString());
                        }
                }
                try
                {
                        _reader.mark(2);
                        int val = _reader.read();
                        if (val == -1) return true;
                        _reader.reset();
                }
                catch (IOException e)
                {
                        return true;
                }
                return false;
        }


        /**
         * Get a line from the stream
         *
         * @return the string
         *
         * @exception virtuoso.logo.LanguageException read not allowed, or io closed
         */
        public synchronized String getLine()
        throws
                LanguageException
        {
                if (_reader == null)
                {
                        if (_writer == null)
                        {
                                throw new LanguageException("Stream is closed");
                        }
                        else
                        {
                                throw new LanguageException("Can't read from " + _name.toString());
                        }
                }
                try
                {
                        String str = _reader.readLine();
                        if (str == null)
                        {
                                return "";
                        }
                        else
                        {
                                return str;
                        }
                }
                catch (IOException e)
                {
                        throw new LanguageException(e.toString());
                }
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
                if (_reader == null)
                {
                        if (_writer == null)
                        {
                                throw new LanguageException("Stream is closed");
                        }
                        else
                        {
                                throw new LanguageException("Can't read from " + _name.toString());
                        }
                }
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
         * Get a character from the stream
         *
         * @return char the character
         *
         * @exception virtuoso.logo.LanguageException read not allowed, or io closed
         */
        public synchronized char getChar()
        throws
                LanguageException
        {
                if (_reader == null)
                {
                        if (_writer == null)
                        {
                                throw new LanguageException("Stream is closed");
                        }
                        else
                        {
                                throw new LanguageException("Can't read from " + _name.toString());
                        }
                }
                try
                {
                        return (char)(_reader.read());
                }
                catch (EOFException e)
                {
                        return '\0';
                }
                catch (IOException e)
                {
                        throw new LanguageException(e.toString());
                }
        }


        /**
         * Character available on stream?
         *
         * @return true iff available
         *
         * @exception virtuoso.logo.LanguageException read not allowed, or io closed
         */
        public synchronized boolean charAvail()
        throws
                LanguageException
        {
                if (_reader == null)
                {
                        if (_writer == null)
                        {
                                throw new LanguageException("Stream is closed");
                        }
                        else
                        {
                                throw new LanguageException("Can't read from " + _name.toString());
                        }
                }
                try
                {
                        return _reader.ready();
                }
                catch (IOException e)
                {
                        throw new LanguageException(e.toString());
                }
        }


        /**
         * Write a string to the stream
         *
         * @param str the string
         *
         * @exception virtuoso.logo.LanguageException write not allowed, or io closed
         */
        public synchronized void put(
                String str)
        throws
                LanguageException
        {
                if (_writer == null)
                {
                        if (_reader == null)
                        {
                                throw new LanguageException("Stream is closed");
                        }
                        else
                        {
                                throw new LanguageException("Can't write to " + _name.toString());
                        }
                }
                try
                {
                        _writer.write(str);
                }
                catch (IOException e)
                {
                        throw new LanguageException(e.toString());
                }
        }


        /**
         * Write a string to the stream, terminated with a newline
         *
         * @param str the string
         *
         * @exception virtuoso.logo.LanguageException write not allowed, or io closed
         */
        public synchronized void putLine(
                String str)
        throws
                LanguageException
        {
                if (_writer == null)
                {
                        if (_reader == null)
                        {
                                throw new LanguageException("Stream is closed");
                        }
                        else
                        {
                                throw new LanguageException("Can't write to " + _name.toString());
                        }
                }
                try
                {
                        _writer.write(str);
                        _writer.newLine();
                        _writer.flush();
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
         *
         * @exception virtuoso.logo.LanguageException write not allowed, or io closed
         */
        public synchronized void put(
                char c)
        throws
                LanguageException
        {
                if (_writer == null)
                {
                        if (_reader == null)
                        {
                                throw new LanguageException("Stream is closed");
                        }
                        else
                        {
                                throw new LanguageException("Can't write to " + _name.toString());
                        }
                }
                try
                {
                        _writer.write(c);
                }
                catch (IOException e)
                {
                        throw new LanguageException(e.toString());
                }
        }


        /**
         * Write a char array to the stream
         *
         * @param buf the buffer
         * @param num number of characters
         *
         * @exception virtuoso.logo.LanguageException write not allowed, or io closed
         */
        public synchronized void put(
                char[] buf,
                int num)
        throws
                LanguageException
        {
                if (_writer == null)
                {
                        if (_reader == null)
                        {
                                throw new LanguageException("Stream is closed");
                        }
                        else
                        {
                                throw new LanguageException("Can't write to " + _name.toString());
                        }
                }
                try
                {
                        _writer.write(buf, 0, num);
                }
                catch (IOException e)
                {
                        throw new LanguageException(e.toString());
                }
        }


        /**
         * Flush the stream
         *
         * @exception virtuoso.logo.LanguageException write not allowed, or io closed
         */
        public synchronized void flush()
        throws
                LanguageException
        {
                if (_writer == null)
                {
                        if (_reader == null)
                        {
                                throw new LanguageException("Stream is closed");
                        }
                        else
                        {
                                throw new LanguageException("Can't write to " + _name.toString());
                        }
                }
                try
                {
                        _writer.flush();
                }
                catch (IOException e)
                {
                        throw new LanguageException(e.toString());
                }
        }

}



