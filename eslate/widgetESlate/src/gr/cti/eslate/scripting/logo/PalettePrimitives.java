package gr.cti.eslate.scripting.logo; /**/

import java.awt.*;
import java.util.*;
import virtuoso.logo.*;
import gr.cti.eslate.scripting.logo.convertions.LogoAWT; //1Sep1999

import gr.cti.eslate.scripting.AsPalette; /**/

/**
 * @version     2.0.1, 23-Jan-2008
 * @author      George Birbilis
 * @author      Kriton Kyrimis
 */
public class PalettePrimitives extends PrimitiveGroup /**/ //1Sep1999
{

 MyMachine myMachine; //25May1999: new-scripting-mechanism

 protected void setup(Machine machine, Console console)
  throws SetupException
 {
  registerPrimitive("SETPALETTEFOREGROUND", "pSETPALETTEFOREGROUND", 1); /**/
  registerPrimitive("PALETTEFOREGROUND", "pPALETTEFOREGROUND", 0);       /**/

  registerPrimitive("SETPALETTEBACKGROUND", "pSETPALETTEBACKGROUND", 1); /**/
  registerPrimitive("PALETTEBACKGROUND", "pPALETTEBACKGROUND", 0);       /**/

  registerPrimitive("SETPALETTEFILL", "pSETPALETTEFILL", 1); /**/
  registerPrimitive("PALETTEFILL", "pPALETTEFILL", 0);       /**/

  //...register new primitives here...

  myMachine=(MyMachine)machine; //25May1999: new-scripting-mechanism

  if(console != null)
      console.putSetupMessage("Loaded ESlate's Palette component primitives"); /**/
 }

//util//

 private Vector<?> getPalettes(){
  return myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.scripting.AsPalette.class);
 }

 private AsPalette getFirstPalette()
  throws LanguageException //if no instance is found, the "getFirstComponentsToTell" method will throw a LanguageException to the Logo console saying "No component to TELL this to!"
 {
  return (AsPalette)myMachine.componentPrimitives.getFirstComponentToTell(gr.cti.eslate.scripting.AsPalette.class);
 }

//PALETTEFOREGROUND//

 public final LogoObject pSETPALETTEFOREGROUND(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
  throws LanguageException
 {
  testNumParams(alogoobject,1);
  Color color = LogoAWT.toColor(alogoobject[0]); //may throw a LanguageException //1Sep1999: using LogoAWT convertion routines
  Vector<?> v=getPalettes();
  for(int i=0;i<v.size();i++) ((AsPalette)v.elementAt(i)).setForegroundColor(color);
  return LogoVoid.obj;
 }

 public final LogoObject pPALETTEFOREGROUND(InterpEnviron interpenviron, LogoObject alogoobject[])
  throws LanguageException
 {
   testNumParams(alogoobject,0);
   Color color=getFirstPalette().getForegroundColor();
   return LogoAWT.toLogoObject(color); //1Sep1999: using LogoAWT convertion routines
 }

//PALETTEBACKGROUND//

 public final LogoObject pSETPALETTEBACKGROUND(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
  throws LanguageException
 {
    testNumParams(alogoobject,1);
    Color color = LogoAWT.toColor(alogoobject[0]); //may throw a LanguageException //1Sep1999: using LogoAWT convertion routines
    Vector<?> v=getPalettes();
    for(int i=0;i<v.size();i++) ((AsPalette)v.elementAt(i)).setBackgroundColor(color);
    return LogoVoid.obj;
 }

 public final LogoObject pPALETTEBACKGROUND(InterpEnviron interpenviron, LogoObject alogoobject[])
  throws LanguageException
 {
   testNumParams(alogoobject,0);
   Color color=getFirstPalette().getBackgroundColor();
   return LogoAWT.toLogoObject(color); //1Sep1999: using LogoAWT convertion routines
 }

//PALETTEFILL//

 public final LogoObject pSETPALETTEFILL(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
  throws LanguageException
 {
    testNumParams(alogoobject,1);
    Color color = LogoAWT.toColor(alogoobject[0]); //may throw a LanguageException //1Sep1999: using LogoAWT convertion routines
    Vector<?> v=getPalettes();
    for(int i=0;i<v.size();i++) ((AsPalette)v.elementAt(i)).setFillColor(color);
    return LogoVoid.obj;
 }

 public final LogoObject pPALETTEFILL(InterpEnviron interpenviron, LogoObject alogoobject[])
  throws LanguageException
 {
   testNumParams(alogoobject,0);
   Color color=getFirstPalette().getFillColor();
   return LogoAWT.toLogoObject(color); //1Sep1999: using LogoAWT convertion routines
 }

}

