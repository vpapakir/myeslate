/*
===============================================================================

        FILE:  ShellPrimitives.java
        
        PROJECT:
        
                Turtle Tracks
        
        CONTENTS:
        
                Shell primitives
        
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

import java.io.*;


/**
 * Names of primitives (keywords)
 */

public final class ShellPrimitives
extends PrimitiveGroup
{

        private static final int READ_BUFFER_SIZE = 256;


        /**
         * Set up primitive group
         */
        protected void setup(
                Machine mach,
                Console console)
        throws
                SetupException
        {
                registerPrimitive("EXEC", "pEXEC", 1);
                registerPrimitive("EXECASYNC", "pEXECASYNC", 1);
                registerPrimitive("SHELL", "pSHELL", 1);
                
                console.putStatusMessage("Turtle Tracks shell primitives v1.0");
        }


        /**
         * Primitive implementation for EXEC
         */
        public final LogoObject pEXEC(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1, 2);
                String[] env = null;
                String cmd = null;
                if (params.length == 2)
                {
                        if (!(params[1] instanceof LogoWord))
                        {
                                throw new LanguageException("Environment property list expected");
                        }
                        env = interp.mach().getPropsForExec(params[1].toCaselessString());
                }
                if (params[0] instanceof LogoList)
                {
                        cmd = ((LogoList)(params[0])).toStringOpen();
                }
                else
                {
                        cmd = params[0].toString();
                }
                try
                {
                        Process proc = Runtime.getRuntime().exec(cmd, env);
                        try
                        {
                                int retVal = 0;
                                
                                OutputStream is = proc.getOutputStream();
                                InputStream os = proc.getInputStream();
                                InputStream es = proc.getErrorStream();
                                if (is == null || os == null || es == null)
                                {
                                        try
                                        {
                                                retVal = proc.waitFor();
                                        }
                                        catch (InterruptedException e) {}
                                }
                                else
                                {
                                        IOStream inStream = new IOStream(new BufferedWriter(new OutputStreamWriter(is)));
                                        IOStream outStream = new IOStream(new BufferedReader(new InputStreamReader(os)));
                                        IOStream errStream = new IOStream(new BufferedReader(new InputStreamReader(es)));
                                        try
                                        {
                                                int s;
                                                boolean procDone = false;
                                                char[] buf = new char[READ_BUFFER_SIZE];
                                                while (!procDone)
                                                {
                                                        
                                                        s = errStream.getAvailable(buf);
                                                        while (s > 0)
                                                        {
                                                                interp.thread().outStream().put(buf, s);
                                                                Thread.yield();
                                                                s = errStream.getAvailable(buf);
                                                        }
                                                        
                                                        s = outStream.getAvailable(buf);
                                                        while (s > 0)
                                                        {
                                                                interp.thread().outStream().put(buf, s);
                                                                Thread.yield();
                                                                s = outStream.getAvailable(buf);
                                                        }
                                                        
                                                        s = interp.thread().inStream().getAvailable(buf);
                                                        while (s > 0)
                                                        {
                                                                inStream.put(buf, s);
                                                                Thread.yield();
                                                                s = interp.thread().inStream().getAvailable(buf);
                                                        }
                                                        
                                                        try
                                                        {
                                                                retVal = proc.exitValue();
                                                                procDone = true;
                                                        }
                                                        catch (IllegalThreadStateException e) {}
                                                        Thread.yield();
                                                }
                                                while (!errStream.eof())
                                                {
                                                        s = errStream.getAvailable(buf);
                                                        if (s > 0)
                                                        {
                                                                interp.thread().outStream().put(buf, s);
                                                        }
                                                }
                                                while (!outStream.eof())
                                                {
                                                        s = outStream.getAvailable(buf);
                                                        if (s > 0)
                                                        {
                                                                interp.thread().outStream().put(buf, s);
                                                        }
                                                }
                                        }
                                        finally
                                        {
                                                inStream.close();
                                                outStream.close();
                                                errStream.close();
                                        }
                                }
                                return new LogoWord(retVal);
                        }
                        catch (Exception e)
                        {
                                proc.destroy();
                                throw new LanguageException("Error while running: " + e.toString());
                        }
                }
                catch (IOException e)
                {
                        throw new LanguageException("I/O Error while launching process");
                }
                catch (SecurityException e)
                {
                        throw new LanguageException("Security prevented launching process");
                }
        }


        /**
         * Primitive implementation for EXECASYNC
         */
        public final LogoObject pEXECASYNC(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1, 2);
                String[] env = null;
                String cmd = null;
                if (params.length == 2)
                {
                        if (!(params[1] instanceof LogoWord))
                        {
                                throw new LanguageException("Environment property list expected");
                        }
                        env = interp.mach().getPropsForExec(params[1].toCaselessString());
                }
                if (params[0] instanceof LogoList)
                {
                        cmd = ((LogoList)(params[0])).toStringOpen();
                }
                else
                {
                        cmd = params[0].toString();
                }
                try
                {
                        /*Process proc = */Runtime.getRuntime().exec(cmd, env);
                        return LogoVoid.obj;
                }
                catch (IOException e)
                {
                        throw new LanguageException("I/O Error while launching process");
                }
                catch (SecurityException e)
                {
                        throw new LanguageException("Security prevented launching process");
                }
        }


        /**
         * Primitive implementation for SHELL
         */
        public final LogoObject pSHELL(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException,
                ThrowException
        {
                testNumParams(params, 1, 2);
                String[] env = null;
                String cmd = null;
                if (params.length == 2)
                {
                        if (!(params[1] instanceof LogoWord))
                        {
                                throw new LanguageException("Environment property list expected");
                        }
                        env = interp.mach().getPropsForExec(params[1].toCaselessString());
                }
                if (params[0] instanceof LogoList)
                {
                        cmd = ((LogoList)(params[0])).toStringOpen();
                }
                else
                {
                        cmd = params[0].toString();
                }
                try
                {
                        Process proc = Runtime.getRuntime().exec(cmd, env);
                        try
                        {
                                InputStream os = proc.getInputStream();
                                InputStream es = proc.getErrorStream();
                                if (os == null || es == null)
                                {
                                        try
                                        {
                                                proc.waitFor();
                                        }
                                        catch (InterruptedException e) {}
                                        return new LogoList();
                                }
                                else
                                {
                                        IOStream outStream = new IOStream(new BufferedReader(new InputStreamReader(os)));
                                        IOStream errStream = new IOStream(new BufferedReader(new InputStreamReader(es)));
                                        StringBuffer retBuf = new StringBuffer();
                                        try
                                        {
                                                int s;
                                                boolean procDone = false;
                                                char[] buf = new char[READ_BUFFER_SIZE];
                                                while (!procDone)
                                                {
                                                        s = errStream.getAvailable(buf);
                                                        while (s > 0)
                                                        {
                                                                interp.thread().outStream().put(buf, s);
                                                                Thread.yield();
                                                                s = errStream.getAvailable(buf);
                                                        }
                                                        
                                                        s = outStream.getAvailable(buf);
                                                        while (s > 0)
                                                        {
                                                                retBuf.append(buf, 0, s);
                                                                Thread.yield();
                                                                s = outStream.getAvailable(buf);
                                                        }
                                                        
                                                        try
                                                        {
                                                                proc.exitValue();
                                                                procDone = true;
                                                        }
                                                        catch (IllegalThreadStateException e) {}
                                                        Thread.yield();
                                                }
                                                while (!errStream.eof())
                                                {
                                                        s = errStream.getAvailable(buf);
                                                        if (s > 0)
                                                        {
                                                                interp.thread().outStream().put(buf, s);
                                                        }
                                                }
                                                while (!outStream.eof())
                                                {
                                                        s = outStream.getAvailable(buf);
                                                        if (s > 0)
                                                        {
                                                                retBuf.append(buf, 0, s);
                                                        }
                                                }
                                        }
                                        finally
                                        {
                                                outStream.close();
                                                errStream.close();
                                        }
                                        return new Tokenizer(0).tokenize(new String(retBuf));
                                }
                        }
                        catch (Exception e)
                        {
                                proc.destroy();
                                throw new LanguageException("Error while running: " + e.toString());
                        }
                }
                catch (IOException e)
                {
                        throw new LanguageException("I/O Error while launching process");
                }
                catch (SecurityException e)
                {
                        throw new LanguageException("Security prevented launching process");
                }
        }

}



