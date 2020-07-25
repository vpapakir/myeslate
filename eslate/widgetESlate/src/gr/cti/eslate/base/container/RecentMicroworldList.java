package gr.cti.eslate.base.container;

import gr.cti.eslate.base.container.event.MwdChangedEvent;
import gr.cti.eslate.utils.ESlateOptionPane;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Enumeration;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.objectspace.jgl.Array;
import com.objectspace.jgl.SList;
////nikosM end


/* The list of the microworlds that have been opened previously.
 */
public class RecentMicroworldList extends SList {
    /* The maximum number of microworld filenames that are kept to reopen
     */
    private int microworldListCapacity = 10;
    ESlateContainer container;
    int maxReopenMenuWidth = 0;
    int halfScreenSize = (int) ((1f/2f) * java.awt.Toolkit.getDefaultToolkit().getScreenSize().width);

    public RecentMicroworldList(ESlateContainer container) {
        this.container = container;

        halfScreenSize = (int) ((1f/2f) * java.awt.Toolkit.getDefaultToolkit().getScreenSize().width);
        maxReopenMenuWidth = halfScreenSize;
    }

    void addMicroworldReopenPopupMenuListener() {
        MenuPanel menuPanel = (container instanceof ESlateComposer)?((ESlateComposer) container).menuPanel:null;
        if (menuPanel == null) return;
        menuPanel.microworldReopenMenu.getPopupMenu().addPopupMenuListener(new PopupMenuListener() {
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                JPopupMenu popup = (JPopupMenu) e.getSource();
                MyMenu menu = (MyMenu) popup.getInvoker();
                Point origin = menu.getPopupMenuOrigin();
//                System.out.println("origin: " + menu.getPopupMenuOrigin());
                Point p = menu.getLocation();
                javax.swing.SwingUtilities.convertPointToScreen(p, menu.getParent());
                int menuYLoc = p.y;
//                System.out.println("2 location: " + p);
                /* If the origin is to the left of the left edge of the screen, then
                 * adjust the size of the popup, so that the popup will start exactly at
                 * the left edge of the screen. The popup size is adjusted by adjusting the
                 * size of the AbbreviatedCheckBoxMenuItems which are contained in it.
                 */
                if (origin.x < 0 && origin.x + p.x < 0) {
                    adjustMenuItemWidth(p.x);
//                    System.out.println("menuYLoc: " + menuYLoc);
                    menu.setMenuLocation(0, menuYLoc);
                    popup.setPopupSize(new Dimension(20, 20));
                    popup.pack();
                }else{
                    if (maxReopenMenuWidth < halfScreenSize && (p.x + menu.getSize().width) < halfScreenSize)
                        adjustMenuItemWidth(halfScreenSize);
                }
            }
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}
            public void popupMenuCanceled(PopupMenuEvent e) {}
        });
    }

    protected void adjustMenuItemWidth(int newWidth) {
        maxReopenMenuWidth = newWidth;
        Enumeration enumeration = elements();
        AbbreviatedCheckBoxMenuItem item = null;
        while (enumeration.hasMoreElements()) {
            item = (AbbreviatedCheckBoxMenuItem) enumeration.nextElement();
            item.setMaxWidth(maxReopenMenuWidth);
        }
    }

    /* Everytime a microworld is opened the list of the last 10 opened
     * microworlds is updated, with the path to the new microworld.
     */
    protected void updateMicroworldList(String mwdFileName,boolean remote) {
        AbbreviatedCheckBoxMenuItem mi = null;
        /* Check if the list already contains this item. If it does
         * then remove it from the list.
         */
////nikosM
        if (remote)
            mwdFileName=mwdFileName.substring(0,mwdFileName.length()-1);
////nikosM end
        Enumeration enumeration = elements();
        AbbreviatedCheckBoxMenuItem item = null;
        while (enumeration.hasMoreElements()) {
            item = (AbbreviatedCheckBoxMenuItem) enumeration.nextElement();
            if (item.getFullText().equals(mwdFileName)) {
                mi = item;
                remove(item);
                break;
            }
        }

        if (mi == null) {// new item
/////nikosM new
            if (remote)
                mi = createMwdReopenMenuItem(mwdFileName+"*",remote);
            else
                mi = createMwdReopenMenuItem(mwdFileName,remote);
            mi.setRemote(remote);
////nikosM new end
        }

        int size = size();
        if (size <= microworldListCapacity)
            pushFront(mi);
        else{
            popBack();
            pushFront(mi);
        }

        // Update the microworld reopen menu
        MenuPanel menuPanel = (container instanceof ESlateComposer)?((ESlateComposer) container).menuPanel:null;
        if (menuPanel != null)
            menuPanel.microworldReopenMenu.removeAll();
        com.objectspace.jgl.ForwardIterator iter = start();
        item = null;
        int count = 0;
        while (!iter.atEnd()) {
            item = (AbbreviatedCheckBoxMenuItem) iter.get();
            count++;
            if (menuPanel != null)
                //menuPanel.microworldReopenMenu.add(item);
                addToReopenMenu(menuPanel, item);
            if (count == 1)
                item.setSelected(true);
            else
                item.setSelected(false);
            iter.advance();
        }

        // Update the relative section in the registry
        if(!ESlateContainer.EMBEDDED_MODE) {
        	container.updateReopenedMicroworldsSection();
        }
        container.containerUtils.fireMwdChanged(new MwdChangedEvent(this));
    }

    private AbbreviatedCheckBoxMenuItem createMwdReopenMenuItem(String mwdFileName,final boolean isRemote) {
        if (isRemote)
            mwdFileName=mwdFileName.substring(0,mwdFileName.length()-1);
        final AbbreviatedCheckBoxMenuItem mi = new AbbreviatedCheckBoxMenuItem(mwdFileName, maxReopenMenuWidth);
        mi.setRemote(isRemote);
        mi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//System.out.println("action Performed at createMwdReolpenMenuItem at RecentML");
                String fileName = ((AbbreviatedCheckBoxMenuItem) e.getSource()).getFullText();
                javax.swing.MenuSelectionManager.defaultManager().clearSelectedPath();
                container.paintImmediately(container.getVisibleRect());
//////
//nikosM code
                if (isRemote) {
                   String webFile, webServer;
                   int c=fileName.indexOf(container.containerBundle.getString("OnServer"));
////nikosM new
//                   System.out.println(c+" "+fileName);
                   if (c==-1) {
                       System.out.println("Error loading remote microworld. Microworld seems to be local");
                       return;
                   }
////nikosM new end
                   webFile=fileName.substring(0,c);
                   String temp=fileName.substring(c+container.containerBundle.getString("OnServer").length(),fileName.length());
                   System.out.println(temp);
                   webServer=(String) container.webSites.get(temp);
//                   System.out.println("_WebServer ="+webServer);
                   if (!container.webServerMicrosHandle.openRemoteMicroWorld(webServer,webFile,true))
                       ESlateOptionPane.showMessageDialog(null, container.containerBundle.getString("ContainerMsg12"), container.containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                }
                else
////nikosM end of code here
//                System.out.println("Dirty region: " + javax.swing.RepaintManager.currentManager(ESlateContainer.this).getDirtyRegion(ESlateContainer.this));
//                javax.swing.RepaintManager.currentManager(ESlateContainer.this).paintDirtyRegions();
//                scrollPane.repaint();
                  if (!container.loadLocalMicroworld(fileName, true, true)) {
                    /* If loadLocalMicroworld() failed because the user tried to re-load the
                     * already loaded microworld and when asked if he/she really wants to bring
                     * the loaded microworld to its stored state the answer was No, then do not
                     * deselect this menu item. If loadLocalMicroworld() failed for any other
                     * reason, deselect it.
                     */
                      if (container.microworld != null &&
                          container.currentlyOpenMwdFileName != null &&
                          container.currentlyOpenMwdFileName.equals(fileName))
                          ((JCheckBoxMenuItem) e.getSource()).setSelected(true);
                      else
                          ((JCheckBoxMenuItem) e.getSource()).setSelected(false);
                  }
            }
        });
        return mi;
    }


    public int getMicroworldListCapacity() {
        return microworldListCapacity;
    }

    public void setMicroworldListCapacity(int cap) {
        if (cap < 5) {
            System.out.println("The capacity of the microworld list cannot be less that 5");
            return;
        }
        if (cap > 50) {
            System.out.println("The capacity of the microworld list cannot be more than 50");
            return;
        }

        if (microworldListCapacity <= cap)
            microworldListCapacity = cap;
        else{
            int diff = microworldListCapacity - cap;
            for (int i=0; i<diff; i++)
                popBack();
            microworldListCapacity = cap;
        }
    }

    public void setContents(Array contents) {
        clear();
        if (contents.size() != 0) {
//            container.menuPanel.microworldReopenMenu.removeAll();
            File f;
            for (int i=0; i<contents.size(); i++) {
/////
//nikosM code
                boolean remote=false;
                int c;
                if (contents.at(i).toString().endsWith("*")) {
//                   contents.at(i)=contents.at(i).toString().substring(0,contents.at(i).toString().length());
                   remote=true;
                }
                else {
                  f = new File((String) contents.at(i));
                  if (!f.exists())
                      continue;
                }
                JCheckBoxMenuItem mi = createMwdReopenMenuItem((String) contents.at(i),remote);
//                JCheckBoxMenuItem mi = createMwdReopenMenuItem((String) contents.at(i));
////nikosM end
//                container.menuPanel.microworldReopenMenu.add(mi);
                mi.setSelected(false);
                pushBack(mi);
            }
        }
        populateMicroworldReopenMenu();
    }

    void populateMicroworldReopenMenu() {
        MenuPanel menuPanel = (container instanceof ESlateComposer)?((ESlateComposer) container).menuPanel:null;
        if (menuPanel == null) return;
        menuPanel.microworldReopenMenu.removeAll();
        for (int i=0; i<size(); i++)
            //menuPanel.microworldReopenMenu.add((JCheckBoxMenuItem) at(i));
            addToReopenMenu(menuPanel, (JCheckBoxMenuItem) at(i));
    }

    /**
     * Add an item to the microworld reopen menu item, ensuring that no
     * duplicates are entered.
     * @param   mp      The E-Slate composer/container menu panel.
     * @param   item    The item to add.
     */
    private static void addToReopenMenu(MenuPanel mp, JCheckBoxMenuItem item)
    {
      String itemText;
      if (item instanceof AbbreviatedCheckBoxMenuItem) {
        itemText = ((AbbreviatedCheckBoxMenuItem)item).getFullText();
      }else{
        itemText = item.getText();
      }
      if (itemText != null) {
        int nItems = mp.microworldReopenMenu.getItemCount();
        for (int i=nItems-1; i>=0; i--) {
          JMenuItem it = mp.microworldReopenMenu.getItem(i);
          String txt;
          if (it instanceof AbbreviatedCheckBoxMenuItem) {
            txt = ((AbbreviatedCheckBoxMenuItem)it).getFullText();
          }else{
            txt = it.getText();
          }
          if (itemText.equals(txt)) {
            mp.microworldReopenMenu.remove(i);
          }
        }
      }
      mp.microworldReopenMenu.add(item);
    }
}
