/*
 * Created by IntelliJ IDEA.
 * User: tsironis
 * Date: 8 Íןו 2002
 * Time: 4:59:59 לל
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package gr.cti.eslate.database;

import java.beans.*;
import java.util.ResourceBundle;
import java.util.Locale;
import java.util.Vector;
import java.lang.reflect.Method;
import java.awt.Image;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import gr.cti.eslate.event.ColumnListener;
import gr.cti.eslate.event.ColumnMovedEvent;
import gr.cti.eslate.utils.TransferHandlerListener;
import gr.cti.eslate.utils.DataImportedEvent;
import gr.cti.eslate.utils.DataExportedEvent;
import gr.cti.eslate.utils.TransferableCreatedEvent;

import javax.swing.ImageIcon;


public class DBTableBeanInfo extends SimpleBeanInfo {
    ResourceBundle bundle;
    ImageIcon mono16Icon = new ImageIcon(getClass().getResource("images/toolbar/fullGrid.gif"));

    public DBTableBeanInfo() {
//        System.out.println("DatabaseBeanInfo constructor");
        bundle=ResourceBundle.getBundle("gr.cti.eslate.database.DBTableBundle", Locale.getDefault());
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        try{
            PropertyDescriptor pd1 = new PropertyDescriptor(
                                            "border",
                                            DBTable.class,
                                            "getBorder",
                                            "setBorder");
            pd1.setDisplayName(bundle.getString("Border"));
            pd1.setShortDescription(bundle.getString("BorderTip"));

            PropertyDescriptor pd2 = new PropertyDescriptor(
                                            "tableHeaderExpansionChangeAllowed",
                                            DBTable.class,
                                            "isTableHeaderExpansionChangeAllowed",
                                            "setTableHeaderExpansionChangeAllowed");
            pd2.setDisplayName(bundle.getString("TableHeaderExpansionChangeAllowed"));
            pd2.setShortDescription(bundle.getString("TableHeaderExpansionChangeAllowedTip"));

            PropertyDescriptor pd3 = new PropertyDescriptor(
                                            "sortFromColumnHeadersEnabled",
                                            DBTable.class,
                                            "isSortFromColumnHeadersEnabled",
                                            "setSortFromColumnHeadersEnabled");
            pd3.setDisplayName(bundle.getString("SortFromColumnHeadersEnabled"));
            pd3.setShortDescription(bundle.getString("SortFromColumnHeadersEnabledTip"));

            PropertyDescriptor pd4 = new PropertyDescriptor(
                                            "tablePopupEnabled",
                                            DBTable.class,
                                            "isTablePopupEnabled",
                                            "setTablePopupEnabled");
            pd4.setDisplayName(bundle.getString("TablePopupEnabled"));
            pd4.setShortDescription(bundle.getString("TablePopupEnabledTip"));

            PropertyDescriptor pd5 = new PropertyDescriptor(
                                            "columnPopupEnabled",
                                            DBTable.class,
                                            "isColumnPopupEnabled",
                                            "setColumnPopupEnabled");
            pd5.setDisplayName(bundle.getString("ColumnPopupEnabled"));
            pd5.setShortDescription(bundle.getString("ColumnPopupEnabledTip"));

            PropertyDescriptor pd6 = new PropertyDescriptor("HeaderFont", DBTable.class);
            pd6.setDisplayName(bundle.getString("HeaderFont"));
            pd6.setShortDescription(bundle.getString("HeaderFontTip"));

            PropertyDescriptor pd7 = new PropertyDescriptor("CalculatedFieldHeaderFont", DBTable.class);
            pd7.setDisplayName(bundle.getString("CalculatedFieldHeaderFont"));
            pd7.setShortDescription(bundle.getString("CalculatedFieldHeaderFontTip"));


            PropertyDescriptor pd8 = new PropertyDescriptor("calculatedFieldHeaderForeground", DBTable.class);
            pd8.setDisplayName(bundle.getString("CalculatedFieldHeaderForeground"));
            pd8.setShortDescription(bundle.getString("CalculatedFieldHeaderForegroundtTip"));

            PropertyDescriptor pd9 = new PropertyDescriptor("headerForeground", DBTable.class);
            pd9.setDisplayName(bundle.getString("HeaderForeground"));
            pd9.setShortDescription(bundle.getString("HeaderForegroundTip"));

            PropertyDescriptor pd10 = new PropertyDescriptor("headerBackground", DBTable.class);
            pd10.setDisplayName(bundle.getString("HeaderBackground"));
            pd10.setShortDescription(bundle.getString("HeaderBackgroundTip"));

            PropertyDescriptor pd11 = new PropertyDescriptor("verticalRowBarBrightColor", DBTable.class);
            pd11.setDisplayName(bundle.getString("VerticalRowBarBrightColor"));
            pd11.setShortDescription(bundle.getString("VerticalRowBarBrightColorTip"));

            PropertyDescriptor pd12 = new PropertyDescriptor("verticalRowBarColor", DBTable.class);
            pd12.setDisplayName(bundle.getString("VerticalRowBarColor"));
            pd12.setShortDescription(bundle.getString("VerticalRowBarColorTip"));

            PropertyDescriptor pd13 = new PropertyDescriptor("verticalRowBarDarkColor", DBTable.class);
            pd13.setDisplayName(bundle.getString("VerticalRowBarDarkColor"));
            pd13.setShortDescription(bundle.getString("VerticalRowBarDarkColorTip"));

            PropertyDescriptor pd14 = new PropertyDescriptor("twoColorBackgroundEnabled", DBTable.class);
            pd14.setDisplayName(bundle.getString("TwoColorBackgroundEnabled"));
            pd14.setShortDescription(bundle.getString("TwoColorBackgroundEnabledTip"));

            PropertyDescriptor pd15 = new PropertyDescriptor("oddRowColor", DBTable.class);
            pd15.setDisplayName(bundle.getString("OddRowColor"));
            pd15.setShortDescription(bundle.getString("OddRowColorTip"));

            PropertyDescriptor pd16 = new PropertyDescriptor("evenRowColor", DBTable.class);
            pd16.setDisplayName(bundle.getString("EvenRowColor"));
            pd16.setShortDescription(bundle.getString("EvenRowColorTip"));

            PropertyDescriptor pd17 = new PropertyDescriptor("recordSelectionChangeAllowed", DBTable.class);
            pd17.setDisplayName(bundle.getString("RecordSelectionChangeAllowed"));
            pd17.setShortDescription(bundle.getString("RecordSelectionChangeAllowedTip"));

            PropertyDescriptor pd18 = new PropertyDescriptor("activeRecordDrawMode", DBTable.class);
            pd18.setDisplayName(bundle.getString("ActiveRecordDrawMode"));
            pd18.setShortDescription(bundle.getString("ActiveRecordDrawModeTip"));
            pd18.setPropertyEditorClass(gr.cti.eslate.database.ActiveRecordDrawModePropertyEditor.class);

            PropertyDescriptor pd19 = new PropertyDescriptor("selectedRecordDrawMode", DBTable.class);
            pd19.setDisplayName(bundle.getString("SelectedRecordDrawMode"));
            pd19.setShortDescription(bundle.getString("SelectedRecordDrawModeTip"));
            pd19.setPropertyEditorClass(gr.cti.eslate.database.ActiveRecordDrawModePropertyEditor.class);

            return new PropertyDescriptor[] {pd1, pd2, pd3, pd4, pd5, pd6, pd7, pd8, pd9, pd10, pd11, pd12, pd13, pd14, pd15, pd16, pd17, pd18, pd19};
        }catch (IntrospectionException exc) {
            System.out.println("IntrospectionException: " + exc.getMessage());
            return null;
        }
    }

    public EventSetDescriptor[] getEventSetDescriptors() {
            Vector descriptors = new Vector();
            EventSetDescriptor esd = null;
            try{
                Method listenerMethod = PropertyChangeListener.class.getMethod("propertyChange", new Class[] {PropertyChangeEvent.class});
                Method addListenerMethod = Database.class.getMethod("addPropertyChangeListener", new Class[] {PropertyChangeListener.class});
                Method removelistenerMethod = Database.class.getMethod("removePropertyChangeListener", new Class[] {PropertyChangeListener.class});
                MethodDescriptor md = new MethodDescriptor(listenerMethod);
                md.setDisplayName(bundle.getString("propertyChange"));
                esd = new EventSetDescriptor("propertyChange",
                                       PropertyChangeListener.class,
                                       new MethodDescriptor[] {md},
                                       addListenerMethod,
                                       removelistenerMethod);
                descriptors.addElement(esd);
            }catch (Exception exc) {
                exc.printStackTrace();
            }

            try{
                Method listenerMethod = VetoableChangeListener.class.getMethod("vetoableChange", new Class[] {PropertyChangeEvent.class});
                Method addListenerMethod = Database.class.getMethod("addVetoableChangeListener", new Class[] {VetoableChangeListener.class});
                Method removelistenerMethod = Database.class.getMethod("removeVetoableChangeListener", new Class[] {VetoableChangeListener.class});
                MethodDescriptor md = new MethodDescriptor(listenerMethod);
                md.setDisplayName(bundle.getString("vetoableChange"));
                esd = new EventSetDescriptor("vetoableChange",
                                       VetoableChangeListener.class,
                                       new MethodDescriptor[] {md},
                                       addListenerMethod,
                                       removelistenerMethod);
                descriptors.addElement(esd);
            }catch (Exception exc) {
                exc.printStackTrace();
            }

            try{
                Method listenerMethod1 = MouseListener.class.getMethod("mouseEntered", new Class[] {MouseEvent.class});
                MethodDescriptor md1 = new MethodDescriptor(listenerMethod1);
                md1.setDisplayName(bundle.getString("mouseEntered"));
                Method listenerMethod2 = MouseListener.class.getMethod("mouseExited", new Class[] {MouseEvent.class});
                MethodDescriptor md2 = new MethodDescriptor(listenerMethod2);
                md2.setDisplayName(bundle.getString("mouseExited"));
                Method addListenerMethod = Database.class.getMethod("addMouseListener", new Class[] {MouseListener.class});
                Method removelistenerMethod = Database.class.getMethod("removeMouseListener", new Class[] {MouseListener.class});
                esd = new EventSetDescriptor("mouse",
                                       MouseListener.class,
                                       new MethodDescriptor[] {md1, md2},
                                       addListenerMethod,
                                       removelistenerMethod);
                descriptors.addElement(esd);
            }catch (Exception exc) {
                exc.printStackTrace();
            }

            try{
                Method listenerMethod = MouseMotionListener.class.getMethod("mouseMoved", new Class[] {MouseEvent.class});
                Method addListenerMethod = Database.class.getMethod("addMouseMotionListener", new Class[] {MouseMotionListener.class});
                Method removelistenerMethod = Database.class.getMethod("removeMouseMotionListener", new Class[] {MouseMotionListener.class});
                MethodDescriptor md = new MethodDescriptor(listenerMethod);
                md.setDisplayName(bundle.getString("mouseMoved"));
                esd = new EventSetDescriptor("mouseMotion",
                                       MouseMotionListener.class,
                                       new MethodDescriptor[] {md},
                                       addListenerMethod,
                                       removelistenerMethod);
                descriptors.addElement(esd);
            }catch (Exception exc) {
                exc.printStackTrace();
            }

            try{
                Method listenerMethod1 = ComponentListener.class.getMethod("componentHidden", new Class[] {ComponentEvent.class});
                MethodDescriptor md1 = new MethodDescriptor(listenerMethod1);
                md1.setDisplayName(bundle.getString("componentHidden"));
                Method listenerMethod2 = ComponentListener.class.getMethod("componentShown", new Class[] {ComponentEvent.class});
                MethodDescriptor md2 = new MethodDescriptor(listenerMethod2);
                md2.setDisplayName(bundle.getString("componentShown"));
                Method addListenerMethod = Database.class.getMethod("addComponentListener", new Class[] {ComponentListener.class});
                Method removelistenerMethod = Database.class.getMethod("removeComponentListener", new Class[] {ComponentListener.class});
                esd = new EventSetDescriptor("component",
                                       ComponentListener.class,
                                       new MethodDescriptor[] {md1, md2},
                                       addListenerMethod,
                                       removelistenerMethod);
                descriptors.addElement(esd);
            }catch (Exception exc) {
                exc.printStackTrace();
            }

            try{
                Method addListenerMethod = DBTable.class.getMethod("addTransferHandlerListener", new Class[] {gr.cti.eslate.utils.TransferHandlerListener.class});
                Method removelistenerMethod = DBTable.class.getMethod("removeTransferHandlerListener", new Class[] {gr.cti.eslate.utils.TransferHandlerListener.class});

                Method listenerMethod = TransferHandlerListener.class.getMethod("dataImportAllowed", new Class[] {gr.cti.eslate.utils.DataImportAllowedEvent.class});
                MethodDescriptor md = new MethodDescriptor(listenerMethod);
                md.setDisplayName(bundle.getString("dataImportAllowed"));

                Method listenerMethod1 = TransferHandlerListener.class.getMethod("dataImported", new Class[] {gr.cti.eslate.utils.DataImportedEvent.class});
                MethodDescriptor md1 = new MethodDescriptor(listenerMethod1);
                md1.setDisplayName(bundle.getString("dataImported"));

                Method listenerMethod2 = TransferHandlerListener.class.getMethod("dataExported", new Class[] {gr.cti.eslate.utils.DataExportedEvent.class});
                MethodDescriptor md2 = new MethodDescriptor(listenerMethod2);
                md2.setDisplayName(bundle.getString("dataExported"));

                Method listenerMethod3 = TransferHandlerListener.class.getMethod("transferableCreated", new Class[] {gr.cti.eslate.utils.TransferableCreatedEvent.class});
                MethodDescriptor md3 = new MethodDescriptor(listenerMethod3);
                md3.setDisplayName(bundle.getString("transferableCreated"));

                esd = new EventSetDescriptor("TransferHandlerEventSet",
                                       TransferHandlerListener.class,
                                       new MethodDescriptor[] {md, md1, md2, md3},
                                       addListenerMethod,
                                       removelistenerMethod);
                descriptors.addElement(esd);
            }catch (Exception exc) {
                exc.printStackTrace();
            }

            EventSetDescriptor[] d = new EventSetDescriptor[descriptors.size()];
            for (int i=0; i<d.length; i++)
                d[i] = (EventSetDescriptor) descriptors.elementAt(i);
            return d;
    }

    public Image getIcon(int iconKind) {
        if (iconKind == BeanInfo.ICON_MONO_16x16 || iconKind == BeanInfo.ICON_COLOR_16x16)
            return mono16Icon.getImage();
        return null;
    }

/*    public BeanDescriptor getBeanDescriptor() {
        return new BeanDescriptor(Database.class, DatabaseCustomizer.class);
    }
*/

/*    public BeanInfo[] getAdditionalBeanInfo() {
        Class superclass = Button.class.getSuperclass();
        try{
            BeanInfo superBeanInfo = Introspector.getBeanInfo(superclass);
            return new BeanInfo[] {superBeanInfo};
        }catch (IntrospectionException exc) {
            return null;
        }
    }
*/
}


