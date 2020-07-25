package gr.cti.eslate.base.container;

import java.awt.Frame;
import java.util.ArrayList;

import javax.swing.JFrame;

import com.objectspace.jgl.Array;

/* This class keeps track of all the JFrames (with script editors) which have been opened
 * and are still open in an ESlateContainer. We can get the editor of any listener, if one is
 * already open. Therefore we avoid having more than 1 open editors for the same event of the
 * same component.
 */
public class ListenerDialogList {
    Array editedListeners = new Array();
    Array listenerEditors = new Array();

    public boolean addListener(String componentName, String methodName, boolean logoScript, JFrame editor) {
        if (getEditedListener(componentName, methodName, logoScript) != null)
            return false;
        EditedListener el = new EditedListener(componentName, methodName, logoScript);
        editedListeners.add(el);
        listenerEditors.add(editor);
        return true;
    }

    public EditedListener getEditedListener(String componentName, String methodName, boolean logoScript) {
        EditedListener el;
        for (int i=0; i<editedListeners.size(); i++) {
            el = (EditedListener) editedListeners.at(i);
            if (el.componentName.equals(componentName) && el.methodName.equals(methodName) && el.logoScript == logoScript)
                return el;
        }
        return null;
    }

    /* Returns the frames of all the listeners of the component weith the given name. */
    public Frame[] getComponentListenerFrames(String componentName, boolean logoScript) {
        ArrayList frames = new ArrayList();
        EditedListener el;
        for (int i=0; i<editedListeners.size(); i++) {
            el = (EditedListener) editedListeners.at(i);
            if (el.componentName.equals(componentName) && el.logoScript == logoScript)
                frames.add(listenerEditors.at(i));
        }
        Frame[] listenerFrames = new Frame[frames.size()];
        for (int i=0; i<frames.size(); i++)
            listenerFrames[i] = (Frame) frames.get(i);
        return listenerFrames;
    }

    private int getEditedListenerIndex(String componentName, String methodName, boolean logoScript) {
        EditedListener el, foundListener = null;
        for (int i=0; i<editedListeners.size(); i++) {
            el = (EditedListener) editedListeners.at(i);
            if (el.componentName.equals(componentName) && el.methodName.equals(methodName) && el.logoScript == logoScript) {
                foundListener = el;
                break;
            }
        }
        if (foundListener == null)
            return -1;
        return editedListeners.indexOf(foundListener);
    }

    public JFrame getEditorForListener(String componentName, String methodName, boolean logoScript) {
        int index = getEditedListenerIndex(componentName, methodName, logoScript);
        if (index == -1)
            return null;
        return (JFrame) listenerEditors.at(index);
    }

    public JFrame getEditorForListener(EditedListener el) {
        int index = editedListeners.indexOf(el);
        if (index == -1)
            return null;
        return (JFrame) listenerEditors.at(index);
    }

    public void removeListener(String componentName, String methodName, boolean logoScript) {
        int index = getEditedListenerIndex(componentName, methodName, logoScript);
        if (index == -1)
            return;
        editedListeners.remove(index);
        listenerEditors.remove(index);
    }

    public void removeListener(Frame editor) {
        int index = listenerEditors.indexOf(editor);
        if (index == -1)
            return;
        editedListeners.remove(index);
        listenerEditors.remove(index);
    }

    public int size() {
        return editedListeners.size();
    }

}


/* Holds all the needed info about a listener which is currently being edited, i.e.
 * its editor window is open.
 */
class EditedListener {
    String componentName;
    String methodName;
    boolean logoScript = false;

    EditedListener(String componentName, String methodName, boolean logoScript) {
        this.componentName = componentName;
        this.methodName = methodName;
        this.logoScript = logoScript;
    }
}
