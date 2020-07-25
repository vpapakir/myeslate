//Version: 3Apr2001

package gr.cti.eslate.stage.models;

import java.awt.Image;

public interface HasImage
{
 public void setImage(String filename);
 public void setImage(Image image);
 public Image getImage();
}

