package gr.cti.eslate.scripting.logo;


import java.util.*;
import virtuoso.logo.*;
import gr.cti.eslate.eslateComboBox.*;
import gr.cti.eslate.eslateComboBox.ESlateComboBox;
import java.awt.*;


public class ComboBoxPrimitives extends PrimitiveGroup {
    MyMachine myMachine;

    protected void setup(Machine machine, Console console) throws SetupException {
        registerPrimitive("COMBOBOX.ADDITEM", "pADDITEM", 1);
        registerPrimitive("COMBOBOX.ITEM", "pITEM", 0);
        registerPrimitive("COMBOBOX.SETITEM", "pSETITEM", 1);     //georgia
        registerPrimitive("COMBOBOX.ITEMCOUNT", "pITEMCOUNT", 0);
        registerPrimitive("COMBOBOX.ENABLE", "pENABLE", 0);
        registerPrimitive("COMBOBOX.DISABLE", "pDISABLE", 0);
        registerPrimitive("COMBOBOX.OPAQUE", "pOPAQUE", 0);
        registerPrimitive("COMBOBOX.NOTOPAQUE", "pNOTOPAQUE", 0);
        registerPrimitive("COMBOBOX.EDITABLE", "pEDITABLE", 0);
        registerPrimitive("COMBOBOX.NOTEDITABLE", "pNOTEDITABLE", 0);
        registerPrimitive("COMBOBOX.REMOVEALL", "pREMOVEALL", 0);
        registerPrimitive("COMBOBOX.REMOVE", "pREMOVE", 1);
        registerPrimitive("COMBOBOX.SELECTEDINDEX", "pSELECTEDINDEX", 0);
        registerPrimitive("COMBOBOX.SELECTEDITEM", "pSELECTEDITEM", 0);
        registerPrimitive("COMBOBOX.SETSELECTEDINDEX", "pSETSELECTEDINDEX", 1);
        registerPrimitive("COMBOBOX.SETSELECTEDITEM", "pSETSELECTEDITEM", 1);

        registerPrimitive("COMBOBOX.ENABLED", "pENABLED", 0);
        registerPrimitive("COMBOBOX.ISOPAQUE", "pISOPAQUE", 0);
        registerPrimitive("COMBOBOX.ISEDITABLE", "pISEDITABLE", 0);

        registerPrimitive("COMBOBOX.SETBGCOLOR", "pSETBGCOLOR", 1);
        registerPrimitive("COMBOBOX.SETFGCOLOR", "pSETFGCOLOR", 1);
        registerPrimitive("COMBOBOX.FGCOLOR", "pFGCOLOR", 0);
        registerPrimitive("COMBOBOX.BGCOLOR", "pBGCOLOR", 0);

        myMachine = (MyMachine) machine;

        if (console != null)
            console.putSetupMessage("Loaded ComboBox primitives");

    }

    public final LogoObject pADDITEM(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 2);
        testMinParams(aLogoObject, 1);

        int index = -1;
        String item;

