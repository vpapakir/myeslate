package gr.cti.eslate.scripting.logo;


import java.util.*;
import virtuoso.logo.*;
import gr.cti.eslate.eslateToggleButton.*;
import java.awt.*;


public class ToggleButtonPrimitives extends PrimitiveGroup {
    MyMachine myMachine;

    protected void setup(Machine machine, Console console) throws SetupException {
        registerPrimitive("TBUTTON.TIME", "pTIME", 0);
        registerPrimitive("TBUTTON.TEXT", "pTEXT", 0);
        registerPrimitive("TBUTTON.CLICK", "pCLICK", 0);
        registerPrimitive("TBUTTON.ENABLED", "pENABLED", 0);
        registerPrimitive("TBUTTON.ENABLE", "pENABLE", 0);
        registerPrimitive("TBUTTON.DISABLE", "pDISABLE", 0);
        registerPrimitive("TBUTTON.ISOPAQUE", "pISOPAQUE", 0);
        registerPrimitive("TBUTTON.OPAQUE", "pOPAQUE", 0);
        registerPrimitive("TBUTTON.NOTOPAQUE", "pNOTOPAQUE", 0);
        registerPrimitive("TBUTTON.FOCUSPAINTED", "pFOCUSPAINTED", 0);
        registerPrimitive("TBUTTON.FOCUSPAINT", "pFOCUSPAINT", 0);
        registerPrimitive("TBUTTON.NOFOCUSPAINT", "pNOFOCUSPAINT", 0);
        registerPrimitive("TBUTTON.SELECTED", "pSELECTED", 0);
        registerPrimitive("TBUTTON.SELECTED2", "pSELECTED2", 0);
        registerPrimitive("TBUTTON.SELECT", "pSELECT", 0);
        registerPrimitive("TBUTTON.UNSELECT", "pUNSELECT", 0);
        registerPrimitive("TBUTTON.SETTEXT", "pSETTEXT", 1);

        registerPrimitive("TBUTTON.SETBGCOLOR", "pSETBGCOLOR", 1);
        registerPrimitive("TBUTTON.SETFGCOLOR", "pSETFGCOLOR", 1);
        registerPrimitive("TBUTTON.FGCOLOR", "pFGCOLOR", 0);
        registerPrimitive("TBUTTON.BGCOLOR", "pBGCOLOR", 0);

        myMachine = (MyMachine) machine;

        if (console != null)
            console.putSetupMessage("Loaded ToggleButton primitives");
    }

    public final LogoObject pTIME(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        return new LogoWord(System.currentTimeMillis());
    }

    public final LogoObject pTEXT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateToggleButton button = (ESlateToggleButton) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateToggleButton.ESlateToggleButton.class);

        String text = button.getText();

        return new LogoWord(text);
    }

    public final LogoObject pCLICK(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector<?> v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateToggleButton.ESlateToggleButton.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateToggleButton button = (ESlateToggleButton) v.elementAt(i);

            button.doClick();
        }

        return LogoVoid.obj;
    }

    public final LogoObject pSETTEXT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        Vector<?> v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateToggleButton.ESlateToggleButton.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateToggleButton button = (ESlateToggleButton) v.elementAt(i);
            String text = aLogoObject[0].toString();

            button.setText(text);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pENABLE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector<?> v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateToggleButton.ESlateToggleButton.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateToggleButton button = (ESlateToggleButton) v.elementAt(i);

            button.setEnabled(true);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pDISABLE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector<?> v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateToggleButton.ESlateToggleButton.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateToggleButton button = (ESlateToggleButton) v.elementAt(i);

            button.setEnabled(false);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pENABLED(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateToggleButton button = (ESlateToggleButton) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateToggleButton.ESlateToggleButton.class);

        return new LogoWord(button.isEnabled());
    }

    public final LogoObject pISOPAQUE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateToggleButton button = (ESlateToggleButton) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateToggleButton.ESlateToggleButton.class);

        return new LogoWord(button.isOpaque());
    }

    public final LogoObject pOPAQUE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector<?> v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateToggleButton.ESlateToggleButton.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateToggleButton button = (ESlateToggleButton) v.elementAt(i);

            button.setOpaque(true);
            button.repaint();
        }

        return LogoVoid.obj;
    }

    public final LogoObject pNOTOPAQUE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector<?> v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateToggleButton.ESlateToggleButton.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateToggleButton button = (ESlateToggleButton) v.elementAt(i);

            button.setOpaque(false);
            button.repaint();
        }

        return LogoVoid.obj;
    }

    public final LogoObject pFOCUSPAINTED(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateToggleButton button = (ESlateToggleButton) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateToggleButton.ESlateToggleButton.class);

        return new LogoWord(button.isFocusPainted());
    }

    public final LogoObject pFOCUSPAINT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector<?> v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateToggleButton.ESlateToggleButton.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateToggleButton button = (ESlateToggleButton) v.elementAt(i);

            button.setFocusPainted(true);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pNOFOCUSPAINT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector<?> v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateToggleButton.ESlateToggleButton.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateToggleButton button = (ESlateToggleButton) v.elementAt(i);

            button.setFocusPainted(false);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pSELECTED(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateToggleButton button = (ESlateToggleButton) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateToggleButton.ESlateToggleButton.class);

        return new LogoWord(button.isSelected());
    }

    public final LogoObject pSELECTED2(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateToggleButton button = (ESlateToggleButton) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateToggleButton.ESlateToggleButton.class);

        return new LogoWord(button.isSelected());
    }

    public final LogoObject pSELECT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector<?> v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateToggleButton.ESlateToggleButton.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateToggleButton button = (ESlateToggleButton) v.elementAt(i);

            button.setSelected(true);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pUNSELECT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector<?> v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateToggleButton.ESlateToggleButton.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateToggleButton button = (ESlateToggleButton) v.elementAt(i);

            button.setSelected(false);
        }

        return LogoVoid.obj;
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
        Vector<?> v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateToggleButton.ESlateToggleButton.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateToggleButton button = (ESlateToggleButton) v.elementAt(i);

            button.setBackground(bgColor);
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
        Vector<?> v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateToggleButton.ESlateToggleButton.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateToggleButton button = (ESlateToggleButton) v.elementAt(i);

            button.setForeground(fgColor);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pBGCOLOR(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateToggleButton button = (ESlateToggleButton) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateToggleButton.ESlateToggleButton.class);

        Vector<LogoWord> vColor = new Vector<LogoWord>();
        LogoWord red = new LogoWord(button.getBackground().getRed());
        LogoWord green = new LogoWord(button.getBackground().getGreen());
        LogoWord blue = new LogoWord(button.getBackground().getBlue());

        vColor.addElement(red);
        vColor.addElement(green);
        vColor.addElement(blue);

        return new LogoList(vColor);
    }

    public final LogoObject pFGCOLOR(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateToggleButton button = (ESlateToggleButton) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateToggleButton.ESlateToggleButton.class);

        try {
            Vector<LogoWord> vColor = new Vector<LogoWord>();
            LogoWord red = new LogoWord(button.getForeground().getRed());
            LogoWord green = new LogoWord(button.getForeground().getGreen());
            LogoWord blue = new LogoWord(button.getForeground().getBlue());

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
