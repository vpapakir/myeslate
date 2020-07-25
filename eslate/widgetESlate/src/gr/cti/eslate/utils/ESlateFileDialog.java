package gr.cti.eslate.utils;

import java.awt.*;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;

/**
 * A file dialog extending the environment default file dialog. In this dialog one can set default
 * extensions which are automatically appended to the file name written in the dialog, if a file with
 * the given name doesn't exist. Look the following examples to see what happens on loading and on saving.
 * <P>
 * <B>Example 1:</B> Suppose that an instance of this dialog is used to <B>load</B> a file. Suppose that the default
 * extensions given to the dialog are ".doc", ".txt" and ".wri". Suppose also that the user enters
 * the string "test" in the file dialog. If a file named "test" exists, it is returned by the dialog.
 * If not, files "test.doc", "test.txt" and "test.wri" are searched sequentialy. If one is found,
 * it is returned. If none is found and the <code>loadFileMustExist</code> parameter is true, an
 * error dialog pops up. If <code>loadFileMustExist</code> parameter is false, the file name given
 * by the user is returned, ignoring that such a file doesn't exist.
 * <P>
 * <B>Example 2:</B> Suppose that an instance of this dialog is used to <B>save</B> a file. Suppose that the default
 * extensions given to the dialog are ".doc", ".txt" and ".wri". Suppose also that the user enters
 * the string "test" in the file dialog. If a file named "test" exists, the user is asked to overwrite
 * the file. If a file named "test" doesn't exist, files named "test.doc", "test.txt" and "test.wri" are
 * searched sequentialy. If one of them is found, the user is asked to overwrite it. If none is found,
 * the dialog automatically appends the <B>first</B> default extension to the file name. If the user
 * enters any of "test.doc", "test.txt" and "test.wri", the dialog uses it.
 *
 * @author      Giorgos Vasiliou
 * @author      Kriton Kyrimis
 * @version     2.0.8, 20-Jun-2006
 */
public class ESlateFileDialog extends FileDialog
{
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
    /**
     * Creates a file dialog for loading a file. No default extensions exist, so the dialog
     * behaves as a normal file dialog.
     * @param   frame   the owner of the dialog.
     */
    public ESlateFileDialog(Frame frame)
    {
        super(getFrame(frame));
        addExtension = new String[0];
        mustExist = true;
    }
    /**
     * Creates a file dialog for loading a file with the specified title. No default extensions exist,
     * so the dialog behaves as a normal file dialog.
     * @param   frame   the owner of the dialog.
     * @param   title   the title of the dialog.
     */
    public ESlateFileDialog(Frame frame, String title)
    {
        super(getFrame(frame), title);
        addExtension = new String[0];
        mustExist = true;
    }
    /**
     * Creates a file dialog window with the specified title for loading or saving a file.
     * No default extensions exist, so the dialog behaves as a normal file dialog.
     * <P>
     * If the value of mode is LOAD, then the file dialog is finding a file to read.
     * If the value of mode is SAVE, the file dialog is finding a place to write a file.
     * @param   parent  The owner of the dialog.
     * @param   title   The title of the dialog.
     * @param   mode    The mode of the dialog.
     */
    public ESlateFileDialog(Frame parent, String title, int mode)
    {
        super(getFrame(parent), title, mode);
        addExtension = new String[0];
        mustExist = true;
        mes = ResourceBundle.getBundle("gr.cti.eslate.utils.ESlateFileDialogMessagesBundle", Locale.getDefault());
    }

    /**
     * Returns a <code>Frame</code> suitable for use as the dialog owner.
     * @param   frame   A frame to attempt to use as the owner.
     * @return  If <code>frame</code> is not <code>null</code>,
     *          <code>frame</code> is returned, otherwise a new
     *          <code>Frame</code> is returned.
     */
    private static Frame getFrame(Frame frame)
    {
      if (frame != null) {
        return frame;
      }else{
        return new Frame();
      }
    }

