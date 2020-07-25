/*
===============================================================================

        FILE:  NetworkPrimitives.java
        
        PROJECT:
        
                Turtle Tracks
        
        CONTENTS:
        
                Standard network primitives
        
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

import java.net.*;
import java.io.*;


/**
 * Names of primitives (keywords)
 */

public final class NetworkPrimitives
extends PrimitiveGroup
{

        /**
         * Set up primitive group
         */
        protected void setup(
                Machine mach,
                Console console)
        throws
                SetupException
        {
                registerPrimitive("LOADURL", "pLOADURL", 1);
                registerPrimitive("OPENSOCKET", "pOPENSOCKET", 2);
                registerPrimitive("OPENURL", "pOPENURL", 1);
                registerPrimitive("SERVERSOCKET", "pSERVERSOCKET", 4);
                
                console.putStatusMessage("Turtle Tracks networking primitives v1.0");
        }


        /**
         * Primitive implementation for LOADURL
         */
        public final LogoObject pLOADURL(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                if (!(params[0] instanceof LogoWord))
                {
                        throw new LanguageException("URL expected");
                }
                if (interp.thread().isLoading())
                {
                        throw new LanguageException("Attempt to load from within a load");
                }
                interp.mach().executeStream(new IOURL(params[0].toString()), interp.thread());
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for OPENSOCKET
         */
        public final LogoObject pOPENSOCKET(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 2, 3);
                if (!(params[0] instanceof LogoWord))
                {
                        throw new LanguageException("Hostname expected");
                }
                CaselessString streamid;
                if (params.length == 3)
                {
                        if (!(params[2] instanceof LogoWord))
                        {
                                throw new LanguageException("Stream id expected");
                        }
                        streamid = params[2].toCaselessString();
                }
                else
                {
                        streamid = new CaselessString("__f" + interp.mach().getUniqueNum());
                }
                interp.mach().registerIO(streamid, new IOSocket(params[0].toString(), params[1].toInteger()));
                if (params.length == 3)
                {
                        return LogoVoid.obj;
                }
                else
                {
                        return new LogoWord(streamid);
                }
        }


        /**
         * Primitive implementation for OPENURL
         */
        public final LogoObject pOPENURL(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1, 2);
                if (!(params[0] instanceof LogoWord))
                {
                        throw new LanguageException("URL expected");
                }
                CaselessString streamid;
                if (params.length == 2)
                {
                        if (!(params[1] instanceof LogoWord))
                        {
                                throw new LanguageException("Stream id expected");
                        }
                        streamid = params[1].toCaselessString();
                }
                else
                {
                        streamid = new CaselessString("__f" + interp.mach().getUniqueNum());
                }
                interp.mach().registerIO(streamid, new IOURL(params[0].toString()));
                if (params.length == 2)
                {
                        return LogoVoid.obj;
                }
                else
                {
                        return new LogoWord(streamid);
                }
        }


        /**
         * Primitive implementation for SERVERSOCKET
         */
        public final LogoObject pSERVERSOCKET(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException,
                ThrowException
        {
                testNumParams(params, 4);
                try
                {
                        ServerSocket server = new ServerSocket(params[0].toInteger());
                        try
                        {
                                server.setSoTimeout(params[2].toInteger());
                                boolean keepgoing = true;
                                while (keepgoing)
                                {
                                        LogoObject obj = LogoVoid.obj;
                                        try
                                        {
                                                IOSocket sock = new IOSocket(server.accept());
                                                CaselessString fileid = new CaselessString("__f" + interp.mach().getUniqueNum());
                                                interp.mach().registerIO(fileid, sock);
                                                LogoObject[] objlis = new LogoObject[1];
                                                objlis[0] = new LogoWord(fileid);
                                                if (params[1] instanceof LogoList)
                                                {
                                                        obj = applyAnonymous(interp, (LogoList)(params[1]), new LogoList(objlis), false);
                                                }
                                                else if (params[1] instanceof LogoWord)
                                                {
                                                        obj = applyProc(interp, params[1].toCaselessString(), new LogoList(objlis));
                                                }
                                                else
                                                {
                                                        throw new LanguageException("Lambda expected");
                                                }
                                                if (!interp.mach().isAutoIgnore() && obj != LogoVoid.obj)
                                                {
                                                        throw new LanguageException("You don't say what to do with " + obj.toString());
                                                }
                                        }
                                        catch (InterruptedIOException e)
                                        {
                                        }
                                        catch (IOException e)
                                        {
                                                throw new LanguageException("Can't accept socket: " + e.getMessage());
                                        }
                                        
                                        if (params[3] instanceof LogoList)
                                        {
                                                obj = applyAnonymous(interp, (LogoList)(params[3]), new LogoList(), false);
                                        }
                                        else if (params[3] instanceof LogoWord)
                                        {
                                                obj = applyProc(interp, params[3].toCaselessString(), new LogoList());
                                        }
                                        else
                                        {
                                                throw new LanguageException("Lambda expected");
                                        }
                                        keepgoing = obj.toBoolean();
                                }
                        }
                        finally
                        {
                                server.close();
                        }
                }
                catch (IOException e)
                {
                        throw new LanguageException("Can't open server socket: " + e.getMessage());
                }
                return LogoVoid.obj;
        }

}



