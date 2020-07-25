package gr.cti.eslate.graph2;

import gr.cti.eslate.utils.ESlateBeanInfo;

import java.beans.PropertyDescriptor;

import javax.swing.ImageIcon;

/**
 * BeanInfo for Graph2 component.
 * @author augril
 */
public class Graph2BeanInfo extends ESlateBeanInfo {
	/**
	 * Construct the BeanInfo.
	 */
	public Graph2BeanInfo() {
		super();
		try {
			set16x16ColorIcon(new ImageIcon(getClass().getResource(
					"images/graph.gif")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the bean's <code>PropertyDescriptor</code>s.
	 * 
	 * @return An array of PropertyDescriptors describing the editable
	 *         properties supported by this bean.
	 */
	public PropertyDescriptor[] getPropertyDescriptors() {
		try {
			PropertyDescriptor propertyDescriptor1 = new PropertyDescriptor(
					"expression", Graph2.class,
					"getExpression", "setExpression");
			propertyDescriptor1.setDisplayName(BundleMessages
					.getMessage("expressionProperty"));
			propertyDescriptor1.setShortDescription(BundleMessages
					.getMessage("expressionPropertyTip"));

			PropertyDescriptor propertyDescriptor2 = new PropertyDescriptor(
					"expressionVisible", Graph2.class,
					"isExpressionVisible", "setExpressionVisible");
			propertyDescriptor2.setDisplayName(BundleMessages
					.getMessage("expressionVisibleProperty"));
			propertyDescriptor2.setShortDescription(BundleMessages
					.getMessage("expressionVisiblePropertyTip"));
			
			PropertyDescriptor[] propertyDescriptors = new PropertyDescriptor[2];
			int i = 0;
			propertyDescriptors[i++] = propertyDescriptor1;
			propertyDescriptors[i++] = propertyDescriptor2;
			return propertyDescriptors;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Gets the bean's <code>EventSetDescriptor</code>s.
	 * 
	 * @return An array of EventSetDescriptors describing the kinds of events
	 *         fired by this bean.
	 */
//	public EventSetDescriptor[] getEventSetDescriptors() {
//		try {
//			Method listenerMethod1 = TimeMachine2Listener.class.getMethod(
//					"scaleChanged", new Class[] { ScaleEvent.class });
//			Method listenerMethod2 = TimeMachine2Listener.class.getMethod(
//					"selectedPeriodChanged",
//					new Class[] { SelectedPeriodEvent.class });
//			Method addTimeMachine2ListenerMethod = TimeMachine2.class
//					.getMethod("addTimeMachine2Listener",
//							new Class[] { TimeMachine2Listener.class });
//			Method removeTimeMachine2ListenerMethod = TimeMachine2.class
//					.getMethod("removeTimeMachine2Listener",
//							new Class[] { TimeMachine2Listener.class });
//
//			MethodDescriptor methodDescriptor1 = new MethodDescriptor(
//					listenerMethod1);
//			methodDescriptor1.setDisplayName(BundleMessages
//					.getMessage("scaleEvent"));
//			MethodDescriptor methodDescriptor2 = new MethodDescriptor(
//					listenerMethod2);
//			methodDescriptor2.setDisplayName(BundleMessages
//					.getMessage("selectedPeriodEvent"));
//			EventSetDescriptor eventSetDescriptor1 = new EventSetDescriptor(
//					"timeMachine2", TimeMachine2Listener.class,
//					new MethodDescriptor[] { methodDescriptor1,
//							methodDescriptor2, },
//					addTimeMachine2ListenerMethod,
//					removeTimeMachine2ListenerMethod);
//
//			EventSetDescriptor[] eventSetDescriptors = new EventSetDescriptor[1];
//			int i = 0;
//			eventSetDescriptors[i++] = eventSetDescriptor1;
//			return eventSetDescriptors;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
}
