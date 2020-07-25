/*
===============================================================================

        FILE:  ThreadPrimitives.java
        
        PROJECT:
        
                Turtle Tracks
        
        CONTENTS:
        
                Standard thread primitives
        
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


package virtuoso.logo.lib;

import virtuoso.logo.*;
import java.util.*;


/**
 * Names of primitives (keywords)
 */
@SuppressWarnings(value={"unchecked"})
public final class ThreadPrimitives
extends PrimitiveGroup
{

        private Hashtable _barriers;
        private Hashtable _locks;


        /**
         * Set up primitive group
         */
        protected void setup(
                Machine mach,
                Console console)
        throws
                SetupException
        {
                registerPrimitive("BARRIER", "pBARRIER", 2);
                registerPrimitive("CRITICAL", "pCRITICAL", 2);
                registerPrimitive("CURRENTTHREAD", "pCURRENTTHREAD", 0);
                registerPrimitive("STOPTHREAD", "pSTOPTHREAD", 0);
                registerPrimitive("THREAD", "pTHREAD", 1);
                registerPrimitive("THREADAPPLY", "pTHREADAPPLY", 2);
                registerPrimitive("THREADAPPLYID", "pTHREADAPPLYID", 2);
                registerPrimitive("THREADRUN", "pTHREADRUN", 1);
                registerPrimitive("THREADRUNID", "pTHREADRUNID", 1);
                registerPrimitive("THREADTERMINATE", "pTHREADTERMINATE", 1);
                
                _barriers = new Hashtable();
                _locks = new Hashtable();
                
                console.putStatusMessage("Turtle Tracks thread primitives v1.0");
        }


        /**
         * Primitive implementation for BARRIER
         */
        public final LogoObject pBARRIER(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 2);
                if (!(params[0] instanceof LogoWord))
                {
                        throw new LanguageException("Barrier name expected");
                }
                Barrier bar;
                CaselessString barrierID = params[0].toCaselessString();
                int maxThreads = params[1].toInteger();
                synchronized(_barriers)
                {
                        bar = (Barrier)(_barriers.get(barrierID));
                        if (bar == null)
                        {
                                bar = new Barrier(maxThreads);
                                _barriers.put(barrierID, bar);
                        }
                        if (bar.checkRequest(maxThreads))
                        {
                                _barriers.remove(barrierID);
                        }
                }
                bar.hit();
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for CRITICAL
         */
        public final LogoObject pCRITICAL(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException,
                ThrowException
        {
                testNumParams(params, 2);
                LogoObject ret = LogoVoid.obj;
                if (!(params[0] instanceof LogoWord))
                {
                        throw new LanguageException("Lock name expected");
                }
                CaselessString lockID = params[0].toCaselessString();
                CriticalLock lock;
                synchronized(_locks)
                {
                        lock = (CriticalLock)(_locks.get(lockID));
                        if (lock == null)
                        {
                                lock = new CriticalLock();
                                _locks.put(lockID, lock);
                        }
                        lock.promote();
                }
                try
                {
                        synchronized(lock)
                        {
                                ret = params[1].getRunnable(interp.mach()).execute(interp);
                        }
                }
                finally
                {
                        synchronized(_locks)
                        {
                                lock.demote();
                                if (lock.unused())
                                {
                                        _locks.remove(lockID);
                                }
                        }
                }
                return ret;
        }


        /**
         * Primitive implementation for CURRENTTHREAD
         */
        public final LogoObject pCURRENTTHREAD(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 0);
                return new LogoWord(interp.thread().threadID());
        }


        /**
         * Primitive implementation for STOPTHREAD
         */
        public final LogoObject pSTOPTHREAD(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException,
                ThrowException
        {
                testNumParams(params, 0);
                throw new ThrowException("STOPTHREAD");
        }


        /**
         * Primitive implementation for THREAD
         */
        public final LogoObject pTHREAD(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1, 2);
                if (params.length == 1)
                {
                        threadRunHelper(interp, params);
                }
                else
                {
                        threadApplyHelper(interp, params);
                }
                return LogoVoid.obj;
        }


/*
                boolean needReturn = false;
                LogoList paramNames = null;
                LogoList runnable = null;
                ParseTree treeable = null;
                
                if (params[0] instanceof LogoWord)
                {
                        testMinParams(params, 2);
                        
                        // Get parameters
                        ParseObject[] cmds = new ParseObject[1];
                        LogoObject[] arr;
                        if (params[1] instanceof LogoList)
                        {
                                LogoList paramValuesList = (LogoList)(params[1]);
                                int len = paramValuesList.length();
                                arr = new LogoObject[len];
                                for (int i=0; i<len; i++)
                                {
                                        arr[i] = paramValuesList.pickInPlace(i);
                                }
                        }
                        else
                        {
                                arr = new LogoObject[1];
                                arr[0] = params[1];
                        }
                        
                        // Create parse tree
                        CaselessString name = params[0].toCaselessString();
                        Procedure proc = interp.mach().resolveProc(name);
                        if (proc != null)
                        {
                                cmds[0] = new ParseProcedure(proc, arr);
                        }
                        else
                        {
                                PrimitiveSpec prim = interp.mach().findPrimitive(name);
                                if (prim != null)
                                {
                                        cmds[0] = new ParsePrimitive(prim, name.str, arr);
                                }
                                else
                                {
                                        throw new LanguageException("I don't know how to " + name);
                                }
                        }
                        treeable = new ParseTree(0, cmds);
                }
                else
                {
                        // Find paramNames and runnable
                        LogoList lambda = (LogoList)(params[0]);
                        if (lambda.length() > 1 && (lambda.pickInPlace(0) instanceof LogoList))
                        {
                                paramNames = (LogoList)(lambda.pickInPlace(0));
                                if (lambda.length() == 2 && (lambda.pickInPlace(1) instanceof LogoList))
                                {
                                        runnable = (LogoList)(lambda.pickInPlace(1));
                                }
                                else
                                {
                                        runnable = (LogoList)(lambda.butFirst());
                                }
                        }
                        else
                        {
                                runnable = lambda;
                        }
                }
                
                // Find paramValues and threadid
                LogoList paramValues = null;
                String threadid = null;
                if (paramNames != null)
                {
                        testMinParams(params, 2);
                        if (!(params[1] instanceof LogoList))
                        {
                                throw new LanguageException("Argument list expected");
                        }
                        else
                        {
                                paramValues = (LogoList)(params[1]);
                                if (params.length == 3)
                                {
                                        if (!(params[2] instanceof LogoWord))
                                        {
                                                throw new LanguageException("Thread ID expected");
                                        }
                                        threadid = params[2].toString();
                                }
                                else
                                {
                                        threadid = "__t" + interp.mach().getUniqueNum();
                                        needReturn = true;
                                }
                        }
                }
                else
                {
                        testMaxParams(params, 2);
                        if (params.length == 2)
                        {
                                if (!(params[1] instanceof LogoWord))
                                {
                                        throw new LanguageException("Thread ID expected");
                                }
                                threadid = params[1].toString();
                        }
                        else
                        {
                                threadid = "__t" + interp.mach().getUniqueNum();
                                needReturn = true;
                        }
                }
                
                // Make symbol table if necessary
                SymbolTable local = new SymbolTable();
                if (paramNames != null)
                {
                        for (int i=0; i<paramValues.length(); i++)
                        {
                                if (!(paramNames.pickInPlace(i) instanceof LogoWord))
                                {
                                        throw new LanguageException("Bad symbol list in lambda expression");
                                }
                                local.makeForced(
                                        paramNames.pickInPlace(i).toCaselessString(),
                                        paramValues.pickInPlace(i));
                        }
                }
                
                // Spawn thread
                InterpreterThread thr = interp.thread();
                if (runnable != null)
                {
                        interp.mach().spawnThread(new CaselessString(threadid), runnable, thr.inStreamID(),
                                thr.inStream(), thr.outStreamID(), thr.outStream(), local);
                }
                else
                {
                        interp.mach().spawnThread(new CaselessString(threadid), treeable, thr.inStreamID(),
                                thr.inStream(), thr.outStreamID(), thr.outStream(), local);
                }
                
                // Return
                if (needReturn)
                {
                        return new LogoWord(threadid);
                }
                else
                {
                        return LogoVoid.obj;
                }
*/


        /**
         * Primitive implementation for THREADAPPLY
         */
        public final LogoObject pTHREADAPPLY(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                threadApplyHelper(interp, params);
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for THREADAPPLYID
         */
        public final LogoObject pTHREADAPPLYID(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                return new LogoWord(threadApplyHelper(interp, params));
        }


        /**
         * Primitive implementation for THREADRUN
         */
        public final LogoObject pTHREADRUN(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                threadRunHelper(interp, params);
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for THREADRUNID
         */
        public final LogoObject pTHREADRUNID(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                return new LogoWord(threadRunHelper(interp, params));
        }


        /**
         * Primitive implementation for THREADTERMINATE
         */
        public final LogoObject pTHREADTERMINATE(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                if (!(params[0] instanceof LogoWord))
                {
                        throw new LanguageException("Thread name expected");
                }
                interp.mach().terminateOneThread(params[0].toCaselessString());
                return LogoVoid.obj;
        }


        /**
         * Helper for THREADRUN
         */
        private final String threadRunHelper(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1, 2);
                String threadid = null;
                LogoList runnable = null;
                if (params.length == 1)
                {
                        if (!(params[0] instanceof LogoList))
                        {
                                throw new LanguageException("Runnable list expected");
                        }
                        runnable = (LogoList)(params[0]);
                }
                else
                {
                        if (!(params[0] instanceof LogoWord))
                        {
                                throw new LanguageException("Thread ID expected");
                        }
                        threadid = params[0].toString();
                        if (!(params[1] instanceof LogoList))
                        {
                                throw new LanguageException("Runnable list expected");
                        }
                        runnable = (LogoList)(params[1]);
                }
                return makeThread(interp, threadid, runnable, null, null, null);
        }


        /**
         * Helper for THREADAPPLY
         */
        private final String threadApplyHelper(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 2, 3);
                
                // Map params to info
                String threadid = null;
                LogoObject lambda = null;
                LogoList lambdaArgs = null;
                if (params.length == 2)
                {
                        lambda = params[0];
                        if (!(params[1] instanceof LogoList))
                        {
                                throw new LanguageException("Argument list expected");
                        }
                        lambdaArgs = (LogoList)(params[1]);
                }
                else
                {
                        if (!(params[0] instanceof LogoWord))
                        {
                                throw new LanguageException("Thread ID expected");
                        }
                        threadid = params[0].toString();
                        lambda = params[1];
                        if (!(params[2] instanceof LogoList))
                        {
                                throw new LanguageException("Argument list expected");
                        }
                        lambdaArgs = (LogoList)(params[2]);
                }
                
                // Build ParseTree or runnable/names/values triple
                LogoList runnable = null;
                ParseTree treeable = null;
                LogoList paramNames = null;
                LogoList paramValues = null;
                if (lambda instanceof LogoWord)
                {
                        // Make a treeable
                        ParseObject[] cmds = new ParseObject[1];
                        
                        // Get parameters
                        int len = lambdaArgs.length();
                        LogoObject[] arr = new LogoObject[len];
                        for (int i=0; i<len; i++)
                        {
                                arr[i] = lambdaArgs.pickInPlace(i);
                        }
                        
                        // Create parse tree
                        CaselessString name = lambda.toCaselessString();
                        Procedure proc = interp.mach().resolveProc(name);
                        if (proc != null)
                        {
                                cmds[0] = new ParseProcedure(proc, arr);
                        }
                        else
                        {
                                PrimitiveSpec prim = interp.mach().findPrimitive(name);
                                if (prim != null)
                                {
                                        cmds[0] = new ParsePrimitive(prim, name.str, arr);
                                }
                                else
                                {
                                        throw new LanguageException("I don't know how to " + name);
                                }
                        }
                        treeable = new ParseTree(0, cmds);
                }
                else
                {
                        // Extract paramNames, paramValues and runnable
                        LogoList lambdaList = (LogoList)(lambda);
                        if (lambdaList.length() == 0 || !(lambdaList.pickInPlace(0) instanceof LogoList))
                        {
                                throw new LanguageException("Lambda list expected");
                        }
                        paramNames = (LogoList)(lambdaList.pickInPlace(0));
                        if (lambdaList.length() == 2 && (lambdaList.pickInPlace(1) instanceof LogoList))
                        {
                                runnable = (LogoList)(lambdaList.pickInPlace(1));
                        }
                        else
                        {
                                runnable = (LogoList)(lambdaList.butFirst());
                        }
                        paramValues = lambdaArgs;
                }
                
                // Spawn thread
                return makeThread(interp, threadid, runnable, paramNames, paramValues, treeable);
        }


        /**
         * Helper for thread spawning primitives
         */
        private final String makeThread(
                InterpEnviron interp,
                String threadid,
                LogoList runnable,
                LogoList paramNames,
                LogoList paramValues,
                ParseTree treeable)
        throws
                LanguageException
        {
                // Make symbol table if necessary
                SymbolTable local = new SymbolTable();
                if (paramNames != null)
                {
                        for (int i=0; i<paramValues.length(); i++)
                        {
                                if (!(paramNames.pickInPlace(i) instanceof LogoWord))
                                {
                                        throw new LanguageException("Bad symbol list in lambda expression");
                                }
                                local.makeForced(
                                        paramNames.pickInPlace(i).toCaselessString(),
                                        paramValues.pickInPlace(i));
                        }
                }
                
                // Spawn thread
                if (threadid == null)
                {
                        threadid = "__t" + interp.mach().getUniqueNum();
                }
                InterpreterThread thr = interp.thread();
                if (runnable != null)
                {
                        interp.mach().spawnThread(new CaselessString(threadid), runnable, thr.inStreamID(),
                                thr.inStream(), thr.outStreamID(), thr.outStream(), local);
                }
                else
                {
                        interp.mach().spawnThread(new CaselessString(threadid), treeable, thr.inStreamID(),
                                thr.inStream(), thr.outStreamID(), thr.outStream(), local);
                }
                
                // Return
                return threadid;
        }


        /**
         * Barrier object for thread synchronization
         */

        final class Barrier
        {

                private int _maxThreads;
                private int _numThreads;
                private int _reqCount;


                /**
                 * Default constructor
                 */
                Barrier(
                        int maxThreads)
                {
                        _maxThreads = maxThreads;
                        _numThreads = 0;
                        _reqCount = 0;
                }


                /**
                 * Check barrier request
                 */
                final boolean checkRequest(
                        int maxThreads)
                throws
                        LanguageException
                {
                        if (maxThreads != _maxThreads)
                        {
                                throw new LanguageException("Mismatch in thread count");
                        }
                        _reqCount++;
                        return (_reqCount >= _maxThreads);
                }


                /**
                 * Hit barrier
                 */
                final synchronized void hit()
                {
                        _numThreads++;
                        if (_numThreads >= _maxThreads)
                        {
                                notifyAll();
                        }
                        else
                        {
                                try
                                {
                                        wait();
                                }
                                catch (InterruptedException e) {}
                        }
                }

        }


        /**
         * Lock object for a critical section
         */

        final class CriticalLock
        {

                private int _refCount;


                /**
                 * Default constructor
                 */
                CriticalLock()
                {
                        _refCount = 0;
                }


                /**
                 * Promote reference count
                 */
                final synchronized void promote()
                {
                        _refCount++;
                }


                /**
                 * Demote reference count
                 */
                final synchronized void demote()
                {
                        _refCount--;
                }


                /**
                 * Is lock unused? (refCount == 0)
                 */
                final synchronized boolean unused()
                {
                        return (_refCount == 0);
                }

        }

}



