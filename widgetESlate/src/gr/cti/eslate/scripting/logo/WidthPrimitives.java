package gr.cti.eslate.scripting.logo; /**/

import java.util.*;
import virtuoso.logo.*;

import gr.cti.eslate.stage.models.HasWidth;

/**
 * @version     2.0.11, 26-Jun-2007
 * @author      George Birbilis
 * @author      Nikos Drossos
 * @author      Kriton Kyrimis
 */
public class WidthPrimitives extends PrimitiveGroup /**/
{

    MyMachine myMachine; //new-scripting-mechanism

    protected void setup(Machine machine, Console console)
        throws SetupException
    {
        registerPrimitive("SETWIDTH", "pSETWIDTH", 1); /**/
        registerPrimitive("WIDTH", "pWIDTH", 0);       /**/

        myMachine=(MyMachine)machine;

        if(console != null)
            console.putSetupMessage("Loaded Physics' Width primitives"); /**/
    }

//WIDTH//

    public final LogoObject pSETWIDTH(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,1);

       double width=alogoobject[0].toNumber();

       Vector<?> v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.stage.models.HasWidth.class);
       for(int i=0;i<v.size();i++) ((HasWidth)v.elementAt(i)).setObjectWidth(width);
       return LogoVoid.obj;
    }

    public final LogoObject pWIDTH(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,0);

      Vector<?> v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.stage.models.HasWidth.class);
      try{return new LogoWord(((HasWidth)v.firstElement()).getObjectWidth());
      }catch(NoSuchElementException e){throw new LanguageException("There is no object to TELL this to");}

    }

}