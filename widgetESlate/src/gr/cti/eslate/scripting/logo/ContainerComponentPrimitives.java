package gr.cti.eslate.scripting.logo;

import gr.cti.eslate.base.ESlateMicroworld;
import gr.cti.eslate.base.container.ESlateComponent;
import gr.cti.eslate.base.container.ESlateContainer;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Enumeration;
import java.util.Vector;

import virtuoso.logo.Console;
import virtuoso.logo.InterpEnviron;
import virtuoso.logo.LanguageException;
import virtuoso.logo.LogoList;
import virtuoso.logo.LogoObject;
import virtuoso.logo.LogoVoid;
import virtuoso.logo.LogoWord;
import virtuoso.logo.Machine;
import virtuoso.logo.MyMachine;
import virtuoso.logo.PrimitiveGroup;
import virtuoso.logo.SetupException;


public class ContainerComponentPrimitives extends PrimitiveGroup {
    MyMachine myMachine;

    protected void setup(Machine machine, Console console) throws SetupException {
        registerPrimitive("MOVE", "pMOVE", 2);

        registerPrimitive("LOCATION", "pLOCATION", 0);

        registerPrimitive("MAXIMIZE", "pMAXIMIZE", 0);

        registerPrimitive("HIDE", "pHIDE", 0);

        registerPrimitive("RESTORE", "pRESTORE", 0);

        registerPrimitive("SETRESIZABLE", "pSETRESIZABLE", 0);

        registerPrimitive("SETUNRESIZABLE", "pSETUNRESIZABLE", 0);

        registerPrimitive("RESIZABLE", "pRESIZABLE", 0);

        registerPrimitive("SETTITLEON", "pSETTITLEON", 0);

        registerPrimitive("SETTITLEOFF", "pSETTITLEOFF", 0);

        registerPrimitive("CLOSECOMPONENT", "pCLOSECOMPONENT", 0);

        registerPrimitive("ACTIVATECOMPONENT", "pACTIVATECOMPONENT", 0);

        registerPrimitive("DEACTIVATECOMPONENT", "pDEACTIVATECOMPONENT", 0);

        registerPrimitive("SETCOMPONENTSIZE", "pSETCOMPONENTSIZE", 2);

        registerPrimitive("COMPONENTSIZE", "pCOMPONENTSIZE", 0);

        registerPrimitive("SETCOMPONENTBOUNDS", "pSETCOMPONENTBOUNDS", 4);

        registerPrimitive("COMPONENTBOUNDS", "pCOMPONENTBOUNDS", 0);

        registerPrimitive("PRINTCOMPONENT", "pPRINTCOMPONENT", 1);

        myMachine=(MyMachine)machine;

        if (console != null)
            console.putSetupMessage("Loaded Container Component  primitives");
    }

    public final LogoObject pMOVE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 2);
        int xLoc = 0, yLoc = 0;
        try{
            xLoc = new Integer(aLogoObject[0].toString()).intValue();
            yLoc = new Integer(aLogoObject[1].toString()).intValue();
        }catch (Exception exc) {
            throw new LanguageException("Move accepts only numbers");
        }

        Vector v=myMachine.componentPrimitives.getComponentsToTell(java.lang.Object.class);
        if (v==null || v.size() == 0)
            throw new LanguageException("There is no component to TELL this to");

        Enumeration en = v.elements();
        while (en.hasMoreElements()) {
            Object compo = en.nextElement();
//            System.out.println("compo: " + compo);
            ESlateMicroworld mwd = ESlateMicroworld.getComponentMicroworld(compo);
            if (mwd == null)
            	continue;
            Object mwdEnv = mwd.getMicroworldEnvironment();
            if (mwdEnv == null)
                continue;
            if (!ESlateContainer.class.isInstance(mwdEnv))
                throw new LanguageException("Move can only be used inside the E-Slate Container");
//            System.out.println("mwd: " + mwd);

            ((ESlateContainer) mwdEnv).setDesktopItemLocation(compo, new Point(xLoc, yLoc));
        }
