package gr.cti.eslate.animationEditor;

import gr.cti.eslate.animation.Actor;
import gr.cti.eslate.animation.Animation;
import gr.cti.eslate.animation.AnimationEvent;
import gr.cti.eslate.animation.AnimationModelListener;
import gr.cti.eslate.animation.AnimationViewListener;
import gr.cti.eslate.animation.BaseActor;
import gr.cti.eslate.animation.BaseMilestone;
import gr.cti.eslate.animation.BaseSegment;
import gr.cti.eslate.animation.CursorEvent;
import gr.cti.eslate.animation.FpsEvent;
import gr.cti.eslate.animation.FrameLabel;
import gr.cti.eslate.animation.LoopEvent;
import gr.cti.eslate.animation.Milestone;
import gr.cti.eslate.animation.PlayThreadEvent;
import gr.cti.eslate.animation.Segment;
import gr.cti.eslate.animation.SoundActor;
import gr.cti.eslate.animation.SoundSegment;
import gr.cti.eslate.base.ConnectionEvent;
import gr.cti.eslate.base.ConnectionListener;
import gr.cti.eslate.base.DisconnectionEvent;
import gr.cti.eslate.base.DisconnectionListener;
import gr.cti.eslate.base.ESlate;
import gr.cti.eslate.base.ESlateHandle;
import gr.cti.eslate.base.ESlateInfo;
import gr.cti.eslate.base.ESlatePart;
import gr.cti.eslate.base.InvalidPlugParametersException;
import gr.cti.eslate.base.Plug;
import gr.cti.eslate.base.PlugExistsException;
import gr.cti.eslate.base.RenamingForbiddenException;
import gr.cti.eslate.base.SingleInputPlug;
import gr.cti.eslate.base.sharedObject.SharedObjectEvent;
import gr.cti.eslate.base.sharedObject.SharedObjectListener;
import gr.cti.eslate.sharedObject.AnimationSO;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;
import gr.cti.typeArray.IntBaseArray;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.Externalizable;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.zookitec.layout.ComponentEF;
import com.zookitec.layout.ContainerEF;
import com.zookitec.layout.ExplicitConstraints;
import com.zookitec.layout.ExplicitLayout;

/**
 * This class implements a view and controller for the animation model.
 * @author	Augustine Grillakis
 * @version	1.0.0, 11-Mar-2002
 */
public class AnimationEditor extends JPanel implements ESlatePart, Externalizable {
  private ESlateHandle handle = null;
//  private final static String ANIMATION = "animation";
  private final static String ZOOM = "zoom";
  private final static String version = "1.0.0";
  private final static int storageVersion = 1;

//  Animation animation = new Animation();
  Animation animation = null;
  ArrayList actorViewers = new ArrayList();
  AnimationModelListener animationModelListener;
  AnimationViewListener animationViewListener;
  boolean processModelEvents = true;
  boolean processViewEvents = true;

  private static final int HGAP = 5;
  private static final int VGAP = 5;
  private int animationEditorWidth = 640;
  private int animationEditorHeight = 200;
  static final int ACTOR_VIEWER_HEIGHT = 20;
  static final int ACTOR_SPACE = 1;
  static final int SEGMENT_HEIGHT = 16;
  static final int SEGMENT_Y = 2;

  JScrollPane scoreScrollPane;
  int scrollPaneX = 0;
  int scrollPaneY = 30;
  int viewportGap = 4;
  Rule columnView;

  ActorPanel lblActorPanel = new ActorPanel(this);
  int lblActorWidth = 60;
  int lblActorHeight = 20;
  BufferedImage actorImage;

  ScorePanel mainPanel = new ScorePanel(this);
  int mainPanelRealWidth;
  int mainPanelZoomedWidth;
  BufferedImage scoreImage;

  TimeCursor timeCursorColumnView;
  TimeCursor timeCursorMainPanel;

  int zoom = 200;
  int actorViewerMaxWidth=0;
  int mouseMarkXStart=0;
  int mouseMarkXEnd=-1;

  ArrayList resizeThreads = new ArrayList();
//  PlayThread pt;
  JComboBox zoomList;

  private JToggleButton startButton, stopButton;
  private JButton resetButton;
  private JCheckBox loopButton;
  JComboBox fpsList;
  MilestoneViewer editMilestoneViewer = null;
//  ImageIcon start1, start2, start3, stop1, stop2 ,stop3, reset1, reset2, reset3;
  JPopupMenu popup = new JPopupMenu();
  JMenuItem menuItemAddFrameLabel;
  JMenuItem menuItemRemoveFrameLabel;
  JMenuItem menuItemEditFrameLabel;
  boolean showFlag;
  int mousePressedX;
  ResourceBundle resources = ResourceBundle.getBundle(
    "gr.cti.eslate.animationEditor.AnimationEditorResource", Locale.getDefault()
  );

  // For measuring the perfomance.
//  long startTime;
//  JLabel fpsLabel;

  /**
   * Returns Copyright information.
   * @return	The Copyright information.
   */
  private ESlateInfo getInfo()
  {
    String[] info = {
      resources.getString("credits1"),
      resources.getString("credits2"),
      resources.getString("credits3"),
    };
    return new ESlateInfo(
      resources.getString("componentName") + ", " +
	      resources.getString("version") + " " + version,
        info);
  }

