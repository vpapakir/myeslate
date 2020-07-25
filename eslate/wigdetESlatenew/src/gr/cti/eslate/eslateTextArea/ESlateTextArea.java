package gr.cti.eslate.eslateTextArea;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.border.*;
import gr.cti.eslate.utils.*;
import java.awt.*;
import java.beans.*;
import java.text.MessageFormat;
import java.util.*;
import gr.cti.eslate.base.*;
import gr.cti.eslate.base.sharedObject.*;
import gr.cti.eslate.sharedObject.*;
import java.io.*;
import java.net.*;
import javax.swing.event.*;
import java.beans.PropertyChangeEvent;
import gr.cti.eslate.base.container.PerformanceManager;
import gr.cti.eslate.base.container.PerformanceTimerGroup;
import gr.cti.eslate.base.container.PerformanceTimer;
import gr.cti.eslate.base.container.event.PerformanceAdapter;
import gr.cti.eslate.base.container.event.PerformanceListener;


public class ESlateTextArea extends JScrollPane implements ESlatePart, Externalizable {

    private StringSO stringSO;
    private StringSO textFileSO;
    private ESlateHandle handle;
    private static final int FORMAT_VERSION = 1; //"1.0";
    static final long serialVersionUID = -6373860485571787092L;
    private static ResourceBundle bundleMessages;
    private final static String version = "2.0.9";

