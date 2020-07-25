package gr.cti.eslate.scripting.logo;

import java.util.*;
import virtuoso.logo.*;

import gr.cti.eslate.stage.models.AsSpring;

/**
 * @version     2.0.11, 26-Jun-2007
 * @author      George Birbilis
 * @author      Nikos Drossos
 * @author      Kriton Kyrimis
 */
public class SpringPrimitives extends PrimitiveGroup
{

    MyMachine myMachine; //new-scripting-mechanism

    protected void setup(Machine machine, Console console)
        throws SetupException
    {
        registerPrimitive("SETSPRINGCONSTANT", "pSETSPRINGCONSTANT", 1);
        registerPrimitive("SPRINGCONSTANT", "pSPRINGCONSTANT", 0);

        registerPrimitive("SETNATURALLENGTH", "pSETNATURALLENGTH", 1);
        registerPrimitive("NATURALLENGTH", "pNATURALLENGTH", 0);

        registerPrimitive("SETMAXIMUMLENGTH", "pSETMAXIMUMLENGTH", 1);
        registerPrimitive("MAXIMUMLENGTH", "pMAXIMUMLENGTH", 0);

        registerPrimitive("SETDISPLACEMENT", "pSETDISPLACEMENT", 1); //30Aug1999 //2Nov1999: the primitive name is now "SETDISPLACEMENT"
        registerPrimitive("DISPLACEMENT", "pDISPLACEMENT", 0); //2Nov1999: the primitive name is now "DISPLACEMENT"

        myMachine=(MyMachine)machine;

        if(console != null)
            console.putSetupMessage("Loaded Physics' Spring primitives");
    }

//utility method//

 private Vector<?> getKnownObjects(){ //2Nov1999
  return myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.stage.models.AsSpring.class);
 }

//SPRINGCONSTANT//

    public final LogoObject pSETSPRINGCONSTANT(InterpEnviron interpenviron, LogoObject alogoobject[])
        throws LanguageException
    {
       testNumParams(alogoobject,1);

       double k=alogoobject[0].toNumber();

       Vector<?> v=getKnownObjects();
       for(int i=0;i<v.size();i++) ((AsSpring)v.elementAt(i)).setSpringConstant(k);
       return LogoVoid.obj;
    }

    public final LogoObject pSPRINGCONSTANT(InterpEnviron interpenviron, LogoObject alogoobject[])
        throws LanguageException
    {
       testNumParams(alogoobject,0);

      Vector<?> v=getKnownObjects();
      try{return new LogoWord(((AsSpring)v.firstElement()).getSpringConstant());
      }catch(NoSuchElementException e){throw new LanguageException("There is no object to TELL this to");}

    }

//NATURALLENGTH//

    public final LogoObject pSETNATURALLENGTH(InterpEnviron interpenviron, LogoObject alogoobject[])
        throws LanguageException
    {
       testNumParams(alogoobject,1);

       double len=alogoobject[0].toNumber();

       Vector<?> v=getKnownObjects();
       for(int i=0;i<v.size();i++) ((AsSpring)v.elementAt(i)).setNaturalLength(len);
       return LogoVoid.obj;
    }

    public final LogoObject pNATURALLENGTH(InterpEnviron interpenviron, LogoObject alogoobject[])
        throws LanguageException
    {
      testNumParams(alogoobject,0);

      Vector<?> v=getKnownObjects();
      try{return new LogoWord(((AsSpring)v.firstElement()).getNaturalLength());
      }catch(NoSuchElementException e){throw new LanguageException("There is no object to TELL this to");}

    }

//MAXIMUMLENGTH//

    public final LogoObject pSETMAXIMUMLENGTH(InterpEnviron interpenviron, LogoObject alogoobject[])
        throws LanguageException
    {
       testNumParams(alogoobject,1);

       double len=alogoobject[0].toNumber();

       Vector<?> v=getKnownObjects();
       for(int i=0;i<v.size();i++) ((AsSpring)v.elementAt(i)).setMaximumLength(len);
       return LogoVoid.obj;
    }

    public final LogoObject pMAXIMUMLENGTH(InterpEnviron interpenviron, LogoObject alogoobject[])
        throws LanguageException
    {
       testNumParams(alogoobject,0);

      Vector<?> v=getKnownObjects();
      try{return new LogoWord(((AsSpring)v.firstElement()).getMaximumLength());
      }catch(NoSuchElementException e){throw new LanguageException("There is no object to TELL this to");}

    }

//DISPLACEMENT//   //30Aug1999

    public final LogoObject pSETDISPLACEMENT(InterpEnviron interpenviron, LogoObject alogoobject[])
        throws LanguageException
    {
       testNumParams(alogoobject,1);

       double d=alogoobject[0].toNumber();

       Vector<?> v=getKnownObjects();
       for(int i=0;i<v.size();i++) ((AsSpring)v.elementAt(i)).setDisplacement(d);
       return LogoVoid.obj;
    }

    public final LogoObject pDISPLACEMENT(InterpEnviron interpenviron, LogoObject alogoobject[])
        throws LanguageException
    {
      testNumParams(alogoobject,0);

      Vector<?> v=getKnownObjects();
      try{return new LogoWord(((AsSpring)v.firstElement()).getDisplacement());
      }catch(NoSuchElementException e){throw new LanguageException("There is no object to TELL this to");}

    }

}