    /**
     * Creates a file dialog window with the specified title for loading or saving a file.
     * One default extension exists in the either of ".xyzw" or "xyzw" formats.
     * <P>
     * If the value of mode is LOAD, then the file dialog is finding a file to read.
     * If the value of mode is SAVE, the file dialog is finding a place to write a file.
     * @param   parent  The owner of the dialog.
     * @param   title   The title of the dialog.
     * @param   mode    The mode of the dialog.
     * @param   ext     The default extension.
     */
    public ESlateFileDialog(Frame parent, String title, int mode, String ext)
    {
        this(getFrame(parent), title, mode);
        if(ext != null && ext != "" && !ext.startsWith("."))
            ext = (new String(".")).concat(ext);
        addExtension = new String[1];
        addExtension[0] = ext;
    }
    /**
     * Creates a file dialog window with the specified title for loading or saving a file.
     * Many default extensions exist in the either of ".xyzw" or "xyzw" formats. The order
     * of the extensions in the array is the order used by the dialog when looking for files.
     * <P>
     * If the value of mode is LOAD, then the file dialog is finding a file to read.
     * If the value of mode is SAVE, the file dialog is finding a place to write a file.
     * @param   parent  The owner of the dialog.
     * @param   title   The title of the dialog.
     * @param   mode    The mode of the dialog.
     * @param   ext     An ordered array containing the default extensions.
     */
    public ESlateFileDialog(Frame parent, String title, int mode, String ext[])
    {
        this(getFrame(parent), title, mode);
        for(int i = 0; i < ext.length; i++)
            if(ext[i] != null && ext[i] != "" && !ext[i].startsWith("."))
                ext[i] = (new String(".")).concat(ext[i]);

        addExtension = ext;
    }
    /**
     * Resets the default extensions and sets a new one.
     * @param   ext the default extension.
     */
    public void setDefaultExtension(String ext)
    {
        if(ext != null && ext != "" && !ext.startsWith("."))
            ext = (new String(".")).concat(ext);
        addExtension = new String[1];
        addExtension[0] = ext;
    }
    /**
     * Resets the default extensions and sets a new ordered array of extensions.
     * @param   ext     An ordered array containing the default extensions.
     */
    public void setDefaultExtension(String ext[])
    {
        for(int i = 0; i < ext.length; i++)
            if(ext[i] != null && ext[i] != "" && !ext[i].startsWith("."))
                ext[i] = (new String(".")).concat(ext[i]);

        addExtension = ext;
    }
    /**
     * The default extensions.
     * @return  an ordered array of the default extensions.
     */
    public String[] getDefaultExtension()
    {
        return addExtension;
    }
    /**
     * If the <code>loadFileMustExist</code> parameter is true, the dialog checks
     * if the file given exists. If it is false, the dialog ignores the inexistence of such a file.
     * (See example 1 on top).
     */
    public void setLoadFileMustExist(boolean value)
    {
        mustExist = value;
    }
    /**
     * If the <code>loadFileMustExist</code> parameter is true, the dialog checks
     * if the file given exists. If it is false, the dialog ignores the inexistence of such a file.
     * (See example 1 on top).
     * @return  the <code>loadFileMustExist</code> parameter.
     */
    public boolean getLoadFileMustExist()
    {
        return mustExist;
    }
    /**
     * Show the dialog.
     */
    @SuppressWarnings(value={"deprecation"})
    public void show()
    {
        super.show();
        if(getFile() != null && addExtension.length != 0)
        {
            String filename = getDirectory() + getFile();
            if(getMode() == LOAD)
            {
                //First test if the given name exists. If it exists, use this name, ignoring the default
                //extensions.
                File test = new File(filename);
                boolean found=false;
                if (!test.exists() && mustExist)
                {
                    for (int i=0;i<addExtension.length && !found;i++)
                    {
                        if(new File((filename.concat(addExtension[i]))).exists())
                        {
                            found=true;
                            setFile(getFile().concat(addExtension[i]));
                        }
                    }
                    if(mustExist && !found)
                    {
                        Object conf_no[] = {
                            mes.getString("ok")
                        };
                        JOptionPane pane = new JOptionPane(mes.getString("notExist1") + filename + mes.getString("notExist2"), 0, 0, UIManager.getIcon("OptionPane.errorIcon"), conf_no, mes.getString("ok"));
                        JDialog dialog = pane.createDialog(new JFrame(), getTitle());
                        dialog.setVisible(true);
                        show();
                    }
                }
            } else
            if(getMode() == SAVE && addExtension.length != 0)
            {
                //First test if the given name exists. If it exists, use this name, ignoring the default
                //extensions.
                File test = new File(filename);
                if (!test.exists())
                {
                    //If the given name doesn't exist, add one by one the default extensions
                    //and see if one of the defaults exists. If it does, ask to overwrite. If
                    //none exists, use the first default one.
                    boolean found=false;
                    boolean ignore=false;
                    String testname;
                    for (int i=0;i<addExtension.length && !found && !ignore;i++)
                    {
                        if (filename.toLowerCase().endsWith(addExtension[i])) {
                            testname=filename;
                            found=true; //1
                        } else
                            testname = filename.concat(addExtension[i]);
                        test = new File(testname);
                        if(test.exists())
                        {
                            Object conf_no[] = {
                                mes.getString("yes"), mes.getString("no")
                            };
                            JOptionPane pane = new JOptionPane(mes.getString("exist1") + testname + mes.getString("exist2"), 3, 0, UIManager.getIcon("OptionPane.QuestionIcon"), conf_no, mes.getString("yes"));
                            JDialog dialog = pane.createDialog(new JFrame(), getTitle());
                            dialog.setVisible(true);
                            Object option = pane.getValue();
                            if(option == mes.getString("no") || option == null) {
                                show();
                                ignore=true;
                            } else {
                                //The file already contains the extension. See //1
                                if (!found) {
                                    setFile(getFile().concat(addExtension[i]));
                                    found=true;
                                }
                            }
                        }
                    }
                    if (!ignore && !found)
                        setFile(getFile().concat(addExtension[0]));
                }
            }
        }
    }

    public static void main(String argv[])
    {
        do
            try
            {
                String a[] = {
                    "doc", "txt", "wri"
                };
                ESlateFileDialog ef = new ESlateFileDialog(new Frame(), "\u0394\u03BF\u03BA\u03B9\u03BC\u03AE", 1);
                ef.setDefaultExtension(a);
                ef.setLoadFileMustExist(true);
                ef.show();
                System.out.println(ef.getFile());
                ef.dispose();
            }
            catch(Throwable t)
            {
                System.err.println(t);
            }
        while(true);
    }

    private String addExtension[];
    private boolean mustExist;
    private static ResourceBundle mes;
}
