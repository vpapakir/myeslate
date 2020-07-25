package gr.cti.eslate.scripting.logo;


import gr.cti.eslate.webWindow.ESlateWebBrowser;

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


public class BrowserPrimitives extends PrimitiveGroup {
    MyMachine myMachine;

    protected void setup(Machine machine, Console console) throws SetupException {
        registerPrimitive("BROWSER.LOADURL", "pLOADURL", 1);
        registerPrimitive("BROWSER.GETURL", "pGETURL", 0);
        registerPrimitive("BROWSER.GOHOME", "pGOHOME", 0);
        registerPrimitive("BROWSER.REFRESH", "pREFRESH", 0);
        registerPrimitive("BROWSER.GOBACK", "pGOBACK", 0);
        registerPrimitive("BROWSER.GOFORWARD", "pGOFORWARD", 0);

        registerPrimitive("BROWSER.STOP", "pSTOP", 0);

        registerPrimitive("BROWSER.PRINTPAGE", "pPRINTPAGE", 0);

        myMachine = (MyMachine) machine;

        if (console != null)
            console.putSetupMessage("Loaded WebBrowser primitives");

    }

    public final LogoObject pLOADURL(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.webWindow.ESlateWebBrowser.class);

        ESlateWebBrowser browser = (ESlateWebBrowser) v.firstElement();
        String addr = aLogoObject[0].toString();

        browser.loadPage(addr, true);

        return LogoVoid.obj;
    }

    public final LogoObject pGETURL(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.webWindow.ESlateWebBrowser.class);

        ESlateWebBrowser browser = (ESlateWebBrowser) v.firstElement();

        return new LogoWord(browser.getURLLocation());
    }

    public final LogoObject pGOHOME(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.webWindow.ESlateWebBrowser.class);

        ESlateWebBrowser browser = (ESlateWebBrowser) v.firstElement();

        browser.loadPage(browser.getHomeURL().toString(), true);

        return LogoVoid.obj;
    }

    public final LogoObject pREFRESH(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.webWindow.ESlateWebBrowser.class);

        ESlateWebBrowser browser = (ESlateWebBrowser) v.firstElement();

        browser.reload();

        return LogoVoid.obj;
    }

    public final LogoObject pGOBACK(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.webWindow.ESlateWebBrowser.class);

        ESlateWebBrowser browser = (ESlateWebBrowser) v.firstElement();

        browser.goBack();

        return LogoVoid.obj;
    }

    public final LogoObject pGOFORWARD(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.webWindow.ESlateWebBrowser.class);

        ESlateWebBrowser browser = (ESlateWebBrowser) v.firstElement();

        browser.goForward();

        return LogoVoid.obj;
    }

    public final LogoObject pSTOP(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);
        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.webWindow.ESlateWebBrowser.class);

        ESlateWebBrowser browser = (ESlateWebBrowser) v.firstElement();

        browser.stop();

        return LogoVoid.obj;
    }

    public final LogoObject pPRINTPAGE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        System.out.println("Page should be printed ...real print will be added later");

        return LogoVoid.obj;
    }

    /*
     public final LogoObject pPRINTPAGE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
     testNumParams(aLogoObject, 0);

     Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.webWindow.ESlateWebBrowser.class);

     ESlateWebBrowser browser = (ESlateWebBrowser) v.firstElement();
     int red;
     int green;
     int blue;
     red = aLogoObject[0].pick(1).toInteger();
     green = aLogoObject[0].pick(2).toInteger();
     blue = aLogoObject[0].pick(3).toInteger();
     Color bgColor = new Color(red,green,blue);
     browser.setBackground(bgColor);

     return LogoVoid.obj;
     }

     public final LogoObject pSETFGCOLOR(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
     testNumParams(aLogoObject, 1);

     Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.webWindow.ESlateWebBrowser.class);

     ESlateWebBrowser browser = (ESlateWebBrowser) v.firstElement();
     int red;
     int green;
     int blue;
     red = aLogoObject[0].pick(1).toInteger();
     green = aLogoObject[0].pick(2).toInteger();
     blue = aLogoObject[0].pick(3).toInteger();
     Color fgColor = new Color(red,green,blue);
     browser.setForeground(fgColor);

     return LogoVoid.obj;
     }

     public final LogoObject pBGCOLOR(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
     testNumParams(aLogoObject, 0);

     Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.webWindow.ESlateWebBrowser.class);

     ESlateWebBrowser browser = (ESlateWebBrowser) v.firstElement();
     Vector vColor = new Vector();
     LogoWord red = new LogoWord(browser.getBackground().getRed());
     LogoWord green = new LogoWord(browser.getBackground().getGreen());
     LogoWord blue = new LogoWord(browser.getBackground().getBlue());
     vColor.addElement(red);
     vColor.addElement(green);
     vColor.addElement(blue);

     return new LogoList(vColor);
     }

     public final LogoObject pFGCOLOR(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
     testNumParams(aLogoObject, 0);

     ESlateWebBrowser browser = (ESlateWebBrowser) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.webWindow.ESlateWebBrowser.class);
     try{
     Vector vColor = new Vector();
     LogoWord red = new LogoWord(browser.getForeground().getRed());
     LogoWord green = new LogoWord(browser.getForeground().getGreen());
     LogoWord blue = new LogoWord(browser.getForeground().getBlue());
     vColor.addElement(red);
     vColor.addElement(green);
     vColor.addElement(blue);
     return new LogoList(vColor);
     }catch (Exception exc) {
     exc.printStackTrace();
     return LogoVoid.obj;
     }

     }

     */

}
