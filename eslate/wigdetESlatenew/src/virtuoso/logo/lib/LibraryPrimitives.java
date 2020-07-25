/*
===============================================================================

        FILE:  LibraryPrimitives.java
        
        PROJECT:
        
                Turtle Tracks
        
        CONTENTS:
        
                Standard library primitives
        
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

public final class LibraryPrimitives
extends PrimitiveGroup
{

        private int _gensymNum;
        private static Random _rand;

        static
        {
                _rand = new Random();
        }


        /**
         * Set up primitive group
         */
        protected void setup(
                Machine mach,
                Console console)
        throws
                SetupException
        {
                registerPrimitive("CLOSEALL", "pCLOSEALL", 0);
                registerPrimitive("DO.UNTIL", "pDOUNTIL", 2);
                registerPrimitive("DO.WHILE", "pDOWHILE", 2);
                registerPrimitive("ERALL", "pERALL", 0);
                registerPrimitive("FOR", "pFOR", 2);
                registerPrimitive("FOREACH", "pFOREACH", 2);
                registerPrimitive("GENSYM", "pGENSYM", 0);
                registerPrimitive("LOCALMAKE", "pLOCALMAKE", 2);
                registerPrimitive("MAP", "pMAP", 2);
                registerPrimitive("NAME", "pNAME", 2);
                registerPrimitive("PICK", "pPICK", 1);
                registerPrimitive("POALL", "pPOALL", 0);
                registerPrimitive("QSORT", "pQSORT", 2);
                registerPrimitive("UNTIL", "pUNTIL", 2);
                registerPrimitive("WHILE", "pWHILE", 2);
                
                _gensymNum = 0;
                
                console.putStatusMessage("Turtle Tracks library primitives v1.0");
        }


        /**
         * Primitive implementation for CLOSEALL
         */
        public final LogoObject pCLOSEALL(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 0);
                interp.mach().closeAllIO();
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for DO.UNTIL
         */
        public final LogoObject pDOUNTIL(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException,
                ThrowException
        {
                testNumParams(params, 2);
                while (true)
                {
                        LogoObject ret = params[0].getRunnable(interp.mach()).execute(interp);
                        if (!interp.mach().isAutoIgnore() && ret != LogoVoid.obj)
                        {
                                throw new LanguageException("You don't say what to do with " + ret.toString());
                        }
                        if (params[1].getRunnable(interp.mach()).execute(interp).toBoolean())
                                break;
                }
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for DO.WHILE
         */
        public final LogoObject pDOWHILE(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException,
                ThrowException
        {
                testNumParams(params, 2);
                while (true)
                {
                        LogoObject ret = params[0].getRunnable(interp.mach()).execute(interp);
                        if (!interp.mach().isAutoIgnore() && ret != LogoVoid.obj)
                        {
                                throw new LanguageException("You don't say what to do with " + ret.toString());
                        }
                        if (!(params[1].getRunnable(interp.mach()).execute(interp).toBoolean()))
                                break;
                }
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for ERALL
         */
        public final LogoObject pERALL(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 0);
                interp.mach().eraseAll();
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for FOR
         */
        public final LogoObject pFOR(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException,
                ThrowException
        {
                testNumParams(params, 2);
                
                if (!(params[0] instanceof LogoList) || params[0].length() < 3 || params[0].length() > 4)
                {
                        throw new LanguageException("Control list expected");
                }
                LogoList controlList = (LogoList)(params[0]);
                if (!(controlList.pickInPlace(0) instanceof LogoWord))
                {
                        throw new LanguageException("Step=0 in control list");
                }
                int start = controlList.pickInPlace(1).toInteger();
                int end = controlList.pickInPlace(2).toInteger();
                int step = 1;
                if (controlList.length() == 4)
                {
                        step = controlList.pickInPlace(3).toInteger();
                        if (step == 0)
                        {
                                throw new LanguageException("Step=0 in control list");
                        }
                }
                
                SymbolTable local = new SymbolTable();
                CaselessString name = controlList.pickInPlace(0).toCaselessString();
                local.makeForced(name, new LogoWord(start));
                interp.thread().enterProcedure(local);
                try
                {
                        int i = start;
                        while ((step > 0 || i >= end) && (step < 0 || i <= end))
                        {
                                LogoObject ret = params[1].getRunnable(interp.mach()).execute(interp);
                                if (!interp.mach().isAutoIgnore() && ret != LogoVoid.obj)
                                {
                                        throw new LanguageException("You don't say what to do with " + ret.toString());
                                }
                                LogoObject obj = local.resolve(name);
                                if (obj == null)
                                {
                                        throw new LanguageException("Loop control variable erased.");
                                }
                                i = obj.toInteger() + step;
                                local.makeForced(name, new LogoWord(i));
                        }
                }
                finally
                {
                        interp.thread().exitProcedure();
                }
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for FOREACH
         */
        public final LogoObject pFOREACH(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException,
                ThrowException
        {
                testMinParams(params, 2);
                LogoList[] arr = new LogoList[params.length-1];
                for (int i=0; i<params.length-1; i++)
                {
                        if (!(params[i] instanceof LogoList))
                        {
                                throw new LanguageException("Mapping list expected");
                        }
                        arr[i] = (LogoList)(params[i]);
                }
                mapHelper(interp, params[params.length-1], arr, null);
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for GENSYM
         */
        public final LogoObject pGENSYM(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 0);
                synchronized(this)
                {
                        _gensymNum++;
                        return new LogoWord("." + String.valueOf(_gensymNum));
                }
        }


        /**
         * Primitive implementation for LOCALMAKE
         */
        public final LogoObject pLOCALMAKE(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 2);
                if (!(params[0] instanceof LogoWord))
                {
                        throw new LanguageException("Name expected");
                }
                interp.thread().localName(params[0].toCaselessString());
                interp.thread().makeName(params[0].toCaselessString(), params[1]);
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for MAP
         */
        public final LogoObject pMAP(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException,
                ThrowException
        {
                testMinParams(params, 2);
                LogoList[] arr = new LogoList[params.length-1];
                for (int i=1; i<params.length; i++)
                {
                        if (!(params[i] instanceof LogoList))
                        {
                                throw new LanguageException("Mapping list expected");
                        }
                        arr[i-1] = (LogoList)(params[i]);
                }
                LogoObject[] ret = new LogoObject[params[1].length()];
                int len = mapHelper(interp, params[0], arr, ret);
                LogoObject[] ret2 = new LogoObject[len];
                System.arraycopy(ret, 0, ret2, 0, len);
                return new LogoList(ret2);
        }


        /**
         * Primitive implementation for NAME
         */
        public final LogoObject pNAME(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 2);
                if (!(params[1] instanceof LogoWord))
                {
                        throw new LanguageException("Name expected");
                }
                interp.thread().makeName(params[1].toCaselessString(), params[0]);
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for PICK
         */
        public final LogoObject pPICK(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                int len = params[0].length();
                if (len == 0)
                {
                        throw new LanguageException("List or word is empty");
                }
                int i = interp.mach().random().nextInt();
                if (i<0)
                        i = i & 0x7fffffff;
                return params[0].pick((i % len) + 1);
        }


        /**
         * Primitive implementation for POALL
         */
        public final LogoObject pPOALL(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 0);
                interp.mach().printout(interp.thread().outStream(), null, null, null);
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for QSORT
         */
        public final LogoObject pQSORT(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException,
                ThrowException
        {
                testNumParams(params, 2);
                if (!(params[0] instanceof LogoList))
                {
                        throw new LanguageException("List expected");
                }
                LogoList old = (LogoList)(params[0]);
                LogoObject[] list = new LogoObject[old.length()];
                for (int i=0; i<old.length(); i++)
                {
                        list[i] = old.pickInPlace(i);
                }
                if (params[1] instanceof LogoList)
                {
                        qsortHelper(interp, list, 0, old.length(), (LogoList)(params[1]), null, null, null);
                }
                else
                {
                        CaselessString name = params[1].toCaselessString();
                        Procedure proc = interp.mach().resolveProc(name);
                        if (proc != null)
                        {
                                qsortHelper(interp, list, 0, old.length(), null, proc, null, null);
                        }
                        else
                        {
                                PrimitiveSpec prim = interp.mach().findPrimitive(name);
                                if (prim != null)
                                {
                                        qsortHelper(interp, list, 0, old.length(), null, null, prim, name.str);
                                }
                                else
                                {
                                        throw new LanguageException("I don't know how to " + name);
                                }
                        }
                }
                return new LogoList(list);
        }


        /**
         * Primitive implementation for UNTIL
         */
        public final LogoObject pUNTIL(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException,
                ThrowException
        {
                testNumParams(params, 2);
                while (true)
                {
                        if (params[0].getRunnable(interp.mach()).execute(interp).toBoolean())
                                break;
                        LogoObject ret = params[1].getRunnable(interp.mach()).execute(interp);
                        if (!interp.mach().isAutoIgnore() && ret != LogoVoid.obj)
                        {
                                throw new LanguageException("You don't say what to do with " + ret.toString());
                        }
                }
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for WHILE
         */
        public final LogoObject pWHILE(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException,
                ThrowException
        {
                testNumParams(params, 2);
                while (true)
                {
                        if (!(params[0].getRunnable(interp.mach()).execute(interp).toBoolean()))
                                break;
                        LogoObject ret = params[1].getRunnable(interp.mach()).execute(interp);
                        if (!interp.mach().isAutoIgnore() && ret != LogoVoid.obj)
                        {
                                throw new LanguageException("You don't say what to do with " + ret.toString());
                        }
                }
                return LogoVoid.obj;
        }


        /**
         * Helper for FOREACH and MAP
         */
        private static final int mapHelper(
                InterpEnviron interp,
                LogoObject procedure,
                LogoList[] map,
                LogoObject[] ret)
        throws
                LanguageException,
                ThrowException
        {
                // Determine nature of lambda
                LogoList lambda = null;
                Procedure proc = null;
                PrimitiveSpec prim = null;
                String primName = null;
                if (procedure instanceof LogoList)
                {
                        lambda = (LogoList)procedure;
                }
                else
                {
                        CaselessString name = procedure.toCaselessString();
                        proc = interp.mach().resolveProc(name);
                        if (proc == null)
                        {
                                prim = interp.mach().findPrimitive(name);
                                if (prim != null)
                                {
                                        primName = name.str;
                                }
                                else
                                {
                                        throw new LanguageException("I don't know how to " + name);
                                }
                        }
                }
                
                // Double-check map lists for length
                int len = 0;
                for (int i=0; i<map.length; i++)
                {
                        if (i==0)
                        {
                                len = map[i].length();
                        }
                        else if (map[i].length() != len)
                        {
                                throw new LanguageException("Map lists do not have the same length");
                        }
                }
                
                // Invoke
                LogoObject[] arr = new LogoObject[map.length];
                ParseObject[] cmds = new ParseObject[1];
                int reti = 0;
                for (int i=0; i<len; i++)
                {
                        LogoObject obj = LogoVoid.obj;
                        for (int j=0; j<map.length; j++)
                        {
                                arr[j] = map[j].pickInPlace(i);
                        }
                        if (lambda != null)
                        {
                                obj = applyAnonymous(interp, lambda, new LogoList(arr), true);
                        }
                        else
                        {
                                if (proc != null)
                                {
                                        cmds[0] = new ParseProcedure(proc, arr);
                                }
                                else
                                {
                                        cmds[0] = new ParsePrimitive(prim, primName, arr);
                                }
                                obj = new ParseTree(0, cmds).execute(interp);
                        }
                        if (ret != null)
                        {
                                if (obj != LogoVoid.obj)
                                {
                                        ret[reti] = obj;
                                        reti++;
                                }
                        }
                        else
                        {
                                if (!interp.mach().isAutoIgnore() && obj != LogoVoid.obj)
                                {
                                        throw new LanguageException("You don't say what to do with " + obj.toString());
                                }
                        }
                }
                
                return reti;
        }


        /**
         * Helper for QSORT
         */
        private final static void qsortHelper(
                InterpEnviron interp,
                LogoObject[] list,
                int start,
                int end,
                LogoList lambda,
                Procedure proc,
                PrimitiveSpec prim,
                String primName)
        throws
                LanguageException,
                ThrowException
        {
                // Check base case
                if (start > end-2)
                        return;
                
                // Choose pivot
                int pivot = _rand.nextInt();
                if (pivot < 0)
                        pivot = -pivot;
                pivot = start + (pivot % (end-start));
                LogoObject temp = list[pivot];
                list[pivot] = list[start];
                list[start] = temp;
                
                // Partition
                LogoObject[] arr = new LogoObject[2];
                ParseObject[] cmds = new ParseObject[1];
                int j=start+1;
                int k=end-1;
                while (true)
                {
                        while (j<=k)
                        {
                                arr[0] = list[start];
                                arr[1] = list[j];
                                if (lambda != null)
                                {
                                        if (applyAnonymous(interp, lambda, new LogoList(arr), true).toBoolean())
                                                break;
                                }
                                else if (proc != null)
                                {
                                        cmds[0] = new ParseProcedure(proc, arr);
                                        if (new ParseTree(0, cmds).execute(interp).toBoolean())
                                                break;
                                }
                                else
                                {
                                        cmds[0] = new ParsePrimitive(prim, primName, arr);
                                        if (new ParseTree(0, cmds).execute(interp).toBoolean())
                                                break;
                                }
                                j++;
                        }
                        while (j<=k)
                        {
                                if (j!=k)
                                {
                                        arr[0] = list[k];
                                        arr[1] = list[start];
                                        if (lambda != null)
                                        {
                                                if (applyAnonymous(interp, lambda, new LogoList(arr), true).toBoolean())
                                                        break;
                                        }
                                        else if (proc != null)
                                        {
                                                cmds[0] = new ParseProcedure(proc, arr);
                                                if (new ParseTree(0, cmds).execute(interp).toBoolean())
                                                        break;
                                        }
                                        else
                                        {
                                                cmds[0] = new ParsePrimitive(prim, primName, arr);
                                                if (new ParseTree(0, cmds).execute(interp).toBoolean())
                                                        break;
                                        }
                                }
                                k--;
                        }
                        if (j<k)
                        {
                                temp = list[j];
                                list[j] = list[k];
                                list[k] = temp;
                        }
                        else
                        {
                                break;
                        }
                }
                
                // Insert pivot
                temp = list[k];
                list[k] = list[start];
                list[start] = temp;
                
                // Recurse
                qsortHelper(interp, list, start, k, lambda, proc, prim, primName);
                qsortHelper(interp, list, k+1, end, lambda, proc, prim, primName);
        }

}



