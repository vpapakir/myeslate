/*
===============================================================================

        FILE:  StandardPrimitives.java
        
        PROJECT:
        
                Turtle Tracks
        
        CONTENTS:
        
                Standard primitives
        
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
import virtuoso.logo.Console;

import java.util.*;
import java.io.*;


/**
 * Names of primitives (keywords)
 */
@SuppressWarnings(value={"unchecked"})
public final class StandardPrimitives
extends PrimitiveGroup
{

        private static final double _radToDeg = 180.0 / Math.PI;
        private static final double _degToRad = Math.PI / 180.0;


        /**
         * Set up primitive group
         */
        protected void setup(
                Machine mach,
                Console console)
        throws
                SetupException
        {
                registerPrimitive("=", "pEQUALP", 2);
                registerPrimitive(">", "pGREATERP", 2);
                registerPrimitive("<", "pLESSP", 2);
                registerPrimitive(">=", "pGREATEREQUALP", 2);
                registerPrimitive("<=", "pLESSEQUALP", 2);
                registerPrimitive("+", "pSUM", 2);
                registerPrimitive("-", "pDIFFERENCE", 2);
                registerPrimitive("*", "pPRODUCT", 2);
                registerPrimitive("/", "pQUOTIENT", 2);
                registerPrimitive("ABS", "pABS", 1);
                registerPrimitive("ALLOF", "pALLOF", 2);
                registerPrimitive("ALLOPEN", "pALLOPEN", 0);
                registerPrimitive("AND", "pALLOF", 2);
                registerPrimitive("ANYOF", "pANYOF", 2);
                registerPrimitive("APPLY", "pAPPLY", 2);
                registerPrimitive("APPLYRESULT", "pAPPLYRESULT", 2);
                registerPrimitive("ARCCOS", "pARCCOS", 1);
                registerPrimitive("ARCSIN", "pARCSIN", 1);
                registerPrimitive("ARCTAN", "pARCTAN", 1);
                registerPrimitive("ARCTAN2", "pARCTAN2", 2);
                registerPrimitive("ASCII", "pASCII", 1);
                registerPrimitive("ASHIFT", "pASHIFT", 2);
                registerPrimitive("BEFORE?", "pBEFOREP", 2);
                registerPrimitive("BEFOREP", "pBEFOREP", 2);
                registerPrimitive("BF", "pBUTFIRST", 1);
                registerPrimitive("BITAND", "pBITAND", 2);
                registerPrimitive("BITNOT", "pBITNOT", 1);
                registerPrimitive("BITOR", "pBITOR", 2);
                registerPrimitive("BITXOR", "pBITXOR", 2);
                registerPrimitive("BL", "pBUTLAST", 1);
                registerPrimitive("BUTFIRST", "pBUTFIRST", 1);
                registerPrimitive("BUTLAST", "pBUTLAST", 1);
                registerPrimitive("BYE", "pGOODBYE", 0);
                registerPrimitive("CATCH", "pCATCH", 2);
                registerPrimitive("CHAR", "pCHAR", 1);
                registerPrimitive("CLOSE", "pCLOSE", 1);
                registerPrimitive("CONTENTS", "pCONTENTS", 0);
                registerPrimitive("COS", "pCOS", 1);
                registerPrimitive("DEFINE", "pDEFINE", 2);
                registerPrimitive("DEFINED?", "pDEFINEDP", 1);
                registerPrimitive("DEFINEDP", "pDEFINEDP", 1);
                registerPrimitive("DIFFERENCE", "pDIFFERENCE", 2);
                registerPrimitive("EDIT", "pEDIT", 1);
                registerPrimitive("EMPTY?", "pEMPTYP", 1);
                registerPrimitive("EMPTYP", "pEMPTYP", 1);
                registerPrimitive("EOF?", "pEOFP", 0);
                registerPrimitive("EOFP", "pEOFP", 0);
                registerPrimitive("EQUAL?", "pEQUALP", 2);
                registerPrimitive("EQUALP", "pEQUALP", 2);
                registerPrimitive("ERASE", "pERASE", 1);
                registerPrimitive("ERASENAME", "pERASENAME", 1);
                registerPrimitive("ERASEPLIST", "pERASEPLIST", 1);
                registerPrimitive("ERASEPROCEDURE", "pERASEPROCEDURE", 1);
                registerPrimitive("ERN", "pERASENAME", 1);
                registerPrimitive("ERP", "pERASEPROCEDURE", 1);
                registerPrimitive("ERPL", "pERASEPLIST", 1);
                registerPrimitive("EXP", "pEXP", 1);
                registerPrimitive("FALSE", "pFALSE", 0);
                registerPrimitive("FIRST", "pFIRST", 1);
                registerPrimitive("FPUT", "pFPUT", 2);
                registerPrimitive("GC", "pGC", 0);
                registerPrimitive("GOODBYE", "pGOODBYE", 0);
                registerPrimitive("GPROP", "pGPROP", 2);
                registerPrimitive("GREATER?", "pGREATERP", 2);
                registerPrimitive("GREATEREQUAL?", "pGREATEREQUALP", 2);
                registerPrimitive("GREATEREQUALP", "pGREATEREQUALP", 2);
                registerPrimitive("GREATERP", "pGREATERP", 2);
                registerPrimitive("IF", "pIF", 2);
                registerPrimitive("IFELSE", "pIFELSE", 3);
                registerPrimitive("IFF", "pIFFALSE", 1);
                registerPrimitive("IFFALSE", "pIFFALSE", 1);
                registerPrimitive("IFT", "pIFTRUE", 1);
                registerPrimitive("IFTRUE", "pIFTRUE", 1);
                registerPrimitive("IGNORE", "pIGNORE", 1);
                registerPrimitive("INTEGER", "pINTEGER", 1);
                registerPrimitive("INVOKE", "pINVOKE", 2);
                registerPrimitive("INVOKERESULT", "pINVOKERESULT", 2);
                registerPrimitive("ITEM", "pITEM", 2);
                registerPrimitive("KEY?", "pKEYP", 0);
                registerPrimitive("KEYP", "pKEYP", 0);
                registerPrimitive("LAST", "pLAST", 1);
                registerPrimitive("LENGTH", "pLENGTH", 1);
                registerPrimitive("LESS?", "pLESSP", 2);
                registerPrimitive("LESSEQUAL?", "pLESSEQUALP", 2);
                registerPrimitive("LESSEQUALP", "pLESSEQUALP", 2);
                registerPrimitive("LESSP", "pLESSP", 2);
                registerPrimitive("LIST", "pLIST", 2);
                registerPrimitive("LIST?", "pLISTP", 1);
                registerPrimitive("LISTP", "pLISTP", 1);
                registerPrimitive("LOCAL", "pLOCAL", 1);
                registerPrimitive("LOG", "pLOG", 1);
                registerPrimitive("LOWERCASE", "pLOWERCASE", 1);
                registerPrimitive("LPUT", "pLPUT", 2);
                registerPrimitive("LSHIFT", "pLSHIFT", 2);
                registerPrimitive("MACRO?", "pMACROP", 1);
                registerPrimitive("MACROP", "pMACROP", 1);
                registerPrimitive("MAKE", "pMAKE", 2);
                registerPrimitive("MEMBER?", "pMEMBERP", 2);
                registerPrimitive("MEMBERP", "pMEMBERP", 2);
                registerPrimitive("MINUS", "pMINUS", 1);
                registerPrimitive("NAME?", "pNAMEP", 1);
                registerPrimitive("NAMEP", "pNAMEP", 1);
                registerPrimitive("NAMES", "pNAMES", 0);
                registerPrimitive("NOT", "pNOT", 1);
                registerPrimitive("NUMBER?", "pNUMBERP", 1);
                registerPrimitive("NUMBERP", "pNUMBERP", 1);
                registerPrimitive("OP", "pOUTPUT", 1);
                registerPrimitive("OR", "pANYOF", 2);
                registerPrimitive("OUTPUT", "pOUTPUT", 1);
                registerPrimitive("PARSE", "pPARSE", 1);
                registerPrimitive("PI", "pPI", 0);
                registerPrimitive("PLISTS", "pPLISTS", 0);
                registerPrimitive("PO", "pPRINTOUT", 1);
                registerPrimitive("PON", "pPRINTOUTNAME", 1);
                registerPrimitive("POP", "pPRINTOUTPROCEDURE", 1);
                registerPrimitive("POPL", "pPRINTOUTPLIST", 1);
                registerPrimitive("POWER", "pPOWER", 2);
                registerPrimitive("PPROP", "pPPROP", 3);
                registerPrimitive("PR", "pPRINT", 1);
                registerPrimitive("PRIMITIVE?", "pPRIMITIVEP", 1);
                registerPrimitive("PRIMITIVEP", "pPRIMITIVEP", 1);
                registerPrimitive("PRINT", "pPRINT", 1);
                registerPrimitive("PRINT1", "pPRINT1", 1);
                registerPrimitive("PRINTOUT", "pPRINTOUT", 1);
                registerPrimitive("PRINTOUTNAME", "pPRINTOUTNAME", 1);
                registerPrimitive("PRINTOUTPLIST", "pPRINTOUTPLIST", 1);
                registerPrimitive("PRINTOUTPROCEDURE", "pPRINTOUTPROCEDURE", 1);
                registerPrimitive("PROCEDURE?", "pPROCEDUREP", 1);
                registerPrimitive("PROCEDUREP", "pPROCEDUREP", 1);
                registerPrimitive("PROCEDURES", "pPROCEDURES", 0);
                registerPrimitive("PRODUCT", "pPRODUCT", 2);
                registerPrimitive("QUOTIENT", "pQUOTIENT", 2);
                registerPrimitive("RADARCCOS", "pRADARCCOS", 1);
                registerPrimitive("RADARCSIN", "pRADARCSIN", 1);
                registerPrimitive("RADARCTAN", "pRADARCTAN", 1);
                registerPrimitive("RADARCTAN2", "pRADARCTAN2", 2);
                registerPrimitive("RADCOS", "pRADCOS", 1);
                registerPrimitive("RADSIN", "pRADSIN", 1);
                registerPrimitive("RADTAN", "pRADTAN", 1);
                registerPrimitive("RANDOM", "pRANDOM", 1);
                registerPrimitive("RANDOMIZE", "pRANDOMIZE", 0);
                registerPrimitive("RC", "pREADCHARACTER", 0);
                registerPrimitive("READCHARACTER", "pREADCHARACTER", 0);
                registerPrimitive("READER", "pREADER", 0);
                registerPrimitive("READLIST", "pREADLIST", 0);
                registerPrimitive("READWORD", "pREADWORD", 0);
                registerPrimitive("REMAINDER", "pREMAINDER", 2);
                registerPrimitive("REMPROP", "pREMPROP", 2);
                registerPrimitive("REPCOUNT", "pREPCOUNT", 0);
                registerPrimitive("REPEAT", "pREPEAT", 2);
                registerPrimitive("RERANDOM", "pRANDOMIZE", 0);
                registerPrimitive("RL", "pREADLIST", 0);
                registerPrimitive("ROUND", "pROUND", 1);
                registerPrimitive("RUN", "pRUN", 1);
                registerPrimitive("RUNRESULT", "pRUNRESULT", 1);
                registerPrimitive("RW", "pREADWORD", 0);
                registerPrimitive("SE", "pSENTENCE", 2);
                registerPrimitive("SENTENCE", "pSENTENCE", 2);
                registerPrimitive("SETREAD", "pSETREAD", 1);
                registerPrimitive("SETSTREAMPOS", "pSETSTREAMPOS", 2);
                registerPrimitive("SETWRITE", "pSETWRITE", 1);
                registerPrimitive("SHOW", "pSHOW", 1);
                registerPrimitive("SHOW1", "pSHOW1", 1);
                registerPrimitive("SIN", "pSIN", 1);
                registerPrimitive("SQRT", "pSQRT", 1);
                registerPrimitive("STOP", "pSTOP", 0);
                registerPrimitive("STREAMRANDOM?", "pSTREAMRANDOMP", 1);
                registerPrimitive("STREAMRANDOMP", "pSTREAMRANDOMP", 1);
                registerPrimitive("STREAMKIND", "pSTREAMKIND", 1);
                registerPrimitive("STREAMLENGTH", "pSTREAMLENGTH", 1);
                registerPrimitive("STREAMNAME", "pSTREAMNAME", 1);
                registerPrimitive("STREAMPOS", "pSTREAMPOS", 1);
                registerPrimitive("SUM", "pSUM", 2);
                registerPrimitive("TAN", "pTAN", 1);
                registerPrimitive("TEST", "pTEST", 1);
                registerPrimitive("TEXT", "pTEXT", 1);
                registerPrimitive("THING", "pTHING", 1);
                registerPrimitive("THROW", "pTHROW", 1);
                registerPrimitive("TOPLEVEL", "pTOPLEVEL", 0);
                registerPrimitive("TRUE", "pTRUE", 0);
                registerPrimitive("TRY", "pTRY", 3);
                registerPrimitive("TYPE", "pPRINT1", 1);
                registerPrimitive("UPPERCASE", "pUPPERCASE", 1);
                registerPrimitive("WAIT", "pWAIT", 1);
                registerPrimitive("WORD", "pWORD", 2);
                registerPrimitive("WORD?", "pWORDP", 1);
                registerPrimitive("WORDP", "pWORDP", 1);
                registerPrimitive("WRITER", "pWRITER", 0);
                
                console.putStatusMessage("Turtle Tracks standard primitives v1.0");
        }


        /**
         * Primitive implementation for ABS
         */
        public final LogoObject pABS(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                return new LogoWord(Math.abs(params[0].toNumber()));
        }


        /**
         * Primitive implementation for ALLOF
         */
        public final LogoObject pALLOF(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                boolean curVal = true;
                int i;
                for (i=0; i<params.length && curVal; i++)
                {
                        curVal = params[i].toBoolean() && curVal;
                }
                return new LogoWord(curVal);
        }


        /**
         * Primitive implementation for ALLOPEN
         */
        public final LogoObject pALLOPEN(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 0);
                return interp.mach().getFileidList();
        }


        /**
         * Primitive implementation for ANYOF
         */
        public final LogoObject pANYOF(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                boolean curVal = false;
                int i;
                for (i=0; i<params.length && !curVal; i++)
                {
                        curVal = params[i].toBoolean() || curVal;
                }
                return new LogoWord(curVal);
        }


        /**
         * Primitive implementation for APPLY
         */
        public final LogoObject pAPPLY(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException,
                ThrowException
        {
                testNumParams(params, 2);
                if (!(params[1] instanceof LogoList))
                {
                        throw new LanguageException("Argument list expected");
                }
                if (params[0] instanceof LogoWord)
                {
                        return applyProc(interp, params[0].toCaselessString(), (LogoList)(params[1]));
                }
                else
                {
                        return applyAnonymous(interp, (LogoList)(params[0]), (LogoList)(params[1]), true);
                }
        }


        /**
         * Primitive implementation for APPLYRESULT
         */
        public final LogoObject pAPPLYRESULT(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException,
                ThrowException
        {
                testNumParams(params, 2);
                if (!(params[1] instanceof LogoList))
                {
                        throw new LanguageException("Argument list expected");
                }
                LogoObject obj;
                if (params[0] instanceof LogoWord)
                {
                        obj = applyProc(interp, params[0].toCaselessString(), (LogoList)(params[1]));
                }
                else
                {
                        obj = applyAnonymous(interp, (LogoList)(params[0]), (LogoList)(params[1]), true);
                }
                if (obj == LogoVoid.obj)
                {
                        return new LogoList();
                }
                else
                {
                        LogoObject[] arr = new LogoObject[1];
                        arr[0] = obj;
                        return new LogoList(arr);
                }
        }


        /**
         * Primitive implementation for ARCCOS
         */
        public final LogoObject pARCCOS(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                double val = params[0].toNumber();
                if (val < -1.0 || val > 1.0)
                {
                        throw new LanguageException("Value out of range");
                }
                return new LogoWord(Math.acos(val) * _radToDeg);
        }


        /**
         * Primitive implementation for ARCSIN
         */
        public final LogoObject pARCSIN(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                double val = params[0].toNumber();
                if (val < -1.0 || val > 1.0)
                {
                        throw new LanguageException("Value out of range");
                }
                return new LogoWord(Math.asin(val) * _radToDeg);
        }


        /**
         * Primitive implementation for ARCTAN
         */
        public final LogoObject pARCTAN(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                return new LogoWord(Math.atan(params[0].toNumber()) * _radToDeg);
        }


        /**
         * Primitive implementation for ARCTAN2
         */
        public final LogoObject pARCTAN2(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 2);
                return new LogoWord(Math.atan2(params[0].toNumber(), params[1].toNumber()) * _radToDeg);
        }


        /**
         * Primitive implementation for ASCII
         */
        public final LogoObject pASCII(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                if (!(params[0] instanceof LogoWord) || params[0].length() != 1)
                {
                        throw new LanguageException("Character expected");
                }
                return new LogoWord((double)((int)(params[0].toString().charAt(0))));
        }


        /**
         * Primitive implementation for ASHIFT
         */
        public final LogoObject pASHIFT(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 2);
                int num1 = params[0].toInteger();
                int num2 = params[1].toInteger();
                if (num2 > 0)
                {
                        return new LogoWord(num1 << num2);
                }
                else if (num2 < 0)
                {
                        return new LogoWord(num1 >> -num2);
                }
                else
                {
                        return params[0];
                }
        }


        /**
         * Primitive implementation for BEFORE?
         */
        public final LogoObject pBEFOREP(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 2);
                if (!(params[0] instanceof LogoWord) || !(params[1] instanceof LogoWord))
                {
                        throw new LanguageException("Words expected");
                }
                return new LogoWord(CaselessString.staticCompare(params[0].toString(), params[1].toString()) < 0);
        }


        /**
         * Primitive implementation for BITAND
         */
        public final LogoObject pBITAND(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                int i;
                int ret = 0xFFFFFFFF;
                for (i=0; i<params.length; i++)
                {
                        ret &= params[i].toInteger();
                }
                return new LogoWord(ret);
        }


        /**
         * Primitive implementation for BITNOT
         */
        public final LogoObject pBITNOT(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                return new LogoWord(~(params[0].toInteger()));
        }


        /**
         * Primitive implementation for BITOR
         */
        public final LogoObject pBITOR(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                int i;
                int ret = 0;
                for (i=0; i<params.length; i++)
                {
                        ret |= params[i].toInteger();
                }
                return new LogoWord(ret);
        }


        /**
         * Primitive implementation for BITXOR
         */
        public final LogoObject pBITXOR(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                int i;
                int ret = 0;
                for (i=0; i<params.length; i++)
                {
                        ret ^= params[i].toInteger();
                }
                return new LogoWord(ret);
        }


        /**
         * Primitive implementation for BUTFIRST
         */
        public final LogoObject pBUTFIRST(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                return params[0].butFirst();
        }


        /**
         * Primitive implementation for BUTLAST
         */
        public final LogoObject pBUTLAST(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                return params[0].butLast();
        }


        /**
         * Primitive implementation for CATCH
         */
        public final LogoObject pCATCH(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException,
                ThrowException
        {
                testNumParams(params, 2);
                try
                {
                        return params[1].getRunnable(interp.mach()).execute(interp);
                }
                catch (ThrowException e)
                {
                        LogoObject tagObj = new LogoWord(e.getTag());
                        if (params[0] instanceof LogoWord)
                        {
                                if (params[0].equals(tagObj))
                                {
                                        return e.getObj();
                                }
                        }
                        else
                        {
                                if (params[0].length() == 0 || ((LogoList)(params[0])).isMember(tagObj))
                                {
                                        return e.getObj();
                                }
                        }
                        throw e;
                }
                catch (LanguageException e)
                {
                        LogoObject tagObj = new LogoWord("ERROR");
                        if (params[0] instanceof LogoWord)
                        {
                                if (params[0].equals(tagObj))
                                {
                                        return new Tokenizer(0).tokenize(e.getMessage());
                                }
                        }
                        else
                        {
                                if (params[0].length() == 0 || ((LogoList)(params[0])).isMember(tagObj))
                                {
                                        return new Tokenizer(0).tokenize(e.getMessage());
                                }
                        }
                        throw e;
                }
        }


        /**
         * Primitive implementation for CHAR
         */
        public final LogoObject pCHAR(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                int val = params[0].toInteger();
                if (val < 0 || val > 255)
                {
                        throw new LanguageException("Value out of range");
                }
                char[] ca = new char[1];
                ca[0] = (char)val;
                return new LogoWord(new String(ca));
        }


        /**
         * Primitive implementation for CLOSE
         */
        public final LogoObject pCLOSE(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                if (!(params[0] instanceof LogoWord))
                {
                        throw new LanguageException("File id expected");
                }
                interp.mach().closeIO(params[0].toCaselessString());
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for CONTENTS
         */
        public final LogoObject pCONTENTS(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 0);
                LogoObject[] a = new LogoObject[3];
                a[0] = interp.mach().getProcList();
                a[1] = interp.mach().getNameList();
                a[2] = interp.mach().getPLists();
                return new LogoList(a);
        }


        /**
         * Primitive implementation for COS
         */
        public final LogoObject pCOS(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                return new LogoWord(Math.cos(params[0].toNumber() * _degToRad));
        }


        /**
         * Primitive implementation for DEFINE
         */
        public final LogoObject pDEFINE(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 2);
                if (!(params[0] instanceof LogoWord))
                {
                        throw new LanguageException("Procedure name expected");
                }
                CaselessString name = params[0].toCaselessString();
                Procedure.checkName(name.str);
                if (!interp.mach().isOverridePrimitives() && interp.mach().isReserved(name))
                {
                        throw new LanguageException("The name " + name + " is a primitive");
                }
                if (!(params[1] instanceof LogoList) || params[1].length() < 2)
                {
                        throw new LanguageException("Lambda list expected");
                }
                LogoList defn = (LogoList)params[1];
                if (!(defn.pickInPlace(0) instanceof LogoList))
                {
                        throw new LanguageException("First element in lambda list must be a parameter list");
                }
                Procedure.checkParamNames((LogoList)(defn.pickInPlace(0)));
                if (defn.pickInPlace(1) instanceof LogoList)
                {
                        if (params[1].length() > 2)
                        {
                                throw new LanguageException("Too many elements in lambda list");
                        }
                        interp.mach().defineProc(new Procedure(interp.mach(), name,
                                (LogoList)(defn.pickInPlace(0)), (LogoList)(defn.pickInPlace(1)), false));
                }
                else
                {
                        interp.mach().defineProc(new Procedure(interp.mach(), name,
                                (LogoList)(defn.pickInPlace(0)), (LogoList)(defn.butFirst()), true));
                }
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for DEFINEDP
         */
        public final LogoObject pDEFINEDP(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                return new LogoWord(interp.mach().resolveProc(params[0].toCaselessString()) != null);
        }


        /**
         * Primitive implementation for DIFFERENCE
         */
        public final LogoObject pDIFFERENCE(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 2);
                return new LogoWord(params[0].toNumber() - params[1].toNumber());
        }


        /**
         * Implementation for EDIT
         */
        public final LogoObject pEDIT(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 0, 1);
                if (params.length == 0)
                {
                        interp.mach().console().createEditor("");
                }
                else
                {
                        if (params[0] instanceof LogoWord)
                        {
                                CaselessString name = params[0].toCaselessString();
                                Procedure proc = interp.mach().resolveProc(name);
                                if (proc != null)
                                {
                                        interp.mach().console().createEditor(proc.toString());
                                }
                                else
                                {
                                        Procedure.checkName(name.str);
                                        interp.mach().console().createEditor("TO " + name.str + Machine.LINE_SEPARATOR);
                                }
                        }
                        else
                        {
                                if (params[0].length() != 3)
                                {
                                        throw new LanguageException("Contents list expected");
                                }
                                StringWriter buf = new StringWriter();
                                IOStream writer = new IOStream(new BufferedWriter(buf));
                                interp.mach().printout(writer, ((LogoList)(params[0])).pickInPlace(0),
                                        ((LogoList)(params[0])).pickInPlace(1), ((LogoList)(params[0])).pickInPlace(2));
                                writer.close();
                                interp.mach().console().createEditor(buf.toString());
                        }
                }
                return LogoVoid.obj;
        }


        /**
         * Implementation for EMPTYP
         */
        public final LogoObject pEMPTYP(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                return new LogoWord(params[0].length() == 0);
        }


        /**
         * Primitive implementation for EOFP
         */
        public final LogoObject pEOFP(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 0);
                return new LogoWord(interp.thread().inStream().eof());
        }


        /**
         * Primitive implementation for EQUALP
         */
        public final LogoObject pEQUALP(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 2);
                return new LogoWord(params[0].equals(params[1]));
        }


        /**
         * Primitive implementation for ERASE
         */
        public final LogoObject pERASE(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                if (!(params[0] instanceof LogoList) || params[0].length() != 3)
                {
                        throw new LanguageException("Contents list expected");
                }
                eraseProcs(interp, ((LogoList)(params[0])).pickInPlace(0));
                eraseNames(interp, ((LogoList)(params[0])).pickInPlace(1));
                erasePLists(interp, ((LogoList)(params[0])).pickInPlace(2));
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for ERASENAME
         */
        public final LogoObject pERASENAME(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                eraseNames(interp, params[0]);
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for ERASEPROCEDURE
         */
        public final LogoObject pERASEPROCEDURE(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                eraseProcs(interp, params[0]);
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for ERASEPLIST
         */
        public final LogoObject pERASEPLIST(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                erasePLists(interp, params[0]);
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for EXP
         */
        public final LogoObject pEXP(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                return new LogoWord(Math.exp(params[0].toNumber()));
        }


        /**
         * Primitive implementation for FALSE
         */
        public final LogoObject pFALSE(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 0);
                return new LogoWord(false);
        }


        /**
         * Primitive implementation for FIRST
         */
        public final LogoObject pFIRST(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                return params[0].first();
        }


        /**
         * Primitive implementation for FPUT
         */
        public final LogoObject pFPUT(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 2);
                if (!(params[1] instanceof LogoList))
                {
                        throw new LanguageException("List expected for inserting into");
                }
                return ((LogoList)(params[1])).fput(params[0]);
        }


        /**
         * Primitive implementation for GC
         */
        public final LogoObject pGC(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 0);
                System.gc();
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for GOODBYE
         */
        public final LogoObject pGOODBYE(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException,
                ThrowException
        {
                testNumParams(params, 0);
                throw new ThrowException("GOODBYE");
        }


        /**
         * Primitive implementation for GPROP
         */
        public final LogoObject pGPROP(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 2);
                if (!(params[0] instanceof LogoWord))
                {
                        throw new LanguageException("Property list name expected");
                }
                if (!(params[1] instanceof LogoWord))
                {
                        throw new LanguageException("Property key expected");
                }
                return interp.mach().getProp(params[0].toCaselessString(), params[1].toCaselessString());
        }


        /**
         * Primitive implementation for GREATERP
         */
        public final LogoObject pGREATERP(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 2);
                return new LogoWord(params[0].toNumber() > params[1].toNumber());
        }


        /**
         * Primitive implementation for GREATEREQUALP
         */
        public final LogoObject pGREATEREQUALP(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 2);
                return new LogoWord(params[0].toNumber() >= params[1].toNumber());
        }


        /**
         * Primitive implementation for IF
         */
        public final LogoObject pIF(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException,
                ThrowException
        {
                testNumParams(params, 2, 3);
                if (params[0].toBoolean())
                {
                        return params[1].getRunnable(interp.mach()).execute(interp);
                }
                else if (params.length == 3)
                {
                        return params[2].getRunnable(interp.mach()).execute(interp);
                }
                else
                {
                        return LogoVoid.obj;
                }
        }


        /**
         * Primitive implementation for IFELSE
         */
        public final LogoObject pIFELSE(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException,
                ThrowException
        {
                testNumParams(params, 3);
                if (params[0].toBoolean())
                {
                        return params[1].getRunnable(interp.mach()).execute(interp);
                }
                else
                {
                        return params[2].getRunnable(interp.mach()).execute(interp);
                }
        }


        /**
         * Primitive implementation for IFFALSE
         */
        public final LogoObject pIFFALSE(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException,
                ThrowException
        {
                testNumParams(params, 1);
                if (!(interp.getTestResult()))
                {
                        return params[0].getRunnable(interp.mach()).execute(interp);
                }
                else
                {
                        return LogoVoid.obj;
                }
        }


        /**
         * Primitive implementation for IFTRUE
         */
        public final LogoObject pIFTRUE(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException,
                ThrowException
        {
                testNumParams(params, 1);
                if (interp.getTestResult())
                {
                        return params[0].getRunnable(interp.mach()).execute(interp);
                }
                else
                {
                        return LogoVoid.obj;
                }
        }


        /**
         * Primitive implementation for IGNORE
         */
        public final LogoObject pIGNORE(
                InterpEnviron interp,
                LogoObject[] params)
        {
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for INTEGER
         */
        public final LogoObject pINTEGER(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                double val = params[0].toNumber();
                if (val < 0)
                {
                        return new LogoWord(Math.ceil(val));
                }
                else
                {
                        return new LogoWord(Math.floor(val));
                }
        }


        /**
         * Primitive implementation for INVOKE
         */
        public final LogoObject pINVOKE(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException,
                ThrowException
        {
                testMinParams(params, 1);
                LogoObject[] arr = new LogoObject[params.length-1];
                for (int i=1; i<params.length; i++)
                {
                        arr[i-1] = params[i];
                }
                if (params[0] instanceof LogoWord)
                {
                        return applyProc(interp, params[0].toCaselessString(), new LogoList(arr));
                }
                else
                {
                        return applyAnonymous(interp, (LogoList)(params[0]), new LogoList(arr), true);
                }
        }


        /**
         * Primitive implementation for INVOKERESULT
         */
        public final LogoObject pINVOKERESULT(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException,
                ThrowException
        {
                testMinParams(params, 1);
                LogoObject[] arr = new LogoObject[params.length-1];
                for (int i=1; i<params.length; i++)
                {
                        arr[i-1] = params[i];
                }
                LogoObject obj;
                if (params[0] instanceof LogoWord)
                {
                        obj = applyProc(interp, params[0].toCaselessString(), new LogoList(arr));
                }
                else
                {
                        obj = applyAnonymous(interp, (LogoList)(params[0]), new LogoList(arr), true);
                }
                if (obj == LogoVoid.obj)
                {
                        return new LogoList();
                }
                else
                {
                        arr = new LogoObject[1];
                        arr[0] = obj;
                        return new LogoList(arr);
                }
        }


        /**
         * Primitive implementation for ITEM
         */
        public final LogoObject pITEM(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 2);
                int val = params[0].toInteger();
                return (params[1].pick(val));
        }


        /**
         * Primitive implementation for KEYP
         */
        public final LogoObject pKEYP(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 0);
                return new LogoWord(interp.thread().inStream().charAvail());
        }


        /**
         * Primitive implementation for LAST
         */
        public final LogoObject pLAST(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                return params[0].last();
        }


        /**
         * Primitive implementation for LENGTH
         */
        public final LogoObject pLENGTH(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException,
                ThrowException
        {
                testNumParams(params, 1);
                return new LogoWord(params[0].length());
        }


        /**
         * Primitive implementation for LESSP
         */
        public final LogoObject pLESSP(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 2);
                return new LogoWord(params[0].toNumber() < params[1].toNumber());
        }


        /**
         * Primitive implementation for LESSEQUALP
         */
        public final LogoObject pLESSEQUALP(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 2);
                return new LogoWord(params[0].toNumber() <= params[1].toNumber());
        }


        /**
         * Primitive implementation for LIST
         */
        public final LogoObject pLIST(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                LogoObject[] a = new LogoObject[params.length];
                int i;
                for (i=0; i<params.length; i++)
                {
                        a[i] = params[i];
                }
                return new LogoList(a);
        }


        /**
         * Primitive implementation for LISTP
         */
        public final LogoObject pLISTP(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                return new LogoWord(params[0] instanceof LogoList);
        }


        /**
         * Primitive implementation for LOCAL
         */
        public final LogoObject pLOCAL(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testMinParams(params, 1);
                for (int i=0; i<params.length; i++)
                {
                        if (params[i] instanceof LogoWord)
                        {
                                interp.thread().localName(params[i].toCaselessString());
                        }
                        else
                        {
                                for (int j=0; j<params[i].length(); j++)
                                {
                                        LogoObject obj = ((LogoList)(params[i])).pickInPlace(j);
                                        if (!(obj instanceof LogoWord))
                                        {
                                                throw new LanguageException("Elements of list must be names");
                                        }
                                        interp.thread().localName(obj.toCaselessString());
                                }
                        }
                }
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for LOG
         */
        public final LogoObject pLOG(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                double val = params[0].toNumber();
                if (val <= 0.0)
                {
                        throw new LanguageException("Value must be positive");
                }
                return new LogoWord(Math.log(val));
        }


        /**
         * Primitive implementation for LOWERCASE
         */
        public final LogoObject pLOWERCASE(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                if (!(params[0] instanceof LogoWord))
                {
                        throw new LanguageException("Word expected");
                }
                return new LogoWord(params[0].toString().toLowerCase());
        }


        /**
         * Primitive implementation for LPUT
         */
        public final LogoObject pLPUT(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 2);
                if (!(params[1] instanceof LogoList))
                {
                        throw new LanguageException("List expected for inserting into");
                }
                return ((LogoList)(params[1])).lput(params[0]);
        }


        /**
         * Primitive implementation for LSHIFT
         */
        public final LogoObject pLSHIFT(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 2);
                int num1 = params[0].toInteger();
                int num2 = params[1].toInteger();
                if (num2 > 0)
                {
                        return new LogoWord(num1 << num2);
                }
                else if (num2 < 0)
                {
                        return new LogoWord(num1 >>> -num2);
                }
                else
                {
                        return params[0];
                }
        }


        /**
         * Primitive implementation for MACROP
         */
        public final LogoObject pMACROP(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                Procedure proc = interp.mach().resolveProc(params[0].toCaselessString());
                if (proc == null)
                {
                        return new LogoWord(false);
                }
                else
                {
                        return new LogoWord(proc.isMacro());
                }
        }


        /**
         * Primitive implementation for MAKE
         */
        public final LogoObject pMAKE(
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
                interp.thread().makeName(params[0].toCaselessString(), params[1]);
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for MEMBERP
         */
        public final LogoObject pMEMBERP(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 2);
                if (!(params[1] instanceof LogoList))
                {
                        throw new LanguageException("List expected");
                }
                return new LogoWord(((LogoList)(params[1])).isMember(params[0]));
        }


        /**
         * Primitive implementation for MINUS
         */
        public final LogoObject pMINUS(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                return new LogoWord(-params[0].toNumber());
        }


        /**
         * Primitive implementation for NAMEP
         */
        public final LogoObject pNAMEP(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                if (!(params[0] instanceof LogoWord))
                {
                        throw new LanguageException("Name expected");
                }
                CaselessString name = params[0].toCaselessString();
                LogoObject ret = interp.thread().resolveName(name);
                if (ret == null)
                {
                        return new LogoWord(false);
                }
                else
                {
                        return new LogoWord(true);
                }
        }


        /**
         * Primitive implementation for NAMES
         */
        public final LogoObject pNAMES(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 0);
                LogoObject[] a = new LogoObject[3];
                a[0] = new LogoList();
                a[1] = interp.mach().getNameList();
                a[2] = new LogoList();
                return new LogoList(a);
        }


        /**
         * Primitive implementation for NOT
         */
        public final LogoObject pNOT(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                return new LogoWord(!(params[0].toBoolean()));
        }


        /**
         * Primitive implementation for NUMBERP
         */
        public final LogoObject pNUMBERP(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                try
                {
                        /*double val = */params[0].toNumber();
                        return new LogoWord(true);
                }
                catch (LanguageException e)
                {
                        return new LogoWord(false);
                }
        }


        /**
         * Primitive implementation for OUTPUT
         */
        public final LogoObject pOUTPUT(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException,
                ThrowException
        {
                testNumParams(params, 1);
                throw new ThrowException("STOP", params[0]);
        }


        /**
         * Primitive implementation for PARSE
         */
        public final LogoObject pPARSE(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                if (!(params[0] instanceof LogoWord))
                {
                        throw new LanguageException("Word expected");
                }
                return new Tokenizer(0).tokenize(params[0].toString());
        }


        /**
         * Primitive implementation for PI
         */
        public final LogoObject pPI(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 0);
                return new LogoWord(Math.PI);
        }


        /**
         * Primitive implementation for PLISTS
         */
        public final LogoObject pPLISTS(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 0);
                LogoObject[] a = new LogoObject[3];
                a[0] = new LogoList();
                a[1] = new LogoList();
                a[2] = interp.mach().getPLists();
                return new LogoList(a);
        }


        /**
         * Primitive implementation for POWER
         */
        public final LogoObject pPOWER(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 2);
                try
                {
                        double op1 = params[0].toNumber();
                        double op2 = params[1].toNumber();
                        if ((op1 == 0 && op2 == 0) ||
                                op1 < 0 && op2 != (int)op2)
                        {
                                throw new LanguageException("Mathematically invalid exponent");
                        }
                        return new LogoWord(Math.pow(params[0].toNumber(), params[1].toNumber()));
                }
                catch (ArithmeticException e)
                {
                        throw new LanguageException(e.toString());
                }
        }


        /**
         * Primitive implementation for PPROP
         */
        public final LogoObject pPPROP(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 3);
                if (!(params[0] instanceof LogoWord))
                {
                        throw new LanguageException("Property list name expected");
                }
                CaselessString name = params[0].toCaselessString();
                if (!(params[1] instanceof LogoWord))
                {
                        throw new LanguageException("Property key expected");
                }
                CaselessString key = params[1].toCaselessString();
                interp.mach().putProp(name, key, params[2]);
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for PRIMITIVEP
         */
        public final LogoObject pPRIMITIVEP(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                return new LogoWord(interp.mach().isReserved(params[0].toCaselessString()));
        }


        /**
         * Primitive implementation for PRINT
         */
        public final LogoObject pPRINT(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                StringBuffer sb = new StringBuffer();
                for (int i=0; i<params.length; i++)
                {
                        if (params[i] instanceof LogoList)
                        {
                                sb.append(((LogoList)(params[i])).toStringOpen());
                        }
                        else
                        {
                                sb.append(((LogoWord)(params[i])).toString());
                        }
                        if (i < params.length-1)
                        {
                                sb.append(' ');
                        }
                }
                interp.thread().outStream().putLine(sb.toString());
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for PRINT1
         */
        public final LogoObject pPRINT1(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                StringBuffer sb = new StringBuffer();
                for (int i=0; i<params.length; i++)
                {
                        if (params[i] instanceof LogoList)
                        {
                                sb.append(((LogoList)(params[i])).toStringOpen());
                        }
                        else
                        {
                                sb.append(((LogoWord)(params[i])).toString());
                        }
                }
                interp.thread().outStream().put(sb.toString());
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for PRINTOUT
         */
        public final LogoObject pPRINTOUT(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                if (!(params[0] instanceof LogoList) || params[0].length() != 3)
                {
                        throw new LanguageException("Contents list expected");
                }
                interp.mach().printout(interp.thread().outStream(), ((LogoList)(params[0])).pickInPlace(0),
                        ((LogoList)(params[0])).pickInPlace(1), ((LogoList)(params[0])).pickInPlace(2));
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for PRINTOUTNAME
         */
        public final LogoObject pPRINTOUTNAME(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                interp.mach().printout(interp.thread().outStream(), LogoVoid.obj, params[0], LogoVoid.obj);
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for PRINTOUTPROCEDURE
         */
        public final LogoObject pPRINTOUTPROCEDURE(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                interp.mach().printout(interp.thread().outStream(), params[0], LogoVoid.obj, LogoVoid.obj);
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for PRINTOUTPLIST
         */
        public final LogoObject pPRINTOUTPLIST(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                interp.mach().printout(interp.thread().outStream(), LogoVoid.obj, LogoVoid.obj, params[0]);
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for PROCEDUREP
         */
        public final LogoObject pPROCEDUREP(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                if (interp.mach().isReserved(params[0].toCaselessString()))
                        return new LogoWord(true);
                if (interp.mach().resolveProc(params[0].toCaselessString()) != null)
                        return new LogoWord(true);
                return new LogoWord(false);
        }


        /**
         * Primitive implementation for PROCEDURES
         */
        public final LogoObject pPROCEDURES(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 0);
                LogoObject[] a = new LogoObject[3];
                a[0] = interp.mach().getProcList();
                a[1] = new LogoList();
                a[2] = new LogoList();
                return new LogoList(a);
        }


        /**
         * Primitive implementation for PRODUCT
         */
        public final LogoObject pPRODUCT(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                double ret = 1.0;
                for (int i=0; i<params.length; i++)
                {
                        ret *= params[i].toNumber();
                }
                return new LogoWord(ret);
        }


        /**
         * Primitive implementation for QUOTIENT
         */
        public final LogoObject pQUOTIENT(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 2);
                double val2 = params[1].toNumber();
                if (val2 == 0)
                {
                        throw new LanguageException("Division by zero");
                }
                return new LogoWord(params[0].toNumber() / val2);
        }


        /**
         * Primitive implementation for RADARCCOS
         */
        public final LogoObject pRADARCCOS(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                double val = params[0].toNumber();
                if (val < -1.0 || val > 1.0)
                {
                        throw new LanguageException("Value out of range");
                }
                return new LogoWord(Math.acos(val));
        }


        /**
         * Primitive implementation for RADARCSIN
         */
        public final LogoObject pRADARCSIN(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                double val = params[0].toNumber();
                if (val < -1.0 || val > 1.0)
                {
                        throw new LanguageException("Value out of range");
                }
                return new LogoWord(Math.asin(val));
        }


        /**
         * Primitive implementation for RADARCTAN
         */
        public final LogoObject pRADARCTAN(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                return new LogoWord(Math.atan(params[0].toNumber()));
        }


        /**
         * Primitive implementation for RADARCTAN2
         */
        public final LogoObject pRADARCTAN2(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 2);
                return new LogoWord(Math.atan2(params[0].toNumber(), params[1].toNumber()));
        }


        /**
         * Primitive implementation for RADCOS
         */
        public final LogoObject pRADCOS(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                return new LogoWord(Math.cos(params[0].toNumber()));
        }


        /**
         * Primitive implementation for RADSIN
         */
        public final LogoObject pRADSIN(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                return new LogoWord(Math.sin(params[0].toNumber()));
        }


        /**
         * Primitive implementation for RADTAN
         */
        public final LogoObject pRADTAN(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                return new LogoWord(Math.tan(params[0].toNumber()));
        }


        /**
         * Primitive implementation for RANDOM
         */
        public final LogoObject pRANDOM(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                double val = interp.mach().random().nextDouble();
                return new LogoWord(Math.floor(val*(double)(params[0].toInteger())));
        }


        /**
         * Primitive implementation for RANDOMIZE
         */
        public final LogoObject pRANDOMIZE(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 0, 1);
                long seed;
                if (params.length == 1)
                {
                        seed = (long)(params[0].toInteger());
                }
                else
                {
                        seed = System.currentTimeMillis();
                }
                interp.mach().random().setSeed(seed);
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for READCHARACTER
         */
        public final LogoObject pREADCHARACTER(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 0);
                return new LogoWord(new CaselessString(interp.thread().inStream().getChar()));
        }


        /**
         * Primitive implementation for READER
         */
        public final LogoObject pREADER(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 0);
                CaselessString fileid = interp.thread().inStreamID();
                if (fileid == null)
                {
                        return new LogoWord(Machine.CONSOLE_STREAM_NAME);
                }
                else
                {
                        return new LogoWord(fileid);
                }
        }


        /**
         * Primitive implementation for READLIST
         */
        public final LogoObject pREADLIST(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 0);
                Console console = null;
                if (interp.thread().inStream() instanceof Console)
                {
                        console = (Console)(interp.thread().inStream());
                }
                StringBuffer cumulative = new StringBuffer();
                char promptChar = '\0';
                Tokenizer tokenizer = new Tokenizer(0);
                while (true)
                {
                        try
                        {
                                if (console != null && promptChar != '\0')
                                {
                                        cumulative.append(console.promptGetLine(promptChar));
                                }
                                else
                                {
                                        cumulative.append(interp.thread().inStream().getLine());
                                }
                                if (cumulative.length() == 0)
                                {
                                        return new LogoList();
                                }
                                if (cumulative.charAt(cumulative.length()-1) == '~')
                                {
                                        cumulative.setCharAt(cumulative.length()-1, ' ');
                                        promptChar = '~';
                                }
                                else
                                {
                                        return tokenizer.tokenize(cumulative.toString());
                                }
                        }
                        catch (LanguageException e)
                        {
                                promptChar = e.getContChar();
                                if (promptChar == '|' || promptChar == '\\')
                                {
                                        cumulative.append(Machine.LINE_SEPARATOR);
                                }
                                else if (promptChar == '~')
                                {
                                        cumulative.append(' ');
                                }
                                else
                                {
                                        throw e;
                                }
                        }
                }
        }


        /**
         * Primitive implementation for READWORD
         */
        public final LogoObject pREADWORD(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 0);
                return new LogoWord(interp.thread().inStream().getLine());
        }


        /**
         * Primitive implementation for REMAINDER
         */
        public final LogoObject pREMAINDER(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 2);
                int val1 = (int)Math.round(params[0].toNumber());
                int val2 = (int)Math.round(params[1].toNumber());
                if (val2 == 0)
                {
                        throw new LanguageException("Division by zero");
                }
                return new LogoWord(val1 % val2);
        }


        /**
         * Primitive implementation for REMPROP
         */
        public final LogoObject pREMPROP(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 2);
                if (!(params[0] instanceof LogoWord))
                {
                        throw new LanguageException("Property list name expected");
                }
                if (!(params[1] instanceof LogoWord))
                {
                        throw new LanguageException("Property key expected");
                }
                interp.mach().remProp(params[0].toCaselessString(), params[1].toCaselessString());
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for REPCOUNT
         */
        public final LogoObject pREPCOUNT(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 0);
                return new LogoWord(interp.getLoopIndex());
        }


        /**
         * Primitive implementation for REPEAT
         */
        public final LogoObject pREPEAT(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException,
                ThrowException
        {
                testNumParams(params, 2);
                int i;
                int max = params[0].toInteger();
                for (i=1; i<=max; i++)
                {
                        InterpEnviron interp2 = new InterpEnviron(interp.getTestState(), i, interp.thread());
                        LogoObject ret = LogoVoid.obj;
                        try
                        {
                                ret = params[1].getRunnable(interp.mach()).execute(interp2);
                        }
                        finally
                        {
                                interp.setTestState(interp2.getTestState());
                        }
                        if (!interp.mach().isAutoIgnore() && ret != LogoVoid.obj)
                        {
                                throw new LanguageException("You don't say what to do with " + ret.toString());
                        }
                }
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for ROUND
         */
        public final LogoObject pROUND(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                return new LogoWord(Math.round(params[0].toNumber()));
        }


        /**
         * Primitive implementation for RUN
         */
        public final LogoObject pRUN(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException,
                ThrowException
        {
                testNumParams(params, 1);
                return params[0].getRunnable(interp.mach()).execute(interp);
        }


        /**
         * Primitive implementation for RUNRESULT
         */
        public final LogoObject pRUNRESULT(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException,
                ThrowException
        {
                testNumParams(params, 1);
                LogoObject obj = params[0].getRunnable(interp.mach()).execute(interp);
                if (obj == LogoVoid.obj)
                {
                        return new LogoList();
                }
                else
                {
                        LogoObject[] arr = new LogoObject[1];
                        arr[0] = obj;
                        return new LogoList(arr);
                }
        }


        /**
         * Primitive implementation for SENTENCE
         */
        public final LogoObject pSENTENCE(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                int i;
                Vector v = new Vector();
                
                for (i=0; i<params.length; i++)
                {
                        int j;
                        
                        if (params[i] instanceof LogoWord)
                        {
                                v.addElement(params[i]);
                        }
                        else
                        {
                                for (j=0; j<params[i].length(); j++)
                                {
                                        v.addElement(((LogoList)(params[i])).pickInPlace(j));
                                }
                        }
                }
                return new LogoList(v);
        }


        /**
         * Primitive implementation for SETREAD
         */
        public final LogoObject pSETREAD(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                if (params[0] instanceof LogoList)
                {
                        if (params[0].length() == 0)
                        {
                                interp.thread().setInStream(null, interp.mach().console());
                                return LogoVoid.obj;
                        }
                }
                else
                {
                        CaselessString name = params[0].toCaselessString();
                        if (name.equals(Machine.CONSOLE_STREAM_NAME))
                        {
                                interp.thread().setInStream(null, interp.mach().console());
                        }
                        else
                        {
                                IOBase io = interp.mach().getIO(name);
                                if (io == null)
                                {
                                        throw new LanguageException("No such file");
                                }
                                interp.thread().setInStream(name, io);
                        }
                        return LogoVoid.obj;
                }
                throw new LanguageException("File id expected");
        }


        /**
         * Primitive implementation for SETSTREAMPOS
         */
        public final LogoObject pSETSTREAMPOS(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 2);
                if (params[0] instanceof LogoList && params[0].length() == 0)
                {
                        throw new LanguageException("Stream .CONSOLE is not random access");
                }
                if (params[0] instanceof LogoWord)
                {
                        IOBase io = interp.mach().getIO(params[0].toCaselessString());
                        if (io == null)
                        {
                                throw new LanguageException("Stream " + params[0].toString() + " is closed");
                        }
                        if (!(io instanceof IORandom))
                        {
                                throw new LanguageException("Stream " + params[0].toString() + " is not random access");
                        }
                        ((IORandom)io).seek(params[1].toInteger());
                        return LogoVoid.obj;
                }
                throw new LanguageException("Stream id expected");
        }


        /**
         * Primitive implementation for SETWRITE
         */
        public final LogoObject pSETWRITE(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                if (params[0] instanceof LogoList)
                {
                        if (params[0].length() == 0)
                        {
                                interp.thread().setOutStream(null, interp.mach().console());
                                return LogoVoid.obj;
                        }
                }
                else
                {
                        CaselessString name = params[0].toCaselessString();
                        if (name.equals(Machine.CONSOLE_STREAM_NAME))
                        {
                                interp.thread().setOutStream(null, interp.mach().console());
                        }
                        else
                        {
                                IOBase io = interp.mach().getIO(name);
                                if (io == null)
                                {
                                        throw new LanguageException("No such file");
                                }
                                interp.thread().setOutStream(name, io);
                        }
                        return LogoVoid.obj;
                }
                throw new LanguageException("File id expected");
        }


        /**
         * Primitive implementation for SHOW
         */
        public final LogoObject pSHOW(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                StringBuffer sb = new StringBuffer();
                for (int i=0; i<params.length; i++)
                {
                        sb.append(params[i].toString());
                        if (i < params.length-1)
                        {
                                sb.append(' ');
                        }
                }
                interp.thread().outStream().putLine(sb.toString());
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for SHOW1
         */
        public final LogoObject pSHOW1(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                StringBuffer sb = new StringBuffer();
                for (int i=0; i<params.length; i++)
                {
                        sb.append(params[i].toString());
                }
                interp.thread().outStream().put(sb.toString());
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for SIN
         */
        public final LogoObject pSIN(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                return new LogoWord(Math.sin(params[0].toNumber() * _degToRad));
        }


        /**
         * Primitive implementation for SQRT
         */
        public final LogoObject pSQRT(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                double val = params[0].toNumber();
                if (val < 0.0)
                {
                        throw new LanguageException("Value must be nonnegative");
                }
                return new LogoWord(Math.sqrt(val));
        }


        /**
         * Primitive implementation for STOP
         */
        public final LogoObject pSTOP(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException,
                ThrowException
        {
                testNumParams(params, 0);
                throw new ThrowException("STOP");
        }


        /**
         * Primitive implementation for STREAMRANDOM?
         */
        public final LogoObject pSTREAMRANDOMP(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                if (params[0] instanceof LogoList && params[0].length() == 0)
                {
                        return new LogoWord(false);
                }
                if (params[0] instanceof LogoWord)
                {
                        IOBase io = interp.mach().getIO(params[0].toCaselessString());
                        if (io == null)
                        {
                                throw new LanguageException("Stream " + params[0].toString() + " is closed");
                        }
                        return new LogoWord(io instanceof IORandom);
                }
                throw new LanguageException("Stream id expected");
        }


        /**
         * Primitive implementation for STREAMKIND
         */
        public final LogoObject pSTREAMKIND(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                if (params[0] instanceof LogoList)
                {
                        if (params[0].length() == 0)
                        {
                                return new LogoWord("CONSOLE");
                        }
                }
                else if (params[0] instanceof LogoWord)
                {
                        if (params[0].toCaselessString().equals(Machine.CONSOLE_STREAM_NAME))
                        {
                                return new LogoWord("CONSOLE");
                        }
                        IOBase io = interp.mach().getIO(params[0].toCaselessString());
                        if (io == null)
                        {
                                return new LogoWord("CLOSED");
                        }
                        else if (!io.isOpen())
                        {
                                return new LogoWord("CLOSED");
                        }
                        else
                        {
                                return io.kind();
                        }
                }
                throw new LanguageException("Stream id expected");
        }


        /**
         * Primitive implementation for STREAMLENGTH
         */
        public final LogoObject pSTREAMLENGTH(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                if (params[0] instanceof LogoList && params[0].length() == 0)
                {
                        throw new LanguageException("Stream .CONSOLE is not random access");
                }
                if (params[0] instanceof LogoWord)
                {
                        IOBase io = interp.mach().getIO(params[0].toCaselessString());
                        if (io == null)
                        {
                                throw new LanguageException("Stream " + params[0].toString() + " is closed");
                        }
                        if (!(io instanceof IORandom))
                        {
                                throw new LanguageException("Stream " + params[0].toString() + " is not random access");
                        }
                        return new LogoWord(((IORandom)io).length());
                }
                throw new LanguageException("Stream id expected");
        }


        /**
         * Primitive implementation for STREAMNAME
         */
        public final LogoObject pSTREAMNAME(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                if (params[0] instanceof LogoList)
                {
                        if (params[0].length() == 0)
                        {
                                return new LogoWord(interp.mach().console().getClass().getName());
                        }
                }
                else if (params[0] instanceof LogoWord)
                {
                        if (params[0].toCaselessString().equals(Machine.CONSOLE_STREAM_NAME))
                        {
                                return new LogoWord(interp.mach().console().getClass().getName());
                        }
                        IOBase io = interp.mach().getIO(params[0].toCaselessString());
                        if (io == null)
                        {
                                throw new LanguageException("Stream is closed");
                        }
                        return io.name();
                }
                throw new LanguageException("Stream id expected");
        }


        /**
         * Primitive implementation for STREAMPOS
         */
        public final LogoObject pSTREAMPOS(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                if (params[0] instanceof LogoList && params[0].length() == 0)
                {
                        throw new LanguageException("Stream .CONSOLE is not random access");
                }
                if (params[0] instanceof LogoWord)
                {
                        IOBase io = interp.mach().getIO(params[0].toCaselessString());
                        if (io == null)
                        {
                                throw new LanguageException("Stream " + params[0].toString() + " is closed");
                        }
                        if (!(io instanceof IORandom))
                        {
                                throw new LanguageException("Stream " + params[0].toString() + " is not random access");
                        }
                        return new LogoWord(((IORandom)io).tell());
                }
                throw new LanguageException("Stream id expected");
        }


        /**
         * Primitive implementation for SUM
         */
        public final LogoObject pSUM(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                double ret = 0.0;
                for (int i=0; i<params.length; i++)
                {
                        ret += params[i].toNumber();
                }
                return new LogoWord(ret);
        }


        /**
         * Primitive implementation for TAN
         */
        public final LogoObject pTAN(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                return new LogoWord(Math.tan(params[0].toNumber() * _degToRad));
        }


        /**
         * Primitive implementation for TEST
         */
        public final LogoObject pTEST(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                interp.setTestResult(params[0].toBoolean());
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for TEXT
         */
        public final LogoObject pTEXT(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                if (!(params[0] instanceof LogoWord))
                {
                        throw new LanguageException("Procedure name expected");
                }
                CaselessString name = params[0].toCaselessString();
                Procedure proc = interp.mach().resolveProc(name);
                if (proc == null)
                {
                        throw new LanguageException("I don't know how to " + name);
                }
                LogoObject[] a = new LogoObject[2];
                a[0] = proc.getParams();
                a[1] = proc.getCode();
                return new LogoList(a);
        }


        /**
         * Primitive implementation for THING
         */
        public final LogoObject pTHING(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                if (!(params[0] instanceof LogoWord))
                {
                        throw new LanguageException("Name expected");
                }
                CaselessString name = params[0].toCaselessString();
                LogoObject ret = interp.thread().resolveName(name);
                if (ret == null)
                {
                        throw new LanguageException(name.str + " has no value");
                }
                return ret;
        }


        /**
         * Primitive implementation for THROW
         */
        public final LogoObject pTHROW(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException,
                ThrowException
        {
                testNumParams(params, 1, 2);
                if (!(params[0] instanceof LogoWord))
                {
                        throw new LanguageException("Tag expected");
                }
                LogoObject obj2 = LogoVoid.obj;
                if (params.length == 2)
                {
                        obj2 = params[1];
                }
                if (params[0].toCaselessString().equals("ERROR"))
                {
                        if (obj2 instanceof LogoList)
                        {
                                throw new LanguageException(((LogoList)obj2).toStringOpen());
                        }
                        else if (obj2 instanceof LogoWord)
                        {
                                throw new LanguageException(obj2.toString());
                        }
                        else
                        {
                                throw new LanguageException("User error");
                        }
                }
                throw new ThrowException(params[0].toString(), obj2);
        }


        /**
         * Primitive implementation for TOPLEVEL
         */
        public final LogoObject pTOPLEVEL(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException,
                ThrowException
        {
                testNumParams(params, 0);
                throw new ThrowException("TOPLEVEL");
        }


        /**
         * Primitive implementation for TRUE
         */
        public final LogoObject pTRUE(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 0);
                return new LogoWord(true);
        }


        /**
         * Primitive implementation for TRY
         */
        public final LogoObject pTRY(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException,
                ThrowException
        {
                testMinParams(params, 1);
                if ((params.length % 2) == 0)
                {
                        throw new LanguageException("Even number of arguments");
                }
                LogoObject ret = LogoVoid.obj;
                try
                {
                        ret = params[0].getRunnable(interp.mach()).execute(interp);
                }
                catch (ThrowException e)
                {
                        ret = parseCatches(interp, params, e, null);
                }
                catch (LanguageException e)
                {
                        ret = parseCatches(interp, params, null, e);
                }
                return ret;
        }


        /**
         * Primitive implementation for UPPERCASE
         */
        public final LogoObject pUPPERCASE(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                if (!(params[0] instanceof LogoWord))
                {
                        throw new LanguageException("Word expected");
                }
                return new LogoWord(params[0].toString().toUpperCase());
        }


        /**
         * Primitive implementation for WAIT
         */
        public final LogoObject pWAIT(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                int time = params[0].toInteger();
                if (time < 0)
                {
                        throw new LanguageException("Time interval is negative");
                }
                try
                {
                        Thread.sleep(time);
                }
                catch (InterruptedException e) {}
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for WORD
         */
        public final LogoObject pWORD(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                StringBuffer sb = new StringBuffer();
                int i;
                for (i=0; i<params.length; i++)
                {
                        if (params[i] instanceof LogoWord)
                        {
                                sb.append(params[i].toString());
                        }
                        else
                        {
                                throw new LanguageException("Word expected");
                        }
                }
                return new LogoWord(sb.toString());
        }


        /**
         * Primitive implementation for WORDP
         */
        public final LogoObject pWORDP(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                return new LogoWord(params[0] instanceof LogoWord);
        }


        /**
         * Primitive implementation for WRITER
         */
        public final LogoObject pWRITER(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 0);
                CaselessString fileid = interp.thread().outStreamID();
                if (fileid == null)
                {
                        return new LogoWord(Machine.CONSOLE_STREAM_NAME);
                }
                else
                {
                        return new LogoWord(fileid);
                }
        }


        /**
         * Utility for Erase names
         */
        private final void eraseNames(
                InterpEnviron interp,
                LogoObject list)
        throws
                LanguageException
        {
                if (list instanceof LogoWord)
                {
                        interp.thread().eraseName(list.toCaselessString());
                }
                else
                {
                        int i;
                        for (i=0; i<list.length(); i++)
                        {
                                LogoObject obj = ((LogoList)list).pickInPlace(i);
                                if (!(obj instanceof LogoWord))
                                {
                                        throw new LanguageException("Name expected");
                                }
                                interp.thread().eraseName(obj.toCaselessString());
                        }
                }
        }


        /**
         * Utility for Erase procedures
         */
        private final void eraseProcs(
                InterpEnviron interp,
                LogoObject list)
        throws
                LanguageException
        {
                if (list instanceof LogoWord)
                {
                        interp.mach().eraseProc(list.toCaselessString());
                }
                else
                {
                        int i;
                        for (i=0; i<list.length(); i++)
                        {
                                LogoObject obj = ((LogoList)list).pickInPlace(i);
                                if (!(obj instanceof LogoWord))
                                {
                                        throw new LanguageException("Procedure title expected");
                                }
                                interp.mach().eraseProc(obj.toCaselessString());
                        }
                }
        }


        /**
         * Utility for Erase plists
         */
        private final void erasePList(
                InterpEnviron interp,
                CaselessString name)
        throws
                LanguageException
        {
                LogoList props = interp.mach().getPropsInList(name);
                int i;
                for (i=0; i<props.length(); i++)
                {
                        interp.mach().remProp(name, props.pickInPlace(i).toCaselessString());
                }
        }


        /**
         * Utility for Erase plists
         */
        private final void erasePLists(
                InterpEnviron interp,
                LogoObject list)
        throws
                LanguageException
        {
                if (list instanceof LogoWord)
                {
                        erasePList(interp, list.toCaselessString());
                }
                else
                {
                        int i;
                        for (i=0; i<list.length(); i++)
                        {
                                LogoObject obj = ((LogoList)list).pickInPlace(i);
                                if (!(obj instanceof LogoWord))
                                {
                                        throw new LanguageException("Property list name expected");
                                }
                                erasePList(interp, obj.toCaselessString());
                        }
                }
        }


        /**
         * Utility for TRY
         */
        private final LogoObject parseCatches(
                InterpEnviron interp,
                LogoObject[] params,
                ThrowException te,
                LanguageException le)
        throws
                LanguageException,
                ThrowException
        {
                Tokenizer tokenizer = new Tokenizer(0);
                boolean handled = false;
                LogoObject ret = LogoVoid.obj;
                //LogoObject tag = LogoVoid.obj;
                //LogoObject lis = LogoVoid.obj;
                int pos;
                for (pos=1; pos<params.length && !handled; pos+=2)
                {
                        // determine that the tag matches
                        LogoObject tagObj;
                        if (te != null)
                        {
                                tagObj = new LogoWord(te.getTag());
                        }
                        else
                        {
                                tagObj = new LogoWord("ERROR");
                        }
                        if (params[pos] instanceof LogoWord)
                        {
                                if (!params[pos].equals(tagObj))
                                {
                                        continue;
                                }
                        }
                        else
                        {
                                if (params[pos].length() != 0)
                                {
                                        if (!((LogoList)(params[pos])).isMember(tagObj))
                                        {
                                                continue;
                                        }
                                }
                        }
                        
                        // tag matches: run list
                        if (params[pos+1] instanceof LogoList &&
                                (params[pos+1].length() == 0 ||
                                !(((LogoList)(params[pos+1])).pickInPlace(0) instanceof LogoList)))
                        {
                                ret = params[pos+1].getRunnable(interp.mach()).execute(interp);
                        }
                        else
                        {
                                LogoObject[] objlis = new LogoObject[2];
                                objlis[0] = tagObj;
                                if (te != null)
                                {
                                        objlis[1] = te.getObj();
                                }
                                else
                                {
                                        objlis[1] = tokenizer.tokenize(le.getMessage());
                                }
                                if (params[pos+1] instanceof LogoWord)
                                {
                                        ret = applyProc(interp, params[pos+1].toCaselessString(), new LogoList(objlis));
                                }
                                else
                                {
                                        ret = applyAnonymous(interp, (LogoList)(params[pos+1]), new LogoList(objlis), false);
                                }
                        }
                        handled = true;
                }
                if (!handled)
                {
                        if (te != null)
                                throw te;
                        if (le != null)
                                throw le;
                }
                return ret;
        }

}