  /**
   * Create a animation viewer component.
   */
  public AnimationEditor() {
    super();
    setLayout(new ExplicitLayout());
    setPreferredSize(new Dimension(animationEditorWidth, animationEditorHeight));
//    setBackground(Color.gray);

    menuItemAddFrameLabel = new JMenuItem();
    menuItemRemoveFrameLabel = new JMenuItem();
    menuItemEditFrameLabel = new JMenuItem();

//    start1 = new ImageIcon(getClass().getResource("images/start1.jpg"));
//    start2 = new ImageIcon(getClass().getResource("images/start2.jpg"));
//    start3 = new ImageIcon(getClass().getResource("images/start3.jpg"));
//    stop1 = new ImageIcon(getClass().getResource("images/stop1.jpg"));
//    stop2 = new ImageIcon(getClass().getResource("images/stop2.jpg"));
//    stop3 = new ImageIcon(getClass().getResource("images/stop3.jpg"));
//    reset1 = new ImageIcon(getClass().getResource("images/reset1.jpg"));
//    reset2 = new ImageIcon(getClass().getResource("images/reset2.jpg"));
//    reset3 = new ImageIcon(getClass().getResource("images/reset3.jpg"));
    Box buttonRow = Box.createHorizontalBox();
    buttonRow.add(Box.createHorizontalGlue());
    
    Dimension dimensionButtons = new Dimension(24, 24);
    
    startButton = new JToggleButton(new ImageIcon(getClass().getResource("images/start.gif")));
    startButton.setMnemonic(KeyEvent.VK_P);
    startButton.setToolTipText(resources.getString("start"));
//    startButton.setSelectedIcon(start2);
    startButton.setSelected(false);
    startButton.setBorder((Border)null);
    startButton.setBorderPainted(false);
    startButton.setFocusPainted(false);
    startButton.setActionCommand("start");
    startButton.setMinimumSize(dimensionButtons);
    startButton.setMaximumSize(dimensionButtons);
    startButton.setPreferredSize(dimensionButtons);
    startButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if ((animation != null) && (animation.getMaxFrameTime()>0)) {
          if (editMilestoneViewer != null)
            exitMilestoneEditMode();
          processViewEvents = false;
          animation.play();
        }
        else {
          startButton.setSelected(false);
          stopButton.setSelected(true);
        }
      }
    });
    startButton.setRolloverEnabled(true);
//    startButton.setRolloverIcon(start3);
    buttonRow.add(startButton);
//    buttonRow.add(Box.createHorizontalStrut(HGAP));
    
    stopButton = new JToggleButton(new ImageIcon(getClass().getResource("images/stop.gif")));
//    stopButton.setSelectedIcon(stop2);
    stopButton.setMnemonic(KeyEvent.VK_S);
    stopButton.setToolTipText(resources.getString("stop"));
    stopButton.setSelected(true);
    stopButton.setBorder((Border)null);
    stopButton.setBorderPainted(false);
    stopButton.setFocusPainted(false);
    stopButton.setActionCommand("stop");
    stopButton.setMinimumSize(dimensionButtons);
    stopButton.setMaximumSize(dimensionButtons);
    stopButton.setPreferredSize(dimensionButtons);
    stopButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (animation != null) {
          if (editMilestoneViewer != null)
            exitMilestoneEditMode();
          processViewEvents = false;
          animation.stop();
        }
      }
    });
    stopButton.setRolloverEnabled(true);
//    stopButton.setRolloverIcon(stop3);
    buttonRow.add(stopButton);
    ButtonGroup group = new ButtonGroup();
    group.add(startButton);
    group.add(stopButton);
//    buttonRow.add(Box.createHorizontalStrut(HGAP));
    
    ButtonGroup buttonGroup = new ButtonGroup();
    buttonGroup.add(startButton);
    buttonGroup.add(stopButton);
    
    resetButton = new JButton(new ImageIcon(getClass().getResource("images/reset.gif")));
//    resetButton.setPressedIcon(reset2);
    resetButton.setMnemonic(KeyEvent.VK_R);
    resetButton.setToolTipText(resources.getString("reset"));
    resetButton.setMargin(new Insets(0,0,0,0));
    resetButton.setActionCommand("reset");
    resetButton.setMinimumSize(dimensionButtons);
    resetButton.setMaximumSize(dimensionButtons);
    resetButton.setPreferredSize(dimensionButtons);
    resetButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (animation != null) {
          if (editMilestoneViewer != null)
            exitMilestoneEditMode();
          processViewEvents = false;
          animation.stop();
          animation.setCursorTime(1);
          startButton.setSelected(false);
          stopButton.setSelected(true);
        }
      }
    });
    resetButton.setBorder((Border)null);
    resetButton.setBorderPainted(false);
    resetButton.setFocusPainted(false);
    resetButton.setRolloverEnabled(true);
//    resetButton.setRolloverIcon(reset3);
    buttonRow.add(resetButton);
    buttonRow.add(Box.createHorizontalGlue());

    // Animation listener and its methods (actorAdded,actorRemoved, timeChanged).
    animationModelListener = new AnimationModelListener() {
      public void actorAdded(AnimationEvent e) {
        if (processModelEvents == false)
          processModelEvents = true;
        else {
          Rectangle2D.Double actorShape = new Rectangle2D.Double();
          ActorViewer actorViewer = new ActorViewer(AnimationEditor.this, e.getActor(), actorShape);
          addActorViewer(actorViewer);
          actorViewer.actorViewerZoomedWidth = getSize().width-scrollPaneX-lblActorWidth-viewportGap;
          actorViewer.actorViewerRealWidth = getReal(actorViewer.getActorViewerZoomedWidth());
          mainPanel.setPreferredSize(new Dimension(getMainPanelZoomedWidth(), actorViewers.size()*(ACTOR_VIEWER_HEIGHT+ACTOR_SPACE)));
          lblActorPanel.setPreferredSize(new Dimension(lblActorWidth, actorViewers.size()*(ACTOR_VIEWER_HEIGHT+ACTOR_SPACE)));
          mainPanel.revalidate();
          lblActorPanel.revalidate();
//          lblActorPanel.paintActors(actorViewers.size());
          lblActorPanel.paintActors();
          lblActorPanel.repaint();
          mainPanel.paintScoreImage(
            actorViewers.indexOf(actorViewer),
            actorViewers.indexOf(actorViewer),
            0,
            actorViewer.getActorViewerRealWidth());
          mainPanel.repaint();
          Rectangle r = new Rectangle(0, actorViewers.size()*(ACTOR_VIEWER_HEIGHT+ACTOR_SPACE), 1, 1);
          mainPanel.scrollRectToVisible(r);
        }
      }
      public void actorRemoved(AnimationEvent e) {
        if (processModelEvents == false)
          processModelEvents = true;
        else {
          for (int i=1;i<actorViewers.size();i++) {
            if (((ActorViewer)actorViewers.get(i)).actor == e.getActor()) {
              removeActorViewer((ActorViewer)actorViewers.get(i));
              break;
            }
          }
//          lblActorPanel.paintActors(actorViewers.size());
          lblActorPanel.paintActors();
          lblActorPanel.repaint();
          Graphics2D gi2 = scoreImage.createGraphics();
          gi2.setColor(gi2.getBackground());
          gi2.fillRect(0,
            actorViewers.size()*(ACTOR_VIEWER_HEIGHT+ACTOR_SPACE),
            scoreImage.getWidth(),
            ACTOR_VIEWER_HEIGHT+ACTOR_SPACE);
          mainPanel.drawBufferedScoreImage();
          mainPanel.repaint();
          mainPanel.setPreferredSize(new Dimension(getMainPanelZoomedWidth(), actorViewers.size()*(ACTOR_VIEWER_HEIGHT+ACTOR_SPACE)));
          lblActorPanel.setPreferredSize(new Dimension(lblActorWidth, actorViewers.size()*(ACTOR_VIEWER_HEIGHT+ACTOR_SPACE)));
          mainPanel.revalidate();
          lblActorPanel.revalidate();
        }
      }

      public void timeChanged(CursorEvent e) {
        if (processModelEvents == false)
          processModelEvents = true;
        else {
          int offsetY = scoreScrollPane.getViewport().getViewPosition().y;
          columnView.repaint();
          mainPanel.repaint(
            getZoomed(columnView.getFrameStartTime(e.getTime()))-columnView.increment/2,
            offsetY,
            columnView.increment,
            getSize().height-scrollPaneY-columnView.SIZE);

          // For measuring the perfomance.
/*          if (animation.isPlayThreadActive()) {
            if (animation.getCursorTime() == 1)
              startTime = System.currentTimeMillis();
            if (animation.getCursorTime() == animation.getMaxFrameTime())
              fpsLabel.setText(
                ((double)animation.getMaxFrameTime()/
                ((double)(System.currentTimeMillis()-startTime)/1000))
                +" fps");
          }*/
        }
      }
    };

    // Animation view listener and its method (playThreadStarted,playThreadStopped, loopChanged, fpsChanged).
    animationViewListener = new AnimationViewListener() {
      public void playThreadStarted(PlayThreadEvent e) {
        if (processViewEvents == false)
          processViewEvents = true;
        else {
          startButton.setSelected(true);
          stopButton.setSelected(false);
        }
      }
      public void playThreadStopped(PlayThreadEvent e) {
        if (processViewEvents == false)
          processViewEvents = true;
        else {
          stopButton.setSelected(true);
          startButton.setSelected(false);
        }
      }

      public void loopChanged(LoopEvent e) {
        if (processViewEvents == false)
          processViewEvents = true;
        else {
          if (e.getLooped())
            loopButton.setSelected(true);
          else
            loopButton.setSelected(false);
        }
      }

      public void fpsChanged(FpsEvent e) {
        if (processViewEvents == false) {
          processViewEvents = true;
        }
        else {
         fpsList.setSelectedItem(Integer.toString(e.getFps()));
        }
      }
    };

    mainPanel.setLayout(null);
    mainPanelZoomedWidth = animationEditorWidth-scrollPaneX-lblActorWidth-viewportGap;
    mainPanelRealWidth = getReal(getMainPanelZoomedWidth());
    mainPanel.setPreferredSize(new Dimension(getMainPanelZoomedWidth(), getSize().height-scrollPaneY-columnView.SIZE));
