package gr.cti.eslate.base.container;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;


public class ViewMenu extends JMenu {
    ActionListener al;
    ESlateContainer container;

    public ViewMenu(ESlateContainer container, String text) {
        super(text);
        this.container = container;
        al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Microworld microworld = ViewMenu.this.container.microworld;
                if (microworld == null) return;
                /* This is an action controlled by a microworld setting. When the setting forbits
                 * the action, there is no way the action can be taked by anyone no matter if the microworld
                 * is locked or not.
                 */
                microworld.checkActionPriviledge(microworld.viewActivationAllowed, "viewActivationAllowed");

                String viewName = ((JMenuItem) e.getSource()).getText();
                ViewMenu.this.container.applyView(ViewMenu.this.container.mwdViews.getView(viewName));
            }
        };
    }

    public void addItem(String name) {
        JMenuItem item = new JMenuItem(name);
        item.addActionListener(al);
        add(item);
        int itemCount = getItemCount();
        if (itemCount > 10) return;
        if (itemCount == 10)
            itemCount = 0;
        item.setAccelerator(KeyStroke.getKeyStroke("control " + new Integer(itemCount).toString()));
    }

    public int getItemPosition(String name) {
        for (int i=0; i<getItemCount(); i++) {
            if (getItem(i).getText().equals(name))
                return i;
        }
        return -1;
    }

    public void removeItem(String name) {
        int pos = getItemPosition(name);
        if (pos != -1) {
            remove(pos);
            int itemCount = getItemCount();
            for (int i=0; i<itemCount; i++) {
                int accelerator = i;
                if (accelerator > 10) return;
                if (accelerator == 10)
                    accelerator = 0;
                getItem(accelerator).setAccelerator(KeyStroke.getKeyStroke("control " + new Integer(accelerator).toString()));
            }
        }
    }
}
