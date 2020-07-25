package gr.cti.eslate.scripting.logo; 

import java.util.*;
import virtuoso.logo.*;

import gr.cti.eslate.stage.models.HasLength;

/**
 * @version     2.0.11, 26-Jun-2007
 * @author      George Birbilis
 * @author      Nikos Drossos
 * @author      Kriton Kyrimis
 */
public class LengthPrimitives extends PrimitiveGroup
{

    MyMachine myMachine; //new-scripting-mechanism

    protected void setup(Machine machine, Console console)
        throws SetupException
    {
        registerPrimitive("SETLENGTH", "pSETLENGTH", 1);
        registerPrimitive("GETLENGTH", "pGETLENGTH", 0);

        myMachine=(MyMachine)machine;

        if(console != null)
            console.putSetupMessage("Loaded Physics' Length primitives");
    }

//LENGTH//

    public final LogoObject pSETLENGTH(InterpEnviron interpenviron, LogoObject alogoobject[])
        throws LanguageException
    {
       testNumParams(alogoobject,1);

       double mass=alogoobject[0].toNumber();

       Vector<?> v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.stage.models.HasLength.class);
       for(int i=0;i<v.size();i++) ((HasLength)v.elementAt(i)).setLength(mass);
       return LogoVoid.obj;
    }

    public final LogoObject pGETLENGTH(InterpEnviron interpenviron, LogoObject alogoobject[])
        throws LanguageException
    {
       testNumParams(alogoobject,0);

      Vector<?> v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.stage.models.HasLength.class);
      try{return new LogoWord(((HasLength)v.firstElement()).getLength());
      }catch(NoSuchElementException e){throw new LanguageException("There is no object to TELL this to");}

    }

}
