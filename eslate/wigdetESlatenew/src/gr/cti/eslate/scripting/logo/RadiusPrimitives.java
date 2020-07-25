package gr.cti.eslate.scripting.logo; /**/

import java.util.*;
import virtuoso.logo.*;

import gr.cti.eslate.stage.models.HasRadius;

/**
 * @version     2.0.11, 26-Jun-2007
 * @author      George Birbilis
 * @author      Nikos Drossos
 * @author      Kriton Kyrimis
 */
public class RadiusPrimitives extends PrimitiveGroup /**/
{

    MyMachine myMachine; //new-scripting-mechanism

    protected void setup(Machine machine, Console console)
        throws SetupException
    {
        registerPrimitive("SETRADIUS", "pSETRADIUS", 1); /**/
        registerPrimitive("RADIUS", "pRADIUS", 0);       /**/
        registerPrimitive("SETDIAMETER", "pSETDIAMETER", 1); /**/
        registerPrimitive("DIAMETER", "pDIAMETER", 0);       /**/

        myMachine=(MyMachine)machine;

        if(console != null)
            console.putSetupMessage("Loaded Physics' Radius primitives"); /**/
    }

//RADIUS//

    public final LogoObject pSETRADIUS(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,1);

       double radius=alogoobject[0].toNumber();

       Vector<?> v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.stage.models.HasRadius.class);
       for(int i=0;i<v.size();i++) ((HasRadius)v.elementAt(i)).setRadius(radius);
       return LogoVoid.obj;
    }

    public final LogoObject pRADIUS(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,0);

      Vector<?> v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.stage.models.HasRadius.class);
      try{return new LogoWord(((HasRadius)v.firstElement()).getRadius());
      }catch(NoSuchElementException e){throw new LanguageException("There is no object to TELL this to");}

    }

//DIAMETER=RADIUS*2//

    public final LogoObject pSETDIAMETER(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,1);

       double radius=alogoobject[0].toNumber()/2d;

       Vector<?> v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.stage.models.HasRadius.class);
       for(int i=0;i<v.size();i++) ((HasRadius)v.elementAt(i)).setRadius(radius);
       return LogoVoid.obj;
    }

    public final LogoObject pDIAMETER(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,0);

      Vector<?> v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.stage.models.HasRadius.class);
      try{return new LogoWord(((HasRadius)v.firstElement()).getRadius()*2);
      }catch(NoSuchElementException e){throw new LanguageException("There is no object to TELL this to");}

    }


}