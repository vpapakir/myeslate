package gr.cti.eslate.database.engine;

import java.beans.SimpleBeanInfo;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.util.ResourceBundle;
import java.util.Locale;
import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.beans.*;
import java.lang.reflect.Method;
import java.util.ArrayList;

import gr.cti.eslate.tableModel.event.*;


public class TableBeanInfo extends SimpleBeanInfo {

    ResourceBundle bundle;
    ImageIcon mono16Icon = new ImageIcon(getClass().getResource("images/table16x16.gif"));
    ImageIcon mono32Icon = new ImageIcon(getClass().getResource("images/table32x32.gif"));

    public TableBeanInfo() {
        bundle=ResourceBundle.getBundle("gr.cti.eslate.database.engine.CTableBeanInfoBundle", Locale.getDefault());
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        try{
            PropertyDescriptor pd1 = new PropertyDescriptor(
                                            "activeRecord",
                                            Table.class,
                                            "getActiveRecord",
                                            "setActiveRecord");
            pd1.setDisplayName(bundle.getString("ActiveRecord"));
            pd1.setShortDescription(bundle.getString("ActiveRecordTip"));

            PropertyDescriptor pd2 = new PropertyDescriptor(
                                            "dataChangeAllowed",
                                            Table.class,
                                            "isDataChangeAllowed",
                                            "setDataChangeAllowed");
            pd2.setDisplayName(bundle.getString("DataChangeAllowed"));
            pd2.setShortDescription(bundle.getString("DataChangeAllowedTip"));

            PropertyDescriptor pd3 = new PropertyDescriptor(
                                            "fieldAdditionAllowed",
                                            Table.class,
                                            "isFieldAdditionAllowed",
                                            "setFieldAdditionAllowed");
            pd3.setDisplayName(bundle.getString("FieldAdditionAllowed"));
            pd3.setShortDescription(bundle.getString("FieldAdditionAllowedTip"));

            PropertyDescriptor pd4 = new PropertyDescriptor(
                                            "fieldRemovalAllowed",
                                            Table.class,
                                            "isFieldRemovalAllowed",
                                            "setFieldRemovalAllowed");
            pd4.setDisplayName(bundle.getString("FieldRemovalAllowed"));
            pd4.setShortDescription(bundle.getString("FieldRemovalAllowedTip"));

            PropertyDescriptor pd5 = new PropertyDescriptor(
                                            "fieldReorderingAllowed",
                                            Table.class,
                                            "isFieldReorderingAllowed",
                                            "setFieldReorderingAllowed");
            pd5.setDisplayName(bundle.getString("FieldReorderingAllowed"));
            pd5.setShortDescription(bundle.getString("FieldReorderingAllowedTip"));

            PropertyDescriptor pd6 = new PropertyDescriptor(
                                            "keyChangedAllowed",
                                            Table.class,
                                            "isKeyChangeAllowed",
                                            "setKeyChangeAllowed");
            pd6.setDisplayName(bundle.getString("KeyChangedAllowed"));
            pd6.setShortDescription(bundle.getString("KeyChangedAllowedTip"));

            PropertyDescriptor pd7 = new PropertyDescriptor(
                                            "metadata",
                                            Table.class,
                                            "getMetadata",
                                            "setMetadata");
            pd7.setDisplayName(bundle.getString("Metadata"));
            pd7.setShortDescription(bundle.getString("MetadataTip"));

            PropertyDescriptor pd8 = new PropertyDescriptor(
                                            "recordAdditionAllowed",
                                            Table.class,
                                            "isRecordAdditionAllowed",
                                            "setRecordAdditionAllowed");
            pd8.setDisplayName(bundle.getString("RecordAdditionAllowed"));
            pd8.setShortDescription(bundle.getString("RecordAdditionAllowedTip"));

            PropertyDescriptor pd9 = new PropertyDescriptor(
                                            "recordRemovalAllowed",
                                            Table.class,
                                            "isRecordRemovalAllowed",
                                            "setRecordRemovalAllowed");
            pd9.setDisplayName(bundle.getString("RecordRemovalAllowed"));
            pd9.setShortDescription(bundle.getString("RecordRemovalAllowedTip"));

            PropertyDescriptor pd10 = new PropertyDescriptor(
                                            "title",
                                            Table.class,
                                            "getTitle",
                                            "setTitle");
            pd10.setDisplayName(bundle.getString("Title"));
            pd10.setShortDescription(bundle.getString("TitleTip"));

            PropertyDescriptor pd11 = new PropertyDescriptor(
                                            "fieldPropertyEditingAllowed",
                                            Table.class,
                                            "isFieldPropertyEditingAllowed",
                                            "setFieldPropertyEditingAllowed");
            pd11.setDisplayName(bundle.getString("FieldPropertyEditingAllowed"));
            pd11.setShortDescription(bundle.getString("FieldPropertyEditingAllowedTip"));

            PropertyDescriptor pd12 = new PropertyDescriptor(
                                            "tableRenamingAllowed",
                                            Table.class,
                                            "isTableRenamingAllowed",
                                            "setTableRenamingAllowed");
            pd12.setDisplayName(bundle.getString("TableRenamingAllowed"));
            pd12.setShortDescription(bundle.getString("TableRenamingAllowedTip"));

            return new PropertyDescriptor[] {pd1, pd2, pd3, pd4, pd5, pd6, pd7, pd8, pd9, pd10, pd11, pd12};
        }catch (IntrospectionException exc) {
            System.out.println("IntrospectionException: " + exc.getMessage());
            return null;
        }
    }

