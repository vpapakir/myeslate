package gr.cti.eslate.protocol;

import java.awt.image.BufferedImage;

/**
 * Raster layer view interface.
 */
public interface IRasterLayerView extends ILayerView {
    /**
     * The raster. If it is swapped out, the method loads it as well.
     */
    public abstract BufferedImage getRaster();
    /**
     * The transparency level of the color areas when in normal view.
     * It is given between [0,255], with 0 meaning transparent and 255 meaning opaque.
     */
    public abstract int getNormalViewTransparencyLevel();
    /**
     * The transparency level of the color areas when in selected view.
     * It is given between [0,255], with 0 meaning transparent and 255 meaning opaque.
     */
    public abstract int getSelectedViewTransparencyLevel();
    /**
     * The transparency level of the color areas when in highlighted view.
     * It is given between [0,255], with 0 meaning transparent and 255 meaning opaque.
     */
    public abstract int getHighlightedViewTransparencyLevel();
}