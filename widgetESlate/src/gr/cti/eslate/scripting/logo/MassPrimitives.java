package gr.cti.eslate.scripting.logo; /**/

import java.util.*;
import virtuoso.logo.*;

import gr.cti.eslate.stage.models.HasMass;

/**
 * @version     2.0.11, 26-Jun-2007
 * @author      George Birbilis
 * @author      Nikos Drossos
 * @author      Kriton Kyrimis
 */
public class MassPrimitives extends PrimitiveGroup /**/
{

    MyMachine myMachine; //new-scripting-mechanism

    protected void setup(Machine machine, Console console)
        throws SetupException
    {
        registerPrimitive("SETMASS", "pSETMASS", 1); /**/
        registerPrimitive("MASS", "pMASS", 0);       /**/

        myMachine=(MyMachine)machine;

        if(console != null)
            console.putSetupMessage("Loaded Physics' Mass primitives"); /**/
    }

//MASS//

    public final LogoObject pSETMASS(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,1);

       double mass=alogoobject[0].toNumber();

       Vector<?> v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.stage.models.HasMass.class);
       for(int i=0;i<v.size();i++) ((HasMass)v.elementAt(i)).setMass(mass);
       return LogoVoid.obj;
    }

    public final LogoObject pMASS(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,0);

      Vector<?> v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.stage.models.HasMass.class);
      try{return new LogoWord(((HasMass)v.firstElement()).getMass());
      }catch(NoSuchElementException e){throw new LanguageException("There is no object to TELL this to");}

    }

}