    public EventSetDescriptor[] getEventSetDescriptors() {
//    public void columnKeyChanged(ColumnKeyChangedEvent e) {}
//    public void calcColumnReset(CalcColumnResetEvent e) {}
//    public void columnEditableStateChanged(ColumnEditableStateChangedEvent e) {}
//    public void columnRemovableStateChanged(ColumnRemovableStateChangedEvent e) {}
//    public void columnHiddenStateChanged(ColumnHiddenStateChangedEvent e) {}
//    public void calcColumnFormulaChanged(CalcColumnFormulaChangedEvent e) {}
//    public void recordAdded(RecordAddedEvent e) {}
//    public void emptyRecordAdded(RecordAddedEvent e) {}
//    public void recordRemoved(RecordRemovedEvent e) {}
//    public void activeRecordChanged(ActiveRecordChangedEvent e) {}
//    public void tableHiddenStateChanged(TableHiddenStateChangedEvent e) {}
        ArrayList descriptors = new ArrayList();

        EventSetDescriptor esd = null;
        try{
            Method listenerMethod = DatabaseTableModelListener.class.getMethod("tableHiddenStateChanged", new Class[] {TableHiddenStateChangedEvent.class});
            Method addListenerMethod = Table.class.getMethod("addTableModelListener", new Class[] {TableModelListener.class});
            Method removelistenerMethod = Table.class.getMethod("removeTableModelListener", new Class[] {TableModelListener.class});
            MethodDescriptor md = new MethodDescriptor(listenerMethod);
            md.setDisplayName(bundle.getString("TableHiddenStateChanged"));
            esd = new EventSetDescriptor("tableHiddenStateChanged",
                                   DatabaseTableModelListener.class,
                                   new MethodDescriptor[] {md},
                                   addListenerMethod,
                                   removelistenerMethod);
            descriptors.add(esd);
        }catch (Exception exc) {
            exc.printStackTrace();
        }

        try{
            Method listenerMethod = TableModelListener.class.getMethod("recordRemoved", new Class[] {RecordRemovedEvent.class});
            Method addListenerMethod = Table.class.getMethod("addTableModelListener", new Class[] {TableModelListener.class});
            Method removelistenerMethod = Table.class.getMethod("removeTableModelListener", new Class[] {TableModelListener.class});
            MethodDescriptor md = new MethodDescriptor(listenerMethod);
            md.setDisplayName(bundle.getString("RecordRemoved"));
            esd = new EventSetDescriptor("recordRemoved",
                                   TableModelListener.class,
                                   new MethodDescriptor[] {md},
                                   addListenerMethod,
                                   removelistenerMethod);
            descriptors.add(esd);
        }catch (Exception exc) {
            exc.printStackTrace();
        }

/*        try{
            Method listenerMethod = DatabaseTableModelListener.class.getMethod("emptyRecordAdded", new Class[] {RecordAddedEvent.class});
            Method addListenerMethod = Table.class.getMethod("addTableModelListener", new Class[] {TableModelListener.class});
            Method removelistenerMethod = Table.class.getMethod("removeTableModelListener", new Class[] {TableModelListener.class});
            MethodDescriptor md = new MethodDescriptor(listenerMethod);
            md.setDisplayName(bundle.getString("EmptyRecordAdded"));
            esd = new EventSetDescriptor("emptyRecordAdded",
                                   DatabaseTableModelListener.class,
                                   new MethodDescriptor[] {md},
                                   addListenerMethod,
                                   removelistenerMethod);
            descriptors.add(esd);
        }catch (Exception exc) {
            exc.printStackTrace();
        }
*/
        try{
            Method listenerMethod = DatabaseTableModelListener.class.getMethod("recordAdded", new Class[] {RecordAddedEvent.class});
            Method addListenerMethod = Table.class.getMethod("addTableModelListener", new Class[] {TableModelListener.class});
            Method removelistenerMethod = Table.class.getMethod("removeTableModelListener", new Class[] {TableModelListener.class});
            MethodDescriptor md = new MethodDescriptor(listenerMethod);
            md.setDisplayName(bundle.getString("RecordAdded"));
            esd = new EventSetDescriptor("recordAdded",
                                   DatabaseTableModelListener.class,
                                   new MethodDescriptor[] {md},
                                   addListenerMethod,
                                   removelistenerMethod);
            descriptors.add(esd);
        }catch (Exception exc) {
            exc.printStackTrace();
        }

        try{
            Method listenerMethod = DatabaseTableModelListener.class.getMethod("calcColumnFormulaChanged", new Class[] {CalcColumnFormulaChangedEvent.class});
            Method addListenerMethod = Table.class.getMethod("addTableModelListener", new Class[] {TableModelListener.class});
            Method removelistenerMethod = Table.class.getMethod("removeTableModelListener", new Class[] {TableModelListener.class});
            MethodDescriptor md = new MethodDescriptor(listenerMethod);
            md.setDisplayName(bundle.getString("CalcColumnFormulaChanged"));
            esd = new EventSetDescriptor("calcColumnFormulaChanged",
                                   DatabaseTableModelListener.class,
                                   new MethodDescriptor[] {md},
                                   addListenerMethod,
                                   removelistenerMethod);
            descriptors.add(esd);
        }catch (Exception exc) {
            exc.printStackTrace();
        }

        try{
            Method listenerMethod = DatabaseTableModelListener.class.getMethod("columnHiddenStateChanged", new Class[] {ColumnHiddenStateChangedEvent.class});
            Method addListenerMethod = Table.class.getMethod("addTableModelListener", new Class[] {TableModelListener.class});
            Method removelistenerMethod = Table.class.getMethod("removeTableModelListener", new Class[] {TableModelListener.class});
            MethodDescriptor md = new MethodDescriptor(listenerMethod);
            md.setDisplayName(bundle.getString("ColumnHiddenStateChanged"));
            esd = new EventSetDescriptor("columnHiddenStateChanged",
                                   DatabaseTableModelListener.class,
                                   new MethodDescriptor[] {md},
                                   addListenerMethod,
                                   removelistenerMethod);
            descriptors.add(esd);
        }catch (Exception exc) {
            exc.printStackTrace();
        }

        try{
            Method listenerMethod = DatabaseTableModelListener.class.getMethod("columnEditableStateChanged", new Class[] {ColumnEditableStateChangedEvent.class});
            Method addListenerMethod = Table.class.getMethod("addTableModelListener", new Class[] {TableModelListener.class});
            Method removelistenerMethod = Table.class.getMethod("removeTableModelListener", new Class[] {TableModelListener.class});
            MethodDescriptor md = new MethodDescriptor(listenerMethod);
            md.setDisplayName(bundle.getString("ColumnEditableStateChanged"));
            esd = new EventSetDescriptor("columnEditableStateChanged",
                                   DatabaseTableModelListener.class,
                                   new MethodDescriptor[] {md},
                                   addListenerMethod,
                                   removelistenerMethod);
            descriptors.add(esd);
        }catch (Exception exc) {
            exc.printStackTrace();
        }

        try{
            Method listenerMethod = DatabaseTableModelListener.class.getMethod("columnRemovableStateChanged", new Class[] {ColumnRemovableStateChangedEvent.class});
            Method addListenerMethod = Table.class.getMethod("addTableModelListener", new Class[] {TableModelListener.class});
            Method removelistenerMethod = Table.class.getMethod("removeTableModelListener", new Class[] {TableModelListener.class});
            MethodDescriptor md = new MethodDescriptor(listenerMethod);
            md.setDisplayName(bundle.getString("ColumnRemovableStateChanged"));
            esd = new EventSetDescriptor("columnRemovableStateChanged",
                                   DatabaseTableModelListener.class,
                                   new MethodDescriptor[] {md},
                                   addListenerMethod,
                                   removelistenerMethod);
            descriptors.add(esd);
        }catch (Exception exc) {
            exc.printStackTrace();
        }

        try{
            Method listenerMethod = DatabaseTableModelListener.class.getMethod("columnKeyChanged", new Class[] {ColumnKeyChangedEvent.class});
            Method addListenerMethod = Table.class.getMethod("addTableModelListener", new Class[] {TableModelListener.class});
            Method removelistenerMethod = Table.class.getMethod("removeTableModelListener", new Class[] {TableModelListener.class});
            MethodDescriptor md = new MethodDescriptor(listenerMethod);
            md.setDisplayName(bundle.getString("ColumnKeyChanged"));
            esd = new EventSetDescriptor("columnKeyChanged",
                                   DatabaseTableModelListener.class,
                                   new MethodDescriptor[] {md},
                                   addListenerMethod,
                                   removelistenerMethod);
            descriptors.add(esd);
        }catch (Exception exc) {
            exc.printStackTrace();
        }

        try{
            Method listenerMethod = DatabaseTableModelListener.class.getMethod("calcColumnReset", new Class[] {CalcColumnResetEvent.class});
            Method addListenerMethod = Table.class.getMethod("addTableModelListener", new Class[] {TableModelListener.class});
            Method removelistenerMethod = Table.class.getMethod("removeTableModelListener", new Class[] {TableModelListener.class});
            MethodDescriptor md = new MethodDescriptor(listenerMethod);
            md.setDisplayName(bundle.getString("CalcColumnReset"));
            esd = new EventSetDescriptor("calcColumnReset",
                                   DatabaseTableModelListener.class,
                                   new MethodDescriptor[] {md},
                                   addListenerMethod,
                                   removelistenerMethod);
            descriptors.add(esd);
        }catch (Exception exc) {
            exc.printStackTrace();
        }

        try{
            Method listenerMethod = PropertyChangeListener.class.getMethod("propertyChange", new Class[] {PropertyChangeEvent.class});
            Method addListenerMethod = Table.class.getMethod("addPropertyChangeListener", new Class[] {PropertyChangeListener.class});
            Method removelistenerMethod = Table.class.getMethod("removePropertyChangeListener", new Class[] {PropertyChangeListener.class});
            MethodDescriptor md = new MethodDescriptor(listenerMethod);
            md.setDisplayName(gr.cti.eslate.utils.ESlateBeanResource.getString("propertyChange"));
            esd = new EventSetDescriptor("propertyChange",
                                   PropertyChangeListener.class,
                                   new MethodDescriptor[] {md},
                                   addListenerMethod,
                                   removelistenerMethod);
            descriptors.add(esd);
        }catch (Exception exc) {
            exc.printStackTrace();
        }

        try{
            Method listenerMethod = TableModelListener.class.getMethod("cellValueChanged", new Class[] {CellValueChangedEvent.class});
            Method addListenerMethod = Table.class.getMethod("addTableModelListener", new Class[] {TableModelListener.class});
            Method removelistenerMethod = Table.class.getMethod("removeTableModelListener", new Class[] {TableModelListener.class});
            MethodDescriptor md = new MethodDescriptor(listenerMethod);
            md.setDisplayName(bundle.getString("CellValueChanged"));
            esd = new EventSetDescriptor("cellValueChanged",
                                   TableModelListener.class,
                                   new MethodDescriptor[] {md},
                                   addListenerMethod,
                                   removelistenerMethod);
            descriptors.add(esd);
        }catch (Exception exc) {
            exc.printStackTrace();
        }

        try{
            Method listenerMethod = TableModelListener.class.getMethod("activeRecordChanged", new Class[] {ActiveRecordChangedEvent.class});
            Method addListenerMethod = Table.class.getMethod("addTableModelListener", new Class[] {TableModelListener.class});
            Method removelistenerMethod = Table.class.getMethod("removeTableModelListener", new Class[] {TableModelListener.class});
            MethodDescriptor md = new MethodDescriptor(listenerMethod);
            md.setDisplayName(bundle.getString("ActiveRecordChanged"));
            esd = new EventSetDescriptor("activeRecordChanged",
                                   TableModelListener.class,
                                   new MethodDescriptor[] {md},
                                   addListenerMethod,
                                   removelistenerMethod);
            descriptors.add(esd);
        }catch (Exception exc) {
            exc.printStackTrace();
        }

        try{
            Method listenerMethod = TableModelListener.class.getMethod("columnTypeChanged", new Class[] {ColumnTypeChangedEvent.class});
            Method addListenerMethod = Table.class.getMethod("addTableModelListener", new Class[] {TableModelListener.class});
            Method removelistenerMethod = Table.class.getMethod("removeTableModelListener", new Class[] {TableModelListener.class});
            MethodDescriptor md = new MethodDescriptor(listenerMethod);
            md.setDisplayName(bundle.getString("ColumnTypeChanged"));
            esd = new EventSetDescriptor("columnTypeChanged",
                                   TableModelListener.class,
                                   new MethodDescriptor[] {md},
                                   addListenerMethod,
                                   removelistenerMethod);
            descriptors.add(esd);
        }catch (Exception exc) {
            exc.printStackTrace();
        }

        try{
            Method listenerMethod = TableModelListener.class.getMethod("columnRenamed", new Class[] {ColumnRenamedEvent.class});
            Method addListenerMethod = Table.class.getMethod("addTableModelListener", new Class[] {TableModelListener.class});
            Method removelistenerMethod = Table.class.getMethod("removeTableModelListener", new Class[] {TableModelListener.class});
            MethodDescriptor md = new MethodDescriptor(listenerMethod);
            md.setDisplayName(bundle.getString("ColumnRenamed"));
            esd = new EventSetDescriptor("columnRenamed",
                                   TableModelListener.class,
                                   new MethodDescriptor[] {md},
                                   addListenerMethod,
                                   removelistenerMethod);
            descriptors.add(esd);
        }catch (Exception exc) {
            exc.printStackTrace();
        }

        try{
            Method listenerMethod = TableModelListener.class.getMethod("columnAdded", new Class[] {ColumnAddedEvent.class});
            Method addListenerMethod = Table.class.getMethod("addTableModelListener", new Class[] {TableModelListener.class});
            Method removelistenerMethod = Table.class.getMethod("removeTableModelListener", new Class[] {TableModelListener.class});
            MethodDescriptor md = new MethodDescriptor(listenerMethod);
            md.setDisplayName(bundle.getString("ColumnAdded"));
            esd = new EventSetDescriptor("columnAdded",
                                   TableModelListener.class,
                                   new MethodDescriptor[] {md},
                                   addListenerMethod,
                                   removelistenerMethod);
            descriptors.add(esd);
        }catch (Exception exc) {
            exc.printStackTrace();
        }

        try{
            Method listenerMethod = TableModelListener.class.getMethod("columnRemoved", new Class[] {ColumnRemovedEvent.class});
            Method addListenerMethod = Table.class.getMethod("addTableModelListener", new Class[] {TableModelListener.class});
            Method removelistenerMethod = Table.class.getMethod("removeTableModelListener", new Class[] {TableModelListener.class});
            MethodDescriptor md = new MethodDescriptor(listenerMethod);
            md.setDisplayName(bundle.getString("ColumnRemoved"));
            esd = new EventSetDescriptor("columnRemoved",
                                   TableModelListener.class,
                                   new MethodDescriptor[] {md},
                                   addListenerMethod,
                                   removelistenerMethod);
            descriptors.add(esd);
        }catch (Exception exc) {
            exc.printStackTrace();
        }

        try{
            Method listenerMethod = TableModelListener.class.getMethod("selectedRecordSetChanged", new Class[] {SelectedRecordSetChangedEvent.class});
            Method addListenerMethod = Table.class.getMethod("addTableModelListener", new Class[] {TableModelListener.class});
            Method removelistenerMethod = Table.class.getMethod("removeTableModelListener", new Class[] {TableModelListener.class});
            MethodDescriptor md = new MethodDescriptor(listenerMethod);
            md.setDisplayName(bundle.getString("SelectedRecordSetChanged"));
            esd = new EventSetDescriptor("selectedRecordSetChanged",
                                   TableModelListener.class,
                                   new MethodDescriptor[] {md},
                                   addListenerMethod,
                                   removelistenerMethod);
            descriptors.add(esd);
        }catch (Exception exc) {
            exc.printStackTrace();
        }

        try{
            Method listenerMethod = TableModelListener.class.getMethod("rowOrderChanged", new Class[] {RowOrderChangedEvent.class});
            Method addListenerMethod = Table.class.getMethod("addTableModelListener", new Class[] {TableModelListener.class});
            Method removelistenerMethod = Table.class.getMethod("removeTableModelListener", new Class[] {TableModelListener.class});
            MethodDescriptor md = new MethodDescriptor(listenerMethod);
            md.setDisplayName(bundle.getString("RowOrderChanged"));
            esd = new EventSetDescriptor("rowOrderChanged",
                                   TableModelListener.class,
                                   new MethodDescriptor[] {md},
                                   addListenerMethod,
                                   removelistenerMethod);
            descriptors.add(esd);
        }catch (Exception exc) {
            exc.printStackTrace();
        }

        try{
            Method listenerMethod = TableModelListener.class.getMethod("tableRenamed", new Class[] {TableRenamedEvent.class});
            Method addListenerMethod = Table.class.getMethod("addTableModelListener", new Class[] {TableModelListener.class});
            Method removelistenerMethod = Table.class.getMethod("removeTableModelListener", new Class[] {TableModelListener.class});
            MethodDescriptor md = new MethodDescriptor(listenerMethod);
            md.setDisplayName(bundle.getString("TableRenamed"));
            esd = new EventSetDescriptor("tableRenamed",
                                   TableModelListener.class,
                                   new MethodDescriptor[] {md},
                                   addListenerMethod,
                                   removelistenerMethod);
            descriptors.add(esd);
        }catch (Exception exc) {
            exc.printStackTrace();
        }

        EventSetDescriptor[] d = new EventSetDescriptor[descriptors.size()];
        for (int i=0; i<d.length; i++)
            d[i] = (EventSetDescriptor) descriptors.get(i);
        return d;
    }

    public Image getIcon(int iconKind) {
        if (iconKind == BeanInfo.ICON_MONO_16x16 || iconKind == BeanInfo.ICON_COLOR_16x16)
            return mono16Icon.getImage();
        else if (iconKind == BeanInfo.ICON_MONO_32x32 || iconKind == BeanInfo.ICON_COLOR_32x32)
            return mono32Icon.getImage();
        return null;
    }

}