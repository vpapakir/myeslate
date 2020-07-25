/*
===============================================================================

        FILE:  Parser.java
        
        PROJECT:
        
                Turtle Tracks
        
        CONTENTS:
        
                Parser object
        
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
 * Parser. Generates a ParseTree from a LogoList.
 */
@SuppressWarnings(value={"unchecked"})
final class Parser
{

        private static String STRING_OPEN_PAREN = "(";
        private static String STRING_CLOSE_PAREN = ")";
        
        private int _index;
        private LogoList _list;
        private Machine _mach;
        private boolean _paren;
        
        private LogoObject _next;
        private LogoWord _nextw;
        private LogoList _nextl;


        /**
         * Parse the given list (interface)
         *
         * @param l The list to interpret
         * @param mach the machine to interpret in
         *
         * @exception virtuoso.logo.LanguageException language error
         */
        ParseTree parse(
                LogoList ll,
                Machine mach)
        throws
                LanguageException
        {
                _mach = mach;
                _list = new Tokenizer(0).tokenizeRunnable(ll);
                _index = 0;
                nextWord();
                
                if (_next == LogoVoid.obj)
                {
                        return new ParseTree(-1, new ParseObject[0]);
                }

    CaselessString s=_next.toCaselessString(); //Birb:30/8/1998
                /*else*/ if //Birb
     (s.equals(ParseSpecial.strTO) || s.equals(ParseSpecial.strLocalizedTO)) //Birb:30/8/1998
     //Birb// (_next.toCaselessString().equals(ParseSpecial.strTO))
                {
                        ParseObject[] a = new ParseObject[1];
                        a[0] = new ParseSpecial(ParseSpecial.TO, (LogoList)(_list.butFirst()));
                        return new ParseTree(-1, a);
                }
                else if
     (s.equals(ParseSpecial.strTOMACRO) || s.equals(ParseSpecial.strLocalizedTOMACRO)) //Birb:30/8/1998
     //Birb// (_next.toCaselessString().equals(ParseSpecial.strTOMACRO))
                {
                        ParseObject[] a = new ParseObject[1];
                        a[0] = new ParseSpecial(ParseSpecial.TOMACRO, (LogoList)(_list.butFirst()));
                        return new ParseTree(-1, a);
                }
                
                else
                {
                        Vector vec = new Vector();
                        int clock;
                        synchronized(_mach)
                        {
                                clock = _mach.getClock();
                                while (_next != LogoVoid.obj)
                                {
                                        vec.addElement(parseD());
                                }
                        }
                        return new ParseTree(clock, vec);
                }
        }


        /**
         * Utility for creating a primitive node with two parameters
         * Only for use with infix operators (TYPE_PUNCT)
         */
        final private ParseObject createPrimitive(
                String n,
                ParseObject param1,
                ParseObject param2)
        throws
                LanguageException
        {
                PrimitiveSpec prim = _mach.findPrimitive(new CaselessString(n));
                if (prim != null)
                {
                        ParseObject[] params = new ParseObject[2];
                        params[0] = param1;
                        params[1] = param2;
                        return new ParsePrimitive(prim, n, params);
                }
                else
                {
                        throw new LanguageException("Fatal error: no primitive for " + n);
                }
        }


        /**
         * Part of the parser
         */
        final private ParseObject parseC()
        throws
                LanguageException
        {
                if (_nextw != null)
                {
                        if (_nextw.getType() == LogoWord.TYPE_WORD)
                        {
                                PrimitiveSpec prim = null;
                                Procedure proc = null;
                                int max;
                                CaselessString name = _nextw.toCaselessString();
                                
                                proc = _mach.resolveProc(name);
                                if (proc != null)
                                {
                                        max = proc.numArgs();
                                }
                                else
                                {
                                        prim = _mach.findPrimitive(name);
                                        if (prim != null)
                                        {
                                                max = prim.numArgs();
                                        }
                                        else
                                        {
                                                throw new LanguageException("I don't know how to " + name);
                                        }
                                }
                                
                                nextWord();
                                ParseObject[] params;
                                int i;
                                if (_paren)
                                {
                                        Vector v = new Vector();
                                        while (true)
                                        {
                                                if (_nextw != null)
                                                {
                                                        if (_nextw.getType() == LogoWord.TYPE_PUNCT &&
                                                                !_nextw.toString().equals(STRING_OPEN_PAREN))
                                                        {
                                                                break;
                                                        }
                                                }
                                                v.addElement(parseD());
                                        }
                                        params = new ParseObject[v.size()];
                                        for (i=0; i<v.size(); i++)
                                        {
                                                params[i] = (ParseObject)(v.elementAt(i));
                                        }
                                }
                                else
                                {
                                        params = new ParseObject[max];
                                        for (i=0; i<max; i++)
                                        {
                                                params[i] = parseD();
                                        }
                                }
                                
                                if (proc != null)
                                {
                                        return new ParseProcedure(proc, params);
                                }
                                else
                                {
                                        return new ParsePrimitive(prim, name.str, params);
                                }
                        }
                }
                
                return parseD();
        }


        /**
         * Part of the parser
         */
        final private ParseObject parseD()
        throws
                LanguageException
        {
                ParseObject ret;
                boolean oldParen = _paren;
                _paren = false;
                try
                {
                        ret = parseDp(parseE());
                }
                finally
                {
                        _paren = oldParen;
                }
                return ret;
        }


        /**
         * Part of the parser
         */
        final private ParseObject parseDParen()
        throws
                LanguageException
        {
                ParseObject ret;
                boolean oldParen = _paren;
                _paren = true;
                try
                {
                        ret = parseDp(parseE());
                }
                finally
                {
                        _paren = oldParen;
                }
                return ret;
        }


