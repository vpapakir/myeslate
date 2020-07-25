//26Nov1999 - renamed all PHYSICS.* to SCENE.*
//29Nov1999 - renamed all STAGE.* to SCENE.*
//24Feb2000 - added SCENE.CLONEOBJECT primitive
//31Mar2000 - added SCENE.BRINGTOFRONT and SCENE.SENDTOBACK primitives
//10May2000 - added SCENE.PHOTO primitive

package gr.cti.eslate.scripting.logo; /**/

import java.util.*;

import virtuoso.logo.*;

import gr.cti.eslate.stage.models.AsScene;

import java.awt.Image;
import gr.cti.utils.ImageFile;

/**
 * @version     2.0.11, 26-Jun-2007
 * @author      George Birbilis
 * @author      Nikos Drossos
 * @author      Kriton Kyrimis
 */
public class ScenePrimitives extends PrimitiveGroup /**/
{

    MyMachine myMachine; //new-scripting-mechanism
    Console console;

    protected void setup(Machine machine, Console console)
        throws SetupException
    {
        registerPrimitive("SCENE.MAKEOBJECT", "pMAKEOBJECT", 1);
        registerPrimitive("SCENE.REMOVEOBJECT", "pREMOVEOBJECT", 1);
        registerPrimitive("SCENE.CLONEOBJECT", "pCLONEOBJECT", 1); //24Feb2000

        registerPrimitive("SCENE.BRINGTOFRONT", "pBRINGTOFRONT", 1); //31Mar2000
        registerPrimitive("SCENE.SENDTOBACK", "pSENDTOBACK", 1); //31Mar2000

        registerPrimitive("SCENE.CLEAR", "pCLEAR", 0); //24Feb2000: renamed Scene.ClearScene primitive to Scene.Clear
        registerPrimitive("SCENE.ENABLEREFRESH","pENABLEREFRESH",0);
        registerPrimitive("SCENE.DISABLEREFRESH","pDISABLEREFRESH",0);
        registerPrimitive("SCENE.TRANSLATEVIEW","pTRANSLATEVIEW",2);

        registerPrimitive("SCENE.PHOTO", "pPHOTO", 1); //10May2000

        registerPrimitive("SCENE.GRIDSIZE", "pGRIDSIZE", 0);
        registerPrimitive("SCENE.SETGRIDSIZE", "pSETGRIDSIZE", 1);
        registerPrimitive("SCENE.GRIDVISIBLE", "pGRIDVISIBLE", 0);
        registerPrimitive("SCENE.SHOWGRID", "pSHOWGRID", 0);
        registerPrimitive("SCENE.HIDEGRID", "pHIDEGRID", 0);
        registerPrimitive("SCENE.SETAXESOVERSHAPES", "pSETAXESOVERSHAPES", 0);
        registerPrimitive("SCENE.SETAXESUNDERSHAPES", "pSETAXESUNDERSHAPES", 0);
        registerPrimitive("SCENE.AXESOVERSHAPES", "pAXESOVERSHAPES", 0);

        myMachine=(MyMachine)machine;
        this.console=console;

        if(console != null)
            console.putSetupMessage("Loaded Scene's primitives"); /**/
    }

//utility methods//

 private Vector<?> getKnownObjects(){ //2Nov1999
  return myMachine.componentPrimitives.getComponentsToTell(AsScene.class);
 }

 private AsScene getFirstKnownScene() throws LanguageException{ //1Apr2000
  return (AsScene)myMachine.componentPrimitives.getFirstComponentToTell(AsScene.class); //this will throw a LanguageException if no such object is found
 }

//SCENE.ENABLEREFRESH//

    public final LogoObject pENABLEREFRESH(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,0);
       Vector<?> v=getKnownObjects();
       for(int i=0;i<v.size();i++)
        ((AsScene)v.elementAt(i)).setRefreshEnabled(true);
       return LogoVoid.obj;
    }

//SCENE.DISABLEREFRESH//

    public final LogoObject pDISABLEREFRESH(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,0);
       Vector<?> v=getKnownObjects();
       for(int i=0;i<v.size();i++)
        ((AsScene)v.elementAt(i)).setRefreshEnabled(false);
       return LogoVoid.obj;
    }

//SCENE.MAKEOBJECT//

 public final LogoObject pMAKEOBJECT(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
  throws LanguageException
 {
  testNumParams(alogoobject,1);
  String type=alogoobject[0].toString();

  AsScene scene=getFirstKnownScene();
  try{
   return new LogoWord(scene.newSceneObject(type).getName());} //1Apr2000: fixed-bug: now only the 1st scene object in tell vector is spoken to! (since we can return only 1 result value and not many!)
  catch(Exception e){
   throw new LanguageException("couldn't make a new sceneObject of type "+type);
  }
 }

//SCENE.REMOVEOBJECT//

    public final LogoObject pREMOVEOBJECT(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,1);

       String name=alogoobject[0].toString();

       Vector<?> v=getKnownObjects();
       for(int i=0;i<v.size();i++)
        ((AsScene)v.elementAt(i)).removeSceneObject(name);
       return LogoVoid.obj;
    }

