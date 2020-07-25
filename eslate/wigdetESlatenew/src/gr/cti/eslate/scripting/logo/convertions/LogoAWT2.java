package gr.cti.eslate.scripting.logo.convertions;

import virtuoso.logo.*;
import java.awt.*;

public class LogoAWT2 { //for Java2 colors (allows optional transparency parameter!)

    public final static LogoObject toLogoObject(Color color){
     LogoObject alogoobject[] = new LogoObject[4];
     alogoobject[0] = new LogoWord(color.getRed());
     alogoobject[1] = new LogoWord(color.getGreen());
     alogoobject[2] = new LogoWord(color.getBlue());
     alogoobject[3] = new LogoWord(color.getAlpha()); //Alpha comes last, RGBA format
     return new LogoList(alogoobject);
    }

    public final static Color toColor(LogoObject logoobject)
        throws LanguageException
    {
       if(!(logoobject instanceof LogoList)) //29Jun1999
         throw new LanguageException("Vector expected");

        int length=logoobject.length();
        if(length!=3 && length!=4) //RGB and RGBA formats supported
            throw new LanguageException("Four elements [RGBA=Red-Green-Blue-Alpha] or three elements [RGB] expected in color list. Each element must be an integer value in the range 0-255");

        int red,green,blue,
            alpha=255; //default is opaque if not supplied by user

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
            return new Color(red, green, blue,alpha);
    }

}
