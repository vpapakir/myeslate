/*
===============================================================================

        FILE:  InterpEnviron.java
        
        PROJECT:
        
                Turtle Tracks
        
        CONTENTS:
        
                Interpreter environment object
        
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
 * Interpreter environment data
 */

public final class InterpEnviron
{
        private byte _testState;
        private int _loopIndex;
        
        private InterpreterThread _thread;
        private Machine _mach;


        /**
         * Construct an empty interpreter environment
         * (used only by PrimitiveGroup's initializer)
         */
        InterpEnviron()
        {
        }


        /**
         * Construct a default interpreter environment, with an undefined test
         * state and loop index.
         *
         * @param t the thread running
         */
        public InterpEnviron(
                InterpreterThread t)
        {
                _testState = (byte)(-1);
                _loopIndex = -1;
                _thread = t;
                _mach = t.mach();
        }


        /**
         * Construct an interpreter environment
         *
         * @param ts the test state (must be 0 for false, 1 for true, or -1 for unset)
         * @param li the current repcount
         * @param t the thread running
         */
        public InterpEnviron(
                byte ts,
                int li,
                InterpreterThread t)
        {
                _testState = ts;
                _loopIndex = li;
                _thread = t;
                _mach = t.mach();
        }


        /**
         * Construct an interpreter environment
         *
         * @param env the environment to copy
         */
        public InterpEnviron(
                InterpEnviron env)
        {
                _testState = env._testState;
                _loopIndex = env._loopIndex;
                _thread = env._thread;
                _mach = env._mach;
        }


        /**
         * Set the test state
         *
         * @param val true or false
         */
        public final void setTestResult(
                boolean val)
        {
                _testState = val ? (byte)1 : (byte)0;
        }


        /**
         * Get the test state
         *
         * @return true or false
         *
         * @exception virtuoso.logo.LanguageException the current test state is unset
         */
        public final boolean getTestResult()
        throws
                LanguageException
        {
                if (_testState == -1)
                {
                        throw new LanguageException("No corresponding TEST statement");
                }
                return _testState == 1;
        }


        /**
         * Set the test state (low level)
         *
         * @param val 1 for true, 0 for false, or -1 for unset
         */
        public final void setTestState(
                byte val)
        {
                _testState = val;
        }


        /**
         * Get the test state (low level)
         *
         * @return 1 for true, 0 for false, or -1 for unset
         */
        public final byte getTestState()
        {
                return _testState;
        }


        /**
         * Get the repeat count
         *
         * @return the repeat count
         */
        public final int getLoopIndex()
        {
                return _loopIndex;
        }


        /**
         * Accessor for machine
         *
         * @return the machine
         */
        public final Machine mach()
        {
                return _mach;
        }


        /**
         * Accessor for thread
         *
         * @return the thread
         */
        public final InterpreterThread thread()
        {
                return _thread;
        }

}



