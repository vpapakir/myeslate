/*
===============================================================================

        FILE:  ParseProcedure.java
        
        PROJECT:
        
                Turtle Tracks
        
        CONTENTS:
        
                Procedure node in a parse tree
        
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
 * Procedure node in a parse tree
 */

public class ParseProcedure
extends ParseObject
{

        private Procedure _proc;
        private ParseObject[] _args;


        /**
         * Constructor
         *
         * @param proc the procedure to call
         * @param args the arguments, as parse tree node
         */
        public ParseProcedure(
                Procedure proc,
                ParseObject[] args)
        {
                _proc = proc;
                _args = args;
        }


        /**
         * Clone the object
         *
         * @return a clone of this object
         */
        public Object clone()
        {
                return new ParseProcedure(_proc, _args);
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
         * The name of the procedure
         *
         * @return the name
         */
        String procName()
        {
                return _proc.getName().str;
        }


        /**
         * Evaluate this object in the given environment
         *
         * @param interp the environment
         *
         * @return the return value
         *
         * @exception virtuoso.logo.LanguageException error thrown
         * @exception virtuoso.logo.ThrowException exception thrown
         */
        LogoObject evaluate(
                InterpEnviron interp)
        throws
                LanguageException,
                ThrowException
        {
                // Evaluate arguments
                LogoObject[] evalArgs = new LogoObject[_args.length];
                for (int i=0; i<_args.length; i++)
                {
                        evalArgs[i] = _args[i].evaluate(interp);
                        if (evalArgs[i] == LogoVoid.obj)
                        {
                                throw new LanguageException(_args[i].procName().toUpperCase() +
                                        " didn't output to " + _proc.getName().str.toUpperCase());
                        }
                }
                
                // Check for thread suspension and termination
                Thread.yield();
                interp.mach().checkSuspend();
                if (interp.thread().stopping())
                {
                        throw new ThrowException(".SUDDENSTOPTHREAD");
                }
                
                // Set up new stack frame
                LogoObject ret = LogoVoid.obj;
                LogoList paramNames = _proc.getParams();
                SymbolTable local = new SymbolTable();
                for (int i=0; i<paramNames.length(); i++)
                {
                        local.makeForced(
                                paramNames.pickInPlace(i).toCaselessString(),
                                evalArgs[i]);
                }
                InterpEnviron interp2 = new InterpEnviron(interp);
                interp.thread().enterProcedure(local);
                
                // Invoke procedure
                LogoObject obj = LogoVoid.obj;
                try
                {
                        obj = _proc.getCode().getRunnable(interp.mach()).execute(interp2);
                }
                catch (ThrowException e)
                {
                        if (!_proc.isMacro() && e.getTag().equals("STOP"))
                        {
                                ret = e.getObj();
                        }
                        else
                        {
                                throw e;
                        }
                }
                catch (LanguageException e)
                {
                        if (e.getProcName() == null)
                        {
                                throw new LanguageException(e.getMessage(),
                                        e.getPrimName(), _proc.getName().toString(), e.getContChar());
                        }
                        else
                        {
                                throw e;
                        }
                }
                finally
                {
                        interp.thread().exitProcedure();
                }
                if (_proc.isMacro())
                {
                        ret = obj;
                }
                else if (!interp.mach().isAutoIgnore() && obj != LogoVoid.obj)
                {
                        throw new LanguageException("You don't say what to do with " + obj);
                }
                return ret;
        }

}



