package gr.cti.eslate.scripting.logo;


import java.util.*;
import virtuoso.logo.*;
import gr.cti.eslate.eslateTextField.*;
import java.awt.*;


public class TextFieldPrimitives extends PrimitiveGroup {
    MyMachine myMachine;

    protected void setup(Machine machine, Console console) throws SetupException {
        registerPrimitive("TFIELD.COPY", "pCOPY", 0);
        registerPrimitive("TFIELD.PASTE", "pPASTE", 0);
        registerPrimitive("TFIELD.CUT", "pCUT", 0);
        registerPrimitive("TFIELD.TEXT", "pTEXT", 0);
        registerPrimitive("TFIELD.SETTEXT", "pSETTEXT", 1);
        registerPrimitive("TFIELD.SELECTALL", "pSELECTALL", 0);
        registerPrimitive("TFIELD.ENABLE", "pENABLE", 0);
        registerPrimitive("TFIELD.DISABLE", "pDISABLE", 0);
        registerPrimitive("TFIELD.OPAQUE", "pOPAQUE", 0);
        registerPrimitive("TFIELD.NOTOPAQUE", "pNOTOPAQUE", 0);
        registerPrimitive("TFIELD.EDITABLE", "pEDITABLE", 0);
        registerPrimitive("TFIELD.NOTEDITABLE", "pNOTEDITABLE", 0);
        registerPrimitive("TFIELD.ISEDITABLE", "pISEDITABLE", 0);
        registerPrimitive("TFIELD.ISOPAQUE", "pISOPAQUE", 0);
        registerPrimitive("TFIELD.ENABLED", "pENABLED", 0);

        registerPrimitive("TFIELD.SETBGCOLOR", "pSETBGCOLOR", 1);
        registerPrimitive("TFIELD.SETFGCOLOR", "pSETFGCOLOR", 1);
        registerPrimitive("TFIELD.FGCOLOR", "pFGCOLOR", 0);
        registerPrimitive("TFIELD.BGCOLOR", "pBGCOLOR", 0);

        myMachine = (MyMachine) machine;

        if (console != null)
            console.putSetupMessage("Loaded TextField primitives");

    }

    public final LogoObject pCOPY(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateTextField field = (ESlateTextField) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateTextField.ESlateTextField.class);

        field.copy();

        return LogoVoid.obj;
    }

    public final LogoObject pPASTE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateTextField.ESlateTextField.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateTextField field = (ESlateTextField) v.elementAt(i);

            field.paste();
        }
      
        return LogoVoid.obj;
    }

    public final LogoObject pCUT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);
        ESlateTextField field = (ESlateTextField) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateTextField.ESlateTextField.class);

        field.cut();

        return LogoVoid.obj;
    }

    public final LogoObject pSETTEXT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        String text = aLogoObject[0].toString();
        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateTextField.ESlateTextField.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateTextField field = (ESlateTextField) v.elementAt(i);

            field.setText(text);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pENABLE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateTextField.ESlateTextField.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateTextField field = (ESlateTextField) v.elementAt(i);

            field.setEnabled(true);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pDISABLE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateTextField.ESlateTextField.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateTextField field = (ESlateTextField) v.elementAt(i);

            field.setEnabled(false);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pOPAQUE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateTextField.ESlateTextField.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateTextField field = (ESlateTextField) v.elementAt(i);

            field.setOpaque(true);
            field.repaint();
        }

        return LogoVoid.obj;
    }

    public final LogoObject pNOTOPAQUE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateTextField.ESlateTextField.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateTextField field = (ESlateTextField) v.elementAt(i);

            field.setOpaque(false);
            field.repaint();
        }

        return LogoVoid.obj;
    }

    public final LogoObject pEDITABLE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateTextField.ESlateTextField.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateTextField field = (ESlateTextField) v.elementAt(i);

            field.setEditable(true);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pNOTEDITABLE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateTextField.ESlateTextField.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateTextField field = (ESlateTextField) v.elementAt(i);

            field.setEditable(false);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pSELECTALL(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateTextField.ESlateTextField.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateTextField field = (ESlateTextField) v.elementAt(i);

            field.selectAll();
        }

        return LogoVoid.obj;
    }

    public final LogoObject pTEXT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateTextField field = (ESlateTextField) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateTextField.ESlateTextField.class);

        String text = field.getText();

        return new LogoWord(text);
    }

    public final LogoObject pISEDITABLE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateTextField field = (ESlateTextField) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateTextField.ESlateTextField.class);

        return new LogoWord(field.isEditable());
    }

    public final LogoObject pISOPAQUE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateTextField field = (ESlateTextField) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateTextField.ESlateTextField.class);

        return new LogoWord(field.isOpaque());
    }

    public final LogoObject pENABLED(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateTextField field = (ESlateTextField) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateTextField.ESlateTextField.class);

        return new LogoWord(field.isEnabled());
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
        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateTextField.ESlateTextField.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateTextField field = (ESlateTextField) v.elementAt(i);

            field.setBackground(bgColor);
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
        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateTextField.ESlateTextField.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateTextField field = (ESlateTextField) v.elementAt(i);

            field.setForeground(fgColor);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pBGCOLOR(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateTextField field = (ESlateTextField) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateTextField.ESlateTextField.class);

        Vector vColor = new Vector();
        LogoWord red = new LogoWord(field.getBackground().getRed());
        LogoWord green = new LogoWord(field.getBackground().getGreen());
        LogoWord blue = new LogoWord(field.getBackground().getBlue());

        vColor.addElement(red);
        vColor.addElement(green);
        vColor.addElement(blue);

        return new LogoList(vColor);
    }

    public final LogoObject pFGCOLOR(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateTextField field = (ESlateTextField) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateTextField.ESlateTextField.class);

        try {
            Vector vColor = new Vector();
            LogoWord red = new LogoWord(field.getForeground().getRed());
            LogoWord green = new LogoWord(field.getForeground().getGreen());
            LogoWord blue = new LogoWord(field.getForeground().getBlue());

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
