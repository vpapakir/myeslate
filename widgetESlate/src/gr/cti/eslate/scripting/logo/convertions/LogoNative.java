package gr.cti.eslate.scripting.logo.convertions;

import virtuoso.logo.*;

/**
 * @version     2.0.0, 22-May-2006
 */
public class LogoNative {

 /**
  * @since 8Aug2001
  */
 @SuppressWarnings("unchecked")
public static final Object convertLogoObject(LogoObject value, Class type)
  throws LanguageException
 {
  String typename=type.getName();

  //more frequent native types//
  if(type==java.lang.Object.class) return value.toString(); //!!!
  if(type==java.lang.String.class) return value.toString();
  if(typename.equals("int") || type==java.lang.Integer.class) return new Integer(value.toInteger());
  if(typename.equals("boolean") || type==java.lang.Boolean.class) return new Boolean(value.toBoolean());
  if(typename.equals("double") || type==java.lang.Double.class) return new Double(value.toNumber());

  //less frequent native types//
  if(typename.equals("long") || type==java.lang.Long.class) return new Long(value.toInteger());
  if(typename.equals("short") || type==java.lang.Short.class) return new Short((short)value.toInteger());
  if(typename.equals("float") || type==java.lang.Float.class) return new Float((float)value.toNumber());
  if(typename.equals("char") || type==java.lang.Character.class) return new Character(value.toString().charAt(0)); //return first char of string
  if(typename.equals("byte") || type==java.lang.Byte.class) return new Byte((byte)value.toInteger()); //cut down to a byte

  return null;
 }

 ////////

 /**
  * @since 5Aug2002
  */
 public static final LogoObject toLogoObject(Object object){
  if(object instanceof String) return new LogoWord((String)object);
  if(object instanceof Integer) return new LogoWord(((Integer)object).intValue());
  if(object instanceof Boolean) return new LogoWord(((Boolean)object).booleanValue());
  if(object instanceof Double) return new LogoWord(((Double)object).doubleValue());
  if(object instanceof Long) return new LogoWord(((Long)object).longValue());
  if(object instanceof Short) return new LogoWord(((Short)object).shortValue());
  if(object instanceof Float) return new LogoWord(((Float)object).floatValue());
  if(object instanceof Character) return new LogoWord(((Character)object).charValue());
  if(object instanceof Byte) return new LogoWord(((Byte)object).byteValue());

  if(object instanceof String[]) return toLogoObject((String[])object);
  if(object instanceof int[]) return toLogoObject((int[])object);
  if(object instanceof boolean[]) return toLogoObject((boolean[])object);
  if(object instanceof double[]) return toLogoObject((double[])object);
  if(object instanceof long[]) return toLogoObject((long[])object);
  if(object instanceof short[]) return toLogoObject((short[])object);
  if(object instanceof float[]) return toLogoObject((float[])object);
  if(object instanceof char[]) return toLogoObject((char[])object);
  if(object instanceof byte[]) return toLogoObject((byte[])object);

  return null;
 }

 public static final LogoObject toLogoObject(String[] items){ //31May2000
  int count=items.length;
  LogoObject result[] = new LogoObject[count];
  for(int i=count;i-->0;) result[i]=new LogoWord(items[i]);
  return new LogoList(result);
 }

 public static final LogoObject toLogoObject(int[] items){ //31May2000
  int count=items.length;
  LogoObject result[] = new LogoObject[count];
  for(int i=count;i-->0;) result[i]=new LogoWord(items[i]);
  return new LogoList(result);
 }

 public static final LogoObject toLogoWord(boolean[] items){
  int count=items.length;
  LogoObject result[] = new LogoObject[count];
  for(int i=count;i-->0;) result[i]=new LogoWord(items[i]);
  return new LogoList(result);
 }

 public static final LogoObject toLogoWord(double[] items){ //31May2000
  int count=items.length;
  LogoObject result[] = new LogoObject[count];
  for(int i=count;i-->0;) result[i]=new LogoWord(items[i]);
  return new LogoList(result);
 }

 public static final LogoObject toLogoObject(long[] items){ //31May2000
  int count=items.length;
  LogoObject result[] = new LogoObject[count];
  for(int i=count;i-->0;) result[i]=new LogoWord(items[i]);
  return new LogoList(result);
 }

 public static final LogoObject toLogoObject(short[] items){ //31May2000
  int count=items.length;
  LogoObject result[] = new LogoObject[count];
  for(int i=count;i-->0;) result[i]=new LogoWord(items[i]);
  return new LogoList(result);
 }

 public static final LogoObject toLogoObject(float[] items){ //31May2000
  int count=items.length;
  LogoObject result[] = new LogoObject[count];
  for(int i=count;i-->0;) result[i]=new LogoWord(items[i]);
  return new LogoList(result);
 }

 public static final LogoObject toLogoObject(char[] items){ //5Aug2002
  int count=items.length;
  LogoObject result[] = new LogoObject[count];
  for(int i=count;i-->0;) result[i]=new LogoWord(items[i]);
  return new LogoList(result);
 }

 public static final LogoObject toLogoObject(byte[] items){ //31May2000
  int count=items.length;
  LogoObject result[] = new LogoObject[count];
  for(int i=count;i-->0;) result[i]=new LogoWord(items[i]);
  return new LogoList(result);
 }

}

