package gr.cti.eslate.database.engine;


import javax.swing.ImageIcon;
import javax.swing.Icon;
import java.io.File;
import java.io.FileNotFoundException;
import java.awt.Color;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.awt.Toolkit;
import java.io.ByteArrayOutputStream;

import gr.cti.eslate.utils.RestorableImageIcon;
import gr.cti.eslate.utils.NewRestorableImageIcon;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;

//import Acme.GifEncoder;


/** Class CImageIcon is used to store images in CTables. Basically it
 *  overrides Swing class ImageIcon, adding a "fileName" attribute to
 *  it, which is the full path of the file from which the CImageIcon was
 *  loaded. Also background and foreground color information is stored
 *  for each icon.
 *
 * @version	2.0, May 01
 *
 */

public class CImageIcon implements Serializable, ReferenceToExternalFile {
    /** The version of the storage format of the CImageIcon class
     */
//    public static final String STR_FORMAT_VERSION = "1.1";
    public static final int FORMAT_VERSION = 2;

//    static java.awt.Toolkit toolkit = java.awt.Toolkit.getDefaultToolkit();
    boolean referenceToExternalFile = false;
//    ImageIcon icon;
    NewRestorableImageIcon icon;
    String fileName;
    Color backgroundColor = null;
    int foregroundTransparency = 255;
    Color foregroundColor = null;
    int backgroundTransparency = 255;
    static final long serialVersionUID = 12;

    /** Create a new CImageIcon instance from the specified image (.gif or .jpg) file.
     */
    public CImageIcon(String fileName) throws UnableToCreateImageIconException {
        icon = new NewRestorableImageIcon(fileName);
        File f = new File(fileName);
        if (!f.exists())
            throw new UnableToCreateImageIconException(DBase.resources.getString("CImageIconMsg1") + fileName + DBase.resources.getString("CImageIconMsg2"));
//        System.out.println(getImage() + ", " + f.exists());
        this.fileName = fileName;
    }

    /** Create a new CImageIcon instance from the specified image (.gif or .jpg) file.
     */
    public CImageIcon(String fileName, Boolean referenceToExternalFile) throws UnableToCreateImageIconException {
        icon = new NewRestorableImageIcon(fileName);
        File f = new File(fileName);
        if (!f.exists())
            throw new UnableToCreateImageIconException(DBase.resources.getString("CImageIconMsg1") + fileName + DBase.resources.getString("CImageIconMsg2"));
//        System.out.println(getImage() + ", " + f.exists());
        this.fileName = fileName;
        this.referenceToExternalFile = referenceToExternalFile.booleanValue();
    }

    /** Create a new CImageIcon instance.
     */
    public CImageIcon() {
        icon = new NewRestorableImageIcon();
        fileName = null;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        if (this.fileName == null && (fileName != null && fileName.trim().length()== 0))
            return;
//        System.out.println("Setting filename to: " + fileName);
        this.fileName = fileName;
    }

    public void setForegroundColor(Color color, int transparency) {
        foregroundColor = color;
        foregroundTransparency =  transparency;
    }

    public void setBackgroundColor(Color color, int transparency) {
        backgroundColor = color;
        backgroundTransparency =  transparency;
    }

    public Color getForegroundColor() {
        return foregroundColor;
    }