//            System.out.println("fr: " + fr);
        return LogoVoid.obj;
    }

    public final LogoObject pLOCATION(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v=myMachine.componentPrimitives.getComponentsToTell(java.lang.Object.class);
        if (v==null || v.size() == 0)
            throw new LanguageException("There is no component to TELL this to");
        Enumeration en = v.elements();
        Object compo = en.nextElement();
        ESlateMicroworld mwd = ESlateMicroworld.getComponentMicroworld(compo);
        Object mwdEnv = mwd.getMicroworldEnvironment();
        if (!ESlateContainer.class.isInstance(mwdEnv))
            throw new LanguageException("\"Location\" can only be used inside the E-Slate Container");

        Point p = ((ESlateContainer) mwdEnv).getDesktopItemLocation(compo);
        if (p != null) {
            LogoWord[] loc = new LogoWord[2];
            loc[0] = new LogoWord(p.x);
            loc[1] = new LogoWord(p.y);
            return new LogoList(loc);
        }else
            return LogoVoid.obj;
    }

    public final LogoObject pMAXIMIZE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        Vector v=myMachine.componentPrimitives.getComponentsToTell(java.lang.Object.class);
        if (v==null || v.size() == 0)
            throw new LanguageException("There is no component to TELL this to");

        Enumeration en = v.elements();
        while (en.hasMoreElements()) {
            Object compo = en.nextElement();
//            System.out.println("compo: " + compo);
            ESlateMicroworld mwd = ESlateMicroworld.getComponentMicroworld(compo);
            Object mwdEnv = mwd.getMicroworldEnvironment();
            if (mwdEnv == null)
                continue;
            if (!ESlateContainer.class.isInstance(mwdEnv))
                throw new LanguageException("MAXIMIZE can only be used inside the E-Slate Container");

//1            ESlateInternalFrame fr = ((ESlateContainer) mwdEnv).getDesktopItemFrame(compo);
            ESlateComponent ecomponent = ((ESlateContainer) mwdEnv).getComponent(compo);
            try{
                ecomponent.getDesktopItem().setMaximum(true);
            }catch (Exception exc) {
                throw new LanguageException(exc.getMessage());
            }
        }
        return LogoVoid.obj;
    }

    public final LogoObject pHIDE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        Vector v=myMachine.componentPrimitives.getComponentsToTell(java.lang.Object.class);
        if (v==null || v.size() == 0)
            throw new LanguageException("There is no component to TELL this to");

        Enumeration en = v.elements();
        while (en.hasMoreElements()) {
            Object compo = en.nextElement();
//            System.out.println("compo: " + compo);
            ESlateMicroworld mwd = ESlateMicroworld.getComponentMicroworld(compo);
            Object mwdEnv = mwd.getMicroworldEnvironment();
            if (mwdEnv == null)
                continue;
            if (!ESlateContainer.class.isInstance(mwdEnv))
                throw new LanguageException("HIDE can only be used inside the E-Slate Container");

            ESlateComponent ecomponent = ((ESlateContainer) mwdEnv).getComponent(compo);
            try{
                ecomponent.getDesktopItem().setIcon(true);
            }catch (Exception exc) {
                throw new LanguageException(exc.getMessage());
            }
        }
        return LogoVoid.obj;
    }

    public final LogoObject pRESTORE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        Vector v=myMachine.componentPrimitives.getComponentsToTell(java.lang.Object.class);
        if (v==null || v.size() == 0)
            throw new LanguageException("There is no component to TELL this to");

        Enumeration en = v.elements();
        while (en.hasMoreElements()) {
            Object compo = en.nextElement();
//            System.out.println("compo: " + compo);
            ESlateMicroworld mwd = ESlateMicroworld.getComponentMicroworld(compo);
            Object mwdEnv = mwd.getMicroworldEnvironment();
            if (mwdEnv == null)
                continue;
            if (!ESlateContainer.class.isInstance(mwdEnv))
                throw new LanguageException("RESTORE can only be used inside the E-Slate Container");

            ESlateComponent ecomponent = ((ESlateContainer) mwdEnv).getComponent(compo);
            try{
                ecomponent.getDesktopItem().setIcon(false);
                ecomponent.getDesktopItem().setMaximum(false);
            }catch (Exception exc) {
                throw new LanguageException(exc.getMessage());
            }
        }
        return LogoVoid.obj;
    }

    public final LogoObject pSETRESIZABLE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        Vector v=myMachine.componentPrimitives.getComponentsToTell(java.lang.Object.class);
        if (v==null || v.size() == 0)
            throw new LanguageException("There is no component to TELL this to");

        Enumeration en = v.elements();
        while (en.hasMoreElements()) {
            Object compo = en.nextElement();
            ESlateMicroworld mwd = ESlateMicroworld.getComponentMicroworld(compo);
            Object mwdEnv = mwd.getMicroworldEnvironment();
            if (mwdEnv == null)
                continue;
            if (!ESlateContainer.class.isInstance(mwdEnv))
                throw new LanguageException("SETRESIZABLE can only be used inside the E-Slate Container");

//1            ESlateInternalFrame fr = ((ESlateContainer) mwdEnv).getComponentFrame(compo);
            ESlateComponent ecomponent = ((ESlateContainer) mwdEnv).getComponent(compo);
            ecomponent.getDesktopItem().setDesktopItemResizable(true);
        }
        return LogoVoid.obj;
    }

    public final LogoObject pSETUNRESIZABLE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        Vector v=myMachine.componentPrimitives.getComponentsToTell(java.lang.Object.class);
        if (v==null || v.size() == 0)
            throw new LanguageException("There is no component to TELL this to");

        Enumeration en = v.elements();
        while (en.hasMoreElements()) {
            Object compo = en.nextElement();
            ESlateMicroworld mwd = ESlateMicroworld.getComponentMicroworld(compo);
            Object mwdEnv = mwd.getMicroworldEnvironment();
            if (mwdEnv == null)
                continue;
            if (!ESlateContainer.class.isInstance(mwdEnv))
                throw new LanguageException("\"SETUNRESIZABLE\" can only be used inside the E-Slate Container");

//1            ESlateInternalFrame fr = ((ESlateContainer) mwdEnv).getComponentFrame(compo);
            ESlateComponent ecomponent = ((ESlateContainer) mwdEnv).getComponent(compo);
            ecomponent.getDesktopItem().setDesktopItemResizable(false);
        }
        return LogoVoid.obj;
    }

    public final LogoObject pRESIZABLE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v=myMachine.componentPrimitives.getComponentsToTell(java.lang.Object.class);
        if (v==null || v.size() == 0)
            throw new LanguageException("There is no component to TELL this to");
        Enumeration en = v.elements();
        Object compo = en.nextElement();
        ESlateMicroworld mwd = ESlateMicroworld.getComponentMicroworld(compo);
        Object mwdEnv = mwd.getMicroworldEnvironment();
        if (!ESlateContainer.class.isInstance(mwdEnv))
            throw new LanguageException("\"RESIZABLE\" can only be used inside the E-Slate Container");

