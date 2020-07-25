package gr.cti.eslate.scripting.logo;


import java.util.*;
import virtuoso.logo.*;
import gr.cti.eslate.eslateMenuBar.*;
import gr.cti.eslate.eslateMenuBar.ESlateMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.*;


public class MenuBarPrimitives extends PrimitiveGroup {
    MyMachine myMachine;

    protected void setup(Machine machine, Console console) throws SetupException {
        registerPrimitive("MENUBAR.ADDITEM", "pADDITEM", 3);
        registerPrimitive("MENUBAR.DELETEITEM", "pDELETEITEM", 1);
        registerPrimitive("MENUBAR.SETITEMTEXT", "pSETITEMTEXT", 2);
        registerPrimitive("MENUBAR.ITEMTEXT", "pITEMTEXT", 2);
        registerPrimitive("MENUBAR.ITEMCOUNT", "pITEMCOUNT", 1);
        registerPrimitive("MENUBAR.ENABLE", "pENABLE", 0);
        registerPrimitive("MENUBAR.DISABLE", "pDISABLE", 0);
        registerPrimitive("MENUBAR.ENABLED", "pENABLED", 0);
        registerPrimitive("MENUBAR.ENABLEPATH", "pENABLEPATH", 1);
        registerPrimitive("MENUBAR.DISABLEPATH", "pDISABLEPATH", 1);
        registerPrimitive("MENUBAR.PATHENABLED", "pPATHENABLED", 1);

        registerPrimitive("MENUBAR.SETBGCOLOR", "pSETBGCOLOR", 1);
        registerPrimitive("MENUBAR.SETFGCOLOR", "pSETFGCOLOR", 1);
        registerPrimitive("MENUBAR.FGCOLOR", "pFGCOLOR", 0);
        registerPrimitive("MENUBAR.BGCOLOR", "pBGCOLOR", 0);

        myMachine = (MyMachine) machine;

        if (console != null)
            console.putSetupMessage("Loaded MenuBar primitives");

    }

    public final LogoObject pADDITEM(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testMaxParams(aLogoObject, 3);
        testMinParams(aLogoObject, 1);

        int position = 0;
        String menu;
        String[] menuPath = null;

        if (aLogoObject.length == 3) {
            menu = aLogoObject[0].toString();
            int length = aLogoObject[1].length();

            //         System.out.println("length: " + length);
            menuPath = new String[length];
            for (int i = 0; i < length; i++) {
                //               System.out.println(i + ": " + aLogoObject[1].pick(i+1).toString());
                menuPath[i] = aLogoObject[1].pick(i + 1).toString();
            }
            position = aLogoObject[2].toInteger();
        } else if (aLogoObject.length == 2) {
            menu = aLogoObject[0].toString();
            position = aLogoObject[1].toInteger();
        } else
            menu = aLogoObject[0].toString();

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateMenuBar.ESlateMenuBar.class);

        for (int k = 0; k < v.size(); k++) {
            ESlateMenuBar menuBar = (ESlateMenuBar) v.elementAt(k);

            if (aLogoObject.length == 3) {
                String[] nodesToLastPath = new String[aLogoObject[1].length() + 1];

                nodesToLastPath[0] = menuBar.getESlateHandle().getMenuPanel().getNameLabel().getText();

                for (int i = 1; i < aLogoObject[1].length() + 1; i++) {
                    nodesToLastPath[i] = aLogoObject[1].pick(i).toString();
                }

                String[] nodesToPreviousPath = new String[aLogoObject[1].length()];

                nodesToPreviousPath[0] = menuBar.getESlateHandle().getMenuPanel().getNameLabel().getText();
                for (int i = 1; i < aLogoObject[1].length(); i++) {
                    nodesToPreviousPath[i] = aLogoObject[1].pick(i).toString();
                }

                try {
                    JMenuItem lastNode = menuBar.getLastPathComponent(nodesToLastPath);

                    if (lastNode == null) {
                        throw new LanguageException("No such path found ");
                    } else {
                        if (lastNode instanceof ESlateMenu) {
                            if (position > ((ESlateMenu) lastNode).menuEntries.size())
                                throw new LanguageException("Invalid position");
                            //((ESlateMenu) lastNode).addItem(new ESlateMenuItem(menu,menuBar));
                            else
                                ((ESlateMenu) lastNode).insert(new ESlateMenuItem(menu, menuBar), position - 1);
                        } else {
                            if (position == 1) {
                                //System.out.println("Trying to add to a MenuItem, ignoring position parameter");
                                int previousPosition = 0;
                                String text = lastNode.getText();
                                ESlateMenu previousNode = (ESlateMenu) menuBar.getLastPathComponent(nodesToPreviousPath);

                                for (int i = 0; i < previousNode.menuEntries.size(); i++) {
                                    if (((JMenuItem) previousNode.menuEntries.get(i)).getText().equals(text)) {
                                        previousPosition = i;
                                        break;
                                    }
                                }
                                previousNode.remove(lastNode);
                                ESlateMenu newMenu = new ESlateMenu(text, menuBar);

                                newMenu.insert(new ESlateMenuItem(menu, menuBar), position - 1);
                                previousNode.insert(newMenu, previousPosition);
                            }
                        }

                    }
                } catch (Throwable thr) {
                    thr.printStackTrace();
                }
            }

            if (aLogoObject.length == 2 && position <= menuBar.getMenuCount() && position > 0)
                menuBar.add(new ESlateMenu(menu, menuBar), position - 1);
            else if (aLogoObject.length == 2 && position > menuBar.getMenuCount())
                menuBar.add(new ESlateMenu(menu, menuBar));
            if (aLogoObject.length == 1)
                menuBar.add(new ESlateMenu(menu, menuBar));

            menuBar.revalidate();
            menuBar.repaint();
        }

