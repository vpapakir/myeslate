package gr.cti.eslate.scripting.logo;


import java.util.*;
import virtuoso.logo.*;
import gr.cti.eslate.eslateList.*;
import gr.cti.eslate.eslateList.ESlateList;
import java.awt.*;


public class ESlateListPrimitives extends PrimitiveGroup {
    MyMachine myMachine;

    protected void setup(Machine machine, Console console) throws SetupException {
        registerPrimitive("LIST.ADDITEM", "pADDITEM", 1);
        registerPrimitive("LIST.ITEMAT", "pITEMAT", 1);
        registerPrimitive("LIST.ITEMCOUNT", "pITEMCOUNT", 0);
        registerPrimitive("LIST.ENABLE", "pENABLE", 0);
        registerPrimitive("LIST.DISABLE", "pDISABLE", 0);
        registerPrimitive("LIST.OPAQUE", "pOPAQUE", 0);
        registerPrimitive("LIST.NOTOPAQUE", "pNOTOPAQUE", 0);
        registerPrimitive("LIST.REMOVEALL", "pREMOVEALL", 0);
        registerPrimitive("LIST.REMOVE", "pREMOVE", 1);
        registerPrimitive("LIST.SELECTEDINDEX", "pSELECTEDINDEX", 0);
        registerPrimitive("LIST.SETSELECTEDINDEX", "pSETSELECTEDINDEX", 1);
        registerPrimitive("LIST.SELECTEDITEM", "pSELECTEDITEM", 0);
        registerPrimitive("LIST.SETSELECTEDITEM", "pSETSELECTEDITEM", 1);

        registerPrimitive("LIST.ENABLED", "pENABLED", 0);
        registerPrimitive("LIST.ISOPAQUE", "pISOPAQUE", 0);

        registerPrimitive("LIST.SETBGCOLOR", "pSETBGCOLOR", 1);
        registerPrimitive("LIST.SETFGCOLOR", "pSETFGCOLOR", 1);
        registerPrimitive("LIST.FGCOLOR", "pFGCOLOR", 0);
        registerPrimitive("LIST.BGCOLOR", "pBGCOLOR", 0);

        myMachine = (MyMachine) machine;

        if (console != null)
            console.putSetupMessage("Loaded List primitives");

    }

    public final LogoObject pADDITEM(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 2);
        testMinParams(aLogoObject, 1);

        int index = 0;
        String item;

