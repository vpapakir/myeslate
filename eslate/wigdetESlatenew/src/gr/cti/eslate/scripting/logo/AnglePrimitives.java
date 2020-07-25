package gr.cti.eslate.scripting.logo; /**/

import java.util.*;
import virtuoso.logo.*;

import gr.cti.eslate.stage.models.HasAngle;

/**
 * @version     2.0.11, 26-Jun-2007
 * @author      George Birbilis
 * @author      Nikos Drossos
 * @author      Kriton Kyrimis
 */
public class AnglePrimitives extends PrimitiveGroup /**/
{

    MyMachine myMachine; //new-scripting-mechanism

    protected void setup(Machine machine, Console console)
        throws SetupException
    {
        registerPrimitive("SETANGLE", "pSETANGLE", 1); /**/
        registerPrimitive("ANGLE", "pANGLE", 0);       /**/

        myMachine=(MyMachine)machine;

        if(console != null)
            console.putSetupMessage("Loaded Physics' Angle primitives"); /**/
    }

//ANGLE//

    public final LogoObject pSETANGLE(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,1);

       double angle=alogoobject[0].toNumber();

       Vector<?> v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.stage.models.HasAngle.class);
       for(int i=0;i<v.size();i++) ((HasAngle)v.elementAt(i)).setAngle(angle);
       return LogoVoid.obj;
    }

    public final LogoObject pANGLE(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,0);

      Vector<?> v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.stage.models.HasAngle.class);
      try{return new LogoWord(((HasAngle)v.firstElement()).getAngle());
      }catch(NoSuchElementException e){throw new LanguageException("There is no object to TELL this to");}

    }

}