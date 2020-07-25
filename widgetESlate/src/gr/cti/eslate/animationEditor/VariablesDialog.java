package gr.cti.eslate.animationEditor;

import gr.cti.eslate.animation.Actor;
import gr.cti.eslate.animation.BaseMilestone;
import gr.cti.eslate.animation.BaseSegment;
import gr.cti.eslate.animation.Milestone;
import gr.cti.eslate.protocol.AnimatedPropertyDescriptor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.zookitec.layout.ComponentEF;
import com.zookitec.layout.ContainerEF;
import com.zookitec.layout.ExplicitConstraints;
import com.zookitec.layout.ExplicitLayout;
import com.zookitec.layout.MathEF;

/**
 * This class implements a dialog for edit actor's variables.
 * @author	Augustine Grillakis
 * @version	1.0.0, 16-Apr-2002
 */
// * @see		gr.cti.eslate.animationEditor.ActorPanel
class VariablesDialog extends JDialog {
  private static final int HGAP = 10;
  private static final int VGAP = 10;
  int variablesDialogWidth = 420;
  int variablesDialogHeight = 300;
  DefaultListModel fieldListModel, animatedFieldListModel;
  JButton okButton, cancelButton, addOneButton, removeOneButton, addAllButton, removeAllButton;
  JList fieldList, animatedFieldList;

  AnimationEditor animationEditor;
  Actor actor;

