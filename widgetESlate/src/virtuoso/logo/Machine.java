/*
===============================================================================

        FILE:  Machine.java
        
        PROJECT:
        
                Turtle Tracks
        
        CONTENTS:
        
                Virtual machine object
        
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

import java.util.*;


/**
 * Logo runtime machine. Controls the current state of the runtime system.
 * All Logo interpretation is done relative to a Machine.
 */
@SuppressWarnings(value={"unchecked"})
public /*final*/ class Machine //Birb: removed final
{

        /**
         * The name of the console stream
         */
        public static final String CONSOLE_STREAM_NAME = ".CONSOLE";
        
        /**
         * The name of the main thread
         */
        public static final String MAIN_THREAD_NAME = ".MAIN";
        
        /**
         * The name of a private thread for use by loading, etc.
         */
        public static final String PRIVATE_NAME = ".PRIVATE";
        
        /**
         * The name of the system property list
         */
        public static final String LOGO_PROPERTYLIST_NAME = ".SYSTEM";
        
        /**
         * The current version of Turtle Tracks
         */
        public static final String TURTLETRACKS_VERSION = "1.0";
        
        /**
         * The value of the system property "line.separator"
         */
        public static final String LINE_SEPARATOR = System.getProperty("line.separator");


  protected Vector _primitiveGroups; //Birbilis: changed private to protected
        
        private SymbolTable _syms;
        private Hashtable _procs;
        private Hashtable _fileids;
        private Hashtable _proplists;
        
        private Hashtable _data;
        
        private Hashtable _threads;
        private int _suspended;
        private Object _suspendedLock;
        
        private Random _random;
        private int _uniqueNum;
        private Object _uniqueNumLock;
  protected int _clock; //Birbilis: changed private to protected
        
        private IOBase _instream;
        private CaselessString _instreamID;
        private IOBase _outstream;
        private CaselessString _outstreamID;
        
        private Console _console;
        
        private boolean _hashComments;
        private boolean _overridePrimitive;
        private boolean _autoIgnore;


        /**
         * Create a new virtual machine. Note that the console and primitivegroups
         * should be instantiated first, then passed to this constructor. After
         * instantiating the Machine, you must call the setup() method to set up
         * the console and primitivegroups.
         *
         * @param ci the console to use
         * @param pg the primitive groups to use
         */
        public Machine(
                Console ci,
                PrimitiveGroup[] pg)
        {
                _syms = new SymbolTable();
                _procs = new Hashtable();
                _fileids = new Hashtable();
                _proplists = new Hashtable();
                
                _data = new Hashtable();
                
                _threads = new Hashtable();
                _suspended = 0;
                _suspendedLock = new Object();
                
                _uniqueNum = 0;
                _uniqueNumLock = new Object();
                _random = new Random(0);
                
                _console = ci;

    if(pg!=null){ //Birb:31May2000

     _primitiveGroups = new Vector(pg.length);
     for (int i=0; i<pg.length; i++)
     {
       _primitiveGroups.addElement(pg[i]);
     }

    } else _primitiveGroups=new Vector(30); //31May2000: have default initial allocated space for at least 30 primitive groups if no startup primitive groups are passed to the machine

                _instream = _console;
                _outstream = _console;
                _instreamID = null;
                _outstreamID = null;

                _clock = 0;
                
                _hashComments = false;
                _overridePrimitive = false;
                _autoIgnore = false;
        }


        /**
         * Prepare environment by calling all the appropriate setup methods of the
         * console and primitivegroups. This must be called before the machine can
         * be invoked in any other way.
         *
         * @exception virtuoso.logo.SetupException The console or one of the
         * PrimitiveGroups failed to initialize.
         */
        public final void setup()
        throws
                SetupException
        {
                _console.setMachine(this);
                _console.setup();
                _console.putStatusMessage("Turtle Tracks runtime system v1.0");
                for (int i=0; i<_primitiveGroups.size(); i++)
                {
                        ((PrimitiveGroup)(_primitiveGroups.elementAt(i))).setup(this, _console);
                }
        }


        /**
         * Tear down environment. This should be called after the machine shuts down.
         * It informs the console and all primitive groups to clean up.
         */
        public /*final*/ void cleanup() //Birb: 28Jun2000: removed final
        {
                for (int i=0; i<_primitiveGroups.size(); i++)
                {
                        ((PrimitiveGroup)(_primitiveGroups.elementAt(i))).exiting();
                }
                _console.exiting();
        }

  public final void prepare() throws SetupException{ //Birb-28Jun2000: provided for compatibility with existing clients
   setup();
  }

  public final void tearDown(){ //Birb-28Jun2000: provided for compatibility with existing clients
   cleanup();
  }

        /**
         * Get console
         *
         * @return the console
         */
        public final Console console()
        {
                return _console;
        }


        /**
         * Load primitive group and sets it up given a class name. Does nothing if a
         * PrimitiveGroup with the same class name is already loaded in this machine.
         *
         * @param classname the name of the Java class to load.
         *
         * @exception virtuoso.logo.LanguageException couldn't load the primitive group.
         */
        public final synchronized void loadPrimitives(
                String classname)
        throws
                LanguageException
        {
                try
                {
                        int i;
                        for (i=0; i<_primitiveGroups.size(); i++)
                        {
                                if (_primitiveGroups.elementAt(i).getClass().getName().equals(classname))
                                {
                                        return;
                                }
                        }
                        Class theClass = Class.forName(classname);
                        PrimitiveGroup pg = (PrimitiveGroup)(theClass.newInstance());
                        try
                        {
                                pg.setup(this, _console);
                        }
                        catch (SetupException e)
                        {
                                throw new LanguageException("Primitive group " + classname + " refused to load.");
                        }
                        _primitiveGroups.addElement(pg);
                }
                catch (ClassNotFoundException e)
                {
                        throw new LanguageException("Class " + classname + " not found.");
                }
                catch (ClassCastException e)
                {
                        throw new LanguageException("Class " + classname + " isn't a PrimitiveGroup.");
                }
                catch (InstantiationException e)
                {
                        throw new LanguageException("Class " + classname + " couldn't be instantiated.");
                }
                catch (IllegalAccessException e)
                {
                        throw new LanguageException("Class " + classname + " not accessible.");
                }
    catch(Throwable th){
      throw new LanguageException("Exception when loading "+classname+" : "+th);
    }
                _clock++;
        }


        /**
         * Load primitive group and sets it up. The given PrimitiveGroup should be
         * constructed, but should not already have had its setup method called--
         * loadPrimitives will call the setup method. Does nothing if a PrimitiveGroup
         * with the same class name is already loaded in this machine.
         *
         * @param pg the PrimitiveGroup object
         *
         * @exception virtuoso.logo.LanguageException couldn't setup the primitive group.
         */
        public final synchronized void loadPrimitives(
                PrimitiveGroup pg)
        throws
                LanguageException
        {
                String className = pg.getClass().getName();
                for (int i=0; i<_primitiveGroups.size(); i++)
                {
                        if (_primitiveGroups.elementAt(i).getClass().getName().equals(className))
                        {
                                return;
                        }
                }
                try
                {
                        pg.setup(this, _console);
                }
                catch (SetupException e)
                {
                        throw new LanguageException("Primitive group " + className + " refused to load.");
                }
                _primitiveGroups.addElement(pg);
                _clock++;
        }


        /**
         * Clean up and unload primitive group. If no PrimitiveGroup with the given
         * class name is loaded in this machine, unloadPrimitives does nothing.
         *
         * @param classname the name of the Java class to unload.
         */
        public final synchronized void unloadPrimitives(
                String classname)
        {
                for (int i=0; i<_primitiveGroups.size(); i++)
                {
                        if (_primitiveGroups.elementAt(i).getClass().getName().equals(classname))
                        {
                                ((PrimitiveGroup)(_primitiveGroups.elementAt(i))).exiting();
                                _primitiveGroups.removeElementAt(i);
                                _clock++;
                                return;
                        }
                }
        }


        /**
         * Clean up and unload the given primitive group. If the PrimitiveGroup
         * is not loaded into this machine, unloadPrimitives does nothing.
         *
         * @param pg the PrimitiveGroup to unload
         */
        public final synchronized void unloadPrimitives(
                PrimitiveGroup pg)
        {
                for (int i=0; i<_primitiveGroups.size(); i++)
                {
                        if (_primitiveGroups.elementAt(i) == pg)
                        {
                                pg.exiting();
                                _primitiveGroups.removeElementAt(i);
                                _clock++;
                                return;
                        }
                }
        }


        /**
         * Get primitive group given the class name.
         *
         * @param classname the name of the Java class to look for.
         *
         * @return the PrimitiveGroup, or null if not found.
         */
        public final synchronized PrimitiveGroup getPrimitiveGroup(
                String classname)
        {
                for (int i=0; i<_primitiveGroups.size(); i++)
                {
                        if (_primitiveGroups.elementAt(i).getClass().getName().equals(classname))
                        {
                                return (PrimitiveGroup)(_primitiveGroups.elementAt(i));
                        }
                }
                return null;
        }


        /**
         * Search primitive groups for primitive
         *
         * @param name the name of the primitive to find
         *
         * @return a PrimitiveSpec describing the primitive definition, or null
         * if the primitive is not found.
         */
        public final synchronized PrimitiveSpec findPrimitive(
                CaselessString name)
        {
                int i;
                for (i=_primitiveGroups.size()-1; i>=0; i--)
                {
                        PrimitiveSpec spec = ((PrimitiveGroup)(_primitiveGroups.elementAt(i))).
                                getPrimitiveMethod(name);
                        if (spec != null)
                        {
                                return spec;
                        }
                }
                return null;
        }


        /**
         * Spawn new thread given a LogoList
         *
         * @param id the thread id
         * @param ll the list to interpret
         * @param isid the input stream id
         * @param is the input stream itself
         * @param osid the output stream id
         * @param os the output stream itself
         * @param syms the toplevel local scope SymbolTable
         *
         * @exception virtuoso.logo.LanguageException duplicate thread ID
         */
        public final void spawnThread(
                CaselessString id,
                LogoList ll,
                CaselessString isid,
                IOBase is,
                CaselessString osid,
                IOBase os,
                SymbolTable syms)
        throws
                LanguageException
        {
                synchronized (_threads)
                {
                        // Don't allow a stopped thread to spawn other threads
                        Thread t = Thread.currentThread();
                        if (t instanceof InterpreterThread)
                        {
                                if (((InterpreterThread)t).stopping())
                                {
                                        return;
                                }
                        }
                        if (_threads.get(id) != null)
                        {
                                throw new LanguageException("Duplicate thread ID");
                        }
                        InterpreterThread thread = new InterpreterThread(id, ll,
                                isid, is, osid, os, syms, this);
                        _threads.put(id, thread);
                        thread.start();
                }
        }


        /**
         * Spawn new thread given a ParseTree
         *
         * @param id the thread id
         * @param pt the ParseTree to interpret
         * @param isid the input stream id
         * @param is the input stream itself
         * @param osid the output stream id
         * @param os the output stream itself
         * @param syms the toplevel local scope SymbolTable
         *
         * @exception virtuoso.logo.LanguageException duplicate thread ID
         */
        public final void spawnThread(
                CaselessString id,
                ParseTree pt,
                CaselessString isid,
                IOBase is,
                CaselessString osid,
                IOBase os,
                SymbolTable syms)
        throws
                LanguageException
        {
                synchronized (_threads)
                {
                        // Don't allow a stopped thread to spawn other threads
                        Thread t = Thread.currentThread();
                        if (t instanceof InterpreterThread)
                        {
                                if (((InterpreterThread)t).stopping())
                                {
                                        return;
                                }
                        }
                        if (_threads.get(id) != null)
                        {
                                throw new LanguageException("Duplicate thread ID");
                        }
                        InterpreterThread thread = new InterpreterThread(id, pt,
                                isid, is, osid, os, syms, this);
                        _threads.put(id, thread);
                        thread.start();
                }
        }


        /**
         * Spawn main thread
         *
         * @param ll the list to interpret
         *
         * @exception virtuoso.logo.LanguageException Already a main thread running
         */
        public final void spawnMainThread(
                LogoList ll)
        throws
                LanguageException
        {
                spawnThread(new CaselessString(MAIN_THREAD_NAME), ll,
                        _instreamID, _instream, _outstreamID, _outstream, new SymbolTable());
        }


        /**
         * Remove thread
         */
        final void threadEnding(
                CaselessString id)
        {
                synchronized (_threads)
                {
                        _threads.remove(id);
                        _threads.notifyAll();
                }
        }


        /**
         * Schedules a thread for immediate termination, and performs the appropriate
         * interruptions. Does not wait for the thread to actually terminate.
         *
         * @param threadid the id of the thread to terminate
         *
         * @exception virtuoso.logo.LanguageException thread not found
         */
        public final void terminateOneThread(
                CaselessString threadid)
        throws
                LanguageException
        {
                synchronized (_threads)
                {
                        InterpreterThread thread = (InterpreterThread)(_threads.get(threadid));
                        if (thread == null)
                        {
                                throw new LanguageException("No such thread \"" + threadid + "\"");
                        }
                        thread.signalStop();
                        thread.interrupt();
                }
        }


        /**
         * Schedules all threads for immediate termination, and performs the appropriate
         * interruptions. Does not wait for the threads to actually terminate.
         */
        public final void terminateAllThreads()
        {
                synchronized (_threads)
                {
                        Enumeration elems = _threads.elements();
                        while (elems.hasMoreElements())
                        {
                                InterpreterThread thread = (InterpreterThread)(elems.nextElement());
                                thread.signalStop();
                                thread.interrupt();
                        }
                }
        }


        /**
         * Wait for all threads to terminate.
         */
        public final void waitForAllTerminated()
        {
                synchronized (_threads)
                {
                        while (_threads.size() != 0)
                        {
                                try
                                {
                                        _threads.wait();
                                }
                                catch (InterruptedException e) {}
                        }
                }
        }


        /**
         * Suspend machine
         */
        public final void suspend()
        {
                synchronized (_suspendedLock)
                {
                        _suspended++;
                }
        }


        /**
         * Unsuspend machine
         */
        public final void unsuspend()
        {
                synchronized (_suspendedLock)
                {
                        _suspended--;
                        if (_suspended == 0)
                        {
                                _suspendedLock.notifyAll();
                        }
                }
        }


        /**
         * Check for suspension. Called during the execution of a thread.
         */
        final void checkSuspend()
        {
                synchronized (_suspendedLock)
                {
                        if (_suspended > 0)
                        {
                                try
                                {
                                        _suspendedLock.wait();
                                }
                                catch (InterruptedException e) {}
                        }
                }
        }


        /**
         * Get the clock value. This is a global clock incremented whenever lists
         * require reparsing.
         *
         * @return the clock value
         */
        final synchronized int getClock()
        {
                return _clock;
        }


        /**
         * Get the machine's random number generator
         *
         * @return the generator
         */
        public final Random random()
        {
                return _random;
        }


        /**
         * Reseed the machine's random number generator
         *
         * @param seed the seed value
         */
        public final void randomize(
                long seed)
        {
                _random.setSeed(seed);
        }


        /**
         * Sets user info, using a key String and a data Object. Your console or
         * your custom primitives can use this mechanism to store data associated
         * with this machine, which should be useful for communicating between
         * PrimitiveGroups. For example, FilePrimitives ane ExtFilePrimitives use
         * this mechanism to keep track of the current working directory. setData
         * overwrites any existing data with this key.
         *
         * @param dataID key for the data
         * @param data the data to store
         */
        public final void setData(
                String dataID,
                Object data)
        {
                synchronized (_data)
                {
                        _data.put(dataID, data);
                }
        }


        /**
         * Gets user info associated with the given key. Returns null if the key
         * is not found.
         *
         * @param dataID key for the data
         *
         * @return the data, or null if key not found.
         */
        public final Object getData(
                String dataID)
        {
                synchronized (_data)
                {
                        return _data.get(dataID);
                }
        }


        /**
         * Mutator for default inStream
         *
         * @param isid the new stream name.
         * @param is the new stream.
         */
        public final void setInStream(
                CaselessString isid,
                IOBase is)
        {
                _instreamID = isid;
                _instream = is;
        }


        /**
         * Mutator for default outStream
         *
         * @param osid the new stream name.
         * @param os the new stream.
         */
        public final void setOutStream(
                CaselessString osid,
                IOBase os)
        {
                _outstreamID = osid;
                _outstream = os;
        }


        /**
         * Set hash comments on or off
         *
         * @param onoroff hash comments on or off
         */
        public final void setHashComments(
                boolean onoroff)
        {
                _hashComments = onoroff;
        }


        /**
         * Accessor for hash comments setting
         *
         * @return flags to give to tokenizer
         */
        public final int getTokenizerCommentFlags()
        {
                if (_hashComments)
                {
                        return Tokenizer.HONOR_COMMENTS | Tokenizer.HASH_COMMENT;
                }
                else
                {
                        return Tokenizer.HONOR_COMMENTS;
                }
        }


        /**
         * Is overriding of primitive names allowed?
         *
         * @return true if overriding is allowed
         */
        public final synchronized boolean isOverridePrimitives()
        {
                return _overridePrimitive;
        }


        /**
         * Is auto-ignore activated
         *
         * @return true if auto-ignore is active
         */
        public final synchronized boolean isAutoIgnore()
        {
                return _autoIgnore;
        }


        /**
         * Is the given name reserved?
         *
         * @param name the string to look for
         *
         * @return true if the string is reserved
         */
        public final synchronized boolean isReserved(
                CaselessString name)
        {
                int i;
                for (i=0; i<_primitiveGroups.size(); i++)
                {
                        if (((PrimitiveGroup)(_primitiveGroups.elementAt(i))).isPrimitive(name))
                        {
                                return true;
                        }
                }
                if (name.equals(ParseSpecial.strTO))
                {
                        return true;
                }
                if (name.equals(ParseSpecial.strTOMACRO))
                {
                        return true;
                }
                if (name.equals(ParseSpecial.strEND))
                {
                        return true;
                }
                return false;
        }


        /**
         * Define a procedure
         *
         * @param proc the procedure to define
         */
        public final synchronized void defineProc(
                Procedure proc)
        {
                _clock++;
                _procs.remove(proc.getName());
                _procs.put(proc.getName(), proc);
        }


        /**
         * Get a procedure
         *
         * @param name name of the procedure to look for
         *
         * @return the procedure, or null if not found
         */
        public final synchronized Procedure resolveProc(
                CaselessString name)
        {
                return (Procedure)(_procs.get(name));
        }


        /**
         * Erase procedure
         *
         * @param name name of the procedure to erase
         *
         * @return true iff procedure existed
         */
        public final synchronized boolean eraseProc(
                CaselessString name)
        {
                return (_procs.remove(name) != null);
        }


        /**
         * Get LogoList of defined procedure names
         *
         * @return the procedure list
         */
        public final synchronized LogoList getProcList()
        {
                Vector v = new Vector();
                Enumeration enumer = _procs.keys();
                
                while (enumer.hasMoreElements())
                {
                        v.addElement(new LogoWord((CaselessString)(enumer.nextElement())));
                }
                
                return new LogoList(v);
        }


        /**
         * Make a global symbol name
         *
         * @param name the name of the variable
         * @param obj value of the variable
         */
        public final synchronized void makeName(
                CaselessString name,
                LogoObject obj)
        {
                _syms.makeForced(name, obj);
        }


        /**
         * Get value of a variable.
         *
         * @param name the name of the variable
         *
         * @return the resolved symbol, or null if not found
         */
        public final synchronized LogoObject resolveName(
                CaselessString name)
        {
                return _syms.resolve(name);
        }


        /**
         * Erase a variable.
         *
         * @param name the name of the variable
         */
        public final synchronized void eraseName(
                CaselessString name)
        {
                _syms.erase(name);
        }


        /**
         * Get LogoList of names
         *
         * @return the name list
         */
        public final synchronized LogoObject getNameList()
        {
                return _syms.getNames();
        }


        /**
         * Make a property
         *
         * @param name property list name
         * @param key property key name
         * @param obj value of the property
         *
         * @exception virtuoso.logo.LanguageException invalid access to system property
         */
        public final synchronized void putProp(
                CaselessString name,
                CaselessString key,
                LogoObject obj)
        throws
                LanguageException
        {
                if (name.equals(LOGO_PROPERTYLIST_NAME))
                {
                        if (key.equals("LOGO.CASESENSITIVE"))
                        {
                                CaselessString.setSensitivity(obj.toBoolean());
                        }
                        else if (key.equals("LOGO.OVERRIDEPRIMITIVES"))
                        {
                                _overridePrimitive = obj.toBoolean();
                        }
                        else if (key.equals("LOGO.AUTOIGNORE"))
                        {
                                _autoIgnore = obj.toBoolean();
                        }
                        else
                        {
                                throw new LanguageException("Can't write to property " + key.toString() +
                                        " in system property list.");
                        }
                }
                else
                {
                        Hashtable list = (Hashtable)(_proplists.get(name));
                        if (list == null)
                        {
                                list = new Hashtable();
                                _proplists.put(name, list);
                        }
                        list.remove(key);
                        list.put(key, obj);
                }
        }


        /**
         * Get value of a property
         *
         * @param name property list name
         * @param key property key name
         *
         * @return value of the property
         *
         * @exception virtuoso.logo.LanguageException invalid access to system property
         */
        public final synchronized LogoObject getProp(
                CaselessString name,
                CaselessString key)
        throws
                LanguageException
        {
                if (name.equals(LOGO_PROPERTYLIST_NAME))
                {
                        if (key.equals("LOGO.CASESENSITIVE"))
                        {
                                return new LogoWord(CaselessString.getSensitivity());
                        }
                        else if (key.equals("LOGO.OVERRIDEPRIMITIVES"))
                        {
                                return new LogoWord(_overridePrimitive);
                        }
                        else if (key.equals("LOGO.AUTOIGNORE"))
                        {
                                return new LogoWord(_autoIgnore);
                        }
                        else if (key.equals("LOGO.VERSION"))
                        {
                                return new LogoWord(TURTLETRACKS_VERSION);
                        }
                        else if (key.equals("JAVA.VERSION"))
                        {
                                return new LogoWord(System.getProperty("java.version"));
                        }
                        else if (key.equals("OS.NAME"))
                        {
                                return new LogoWord(System.getProperty("os.name"));
                        }
                        else if (key.equals("OS.ARCH"))
                        {
                                return new LogoWord(System.getProperty("os.arch"));
                        }
                        else
                        {
                                throw new LanguageException("Can't read property " + key.toString() +
                                        " in system property list.");
                        }
                }
                else
                {
                        Hashtable list = (Hashtable)(_proplists.get(name));
                        if (list != null)
                        {
                                LogoObject ret = (LogoObject)(list.get(key));
                                if (ret != null)
                                {
                                        return ret;
                                }
                        }
                }
                return new LogoList();
        }


        /**
         * Remove a property
         *
         * @param name property list name
         * @param key property key name
         *
         * @exception virtuoso.logo.LanguageException invalid access to system property
         */
        public final synchronized void remProp(
                CaselessString name,
                CaselessString key)
        throws
                LanguageException
        {
                if (name.equals(LOGO_PROPERTYLIST_NAME))
                {
                        throw new LanguageException("Can't remove properties from system property list.");
                }
                else
                {
                        Hashtable list = (Hashtable)(_proplists.get(name));
                        if (list != null)
                        {
                                list.remove(key);
                                if (list.isEmpty())
                                {
                                        _proplists.remove(name);
                                }
                        }
                }
        }


        /**
         * Get list of property list names
         *
         * @return LogoList of list names
         */
        public final synchronized LogoList getPLists()
        {
                Vector v = new Vector();
                Enumeration enumer = _proplists.keys();
                
                while (enumer.hasMoreElements())
                {
                        v.addElement(new LogoWord((CaselessString)(enumer.nextElement())));
                }
                
                return new LogoList(v);
        }


        /**
         * Get properties in given list
         *
         * @param name property list name
         */
        public final synchronized LogoList getPropsInList(
                CaselessString name)
        {
                Hashtable list = (Hashtable)(_proplists.get(name));
                if (list == null)
                {
                        return new LogoList();
                }
                else
                {
                        Vector v = new Vector();
                        Enumeration enumer = list.keys();
                        
                        while (enumer.hasMoreElements())
                        {
                                v.addElement(new LogoWord((CaselessString)(enumer.nextElement())));
                        }
                        
                        return new LogoList(v);
                }
        }


        /**
         * Get props in proper format for Runtime.exec()
         *
         * @param name property list name
         */
        public final synchronized String[] getPropsForExec(
                CaselessString name)
        {
                Hashtable list = (Hashtable)(_proplists.get(name));
                if (list == null)
                {
                        return null;
                }
                else
                {
                        String[] ret = new String[list.size()];
                        Enumeration enumer = list.keys();
                        int i=0;
                        
                        while (enumer.hasMoreElements())
                        {
                                CaselessString key = (CaselessString)(enumer.nextElement());
                                LogoObject obj = (LogoObject)(list.get(key));
                                if (obj instanceof LogoList)
                                {
                                        ret[i] = key.str + '=' + ((LogoList)obj).toStringOpen();
                                }
                                else
                                {
                                        ret[i] = key.str + '=' + ((LogoList)obj).toString();
                                }
                                i++;
                        }
                        
                        return ret;
                }
        }


        /**
         * Erase all procedures. names, and property lists
         */
        public final synchronized void eraseAll()
        {
                _syms.eraseAll();
                _procs.clear();
                _proplists.clear();
        }


        /**
         * Register a fileid
         *
         * @param fileid the fileid to register
         * @param io the IOInterface to register
         *
         * @exception virtuoso.logo.LanguageException duplicate file id
         */
        public final void registerIO(
                CaselessString fileid,
                IOBase io)
        throws
                LanguageException
        {
                synchronized (_fileids)
                {
                        if (_fileids.containsKey(fileid))
                        {
                                throw new LanguageException("Duplicate fileid");
                        }
                        _fileids.put(fileid, io);
                }
        }


        /**
         * Get a stream given the id
         *
         * @param fileid fileid
         *
         * @return the IOInterface, or null if not found
         */
        public final IOBase getIO(
                CaselessString fileid)
        {
                synchronized (_fileids)
                {
                        return (IOBase)(_fileids.get(fileid));
                }
        }


        /**
         * Erase stream given the id
         *
         * @param fileid fileid
         *
         * @exception virtuoso.logo.LanguageException fileid is closed already
         */
        public final void closeIO(
                CaselessString fileid)
        throws
                LanguageException
        {
                synchronized (_fileids)
                {
                        IOBase io = (IOBase)(_fileids.remove(fileid));
                        if (io == null)
                        {
                                throw new LanguageException("Stream is closed");
                        }
                        else
                        {
                                io.close();
                        }
                }
        }


        /**
         * Close all streams
         */
        public final void closeAllIO()
        {
                synchronized (_fileids)
                {
                        Enumeration enumer = _fileids.keys();
                        
                        try
                        {
                                while (enumer.hasMoreElements())
                                {
                                        closeIO((CaselessString)(enumer.nextElement()));
                                }
                        }
                        catch (LanguageException e) {}
                }
        }


        /**
         * Get LogoList of fileids
         *
         * @return the procedure list
         */
        public final LogoList getFileidList()
        {
                Vector v = new Vector();
                
                synchronized (_fileids)
                {
                        Enumeration enumer = _fileids.keys();
                        
                        while (enumer.hasMoreElements())
                        {
                                v.addElement(new LogoWord((CaselessString)(enumer.nextElement())));
                        }
                }
                
                return new LogoList(v);
        }


        /**
         * Get new unique integer
         *
         * @return the unique number
         */
        public final int getUniqueNum()
        {
                synchronized (_uniqueNumLock)
                {
                        _uniqueNum++;
                        return _uniqueNum;
                }
        }


        /**
         * Read and execute stream. Closes the stream afterwards.
         *
         * @param stream the stream to load from
         * @param thread thread running when LOAD instruction encountered
         *
         * @exception virtuoso.logo.LanguageException unable to execute
         */
        public final synchronized void executeStream(
                IOBase stream,
                InterpreterThread thread)
        throws
                LanguageException
        {
                try
                {
                        Tokenizer tokenizer = new Tokenizer(getTokenizerCommentFlags());
                        thread.startLoading(stream);
                        while (true)
                        {
                                if (stream.eof())
                                {
                                        break;
                                }
                                StringBuffer cumulative = new StringBuffer();
                                LogoList line = null;
                                while (true)
                                {
                                        if (stream.eof())
                                        {
                                                break;
                                        }
                                        try
                                        {
                                                cumulative.append(thread.inStream().getLine());
                                                if (cumulative.length() > 0)
                                                {
                                                        if (cumulative.charAt(cumulative.length()-1) == '~')
                                                        {
                                                                cumulative.setCharAt(cumulative.length()-1, ' ');
                                                        }
                                                        else
                                                        {
                                                                line = tokenizer.tokenize(cumulative.toString());
                                                                break;
                                                        }
                                                }
                                        }
                                        catch (LanguageException e)
                                        {
                                                char promptChar = e.getContChar();
                                                if (promptChar == '|' || promptChar == '\\' || promptChar == '~')
                                                {
                                                        cumulative.append(Machine.LINE_SEPARATOR);
                                                }
                                                else
                                                {
                                                        throw e;
                                                }
                                        }
                                }
                                if (line != null)
                                {
                                        line.getRunnable(thread.mach()).execute(new InterpEnviron(thread));
                                }
                        }
                }
                catch (LanguageException e)
                {
                        throw new LanguageException("Error while running: " + e.generateMessage());
                }
                catch (ThrowException e)
                {
                        throw new LanguageException("Error while running: " + e.toString());
                }
                finally
                {
                        stream.close();
                        thread.endLoading();
                }
        }


        /**
         * Printout specified info to stream
         *
         * @param stream the stream to print to
         * @param procs the procedures to print
         * @param names the names to print
         * @param plists the property lists to print
         *
         * @exception virtuoso.logo.LanguageException unable to print
         */
        public final synchronized void printout(
                IOBase stream,
                LogoObject procs,
                LogoObject names,
                LogoObject plists)
        throws
                LanguageException
        {
                Enumeration enumer;
                
                if (names != null)
                {
                        enumer = names.enumerateCaselessStrings();
                }
                else
                {
                        enumer = _syms.getEnumeratedNames();
                }
                while (enumer.hasMoreElements())
                {
                        CaselessString sym = (CaselessString)(enumer.nextElement());
                        LogoObject obj = _syms.resolve(sym);
                        if (obj == null)
                        {
                                throw new LanguageException(sym + " has no value.");
                        }
                        String symStr = sym.unparse();
                        String valStr = obj.unparse();
                        if (obj instanceof LogoList)
                        {
                                stream.putLine("MAKE \"" + symStr + ' ' + valStr);
                        }
                        else if (obj instanceof LogoWord)
                        {
                                stream.putLine("MAKE \"" + symStr + " \"" + valStr);
                        }
                }
                
                if (plists != null)
                {
                        enumer = plists.enumerateCaselessStrings();
                }
                else
                {
                        enumer = _proplists.keys();
                }
                while (enumer.hasMoreElements())
                {
                        CaselessString sym = (CaselessString)(enumer.nextElement());
                        String symStr = sym.unparse();
                        Hashtable list = (Hashtable)(_proplists.get(sym));
                        if (list != null)
                        {
                                Enumeration enum2 = list.keys();
                                while (enum2.hasMoreElements())
                                {
                                        CaselessString key = (CaselessString)(enum2.nextElement());
                                        LogoObject obj = (LogoObject)(list.get(key));
                                        String keyStr = key.unparse();
                                        String valStr = obj.unparse();
                                        if (obj instanceof LogoList)
                                        {
                                                stream.putLine("PPROP \"" + symStr +  " \"" + keyStr + ' ' + valStr);
                                        }
                                        else if (obj instanceof LogoWord)
                                        {
                                                stream.putLine("PPROP \"" + symStr +  " \"" + keyStr + " \"" + valStr);
                                        }
                                }
                        }
                }
                
                if (procs != null)
                {
                        enumer = procs.enumerateCaselessStrings();
                }
                else
                {
                        enumer = _procs.keys();
                }
                while (enumer.hasMoreElements())
                {
                        CaselessString sym = (CaselessString)(enumer.nextElement());
                        Procedure proc = (Procedure)(_procs.get(sym));
                        if (proc == null)
                        {
                                throw new LanguageException("I don't know how to " + sym);
                        }
                        stream.putLine("");
                        proc.writeToIO(stream);
                }
        }

}



