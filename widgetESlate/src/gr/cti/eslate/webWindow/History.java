package gr.cti.eslate.webWindow;


import java.util.Vector;


public class History {

    Vector<String> urls;
    int ptr;

    public History() {
        urls = new Vector<String>();
        ptr = -1;
    }

    public void add(String s) {
        //System.out.println("something added"); 
        if (ptr == urls.size() - 1) {
            urls.addElement(s);
            ptr = urls.size() - 1;
        } else {
            ptr++;
            urls.insertElementAt(s, ptr);
            for (int i = urls.size() - 1; i > ptr; i--)
                urls.removeElementAt(i);

        }
    }

    public String back() {
        if (ptr > 0 && ptr - 1 < urls.size()) {
            ptr--;
            String s = (String) urls.elementAt(ptr);

            return s;
        } else return null;
    }

    public boolean canMoveBack() {
        return ptr > 0;
    }

    public boolean canMoveForward() {
        return ptr + 1 < urls.size();
    }

    public void clear() {
        urls.removeAllElements();
    }

    public String forward() {
        if (ptr + 1 < urls.size()) {
            ptr++;
            String s = (String) urls.elementAt(ptr);

            return s;
        } else return null;

    }

    public int getURLPointer() {
        return ptr;
    }

    public void setURLPointer(int i) {
        ptr = i;
    }

    public Vector getURLS() {
        return urls;
    }

    public String getCurrentURL() {
        return (String) urls.elementAt(ptr);
    }  

    public void setURLS(Vector v) {
        urls = (Vector<String>) v.clone();
    }

}
