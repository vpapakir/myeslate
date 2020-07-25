package gr.cti.eslate.scripting.logo;


import java.util.*;
import virtuoso.logo.*;
import gr.cti.eslate.eslateTextArea.*;
import java.awt.*;


public class TextAreaPrimitives extends PrimitiveGroup {
    MyMachine myMachine;

    protected void setup(Machine machine, Console console) throws SetupException {
        registerPrimitive("AREA.COPY", "pCOPY", 0);
        registerPrimitive("AREA.PASTE", "pPASTE", 0);
        registerPrimitive("AREA.CUT", "pCUT", 0);
        registerPrimitive("AREA.TEXT", "pTEXT", 0);
        registerPrimitive("AREA.SETTEXT", "pSETTEXT", 1);
        registerPrimitive("AREA.SELECTALL", "pSELECTALL", 0);
        registerPrimitive("AREA.ENABLE", "pENABLE", 0);
        registerPrimitive("AREA.DISABLE", "pDISABLE", 0);
        registerPrimitive("AREA.OPAQUE", "pOPAQUE", 0);
        registerPrimitive("AREA.NOTOPAQUE", "pNOTOPAQUE", 0);
        registerPrimitive("AREA.EDITABLE", "pEDITABLE", 0);
        registerPrimitive("AREA.NOTEDITABLE", "pNOTEDITABLE", 0);
        registerPrimitive("AREA.APPEND", "pAPPEND", 1);
        registerPrimitive("AREA.INSERT", "pINSERT", 2);
        registerPrimitive("AREA.LINEWRAP", "pLINEWRAP", 0);
        registerPrimitive("AREA.NOLINEWRAP", "pNOLINEWRAP", 0);
        registerPrimitive("AREA.GETLINEWRAP", "pGETLINEWRAP", 0);
        registerPrimitive("AREA.LINECOUNT", "pLINECOUNT", 0);
        registerPrimitive("AREA.ISEDITABLE", "pISEDITABLE", 0);
        registerPrimitive("AREA.ISOPAQUE", "pISOPAQUE", 0);
        registerPrimitive("AREA.ENABLED", "pENABLED", 0);

        registerPrimitive("AREA.SETBGCOLOR", "pSETBGCOLOR", 1);
        registerPrimitive("AREA.SETFGCOLOR", "pSETFGCOLOR", 1);
        registerPrimitive("AREA.FGCOLOR", "pFGCOLOR", 0);
        registerPrimitive("AREA.BGCOLOR", "pBGCOLOR", 0);

        myMachine = (MyMachine) machine;

        if (console != null)
            console.putSetupMessage("Loaded TextArea primitives");

    }

    public final LogoObject pCOPY(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateTextArea area = (ESlateTextArea) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateTextArea.ESlateTextArea.class);

        area.getTextArea().copy();

