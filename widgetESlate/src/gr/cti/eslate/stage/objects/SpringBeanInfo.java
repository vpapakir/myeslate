package gr.cti.eslate.stage.objects;

import java.beans.*;
import java.lang.reflect.*;
import java.util.*;

import gr.cti.eslate.protocol.*;

/**
 * BeanInfo for Spring object.
 *
 * @version     2.0.13, 24-Jun-2008
 * @author      Kriton Kyrimis
 */
public class SpringBeanInfo extends SimpleBeanInfo
{
  /**
   * Localized resources.
   */
  ResourceBundle resources = ResourceBundle.getBundle(
    "gr.cti.eslate.stage.objects.SpringResource",
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
        "altitude", Spring.class,
        "getAltitude", "setAltitude"
      );
      pd.setDisplayName(resources.getString("setAltitude"));
      pd.setShortDescription(resources.getString("setAltitudeTip"));
      pds.add(pd);

      pd = new PropertyDescriptor(
        "angle", Spring.class,
        "getAngle", "setAngle"
      );
      pd.setDisplayName(resources.getString("setAngle"));
      pd.setShortDescription(resources.getString("setAngleTip"));
      pds.add(pd);

      pd = new PropertyDescriptor(
        "color", Spring.class,
        "getColor", "setColor"
      );
      pd.setDisplayName(resources.getString("setColor"));
      pd.setShortDescription(resources.getString("setColorTip"));
      pds.add(pd);

      pd = new PropertyDescriptor(
        "displacement", Spring.class,
        "getDisplacement", "setDisplacement"
      );
      pd.setDisplayName(resources.getString("setDisplacement"));
      pd.setShortDescription(resources.getString("setDisplacementTip"));
      pds.add(pd);

      pd = new PropertyDescriptor(
        "image", Spring.class,
        "getImage", "setImage"
      );
      pd.setDisplayName(resources.getString("setImage"));
      pd.setShortDescription(resources.getString("setImageTip"));
      pds.add(pd);

      pd = new PropertyDescriptor(
        "length", Spring.class,
        "getLength", "setLength"
      );
      pd.setDisplayName(resources.getString("setLength"));
      pd.setShortDescription(resources.getString("setLengthTip"));
      pds.add(pd);

      pd = new PropertyDescriptor(
        "mass", Spring.class,
        "getMass", "setMass"
      );
      pd.setDisplayName(resources.getString("setMass"));
      pd.setShortDescription(resources.getString("setMassTip"));
      pds.add(pd);

      pd = new PropertyDescriptor(
        "maximumLength", Spring.class,
        "getMaximumLength", "setMaximumLength"
      );
      pd.setDisplayName(resources.getString("setMaximumLength"));
      pd.setShortDescription(resources.getString("setMaximumLengthTip"));
      pds.add(pd);

      pd = new PropertyDescriptor(
        "name", Spring.class,
        "getName", "setName"
      );
      pd.setDisplayName(resources.getString("setName"));
      pd.setShortDescription(resources.getString("setNameTip"));
      pds.add(pd);

      pd = new PropertyDescriptor(
        "naturalLength", Spring.class,
        "getNaturalLength", "setNaturalLength"
      );
      pd.setDisplayName(resources.getString("setNaturalLength"));
      pd.setShortDescription(resources.getString("setNaturalLengthTip"));
      pds.add(pd);

      pd = new PropertyDescriptor(
        "preparedForCopy", Spring.class,
        "isPreparedForCopy", "setPreparedForCopy"
      );
      pd.setDisplayName(resources.getString("setPreparedForCopy"));
      pd.setShortDescription(resources.getString("setPreparedForCopyTip"));
      pds.add(pd);

      pd = new PropertyDescriptor(
        "springConstant", Spring.class,
        "getSpringConstant", "setSpringConstant"
      );
      pd.setDisplayName(resources.getString("setSpringConstant"));
      pd.setShortDescription(resources.getString("setSpringConstantTip"));
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
      addListenerMethod = Spring.class.getMethod(
        "addActorNameListener", new Class[] {ActorNameListener.class}
      );
      removeListenerMethod = Spring.class.getMethod(
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
      addListenerMethod = Spring.class.getMethod(
        "addPropertyChangeListener", new Class[] {PropertyChangeListener.class}
      );
      removeListenerMethod = Spring.class.getMethod(
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
      addListenerMethod = Spring.class.getMethod(
        "addVetoableChangeListener", new Class[] {VetoableChangeListener.class}
      );
      removeListenerMethod = Spring.class.getMethod(
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