  /**
   * Create a milestone dialog.
   * @param frame            Parent frame.
   * @param newActor         The actor whose variables to edit.
   * @param aniEditor        The animation viewer of the actor to edit.
   */
  public VariablesDialog(Frame frame, Actor newActor, AnimationEditor aniEditor) {
    super(frame, true);
    this.animationEditor = aniEditor;
    this.actor = newActor;

    setTitle(animationEditor.resources.getString("editVariables")
      +actor.getActorInterface().getActorName()
      +" "
      +actor.getAnimationSession().getPlugID());
//    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setResizable(false);
    setSize(new Dimension(variablesDialogWidth,variablesDialogHeight));

    fieldListModel = new DefaultListModel();
    fieldList = new JList(fieldListModel);
    fieldList.setCellRenderer(new CustomCellRenderer());
    fieldList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    animatedFieldListModel = new DefaultListModel();
    animatedFieldList = new JList(animatedFieldListModel);
    animatedFieldList.setCellRenderer(new CustomAnimatedCellRenderer());
    animatedFieldList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    // Fill the "fieldListModel" and "animatedFieldListModel".
    for (int i=0; i<actor.getVarCount(); i++) {
      if (!actor.getAnimationSession().isAnimated(((AnimatedPropertyDescriptor)actor.getActorInterface().getAnimatedPropertyStructure().getAnimatedPropertyDescriptors().get(i)).getPropertyID()))
        fieldListModel.addElement(((AnimatedPropertyDescriptor)actor.getActorInterface().getAnimatedPropertyStructure().getAnimatedPropertyDescriptors().get(i)).getPropertyName());
      else
        animatedFieldListModel.addElement(((AnimatedPropertyDescriptor)actor.getActorInterface().getAnimatedPropertyStructure().getAnimatedPropertyDescriptors().get(i)).getPropertyName());
    }

    fieldList.addListSelectionListener(new ListSelectionListener() {
        public void valueChanged(ListSelectionEvent e) {
            if (fieldList.isSelectionEmpty() || (animatedFieldListModel.size()==actor.getVarCount())
              || !isThereAnyVariable()) {
                addOneButton.setEnabled(false);
                addAllButton.setEnabled(false);
            }
            else {
                addAllButton.setEnabled(true);
                int index = fieldList.getSelectedIndex();
                if (((JLabel)fieldList.getCellRenderer().getListCellRendererComponent(
                  fieldList,
                  fieldList.getModel().getElementAt(index),
                  index,
                  true,
                  true
                )).isEnabled())
                  addOneButton.setEnabled(true);
            }
        }
    });
    fieldList.addMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
              int index = fieldList.locationToIndex(e.getPoint());

              if (((JLabel)fieldList.getCellRenderer().getListCellRendererComponent(
                fieldList,
                fieldList.getModel().getElementAt(index),
                index,
                true,
                true
              )).isEnabled()) {

                removeOneButton.setEnabled(true);
                removeAllButton.setEnabled(true);
                String fieldName = (String) fieldList.getSelectedValue();

                fieldListModel.removeElement(fieldName);
                fieldList.repaint();
                animatedFieldListModel.insertElementAt(fieldName, getAnimatedPosition(fieldName));

                if (!fieldListModel.isEmpty()) {
                    if (fieldListModel.size() == index)
                        fieldList.setSelectedIndex(index-1);
                    else
                        fieldList.setSelectedIndex(index);
                }

                animatedFieldList.setSelectedValue(fieldName, true);

                if (fieldListModel.isEmpty() || (animatedFieldListModel.size() == actor.getVarCount())
                  || !isThereAnyVariable()) {
                    addOneButton.setEnabled(false);
                    addAllButton.setEnabled(false);
                }
              }
            }
        }
    });

    JScrollPane fieldScrollPane = new JScrollPane(fieldList);
    fieldScrollPane.setPreferredSize(new Dimension(170,200));
    fieldScrollPane.setMaximumSize(new Dimension(170,200));
    fieldScrollPane.setMinimumSize(new Dimension(170,200));
    TitledBorder tb1 = new TitledBorder(animationEditor.resources.getString("variables"));
    fieldScrollPane.setBorder(tb1);

    addOneButton = new JButton(new ImageIcon(getClass().getResource("images/addOne1.gif")));
    addOneButton.setPressedIcon(new ImageIcon(getClass().getResource("images/addOne2.gif")));
    addOneButton.setRolloverEnabled(true);
    addOneButton.setRolloverIcon(new ImageIcon(getClass().getResource("images/addOne3.gif")));
    addOneButton.setEnabled(false);
    addOneButton.setBorder((Border)null);
    addOneButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            int index = fieldList.getSelectedIndex();
          if (((JLabel)fieldList.getCellRenderer().getListCellRendererComponent(
            fieldList,
            fieldList.getModel().getElementAt(index),
            index,
            true,
            true
          )).isEnabled()) {
            String fieldName = (String) fieldList.getSelectedValue();
            removeOneButton.setEnabled(true);
            removeAllButton.setEnabled(true);

            fieldListModel.removeElement(fieldName);
            fieldList.repaint();
            animatedFieldListModel.insertElementAt(fieldName, getAnimatedPosition(fieldName));

            if (!fieldListModel.isEmpty()) {
                if (fieldListModel.size() == index)
                    fieldList.setSelectedIndex(index-1);
                else
                    fieldList.setSelectedIndex(index);
            }

            animatedFieldList.setSelectedValue(fieldName, true);

            if (fieldListModel.isEmpty()
              || !isThereAnyVariable()) {
                addOneButton.setEnabled(false);
                addAllButton.setEnabled(false);
            }
          }
        }
    });

    removeOneButton = new JButton(new ImageIcon(getClass().getResource("images/removeOne1.gif")));
    removeOneButton.setPressedIcon(new ImageIcon(getClass().getResource("images/removeOne2.gif")));
    removeOneButton.setRolloverEnabled(true);
    removeOneButton.setRolloverIcon(new ImageIcon(getClass().getResource("images/removeOne3.gif")));
    removeOneButton.setEnabled(false);
    removeOneButton.setBorder((Border)null);
    removeOneButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (animatedFieldListModel.size() == 1) {
                removeOneButton.setEnabled(false);
                removeAllButton.setEnabled(false);
            }
            addOneButton.setEnabled(true);
            addAllButton.setEnabled(true);

            String fieldName = (String) animatedFieldList.getSelectedValue();

            int index  = animatedFieldList.getSelectedIndex();

            animatedFieldListModel.removeElement(fieldName);
            animatedFieldList.repaint();
            fieldListModel.insertElementAt(fieldName, getPosition(fieldName));

            if (!animatedFieldListModel.isEmpty()) {
                if (animatedFieldListModel.size() == index)
                    animatedFieldList.setSelectedIndex(index-1);
                else
                    animatedFieldList.setSelectedIndex(index);
            }

            fieldList.setSelectedValue(fieldName, true);
        }
    });

    addAllButton = new JButton(new ImageIcon(getClass().getResource("images/addAll1.gif")));
    addAllButton.setPressedIcon(new ImageIcon(getClass().getResource("images/addAll2.gif")));
    addAllButton.setRolloverEnabled(true);
    addAllButton.setRolloverIcon(new ImageIcon(getClass().getResource("images/addAll3.gif")));
    if (fieldListModel.isEmpty()
      || !isThereAnyVariable())
        addAllButton.setEnabled(false);
    else
        addAllButton.setEnabled(true);
    addAllButton.setBorder((Border)null);
    addAllButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            boolean foundFlag = false;
            for (int i=0; i<fieldListModel.size(); i++) {
              if (((JLabel)fieldList.getCellRenderer().getListCellRendererComponent(
                fieldList,
                fieldList.getModel().getElementAt(i),
                i,
                true,
                true
              )).isEnabled()) {
                foundFlag = true;
                animatedFieldListModel.insertElementAt(
                  (String)fieldList.getModel().getElementAt(i),
                  getAnimatedPosition((String)fieldList.getModel().getElementAt(i)));
                fieldListModel.removeElementAt(i);
                i--;
              }
            }
            if (foundFlag) {
              fieldList.repaint();
              animatedFieldList.repaint();
              removeAllButton.setEnabled(true);
              addOneButton.setEnabled(false);
              addAllButton.setEnabled(false);
            }
        }
    });

    removeAllButton = new JButton(new ImageIcon(getClass().getResource("images/removeAll1.gif")));
    removeAllButton.setPressedIcon(new ImageIcon(getClass().getResource("images/removeAll2.gif")));
    removeAllButton.setRolloverEnabled(true);
    removeAllButton.setRolloverIcon(new ImageIcon(getClass().getResource("images/removeAll3.gif")));
    if (animatedFieldListModel.isEmpty())
        removeAllButton.setEnabled(false);
    else
        removeAllButton.setEnabled(true);
    removeAllButton.setBorder((Border)null);
    removeAllButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            removeOneButton.setEnabled(false);
            removeAllButton.setEnabled(false);
            addAllButton.setEnabled(true);

            animatedFieldListModel.removeAllElements();
            animatedFieldList.repaint();
            fieldListModel.removeAllElements();
            fieldList.repaint();
            for (int i=0; i<actor.getVarCount(); i++) {
              fieldListModel.addElement(((AnimatedPropertyDescriptor)actor.getActorInterface().getAnimatedPropertyStructure().getAnimatedPropertyDescriptors().get(i)).getPropertyName());
            }
        }
    });

    animatedFieldList.addListSelectionListener(new ListSelectionListener() {
        public void valueChanged(ListSelectionEvent e) {
            if (animatedFieldList.isSelectionEmpty()) {
                removeOneButton.setEnabled(false);
                removeAllButton.setEnabled(false);
            }
            else {
                removeOneButton.setEnabled(true);
                removeAllButton.setEnabled(true);
            }
        }
    });
    animatedFieldList.addMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                int index = animatedFieldList.locationToIndex(e.getPoint());

                if (animatedFieldListModel.size() == 1) {
                    removeOneButton.setEnabled(false);
                    removeAllButton.setEnabled(false);
                }
                addOneButton.setEnabled(true);
                addAllButton.setEnabled(true);

                String fieldName = (String) animatedFieldList.getSelectedValue();
                animatedFieldListModel.removeElement(fieldName);
                animatedFieldList.repaint();
                fieldListModel.insertElementAt(fieldName, getPosition(fieldName));

                if (!animatedFieldListModel.isEmpty()) {
                    if (animatedFieldListModel.size() == index)
                        animatedFieldList.setSelectedIndex(index-1);
                    else
                        animatedFieldList.setSelectedIndex(index);
                }

                fieldList.setSelectedValue(fieldName, true);
            }
        }
    });

    JScrollPane animatedFieldScrollPane = new JScrollPane(animatedFieldList);
    animatedFieldScrollPane.setPreferredSize(new Dimension(170,200));
    animatedFieldScrollPane.setMaximumSize(new Dimension(170,200));
    animatedFieldScrollPane.setMinimumSize(new Dimension(170,200));
    TitledBorder tb2 = new TitledBorder(animationEditor.resources.getString("animatedVariables"));
    animatedFieldScrollPane.setBorder(tb2);

    getContentPane().setLayout(new ExplicitLayout());
    getContentPane().add(fieldScrollPane, new ExplicitConstraints(fieldScrollPane,
                          ContainerEF.left(getContentPane()).add(HGAP),
                          ContainerEF.top(getContentPane()).add(VGAP),
                          null, null, 0.0, 0.0, true, true));
    getContentPane().add(addOneButton, new ExplicitConstraints(addOneButton,
                          ComponentEF.right(fieldScrollPane).add(HGAP),
                          ContainerEF.top(getContentPane()).add(VGAP*4),
                          null, null, 0.0, 0.0, true, true));
    getContentPane().add(removeOneButton, new ExplicitConstraints(removeOneButton,
                          ComponentEF.right(fieldScrollPane).add(HGAP),
                          ComponentEF.bottom(addOneButton).add(VGAP*2),
                          null, null, 0.0, 0.0, true, true));
    getContentPane().add(addAllButton, new ExplicitConstraints(addAllButton,
                          ComponentEF.right(fieldScrollPane).add(HGAP),
                          ComponentEF.bottom(removeOneButton).add(VGAP*2),
                          null, null, 0.0, 0.0, true, true));
    getContentPane().add(removeAllButton, new ExplicitConstraints(removeAllButton,
                          ComponentEF.right(fieldScrollPane).add(HGAP),
                          ComponentEF.bottom(addAllButton).add(VGAP*2),
                          null, null, 0.0, 0.0, true, true));
    getContentPane().add(animatedFieldScrollPane, new ExplicitConstraints(animatedFieldScrollPane,
                          ComponentEF.right(addOneButton).add(HGAP),
                          ContainerEF.top(getContentPane()).add(VGAP),
                          null, null, 0.0, 0.0, true, true));


    okButton = new JButton(animationEditor.resources.getString("okButton"));
    cancelButton = new JButton(animationEditor.resources.getString("cancelButton"));

    getContentPane().add(okButton, new ExplicitConstraints(okButton,
                          ContainerEF.left(getContentPane()).add(215),
                          ComponentEF.bottom(fieldScrollPane).add(VGAP*2),
                          MathEF.max(ComponentEF.preferredWidth(okButton),ComponentEF.preferredWidth(cancelButton)),
                          null, 0.0, 0.0, true, true));
    getContentPane().add(cancelButton, new ExplicitConstraints(cancelButton,
                          ComponentEF.right(okButton).add(HGAP*2),
                          ComponentEF.bottom(fieldScrollPane).add(VGAP*2),
                          MathEF.max(ComponentEF.preferredWidth(okButton),ComponentEF.preferredWidth(cancelButton)),
                          null, 0.0, 0.0, true, true));

    okButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e){
        boolean value;
        for (int i=0;i<actor.getVarCount();i++) {
          value = false;
          for (int j=0;j<animatedFieldListModel.size();j++) {
            if (((AnimatedPropertyDescriptor)actor.getActorInterface().getAnimatedPropertyStructure().getAnimatedPropertyDescriptors().get(i)).getPropertyName() == (String)animatedFieldListModel.get(j))
              value = true;
          }
          if ((actor.getAnimationSession().isAnimated(((AnimatedPropertyDescriptor)actor.getActorInterface().getAnimatedPropertyStructure().getAnimatedPropertyDescriptors().get(i)).getPropertyID()))
            && (value == false)) {
              actor.getAnimationSession().setAnimated(((AnimatedPropertyDescriptor)actor.getActorInterface().getAnimatedPropertyStructure().getAnimatedPropertyDescriptors().get(i)).getPropertyID(), false);
// Base change
              BaseSegment iteratorSegment = actor.getStartSegment();
              BaseMilestone iteratorMilestone;
              while (iteratorSegment != null) {
                iteratorMilestone = iteratorSegment.getStartMilestone();
                while (iteratorMilestone != null) {
// Base change
                  ((Milestone)iteratorMilestone).getAniVarValues().remove(actor.indexOfAnimatedPropertyID(((AnimatedPropertyDescriptor)actor.getActorInterface().getAnimatedPropertyStructure().getAnimatedPropertyDescriptors().get(i)).getPropertyID()));
                  iteratorMilestone = iteratorMilestone.getNext();
                }
                iteratorSegment = iteratorSegment.getNext();
              }
              actor.getAniPropertyIDs().remove(actor.indexOfAnimatedPropertyID(((AnimatedPropertyDescriptor)actor.getActorInterface().getAnimatedPropertyStructure().getAnimatedPropertyDescriptors().get(i)).getPropertyID()));
              actor.setAniVarCount(actor.getAniVarCount()-1);
/*              IProtocolPlug [] protocolPlugs = ((ProtocolPlug)animationEditor.animation.getESlateHandle().getPlug(animationEditor.animation.getResources().getString("actorPlug"))).getProtocolPlugs();
              for (int l=0;l<protocolPlugs.length;l++) {
                if (actor.getAnimationSession() == animationEditor.animation.getAnimationSessions().get((ProtocolPlug)protocolPlugs[l])) {
                  ((IntBaseArray)animationEditor.animation.getUsedVars().get((ProtocolPlug)protocolPlugs[l])).remove(
                    ((IntBaseArray)animationEditor.animation.getUsedVars().get((ProtocolPlug)protocolPlugs[l])).indexOf(
                      ((AnimatedPropertyDescriptor)actor.getActorInterface().getAnimatedPropertyStructure().getAnimatedPropertyDescriptors().get(i)).getPropertyID()));
                  break;
                }
              }*/
              actor.getActorInterface().getAnimatedPropertyStructure().setAnimated(
                ((AnimatedPropertyDescriptor)actor.getActorInterface().getAnimatedPropertyStructure().getAnimatedPropertyDescriptors().get(i)).getPropertyID(),
                false);
          }
          if ((!actor.getAnimationSession().isAnimated(((AnimatedPropertyDescriptor)actor.getActorInterface().getAnimatedPropertyStructure().getAnimatedPropertyDescriptors().get(i)).getPropertyID()))
            && (value == true)) {
              // Position to insert new property ID/value.
              actor.getAnimationSession().setAnimated(((AnimatedPropertyDescriptor)actor.getActorInterface().getAnimatedPropertyStructure().getAnimatedPropertyDescriptors().get(i)).getPropertyID(), true);
              int position = actor.getAniVarCount();
              for (int k=0;k<actor.getAniVarCount();k++) {
                if (i < actor.getActorInterface().getAnimatedPropertyStructure().indexOfAnimatedPropertyDescriptor(
                 (actor.getAniPropertyIDs().get(k)))) {
                  position = k;
                  break;
                }
              }
              actor.setAniVarCount(actor.getAniVarCount()+1);
// Base change
              BaseSegment iteratorSegment = actor.getStartSegment();
              BaseMilestone iteratorMilestone;
              while (iteratorSegment != null) {
                iteratorMilestone = iteratorSegment.getStartMilestone();
                while (iteratorMilestone != null) {
// Base change
                  ((Milestone)iteratorMilestone).getAniVarValues().add(position,
                    actor.getActorInterface().getVarValues().get(
                    actor.getActorInterface().getAnimatedPropertyStructure().indexOfAnimatedPropertyDescriptor(
                    ((AnimatedPropertyDescriptor)actor.getActorInterface().getAnimatedPropertyStructure().getAnimatedPropertyDescriptors().get(i)).getPropertyID())));
                  iteratorMilestone = iteratorMilestone.getNext();
                }
                iteratorSegment = iteratorSegment.getNext();
              }
              actor.getAniPropertyIDs().add(position, //actor.getActorInterface().getAnimatedPropertyStructure().indexOfAnimatedPropertyDescriptor(
                ((AnimatedPropertyDescriptor)actor.getActorInterface().getAnimatedPropertyStructure().getAnimatedPropertyDescriptors().get(i)).getPropertyID());
/*              IProtocolPlug [] protocolPlugs = ((ProtocolPlug)animationEditor.animation.getESlateHandle().getPlug(animationEditor.animation.getResources().getString("actorPlug"))).getProtocolPlugs();
              for (int l=0;l<protocolPlugs.length;l++) {
                if (actor.getAnimationSession() == animationEditor.animation.getAnimationSessions().get((ProtocolPlug)protocolPlugs[l])) {
                  ((IntBaseArray)animationEditor.animation.getUsedVars().get((ProtocolPlug)protocolPlugs[l])).add(position,
                    ((AnimatedPropertyDescriptor)actor.getActorInterface().getAnimatedPropertyStructure().getAnimatedPropertyDescriptors().get(l)).getPropertyID());
                  break;
                }
              }*/
              actor.getActorInterface().getAnimatedPropertyStructure().setAnimated(
                ((AnimatedPropertyDescriptor)actor.getActorInterface().getAnimatedPropertyStructure().getAnimatedPropertyDescriptors().get(i)).getPropertyID(),
                true);
          }
        }
        animationEditor.lblActorPanel.paintActors();
        dispose();
      }
    });

    cancelButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e){
        dispose();
      }
    });

    // OK handler.
    getRootPane().registerKeyboardAction(new ActionListener() {
                                           public void actionPerformed(ActionEvent e) {
                                             okButton.doClick();
                                           }
                                        },
      KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ENTER, 0),
      JComponent.WHEN_IN_FOCUSED_WINDOW);
    // Esc handler.
    getRootPane().registerKeyboardAction(new ActionListener() {
                                           public void actionPerformed(ActionEvent e) {
                                             cancelButton.doClick();
                                           }
                                        },
      KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0),
      JComponent.WHEN_IN_FOCUSED_WINDOW);

    // Add the window listener.
    addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
            dispose();
        }
    });
  }


  /**
   * Get position in animated list to add variable to.
   * @param fieldName The name of the variable to add.
   * @return  The position in animated list to add to.
   */
  public int getAnimatedPosition(String fieldName) {
    int indexOfSelectedVariable=0;
    for (int i=0;i<actor.getVarCount();i++) {
      if (((AnimatedPropertyDescriptor)actor.getActorInterface().getAnimatedPropertyStructure().getAnimatedPropertyDescriptors().get(i)).getPropertyName() == fieldName) {
        indexOfSelectedVariable = i;
        break;
      }
    }
    int position = animatedFieldListModel.size();
    for (int k=0;k<animatedFieldListModel.size();k++) {
      int indexOfOtherVariable=0;
      for (int i=0;i<actor.getVarCount();i++) {
        if (((AnimatedPropertyDescriptor)actor.getActorInterface().getAnimatedPropertyStructure().getAnimatedPropertyDescriptors().get(i)).getPropertyName() == (String)animatedFieldListModel.get(k)) {
          indexOfOtherVariable = i;
          break;
        }
      }
      if (indexOfSelectedVariable < indexOfOtherVariable) {
        position = k;
        break;
      }
    }
    return position;
  }

  /**
   * Get position in list to add variable to.
   * @param fieldName The name of the variable to add.
   * @return  The position in list to add to.
   */
  public int getPosition(String fieldName) {
    int indexOfSelectedVariable=0;
    for (int i=0;i<actor.getVarCount();i++) {
      if (((AnimatedPropertyDescriptor)actor.getActorInterface().getAnimatedPropertyStructure().getAnimatedPropertyDescriptors().get(i)).getPropertyName() == fieldName) {
        indexOfSelectedVariable = i;
        break;
      }
    }
    int position = fieldListModel.size();
    for (int k=0;k<fieldListModel.size();k++) {
      int indexOfOtherVariable=0;
      for (int i=0;i<actor.getVarCount();i++) {
        if (((AnimatedPropertyDescriptor)actor.getActorInterface().getAnimatedPropertyStructure().getAnimatedPropertyDescriptors().get(i)).getPropertyName() == (String)fieldListModel.get(k)) {
          indexOfOtherVariable = i;
          break;
        }
      }
      if (indexOfSelectedVariable < indexOfOtherVariable) {
        position = k;
        break;
      }
    }
    return position;
  }

  /**
   * This class implements a custom cell renderer.
   */
  class CustomCellRenderer extends JLabel implements ListCellRenderer {

    public CustomCellRenderer() {
      super();
      setOpaque(true);
    }

    // This is the only method defined by ListCellRenderer.
    // We just reconfigure the JLabel each time we're called.
    public Component getListCellRendererComponent(
      JList list,
      Object value,            // value to display
      int index,               // cell index
      boolean isSelected,      // is the cell selected
      boolean cellHasFocus)    // the list and the cell have the focus
    {
      setText(value.toString());
      setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
      setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
//      setBackground(isSelected ? Color.red : Color.white);
//      setForeground(isSelected ? Color.white : Color.black);

      int propertyID = 0;
      for (int i=0;i<actor.getActorInterface().getAnimatedPropertyStructure().getAnimatedPropertyDescriptors().size();i++) {
        if (((AnimatedPropertyDescriptor)actor.getActorInterface().getAnimatedPropertyStructure().getAnimatedPropertyDescriptors().get(i)).getPropertyName() == value.toString()) {
          propertyID = ((AnimatedPropertyDescriptor)actor.getActorInterface().getAnimatedPropertyStructure().getAnimatedPropertyDescriptors().get(i)).getPropertyID();
          break;
        }
      }
      if (!actor.getActorInterface().getAnimatedPropertyStructure().isAnimated(propertyID)
        || actor.getAnimationSession().isAnimated(propertyID))
          setEnabled(true);
      else
        setEnabled(false);

      setFont(list.getFont());
      return this;
    }
  }

  /**
   * This class implements a custom animated cell renderer.
   */
  class CustomAnimatedCellRenderer extends JLabel implements ListCellRenderer {

    public CustomAnimatedCellRenderer() {
      super();
      setOpaque(true);
    }

    // This is the only method defined by ListCellRenderer.
    // We just reconfigure the JLabel each time we're called.
    public Component getListCellRendererComponent(
      JList list,
      Object value,            // value to display
      int index,               // cell index
      boolean isSelected,      // is the cell selected
      boolean cellHasFocus)    // the list and the cell have the focus
    {
      setText(value.toString());
      setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
      setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
//      setBackground(isSelected ? Color.red : Color.white);
//      setForeground(isSelected ? Color.white : Color.black);
      setEnabled(list.isEnabled());
      setFont(list.getFont());
      return this;
    }
  }

  /**
   * Checks if there is available variable.
   * @return True if there is available valriable.
   */
  public boolean isThereAnyVariable() {
    boolean isThere = false;
    for (int i=0;i<fieldListModel.size();i++) {
      if (((JLabel)fieldList.getCellRenderer().getListCellRendererComponent(
        fieldList,
        fieldList.getModel().getElementAt(i),
        i,
        true,
        true
      )).isEnabled()) {
        isThere = true;
        break;
      }
    }
    return isThere;
  }
}
