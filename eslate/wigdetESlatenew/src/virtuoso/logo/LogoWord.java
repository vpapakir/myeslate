/*
===============================================================================

        FILE:  LogoWord.java
        
        PROJECT:
        
                Turtle Tracks
        
        CONTENTS:
        
                Word logo object
        
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
 * Word object
 */

public class LogoWord
extends LogoObject
{

        private String _str;
        private String _unparsed;
        private byte _type;
        private double _val;
        
        private static final String _true = "TRUE";
        private static final String _false = "FALSE";
        
        static final byte TYPE_WORD = 1;
        static final byte TYPE_PUNCT = 2;
        static final byte TYPE_INT = 3;
        static final byte TYPE_FLOAT = 4;


        /**
         * Construct an empty word
         */
        public LogoWord()
        {
                _str = "";
                _unparsed = "";
                _type = TYPE_WORD;
                _val = 0;
        }


        /**
         * Construct a word given complete info (used by clone)
         */
        private LogoWord(
                String s,
                String u,
                byte t,
                double v)
        {
                _str = s;
                _unparsed = u;
                _type = t;
                _val = v;
        }


        /**
         * Construct a word with a given string and unparsed string info
         *
         * @param s the string to represent
         * @param u the unparsed representation
         */
        public LogoWord(
                String s,
                String u)
        {
                _str = s;
                _unparsed = u;
                
                // Hack: Netscape wants to interpret the empty string as 0
                if (_str.length() == 0)
                {
                        _type = TYPE_WORD;
                }
                else
                {
                        try
                        {
                                _val = Double.valueOf(_str).doubleValue();
                                int intval = (int)_val;
                                if (intval == _val)
                                {
                                        _str = String.valueOf(intval);
                                        _type = TYPE_INT;
                                }
                                else
                                {
                                        _type = TYPE_FLOAT;
                                }
                        }
                        catch (NumberFormatException e)
                        {
                                _type = TYPE_WORD;
                        }
                }
        }


        /**
         * Construct a word with a given string
         *
         * @param s the string to represent
         */
        public LogoWord(
                String s)
        {
                this(s, null);
        }


        /**
         * Construct a word with a given caseless string and unparsed string info
         *
         * @param s the string to represent
         * @param u the unparsed representation
         */
        public LogoWord(
                CaselessString s,
                String u)
        {
                this(s.str, u);
        }


        /**
         * Construct a word with a given caseless string
         *
         * @param s the string to represent
         */
        public LogoWord(
                CaselessString s)
        {
                this(s.str, null);
        }


        /**
         * Construct a word with a boolean
         *
         * @param b the boolean to represent
         */
        public LogoWord(
                boolean b)
        {
                if (b)
                {
                        _unparsed = _true;
                        _str = _true;
                }
                else
                {
                        _unparsed = _false;
                        _str = _false;
                }
                _type = TYPE_WORD;
        }


        /**
         * Construct a word with a number
         *
         * @param n the double to represent
         */
        public LogoWord(
                double n)
        {
                _val = n;
                int intval = (int)_val;
                if (intval == _val)
                {
                        _unparsed = String.valueOf(intval);
                        _str = _unparsed;
                        _type = TYPE_INT;
                }
                else
                {
                        _unparsed = String.valueOf(_val);
                        _str = _unparsed;
                        _type = TYPE_FLOAT;
                }
        }


        /**
         * Construct a word with an integer
         *
         * @param n the integer to represent
         */
        public LogoWord(
                int n)
        {
                _val = (double)n;
                _unparsed = String.valueOf(n);
                _str = _unparsed;
                _type = TYPE_INT;
        }


        /**
         * Construct a word given a punctuation character (used by Tokenizer)
         */
        LogoWord(
                char c)
        {
                char[] ca = new char[1];
                ca[0] = c;
                _str = new String(ca);
                _unparsed = _str;
                _type = TYPE_PUNCT;
        }


        /**
         * Construct a word given two punctuation characters (used by Tokenizer)
         */
        LogoWord(
                char c1,
                char c2)
        {
                char[] ca = new char[2];
                ca[0] = c1;
                ca[1] = c2;
                _str = new String(ca);
                _unparsed = _str;
                _type = TYPE_PUNCT;
        }


        /**
         * Clone the object
         *
         * @return a clone of this object
         */
        public Object clone()
        {
                return new LogoWord(_str, _unparsed, _type, _val);
        }


        /**
         * Determine if another object is equal to this one
         *
         * @param obj what to compare with
         *
         * @return true iff equal
         */
        public boolean equals(
                Object obj)
        {
                if (obj instanceof LogoWord)
                {
                        return CaselessString.staticEquals(_str, obj.toString());
                }
                else
                {
                        return false;
                }
        }


        /**
         * Write to a string
         *
         * @return the string
         */
        public String toString()
        {
                return _str;
        }


        /**
         * Unparse word
         *
         * @return the string
         */
        public String unparse()
        {
                if (_unparsed == null)
                {
                        _unparsed = CaselessString.staticUnparse(_str);
                }
                return _unparsed;
        }


        /**
         * Get the type code
         *
         * @return the type
         */
        byte getType()
        {
                return _type;
        }


        /**
         * Copy the first character of the word
         *
         * @return a new object containing first
         *
         * @exception virtuoso.logo.LanguageException object is an empty word
         */
        public LogoObject first()
        throws
                LanguageException
        {
                try
                {
                        return new LogoWord(_str.substring(0, 1));
                }
                catch (StringIndexOutOfBoundsException e)
                {
                        throw new LanguageException("Empty word");
                }
        }


        /**
         * Copy the last character of the word
         *
         * @return a new object containing last
         *
         * @exception virtuoso.logo.LanguageException object is an empty word
         */
        public LogoObject last()
        throws
                LanguageException
        {
                try
                {
                        return new LogoWord(_str.substring(_str.length()-1));
                }
                catch (StringIndexOutOfBoundsException e)
                {
                        throw new LanguageException("Empty word");
                }
        }


        /**
         * Copy all parts except first
         *
         * @return a new object containing butfirst
         *
         * @exception virtuoso.logo.LanguageException object is an empty word
         */
        public LogoObject butFirst()
        throws
                LanguageException
        {
                try
                {
                        return new LogoWord(_str.substring(1));
                }
                catch (StringIndexOutOfBoundsException e)
                {
                        throw new LanguageException("Empty word");
                }
        }


        /**
         * Copy all parts except last
         *
         * @return a new object containing butfirst
         *
         * @exception virtuoso.logo.LanguageException object is an empty word
         */
        public LogoObject butLast()
        throws
                LanguageException
        {
                try
                {
                        return new LogoWord(_str.substring(0, _str.length()-1));
                }
                catch (StringIndexOutOfBoundsException e)
                {
                        throw new LanguageException("Empty word");
                }
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
                if (_type == TYPE_INT || _type == TYPE_FLOAT)
                {
                        return _val;
                }
                else
                {
                        throw new LanguageException("Number expected");
                }
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
                if (_type == TYPE_INT)
                {
                        return (int)_val;
                }
                else
                {
                        throw new LanguageException("Integer expected");
                }
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
                if (CaselessString.staticEquals(_str, _true))
                {
                        return true;
                }
                else if (CaselessString.staticEquals(_str, _false))
                {
                        return false;
                }
                else
                {
                        throw new LanguageException("Boolean expression expected");
                }
        }


        /**
         * Returns length of the word
         *
         * @return the length
         */
        public int length()
        {
                return _str.length();
        }


        /**
         * Pick the index'th character of the word (1-based)
         *
         * @return a copy of the member
         *
         * @exception virtuoso.logo.LanguageException index out of bounds or empty object
         */
        public LogoObject pick(
                int index)
        throws
                LanguageException
        {
                if (_str.length() == 0)
                {
                        throw new LanguageException("Empty word");
                }
                try
                {
                        return new LogoWord(_str.substring(index-1, index));
                }
                catch (StringIndexOutOfBoundsException e)
                {
                        throw new LanguageException("Index out of bounds");
                }
        }

}



