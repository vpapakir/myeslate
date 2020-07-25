/*
 * Created on 5 Ìבת 2005
 *
 */
package gr.cti.eslate.object3D.loaders.obj;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.swing.ImageIcon;

import com.sun.opengl.util.GLUT;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;

import gr.cti.eslate.object3D.loaders.obj.ObjFile.TriStrip;
import gr.cti.eslate.object3D.loaders.obj.ObjFileMaterials.ObjectFileMaterial;
import gr.cti.eslate.scene3d.viewer.Camera;
import gr.cti.eslate.scene3d.viewer.GLHandle;
import gr.cti.eslate.scene3d.viewer.Object3D;
import gr.cti.eslate.math.linalg.*;


/**
 * @author mantesat
 *
 */
public class ObjModel extends Object3D{
    
    //A simple object model for now...using only vertices and faces
    
    ArrayList coordsList, coordsIdxList, stripList, texCoordsList, texCoordsIdxList, strips, normalCoordsList, normalCoordsIdxList;
    ObjFileMaterials materials;
    HashMap groupMaterials;
    private int displayListCompileID = -1;
    private ObjectFileMaterial lastUsedMaterial;
    private ArrayList<Texture> texturesUsed = new ArrayList<Texture>();
    private float transparency = 1;
    
    Color color = Color.GREEN;
    GLUT glut = new GLUT();
    private boolean disposeMode = false;
    private ObjFile objFile;
    
    private boolean initialized = false;
    
    public ObjModel(ObjFile file){
        this.coordsList = file.coordList;
        this.coordsIdxList = file.coordIdxList;
        this.stripList = file.stripCounts;
        this.texCoordsList = file.texList;
        this.texCoordsIdxList = file.texIdxList;
        this.strips = file.strips;
        this.materials = file.objMaterials;
        this.groupMaterials = file.groupMaterials;
        this.normalCoordsList = file.normList;
        this.normalCoordsIdxList = file.normIdxList;
        this.objFile = file;
        
    }   
    
    public void setColor(Color color){
        this.color = color;
    }
    
    public Color getColor(){
        return color;
    }
    
    public void setObjFile(ObjFile file){
        disposeMode = true;
        this.coordsList = file.coordList;
        this.coordsIdxList = file.coordIdxList;
        this.stripList = file.stripCounts;
        this.texCoordsList = file.texList;
        this.texCoordsIdxList = file.texIdxList;
        this.strips = file.strips;
        this.materials = file.objMaterials;
        this.groupMaterials = file.groupMaterials;
        this.normalCoordsList = file.normList;
        this.normalCoordsIdxList = file.normIdxList;
    }
    
    public void dispose() {
        // TODO Auto-generated method stub
        coordsList = null;
        coordsIdxList = null;
        stripList = null;
        texCoordsList = null;
        texCoordsIdxList = null;
        strips = null;
        materials = null;
        groupMaterials = null;
        normalCoordsList = null;
        normalCoordsIdxList = null;
        coordsIdxList = null;
        stripList = null;
        texCoordsList = null;
        texCoordsIdxList = null;
        strips = null;
        materials = null;
        groupMaterials = null;
        normalCoordsList = null;
        normalCoordsIdxList = null;
        disposeMode = true;
        objFile = null;
        
        
    }

    private int render(Camera camera, GL gl, GLU glu) {  
        gl.glDisable(GL.GL_CULL_FACE);        
        gl.glDisable(GL.GL_BLEND);
        gl.glDisable(GL.GL_TEXTURE_2D);
        gl.glEnable(GL.GL_NORMALIZE);
        gl.glClear(GL.GL_STENCIL_BUFFER_BIT);
        Vec3f direction = camera.direction().toFloat();

        if (strips!= null){
            gl.glPushMatrix();
            // NOTE: For a strange reason, models sould be x-axis mirrored, i.e. scaled(-1,1,1)
            gl.glScalef(-1,1,1);
    
            if (displayListCompileID==-1){
                displayListCompileID = gl.glGenLists(1);
                gl.glNewList(displayListCompileID, GL.GL_COMPILE);
           
                TriStrip tristrip;
                if (strips!=null)
                for (int i=0;i<strips.size();i++){
                    tristrip = (TriStrip) strips.get(i);
                    gl.glColor3d(1, 0, 0);
                    int verticesNum = tristrip.vertexIndices.length;
                    int texCoordsNum = tristrip.textureIndices.length;
                    int normalCoordsNum = tristrip.normalIndices.length;
                    boolean hasTexture = false, hasNormals = false;
                    if (tristrip.textureIndices.length!=0)
                        hasTexture = true;
                    if (tristrip.normalIndices.length!=0)
                        hasNormals = true;
                    if (hasTexture && hasNormals)
                        renderStripFull(gl, tristrip);
                    else if (hasTexture)
                        renderStripGeomTexture(gl, tristrip);
                    else if (hasNormals)
                        renderStripGeomNormal(gl, tristrip);
                    else
                        renderStripGeomOnly(gl, tristrip);            
                }
                gl.glEndList();
            }else{
                gl.glCallList(displayListCompileID);
            }
            gl.glPopMatrix();
        }
        return 0;
    }
    
