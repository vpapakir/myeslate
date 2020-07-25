package gr.cti.eslate.base.container.internalFrame;

import gr.cti.eslate.base.effect.ClippingEffect;
import gr.cti.eslate.base.effect.CompositeEffect;
import gr.cti.eslate.base.effect.EffectInterface;
import gr.cti.eslate.base.effect.IntersectionEffect;
import gr.cti.eslate.utils.NoBorderButton;

import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyEditorSupport;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

/**
 * Property editor for components effect property.
 *
 * @author Augustine Grillakis
 * @version 1.0.0, 31-Jul-2002
 */
public class EffectsPropertyEditor extends PropertyEditorSupport {
    private PropertyChangeSupport pcs;
    protected String propertyName;
    private EffectInterface effect;
    private JPanel editor;
    private JComboBox cb;
    private JButton editButton;
    private static final int GAP = 4;

    /**
     * Localized resources.
     */
    static ResourceBundle resources = ResourceBundle.getBundle(
      "gr.cti.eslate.base.container.internalFrame.ESlateInternalFrameResourceBundle", Locale.getDefault());

    public EffectsPropertyEditor() {
        propertyName = "Effect";
        pcs = new PropertyChangeSupport(this);
        cb = new JComboBox(new String[] {
            resources.getString("noneEffect"),
            resources.getString("alphaCompositeEffect"),
            resources.getString("clippingEffect"),
            resources.getString("intersectionEffect")
        });
        cb.setEditable(false);
        cb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionevent) {
                comboAction();
            }
        });
        editButton = new NoBorderButton(resources.getString("edit"));
        editButton.setMargin(new Insets(0, 0, 0, 0));
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionevent) {
                buttonAction();
            }
        });
        editor = new JPanel();
        editor.setLayout(new BoxLayout(editor, 0));
        editor.add(cb);
        editor.add(Box.createHorizontalStrut(4));
        editor.add(editButton);
        showEditButton(false);
    }

    private void comboAction() {
        int i = cb.getSelectedIndex();
        if (effect == null && i == 0)
            return;
        if (effect != null) {
            if (CompositeEffect.class.isAssignableFrom(effect.getClass()) && i == 1)
                return;
            if (IntersectionEffect.class.isAssignableFrom(effect.getClass()) && i == 3)
                return;
            if (ClippingEffect.class.isAssignableFrom(effect.getClass()) && i == 2)
                return;
        }

        if (i == 0)
            setValue(null);
        else if (i == 1)
            setValue(new CompositeEffect(15, false, false));
        else if (i == 2)
            setValue(new ClippingEffect(40, false, false));
        else if (i == 3)
            setValue(new IntersectionEffect(40, IntersectionEffect.HEIGHT_INCREASE, false, false));
    }

    private void buttonAction() {
        if(effect != null) {
          EffectsDialog effectsDialog = new EffectsDialog(editButton, effect);
          effectsDialog.show();
          if (effectsDialog.getReturnCode() == EffectsDialog.DIALOG_OK)
              setValue(effect);
        }
    }

    public void setValue(EffectInterface effect) {
        EffectInterface prevEffect = this.effect;
        this.effect = effect;
        if (effect == null) {
            cb.setSelectedIndex(0);
            showEditButton(false);
        }else if (CompositeEffect.class.isAssignableFrom(effect.getClass())) {
            cb.setSelectedIndex(1);
            showEditButton(true);
        }else if (IntersectionEffect.class.isAssignableFrom(effect.getClass())) {
            cb.setSelectedIndex(3);
            showEditButton(true);
        }else if (ClippingEffect.class.isAssignableFrom(effect.getClass())) {
            cb.setSelectedIndex(2);
            showEditButton(true);
        }else{
            cb.setSelectedIndex(0);
            showEditButton(false);
        }
        pcs.firePropertyChange(propertyName, null, effect);
    }

    private void showEditButton(boolean flag) {
        editor.removeAll();
        editor.add(cb);
        if(flag) {
            editor.add(Box.createHorizontalStrut(4));
            editor.add(editButton);
        }
        editor.revalidate();
    }

    public Object getValue() {
        return effect;
    }

    public Component getCustomEditor() {
        return editor;
    }

    public boolean supportsCustomEditor() {
        return true;
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener propertychangelistener) {
        pcs.addPropertyChangeListener(propertychangelistener);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener propertychangelistener) {
        pcs.removePropertyChangeListener(propertychangelistener);
    }
}