package gr.cti.eslate.agent;

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

public class AgentBeanInfo extends ESlateBeanInfo {
    public AgentBeanInfo() {
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        try{
            PropertyDescriptor pd1 = new PropertyDescriptor("phaseImages",Agent.class);
            pd1.setDisplayName(bundle.getString("phaseImages"));
            pd1.setShortDescription(bundle.getString("phaseImagesTip"));
            pd1.setPropertyEditorClass(EditorAgentFace.class);

            PropertyDescriptor pd2 = new PropertyDescriptor("type",Agent.class);
            pd2.setDisplayName(bundle.getString("type"));
            pd2.setShortDescription(bundle.getString("typeTip"));
            pd2.setPropertyEditorClass(EditorType.class);

            PropertyDescriptor pd3 = new PropertyDescriptor("componentFace",Agent.class);
            pd3.setDisplayName(bundle.getString("componentFace"));
            pd3.setShortDescription(bundle.getString("componentFaceTip"));

            PropertyDescriptor pd4 = new PropertyDescriptor("travellingOnMotionLayerID",Agent.class);
            pd4.setDisplayName(bundle.getString("travellingOnMotionLayerID"));
            pd4.setShortDescription(bundle.getString("travellingOnMotionLayerIDTip"));
            pd4.setPropertyEditorClass(EditorTravelsOnID.class);

            PropertyDescriptor pd5 = new PropertyDescriptor("embarksOn",Agent.class);
            pd5.setDisplayName(bundle.getString("embarksOn"));
            pd5.setShortDescription(bundle.getString("embarksOnTip"));
            pd5.setPropertyEditorClass(EditorEmbarksOn.class);

            PropertyDescriptor pd6 = new PropertyDescriptor("potentialVelocity",Agent.class);
            pd6.setDisplayName(bundle.getString("velocity"));
            pd6.setShortDescription(bundle.getString("velocityTip"));

            PropertyDescriptor pd7 = new PropertyDescriptor("minVelocity",Agent.class);
            pd7.setDisplayName(bundle.getString("minVelocity"));
            pd7.setShortDescription(bundle.getString("minVelocityTip"));

            PropertyDescriptor pd8 = new PropertyDescriptor("maxVelocity",Agent.class);
            pd8.setDisplayName(bundle.getString("maxVelocity"));
            pd8.setShortDescription(bundle.getString("maxVelocityTip"));

            PropertyDescriptor pd9 = new PropertyDescriptor("border",Agent.class);
            pd9.setDisplayName(bundle.getString("border"));
            pd9.setShortDescription(bundle.getString("borderTip"));

            PropertyDescriptor pd10 = new PropertyDescriptor("statusbarVisible",Agent.class);
            pd10.setDisplayName(bundle.getString("statusbarVisible"));
            pd10.setShortDescription(bundle.getString("statusbarVisibleTip"));

            PropertyDescriptor pd11 = new PropertyDescriptor("fooInitLoc",Agent.class);
            pd11.setDisplayName(bundle.getString("initLoc"));
            pd11.setShortDescription(bundle.getString("initLocTip"));
            pd11.setPropertyEditorClass(EditorInitLoc.class);

            PropertyDescriptor pd12 = new PropertyDescriptor("alwaysVisible",Agent.class);
            pd12.setDisplayName(bundle.getString("alwaysVisible"));
            pd12.setShortDescription(bundle.getString("alwaysVisibleTip"));

            PropertyDescriptor pd13 = new PropertyDescriptor("unitTolerance",Agent.class);
            pd13.setDisplayName(bundle.getString("unitTolerance"));
            pd13.setShortDescription(bundle.getString("unitToleranceTip"));

            PropertyDescriptor pd14 = new PropertyDescriptor("stopAtCrossings",Agent.class);
            pd14.setDisplayName(bundle.getString("stopAtCrossings"));
            pd14.setShortDescription(bundle.getString("stopAtCrossingsTip"));

            return new PropertyDescriptor[] {pd1,pd2,pd3,pd4,pd5,pd6,pd7,pd8,pd9,pd10,pd11,pd12,pd13,pd14};
        } catch (IntrospectionException exc) {
            System.out.println("AGENT#200005091427: IntrospectionException: " + exc.getMessage());
            return null;
        }
    }

