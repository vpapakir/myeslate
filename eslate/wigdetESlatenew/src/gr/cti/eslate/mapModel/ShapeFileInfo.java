package gr.cti.eslate.mapModel;

/**
 * This class provides information about a shapefile.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	3.0.0, 2-Feb-2000
 */
class ShapeFileInfo {
    ShapeFileInfo(String path,int shapetype,int size) {
        this.path=path;
        this.shapetype=shapetype;
        this.size=size;
    }

    protected String getPath() {
        return path;
    }

    protected int getShapeType() {
        return shapetype;
    }

    protected int getSize() {
        return size;
    }

    static final int POINT_LAYER=1;
    static final int POLYLINE_LAYER=3;
    static final int POLYGON_LAYER=5;
    /**
     * This is not a default shapefile type.
     */
    static final int RASTER_LAYER=Integer.MAX_VALUE;

    private int shapetype;
    int size;
    private String path;
}