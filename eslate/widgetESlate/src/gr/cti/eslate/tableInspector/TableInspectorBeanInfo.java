package gr.cti.eslate.tableInspector;

import gr.cti.eslate.utils.ESlateBeanInfo;

import java.awt.Image;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

public class TableInspectorBeanInfo extends ESlateBeanInfo {
    public TableInspectorBeanInfo() {
        super();
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        try{
            PropertyDescriptor pd1 = new PropertyDescriptor("percentField",TableInspector.class);
            pd1.setDisplayName(bundle.getString("percentField"));
            pd1.setShortDescription(bundle.getString("percentFieldtip"));
            pd1.setPropertyEditorClass(EditorPercentage.class);

            PropertyDescriptor pd2 = new PropertyDescriptor("toolNavigateVisible",TableInspector.class);
            pd2.setDisplayName(bundle.getString("toolNavigateVisible"));
            pd2.setShortDescription(bundle.getString("toolNavigateVisibletip"));

            PropertyDescriptor pd3 = new PropertyDescriptor("toolSelectViewVisible",TableInspector.class);
            pd3.setDisplayName(bundle.getString("toolSelectViewVisible"));
            pd3.setShortDescription(bundle.getString("toolSelectViewVisibletip"));

            PropertyDescriptor pd4 = new PropertyDescriptor("toolShowRecordVisible",TableInspector.class);
            pd4.setDisplayName(bundle.getString("toolShowRecordVisible"));
            pd4.setShortDescription(bundle.getString("toolShowRecordVisibletip"));

            PropertyDescriptor pd5 = new PropertyDescriptor("toolSelectViewDefaultShowAll",TableInspector.class);
            pd5.setDisplayName(bundle.getString("toolSelectViewDefaultShowAll"));
            pd5.setShortDescription(bundle.getString("toolSelectViewDefaultShowAlltip"));

            PropertyDescriptor pd6 = new PropertyDescriptor("toolbarVisible",TableInspector.class);
            pd6.setDisplayName(bundle.getString("toolbarVisible"));
            pd6.setShortDescription(bundle.getString("toolbarVisibletip"));

            PropertyDescriptor pd7 = new PropertyDescriptor("messagebarVisible",TableInspector.class);
            pd7.setDisplayName(bundle.getString("messagebarVisible"));
            pd7.setShortDescription(bundle.getString("messagebarVisibletip"));

            PropertyDescriptor pd8 = new PropertyDescriptor("backgroundImage",TableInspector.class);
            pd8.setDisplayName(bundle.getString("backgroundImage"));
            pd8.setShortDescription(bundle.getString("backgroundImagetip"));

            PropertyDescriptor pd9 = new PropertyDescriptor("tabPlacement",TableInspector.class);
            pd9.setDisplayName(bundle.getString("tabPlacement"));
            pd9.setShortDescription(bundle.getString("tabPlacementtip"));
            pd9.setPropertyEditorClass(EditorTabPlacement.class);

            PropertyDescriptor pd10 = new PropertyDescriptor("imageAlignment",TableInspector.class);
            pd10.setDisplayName(bundle.getString("imageAlignment"));
            pd10.setShortDescription(bundle.getString("imageAlignmenttip"));
            pd10.setPropertyEditorClass(EditorImageAlignment.class);

            PropertyDescriptor pd11 = new PropertyDescriptor("opaque",TableInspector.class);
            pd11.setDisplayName(bundle.getString("opaque"));
            pd11.setShortDescription(bundle.getString("opaquetip"));

            PropertyDescriptor pd12 = new PropertyDescriptor("toolQueryVisible",TableInspector.class);
            pd12.setDisplayName(bundle.getString("toolQueryVisible"));
            pd12.setShortDescription(bundle.getString("toolQueryVisibletip"));

            PropertyDescriptor pd13 = new PropertyDescriptor("widgetsOpaque",TableInspector.class);
            pd13.setDisplayName(bundle.getString("widgetsOpaque"));
            pd13.setShortDescription(bundle.getString("widgetsOpaquetip"));

            PropertyDescriptor pd14 = new PropertyDescriptor("background",TableInspector.class);
            pd14.setDisplayName(bundle.getString("background"));
            pd14.setShortDescription(bundle.getString("backgroundtip"));

            PropertyDescriptor pd15 = new PropertyDescriptor("border",TableInspector.class);
            pd15.setDisplayName(bundle.getString("border"));
            pd15.setShortDescription(bundle.getString("bordertip"));

            PropertyDescriptor pd16 = new PropertyDescriptor("font",TableInspector.class);
            pd16.setDisplayName(bundle.getString("font"));
            pd16.setShortDescription(bundle.getString("fonttip"));

            PropertyDescriptor pd17 = new PropertyDescriptor("tabVisible",TableInspector.class);
            pd17.setDisplayName(bundle.getString("tabVisible"));
            pd17.setShortDescription(bundle.getString("tabVisibletip"));

            PropertyDescriptor pd18 = new PropertyDescriptor("queryType",TableInspector.class);
            pd18.setDisplayName(bundle.getString("queryType"));
            pd18.setShortDescription(bundle.getString("queryTypetip"));
            pd18.setPropertyEditorClass(EditorQueryType.class);

            PropertyDescriptor pd19 = new PropertyDescriptor("rowBackgroundColor",TableInspector.class);
            pd19.setDisplayName(bundle.getString("rowBackgroundColor"));

            PropertyDescriptor pd20 = new PropertyDescriptor("rowSelectedBackgroundColor",TableInspector.class);
            pd20.setDisplayName(bundle.getString("rowSelectedBackgroundColor"));

            PropertyDescriptor pd21 = new PropertyDescriptor("rowForegroundColor",TableInspector.class);
            pd21.setDisplayName(bundle.getString("rowForegroundColor"));

            PropertyDescriptor pd22 = new PropertyDescriptor("rowSelectedForegroundColor",TableInspector.class);
            pd22.setDisplayName(bundle.getString("rowSelectedForegroundColor"));

            PropertyDescriptor pd23 = new PropertyDescriptor("rowBorderPainted",TableInspector.class);
            pd23.setDisplayName(bundle.getString("rowBorderPainted"));
            pd23.setShortDescription(bundle.getString("rowBorderPaintedtip"));

            PropertyDescriptor pd24 = new PropertyDescriptor("followActiveRecord",TableInspector.class);
            pd24.setDisplayName(bundle.getString("followActiveRecord"));
            pd24.setShortDescription(bundle.getString("followActiveRecordtip"));


            return new PropertyDescriptor[] {pd1,pd2,pd3,pd4,pd5,pd6,pd7,pd8,pd9,pd10,pd11,pd12,pd13,pd14,pd15,pd16,pd17,pd18,pd19,pd20,pd21,pd22,pd23,pd24};
        } catch (IntrospectionException exc) {
            System.out.println("TABLEINSPECTOR#200008291733: IntrospectionException: " + exc.getMessage());
            return null;
        }
    }

