package gr.cti.eslate.scripting.logo; /**/

import java.util.*;
import virtuoso.logo.*;

import gr.cti.eslate.stage.models.HasHeight;

/**
 * @version     2.0.11, 26-Jun-2007
 * @author      George Birbilis
 * @author      Nikos Drossos
 * @author      Kriton Kyrimis
 */
public class HeightPrimitives extends PrimitiveGroup /**/
{

    MyMachine myMachine; //new-scripting-mechanism

    protected void setup(Machine machine, Console console)
        throws SetupException
    {
        registerPrimitive("SETHEIGHT", "pSETHEIGHT", 1); /**/
        registerPrimitive("HEIGHT", "pHEIGHT", 0);       /**/

        myMachine=(MyMachine)machine;

        if(console != null)
            console.putSetupMessage("Loaded Physics' Height primitives"); /**/
    }

//HEIGHT//

    public final LogoObject pSETHEIGHT(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,1);

       double height=alogoobject[0].toNumber();

       Vector<?> v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.stage.models.HasHeight.class);
       for(int i=0;i<v.size();i++) ((HasHeight)v.elementAt(i)).setObjectHeight(height);
       return LogoVoid.obj;
    }

    public final LogoObject pHEIGHT(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,0);

      Vector<?> v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.stage.models.HasHeight.class);
      try{return new LogoWord(((HasHeight)v.firstElement()).getObjectHeight());
      }catch(NoSuchElementException e){throw new LanguageException("There is no object to TELL this to");}

    }

}
