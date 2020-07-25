/*
 * Created on 12 Ιουν 2006
 *
 */
package gr.cti.eslate.scripting.logo;

import gr.cti.eslate.math.linalg.Vec3d;
import gr.cti.eslate.scene3d.Scene3D;

import java.util.Vector;

import virtuoso.logo.Console;
import virtuoso.logo.InterpEnviron;
import virtuoso.logo.LanguageException;
import virtuoso.logo.LogoList;
import virtuoso.logo.LogoObject;
import virtuoso.logo.LogoVoid;
import virtuoso.logo.LogoWord;
import virtuoso.logo.Machine;
import virtuoso.logo.MyMachine;
import virtuoso.logo.PrimitiveGroup;
import virtuoso.logo.SetupException;


    public class Viewer3DWrapperPrimitives extends PrimitiveGroup {
        MyMachine myMachine;

        protected void setup(Machine machine, Console console) throws SetupException {
            registerPrimitive("CAMERA.SET_POSITION","pSET_POSITION",3);
            registerPrimitive("CAMERA.SET_PITCH_ANGLE","pSET_PITCH_ANGLE",3);
            registerPrimitive("CAMERA.SET_YAW_ANGLE","pSET_YAW_ANGLE",3);
            registerPrimitive("CAMERA.POSITION","pPOSITION",0);
            registerPrimitive("CAMERA.PITCH_ANGLE","pPITCH_ANGLE",0);
            registerPrimitive("CAMERA.YAW_ANGLE","pYAW_ANGLE",0);

            myMachine = (MyMachine) machine;

            if (console != null)
                console.putSetupMessage("Loaded Object3D primitives");

        }

        public final LogoObject pPOSITION(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
            testNumParams(aLogoObject, 0);
            Scene3D viewer3d = (Scene3D) myMachine.componentPrimitives.getFirstComponentToTell(Scene3D.class);
            Vec3d pos = viewer3d.getCameraPosition();
            Vector v = new Vector();
            v.add(new Double(pos.x()));
            v.add(new Double(pos.y()));
            v.add(new Double(pos.z()));
            return new LogoList(v);
        }

        public final LogoObject pPITCH_ANGLE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
            testNumParams(aLogoObject, 0);
            Scene3D viewer3d = (Scene3D) myMachine.componentPrimitives.getFirstComponentToTell(Scene3D.class);
            double d = viewer3d.getCameraPitchAngle();
            return new LogoWord(d);
        }
        
        public final LogoObject pYAW_ANGLE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
            testNumParams(aLogoObject, 0);
            Scene3D viewer3d = (Scene3D) myMachine.componentPrimitives.getFirstComponentToTell(Scene3D.class);
            double d = viewer3d.getCameraYawAngle();
            return new LogoWord(d);
        }
        

        public final LogoObject pSET_POSITION(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
            testNumParams(aLogoObject, 3);
            double x = aLogoObject[0].toNumber();
            double y = aLogoObject[1].toNumber();
            double z = aLogoObject[2].toNumber();
            Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.object3D.Object3D.class);
            Scene3D v3d;
            for (int i = 0; i < v.size(); i++) {
                v3d = (Scene3D) v.elementAt(i);
                v3d.setCameraPosition(new Vec3d(x,y,z));
                
            }
            return LogoVoid.obj;
        }
        
        public final LogoObject pSET_PITCH_ANGLE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
            testNumParams(aLogoObject, 1);
            double x = aLogoObject[0].toNumber();
            Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.object3D.Object3D.class);
            Scene3D v3d;
            for (int i = 0; i < v.size(); i++) {
                v3d = (Scene3D) v.elementAt(i);
                v3d.setCameraPitchAngle(x);
                
            }
            return LogoVoid.obj;
        }
        
        public final LogoObject pSET_YAW_ANGLE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
            testNumParams(aLogoObject, 1);
            double x = aLogoObject[0].toNumber();
            Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.object3D.Object3D.class);
            Scene3D v3d;
            for (int i = 0; i < v.size(); i++) {
                v3d = (Scene3D) v.elementAt(i);
                v3d.setCameraYawAngle(x);
                
            }
            return LogoVoid.obj;
        }

}