        if (aLogoObject.length == 2) {
            index = aLogoObject[1].toInteger();
            item = aLogoObject[0].toString();
        } else {
            item = aLogoObject[0].toString();
        }

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateComboBox.ESlateComboBox.class);

        for (int i = 0; i < v.size(); i++) {
            ESlateComboBox combobox = (ESlateComboBox) v.elementAt(i);
            ComboBoxVector vector = new ComboBoxVector();
            int modelSize = combobox.getModel().getSize();

            for (int k = 0; k < modelSize; k++) {
                vector.add(k, combobox.getModel().getElementAt(k));
            }

            int pos = index;

            if (pos == -1)
                pos = combobox.getItemCount() + 1;
            if (pos < 1 || pos > combobox.getItemCount() + 1)
                throw new LanguageException("Invalid position " + pos + " in ComboBox.Please select an integer between 1 and " + combobox.getItemCount());

            vector.insertElementAt(item, pos - 1);

            combobox.setModel(vector);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pITEM(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMinParams(aLogoObject, 0);
        testMaxParams(aLogoObject, 1);
        int index = -1;

        if (aLogoObject.length == 1)
            index = aLogoObject[0].toInteger();

        ESlateComboBox combobox = (ESlateComboBox) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateComboBox.ESlateComboBox.class);

        if (index == -1)
            index = combobox.getSelectedIndex();
        if (index == -1)
            throw new LanguageException("There is no selected item in the ESlateComboBox");
        if (index < 1 || index >= combobox.getItemCount() + 1)
            throw new LanguageException("Invalid index " + index + ". The ESlateComboBox has only " + combobox.getItemCount() + " items.");

        String text = (String) combobox.getItemAt(index);

        return new LogoWord(text);
    }

    public final LogoObject pSETITEM(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMinParams(aLogoObject, 1);
        testMaxParams(aLogoObject, 2);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateComboBox.ESlateComboBox.class);

        for (int k = 0; k < v.size(); k++) {
            ESlateComboBox combobox = (ESlateComboBox) v.elementAt(k);
            ComboBoxVector vector = new ComboBoxVector();
            int modelSize = combobox.getModel().getSize();

            for (int i = 0; i < modelSize; i++) {
                vector.add(i, combobox.getModel().getElementAt(i));
            }

            if (aLogoObject.length == 2) {
                String text = aLogoObject[0].toString();
                int index = aLogoObject[1].toInteger();

                if (index < 0 || index > combobox.getItemCount())
                    throw new LanguageException("Invalid position " + index + " in Combobox. Please select between 1 and " + combobox.getItemCount());

                vector.setElementAt(text, index - 1);
            } else {
                String text = aLogoObject[0].toString();
                int index = combobox.getSelectedIndex();

                if (index == -1)
                    throw new LanguageException("There is no selected item in Combobox to tell this to");
                vector.setElementAt(text, index - 1);
            }
            int selIndex = combobox.getSelectedIndex();

            combobox.setModel(vector);
            combobox.setSelectedIndex(selIndex);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pITEMCOUNT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        int count = 0;
        ESlateComboBox combobox = (ESlateComboBox) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateComboBox.ESlateComboBox.class);

        if (aLogoObject.length == 0)
            count = combobox.getModel().getSize();

        return new LogoWord(count);
    }

    public final LogoObject pREMOVEALL(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateComboBox.ESlateComboBox.class);

        for (int k = 0; k < v.size(); k++) {
            ESlateComboBox combobox = (ESlateComboBox) v.elementAt(k);

            ComboBoxVector vector = new ComboBoxVector();

            combobox.setModel(vector);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pREMOVE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        int index = 0;

        index = aLogoObject[0].toInteger();

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateComboBox.ESlateComboBox.class);

        for (int k = 0; k < v.size(); k++) {
            ESlateComboBox combobox = (ESlateComboBox) v.elementAt(k);
            ComboBoxVector vector = new ComboBoxVector();

            if (index < 1 || aLogoObject.length != 1 || (combobox.getModel().getSize()) < index || combobox.getModel().getSize() <= 0)
                if (combobox.getModel().getSize() == 0)
                    throw new LanguageException("Invalid position value " + index + " in ComboBox. The Combobox is empty");
                else
                    throw new LanguageException("Invalid position value " + index + " in ComboBox. Please select an integer between 1 and " + (combobox.getModel().getSize()));

            for (int i = 0; i < combobox.getModel().getSize(); i++) {
                vector.add(i, combobox.getModel().getElementAt(i));
            }
            vector.remove(index - 1);
            combobox.setModel(vector);
        }

        return LogoVoid.obj;

    }

    public final LogoObject pSELECTEDINDEX(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);
        int index;

        ESlateComboBox combobox = (ESlateComboBox) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateComboBox.ESlateComboBox.class);

        index = combobox.getSelectedIndex();

        return new LogoWord(index);
    }

    public final LogoObject pSETSELECTEDINDEX(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);
        int index;

        index = aLogoObject[0].toInteger();

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateComboBox.ESlateComboBox.class);

        for (int k = 0; k < v.size(); k++) {
            ESlateComboBox combobox = (ESlateComboBox) v.elementAt(k);

            if (index < 1 || index > combobox.getModel().getSize() || combobox.getModel().getSize() <= 0)
                if (combobox.getModel().getSize() == 0)
                    throw new LanguageException("Invalid position " + index + " in Combobox. The Combobox is empty");
                else
                    throw new LanguageException("Invalid position " + index + " in Combobox. Please select an integer between 1 and " + (combobox.getModel().getSize()));

            combobox.setSelectedIndex(index - 1);
            combobox.repaint();
        }
        return LogoVoid.obj;
    }

    public final LogoObject pSELECTEDITEM(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);
        String item = "";

        ESlateComboBox combobox = (ESlateComboBox) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateComboBox.ESlateComboBox.class);

        item = (String) combobox.getModel().getSelectedItem();

        return new LogoWord(item);
    }

    public final LogoObject pSETSELECTEDITEM(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);
        String item;

        item = aLogoObject[0].toString();
        boolean itemExists = false;

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateComboBox.ESlateComboBox.class);

        for (int k = 0; k < v.size(); k++) {
            ESlateComboBox combobox = (ESlateComboBox) v.elementAt(k);

            itemExists = false;
            for (int l = 0; l < combobox.getModel().getSize(); l++) {
                if (item.equals(combobox.getModel().getElementAt(l))) {
                    itemExists = true;
                }
            }
            if (!itemExists)
                throw new LanguageException("Invalid item: " + item);

            combobox.getModel().setSelectedItem(item);
            combobox.repaint();
        }

        return LogoVoid.obj;
    }

    public final LogoObject pENABLE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateComboBox.ESlateComboBox.class);

        for (int k = 0; k < v.size(); k++) {
            ESlateComboBox combobox = (ESlateComboBox) v.elementAt(k);

            combobox.setEnabled(true);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pDISABLE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateComboBox.ESlateComboBox.class);

        for (int k = 0; k < v.size(); k++) {
            ESlateComboBox combobox = (ESlateComboBox) v.elementAt(k);

            combobox.setEnabled(false);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pOPAQUE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateComboBox.ESlateComboBox.class);

        for (int k = 0; k < v.size(); k++) {
            ESlateComboBox combobox = (ESlateComboBox) v.elementAt(k);

            combobox.setOpaque(true);
            combobox.repaint();
        }

        return LogoVoid.obj;
    }

    public final LogoObject pNOTOPAQUE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateComboBox.ESlateComboBox.class);

        for (int k = 0; k < v.size(); k++) {
            ESlateComboBox combobox = (ESlateComboBox) v.elementAt(k);

            combobox.setOpaque(false);
            combobox.repaint();
        }

        return LogoVoid.obj;
    }

    public final LogoObject pEDITABLE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateComboBox.ESlateComboBox.class);

        for (int k = 0; k < v.size(); k++) {
            ESlateComboBox combobox = (ESlateComboBox) v.elementAt(k);

            combobox.setEditable(true);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pNOTEDITABLE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateComboBox.ESlateComboBox.class);

        for (int k = 0; k < v.size(); k++) {
            ESlateComboBox combobox = (ESlateComboBox) v.elementAt(k);

            combobox.setEditable(false);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pISEDITABLE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateComboBox combobox = (ESlateComboBox) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateComboBox.ESlateComboBox.class);

        return new LogoWord(combobox.isEditable());
    }

    public final LogoObject pISOPAQUE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateComboBox combobox = (ESlateComboBox) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateComboBox.ESlateComboBox.class);

        return new LogoWord(combobox.isOpaque());
    }

    public final LogoObject pENABLED(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateComboBox combobox = (ESlateComboBox) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateComboBox.ESlateComboBox.class);

        return new LogoWord(combobox.isEnabled());
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

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateComboBox.ESlateComboBox.class);

        for (int k = 0; k < v.size(); k++) {
            ESlateComboBox combobox = (ESlateComboBox) v.elementAt(k);

            combobox.setBackground(bgColor);
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

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateComboBox.ESlateComboBox.class);

        for (int k = 0; k < v.size(); k++) {
            ESlateComboBox combobox = (ESlateComboBox) v.elementAt(k);

            combobox.setForeground(fgColor);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pBGCOLOR(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateComboBox combobox = (ESlateComboBox) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateComboBox.ESlateComboBox.class);

        Vector vColor = new Vector();
        LogoWord red = new LogoWord(combobox.getBackground().getRed());
        LogoWord green = new LogoWord(combobox.getBackground().getGreen());
        LogoWord blue = new LogoWord(combobox.getBackground().getBlue());

        vColor.addElement(red);
        vColor.addElement(green);
        vColor.addElement(blue);

        return new LogoList(vColor);
    }

    public final LogoObject pFGCOLOR(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateComboBox combobox = (ESlateComboBox) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateComboBox.ESlateComboBox.class);

        try {
            Vector vColor = new Vector();
            LogoWord red = new LogoWord(combobox.getForeground().getRed());
            LogoWord green = new LogoWord(combobox.getForeground().getGreen());
            LogoWord blue = new LogoWord(combobox.getForeground().getBlue());

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
