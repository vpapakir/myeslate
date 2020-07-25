package gr.cti.eslate.database;

import gr.cti.eslate.database.engine.*;

import javax.swing.*;
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
class FieldTypeCellRenderer extends JLabel implements ListCellRenderer { //BasicListCellRenderer  {
    JComboBox combobox;
    DBTable table;
    Color selectionColor;
//    static ImageIcon inactiveBoxIcon = null;

    Color selectedCellBackground; // = Color.pink;
    Color selectedCellForeground; // = Color.black;
    Color defaultCellBackground = UIManager.getColor("ComboBox.background");
    Color defaultCellForeground = Color.black;
    boolean opaqueWhenDisabled = true;
    Font typeFont;
    FontMetrics typeFontMetrics;
    transient ResourceBundle infoBundle;

    public FieldTypeCellRenderer(JComboBox x, DBTable tabl, Color selectionForeColor, Color selectionBackColor, ResourceBundle infBundle) {
        this.combobox = x;
        this.table = tabl;
        selectedCellBackground = selectionBackColor;
        selectedCellForeground = selectionForeColor;
        setOpaque(true);
/*        if (inactiveBoxIcon == null) {
//            Toolkit tk = Toolkit.getDefaultToolkit();
            inactiveBoxIcon = new ImageIcon(getClass().getResource("images/inactiveComboBoxIcon.gif"));
        }
*/
        this.infoBundle = infBundle;
        typeFontMetrics = getFontMetrics(getFont());
    }

