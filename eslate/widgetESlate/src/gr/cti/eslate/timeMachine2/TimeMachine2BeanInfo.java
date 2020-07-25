package gr.cti.eslate.timeMachine2;

import gr.cti.eslate.utils.ESlateBeanInfo;

import java.beans.EventSetDescriptor;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import javax.swing.ImageIcon;

/**
 * BeanInfo for Time machine component.
 * 
 * @author augril
 */
public class TimeMachine2BeanInfo extends ESlateBeanInfo {
	/**
	 * Construct the BeanInfo.
	 */
	public TimeMachine2BeanInfo() {
		super();
		try {
			set16x16ColorIcon(new ImageIcon(getClass().getResource(
					"images/timeMachine.gif")));
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
					"leftVisibleYear", TimeMachine2.class,
					"getLeftVisibleYear", "setLeftVisibleYear");
			propertyDescriptor1.setDisplayName(BundleMessages
					.getMessage("leftVisibleYearProperty"));
			propertyDescriptor1.setShortDescription(BundleMessages
					.getMessage("leftVisibleYearPropertyTip"));

			PropertyDescriptor propertyDescriptor2 = new PropertyDescriptor(
					"leftSelectedYear", TimeMachine2.class,
					"getLeftSelectedYear", "setLeftSelectedYear");
			propertyDescriptor2.setDisplayName(BundleMessages
					.getMessage("leftSelectedYearProperty"));
			propertyDescriptor2.setShortDescription(BundleMessages
					.getMessage("leftSelectedYearPropertyTip"));

			PropertyDescriptor propertyDescriptor3 = new PropertyDescriptor(
					"rightSelectedYear", TimeMachine2.class,
					"getRightSelectedYear", "setRightSelectedYear");
			propertyDescriptor3.setDisplayName(BundleMessages
					.getMessage("rightSelectedYearProperty"));
			propertyDescriptor3.setShortDescription(BundleMessages
					.getMessage("rightSelectedYearPropertyTip"));

			PropertyDescriptor propertyDescriptor4 = new PropertyDescriptor(
					"scale", TimeMachine2.class,
					"getScale", "setScale");
			propertyDescriptor4.setDisplayName(BundleMessages
					.getMessage("scaleProperty"));
			propertyDescriptor4.setShortDescription(BundleMessages
					.getMessage("scalePropertyTip"));
			
			PropertyDescriptor propertyDescriptor5 = new PropertyDescriptor(
					"colorPanel", TimeMachine2.class,
					"getColorPanel", "setColorPanel");
			propertyDescriptor5.setDisplayName(BundleMessages
					.getMessage("colorPanelProperty"));
			propertyDescriptor5.setShortDescription(BundleMessages
					.getMessage("colorPanelPropertyTip"));
			
			PropertyDescriptor propertyDescriptor6 = new PropertyDescriptor(
					"colorRuler", TimeMachine2.class,
					"getColorRuler", "setColorRuler");
			propertyDescriptor6.setDisplayName(BundleMessages
					.getMessage("colorRulerProperty"));
			propertyDescriptor6.setShortDescription(BundleMessages
					.getMessage("colorRulerPropertyTip"));
			
			PropertyDescriptor propertyDescriptor7 = new PropertyDescriptor(
					"colorLabels", TimeMachine2.class,
					"getColorLabels", "setColorLabels");
			propertyDescriptor7.setDisplayName(BundleMessages
					.getMessage("colorLabelsProperty"));
			propertyDescriptor7.setShortDescription(BundleMessages
					.getMessage("colorLabelsPropertyTip"));
			
			PropertyDescriptor propertyDescriptor8 = new PropertyDescriptor(
					"colorCursor", TimeMachine2.class,
					"getColorCursor", "setColorCursor");
			propertyDescriptor8.setDisplayName(BundleMessages
					.getMessage("colorCursorProperty"));
			propertyDescriptor8.setShortDescription(BundleMessages
					.getMessage("colorCursorPropertyTip"));
			
			PropertyDescriptor[] propertyDescriptors = new PropertyDescriptor[8];
			int i = 0;
			propertyDescriptors[i++] = propertyDescriptor1;
			propertyDescriptors[i++] = propertyDescriptor2;
			propertyDescriptors[i++] = propertyDescriptor3;
			propertyDescriptors[i++] = propertyDescriptor4;
			propertyDescriptors[i++] = propertyDescriptor5;
			propertyDescriptors[i++] = propertyDescriptor6;
			propertyDescriptors[i++] = propertyDescriptor7;
			propertyDescriptors[i++] = propertyDescriptor8;
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
	public EventSetDescriptor[] getEventSetDescriptors() {
		try {
			Method listenerMethod1 = TimeMachine2Listener.class.getMethod(
					"scaleChanged", new Class[] { ScaleEvent.class });
			Method listenerMethod2 = TimeMachine2Listener.class.getMethod(
					"selectedPeriodChanged",
					new Class[] { SelectedPeriodEvent.class });
			Method addTimeMachine2ListenerMethod = TimeMachine2.class
					.getMethod("addTimeMachine2Listener",
							new Class[] { TimeMachine2Listener.class });
			Method removeTimeMachine2ListenerMethod = TimeMachine2.class
					.getMethod("removeTimeMachine2Listener",
							new Class[] { TimeMachine2Listener.class });

			MethodDescriptor methodDescriptor1 = new MethodDescriptor(
					listenerMethod1);
			methodDescriptor1.setDisplayName(BundleMessages
					.getMessage("scaleEvent"));
			MethodDescriptor methodDescriptor2 = new MethodDescriptor(
					listenerMethod2);
			methodDescriptor2.setDisplayName(BundleMessages
					.getMessage("selectedPeriodEvent"));
			EventSetDescriptor eventSetDescriptor1 = new EventSetDescriptor(
					"timeMachine2", TimeMachine2Listener.class,
					new MethodDescriptor[] { methodDescriptor1,
							methodDescriptor2, },
					addTimeMachine2ListenerMethod,
					removeTimeMachine2ListenerMethod);

			EventSetDescriptor[] eventSetDescriptorsSuper = super.getEventSetDescriptors();
			EventSetDescriptor[] eventSetDescriptors = new EventSetDescriptor[1+eventSetDescriptorsSuper.length];
			int i = 0;
			for (int j = 0; j < eventSetDescriptorsSuper.length; j++)
				eventSetDescriptors[i++] = eventSetDescriptorsSuper[j];
			eventSetDescriptors[i++] = eventSetDescriptor1;
			return eventSetDescriptors;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
