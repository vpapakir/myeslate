/*
===============================================================================

        FILE:  ParseSpecial.java
        
        PROJECT:
        
                Turtle Tracks
        
        CONTENTS:
        
                Special node in a parse tree
        
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
 * Special node in a parse tree
 */
@SuppressWarnings(value={"unchecked"})
class ParseSpecial
extends ParseObject
{

        final static int TO = 0;
        final static int TOMACRO = 1;

        final static String strEND = "END";
        final static String strTO = "TO";
        final static String strTOMACRO = "TOMACRO";
        final static String strLocalizedTO = MyMachine.localize("TO"); //28Jun2000
        final static String strLocalizedTOMACRO = MyMachine.localize("TOMACRO"); //28Jun2000
        final static String strLocalizedEND = MyMachine.localize("END"); //28Jun2000

        //final private static LogoWord _objEND = new LogoWord(strEND);
        private int _code;
        private LogoList _args;


        /**
         * Construct the ParsePrimitive
         *
         * @param code either TO or TOMACRO
         * @param args the arguments to TO.
         */
        ParseSpecial(
                int code,
                LogoList args)
        {
                _code = code;
                _args = args;
        }


        /**
         * Clone the object
         *
         * @return a clone of this object
         */
        public Object clone()
        {
                return new ParseSpecial(_code, _args);
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
                return false;
        }


        /**
         * Evaluate this object in the given environment
         *
         * @param interp the environment
         *
         * @return the return value (void)
         *
         * @exception virtuoso.logo.LanguageException error thrown
         */
        LogoObject evaluate(
                InterpEnviron interp)
        throws
                LanguageException
        {
                if (_code == TO)
                {
                        pTO(interp, false);
                }
                else if (_code == TOMACRO)
                {
                        pTO(interp, true);
                }
                else
                {
                        throw new LanguageException("Internal error: unrecognized special form " +
                                String.valueOf(_code));
                }
                return LogoVoid.obj;
        }


        /**
         * Primitive TO
         *
         * @param interp the environment
         * @param isMacro true if user used TOMACRO
         *
         * @exception virtuoso.logo.LanguageException error thrown
         */
        private void pTO(
                InterpEnviron interp,
                boolean isMacro)
        throws
                LanguageException
        {
    boolean redefinedFlag=false; //Birb-20Jul1999
                Thread.yield();

                LogoObject obj = _args.pickInPlace(0);

                // Parse out and check procedure name
                if (!(obj instanceof LogoWord))
                {
                        throw new LanguageException("Procedure name expected");
                }
                LogoWord nameWord = (LogoWord)obj;
                if (nameWord.getType() == LogoWord.TYPE_PUNCT ||
                        nameWord.getType() == LogoWord.TYPE_INT ||
                        nameWord.getType() == LogoWord.TYPE_FLOAT)
                {
                        throw new LanguageException("Procedure name expected");
                }
                String checkName = nameWord.unparse();
                if (checkName.length() > 0)
                {
                        if (checkName.charAt(0) == '\"')
                        {
                                throw new LanguageException("Procedure name should not be quoted");
                        }
                        if (checkName.charAt(0) == ':')
                        {
                                throw new LanguageException("Procedure name should not be preceded by a colon");
                        }
                }
                CaselessString name = nameWord.toCaselessString();
                Procedure.checkName(name.str);
                if (!interp.mach().isOverridePrimitives() && interp.mach().isReserved(name))
                {
                        throw new LanguageException("The name " + name.str + " is a primitive.");
                }
                if (!interp.thread().isLoading())
                {
                        if (interp.mach().resolveProc(name) != null)
                        {
       redefinedFlag=true;//Birb-20Jul1999: now redefinition of a proc is allowed//
       //Birb-20Jul1999// throw new LanguageException("The procedure " + name.str + " is already defined.");
                }
                }
                
                // Parse out and check formal parameter names
                Vector vec = new Vector();
                int i;
                for (i=1; i<_args.length(); i++)
                {
                        obj = _args.pickInPlace(i);
                        if (!(obj instanceof LogoWord))
                        {
                                throw new LanguageException("Parameter name expected but list found.");
                        }
                        checkName = obj.toString();
                        if (checkName.length() == 0)
                        {
                                throw new LanguageException("Parameter name must be preceded by a colon.");
                        }
                        if (checkName.charAt(0) != ':')
                        {
                                throw new LanguageException("Parameter name must be preceded by a colon");
                        }
                        vec.addElement(new LogoWord(checkName.substring(1)));
                }
                LogoList params = new LogoList(vec);
                Procedure.checkParamNames(params);
                
                // Ready to read
                Tokenizer tokenizer = new Tokenizer(interp.mach().getTokenizerCommentFlags());
                vec = new Vector();
                StringBuffer tester = new StringBuffer();
                String lin;
                char promptChar = '>';
                while (true)
                {
                        if (interp.thread().inStream().eof())
                        {
                                break;
                        }
                        if (interp.thread().isLoading())
                        {
                                lin = interp.thread().inStream().getLine();
                        }
                        else
                        {
                                lin = interp.mach().console().promptGetLine('>');
                        }
                        tester.append(lin);
                        if (tester.length() > 0)
                        {
                                if (tester.charAt(tester.length()-1) == '~')
                                {
                                        tester.setCharAt(tester.length()-1, ' ');
                                        promptChar = '~';
                                }
                                else
                                {
                                        try
                                        {
                                                LogoList ll = tokenizer.tokenize(tester.toString());
                                                if (ll.length() == 1 &&
              ( //Birb
                                                        CaselessString.staticEquals(ll.pickInPlace(0).toString(), strEND)
              || CaselessString.staticEquals(ll.pickInPlace(0).toString(), strLocalizedEND) ) //Birb:30-8-1998
             )
                                                {
                                                        break;
                                                }
                                                tester = new StringBuffer();
                                                promptChar = '>';
                                        }
                                        catch (LanguageException e)
                                        {
                                                promptChar = e.getContChar();
                                                if (promptChar == '|' || promptChar == '\\' || promptChar == '~')
                                                {
                                                        tester.append(Machine.LINE_SEPARATOR);
                                                }
                                                else
                                                {
                                                        throw e;
                                                }
                                        }
                                }
                        }
                        vec.addElement(lin);
                }
                
                // Define procedure
                Procedure proc = new Procedure(interp.mach(), name, params, vec, isMacro);
                interp.mach().defineProc(proc);
                interp.mach().console().putStatusMessage(name.str +
     ((redefinedFlag)?" redefined.": //Birb-20Jul1999: show appropriate message, now that we allow redefinition of procs
     " defined.")
     ) //Birb
     ;
        }

}



