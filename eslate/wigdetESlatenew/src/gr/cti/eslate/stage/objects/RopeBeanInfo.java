package gr.cti.eslate.stage.objects;

import java.beans.*;
import java.lang.reflect.*;
import java.util.*;

import gr.cti.eslate.protocol.*;

/**
 * BeanInfo for Rope object.
 *
 * @version     2.0.13, 24-Jun-2008
 * @author      Kriton Kyrimis
 */
public class RopeBeanInfo extends SimpleBeanInfo
{
  /**
   * Localized resources.
   */
  ResourceBundle resources = ResourceBundle.getBundle(
    "gr.cti.eslate.stage.objects.RopeResource",
    Locale.getDefault()
  );

  /**
   * Gets the bean's <code>PropertyDescriptor</code>s.
   * @return    An array of PropertyDescriptors describing the editable
   *            properties supported by this bean.
   */
  public PropertyDescriptor[] getPropertyDescriptors()
  {
    try {
      ArrayList<PropertyDescriptor> pds = new ArrayList<PropertyDescriptor>();
      PropertyDescriptor pd;

      pd = new PropertyDescriptor(
        "altitude", Rope.class,
        "getAltitude", "setAltitude"
      );
      pd.setDisplayName(resources.getString("setAltitude"));
      pd.setShortDescription(resources.getString("setAltitudeTip"));
      pds.add(pd);

      pd = new PropertyDescriptor(
        "angle", Rope.class,
        "getAngle", "setAngle"
      );
      pd.setDisplayName(resources.getString("setAngle"));
      pd.setShortDescription(resources.getString("setAngleTip"));
      pds.add(pd);

      pd = new PropertyDescriptor(
        "color", Rope.class,
        "getColor", "setColor"
      );
      pd.setDisplayName(resources.getString("setColor"));
      pd.setShortDescription(resources.getString("setColorTip"));
      pds.add(pd);

      pd = new PropertyDescriptor(
        "image", Rope.class,
        "getImage", "setImage"
      );
      pd.setDisplayName(resources.getString("setImage"));
      pd.setShortDescription(resources.getString("setImageTip"));
      pds.add(pd);

      pd = new PropertyDescriptor(
        "length", Rope.class,
        "getLength", "setLength"
      );
      pd.setDisplayName(resources.getString("setLength"));
      pd.setShortDescription(resources.getString("setLengthTip"));
      pds.add(pd);

      pd = new PropertyDescriptor(
        "mass", Rope.class,
        "getMass", "setMass"
      );
      pd.setDisplayName(resources.getString("setMass"));
      pd.setShortDescription(resources.getString("setMassTip"));
      pds.add(pd);

      pd = new PropertyDescriptor(
        "name", Rope.class,
        "getName", "setName"
      );
      pd.setDisplayName(resources.getString("setName"));
      pd.setShortDescription(resources.getString("setNameTip"));
      pds.add(pd);

      pd = new PropertyDescriptor(
        "preparedForCopy", Rope.class,
        "isPreparedForCopy", "setPreparedForCopy"
      );
      pd.setDisplayName(resources.getString("setPreparedForCopy"));
      pd.setShortDescription(resources.getString("setPreparedForCopyTip"));
      pds.add(pd);

      return pds.toArray(new PropertyDescriptor[0]);
    } catch (Exception e) {
      e.printStackTrace();
      return new PropertyDescriptor[0];
    }
  }

  /**
   * Gets the bean's <code>EventSetDescriptor</code>s.
   * @return    An array of EventSetDescriptors describing the kinds of events
   *            fired by this bean.
   */
  public EventSetDescriptor[] getEventSetDescriptors()
  {
    try {
      ArrayList<EventSetDescriptor> esds = new ArrayList<EventSetDescriptor>();
      Method listenerMethod, addListenerMethod, removeListenerMethod;
      MethodDescriptor md;
      EventSetDescriptor esd;

      listenerMethod = ActorNameListener.class.getMethod(
        "actorNameChanged", new Class[] {ActorNameEvent.class}
      );
      addListenerMethod = Rope.class.getMethod(
        "addActorNameListener", new Class[] {ActorNameListener.class}
      );
      removeListenerMethod = Rope.class.getMethod(
        "removeActorNameListener", new Class[] {ActorNameListener.class}
      );
      md = new MethodDescriptor(listenerMethod);
      md.setDisplayName(resources.getString("actorNameChanged"));
      esd = new EventSetDescriptor(
        "actorName", ActorNameListener.class,
        new MethodDescriptor[] {md},
        addListenerMethod, removeListenerMethod
      );
      esds.add(esd);

      listenerMethod = PropertyChangeListener.class.getMethod(
        "propertyChange", new Class[] {PropertyChangeEvent.class}
      );
      addListenerMethod = Rope.class.getMethod(
        "addPropertyChangeListener", new Class[] {PropertyChangeListener.class}
      );
      removeListenerMethod = Rope.class.getMethod(
        "removePropertyChangeListener",
        new Class[] {PropertyChangeListener.class}
      );
      md = new MethodDescriptor(listenerMethod);
      md.setDisplayName(resources.getString("propertyChange"));
      esd = new EventSetDescriptor(
        "propertyChange", PropertyChangeListener.class,
        new MethodDescriptor[] {md},
        addListenerMethod, removeListenerMethod
      );
      esds.add(esd);

      listenerMethod = VetoableChangeListener.class.getMethod(
        "vetoableChange", new Class[] {PropertyChangeEvent.class}
      );
      addListenerMethod = Rope.class.getMethod(
        "addVetoableChangeListener", new Class[] {VetoableChangeListener.class}
      );
      removeListenerMethod = Rope.class.getMethod(
        "removeVetoableChangeListener",
        new Class[] {VetoableChangeListener.class}
      );
      md = new MethodDescriptor(listenerMethod);
      md.setDisplayName(resources.getString("vetoableChange"));
      esd = new EventSetDescriptor(
        "vetoableChange", VetoableChangeListener.class,
        new MethodDescriptor[] {md},
        addListenerMethod, removeListenerMethod
      );
      esds.add(esd);

      return esds.toArray(new EventSetDescriptor[0]);
    } catch (Exception e) {
      e.printStackTrace();
      return new EventSetDescriptor[0];
    }
  }
}
