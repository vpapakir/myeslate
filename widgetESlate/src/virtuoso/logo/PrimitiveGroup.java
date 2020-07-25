/*
===============================================================================

        FILE:  PrimitiveGroup.java
        
        PROJECT:
        
                Turtle Tracks
        
        CONTENTS:
        
                PrimitiveGroup abstract class
        
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


import java.util.Hashtable;
//Birb:not-needed// import java.util.Random;

import java.util.ResourceBundle; //Birb: 31Jul1998
import java.util.Enumeration; //Birb: 07Apr2000
import java.util.Locale; //Birb: 28JunApr2000

/**
 * PrimitiveGroup abstract class. Primitive implementations must extend this
 * class.
 */
@SuppressWarnings(value={"unchecked"})
public abstract class PrimitiveGroup
{

        private Class _thisClass;
        private static Class[] _params;

        private Hashtable _primitiveTable;

  private ResourceBundle m; //Birb: 2-11-1998


        static
        {
                _params = new Class[2];
                _params[0] = (new InterpEnviron()).getClass();
                _params[1] = (new LogoObject[1]).getClass();
        }


        /**
         * Constructor
         */
        protected PrimitiveGroup()
        {
                _primitiveTable = new Hashtable();
                _thisClass = getClass();

   //Birb-start: 2-11-1998
   //24-12-1998:console is too slow// System.out.println("["+_thisClass.getName()+"]");
   try{
    m = ResourceBundle.getBundle(_thisClass.getName()+"Bundle", Locale.getDefault());
    //24-12-1998:console is too slow//   System.out.println("...Logo found a translation/localization bundle for this primitive group");
   }catch(/*MissingResource*/Exception e) { //if no local translation bundle...
    m = MyMachine.getGlobalTranslationBundle(); //...use the global one (if such exists) //28Jun2000: make sure it's loaded
   }
   //if localized bundle is not found it shall load the english version and if none shall throw an exception???
   //Birb-end

        }

  public CaselessString[] getPrimitiveNames(){ //Birb-07Apr2000: need to be able to ask which primitives are available for a given set of primitive groups (calculated from a given set of "named objects / e-slate components" told by the user to print their primitives names)
   CaselessString[] names=new CaselessString[_primitiveTable.size()];

   Enumeration primitives=_primitiveTable.keys();
   for(int i=0;primitives.hasMoreElements();i++)
    names[i]=(CaselessString)primitives.nextElement();

   return names;
  }

  public PrimitiveSpec[] getPrimitiveSpecs(){ //Birb-07Apr2000: need to be able to ask which primitives are available for a given set of primitive groups (calculated from a given set of "named objects / e-slate components" told by the user to print their primitives info)
   PrimitiveSpec[] specs=new PrimitiveSpec[_primitiveTable.size()];

   Enumeration primitives=_primitiveTable.elements();
   for(int i=0;primitives.hasMoreElements();i++)
    specs[i]=(PrimitiveSpec)primitives.nextElement();

   return specs;
  }

        /**
         * Create a primitive definition spec given a primitive name
         *
         * @param name word
         *
         * @return the method, or null for not in this primitive group
         */
        public final PrimitiveSpec getPrimitiveMethod(
                CaselessString name)
        {
                return (PrimitiveSpec)(_primitiveTable.get(name));
        }


        /**
         * Is this name a primitive in this group
         *
         * @param name the string to look for
         *
         * @return true if and only if this primitive is implemented in this PrimitiveGroup
         */
        public final boolean isPrimitive(
                CaselessString name)
        {
                return _primitiveTable.get(name) != null;
        }


  private String getLocalizedPrimitiveName(String primitiveName){ //Birb: 31May2000
   try{return (m!=null)?m.getString(primitiveName):null;}
   catch(Exception e){return null;} //must unfortunately have a missing resource exception being thrown and caught for every primitive localization that's missing: fix-this: use some ResourceBundle that doesn't throw exceptions for missing resources...
  }


  protected final void registerPrimitive(String s, String s1, int i) //Birb: 31-7-1998
      throws SetupException
  {
   //System.out.println("1:"+s+' '+s1); //PRINT TRACE FOR PRIMITIVES LOADING

   //Register the original primitive name:
   registerPrimitive_old(s,s1,i);

   //Register the localized primitive name:
   String localized=getLocalizedPrimitiveName(s);
   if(localized!=null)
    registerPrimitive_old(localized,s1,i); //this gets called only if there's a localized version of the primitive
  }

