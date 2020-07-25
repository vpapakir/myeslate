//Title:        VectorUtil
//Version:      18May1999
//Copyright:    Copyright (c)1999
//Author:       George Birbilis
//Company:      CTI
//Description:  Vector utilities

package gr.cti.utils;

import java.util.*;
import javax.swing.JComponent;

@SuppressWarnings(value={"unchecked"})
public class VectorUtil {

 public static void addIfNotExists(Vector v, Object o){ //7Apr2000
  if(!v.contains(o)) v.addElement(o);
 }

 public static Vector enumerationToVector(Enumeration e){ //27-3-1999
  Vector v=new Vector();
  while(e.hasMoreElements()) v.addElement(e.nextElement());
  return v;
 }

 public static Vector addAll(Vector v,Enumeration e){ //27-3-1999: adding all elements of an enumeration to the end of a vector and return that same vector
  while(e.hasMoreElements()) v.addElement(e.nextElement());
  return v;
 }

 public static Vector addAll(Vector v,Vector v1){ //27-3-1999: adding all elements of v1 to the end of v and return v
  if(v==null) v=new Vector(); //8-4-1999: if v is null, then create a new Vector
  for(int i=0,count=v1.size();i<count;i++)
   v.addElement(v1.elementAt(i));
  return v;
 }

 public static Vector addAll(Vector v,Object[] o){ //18May1999: adding all elements of a 1D Object array to the end of a vector and return that same vector
  for(int i=0;i<o.length;i++) v.addElement(o[i]);
  return v;
 }

 public static Object[] getElements(Vector v){ //26Jul1999: returns a Vector's elements as an Object array
  Object[] o=new Object[v.size()];
  for(int i=o.length;i-->0;) o[i]=v.elementAt(i);
  return o;
 }

 public static JComponent[] makeJComponentArray(Vector v){ //29Ju11999
  JComponent[] c=new JComponent[v.size()];
  try{
   for(int i=c.length;i-->0;) c[i]=(JComponent)v.elementAt(i);
  }catch(ClassCastException e){throw new RuntimeException("makeJComponentArrayFromVector couldn't cast some item to a JComponent");}
  return c;
 }

}
