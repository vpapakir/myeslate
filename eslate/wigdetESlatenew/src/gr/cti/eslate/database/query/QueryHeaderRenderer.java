package gr.cti.eslate.database.query;

import javax.swing.*;
import gr.cti.eslate.database.engine.*;
import java.awt.Image;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Component;
import javax.swing.table.TableColumn;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.border.SoftBevelBorder;
import javax.swing.table.TableCellRenderer;


public class QueryHeaderRenderer extends JLabel implements TableCellRenderer {
//1.0.1    public JLabel headerLabel;
    static Font normalHeaderFont = new Font("TimesRoman", Font.PLAIN, 16);
    static Font calcHeaderFont = new Font("Courier", Font.PLAIN, 16);
    static Color calcHeaderColor = new Color(244,9,123);

    protected QueryHeaderRenderer(String fieldName, Class fieldType, boolean notKey, boolean isCalculated, QueryComponent qComp) {
//1.0.1        super(new JLabel(fieldName));
//        System.out.println("In HeaderRenderer(): " + fieldName + ", " + notKey);
//        System.out.println(table.stringIcon.getIconHeight() + ", " + table.stringIcon.getIconWidth());

//1.0.1        headerLabel = (JLabel) getComponent();
//1.0.1        headerLabel.setText("abcdefghi");
        setText(fieldName);
        setOpaque(true);

        if (isCalculated) {
//            System.out.println("Setting calculated field header settings");
            setFont(calcHeaderFont);
            setForeground(calcHeaderColor);
        }else{
            setFont(normalHeaderFont);
            setForeground(Color.black);
        }

        if (qComp.headerIconsVisible) {
            if (fieldType.equals(IntegerTableField.DATA_TYPE)) {
                if (notKey)
                    setIcon(QueryComponent.integerIcon);
                else
                    setIcon(QueryComponent.keyIntegerIcon);
            }else if (fieldType.equals(DoubleTableField.DATA_TYPE)) {
                if (notKey)
                    setIcon(QueryComponent.doubleIcon);
                else
                    setIcon(QueryComponent.keyDoubleIcon);
            }else if (fieldType.equals(FloatTableField.DATA_TYPE)) {
                if (notKey)
                    setIcon(null); //QueryComponent.doubleIcon);
                else
                    setIcon(null) ;//QueryComponent.keyDoubleIcon);
            }else if (fieldType.equals(StringTableField.DATA_TYPE)) {
                if (notKey)
                    setIcon(QueryComponent.stringIcon);
                else
                    setIcon(QueryComponent.keyStringIcon);
            }else if (fieldType.equals(BooleanTableField.DATA_TYPE)) {
                if (notKey)
                    setIcon(QueryComponent.booleanIcon);
                else
                    setIcon(QueryComponent.keyBooleanIcon);
            }else if (fieldType.equals(ImageTableField.DATA_TYPE)) {
                if (notKey)
                    setIcon(QueryComponent.imageIcon);
                else
                    setIcon(QueryComponent.keyImageIcon);
            }else if (fieldType.equals(DateTableField.DATA_TYPE)) {
                if (notKey)
                    setIcon(QueryComponent.dateIcon);
                else
                    setIcon(QueryComponent.keyDateIcon);
            }else if (fieldType.equals(TimeTableField.DATA_TYPE)) {
                if (notKey)
                    setIcon(QueryComponent.timeIcon);
                else
                    setIcon(QueryComponent.keyTimeIcon);
            }else if (fieldType.equals(URLTableField.DATA_TYPE)) {
                if (notKey)
                    setIcon(QueryComponent.urlIcon);
                else
                    setIcon(QueryComponent.keyURLIcon);
            }

//            headerLabel.setPreferredSize(new Dimension(30, 47));
//            headerLabel.setMinimumSize(new Dimension(30, 47));
        }else{
//            headerLabel.setPreferredSize(new Dimension(30, 20));
//            headerLabel.setMinimumSize(new Dimension(30, 20));
        }

        setVerticalAlignment(SwingConstants.TOP);
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalTextPosition(SwingConstants.BOTTOM);
        setHorizontalTextPosition(SwingConstants.CENTER);

        setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
        setIconTextGap(0);
    }


    protected void setIcon(Class fieldType, boolean notKey, QueryComponent qComp) {
//        System.out.println("In setIcon(): " + fieldType.getName() + ", " + notKey);
        if (qComp.headerIconsVisible) {

            if (fieldType.equals(IntegerTableField.DATA_TYPE)) {
                if (notKey)
                    setIcon(QueryComponent.integerIcon);
                else
                    setIcon(QueryComponent.keyIntegerIcon);
            }else if (fieldType.equals(DoubleTableField.DATA_TYPE)) {
//                System.out.println("Setting to double");
                if (notKey)
                    setIcon(QueryComponent.doubleIcon);
                else
                    setIcon(QueryComponent.keyDoubleIcon);
            }else if (fieldType.equals(StringTableField.DATA_TYPE)) {
                if (notKey)
                    setIcon(QueryComponent.stringIcon);
                else
                    setIcon(QueryComponent.keyStringIcon);
            }else if (fieldType.equals(BooleanTableField.DATA_TYPE)) {
                if (notKey)
                    setIcon(QueryComponent.booleanIcon);
                else{
//                    System.out.println("setting icon: keyBooleanIcon");
                    setIcon(QueryComponent.keyBooleanIcon);
                }
            }else if (fieldType.equals(ImageTableField.DATA_TYPE)) {
                if (notKey)
                    setIcon(QueryComponent.imageIcon);
                else
                    setIcon(QueryComponent.keyImageIcon);
            }else if (fieldType.equals(DateTableField.DATA_TYPE)) {
                if (notKey)
                    setIcon(QueryComponent.dateIcon);
                else
                    setIcon(QueryComponent.keyDateIcon);
            }else if (fieldType.equals(TimeTableField.DATA_TYPE)) {
                if (notKey)
                    setIcon(QueryComponent.timeIcon);
                else
                    setIcon(QueryComponent.keyTimeIcon);
            }else if (fieldType.equals(URLTableField.DATA_TYPE)) {
                if (notKey)
                    setIcon(QueryComponent.urlIcon);
                else
                    setIcon(QueryComponent.keyURLIcon);
            }
        }
    }


/*1.0.1    public void setText(String fieldName) {
        setText(fieldName);
    }
*/

    public Component getTableCellRendererComponent(JTable table,
                                                Object value,
                                                boolean isSelected,
                                                boolean hasFocus,
                                                int rowIndex,
                                                int columnIndex) {
          return this;
    }


    public void refresh() {
        paintImmediately(getVisibleRect());
    }


    public void setNormalFieldHeaderFont() {
        setFont(normalHeaderFont);
        setForeground(Color.black);
    }

    public void setCalculatedFieldHeaderFont() {
        setFont(calcHeaderFont);
        setForeground(calcHeaderColor);
    }
}

