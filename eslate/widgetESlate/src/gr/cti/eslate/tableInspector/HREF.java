package gr.cti.eslate.tableInspector;

import java.net.URL;

class HREF implements java.io.Serializable {
    String label;
    URL url;

    HREF(String label,URL url) {
        this.label=label;
        this.url=url;
    }

    public String toString() {
        return label;
    }

}