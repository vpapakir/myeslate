package gr.cti.eslate.scripting.logo; /**/

import java.util.*;
import virtuoso.logo.*;

import gr.cti.eslate.stage.models.HasKineticEnergy;

/**
 * @version     2.0.11, 26-Jun-2007
 * @author      George Birbilis
 * @author      Nikos Drossos
 * @author      Kriton Kyrimis
 */
public class KineticEnergyPrimitives extends PrimitiveGroup /**/
{

    MyMachine myMachine; //new-scripting-mechanism

    protected void setup(Machine machine, Console console)
        throws SetupException
    {
//        registerPrimitive("SETKINETICENERGY", "pSETKINETICENERGY", 1); /**/
        registerPrimitive("KINETICENERGY", "pKINETICENERGY", 0);       /**/

        myMachine=(MyMachine)machine;

        if(console != null)
            console.putSetupMessage("Loaded Physics' KineticEnergy primitives"); /**/
    }

/*
//KINETICENERGY//

    public final LogoObject pSETKINETICENERGY(InterpEnviron interpenviron, LogoObject alogoobject[])
        throws LanguageException
    {
       testNumParams(alogoobject,1);

       double mass=alogoobject[0].toNumber();

       Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.stage.models.HasKineticEnergy.class);
       for(int i=0;i<v.size();i++) ((HasKineticEnergy)v.elementAt(i)).setKineticEnergy(mass);
       return LogoVoid.obj;
    }
*/

    public final LogoObject pKINETICENERGY(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,0);

      Vector<?> v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.stage.models.HasKineticEnergy.class);
      try{return new LogoWord(((HasKineticEnergy)v.firstElement()).getKineticEnergy());
      }catch(NoSuchElementException e){throw new LanguageException("There is no object to TELL this to");}

    }

}