//1        ESlateInternalFrame fr = ((ESlateContainer) mwdEnv).getComponentFrame(compo);
        ESlateComponent ecomponent = ((ESlateContainer) mwdEnv).getComponent(compo);
        return new LogoWord(ecomponent.getDesktopItem().isDesktopItemResizable());
    }

    public final LogoObject pSETTITLEON(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        Vector v=myMachine.componentPrimitives.getComponentsToTell(java.lang.Object.class);
        if (v==null || v.size() == 0)
            throw new LanguageException("There is no component to TELL this to");

        Enumeration en = v.elements();
        while (en.hasMoreElements()) {
            Object compo = en.nextElement();
            ESlateMicroworld mwd = ESlateMicroworld.getComponentMicroworld(compo);
            Object mwdEnv = mwd.getMicroworldEnvironment();
            if (mwdEnv == null)
                continue;
            if (!ESlateContainer.class.isInstance(mwdEnv))
                throw new LanguageException("\"SETTITLEON\" can only be used inside the E-Slate Container");

//1            ESlateInternalFrame fr = ((ESlateContainer) mwdEnv).getComponentFrame(compo);
            ESlateComponent ecomponent = ((ESlateContainer) mwdEnv).getComponent(compo);
            /* Restore old time frozen components */
            if (ecomponent.getFrame() != null) {
//if                if (ecomponent.getFrame().isFrozen())
//if                    ecomponent.getFrame().setFrozen(false);
            }
            if (ecomponent.getDesktopItem().displaysESlateMenuBar()) {
                ecomponent.getDesktopItem().setTitlePanelVisible(true);
            }
        }
        return LogoVoid.obj;
    }

    public final LogoObject pSETTITLEOFF(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        Vector v=myMachine.componentPrimitives.getComponentsToTell(java.lang.Object.class);
        if (v==null || v.size() == 0)
            throw new LanguageException("There is no component to TELL this to");

        Enumeration en = v.elements();
        while (en.hasMoreElements()) {
            Object compo = en.nextElement();
            ESlateMicroworld mwd = ESlateMicroworld.getComponentMicroworld(compo);
            Object mwdEnv = mwd.getMicroworldEnvironment();
            if (mwdEnv == null)
                continue;
            if (!ESlateContainer.class.isInstance(mwdEnv))
                throw new LanguageException("\"SETTITLEOFF\" can only be used inside the E-Slate Container");

//1            ESlateInternalFrame fr = ((ESlateContainer) mwdEnv).getComponentFrame(compo);
            ESlateComponent ecomponent = ((ESlateContainer) mwdEnv).getComponent(compo);
            if (ecomponent.getDesktopItem().displaysESlateMenuBar())
                ecomponent.getDesktopItem().setTitlePanelVisible(false);
        }
        return LogoVoid.obj;
    }

    public final LogoObject pCLOSECOMPONENT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        Vector v=myMachine.componentPrimitives.getComponentsToTell(java.lang.Object.class);
        if (v==null || v.size() == 0)
            throw new LanguageException("There is no component to TELL this to");

        Enumeration en = v.elements();
        while (en.hasMoreElements()) {
            Object compo = en.nextElement();
            ESlateMicroworld mwd = ESlateMicroworld.getComponentMicroworld(compo);
            Object mwdEnv = mwd.getMicroworldEnvironment();
            if (mwdEnv == null)
                continue;
            if (!ESlateContainer.class.isInstance(mwdEnv))
                throw new LanguageException("\"CLOSECOMPONENT\" can only be used inside the E-Slate Container");

            ((ESlateContainer) mwdEnv).removeComponent(compo, true, true);
//            ESlateInternalFrame fr = ((ESlateContainer) mwdEnv).getComponentFrame(compo);
//            fr.dispose();
        }
        return LogoVoid.obj;
    }

    public final LogoObject pACTIVATECOMPONENT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        Vector v=myMachine.componentPrimitives.getComponentsToTell(java.lang.Object.class);
        if (v==null || v.size() == 0)
            throw new LanguageException("There is no component to TELL this to");

        Enumeration en = v.elements();
        while (en.hasMoreElements()) {
            Object compo = en.nextElement();
            ESlateMicroworld mwd = ESlateMicroworld.getComponentMicroworld(compo);
            Object mwdEnv = mwd.getMicroworldEnvironment();
            if (mwdEnv == null)
                continue;
            if (!ESlateContainer.class.isInstance(mwdEnv))
                throw new LanguageException("\"ACTIVATECOMPONENT\" can only be used inside the E-Slate Container");

            ESlateComponent component = ((ESlateContainer) mwdEnv).getComponent(compo);
            try{
                ((ESlateContainer) mwdEnv).setActiveComponent(component, true);
//                fr.setSelected(true);
            }catch (Exception exc) {
                throw new LanguageException(exc.getMessage());
            }
        }
        return LogoVoid.obj;
    }

    public final LogoObject pDEACTIVATECOMPONENT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        Vector v=myMachine.componentPrimitives.getComponentsToTell(java.lang.Object.class);
        if (v==null || v.size() == 0)
            throw new LanguageException("There is no component to TELL this to");

        Enumeration en = v.elements();
        while (en.hasMoreElements()) {
            Object compo = en.nextElement();
            ESlateMicroworld mwd = ESlateMicroworld.getComponentMicroworld(compo);
            Object mwdEnv = mwd.getMicroworldEnvironment();
            if (mwdEnv == null)
                continue;
            if (!ESlateContainer.class.isInstance(mwdEnv))
                throw new LanguageException("\"DEACTIVATECOMPONENT\" can only be used inside the E-Slate Container");

            ESlateComponent component = ((ESlateContainer) mwdEnv).getComponent(compo);
            try{
                ((ESlateContainer) mwdEnv).setActiveComponent(component, false);
//                fr.setSelected(false);
            }catch (Exception exc) {
                throw new LanguageException(exc.getMessage());
            }
        }
        return LogoVoid.obj;
    }

    public final LogoObject pSETCOMPONENTSIZE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 2);
        int width = 0, height = 0;
        try{
            width = new Integer(aLogoObject[0].toString()).intValue();
            height = new Integer(aLogoObject[1].toString()).intValue();
        }catch (Exception exc) {
            throw new LanguageException("\"SETCOMPONENTSIZE\" accepts only numbers");
        }

        Vector v=myMachine.componentPrimitives.getComponentsToTell(java.lang.Object.class);
        if (v==null || v.size() == 0)
            throw new LanguageException("There is no component to TELL this to");

        Enumeration en = v.elements();
        while (en.hasMoreElements()) {
            Object compo = en.nextElement();
            ESlateMicroworld mwd = ESlateMicroworld.getComponentMicroworld(compo);
            Object mwdEnv = mwd.getMicroworldEnvironment();
            if (mwdEnv == null)
                continue;
            if (!ESlateContainer.class.isInstance(mwdEnv))
                throw new LanguageException("\"SETCOMPONENTSIZE\" can only be used inside the E-Slate Container");

//1            ESlateInternalFrame fr = ((ESlateContainer) mwdEnv).getComponentFrame(compo);
            ESlateComponent ecomponent = ((ESlateContainer) mwdEnv).getComponent(compo);
            ecomponent.getDesktopItem().setDesktopItemSize(new Dimension(width, height));
        }
        return LogoVoid.obj;
    }

    public final LogoObject pSETCOMPONENTBOUNDS(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 4);
        int width = 0, height = 0, xLoc, yLoc;
        try{
            xLoc = new Integer(aLogoObject[0].toString()).intValue();
            yLoc = new Integer(aLogoObject[1].toString()).intValue();
            width = new Integer(aLogoObject[2].toString()).intValue();
            height = new Integer(aLogoObject[3].toString()).intValue();
        }catch (Exception exc) {
            throw new LanguageException("\"SETCOMPONENTBOUNDS\" accepts only numbers");
        }

        Vector v=myMachine.componentPrimitives.getComponentsToTell(java.lang.Object.class);
        if (v==null || v.size() == 0)
            throw new LanguageException("There is no component to TELL this to");

        Enumeration en = v.elements();
        while (en.hasMoreElements()) {
            Object compo = en.nextElement();
            ESlateMicroworld mwd = ESlateMicroworld.getComponentMicroworld(compo);
            Object mwdEnv = mwd.getMicroworldEnvironment();
            if (mwdEnv == null)
                continue;
            if (!ESlateContainer.class.isInstance(mwdEnv))
                throw new LanguageException("\"SETCOMPONENTBOUNDS\" can only be used inside the E-Slate Container");

//1            ESlateInternalFrame fr = ((ESlateContainer) mwdEnv).getComponentFrame(compo);
            ESlateComponent ecomponent = ((ESlateContainer) mwdEnv).getComponent(compo);
            ecomponent.getDesktopItem().setDesktopItemBounds(new Rectangle(xLoc, yLoc, width, height));
        }
        return LogoVoid.obj;
    }

    public final LogoObject pCOMPONENTSIZE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v=myMachine.componentPrimitives.getComponentsToTell(java.lang.Object.class);
        if (v==null || v.size() == 0)
            throw new LanguageException("There is no component to TELL this to");
        Enumeration en = v.elements();
        Object compo = en.nextElement();
        ESlateMicroworld mwd = ESlateMicroworld.getComponentMicroworld(compo);
        Object mwdEnv = mwd.getMicroworldEnvironment();
        if (!ESlateContainer.class.isInstance(mwdEnv))
            throw new LanguageException("\"COMPONENTSIZE\" can only be used inside the E-Slate Container");