    public EventSetDescriptor[] getEventSetDescriptors() {
        EventSetDescriptor ed1=null;
        EventSetDescriptor ed2=null;
        EventSetDescriptor ed3=null;
        EventSetDescriptor ed4=null;

	    try {
	        Method listenerMethod = AgentListener.class.getMethod("locationChanged", new Class[] {AgentLocationChangedEvent.class});
	        Method addListenerMethod = Agent.class.getMethod("addAgentListener", new Class[] {AgentListener.class});
	        Method removelistenerMethod = Agent.class.getMethod("removeAgentListener", new Class[] {AgentListener.class});
	        MethodDescriptor md = new MethodDescriptor(listenerMethod);
	        md.setDisplayName(bundle.getString("locationChanged"));
	        ed1 = new EventSetDescriptor("locationChanged",
	                               AgentListener.class,
	                               new MethodDescriptor[] {md},
	                               addListenerMethod,
	                               removelistenerMethod);
	    } catch(Exception exc) {
	        exc.printStackTrace();
	    }

/*
	    try {
	        Method listenerMethod = AgentListener.class.getMethod("agentStopped", new Class[] {AgentEvent.class});
	        Method addListenerMethod = Agent.class.getMethod("addAgentListener", new Class[] {AgentListener.class});
	        Method removelistenerMethod = Agent.class.getMethod("removeAgentListener", new Class[] {AgentListener.class});
	        MethodDescriptor md = new MethodDescriptor(listenerMethod);
	        md.setDisplayName(bundle.getString("agentStopped"));
	        ed1 = new EventSetDescriptor("agentStopped",
	                               AgentListener.class,
	                               new MethodDescriptor[] {md},
	                               addListenerMethod,
	                               removelistenerMethod);
	    } catch(Exception exc) {
	        exc.printStackTrace();
	    }
*/

        try {
            Method listenerMethod = AgentListener.class.getMethod("agentMeeting", new Class[] {AgentMeetingEvent.class});
            Method addListenerMethod = Agent.class.getMethod("addAgentListener", new Class[] {AgentListener.class});
            Method removelistenerMethod = Agent.class.getMethod("removeAgentListener", new Class[] {AgentListener.class});
            MethodDescriptor md = new MethodDescriptor(listenerMethod);
            md.setDisplayName(bundle.getString("agentMeeting"));
            ed2 = new EventSetDescriptor("agentMeeting",
                                   AgentListener.class,
                                   new MethodDescriptor[] {md},
                                   addListenerMethod,
                                   removelistenerMethod);
        } catch(Exception exc) {
            exc.printStackTrace();
        }

        try {
            Method listenerMethod = AgentListener.class.getMethod("geographicObjectTouched", new Class[] {GeoObjectAgentEvent.class});
            Method addListenerMethod = Agent.class.getMethod("addAgentListener", new Class[] {AgentListener.class});
            Method removelistenerMethod = Agent.class.getMethod("removeAgentListener", new Class[] {AgentListener.class});
            MethodDescriptor md = new MethodDescriptor(listenerMethod);
            md.setDisplayName(bundle.getString("geographicObjectTouched"));
            ed3 = new EventSetDescriptor("geographicObjectTouched",
                                   AgentListener.class,
                                   new MethodDescriptor[] {md},
                                   addListenerMethod,
                                   removelistenerMethod);
        } catch(Exception exc) {
            exc.printStackTrace();
        }

        try {
            Method listenerMethod = AgentListener.class.getMethod("motionObjectChanged", new Class[] {GeoObjectAgentEvent.class});
            Method addListenerMethod = Agent.class.getMethod("addAgentListener", new Class[] {AgentListener.class});
            Method removelistenerMethod = Agent.class.getMethod("removeAgentListener", new Class[] {AgentListener.class});
            MethodDescriptor md = new MethodDescriptor(listenerMethod);
            md.setDisplayName(bundle.getString("motionObjectChanged"));
            ed4 = new EventSetDescriptor("motionObjectChanged",
                                   AgentListener.class,
                                   new MethodDescriptor[] {md},
                                   addListenerMethod,
                                   removelistenerMethod);
        } catch(Exception exc) {
            exc.printStackTrace();
        }

        //Combine my event descriptors with my parent's descriptors.
        EventSetDescriptor[] par=super.getEventSetDescriptors();
        EventSetDescriptor[] my=new EventSetDescriptor[par.length+4];

        System.arraycopy(par,0,my,0,par.length);
        my[my.length-4]=ed1;
        my[my.length-3]=ed2;
        my[my.length-2]=ed3;
        my[my.length-1]=ed4;

        return my;
    }

    public Image getIcon(int iconKind) {
        if (iconKind == BeanInfo.ICON_MONO_16x16 || iconKind == BeanInfo.ICON_COLOR_16x16)
            return mono16Icon.getImage();
        return null;
    }

    protected static ResourceBundle bundle=ResourceBundle.getBundle("gr.cti.eslate.agent.BundleAgentBeanInfo",Locale.getDefault());
    private ImageIcon mono16Icon = new ImageIcon(Agent.class.getResource("images/agentBeanIcon.gif"));
}