        /**
         * Set up one primitive in current primitive group. Should be called from setup().
         *
         * @param primitiveName the name of the primitive
         * @param methodName the name of the method that implements the primitive.
         * @param numDefArgs default number of arguments for this primitive
         *
         * @exception virtuoso.logo.SetupException no such method
         */
  /*protected*/ private final void registerPrimitive_old( //Birb: 31-7-1998
                String primitiveName,
                String methodName,
                int numDefArgs)
        throws
                SetupException
        {
                try
                {
                        try
                        {
                                _primitiveTable.put(new CaselessString(primitiveName), new PrimitiveSpec(
                                        this, _thisClass.getDeclaredMethod(methodName, _params), numDefArgs));
                        }
                        catch (SecurityException e)
                        {
                                _primitiveTable.put(new CaselessString(primitiveName), new PrimitiveSpec(
                                        this, _thisClass.getMethod(methodName, _params), numDefArgs));
                        }
                }
                catch (SecurityException e)
                {
                        throw new SetupException("Security prohibits reflection",
                                SetupException.securityError);
                }
                catch (NoSuchMethodException e)
                {
                        throw new SetupException("No such method: " + methodName,
                                SetupException.noSuchMethodErr);
                }
        }


  private final void putInPrimitivesTable(String primitiveName, Object primitiveImpl) //Birb: 31May2000
   throws SetupException
  {
   //System.out.println("4:"+primitiveName); //PRINT TRACE FOR PRIMITIVES LOADING

   //Register the original primitive name:
   _primitiveTable.put(new CaselessString(primitiveName), primitiveImpl);

   //Register the localized primitive name:
   String localized=getLocalizedPrimitiveName(primitiveName);
   if(localized!=null) //31May2000: removed costly try-catch block, added a !=null check
    _primitiveTable.put(new CaselessString(localized), primitiveImpl); //this gets called only if there's a localized version of the primitive
  }


        /**
         * Alias one primitive to the setup in another primitive group
         *
         * @param primitiveName the name of the primitive
         * @param group PrimitiveGroup to proxy.
         *
         * @exception virtuoso.logo.SetupException no such primitive in proxy
         */
        protected final void registerPrimitive(
                String primitiveName,
                PrimitiveGroup group)
        throws
                SetupException
        {
                PrimitiveSpec spec = group.getPrimitiveMethod(new CaselessString(primitiveName));
                if (spec == null)
                {
                        throw new SetupException("No such primitive: " + primitiveName +
                                " in group " + group.getClass().getName(),
                                SetupException.noSuchPrimitiveErr);
                }
                else
                {
      putInPrimitivesTable(primitiveName,spec); //Birb: 31May2000
                        //Birb: 31May2000// _primitiveTable.put(new CaselessString(primitiveName), spec);
                }
        }


        /**
         * Alias one primitive to another primitive in another primitive group
         *
         * @param primitiveName the name of the primitive
         * @param group PrimitiveGroup to proxy.
         * @param otherName primitive name in the proxy primitive group
         *
         * @exception virtuoso.logo.SetupException no such primitive in proxy
         */
        protected final void registerPrimitive(
                String primitiveName,
                PrimitiveGroup group,
                String otherName)
        throws
                SetupException
        {
                PrimitiveSpec spec = group.getPrimitiveMethod(new CaselessString(otherName));
                if (spec == null)
                {
                        throw new SetupException("No such primitive: " + otherName +
                                " in group " + group.getClass().getName(),
                                SetupException.noSuchPrimitiveErr);
                }
                else
                {
      putInPrimitivesTable(primitiveName,spec); //Birb: 31May2000
                        //Birb: 31May2000// _primitiveTable.put(new CaselessString(primitiveName), spec);
                }
        }


        /**
         * A primitive group must implement this method to register all its primitives.
         *
         * @param mach the machine in use.
         * @param console useful for printing an initialization message.
         *
         * @exception virtuoso.logo.SetupException Could not set up the primitive group for some reason
         */
        protected abstract void setup(
                Machine mach,
                Console console)
        throws
                SetupException;


        /**
         * Called when VM is closing down. Override to perform any necessary cleanup.
         */
        protected void exiting()
        {
        }


        /**
         * Test the parameter list for correct number of arguments.
         *
         * @param params the parameter list
         * @param i the expected number of parameters
         *
         * @exception virtuoso.logo.LanguageException parameter number mismatch
         */
        protected final void testNumParams(
                LogoObject[] params,
                int i)
        throws
                LanguageException
        {
                if (params.length > i)
                {
                        throw new LanguageException("Too many arguments");
                }
                else if (params.length < i)
                {
                        throw new LanguageException("Not enough arguments");
                }
        }


        /**
         * Test the parameter list for correct number of arguments
         *
         * @param params the parameter list
         * @param i the minimum expected number of parameters
         *
         * @exception virtuoso.logo.LanguageException parameter number mismatch
         */
        protected final void testMinParams(
                LogoObject[] params,
                int i)
        throws
                LanguageException
        {
                if (params.length < i)
                {
                        throw new LanguageException("Not enough arguments");
                }
        }


