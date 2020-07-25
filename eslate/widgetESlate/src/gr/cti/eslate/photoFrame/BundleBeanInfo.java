package gr.cti.eslate.photoFrame;


import java.util.ListResourceBundle;


public class BundleBeanInfo extends ListResourceBundle {
    public Object[][] getContents() {
        return contents;
    }

    static final Object[][] contents = {
            {"background", "Background color"},
            {"backgroundtip", "Shown only if the component is opaque"},
            {"border", "Border"},
            {"bordertip", "Controls the component border"},
            {"opaque", "Opaque"},
            {"opaquetip", "Controls the opaqueness of the component"},
            {"automaticallyFitImage", "Automatically fit image"},
            {"automaticallyFitImagetip", "Shrinks the image if needed to fit in the panel without having to click on it."},
            {"stretchImage", "Automatically stretch image"},
            {"stretchImagetip", "Stretches image so as to take all available space"},
            {"filename", "File name"},
            {"filenametip", "Defines file name containing the image to be loaded"},
            {"nullImage", "\"No image\" image"},
            {"nullImageTip", "Defines image displayed when no actual image is loaded"},
        };
}