//    mainPanel.setBackground(Color.black);
    MouseInputHandler mouseInputHandler = new MouseInputHandler(this);
    mainPanel.addMouseListener(mouseInputHandler);
    mainPanel.addMouseMotionListener(mouseInputHandler);

    MouseListener popupListener = new PopupListener(this);
    mainPanel.addMouseListener(popupListener);

    // Create the column header.
    columnView = new Rule(Rule.HORIZONTAL, true, this);
    columnView.setPreferredWidth(animationEditorWidth-scrollPaneX-lblActorWidth);
    columnView.addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseMoved(MouseEvent e) {
        int mouseX = e.getX();
        if (mouseX % columnView.getIncrement() != 0)
          mouseX -= mouseX % columnView.getIncrement();
        mouseMarkXStart = mouseX;
        mouseMarkXEnd = -1;
        columnView.repaint();

        if (animation != null) {
          String toolTipText = null;
          if (e.getY() > 14) {
            for (int i=0;i<animation.getFrameLabels().size();i++) {
              if (((FrameLabel)animation.getFrameLabels().get(i)).getFrame() == columnView.getFrame(getReal(mouseX))) {
                 toolTipText =
                  "<html><font face=Helvetica size=2>"
                  +"<B>"+"Frame: "+"</B>"
                  +((FrameLabel)animation.getFrameLabels().get(i)).getFrame()+"<P>"
                  +"<B>"+"Label: "+"</B>"
                  +((FrameLabel)animation.getFrameLabels().get(i)).getLabel()
                  +"</font>";
                break;
              }
            }
            columnView.setToolTipText(toolTipText);
          }
        }
      }
    });
    columnView.addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        showFlag = false;
        if (animation != null)
        // Check if play thread is active.
        if (!animation.isPlayThreadActive()) {
          if ((e.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) {
            if (e.getY() > 14) {
              animation.setCursorTime(columnView.getFrame(getReal(e.getX())));
            }
          }
          else if ((e.getModifiers() & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK) {
            if (e.getY() > 14) {
              mousePressedX = e.getX();
              if (mousePressedX % columnView.increment != 0)
                mousePressedX -= mousePressedX % columnView.increment;
              popup.removeAll();
              showFlag = true;
              menuItemAddFrameLabel.setText(resources.getString("addFrameLabel")+columnView.getFrame(getReal(mousePressedX)));
              menuItemRemoveFrameLabel.setText(resources.getString("removeFrameLabel")+columnView.getFrame(getReal(mousePressedX)));
              menuItemEditFrameLabel.setText(resources.getString("editFrameLabel")+columnView.getFrame(getReal(mousePressedX)));
              if (animation.isFrameLabel(columnView.getFrame(getReal(mousePressedX)))) {
                popup.add(menuItemEditFrameLabel);
                popup.add(menuItemRemoveFrameLabel);
              }
              else
                popup.add(menuItemAddFrameLabel);
            }
          }
        }
      }
      public void mouseReleased(MouseEvent e) {
        if (showFlag == true)
          if (e.isPopupTrigger())
            popup.show(e.getComponent(), e.getX(), e.getY());
      }
    });
    menuItemAddFrameLabel.addActionListener(new ActionListener () {
      public void actionPerformed(ActionEvent e) {
        FrameDialog frameDialog = new FrameDialog(
          (Frame)SwingUtilities.getAncestorOfClass(Frame.class,AnimationEditor.this),
          AnimationEditor.this,
          columnView.getFrame(getReal(mousePressedX)),
          true);
        frameDialog.setLocationRelativeTo(AnimationEditor.this);
        frameDialog.show();
      }
    });
    menuItemRemoveFrameLabel.addActionListener(new ActionListener () {
      public void actionPerformed(ActionEvent e) {
        animation.getFrameLabels().remove(
          animation.indexOfFrame(columnView.getFrame(getReal(mousePressedX))));
        columnView.repaint();
      }
    });
    menuItemEditFrameLabel.addActionListener(new ActionListener () {
      public void actionPerformed(ActionEvent e) {
        FrameDialog frameDialog = new FrameDialog(
          (Frame)SwingUtilities.getAncestorOfClass(Frame.class,AnimationEditor.this),
          AnimationEditor.this,
          columnView.getFrame(getReal(mousePressedX)),
          false);
        frameDialog.setLocationRelativeTo(AnimationEditor.this);
        frameDialog.show();
      }
    });

    lblActorPanel.setPreferredSize(new Dimension(lblActorWidth, animationEditorHeight-scrollPaneY-Rule.SIZE));