        /**
         * Test the parameter list for correct number of arguments
         *
         * @param params the parameter list
         * @param i the maximum expected number of parameters
         *
         * @exception virtuoso.logo.LanguageException parameter number mismatch
         */
        protected final void testMaxParams(
                LogoObject[] params,
                int i)
        throws
                LanguageException
        {
                if (params.length > i)
                {
                        throw new LanguageException("Too many arguments");
                }
        }


        /**
         * Test the parameter list for correct number of arguments
         *
         * @param params the parameter list
         * @param min the minimum expected number of parameters
         * @param max the maximum expected number of parameters
         *
         * @exception virtuoso.logo.LanguageException parameter number mismatch
         */
        protected final void testNumParams(
                LogoObject[] params,
                int min,
                int max)
        throws
                LanguageException
        {
                if (params.length > max)
                {
                        throw new LanguageException("Too many arguments");
                }
                else if (params.length < min)
                {
                        throw new LanguageException("Not enough arguments");
                }
        }


        /**
         * Apply given procedure to given values.
         *
         * @param interp the InterpEnviron
         * @param name the name of the procedure or primitive
         * @param paramValues the values of the arguments.
         *
         * @exception virtuoso.logo.LanguageException error thrown
         * @exception virtuoso.logo.ThrowException exception thrown
         */
        protected final static LogoObject applyProc(
                InterpEnviron interp,
                CaselessString name,
                LogoList paramValues)
        throws
                LanguageException,
                ThrowException
        {
                ParseObject[] cmds = new ParseObject[1];
                int len = paramValues.length();
                LogoObject[] arr = new LogoObject[len];
                for (int i=0; i<len; i++)
                {
                        arr[i] = paramValues.pickInPlace(i);
                }
                
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
                return new ParseTree(0, cmds).execute(interp);
        }


        /**
         * Apply given lambda to given values
         *
         * @param interp the InterpEnviron
         * @param lambda the lambda list
         * @param paramValues the values of the arguments.
         * @param strictParams if true, an exception is thrown if fewer actual parameters
         *        are given than formal parameters. if false, then the unset formal
         *        parameters are simply not bound.
         *
         * @exception virtuoso.logo.LanguageException error thrown
         * @exception virtuoso.logo.ThrowException exception thrown
         */
        protected final static LogoObject applyAnonymous(
                InterpEnviron interp,
                LogoList lambda,
                LogoList paramValues,
                boolean strictParams)
        throws
                LanguageException,
                ThrowException
        {
                // Parse lambda list
                if (lambda.length() == 0 || !(lambda.pickInPlace(0) instanceof LogoList))
                {
                        throw new LanguageException("Lambda list expected");
                }
                LogoList paramNames = (LogoList)(lambda.pickInPlace(0));
                LogoList runnable;
                boolean isMacro = false;
                if (lambda.pickInPlace(1) instanceof LogoList)
                {
                        if (lambda.length() > 2)
                        {
                                throw new LanguageException("Lambda list expected");
                        }
                        runnable = (LogoList)(lambda.pickInPlace(1));
                }
                else
                {
                        isMacro = true;
                        runnable = (LogoList)(lambda.butFirst());
                }
                
                // Bind parameters
                SymbolTable local = new SymbolTable();
                int nlen = paramNames.length();
                int vlen = paramValues.length();
                if ((strictParams && nlen != vlen) || (!strictParams && nlen > vlen))
                {
                        throw new LanguageException("Wrong number of arguments");
                }
                for (int i=0; i<nlen; i++)
                {
                        if (!(paramNames.pickInPlace(i) instanceof LogoWord))
                        {
                                throw new LanguageException("Bad symbol list in lambda expression");
                        }
                        local.makeForced(
                                paramNames.pickInPlace(i).toCaselessString(),
                                paramValues.pickInPlace(i));
                }
                
                // Invoke
                LogoObject ret = LogoVoid.obj;
                InterpEnviron interp2 = new InterpEnviron(interp);
                interp.thread().enterProcedure(local);
                LogoObject obj = LogoVoid.obj;
                try
                {
                        obj = runnable.getRunnable(interp.mach()).execute(interp2);
                }
                catch (ThrowException e)
                {
                        if (!isMacro && e.getTag().equals("STOP"))
                        {
                                ret = e.getObj();
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
                if (isMacro)
                {
                        ret = obj;
                }
                else if (obj != LogoVoid.obj)
                {
                        throw new LanguageException("I don't know what to do with " + obj);
                }
                return ret;
        }

}



