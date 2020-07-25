/*
===============================================================================

        FILE:  ParseTree.java
        
        PROJECT:
        
                Turtle Tracks
        
        CONTENTS:
        
                Runnable parse tree
        
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

import java.util.Vector;


/**
 * ParseTree object, ready to be interpreted.
 */

public final class ParseTree
{

        private ParseObject[] _lis;
        private int _clock;


        /**
         * Construct a tree given a clock value and a vector of parse objects.
         *
         * @param clock the clock value for the creation of the parse tree
         * @param v the vector of ParseObjects
         */
        @SuppressWarnings("unchecked")
        public ParseTree(
                int clock,
                Vector v)
        {
                int i;
                _lis = new ParseObject[v.size()];
                for (i=0; i<_lis.length; i++)
                {
                        _lis[i] = (ParseObject)(v.elementAt(i));
                }
                _clock = clock;
        }


        /**
         * Construct a tree given a clock value and an array of parse objects.
         *
         * @param clock the clock value for the creation of the parse tree
         * @param a the array of ParseObjects
         */
        public ParseTree(
                int clock,
                ParseObject[] a)
        {
                _clock = clock;
                _lis = a;
        }


        /**
         * Test the clock value
         *
         * @return true iff the tree is current, i.e. _clock is -1 (unexpirable) or
         *         _clock equals the remoteclock from the machine.
         */
        boolean testClock(
                int remoteClock)
        {
                return _clock == -1 || remoteClock == _clock;
        }


        /**
         * Clone the object
         *
         * @return a clone of this object
         */
        public Object clone()
        {
                ParseObject[] a = new ParseObject[_lis.length];
                int i;
                for (i=0; i<_lis.length; i++)
                {
                        a[i] = (ParseObject)(_lis[i].clone());
                }
                return new ParseTree(_clock, a);
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
         * Returns length of the parse tree
         *
         * @return the length
         */
        public int length()
        {
                return _lis.length;
        }


        /**
         * Executes this parse tree
         *
         * @param interp the interpreter environment
         *
         * @return the result
         *
         * @exception virtuoso.logo.LanguageException error thrown
         * @exception virtuoso.logo.ThrowException exception thrown
         */
        public LogoObject execute(
                InterpEnviron interp)
        throws
                LanguageException,
                ThrowException
        {
                LogoObject ret = LogoVoid.obj;
                int i;
                
                for (i=0; i<_lis.length; i++)
                {
                        LogoObject ret2 = _lis[i].evaluate(interp);
                        if (ret2 != LogoVoid.obj)
                        {
                                if (!interp.mach().isAutoIgnore() && ret != LogoVoid.obj)
                                {
                                        throw new LanguageException("You don't say what to do with " + ret);
                                }
                                else
                                {
                                        ret = ret2;
                                }
                        }
                }
                
                return ret;
        }

}



