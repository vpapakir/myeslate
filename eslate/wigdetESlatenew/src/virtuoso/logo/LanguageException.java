/*
===============================================================================

        FILE:  LanguageException.java
        
        PROJECT:
        
                Turtle Tracks
        
        CONTENTS:
        
                Miscellaneous language exception
        
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
 * General-purpose language exception. Represents an error condition. If not
 * caught, it will cause the machine to report an error to the console.
 */

public final class LanguageException
extends Exception
{
        /**
         * Serialization version.
         */
        final static long serialVersionUID = 1L;
  
        private String _prim;
        private String _proc;
        private char _cont;


        /**
         * Default constructor
         */
        public LanguageException()
        {
                super();
                _prim = null;
                _proc = null;
                _cont = '\0';
        }


        /**
         * Constructor with a string
         *
         * @param s detail string
         */
        public LanguageException(
                String s)
        {
                super(s);
                _prim = null;
                _proc = null;
                _cont = '\0';
        }

        /**
         * Constructor with an exception
         * @param   e   The exception that caused this exception.
         */
        public LanguageException(Exception e)
        {
          super(e);
          _prim = null;
          _proc = null;
          _cont = '\0';
        }

        /**
         * Constructor with a string and an exception
         * @param   s   detail string
         * @param   e   The exception that caused this exception.
         */
        public LanguageException(String s, Exception e)
        {
          super(s, e);
          _prim = null;
          _proc = null;
          _cont = '\0';
        }
        
        /**
         * Constructor with a string and a continue
         *
         * @param s detail string
         * @param cont continue prompt character (for tokenizer errors)
         */
        LanguageException(
                String s,
                char cont)
        {
                super(s);
                _prim = null;
                _proc = null;
                _cont = cont;
        }


        /**
         * Constructor with a string and a primitive name
         *
         * @param s detail string
         * @param prim which threw the exception
         */
        public LanguageException(
                String s,
                String prim)
        {
                super(s);
                _prim = prim;
                _proc = null;
                _cont = '\0';
        }


        /**
         * Constructor with a string, a primitive name, and a procedure name
         *
         * @param s detail string
         * @param primitive which threw the exception
         * @param procedure which threw the exception
         */
        LanguageException(
                String s,
                String prim,
                String proc)
        {
                super(s);
                _prim = prim;
                _proc = proc;
                _cont = '\0';
        }


        /**
         * Full constructor
         *
         * @param s detail string
         * @param primitive which threw the exception
         * @param procedure which threw the exception
         * @param cont continue prompt character (for tokenizer errors)
         */
        LanguageException(
                String s,
                String prim,
                String proc,
                char cont)
        {
                super(s);
                _prim = prim;
                _proc = proc;
                _cont = cont;
        }


        /**
         * Accessor for primitive name
         *
         * @return the primitive which threw the exception
         */
        public final String getPrimName()
        {
                return _prim;
        }


        /**
         * Accessor for procedure name
         *
         * @return the procedure which threw the exception
         */
        public final String getProcName()
        {
                return _proc;
        }


        /**
         * Accessor for continue character
         *
         * @return the continue prompt character
         */
        public final char getContChar()
        {
                return _cont;
        }


        /**
         * Generate error message
         *
         * @return a string representation of the error message
         */
        public final String generateMessage()
        {
                if (_proc != null && _prim != null)
                {
                        return getMessage() + Machine.LINE_SEPARATOR +
                                "... while executing " + _prim.toUpperCase() + Machine.LINE_SEPARATOR +
                                "... in procedure " + _proc.toUpperCase();
                }
                else if (_prim != null)
                {
                        return getMessage() + Machine.LINE_SEPARATOR +
                                "... while executing " + _prim.toUpperCase();
                }
                else if (_proc != null)
                {
                        return getMessage() + Machine.LINE_SEPARATOR +
                                "... in procedure " + _proc.toUpperCase();
                }
                else
                {
                        return getMessage();
                }
        }

}