    public Component getListCellRendererComponent(
          JList listbox,
          Object value,
          int index,
          boolean isSelected,
          boolean cellHasFocus)
    {
        if (!combobox.isEnabled() && opaqueWhenDisabled) {
    	    //setBackground(Color.lightGray);
            setText(null);
            setIcon(null); //inactiveBoxIcon);
        }else{

//	    }else{
        	if(isSelected) {
	            setBackground(selectedCellBackground);
        	    setForeground(selectedCellForeground);
        	} else {
	            setBackground(defaultCellBackground);
        	    setForeground(defaultCellForeground);
        	}

            Class selectedType = (Class) value;

//System.out.println("value: " + value + ", infoBundle.getString(\"Date\"): " + infoBundle.getString("Date") + ", infoBundle.getString(\"Time\"): " + infoBundle.getString("Time"));
	        if (DoubleTableField.DATA_TYPE.equals(selectedType)) {
	            setIcon(Database.getDoubleTypeIcon());
                    String s = infoBundle.getString("Number");
                    setText(s);
	            setIconTextGap(92-typeFontMetrics.stringWidth(s)); //45
            }else if (FloatTableField.DATA_TYPE.equals(selectedType)) {
                    setIcon(Database.getFloatTypeIcon());
                    String s = infoBundle.getString("Float");
                    setText(s);
                    setIconTextGap(92-typeFontMetrics.stringWidth(s)); //45
            }else if (IntegerTableField.DATA_TYPE.equals(selectedType)) {
                    setIcon(Database.getIntegerTypeIcon());
                    String s = infoBundle.getString("Integer");
                    setText(s);
                    setIconTextGap(92-typeFontMetrics.stringWidth(s)); //45
	        }else if (StringTableField.DATA_TYPE.equals(selectedType)) {
//	            setIconTextGap(16);
                    String s = infoBundle.getString("Alphanumeric");
                    setText(s);
	            setIconTextGap(92-typeFontMetrics.stringWidth(s)); //45
	            setIcon(Database.getStringTypeIcon());
	        }else if (BooleanTableField.DATA_TYPE.equals(selectedType)) {
//	            setIconTextGap(4);
                    String s = infoBundle.getString("Boolean (Yes/No)");
                    setText(s);
	            setIconTextGap(92-typeFontMetrics.stringWidth(s)); //45
	            setIcon(Database.getBooleanTypeIcon());
	        }else if (DateTableField.DATA_TYPE.equals(selectedType)) {
                    String s = infoBundle.getString("Date");
                    setText(s);
	            setIcon(Database.getDateTypeIcon());
	            setIconTextGap(92-typeFontMetrics.stringWidth(s)); //45
//	            setIconTextGap(80);
	        }else if (URLTableField.DATA_TYPE.equals(selectedType)) {
                    String s = infoBundle.getString("URL");
                    setText(s);
	            setIcon(Database.getURLTypeIcon());
	            setIconTextGap(92-typeFontMetrics.stringWidth(s)); //45
//	            setIconTextGap(66);
	        }else if (TimeTableField.DATA_TYPE.equals(selectedType)) {
                    String s = infoBundle.getString("Time");
	            setIcon(Database.getTimeTypeIcon());
                    setText(s);
	            setIconTextGap(92-typeFontMetrics.stringWidth(s)); //45
//	            setIconTextGap(81);
	        }else if (ImageTableField.DATA_TYPE.equals(selectedType)) {
                    String s = infoBundle.getString("Image");
                    setText(s);
	            setIcon(Database.getImageTypeIcon());
	            setIconTextGap(92-typeFontMetrics.stringWidth(s)); //45
//	            setIconTextGap(67);
	        }

//            setText((String) value);
//            int gap = 40;
/*	        if (value.equals(infoBundle.getString("Number"))) {
	            setIcon(Database.doubleTypeIcon);
	            setIconTextGap(92-typeFontMetrics.stringWidth(infoBundle.getString("Number"))); //45
	        }else if (value.equals(infoBundle.getString("Alphanumeric"))) {
//	            setIconTextGap(16);
	            setIconTextGap(92-typeFontMetrics.stringWidth(infoBundle.getString("Alphanumeric"))); //45
	            setIcon(Database.stringTypeIcon);
	        }else if (value.equals(infoBundle.getString("Boolean (Yes/No)"))) {
//	            setIconTextGap(4);
	            setIconTextGap(92-typeFontMetrics.stringWidth(infoBundle.getString("Boolean (Yes/No)"))); //45
	            setIcon(Database.booleanTypeIcon);
	        }else if (value.equals(infoBundle.getString("Date"))) {
	            setIcon(Database.dateTypeIcon);
	            setIconTextGap(92-typeFontMetrics.stringWidth(infoBundle.getString("Date"))); //45
//	            setIconTextGap(80);
	        }else if (value.equals(infoBundle.getString("URL"))) {
	            setIcon(Database.urlTypeIcon);
	            setIconTextGap(92-typeFontMetrics.stringWidth(infoBundle.getString("URL"))); //45
//	            setIconTextGap(66);
	        }else if (value.equals(infoBundle.getString("Time"))) {
	            setIcon(Database.timeTypeIcon);
	            setIconTextGap(92-typeFontMetrics.stringWidth(infoBundle.getString("Time"))); //45
//	            setIconTextGap(81);
	        }else if (value.equals(infoBundle.getString("Image"))) {
	            setIcon(Database.imageTypeIcon);
	            setIconTextGap(92-typeFontMetrics.stringWidth(infoBundle.getString("Image"))); //45
//	            setIconTextGap(67);
	        }
*/
        }

	    setHorizontalTextPosition(LEFT);
	    setOpaque(true);
	    setMaximumSize(new Dimension(140, 25));
	    setMinimumSize(new Dimension(140, 25));
	    setPreferredSize(new Dimension(140, 25));
	    return this;
    }

    protected void setOpaqueWhenDisabled(boolean opaque) {
        opaqueWhenDisabled = opaque;
    }

    protected boolean isOpaqueWhenDisabled() {
        return opaqueWhenDisabled;
    }

    public void updateUI() {
        super.updateUI();
        defaultCellBackground = UIManager.getColor("ComboBox.background");
    }
}
