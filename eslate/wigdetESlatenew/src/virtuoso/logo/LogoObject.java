/*
===============================================================================

        FILE:  LogoObject.java
        
        PROJECT:
        
                Turtle Tracks
        
        CONTENTS:
        
                Standard logo object
        
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

import java.util.*;


/**
 * Abstract Logo object. LogoList, LogoWord and LogoVoid extend this class.
 */

public abstract class LogoObject
extends ParseObject
{

        /**
         * Evaluate this object in the given environment
         *
         * @param interp the environment
         *
         * @return the value
         */
        LogoObject evaluate(
                InterpEnviron interp)
        {
                return this;
        }


        /**
         * Write to a caseless string
         *
         * @return the string
         */
        public CaselessString toCaselessString()
        {
                return new CaselessString(toString());
        }


        /**
         * Unparse object
         *
         * @return the string
         */
        public abstract String unparse();


        /**
         * Returns a list that has been retokenized for running
         *
         * @param mach the machine to parse with
         * 
         * @return the parse tree
         *
         * @exception virtuoso.logo.LanguageException not a runnable list
         */
        public ParseTree getRunnable(
                Machine mach)
        throws
                LanguageException
        {
                throw new LanguageException("Runnable list expected");
        }


        /**
         * Copy the first element of the list or first character of the word
         *
         * @return an object containing first
         *
         * @exception virtuoso.logo.LanguageException object is an empty list or word
         */
        public LogoObject first()
        throws
                LanguageException
        {
                throw new LanguageException("Object expected");
        }


        /**
         * Copy the last element of the list or last character of the word
         *
         * @return an object containing last
         *
         * @exception virtuoso.logo.LanguageException object is an empty list or word
         */
        public LogoObject last()
        throws
                LanguageException
        {
                throw new LanguageException("Object expected");
        }


        /**
         * Copy all parts except first
         *
         * @return a new object containing butfirst
         *
         * @exception virtuoso.logo.LanguageException object is an empty list or word
         */
        public LogoObject butFirst()
        throws
                LanguageException
        {
                throw new LanguageException("Object expected");
        }


        /**
         * Copy all parts except last
         *
         * @return a new object containing butfirst
         *
         * @exception virtuoso.logo.LanguageException object is an empty list or word
         */
        public LogoObject butLast()
        throws
                LanguageException
        {
                throw new LanguageException("Object expected");
        }


        /**
         * Turns this word into a number
         *
         * @return the number value
         *
         * @exception virtuoso.logo.LanguageException bad number format or list given
         */
        public double toNumber()
        throws
                LanguageException
        {
                throw new LanguageException("Number expected");
        }


        /**
         * Turns this word into an integer
         *
         * @return the number value
         *
         * @exception virtuoso.logo.LanguageException bad number format or list given
         */
        public int toInteger()
        throws
                LanguageException
        {
                throw new LanguageException("Integer expected");
        }


        /**
         * Turns this word into a boolean
         *
         * @return the boolean value
         *
         * @exception virtuoso.logo.LanguageException bad boolean format or list given
         */
        public boolean toBoolean()
        throws
                LanguageException
        {
                throw new LanguageException("Boolean expression expected");
        }


        /**
         * Returns length of the object or word
         *
         * @return the length
         */
        public abstract int length();


        /**
         * Pick the index'th member of the list
         *
         * @return the member
         *
         * @exception virtuoso.logo.LanguageException index out of bounds or empty object
         */
        public LogoObject pick(
                int index)
        throws
                LanguageException
        {
                throw new LanguageException("Object expected");
        }


        /**
         * Enumerates object as caseless strings
         *
         * @return the enumerator
         */
        @SuppressWarnings("unchecked")
        Enumeration enumerateCaselessStrings()
        {
                return new LOCSEnumerator();
        }


        /**
         * LogoObject to CaselessString enumerator
         */

        @SuppressWarnings("unchecked")
        class LOCSEnumerator
        implements
                Enumeration
        {

                private int _index;


                /**
                 * Constructor
                 */
                LOCSEnumerator()
                {
                        if (LogoObject.this instanceof LogoWord)
                        {
                                _index = -1;
                        }
                        else if (LogoObject.this instanceof LogoList)
                        {
                                _index = 0;
                        }
                        else
                        {
                                _index = -2;
                        }
                }


                /**
                 * Does it have more elements?
                 */
            public boolean hasMoreElements()
            {
                        if (_index == -1)
                        {
                                return true;
                        }
                        else if (_index == -2)
                        {
                                return false;
                        }
                        else
                        {
                                return length() > _index;
                        }
            }


                /**
                 * Get next element
                 *
                 * @exception virtuoso.logo.NoSuchElementException no more elements
                 */
            public Object nextElement()
            throws
                NoSuchElementException
            {
                if (_index == -1)
                {
                        _index = -2;
                        return toCaselessString();
                }
                else if (_index >= 0 && _index < length())
                {
                        _index++;
                        return ((LogoList)(LogoObject.this)).pickInPlace(_index-1).toCaselessString();
                }
                else
                {
                                throw new NoSuchElementException("LOCSEnumerator");
                        }
            }

        }

}



