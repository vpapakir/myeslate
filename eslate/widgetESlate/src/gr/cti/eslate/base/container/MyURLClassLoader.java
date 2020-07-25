package gr.cti.eslate.base.container;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by IntelliJ IDEA.
 * User: yiorgos
 * To change this template use Options | File Templates.
 */
public class MyURLClassLoader extends URLClassLoader {
    public MyURLClassLoader(URL[] urls) {
        super(urls);
    }
}
