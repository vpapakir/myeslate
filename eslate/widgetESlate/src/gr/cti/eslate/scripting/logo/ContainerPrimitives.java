package gr.cti.eslate.scripting.logo;

import gr.cti.eslate.base.ESlateMicroworld;
import gr.cti.eslate.base.container.ESlateComposer;
import gr.cti.eslate.base.container.ESlateContainer;
import gr.cti.eslate.base.container.MicroworldView;
import gr.cti.eslate.base.container.MicroworldViewList;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;
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


public class ContainerPrimitives extends PrimitiveGroup {
    MyMachine myMachine;

    protected void setup(Machine machine, Console console) throws SetupException {
        registerPrimitive("CLEARMICROWORLD", "pCLEARMICROWORLD", 0);
        registerPrimitive("PACKMICROWORLD", "pPACKMICROWORLD", 0);

        registerPrimitive("SAVEMICROWORLD", "pSAVEMICROWORLD", 0);

////nikosM news
//        registerPrimitive("��.�������������������������", "pSAVEREMOTEMICROWORLDAS", 0);
        registerPrimitive("SAVEREMOTEMICROWORLDAS", "pSAVEREMOTEMICROWORLDAS", 0);
////nikosM news end
        registerPrimitive("SAVEMICROWORLDAS", "pSAVEMICROWORLDAS", 0);

        registerPrimitive("LOADMICROWORLD", "pLOADMICROWORLD", 1);

////nikosM news
//        registerPrimitive("��.��������������������������������", "pLOADREMOTEMICROWORLD", 2);
        registerPrimitive("LOADREMOTEMICROWORLD", "pLOADREMOTEMICROWORLD", 2);
////nikosM news end

        registerPrimitive("CLOSEMICROWORLD", "pCLOSEMICROWORLD", 0);

        registerPrimitive("PRINTMICROWORLD", "pPRINTMICROWORLD", 0);

        registerPrimitive("SETMICROWORLDBACKGROUND", "pSETMICROWORLDBACKGROUND", 3);

        registerPrimitive("CREATECOMPONENT", "pCREATECOMPONENT", 1);

        registerPrimitive("SHOWPLUGVIEW", "pSHOWPLUGVIEW", 0);

        registerPrimitive("CLOSEPLUGVIEW", "pCLOSEPLUGVIEW", 0);

        registerPrimitive("PLUGVIEWVISIBLE", "pPLUGVIEWVISIBLE", 0);

        registerPrimitive("SETMICROWORLDNAME", "pSETMICROWORLDNAME", 1);

        registerPrimitive("MICROWORLDNAME", "pMICROWORLDNAME", 0);

        registerPrimitive("MICROWORLDFOLDER", "pMICROWORLDFOLDER", 0);

        registerPrimitive("ESLATEFOLDER", "pESLATEFOLDER", 0);

        registerPrimitive("COMPONENTCOUNT", "pCOMPONENTCOUNT", 0);

        registerPrimitive("BACK",           "pBACK", 0);

        registerPrimitive("FORWARD",        "pFORWARD", 0);

        registerPrimitive("SETSCRIPT",      "pSETSCRIPT", 3);

        registerPrimitive("SHOWVIEW",       "pSHOWVIEW", 1);

        registerPrimitive("VIEWNAMES",      "pVIEWNAMES", 0);

        registerPrimitive("NEXTVIEW",       "pNEXTVIEW", 1);

        registerPrimitive("PREVIOUSVIEW",   "pPREVIOUSVIEW", 1);

        myMachine=(MyMachine)machine;

        if (console != null)
            console.putSetupMessage("Loaded Container primitives");
    }

