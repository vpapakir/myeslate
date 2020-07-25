package gr.cti.eslate.base.container;

import gr.cti.eslate.base.ESlateHandle;
import gr.cti.eslate.base.ESlatePart;
import gr.cti.eslate.base.container.event.ActiveComponentChangedEvent;
import gr.cti.eslate.base.container.event.ESlateContainerListener;
import gr.cti.eslate.base.container.event.MwdChangedEvent;
import gr.cti.eslate.base.container.event.MwdHistoryChangedEvent;
import gr.cti.eslate.base.container.internalFrame.ESlateInternalFrame;
import gr.cti.eslate.imageEditor.ImageEditorDialog;
import gr.cti.eslate.utils.ESlateFileDialog;
import gr.cti.structfile.Entry;
import gr.cti.structfile.StructFile;
import gr.cti.structfile.StructInputStream;
import gr.cti.structfile.StructOutputStream;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import com.objectspace.jgl.Array;
import com.zookitec.layout.ComponentEF;
import com.zookitec.layout.ContainerEF;
import com.zookitec.layout.ExplicitConstraints;
import com.zookitec.layout.ExplicitLayout;
import com.zookitec.layout.Expression;
import com.zookitec.layout.GroupEF;
import com.zookitec.layout.MathEF;

//import pv.jfcx.JPVSpin;


public class ESlateContainerUtils {
    ESlateContainer container;
    private static String tmpDirName = ".temp";
    static File tmpDir = null;
	static File scriptDir = null;
    public final static Color titleBorderColor = new Color(119, 40, 104);
    public static final Color color128 = new Color(0, 0, 128);
    private static Vector printJobInfo = null;
    /** The size of the 'OK', 'Cancel' 'Apply' buttons of dialogs in E-Slate.
     */
    public static final Dimension buttonSize = new Dimension(90, 25);
    private ButtonPressedDialogListener buttonPressedDialogListener = null;
    private ButtonReleasedDialogListener buttonReleasedDialogListener = null;
    private ArrayList buttonPressedDialogListeners = new ArrayList();
    private ArrayList buttonReleasedDialogListeners = new ArrayList();


    public ESlateContainerUtils(ESlateContainer cont) {
        container = cont;
        createTmpDir(container.pathToContainerJar);
		createScriptDir();
    }

	public static File getScriptDir() {
		if (scriptDir == null) {
			if (tmpDir == null) createTmpDir(ESlateContainer.pathToContainerJar);
			createScriptDir();
		}
		return scriptDir;
	}

//    protected static String findPathToJikes() {
//        if (ESlateContainer.pathToJikes == null) {
//
//            String containerPath = ESlateContainer.findPathToContainerJar().trim();
//            String fileSeparator = System.getProperty("file.separator");
//  //            System.out.println("1. containerPath: " + containerPath + ", fileSeparator: " + fileSeparator + ", containerPath.endsWith(pathSeparator): " + containerPath.endsWith(fileSeparator));
//            if (containerPath.endsWith(fileSeparator)) {
//  //                System.out.println("Ends with separator: " + containerPath);
//                containerPath = containerPath.substring(0, containerPath.length() - fileSeparator.length());
//  //                System.out.println("2. containerPath: " + containerPath);
//            }
//            File containerDir = new File(containerPath);
//            if (!containerDir.exists() || ! containerDir.isDirectory()) {
//                return null;
//            }
//  //            System.out.println("containerDir: " + containerDir);
//            File jikesDir = null;
//            try{
//                jikesDir = new File(containerDir.getParent());
//                if (jikesDir == null)
//                    return null;
//  //                System.out.println("1. jikesDir: " + jikesDir);
//                jikesDir = new File(jikesDir.getParent());
//                if (jikesDir == null)
//                    return null;
//  //                System.out.println("2. jikesDir: " + jikesDir);
//            }catch (Exception exc) {
//                return null;
//            }
//            jikesDir = new File(jikesDir, "bin");
//  //            System.out.println("3. jikesDir: " + jikesDir);
//            if (!jikesDir.exists() || ! jikesDir.isDirectory()) {
//  //                System.out.println("Cannot find the jikes compiler");
//                return null;
//            }
//            try{
//  //                System.out.println("4. jikesDir: " + jikesDir);
//                return jikesDir.getCanonicalPath();
//            }catch (java.io.IOException exc) {
//                return null;
//            }
//        }else
//            return ESlateContainer.pathToJikes;
//    }

    public static String createUniqueId(int offset) {
        java.util.Date now = new java.util.Date();
        long id = (now.getTime()+(offset*10))/10;
        return Long.toHexString(id);
    }


    public static String getDriveName(String path) {
        int firstDirectorySeparatorIndex = path.indexOf(System.getProperty("file.separator"));
        if (firstDirectorySeparatorIndex == -1)
            return null;
        return path.substring(0, firstDirectorySeparatorIndex);

    }

    public static boolean existAtSameDrive(String path1, String path2) {
        String drive1 = getDriveName(path1);
        String drive2 = getDriveName(path2);
        if (drive1 == null || drive2 == null) return false;
        return drive1.equals(drive2);
    }

    public static boolean existAtSameDrive(File file1, File file2) {
        return existAtSameDrive(file1.getAbsolutePath(), file2.getAbsolutePath());
    }

    /* Returns the depth of the directory the file exists in the file system. */

    public static int getDepth(File file) {
        File dir = new File(file.getParent());
        int depth = 0;
        while (dir.getParent() != null) {
            dir = new File(dir.getParent());
            depth++;
        }
        return depth;
    }


    public static int getDepth(String fileName) {
        File file = new File(fileName);
        return getDepth(file);
    }


    public byte[] readFileBytes(String fileName) {
        try{
            return SimpleClassLoader.getByteArray(fileName);
        }catch (Throwable thr) {
            return null;
        }
    }


    /* Get the contents of the entry 'entryName' in the structured storage file

     * 'mwdFileName'.
     */
    public byte[] getEntryBytesFromStructFile(String mwdFileName, String entryName) {
        try{
            StructFile structFile = new StructFile(mwdFileName, StructFile.OLD);
            Entry iconFileEntry = structFile.findEntry(entryName);
            StructInputStream sis = new StructInputStream(structFile, iconFileEntry);
            ObjectInputStream in = new ObjectInputStream(sis);
            int length = (int)iconFileEntry.getSize();
            byte buff[] = new byte[length];

            int read = 0;
            while (read < length) {
                int r = in.read(buff, read, length-read);
                if (r < 0) {
                    break;
                }
                read += r;
            }
            in.close();
            return buff;
        }catch (Throwable thr) {
            return null;
        }
    }

