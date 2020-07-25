/*
===============================================================================

        FILE:  Procedure.java
        
        PROJECT:
        
                Turtle Tracks
        
        CONTENTS:
        
                Logo procedure
        
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
 * Procedure object
 */

public final class Procedure
implements
        Cloneable
{

        private Machine _mach;
        private CaselessString _name;
        private String[] _codeStrs;
        private LogoList _params;
        private LogoList _code;
        private boolean _isMacro;


        /**
         * Construct a procedure with given strings (used by clone)
         */
        private Procedure(
                Machine mach,
                CaselessString n,
                LogoList p,
                String[] cs,
                boolean isMacro)
        {
                _mach = mach;
                _name = n;
                _params = p;
                _code = null;
                _codeStrs = cs;
                _isMacro = isMacro;
        }


        /**
         * Construct a procedure with given strings (used by TO)
         *
         * @param mach the machine in use
         * @param n name of procedure
         * @param p param list
         * @param v Vector of strings specifying the code
         * @param isMacro is the procedure a macro
         */
        @SuppressWarnings("unchecked")
        public Procedure(
                Machine mach,
                CaselessString n,
                LogoList p,
                Vector v,
                boolean isMacro)
        {
                _mach = mach;
                _name = n;
                _params = p;
                _code = null;
                _codeStrs = new String[v.size()];
                for (int i=0; i<_codeStrs.length; i++)
                {
                        _codeStrs[i] = (String)(v.elementAt(i));
                }
                _isMacro = isMacro;
        }


        /**
         * Generate a procedure given lists (used by DEFINE)
         *
         * @param mach the machine in use
         * @param n name of procedure
         * @param p param list
         * @param c code list
         * @param isMacro is the procedure a macro
         */
        public Procedure(
                Machine mach,
                CaselessString n,
                LogoList p,
                LogoList c,
                boolean isMacro)
        {
                _mach = mach;
                _params = p;
                _code = c;
                _name = n;
                _codeStrs = new String[1];
                _codeStrs[0] = _code.toStringOpen();
                _isMacro = isMacro;
        }


        /**
         * Clone the object
         *
         * @return a clone of this object
         */
        public Object clone()
        {
                return new Procedure(_mach, _name, _params, _codeStrs, _isMacro);
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
                if (obj instanceof Procedure)
                {
                        return ((Procedure)obj)._name.equals(_name);
                }
                else if (obj instanceof CaselessString || obj instanceof String)
                {
                        return (_name.equals(obj));
                }
                else
                {
                        return false;
                }
        }


        /**
         * Sanity check parameter list
         *
         * @param p param list
         *
         * @exception virtuoso.logo.LanguageException bad symbol name
         */
        public static void checkParamNames(
                LogoList p)
        throws
                LanguageException
        {
                LogoObject obj;
                int i;
                int j;
                
                for (i=0; i<p.length(); i++)
                {
                        obj = p.pickInPlace(i);
                        
                        if (!(obj instanceof LogoWord))
                        {
                                throw new LanguageException("Parameter symbol names must be words");
                        }
                        for (j=i+1; j<p.length(); j++)
                        {
                                if (obj.equals(p.pickInPlace(j)))
                                {
                                        throw new LanguageException("Duplicate parameter symbol name: " + obj);
                                }
                        }
                }
        }


        /**
         * Gets the procedure name
         *
         * @return the name
         */
        public final CaselessString getName()
        {
                return _name;
        }


        /**
         * Get parameter list
         *
         * @return the list of parameter names
         */
        public final LogoList getParams()
        {
                return _params;
        }


        /**
         * Get header string
         *
         * @return the header in the form "TO foo :param..."
         */
        public final String getHeaderString()
        {
                StringBuffer sb;
                if (_isMacro)
                {
                        sb = new StringBuffer("TOMACRO ");
                }
                else
                {
                        sb = new StringBuffer("TO ");
                }
                StringTokenizer tok = new StringTokenizer(_params.toStringOpen());
                
                sb.append(_name);
                
                while (tok.hasMoreTokens())
                {
                        sb.append(' ').append(':').append(tok.nextToken());
                }

                return sb.toString();
        }


  public final String getLocalizedHeaderString() //Birb:11-9-1998 //Birb-28Jun2000: renamed to "getLocalizedHeaderString" from "getGreekHeaderString"
  {
   StringBuffer stringbuffer;
   if(_isMacro)
    stringbuffer = new StringBuffer(ParseSpecial.strLocalizedTOMACRO+" ");
   else
    stringbuffer = new StringBuffer(ParseSpecial.strLocalizedTO+" ");
   StringTokenizer stringtokenizer = new StringTokenizer(_params.toStringOpen());
   stringbuffer.append(_name);
   for(; stringtokenizer.hasMoreTokens(); stringbuffer.append(' ').append(':').append(stringtokenizer.nextToken()));
   return stringbuffer.toString();
  }


        /**
         * Convert the procedure into a string
         *
         * @return the string
         */
        public String toString()
        {
                StringBuffer sb = new StringBuffer(getHeaderString());
                for (int i=0; i<_codeStrs.length; i++)
                {
                        sb.append(Machine.LINE_SEPARATOR).append(_codeStrs[i]);
                }
                return sb.append(Machine.LINE_SEPARATOR).append("END").append(Machine.LINE_SEPARATOR).toString();
        }


  public String toLocalizedString() //Birb:11-9-1998 //Birb-28Jun2000: renamed to "toLocalizedString" from "toGreekString"
  {
   StringBuffer stringbuffer = new StringBuffer(getLocalizedHeaderString());
   for(int i = 0; i < _codeStrs.length; i++)
    stringbuffer.append(Machine.LINE_SEPARATOR).append(_codeStrs[i]);

   return stringbuffer.append(Machine.LINE_SEPARATOR).append(ParseSpecial.strLocalizedEND).append(Machine.LINE_SEPARATOR).toString();
  }


        /**
         * Write the procedure to the specified IOBase.
         *
         * @param stream the stream to write to
         *
         * @exception virtuoso.logo.LanguageException i/o error
         */
        public final void writeToIO(
                IOBase stream)
        throws
                LanguageException
        {
                stream.putLine(getHeaderString());
                for (int i=0; i<_codeStrs.length; i++)
                {
                        stream.putLine(_codeStrs[i]);
                }
                stream.putLine(ParseSpecial.strEND);
        }


        /**
         * Get code list
         *
         * @return a list containing the code
         *
         * @exception virtuoso.logo.LanguageException unable to tokenize
         */
        public final synchronized LogoList getCode()
        throws
                LanguageException
        {
                if (_code == null)
                {
                        StringBuffer sb = new StringBuffer();
                        int i;
                        for (i=0; i<_codeStrs.length; i++)
                        {
                                sb.append(_codeStrs[i]).append(Machine.LINE_SEPARATOR);
                        }
                        _code = new Tokenizer(_mach.getTokenizerCommentFlags()).tokenize(sb.toString());
                }
                return _code;
        }


        /**
         * Get default number of arguments
         *
         * @return number of arguments
         */
        public final int numArgs()
        {
                return _params.length();
        }


        /**
         * Is this a macro?
         *
         * @return isMacro
         */
        public final boolean isMacro()
        {
                return _isMacro;
        }


        /**
         * Check procedure name, make sure it is okay
         *
         * @param name name to check
         *
         * @exception virtuoso.logo.LanguageException bad name
         */
        public final static void checkName(
                String name)
        throws
                LanguageException
        {
                if (name.length() == 0)
                {
                        throw new LanguageException("Bad name: empty string");
                }
                try
                {
                        /*double val = */Double.valueOf(name).doubleValue();
                        throw new LanguageException("Bad name: \"" + name +
                                "\" looks like a number.");
                }
                catch (NumberFormatException e) {}
        }

}



