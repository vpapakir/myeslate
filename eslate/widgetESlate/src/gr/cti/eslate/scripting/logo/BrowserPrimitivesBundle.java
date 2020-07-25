package gr.cti.eslate.scripting.logo;


import java.util.ListResourceBundle;


public class BrowserPrimitivesBundle extends ListResourceBundle {
    public Object[][] getContents() {
        return contents;
    }

    static final Object[][] contents = {
            {"BROWSER.LOADURL", "BROWSER.LOADURL"},
            {"BROWSER.GETURL", "BROWSER.GETURL"},
            {"BROWSER.GOHOME", "BROWSER.GOHOME"},
            {"BROWSER.REFRESH", "BROWSER.REFRESH"},
            {"BROWSER.GOBACK", "BROWSER.GOBACK"},
            {"BROWSER.GOFORWARD", "BROWSER.GOFORWARD"},

            {"BROWSER.STOP", "BROWSER.STOP"},

            {"BROWSER.PRINTPAGE", "BROWSER.PRINTPAGE"},

        };
}
