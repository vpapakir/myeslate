package gr.cti.eslate.scripting.logo; /**/

import java.util.*;

import virtuoso.logo.*;

import gr.cti.shapes.DoublePoint2D;
import gr.cti.eslate.scripting.logo.convertions.*;
import gr.cti.eslate.stage.models.*;

/**
 * @version     2.0.11, 26-Jun-2007
 * @author      George Birbilis
 * @author      Nikos Drossos
 * @author      Kriton Kyrimis
 */
public class Acceleration2DPrimitives extends PrimitiveGroup /**/
{

    MyMachine myMachine; //new-scripting-mechanism

    protected void setup(Machine machine, Console console)
        throws SetupException
    {
        registerPrimitive("SETACCELERATION", "pSETACCELERATION", 1); /**/
        registerPrimitive("ACCELERATION", "pACCELERATION", 0);       /**/

        myMachine=(MyMachine)machine;

        if(console != null)
            console.putSetupMessage("Loaded Physics's Acceleration primitives"); //13Oct1999: was saying "Velocity" instead of "Acceleration"
    }

//ACCELERATION//

    public final LogoObject pSETACCELERATION(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,1);

       DoublePoint2D acceleration=LogoJava2D.toDoublePoint2D(alogoobject[0]);

       Vector<?> v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.stage.models.HasAcceleration2D.class); //15Oct1999: fixed: was searching for a HasVelocity object (instead of HasAcceleration)
       for(int i=0;i<v.size();i++) ((HasAcceleration2D)v.elementAt(i)).setAcceleration(acceleration); //13Oct1999: fixed: was casting to a HasVelocity object and doing a setVelocity (instead of HasAcceleration/setAcceleration)
       return LogoVoid.obj;
    }

    public final LogoObject pACCELERATION(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,0);

       DoublePoint2D acceleration;
       Vector<?> v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.stage.models.HasAcceleration2D.class); //15Oct1999: fixed: was searching for a HasVelocity object (instead of HasAcceleration)
       try{acceleration=((HasAcceleration2D)v.firstElement()).getAcceleration();
       }catch(NoSuchElementException e){throw new LanguageException("There is no object to TELL this to");}

       return LogoJava2D.toLogoObject(acceleration);
    }

}