    public void renderStripGeomOnly(GL gl, TriStrip tristrip){
        Vec3f vertex;
        gl.glDisable(GL.GL_TEXTURE_2D);
        String materialName = tristrip.material;
        ObjectFileMaterial material = materials.materials.get(materialName);
        if (material != null && ! material.isSame(lastUsedMaterial)) {
            float transparency = material.transparencyLevel;
            if (transparency<1) {
                gl.glEnable(GL.GL_BLEND);
                gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
                gl.glEnable(GL.GL_CULL_FACE);
                gl.glCullFace(GL.GL_BACK);
            }
            if (material.Ks != null){
                float mat_specular[] = { material.Ks.x(), 
                                         material.Ks.y(),
                                         material.Ks.z(), 
                                         transparency };
                gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_SPECULAR, mat_specular,0);
            }
            if (material.Ka != null){
                float mat_ambient[] = {  material.Ka.x(), 
                                         material.Ka.y(),
                                         material.Ka.z(), 
                                         transparency };
                gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT,mat_ambient, 0);
            }
            if (material.Kd!= null){
                float mat_diffusion[] = {material.Kd.x(), 
                                         material.Kd.y(),
                                         material.Kd.z(), 
                                         transparency };
                gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_DIFFUSE, mat_diffusion,0);
            }
            float mat_shininess[] = { material.illum };
            gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);
            gl.glCullFace(GL.GL_NONE);
            gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_SHININESS,mat_shininess, 0);
        }   
        int verticesNum = tristrip.vertexIndices.length;
        gl.glBegin(GL.GL_POLYGON);
            for (int j=0;j<verticesNum;j++){
              vertex = (Vec3f) coordsList.get(tristrip.vertexIndices[j]);
              gl.glVertex3f(vertex.x(), vertex.y(), vertex.z());
            }
        gl.glEnd();
        if (material != null && !material.isSame(lastUsedMaterial)) {
            gl.glDisable(GL.GL_CULL_FACE);
            gl.glEnable(GL.GL_DEPTH_TEST);
            gl.glDisable(GL.GL_BLEND);
            lastUsedMaterial = material;
        }
    }
    
    public void renderStripGeomTexture(GL gl, TriStrip tristrip){
        Vec3f vertex;
        Vec2f textureCoord;
        int verticesNum = tristrip.vertexIndices.length;
        String materialName = tristrip.material;
        Texture t = null;
        if (materialName!=null)
            t = ((ObjectFileMaterial) materials.materials.get(materialName)).texture;
        if (t!= null){
            //System.out.println("material name: "+materialName+", group: "+tristrip.group);
            t.enable();
            t.bind();
            gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE);
            gl.glBegin(GL.GL_POLYGON);
                for (int j=0;j<verticesNum;j++){
                  vertex = (Vec3f) coordsList.get(tristrip.vertexIndices[j]);
                  textureCoord = (Vec2f) texCoordsList.get(tristrip.textureIndices[j]);
                  gl.glTexCoord2f(textureCoord.x(), textureCoord.y());
                  gl.glVertex3f(vertex.x(), vertex.y(), vertex.z());
                }
            gl.glEnd();
            t.disable();
        }else{   
            renderStripGeomOnly(gl, tristrip);
        }
    }
    
    public void renderStripGeomNormal(GL gl, TriStrip tristrip) {
        Vec3f vertex;
        Vec3f normalCoord;
        int verticesNum = tristrip.vertexIndices.length;
        String materialName = tristrip.material;
        ObjectFileMaterial material = (ObjectFileMaterial) materials.materials
                .get(materialName);
        
        if (material != null && !material.isSame(lastUsedMaterial)) {
            transparency = material.transparencyLevel;
            if (transparency<1) {
                gl.glEnable(GL.GL_BLEND);
                gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
                gl.glEnable(GL.GL_CULL_FACE);
                gl.glCullFace(GL.GL_BACK);
                
            }else{
                gl.glDisable(GL.GL_CULL_FACE);
                gl.glEnable(GL.GL_DEPTH_TEST);
                gl.glDisable(GL.GL_BLEND);
            }
            lastUsedMaterial = material;
            if (material.Ks != null){
                float mat_specular[] = { material.Ks.x(), 
                                         material.Ks.y(),
                                         material.Ks.z(), 
                                         transparency };
                gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_SPECULAR, mat_specular,0);
            }
            if (material.Ka != null){
                float mat_ambient[] = {  material.Ka.x(), 
                                         material.Ka.y(),
                                         material.Ka.z(), 
                                         transparency };
                gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT,mat_ambient, 0);
            }
            if (material.Kd!= null){
                float mat_diffusion[] = {material.Kd.x(), 
                                         material.Kd.y(),
                                         material.Kd.z(), 
                                         transparency };
                gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_DIFFUSE, mat_diffusion,0);
            }
            float mat_shininess[] = { material.illum };
            gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);
            gl.glCullFace(GL.GL_NONE);
            gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_SHININESS,
                    mat_shininess, 0);
        }
        gl.glBegin(GL.GL_POLYGON);
        for (int j = 0; j<verticesNum; j++) {
            vertex = (Vec3f) coordsList.get(tristrip.vertexIndices[j]);
            normalCoord = (Vec3f) normalCoordsList
                    .get(tristrip.normalIndices[j]);
            gl.glNormal3f(normalCoord.x(), normalCoord.y(), normalCoord.z());
            gl.glVertex3f(vertex.x(), vertex.y(), vertex.z());
        }
        gl.glEnd();

    }
    
    public void renderStripFull(GL gl, TriStrip tristrip){
        Vec3f vertex;
        Vec2f textureCoord;
        Vec3f normalCoord;
        int verticesNum = tristrip.vertexIndices.length;
        String materialName = tristrip.material;
        Texture t = null;
  
        if (materials.materials.get(materialName)==null)
            System.out.println("materials.materials.get(materialName)) NULL: materialName: "+materialName);
        if (materialName!=null && materials.materials.get(materialName)!=null)
            t = ((ObjectFileMaterial) materials.materials.get(materialName)).texture;
        if (t!= null){
            ObjectFileMaterial material = (ObjectFileMaterial) materials.materials.get(materialName);
            if (material!= null && !material.isSame(lastUsedMaterial)){
                float transparency = material.transparencyLevel;
                if (transparency<1){
                    gl.glEnable(GL.GL_BLEND);
                    gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
                    gl.glEnable(GL.GL_CULL_FACE);
                    gl.glCullFace(GL.GL_BACK);
                }
                if (material.Ks != null){
                    float mat_specular[] = { material.Ks.x(), 
                                             material.Ks.y(),
                                             material.Ks.z(), 
                                             transparency };
                    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_SPECULAR, mat_specular,0);
                }
                if (material.Ka != null){
                    float mat_ambient[] = {  material.Ka.x(), 
                                             material.Ka.y(),
                                             material.Ka.z(), 
                                             transparency };
                    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT,mat_ambient, 0);
                }
                if (material.Kd!= null){
                    float mat_diffusion[] = {material.Kd.x(), 
                                             material.Kd.y(),
                                             material.Kd.z(), 
                                             transparency };
                    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_DIFFUSE, mat_diffusion,0);
                }
                float mat_shininess[] =
                { material.illum};  
                gl.glMaterialfv(GL.GL_FRONT, GL.GL_SHININESS, mat_shininess, 0);
            } 
            //System.out.println("material name: "+materialName+", group: "+tristrip.group);
            t.enable();
            t.bind();
            gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
            gl.glBegin(GL.GL_POLYGON);
                for (int j=0;j<verticesNum;j++){
                  vertex = (Vec3f) coordsList.get(tristrip.vertexIndices[j]);
                  textureCoord = (Vec2f) texCoordsList.get(tristrip.textureIndices[j]);
                  normalCoord = (Vec3f)normalCoordsList.get(tristrip.normalIndices[j]);
                  gl.glNormal3f(normalCoord.x(), normalCoord.y(), normalCoord.z());
                  gl.glTexCoord2f(textureCoord.x(), textureCoord.y());

                  gl.glVertex3f(vertex.x(), vertex.y(), vertex.z());
                  
                }
            gl.glEnd();
            t.disable();
            if (material==null){
                lastUsedMaterial = null;
            }else if (!material.isSame(lastUsedMaterial)){
                gl.glDisable(GL.GL_CULL_FACE);
                gl.glEnable(GL.GL_DEPTH_TEST);
                gl.glDisable(GL.GL_BLEND);
                lastUsedMaterial = material;
            }
        }else{   
            renderStripGeomNormal(gl, tristrip);
        }
        
    }

    /* (non-Javadoc)
     * @see gr.talent.viewer3d.Object3D#initialize(gr.talent.viewer3d.GLHandle)
     */
    @Override
    public void initialize(GLHandle arg0) {
        // TODO Auto-generated method stub
        GL gl = arg0.getGL();
//        if (gl.isExtensionAvailable("GL_EXT_texture_rectangle"))
//            System.out.println("Non square textures supported!");
//        if (gl.isExtensionAvailable("GL_ARB_texture_rectangle"))
//            System.out.println("Non square textures supported!2");
//        if (gl.isExtensionAvailable("GL_ARB_texture_non_power_of_two"))
//            System.out.println("Non square textures supported!3");
        // This is where all texture initializations should be called.
        if (materials!= null){
            HashMap matHash = materials.materials;
            
            Iterator it = matHash.values().iterator();
            ObjectFileMaterial material;
            while (it.hasNext()){
                material = (ObjectFileMaterial) it.next();  
    
                if (material.imageMap!=null){
                    BufferedImage img;
                    // Try to produce power of 2
                    if (gl.isExtensionAvailable("GL_ARB_texture_rectangle") || 
                        gl.isExtensionAvailable("GL_ARB_texture_non_power_of_two")){
                        img = material.imageMap;
                    }else{
                        int width=MathUtils.power2((int) Math.ceil(MathUtils.log2(material.imageMap.getWidth())));
                        int height=MathUtils.power2((int) Math.ceil(MathUtils.log2(material.imageMap.getHeight())));
                        img = toBufferedImage(material.imageMap.getScaledInstance(width, height, BufferedImage.SCALE_FAST));
                    }
                    Texture t = TextureIO.newTexture(img, false); 
                    texturesUsed.add(t);
                    material.texture = t;
                    img.flush();
                    img = null;
                    material.imageMap.flush();
                    material.imageMap = null;
                }
            }
        }
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glDepthFunc(GL.GL_LESS);
    }
    