    public final LogoObject pCLEARMICROWORLD(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        ESlateContainer container = (ESlateContainer) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.base.container.ESlateContainer.class);
        if (container == null)
            throw new LanguageException("There is no component to TELL this to");
        container.clearMicroworld();
        return LogoVoid.obj;
    }

    public final LogoObject pPACKMICROWORLD(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        ESlateContainer container = (ESlateContainer) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.base.container.ESlateContainer.class);
        if (container == null)
            throw new LanguageException("There is no component to TELL this to");

        container.packMicroworld(ESlateContainer.ALL_SIDES);
        return LogoVoid.obj;
    }

    public final LogoObject pSAVEMICROWORLD(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 1);
        testMinParams(aLogoObject, 0);

        boolean enableWaitDialog = true;

        if (aLogoObject.length == 1)
            enableWaitDialog = aLogoObject[0].toBoolean();

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.base.container.ESlateContainer.class);
//        System.out.println("v: " + v);
//        System.out.println("v.size(): " + v.size());
        if (v==null || v.size() == 0)
            throw new LanguageException("There is no component to TELL this to");

        Enumeration en = v.elements();
        while (en.hasMoreElements()) {
            Object compo = en.nextElement();
/*            System.out.println("compo: " + compo);
            ESlateMicroworld mwd = ESlateMicroworld.getComponentMicroworld(compo);
            Object mwdEnv = mwd.getMicroworldEnvironment();
            System.out.println("mwdEnv: " + mwdEnv);
            if (mwdEnv != null) {
                if (!ESlateContainer.class.isInstance(mwdEnv))
                    throw new LanguageException("\"SAVEMICROWORLD\" can only be used inside the E-Slate Container");
*/
                ((ESlateContainer) compo).saveMicroworld(enableWaitDialog);
//            }
        }
        return LogoVoid.obj;
    }

    public final LogoObject pSAVEMICROWORLDAS(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 1);
        testMinParams(aLogoObject, 0);

        boolean enableWaitDialog = true;

        if (aLogoObject.length == 1)
            enableWaitDialog = aLogoObject[0].toBoolean();

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.base.container.ESlateContainer.class);
//        System.out.println("v: " + v);
//        System.out.println("v.size(): " + v.size());
        if (v==null || v.size() == 0)
            throw new LanguageException("There is no component to TELL this to");

        Enumeration en = v.elements();
        while (en.hasMoreElements()) {
            Object compo = en.nextElement();
/*            System.out.println("compo: " + compo);
            ESlateMicroworld mwd = ESlateMicroworld.getComponentMicroworld(compo);
            Object mwdEnv = mwd.getMicroworldEnvironment();
            System.out.println("mwdEnv: " + mwdEnv);
            if (mwdEnv != null) {
                if (!ESlateContainer.class.isInstance(mwdEnv))
                    throw new LanguageException("\"SAVEMICROWORLD\" can only be used inside the E-Slate Container");
*/
                ((ESlateContainer) compo).saveAsLocalMicroworld(enableWaitDialog);
//            }
        }
        return LogoVoid.obj;
    }

////nikosM news
    public final LogoObject pSAVEREMOTEMICROWORLDAS(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 2);
        testMinParams(aLogoObject, 0);

        String webFileName = null;
        String webSite = null;

        if (aLogoObject.length == 2) {
            webFileName = aLogoObject[0].toString();
            webSite = aLogoObject[1].toString();
        }


        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.base.container.ESlateContainer.class);
//        System.out.println("v: " + v);
//        System.out.println("v.size(): " + v.size());
        if (v==null || v.size() == 0)
            throw new LanguageException("There is no component to TELL this to");

        Enumeration en = v.elements();
        while (en.hasMoreElements()) {
            Object compo = en.nextElement();
            ESlateContainer container = (ESlateContainer) compo;
            if (webFileName == null) {
               container.webServerMicrosHandle.saveMicroWorldAsFromWebFileDialog();
            }
            else {
                   String webSiteServlet = (String)container.webSites.get(webSite);
                   container.webServerMicrosHandle.saveFileToServer(webSiteServlet,webFileName);
            }
        }
        return LogoVoid.obj;
    }
////nikosM news end

    public final LogoObject pLOADMICROWORLD(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
//        testNumParams(aLogoObject, 1);
        testMaxParams(aLogoObject, 3);
        testMinParams(aLogoObject, 0);

        String mwdFileName = null;
        boolean enableWaitDialog = true;
        boolean promptToSave = true;

        if (aLogoObject.length == 1) {
            mwdFileName = aLogoObject[0].toString();
        }else if (aLogoObject.length == 2) {
            mwdFileName = aLogoObject[0].toString();
            enableWaitDialog = aLogoObject[1].toBoolean();
        }else if (aLogoObject.length == 3) {
            mwdFileName = aLogoObject[0].toString();
            enableWaitDialog = aLogoObject[1].toBoolean();
            promptToSave = aLogoObject[2].toBoolean();
        }

//        String mwdFileName;
//        mwdFileName = aLogoObject[0].toString();

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.base.container.ESlateContainer.class);
        if (v==null || v.size() == 0)
            throw new LanguageException("There is no component to TELL this to");

        Enumeration en = v.elements();
        while (en.hasMoreElements()) {
            Object compo = en.nextElement();
//            System.out.println("compo: " + compo);
            ESlateContainer container = (ESlateContainer) compo;
            if (mwdFileName == null) {
                Locale locale = Locale.getDefault();
                ResourceBundle containerBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.ContainerBundle", locale);
                if (!promptToSave)
                    container.closeMicroworld(false);
                container.loadLocalMicroworld(container.getSystemFile(false, containerBundle.getString("ContainerMsg8"), null, container.getMwdFileExtensions()), true, enableWaitDialog);
            }else{
                if (!promptToSave)
                    container.closeMicroworld(false);
                container.loadLocalMicroworld(mwdFileName, true, enableWaitDialog);
            }
        }
        return LogoVoid.obj;
    }

