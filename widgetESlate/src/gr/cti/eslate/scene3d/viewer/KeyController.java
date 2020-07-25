package gr.cti.eslate.scene3d.viewer;

//~--- non-JDK imports --------------------------------------------------------

import gr.cti.eslate.math.linalg.*;

//~--- JDK imports ------------------------------------------------------------

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

//~--- classes ----------------------------------------------------------------

/**
 * Created by IntelliJ IDEA.
 * User: mantesat
 * Date: 3 Ιαν 2005
 * Time: 1:15:41 μμ
 * To change this template use Options | File Templates.
 */
public class KeyController extends KeyAdapter {
    private CharBuffer charBuffer = new CharBuffer(100);
    boolean            ctrlPressed, shiftPressed, altPressed;
    boolean            onScreenRenderingMode;
    final Viewer3D     viewer;

    //~--- constructors -------------------------------------------------------

    public KeyController(Viewer3D viewer) {
        this.viewer = viewer;
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param e
     */
    public void keyPressed(KeyEvent e) {
        charBuffer.store(e.getKeyChar());

        if (charBuffer.getLastEnteredStringByLength(9).equals("wireframe")) {
            viewer.onScrGLLlistener.wireframeView = !viewer.onScrGLLlistener.wireframeView;
        }
        if (charBuffer.getLastEnteredStringByLength(6).equals("headup")) {
            viewer.onScrGLLlistener.showHUD = !viewer.onScrGLLlistener.showHUD;
        }

        Camera   camera = viewer.getActiveCamera();
        float    yawAngle, rollAngle, pitchAngle;
        float    ANGLE_INCREMENT = (float) Math.PI / 180;
        boolean  sign;
        Object3D obj3d;

        for (int i = 0; i < viewer.objectsToView.size(); i++) {
            obj3d = viewer.objectsToView.get(i);

            if (obj3d instanceof EventResponsive) {
                boolean b = ((EventResponsive) obj3d).handleEvent(e);

                if (b) {
                    return;
                }
            }
        }

        float height = 50;

        switch (e.getKeyCode()) {
//        case KeyEvent.VK_Z :
//            onScreenRenderingMode = !onScreenRenderingMode;
//
//            break;

        case KeyEvent.VK_CONTROL :
            ctrlPressed = true;

            break;

        case KeyEvent.VK_SHIFT :
            shiftPressed = true;

            break;

        case KeyEvent.VK_ALT :
            altPressed = true;

            break;

        case KeyEvent.VK_UP :    // VK_NUMPAD8:
            if (!ctrlPressed) {
                camera.move(0, height);
            } else {
                float mod;

                if (shiftPressed) {
                    mod = 5 * ANGLE_INCREMENT;
                } else {
                    mod = ANGLE_INCREMENT;
                }

                pitchAngle = camera.getPitchAngle();
                sign       = Math.cos(pitchAngle) >= 0
                             == Math.cos(pitchAngle - mod) >= 0;

                if (sign) {
                    if (Math.abs(Math.cos((pitchAngle + mod))) > 0.005) {
                        pitchAngle -= mod;
                    } else {

                        // System.out.println("Gimball lock occured!");
                        break;
                    }
                }

                camera.setOrientation(pitchAngle, camera.getYawAngle(),
                                      camera.getRollAngle());
            }

            break;

        case KeyEvent.VK_DOWN :    // VK_NUMPAD2:
            if (!ctrlPressed) {
                camera.move(0, -height);
            } else {
                float mod;

                if (shiftPressed) {
                    mod = 5 * ANGLE_INCREMENT;
                } else {
                    mod = ANGLE_INCREMENT;
                }

                pitchAngle = camera.getPitchAngle();
                sign       = Math.cos(pitchAngle) >= 0
                             == Math.cos(pitchAngle + mod) >= 0;

                if (sign) {
                    if (Math.abs(Math.cos((pitchAngle + mod))) > 0.005) {
                        pitchAngle += mod;
                    } else {

                        // System.out.println("Gimball lock occured!");
                        break;
                    }
                }

                camera.setOrientation(pitchAngle, camera.getYawAngle(),
                                      camera.getRollAngle());
            }

            break;

        case KeyEvent.VK_LEFT :    // VK_NUMPAD4:
            if (!ctrlPressed) {
                camera.move(-height, 0);
            } else {
                yawAngle  = camera.getYawAngle();
                rollAngle = camera.getRollAngle();

                if (shiftPressed) {
                    yawAngle -= 5 * ANGLE_INCREMENT;
                } else {
                    yawAngle -= ANGLE_INCREMENT;
                }

                camera.setOrientation(camera.getPitchAngle(), yawAngle,
                                      rollAngle);
            }

            break;

        case KeyEvent.VK_RIGHT :    // VK_NUMPAD6:
            if (!ctrlPressed) {
                camera.move(+height, 0);
            } else {
                yawAngle  = camera.getYawAngle();
                rollAngle = camera.getRollAngle();

                if (shiftPressed) {
                    yawAngle += 5 * ANGLE_INCREMENT;
                } else {
                    yawAngle += ANGLE_INCREMENT;
                }

                camera.setOrientation(camera.getPitchAngle(), yawAngle,
                                      rollAngle);
            }

            break;

        /*
         * 
         *
          case KeyEvent.VK_D:
              viewer.getActiveCamera().pinpointPosition(new Vec3d(363922,3000,4308132));
              
              break;
         
         *
         *
         * case KeyEvent.VK_F:
         * if (!camera.isFlying()){
         *   camera.startFlying();
         *   engine3D.wallClock.start();
         * } else{
         *   camera.stopFlying();
         *   engine3D.wallClock.stop();
         *   //System.out.println("STOP FLYING");
         * }
         * break;
         *             case KeyEvent.VK_G:
         * fog = !fog;
         * if (fog)
         *   System.err.print("Enabling ");
         * else
         *   System.err.print("Disabling ");
         * System.err.println("fog");
         * break;
         */

        /*
         * case KeyEvent.VK_R:
         * if (camera.isRecording())
         *   camera.stopRecording();
         * else
         *   camera.startRecording();
         * case KeyEvent.VK_P:
         * if (camera.isReplaying())
         *   camera.stopReplaying();
         * else
         *   camera.startReplaying();
         * case KeyEvent.VK_B:
         * break;
         
        case KeyEvent.VK_O :
            viewer.onScrGLLlistener.showHUD = !viewer.onScrGLLlistener.showHUD;

            break;
          */
        // Camera orientation controls
        case KeyEvent.VK_PAGE_UP :
            int i = 1;

            // if (shiftPressed)
            // i = 5;
            camera.setPosition(new Vec3d(camera.position().x(),
                                         camera.position().y() + i * height,
                                         camera.position().z()));

            break;

        case KeyEvent.VK_PAGE_DOWN :
            i = 1;

            // if (shiftPressed)
            // i = 5;
            camera.setPosition(new Vec3d(camera.position().x(),
                                         camera.position().y() - i * height,
                                         camera.position().z()));

            break;

        /**
         * FIXME : needs more thought here : set orientation should be enough to set both orientation
         * and angles on camera but must figure out the inverse transformation
         */

        // old angle controls

        /*
         * case KeyEvent.VK_RIGHT:
         *   yawAngle = camera.getYawAngle();
         *   if (!camera.isFlying()){
         *       if (shiftPressed)
         *           yawAngle -= 5*ANGLE_INCREMENT;
         *       else
         *           yawAngle -= ANGLE_INCREMENT;
         *   } else
         *       rollAngle = camera.getRollAngle()-ANGLE_INCREMENT;
         *   camera.setOrientation(camera.getPitchAngle(), yawAngle, rollAngle);
         *   break;
         * case KeyEvent.VK_LEFT:
         *   yawAngle = camera.getYawAngle();
         *   if (!camera.isFlying()){
         *       if (shiftPressed)
         *           yawAngle += 5*ANGLE_INCREMENT;
         *       else
         *           yawAngle += ANGLE_INCREMENT;
         *   } else
         *       rollAngle = camera.getRollAngle()+ANGLE_INCREMENT;
         *   camera.setOrientation(camera.getPitchAngle(), yawAngle, rollAngle);
         *
         *   break;
         * case KeyEvent.VK_DOWN:
         *   float mod;
         *   if (shiftPressed)
         *       mod= 5*ANGLE_INCREMENT;
         *   else
         *       mod = ANGLE_INCREMENT;
         *   pitchAngle = camera.getPitchAngle();
         *   boolean sign = Math.cos(pitchAngle)>=0 == Math.cos(pitchAngle + mod)>=0;
         *   if (sign){
         *       if (Math.abs(Math.cos((pitchAngle + mod)))>0.005) {
         *           pitchAngle+=mod;
         *       }else{
         *           //System.out.println("Gimball lock occured!");
         *           break;
         *       }
         *   }
         *   camera.setOrientation(pitchAngle, camera.getYawAngle(), camera.getRollAngle());
         *   break;
         *
         * case KeyEvent.VK_UP:
         *   if (shiftPressed)
         *       mod= 5*ANGLE_INCREMENT;
         *   else
         *       mod = ANGLE_INCREMENT;
         *   pitchAngle = camera.getPitchAngle();
         *   sign = Math.cos(pitchAngle)>=0 == Math.cos(pitchAngle - mod)>=0;
         *   if (sign){
         *       if (Math.abs(Math.cos((pitchAngle + mod)))>0.005) {
         *           pitchAngle-=mod;
         *       }else{
         *           //System.out.println("Gimball lock occured!");
         *           break;
         *       }
         *   }
         *   camera.setOrientation(pitchAngle, camera.getYawAngle(), camera.getRollAngle());
         *   break;
         */
        case KeyEvent.VK_SPACE :
            break;

/*
            case KeyEvent.VK_Y:
                exaggeration += EXAGGERATION_INCREMENT;
                System.err.println("Incrementing exaggeration to "+exaggeration);
                break;

            case KeyEvent.VK_T:
                exaggeration -= EXAGGERATION_INCREMENT;
                if (exaggeration < EXAGGERATION_MIN)
                    exaggeration = EXAGGERATION_MIN;
                System.err.println("Decrementing exaggeration to "+exaggeration);
                break;

            case KeyEvent.VK_W:
                viewer.onScrGLLlistener.wireframeView = !viewer.onScrGLLlistener.wireframeView;
                System.err.println("Wireframe Mode: "+viewer.onScrGLLlistener.wireframeView);
                break;
/*
            case KeyEvent.VK_S:
                skyEnabled = !skyEnabled;
                //System.out.println("Enable sun & sky : "+skyEnabled);
                break;

            case KeyEvent.VK_R:
                Thread t = new Thread(){
                    public void run(){
                        try{
                            camera.startFlying();
                            int counter = 0;
                            int moveLeft = 1, moveUp = -1, moveFwd = 1;
                            while (true){
                                counter++;

                                if (counter%1000 == 0){
                                    moveLeft = Math.random() > 0.5 ? -1 : 1;
                                    moveUp = Math.random() > 0.5 ? -1 : 1;
                                    moveFwd = Math.random() > 0.5 ? -1 : 1;
                                    counter = 0;
                                }
                                float fwdIncr = moveFwd*(float) Math.random()*50;
                                float pitchIncr = moveUp*(float) Math.random()*ANGLE_INCREMENT;
                                float yawIncr = moveLeft*(float) Math.random()*ANGLE_INCREMENT;
                                camera.setOrientation(camera.getPitchAngle()+pitchIncr, camera.getYawAngle()+yawIncr, 0);
                                camera.pos_increment += fwdIncr;

                                sleep(100);
                            }
                        } catch (Exception exc){
                            exc.printStackTrace();
                        }
                    }
                };
                t.start();
                break;
            case KeyEvent.VK_Q:
                if (!(engine3D.frame == null)){
                    //gl.glFlush();
                    //engine3D.canvas.doCleanup();
                    //engine3D.canvas.cvsDispose();
                    //drawable.getGLContext().gljDestroy();
                    //drawable.getGLContext().gljFree();
                    engine3D.frame.remove(engine3D.canvas);
                    engine3D.frame.dispose();
                    System.exit(0);
                }
*/
        default :
        }

        // System.out.println("Lets repaint");
        // viewer.renderingWorkerThread.setDirty();
        viewer.canvas.repaint();
    }

    /**
     * Method description
     *
     *
     * @param e
     */
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
        case KeyEvent.VK_CONTROL :
            ctrlPressed = false;

            break;

        case KeyEvent.VK_SHIFT :
            shiftPressed = false;

            break;

        case KeyEvent.VK_ALT :
            altPressed = false;

            break;

        default :
            break;
        }
    }

    //~--- inner classes ------------------------------------------------------

    private class CharBuffer {
        private char[] charArray;
        private int    size;

        //~--- constructors ---------------------------------------------------

        CharBuffer(int size) {
            this.size = size;
            charArray = new char[size];
        }

        //~--- methods --------------------------------------------------------

        void store(char c) {
            System.arraycopy(charArray, 0, charArray, 1, size - 1);
            charArray[0] = c;
        }

        //~--- get methods ----------------------------------------------------

        char getLastEntered() {
            return charArray[0];
        }

        String getLastEnteredStringByLength(int length) {
            String s = "";

            for (int i = length - 1; i >= 0; i--) {
                s = s + charArray[i];
            }

            return s;
        }
    }
}
