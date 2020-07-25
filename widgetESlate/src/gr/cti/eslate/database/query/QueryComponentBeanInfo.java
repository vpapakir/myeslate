package gr.cti.eslate.database.query;

import java.beans.*;
import java.util.ResourceBundle;
import java.util.Locale;
import java.util.Vector;
import java.lang.reflect.Method;
import java.awt.Image;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;

import gr.cti.eslate.utils.ESlateBeanResource;


public class QueryComponentBeanInfo extends SimpleBeanInfo {
    ResourceBundle bundle;
    // Choose a proper icon
    ImageIcon color16Icon = new ImageIcon(getClass().getResource("images/QueryBeanInfoIcon.gif"));

    public QueryComponentBeanInfo() {
 
        bundle=ResourceBundle.getBundle("gr.cti.eslate.database.query.QueryComponentBeanInfoBundle",
                                        Locale.getDefault());
    }
    public PropertyDescriptor[] getPropertyDescriptors() {

        try {
            PropertyDescriptor pd1 = new PropertyDescriptor(
                                            "ExecuteButtonVisible",
                                            QueryComponent.class,
                                            "isExecuteButtonVisible",
                                            "setExecuteButtonVisible");
            pd1.setDisplayName(bundle.getString("ExecuteButtonVisible"));
            pd1.setShortDescription(bundle.getString("ExecuteButtonVisibleTip"));
            pd1.setValue("propertyCategory", bundle.getString("ToolBarButtons"));

            PropertyDescriptor pd2 = new PropertyDescriptor(
                                            "ClearQueryButtonVisible",
                                            QueryComponent.class,
                                            "isClearQueryButtonVisible",
                                            "setClearQueryButtonVisible");
            pd2.setDisplayName(bundle.getString("ClearQueryButtonVisible"));
            pd2.setShortDescription(bundle.getString("ClearQueryButtonVisibleTip"));
            pd2.setValue("propertyCategory", bundle.getString("ToolBarButtons"));


            PropertyDescriptor pd3 = new PropertyDescriptor(
                                            "addRowButtonVisible",
                                            QueryComponent.class,
                                            "isAddRowButtonVisible",
                                            "setAddRowButtonVisible");
            pd3.setDisplayName(bundle.getString("addRowButtonVisible"));
            pd3.setShortDescription(bundle.getString("addRowButtonVisibleTip"));
            pd3.setValue("propertyCategory", bundle.getString("ToolBarButtons"));


            PropertyDescriptor pd4 = new PropertyDescriptor(
                                            "removeRowButtonVisible",
                                            QueryComponent.class,
                                            "isRemoveRowButtonVisible",
                                            "setRemoveRowButtonVisible");
            pd4.setDisplayName(bundle.getString("removeRowButtonVisible"));
            pd4.setShortDescription(bundle.getString("removeRowButtonVisibleTip"));
            pd4.setValue("propertyCategory", bundle.getString("ToolBarButtons"));

            PropertyDescriptor pd5 = new PropertyDescriptor(
                                            "headerStatusButtonVisible",
                                            QueryComponent.class,
                                            "isHeaderStatusButtonVisible",
                                            "setHeaderStatusButtonVisible");
            pd5.setDisplayName(bundle.getString("headerStatusButtonVisible"));
            pd5.setShortDescription(bundle.getString("headerStatusButtonVisibleTip"));
            pd5.setValue("propertyCategory", bundle.getString("ToolBarButtons"));

            PropertyDescriptor pd6 = new PropertyDescriptor(
                                            "queryPaneButtonVisible",
                                            QueryComponent.class,
                                            "isQueryPaneButtonVisible",
                                            "setQueryPaneButtonVisible");
            pd6.setDisplayName(bundle.getString("queryPaneButtonVisible"));
            pd6.setShortDescription(bundle.getString("queryPaneButtonVisibleTip"));
            pd6.setValue("propertyCategory", bundle.getString("ToolBarButtons"));

            PropertyDescriptor pd7 = new PropertyDescriptor(
                                            "newSelectionButtonVisible",
                                            QueryComponent.class,
                                            "isNewSelectionButtonVisible",
                                            "setNewSelectionButtonVisible");
            pd7.setDisplayName(bundle.getString("newSelectionButtonVisible"));
            pd7.setShortDescription(bundle.getString("newSelectionButtonVisibleTip"));
            pd7.setValue("propertyCategory", bundle.getString("ToolBarButtons"));

            PropertyDescriptor pd8 = new PropertyDescriptor(
                                            "addToSelectionButtonVisible",
                                            QueryComponent.class,
                                            "isAddToSelectionButtonVisible",
                                            "setAddToSelectionButtonVisible");
            pd8.setDisplayName(bundle.getString("addToSelectionButtonVisible"));
            pd8.setShortDescription(bundle.getString("addToSelectionButtonVisibleTip"));
            pd8.setValue("propertyCategory", bundle.getString("ToolBarButtons"));

            PropertyDescriptor pd9 = new PropertyDescriptor(
                                            "removeFromSelectionVisible",
                                            QueryComponent.class,
                                            "isRemoveFromSelectionButtonVisible",
                                            "setRemoveFromSelectionButtonVisible");
            pd9.setDisplayName(bundle.getString("removeFromSelectionVisible"));
            pd9.setShortDescription(bundle.getString("removeFromSelectionVisibleTip"));
            pd9.setValue("propertyCategory", bundle.getString("ToolBarButtons"));

            PropertyDescriptor pd10 = new PropertyDescriptor(
                                            "selectFromSelectionButtonVisible",
                                            QueryComponent.class,
                                            "isSelectFromSelectionButtonVisible",
                                            "setSelectFromSelectionButtonVisible");
            pd10.setDisplayName(bundle.getString("selectFromSelectionButtonVisible"));
            pd10.setShortDescription(bundle.getString("selectFromSelectionButtonVisibleTip"));
            pd10.setValue("propertyCategory", bundle.getString("ToolBarButtons"));


//            ResourceBundle eslateBundle = ResourceBundle.getBundle("gr.cti.eslate.base.ESlateResource",
//                                        Locale.getDefault());
            PropertyDescriptor pd11 = new PropertyDescriptor(
                                            gr.cti.eslate.utils.ESlateBeanResource.getString("border"),
                                            QueryComponent.class,
                                            "getBorder",
                                            "setBorder");
            pd11.setDisplayName(ESlateBeanResource.getString("Border"));
            pd11.setShortDescription(ESlateBeanResource.getString("BorderTip"));

            return new PropertyDescriptor[] {pd1, pd2, pd3, pd4, pd5, pd6, pd7, pd8, pd9, pd10, pd11};

        } catch (IntrospectionException exc) {
            System.out.println("IntrospectionException: " + exc.getMessage());
            return null;
        }
    }
    public EventSetDescriptor[] getEventSetDescriptors() {
        Vector descriptors = new Vector();
        EventSetDescriptor esd = null;

        try{
            Method listenerMethod = PropertyChangeListener.class.getMethod("propertyChange", new Class[] {PropertyChangeEvent.class});
            Method addListenerMethod = QueryComponent.class.getMethod("addPropertyChangeListener", new Class[] {PropertyChangeListener.class});
            Method removelistenerMethod = QueryComponent.class.getMethod("removePropertyChangeListener", new Class[] {PropertyChangeListener.class});
            MethodDescriptor md = new MethodDescriptor(listenerMethod);
            md.setDisplayName(bundle.getString("propertyChange"));
            esd = new EventSetDescriptor("propertyChange",
                                   PropertyChangeListener.class,
                                   new MethodDescriptor[] {md},
                                   addListenerMethod,
                                   removelistenerMethod);
            descriptors.addElement(esd);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        try{
            Method listenerMethod = VetoableChangeListener.class.getMethod("vetoableChange", new Class[] {PropertyChangeEvent.class});
            Method addListenerMethod = QueryComponent.class.getMethod("addVetoableChangeListener", new Class[] {VetoableChangeListener.class});
            Method removelistenerMethod = QueryComponent.class.getMethod("removeVetoableChangeListener", new Class[] {VetoableChangeListener.class});
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
            Method addListenerMethod = QueryComponent.class.getMethod("addMouseListener", new Class[] {MouseListener.class});
            Method removelistenerMethod = QueryComponent.class.getMethod("removeMouseListener", new Class[] {MouseListener.class});
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
            Method addListenerMethod = QueryComponent.class.getMethod("addMouseMotionListener", new Class[] {MouseMotionListener.class});
            Method removelistenerMethod = QueryComponent.class.getMethod("removeMouseMotionListener", new Class[] {MouseMotionListener.class});
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
        
        EventSetDescriptor[] d = new EventSetDescriptor[descriptors.size()];
        for (int i=0; i<d.length; i++)
            d[i] = (EventSetDescriptor) descriptors.elementAt(i);
       return d;
    }

    public Image getIcon(int iconKind) {
//        System.out.println("QueryBeanInfo getIcon()");
        if (iconKind == BeanInfo.ICON_MONO_16x16 || iconKind == BeanInfo.ICON_COLOR_16x16)
            return color16Icon.getImage();
        return null;
    }

}