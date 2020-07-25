//31-5-2000: moved to "gr.cti.eslate.scripting.logo" package

//CHANGE THIS TO TALK THROUGH AN INTERFACE TO CLASSES DYNAMICALLY LOADED AT RUNTIME
//SO THAT WHEN THIS CLASS GETS LOADED, THE CLASS LOADER WON'T BRING UP THE WHOLE QTJ WITH IT

package gr.cti.eslate.scripting.logo;

import virtuoso.logo.*;

import quicktime.*;
import quicktime.std.music.*;

public class SoundPrimitives extends PrimitiveGroup
{
 NoteAllocator na;
 boolean sessionOpened=false;

    protected void setup(Machine machine, Console console)
        throws SetupException
    {
//   try{
//    QTSession.open();
//    na=new NoteAllocator();
      registerPrimitive("SOUND", "pSOUND", 4);
      _mach = machine;
      _console = console;
      if(console != null)
       console.putSetupMessage("Loaded QuickTime SOUND primitive");
//   }catch(Exception e){}
    }

    protected void exiting()
    {
/*        synchronized(this)
        {
        }
*/   if (sessionOpened) QTSession.close();
    }

    public final LogoObject pSOUND(InterpEnviron interpenviron, LogoObject alogoobject[])
        throws LanguageException
    {
     int inst,note,pitch,duration;
        if(alogoobject.length == 4)
        {
//         Synchronized(this){ //Synchro risk: get'em+use'em in the same synchro block
            inst = alogoobject[0].toInteger();
            note = alogoobject[1].toInteger();
            pitch = alogoobject[2].toInteger();
            duration = alogoobject[3].toInteger();

            System.out.println("inst="+inst+" note="+note+" pitch="+pitch+" duration="+duration);
//         }
        }
        else
        if(alogoobject.length == 0)
        {
         if(_console != null)
            _console.putLine("Syntax: SOUND inst note pitch duration");
            return LogoVoid.obj; //return nothing
        }
        else
        {
            throw new LanguageException("Wrong number of arguments");
        }

  if (!sessionOpened)
  try{
      QTSession.open();
      na=new NoteAllocator();
      sessionOpened=true;
     }catch(Exception e){System.out.println("Couldn't open QT session!");}

        synchronized(this)
        {
         try{NoteChannel nc = new NoteChannel(na, inst);
             nc.playNote(note,pitch);
             delay(na,duration);
             nc.playNote (note, 0); //shut-up
            }catch(Exception e){}
        }
        return LogoVoid.obj;
    }

    Machine _mach;
    Console _console;

        static void delay (NoteAllocator na, long ms) {
                try { na.task(); Thread.sleep(ms); }
                catch (Exception e) { e.printStackTrace(); }
        }

}