////nikosM news
    public final LogoObject pLOADREMOTEMICROWORLD(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
//        testNumParams(aLogoObject, 1);
        testMaxParams(aLogoObject, 3);
        testMinParams(aLogoObject, 0);
        String mwdFileName = null;
        String webSite = null;
        boolean updateHistory = true;
//System.out.println("length :"+aLogoObject.length);
        if (aLogoObject.length == 1) {
//            mwdFileName = aLogoObject[0].toString();
            throw new LanguageException("Missing parameters");
        }else if (aLogoObject.length == 2) {
            mwdFileName = aLogoObject[0].toString();
            webSite = aLogoObject[1].toString();
        }else if (aLogoObject.length == 3) {
            mwdFileName = aLogoObject[0].toString();
            webSite = aLogoObject[1].toString();
            updateHistory = aLogoObject[2].toBoolean();
        }
//        String mwdFileName;
//        mwdFileName = aLogoObject[0].toString();

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.base.container.ESlateContainer.class);
        if (v==null || v.size() == 0)
            throw new LanguageException("There is no component to TELL this to");

        Enumeration en = v.elements();
        while (en.hasMoreElements()) {
            Object compo = en.nextElement();
            ESlateContainer container = (ESlateContainer) compo;
            if (mwdFileName == null) {
                container.webServerMicrosHandle.openMicroWorldFromWebFileDialog();
            }else{
                String webSiteServlet = (String)container.webSites.get(webSite);
                container.webServerMicrosHandle.openRemoteMicroWorld(webSiteServlet,mwdFileName,updateHistory);
            }
        }
        return LogoVoid.obj;
    }

