package gr.cti.eslate.scripting.logo.convertions;

import virtuoso.logo.*;

public class LogoLogo {

 public static final LogoObject[] toLogoObjectArray(LogoList l){
  int length=l.length();
  LogoObject[] o=new LogoObject[length];
  for(int i=0;i<length;i++) o[i]=l.pickInPlace(i);
  return o;
 }

 public static LogoObject[] toLogoObjectArray(LogoObject object){ //31Aug1999 (must check for LogoList, since LogoList is a subclass of LogoObject, and this method might be called passed a LogoObject variable, but having it at runtime contain a LogoList... the compiler won't know at compile time to call the above method, so it will call this... that's why we need to check and call the above one)
  LogoObject[] o;
  if(object instanceof LogoList) //if the object is a LOGO list, convert it to an objects' array...
   o=LogoLogo.toLogoObjectArray((LogoList)object);
  else
   o=new LogoObject[]{object}; //...else make an object array with just this object in it
  return o; //return the resulting object array
 }

}
