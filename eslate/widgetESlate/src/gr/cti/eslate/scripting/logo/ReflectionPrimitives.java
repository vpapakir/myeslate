package gr.cti.eslate.scripting.logo;

import java.util.*;
import virtuoso.logo.*;
import gr.cti.utils.BeanUtil;
import gr.cti.eslate.scripting.logo.convertions.*;

/**
 * @version     2.0.0, 22-May-2006
 */
public class ReflectionPrimitives extends PrimitiveGroup
{

 MyMachine myMachine; //new-scripting-mechanism

 protected void setup(Machine machine, Console console)
     throws SetupException
 {
  registerPrimitive("GET", "pGET", 1);
  registerPrimitive("SET", "pSET", 2);
  registerPrimitive("SETNULL", "pSETNULL", 1); //5Aug2002
  registerPrimitive("DO", "pDO", 1);
  registerPrimitive("DOANDGET", "pDOANDGET", 1); //25Aug2002
  registerPrimitive("PROPERTIES.NAMES","pPROPERTIES_NAMES",0); //31May2000
  registerPrimitive("PROPERTIES",this,"PROPERTIES.NAMES"); //alias //31May2000
  registerPrimitive("PROPERTIES.DISPLAYNAMES","pPROPERTIES_DISPLAYNAMES",0); //31May2000
  registerPrimitive("PROPERTIES.SHORTDESCRIPTIONS","pPROPERTIES_SHORTDESCRIPTIONS",0); //31May2000
  registerPrimitive("PROPERTIES.TYPES","pPROPERTIES_TYPES",0); //8Aug2001
  registerPrimitive("VERBS","pVERBS",0); //1Jun2000

  //...Place extra init code here...

  myMachine=(MyMachine)machine; //new-scripting-mechanism

  if(console != null)
   console.putSetupMessage("Loaded ESlate's GET/SET Reflection primitives");
 }

//GET//

 public final LogoObject pGET(InterpEnviron interpenviron, LogoObject params[]) //31Aug1999
  throws LanguageException
 {
  testNumParams(params,1);
  String propertyName=params[0].toString();
  Object value;

  Object o=myMachine.componentPrimitives.getFirstComponentToTell(java.lang.Object.class); //all objects!
  try{
   value=BeanUtil.getProperty(o,propertyName);
  }catch(Exception ex){
   throw new LanguageException("Couldn't get value for property "+propertyName);
  }
  return toLogoObject(value);
 }

//SET//

 public final LogoObject pSET(InterpEnviron interpenviron, LogoObject params[])
  throws LanguageException
 {
  testNumParams(params,2);
  String propertyName=params[0].toString();

  LogoObject value=params[1];

  int failures=0;

  Vector<?> v=myMachine.componentPrimitives.getComponentsToTell(java.lang.Object.class); //all objects!
  for(int i=0,size=v.size();i<size;i++){
   try{
    Object bean=v.elementAt(i);
    Class<?> propertyType=BeanUtil.getPropertyType(bean,propertyName);
    BeanUtil.setProperty(bean,propertyName,convertLogoObject(value,propertyType)); //find appropriate property type (for each object separately)
   }catch(Exception e){
    e.printStackTrace();
    failures++;
   }
  }
  if(failures>0) throw new LanguageException("Failed to set property "+propertyName+" at "+failures+" objects");
  return LogoVoid.obj;
 }


//SETNULL//

 /**
  * @since 5Aug2002
  */
 public final LogoObject pSETNULL(InterpEnviron interpenviron, LogoObject params[])
  throws LanguageException
 {
  testNumParams(params,1);
  String propertyName=params[0].toString();

  int failures=0;

  Vector<?> v=myMachine.componentPrimitives.getComponentsToTell(java.lang.Object.class); //all objects!
  for(int i=0,size=v.size();i<size;i++){
   try{
    Object bean=v.elementAt(i);
    BeanUtil.setProperty(bean,propertyName,null);
   }catch(Exception e){
    e.printStackTrace();
    failures++;
   }
  }
  if(failures>0) throw new LanguageException("Failed to set property "+propertyName+" to null at "+failures+" objects");
  return LogoVoid.obj;
 }

//DO//

 public final LogoObject pDO(InterpEnviron interpenviron, LogoObject params[]) //31Aug1999
  throws LanguageException
 {
  testNumParams(params,1);
  String verb=params[0].toString();
  int failures=0;

  Vector<?> v=myMachine.componentPrimitives.getComponentsToTell(java.lang.Object.class); //all objects!
  for(int i=0,size=v.size();i<size;i++)
   try{ //try-catch and count failures so that at exit we throw LanguageException that N objects failed to execute verb
    BeanUtil.doVerb(v.elementAt(i),verb);
   }catch(Exception e){failures++;}
  if(failures>0) throw new LanguageException(failures+" objects failed to do "+verb);
  return LogoVoid.obj;
 }

//DO.GET//

