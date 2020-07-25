package gr.cti.eslate.base.container;

/**
 * Created by IntelliJ IDEA.
 * User: yiorgos
 * To change this template use Options | File Templates.
 */
public class ApplicationRunner {
    public static void main(String[] args) {
/*        SimpleClassLoader loader = SimpleClassLoader.getListenerLoader();
        Thread.currentThread().setContextClassLoader(loader);
        try{
            loader.loadClass("gr.cti.eslate.base.container.ESlateContainerFrame");
        }catch (Throwable thr) {
            thr.printStackTrace();
        }
*/
        ESlateContainerFrame fr = new ESlateContainerFrame(args, null, 1);
        fr.show();
    }
}