//SCENE.CLONEOBJECT//

 public final LogoObject pCLONEOBJECT(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
  throws LanguageException
 {
  testNumParams(alogoobject,1);
  String genetorObjectName=alogoobject[0].toString();

  AsScene scene=getFirstKnownScene();
  try{
   return new LogoWord(scene.cloneSceneObject(genetorObjectName).getName());} //1Apr2000: fixed-bug: now only the 1st scene object in tell vector is spoken to! (since we can return only 1 result value and not many!)
  catch(Exception e){
   throw new LanguageException("couldn't clone object "+genetorObjectName);
  }
 }

//SCENE.BRINGTOFRONT//

    public final LogoObject pBRINGTOFRONT(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,1);

       String name=alogoobject[0].toString();

       Vector<?> v=getKnownObjects();
       for(int i=0;i<v.size();i++)
        ((AsScene)v.elementAt(i)).bringToFront(name);
       return LogoVoid.obj;
    }

//SCENE.SENDTOBACK//

    public final LogoObject pSENDTOBACK(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,1);

       String name=alogoobject[0].toString();

       Vector<?> v=getKnownObjects();
       for(int i=0;i<v.size();i++)
        ((AsScene)v.elementAt(i)).sendToBack(name);
       return LogoVoid.obj;
    }

//SCENE.CLEAR//

    public final LogoObject pCLEAR(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,0);

       Vector<?> v=getKnownObjects();
       for(int i=0;i<v.size();i++)
        ((AsScene)v.elementAt(i)).clearScene();

       return LogoVoid.obj;
    }

//SCENE.TRANSLATEVIEW//

    public final LogoObject pTRANSLATEVIEW(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,2);

       double offsx=alogoobject[0].toNumber();
       double offsy=alogoobject[1].toNumber();

       Vector<?> v=getKnownObjects();
       for(int i=0;i<v.size();i++)
        ((AsScene)v.elementAt(i)).translate(offsx,offsy);
       return LogoVoid.obj;
    }

//SCENE.PHOTO//

    public final LogoObject pPHOTO(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
       testNumParams(alogoobject,1);

       String filename=alogoobject[0].toString();

       int errors=0;
       Vector<?> v=getKnownObjects();
       for(int i=0;i<v.size();i++){
        Image img=((AsScene)v.elementAt(i)).getPhoto();
         if(img!=null)
          try{ImageFile.saveGIF(filename,img);}
          catch(Exception e){errors++;}
        }
       if(errors>0) console.putLine(errors+" scene(s) failed to save their photo");

       return LogoVoid.obj;
    }

    public final LogoObject pGRIDSIZE(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
        testNumParams(alogoobject,0);

        Vector<?> v=myMachine.componentPrimitives.getComponentsToTell(AsScene.class);
        try{return new LogoWord(((AsScene)v.firstElement()).getGridSize());
        }catch(NoSuchElementException e){throw new LanguageException("There is no object to TELL this to");}

    }

    public final LogoObject pSETGRIDSIZE(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
        testNumParams(alogoobject,1);

        double size=alogoobject[0].toNumber();

        Vector<?> v=myMachine.componentPrimitives.getComponentsToTell(AsScene.class);
        for(int i=0;i<v.size();i++) ((AsScene)v.elementAt(i)).setGridSize(size);
        return LogoVoid.obj;
    }

    public final LogoObject pGRIDVISIBLE(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
        testNumParams(alogoobject,0);

        Vector<?> v=myMachine.componentPrimitives.getComponentsToTell(AsScene.class);
        try{return new LogoWord(((AsScene)v.firstElement()).isGridVisible());
        }catch(NoSuchElementException e){throw new LanguageException("There is no object to TELL this to");}

    }

    public final LogoObject pSHOWGRID(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
        testNumParams(alogoobject,0);

        Vector<?> v=myMachine.componentPrimitives.getComponentsToTell(AsScene.class);
        for(int i=0;i<v.size();i++) ((AsScene)v.elementAt(i)).setGridVisible(true);
        return LogoVoid.obj;
    }

    public final LogoObject pHIDEGRID(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
        testNumParams(alogoobject,0);

        Vector<?> v=myMachine.componentPrimitives.getComponentsToTell(AsScene.class);
        for(int i=0;i<v.size();i++) ((AsScene)v.elementAt(i)).setGridVisible(false);
        return LogoVoid.obj;
    }

    public final LogoObject pAXESOVERSHAPES(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
        testNumParams(alogoobject,0);

        Vector<?> v=myMachine.componentPrimitives.getComponentsToTell(AsScene.class);
        try{return new LogoWord(((AsScene)v.firstElement()).isMarksOverShapes());
        }catch(NoSuchElementException e){throw new LanguageException("There is no object to TELL this to");}

    }

    public final LogoObject pSETAXESOVERSHAPES(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
        testNumParams(alogoobject,0);

        Vector<?> v=myMachine.componentPrimitives.getComponentsToTell(AsScene.class);
        for(int i=0;i<v.size();i++) ((AsScene)v.elementAt(i)).setMarksOverShapes(true);
        return LogoVoid.obj;
    }

    public final LogoObject pSETAXESUNDERSHAPES(InterpEnviron interpenviron, LogoObject alogoobject[]) /**/
        throws LanguageException
    {
        testNumParams(alogoobject,0);

        Vector<?> v=myMachine.componentPrimitives.getComponentsToTell(AsScene.class);
        for(int i=0;i<v.size();i++) ((AsScene)v.elementAt(i)).setMarksOverShapes(false);
        return LogoVoid.obj;
    }
}