//  This method returns a buffered image with the contents of an image
    private BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage)image;
        }
    
        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();
    
        // Create a buffered image with a format that's compatible with the screen
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;
            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(
                image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
            // The system does not have a screen
        }
    
        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }
    
        // Copy image to buffered image
        Graphics g = bimage.createGraphics();
    
        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();
    
        return bimage;
    }

    /* (non-Javadoc)
     * @see gr.talent.viewer3d.Object3D#render(gr.talent.viewer3d.GLHandle)
     */
    @Override
    public int render(GLHandle handle) {
        // TODO Auto-generated method stub
        if (disposeMode){
            disposeMode = false;
            Texture t;
            for (int i=0;i<texturesUsed.size();i++){
                t = texturesUsed.get(i);
                t.dispose();
            }
            texturesUsed.clear();
            handle.getGL().glDeleteLists(displayListCompileID,1);
            initialized = false;
            displayListCompileID=-1;
        }
        
        if (!initialized){
            initialize(handle);
            initialized = true;
        }
        render(handle.getCamera(), handle.getGL(), handle.getGLU());
        return 0;
    }

    /* (non-Javadoc)
     * @see gr.talent.viewer3d.Object3D#renderStateChanged()
     */
    @Override
    public void renderStateChanged() {
        // TODO Auto-generated method stub
        super.renderStateChanged();
    }
}