    /* Moves the file 'fileName' from the file system to the entry 'entryName' in the
     * structured storage file 'mwdFileName'.
     */
    public boolean moveFileIntoStructFile(String mwdFileName, String entryName, String fileName) {
        byte[] buff = readFileBytes(fileName);
        if (buff == null) return false;

        try{
            StructFile structFile = new StructFile(mwdFileName, StructFile.OLD);
            Entry iconFileEntry = structFile.createFile(entryName);
            StructOutputStream sos = new StructOutputStream(structFile, iconFileEntry);
            ObjectOutputStream out = new ObjectOutputStream(sos);
            out.write(buff);
            out.close();
            return true;
        }catch (Throwable thr) {
            return false;
        }
    }


    public static String getFileNameFromPath(String path, boolean trimExtension) {
        if (path == null)
            return null;
        String fileSeparator = System.getProperty("file.separator");
        int index = path.lastIndexOf(fileSeparator);
        if (index == -1)
            index = path.lastIndexOf('/'); // For URLs

        if (index != -1 && (index + 1) < path.length()) {
            if (trimExtension) {
                String str = path.substring(index+1);
                int extIndex = str.indexOf('.');
                if (extIndex != -1)
                    return str.substring(0, extIndex);
                else
                    return str;
            }else
                return path.substring(index+1);
        }
        return null;
    }

    public File getTmpDir() {
        return tmpDir;
    }

    public String getPathToContainerJar() {
        return container.pathToContainerJar;
    }

    /* Creates the tmp directory. The parameter is the absolute path to the directory
     * where the tmp directory will be created. In Java 1.1 the directory will be created
     * inside the specified dir. However since Java 1.3 the notion of the OS temp dir has
     * been introduced into java. Thus under Java 1.3 the E-Slate's tmp dir will not be created
     * under the specified (by the method parameter) directory, but under the OS's temp directory.
     */
    protected static File createTmpDir(String tempDirParentName) {
//        System.out.println("createTmpDir() tempDirParentName: " + tempDirParentName);
//        System.out.println("tmpdir: " + System.getProperty("java.io.tmpdir"));

        File tempDirParent = null;
//        System.out.println("isWindowsMachine(): " + isWindowsMachine());
        if (isWindowsMachine() || isMacOSMachine()) {
            String tempDirParentName2 = System.getProperty("java.io.tmpdir");
            if (tempDirParentName2 != null && tempDirParentName2.trim().length() != 0) {
                File f = new File(tempDirParentName2);
                if (f.exists())
                    tempDirParent = new File(tempDirParentName2);
            }
        }

        if (tempDirParent == null) {
            if (tempDirParentName == null) {
                return null;
            }
            tempDirParent = new File(tempDirParentName);
            if (!tempDirParent.exists()) {
                return null;
            }

            /* The tmp dir is created 2 folders up from where the container.jar file
             * is located, i.e. 2 folders up from in the basic file of the lib installation.
             */
            String dir1 = tempDirParent.getParent();
            if (dir1 == null) {
                return null;
            }
            File dir1File = new File(dir1);
            if (dir1File.getParent() == null) {
                tempDirParent = dir1File;
            }else{
                String dir2 = dir1File.getParent();
                File dir2File = new File(dir2);
                if (dir2File.getParent() == null) {
                    tempDirParent = dir1File;
                }else{
                    String dir3 = dir2File.getParent();
                    tempDirParent = new File(dir3);
                }
            }
        }
        if (tempDirParent == null) {
            return null;
        }
        File tempDir = new File(tempDirParent, tmpDirName);
        if (!tempDir.exists()) {
            if (tempDir.mkdir()) {
				tmpDir = tempDir;
                return tempDir;
            }else {
                return null;
            }
        }else if (tempDir.isDirectory()) {
			tmpDir = tempDir;
            return tempDir;
        }else{
            return null;
        }
    }

	private static File createScriptDir() {
		if (tmpDir == null) return null;
		File scriptDirectory = new File(tmpDir, "scripts");
		if (!scriptDirectory.exists()) {
			if (!scriptDirectory.mkdir()) {
				return null;
			}
		}
		scriptDir = scriptDirectory;
		return scriptDir;
	}

    void deleteTmpDir() {
        if (tmpDir != null)
            removeDirectory(tmpDir);
    }

    /* Checks if the supplied file exists in the E-Slate temporary directory.
     */
    public boolean existsInTmpDir(File file) {
        if (file == null || tmpDir == null || !file.exists()) return false;
        File dir = new File(file.getParent());
        String tmpDirAbsPath = tmpDir.getAbsolutePath();
        String dirAbsPath = dir.getAbsolutePath();
        if (System.getProperty("os.name").toLowerCase().indexOf("windows") != -1) {
            tmpDirAbsPath = tmpDirAbsPath.toLowerCase();
            dirAbsPath = dirAbsPath.toLowerCase();
        }
        if (tmpDirAbsPath.equals(dirAbsPath))
            return true;
        return false;
    }

    public boolean existsInTmpDir(String fileName) {
        if (fileName == null) return false;
        return existsInTmpDir(new File(fileName));
    }

    public static void removeDirectoryContents(File directory) {
//        System.out.println("removeDirectoryContents: " + directory);
        if (directory == null || !directory.isDirectory())
            return;
        String[] files = directory.list();
        for (int i=0; i<files.length; i++) {
            File fileToBeDeleted = new File(directory, files[i]);
            if (fileToBeDeleted.isDirectory())
                removeDirectoryContents(fileToBeDeleted);
            fileToBeDeleted.delete();
        }
    }

    protected void removeDirectory(File directory) {
        removeDirectoryContents(directory);
        directory.delete();
    }

