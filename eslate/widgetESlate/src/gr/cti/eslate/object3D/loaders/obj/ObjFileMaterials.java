package gr.cti.eslate.object3D.loaders.obj;

import gr.cti.eslate.math.linalg.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.io.Reader;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.HashMap;
import java.net.URL;
import java.net.MalformedURLException;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import javax.imageio.ImageIO;
import com.sun.opengl.util.texture.Texture;

class ObjFileMaterials implements ImageObserver {
    // DEBUG
    // 1 = Name of materials
    // 16 = Tokens
    private static final int DEBUG = 0;

    private String curName = null;

    private ObjectFileMaterial cur = null;

    HashMap<String,ObjectFileMaterial> materials; // key=String name of material

    // value=ObjectFileMaterial

    private String basePath;
    
    private boolean fromUrl;

    class ObjectFileMaterial {

        public Vec3f Ka; // Remember, Ka, Kd, Ks are colors for ambient, diffusion and specular lighting

        public Vec3f Kd;

        public Vec3f Ks;

        public int illum;

        public float Ns;
        
        public BufferedImage imageMap;

        public Texture texture;

        public boolean transparent;
        
        public String name;

        public float transparencyLevel;

        public ObjectFileMaterial() {
            Ka = null;
            Kd = null;
            Ks = null;
            illum = - 1;
            Ns = - 1.0f;
            imageMap = null;
        } // End of ObjectFileMaterial
        
        public boolean isSame(ObjectFileMaterial material){
            if (material==null){
                return false;
            }
            if (illum==material.illum && 
                    ((Ka!= null && Ka.equals(material.Ka)) || (Ka ==null && material.Ka==null)) &&
                    ((Ks!= null && Ks.equals(material.Ks)) || (Ks ==null && material.Ks==null)) &&
                    ((Kd!= null && Kd.equals(material.Kd)) || (Kd ==null && material.Kd==null)) &&
                Ns==material.Ns && transparent==material.transparent &&
                transparencyLevel==material.transparencyLevel && name.equals(name)){
                return true;
            }
            return false;
        }
    }

    /*
    void assignMaterial(String matName, Shape3D shape) {
        ObjectFileMaterial p = null;

        if ((DEBUG&1)!=0)
            System.out.println("Color "+matName);

        Material m = new Material();
        p = (ObjectFileMaterial) materials.get(matName);
//        Appearance a = new Appearance();

        if (p!=null) {
            // Set ambient & diffuse color
            if (p.Ka!=null)
                m.setAmbientColor(p.Ka);
            if (p.Kd!=null)
                m.setDiffuseColor(p.Kd);

            // Set specular color
            if ((p.Ks!=null)&&(p.illum!=1))
                m.setSpecularColor(p.Ks);
            else if (p.illum==1)
                m.setSpecularColor(0.0f, 0.0f, 0.0f);

            if (p.illum>=1)
                m.setLightingEnable(true);
            else if (p.illum==0)
                m.setLightingEnable(false);

            if (p.Ns!=- 1.0f)
                m.setShininess(p.Ns);

            if (p.t!=null) {
                a.setTexture(p.t);
                // Create Texture Coordinates if not already present
                if ((((GeometryArray) shape.getGeometry()).getVertexFormat()&GeometryArray.TEXTURE_COORDINATE_2)==0) {
                    TexCoordGeneration tcg = new TexCoordGeneration();
                    a.setTexCoordGeneration(tcg);
                }
            }

            if (p.transparent)
                a.setTransparencyAttributes(new TransparencyAttributes(
                        TransparencyAttributes.NICEST, p.transparencyLevel));
        }
        a.setMaterial(m);
        if ((DEBUG&1)!=0)
            System.out.println(m);
        shape.setAppearance(a);
    } // End of assignMaterial
*/
    
    private void readName(ObjFileParser st) throws ParsingErrorException {
        st.getToken();
        if (st.ttype==StreamTokenizer.TT_WORD) {
            curName = new String(st.sval);
            cur = new ObjectFileMaterial();
            cur.name = curName;
            materials.put(curName, cur);
        }
        st.skipToNextLine();
    } // End of readName

    private void readAmbient(ObjFileParser st) throws ParsingErrorException {
        Vec3f p = new Vec3f();

        st.getNumber();
        p.setX((float) st.nval);
        st.getNumber();
        p.setY((float) st.nval);
        st.getNumber();
        p.setZ((float) st.nval);

        cur.Ka = p;

        st.skipToNextLine();
    } // End of readAmbient

    private void readDiffuse(ObjFileParser st) throws ParsingErrorException {
        Vec3f p = new Vec3f();

        st.getNumber();
        p.setX((float) st.nval);
        st.getNumber();
        p.setY((float) st.nval);
        st.getNumber();
        p.setZ((float) st.nval);

        cur.Kd = p;

        st.skipToNextLine();
    } // End of readDiffuse

    private void readSpecular(ObjFileParser st) throws ParsingErrorException {
        Vec3f p = new Vec3f();

        st.getNumber();
        p.setX((float) st.nval);
        st.getNumber();
        p.setY((float) st.nval);
        st.getNumber();
        p.setZ((float) st.nval);

        cur.Ks = p;

        st.skipToNextLine();
    } // End of readSpecular

    private void readIllum(ObjFileParser st) throws ParsingErrorException {

        st.getNumber();
        cur.illum = (int) st.nval;

        st.skipToNextLine();
    } // End of readSpecular