        if (aLogoObject.length == 1) {
            item = aLogoObject[0].toString();
        } else {
            index = aLogoObject[1].toInteger();
            item = aLogoObject[0].toString();
        }

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateList.ESlateList.class);

        for (int k = 0; k < v.size(); k++) {
            ESlateList list = (ESlateList) v.elementAt(k);
            ListVector vector = new ListVector();

            for (int i = 0; i < list.getModel().getSize(); i++) {
                vector.add(i, list.getModel().getElementAt(i));
            }

            if (aLogoObject.length == 2 && list.getModel().getSize() + 1 < index && list.getModel().getSize() >= 0 && index > 0)
                throw new LanguageException("No such active position in List. Please select a valid position to enter the new item ");
            if (aLogoObject.length == 1) {
                vector.addElement(item);
            } else {
                vector.insertElementAt(item, index - 1);
            }

            list.setModel(vector);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pITEMAT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);
        int index = 0;
        String text = "";

        index = aLogoObject[0].toInteger();

        ESlateList list = (ESlateList) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateList.ESlateList.class);

        if (list.getModel().getSize() < index && list.getModel().getSize() >= 0 && index > 0)
            throw new LanguageException("No such active position in List. Please select a valid position to enter the new item ");
        else {
            text = (String) list.getModel().getElementAt(index - 1);
        }

        return new LogoWord(text);
    }

    public final LogoObject pITEMCOUNT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        int count = 0;

        ESlateList list = (ESlateList) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateList.ESlateList.class);

        if (aLogoObject.length == 0)
            count = list.getModel().getSize();

        return new LogoWord(count);
    }

    public final LogoObject pREMOVEALL(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateList.ESlateList.class);

        for (int k = 0; k < v.size(); k++) {
            ESlateList list = (ESlateList) v.elementAt(k);
            ListVector vector = new ListVector();

            list.setModel(vector);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pREMOVE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        int index = 0;

        index = aLogoObject[0].toInteger();

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateList.ESlateList.class);

        for (int k = 0; k < v.size(); k++) {
            ESlateList list = (ESlateList) v.elementAt(k);

            if (index <= 0 || list.getModel().getSize() < index || list.getModel().getSize() <= 0)
                if (list.getModel().getSize() == 0)
                    throw new LanguageException("Invalid position " + index + " in List. There are no items in List");
                else
                    throw new LanguageException("Invalid position " + index + " in List. Please select an integer between 1 and " + list.getModel().getSize());

            ListVector vector = new ListVector();

            for (int i = 0; i < list.getModel().getSize(); i++) {
                vector.add(i, list.getModel().getElementAt(i));
            }
            vector.remove(index - 1);
            list.setModel(vector);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pSELECTEDINDEX(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);
        int index;

        ESlateList list = (ESlateList) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateList.ESlateList.class);

        index = list.getSelectedIndex();

        return new LogoWord(index);
    }

    public final LogoObject pSETSELECTEDINDEX(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);
        int index;

        index = aLogoObject[0].toInteger();

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateList.ESlateList.class);

        for (int k = 0; k < v.size(); k++) {
            ESlateList list = (ESlateList) v.elementAt(k);

            if (index + 1 < list.getModel().getSize() && index > 0)
                list.setSelectedIndex(index - 1);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pSELECTEDITEM(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);
        String item;

        ESlateList list = (ESlateList) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateList.ESlateList.class);

        item = list.getSelectedValue().toString();

        return new LogoWord(item);
    }

    public final LogoObject pSETSELECTEDITEM(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);
        String item;

        item = aLogoObject[0].toString();

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateList.ESlateList.class);

        for (int k = 0; k < v.size(); k++) {
            ESlateList list = (ESlateList) v.elementAt(k);

            list.setSelectedValue(item/*,true*/);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pENABLE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateList.ESlateList.class);

        for (int k = 0; k < v.size(); k++) {
            ESlateList list = (ESlateList) v.elementAt(k);

            list.setEnabled(true);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pDISABLE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateList.ESlateList.class);

        for (int k = 0; k < v.size(); k++) {
            ESlateList list = (ESlateList) v.elementAt(k);

            list.setEnabled(false);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pOPAQUE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateList.ESlateList.class);

        for (int k = 0; k < v.size(); k++) {
            ESlateList list = (ESlateList) v.elementAt(k);

            list.setOpaque(true);
            list.repaint();
        }

        return LogoVoid.obj;
    }

    public final LogoObject pNOTOPAQUE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateList.ESlateList.class);

        for (int k = 0; k < v.size(); k++) {
            ESlateList list = (ESlateList) v.elementAt(k);

            list.setOpaque(false);
            list.repaint();
        }

        return LogoVoid.obj;
    }

    public final LogoObject pENABLED(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateList list = (ESlateList) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateList.ESlateList.class);

        return new LogoWord(list.isEnabled());
    }

    public final LogoObject pISOPAQUE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateList list = (ESlateList) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateList.ESlateList.class);

        return new LogoWord(list.isOpaque());
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
        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateList.ESlateList.class);

        for (int k = 0; k < v.size(); k++) {
            ESlateList list = (ESlateList) v.elementAt(k);

            list.setBackground(bgColor);
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
        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateList.ESlateList.class);

        for (int k = 0; k < v.size(); k++) {
            ESlateList list = (ESlateList) v.elementAt(k);

            list.setForeground(fgColor);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pBGCOLOR(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateList list = (ESlateList) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateList.ESlateList.class);

        Vector vColor = new Vector();
        LogoWord red = new LogoWord(list.getBackground().getRed());
        LogoWord green = new LogoWord(list.getBackground().getGreen());
        LogoWord blue = new LogoWord(list.getBackground().getBlue());

        vColor.addElement(red);
        vColor.addElement(green);
        vColor.addElement(blue);

        return new LogoList(vColor);
    }

    public final LogoObject pFGCOLOR(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateList list = (ESlateList) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateList.ESlateList.class);

        try {
            Vector vColor = new Vector();
            LogoWord red = new LogoWord(list.getForeground().getRed());
            LogoWord green = new LogoWord(list.getForeground().getGreen());
            LogoWord blue = new LogoWord(list.getForeground().getBlue());

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
