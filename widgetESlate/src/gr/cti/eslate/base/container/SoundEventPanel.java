package gr.cti.eslate.base.container;

import gr.cti.eslate.base.ESlateHandle;
import gr.cti.eslate.utils.NoBorderButton;
import gr.cti.eslate.utils.sound.ESlateSound;
import gr.cti.eslate.utils.sound.ESlateSoundEvent;
import gr.cti.eslate.utils.sound.ESlateSoundListener;
import gr.cti.eslate.utils.sound.SoundUtils;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.MethodDescriptor;
import java.io.File;
import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class SoundEventPanel extends JPanel {
    JList eventList;
    JScrollPane scrollPane;
    SoundControlPanel controlPanel;
    boolean initialized = false;
    Object target = null;
    HierarchicalComponentPath2 pathToComponent = null;
    ESlateHandle topHandle = null;
    ESlateContainer container;
    DefaultListModel targetSoundEvents = new DefaultListModel();
    ImageIcon soundIcon = new ImageIcon(getClass().getResource("images/sound.gif"));
    ResourceBundle bundle;

    public SoundEventPanel(ESlateContainer container) {
        this.container = container;
    }

    public void initialize() {
        if (initialized) return;
        container.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR), false);
        initialized = true;

        bundle=ResourceBundle.getBundle("gr.cti.eslate.base.container.SoundEventPanelBundle", Locale.getDefault());

        eventList = new JList() {
            public void updateUI() {
                super.updateUI();
//System.out.println("Setting eventList bgr to: " + javax.swing.UIManager.getColor("control"));
                    setBackground(javax.swing.UIManager.getColor("control"));
            }
        };
//        eventList.setBorder(new javax.swing.border.LineBorder(java.awt.Color.red));
        eventList.setModel(targetSoundEvents);
        eventList.setCellRenderer(new SoundEventRenderer(this));
        eventList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        eventList.setBackground(javax.swing.UIManager.getColor("control"));
        scrollPane = new JScrollPane(eventList);
        scrollPane.setBorder(null);

        controlPanel = new SoundControlPanel(this);

        setLayout(new BorderLayout(0, 0));
        add(scrollPane, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.NORTH);
        setBorder(new EtchedBorder());

        eventList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) return;
                SoundEvent se = (SoundEvent) eventList.getSelectedValue();
                controlPanel.setSoundEvent(se);
            }
        });
        eventList.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    controlPanel.attachNewSound();
                }
            }
        });
        eventList.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    controlPanel.attachNewSound();
            }
        });

        container.setCursor(Cursor.getDefaultCursor(), false);
    }

    void initializeSoundEvents(Object target, ESlateHandle topHandle, HierarchicalComponentPath2 pathToComponent, SoundListener[] soundListeners) {
        if (this.target == target) {
            return;
        }
        this.target = target;
//System.out.println("initializeSoundEvents topHandle: " + topHandle + ", pathToComponent: " + pathToComponent);
        this.pathToComponent = pathToComponent;
        this.topHandle = topHandle;
        targetSoundEvents.clear();

        initialize();

        EventSetDescriptor[] eventDescriptors = null;
        Class objClass = target.getClass();
        BeanInfo objInfo = BeanInfoFactory.getBeanInfo(objClass); //compoClass.getSuperclass());
        if (objInfo == null) return;

//        System.out.println("compoInfo: " + compoInfo);
        eventDescriptors = objInfo.getEventSetDescriptors();
//        System.out.println("eventDescriptors: " + eventDescriptors);
//        System.out.println("eventDescriptors.length: " + eventDescriptors.length);

        if (eventDescriptors == null)
            return;

        //Construct the dialog UI. Create one panel for each event
        for (int i=0; i<eventDescriptors.length; i++) {
            MethodDescriptor[] methodDescriptors = eventDescriptors[i].getListenerMethodDescriptors();
            for (int k=0; k<methodDescriptors.length; k++) {
                String methodName = methodDescriptors[k].getMethod().getName();
                SoundEvent se = new SoundEvent(eventDescriptors[i].getListenerType(), methodName, ' ' + methodDescriptors[k].getDisplayName(), null, eventDescriptors[i]);
                if (soundListeners != null) {
                    for (int j=0; j<soundListeners.length; j++) {
//                        System.out.println("scriptListeners[j].getMethodName(): " + scriptListeners[j].getMethodName() +
//                        ", methods[k].getName(): " + methods[k].getName());
                        if (soundListeners[j].methodName.equals(methodName))
                            se.soundListener = soundListeners[j];
                    }
                }
                targetSoundEvents.add(targetSoundEvents.size(), se);
            }
        }

        /* Sort the event panels based on the names of the their methods.
         */
        com.objectspace.jgl.OrderedMap evtMap = new com.objectspace.jgl.OrderedMap(new com.objectspace.jgl.LessString());
        for (int i=0; i<targetSoundEvents.size(); i++)
            evtMap.add(((SoundEvent) targetSoundEvents.get(i)).humanEventName, targetSoundEvents.get(i));
        java.util.Enumeration methodNames = evtMap.keys();
        int t = 0;
        while (methodNames.hasMoreElements()) {
            targetSoundEvents.set(t, evtMap.get(methodNames.nextElement()));
            t++;
        }
        evtMap = null;

        eventList.setSelectedIndex(-1);
        controlPanel.setSoundEvent(null);
        revalidate();
//        eventList.repaint();
    }

    public void clear() {
        targetSoundEvents.clear();
        initialize();
        controlPanel.setSoundEvent(null);
    }
}