//        ESlateInternalFrame fr = ((ESlateContainer) mwdEnv).getComponentFrame(compo);
        ESlateComponent ecomponent = ((ESlateContainer) mwdEnv).getComponent(compo);
        Dimension dim = ecomponent.getDesktopItem().getDesktopItemSize();
        if (dim != null) {
            LogoWord[] size = new LogoWord[2];
            size[0] = new LogoWord(dim.width);
            size[1] = new LogoWord(dim.height);
            return new LogoList(size);
        }else
            return LogoVoid.obj;
    }

    public final LogoObject pCOMPONENTBOUNDS(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v=myMachine.componentPrimitives.getComponentsToTell(java.lang.Object.class);
        if (v==null || v.size() == 0)
            throw new LanguageException("There is no component to TELL this to");
        Enumeration en = v.elements();
        Object compo = en.nextElement();
        ESlateMicroworld mwd = ESlateMicroworld.getComponentMicroworld(compo);
        Object mwdEnv = mwd.getMicroworldEnvironment();
        if (!ESlateContainer.class.isInstance(mwdEnv))
            throw new LanguageException("\"COMPONENTBOUNDS\" can only be used inside the E-Slate Container");

//1        ESlateInternalFrame fr = ((ESlateContainer) mwdEnv).getComponentFrame(compo);
        ESlateComponent ecomponent = ((ESlateContainer) mwdEnv).getComponent(compo);
        Rectangle b = ecomponent.getDesktopItem().getDesktopItemBounds();
        if (b != null) {
//            LogoWord bounds = new LogoWord("x: " + b.x + ", y: " + b.y + ", width: " + b.width + ", height: " + b.height);
            LogoWord[] bounds = new LogoWord[4];
            bounds[0] = new LogoWord(b.x);
            bounds[1] = new LogoWord(b.y);
            bounds[2] = new LogoWord(b.width);
            bounds[3] = new LogoWord(b.height);
            return new LogoList(bounds);
//            return bounds;
        }else
            return LogoVoid.obj;
    }

    public final LogoObject pPRINTCOMPONENT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 2);
        testMinParams(aLogoObject, 0);
        String destination = "printer";
        String compoName = null;
        if (aLogoObject.length == 1) {
            compoName = aLogoObject[0].toString();
        }else if (aLogoObject.length == 2) {
            compoName = aLogoObject[0].toString();
            destination = aLogoObject[1].toString().toLowerCase();
//            if (!destination.equals("printer") && !destination.equals("pdf")
//                && !destination.equals("rtf"))
//                throw new LanguageException("Invalid print destination. Valid destinations are: \"printer\", \"pdf\", \"rtf\"");
            if (!destination.equals("printer") && !destination.equals("file"))
                throw new LanguageException("Invalid print destination. Valid destinations are: \"printer\", \"file\"");
        }
        int destinationID = ESlateContainer.PRINTER;
        if (destination.equals("file"))
            destinationID = ESlateContainer.FILE;