//    lblActorPanel.setBackground(Color.black);

    scoreScrollPane = new JScrollPane(mainPanel);
    scoreScrollPane.setColumnHeaderView(columnView);
    scoreScrollPane.setRowHeaderView(lblActorPanel);
//    scoreScrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, new Corner(1));
//    scoreScrollPane.setCorner(JScrollPane.UPPER_RIGHT_CORNER, new Corner(2));
//    scoreScrollPane.setCorner(JScrollPane.LOWER_LEFT_CORNER, new Corner(3));
//    scoreScrollPane.setCorner(JScrollPane.LOWER_RIGHT_CORNER, new Corner(4));
    scoreScrollPane.getViewport().addChangeListener(new ChangeListener() {
      public void stateChanged (ChangeEvent e) {
        if (scoreImage != null) {
          mainPanel.drawBufferedScoreImage();
          mainPanel.repaint();
        }
        if (actorImage != null) {
//          lblActorPanel.paintActors(actorViewers.size());
          lblActorPanel.paintActors();
          lblActorPanel.repaint();
        }
      }
    });

    timeCursorColumnView = new TimeCursor(this,1);
    timeCursorMainPanel = new TimeCursor(this,2);

    columnView.setLayout(null);
    columnView.add(timeCursorColumnView);
    timeCursorColumnView.addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseDragged(MouseEvent e) {
        processModelEvents = false;
        int mouseX = timeCursorColumnView.getX() + e.getX();
        int offsetY = scoreScrollPane.getViewport().getViewPosition().y;
        if (mouseX < 0)
          mouseX = 0;
        if (mouseX > getMainPanelZoomedWidth())
          mouseX = getMainPanelZoomedWidth();
        if ((e.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) {
          if (mouseX % columnView.getIncrement() != 0)
            mouseX -= mouseX % columnView.getIncrement();
          animation.setCursorTime(columnView.getFrame(getReal(mouseX)));
          mouseMarkXStart = mouseX;
          mouseMarkXEnd = -1;
          columnView.repaint();
          mainPanel.repaint(
            getZoomed(columnView.getFrameStartTime(animation.getCursorTime())),
            offsetY,
            1,
            getSize().height-scrollPaneY-columnView.SIZE);
        }
      }
      public void mouseMoved(MouseEvent e) {
        timeCursorColumnView.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
      }
    });
    mainPanel.add(timeCursorMainPanel);

/*    JButton btnActor = new JButton("Actor");
    btnActor.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (animation != null)
          synchronized (actorViewers) {
            Actor actor = new Actor(animation);
            processModelEvents = false;
            animation.addActor(actor);
            Rectangle2D.Double actorShape = new Rectangle2D.Double();
            ActorViewer actorViewer = new ActorViewer(AnimationEditor.this, actor, actorShape);
            addActorViewer(actorViewer);

            actorViewer.actorViewerZoomedWidth = getSize().width-scrollPaneX-lblActorWidth-viewportGap;
            actorViewer.actorViewerRealWidth = getReal(actorViewer.getActorViewerZoomedWidth());
            mainPanel.setPreferredSize(new Dimension(getMainPanelZoomedWidth(), actorViewers.size()*(actorViewerHeight+actorSpace)));
            lblActorPanel.setPreferredSize(new Dimension(lblActorWidth, actorViewers.size()*(actorViewerHeight+actorSpace)));
            lblActorPanel.revalidate();
            lblActorPanel.paintActors(actorViewers.size());
            lblActorPanel.repaint();
              mainPanel.paintScoreImage(
                actorViewers.indexOf(actorViewer),
                actorViewers.indexOf(actorViewer),
                0,
                actorViewer.getActorViewerRealWidth());
              mainPanel.repaint();
            mainPanel.revalidate();
            Rectangle r = new Rectangle(0, actorViewers.size()*(actorViewerHeight+actorSpace), 1, 1);
            mainPanel.scrollRectToVisible(r);
          }
        }
    });*/

    String[] zoomStrings = { "100", "200", "300" };
    zoomList = new JComboBox(zoomStrings);
    zoomList.setOpaque(false);
    zoomList.setPreferredSize(new Dimension(60,26));
    zoomList.setSelectedIndex(1);
    zoomList.setEditable(true);
    zoomList.setToolTipText(resources.getString("zoom"));
    zoomList.setPreferredSize(new Dimension(zoomList.getPreferredSize().width, 24));
    zoomList.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (editMilestoneViewer != null)
          exitMilestoneEditMode();
        int width=getWidth()-scrollPaneX-lblActorWidth-viewportGap;
        JComboBox cb = (JComboBox)e.getSource();
        zoom = Integer.decode((String)cb.getSelectedItem()).intValue();
        if (zoom % 25 != 0) {
          zoom -= zoom % 25;
          cb.setSelectedItem(Integer.toString(zoom));
        }
        if (zoom < 50) {
          zoom=50;
          cb.setSelectedItem(Integer.toString(zoom));
        }
        if (zoom > 300) {
          zoom=300;
          cb.setSelectedItem(Integer.toString(zoom));
        }
        columnView.setIncrementAndUnits(zoom);
        int useWidth;
        if (width >= getZoomed(actorViewerMaxWidth))
          useWidth = getReal(width);
        else
          useWidth = actorViewerMaxWidth;
        BaseActorViewer tempActorViewer;
        for (int i=0;i<actorViewers.size();i++) {
          if (i==0)
            tempActorViewer = (SoundActorViewer)actorViewers.get(i);
          else
            tempActorViewer = (ActorViewer)actorViewers.get(i);
          tempActorViewer.actorViewerRealWidth = useWidth;
          tempActorViewer.actorViewerZoomedWidth = getZoomed(useWidth);
        }
        mainPanelRealWidth = useWidth;
        mainPanelZoomedWidth = getZoomed(useWidth);

        mainPanel.setPreferredSize(new Dimension(mainPanelZoomedWidth,
         actorViewers.size()*(ACTOR_VIEWER_HEIGHT+ACTOR_SPACE)));
        mainPanel.revalidate();

        columnView.setPreferredWidth(mainPanelZoomedWidth);
        columnView.revalidate();

        if (animation != null) {
          if (animation.getCursorTime() < 1)
            animation.setCursorTime(1);
          if (columnView.getFrameEndTime(animation.getCursorTime()) > getMainPanelRealWidth())
            animation.setCursorTime(columnView.getFrame(getMainPanelRealWidth()-getReal(columnView.getIncrement())));
        }

        mainPanel.drawBufferedScoreImage();
        mainPanel.repaint();
        columnView.repaint();
      }
    });

    String[] fpsStrings = { "25", "30", "60" };
    fpsList = new JComboBox(fpsStrings);
    fpsList.setOpaque(false);
    fpsList.setPreferredSize(new Dimension(60,26));
    fpsList.setSelectedIndex(1);
    fpsList.setEditable(true);
    fpsList.setToolTipText(resources.getString("fps"));
    fpsList.setPreferredSize(new Dimension(fpsList.getPreferredSize().width, 24));
    fpsList.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (animation != null) {
          if (processViewEvents == false)
            processViewEvents = true;
          else {
            if (editMilestoneViewer != null)
              exitMilestoneEditMode();
            processViewEvents = true;
            JComboBox cb = (JComboBox)e.getSource();
            int fps = Integer.decode((String)cb.getSelectedItem()).intValue();
            if (fps <= 0) {
              fps=1;
              cb.setSelectedItem(Integer.toString(fps));
            }
            processViewEvents = false;
            animation.setFps(fps);
          }
        }
      }
    });

    loopButton = new JCheckBox(resources.getString("loopLabel"));
    loopButton.setOpaque(false);
    loopButton.setMnemonic(KeyEvent.VK_L);
    loopButton.setSelected(true);
