//Copyright: (C)opyright 1999-2002 George Birbilis <birbilis@kagi.com>

package gr.cti.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.beans.Introspector;

/**
 * JavaBean/Reflection Utilities
 *
 * @version 2.0.0, 19-May-2006
 * @author George Birbilis [birbilis@cti.gr]
 */
public class BeanUtil {

 /**
  * Get a property given an object and a property name
  * @param bean the object
  * @param propertyName the property's name (Java will use Reflection or a bean descriptor class if one is available for this object [use bean descriptors to provide localized property names])
  * @return the property value
  */
 public static Object getProperty(Object bean, String propertyName) throws Exception{
  Method getter=new PropertyDescriptor(propertyName,bean.getClass()).getReadMethod();
  return getter.invoke(bean);
 }

 /**
  * Set a property given an object, a property name and a value for it
  * @param bean the object
  * @param propertyName the property's name (Java will use Reflection or a bean descriptor class if one is available for this object [use bean descriptors to provide localized property names])
  * @param value the value (use java.lang.Integer for int properties, java.lang.Double for doubles etc.)
  */
 public static void setProperty(Object bean, String propertyName,Object value) throws Exception{
  Method setter=new PropertyDescriptor(propertyName,bean.getClass()).getWriteMethod();
  setter.invoke(bean,new Object[]{value});
 }

 /**
  * Execute a parameter-less method (a "verb") of an object
  * @param bean the object
  * @param verb the parameter-less method's name (no localization support :o(
  */
 public static Object doVerb(Object bean,String verb) throws Exception{
  //System.out.println("BeanUtil.doVerb "+verb);
  return bean.getClass().getMethod(verb).invoke(bean);
 }

 /**
  * Get a property type, given an object and a property name
  * @param bean the object
  * @param propertyName the property's name (Java will use Reflection or a bean descriptor class if one is available for this object [use bean descriptors to provide localized property names])
  * @return the property class
  * @since 8Aug2001
  */
 public static Class getPropertyType(Object bean, String propertyName) throws Exception{
  return new PropertyDescriptor(propertyName,bean.getClass()).getPropertyType();
 }

 /**
  * Get the names for the properties of an object
  * @param bean the object
  * @return the properties' names in a string array
  */
 public static String[] getPropertyNames(Object bean) throws Exception{
  PropertyDescriptor[] properties=Introspector.getBeanInfo(bean.getClass()).getPropertyDescriptors();
  int count=properties.length;
  String[] result=new String[count];
  for(int i=count;i-->0;)
   result[i]=properties[i].getName();
  return result;
 }

 /**
  * Get any display names (localized) for the properties of an object
  * @param bean the object
  * @return the properties' display names in a string array
  */
 public static String[] getPropertyDisplayNames(Object bean) throws Exception{
  PropertyDescriptor[] properties=Introspector.getBeanInfo(bean.getClass()).getPropertyDescriptors();
  int count=properties.length;
  String[] result=new String[count];
  for(int i=count;i-->0;)
   result[i]=properties[i].getDisplayName();
  return result;
 }

 /**
  * Get any short descriptions (localized) for the properties of an object
  * @param bean the object
  * @return the properties' short descriptions in a string array
  */
 public static String[] getPropertyShortDescriptions(Object bean) throws Exception{
  PropertyDescriptor[] properties=Introspector.getBeanInfo(bean.getClass()).getPropertyDescriptors();
  int count=properties.length;
  String[] result=new String[count];
  for(int i=count;i-->0;)
   result[i]=properties[i].getShortDescription();
  return result;
 }

 /**
  * Get the types for the properties of an object
  * @param bean the object
  * @return the properties' class names in a string array
  * @since 8Aug2001
  */
 public static String[] getPropertyTypes(Object bean) throws Exception{
  PropertyDescriptor[] properties=Introspector.getBeanInfo(bean.getClass()).getPropertyDescriptors();
  int count=properties.length;
  String[] result=new String[count];
  for(int i=count;i-->0;)
   result[i]=properties[i].getPropertyType().getName();
  return result;
 }

 /**
  * Get the number of parameters a method has
  * @param method the method
  * @return the parameters count for the method
  */
 public static int getParametersCount(Method method){
  return method.getParameterTypes().length;
 }

 /**
  * Get the names for the object's methods which take no parameters (verbs)
  * @param bean the object
  * @return the verbs' names in a string array
  */
 public static String[] getVerbs(Object bean) throws Exception{
  Method[] methods=bean.getClass().getMethods();

  //count the verbs//
  int methodsCount=methods.length;
  int verbsCount=0;
  for(int i=methodsCount;i-->0;)
   if(getParametersCount(methods[i])==0) verbsCount++;

  //get the verb names//
  String[] result=new String[verbsCount];
  for(int i=methodsCount;i-->0;)
   if(getParametersCount(methods[i])==0) result[--verbsCount]=methods[i].getName();

  return result;
 }

}