class SoundEvent {
    Class listenerClass = null;
    String methodName = null;
    String humanEventName = null;
    SoundListener soundListener = null;
    EventSetDescriptor eventDescriptor = null;

    public SoundEvent(Class listenerClass, String methodName, String humanEventName, SoundListener soundListener, EventSetDescriptor eventDescriptor) {
        this.listenerClass = listenerClass;
        this.methodName = methodName;
        this.humanEventName = humanEventName;
        this.soundListener = soundListener;
        this.eventDescriptor = eventDescriptor;
    }
}

class SoundEventRenderer extends JPanel implements javax.swing.ListCellRenderer { //DefaultListCellRenderer {
    public static final int CELL_HEIGHT = 25;
    SoundEventPanel soundEventPanel = null;
    JLabel iconLabel, textLabel;
    JPanel textPanel;
    Border noFocusBorder;
    Dimension textLabelSize = new Dimension();

    public SoundEventRenderer(SoundEventPanel sep) {
        super();
        this.soundEventPanel = sep;

        iconLabel = new JLabel();
        iconLabel.setPreferredSize(new Dimension(sep.soundIcon.getIconWidth(), sep.soundIcon.getIconHeight()));
        noFocusBorder = new EmptyBorder(1, 1, 1, 1);

        textLabel = new JLabel();
        textLabel.setOpaque(true);
        textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel, BorderLayout.WEST);

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(textPanel);
        add(Box.createGlue());
        add(iconLabel);
        add(Box.createHorizontalStrut(5));
        setOpaque(false);
//        setIconTextGap(10);
    }

    public Component getListCellRendererComponent(JList list,
                                                  Object value,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus) {

//        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        SoundEvent se = (SoundEvent) value;
        textLabel.setText(se.humanEventName);
        if (se.soundListener != null)
            iconLabel.setIcon(soundEventPanel.soundIcon);
        else
            iconLabel.setIcon(null);
//System.out.println("SoundEventRenderer se: " + se.humanEventName + ", cellHasFocus: " + cellHasFocus + ", isSelected: " + isSelected);
        if (!isSelected) {
            textLabel.setForeground(BeanInfoDialog.PROPERTY_LABEL_COLOR);
            textLabel.setBackground(list.getBackground());
        }else{
            textLabel.setForeground(list.getSelectionForeground());
            textLabel.setBackground(list.getSelectionBackground());
        }
	setBorder((cellHasFocus) ? javax.swing.UIManager.getBorder("List.focusCellHighlightBorder") : noFocusBorder);
        return this;
    }

    public Dimension getPreferredSize() {
        Dimension dim = super.getPreferredSize();
        dim.height = CELL_HEIGHT;
        return dim;
    }

    public void updateUI() {
        super.updateUI();
        if (textLabel != null) {
            textLabel.updateUI();
            iconLabel.updateUI();
        }
    }
}

