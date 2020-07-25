package gr.cti.eslate.scripting.logo; /**/

import java.util.*;

import virtuoso.logo.*;

import java.awt.geom.Point2D;

import gr.cti.eslate.scripting.logo.convertions.*;
import gr.cti.eslate.stage.models.*;

/**
 * @version     2.0.11, 26-Jun-2007
 * @author      George Birbilis
 * @author      Nikos Drossos
 * @author      Kriton Kyrimis
 */
public class AppliedForcePrimitives extends PrimitiveGroup /**/
{

    MyMachine myMachine; //new-scripting-mechanism

    protected void setup(Machine machine, Console console)
        throws SetupException
    {
        registerPrimitive("SETAPPLIEDFORCE", "pSETAPPLIEDFORCE", 1); /**/
        registerPrimitive("APPLIEDFORCE", "pAPPLIEDFORCE", 0);       /**/

        myMachine=(MyMachine)machine;

        if(console != null)
            console.putSetupMessage("Loaded Physics' AppliedForce primitives"); //13Oct1999: was saying "Velocity" instead of "AppliedForce"
    }

//APPLIEDFORCE//

    public final LogoObject pSETAPPLIEDFORCE(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,1);

       Point2D appliedForce=LogoJava2D.toPoint2D(alogoobject[0]);

       Vector<?> v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.stage.models.HasAppliedForce.class); //15Oct1999: fixed: was searching for a HasVelocity object (instead of HasAppliedForce)
       for(int i=0;i<v.size();i++) ((HasAppliedForce)v.elementAt(i)).setAppliedForce(appliedForce); //13Oct1999: fixed: was casting to a HasVelocity object and doing a setVelocity (instead of HasAppliedForce/setAppliedForce)
       return LogoVoid.obj;
    }

    public final LogoObject pAPPLIEDFORCE(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,0);

       Point2D appliedForce;
       Vector<?> v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.stage.models.HasAppliedForce.class); //15Oct1999: fixed: was searching for a HasVelocity object (instead of HasAppliedForce)
       try{appliedForce=((HasAppliedForce)v.firstElement()).getAppliedForce();
       }catch(NoSuchElementException e){throw new LanguageException("There is no object to TELL this to");}

       return LogoJava2D.toLogoObject(appliedForce);
    }

}