/*        if (destination.equals("pdf"))
            destinationID = ESlateContainer.PDF_FILE;
        else if (destination.equals("rtf"))
            destinationID = ESlateContainer.RTF_FILE;
*/
        Vector v=myMachine.componentPrimitives.getComponentsToTell(java.lang.Object.class);
        if (v==null || v.size() == 0)
            throw new LanguageException("There is no component to TELL this to");

        Enumeration en = v.elements();
        while (en.hasMoreElements()) {
            Object compo = en.nextElement();
            ESlateMicroworld mwd = ESlateMicroworld.getComponentMicroworld(compo);
            Object mwdEnv = mwd.getMicroworldEnvironment();
            if (mwdEnv == null)
                continue;
            if (!ESlateContainer.class.isInstance(mwdEnv))
                throw new LanguageException("\"PRINTCOMPONENT\" can only be used inside the E-Slate Container");

            ESlateContainer cont = (ESlateContainer) mwdEnv;
            if (compoName == null) {
                gr.cti.eslate.base.ESlateHandle handle = cont.getActiveComponentHandle();
                if (handle != null)
                    compoName = handle.getComponentName();
            }
            if (compoName != null)
                cont.printComponent(compoName, destinationID);
//1            ESlateInternalFrame fr = cont.getComponentFrame(compo);
//            ESlateComponent ecomponent = ((ESlateContainer) mwdEnv).getComponent(compo);
//            cont.styleReportPrint((java.awt.Component) ecomponent.getDesktopItem(), destinationID);
//            fr.setSize(new Dimension(width, height));
        }
        return LogoVoid.obj;
    }

}