 public final LogoObject pDOANDGET(InterpEnviron interpenviron, LogoObject params[]) //25Aug2002
  throws LanguageException
 {
  testNumParams(params,1);
  String verb=params[0].toString();

  Object o=myMachine.componentPrimitives.getFirstComponentToTell(java.lang.Object.class); //all objects!
  try{ //try-catch and count failures so that at exit we throw LanguageException that N objects failed to execute verb
   return toLogoObject(BeanUtil.doVerb(o,verb));
  }catch(Exception e){
   throw new LanguageException("Object failed to do "+verb);
  }
 }

//PROPERTIES.NAMES//

 public final LogoObject pPROPERTIES_NAMES(InterpEnviron interpenviron, LogoObject params[]) //31May2000
  throws LanguageException
 {
  testNumParams(params,0);
  String[] names;

  Object o=myMachine.componentPrimitives.getFirstComponentToTell(java.lang.Object.class); //all objects!
  try{
   names=BeanUtil.getPropertyNames(o);
  }catch(Exception ex){
   throw new LanguageException("Couldn't get the properties' names");
  }
  return LogoNative.toLogoObject(names);
 }

//PROPERTIES.DISPLAYNAMES//

 public final LogoObject pPROPERTIES_DISPLAYNAMES(InterpEnviron interpenviron, LogoObject params[]) //31May2000
  throws LanguageException
 {
  testNumParams(params,0);
  String[] names;

  Object o=myMachine.componentPrimitives.getFirstComponentToTell(java.lang.Object.class); //all objects!
  try{
   names=BeanUtil.getPropertyDisplayNames(o);
  }catch(Exception ex){
   throw new LanguageException("Couldn't get the properties' display names");
  }
  return LogoNative.toLogoObject(names);
 }

//PROPERTIES.SHORTDESCRIPTIONS//

 public final LogoObject pPROPERTIES_SHORTDESCRIPTIONS(InterpEnviron interpenviron, LogoObject params[]) //31May2000
  throws LanguageException
 {
  testNumParams(params,0);
  String[] names;

  Object o=myMachine.componentPrimitives.getFirstComponentToTell(java.lang.Object.class); //all objects!
  try{
   names=BeanUtil.getPropertyShortDescriptions(o);
  }catch(Exception ex){
   throw new LanguageException("Couldn't get the properties' short descriptions");
  }
  return LogoNative.toLogoObject(names);
 }

//PROPERTIES.TYPES//

 public final LogoObject pPROPERTIES_TYPES(InterpEnviron interpenviron, LogoObject params[]) //8Aug2001
  throws LanguageException
 {
  testNumParams(params,0);
  String[] types;

  Object o=myMachine.componentPrimitives.getFirstComponentToTell(java.lang.Object.class); //all objects!
  try{
   types=BeanUtil.getPropertyTypes(o);
  }catch(Exception ex){
   throw new LanguageException("Couldn't get the properties' types");
  }
  return LogoNative.toLogoObject(types);
 }

//VERBS//

 public final LogoObject pVERBS(InterpEnviron interpenviron, LogoObject params[]) //31May2000
  throws LanguageException
 {
  testNumParams(params,0);
  String[] names;

  Object o=myMachine.componentPrimitives.getFirstComponentToTell(java.lang.Object.class); //all objects!
  try{
   names=BeanUtil.getVerbs(o);
  }catch(Exception ex){
   throw new LanguageException("Couldn't get the verbs");
  }
  return LogoNative.toLogoObject(names);
 }

/////////////////////////////////////////////////////////////////////

 /**
  * @since 8Aug2001
  */
 @SuppressWarnings("unchecked")
public static final Object convertLogoObject(LogoObject value, Class type)
  throws LanguageException
 {
  Object result=LogoNative.convertLogoObject(value,type); //check whether the wanted type is a native object that LogoNative can convert to
  if(result==null) result=LogoAWT.convertLogoObject(value,type); //check whether the wanted type is an AWT object that LogoAWT can convert to
  //if(result==null) result=LogoXXX.convertLogoObject(value,type);
  //if(result==null) result=LogoYYY.convertLogoObject(value,type); //try other converters

  if(result==null)
   throw new LanguageException("Couldn't convert value "+value+" to type "+type.getName());

  return result;
 }

 /**
  * @since 5Aug2002
  */
 public static final LogoObject toLogoObject(Object value)
  throws LanguageException
 {
  if(value==null) return new LogoWord("null");
  LogoObject result=LogoNative.toLogoObject(value); //check whether the native object is an object that LogoNative can convert to a LogoObject
  if(result==null) result=LogoAWT.toLogoObject(value); //check whether the native object is an object that LogoAWT can convert to a LogoObject
  //if(result==null) result=LogoXXX.toLogoObject(value);
  //if(result==null) result=LogoYYY.toLogoObject(value); //try other converters

  if(result==null)
   //throw new LanguageException("Couldn't convert value "+value+" to LogoObject");
   result=new LogoWord(value.toString());

  return result;
 }

}

