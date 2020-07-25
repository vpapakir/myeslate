/*
 * Created on 8 Ιουν 2006
 *
 */
package gr.cti.eslate.scene3d;

import java.awt.Color;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import com.sun.opengl.util.GLUT;

import gr.cti.eslate.scene3d.viewer.Camera;
import gr.cti.eslate.scene3d.viewer.GLHandle;
import gr.cti.eslate.scene3d.viewer.Object3D;
import gr.cti.eslate.scene3d.viewer.Restrainer;

import gr.cti.eslate.math.linalg.*;


public class CartesianSystemHelper extends Object3D implements Restrainer{
    
    private int dList1=-1,dList2=-1;
    private Scene3D scene;
    
    CartesianSystemHelper(Scene3D scene){
        this.scene = scene;
    }

    /* (non-Javadoc)
     * @see gr.talent.viewer3d.Object3D#dispose()
     */
    @Override
    public void dispose() {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see gr.talent.viewer3d.Object3D#initialize(gr.talent.viewer3d.GLHandle)
     */
    @Override
    public void initialize(GLHandle arg0) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see gr.talent.viewer3d.Object3D#render(gr.talent.viewer3d.GLHandle)
     */
    @Override
    public int render(GLHandle glHandle) {
        if (!scene.cartesianHelperVisible)
            return 0;
        GL gl = glHandle.getGL();
       
        gl.glDisable(GL.GL_LIGHTING);
        Camera camera = glHandle.getCamera();
        camera.setNearClip(100);
        camera.setFarClip(1000000);
        if (dList2==-1){
            dList2 = gl.glGenLists(1);
            gl.glNewList(dList2, GL.GL_COMPILE);
            gl.glEnable(GL.GL_BLEND);
            gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
            gl.glColor4f(0,0,1,0.35f);
            
            for (int x=-4000;x<4000;x+=200 ){
                int i=0;
                for (int y=-4000;y<4000;y+=100 ){   
                    gl.glPushMatrix();
                    gl.glTranslatef(i%2==0?x:x+100,0,y);
                    gl.glBegin(GL.GL_TRIANGLE_STRIP);
                        gl.glVertex3d(0,0,0);
                        gl.glVertex3f(0,0,100);
                        gl.glVertex3f(100,0,0);
                        gl.glVertex3f(100,0,100);
                    gl.glEnd();
                    gl.glPopMatrix();
                    i+=1;
                }
                
            }
            gl.glColor4f(1,1,1,0.35f);
            for (int x=-4000;x<4000;x+=200 ){
                int i=0;
                for (int y=-4000;y<4000;y+=100 ){   
                    gl.glPushMatrix();
                    gl.glTranslatef(i%2==1?x:x+100,0,y);
                    gl.glBegin(GL.GL_TRIANGLE_STRIP);
                        gl.glVertex3d(0,0,0);
                        gl.glVertex3f(0,0,100);
                        gl.glVertex3f(100,0,0);
                        gl.glVertex3f(100,0,100);
                    gl.glEnd();
                    gl.glPopMatrix();
                    i+=1;
                }      
            }
            gl.glEndList();
        }else{
            gl.glCallList(dList2);
        }
        
        float mat_specular[] =
        { 1.0f, 1.0f, 1.0f, 1.0f };
        float mat_shininess[] =
        { 50.0f };
        float light_position[] =
        { 1000.0f, 1000.0f, 0.0f, 0.0f };
        
        gl.glDisable(GL.GL_BLEND);
        gl.glDisable(GL.GL_LIGHTING);
        gl.glColor4f(1,0,0,0.8f);
        glHandle.getGLUT().glutSolidSphere(10,10,10);
        gl.glPushMatrix();
            gl.glMatrixMode(GL.GL_PROJECTION);
            gl.glLoadIdentity();
            gl.glOrtho(0.0, camera.getViewWidth(), 0.0,
                           camera.getViewHeight(),-10000,10000); 
            gl.glMatrixMode(GL.GL_MODELVIEW);
            gl.glLoadIdentity();
            gl.glPushMatrix();
                gl.glTranslated(100,100,0);
               
                gl.glRotated(MathUtils.rad2dec(-camera.getPitchAngle()),1,0,0);
                gl.glRotated(MathUtils.rad2dec(-camera.getYawAngle()),0,1,0);
                gl.glRotated(MathUtils.rad2dec(-camera.getRollAngle()),0,0,1);
                if (dList1==-1){
                    dList1 = gl.glGenLists(1);
                    gl.glNewList(dList1, GL.GL_COMPILE);
                    gl.glShadeModel(GL.GL_SMOOTH);
                    gl.glEnable(GL.GL_LIGHTING);
                    gl.glEnable(GL.GL_LIGHT0);
                    GLU glu = new GLU();
                    GLUquadric quadric = glu.gluNewQuadric();
                    GLUT glut = new GLUT();
                    gl.glDisable(GL.GL_BLEND);
                    gl.glColor3f(1,0,0);
                    mat_specular = new float[]{ 1.0f, 0.0f, 0.0f, 0.0f };
                    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_SPECULAR, mat_specular, 0);  
                    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT, mat_specular, 0);  
                    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_DIFFUSE, mat_specular, 0);  
                    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_SHININESS, mat_shininess, 0);
                    gl.glPushMatrix();
                        gl.glRotatef(90,0,1,0);
                        drawVector(Vec3d.ORIGIN, 100,gl,glu,quadric,glut);
                    gl.glPopMatrix();
                
                    gl.glColor3f(0,1,0);
                    mat_specular = new float[]{ 0.0f, 1.0f, 0.0f, 0.0f };
                    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_SPECULAR, mat_specular, 0);  
                    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT, mat_specular, 0);  
                    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_DIFFUSE, mat_specular, 0);  
                    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_SHININESS, mat_shininess, 0);
                    gl.glPushMatrix();
                        gl.glRotatef(-90,1,0,0);
                        drawVector(Vec3d.ORIGIN, 100,gl,glu,quadric,glut);
                    gl.glPopMatrix();
                
                    gl.glColor3f(0,0,1);
                    mat_specular = new float[]{ 0.0f, 0.0f, 1.0f, 0.0f };
                    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_SPECULAR, mat_specular, 0);  
                    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT, mat_specular, 0);  
                    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_DIFFUSE, mat_specular, 0);  
                    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_SHININESS, mat_shininess, 0);
                    gl.glPushMatrix();
                        gl.glRotatef(180,0,1,0);
                        drawVector(Vec3d.ORIGIN, 100,gl,glu,quadric,glut);
                    gl.glPopMatrix();
                    gl.glDisable(GL.GL_LIGHTING);
                    gl.glDisable(GL.GL_LIGHT0);
                    gl.glEndList();
                }else{
                    gl.glCallList(dList1);
                }
            gl.glPopMatrix();
        gl.glPopMatrix();

        gl.glEnable(GL.GL_DEPTH_TEST);

        
        return 0;
    }
    
    private void drawVector(Vec3d origin, double length, GL gl, GLU glu, GLUquadric quadric, GLUT glut){
        glu.gluCylinder(quadric,5.0,5.0,length, 60, (int) length);
        gl.glPushMatrix();
            gl.glTranslated(0,0,length);
            glut.glutSolidCone(15,60,15,60);
        gl.glPopMatrix();
//        
    }

    
    public Vec3d getLegalRestrainPosition(Vec3d possiblePosition) {
        Vec3d legalPos = possiblePosition.copy();
        double xDim = scene.getBoundingBoxDimensions().x();
        double yDim = scene.getBoundingBoxDimensions().y();
        double zDim = scene.getBoundingBoxDimensions().z();
        
        if (possiblePosition.x() < -xDim/2)
            legalPos.setX(-xDim/2);
        if (possiblePosition.x() > xDim/2)
            legalPos.setX(xDim/2);
        if (possiblePosition.z() < -zDim/2)
            legalPos.setZ(-zDim/2);
        if (possiblePosition.z() > zDim/2)
            legalPos.setZ(zDim/2);   
        
        if (possiblePosition.y() < -yDim/2)
            legalPos.setY(-yDim/2);
        if (possiblePosition.y() > yDim/2)
            legalPos.setY(yDim/2);
        return legalPos;
    }

    public boolean insideRestrainBox(Vec3d position) {
        double x = position.x();
        double y = position.y();
        double z = position.z();
        double xDim = scene.getBoundingBoxDimensions().x();
        double yDim = scene.getBoundingBoxDimensions().y();
        double zDim = scene.getBoundingBoxDimensions().z();
        
        if (x>=-xDim/2 && x<=xDim/2 &&
            y>=-yDim/2 && y<=yDim/2 &&
            z>=-zDim/2 && z<=zDim/2)
            return true;
        return false;
    }

}