class SoundControlPanel extends JPanel {
    ImageIcon playSoundInIcon = new ImageIcon(SoundControlPanel.class.getResource("images/playSoundIn.gif"));
    ImageIcon playSoundIcon = new ImageIcon(SoundControlPanel.class.getResource("images/playSound.gif"));
    ImageIcon stopSoundInIcon = new ImageIcon(SoundControlPanel.class.getResource("images/stopSoundIn.gif"));
    ImageIcon stopSoundIcon = new ImageIcon(SoundControlPanel.class.getResource("images/stopSound.gif"));
    ImageIcon prevSoundInIcon = new ImageIcon(SoundControlPanel.class.getResource("images/prevSoundIn.gif"));
    ImageIcon prevSoundIcon = new ImageIcon(SoundControlPanel.class.getResource("images/prevSound.gif"));
    ImageIcon nextSoundInIcon = new ImageIcon(SoundControlPanel.class.getResource("images/nextSoundIn.gif"));
    ImageIcon nextSoundIcon = new ImageIcon(SoundControlPanel.class.getResource("images/nextSound.gif"));
    ImageIcon loadSoundIcon = new ImageIcon(SoundControlPanel.class.getResource("images/openFile.gif"));
    ImageIcon deleteSoundIcon = new ImageIcon(SoundControlPanel.class.getResource("images/delete.gif"));

    JButton play, stop, next, prev;
    NoBorderButton load, delete;
    JTextField soundNameField;
    private Insets buttonInsets = new Insets(0, 0, 0, 0);
    private Dimension minSize = null;
    SoundEvent soundEvent = null;
    SoundEventPanel soundPanel = null;
    ESlateSound sound = null;
long start;
    ESlateSoundListener eslateSoundListener = new ESlateSoundListener() {
        public void soundStopped(ESlateSoundEvent e) {
System.out.println("ET: " + (System.currentTimeMillis()-start));
            stopSound();
            SoundControlPanel.this.soundPanel.container.soundListenerMap.removePlayingSound((ESlateSound) e.getSource());
        }
    };


