package gr.cti.eslate.base.container.internalFrame;

import gr.cti.eslate.base.effect.ClippingEffect;
import gr.cti.eslate.base.effect.CompositeEffect;
import gr.cti.eslate.base.effect.EffectInterface;
import gr.cti.eslate.base.effect.IntersectionEffect;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import com.zookitec.layout.ComponentEF;
import com.zookitec.layout.ContainerEF;
import com.zookitec.layout.ExplicitConstraints;
import com.zookitec.layout.ExplicitLayout;
import com.zookitec.layout.MathEF;

/**
 * This class implements a dialog for editing an effect.
 *
 * @author	Augustine Grillakis
 * @version	1.0.0, 31-Jul-2002
 */
class EffectsDialog extends JDialog {
    public static final int DIALOG_OK = 0;
    public static final int DIALOG_CANCEL = 1;
    private static final int HGAP = 15;
    private static final int VGAP = 10;

    private int returnCode = DIALOG_CANCEL;

    EffectInterface effect;
    private int result;

    JCheckBox antiAliasingCheck;
    JCheckBox renderingQualityCheck;
    JButton okButton;
    JButton cancelButton;
    JTextField numberOfStepsTextField;
    JComboBox directionComboBox;

    /**
     * Localized resources.
     */
    static ResourceBundle resources = ResourceBundle.getBundle(
      "gr.cti.eslate.base.container.internalFrame.ESlateInternalFrameResourceBundle", Locale.getDefault());

