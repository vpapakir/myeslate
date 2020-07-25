//24Feb2000: using LogoAWT2 instead of LogoAWT to allow transparent colors!

package gr.cti.eslate.scripting.logo; /**/

import java.util.*;
import java.awt.Color;

import virtuoso.logo.*;

import gr.cti.eslate.scripting.logo.convertions.*;
import gr.cti.eslate.stage.models.*;

/**
 * @version     2.0.11, 26-Jun-2007
 * @author      George Birbilis
 * @author      Nikos Drossos
 * @author      Kriton Kyrimis
 */
public class ColorPrimitives extends PrimitiveGroup /**/
{

    MyMachine myMachine; //new-scripting-mechanism

    protected void setup(Machine machine, Console console)
        throws SetupException
    {
        registerPrimitive("SETCOLOR", "pSETCOLOR", 1); /**/
        registerPrimitive("COLOR", "pCOLOR", 0);       /**/

        myMachine=(MyMachine)machine;

        if(console != null)
            console.putSetupMessage("Loaded Physics' Color primitives"); /**/
    }

//COLOR//

    @SuppressWarnings("unchecked")
    public final LogoObject pSETCOLOR(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,1);

       Color color=LogoAWT2.toColor(alogoobject[0]);

       Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.stage.models.HasColor.class);
       for(int i=0;i<v.size();i++) ((HasColor)v.elementAt(i)).setColor(color);
       return LogoVoid.obj;
    }

    @SuppressWarnings("unchecked")
    public final LogoObject pCOLOR(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,0);

       Color color;
       Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.stage.models.HasColor.class);
       try{color=((HasColor)v.firstElement()).getColor();
       }catch(NoSuchElementException e){throw new LanguageException("There is no object to TELL this to");}

       return LogoAWT2.toLogoObject(color);
    }

}