////nikosM news end
    public final LogoObject pCLOSEMICROWORLD(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.base.container.ESlateContainer.class);
        if (v==null || v.size() == 0)
            throw new LanguageException("There is no component to TELL this to");

        Enumeration en = v.elements();
        while (en.hasMoreElements()) {
            Object compo = en.nextElement();
//            System.out.println("compo: " + compo);
            ((ESlateContainer) compo).closeMicroworld(false);
        }
        return LogoVoid.obj;
    }


    public final LogoObject pSETMICROWORLDBACKGROUND(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 3);
        int red = 0, green = 0, blue = 0;
        try{
            red = new Integer(aLogoObject[0].toString()).intValue();
            green = new Integer(aLogoObject[1].toString()).intValue();
            blue = new Integer(aLogoObject[2].toString()).intValue();
        }catch (Exception exc) {
            throw new LanguageException("\"SETMICROWORLDBACKGROUND\" accepts only numbers");
        }

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.base.container.ESlateContainer.class);
        if (v==null || v.size() == 0)
            throw new LanguageException("There is no component to TELL this to");

        Enumeration en = v.elements();
        while (en.hasMoreElements()) {
            Object compo = en.nextElement();
//            System.out.println("compo: " + compo);
            ((ESlateContainer) compo).setMicroworldBackground(gr.cti.eslate.base.container.UIDialog.ICON_COLORED_BACKGROUND, new Color(red, green, blue), null, -1);
        }
        return LogoVoid.obj;
    }

    public final LogoObject pCREATECOMPONENT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMinParams(aLogoObject, 1);
        testMaxParams(aLogoObject, 2);
        String compoName = aLogoObject[0].toString();
        Dimension initialSize = null;
        Point initialLocation = null;
        if (aLogoObject.length == 2) {
            if (aLogoObject[1] instanceof LogoList) {
                LogoList bounds = (LogoList) aLogoObject[1];
                if (bounds.length() == 4) {
                    int x = bounds.pick(1).toInteger();
                    int y = bounds.pick(2).toInteger();
                    int width = bounds.pick(3).toInteger();
                    int height = bounds.pick(4).toInteger();
                    initialSize = new Dimension(width, height);
                    initialLocation = new Point(x, y);
                }else if (bounds.length() == 2) {
                    int x = bounds.pick(1).toInteger();
                    int y = bounds.pick(2).toInteger();
                    initialLocation = new Point(x, y);
                }
            }
        }

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.base.container.ESlateContainer.class);
        if (v==null || v.size() == 0)
            throw new LanguageException("There is no component to TELL this to");

        Enumeration en = v.elements();
        while (en.hasMoreElements()) {
            Object compo = en.nextElement();
//            System.out.println("compo: " + compo);
            Class cl = ((ESlateContainer) compo).getRegisteredComponentClass(compoName);
            if (cl == null)
                throw new LanguageException("There is no installed component under the name \"" + compoName + "\"");

            ((ESlateContainer) compo).createComponent(cl.getName(), initialLocation, initialSize);
        }
        return LogoVoid.obj;
    }

    public final LogoObject pSHOWPLUGVIEW(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.base.container.ESlateContainer.class);
        if (v==null || v.size() == 0)
            throw new LanguageException("There is no component to TELL this to");

        Enumeration en = v.elements();
        while (en.hasMoreElements()) {
            Object compo = en.nextElement();
            ((ESlateContainer) compo).setPinViewVisible(true);
        }
        return LogoVoid.obj;
    }

    public final LogoObject pCLOSEPLUGVIEW(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.base.container.ESlateContainer.class);
        if (v==null || v.size() == 0)
            throw new LanguageException("There is no component to TELL this to");

        Enumeration en = v.elements();
        while (en.hasMoreElements()) {
            Object compo = en.nextElement();
            ((ESlateContainer) compo).setPinViewVisible(false);
        }
        return LogoVoid.obj;
    }

    public final LogoObject pPLUGVIEWVISIBLE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.base.container.ESlateContainer.class);
        if (v==null || v.size() == 0)
            throw new LanguageException("There is no component to TELL this to");
        Enumeration en = v.elements();
        Object compo = en.nextElement();
        return new LogoWord(((ESlateContainer) compo).isPinViewVisible());
    }

    public final LogoObject pSETMICROWORLDNAME(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);
        String mwdName;
        mwdName = aLogoObject[0].toString();

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.base.container.ESlateContainer.class);
        if (v==null || v.size() == 0)
            throw new LanguageException("There is no component to TELL this to");

        Enumeration en = v.elements();
        while (en.hasMoreElements()) {
            Object compo = en.nextElement();
            ((ESlateContainer) compo).setMicroworldName(mwdName);
        }
        return LogoVoid.obj;
    }

    public final LogoObject pMICROWORLDNAME(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.base.container.ESlateContainer.class);
        if (v==null || v.size() == 0)
            throw new LanguageException("There is no component to TELL this to");
        Enumeration en = v.elements();
        Object compo = en.nextElement();
        return new LogoWord(((ESlateContainer) compo).getMicroworldName());
    }

    public final LogoObject pMICROWORLDFOLDER(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.base.container.ESlateContainer.class);
        if (v==null || v.size() == 0)
            throw new LanguageException("There is no component to TELL this to");
        Enumeration en = v.elements();
        Object compo = en.nextElement();
        String dirName = ((ESlateContainer) compo).getMicroworldDirectory();
        if (dirName!=null) {
        	try {
				dirName=new File(dirName).getCanonicalPath();
			} catch (IOException e) {
				dirName=null;
				e.printStackTrace();
			}
        }
        if (dirName != null) {
            dirName = dirName + System.getProperty("file.separator");
        }
//        System.out.println("dirName: " + dirName);
        if (dirName == null) dirName = "null";
        return new LogoWord(dirName);
    }

    public final LogoObject pESLATEFOLDER(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.base.container.ESlateContainer.class);
        if (v==null || v.size() == 0)
            throw new LanguageException("There is no component to TELL this to");
        Enumeration en = v.elements();
        Object compo = en.nextElement();
        String dirName = ((ESlateContainer) compo).getESlateUtilities().getPathToContainerJar();
        java.io.File dir = new java.io.File(dirName);
        if (!dir.exists())
            dirName = null;
        else{
            try{
                dirName = dir.getCanonicalPath();
            }catch (Exception exc) {dirName = null;}
        }
        if (dirName != null)
            dirName = dirName + System.getProperty("file.separator");
