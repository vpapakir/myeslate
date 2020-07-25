package gr.cti.eslate.database;

import javax.swing.*;
import gr.cti.eslate.database.engine.*;
import java.awt.*;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.event.*;
import javax.swing.table.*;
import java.awt.image.*;
import java.net.URL;

import gr.cti.typeArray.*;
import gr.cti.eslate.eslateLabel.*;
import gr.cti.eslate.utils.OneLineBevelBorder;

public class HeaderRenderer extends JPanel implements TableCellRenderer {
    private Icon fieldIcon;
    protected int headerNormalHeight = 20;
    private boolean iconVisible = false;
    private final int IMAGE_OFFSET = 7;
    static ImageIcon upSortingIcon = new ImageIcon(HeaderRenderer.class.getResource("images/arrows/down3DArrow.gif"));
    static ImageIcon downSortingIcon = new ImageIcon(HeaderRenderer.class.getResource("images/arrows/up3DArrow.gif"));
    ESlateLabel fieldNameLabel;
    JLabel fieldTypeIconLabel;
    JPanel fieldTypeIconPanel = new JPanel();
    DBTable dbTable;
    AbstractTableField field = null;
    Border border = new OneLineBevelBorder(OneLineBevelBorder.RAISED);

    protected HeaderRenderer(AbstractTableField field, DBTable dbTable) {
        this.dbTable = dbTable;
        this.field = field;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        class MyLabel extends ESlateLabel {
            int numOfLines = 1;

            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                int lineCount = getLineCount(getWidth(), g);
//                System.out.println("numOfLines: " + numOfLines);
                if (numOfLines != lineCount) {
                    numOfLines = lineCount;
                    HeaderRenderer.this.adjustHeaderHeight();
                    HeaderRenderer.this.dbTable.jTable.getTableHeader().resizeAndRepaint();
                }
            }
        };
        fieldNameLabel = new MyLabel();
        fieldNameLabel.setBorder(null);
        fieldNameLabel.setAlignmentX(CENTER_ALIGNMENT);
        fieldNameLabel.setMultilineMode(true);

        fieldTypeIconPanel.setOpaque(false);
        fieldTypeIconPanel.setAlignmentX(CENTER_ALIGNMENT);
        fieldTypeIconPanel.setLayout(new BoxLayout(fieldTypeIconPanel, BoxLayout.X_AXIS));

        add(fieldNameLabel);
        fieldNameLabel.setText(field.getName());
        setToolTipText(fieldNameLabel.getText());

        fieldNameLabel.setVerticalAlignment(SwingConstants.CENTER);
        fieldNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        fieldNameLabel.setVerticalTextPosition(SwingConstants.CENTER);
        fieldNameLabel.setHorizontalTextPosition(SwingConstants.LEFT);

        setOpaque(true);

        if (field.isCalculated()) {
            fieldNameLabel.setFont(dbTable.calculatedFieldHeaderFont);
            fieldNameLabel.setForeground(dbTable.calculatedFieldHeaderForeground);
            setForeground(dbTable.calculatedFieldHeaderForeground);
        }else{
            fieldNameLabel.setFont(dbTable.headerFont);
            fieldNameLabel.setForeground(dbTable.headerForeground);
            setForeground(dbTable.headerForeground);
        }