    public int getForegroundTransparency() {
        return foregroundTransparency;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public int getBackgroundTransparency() {
        return backgroundTransparency;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(FORMAT_VERSION);
        fieldMap.put("Reference to external file", referenceToExternalFile);
        if (!referenceToExternalFile) {
            fieldMap.put("File name", fileName);
            fieldMap.put("Bgr color", backgroundColor);
            fieldMap.put("Bgr transparency", backgroundTransparency);
            fieldMap.put("Fgr color", foregroundColor);
            fieldMap.put("Fgr transparency", foregroundTransparency);
            fieldMap.put("Icon", icon);
        }else{
            fieldMap.put("File name", fileName);
            fieldMap.put("Bgr color", backgroundColor);
            fieldMap.put("Bgr transparency", backgroundTransparency);
            fieldMap.put("Fgr color", foregroundColor);
            fieldMap.put("Fgr transparency", foregroundTransparency);
        }
        out.writeObject(fieldMap);

/*        out.writeObject(new Boolean(referenceToExternalFile));
        if (!referenceToExternalFile) {
//            System.out.println("Writing whole thing...");
            out.writeObject(fileName);
            out.writeObject(backgroundColor);
            out.writeObject(new Integer(backgroundTransparency));
            out.writeObject(foregroundColor);
            out.writeObject(new Integer(foregroundTransparency));
            out.writeObject(icon);
*/
                /* As of CImageIcon's version 1.5 the image is saved as a gif
                 * in the ObjectOutputStream.
                 */
    /*            ByteArrayOutputStream buffer=new ByteArrayOutputStream();
                GifEncoder ge = new GifEncoder(icon.getImage(),buffer);
                ge.encode();
                buffer.close();
                out.writeObject(buffer.toByteArray()); //ByteArrayOutputStream is not serializable: so we get an extra third memory copy of our data :-(
    */
/*        }else{
//            System.out.println("Writing file name only...");
            out.writeObject(fileName);
            out.writeObject(backgroundColor);
            out.writeObject(new Integer(backgroundTransparency));
            out.writeObject(foregroundColor);
            out.writeObject(new Integer(foregroundTransparency));
        }
*/
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        Object firstObj = in.readObject();
        if (!StorageStructure.class.isAssignableFrom(firstObj.getClass())) {
            // Old time readExtermal()
            oldReadObject(in, firstObj);
        }else{
            StorageStructure fieldMap = (StorageStructure) firstObj;
            String dataVersionStr = fieldMap.getDataVersion();
            referenceToExternalFile = fieldMap.get("Reference to external file", false);
            if (!referenceToExternalFile) {
                fileName = fieldMap.get("File name", (String) null);
                backgroundColor = (Color) fieldMap.get("Bgr color", (Color)null);
                backgroundTransparency = fieldMap.get("Bgr transparency", 255);
                foregroundColor = fieldMap.get("Fgr color", (Color)null);
                foregroundTransparency = fieldMap.get("Fgr transparency", 255);
                if (dataVersionStr.equals("1.0")) {
                    System.out.println("Old time RestorableImageIcon. Converting to NewRestorableImageIcon");
                    RestorableImageIcon resIcon = (RestorableImageIcon) fieldMap.get("Icon", (Object)null);
                    if (resIcon != null)
                        icon = new NewRestorableImageIcon(resIcon.getImage());
                    else
                        icon = null;
                }else{
                    icon = (NewRestorableImageIcon) fieldMap.get("Icon", (Object)null);
                }
            }else{
                fileName = fieldMap.get("File name", (String)null);
                backgroundColor = (Color) fieldMap.get("Bgr color", (Color)null);
                backgroundTransparency = fieldMap.get("Bgr transparency", 255);
                foregroundColor = fieldMap.get("Fgr color", (Color)null);
                foregroundTransparency = fieldMap.get("Fgr transparency", 255);
                if (fileName != null) {
                    File file = new File(fileName);
                    if (file.exists())
                        icon = new NewRestorableImageIcon(fileName);
                    else
                        icon = null;
                }else
                    icon = null;
            }
        }
    }

    private void oldReadObject(ObjectInputStream in, Object firstObj) throws IOException, ClassNotFoundException {
        System.out.println("CImageIcon oldReadObject()");
        try{
        referenceToExternalFile = ((Boolean) firstObj).booleanValue(); //in.readObject()).booleanValue();
//        System.out.println("referenceToExternalFile: " + referenceToExternalFile);
        if (!referenceToExternalFile) {
//            System.out.println("Reading whole thing");
/*            CImageIcon icon = (CImageIcon) in.readObject();
            this.setImage(icon.getImage());
            this.fileName = icon.getFileName();
            this.backgroundColor = icon.getBackgroundColor();
            this.backgroundTransparency = icon.getBackgroundTransparency();
            this.foregroundColor = icon.getForegroundColor();
            this.foregroundTransparency = icon.getForegroundTransparency();
            icon = null;
*/
            fileName = (String) in.readObject();
            backgroundColor = (Color) in.readObject();
            backgroundTransparency = ((Integer) in.readObject()).intValue();
            foregroundColor = (Color) in.readObject();
            foregroundTransparency = ((Integer)in.readObject()).intValue();
            /* As of CImageIcon's version 1.5 the image is saved as a gif
             * in the ObjectInputStream.
             */
/*            icon = new ImageIcon(Toolkit.getDefaultToolkit().createImage((byte[])in.readObject()));
*/          ImageIcon iconOldType = (ImageIcon) in.readObject();
            RestorableImageIcon resIcon = new RestorableImageIcon(iconOldType.getImage());
            icon = new NewRestorableImageIcon(resIcon.getImage());
        }else{
//            System.out.println("Reading file name only");
            fileName = (String) in.readObject();
//            System.out.println("Reading file name only1");
            backgroundColor = (Color) in.readObject();
//            System.out.println("Reading file name only2");
            backgroundTransparency = ((Integer) in.readObject()).intValue();
//            System.out.println("Reading file name only3");
            foregroundColor = (Color) in.readObject();
//            System.out.println("Reading file name only4");
            foregroundTransparency = ((Integer)in.readObject()).intValue();
//            System.out.println("Reading file name only5");
            if (fileName != null) {
                File file = new File(fileName);
                if (file.exists())
                    icon = new NewRestorableImageIcon(fileName);
                else
                    icon = null;
            }else
                icon = null;
//            System.out.println("read in icon: " + icon);
//            setImage(toolkit.getImage(fileName));
//            ImageIcon icon = new ImageIcon(fileName);
//            this.setImage(icon.getImage());
//            icon = null;
        }
        }catch (Exception exc) {
            fileName = null;
            icon = null;
            backgroundColor = null;
            foregroundColor = null;
            foregroundTransparency = 255;
            backgroundTransparency = 255;
            referenceToExternalFile = false;
        }
    }

    public Icon getIcon() {
//        System.out.println("getIcon(): Returning icon");
        return icon;
    }

    public java.awt.Image getImage() {
        return icon.getImage();
    }

    public void setImage(java.awt.Image img) {
        icon.setImage(img);
    }

    public boolean isReferenceToExternalFile() {
        return referenceToExternalFile;
    }

    public void setReferenceToExternalFile(Table ctable, AbstractTableField f, boolean isReference) {
        if (ctable.getFieldNames().indexOf(f.getName()) == -1)
            return;
        try{
//            System.out.println(ctable.getField(f.getName()));
            if (ctable.getTableField(f.getName()).indexOf(this) == -1)
                return;
        }catch (InvalidFieldNameException exc) {
            return;
        }
        if (f.containsLinksToExternalData() != isReference)
            return;

        referenceToExternalFile = isReference;
    }

    public Object getReference() {
        return fileName;
    }


    protected void finalize() throws Throwable {
        super.finalize();
        if (icon != null)
            icon.getImage().flush();
    }

}


/** Class CImageIcon is used to store images in CTables. Basically it
 *  overrides Swing class ImageIcon, adding a "fileName" attribute to
 *  it, which is the full path of the file from which the CImageIcon was
 *  loaded.
 */

/*class OldCImageIcon extends ImageIcon {
    String fileName;
    Color backgroundColor = null;
    Color foregroundColor = null;
    int foregroundTransparency = 255;
    int backgroundTransparency = 255;
    static final long serialVersionUID = 12;
*/
    /** Create a new CImageIcon instance from the specified image (.gif or .jpg) file.
     */
/*    public OldCImageIcon(String fileName) throws UnableToCreateImageIconException {
        super(fileName);
        File f = new File(fileName);
        if (!f.exists())
            throw new UnableToCreateImageIconException(DBase.resources.getString("CImageIconMsg1") + fileName + DBase.resources.getString("CImageIconMsg2"));
//        System.out.println(getImage() + ", " + f.exists());
        this.fileName = fileName;
    }
*/
    /** Create a new CImageIcon instance.
     */
/*    public OldCImageIcon() {
        super();
        fileName = "";
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setForegroundColor(Color color) {
        foregroundColor = color;
    }

    public void setBackgroundColor(Color color) {
        backgroundColor = color;
    }

    public Color getForegroundColor() {
        return foregroundColor;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

}
*/