package gr.cti.eslate.base.container;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyEditorSupport;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;
// 1.2
//import java.awt.GraphicsEnvironment;


public class FontPropertyEditor extends PropertyEditorSupport {
    Font font;
    PropertyChangeSupport pcs;
    ResourceBundle fontEditorBundle;
    NoBorderButton fontButton;


    public FontPropertyEditor() {
        super();
        pcs = new PropertyChangeSupport(this);
        fontEditorBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.FontPropertyEditorBundle", Locale.getDefault());
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }


    public void setValue(Object value) {
        if (!Font.class.isInstance(value))
            return;
        font = (Font) value;
//        ((JButton) getCustomEditor()).setBackground(color);
        if (fontButton != null)
            fontButton.setText(font2String(font));
    }

    public Object getValue() {
        return font;
    }

    public java.awt.Component getCustomEditor() {
        fontButton = new NoBorderButton();
        Dimension buttonSize = new Dimension(120, 22);
        fontButton.setPreferredSize(buttonSize);
        fontButton.setMaximumSize(buttonSize);
        fontButton.setMinimumSize(buttonSize);
//        button.setText("Edit font");
        fontButton.setText(font2String(font));
        fontButton.setMargin(new Insets(0, 0, 0, 0));
        fontButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
/*                Font oldFont = font;
                FontEditorDialog fontEditorDialog = new FontEditorDialog(font);
                fontEditorDialog.showDialog((JButton) e.getSource());
*/
                Font oldFont = font;
                String[] sizes = new String[] {"3", "5", "7", "8", "9", "10", "12", "14",
                                            "16", "18", "20", "24", "28", "36", "48", "72"};
                String[] fontNames;
                if (System.getProperty("java.version").startsWith("1.1"))
                    fontNames = Toolkit.getDefaultToolkit().getFontList();
                else
                    fontNames = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

                Frame topFrame = (Frame) javax.swing.SwingUtilities.getAncestorOfClass(Frame.class, fontButton);
                FontEditorDialog2 fontEditorDialog = new FontEditorDialog2(topFrame,
                                                          fontNames,
                                                          sizes,
                                                          font,
                                                          FontPropertyEditor.this);
//                fontEditorDialog.show();
                ESlateContainerUtils.showDialog(fontEditorDialog, topFrame, false);
                if (fontEditorDialog.m_option == JOptionPane.OK_OPTION) {
                    Font newFont = fontEditorDialog.getFont();
                    updateFont(newFont);
                }
            }
        });
        return fontButton;
    }

    public void updateFont(Font newFont) {
        Font oldFont = font;
        if (!newFont.equals(oldFont)) {
            font = newFont;
            fontButton.setText(font2String(font));
            pcs.firePropertyChange("Font", oldFont, font);
        }
    }

    public String getAsText() {
        return null;
    }

    public boolean supportsCustomEditor() {
        return true;
    }

    public String font2String(Font font) {
        StringBuffer sb = new StringBuffer();
        sb.append(font.getFamily());
        sb.append(", ");

        int style = font.getStyle();
        String styleStr = "";
        if (font.isItalic()) {
            if (font.isBold())
                styleStr = fontEditorBundle.getString("Bold") + ", " + fontEditorBundle.getString("Italic");
            else
                styleStr = fontEditorBundle.getString("Italic");
        }else{
            if (font.isBold())
                styleStr = fontEditorBundle.getString("Bold");
        }

        if (styleStr.length() != 0) {
            sb.append(styleStr);
            sb.append(", ");
        }
        sb.append(font.getSize());

        return sb.toString();
    }
}