        boolean headerIconsVisible = dbTable.headerIconsVisible; //1 dbTable.viewStructure.tableView.isHeaderIconsVisible();
        iconVisible = headerIconsVisible;
        Class fieldType = field.getDataType();
        boolean notKey = !dbTable.table.isPartOfTableKey(field);
        if (headerIconsVisible) {
            if (fieldType.equals(IntegerTableField.DATA_TYPE)) {
                if (notKey)
                    setIcon(Database.getIntegerTypeIcon());
                else
                    setIcon(Database.getIntegerTypeKeyIcon());
            }else if (fieldType.equals(DoubleTableField.DATA_TYPE)) {
                if (notKey)
                    setIcon(Database.getDoubleTypeIcon());
                else
                    setIcon(Database.getDoubleTypeKeyIcon());
            }else if (fieldType.equals(StringTableField.DATA_TYPE)) {
                if (notKey)
                    setIcon(Database.getStringTypeIcon());
                else
                    setIcon(Database.getStringTypeKeyIcon());
            }else if (fieldType.equals(FloatTableField.DATA_TYPE)) {
                if (notKey)
                    setIcon(Database.getFloatTypeIcon());
                else
                    setIcon(Database.getFloatTypeKeyIcon());
            }else if (fieldType.equals(BooleanTableField.DATA_TYPE)) {
                if (notKey)
                    setIcon(Database.getBooleanTypeIcon());
                else
                    setIcon(Database.getBooleanTypeKeyIcon());
            }else if (fieldType.equals(ImageTableField.DATA_TYPE)) {
                if (notKey)
                    setIcon(Database.getImageTypeIcon());
                else
                    setIcon(Database.getImageTypeKeyIcon());
            }else if (fieldType.equals(DateTableField.DATA_TYPE)) {
//                if (isDate) {
                if (notKey)
                    setIcon(Database.getDateTypeIcon());
                else
                    setIcon(Database.getDateTypeKeyIcon());
//                }else{
            }else if (fieldType.equals(TimeTableField.DATA_TYPE)) {
                if (notKey)
                    setIcon(Database.getTimeTypeIcon());
                else
                    setIcon(Database.getTimeTypeKeyIcon());
//                }
            }else if (fieldType.equals(URLTableField.DATA_TYPE)) {
                if (notKey)
                    setIcon(Database.getURLTypeIcon());
                else
                    setIcon(Database.getURLTypeKeyIcon());
            }

        }else{
        }

        //setVerticalAlignment(SwingConstants.TOP);
        //setHorizontalAlignment(SwingConstants.CENTER);
        //setVerticalTextPosition(SwingConstants.BOTTOM);
        //setHorizontalTextPosition(SwingConstants.CENTER);

