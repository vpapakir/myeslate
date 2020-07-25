package gr.cti.eslate.scripting.logo;


import java.util.*;
import virtuoso.logo.*;
import gr.cti.eslate.eslateCheckBox.*;
import java.awt.*;


public class CheckBoxPrimitives extends PrimitiveGroup {
    MyMachine myMachine;

    protected void setup(Machine machine, Console console) throws SetupException {

        registerPrimitive("CHECKBOX.TIME", "pTIME", 0);
        registerPrimitive("CHECKBOX.TEXT", "pTEXT", 0);
        registerPrimitive("CHECKBOX.CLICK", "pCLICK", 0);
        registerPrimitive("CHECKBOX.ENABLED", "pENABLED", 0);
        registerPrimitive("CHECKBOX.ENABLE", "pENABLE", 0);
        registerPrimitive("CHECKBOX.DISABLE", "pDISABLE", 0);
        registerPrimitive("CHECKBOX.ISOPAQUE", "pISOPAQUE", 0);
        registerPrimitive("CHECKBOX.OPAQUE", "pOPAQUE", 0);
        registerPrimitive("CHECKBOX.NOTOPAQUE", "pNOTOPAQUE", 0);
        registerPrimitive("CHECKBOX.FOCUSPAINTED", "pFOCUSPAINTED", 0);
        registerPrimitive("CHECKBOX.FOCUSPAINT", "pFOCUSPAINT", 0);
        registerPrimitive("CHECKBOX.NOFOCUSPAINT", "pNOFOCUSPAINT", 0);
        registerPrimitive("CHECKBOX.SELECTED", "pSELECTED", 0);
        registerPrimitive("CHECKBOX.SELECT", "pSELECT", 0);
        registerPrimitive("CHECKBOX.UNSELECT", "pUNSELECT", 0);
        registerPrimitive("CHECKBOX.SETTEXT", "pSETTEXT", 1);

        registerPrimitive("CHECKBOX.SETBGCOLOR", "pSETBGCOLOR", 1);
        registerPrimitive("CHECKBOX.SETFGCOLOR", "pSETFGCOLOR", 1);
        registerPrimitive("CHECKBOX.FGCOLOR", "pFGCOLOR", 0);
        registerPrimitive("CHECKBOX.BGCOLOR", "pBGCOLOR", 0);

        myMachine = (MyMachine) machine;

        if (console != null)
            console.putSetupMessage("Loaded CheckBox primitives");

    }

    public final LogoObject pTIME(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        return new LogoWord(System.currentTimeMillis());
    }

    public final LogoObject pTEXT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateCheckBox checkBox = (ESlateCheckBox) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateCheckBox.ESlateCheckBox.class);

        String text = checkBox.getText();

        return new LogoWord(text);
    }

    public final LogoObject pCLICK(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateCheckBox.ESlateCheckBox.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateCheckBox checkBox = (ESlateCheckBox) v.elementAt(i);

            checkBox.doClick();
        }

        return LogoVoid.obj;
    }

    public final LogoObject pSETTEXT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        String text = aLogoObject[0].toString();
        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateCheckBox.ESlateCheckBox.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateCheckBox checkBox = (ESlateCheckBox) v.elementAt(i);

            checkBox.setText(text);
        }
        return LogoVoid.obj;
    }

    public final LogoObject pENABLE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateCheckBox.ESlateCheckBox.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateCheckBox checkBox = (ESlateCheckBox) v.elementAt(i);

            checkBox.setEnabled(true);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pDISABLE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateCheckBox.ESlateCheckBox.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateCheckBox checkBox = (ESlateCheckBox) v.elementAt(i);

            checkBox.setEnabled(false);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pENABLED(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateCheckBox checkBox = (ESlateCheckBox) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateCheckBox.ESlateCheckBox.class);

        return new LogoWord(checkBox.isEnabled());
    }

    public final LogoObject pISOPAQUE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateCheckBox checkBox = (ESlateCheckBox) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateCheckBox.ESlateCheckBox.class);

        return new LogoWord(checkBox.isOpaque());
    }

    public final LogoObject pOPAQUE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateCheckBox.ESlateCheckBox.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateCheckBox checkBox = (ESlateCheckBox) v.elementAt(i);

            checkBox.setOpaque(true);
            checkBox.repaint();
        }

        return LogoVoid.obj;
    }

    public final LogoObject pNOTOPAQUE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateCheckBox.ESlateCheckBox.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateCheckBox checkBox = (ESlateCheckBox) v.elementAt(i);

            checkBox.setOpaque(false);
            checkBox.repaint();
        }

        return LogoVoid.obj;
    }

    public final LogoObject pFOCUSPAINTED(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateCheckBox checkBox = (ESlateCheckBox) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateCheckBox.ESlateCheckBox.class);

        return new LogoWord(checkBox.isFocusPainted());
    }

    public final LogoObject pFOCUSPAINT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateCheckBox.ESlateCheckBox.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateCheckBox checkBox = (ESlateCheckBox) v.elementAt(i);

            checkBox.setFocusPainted(true);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pNOFOCUSPAINT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateCheckBox.ESlateCheckBox.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateCheckBox checkBox = (ESlateCheckBox) v.elementAt(i);

            checkBox.setFocusPainted(false);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pSELECTED(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateCheckBox checkBox = (ESlateCheckBox) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateCheckBox.ESlateCheckBox.class);

        return new LogoWord(checkBox.isSelected());
    }

    public final LogoObject pSELECT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateCheckBox.ESlateCheckBox.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateCheckBox checkBox = (ESlateCheckBox) v.elementAt(i);

            checkBox.setSelected(true);
        }
        return LogoVoid.obj;
    }

    public final LogoObject pUNSELECT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateCheckBox.ESlateCheckBox.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateCheckBox checkBox = (ESlateCheckBox) v.elementAt(i);

            checkBox.setSelected(false);
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

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateCheckBox.ESlateCheckBox.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateCheckBox checkBox = (ESlateCheckBox) v.elementAt(i);

            checkBox.setBackground(bgColor);
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

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateCheckBox.ESlateCheckBox.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateCheckBox checkBox = (ESlateCheckBox) v.elementAt(i);

            checkBox.setForeground(fgColor);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pBGCOLOR(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateCheckBox checkBox = (ESlateCheckBox) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateCheckBox.ESlateCheckBox.class);

        Vector vColor = new Vector();
        LogoWord red = new LogoWord(checkBox.getBackground().getRed());
        LogoWord green = new LogoWord(checkBox.getBackground().getGreen());
        LogoWord blue = new LogoWord(checkBox.getBackground().getBlue());

        vColor.addElement(red);
        vColor.addElement(green);
        vColor.addElement(blue);

        return new LogoList(vColor);
    }

    public final LogoObject pFGCOLOR(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateCheckBox checkBox = (ESlateCheckBox) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateCheckBox.ESlateCheckBox.class);

        try {
            Vector vColor = new Vector();
            LogoWord red = new LogoWord(checkBox.getForeground().getRed());
            LogoWord green = new LogoWord(checkBox.getForeground().getGreen());
            LogoWord blue = new LogoWord(checkBox.getForeground().getBlue());

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