    public SoundControlPanel(SoundEventPanel soundPanel) {
        this.soundPanel = soundPanel;

        play = new JButton(playSoundIcon);
        play.setDisabledIcon(playSoundInIcon);
        play.setToolTipText(soundPanel.bundle.getString("Play"));
        stop = new JButton(stopSoundIcon);
        stop.setDisabledIcon(stopSoundInIcon);
        stop.setToolTipText(soundPanel.bundle.getString("Stop"));
        prev = new JButton(prevSoundIcon);
        prev.setDisabledIcon(prevSoundInIcon);
        prev.setToolTipText(soundPanel.bundle.getString("Previous"));
        next = new JButton(nextSoundIcon);
        next.setDisabledIcon(nextSoundInIcon);
        next.setToolTipText(soundPanel.bundle.getString("Next"));

        play.setMargin(buttonInsets);
        stop.setMargin(buttonInsets);
        prev.setMargin(buttonInsets);
        next.setMargin(buttonInsets);
        Border buttonBorder = new EmptyBorder(0, 0, 0,2);
        play.setBorder(buttonBorder);
        stop.setBorder(buttonBorder);
        prev.setBorder(buttonBorder);
        next.setBorder(buttonBorder);
        play.setFocusPainted(false);
        stop.setFocusPainted(false);
        next.setFocusPainted(false);
        prev.setFocusPainted(false);
        play.setRequestFocusEnabled(false);
        stop.setRequestFocusEnabled(false);
        next.setRequestFocusEnabled(false);
        prev.setRequestFocusEnabled(false);
        play.setHorizontalAlignment(SwingConstants.CENTER);
        stop.setHorizontalAlignment(SwingConstants.CENTER);
        next.setHorizontalAlignment(SwingConstants.CENTER);
        prev.setHorizontalAlignment(SwingConstants.CENTER);

        minSize = new Dimension(0, playSoundInIcon.getIconHeight());
        JPanel controlPanel = new JPanel() {
            public Dimension getMinimumSize() {
                return minSize;
            }
        };
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
        controlPanel.add(Box.createHorizontalStrut(3));
        controlPanel.add(play);
        controlPanel.add(stop);
        controlPanel.add(prev);
        controlPanel.add(next);

        load = new NoBorderButton(loadSoundIcon);
        load.setToolTipText(soundPanel.bundle.getString("Load"));
        delete = new NoBorderButton(deleteSoundIcon);
        delete.setToolTipText(soundPanel.bundle.getString("Delete"));
        load.setMargin(buttonInsets);
        delete.setMargin(buttonInsets);
        load.setFocusPainted(false);
        delete.setFocusPainted(false);

        JPanel loadDeletePanel = new JPanel();
        loadDeletePanel.setLayout(new BoxLayout(loadDeletePanel, BoxLayout.X_AXIS));
        loadDeletePanel.add(load);
        loadDeletePanel.add(delete);

        soundNameField = new JTextField() {
            public Dimension getPreferredSize() {
                return minSize;
            }
        };
        soundNameField.setEditable(false);
        soundNameField.setHorizontalAlignment(JTextField.LEFT);

        setLayout(new BorderLayout(3, 0));
        add(controlPanel, BorderLayout.WEST);
        add(soundNameField, BorderLayout.CENTER);
        add(loadDeletePanel, BorderLayout.EAST);

        setBorder(new CompoundBorder(new EmptyBorder(4, 0, 3, 0), new CompoundBorder(new gr.cti.eslate.utils.ConfigurableEtchedBorder(false, true, false, false), new EmptyBorder(0, 0, 3, 0))));

        prev.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stopSound();
                int selectedIndex = SoundControlPanel.this.soundPanel.eventList.getSelectedIndex();
                if (selectedIndex > 0) {
                    SoundControlPanel.this.soundPanel.eventList.setSelectedIndex(selectedIndex-1);
//                    SoundControlPanel.this.soundPanel.eventList.repaint();
                }else if (selectedIndex == -1)
                    SoundControlPanel.this.soundPanel.eventList.setSelectedIndex(0);
            }
        });
        next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = SoundControlPanel.this.soundPanel.eventList.getSelectedIndex();
                int size = SoundControlPanel.this.soundPanel.eventList.getModel().getSize();
                stopSound();
                if (selectedIndex < size-1) {
                    SoundControlPanel.this.soundPanel.eventList.setSelectedIndex(selectedIndex+1);
                }else if (selectedIndex == -1)
                    SoundControlPanel.this.soundPanel.eventList.setSelectedIndex(0);
            }
        });
        play.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//System.out.println("soundEvent.soundListener.embeddedFileName: " + soundEvent.soundListener.embeddedFileName);
                ESlateContainer container = SoundControlPanel.this.soundPanel.container;
                if (soundEvent.soundListener.embeddedFileName != null) {
                    Vector path = new Vector();
                    path.add("Sounds");
                    path.add(soundEvent.soundListener.embeddedFileName);
start = System.currentTimeMillis();
                    sound = SoundUtils.playSound(container.currentlyOpenMwdFile, container.currentlyOpenMwdFileName, path);
                }else{
start = System.currentTimeMillis();
                    sound = SoundUtils.playSound(soundEvent.soundListener.pathToSound);
                }
                container.soundListenerMap.addPlayingSound(sound);
                sound.addSoundListener(eslateSoundListener);
                play.setEnabled(false);
                stop.setEnabled(true);
            }
        });
        stop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stopSound();
            }
        });
        load.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                attachNewSound();
            }
        });
        delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ESlateContainer container = SoundControlPanel.this.soundPanel.container;
                ResourceBundle bundle = SoundControlPanel.this.soundPanel.bundle;
                Object target = SoundControlPanel.this.soundPanel.target;
                SoundListener soundListener = soundEvent.soundListener;
