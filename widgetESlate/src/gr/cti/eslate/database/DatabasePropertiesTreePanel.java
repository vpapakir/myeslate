package gr.cti.eslate.database;

import javax.swing.*;
import javax.swing.text.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.tree.*;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.CompoundBorder;
import gr.cti.eslate.database.engine.DBase;
import gr.cti.eslate.database.engine.Table;
import gr.cti.eslate.database.engine.AbstractTableField;
import gr.cti.eslate.database.engine.InvalidFieldIndexException;
import com.objectspace.jgl.HashMap;


public class DatabasePropertiesTreePanel extends JPanel {
    JTree tree;
    JScrollPane scrollpane;
    HashMap dbProperties = new HashMap(false);

    public DatabasePropertiesTreePanel(Database database) {
  	    setLayout(new BorderLayout());

        String rootString;
        DatabasePropertiesTreeNode databaseNode;
        DBase db = database.db;
        if (db == null)
            databaseNode = new DatabasePropertiesTreeNode("No database", 1);
        else{
            databaseNode = new DatabasePropertiesTreeNode(db.getTitle(), 1);

            dbProperties.add("TableAddition", new Boolean(database.db.isTableAdditionAllowed()));
            dbProperties.add("TableRemoval", new Boolean(database.db.isTableRemovalAllowed()));
            dbProperties.add("TableExportation", new Boolean(database.db.isTableExportationAllowed()));
            dbProperties.add("DisplayHiddenTables", new Boolean(database.db.isHiddenTablesDisplayed()));
            dbProperties.add("Modified", Boolean.FALSE);
            HashMap tableHash = new HashMap(false);
            dbProperties.add("Tables", tableHash);

            for (int i=0; i<db.getTableCount(); i++) {
                Table ctable = db.getTableAt(i);
                DatabasePropertiesTreeNode tableNode = new DatabasePropertiesTreeNode(ctable.getTitle() , 2);

                HashMap tableProperties = new HashMap(false);
                tableHash.add(ctable.getTitle(), tableProperties);
                tableProperties.add("TableRename", new Boolean(ctable.isTableRenamingAllowed()));
                tableProperties.add("RecordAddition", new Boolean(ctable.isRecordAdditionAllowed()));
                tableProperties.add("RecordRemoval", new Boolean(ctable.isRecordRemovalAllowed()));
                tableProperties.add("FieldAddition", new Boolean(ctable.isFieldAdditionAllowed()));
                tableProperties.add("FieldRemoval", new Boolean(ctable.isFieldRemovalAllowed()));
                tableProperties.add("FieldReordering", new Boolean(ctable.isFieldReorderingAllowed()));
                tableProperties.add("FieldPropertiesEditing", new Boolean(ctable.isFieldPropertyEditingAllowed()));
                tableProperties.add("DataChange", new Boolean(ctable.isDataChangeAllowed()));
                tableProperties.add("KeyChange", new Boolean(ctable.isKeyChangeAllowed()));
                tableProperties.add("DisplayHiddenFields", new Boolean(ctable.isHiddenFieldsDisplayed()));
                tableProperties.add("HiddedChange", new Boolean(ctable.isHiddenAttributeChangeAllowed()));
                tableProperties.add("Modified", Boolean.FALSE);

                HashMap fieldHash = new HashMap(false);
                tableProperties.add("Fields", fieldHash);


                for (int k=0; k<ctable.getFieldCount(); k++) {
                    try{
                        AbstractTableField fld = ctable.getTableField(k);
                        DatabasePropertiesTreeNode fieldNode;
                        if (!fld.isCalculated())
                            fieldNode = new DatabasePropertiesTreeNode(fld.getName() , 3);
                        else
                            fieldNode = new DatabasePropertiesTreeNode(fld.getName() , 4);

                        tableNode.add(fieldNode);

                        HashMap fieldProperties = new HashMap(false);
                        fieldHash.add(fld.getName(), fieldProperties);
                        fieldProperties.add("DataChange", new Boolean(fld.isFieldEditabilityChangeAllowed()));
                        fieldProperties.add("Removability", new Boolean(fld.isFieldRemovabilityChangeAllowed()));
                        fieldProperties.add("DataTypeChange", new Boolean(fld.isFieldDataTypeChangeAllowed()));
                        fieldProperties.add("KeyChange", new Boolean(fld.isFieldKeyAttributeChangeAllowed()));
                        fieldProperties.add("HiddenChange", new Boolean(fld.isFieldHiddenAttributeChangeAllowed()));
                        fieldProperties.add("CalculatedReset", new Boolean(fld.isCalcFieldResetAllowed()));
                        fieldProperties.add("FormulaChange", new Boolean(fld.isCalcFieldFormulaChangeAllowed()));
                        fieldProperties.add("Modified", Boolean.FALSE);
                    }catch (InvalidFieldIndexException exc) {
                        System.out.println("Serious inconsistency error in DatabasePropertiesTreePanel DatabasePropertiesTreePanel() : 1");
                    }
                }
                databaseNode.add(tableNode);
            }
        }

    	tree = new JTree(databaseNode);
        tree.setCellRenderer(new DatabasePropertiesTreeCellRenderer());
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        scrollpane = new JScrollPane(tree);
//        scrollpane.setBackground(Color.white);
        scrollpane.setBorder(new CompoundBorder(scrollpane.getBorder(), new EmptyBorder(2,0,0,0))); //new SoftBevelBorder(SoftBevelBorder.LOWERED));
    	add(scrollpane, BorderLayout.CENTER);
    }

}


class DatabasePropertiesTreeNode extends DefaultMutableTreeNode {
    int type;
    public DatabasePropertiesTreeNode(Object obj, int type) {
        super(obj);
        this.type = type;
    }
}

class DatabasePropertiesTreeCellRenderer extends JLabel implements TreeCellRenderer {
    ImageIcon databaseIcon = new ImageIcon(getClass().getResource("images/database.gif"));
    ImageIcon tableIcon = new ImageIcon(getClass().getResource("images/table2.gif"));
    ImageIcon fieldIcon = new ImageIcon(getClass().getResource("images/field.gif"));
    static Color selectedForeground = Color.white;
    static Color selectedBackground = new Color(0, 0, 128);
    static Color foreground = Color.black;
    static Color background = UIManager.getColor("Tree.background");
    static Color calcForeground = new Color(244,9,123);

    public DatabasePropertiesTreeCellRenderer() {
        super();
        setOpaque(true);
        setIconTextGap(0);
    }

    public Component getTreeCellRendererComponent (JTree tree,
                                                Object value,            // value to display
                                                boolean isSelected,      // is the cell selected
                                                boolean expanded,    // the jTable and the cell have the focus
                                                boolean leaf,
                                                int row,
                                                boolean hasFocus)
    {
        DatabasePropertiesTreeNode dptn = (DatabasePropertiesTreeNode) value;
        if (dptn.type == 1)
            setIcon(databaseIcon);
        else if (dptn.type == 2)
            setIcon(tableIcon);
        else if (dptn.type >= 3 )
            setIcon(fieldIcon);
        else
            setIcon(null);

        setText(value.toString());
        if (isSelected) {
            if (dptn.type != 4)
                setForeground(selectedForeground);
            else
                setForeground(calcForeground);
            setBackground(selectedBackground);
        }else{
            if (dptn.type != 4)
                setForeground(foreground);
            else
                setForeground(calcForeground);
            setBackground(background);
        }
        return this;
    }

    public void updateUI() {
        super.updateUI();
        background = UIManager.getColor("Tree.background");
    }
}