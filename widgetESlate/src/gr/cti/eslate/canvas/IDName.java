package gr.cti.eslate.canvas;

import java.util.ResourceBundle;

public class IDName {
    ResourceBundle bundle;
    String key;
    int id;

    public IDName(ResourceBundle bundle, String key, int id) {
        this.bundle = bundle;
        this.key = key;
        this.id = id;
    }

    public void setKey(String k) {
        key = k;
    }

    public String getKey() {
        return key;
    }

    public void setID(int idNum) {
        id = idNum;
    }

    public int getID() {
        return id;
    }

    public ResourceBundle getBundle() {
        return bundle;
    }

    public String getName() {
        return bundle.getString(key+id);
    }
}
