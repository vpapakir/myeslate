/*
===============================================================================

        FILE:  LogoList.java
        
        PROJECT:
        
                Turtle Tracks
        
        CONTENTS:
        
                List logo object
        
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
 * Logo List object
 */

public class LogoList
extends LogoObject
{

        private LogoObject[] _lis;
        private ParseTree _run;


        /**
         * Construct an empty list
         */
        public LogoList()
        {
                _lis = new LogoObject[0];
                _run = null;
        }


        /**
         * Construct a list with a given LogoObject array.
         *
         * @param a array to use
         */
        public LogoList(
                LogoObject[] a)
        {
                _lis = a;
                _run = null;
        }


        /**
         * Construct a list with a given vector of LogoObjects
         *
         * @param v vector to use
         */
        @SuppressWarnings("unchecked")
        public LogoList(
                Vector v)
        {
                _lis = new LogoObject[v.size()];
                int i;
                for (i=0; i<_lis.length; i++)
                {
                        _lis[i] = (LogoObject)(v.elementAt(i));
                }
                _run = null;
        }


        /**
         * Clone the object
         *
         * @return a clone of this object
         */
        public Object clone()
        {
                int i;
                LogoObject[] a = new LogoObject[_lis.length];
                
                for (i=0; i<_lis.length; i++)
                {
                        a[i] = _lis[i];
                }
                return new LogoList(a);
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
                if (obj instanceof LogoList)
                {
                        if (((LogoList)obj).length() != _lis.length)
                        {
                                return false;
                        }
                        else
                        {
                                int i;
                                
                                for (i=0; i<_lis.length; i++)
                                {
                                        if (!(_lis[i].equals(((LogoList)obj)._lis[i])))
                                        {
                                                return false;
                                        }
                                }
                                return true;
                        }
                }
                else
                {
                        return false;
                }
        }


        /**
         * Convert to a string, for display purposes.
         *
         * @return the string
         */
        public String toString()
        {
                int i;
                StringBuffer sb = new StringBuffer();
                
                sb.append('[').append(' ');
                for (i=0; i<_lis.length; i++)
                {
                        sb.append(_lis[i].toString()).append(' ');
                }
                sb.append(']');
                
                return sb.toString();
        }


        /**
         * Unparse list. Emits escape sequences.
         *
         * @return the string
         */
        public String unparse()
        {
                int i;
                StringBuffer sb = new StringBuffer();
                
                sb.append('[').append(' ');
                for (i=0; i<_lis.length; i++)
                {
                        sb.append(_lis[i].unparse()).append(' ');
                }
                sb.append(']');
                
                return new String(sb);
        }


        /**
         * Convert to a string with no enclosing brackets
         *
         * @return the string
         */
        public String toStringOpen()
        {
                int i;
                StringBuffer sb = new StringBuffer();
                
                for (i=0; i<_lis.length; i++)
                {
                        sb.append(_lis[i].toString());
                        if (i != _lis.length-1)
                                sb.append(' ');
                }
                
                return sb.toString();
        }


        /**
         * Returns a list that has been retokenized for running
         *
         * @param mach the machine to parse with
         * 
         * @return the parse tree
         *
         * @exception virtuoso.logo.LanguageException unable to parse
         */
        public ParseTree getRunnable(
                Machine mach)
        throws
                LanguageException
        {
                if (_run != null)
                {
                        if (_run.testClock(mach.getClock()))
                        {
                                return _run;
                        }
                }
                _run = (new Parser()).parse(this, mach);
                return _run;
        }


        /**
         * Copy the first element of the list
         *
         * @return an object containing first
         *
         * @exception virtuoso.logo.LanguageException object is an empty list
         */
        public LogoObject first()
        throws
                LanguageException
        {
                if (_lis.length == 0)
                {
                        throw new LanguageException("Empty list");
                }
                return _lis[0];
        }


        /**
         * Copy the last element of the list
         *
         * @return an object containing last
         *
         * @exception virtuoso.logo.LanguageException object is an empty list
         */
        public LogoObject last()
        throws
                LanguageException
        {
                if (_lis.length == 0)
                {
                        throw new LanguageException("Empty list");
                }
                return _lis[_lis.length-1];
        }


        /**
         * Copy all parts except first
         *
         * @return an object containing butfirst
         *
         * @exception virtuoso.logo.LanguageException object is an empty list
         */
        public LogoObject butFirst()
        throws
                LanguageException
        {
                if (_lis.length == 0)
                {
                        throw new LanguageException("Empty list");
                }
                
                int i;
                LogoObject[] a = new LogoObject[_lis.length-1];
                
                for (i=1; i<_lis.length; i++)
                {
                        a[i-1] = _lis[i];
                }
                return new LogoList(a);
        }


        /**
         * Copy all parts except last
         *
         * @return a new object containing butlast
         *
         * @exception virtuoso.logo.LanguageException object is an empty list
         */
        public LogoObject butLast()
        throws
                LanguageException
        {
                if (_lis.length == 0)
                {
                        throw new LanguageException("Empty list");
                }
                
                int i;
                LogoObject[] a = new LogoObject[_lis.length-1];
                
                for (i=0; i<_lis.length-1; i++)
                {
                        a[i] = _lis[i];
                }
                return new LogoList(a);
        }


        /**
         * Inserts given object at front of list, and returns new list
         *
         * @param obj the object to insert
         *
         * @return a new object containing fput
         */
        public LogoObject fput(
                LogoObject obj)
        {
                if (obj instanceof LogoVoid)
                {
                        return this;
                }
                else
                {
                        int i;
                        LogoObject[] a = new LogoObject[_lis.length+1];
                        
                        a[0] = obj;
                        for (i=0; i<_lis.length; i++)
                        {
                                a[i+1] = _lis[i];
                        }
                        return new LogoList(a);
                }
        }


        /**
         * Inserts given object at back of list, and returns new list
         *
         * @param obj the object to insert
         *
         * @return a new object containing lput
         */
        public LogoObject lput(
                LogoObject obj)
        {
                if (obj instanceof LogoVoid)
                {
                        return this;
                }
                else
                {
                        int i;
                        LogoObject[] a = new LogoObject[_lis.length+1];
                        
                        for (i=0; i<_lis.length; i++)
                        {
                                a[i] = _lis[i];
                        }
                        a[_lis.length] = obj;
                        return new LogoList(a);
                }
        }


        /**
         * Returns length of the object
         *
         * @return the length
         */
        public int length()
        {
                return _lis.length;
        }


        /**
         * Is given object a member of this list
         *
         * @return true iff member
         */
        public boolean isMember(
                LogoObject obj)
        {
                int i;
                
                for (i=0; i<_lis.length; i++)
                {
                        if (_lis[i].equals(obj))
                        {
                                return true;
                        }
                }
                
                return false;
        }


        /**
         * Pick the index'th member of the list
         *
         * @param index 1-based index
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
                if (_lis.length == 0)
                {
                        throw new LanguageException("Empty list");
                }
                if (index < 1 || index > _lis.length)
                {
                        throw new LanguageException("Index out of bounds");
                }
                return _lis[index-1];
        }


        /**
         * Pick the index'th member of the list. Throws ArrayIndexOutOfBoundsException
         * instead of LanguageException for index out of bounds
         *
         * @param index 0-based index
         *
         * @return the member
         */
        public LogoObject pickInPlace(
                int index)
        {
                return _lis[index];
        }


}



