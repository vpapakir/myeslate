package gr.cti.eslate.scripting.logo;

import gr.cti.eslate.agent.AgentRefusesToChangePositionException;
import gr.cti.eslate.protocol.IAgent;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import virtuoso.logo.Console;
import virtuoso.logo.InterpEnviron;
import virtuoso.logo.LanguageException;
import virtuoso.logo.LogoObject;
import virtuoso.logo.LogoVoid;
import virtuoso.logo.LogoWord;
import virtuoso.logo.Machine;
import virtuoso.logo.MyMachine;
import virtuoso.logo.PrimitiveGroup;
import virtuoso.logo.SetupException;

public class AgentPrimitives extends PrimitiveGroup {
    private static ResourceBundle bundle,bundleMessages;

    public AgentPrimitives() {
        bundle=ResourceBundle.getBundle("gr.cti.eslate.agent.BundlePrimitives",Locale.getDefault());
        bundleMessages=ResourceBundle.getBundle("gr.cti.eslate.agent.BundleMessages",Locale.getDefault());
    }

    protected void setup(Machine machine, Console console) throws SetupException {
        registerPrimitive("GO", "pGO", 1);
        registerPrimitive("LOOK", "pLOOK", 1);
        registerPrimitive("TURN", "pTURN", 1);
        registerPrimitive("GOTO", "pGOTO", 2);
        registerPrimitive("JUMPTO", "pJUMPTO", 2);
        registerPrimitive("TRAILON", "pTRAILON", 0);
        registerPrimitive("TRAILOFF", "pTRAILOFF", 0);
        registerPrimitive("CLEARTRAIL", "pCLEARTRAIL", 0);
        registerPrimitive("EMBARK", "pEMBARK", 1);
        registerPrimitive("DISEMBARK", "pDISEMBARK", 1);
        registerPrimitive("FORGETEVERYTHING", "pFORGETEVERYTHING", 0);
        registerPrimitive("LONGITUDE", "pLONGITUDE", 0);
        registerPrimitive("LATITUDE", "pLATITUDE", 0);
        registerPrimitive("ISPOSITIONED", "pISPOSITIONED", 0);
        myMachine=(MyMachine) machine;
        myConsole=console;
        if (console!=null)
            console.putSetupMessage(bundle.getString("setup"));
    }

