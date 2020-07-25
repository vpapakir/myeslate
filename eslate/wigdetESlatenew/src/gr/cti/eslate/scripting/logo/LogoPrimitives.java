package gr.cti.eslate.scripting.logo;

import java.util.*;
import virtuoso.logo.*;

public class LogoPrimitives extends PrimitiveGroup
{
    MyMachine myMachine;

    protected void setup(Machine machine, Console console)
        throws SetupException
    {
        registerPrimitive("EDALL","pEDALL", 0); //31-8-1998
        registerPrimitive("LOGO.EXECUTE","pEXECUTE",1); //28Sep1999
        registerPrimitive("LOGO.STOP","pSTOP", 0); //8Oct1999
        registerPrimitive("LOGO.PAUSE","pPAUSE",0); //8Oct1999
        registerPrimitive("LOGO.UNPAUSE","pUNPAUSE",0); //8Oct1999
        registerPrimitive("LOGO.TOGGLEPAUSE","pTOGGLEPAUSE",0); //8Oct1999

        //machine.loadPrimitiveGroups(scriptableObjects); //don't do that!!! would try to call loadPrimitives again which is a synchronized method and would lock-up (we're already in installPrimitives)

        myMachine=(MyMachine)machine;

        if(console != null)
            console.putSetupMessage("Loaded ESlate's Logo primitives"); //18-7-1998
    }

//EDALL//

    public final LogoObject pEDALL(InterpEnviron interpenviron, LogoObject params[]) /**/
        throws LanguageException
    {
       testNumParams(params,0);
       Vector<?> v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.logo.Logo.class);
       for(int i=0;i<v.size();i++) ((gr.cti.eslate.logo.Logo)v.elementAt(i)).editAll();
       return LogoVoid.obj;
    }

//LOGO.EXECUTE//

    public final LogoObject pEXECUTE(InterpEnviron interpenviron, LogoObject params[]) /**/
        throws LanguageException
    {
       testNumParams(params,1);
       String command=params[0].toString();

       Vector<?> v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.logo.Logo.class);
       for(int i=0;i<v.size();i++) ((gr.cti.eslate.logo.Logo)v.elementAt(i)).executeLogoCommand(command);
       return LogoVoid.obj;
    }

//LOGO.STOP//

    public final LogoObject pSTOP(InterpEnviron interpenviron, LogoObject params[]) /**/
        throws LanguageException
    {
       testNumParams(params,0);
       Vector<?> v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.logo.Logo.class);
       for(int i=0;i<v.size();i++) ((gr.cti.eslate.logo.Logo)v.elementAt(i)).stop();
       return LogoVoid.obj;
    }

//LOGO.PAUSE//

    public final LogoObject pPAUSE(InterpEnviron interpenviron, LogoObject params[]) /**/
        throws LanguageException
    {
       testNumParams(params,0);
       Vector<?> v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.logo.Logo.class);
       for(int i=0;i<v.size();i++) ((gr.cti.eslate.logo.Logo)v.elementAt(i)).pause();
       return LogoVoid.obj;
    }

//LOGO.UNPAUSE//

    public final LogoObject pUNPAUSE(InterpEnviron interpenviron, LogoObject params[]) /**/
        throws LanguageException
    {
       testNumParams(params,0);
       Vector<?> v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.logo.Logo.class);
       for(int i=0;i<v.size();i++) ((gr.cti.eslate.logo.Logo)v.elementAt(i)).unpause();
       return LogoVoid.obj;
    }

//LOGO.TOGGLEPAUSE//

    public final LogoObject pTOGGLEPAUSE(InterpEnviron interpenviron, LogoObject params[]) /**/
        throws LanguageException
    {
       testNumParams(params,0);
       Vector<?> v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.logo.Logo.class);
       for(int i=0;i<v.size();i++) ((gr.cti.eslate.logo.Logo)v.elementAt(i)).togglePausedState();
       return LogoVoid.obj;
    }

}