        setBorder(border); //1 new SoftBevelBorder(SoftBevelBorder.RAISED));
        //setIconTextGap(0);

/*        fieldNameLabel.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                adjustHeaderHeightWhileResizing();
//                fieldNameLabel.revalidate();
//                revalidate();
            }
        });
*/
    }


    /**
     * Adjusts the height of the HeaderRenderer, so that name of the column is visible. This should be used while
     * the column is resized only.
     */
    void adjustHeaderHeightWhileResizing() {
        if (HeaderRenderer.this.field.isHidden()) return;
        fieldNameLabel.updatePreferredHeight(HeaderRenderer.this.dbTable.getGraphics());
//System.out.println(fieldNameLabel.getText() + ", height: " + fieldNameLabel.getPreferredSize().height);
        HeaderRenderer.this.dbTable.jTable.getTableHeader().resizeAndRepaint();
    }

    /**
     * Adjusts the height of the HeaderRenderer, so that name of the column is visible.
     */
    private void adjustHeaderHeight() {
        if (HeaderRenderer.this.field.isHidden()) return;
//System.out.println("header font: " + dbTable.jTable.getTableHeader().getFont() + ", label font: " + fieldNameLabel.getFont());
        fieldNameLabel.updatePreferredHeight(getGraphics());
//System.out.println("adjustHeaderHeight() " + fieldNameLabel.getText() + ", height: " + fieldNameLabel.getPreferredSize().height + ", getGraphics(): "+ getGraphics());
//        HeaderRenderer.this.dbTable.jTable.getTableHeader().resizeAndRepaint();
    }

    public void setText(String s){
        fieldNameLabel.setText(s);
        setToolTipText(fieldNameLabel.getText());
        dbTable.jTable.getTableHeader().repaint();
    }

    public String getText(){
        return fieldNameLabel.getText();
    }

    public void updateFieldIcons(String name){
        try{
            if (field.isSorted()){
                if (field.getSortDirection() == AbstractTableField.ASCENDING)
                    fieldNameLabel.setIcon(upSortingIcon);
                else
                    fieldNameLabel.setIcon(downSortingIcon);
            }
            else
                fieldNameLabel.setIcon(null);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void setIcon(Icon ico){
        if (ico == null){
            iconVisible = false;
            if (fieldTypeIconLabel != null)
                remove(fieldTypeIconPanel);
            //remove(fieldTypeIconLabel);
            return;
        }
        fieldIcon = ico;
        fieldTypeIconLabel = new JLabel(ico);
        fieldTypeIconLabel.setHorizontalAlignment(JLabel.CENTER);

        fieldTypeIconPanel.removeAll();
        fieldTypeIconPanel.add(Box.createHorizontalGlue());
        fieldTypeIconPanel.add(fieldTypeIconLabel);
        fieldTypeIconPanel.add(Box.createHorizontalGlue());

        add(fieldTypeIconPanel, 0);
        fieldNameLabel.updatePreferredHeight(getGraphics());
        //add(fieldTypeIconLabel, 0);
    }

    public Icon getIcon(){
        return fieldIcon;
    }

    protected void setIcon(Class fieldType, boolean notKey, /*boolean isDate,*/ DBTable table) {
        boolean headerIconsVisible = dbTable.headerIconsVisible; //1 table.viewStructure.tableView.isHeaderIconsVisible();
        iconVisible = headerIconsVisible;
        if (headerIconsVisible) {
            if (fieldType.equals(IntegerTableField.DATA_TYPE)) {
                if (notKey)
                    setIcon(Database.getIntegerTypeIcon());
                else
                    setIcon(Database.getIntegerTypeKeyIcon());
            }else if (fieldType.equals(DoubleTableField.DATA_TYPE)) {
                if (notKey)
                    setIcon(Database.getDoubleTypeIcon());
                else
                    setIcon(Database.getDoubleTypeKeyIcon());
            }else if (fieldType.equals(FloatTableField.DATA_TYPE)) {
                if (notKey)
                    setIcon(Database.getFloatTypeIcon());
                else
                    setIcon(Database.getFloatTypeKeyIcon());
            }else if (fieldType.equals(StringTableField.DATA_TYPE)) {
                if (notKey)
                    setIcon(Database.getStringTypeIcon());
                else
                    setIcon(Database.getStringTypeKeyIcon());
            }else if (fieldType.equals(BooleanTableField.DATA_TYPE)) {
                if (notKey)
                    setIcon(Database.getBooleanTypeIcon());
                else{
//                    System.out.println("setting icon: booleanTypeKeyIcon");
                    setIcon(Database.getBooleanTypeKeyIcon());
                }
            }else if (fieldType.equals(ImageTableField.DATA_TYPE)) {
                if (notKey)
                    setIcon(Database.getImageTypeIcon());
                else
                    setIcon(Database.getImageTypeKeyIcon());
            }else if (fieldType.equals(DateTableField.DATA_TYPE)) {
//                if (isDate) {
                if (notKey)
                    setIcon(Database.getDateTypeIcon());
                else
                    setIcon(Database.getDateTypeKeyIcon());
//                }else{
            }else if (fieldType.equals(TimeTableField.DATA_TYPE)) {
                if (notKey)
                    setIcon(Database.getTimeTypeIcon());
                else
                    setIcon(Database.getTimeTypeKeyIcon());
//                }
            }else if (fieldType.equals(URLTableField.DATA_TYPE)) {
                if (notKey)
                    setIcon(Database.getURLTypeIcon());
                else
                    setIcon(Database.getURLTypeKeyIcon());
            }
        }
    }

    public Dimension getPreferredSize() {
        Dimension dim = super.getPreferredSize();
        if (iconVisible)
            dim.height = fieldNameLabel.getPreferredSize().height+ IMAGE_OFFSET+fieldIcon.getIconHeight();
        else
            dim.height = fieldNameLabel.getPreferredSize().height;
//        System.out.println("HeaderRenderer getPreferredSize(): " + dim + ", name: " + fieldNameLabel.getText());
        return dim;
    }

    public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean hasFocus,
                                                   int rowIndex,
                                                   int colIndex) {

//System.out.println("value: " + value);
        if (field.isSorted()){
            if (field.getSortDirection() == AbstractTableField.ASCENDING)
                fieldNameLabel.setIcon(upSortingIcon);
            else
                fieldNameLabel.setIcon(downSortingIcon);
        }
        else
            fieldNameLabel.setIcon(null);
        setBackground(dbTable.headerBackground);
        return this;
    }

    void updateHeaderFont() {
        fieldNameLabel.setFont(dbTable.headerFont);
        fieldNameLabel.setForeground(dbTable.headerForeground);
        setForeground(dbTable.headerForeground);
        fieldNameLabel.updatePreferredHeight(getGraphics());
//System.out.println("fieldNameLabel: " + fieldNameLabel.getText() + ", height: " + fieldNameLabel.getPreferredSize().height);
    }

    void updateCalculatedFieldHeaderFont() {
        fieldNameLabel.setFont(dbTable.calculatedFieldHeaderFont);
        fieldNameLabel.setForeground(dbTable.calculatedFieldHeaderForeground);
        setForeground(dbTable.calculatedFieldHeaderForeground);
        fieldNameLabel.updatePreferredHeight(getGraphics());
    }
}