    private int dot;
    private int mark;
    //   protected EventListenerList keyListenerList;
    private NewRestorableImageIcon initialIcon = null;
    File backImageFile;
    File textFile;
    boolean fireOnEnterPress, plugsCreated;
    private String originalName;
    MyTextArea textArea;
    //Vector stringSOPath = null;
    boolean alreadyChanged = false;
    String text = "";
    SharedObjectPlug plug, plug2;
    private Cursor currentCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);

    /**
     * Timer which measures the time required for loading the state of the
     * component.
     */
    PerformanceTimer loadTimer;

    /**
     * Timer which measures the time required for saving the state of the
     * component.
     */
    PerformanceTimer saveTimer;

    /**
     * Timer which measures the time required for the construction of the
     * component.
     */
    PerformanceTimer constructorTimer;

    /**
     * Timer which measures the time required for initializing the E-Slate
     * aspect of the component.
     */
    PerformanceTimer initESlateAspectTimer;

    /**
     * The listener that notifies about changes to the state of the
     * Performance Manager.
     */
    PerformanceListener perfListener = null;

    /**
     * Returns Copyright information.
     * @return	The Copyright information.
     */

    private ESlateInfo getInfo() {
        String[] info = {
                bundleMessages.getString("part"),
                bundleMessages.getString("development"),
                // bundleMessages.getString("funding"),
                bundleMessages.getString("copyright")
            };

        return new ESlateInfo(
                bundleMessages.getString("componentName") + " " +
                bundleMessages.getString("version") + " " + version,
                info);
    }

    /**

     * Constructs a new ESlateTextArea
     */


    public ESlateTextArea() {
        bundleMessages = ResourceBundle.getBundle("gr.cti.eslate.eslateTextArea.BundleMessages", Locale.getDefault());
        attachTimers();
        PerformanceManager pm = PerformanceManager.getPerformanceManager();

        pm.constructionStarted(this);
        pm.init(constructorTimer);
        setOpaque(true);
        setBorder(new NoTopOneLineBevelBorder(0));
        setBackground(new Color(255, 255, 255));
        textArea = new MyTextArea();
        textArea.setOpaque(false);
        textArea.setBorder(null);
        setViewportView(textArea);
        getViewport().setOpaque(false);
        textArea.setLineWrap(true);
        setBorder(new NoTopOneLineBevelBorder(0));

        setPreferredSize(new Dimension(200, 100));
        textArea.setTabSize(4);

        textArea.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    MouseEvent newEvent = new MouseEvent(ESlateTextArea.this, e.getID(), e.getWhen(), e.getModifiers(), e.getX(), e.getY(), e.getClickCount(), e.isPopupTrigger());

                    fireMousePressed(newEvent);
                }

                public void mouseReleased(MouseEvent e) {
                    MouseEvent newEvent = new MouseEvent(ESlateTextArea.this, e.getID(), e.getWhen(), e.getModifiers(), e.getX(), e.getY(), e.getClickCount(), e.isPopupTrigger());

                    fireMouseReleased(newEvent);
                }

                public void mouseClicked(MouseEvent e) {
                    MouseEvent newEvent = new MouseEvent(ESlateTextArea.this, e.getID(), e.getWhen(), e.getModifiers(), e.getX(), e.getY(), e.getClickCount(), e.isPopupTrigger());

                    fireMouseClicked(newEvent);
                }

                public void mouseEntered(MouseEvent e) {
                    MouseEvent newEvent = new MouseEvent(ESlateTextArea.this, e.getID(), e.getWhen(), e.getModifiers(), e.getX(), e.getY(), e.getClickCount(), e.isPopupTrigger());

                    fireMouseEntered(newEvent);
                }

                public void mouseExited(MouseEvent e) {
                    MouseEvent newEvent = new MouseEvent(ESlateTextArea.this, e.getID(), e.getWhen(), e.getModifiers(), e.getX(), e.getY(), e.getClickCount(), e.isPopupTrigger());

                    fireMouseExited(newEvent);
                }

            }
        );

        textArea.addMouseMotionListener(new MouseMotionListener() {
                public void mouseDragged(MouseEvent e) {
                    MouseEvent newEvent = new MouseEvent(ESlateTextArea.this, e.getID(), e.getWhen(), e.getModifiers(), e.getX(), e.getY(), e.getClickCount(), e.isPopupTrigger());

                    fireMouseDragged(newEvent);
                }

                public void mouseMoved(MouseEvent e) {
                    MouseEvent newEvent = new MouseEvent(ESlateTextArea.this, e.getID(), e.getWhen(), e.getModifiers(), e.getX(), e.getY(), e.getClickCount(), e.isPopupTrigger());

                    fireMouseMoved(newEvent);
                }
            }
        );

        textArea.addVetoableChangeListener(new VetoableChangeListener() {
                public void vetoableChange(PropertyChangeEvent e) {
                    PropertyChangeEvent newEvent = new PropertyChangeEvent(ESlateTextArea.this, e.getPropertyName(), e.getOldValue(), e.getNewValue());

                    fireVetoableChange(newEvent);
                }
            }
        );

        textArea.addPropertyChangeListener(new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent e) {
                    PropertyChangeEvent newEvent = new PropertyChangeEvent(ESlateTextArea.this, e.getPropertyName(), e.getOldValue(), e.getNewValue());

                    firePropertyChange(newEvent);
                }
            }
        );

        textArea.addCaretListener(new CaretListener() {
                public void caretUpdate(final CaretEvent e) {
                    CaretEvent newEvent = new CaretEvent(ESlateTextArea.this) {
                    		static final long serialVersionUID = 1L;
                            public int getDot() {
                                return e.getDot();
                            }

                            public int getMark() {
                                return e.getMark();
                            }
                        };

                    fireCaretUpdate(newEvent);
                    //Get the location in the text.
                    dot = e.getDot();
                    mark = e.getMark();
                }
            }
        );
        pm.stop(constructorTimer);
        pm.constructionEnded(this);
        pm.displayTime(constructorTimer, "", "ms");

    }

    /**
     * Returns the component's handle
     */

    public ESlateHandle getESlateHandle() {
        if (handle == null) {
            PerformanceManager pm = PerformanceManager.getPerformanceManager();

            pm.eSlateAspectInitStarted(this);
            pm.init(initESlateAspectTimer);

            handle = ESlate.registerPart(this);
            try {
                handle.setUniqueComponentName(bundleMessages.getString("ESlateTextArea"));
            } catch (RenamingForbiddenException e) {
                e.printStackTrace();
            }
            handle.addPrimitiveGroup("gr.cti.eslate.scripting.logo.TextAreaPrimitives");
            handle.setInfo(getInfo());

            handle.addESlateListener(new ESlateAdapter() {
                    public void handleDisposed(HandleDisposalEvent e) {
                        if (initialIcon != null) {
                            initialIcon.getImage().flush();
                            initialIcon = null;
                        }
                        PerformanceManager pm = PerformanceManager.getPerformanceManager();

                        pm.removePerformanceListener(perfListener);
                        perfListener = null;
                    }
                }
            );

            textArea.getDocument().addDocumentListener(new DocumentListener() {
                    public void changedUpdate(DocumentEvent e) {
                        if (isEnabled() == true && fireOnEnterPress == false && stringSO != null) {
                            stringSO.setString(textArea.getText());
                        }
                    }

                    public void insertUpdate(DocumentEvent e) {
                        if (isEnabled() == true && fireOnEnterPress == false && stringSO != null) {
                            stringSO.setString(textArea.getText());
                            alreadyChanged = true;
                            text = textArea.getText();
                        }

                    }

                    public void removeUpdate(DocumentEvent e) {
                        if (stringSO != null && isEnabled() == true && fireOnEnterPress == false && (!alreadyChanged || (alreadyChanged && text.equals(textArea.getText())))) {
                            stringSO.setString(textArea.getText());
                        } else
                            alreadyChanged = false;

                    }
                }
            );

            textArea.addKeyListener(new KeyListener() {
                    public void keyReleased(KeyEvent e) {
                        KeyEvent newEvent = new KeyEvent(ESlateTextArea.this, e.getID(), e.getWhen(), e.getModifiers(), e.getKeyCode(), e.getKeyChar());

                        fireKeyReleased(newEvent);
                        if (stringSO != null && e.getKeyCode() == KeyEvent.VK_ENTER && fireOnEnterPress == true && isEnabled() == true) {
                            stringSO.setString(textArea.getText());
                        }
                        if (stringSO != null && e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE)
                            stringSO.setString(textArea.getText());

                    }

                    public void keyPressed(KeyEvent e) {
                        KeyEvent newEvent = new KeyEvent(ESlateTextArea.this, e.getID(), e.getWhen(), e.getModifiers(), e.getKeyCode(), e.getKeyChar());

                        fireKeyPressed(newEvent);
                    }

                    public void keyTyped(KeyEvent e) {
                        KeyEvent newEvent = new KeyEvent(ESlateTextArea.this, e.getID(), e.getWhen(), e.getModifiers(), e.getKeyCode(), e.getKeyChar());

                        fireKeyTyped(newEvent);
                    }
                }
            );

            // When handle is created, there is no need for plugs to be created too. If the component is created by
            // a user, then the user is responsible to use the right property method (or editor) to create plus. Plug state is
            // stored and retrieved with the component's state.

            setPlugsUsed(true);
            pm.stop(initESlateAspectTimer);
            pm.eSlateAspectInitEnded(this);
            pm.displayTime(initESlateAspectTimer, handle, "", "ms");
        }
        return handle;
    }

    /**
     * Sets the area's text form a given text file
     * @param s The path of the text file
     */

    public void setTextFromFile(String s) {
        //URL url = null;
        String path = null;
        File file1 = null;
        File file2 = null;
        
        if (isEnabled() == true && s != null) {
            URL documentBase = handle.getESlateMicroworld().getDocumentBase();
            if (documentBase != null) {
                //url = new URL(documentBase, s);
                //path = url.toString().substring(6);
                try {
                  path  = URLDecoder.decode(documentBase.toString(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                  e.printStackTrace();
                  path = documentBase.toString();
                }
                int index = path.indexOf(':');
                int len1 = path.length() - 1;
                int removedSlashes = -1;
                if (index >= 0) {
                    do {
                        if (index < len1) {
                            index++;
                            removedSlashes++;
                        }else{
                            index = -1;
                            break;
                        }
                      } while (path.charAt(index) == '/');
                      if (index >= 0) {
                          path = path.substring(index);
                          // Check if we removed one / too many!
                          if (!new File(path).isAbsolute()) {
                              path = File.separator + path;
                              // If we removed 4 slashes, this was probably a
                              // Windows share, so prepend a second file
                              // separator. Under unix this won't hurt.
                              if (removedSlashes >= 4) {
                                path = File.separator + path;
                              }
                          }
                      }else{
                          path = "";
                    }
                }else{
                  path = "";
                }
            }
            file1 = new File(s);
            if (file1.exists()) {
                readFile(file1);
            } else {
                if (path != null) {
                    //file2 = new File(path);
                    file2 = new File(path, s);
                    if (file2.exists()) {
                        readFile(file2);
                    } else {
                        String msg = MessageFormat.format(
                            bundleMessages.getString("fileDoesNotExist"), s
                        );
                        setText(msg);
                    }
                }
            }
            if (handle != null)
                textFileSO.setString(s);
        }
    }

    /**
     * Sets the field's text
     * @param s The text
     */

    public void setText(String s) {
        if (isEnabled()){
            if (s!= null){
                if (!s.equals(textArea.getText()))
                    textArea.setText(s);
            }else
                textArea.setText(null);
        }
    }

    /**

     * Returns the valid background image file extensions
     */


    public String[] getBackgroundImageFileExtensions() {
        return new String[] {"gif", "jpg", "png"};
    }

    /**
     * Returns the valid background image file extension descriptions
     */

    public String[] getBackgroundImageFileExtensionDescriptions() {
        return new String[] {bundleMessages.getString("GIF Files"), bundleMessages.getString("JPEG Files"), bundleMessages.getString("PNG Files")};
    }

    /**

     * Reads from ESlateFieldMap to restore stored values and properties
     */

    public void readExternal(java.io.ObjectInput in) throws IOException, ClassNotFoundException {
        PerformanceManager pm = PerformanceManager.getPerformanceManager();

        pm.init(loadTimer);
        Object firstObj = in.readObject();

        /*if (!ESlateFieldMap.class.isAssignableFrom(firstObj.getClass())) {
         // Old time readExtermal()
         oldTimeReadExternal(in, firstObj);
         return;
         } */

//        ESlateFieldMap fieldMap = (ESlateFieldMap) firstObj;
        StorageStructure fieldMap = (StorageStructure) firstObj;

        setAlignmentX(fieldMap.get("AlignmentX", getAlignmentX()));
        setAlignmentY(fieldMap.get("AlignmentY", getAlignmentY()));

        if (fieldMap.containsKey("Font"))
            textArea.setFont((Font) fieldMap.get("Font", textArea.getFont()));

        setEnabled(fieldMap.get("Enabled", isEnabled()));
        textArea.setEditable(fieldMap.get("Editable", textArea.isEditable()));
        textArea.setLineWrap(fieldMap.get("LineWrap", textArea.getLineWrap()));
        setDoubleBuffered(fieldMap.get("DoubleBuffered", isDoubleBuffered()));
        textArea.setWrapStyleWord(fieldMap.get("WrapStyleWord", textArea.getWrapStyleWord()));
        setOpaque(fieldMap.get("Opaque", isOpaque()));
        setDebugGraphicsOptions(fieldMap.get("DebugGraphicsOptions", getDebugGraphicsOptions()));
        textArea.setColumns(fieldMap.get("Columns", textArea.getColumns()));
        textArea.setRows(fieldMap.get("Rows", textArea.getRows()));

        textArea.setText(fieldMap.get("Text", textArea.getText()));
        textArea.setTabSize(fieldMap.get("TabSize", textArea.getTabSize()));

        if (fieldMap.get("CaretPosition", textArea.getCaretPosition()) >= 0 && fieldMap.get("Text", textArea.getText()).length() + 1 >= fieldMap.get("CaretPosition", textArea.getCaretPosition())) {
            textArea.setCaretPosition(fieldMap.get("CaretPosition", textArea.getCaretPosition()));
        }
        setName(fieldMap.get("Name", getName()));
        setToolTipText(fieldMap.get("ToolTipText", getToolTipText()));
        textArea.setMargin((Insets) fieldMap.get("Margin", textArea.getMargin()));

        if (fieldMap.containsKey("Background"))
            setBackground(fieldMap.get("Background", getBackground()));
        if (fieldMap.containsKey("Foreground"))
            setForeground(fieldMap.get("Foreground", getForeground()));

        textArea.setCaretColor(fieldMap.get("CaretColor", textArea.getCaretColor()));

        textArea.setDisabledTextColor(fieldMap.get("DisabledTextColor", textArea.getDisabledTextColor()));
        textArea.setSelectedTextColor(fieldMap.get("SelectedTextColor", textArea.getSelectedTextColor()));
        textArea.setSelectionColor(fieldMap.get("SelectionColor", textArea.getSelectionColor()));
        setFireOnEnterPress(fieldMap.get("FireOnEnterPress", getFireOnEnterPress()));
        try {
            setSelectionStart(fieldMap.get("SelectionStart", getSelectionStart()));
            setSelectionEnd(fieldMap.get("SelectionEnd", getSelectionEnd()));
        } catch (IllegalArgumentException e) {
            System.out.println(" Problem caused by : " + handle.getComponentName());
        }

        if (fieldMap.containsKey("BackgroundImage")) {
            try {
                setBackgroundIcon((NewRestorableImageIcon) fieldMap.get("BackgroundImage", getBackgroundIcon()));
            } catch (Throwable thr) {}
        }

        setMaximumSize((Dimension) fieldMap.get("MaximumSize", getMaximumSize()));
        setMinimumSize((Dimension) fieldMap.get("MinimumSize", getMinimumSize()));
        setPreferredSize((Dimension) fieldMap.get("PreferredSize", getPreferredSize()));

        setHorizontalScrollBarPolicy(fieldMap.get("HorizontalScrollBarPolicy", getHorizontalScrollBarPolicy()));
        setVerticalScrollBarPolicy(fieldMap.get("VerticalScrollBarPolicy", getVerticalScrollBarPolicy()));
        if (fieldMap.containsKey("PlugsUsed"))
            setPlugsUsed(fieldMap.get("PlugsUsed", getPlugsUsed()));

        if (fieldMap.containsKey("Border")) {
            try {
                BorderDescriptor bd = null;

                if (fieldMap.get("Border") == null) {
                    setBorder(null);
                } else {
                    bd = (BorderDescriptor) fieldMap.get("Border");
                    setBorder(bd.getBorder());
                }

            } catch (Throwable thr) {
                thr.printStackTrace();
            }
        } else setBorder(null);
        pm.stop(loadTimer);
        pm.displayTime(loadTimer, getESlateHandle(), "", "ms");

    }

    /**
     * Writes to ESlateFieldMap to store values and properties
     */

    public void writeExternal(java.io.ObjectOutput out) throws IOException {
        PerformanceManager pm = PerformanceManager.getPerformanceManager();

        pm.init(saveTimer);
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(FORMAT_VERSION);

        fieldMap.put("AlignmentX", getAlignmentX());
        fieldMap.put("AlignmentY", getAlignmentY());

        if (!(getFont() instanceof UIResource))
            fieldMap.put("Font", textArea.getFont());

        fieldMap.put("Enabled", isEnabled());
        fieldMap.put("Editable", textArea.isEditable());
        fieldMap.put("LineWrap", textArea.getLineWrap());
        fieldMap.put("DoubleBuffered", isDoubleBuffered());
        fieldMap.put("WrapStyleWord", textArea.getWrapStyleWord());
        fieldMap.put("Opaque", isOpaque());

        fieldMap.put("DebugGraphicsOptions", getDebugGraphicsOptions());
        fieldMap.put("Columns", textArea.getColumns());
        fieldMap.put("Rows", textArea.getRows());

        fieldMap.put("Text", textArea.getText());

        fieldMap.put("TabSize", textArea.getTabSize());
        fieldMap.put("CaretPosition", textArea.getCaretPosition());
        fieldMap.put("Name", getName());
        fieldMap.put("ToolTipText", getToolTipText());
        fieldMap.put("Margin", textArea.getMargin());

        if (!(getBackground() instanceof UIResource))
            fieldMap.put("Background", getBackground());
        if (!(getForeground() instanceof UIResource))
            fieldMap.put("Foreground", getForeground());
        fieldMap.put("CaretColor", textArea.getCaretColor());
        fieldMap.put("DisabledTextColor", textArea.getDisabledTextColor());
        fieldMap.put("SelectedTextColor", textArea.getSelectedTextColor());
        fieldMap.put("SelectionColor", textArea.getSelectionColor());
        fieldMap.put("FireOnEnterPress", getFireOnEnterPress());
        fieldMap.put("SelectionStart", textArea.getSelectionStart());
        fieldMap.put("SelectionEnd", textArea.getSelectionEnd());
        if (initialIcon != null) {
            fieldMap.put("BackgroundImage", getBackgroundIcon());
        }
        fieldMap.put("MaximumSize", getMaximumSize());
        fieldMap.put("MinimumSize", getMinimumSize());
        fieldMap.put("PreferredSize", getPreferredSize());

        fieldMap.put("HorizontalScrollBarPolicy", getHorizontalScrollBarPolicy());
        fieldMap.put("Vertic4alScrollBarPolicy", getVerticalScrollBarPolicy());
        fieldMap.put("PlugsUsed", getPlugsUsed());

        if (getBorder() != null && !(getBorder() instanceof UIResource)) {
            try {
                BorderDescriptor bd = ESlateUtils.getBorderDescriptor(getBorder(), this);

                fieldMap.put("Border", bd);
            } catch (Throwable thr) {}
        } else if (getBorder() == null) {
            try {
                BorderDescriptor bd = ESlateUtils.getBorderDescriptor(getBorder(), this);

                fieldMap.put("Border", bd);
            } catch (Throwable thr) {}
        }
        out.writeObject(fieldMap);
        pm.stop(saveTimer);
        pm.displayTime(saveTimer, getESlateHandle(), "", "ms");
    }

    /**
     * Sets an image as background from a valid image file
     * @param s The image file
     */

    public void setBackgroundImageFile(File s) {
        System.out.println("File  :"+s.getPath());
        if (s == null) {
            initialIcon.getImage().flush();
            initialIcon = null;
            return;
        }
        originalName = s.getAbsolutePath();

        backImageFile = s;

        if ((originalName.toLowerCase().endsWith(".gif")) || (originalName.toLowerCase().endsWith(".jpg")) || (originalName.toLowerCase().endsWith(".png")))
            try {
                initialIcon = new NewRestorableImageIcon(new URL("file:/" + originalName));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        //         textArea.setOpaque(false);
        repaint();
    }

    /**

     * Gets the valid image file whose image is used for background
     * @return s The image file
     */


    public File getBackgroundImageFile() {

        return backImageFile;

    }

    /**

     * Sets the icon whose image is used for background
     * @param icon The icon (NewRestorableImageIcon)
     */


    public void setBackgroundIcon(NewRestorableImageIcon icon) {

        if (icon != null)

            initialIcon = icon;

        repaint();

    }

    /**

     * Gets the icon whose image is used for background
     * @return initialIcon The icon (NewRestorableImageIcon)
     */


    public NewRestorableImageIcon getBackgroundIcon() {

        return initialIcon;

    }

    protected void paintComponent(Graphics g) {

        Graphics cg;

        cg = g.create();
        if (initialIcon != null)
            cg.drawImage(initialIcon.getImage(), 0, 0, this.getWidth(), this.getHeight(), null);
        cg.dispose();
        super.paintComponent(g);
    }

    /**
     * Reads a file line by line and puts the text to the area
     * @param file The text file
     */

    public void readFile(File file) {
        setText(null);
        FileReader fr = null;
        if (file != null) {
            try {
                fr = new FileReader(file);
                String line;
                BufferedReader br = new BufferedReader(fr, 30000);

                try {
                    while ((line = br.readLine()) != null) {
                        textArea.append(line);
                        textArea.append("\n");
                    }
                    br.close();
                } catch (IOException e) {}
            } catch (FileNotFoundException e) {}
        } else return;
    }

    /**
     * Sets the text file whose text will be placed in the textArea
     * @param s The text file
     */


    public void setTextFile(File s) {
        System.out.println("File  :"+s.getPath());
        textFile = s;

        readFile(textFile);

    }

    /**

     * Gets the text file whose text was used in the textArea
     * @return textFile The text file
     */


    public File getTextFile() {

        return textFile;

    }

    public int[] getSelectionPoints() {

        if (dot == mark) {  // no selection
            int[] array = {dot, mark};

            return array;
        } else if (dot < mark) {
            int[] array = {dot, mark};

            return array;
        } else {
            int[] array = {mark, dot};

            return array;
        }
    }

    /**
     * Determines whether text should be distributed to other components on ENTER press
     */

    public void setFireOnEnterPress(boolean fireOnEnter) {
        fireOnEnterPress = fireOnEnter;
    }

    /**
     * Returns whether text is distributed to other components on ENTER press or not
     */

    public boolean getFireOnEnterPress() {
        return fireOnEnterPress;
    }

    public Color getCaretColor() {
        return textArea.getCaretColor();
    }

    public void setCaretColor(Color c) {
        textArea.setCaretColor(c);
    }

    public int getCaretPosition() {
        return textArea.getCaretPosition();
    }

    public void setCaretPosition(int i) {
        textArea.setCaretPosition(i);
    }

    public int getRows() {

        return textArea.getRows();
    }

    public void setRows(int i) {
        textArea.setRows(i);
    }

    public int getColumns() {

        return textArea.getColumns();
    }

    public void setColumns(int i) {
        textArea.setColumns(i);
    }

    public int getSelectionStart() {

        return textArea.getSelectionStart();
    }

    public void setSelectionStart(int i) {
        textArea.setSelectionStart(i);
    }

    public int getSelectionEnd() {

        return textArea.getSelectionEnd();
    }

    public void setSelectionEnd(int i) {
        textArea.setSelectionEnd(i);
    }

    public int getTabSize() {

        return textArea.getTabSize();
    }

    public void setTabSize(int i) {
        textArea.setTabSize(i);
    }

    public boolean isEditable() {

        return textArea.isEditable();
    }

    public void setEditable(boolean b) {
        textArea.setEditable(b);
    }

    public boolean getLineWrap() {

        return textArea.getLineWrap();
    }

    public void setLineWrap(boolean b) {
        textArea.setLineWrap(b);
    }

    public boolean getWrapStyleWord() {

        return textArea.getWrapStyleWord();
    }

    public void setWrapStyleWord(boolean b) {
        textArea.setWrapStyleWord(b);
    }

    public Color getDisabledTextColor() {

        return textArea.getDisabledTextColor();
    }

    public void setDisabledTextColor(Color c) {
        textArea.setDisabledTextColor(c);
    }

    public Color getSelectedTextColor() {

        return textArea.getSelectedTextColor();
    }

    public void setSelectedTextColor(Color c) {
        textArea.setSelectedTextColor(c);
    }

    public Color getSelectionColor() {

        return textArea.getSelectionColor();
    }

    public void setSelectionColor(Color c) {
        textArea.setSelectionColor(c);
    }

    public String getText() {

        return textArea.getText();

    }

    public void setFont(Font f){
        if (textArea != null)
            textArea.setFont(f);
    }

    public Font getFont(){
        if (textArea != null)
            return textArea.getFont();
        else return null;
    }

    public void setForeground(Color c) {

        if (textArea != null)

            textArea.setForeground(c);

    }

    public Color getForeground() {

        if (textArea != null)

            return textArea.getForeground();

        else return null;

    }

    public void setCursor(Cursor cursor){
        if (textArea!= null)
            textArea.setCursor(cursor);
        this.currentCursor = cursor;
    }

    public Cursor getCursor(){
        return currentCursor;
    }

    public void setEnabled(boolean b) {

        super.setEnabled(b);

        if (textArea != null)

            textArea.setEnabled(b);

    }

    public boolean isEnabled() {

        if (textArea != null)

            return textArea.isEnabled();

        else

            return super.isEnabled();

    }

    protected void fireKeyPressed(KeyEvent e) {
        EventListener[] listeners = listenerList.getListeners(KeyListener.class);

        for (int i = 0; i < listeners.length; i++) {
            ((KeyListener) listeners[i]).keyPressed(e);
        }
    }

    protected void fireKeyReleased(KeyEvent e) {
        EventListener[] listeners = listenerList.getListeners(KeyListener.class);

        for (int i = 0; i < listeners.length; i++) {
            ((KeyListener) listeners[i]).keyReleased(e);
        }
    }

    protected void fireKeyTyped(KeyEvent e) {
        EventListener[] listeners = listenerList.getListeners(KeyListener.class);

        for (int i = 0; i < listeners.length; i++) {
            ((KeyListener) listeners[i]).keyTyped(e);
        }

    }

    protected void fireMousePressed(MouseEvent e) {

        EventListener[] listeners = listenerList.getListeners(MouseListener.class);

        for (int i = 0; i < listeners.length; i++) {
            ((MouseListener) listeners[i]).mousePressed(e);
        }
    }

    protected void fireMouseReleased(MouseEvent e) {

        EventListener[] listeners = listenerList.getListeners(MouseListener.class);

        for (int i = 0; i < listeners.length; i++) {
            ((MouseListener) listeners[i]).mouseReleased(e);
        }
    }

    protected void fireMouseClicked(MouseEvent e) {

        EventListener[] listeners = listenerList.getListeners(MouseListener.class);

        for (int i = 0; i < listeners.length; i++) {
            ((MouseListener) listeners[i]).mouseClicked(e);
        }

    }

    protected void fireMouseEntered(MouseEvent e) {

        EventListener[] listeners = listenerList.getListeners(MouseListener.class);

        for (int i = 0; i < listeners.length; i++) {
            ((MouseListener) listeners[i]).mouseEntered(e);
        }
    }

    protected void fireMouseExited(MouseEvent e) {

        EventListener[] listeners = listenerList.getListeners(MouseListener.class);

        for (int i = 0; i < listeners.length; i++) {
            ((MouseListener) listeners[i]).mouseExited(e);
        }
    }

    protected void fireMouseDragged(MouseEvent e) {

        EventListener[] listeners = listenerList.getListeners(MouseMotionListener.class);

        for (int i = 0; i < listeners.length; i++) {
            ((MouseMotionListener) listeners[i]).mouseDragged(e);
        }

    }

    protected void fireMouseMoved(MouseEvent e) {

        EventListener[] listeners = listenerList.getListeners(MouseMotionListener.class);

        for (int i = 0; i < listeners.length; i++) {
            ((MouseMotionListener) listeners[i]).mouseMoved(e);
        }

    }

    protected void fireVetoableChange(PropertyChangeEvent e) {

        EventListener[] listeners = listenerList.getListeners(VetoableChangeListener.class);

        for (int i = 0; i < listeners.length; i++) {
            try {
                ((VetoableChangeListener) listeners[i]).vetoableChange(e);
            } catch (Exception exc) {
                exc.printStackTrace();

            }

        }

    }

    protected void firePropertyChange(PropertyChangeEvent e) {

        EventListener[] listeners = listenerList.getListeners(PropertyChangeListener.class);

        for (int i = 0; i < listeners.length; i++) {
            ((PropertyChangeListener) listeners[i]).propertyChange(e);
        }

    }

    public void addKeyListener(KeyListener l) {

        listenerList.add(KeyListener.class, l);

    }

    public void removeKeyListener(KeyListener l) {

        listenerList.remove(KeyListener.class, l);

    }

    public void addMouseListener(MouseListener l) {

        listenerList.add(MouseListener.class, l);

    }

    public void removeMouseListener(MouseListener l) {

        listenerList.remove(MouseListener.class, l);

    }

    public void addMouseMotionListener(MouseMotionListener l) {

        listenerList.add(MouseMotionListener.class, l);

    }

    public void removeMouseMotionListener(MouseMotionListener l) {

        listenerList.remove(MouseMotionListener.class, l);

    }

    public void addPropertyChangeListener(PropertyChangeListener l) {

        listenerList.add(PropertyChangeListener.class, l);

    }

    public void removePropertyChangeListener(PropertyChangeListener l) {

        listenerList.remove(PropertyChangeListener.class, l);

    }

    public void addVetoableChangeListener(VetoableChangeListener l) {

        listenerList.add(VetoableChangeListener.class, l);

    }

    public void removeVetoableChangeListener(VetoableChangeListener l) {

        listenerList.remove(VetoableChangeListener.class, l);

    }

    public void addCaretListener(CaretListener l) {

        listenerList.add(CaretListener.class, l);

    }

    public void removeCaretListener(CaretListener l) {

        listenerList.remove(CaretListener.class, l);

    }

    protected void fireCaretUpdate(CaretEvent e) {

        EventListener[] listeners = listenerList.getListeners(CaretListener.class);

        for (int i = 0; i < listeners.length; i++) {
            ((CaretListener) listeners[i]).caretUpdate(e);
        }

    }

    public JTextArea getTextArea() {

        return textArea;

    }

    public void updateUI() {

        boolean opaque = isOpaque();
        Border border = getBorder();

        super.updateUI();
        setOpaque(opaque);
        if (textArea != null)
            textArea.updateUI();
        getViewport().setOpaque(false);
        setBorder(border);
    }

    private void createPlugs() {
        if (handle == null)
            return;
        stringSO = new StringSO(handle);
        textFileSO = new StringSO(handle);
        try {
            SharedObjectListener sol = new SharedObjectListener() {
                    public synchronized void handleSharedObjectEvent(SharedObjectEvent e) {
                        if (!getText().equals(((StringSO) e.getSharedObject()).getString()))
                            setText(((StringSO) e.getSharedObject()).getString());
                    }
                };

            plug = new MultipleInputMultipleOutputPlug(handle, bundleMessages, "Title", new Color(139, 117, 0),
                        gr.cti.eslate.sharedObject.StringSO.class,
                        stringSO, sol);
            plug.addConnectionListener(new ConnectionListener() {
                    public void handleConnectionEvent(ConnectionEvent e) {
                        if (e.getType() == Plug.INPUT_CONNECTION) {
                            StringSO so = (StringSO) ((SharedObjectPlug) e.getPlug()).getSharedObject();

                            setText(so.getString());
                        }
                    }
                }
            );
            handle.addPlug(plug);
        } catch (InvalidPlugParametersException e) {} catch (PlugExistsException e) {}

        try {
            SharedObjectListener sol = new SharedObjectListener() {
                    public synchronized void handleSharedObjectEvent(SharedObjectEvent e) {
                        setTextFromFile(((StringSO) e.getSharedObject()).getString());
                    }
                };

            plug2 = new MultipleInputPlug(handle, bundleMessages, "Text File", new Color(139, 117, 0),
                        gr.cti.eslate.sharedObject.StringSO.class,

                        /*textFileSO,*/sol);
            plug2.addConnectionListener(new ConnectionListener() {
                    public void handleConnectionEvent(ConnectionEvent e) {
                        // make sure connected component gets value of vector when connected
                        if (e.getType() == Plug.INPUT_CONNECTION) {
                            StringSO so = (StringSO) ((SharedObjectPlug) e.getPlug()).getSharedObject();

                            setTextFromFile(so.getString());
                        }
                    }
                }
            );
            handle.addPlug(plug2);
        } catch (InvalidPlugParametersException e) {} catch (PlugExistsException e) {}
    }

    public void setPlugsUsed(boolean create) {
        if (plugsCreated==create)
            return;
        if (handle != null) {
            if (create)
                createPlugs();
            else
                destroyPlugs();
            plugsCreated = create;
        } else
            plugsCreated = false;
    }

    public boolean getPlugsUsed() {
        return plugsCreated;
    }

    private void destroyPlugs() {
        try {
            if (plug2 != null)
                handle.removePlug(plug2);
            if (plug != null)
                handle.removePlug(plug);
        } catch (Exception exc) {
            System.out.println("Plug to be removed not found");
            exc.printStackTrace();
        }
        plug2 = null;
        plug = null;
        stringSO = null;
        textFileSO = null;
    }

    class MyTextArea extends JTextArea {

        public MyTextArea()
        {
          super();
          setFont(new Font("Dialog", Font.PLAIN, 12));
        }
    	
    	static final long serialVersionUID = 9L;
        public void updateUI() {
            boolean opaque = isOpaque();

            super.updateUI();
            setOpaque(opaque);
        }
    }

    /**
     * This method creates and adds a PerformanceListener to the E-Slate's
     * Performance Manager. The PerformanceListener attaches the component's
     * timers when the Performance Manager becomes enabled.
     */
    private void createPerformanceManagerListener(PerformanceManager pm) {
        if (perfListener == null) {
            perfListener = new PerformanceAdapter() {
                        public void performanceManagerStateChanged(PropertyChangeEvent e) {
                            boolean enabled = ((Boolean) e.getNewValue()).booleanValue();

                            // When the Performance Manager is enabled, try to attach the
                            // timers.
                            if (enabled) {
                                attachTimers();
                            }
                        }
                    };
            pm.addPerformanceListener(perfListener);
        }
    }

    /**
     * This method creates and attaches the component's timers. The timers are
     * created only once and are assigned to global variables. If the timers
     * have been already created, they are not re-created. If the timers have
     * been already attached, they are not attached again.
     * This method does not create any timers while the PerformanceManager is
     * disabled.
     */
    private void attachTimers() {
        PerformanceManager pm = PerformanceManager.getPerformanceManager();
        boolean pmEnabled = pm.isEnabled();

        // If the performance manager is disabled, install a listener which will
        // re-invoke this method when the performance manager is enabled.
        if (!pmEnabled && (perfListener == null)) {
            createPerformanceManagerListener(pm);
        }

        // Do nothing if the PerformanceManager is disabled.
        if (!pmEnabled) {
            return;
        }

        boolean timersCreated = (loadTimer != null);

        // If the component's timers have not been constructed yet, then
        // construct them. During construction, the timers are also attached.
        if (!timersCreated) {
            // Get the performance timer group for this component.
            PerformanceTimerGroup compoTimerGroup = pm.getPerformanceTimerGroup(this);

            // Construct and attach the component's timers.
            constructorTimer = (PerformanceTimer) pm.createPerformanceTimerGroup(
                        compoTimerGroup, bundleMessages.getString("ConstructorTimer"), true
                    );
            loadTimer = (PerformanceTimer) pm.createPerformanceTimerGroup(
                        compoTimerGroup, bundleMessages.getString("LoadTimer"), true
                    );
            saveTimer = (PerformanceTimer) pm.createPerformanceTimerGroup(
                        compoTimerGroup, bundleMessages.getString("SaveTimer"), true
                    );
            initESlateAspectTimer = (PerformanceTimer) pm.createPerformanceTimerGroup(
                        compoTimerGroup, bundleMessages.getString("InitESlateAspectTimer"), true
                    );
            pm.registerPerformanceTimerGroup(
                PerformanceManager.CONSTRUCTOR, constructorTimer, this
            );
            pm.registerPerformanceTimerGroup(
                PerformanceManager.LOAD_STATE, loadTimer, this
            );
            pm.registerPerformanceTimerGroup(
                PerformanceManager.SAVE_STATE, saveTimer, this
            );
            pm.registerPerformanceTimerGroup(
                PerformanceManager.INIT_ESLATE_ASPECT, initESlateAspectTimer, this
            );
        }
    }

}

