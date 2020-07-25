//Title:        Java2D types <-> Logo convertions
//Version:      31Oct1999
//Copyright:    Copyright (c) 1998-2006
//Author:       George Birbilis
//Company:      CTI
//Description:  Physics

package gr.cti.eslate.scripting.logo.convertions;

import java.awt.geom.*;
import virtuoso.logo.*;

import gr.cti.shapes.*;

public class LogoJava2D {

 public static final LogoObject toLogoObject(Point2D point2D){
  LogoObject alogoobject[] = new LogoObject[2];
  alogoobject[0] = new LogoWord(point2D.getX());
  alogoobject[1] = new LogoWord(point2D.getY());
  return new LogoList(alogoobject);
 }

 public static final DoublePoint2D toDoublePoint2D(LogoObject logoobject)
  throws LanguageException
 {

       if(!(logoobject instanceof LogoList)) //29Jun1999
         throw new LanguageException("Vector expected");

        if(logoobject.length() != 2)
         throw new LanguageException("Two elements expected in vector");
        double x = 0;
        double y = 0;

        try
        {
            x = ((LogoList)logoobject).pickInPlace(0).toNumber();
        }
        catch(LanguageException languageexception)
        {
            throw new LanguageException(languageexception.getMessage() + " in x value of vector list");
        }

        try
        {
            y = ((LogoList)logoobject).pickInPlace(1).toNumber();
        }
        catch(LanguageException languageexception1)
        {
            throw new LanguageException(languageexception1.getMessage() + " in y value of vector list");
        }
        // System.out.println("x="+x+" y="+y);

        return new DoublePoint2D(x, y);
    }

//////////////

 public static final Point2D toPoint2D(LogoObject logoobject) //31Oct1999
  throws LanguageException
 {

       if(!(logoobject instanceof LogoList)) //29Jun1999
         throw new LanguageException("Vector expected");

        if(logoobject.length() != 2)
         throw new LanguageException("Two elements expected in vector");
        double x = 0;
        double y = 0;

        try
        {
            x = ((LogoList)logoobject).pickInPlace(0).toNumber();
        }
        catch(LanguageException languageexception)
        {
            throw new LanguageException(languageexception.getMessage() + " in x value of vector list");
        }

        try
        {
            y = ((LogoList)logoobject).pickInPlace(1).toNumber();
        }
        catch(LanguageException languageexception1)
        {
            throw new LanguageException(languageexception1.getMessage() + " in y value of vector list");
        }
        // System.out.println("x="+x+" y="+y);

        return new Point2D.Double(x, y);
    }


}
