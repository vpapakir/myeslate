package gr.cti.eslate.database;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.Border;
//1.0.1 import javax.swing.basic.BasicComboBoxRenderer; //BasicListCellRenderer;
import java.awt.Component;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import java.util.ResourceBundle;
import java.awt.FontMetrics;
import java.awt.Font;


//1.0.1 class FieldTypeCellRenderer extends BasicComboBoxRenderer { //BasicListCellRenderer  {
class FontTypeCellRenderer extends DefaultListCellRenderer { //JLabel implements ListCellRenderer { //BasicListCellRenderer  {
    JComboBox combobox;
    Color selectedCellBackground; // = Color.pink;
    Color selectedCellForeground; // = Color.black;
    final static Color defaultCellBackground = Color.white;
    final static Color defaultCellForeground = Color.black;

    public FontTypeCellRenderer(JComboBox x, Color selectionForeColor, Color selectionBackColor) {
        this.combobox = x;
        selectedCellBackground = selectionBackColor;
        selectedCellForeground = selectionForeColor;
        setOpaque(true);
    }

/*    public Component getListCellRendererComponent(JList listbox, Object value, int index,
    boolean isSelected, boolean cellHasFocus) {
        if(isSelected) {
            setBackground(selectedCellBackground);
            setForeground(selectedCellForeground);
        } else {
            setBackground(defaultCellBackground);
            setForeground(defaultCellForeground);
        }
        setText((String) value);
        return this;
    }
*/
}