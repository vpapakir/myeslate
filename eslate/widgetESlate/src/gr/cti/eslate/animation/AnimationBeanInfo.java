package gr.cti.eslate.animation;

import javax.swing.*;
import java.util.*;
import java.beans.*;
import java.lang.reflect.*;

import gr.cti.eslate.utils.*;

/**
 * BeanInfo for Animation component.
 *
 * @author	Augustine Grillakis
 * @version	1.0.0, 28-Apr-2002
 */
public class AnimationBeanInfo extends ESlateBeanInfo {
  /**
   * Localized resources.
   */
  ResourceBundle resources = ResourceBundle.getBundle(
    "gr.cti.eslate.animation.AnimationResource", Locale.getDefault());

  /**
   * Construct the BeanInfo.
   */
  public AnimationBeanInfo() {
    super();
    try {
      set16x16ColorIcon(
        new ImageIcon(getClass().getResource("images/animation16x16.gif"))
      );
      set32x32ColorIcon(
        new ImageIcon(getClass().getResource("images/animation32x32.gif"))
      );
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Gets the bean's <code>PropertyDescriptor</code>s.
   * @return	An array of PropertyDescriptors describing the editable
   *		properties supported by this bean.
   */
/*  public PropertyDescriptor[] getPropertyDescriptors () {
    try {
      PropertyDescriptor pd1 =
        new PropertyDescriptor ("Delay", Animation.class,
                                "getDelay", "setDelay");
      pd1.setDisplayName(resources.getString("setDelay"));
      pd1.setShortDescription(resources.getString("setDelayTip"));

      PropertyDescriptor [] pd = new PropertyDescriptor[1];
      int i=0;
      pd[i++] = pd1;
      return pd;

    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }*/

  /**
   * Gets the bean's <code>EventSetDescriptor</code>s.
   * @return	An array of EventSetDescriptors describing the kinds of events
   *		fired by this bean.
   */
  public EventSetDescriptor[] getEventSetDescriptors()
  {
    try {
      Method listenerMethod1 = AnimationModelListener.class.getMethod(
        "timeChanged", new Class[] {CursorEvent.class}
      );
      Method addCursorListenerMethod = Animation.class.getMethod(
        "addAnimationModelListener", new Class[] {AnimationModelListener.class}
      );
      Method removeCursorListenerMethod = Animation.class.getMethod(
        "removeAnimationModelListener", new Class[] {AnimationModelListener.class}
      );
      Method listenerMethod2 = MilestoneProcessListener.class.getMethod(
        "milestoneFound", new Class[] {MilestoneProcessEvent.class}
      );
      Method addMilestoneProcessListenerMethod = Animation.class.getMethod(
        "addMilestoneProcessListener", new Class[] {MilestoneProcessListener.class}
      );
      Method removeMilestoneProcessListenerMethod = Animation.class.getMethod(
        "removeMilestoneProcessListener", new Class[] {MilestoneProcessListener.class}
      );
      Method listenerMethod3 = SegmentProcessListener.class.getMethod(
        "segmentEntered", new Class[] {SegmentProcessEvent.class}
      );
      Method listenerMethod4 = SegmentProcessListener.class.getMethod(
        "segmentExited", new Class[] {SegmentProcessEvent.class}
      );
      Method addSegmentProcessListenerMethod = Animation.class.getMethod(
        "addSegmentProcessListener", new Class[] {SegmentProcessListener.class}
      );
      Method removeSegmentProcessListenerMethod = Animation.class.getMethod(
        "removeSegmentProcessListener", new Class[] {SegmentProcessListener.class}
      );

      MethodDescriptor md1 = new MethodDescriptor(listenerMethod1);
      md1.setDisplayName(resources.getString("timeChanged"));
      MethodDescriptor md2 = new MethodDescriptor(listenerMethod2);
      md2.setDisplayName(resources.getString("milestoneFound"));
      MethodDescriptor md3 = new MethodDescriptor(listenerMethod3);
      md3.setDisplayName(resources.getString("segmentEnter"));
      MethodDescriptor md4 = new MethodDescriptor(listenerMethod4);
      md4.setDisplayName(resources.getString("segmentExit"));
      EventSetDescriptor esd1 = new EventSetDescriptor(
        "cursor", AnimationModelListener.class,
	new MethodDescriptor[] {md1},
	addCursorListenerMethod, removeCursorListenerMethod
      );
      EventSetDescriptor esd2 = new EventSetDescriptor(
        "milestoneProcess", MilestoneProcessListener.class,
	new MethodDescriptor[] {md2},
	addMilestoneProcessListenerMethod, removeMilestoneProcessListenerMethod
      );
      EventSetDescriptor esd3 = new EventSetDescriptor(
        "segmentProcess", SegmentProcessListener.class,
	new MethodDescriptor[] {md3, md4},
	addSegmentProcessListenerMethod, removeSegmentProcessListenerMethod
      );

      EventSetDescriptor[] esd =
        new EventSetDescriptor[3];

      int i=0;
      esd[i++] = esd1;
      esd[i++] = esd2;
      esd[i++] = esd3;
      return esd;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
