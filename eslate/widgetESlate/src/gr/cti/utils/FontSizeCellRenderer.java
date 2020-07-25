package gr.cti.utils;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;

import javax.swing.*;
import javax.swing.border.Border;

public class FontSizeCellRenderer extends JLabel
                           implements ListCellRenderer
{
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
  
    public FontSizeCellRenderer(FontSizeComboBox x, Color selectionForeColor, Color selectionBackColor)
    {
        combobox = x;
        selectedCellBackground = selectionBackColor;
        selectedCellForeground = selectionForeColor;
        setOpaque(true);
//        normalComboBoxBorder = combobox.getBorder();
    }

    public Component getListCellRendererComponent(JList listbox, Object value, int index, boolean isSelected, boolean cellHasFocus)
    {
        if(!combobox.isEnabled())
        {
            setText("");
            setIcon(null);
        }
        else
        {
//            if(normalComboBoxBorder!=null && !normalComboBoxBorder.equals(combobox.getBorder())) combobox.setBorder(normalComboBoxBorder); //does Java do short-circuil if evaluation? (else if normalComboBox==null a NullPointerException may be thrown)
            if(isSelected)
            {
                setBackground(selectedCellBackground);
                setForeground(selectedCellForeground);
            }
            else
            {
                setBackground(defaultCellBackground);
                setForeground(defaultCellForeground);
            }

         try{
          typeFont = combobox.getCustomerFont();
          typeFont = new Font(typeFont.getName(),typeFont.getStyle(),Integer.parseInt((String)value));          //System.out.println(typeFont);
          setFont(typeFont);

          Graphics2D g2 = (Graphics2D)getGraphics();
          FontRenderContext frc = g2.getFontRenderContext();
          String s = (String)value;
          LineMetrics lm = typeFont.getLineMetrics(s, frc);

          int w = typeFont.getStringBounds(s, frc).getBounds().width;
          int h = (int)Math.round(lm.getHeight());
          setPreferredSize(new Dimension(w, h));
          g2.dispose();

          setText(s);
         }catch(Exception e){}
        }
        return this;
    }

    FontSizeComboBox combobox;
    Color selectionColor;
    Border normalComboBoxBorder;
    Color selectedCellBackground;
    Color selectedCellForeground;
    static final Color defaultCellBackground=Color.white;
    static final Color defaultCellForeground=Color.black;
    Font typeFont;
}