//        System.out.println("dirName: " + dirName);
        if (dirName == null) dirName = "null";
        return new LogoWord(dirName);
    }

    public final LogoObject pCOMPONENTCOUNT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.base.container.ESlateContainer.class);
        if (v==null || v.size() == 0)
            throw new LanguageException("There is no component to TELL this to");
        Enumeration en = v.elements();
        Object compo = en.nextElement();
        return new LogoWord(((ESlateContainer) compo).getTopLevelComponentCount());
    }

    public final LogoObject pBACK(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.base.container.ESlateContainer.class);
        if (v==null || v.size() == 0)
            throw new LanguageException("There is no component to TELL this to");

        Enumeration en = v.elements();
        while (en.hasMoreElements()) {
            Object compo = en.nextElement();
            ((ESlateContainer) compo).back();
        }
        return LogoVoid.obj;
    }

    public final LogoObject pFORWARD(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.base.container.ESlateContainer.class);
        if (v==null || v.size() == 0)
            throw new LanguageException("There is no component to TELL this to");

        Enumeration en = v.elements();
        while (en.hasMoreElements()) {
            Object compo = en.nextElement();
            ((ESlateContainer) compo).forward();
        }
        return LogoVoid.obj;
    }

    public final LogoObject pSETSCRIPT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 3);
        String compoName = aLogoObject[0].toString();
        String methodName = aLogoObject[1].toString();
        String script = aLogoObject[2].toString();

        Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.base.container.ESlateComposer.class);
        if (v==null || v.size() == 0)
            throw new LanguageException("There is no component to TELL this to");

        Enumeration en = v.elements();
        while (en.hasMoreElements()) {
            ESlateComposer composer = (ESlateComposer) en.nextElement();
            ESlateMicroworld mwd = composer.getMicroworld().getESlateMicroworld();
            Object compo = mwd.getComponent(compoName);
            if (compo == null)
                throw new LanguageException("There is no component named \"" + compoName + "\" in microworld \"" + mwd.getName() + '\"');
            composer.getESlateComposerUtilities().attachLogoScript(compo, script, methodName);
        }
        return LogoVoid.obj;
    }

    public final LogoObject pPRINTMICROWORLD(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 1);
        testMinParams(aLogoObject, 0);
        String destination = "printer";
        if (aLogoObject.length == 1) {
            destination = aLogoObject[0].toString().toLowerCase();
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
        ESlateContainer cont = (ESlateContainer) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.base.container.ESlateContainer.class);
        if (cont == null)
            throw new LanguageException("There is no component to TELL this to");

        cont.printMicroworld(destinationID);
        return LogoVoid.obj;
    }

    public final LogoObject pSHOWVIEW(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);
        String viewName = aLogoObject[0].toString();

        ESlateContainer container = (ESlateContainer) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.base.container.ESlateContainer.class);
        MicroworldView view = container.getViewList().getView(viewName);
        if (view != null)
            container.applyView(view);
        else
            throw new LanguageException("There is no view with the name \"" + viewName + "\"");
//        if (v==null || v.size() == 0)
//            throw new LanguageException("There is no component to TELL this to");

//        Enumeration enum = v.elements();
//        while (enum.hasMoreElements()) {
//            Object compo = enum.nextElement();
//            ((ESlateContainer) compo).setMicroworldName(mwdName);
//        }
        return LogoVoid.obj;
    }

    public final LogoObject pVIEWNAMES(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        ESlateContainer container = (ESlateContainer) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.base.container.ESlateContainer.class);
        MicroworldViewList list = container.getViewList();
        String[] viewNames = list.getViewNames();
        if (viewNames == null)
            viewNames = new String[0];
        LogoWord[] w = new LogoWord[viewNames.length];
        for (int i=0; i<viewNames.length; i++)
            w[i] = new LogoWord(viewNames[i]);
        return new LogoList(w);
    }

    public final LogoObject pNEXTVIEW(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);
        String viewName = aLogoObject[0].toString();
        ESlateContainer container = (ESlateContainer) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.base.container.ESlateContainer.class);
        MicroworldView view = container.getViewList().getView(viewName);
        if (view == null)
            throw new LanguageException("There is no view with the name \"" + viewName + "\"");
        MicroworldView nextView = container.getViewList().getView(view.getName(), true);
        if (nextView != null)
            container.applyView(nextView);
        else
            throw new LanguageException("There is no view after view \"" + viewName + "\"");
        return LogoVoid.obj;
    }

    public final LogoObject pPREVIOUSVIEW(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);
        String viewName = aLogoObject[0].toString();
        ESlateContainer container = (ESlateContainer) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.base.container.ESlateContainer.class);
        MicroworldView view = container.getViewList().getView(viewName);
        if (view == null)
            throw new LanguageException("There is no view with the name \"" + viewName + "\"");
        MicroworldView prevView = container.getViewList().getView(view.getName(), false);
        if (prevView != null)
            container.applyView(prevView);
        else
            throw new LanguageException("There is no view before view \"" + viewName + "\"");
        return LogoVoid.obj;
    }

}