//    loopButton.setBackground(Color.gray);
//    loopButton.setForeground(Color.white);
    loopButton.setToolTipText(resources.getString("loop"));
    loopButton.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (animation != null) {
          if (processViewEvents == false)
            processViewEvents = true;
          else {
            if (editMilestoneViewer != null)
              exitMilestoneEditMode();
              processViewEvents = false;
            if (e.getStateChange() == ItemEvent.SELECTED)
              animation.setLoopedPlayback(true);
            else if (e.getStateChange() == ItemEvent.DESELECTED)
              animation.setLoopedPlayback(false);
          }
        }
      }
    });

    add(scoreScrollPane, new ExplicitConstraints(scoreScrollPane,
          ContainerEF.left(this).add(scrollPaneX),
          ContainerEF.top(this).add(scrollPaneY),
          ContainerEF.right(this).subtract(scrollPaneX),
          ContainerEF.bottom(this).subtract(scrollPaneY),
          0.0, 0.0, true, true));
/*    add(btnActor, new ExplicitConstraints(btnActor,
          ContainerEF.left(),
          ContainerEF.top(),
          null, null, 0.0, 0.0, true, true));*/
    add(zoomList, new ExplicitConstraints(zoomList,
//          ComponentEF.right(btnActor).add(HGAP),
          ContainerEF.left(this).add(HGAP/2),
          ContainerEF.top(this).add(2),
          null, null, 0.0, 0.0, true, true));
    add(buttonRow, new ExplicitConstraints(buttonRow,
          ComponentEF.right(zoomList).add(HGAP),
          ContainerEF.top(this).add(2),
          null, null, 0.0, 0.0, true, true));
    add(loopButton, new ExplicitConstraints(loopButton,
          ComponentEF.right(buttonRow).add(HGAP),
          ContainerEF.top(this).add(2),
          null, null, 0.0, 0.0, true, true));
    add(fpsList, new ExplicitConstraints(fpsList,
          ComponentEF.right(loopButton).add(HGAP),
          ContainerEF.top(this).add(2),
          null, null, 0.0, 0.0, true, true));

