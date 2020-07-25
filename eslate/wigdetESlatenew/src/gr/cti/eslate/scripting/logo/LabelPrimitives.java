package gr.cti.eslate.scripting.logo;


import gr.cti.eslate.eslateLabel.ESlateLabel;

import java.awt.Color;
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


public class LabelPrimitives extends PrimitiveGroup {
    MyMachine myMachine;

    protected void setup(Machine machine, Console console) throws SetupException {
        registerPrimitive("LABEL.TEXT", "pTEXT", 0);
        registerPrimitive("LABEL.SETTEXT", "pSETTEXT", 1);
        registerPrimitive("LABEL.ENABLE", "pENABLE", 0);
        registerPrimitive("LABEL.DISABLE", "pDISABLE", 0);
        registerPrimitive("LABEL.OPAQUE", "pOPAQUE", 0);
        registerPrimitive("LABEL.NOTOPAQUE", "pNOTOPAQUE", 0);

        registerPrimitive("LABEL.ENABLED", "pENABLED", 0);
        registerPrimitive("LABEL.ISOPAQUE", "pISOPAQUE", 0);

        registerPrimitive("LABEL.SETBGCOLOR", "pSETBGCOLOR", 1);
        registerPrimitive("LABEL.SETFGCOLOR", "pSETFGCOLOR", 1);
        registerPrimitive("LABEL.FGCOLOR", "pFGCOLOR", 0);
        registerPrimitive("LABEL.BGCOLOR", "pBGCOLOR", 0);

        registerPrimitive("LABEL.TEXT1", "pTEXT", 0);
        registerPrimitive("LABEL.SETTEXT1", "pSETTEXT", 1);
        registerPrimitive("LABEL.ENABLE1", "pENABLE", 0);
        registerPrimitive("LABEL.DISABLE1", "pDISABLE", 0);
        registerPrimitive("LABEL.OPAQUE1", "pOPAQUE", 0);
        registerPrimitive("LABEL.NOTOPAQUE1", "pNOTOPAQUE", 0);

        registerPrimitive("LABEL.ENABLED1", "pENABLED", 0);
        registerPrimitive("LABEL.ISOPAQUE1", "pISOPAQUE", 0);

        registerPrimitive("LABEL.SETBGCOLOR1", "pSETBGCOLOR", 1);
        registerPrimitive("LABEL.SETFGCOLOR1", "pSETFGCOLOR", 1);
        registerPrimitive("LABEL.FGCOLOR1", "pFGCOLOR", 0);
        registerPrimitive("LABEL.BGCOLOR1", "pBGCOLOR", 0);

        myMachine = (MyMachine) machine;

        if (console != null)
            console.putSetupMessage("Loaded Label primitives");
    }

    public final LogoObject pSETTEXT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateLabel.ESlateLabel.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateLabel label = (ESlateLabel) v.elementAt(i);
            String text = aLogoObject[0].toString();

            label.setText(text);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pENABLE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateLabel.ESlateLabel.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateLabel label = (ESlateLabel) v.elementAt(i);

            label.setEnabled(true);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pDISABLE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateLabel.ESlateLabel.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateLabel label = (ESlateLabel) v.elementAt(i);

            label.setEnabled(false);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pOPAQUE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateLabel.ESlateLabel.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateLabel label = (ESlateLabel) v.elementAt(i);

            label.setOpaque(true);
            label.repaint();
        }

        return LogoVoid.obj;
    }

    public final LogoObject pNOTOPAQUE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateLabel.ESlateLabel.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateLabel label = (ESlateLabel) v.elementAt(i);

            label.setOpaque(false);
            label.repaint();
        }

        return LogoVoid.obj;
    }

    public final LogoObject pENABLED(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateLabel label = (ESlateLabel) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateLabel.ESlateLabel.class);

        if (label == null)
            throw new LanguageException("There is no ESlateLabel to TELL this to");

        return new LogoWord(label.isEnabled());
    }

    public final LogoObject pISOPAQUE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateLabel label = (ESlateLabel) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateLabel.ESlateLabel.class);

        if (label == null)
            throw new LanguageException("There is no ESlateLabel to TELL this to");

        return new LogoWord(label.isOpaque());
    }

    public final LogoObject pTEXT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateLabel label = (ESlateLabel) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateLabel.ESlateLabel.class);

        if (label == null)
            throw new LanguageException("There is no ESlateLabel to TELL this to");

        String text = label.getText();

        return new LogoWord(text);
    }

    public final LogoObject pSETBGCOLOR(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        int red;
        int green;
        int blue;

        red = aLogoObject[0].pick(1).toInteger();
        green = aLogoObject[0].pick(2).toInteger();
        blue = aLogoObject[0].pick(3).toInteger();
        Color bgColor = new Color(red, green, blue);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateLabel.ESlateLabel.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateLabel label = (ESlateLabel) v.elementAt(i);

            label.setBackground(bgColor);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pSETFGCOLOR(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        int red;
        int green;
        int blue;

        red = aLogoObject[0].pick(1).toInteger();
        green = aLogoObject[0].pick(2).toInteger();
        blue = aLogoObject[0].pick(3).toInteger();
        Color fgColor = new Color(red, green, blue);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateLabel.ESlateLabel.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateLabel label = (ESlateLabel) v.elementAt(i);

            label.setForeground(fgColor);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pBGCOLOR(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateLabel label = (ESlateLabel) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateLabel.ESlateLabel.class);

        if (label == null)
            throw new LanguageException("There is no ESlateLabel to TELL this to");

        Vector vColor = new Vector();
        LogoWord red = new LogoWord(label.getBackground().getRed());
        LogoWord green = new LogoWord(label.getBackground().getGreen());
        LogoWord blue = new LogoWord(label.getBackground().getBlue());

        vColor.addElement(red);
        vColor.addElement(green);
        vColor.addElement(blue);

        return new LogoList(vColor);
    }

    public final LogoObject pFGCOLOR(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateLabel label = (ESlateLabel) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateLabel.ESlateLabel.class);

        try {
            Vector vColor = new Vector();
            LogoWord red = new LogoWord(label.getForeground().getRed());
            LogoWord green = new LogoWord(label.getForeground().getGreen());
            LogoWord blue = new LogoWord(label.getForeground().getBlue());

            vColor.addElement(red);
            vColor.addElement(green);
            vColor.addElement(blue);
            return new LogoList(vColor);
        } catch (Exception exc) {
            exc.printStackTrace();
            return LogoVoid.obj;
        }
    }

}