    public EventSetDescriptor[] getEventSetDescriptors() {
        EventSetDescriptor ed1 = null;

        try {
            Method listenerMethod = TableInspectorListener.class.getMethod("activeTabChanged", new Class[] {TableInspectorEvent.class});
            Method addListenerMethod = TableInspector.class.getMethod("addTableInspectorListener", new Class[] {TableInspectorListener.class});
            Method removelistenerMethod = TableInspector.class.getMethod("removeTableInspectorListener", new Class[] {TableInspectorListener.class});
            MethodDescriptor md = new MethodDescriptor(listenerMethod);
            md.setDisplayName(bundle.getString("activeTabChanged"));
            ed1 = new EventSetDescriptor("activeTabChanged",
                                   TableInspectorListener.class,
                                   new MethodDescriptor[] {md},
                                   addListenerMethod,
                                   removelistenerMethod);
        } catch(Exception exc) {
            exc.printStackTrace();
        }

        try {
            Method listenerMethod = TableInspectorListener.class.getMethod("activeRecordBrowserRecordChanged", new Class[] {TableInspectorEvent.class});
            Method addListenerMethod = TableInspector.class.getMethod("addTableInspectorListener", new Class[] {TableInspectorListener.class});
            Method removelistenerMethod = TableInspector.class.getMethod("removeTableInspectorListener", new Class[] {TableInspectorListener.class});
            MethodDescriptor md = new MethodDescriptor(listenerMethod);
            md.setDisplayName(bundle.getString("activeRecordBrowserRecordChanged"));
            ed1 = new EventSetDescriptor("activeRecordBrowserRecordChanged",
                                   TableInspectorListener.class,
                                   new MethodDescriptor[] {md},
                                   addListenerMethod,
                                   removelistenerMethod);
        } catch(Exception exc) {
            exc.printStackTrace();
        }

        //Combine my event descriptors with my parent's descriptors.
        EventSetDescriptor[] par=super.getEventSetDescriptors();
        EventSetDescriptor[] my=new EventSetDescriptor[par.length+1];

        System.arraycopy(par,0,my,0,par.length);
        my[my.length-1]=ed1;

        return my;
    }

    public Image getIcon(int iconKind) {
        if (iconKind == BeanInfo.ICON_MONO_16x16 || iconKind == BeanInfo.ICON_COLOR_16x16)
            return mono16Icon.getImage();
        return null;
    }

    static ResourceBundle bundle;
    private ImageIcon mono16Icon = new ImageIcon(TableInspector.class.getResource("images/tableInspectorBeanIcon.gif"));

    {
        bundle=ResourceBundle.getBundle("gr.cti.eslate.tableInspector.BundleBeanInfo", Locale.getDefault());
    }

}