/*    fpsLabel = new JLabel("0 fps");
    fpsLabel.setForeground(Color.green);
    fpsLabel.setBorder(BorderFactory.createCompoundBorder(
         BorderFactory.createLineBorder(Color.green),
         BorderFactory.createEmptyBorder(3,3,3,3)
    ));
    add(fpsLabel, new ExplicitConstraints(fpsLabel,
          ComponentEF.right(fpsList).add(HGAP*2),
          ContainerEF.top().add(3),
          null, null, 0.0, 0.0, true, true));*/

    addComponentListener(new ComponentAdapter(){
      public void componentResized(ComponentEvent e) {
        ResizeThread rt = new ResizeThread(AnimationEditor.this);
        resizeThreads.add(rt);
        rt.start();
      }
    });
  }

  /**
   * Returns the component's E-Slate handle.
   * @return	The requested handle. If the component's constructor has not
   *		been called, this method returns null.
   */
  public ESlateHandle getESlateHandle()
  {
    if (handle == null) {
      initESlate();
    }
    return handle;
  }

  /**
   * Initializes the E-Slate functionality of the component.
   */
  private void initESlate()
  {
    handle = ESlate.registerPart(this);
    handle.setInfo(getInfo());
	try {
		handle.setUniqueComponentName(resources.getString("name"));
	} catch (RenamingForbiddenException e) {
		e.printStackTrace();
	}
    SharedObjectListener listener = new SharedObjectListener () {
      public void handleSharedObjectEvent (SharedObjectEvent e) {
System.out.println("Handle Animation Viewer Shared Object Event");
        AnimationSO newAnimationSO = (AnimationSO)(e.getSharedObject());
        animation = newAnimationSO.getAnimation();
        animation2AnimationEditor();
//        lblActorPanel.paintActors(actorViewers.size());
        lblActorPanel.paintActors();
        lblActorPanel.repaint();
        mainPanel.drawBufferedScoreImage();
        mainPanel.repaint();
      }
    };
    // Construct a SingleInputPlug in order to import the animation.
    try {
      Plug plug = new SingleInputPlug(
        handle, resources, "animationPlug", new Color(0,255,100),
        AnimationSO.class, listener);
      handle.addPlug(plug);
      plug.addConnectionListener(new ConnectionListener() {
        public void handleConnectionEvent(ConnectionEvent e) {
          animation = (Animation)e.getPlug().getHandle().getComponent();
          animation2AnimationEditor();
          zoomList.setSelectedItem(Integer.toString(zoom));

          processViewEvents = false;
          if (animation.isLoopedPlayback())
            loopButton.setSelected(true);
          else
            loopButton.setSelected(false);
          processViewEvents = false;
          fpsList.setSelectedItem(Integer.toString(animation.getFps()));

          // When occurs connection of a new actor, the paint is accomplished
          // through actorAdded at animationListener.
          // When occurs connection of an actor because of loading of a
          // microworld, the paint is accomplished through stateChanged at
          // changeListener of scrollPane.

          lblActorPanel.paintFixedActors();

          // Check if plug connection among Animation and components has been
          // completed.
          if (actorViewers.size() > 1) {
// Base Change
            if (((Actor)((ActorViewer)actorViewers.get(1)).actor).getActorInterface() != null) {
//          lblActorPanel.paintActors(actorViewers.size());
              lblActorPanel.paintActors();
              lblActorPanel.repaint();
            }
          }

/*          mainPanel.drawBufferedScoreImage();
          mainPanel.repaint();
          columnView.repaint();*/
          animation.addAnimationModelListener(animationModelListener);
          animation.addAnimationViewListener(animationViewListener);
        }
      });
      plug.addDisconnectionListener(new DisconnectionListener() {
        public void handleDisconnectionEvent(DisconnectionEvent e) {
          int width=getSize().width-scrollPaneX-lblActorWidth-viewportGap;
          int height=getSize().height-scrollPaneY-columnView.SIZE;
          // Remove all animation viewer's listeners from animation
          BaseActorViewer tempActorViewer;
          BaseSegmentViewer tempSegmentViewer;
          BaseMilestoneViewer tempMilestoneViewer;
          for (int i=0;i<actorViewers.size();i++) {
            if (i==0)
              tempActorViewer = (SoundActorViewer)actorViewers.get(i);
            else
              tempActorViewer = (ActorViewer)actorViewers.get(i);
            for (int j=0;j<tempActorViewer.segmentViewers.size();j++) {
              if (i==0)
                tempSegmentViewer = (SoundSegmentViewer)tempActorViewer.segmentViewers.get(j);
              else
                tempSegmentViewer = (SegmentViewer)tempActorViewer.segmentViewers.get(j);
              for (int k=0;k<tempSegmentViewer.milestoneViewers.size();k++) {
                if (i==0)
                  tempMilestoneViewer = (SoundMilestoneViewer)tempSegmentViewer.milestoneViewers.get(k);
                else
                  tempMilestoneViewer = (MilestoneViewer)tempSegmentViewer.milestoneViewers.get(k);
                tempMilestoneViewer.milestone.removeMilestoneListener(tempMilestoneViewer.milestoneListener);
              }
              tempSegmentViewer.segment.removeSegmentListener(tempSegmentViewer.segmentListener);
            }
            tempActorViewer.actor.removeActorListener(tempActorViewer.actorListener);
          }
          animation.removeAnimationModelListener(animationModelListener);
          animation.removeAnimationViewListener(animationViewListener);
          animation = null;
          actorViewers.clear();
          actorViewerMaxWidth = 0;

          mainPanelZoomedWidth = width;
          mainPanelRealWidth = getReal(width);
          mainPanel.setPreferredSize(new Dimension(mainPanelZoomedWidth, actorViewers.size()*(ACTOR_VIEWER_HEIGHT+ACTOR_SPACE)));
          columnView.setPreferredWidth(mainPanelZoomedWidth);
          columnView.revalidate();
          mainPanel.revalidate();
          lblActorPanel.setPreferredSize(new Dimension(lblActorWidth, actorViewers.size()*(ACTOR_VIEWER_HEIGHT+ACTOR_SPACE)));
          lblActorPanel.revalidate();
//          lblActorPanel.paintActors(actorViewers.size());
          lblActorPanel.paintActors();
          lblActorPanel.repaint();
          Graphics2D gi2 = scoreImage.createGraphics();
          gi2.setColor(gi2.getBackground());
          gi2.fillRect(0, 0,
            scoreImage.getWidth(),
            scoreImage.getHeight());
          mainPanel.repaint();
        }
      });
    } catch (InvalidPlugParametersException e) {
    } catch (PlugExistsException e) {
    }
  }

  /**
   * Gets zoomed coordinate.
   * @param   time   Real coordinate.
   * @return  The zoomed coordinate.
   */
  public int getZoomed(int time) {
    return (int)(time * ((double)zoom/100));
  }

  /**
   * Gets real coordinate.
   * @param   time   Zoomed coordinate.
   * @return  The real coordinate.
   */
  public int getReal(int time) {
    return (int)(time * ((double)100/zoom));
  }

  /**
   * Sets main panel's zoomed width.
   * @param   newMainPanelZoomedWidth   Main panel's zoomed width.
   */
  public void setMainPanelZoomedWidth(int newMainPanelZoomedWidth) {
    this.mainPanelZoomedWidth = newMainPanelZoomedWidth;
  }

  /**
   * Gets main panel's zoomed width.
   * @return  The main panel's zoomed width.
   */
  public int getMainPanelZoomedWidth() {
    return mainPanelZoomedWidth;
  }

  /**
   * Sets main panel's real width.
   * @param   newMainPanelRealWidth   Main panel's real width.
   */
  public void setMainPanelRealWidth(int newMainPanelRealWidth) {
    this.mainPanelRealWidth = newMainPanelRealWidth;
  }

  /**
   * Gets main panel's real width.
   * @return  The main panel's real width.
   */
  public int getMainPanelRealWidth() {
    return mainPanelRealWidth;
  }

  /**
   * Gets animation.
   * @return	animation   The animation to return.
   */
  public Animation getAnimation() {
    return animation;
  }

  /**
   * Sets the animation.
   * @param	animation   The sequnece to set.
   */
  public void setAnimation(Animation animation) {
    this.animation = animation;
  }

  /**
   * Add an actor viewer.
   * @param	actorViewer   The actor viewer to add.
   */
  public void addActorViewer(BaseActorViewer actorViewer) {
    synchronized (actorViewers) {
      if (!actorViewers.contains(actorViewer)) {
        actorViewers.add(actorViewer);
      }
    }
  }

  /**
   * Remove an actor viewer.
   * @param   actorViewer   The actor viewer to remove.
   */
  public void removeActorViewer(BaseActorViewer actorViewer) {
    // Remove actor's listeners from animation
    BaseSegmentViewer tempSegmentViewer;
    BaseMilestoneViewer tempMilestoneViewer;
    for (int j=0;j<actorViewer.segmentViewers.size();j++) {
      if (SoundActorViewer.class.isAssignableFrom(actorViewer.getClass()))
        tempSegmentViewer = (SoundSegmentViewer)actorViewer.segmentViewers.get(j);
      else
        tempSegmentViewer = (SegmentViewer)actorViewer.segmentViewers.get(j);
      for (int k=0;k<tempSegmentViewer.milestoneViewers.size();k++) {
        if (SoundActorViewer.class.isAssignableFrom(actorViewer.getClass()))
          tempMilestoneViewer = (SoundMilestoneViewer)tempSegmentViewer.milestoneViewers.get(k);
        else
          tempMilestoneViewer = (MilestoneViewer)tempSegmentViewer.milestoneViewers.get(k);
        tempMilestoneViewer.milestone.removeMilestoneListener(tempMilestoneViewer.milestoneListener);
      }
      tempSegmentViewer.segment.removeSegmentListener(tempSegmentViewer.segmentListener);
    }
    actorViewer.actor.removeActorListener(actorViewer.actorListener);
    synchronized (actorViewers) {
      int ind = actorViewers.indexOf(actorViewer);
      if (ind >= 0) {
        actorViewers.remove(actorViewer);
      }
    }
  }

  /**
   * Resize animation viewer.
   */
  public void resizeAnimationEditor() {
    int width=getSize().width-scrollPaneX-lblActorWidth-viewportGap;
    int height=getSize().height-scrollPaneY-columnView.SIZE;

    int useWidth;
    if (width >= getZoomed(actorViewerMaxWidth))
      useWidth = getReal(width);
    else
      useWidth = actorViewerMaxWidth;
    BaseActorViewer tempActorViewer;
    for (int i=0;i<actorViewers.size();i++) {
      if (i==0)
        tempActorViewer = (SoundActorViewer)actorViewers.get(i);
      else
        tempActorViewer = (ActorViewer)actorViewers.get(i);
      tempActorViewer.actorViewerRealWidth = useWidth;
      tempActorViewer.actorViewerZoomedWidth = getZoomed(useWidth);
    }
    mainPanelRealWidth = useWidth;
    mainPanelZoomedWidth = getZoomed(useWidth);

    mainPanel.setPreferredSize(new Dimension(mainPanelZoomedWidth, actorViewers.size()*(ACTOR_VIEWER_HEIGHT+ACTOR_SPACE)));
    columnView.setPreferredWidth(mainPanelZoomedWidth);

    if (width<=0)
      width = 1;
    if (height<=0)
      height = 1;
    if (scoreImage != null) {
      if ((scoreImage.getWidth() != width) || (scoreImage.getHeight() != height)) {
        scoreImage = new BufferedImage(
          width,
          height,
          BufferedImage.TYPE_INT_ARGB);
        mainPanel.drawBufferedScoreImage();
        mainPanel.repaint();
      }
    }
    else {
      scoreImage = new BufferedImage(
        width,
        height,
        BufferedImage.TYPE_INT_ARGB);
      mainPanel.drawBufferedScoreImage();
      mainPanel.repaint();
    }

    if (actorImage != null) {
      if (actorImage.getHeight() != height) {
        actorImage = new BufferedImage(
          lblActorWidth,
          height,
          BufferedImage.TYPE_INT_ARGB);
//        lblActorPanel.paintActors(actorViewers.size());
        lblActorPanel.paintActors();
        lblActorPanel.repaint();
      }
    }
    else {
      actorImage = new BufferedImage(
        lblActorWidth,
        height,
        BufferedImage.TYPE_INT_ARGB);
//      lblActorPanel.paintActors(actorViewers.size());
      lblActorPanel.paintActors();
      lblActorPanel.repaint();
    }

    if (animation != null) {
      if (animation.getCursorTime() < 1)
        animation.setCursorTime(1);
      if (columnView.getFrameEndTime(animation.getCursorTime()) > mainPanelRealWidth)
        animation.setCursorTime(columnView.getFrame(mainPanelRealWidth-getReal(columnView.increment)));
    }
    columnView.revalidate();
    mainPanel.revalidate();
  }

  /**
   * Compute actor viewer max width.
   */
  public void computeActorViewerMaxWidth() {
    actorViewerMaxWidth = 0;
    BaseActorViewer tempActorViewer;
    BaseSegmentViewer tempSegmentViewer;
    for (int i=0;i<actorViewers.size();i++) {
      if (i==0)
        tempActorViewer = (SoundActorViewer)actorViewers.get(i);
      else
        tempActorViewer = (ActorViewer)actorViewers.get(i);
      for (int j=0;j<tempActorViewer.segmentViewers.size();j++) {
        if (i==0)
          tempSegmentViewer = (SoundSegmentViewer)tempActorViewer.segmentViewers.get(j);
        else
          tempSegmentViewer = (SegmentViewer)tempActorViewer.segmentViewers.get(j);
        if (actorViewerMaxWidth < columnView.getFrameEndTime(tempSegmentViewer.segment.getEnd()))
          actorViewerMaxWidth = columnView.getFrameEndTime(tempSegmentViewer.segment.getEnd());
      }
    }
  }

  /**
   * Exit milestone edit mode.
   */
  public void exitMilestoneEditMode() {
    mainPanel.paintScoreImage(
      actorViewers.indexOf(editMilestoneViewer.segmentViewer.actorViewer),
      actorViewers.indexOf(editMilestoneViewer.segmentViewer.actorViewer),
      0,
      editMilestoneViewer.segmentViewer.actorViewer.actorViewerRealWidth);
    mainPanel.repaint(
      0,
      actorViewers.indexOf(editMilestoneViewer.segmentViewer.actorViewer)*(ACTOR_VIEWER_HEIGHT+ACTOR_SPACE),
      editMilestoneViewer.segmentViewer.actorViewer.actorViewerZoomedWidth,
      ACTOR_VIEWER_HEIGHT);

    // Recompute the segment's variables change step.
// Base Change
    ((Segment)editMilestoneViewer.segmentViewer.segment).betweenMilestones = false;

    IntBaseArray aniValues = new IntBaseArray();
// Base change
    Actor tempActor = (Actor)editMilestoneViewer.segmentViewer.segment.getActor();
    for (int j=0;j<tempActor.getAniVarCount();j++) {
      int newAniValue = tempActor.getActorInterface().getVarValues().get(
        tempActor.getActorInterface().getAnimatedPropertyStructure().indexOfAnimatedPropertyDescriptor(
        tempActor.getAniPropertyIDs().get(j)));
      aniValues.add(newAniValue);
    }
// Base change
    ((Milestone)editMilestoneViewer.milestone).setAniVarValues(aniValues);

    editMilestoneViewer = null;
  }

  /**
   * Transform a animation to animation editor.
   */
  public void animation2AnimationEditor() {
    int width=getSize().width-scrollPaneX-lblActorWidth-viewportGap;
    int height=getSize().height-scrollPaneY-columnView.SIZE;
    synchronized (actorViewers) {
      actorViewers.clear();
      BaseActor tempActor;
      BaseActorViewer actorViewer;
      BaseSegmentViewer segmentViewer;
      BaseMilestoneViewer milestoneViewer;
      for (int i=0;i<animation.getActors().size();i++) {
        Rectangle2D.Double actorShape = new Rectangle2D.Double();
        if (i==0) {
          tempActor = (SoundActor)animation.getActors().get(i);
          actorViewer = new SoundActorViewer(this, tempActor, actorShape);
          }
        else {
          tempActor = (Actor)animation.getActors().get(i);
          actorViewer = new ActorViewer(this, tempActor, actorShape);
        }
        actorViewers.add(actorViewer);
// Base change
        BaseSegment iteratorSegment = (tempActor).getStartSegment();
        while (iteratorSegment != null) {
          Rectangle2D.Double segmentShape = new Rectangle2D.Double();
          if (i==0)
            segmentViewer = new SoundSegmentViewer(actorViewer, iteratorSegment, segmentShape);
          else
            segmentViewer = new SegmentViewer(actorViewer, iteratorSegment, segmentShape);
          actorViewer.segmentViewers.add(segmentViewer);
// Base change
          BaseMilestone iteratorMilestone = iteratorSegment.getStartMilestone();
          while (iteratorMilestone != null) {
            Ellipse2D.Double milestoneShape = new Ellipse2D.Double();
          if (i==0)
            milestoneViewer = new SoundMilestoneViewer(segmentViewer, iteratorMilestone, milestoneShape);
          else
            milestoneViewer = new MilestoneViewer(segmentViewer, iteratorMilestone, milestoneShape);
            segmentViewer.milestoneViewers.add(milestoneViewer);
            iteratorMilestone = iteratorMilestone.getNext();
          }
          iteratorSegment = iteratorSegment.getNext();
        }
      }
      computeActorViewerMaxWidth();
      int widthSet;
      if (actorViewerMaxWidth < getReal(width))
        widthSet = getReal(width);
      else
        widthSet = actorViewerMaxWidth;
      BaseActorViewer tempActorViewer;
      for (int k=0;k<actorViewers.size();k++) {
        if (k==0)
          tempActorViewer = (SoundActorViewer)actorViewers.get(k);
        else
          tempActorViewer = (ActorViewer)actorViewers.get(k);
        tempActorViewer.actorViewerRealWidth = widthSet;
        tempActorViewer.actorViewerZoomedWidth = getZoomed(widthSet);
      }
      mainPanelZoomedWidth = getZoomed(widthSet);
      mainPanelRealWidth = widthSet;
      mainPanel.setPreferredSize(new Dimension(mainPanelZoomedWidth, actorViewers.size()*(ACTOR_VIEWER_HEIGHT+ACTOR_SPACE)));
      columnView.setPreferredWidth(mainPanelZoomedWidth);
      columnView.revalidate();
      mainPanel.revalidate();
      lblActorPanel.setPreferredSize(new Dimension(lblActorWidth, actorViewers.size()*(ACTOR_VIEWER_HEIGHT+ACTOR_SPACE)));
      lblActorPanel.revalidate();
      if (width<=0)
        width = 1;
      if (height<=0)
        height = 1;
      if (scoreImage == null)
        scoreImage = new BufferedImage(
          width,
          height,
          BufferedImage.TYPE_INT_ARGB);
      if (actorImage == null)
        actorImage = new BufferedImage(
          lblActorWidth,
          height,
          BufferedImage.TYPE_INT_ARGB);
    }
  }

  /**
   * Insert sound file.
   * @param soundSegment  The sound segment to add sound file to.
   */
  public void chooseSoundFile(SoundSegment soundSegment) {
    final JFileChooser fc = new JFileChooser();
    fc.addChoosableFileFilter(new SoundFilter(this));
    int returnVal = fc.showOpenDialog(this);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      File file = fc.getSelectedFile();
      soundSegment.setSoundFile(file);
    }