    public final LogoObject pGO(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject,1);
        testMinParams(aLogoObject,1);
        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.protocol.IAgent.class);
        for (int i=0;i<v.size();i++) {
            IAgent agent=(IAgent) v.elementAt(i);
            if (!agent.isPositioned())
                myConsole.putStatusMessage(agent.getName()+": "+bundle.getString("notpositioned"));
            agent.goDistance(aLogoObject[0].toNumber(),false);
        }
        return LogoVoid.obj;
    }

    public final LogoObject pLOOK(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject,1);
        testMinParams(aLogoObject,1);
        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.protocol.IAgent.class);
        for (int i=0;i<v.size();i++) {
            IAgent agent=(IAgent) v.elementAt(i);
            agent.setTiltFromUpright(aLogoObject[0].toNumber());
        }
        return LogoVoid.obj;
    }

    public final LogoObject pTURN(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject,1);
        testMinParams(aLogoObject,1);
        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.protocol.IAgent.class);
        for (int i=0;i<v.size();i++) {
            IAgent agent=(IAgent) v.elementAt(i);
            agent.turn(aLogoObject[0].toNumber());
        }
        return LogoVoid.obj;
    }

    public final LogoObject pGOTO(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject,2);
        testMinParams(aLogoObject,2);
        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.protocol.IAgent.class);
        for (int i=0;i<v.size();i++) {
            IAgent agent=(IAgent) v.elementAt(i);
            agent.goTo(aLogoObject[0].toNumber(),aLogoObject[1].toNumber());
        }
        return LogoVoid.obj;
    }

    public final LogoObject pJUMPTO(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject,2);
        testMinParams(aLogoObject,2);
        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.protocol.IAgent.class);
        for (int i=0;i<v.size();i++) {
            IAgent agent=(IAgent) v.elementAt(i);
            try {
                agent.setLongLat(aLogoObject[0].toNumber(),aLogoObject[1].toNumber());
            } catch(AgentRefusesToChangePositionException e) {
                myConsole.putStatusMessage(agent.getName()+": "+bundle.getString("refusejump"));
            }
        }
        return LogoVoid.obj;
    }

    public final LogoObject pTRAILON(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject,0);
        testMinParams(aLogoObject,0);
        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.protocol.IAgent.class);
        for (int i=0;i<v.size();i++) {
            IAgent agent=(IAgent) v.elementAt(i);
            agent.setTrailOn(true);
        }
        return LogoVoid.obj;
    }

    public final LogoObject pTRAILOFF(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject,0);
        testMinParams(aLogoObject,0);
        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.protocol.IAgent.class);
        for (int i=0;i<v.size();i++) {
            IAgent agent=(IAgent) v.elementAt(i);
            agent.setTrailOn(false);
        }
        return LogoVoid.obj;
    }

    public final LogoObject pCLEARTRAIL(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject,0);
        testMinParams(aLogoObject,0);
        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.protocol.IAgent.class);
        for (int i=0;i<v.size();i++) {
            IAgent agent=(IAgent) v.elementAt(i);
            agent.clearTrail();
        }
        return LogoVoid.obj;
    }

    public final LogoObject pEMBARK(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject,1);
        testMinParams(aLogoObject,1);
        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.protocol.IAgent.class);
        String c=aLogoObject[0].toString();
        for (int i=0;i<v.size() && c!=null;i++) {
            IAgent agent=(IAgent) v.elementAt(i);
            IAgent veh=agent.canEmbarkOn(c);
            if (veh!=null)
                veh.embark(agent);
            else
                myConsole.putStatusMessage(agent.getName()+": "+bundleMessages.getString("cannotembark"));
        }
        return LogoVoid.obj;
    }

    public final LogoObject pDISEMBARK(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject,1);
        testMinParams(aLogoObject,1);
        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.protocol.IAgent.class);
        String c=aLogoObject[0].toString();
        for (int i=0;i<v.size() && c!=null;i++) {
            IAgent agent=(IAgent) v.elementAt(i);
            if (!agent.disembark(c))
                myConsole.putStatusMessage(agent.getName()+": "+bundleMessages.getString("cannotdisembark"));
        }
        return LogoVoid.obj;
    }

    public final LogoObject pFORGETEVERYTHING(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject,0);
        testMinParams(aLogoObject,0);
        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.protocol.IAgent.class);
        for (int i=0;i<v.size();i++) {
            IAgent agent=(IAgent) v.elementAt(i);
            agent.forgetEverything();
        }
        return LogoVoid.obj;
    }

    public final LogoObject pLONGITUDE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject,0);
        testMinParams(aLogoObject,0);
        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.protocol.IAgent.class);
        for (int i=0;i<v.size();i++) {
            IAgent agent=(IAgent) v.elementAt(i);
            if (agent.getLongLat().x!=Double.MAX_VALUE)
                return new LogoWord(agent.getLongLat().x);
            else
                throw new LanguageException(bundle.getString("notpositioned"));
        }
        return LogoVoid.obj;
    }

    public final LogoObject pLATITUDE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject,0);
        testMinParams(aLogoObject,0);
        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.protocol.IAgent.class);
        for (int i=0;i<v.size();i++) {
            IAgent agent=(IAgent) v.elementAt(i);
            if (agent.getLongLat().y!=Double.MAX_VALUE)
                return new LogoWord(agent.getLongLat().y);
            else
                throw new LanguageException(bundle.getString("notpositioned"));
        }
        return LogoVoid.obj;
    }

    public final LogoObject pISPOSITIONED(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject,0);
        testMinParams(aLogoObject,0);
        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.protocol.IAgent.class);
        for (int i=0;i<v.size();i++) {
            IAgent agent=(IAgent) v.elementAt(i);
            if (agent.getLongLat().x!=Double.MAX_VALUE && agent.getLongLat().y!=Double.MAX_VALUE)
                return new LogoWord(1);
            else
                return new LogoWord(0);
        }
        return LogoVoid.obj;
    }

    private MyMachine myMachine;
    private Console myConsole;
}
