/*
 * Created on 13 Ιουν 2006
 *
 */
package gr.cti.eslate.scripting.logo;
import gr.cti.eslate.math.linalg.Vec3d;
import gr.cti.eslate.object3D.Object3D;

import java.util.Vector;

import virtuoso.logo.Console;
import virtuoso.logo.InterpEnviron;
import virtuoso.logo.LanguageException;
import virtuoso.logo.LogoList;
import virtuoso.logo.LogoObject;
import virtuoso.logo.LogoVoid;
import virtuoso.logo.Machine;
import virtuoso.logo.MyMachine;
import virtuoso.logo.PrimitiveGroup;
import virtuoso.logo.SetupException;

public class Object3DPrimitives extends PrimitiveGroup {
        MyMachine myMachine;

        protected void setup(Machine machine, Console console) throws SetupException {
            registerPrimitive("OBJECT3D.SET_POSITION","pSET_POSITION",3);
            registerPrimitive("OBJECT3D.SET_ORIENTATION","pSET_ORIENTATION",3);
            registerPrimitive("OBJECT3D.SET_SCALE","pSET_SCALE",3);
            registerPrimitive("OBJECT3D.POSITION","pPOSITION",0);
            registerPrimitive("OBJECT3D.ORIENTATION","pORIENTATION",0);
            registerPrimitive("OBJECT3D.SCALE","pSCALE",0);

            myMachine = (MyMachine) machine;

            if (console != null)
                console.putSetupMessage("Loaded Object3D primitives");

        }

        public final LogoObject pPOSITION(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
            testNumParams(aLogoObject, 0);
            Object3D obj3d = (Object3D) myMachine.componentPrimitives.getFirstComponentToTell(Object3D.class);
            Vec3d pos = obj3d.getPosition();
            Vector<Double> v = new Vector<Double>();
            v.add(new Double(pos.x()));
            v.add(new Double(pos.y()));
            v.add(new Double(pos.z()));
            return new LogoList(v);
        }

        public final LogoObject pORIENTATION(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
            testNumParams(aLogoObject, 0);
            Object3D obj3d = (Object3D) myMachine.componentPrimitives.getFirstComponentToTell(Object3D.class);
            Vec3d orientation = obj3d.getPosition();
            Vector<Double> v = new Vector<Double>();
            v.add(new Double(orientation.x()));
            v.add(new Double(orientation.y()));
            v.add(new Double(orientation.z()));
            return new LogoList(v);
        }
        
        public final LogoObject pSCALE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
            testNumParams(aLogoObject, 0);
            Object3D obj3d = (Object3D) myMachine.componentPrimitives.getFirstComponentToTell(Object3D.class);
            Vec3d scale = obj3d.getPosition();
            Vector<Double> v = new Vector<Double>();
            v.add(new Double(scale.x()));
            v.add(new Double(scale.y()));
            v.add(new Double(scale.z()));
            return new LogoList(v);
        }

        public final LogoObject pSET_POSITION(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
            testNumParams(aLogoObject, 3);
            double x = aLogoObject[0].toNumber();
            double y = aLogoObject[1].toNumber();
            double z = aLogoObject[2].toNumber();
            Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.object3D.Object3D.class);
            Object3D obj3d;
            for (int i = 0; i < v.size(); i++) {
                obj3d = (Object3D) v.elementAt(i);
                obj3d.setPosition(new Vec3d(x,y,z));
                
            }
            return LogoVoid.obj;
        }
        
        public final LogoObject pSET_ORIENTATION(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
            testNumParams(aLogoObject, 3);
            double x = aLogoObject[0].toNumber();
            double y = aLogoObject[1].toNumber();
            double z = aLogoObject[2].toNumber();
            Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.object3D.Object3D.class);
            Object3D obj3d;
            for (int i = 0; i < v.size(); i++) {
                obj3d = (Object3D) v.elementAt(i);
                obj3d.setOrientation(new Vec3d(x,y,z));
                
            }
            return LogoVoid.obj;
        }
        
        public final LogoObject pSET_SCALE(InterpEnviron interpenviron, LogoObject[] aLogoObject) throws LanguageException {
            testNumParams(aLogoObject, 3);
            double x = aLogoObject[0].toNumber();
            double y = aLogoObject[1].toNumber();
            double z = aLogoObject[2].toNumber();
            Vector v = myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.object3D.Object3D.class);
            Object3D obj3d;
            for (int i = 0; i < v.size(); i++) {
                obj3d = (Object3D) v.elementAt(i);
                obj3d.setScale(new Vec3d(x,y,z));
                
            }
            return LogoVoid.obj;
        }

        


}