    /**
     * Create a frame dialog.
     * @param component   The component that opens the dialog.
     * @param effect The effect to edit.
     */
    public EffectsDialog(Component component, EffectInterface effect) {
      super((Frame)SwingUtilities.getAncestorOfClass(java.awt.Frame.class, component), resources.getString("effectsDialogTitle"), true);
      this.effect = effect;

      int effectID = 0;
      if (CompositeEffect.class.isAssignableFrom(effect.getClass()))
          effectID = 1;
      else if (ClippingEffect.class.isAssignableFrom(effect.getClass()))
          effectID = 2;
      else if (IntersectionEffect.class.isAssignableFrom(effect.getClass()))
          effectID = 3;

      if (effectID == 3)
        setSize(new Dimension(240, 220));
      else
        setSize(new Dimension(240, 190));
      setResizable(false);

      getContentPane().setLayout(new ExplicitLayout());

      antiAliasingCheck = new JCheckBox();
      antiAliasingCheck.setText(resources.getString("antiAliasingCheck"));
      getContentPane().add(antiAliasingCheck, new ExplicitConstraints(antiAliasingCheck,
                            ContainerEF.left(getContentPane()).add(HGAP),
                            ContainerEF.top(getContentPane()).add(VGAP),
                            null, null, 0.0, 0.0, true, true));

      renderingQualityCheck = new JCheckBox();
      renderingQualityCheck.setText(resources.getString("renderingQualityCheck"));
      getContentPane().add(renderingQualityCheck, new ExplicitConstraints(renderingQualityCheck,
                            ComponentEF.left(antiAliasingCheck),
                            ComponentEF.bottom(antiAliasingCheck).add(VGAP),
                            null, null, 0.0, 0.0, true, true));

      Component lastComponent = renderingQualityCheck;

      if (effectID == 1 || effectID == 2 || effectID == 3) {
          JLabel numberOfStepsLabel = new JLabel(resources.getString("numberOfSteps"));
          getContentPane().add(numberOfStepsLabel, new ExplicitConstraints(numberOfStepsLabel,
                                ContainerEF.left(getContentPane()).add(HGAP),
                                ComponentEF.bottom(renderingQualityCheck).add(VGAP),
                                null, null, 0.0, 0.0, true, true));
          numberOfStepsTextField = new JTextField();
          getContentPane().add(numberOfStepsTextField, new ExplicitConstraints(numberOfStepsTextField,
                                ComponentEF.right(numberOfStepsLabel).add(HGAP*4),
                                ComponentEF.top(numberOfStepsLabel),
                                MathEF.constant(40), null,
                                0.0, 0.0, true, true));
          lastComponent = numberOfStepsTextField;
      }
      if (effectID == 3) {
        JLabel directionLabel = new JLabel(resources.getString("direction"));
        getContentPane().add(directionLabel, new ExplicitConstraints(directionLabel,
                              ContainerEF.left(getContentPane()).add(HGAP),
                              ComponentEF.bottom(lastComponent).add(VGAP),
                              null, null, 0.0, 0.0, true, true));
        directionComboBox = new JComboBox();
        getContentPane().add(directionComboBox, new ExplicitConstraints(directionComboBox,
                              ComponentEF.right(directionLabel).add(HGAP),
                              ComponentEF.top(directionLabel),
                              null, null, 0.0, 0.0, true, true));
        directionComboBox.addItem(resources.getString("heightDecrease"));
        directionComboBox.addItem(resources.getString("heightIncrease"));
        directionComboBox.addItem(resources.getString("widthDecrease"));
        directionComboBox.addItem(resources.getString("widthIncrease"));
        lastComponent = directionComboBox;
      }

      okButton = new JButton(resources.getString("okButton"));
      cancelButton = new JButton(resources.getString("cancelButton"));

      getContentPane().add(okButton, new ExplicitConstraints(okButton,
                            MathEF.subtract(ContainerEF.xFraction(getContentPane(), 0.5),MathEF.add(MathEF.max(ComponentEF.preferredWidth(okButton),ComponentEF.preferredWidth(cancelButton)),20)),
                            ComponentEF.bottom(lastComponent).add(VGAP*2),
                            MathEF.max(ComponentEF.preferredWidth(okButton),ComponentEF.preferredWidth(cancelButton)),
                            null, 0.0, 0.0, true, true));
      okButton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e){
              if (CompositeEffect.class.isAssignableFrom(EffectsDialog.this.effect.getClass())) {
                  CompositeEffect eff = (CompositeEffect) EffectsDialog.this.effect;
                  eff.setAntiAliased(antiAliasingCheck.isSelected());
                  eff.setRenderingQualityOn(renderingQualityCheck.isSelected());
                  try{
                      eff.setNumberOfSteps(Integer.parseInt(numberOfStepsTextField.getText()));
                  }catch (NumberFormatException exc) {}
              }else if (ClippingEffect.class.isAssignableFrom(EffectsDialog.this.effect.getClass())) {
                  ClippingEffect eff = (ClippingEffect) EffectsDialog.this.effect;
                  eff.setAntiAliased(antiAliasingCheck.isSelected());
                  eff.setRenderingQualityOn(renderingQualityCheck.isSelected());
                  try{
                      eff.setNumberOfSteps(Integer.parseInt(numberOfStepsTextField.getText()));
                  }catch (NumberFormatException exc) {}
              }else if (IntersectionEffect.class.isAssignableFrom(EffectsDialog.this.effect.getClass())) {
                  IntersectionEffect eff = (IntersectionEffect) EffectsDialog.this.effect;
                  eff.setAntiAliased(antiAliasingCheck.isSelected());
                  eff.setRenderingQualityOn(renderingQualityCheck.isSelected());
                  try{
                      eff.setNumberOfSteps(Integer.parseInt(numberOfStepsTextField.getText()));
                  }catch (NumberFormatException exc) {}
                  eff.setDirection(directionComboBox.getSelectedIndex());
              }
              returnCode = DIALOG_OK;
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

      getContentPane().add(cancelButton, new ExplicitConstraints(cancelButton,
                            MathEF.add(ContainerEF.xFraction(getContentPane(), 0.5),20),
                            ComponentEF.top(okButton),
                            MathEF.max(ComponentEF.preferredWidth(okButton),ComponentEF.preferredWidth(cancelButton)),
                            null, 0.0, 0.0, true, true));
      cancelButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e){
          dispose();
        }
      });
      // Esc handler.
      getRootPane().registerKeyboardAction(new ActionListener() {
                                             public void actionPerformed(ActionEvent e) {
                                               cancelButton.doClick();
                                             }
                                          },
        KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0),
        JComponent.WHEN_IN_FOCUSED_WINDOW);

      center(component);
      addWindowListener(new WindowAdapter() {
          public void windowClosing(WindowEvent e) {
              dispose();
          }
          public void windowActivated(WindowEvent windowevent)
          {
              if(!gotFocus)
              {
                  okButton.requestFocus();
                  JRootPane jrootpane = SwingUtilities.getRootPane(okButton);
                  if(jrootpane != null)
                      jrootpane.setDefaultButton(okButton);
                  gotFocus = true;
              }
          }
          boolean gotFocus;
          {
              gotFocus = false;
          }
      });

      // Initialization
      if (CompositeEffect.class.isAssignableFrom(effect.getClass())) {
          CompositeEffect eff = (CompositeEffect) effect;
          antiAliasingCheck.setSelected(eff.isAntiAliased());
          renderingQualityCheck.setSelected(eff.isRenderingQualityOn());
          numberOfStepsTextField.setText(String.valueOf(eff.getNumberOfSteps()));
      }else if (ClippingEffect.class.isAssignableFrom(effect.getClass())) {
          ClippingEffect eff = (ClippingEffect) effect;
          antiAliasingCheck.setSelected(eff.isAntiAliased());
          renderingQualityCheck.setSelected(eff.isRenderingQualityOn());
          numberOfStepsTextField.setText(String.valueOf(eff.getNumberOfSteps()));
      }else if (IntersectionEffect.class.isAssignableFrom(effect.getClass())) {
          IntersectionEffect eff = (IntersectionEffect) effect;
          antiAliasingCheck.setSelected(eff.isAntiAliased());
          renderingQualityCheck.setSelected(eff.isRenderingQualityOn());
          numberOfStepsTextField.setText(String.valueOf(eff.getNumberOfSteps()));
          directionComboBox.setSelectedIndex(eff.getDirection());
      }

    }

    private void center(Component component) {
        Dimension dimension = getSize();
        int i = dimension.width;
        int j = dimension.height;
        Dimension dimension1 = Toolkit.getDefaultToolkit().getScreenSize();
        int i1 = dimension1.width;
        int j1 = dimension1.height;
        int k;
        int l;
        if(component == null || !component.isVisible())
        {
            k = (i1 - i) / 2;
            l = (j1 - j) / 2;
        } else {
            Dimension dimension2 = component.getSize();
            int k1 = dimension2.width;
            int l1 = dimension2.height;
            Point point = component.getLocationOnScreen();
            k = point.x + (k1 - i) / 2;
            l = point.y + (l1 - j) / 2;
            if(k + i > i1)
                k = i1 - i;
            if(l + j > j1)
                l = j1 - j;
            if(k < 0)
                k = 0;
            if(l < 0)
                l = 0;
        }
        setLocation(k, l);
    }

    public int getReturnCode() {
        return returnCode;
    }
}
