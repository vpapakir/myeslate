package gr.cti.eslate.scripting.logo;


import java.util.*;
import virtuoso.logo.*;
import gr.cti.eslate.eslateRadioButton.*;
import java.awt.*;


public class RadioButtonPrimitives extends PrimitiveGroup {
    MyMachine myMachine;

    protected void setup(Machine machine, Console console) throws SetupException {

        registerPrimitive("RBUTTON.TIME", "pTIME", 0);
        registerPrimitive("RBUTTON.TEXT", "pTEXT", 0);
        registerPrimitive("RBUTTON.CLICK", "pCLICK", 0);
        registerPrimitive("RBUTTON.ENABLED", "pENABLED", 0);
        registerPrimitive("RBUTTON.ENABLE", "pENABLE", 0);
        registerPrimitive("RBUTTON.DISABLE", "pDISABLE", 0);
        registerPrimitive("RBUTTON.ISOPAQUE", "pISOPAQUE", 0);
        registerPrimitive("RBUTTON.OPAQUE", "pOPAQUE", 0);
        registerPrimitive("RBUTTON.NOTOPAQUE", "pNOTOPAQUE", 0);
        registerPrimitive("RBUTTON.FOCUSPAINTED", "pFOCUSPAINTED", 0);
        registerPrimitive("RBUTTON.FOCUSPAINT", "pFOCUSPAINT", 0);
        registerPrimitive("RBUTTON.NOFOCUSPAINT", "pNOFOCUSPAINT", 0);
        registerPrimitive("RBUTTON.SELECTED", "pSELECTED", 0);
        registerPrimitive("RBUTTON.SELECT", "pSELECT", 0);
        registerPrimitive("RBUTTON.UNSELECT", "pUNSELECT", 0);
        registerPrimitive("RBUTTON.SETTEXT", "pSETTEXT", 1);

        registerPrimitive("RBUTTON.SETBGCOLOR", "pSETBGCOLOR", 1);
        registerPrimitive("RBUTTON.SETFGCOLOR", "pSETFGCOLOR", 1);
        registerPrimitive("RBUTTON.FGCOLOR", "pFGCOLOR", 0);
        registerPrimitive("RBUTTON.BGCOLOR", "pBGCOLOR", 0);

        myMachine = (MyMachine) machine;

        if (console != null)
            console.putSetupMessage("Loaded RadioButton primitives");

    }

    public final LogoObject pTIME(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        return new LogoWord(System.currentTimeMillis());
    }

    public final LogoObject pTEXT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateRadioButton button = (ESlateRadioButton) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateRadioButton.ESlateRadioButton.class);

        String text = button.getText();

        return new LogoWord(text);
    }

    public final LogoObject pCLICK(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateRadioButton.ESlateRadioButton.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateRadioButton button = (ESlateRadioButton) v.elementAt(i);

            button.doClick();
        }

        return LogoVoid.obj;
    }

    public final LogoObject pSETTEXT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        String text = aLogoObject[0].toString();
        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateRadioButton.ESlateRadioButton.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateRadioButton button = (ESlateRadioButton) v.elementAt(i);

            button.setText(text);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pENABLE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateRadioButton.ESlateRadioButton.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateRadioButton button = (ESlateRadioButton) v.elementAt(i);

            button.setEnabled(true);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pDISABLE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateRadioButton.ESlateRadioButton.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateRadioButton button = (ESlateRadioButton) v.elementAt(i);

            button.setEnabled(false);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pENABLED(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateRadioButton button = (ESlateRadioButton) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateRadioButton.ESlateRadioButton.class);

        return new LogoWord(button.isEnabled());
    }

    public final LogoObject pISOPAQUE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateRadioButton button = (ESlateRadioButton) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateRadioButton.ESlateRadioButton.class);

        return new LogoWord(button.isOpaque());
    }

    public final LogoObject pOPAQUE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateRadioButton.ESlateRadioButton.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateRadioButton button = (ESlateRadioButton) v.elementAt(i);

            button.setOpaque(true);
            button.repaint();
        }

        return LogoVoid.obj;
    }

    public final LogoObject pNOTOPAQUE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateRadioButton.ESlateRadioButton.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateRadioButton button = (ESlateRadioButton) v.elementAt(i);

            button.setOpaque(false);
            button.repaint();
        }

        return LogoVoid.obj;
    }

    public final LogoObject pFOCUSPAINTED(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateRadioButton button = (ESlateRadioButton) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateRadioButton.ESlateRadioButton.class);

        return new LogoWord(button.isFocusPainted());
    }

    public final LogoObject pFOCUSPAINT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateRadioButton.ESlateRadioButton.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateRadioButton button = (ESlateRadioButton) v.elementAt(i);

            button.setFocusPainted(true);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pNOFOCUSPAINT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateRadioButton.ESlateRadioButton.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateRadioButton button = (ESlateRadioButton) v.elementAt(i);

            button.setFocusPainted(false);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pSELECTED(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateRadioButton button = (ESlateRadioButton) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateRadioButton.ESlateRadioButton.class);

        return new LogoWord(button.isSelected());
    }

    public final LogoObject pSELECT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateRadioButton.ESlateRadioButton.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateRadioButton button = (ESlateRadioButton) v.elementAt(i);

            button.setSelected(true);
        }
        return LogoVoid.obj;
    }

    public final LogoObject pUNSELECT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateRadioButton.ESlateRadioButton.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateRadioButton button = (ESlateRadioButton) v.elementAt(i);

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
        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateRadioButton.ESlateRadioButton.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateRadioButton button = (ESlateRadioButton) v.elementAt(i);

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
        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateRadioButton.ESlateRadioButton.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateRadioButton button = (ESlateRadioButton) v.elementAt(i);

            button.setForeground(fgColor);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pBGCOLOR(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateRadioButton button = (ESlateRadioButton) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateRadioButton.ESlateRadioButton.class);

        Vector vColor = new Vector();
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

        ESlateRadioButton button = (ESlateRadioButton) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateRadioButton.ESlateRadioButton.class);

        try {
            Vector vColor = new Vector();
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
