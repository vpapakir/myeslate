package gr.cti.eslate.scripting.logo.convertions;

import virtuoso.logo.*;
import java.awt.*;

/**
 * @version     2.0.0, 22-May-2006
 */
public class LogoAWT {

// convertion to LogoObject //////////////////////

 /**
  * @since 5Aug2002
  */
 public static final LogoObject toLogoObject(Object object){
  if(object instanceof Color) return toLogoObject((Color)object);
  if(object instanceof Dimension) return toLogoObject((Dimension)object);
  if(object instanceof Rectangle) return toLogoObject((Rectangle)object);    

  return null;
 }

 /**
  * @since 8Aug2001
  */
 public static final LogoObject toLogoObject(Color color){
  LogoObject alogoobject[] = new LogoObject[4]; //Java1.2+
  //LogoObject alogoobject[] = new LogoObject[3]; //Java1.1
  alogoobject[0] = new LogoWord(color.getRed());
  alogoobject[1] = new LogoWord(color.getGreen());
  alogoobject[2] = new LogoWord(color.getBlue());
  alogoobject[3] = new LogoWord(color.getAlpha()); //Alpha comes last, RGBA format //Java1.2+
  return new LogoList(alogoobject);
 }

 /**
  * @since 5Aug2002
  */
 public static final LogoObject toLogoObject(Dimension dimension){
  LogoObject alogoobject[] = new LogoObject[2];
  alogoobject[0] = new LogoWord(dimension.width);
  alogoobject[1] = new LogoWord(dimension.height);
  return new LogoList(alogoobject);
 }

 /**
  * @since 5Aug2002
  */
 public static final LogoObject toLogoObject(Rectangle rectangle){
  LogoObject alogoobject[] = new LogoObject[4];
  alogoobject[0] = new LogoWord(rectangle.x);
  alogoobject[1] = new LogoWord(rectangle.y);
  alogoobject[2] = new LogoWord(rectangle.width);
  alogoobject[3] = new LogoWord(rectangle.height);
  return new LogoList(alogoobject);
 }

// convertion from LogoObject //////////////////////

 /**
  * @since 8Aug2001
  */
 @SuppressWarnings("unchecked")
public static final Object convertLogoObject(LogoObject value, Class type)
  throws LanguageException
 {
  if(type==java.awt.Color.class) return toColor(value);
  if(type==java.awt.Dimension.class) return toDimension(value);
  if(type==java.awt.Rectangle.class) return toRectangle(value);
  return null;
 }

 /**
  * @since 8Aug2001
  */
 public static final Color toColor(LogoObject logoobject)
     throws LanguageException
 {
  if(!(logoobject instanceof LogoList)) //29Jun1999
   throw new LanguageException("Vector expected");

  int length=logoobject.length();
  if(length!=3 && length!=4) //RGB and RGBA formats supported
   throw new LanguageException("Four elements [RGBA=Red-Green-Blue-Alpha] or three elements [RGB] expected in color list. Each element must be an integer value in the range 0-255");

  int red,green,blue,alpha=255; //default is opaque if not supplied by user

  try{
   red=((LogoList)logoobject).pickInPlace(0).toInteger();
  }catch(LanguageException languageexception){
   throw new LanguageException(languageexception.getMessage() + " in red value of color list");
  }

  try{
   green=((LogoList)logoobject).pickInPlace(1).toInteger(); //29-7-1998: bug-fixed: had 0 instead of 1
  }catch(LanguageException languageexception1){
   throw new LanguageException(languageexception1.getMessage() + " in green value of color list");
  }

  try{
   blue=((LogoList)logoobject).pickInPlace(2).toInteger(); //29-7-1998: bug-fixed had 0 instead of 2
  }catch(LanguageException languageexception2){
   throw new LanguageException(languageexception2.getMessage() + " in blue value of color list");
  }

  if(length==4)
   try{
    alpha=((LogoList)logoobject).pickInPlace(3).toInteger();
   }catch(LanguageException languageexception2){
    throw new LanguageException(languageexception2.getMessage() + " in alpha value of color list");
   }

  // System.out.println("Red="+red+" Green="+green+" Blue="+blue+" Alpha="+alpha);

  if(red < 0 || red > 255)
   throw new LanguageException("Bad color (red value out of 0-255 range)");
  if(green < 0 || green > 255)
   throw new LanguageException("Bad color (green value out of 0-255 range)");
  if(blue < 0 || blue > 255)
   throw new LanguageException("Bad color (blue value out of 0-255 range)");
  if(alpha < 0 || alpha > 255)
   throw new LanguageException("Bad color (alpha value out of 0-255 range)");
  else
   return new Color(red, green, blue,alpha); //Java1.2+
   //return new Color(red,green,blue); //Java1.1
 }

 /**
  * @since 5Aug2002
  */
 public static final Dimension toDimension(LogoObject logoobject)
     throws LanguageException
 {
  if(!(logoobject instanceof LogoList))
   throw new LanguageException("Vector expected");

  int length=logoobject.length();
  if(length!=2)
   throw new LanguageException("Two elements [width height] expected in a dimension list. Each element must be an integer value");

  int width,height;

  try{
   width=((LogoList)logoobject).pickInPlace(0).toInteger();
  }catch(LanguageException e){
   throw new LanguageException(e.getMessage() + " in width value of dimension list");
  }

  try{
   height=((LogoList)logoobject).pickInPlace(1).toInteger();
  }catch(LanguageException e){
   throw new LanguageException(e.getMessage() + " in height value of dimension list");
  }

  // System.out.println("Width="+width+" Height="+height);
  return new Dimension(width,height);
 }

 /**
  * @since 5Aug2002
  */
 public static final Rectangle toRectangle(LogoObject logoobject)
     throws LanguageException
 {
  if(!(logoobject instanceof LogoList))
   throw new LanguageException("Vector expected");

  int length=logoobject.length();
  if(length!=4)
   throw new LanguageException("Four elements [x y width height] expected in a dimension list. Each element must be an integer value");

  int x,y,width,height;

  try{
   x=((LogoList)logoobject).pickInPlace(0).toInteger();
  }catch(LanguageException e){
   throw new LanguageException(e.getMessage() + " in x value of rectangle list");
  }

  try{
   y=((LogoList)logoobject).pickInPlace(1).toInteger();
  }catch(LanguageException e){
   throw new LanguageException(e.getMessage() + " in y value of rectangle list");
  }

  try{
   width=((LogoList)logoobject).pickInPlace(2).toInteger();
  }catch(LanguageException e){
   throw new LanguageException(e.getMessage() + " in width value of rectangle list");
  }

  try{
   height=((LogoList)logoobject).pickInPlace(3).toInteger();
  }catch(LanguageException e){
   throw new LanguageException(e.getMessage() + " in height value of rectangle list");
  }

  // System.out.println("Width="+width+" Height="+height);
  return new Rectangle(x,y,width,height);
 }

}