        return LogoVoid.obj;
    }

    public final LogoObject pDELETEITEM(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateMenuBar.ESlateMenuBar.class);

        for (int k = 0; k < v.size(); k++) {
            ESlateMenuBar menuBar = (ESlateMenuBar) v.elementAt(k);

            if (aLogoObject[0].length() == 0)
                throw new LanguageException("Invalid path");

            String[] nodesToLastPath = new String[aLogoObject[0].length() + 1];

            nodesToLastPath[0] = menuBar.getESlateHandle().getMenuPanel().getNameLabel().getText();

            for (int i = 1; i < aLogoObject[0].length() + 1; i++) {
                nodesToLastPath[i] = aLogoObject[0].pick(i).toString();
            }

            String[] nodesToPreviousPath = new String[aLogoObject[0].length()];

            nodesToPreviousPath[0] = menuBar.getESlateHandle().getMenuPanel().getNameLabel().getText();
            for (int i = 1; i < aLogoObject[0].length(); i++) {
                nodesToPreviousPath[i] = aLogoObject[0].pick(i).toString();
            }
            String[] nodesToPreviousPreviousPath = null;

            if (nodesToPreviousPath.length > 1) {
                nodesToPreviousPreviousPath = new String[aLogoObject[0].length() - 1];
                nodesToPreviousPreviousPath[0] = menuBar.getESlateHandle().getMenuPanel().getNameLabel().getText();
                for (int i = 1; i < aLogoObject[0].length() - 1; i++) {
                    nodesToPreviousPreviousPath[i] = aLogoObject[0].pick(i).toString();
                }
            }

            JMenuItem lastNode = menuBar.getLastPathComponent(nodesToLastPath);

            //try{
            if (lastNode == null) {
                throw new LanguageException("Invalid path. No deletion occured! ");
            } else {
                if (nodesToPreviousPath.length == 1) {
                    menuBar.remove(lastNode);
                } else {
                    ESlateMenu previousNode = (ESlateMenu) menuBar.getLastPathComponent(nodesToPreviousPath);

                    previousNode.remove(lastNode);
                    if (previousNode.getMenuComponentCount() == 0 && nodesToPreviousPreviousPath.length != 1 && nodesToPreviousPath.length != 1 && nodesToPreviousPreviousPath != null) {
                        String previousNodeText = previousNode.getText();
                        ESlateMenu previousPreviousNode = (ESlateMenu) menuBar.getLastPathComponent(nodesToPreviousPreviousPath);

                        previousPreviousNode.remove(previousNode);
                        previousPreviousNode.add(new ESlateMenuItem(previousNodeText, menuBar));
                    }
                }
            }
            menuBar.revalidate();
            menuBar.repaint();
            //}catch (Throwable  thr) {thr.printStackTrace();}

        }
        return LogoVoid.obj;
    }

    public final LogoObject pITEMTEXT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 2);

        int position = aLogoObject[1].toInteger();
        String text = "";

        ESlateMenuBar menuBar = (ESlateMenuBar) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateMenuBar.ESlateMenuBar.class);

        String[] nodesToLastPath = new String[aLogoObject[0].length() + 1];

        nodesToLastPath[0] = menuBar.getESlateHandle().getMenuPanel().getNameLabel().getText();

        for (int i = 1; i < aLogoObject[0].length() + 1; i++) {
            nodesToLastPath[i] = aLogoObject[0].pick(i).toString();
        }

        if (nodesToLastPath.length == 1)
            text = menuBar.getMenu(position - 1).getText();
        else {
            JMenuItem lastNode = menuBar.getLastPathComponent(nodesToLastPath);

            try {
                if (lastNode == null) {
                    throw new LanguageException("No such path found. ");
                } else {
                    if (lastNode instanceof JMenu)
                        text = ((JMenu) lastNode).getItem(position - 1).getText();
                    else
                        throw new LanguageException("No children at this Node");
                }
            } catch (Throwable  thr) {
                thr.printStackTrace();
            }
        }
        return new LogoWord(text);
    }

    public final LogoObject pSETITEMTEXT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 2);

        String text = "";

        text = aLogoObject[0].toString();

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateMenuBar.ESlateMenuBar.class);

        for (int k = 0; k < v.size(); k++) {
            ESlateMenuBar menuBar = (ESlateMenuBar) v.elementAt(k);
            String[] nodesToLastPath = new String[aLogoObject[1].length() + 1];

            nodesToLastPath[0] = menuBar.getESlateHandle().getMenuPanel().getNameLabel().getText();

            for (int i = 1; i < aLogoObject[1].length() + 1; i++) {
                nodesToLastPath[i] = aLogoObject[1].pick(i).toString();
            }

            JMenuItem lastNode = menuBar.getLastPathComponent(nodesToLastPath);

            try {
                if (lastNode == null) {
                    throw new LanguageException("No such path found. ");
                } else {
                    if (nodesToLastPath.length == 1)
                        menuBar.setName(text);
                    else
                        lastNode.setText(text);
                }
            } catch (Throwable  thr) {
                thr.printStackTrace();
            }
            // menuBar.repaint();
        }

        return LogoVoid.obj;
    }

    public final LogoObject pITEMCOUNT(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        int childNum = 0;
        ESlateMenuBar menuBar = (ESlateMenuBar) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateMenuBar.ESlateMenuBar.class);

        String[] nodesToLastPath = new String[aLogoObject[0].length() + 1];

        nodesToLastPath[0] = menuBar.getESlateHandle().getMenuPanel().getNameLabel().getText();

        for (int i = 1; i < aLogoObject[0].length() + 1; i++) {
            nodesToLastPath[i] = aLogoObject[0].pick(i).toString();
        }

        if (nodesToLastPath.length == 1)
            childNum = menuBar.getMenuCount();
        else {
            JMenuItem lastNode = menuBar.getLastPathComponent(nodesToLastPath);

            if (lastNode == null)
                throw new LanguageException("No such path found. ");
            else {
                if (lastNode instanceof ESlateMenu)
                    childNum = ((JMenu) lastNode).getItemCount() - 2;
                else
                    childNum = 0;
            }
        }

        return new LogoWord(childNum);
    }

    public final LogoObject pENABLE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateMenuBar.ESlateMenuBar.class);

        for (int k = 0; k < v.size(); k++) {
            ESlateMenuBar menuBar = (ESlateMenuBar) v.elementAt(k);

            menuBar.setEnabled(true);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pDISABLE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateMenuBar.ESlateMenuBar.class);

        for (int k = 0; k < v.size(); k++) {
            ESlateMenuBar menuBar = (ESlateMenuBar) v.elementAt(k);

            menuBar.setEnabled(false);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pENABLED(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateMenuBar menuBar = (ESlateMenuBar) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateMenuBar.ESlateMenuBar.class);

        return new LogoWord(menuBar.isEnabled());
    }

    public final LogoObject pENABLEPATH(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateMenuBar.ESlateMenuBar.class);

        for (int k = 0; k < v.size(); k++) {
            ESlateMenuBar menuBar = (ESlateMenuBar) v.elementAt(k);

            if (aLogoObject[0].length() == 0)
                throw new LanguageException("Invalid path");

            String[] nodesToLastPath = new String[aLogoObject[0].length() + 1];

            nodesToLastPath[0] = menuBar.getESlateHandle().getMenuPanel().getNameLabel().getText();

            for (int i = 1; i < aLogoObject[0].length() + 1; i++) {
                nodesToLastPath[i] = aLogoObject[0].pick(i).toString();
            }

            JMenuItem lastNode = menuBar.getLastPathComponent(nodesToLastPath);

            // try{
            if (lastNode == null) {
                throw new LanguageException("Invalid path");
            } else {
                if (nodesToLastPath.length == 1)
                    menuBar.setEnabled(true);
                else
                    lastNode.setEnabled(true);
            }
            //  }catch (Throwable  thr) {thr.printStackTrace();}

        }
        return LogoVoid.obj;
    }

    public final LogoObject pDISABLEPATH(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateMenuBar.ESlateMenuBar.class);

        for (int k = 0; k < v.size(); k++) {
            ESlateMenuBar menuBar = (ESlateMenuBar) v.elementAt(k);

            if (aLogoObject[0].length() == 0)
                throw new LanguageException("Invalid path");

            String[] nodesToLastPath = new String[aLogoObject[0].length() + 1];

            nodesToLastPath[0] = menuBar.getESlateHandle().getMenuPanel().getNameLabel().getText();

            for (int i = 1; i < aLogoObject[0].length() + 1; i++) {
                nodesToLastPath[i] = aLogoObject[0].pick(i).toString();
            }
            //System.out.println("nodesToLastPath " + nodesToLastPath);

            JMenuItem lastNode = menuBar.getLastPathComponent(nodesToLastPath);

            //System.out.println("lastNode " + lastNode);
            //try{
            if (lastNode == null) {
                throw new LanguageException("Invalid path");
            } else {
                if (nodesToLastPath.length == 1)
                    menuBar.setEnabled(false);
                else
                    lastNode.setEnabled(false);
            }
            //}catch (Throwable  thr) {thr.printStackTrace();}
        }
        return LogoVoid.obj;
    }

    public final LogoObject pPATHENABLED(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        ESlateMenuBar menuBar = (ESlateMenuBar) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateMenuBar.ESlateMenuBar.class);

        String[] nodesToLastPath = new String[aLogoObject[0].length() + 1];

        nodesToLastPath[0] = menuBar.getESlateHandle().getMenuPanel().getNameLabel().getText();

        for (int i = 1; i < aLogoObject[0].length() + 1; i++) {
            nodesToLastPath[i] = aLogoObject[0].pick(i).toString();
        }

        JMenuItem lastNode = menuBar.getLastPathComponent(nodesToLastPath);

        if (lastNode == null) {
            throw new LanguageException("Invalid path");
        } else {
            if (nodesToLastPath.length == 1)
                return new LogoWord(menuBar.isEnabled());
            else
                return new LogoWord(lastNode.isEnabled());
        }

        //return new LogoWord(lastNode.isEnabled());
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

        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateMenuBar.ESlateMenuBar.class);

        for (int k = 0; k < v.size(); k++) {
            ESlateMenuBar menuBar = (ESlateMenuBar) v.elementAt(k);

            menuBar.setBackground(bgColor);
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
        Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.eslateMenuBar.ESlateMenuBar.class);

        for (int k = 0; k < v.size(); k++) {
            ESlateMenuBar menuBar = (ESlateMenuBar) v.elementAt(k);

            menuBar.setForeground(fgColor);
        }

        return LogoVoid.obj;
    }

    public final LogoObject pBGCOLOR(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateMenuBar menuBar = (ESlateMenuBar) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateMenuBar.ESlateMenuBar.class);

        Vector vColor = new Vector();
        LogoWord red = new LogoWord(menuBar.getBackground().getRed());
        LogoWord green = new LogoWord(menuBar.getBackground().getGreen());
        LogoWord blue = new LogoWord(menuBar.getBackground().getBlue());

        vColor.addElement(red);
        vColor.addElement(green);
        vColor.addElement(blue);

        return new LogoList(vColor);
    }

    public final LogoObject pFGCOLOR(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 0);

        ESlateMenuBar menuBar = (ESlateMenuBar) myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.eslateMenuBar.ESlateMenuBar.class);

        try {
            Vector vColor = new Vector();
            LogoWord red = new LogoWord(menuBar.getForeground().getRed());
            LogoWord green = new LogoWord(menuBar.getForeground().getGreen());
            LogoWord blue = new LogoWord(menuBar.getForeground().getBlue());

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
