/*
===============================================================================

        FILE:  IORandomFile.java
        
        PROJECT:
        
                Turtle Tracks
        
        CONTENTS:
        
                Random-access file object
        
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
 * Stream that reads and writes to a file
 */

public class IORandomFile
implements
        IORandom
{

        private static LogoObject _kind;

        private LogoObject _name;
        private RandomAccessFile _file;


        static
        {
                _kind = new LogoWord("RANDOMFILE");
        }


        /**
         * Constructor
         *
         * @param f file to open
         *
         * @exception virtuoso.logo.LanguageException unable to open file
         */
        public IORandomFile(
                File f)
        throws
                LanguageException
        {
                try
                {
                        _name = new LogoWord(f.getPath());
                        _file = new RandomAccessFile(f, "rw");
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
         * Close the stream
         *
         * @exception virtuoso.logo.LanguageException can't close
         */
        public synchronized void close()
        throws
                LanguageException
        {
                if (_file == null)
                {
                        throw new LanguageException("Stream already closed");
                }
                try
                {
                        _file.close();
                        _file = null;
                }
                catch (IOException e) {}
        }


        /**
         * Is the stream open?
         *
         * @return true iff can interact with stream
         */
        public synchronized boolean isOpen()
        {
                return (_file != null);
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
                if (_file == null)
                {
                        throw new LanguageException("Stream is closed");
                }
                try
                {
                        return _file.getFilePointer() >= _file.length();
                }
                catch (IOException e)
                {
                        throw new LanguageException(e.toString());
                }
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
                if (_file == null)
                {
                        throw new LanguageException("Stream is closed");
                }
                try
                {
                        String str = _file.readLine();
                        if (str == null)
                        {
                                str = "";
                        }
                        int len = str.length();
                        if (len > 0 && str.charAt(len-1) == '\r')
                        {
                                str = str.substring(0, len-1);
                        }
                        else if (len > 0 && str.charAt(len-1) == '\n')
                        {
                                if (len > 1 && str.charAt(len-2) == '\r')
                                {
                                        str = str.substring(0, len-2);
                                }
                                else
                                {
                                        str = str.substring(0, len-1);
                                }
                        }
                        return str;
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
                if (_file == null)
                {
                        throw new LanguageException("Stream is closed");
                }
                try
                {
                        byte[] inbuf = new byte[buf.length];
                        int len = _file.read(inbuf);
                        for (int i = 0; i<len; i++)
                        {
                                buf[i] = (char)(inbuf[i] & 0xff);
                        }
                        return len;
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
                if (_file == null)
                {
                        throw new LanguageException("Stream is closed");
                }
                try
                {
                        int c = _file.read();
                        return (c == -1) ? '\0' : (char)c;
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
                if (_file == null)
                {
                        throw new LanguageException("Stream is closed");
                }
                try
                {
                        return _file.getFilePointer() < _file.length();
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
                if (_file == null)
                {
                        throw new LanguageException("Stream is closed");
                }
                try
                {
                        _file.writeBytes(str);
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
        public synchronized void putLine(
                String str)
        throws
                LanguageException
        {
                if (_file == null)
                {
                        throw new LanguageException("Stream is closed");
                }
                try
                {
                        _file.writeBytes(str);
                        _file.writeBytes(Machine.LINE_SEPARATOR);
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
                if (_file == null)
                {
                        throw new LanguageException("Stream is closed");
                }
                try
                {
                        _file.writeByte((byte)c);
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
                if (_file == null)
                {
                        throw new LanguageException("Stream is closed");
                }
                try
                {
                        _file.writeBytes(new String(buf, 0, num));
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
                if (_file == null)
                {
                        throw new LanguageException("Stream is closed");
                }
        }


        /**
         * Seek to a location in the file
         *
         * @param pos position in the stream
         *
         * @exception virtuoso.logo.LanguageException write not allowed, or io closed
         */
        public synchronized void seek(
                long pos)
        throws
                LanguageException
        {
                if (_file == null)
                {
                        throw new LanguageException("Stream is closed");
                }
                try
                {
                        _file.seek(pos);
                }
                catch (IOException e)
                {
                        throw new LanguageException(e.toString());
                }
        }


        /**
         * Tell current location in the file
         *
         * @return position in the stream
         *
         * @exception virtuoso.logo.LanguageException write not allowed, or io closed
         */
        public synchronized long tell()
        throws
                LanguageException
        {
                if (_file == null)
                {
                        throw new LanguageException("Stream is closed");
                }
                try
                {
                        return _file.getFilePointer();
                }
                catch (IOException e)
                {
                        throw new LanguageException(e.toString());
                }
        }


        /**
         * Get length of file
         *
         * @return length
         *
         * @exception virtuoso.logo.LanguageException write not allowed, or io closed
         */
        public synchronized long length()
        throws
                LanguageException
        {
                if (_file == null)
                {
                        throw new LanguageException("Stream is closed");
                }
                try
                {
                        return _file.length();
                }
                catch (IOException e)
                {
                        throw new LanguageException(e.toString());
                }
        }

}