/*    else
      soundSegment.setSoundFile(null);*/
  }

  /**
   * Save the component's state.
   * @param	oo	The stream where the state should be saved.
   * @throws    IOException
   */
  public void writeExternal(ObjectOutput oo) throws IOException
  {
    ESlateFieldMap2 map = new ESlateFieldMap2(storageVersion);

//    map.put(ANIMATION, animation);
    map.put(ZOOM, zoom);

    oo.writeObject(map);
  }

  /**
   * Load the component's state.
   * @param	oi	The stream from where the state should be loaded.
   * @throws    IOException
   * @throws    ClassNotFoundException
   */
  public void readExternal(ObjectInput oi)
    throws IOException, ClassNotFoundException
  {
    StorageStructure map = (StorageStructure)(oi.readObject());

//    animation = (Animation)map.get(ANIMATION);
    zoom = map.get(ZOOM, 0);
    columnView.setIncrementAndUnits(zoom);

/*    if (animation != null) {
      animation2AnimationEditor();
      zoomList.setSelectedItem(Integer.toString(zoom));
    }*/
  }

  /**
   * Run the component as an application--useful for debugging.
   * @param	args	Application's arguments (not used).
   */
  public static void main (String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

	JFrame frame = new JFrame("Score");
    frame.addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
            System.exit(0);
        }
    });
    frame.setContentPane(new AnimationEditor());
    frame.pack();

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = frame.getSize();
    int x = (screenSize.width - frameSize.width) / 2;
    int y = (screenSize.height - frameSize.height) / 2;
    frame.setLocation(x, y);

    frame.setVisible(true);
 }
}
;