    private void readTransparency(ObjFileParser st)
            throws ParsingErrorException {

        st.getNumber();
        cur.transparencyLevel = (float) st.nval;
        if (cur.transparencyLevel<1.0f) {
            cur.transparent = true;
        }
        st.skipToNextLine();
    } // End of readTransparency

    private void readShininess(ObjFileParser st)
            throws ParsingErrorException {
        float f;

        st.getNumber();
        cur.Ns = (float) st.nval;
        if (cur.Ns<1.0f)
            cur.Ns = 1.0f;
        else if (cur.Ns>128.0f)
            cur.Ns = 128.0f;

        st.skipToNextLine();
    } // End of readSpecular

    public void readMapKd(ObjFileParser st) {
        
        // Filenames are case sensitive
        st.lowerCaseMode(false);

        // Get name of texture file (skip path)
        String tFile = null;
        do {
            st.getToken();
            if (st.ttype==StreamTokenizer.TT_WORD)
                tFile = st.sval;
        } while (st.ttype!=StreamTokenizer.TT_EOL);

        st.lowerCaseMode(true);

        if (tFile!=null) {
            
            // Check for filename with no extension
            if (tFile.lastIndexOf('.')!=- 1) {
                try {
                    File f = new File(basePath+tFile);
                    if (!f.exists())
                        System.out.println("ERROR! Resource not found!");
                    else{
                        BufferedImage img = ImageIO.read(f);
                        cur.imageMap = img;
                        //System.out.println("Texture load! "+cur.name+", resource: "+(basePath+tFile));
                    }

                } catch (FileNotFoundException e) {
                    // Texture won't get loaded if file can't be found
                } catch (MalformedURLException e) {
                    // Texture won't get loaded if file can't be found
                } catch (IOException e) {
                    // Texture won't get loaded if file can't be found
                }
            }
        }
        st.skipToNextLine();
    } // End of readMapKd

    private void readFile(ObjFileParser st) throws ParsingErrorException {
        
        int t;
        st.getToken();
        while (st.ttype!=StreamTokenizer.TT_EOF) {
               
            // Print out one token for each line
            if ((DEBUG&16)!=0) {
                System.out.print("Token ");
                if (st.ttype==StreamTokenizer.TT_EOL)
                    System.out.println("EOL");
                else if (st.ttype==StreamTokenizer.TT_WORD)
                    System.out.println(st.sval);
                else
                    System.out.println((char) st.ttype);
            }

            if (st.ttype==StreamTokenizer.TT_WORD) {
                st.sval = st.sval.toLowerCase(); 
                if (st.sval.equals("newmtl")) {
                    readName(st);
                } else if (st.sval.equals(("ka"))) {
                    readAmbient(st);
                } else if (st.sval.equals("kd")) {
                    readDiffuse(st);
                } else if (st.sval.equals("ks")) {
                    readSpecular(st);
                } else if (st.sval.equals("illum")) {
                    readIllum(st);
                } else if (st.sval.equals("d")) {
                    readTransparency(st);
                } else if (st.sval.equals("ns")) {
                    readShininess(st);
                } else if (st.sval.equals("tf")) {
                    st.skipToNextLine();
                } else if (st.sval.equals("sharpness")) {
                    st.skipToNextLine();
                } else if (st.sval.equals("map_kd")) {
                    readMapKd(st);
                } else if (st.sval.equals("map_ka")) {
                    readMapKd(st);
                    //st.skipToNextLine();
                } else if (st.sval.equals("map_ks")) {
                    readMapKd(st);
                    //st.skipToNextLine();
                } else if (st.sval.equals("map_ns")) {
                    st.skipToNextLine();
                } else if (st.sval.equals("bump")) {
                    st.skipToNextLine();
                }
            }

            st.skipToNextLine();

            // Get next token
            st.getToken();
        }
        if (curName!=null)
            materials.put(curName, cur);
    } // End of readFile

    void readMaterialFile(boolean fromUrl, String basePath, String fileName)
            throws ParsingErrorException {
        System.out.println("Material file: "+basePath+fileName);
        Reader reader;

        this.basePath = basePath;
        this.fromUrl = fromUrl;

        try {
            if (fromUrl) {
                reader = new InputStreamReader(
                        new BufferedInputStream((new URL(basePath+fileName)
                                .openStream())));
            } else {
                reader = new BufferedReader(new FileReader(basePath+fileName));
            }
        } catch (IOException e) {
            // couldn't find it - ignore mtllib
            return;
        }
        //if ((DEBUG&1)!=0)
            

        ObjFileParser st = new ObjFileParser(reader);
        readFile(st);
    } // End of readMaterialFile

    ObjFileMaterials() throws ParsingErrorException {
        Reader reader = new StringReader(DefaultMaterials.materials);

        ObjFileParser st = new ObjFileParser(reader);
        materials = new HashMap<String, ObjectFileMaterial>(50);
        readFile(st);
    } // End of ObjectFileMaterials

    /**
     * Implement the ImageObserver interface.  Needed to load jpeg and gif
     * files using the Toolkit.
     */
    public boolean imageUpdate(Image img, int flags, int x, int y, int w, int h) {

        return (flags&(ALLBITS|ABORT))==0;
    } // End of imageUpdate

} // End of class ObjectFileMaterials
