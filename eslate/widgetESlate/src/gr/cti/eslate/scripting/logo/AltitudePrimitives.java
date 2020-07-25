package gr.cti.eslate.scripting.logo; /**/

import java.util.*;
import virtuoso.logo.*;

import gr.cti.eslate.stage.models.HasAltitude;

/**
 * @version     2.0.11, 26-Jun-2007
 * @author      George Birbilis
 * @author      Nikos Drossos
 * @author      Kriton Kyrimis
 */
public class AltitudePrimitives extends PrimitiveGroup //2Nov1999
{

    MyMachine myMachine; //new-scripting-mechanism

    protected void setup(Machine machine, Console console)
        throws SetupException
    {
        registerPrimitive("SETALTITUDE", "pSETALTITUDE", 1); /**/
        registerPrimitive("ALTITUDE", "pALTITUDE", 0);       /**/

        myMachine=(MyMachine)machine;

        if(console != null)
            console.putSetupMessage("Loaded Physics' Altitude primitives"); /**/
    }

//ALTITUDE//

    public final LogoObject pSETALTITUDE(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,1);

       double altitude=alogoobject[0].toNumber();

       Vector<?> v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.stage.models.HasAltitude.class);
       for(int i=0;i<v.size();i++) ((HasAltitude)v.elementAt(i)).setAltitude(altitude);
       return LogoVoid.obj;
    }

    public final LogoObject pALTITUDE(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,0);

      Vector<?> v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.stage.models.HasAltitude.class);
      try{return new LogoWord(((HasAltitude)v.firstElement()).getAltitude());
      }catch(NoSuchElementException e){throw new LanguageException("There is no object to TELL this to");}

    }

}