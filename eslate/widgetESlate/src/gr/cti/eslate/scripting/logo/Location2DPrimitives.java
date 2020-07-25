package gr.cti.eslate.scripting.logo; /**/

import java.util.*;
import java.awt.geom.Point2D;

import virtuoso.logo.*;

import gr.cti.eslate.scripting.logo.convertions.*;
import gr.cti.eslate.stage.models.*;

/**
 * @version     2.0.11, 26-Jun-2007
 * @author      George Birbilis
 * @author      Nikos Drossos
 * @author      Kriton Kyrimis
 */
public class Location2DPrimitives extends PrimitiveGroup /**/
{

    MyMachine myMachine; //new-scripting-mechanism

    protected void setup(Machine machine, Console console)
        throws SetupException
    {
        try {
        registerPrimitive("SETLOCATION", "pSETLOCATION", 1); /**/
        registerPrimitive("LOCATION", "pLOCATION", 0);       /**/

        myMachine=(MyMachine)machine;

        if(console != null)
            console.putSetupMessage("Loaded Physics' Location primitives"); /**/
        } catch (Exception e) {
          e.printStackTrace();
        }
    }

//LOCATION//

    public final LogoObject pSETLOCATION(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,1);

       Point2D location=LogoJava2D.toDoublePoint2D(alogoobject[0]);

       Vector<?> v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.stage.models.HasLocation2D.class);
       for(int i=0;i<v.size();i++) ((HasLocation2D)v.elementAt(i)).setLocation2D(location);
       return LogoVoid.obj;
    }

    public final LogoObject pLOCATION(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,0);

       Point2D location;
       Vector<?> v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.stage.models.HasLocation2D.class);
       try{
        location=((HasLocation2D)v.firstElement()).getLocation2D();
       }catch(NoSuchElementException e){throw new LanguageException("There is no object to TELL this to");}

       return LogoJava2D.toLogoObject(location);
    }

}
