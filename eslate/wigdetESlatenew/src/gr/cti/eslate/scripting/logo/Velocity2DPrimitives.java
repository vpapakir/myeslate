package gr.cti.eslate.scripting.logo; /**/

import java.util.*;
import gr.cti.shapes.DoublePoint2D;

import virtuoso.logo.*;

import gr.cti.eslate.scripting.logo.convertions.*;
import gr.cti.eslate.stage.models.*;

/**
 * @version     2.0.11, 26-Jun-2007
 * @author      George Birbilis
 * @author      Nikos Drossos
 * @author      Kriton Kyrimis
 */
public class Velocity2DPrimitives extends PrimitiveGroup /**/
{

    MyMachine myMachine; //new-scripting-mechanism

    protected void setup(Machine machine, Console console)
        throws SetupException
    {
        registerPrimitive("SETVELOCITY", "pSETVELOCITY", 1); /**/
        registerPrimitive("VELOCITY", "pVELOCITY", 0);       /**/

        myMachine=(MyMachine)machine;

        if(console != null)
            console.putSetupMessage("Loaded Physics' Velocity primitives"); /**/
    }

//VELOCITY//

    public final LogoObject pSETVELOCITY(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,1);

       DoublePoint2D velocity=LogoJava2D.toDoublePoint2D(alogoobject[0]);

       Vector<?> v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.stage.models.HasVelocity2D.class);
       for(int i=0;i<v.size();i++) ((HasVelocity2D)v.elementAt(i)).setVelocity(velocity);
       return LogoVoid.obj;
    }

    public final LogoObject pVELOCITY(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,0);

       DoublePoint2D velocity;
       Vector<?> v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.stage.models.HasVelocity2D.class);
       try{velocity=((HasVelocity2D)v.firstElement()).getVelocity();
       }catch(NoSuchElementException e){throw new LanguageException("There is no object to TELL this to");}

       return LogoJava2D.toLogoObject(velocity);
    }

}