//System.out.println("delete() soundEvent.soundListener: " + soundEvent.soundListener);
                if (soundListener == null) {
                    ((JButton) e.getSource()).setEnabled(false);
                    return;
                }

                stopSound();

                // Remove the listener from the component.
                try{
                    EventListener currentListener = null;
                    if (soundListener != null)
                        currentListener = (EventListener) soundListener.listener;
//System.out.println("currentListener: " + currentListener);
                    if (currentListener != null) {
                        // Remove the installed listener
                        Method removeListenerMethod = soundEvent.eventDescriptor.getRemoveListenerMethod();
                        removeListenerMethod.invoke(target, new Object[] {currentListener});
                        soundListener.listener = null;
                    }
                }catch (Throwable thr) {
                    Frame topLevelFrame = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, container.parentComponent);
                    DetailedErrorDialog dialog = new DetailedErrorDialog(topLevelFrame);
                    dialog.setMessage(bundle.getString("FailureMsg") + ' ' + soundListener.soundName + ' ' + bundle.getString("FailureMsg1") + ' ' + soundEvent.humanEventName + '.');
                    dialog.appendToDetails(bundle.getString("FailureMsg3") + ' ' + soundEvent.listenerClass.getName() + '\n');
                    dialog.appendThrowableStackTrace(thr);
                    ESlateContainerUtils.showDetailedErrorDialog(container, dialog, null, true);
                }
                soundEvent.soundListener = null;
                container.soundListenerMap.removeSoundListener(soundListener);
                ((JButton) e.getSource()).setEnabled(false);
                SoundControlPanel.this.soundNameField.setText("");
                SoundControlPanel.this.play.setEnabled(false);
                SoundControlPanel.this.soundPanel.eventList.repaint();
            }
        });
    }

    void stopSound() {
        if (sound == null) return;
        SoundUtils.stopSound(sound);
        sound.removeSoundListener(eslateSoundListener);
        sound = null;
        stop.setEnabled(false);
        play.setEnabled(true);
    }

    public void setSoundEvent(SoundEvent e) {
        if (soundEvent == e && e != null) return;
        soundEvent = e;
        stopSound();
        if (e == null) {
            play.setEnabled(false);
            stop.setEnabled(false);
            prev.setEnabled(false);
            next.setEnabled(false);
            delete.setEnabled(false);
            load.setEnabled(false);
            soundNameField.setText("");
            soundNameField.setEnabled(false);
        }else{
            soundNameField.setEnabled(true);
            if (e.soundListener == null)
                soundNameField.setText("");
            else{
                soundNameField.setText(e.soundListener.soundName);
            }
            if (e.soundListener == null) {
                play.setEnabled(false);
                stop.setEnabled(false);
                delete.setEnabled(false);
            }else{
                play.setEnabled(true);
                stop.setEnabled(false);
                delete.setEnabled(true);
            }
            int index = soundPanel.targetSoundEvents.indexOf(e);
            adjustNextPrevButtons(index);
            if (index != -1 && index < soundPanel.targetSoundEvents.size())
                load.setEnabled(true);
            else
                load.setEnabled(false);

        }
    }

    void adjustNextPrevButtons(int index) {
        if (index == 0)
            prev.setEnabled(false);
        else
            prev.setEnabled(true);
        if (index == soundPanel.targetSoundEvents.size()-1)
            next.setEnabled(false);
        else
            next.setEnabled(true);
    }

    void attachNewSound() {
        stopSound();
        ESlateContainer container = soundPanel.container;
        JFileChooser fch = container.getSoundFileChooser();
        if (fch.isShowing()) {
            return;
        }
        int status = fch.showOpenDialog(container.parentComponent);
        if (status == javax.swing.JFileChooser.CANCEL_OPTION)
            return;

        if (fch.getSelectedFile() == null)
            return;

        File soundFile = fch.getSelectedFile();
        String pathToSound = soundFile.getAbsolutePath();
        String soundName = soundFile.getName();
//System.out.println("soundName: " + soundName);
//System.out.println("pathToSound: " + pathToSound);
//System.out.println("soundEvent: " + soundEvent);
        SoundHandler soundHandler = new SoundHandler(soundEvent.methodName);
        soundHandler.container = container;
//                SoundListener sl = new SoundListener(soundName, pathToSound, soundEvent.methodName, SoundControlPanel.this.soundPanel.pathToComponent, soundHandler, soundEvent.listenerClass);

        ResourceBundle bundle = soundPanel.bundle;
        Object target = soundPanel.target;
        SoundListener soundListener = soundEvent.soundListener;

        boolean soundAttachementFailed = false;
        EventListener listener = null;
        try{
            listener = (EventListener) java.lang.reflect.Proxy.newProxyInstance(this.getClass().getClassLoader(),
                                                new Class[] { soundEvent.listenerClass },
                                                soundHandler);
//System.out.println("Attaching listener: " + listener + ", soundHandler: " + soundHandler);
        }catch (Throwable thr) {
            Frame topLevelFrame = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, container.parentComponent);
            DetailedErrorDialog dialog = new DetailedErrorDialog(topLevelFrame);
            dialog.setMessage(bundle.getString("FailureMsg") + ' ' + soundName + ' ' + bundle.getString("FailureMsg1") + ' ' + soundEvent.humanEventName + '.');
            dialog.appendToDetails(bundle.getString("FailureMsg2") + ' ' + soundEvent.listenerClass.getName() + '\n');
            dialog.appendThrowableStackTrace(thr);
            ESlateContainerUtils.showDetailedErrorDialog(container, dialog, null, true);
            soundAttachementFailed = true;
        }

        // Add the instance of the listener class to the component.
        Method addListenerMethod = soundEvent.eventDescriptor.getAddListenerMethod();
        try{
            EventListener currentListener = null;
            if (soundListener != null)
                currentListener = (EventListener) soundListener.listener;
            if (!soundAttachementFailed && currentListener != null) {
                // Remove the installed listener
                Method removeListenerMethod = soundEvent.eventDescriptor.getRemoveListenerMethod();
                removeListenerMethod.invoke(target, new Object[] {currentListener});
//System.out.println("Removing listener: " + currentListener);
                soundListener.listener = null;
            }
        }catch (Throwable thr) {
            Frame topLevelFrame = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, container.parentComponent);
            DetailedErrorDialog dialog = new DetailedErrorDialog(topLevelFrame);
            dialog.setMessage(bundle.getString("FailureMsg") + ' ' + soundName + ' ' + bundle.getString("FailureMsg1") + ' ' + soundEvent.humanEventName + '.');
            dialog.appendToDetails(bundle.getString("FailureMsg3") + ' ' + soundEvent.listenerClass.getName() + '\n');
            dialog.appendThrowableStackTrace(thr);
            ESlateContainerUtils.showDetailedErrorDialog(container, dialog, null, true);
            soundAttachementFailed = true;
        }
        try{
            if (!soundAttachementFailed)
                addListenerMethod.invoke(target, new Object[] {listener});
        }catch (Throwable thr) {
            Frame topLevelFrame = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, container);
            DetailedErrorDialog dialog = new DetailedErrorDialog(topLevelFrame);
            dialog.setMessage(bundle.getString("FailureMsg") + ' ' + soundName + ' ' + bundle.getString("FailureMsg1") + ' ' + soundEvent.humanEventName + '.');
            dialog.appendToDetails(bundle.getString("FailureMsg4") + ' ' + soundEvent.listenerClass.getName() + '\n');
            dialog.appendThrowableStackTrace(thr);
            ESlateContainerUtils.showDetailedErrorDialog(container, dialog, null, true);
            soundAttachementFailed = true;
        }

        if (soundAttachementFailed) {
            if (soundListener != null)
                container.soundListenerMap.removeSoundListener(soundListener);
            soundNameField.setText("");
            play.setEnabled(false);
        }else{
//System.out.println("attachNewSound(): " + soundPanel.pathToComponent);
//            ESlateHandle topHandle = container.microworld.eslateMwd.getComponentHandle(soundPanel.pathToComponent.path[0]);
            if (soundListener == null) {
                soundListener = new SoundListener(soundName, pathToSound, soundEvent.methodName, soundPanel.pathToComponent, soundHandler, soundEvent.listenerClass);
                container.soundListenerMap.addSoundListener(target, soundListener, soundPanel.topHandle);
                soundEvent.soundListener = soundListener;
            }else{
                soundListener.soundName = soundName;
                if (soundListener.embeddedFileName != null)
                    soundListener.previousEmbeddedFileName = soundListener.embeddedFileName;
                soundListener.embeddedFileName = null;
                soundListener.pathToSound = pathToSound;
                soundListener.soundHandler = soundHandler;
            }
            soundListener.listener = listener;
            soundHandler.soundListener = soundListener;

            soundNameField.setText(soundName);
            play.setEnabled(true);
        }
        delete.setEnabled(true);
        play.setEnabled(true);
        soundPanel.eventList.repaint();
//System.out.println("soundEvent.soundListener: " + soundEvent.soundListener + ", soundEvent.soundListener.listener: " + soundEvent.soundListener.listener);
    }

}

