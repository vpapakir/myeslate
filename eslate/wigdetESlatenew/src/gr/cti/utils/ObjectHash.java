//Title:        utilsBirb
//Version:      15Apr2000
//Copyright:    Copyright (c) 1999-2000
//Author:       George Birbilis
//Company:      CTI
//Description:  My utilities

package gr.cti.utils;

import java.util.*;
import java.awt.Color;

@SuppressWarnings(value={"unchecked"})
public class ObjectHash extends Hashtable {

 static final long serialVersionUID = -4975697475236812499L; //15Apr2000: for compatibity with older saved versions so that we can add methods in the future 

 public ObjectHash(int p0, float p1) {
  super(p0,p1);
 }

 public ObjectHash(int p0) {
  super(p0);
 }

 public ObjectHash() {
  super();
 }

 //utility methods similar to those in java.lang.DataInput/java.lang.DataOutput interfaces

 public Object put(Object key,Object o){ //15Apr2000: putting an object to the hash table only if it's not null (avoid having the client to check for null before calling "put" for some object reference)
  if(o!=null) return super.put(key,o); else return null;
 }

 //

 public void putBoolean(Object key, boolean b){
  super.put(key,new Boolean(b));
 }

 public Boolean put(Object key, boolean b){ //15Apr2000
  Boolean bb=new Boolean(b);
  super.put(key,bb);
  return bb;
 }

 //

 public void putInt(Object key, int i){
  super.put(key,new Integer(i));
 }

 public Integer put(Object key, int i){ //15Apr2000
  Integer ii=new Integer(i);
  super.put(key,ii);
  return ii;
 }

 //

 public void putLong(Object key,long l){ //15Apr2000
  super.put(key,new Long(l));
 }

 public Long put(Object key,long l){ //15Apr2000
  Long ll=new Long(l);
  super.put(key,ll);
  return ll;
 }

 //

 public void putShort(Object key,short s){ //15Apr2000
  super.put(key,new Short(s));
 }

 public Short put(Object key,short s){ //15Apr2000
  Short ss=new Short(s);
  super.put(key,ss);
  return ss;
 }

 //

 public void putByte(Object key,byte b){ //15Apr2000
  super.put(key,new Byte(b));
 }

 public Byte put(Object key,byte b){ //15Apr2000
  Byte bb=new Byte(b);
  super.put(key,bb);
  return bb;
 }

 //

 public void putDouble(Object key, double d){
  super.put(key,new Double(d));
 }

 public Double put(Object key,double d){ //15Apr2000
  Double dd=new Double(d);
  super.put(key,dd);
  return dd;
 }

 //

 public void putString(Object key, String s){
  put(key,s); //15Apr2000: removed redundant string creation, was doing "new String(s)"
 }

 public void putColor(Object key, Color c){ //15Apr2000
  put(key,c);
 }

 ///////////////

 public boolean getBoolean(Object key){
  return ((Boolean)get(key)).booleanValue();
 }

 public boolean getBoolean(Object key, boolean defaultValue){ //15Apr2000
  try{return getBoolean(key);}catch(Exception e){return defaultValue;}
 }

 //

 public int getInt(Object key){
  return ((Integer)get(key)).intValue();
 }

 public int getInt(Object key,int defaultValue){ //15Apr2000
  try{return getInt(key);}catch(Exception e){return defaultValue;}
 }

 //

 public long getLong(Object key){ //15Apr2000
  return ((Long)get(key)).longValue();
 }

 public long getLong(Object key,long defaultValue){ //15Apr2000
  try{return getLong(key);}catch(Exception e){return defaultValue;}
 }

 //

 public short getShort(Object key){ //15Apr2000
  return ((Short)get(key)).shortValue();
 }

 public short getShort(Object key,short defaultValue){ //15Apr2000
  try{return getShort(key);}catch(Exception e){return defaultValue;}
 }

 //

 public byte getByte(Object key){ //15Apr2000
  return ((Byte)get(key)).byteValue();
 }

 public byte getByte(Object key,byte defaultValue){ //15Apr2000
  try{return getByte(key);}catch(Exception e){return defaultValue;}
 }

 //

 public double getDouble(Object key){
  return ((Double)get(key)).doubleValue();
 }

 public double getDouble(Object key, double defaultValue){ //15Apr2000
  try{return getDouble(key);}catch(Exception e){return defaultValue;}
 }

 //

 public String getString(Object key){
  return (String)get(key);
 }

 public String getString(Object key, String defaultValue){ //15Apr2000
  try{return getString(key);}catch(Exception e){return defaultValue;}
 }

 //

 public Color getColor(Object key){ //15Apr2000
  return (Color)get(key);
 }

 public Color getColor(Object key, Color defaultValue){ //15Apr2000
  try{return getColor(key);}catch(Exception e){return defaultValue;}
 }

}