        /**
         * Part of the parser
         */
        final private ParseObject parseDp(
                ParseObject prevObj)
        throws
                LanguageException
        {
                if (_nextw != null)
                {
                        if (_nextw.getType() == LogoWord.TYPE_PUNCT)
                        {
                                String cmd = _nextw.toString();
                                if (cmd.equals("=") || cmd.equals(">") || cmd.equals("<") ||
                                        cmd.equals(">=") || cmd.equals("<="))
                                {
                                        nextWord();
                                        return parseDp(createPrimitive(cmd, prevObj, parseE()));
                                }
                        }
                }
                
                return prevObj;
        }


        /**
         * Part of the parser
         */
        final private ParseObject parseE()
        throws
                LanguageException
        {
                return parseEp(parseT());
        }


        /**
         * Part of the parser
         */
        final private ParseObject parseEp(
                ParseObject prevObj)
        throws
                LanguageException
        {
                if (_nextw != null)
                {
                        if (_nextw.getType() == LogoWord.TYPE_PUNCT)
                        {
                                String cmd = _nextw.toString();
                                if (cmd.equals("+") || cmd.equals("-"))
                                {
                                        nextWord();
                                        return parseEp(createPrimitive(cmd, prevObj, parseT()));
                                }
                        }
                }
                
                return prevObj;
        }


        /**
         * Part of the parser
         */
        final private ParseObject parseT()
        throws
                LanguageException
        {
                return parseTp(parseF());
        }


        /**
         * Part of the parser
         */
        final private ParseObject parseTp(
                ParseObject prevObj)
        throws
                LanguageException
        {
                if (_nextw != null)
                {
                        if (_nextw.getType() == LogoWord.TYPE_PUNCT)
                        {
                                String cmd = _nextw.toString();
                                if (cmd.equals("*") || cmd.equals("/"))
                                {
                                        nextWord();
                                        return parseTp(createPrimitive(cmd, prevObj, parseF()));
                                }
                        }
                }
                
                return prevObj;
        }


        /**
         * Part of the parser
         */
        final private ParseObject parseF()
        throws
                LanguageException
        {
                ParseObject ret = LogoVoid.obj;
                
                if (_nextw != null)
                {
                        // Punctuation (open paren)
                        if (_nextw.getType() == LogoWord.TYPE_PUNCT)
                        {
                                String cmd = _nextw.toString();
                                if (cmd.equals(STRING_OPEN_PAREN))
                                {
                                        nextWord();
                                        ret = parseDParen();
                                        if (_nextw == null)
                                        {
                                                throw new LanguageException("Missing closing paren", '(');
                                        }
                                        if (!_nextw.toString().equals(STRING_CLOSE_PAREN))
                                        {
                                                throw new LanguageException("Closing paren expected");
                                        }
                                        nextWord();
                                        return ret;
                                }
                                else if (cmd.equals(STRING_CLOSE_PAREN))
                                {
                                        throw new LanguageException("Unexpected closing paren");
                                }
                                else if (!(cmd.equals("-")))
                                {
                                        throw new LanguageException("Not enough inputs to " + _nextw);
                                }
                        }
                        
                        // Numeric literal
                        if (_nextw.getType() == LogoWord.TYPE_FLOAT ||
                                _nextw.getType() == LogoWord.TYPE_INT)
                        {
                                ret = _next;
                                nextWord();
                                return ret;
                        }
                        
                        // Prefix function
                        String str = _nextw.toString();
                        if (str.length() > 0)
                        {
                                if (str.equals("-"))
                                {
                                        nextWord();
                                        PrimitiveSpec prim = _mach.findPrimitive(new CaselessString("MINUS"));
                                        if (prim == null)
                                        {
                                                throw new LanguageException("Fatal error: no primitive for operator -");
                                        }
                                        ParseObject[] params = new ParseObject[1];
                                        params[0] = parseF();
                                        return new ParsePrimitive(prim, "-", params);
                                }
                                else if (str.charAt(0) == '\"')
                                {
                                        ret = _next.butFirst();
                                        nextWord();
                                        return ret;
                                }
                                else if (str.charAt(0) == ':')
                                {
                                        PrimitiveSpec prim = _mach.findPrimitive(new CaselessString("THING"));
                                        if (prim == null)
                                        {
                                                throw new LanguageException("Fatal error: no primitive for operator :");
                                        }
                                        ParseObject[] params = new ParseObject[1];
                                        params[0] = _next.butFirst();
                                        ret = new ParsePrimitive(prim, null, params);
                                        nextWord();
                                        return ret;
                                }
                                else
                                {
                                        return parseC();
                                }
                        }
                        
                        // Problem
                        throw new LanguageException("Empty word in list");
                }
                
                // List literal
                else if (_nextl != null)
                {
                        ret = _next;
                        nextWord();
                        return ret;
                }
                
                // End of list
                else
                {
                        throw new LanguageException("Not enough inputs");
                }
        }


        /**
         * Get next word in the list
         */
        final private void nextWord()
        {
                if (_index >= _list.length())
                {
                        _next = LogoVoid.obj;
                        _nextw = null;
                        _nextl = null;
                }
                else
                {
                        _next = _list.pickInPlace(_index++);
                        if (_next instanceof LogoWord)
                        {
                                _nextw = (LogoWord)_next;
                                _nextl = null;
                        }
                        else
                        {
                                _nextw = null;
                                _nextl = (LogoList)_next;
                        }
                }
        }

}