        return LogoVoid.obj;
    }

    public final LogoObject pPASTE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateTextArea area = (ESlateTextArea) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateTextArea.ESlateTextArea.class);

        area.getTextArea().paste();
        return LogoVoid.obj;
    }

    public final LogoObject pCUT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateTextArea area = (ESlateTextArea) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateTextArea.ESlateTextArea.class);

        area.getTextArea().cut();

        return LogoVoid.obj;
    }

    public final LogoObject pSETTEXT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        String text = aLogoObject[0].toString();
        Vector<?> v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateTextArea.ESlateTextArea.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateTextArea area = (ESlateTextArea) v.elementAt(i);

            area.setText(text);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pENABLE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector<?> v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateTextArea.ESlateTextArea.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateTextArea area = (ESlateTextArea) v.elementAt(i);

            area.setEnabled(true);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pDISABLE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector<?> v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateTextArea.ESlateTextArea.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateTextArea area = (ESlateTextArea) v.elementAt(i);

            area.setEnabled(false);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pOPAQUE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector<?> v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateTextArea.ESlateTextArea.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateTextArea area = (ESlateTextArea) v.elementAt(i);

            area.setOpaque(true);
            area.repaint();
        }

        return LogoVoid.obj;
    }

    public final LogoObject pNOTOPAQUE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector<?> v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateTextArea.ESlateTextArea.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateTextArea area = (ESlateTextArea) v.elementAt(i);

            area.setOpaque(false);
            area.repaint();
        }

        return LogoVoid.obj;
    }

    public final LogoObject pEDITABLE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector<?> v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateTextArea.ESlateTextArea.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateTextArea area = (ESlateTextArea) v.elementAt(i);

            area.getTextArea().setEditable(true);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pNOTEDITABLE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector<?> v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateTextArea.ESlateTextArea.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateTextArea area = (ESlateTextArea) v.elementAt(i);

            area.getTextArea().setEditable(false);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pSELECTALL(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector<?> v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateTextArea.ESlateTextArea.class);
        //for (int i=0; i<v.size(); i++) {
        ESlateTextArea area = (ESlateTextArea) v.elementAt(0);

        area.getTextArea().selectAll();

        //}

        return LogoVoid.obj;
    }

    public final LogoObject pTEXT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateTextArea area = (ESlateTextArea) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateTextArea.ESlateTextArea.class);

        String text = area.getTextArea().getText();

        return new LogoWord(text);
    }

    public final LogoObject pAPPEND(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        String appendtext = "";

        appendtext = aLogoObject[0].toString();

        Vector<?> v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateTextArea.ESlateTextArea.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateTextArea area = (ESlateTextArea) v.elementAt(i);
            String text = area.getText();

            area.setText(text + appendtext);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pLINECOUNT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateTextArea area = (ESlateTextArea) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateTextArea.ESlateTextArea.class);

        int lines = area.getTextArea().getLineCount();

        return new LogoWord(lines);
    }

    public final LogoObject pINSERT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 2);

        String inserttext = aLogoObject[0].toString();
        int position = aLogoObject[1].toInteger();

        Vector<?> v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateTextArea.ESlateTextArea.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateTextArea area = (ESlateTextArea) v.elementAt(i);
            String text = area.getTextArea().getText();

            if (position < 1 || position > text.length() + 1)     //prosthetei apo th thesh 1 ews kai sto telos
                throw new LanguageException("Invalid position " + position + ". Please select an integer between 1 and " + (text.length() + 1));

            String newtext = text.substring(0, position - 1) + inserttext + text.substring(position - 1, text.length());

            area.setText(newtext);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pLINEWRAP(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector<?> v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateTextArea.ESlateTextArea.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateTextArea area = (ESlateTextArea) v.elementAt(i);

            area.getTextArea().setLineWrap(true);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pNOLINEWRAP(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector<?> v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateTextArea.ESlateTextArea.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateTextArea area = (ESlateTextArea) v.elementAt(i);

            area.getTextArea().setLineWrap(false);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pISEDITABLE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateTextArea area = (ESlateTextArea) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateTextArea.ESlateTextArea.class);

        return new LogoWord(area.getTextArea().isEditable());
    }

    public final LogoObject pISOPAQUE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateTextArea area = (ESlateTextArea) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateTextArea.ESlateTextArea.class);

        return new LogoWord(area.getTextArea().isOpaque());
    }

    public final LogoObject pENABLED(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateTextArea area = (ESlateTextArea) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateTextArea.ESlateTextArea.class);

        return new LogoWord(area.getTextArea().isEnabled());
    }

    public final LogoObject pGETLINEWRAP(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateTextArea area = (ESlateTextArea) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateTextArea.ESlateTextArea.class);

        return new LogoWord(area.getTextArea().getLineWrap());
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
        Vector<?> v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateTextArea.ESlateTextArea.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateTextArea area = (ESlateTextArea) v.elementAt(i);

            area.setBackground(bgColor);
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
        Vector<?> v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateTextArea.ESlateTextArea.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateTextArea area = (ESlateTextArea) v.elementAt(i);

            area.setForeground(fgColor);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pBGCOLOR(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateTextArea area = (ESlateTextArea) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateTextArea.ESlateTextArea.class);

        Vector<LogoWord> vColor = new Vector<LogoWord>();
        LogoWord red = new LogoWord(area.getBackground().getRed());
        LogoWord green = new LogoWord(area.getBackground().getGreen());
        LogoWord blue = new LogoWord(area.getBackground().getBlue());

        vColor.addElement(red);
        vColor.addElement(green);
        vColor.addElement(blue);

        return new LogoList(vColor);
    }

    public final LogoObject pFGCOLOR(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateTextArea area = (ESlateTextArea) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateTextArea.ESlateTextArea.class);

        try {
            Vector<LogoWord> vColor = new Vector<LogoWord>();
            LogoWord red = new LogoWord(area.getForeground().getRed());
            LogoWord green = new LogoWord(area.getForeground().getGreen());
            LogoWord blue = new LogoWord(area.getForeground().getBlue());

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