    /** Makes a copy of the file at path 'sourceFile'. The copy is the 'targetFile'.
     *  @return true, if the file copy was successful, false otherwise.
     */
    public static boolean copyFile(String sourceFile, File targetFile) {
        try{
            BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(sourceFile), 32768);
            BufferedOutputStream outStream = new BufferedOutputStream(new FileOutputStream(targetFile), 32768);
            int bytesRead = 0, currPos = 0;
            byte[] bytes = new byte[32768];
            while (true) {
                bytesRead = inStream.read(bytes, currPos, bytes.length);
                if (bytesRead == -1) break;
                outStream.write(bytes, 0, bytesRead);
            }
            inStream.close();
            outStream.close();
            return true;
//System.out.println("Time to copy file: " + (System.currentTimeMillis()-time));
        }catch (Throwable thr) {
            System.out.println("Unable to create back-up copy of the current microworld file.");
        }
        return false;
    }

    public static boolean isWindowsMachine() {
        if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
            return true;
        }
        return false;
    }

    public static boolean isMacOSMachine() {
        if (System.getProperty("os.name").toLowerCase().startsWith("mac os")) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether a class implements all the methods defined in an interface,
     * even if the class does not declare that it implements this interface.
     * @param	cl	The class to check.
     * @param	iFace	The interface.
     * @return	True if the class implements the method, false otherwise.
     */
    public static boolean implementsInterface(Class cl, Class iFace) {
        if (iFace.isAssignableFrom(cl)) {
            return true;
        }else{
            try {
                Method m[] = iFace.getMethods();
                int n = m.length;
                for (int i=0; i<n; i++) {
                    Method mm = cl.getMethod(m[i].getName(), m[i].getParameterTypes());
                    if (!m[i].getReturnType().equals(mm.getReturnType())) {
                        return false;
                    }
                }
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    /* Accepts an object and if it is an ESlatePart or defines the methods of the
     * ESlatePart i/f, it calls its getESlateHandle() method and returns its ESlateHandle.
     */
    public static ESlateHandle getESlateHandle(Object obj) {
        if (ESlatePart.class.isAssignableFrom(obj.getClass()))
            return ((ESlatePart) obj).getESlateHandle();
        if (implementsInterface(obj.getClass(), ESlatePart.class)) {
            try{
                Method mm = obj.getClass().getMethod("getESlateHandle", new Class[0]);
                if (mm == null) return null;
                return (ESlateHandle) mm.invoke(obj, new Object[0]);
            }catch (Throwable thr) {
                return null;
            }
        }
        return null;
    }

    protected void fireMwdChanged(MwdChangedEvent e) {
        Array cl;
        synchronized(this) {cl = (Array) container.containerListeners.clone();}
        for (int i=0; i<cl.size(); i++)
            ((ESlateContainerListener) cl.at(i)).mwdChanged(e);
    }

    protected void fireMwdHistoryChanged(MwdHistoryChangedEvent e) {
        Array cl;
        synchronized(this) {cl = (Array) container.containerListeners.clone();}
        for (int i=0; i<cl.size(); i++)
            ((ESlateContainerListener) cl.at(i)).mwdHistoryChanged(e);
    }

    protected void fireActiveComponentChanged(ActiveComponentChangedEvent e) {

        Array cl;
        synchronized(this) {cl = (Array) container.containerListeners.clone();}
        for (int i=0; i<cl.size(); i++)
            ((ESlateContainerListener) cl.at(i)).activeComponentChanged(e);
        container.setMicroworldChanged(true);
    }

    protected OK_Cancel_ApplyPanel createOKCancelApplyPanel(Font buttonFont) {
        JButton okButton = new JButton(container.containerBundle.getString("OK"));
        Color color128 = new Color(0, 0, 128);
        okButton.setForeground(color128);
        Dimension buttonSize = new Dimension(90, 25);
        okButton.setMaximumSize(buttonSize);
        okButton.setPreferredSize(buttonSize);
        okButton.setMinimumSize(buttonSize);
        Insets zeroInsets = new Insets(0,0,0,0);
        okButton.setMargin(zeroInsets);

        JButton cancelButton = new JButton(container.containerBundle.getString("Cancel"));
        cancelButton.setForeground(color128);
        cancelButton.setMaximumSize(buttonSize);
        cancelButton.setPreferredSize(buttonSize);
        cancelButton.setMinimumSize(buttonSize);
        cancelButton.setMargin(zeroInsets);

        JButton applyButton = new JButton(container.containerBundle.getString("Apply"));
        applyButton.setForeground(color128);
        applyButton.setMaximumSize(buttonSize);
        applyButton.setPreferredSize(buttonSize);
        applyButton.setMinimumSize(buttonSize);
        applyButton.setMargin(zeroInsets);

        if (buttonFont != null) {
            okButton.setFont(buttonFont);
            cancelButton.setFont(buttonFont);
            applyButton.setFont(buttonFont);
        }

        OK_Cancel_ApplyPanel buttonPanel = new OK_Cancel_ApplyPanel(okButton, cancelButton, applyButton);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        buttonPanel.add(Box.createGlue());
        buttonPanel.add(okButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(cancelButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(applyButton);
        buttonPanel.add(Box.createGlue());

        buttonPanel.setBorder(new EmptyBorder(5,5,5,5));

        return buttonPanel;

    }

    public static void showDetailedErrorDialog(ESlateContainer container, Window dialog, Component centerAroundComp, boolean pack) {
        if (container != null)
            container.playSystemSound(SoundTheme.ERROR_SOUND);
        showDialog(dialog, centerAroundComp, pack);
    }


    public static void showDialog(Window dialog, Component centerAroundComp, boolean pack) {
        if (pack)
            dialog.pack();

        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int x, y;
        if (centerAroundComp == null || !centerAroundComp.isVisible()) {
            x = (screenSize.width/2) - (dialog.getSize().width/2);
            y = (screenSize.height/2) - (dialog.getSize().height/2);
        }else{
	        	/*Rectangle compBounds = centerAroundComp.getBounds();
	            java.awt.Point compLocation = centerAroundComp.getLocationOnScreen();
	    //        System.out.println("dbBounds: " + dbBounds + " location: " + database.getLocationOnScreen());
	            x = compLocation.x + compBounds.width/2 - dialog.getSize().width/2;
	            y = compLocation.y + compBounds.height/2-dialog.getSize().height/2;
	            if (x+dialog.getSize().width > screenSize.width)
	                x = screenSize.width - dialog.getSize().width;
	            if (y+dialog.getSize().height > screenSize.height)
	                y = screenSize.height - dialog.getSize().height;
	            if (x < 0) x = 0;
	            if (y < 0) y = 0;*/
        	x = 256;
        	y = 256;
        }
        dialog.setLocation(x, y);
        dialog.setVisible(true);
    }

    public static void showDialog(Window dialog, Component centerAroundComp, boolean pack, javax.swing.JComponent initialFocusedCompo) {
        if (pack)
            dialog.pack();

        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int x, y;
        if (centerAroundComp == null || !centerAroundComp.isVisible()) {
            x = (screenSize.width/2) - (dialog.getSize().width/2);
            y = (screenSize.height/2) - (dialog.getSize().height/2);
        }else{
            Rectangle compBounds = centerAroundComp.getBounds();
            java.awt.Point compLocation = centerAroundComp.getLocationOnScreen();
    //        System.out.println("dbBounds: " + dbBounds + " location: " + database.getLocationOnScreen());
            x = compLocation.x + compBounds.width/2 - dialog.getSize().width/2;
            y = compLocation.y + compBounds.height/2-dialog.getSize().height/2;
            if (x+dialog.getSize().width > screenSize.width)
                x = screenSize.width - dialog.getSize().width;
            if (y+dialog.getSize().height > screenSize.height)
                y = screenSize.height - dialog.getSize().height;
            if (x < 0) x = 0;
            if (y < 0) y = 0;
        }
        dialog.setLocation(x, y);
        if (initialFocusedCompo != null)
            initialFocusedCompo.requestFocus();

        dialog.setVisible(true);
    }

    public static Color showColorDialog(Component centerAroundComp, String title, Color initialColor) {
        JColorChooser chooser = new JColorChooser(initialColor);
        JDialog colorChooserDialog = JColorChooser.createDialog(centerAroundComp, title, true, chooser, null, null);
        ESlateContainerUtils.showDialog(colorChooserDialog, centerAroundComp, false);
        return chooser.getColor();
    }

    public void snapComponentToGrid(ESlateInternalFrame frame) {
        int gridStep = container.lc.gridStep;
        Point upperFrameCorner = frame.getLocation();
        upperFrameCorner.x = upperFrameCorner.x - (upperFrameCorner.x % gridStep);
        upperFrameCorner.y = upperFrameCorner.y - (upperFrameCorner.y % gridStep);
//        System.out.println("upperFrameCorner: " + upperFrameCorner);

        Dimension frameSize = frame.getSize();
        int w = frameSize.width % gridStep;
        if (w != 0)
            frameSize.width = frameSize.width + (gridStep - (w));

        int h = frameSize.height % gridStep;
        if (h != 0)
            frameSize.height = frameSize.height + (gridStep - (h));
//        System.out.println("framesize: " + frameSize);

        frame.setBounds(upperFrameCorner.x, upperFrameCorner.y,
                        frameSize.width,
                        frameSize.height);
    }

    public void forceMenuClose() {
        javax.swing.MenuSelectionManager.defaultManager().clearSelectedPath();
        Rectangle refreshRect = new Rectangle(0, 0, 400, 400);
        container.paintImmediately(refreshRect);
    }

    /* This method is called after a menu item is selected and before its main action gets
     * executed. This method forces the disappearance of the popups of all the open JMenus,
     * before the main action starts. It does this by calculating the rectangle covered by
     * the open popups and repainting this area of the ESlateContainer. The single argument
     * to this method is the array of open JMenus in the order in which they were opened.
     */
    public void forceMenuClose(JMenu[] menus) {
        if (menus == null || menus.length == 0) return;
        Point origin = menus[0].getLocation();
        Rectangle refreshRect = new Rectangle(origin.x, origin.y, 0, 0);
        int menuHeight = 0;
        for (int i = 0; i<menus.length; i++) {
              menuHeight = menuHeight + menus[i].getLocation().y + menus[i].getBounds().height;
              Rectangle r = menus[i].getPopupMenu().getBounds();
              if (refreshRect.height < menuHeight + r.height)
                  refreshRect.height = menuHeight + r.height;
              refreshRect.width = refreshRect.width + r.width;
//              System.out.println("refreshRect: " + refreshRect);
        }
        javax.swing.MenuSelectionManager.defaultManager().clearSelectedPath();
        container.paintImmediately(refreshRect);
    }

    public void forceMenuClose(Rectangle r) {
        javax.swing.MenuSelectionManager.defaultManager().clearSelectedPath();
        container.paintImmediately(r);
    }

    /* Parses all the component of a java.awt.Container which has a BorderLayout manager
     * and finds the constraints for each component, i.e. which component has the BorderLayout.WEST
     * constraint, which component has the BorderLayout.CENTER constraint... All the components are
     * inserted in a Hashtable which has the component constraints as keys.
     */
    public static Hashtable analyzeBorderLayoutComponents(Container container) {
        Hashtable map = new Hashtable();
        if (!BorderLayout.class.isAssignableFrom(container.getLayout().getClass()))
            return map;
        Rectangle containerBnd = container.getBounds();
        for (int i=0; i<container.getComponentCount(); i++) {
            Component compo = container.getComponent(i);
//            if (JButton.class.isAssignableFrom(compo.getClass()))
//                System.out.println("Checking component: " + ((JButton) compo).getText());
            Rectangle compoBnd = compo.getBounds();
//            System.out.println("containerBnd: " + containerBnd);
//            System.out.println("compoBnd: " + compoBnd);
            if (compoBnd.x == containerBnd.x && compoBnd.width != containerBnd.width) {
                // This is either WEST or CENTER
                container.setBounds(containerBnd.x, containerBnd.y, containerBnd.width+10, containerBnd.height);
                container.validate();
                container.doLayout();
                if (compoBnd.width == compo.getBounds().width) {
                    if (compoBnd.x == containerBnd.x) {
                        map.put(BorderLayout.WEST, compo);
//                        System.out.println("West");
                    }else{
                        map.put(BorderLayout.EAST, compo);
//                        System.out.println("East");
                    }
                }else{
                    map.put(BorderLayout.CENTER, compo);
//                    System.out.println("Center");
                }
                container.setBounds(containerBnd);
                container.validate();
                container.doLayout();
            }else if (compoBnd.y == containerBnd.y && compoBnd.height != containerBnd.height) {
                // This is either NORTH or CENTER
                container.setBounds(containerBnd.x, containerBnd.y, containerBnd.width, containerBnd.height+10);
                container.validate();
                container.doLayout();
                if (compoBnd.height == compo.getBounds().height) {
                    if (compoBnd.y == containerBnd.y) {
                        map.put(BorderLayout.NORTH, compo);
//                        System.out.println("North");
                    }else{
                        map.put(BorderLayout.SOUTH, compo);
//                        System.out.println("South");
                    }
                }else{
                    map.put(BorderLayout.CENTER, compo);
//                    System.out.println("Center");
                }
                container.setBounds(containerBnd);
                container.validate();
                container.doLayout();
            } else if (compoBnd.x+compoBnd.width == containerBnd.x+containerBnd.width && compoBnd.width != containerBnd.width) {
                // This is either EAST or CENTER
                container.setBounds(containerBnd.x, containerBnd.y, containerBnd.width+10, containerBnd.height);
                container.validate();
                container.doLayout();
                if (compoBnd.width == compo.getBounds().width) {
                    if (compoBnd.x+compoBnd.width == containerBnd.x+containerBnd.width) {
                        map.put(BorderLayout.EAST, compo);
//                        System.out.println("East");
                    }else{
                        map.put(BorderLayout.WEST, compo);
//                        System.out.println("West");
                    }
                }else{
                    map.put(BorderLayout.CENTER, compo);
//                    System.out.println("Center");
                }
                container.setBounds(containerBnd);
                container.validate();
                container.doLayout();
            }else if (compoBnd.y+compoBnd.height == containerBnd.y+containerBnd.height &&
                compoBnd.height != containerBnd.height) {
                // This is either SOUTH or CENTER
                container.setBounds(containerBnd.x, containerBnd.y, containerBnd.width, containerBnd.height+10);
                container.validate();
                container.doLayout();
                if (compoBnd.height == compo.getBounds().height) {
                    if (compoBnd.y+compoBnd.height == containerBnd.y+containerBnd.height) {
                        map.put(BorderLayout.SOUTH, compo);
//                        System.out.println("South");
                    }else{
                        map.put(BorderLayout.NORTH, compo);
//                        System.out.println("North");
                    }
                }else{
                    map.put(BorderLayout.CENTER, compo);
//                    System.out.println("Center");
                }
                container.setBounds(containerBnd);
                container.validate();
                container.doLayout();
            }
        }
        /* In some cases, when the number of the components in the container is 5, the component
         * in the BorderLayout.CENTER does not get inserted in the Hashtable, by the above algorithm.
         * We generalize this case to the following: when the component number is 5, but the algorithm
         * has positioned only 4 of the components, then set 5th component's position to the remaining
         * one.
         */
        if (map.size() == 4 && container.getComponentCount() == 5) {
            Component leftOut = null;
            String constraint = null;
            String[] con = new String[5];
            con[0] = BorderLayout.WEST;
            con[1] = BorderLayout.EAST;
            con[2] = BorderLayout.CENTER;
            con[3] = BorderLayout.NORTH;
            con[4] = BorderLayout.SOUTH;
            for (int i=0; i<container.getComponentCount(); i++) {
                if (!map.contains(container.getComponent(i))) {
                    leftOut = container.getComponent(i);
                    break;
                }
            }
            for (int i=0; i<con.length; i++) {
                if (!map.containsKey(con[i])) {
                    constraint = con[i];
                    break;
                }
            }
            if (constraint != null && leftOut != null)
                map.put(constraint, leftOut);
        }
        /* If the method did not achieve to specify the BorderLayout constraint for every component
         * of the container, then return nothing. Only total success is accepted.
         */
        if (container.getComponentCount() != map.size())
            return null;
        return map;
    }

/*    public static Image getBeanIcon(Class beanClass, int iconKind) {
        return BeanInfoFactory.getBeanIcon(beanClass, iconKind);
        java.beans.BeanInfo beanInfo = BeanInfoFactory.getBeanImfo(beanClass);
        if (beanInfo == null) return null;
        Image img = beanInfo.getIcon(iconKind);
        if (img == null) return null;
        ImageIcon beanIcon = new ImageIcon(img);
        return beanIcon;
    }

    public static Image getBeanIcon(String beanClassName, int iconKind) {
        try{
            Class beanClass = Class.forName(beanClassName);
            return getBeanIcon(beanClass, iconKind);
        }catch (Throwable exc) {
//            exc.printStackTrace();
//            System.out.println(exc.getClass() + ", " + exc.getMessage());
            return null;
        }
    }
*/

    public static boolean deleteFile(String fileName) {
        if (fileName == null) return false;
//        System.out.println("ESlateContainerUtils deleting file: " + fileName);

        File f = new File(fileName);
        if (f.exists())
            return f.delete();
//        System.out.println("deleteFile --> File " + f + " does not exist.");
        return false;
    }

    public static int getFieldMapContentLength(gr.cti.eslate.utils.ESlateFieldMap fieldMap) {
        try{
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream(100);
        java.io.BufferedOutputStream bos = new java.io.BufferedOutputStream(baos);
        java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(bos);
        oos.writeObject(fieldMap);
        oos.flush();
        byte[] ba = baos.toByteArray();
        oos.close();
        return ba.length;
        }catch (Throwable thr) {return -1;}
    }

    public static Vector getPrJob() {
        return printJobInfo;
    }

    /* This method creates a PrintJob and fills a Vector with:
     * - the PrintJob
     * - the JobAttributes of the PrintJob
     * - the PageAttributes of the PrintJob
     * - the ESlateContainer's menuPanel.
     * All this information is stored in a static variable in order to be acquired
     * by the Acrobat reader java software, which actually does the printing and
     * needs a valid PrintJob with its attributes and the 'menuPanel' in order to
     * update the number of the page being printed.
     */
    public boolean createPrJob() {
        java.awt.Toolkit toolkit = java.awt.Toolkit.getDefaultToolkit();
        java.awt.PageAttributes pageAttributes = new java.awt.PageAttributes();
        java.awt.JobAttributes jobAttributes = new java.awt.JobAttributes();
//        jobAttributes.setDialog(java.awt.JobAttributes.DialogType.COMMON);
        java.awt.PrintJob job = toolkit.getPrintJob(container.parentFrame,
                    "Print Dialog?",
                    jobAttributes,
                    pageAttributes);

        if (job == null) {
            return false;
        }
        printJobInfo = new java.util.Vector();
        printJobInfo.addElement(job);
        printJobInfo.addElement(jobAttributes);
        printJobInfo.addElement(pageAttributes);
        if (container instanceof ESlateComposer)
            printJobInfo.addElement(((ESlateComposer) container).menuPanel);
        return true;
    }

    public void finalizePrJob() {
        java.awt.PrintJob job = (java.awt.PrintJob) printJobInfo.elementAt(0);
        job.end();
        printJobInfo.clear();
    }

    public static boolean isVisualComponent(Class cls) {
        if (Component.class.isAssignableFrom(cls))
            return true;
        return false;
    }

    void discardDialogButtonListeners(Dialog dialog) {
//        System.out.println("Before discard size: " + buttonPressedDialogListeners.size() + ", dialog: " + dialog.getClass());
        for (int i=buttonPressedDialogListeners.size()-1; i>=0; i--) {
            ButtonPressedDialogListener listener = (ButtonPressedDialogListener) buttonPressedDialogListeners.get(i);
            if (listener.dialog == dialog) {
                listener.button = null;
                listener.dialog = null;
                ButtonReleasedDialogListener listener2 = (ButtonReleasedDialogListener) buttonReleasedDialogListeners.get(i);
                listener2.button = null;
                listener2.dialog = null;
                buttonPressedDialogListeners.remove(i);
                buttonReleasedDialogListeners.remove(i);
            }
        }
//        System.out.println("After discard size: " + buttonPressedDialogListeners.size());
    }

    void attachDialogButtonListener(JDialog dialog, JButton button, int key) {
//        if (buttonPressedDialogListener == null) {
            buttonPressedDialogListener = new ButtonPressedDialogListener(button, dialog);
            buttonReleasedDialogListener = new ButtonReleasedDialogListener(button, dialog);
//        }else{
//            buttonPressedDialogListener.button = null;
//            buttonPressedDialogListener.button = button;
//            buttonReleasedDialogListener.button = null;
//            buttonReleasedDialogListener.button = button;

            buttonPressedDialogListeners.add(buttonPressedDialogListener);
            buttonReleasedDialogListeners.add(buttonReleasedDialogListener);
//        }

        dialog.getRootPane().registerKeyboardAction(buttonPressedDialogListener,
            javax.swing.KeyStroke.getKeyStroke(key, 0, false), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
        dialog.getRootPane().registerKeyboardAction(buttonReleasedDialogListener,
            javax.swing.KeyStroke.getKeyStroke(key, 0, true), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    class ButtonPressedDialogListener implements ActionListener {
        JButton button;
        Dialog dialog;
        public ButtonPressedDialogListener(JButton button, Dialog dialog) {
            this.button = button;
            this.dialog = dialog;
        }
        public void actionPerformed(ActionEvent e) {
            javax.swing.ButtonModel bm = button.getModel();
            bm.setArmed(true);
            bm.setPressed(true);
        }
    }

    class ButtonReleasedDialogListener implements ActionListener {
        JButton button;
        Dialog dialog;
        public ButtonReleasedDialogListener(JButton button, Dialog dialog) {
            this.button = button;
            this.dialog = dialog;
        }
        public void actionPerformed(ActionEvent e) {
            javax.swing.ButtonModel bm = button.getModel();
            bm.setPressed(false);
        }
    }

    /** Selects another entry in the supplied JList, before the selected entry of
     *  the list is removed. The new selected entry will be the next of the entry
     *  to be removed, unless:
     *  * The list is emptied, in which case no entry is selected.
     *  * The last entry of the list is to be removed(but the list in not empty),
     *    in which case the previous entry is selected.
     */
    boolean selectNewBeforeSelectedEntryIsRemoved(JList list) {
        int selectedIndex = list.getSelectedIndex();
        if (selectedIndex == -1) return false;
        int newSelectionIndex = -1;
        if (selectedIndex == list.getModel().getSize()-1) {
            newSelectionIndex = selectedIndex-1;
        }else{
            newSelectionIndex = selectedIndex+1;
        }
        list.setSelectedIndex(newSelectionIndex);
        return true;
    }

    /** Checks the class of the supplied to object to find a method with the name 'methodName'.
     *  If the method is found, it is returned.
     */
    public static Method findMethod(Object obj, String methodName) {
        if (obj == null) return null;
        Method[] methods = obj.getClass().getDeclaredMethods();
        for (int i=0; i<methods.length; i++) {
//System.out.println("methods[i].getName(): " + methods[i].getName() + ", methodName: " + methodName);
            if (methods[i].getName().equals(methodName))
                return methods[i];
        }
        return null;
    }

}

class OK_Cancel_ApplyPanel extends JPanel {
    JButton okButton, cancelButton, applyButton;

    public OK_Cancel_ApplyPanel(JButton okButton, JButton cancelButton, JButton applyButton) {
        super(true);
        if (okButton == null || cancelButton == null || applyButton == null)
            throw new NullPointerException();
        this.okButton = okButton;
        this.cancelButton = cancelButton;
        this.applyButton = applyButton;
    }

    public JButton getOKButton() {
        return okButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public JButton getApplyButton() {
        return applyButton;
    }
}

interface NumericValueListener extends java.util.EventListener {
    public abstract void numericValueChanged(NumericValueChangedEvent e);
}

class NumericValueChangedEvent extends java.util.EventObject {
    private int oldValue;
    private int value;

    public NumericValueChangedEvent(Object source, int value, int oldValue) {
        super(source);
        this.value = value;
        this.oldValue = oldValue;
    }

    public int getValue() {
        return value;
    }

    public int getOldValue() {
        return oldValue;
    }
}


class NumericTextField extends JTextField {
    private int val = 0;
    private int prevVal = 0;
    int minValue = Integer.MIN_VALUE;
    int maxValue = Integer.MAX_VALUE;

    public NumericTextField() {
        super();
    }

    public void setText(String s) {
        try{
            int value = new Integer(s).intValue();
            setText(value);
        }catch (Exception exc) {
            undoSetText();
        }
    }

    public void setText(int k) {
        if (k >= minValue && k <= maxValue) {
            prevVal = val;
            val = k;
//            System.out.println("NumericTextField setText() k: " + k + ", prevVal: " + prevVal + ", val: " + val);
            super.setText(new Integer(k).toString());
        }
    }

    private void undoSetText() {
        setText(val);
    }

/*    public void setValue(int value) {
        if (value != val) {
            prevVal = val;
            val = value;
        }
    }
*/
    public int getValue() {
        return val;
    }

    public int getPreviousValue() {
        return prevVal;
    }

    public void setMaxValue(int value) {
        maxValue = value;
        if (getValue() > maxValue)
            setText(maxValue);
    }

    public void setMinValue(int value) {
        minValue = value;
        if (getValue() < minValue)
            setText(minValue);
    }
}

class ColorChoosePanel extends JPanel implements LabeledPanel {
    public static final int H_GAP = 3;
    public static final Expression H_GAP_EXP = MathEF.constant(H_GAP);
    JLabel label;
    JPanel panel;
    JButton button;
    private Color color = Color.lightGray;
    private boolean localeIsGreek = false;
    private ResourceBundle colorChoosePanelBundle;
    ExplicitLayout el = new ExplicitLayout();

    public ColorChoosePanel(boolean doubleBuffered, String labelText, Color c) {
        super(doubleBuffered);
        color = c;
        colorChoosePanelBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.ColorChoosePanelBundle", Locale.getDefault());
        if (colorChoosePanelBundle.getClass().getName().equals("gr.cti.eslate.base.container.ColorChoosePanelBundle_el_GR"))
            localeIsGreek = true;

        if (labelText == null)
            label = new JLabel(colorChoosePanelBundle.getString("Color"));
        else
            label = new JLabel(labelText);

        panel = new JPanel();

        Dimension d = new Dimension(20, 16);
        button = new JButton(new ImageIcon(getClass().getResource("images/palette.gif")));

//        chooseColor.setFocusPainted(false);
//        chooseIcon.setFocusPainted(false);

        setLayout(el);
        ExplicitConstraints ec1 = new ExplicitConstraints(label);
        ec1.setX(ContainerEF.left(this));
        ec1.setY(ContainerEF.top(this));
        ec1.setHeight(ContainerEF.height(this));
        ec1.setWidth(MathEF.constant(label.getFontMetrics(label.getFont()).stringWidth(label.getText())));
        add(label, ec1);
        ExplicitConstraints ec2 = new ExplicitConstraints(panel);
        ec2.setX(ComponentEF.right(label));
        ec2.setOriginY(ExplicitConstraints.CENTER);
        ec2.setY(ContainerEF.heightFraction(this, 0.5d));
        ec2.setHeight(MathEF.constant(16));
        ec2.setWidth(MathEF.constant(20));
        add(panel, ec2);
        ExplicitConstraints ec3 = new ExplicitConstraints(button);
        ec3.setX(ComponentEF.right(panel).add(H_GAP_EXP));
        ec3.setOriginY(ExplicitConstraints.CENTER);
        ec3.setY(ContainerEF.heightFraction(this, 0.5d));
        ec3.setHeight(MathEF.constant(17));
        ec3.setWidth(MathEF.constant(20));
        add(button, ec3);

        Component[] comps = new Component[] {label, panel, button};
//        Expression[] heightExpr = GroupEF.createExpressions(ComponentEF.preferredHeight(null), comps);
//        el.setPreferredLayoutSize(ec1.getWidth().add(ec2.getWidth()).add(ec3.getWidth()).add(H_GAP_EXP), MathEF.max(heightExpr));
        Expression heightExpr = GroupEF.heightMax(comps);
        el.setPreferredLayoutSize(ec1.getWidth().add(ec2.getWidth()).add(ec3.getWidth()).add(H_GAP_EXP), heightExpr);

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                Color newColor = JColorChooser.showDialog(new JFrame(), colorChoosePanelBundle.getString("ChooseColor"), color);
                Frame topLevelFrame = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, button);
                Color newColor = ESlateContainerUtils.showColorDialog(topLevelFrame, colorChoosePanelBundle.getString("ChooseColor"), color);
                if (newColor != null) {
                    Color oldColor = color;
                    color = newColor;
                    panel.setBackground(color);
                    firePropertyChange("Color", oldColor, color);
                }
            }
        });

        // Initialization
        panel.setBackground(color);
    }

    public JLabel getLabel() {
        return label;
    }

    public ExplicitLayout getExplicitLayout() {
        return el;
    }

    public void setColor(Color clr) {
        if (clr == null) return;
        if (clr.equals(color)) return;
        Color oldColor = color;
        color = clr;
        panel.setBackground(color);
        firePropertyChange("Color", oldColor, color);
    }

    public Color getColor() {
        return color;
    }

    public void setEnabled(boolean enabled) {
        label.setEnabled(enabled);
        panel.setEnabled(enabled);
        button.setEnabled(enabled);
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        super.firePropertyChange(propertyName, oldValue, newValue);
    }
}

class IconChoosePanel extends JPanel implements LabeledPanel {
    public static final int H_GAP = 3;
    public static final Expression H_GAP_EXP = MathEF.constant(H_GAP);
    JLabel label;
    JButton openFile;
    JButton clearIcon;
    JButton editIcon;
//    protected Font greekUIFont = new Font("Helvetica", Font.PLAIN, 12);
    private boolean localeIsGreek = false;
    private ResourceBundle iconChoosePanelBundle;
    Icon icon;
    ESlateFileDialog fileDialog = null;
    ImageEditorDialog imageEditorDialog = null;
    ExplicitLayout el = new ExplicitLayout();


    public IconChoosePanel(boolean doubleBuffered, String labelText, Icon ic) {
        super(doubleBuffered);
        icon = ic;
        iconChoosePanelBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.IconChoosePanelBundle", Locale.getDefault());
        if (iconChoosePanelBundle.getClass().getName().equals("gr.cti.eslate.base.container.IconChoosePanelBundle_el_GR"))
            localeIsGreek = true;

        if (labelText == null)
            label = new JLabel(iconChoosePanelBundle.getString("Icon"));
        else
            label = new JLabel(labelText);
//        if (localeIsGreek)
//            label.setFont(greekUIFont);

        openFile = new JButton(new ImageIcon(getClass().getResource("images/openFile.gif")));
        Dimension buttonSize = new Dimension(20, 17);
        openFile.setMargin(new Insets(0, 0, 0, 0));
        openFile.setToolTipText(iconChoosePanelBundle.getString("OpenIcon"));

        editIcon = new JButton(new ImageIcon(getClass().getResource("images/imageEdit.gif")));
        editIcon.setMargin(new Insets(0, 0, 0, 0));
        editIcon.setToolTipText(iconChoosePanelBundle.getString("EditIcon"));

        clearIcon = new JButton(new ImageIcon(getClass().getResource("images/clear.gif")));
        clearIcon.setMargin(new Insets(0, 0, 0, 0));
        openFile.setToolTipText(iconChoosePanelBundle.getString("ClearIcon"));

//        chooseColor.setFocusPainted(false);
//        chooseIcon.setFocusPainted(false);

        Expression buttonWidth = MathEF.constant(20);
        Expression buttonHeight = MathEF.constant(17);

        setLayout(el);
        ExplicitConstraints ec1 = new ExplicitConstraints(label);
        ec1.setX(ContainerEF.left(this));
        ec1.setY(ContainerEF.top(this));
        ec1.setHeight(ContainerEF.height(this));
        ec1.setWidth(MathEF.constant(label.getFontMetrics(label.getFont()).stringWidth(label.getText())));
        add(label, ec1);
        ExplicitConstraints ec2 = new ExplicitConstraints(openFile);
        ec2.setX(ComponentEF.right(label));
        ec2.setOriginY(ExplicitConstraints.CENTER);
        ec2.setY(ContainerEF.heightFraction(this, 0.5d));
        ec2.setHeight(buttonHeight);
        ec2.setWidth(buttonWidth);
        add(openFile, ec2);
        ExplicitConstraints ec3 = new ExplicitConstraints(clearIcon);
        ec3.setX(ComponentEF.right(openFile).add(H_GAP_EXP));
        ec3.setOriginY(ExplicitConstraints.CENTER);
        ec3.setY(ContainerEF.height(this).divide(2));
        ec3.setHeight(buttonHeight);
        ec3.setWidth(buttonWidth);
        add(clearIcon, ec3);
        ExplicitConstraints ec4 = new ExplicitConstraints(editIcon);
        ec4.setX(ComponentEF.right(clearIcon).add(H_GAP_EXP));
        ec4.setOriginY(ExplicitConstraints.CENTER);
        ec4.setY(ContainerEF.height(this).divide(2));
        ec4.setHeight(buttonHeight);
        ec4.setWidth(buttonWidth);
        add(editIcon, ec4);

        Component[] comps = new Component[] {label, openFile, editIcon, clearIcon};
//        Expression[] heightExpr = GroupEF.createExpressions(ComponentEF.preferredHeight(null), comps);
//        el.setPreferredLayoutSize(ec1.getWidth().add(ec2.getWidth()).add(ec3.getWidth()).add(ec4.getWidth()).add(H_GAP_EXP.multiply(2)) , MathEF.max(heightExpr));
        Expression heightExpr = GroupEF.heightMax(comps);
        el.setPreferredLayoutSize(ec1.getWidth().add(ec2.getWidth()).add(ec3.getWidth()).add(ec4.getWidth()).add(H_GAP_EXP.multiply(2)), heightExpr);
//System.out.println("IconChoosePanel preferredWidth: " + ec1.getWidth().add(ec2.getWidth()).add(ec3.getWidth()).add(ec4.getWidth()).add(H_GAP_EXP.multiply(2)).getValue(el));

        openFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (fileDialog == null) {
                    try{
                        if (Class.forName("gr.cti.eslate.base.container.ESlateContainer") != null) {
                            if (ESlateContainer.iconFileDialog != null)
                                fileDialog = ESlateContainer.iconFileDialog;
                            else{
                                Frame topLevelFrame = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, IconChoosePanel.this);
                                fileDialog = new ESlateFileDialog(topLevelFrame, iconChoosePanelBundle.getString("OpenIcon"), ESlateFileDialog.LOAD);
                                ESlateContainer.iconFileDialog = fileDialog;
                            }
                        }else{
                            Frame topLevelFrame = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, IconChoosePanel.this);
                            fileDialog = new ESlateFileDialog(topLevelFrame, iconChoosePanelBundle.getString("OpenIcon"), ESlateFileDialog.LOAD);
                        }
                    }catch (ClassNotFoundException exc) {
                        fileDialog = new ESlateFileDialog(new JFrame(), iconChoosePanelBundle.getString("OpenIcon"), ESlateFileDialog.LOAD);
                    }
                }

                if (fileDialog.isShowing()) {
                    return;
                }
                fileDialog.setFile("*.gif; *.jpg");
                fileDialog.setMode(ESlateFileDialog.LOAD);
                String[] extensions = new String[2];
                extensions[0] = "gif";
                extensions[1] = "jpg";
                fileDialog.setDefaultExtension(extensions);
                fileDialog.show();

                String iconFileName = fileDialog.getFile();
                if (iconFileName == null) {
                    return;
                }else{
                    String currDirectory = fileDialog.getDirectory();
                    iconFileName = currDirectory+iconFileName;
                    Icon oldIcon = icon;
                    icon = new ImageIcon(iconFileName);
                    clearIcon.setEnabled(true);
                    firePropertyChange("Icon", oldIcon, icon);
                    if (oldIcon != null && ImageIcon.class.isAssignableFrom(oldIcon.getClass()))
                        ((ImageIcon) oldIcon).getImage().flush();
                    oldIcon = null;
                }
            }
        });
        editIcon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                try{
                    if (imageEditorDialog == null) {
                        Frame topLevelFrame = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, IconChoosePanel.this);
                        imageEditorDialog = new ImageEditorDialog(topLevelFrame, 32, 32); //topLevelFrame, 32, 32);
                    }

                    Icon oldIcon = icon;
                    if (icon != null) {
                        /* we have to supply an ImageIcon to the ImageEditor, but we have an Icon.
                         * If the icon can be casted to an ImageIcon, then everything is fine.
                         * Otherwise create the required ImageIcon by drwaing the Icon on an Image
                         * and puting this image in the new ImageIcon.
                         */
/*                        if (!ImageIcon.class.isAssignableFrom(icon.getClass())) {
                            Icon ic = icon;
                            icon = new ImageIcon();
                            Image img1 = editIcon.createImage(ic.getIconWidth(), ic.getIconHeight());
                            ((Icon) ic).paintIcon(null, img1.getGraphics(), 0, 0);
                            ((ImageIcon) icon).setImage(img1);
                        }
*/
                        imageEditorDialog.setIcon(icon); //setImage((ImageIcon) icon);
                    }
//                    if (icon.getForegroundColor() != null) {
//                        iconEditor.setForegroundColor(icon.getForegroundColor(), icon.getForegroundTransparency());
//                    }
//                    if (icon.getBackgroundColor() != null) {
//                        iconEditor.setBackgroundColor(icon.getBackgroundColor(), icon.getBackgroundTransparency());
//                    }
//                    iconEditor.setFileName(icon.getFileName());
                    imageEditorDialog.showDialog(null);
                    if (imageEditorDialog.getReturnCode() == ImageEditorDialog.IMAGE_EDITOR_OK) {
                        icon = imageEditorDialog.getIcon(); // imageEditorDialog.getImage();
                        if (icon != null)
                            clearIcon.setEnabled(true);
                        else
                            clearIcon.setEnabled(false);
                        firePropertyChange("Icon", oldIcon, icon);
                    }
                    if (oldIcon != null && ImageIcon.class.isAssignableFrom(oldIcon.getClass()))
                        ((ImageIcon) oldIcon).getImage().flush();
                    oldIcon = null;
/*                }catch (IconTooBigException exc) {
                    ESlateOptionPane.showMessageDialog(new JFrame(), exc.getMessage(), iconChoosePanelBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                    return;
                }
*/
            }
        });
        clearIcon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Icon oldIcon = icon;
                icon = null;
                clearIcon.setEnabled(false);
                firePropertyChange("Icon", oldIcon, icon);
                if (oldIcon != null && ImageIcon.class.isAssignableFrom(oldIcon.getClass()))
                    ((ImageIcon) oldIcon).getImage().flush();
                oldIcon = null;
            }
        });

        // Initialization
        if (icon == null)
            clearIcon.setEnabled(false);
        try{
            Class imageEditorClass = Class.forName("gr.cti.eslate.imageEditor.ImageEditor");
        }catch (Exception exc) {
            editIcon.setVisible(false);
        }

    }

    public JLabel getLabel() {
        return label;
    }

    public ExplicitLayout getExplicitLayout() {
        return el;
    }

    public void setIcon(Icon newIcon) {
        Icon oldIcon = icon;
        icon = newIcon;
        firePropertyChange("Color", oldIcon, icon);
        if (oldIcon != null && ImageIcon.class.isAssignableFrom(oldIcon.getClass()))
            ((ImageIcon) oldIcon).getImage().flush();
        oldIcon = null;
    }

    public Icon getIcon() {
        return icon;
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        super.firePropertyChange(propertyName, oldValue, newValue);
    }
}



