package gr.cti.eslate.sharedObject;

import gr.cti.eslate.protocol.Walker;
import gr.cti.eslate.base.*;
import gr.cti.eslate.base.sharedObject.*;

/**
 * View shared object.
 *
 * @version     5.0.0, 19-May-2006
 * @author      George Birbilis
 * @author      Kriton Kyrimis
 */
public class View extends SharedObject
{
  public String movie;
  public int panAngle, tiltAngle;
  public float fov;
  public Walker walker;

  public static final int NEW_MOVIE = 0;
  public static final int NEW_PAN = 1;
  public static final int NEW_TILT = 2;
  public static final int NEW_FOV = 3;

  //26Apr1999: changed ESlateComponent to ESlateHandle
  public View(ESlateHandle app, Walker w)
  {
    super(app);
    this.walker = w;
    //when you want to unload a movie send a NEW_MOVIE with (movie==null)
    this.movie = null;
  }

  //

  public void set_movie(String movie, int pan, int tilt, float fov)
  {
    if (areDifferent(this.movie, movie) || (this.panAngle != pan) ||
        (this.tiltAngle != tilt) || (this.fov != fov)) {
      this.movie = movie;
      this.panAngle = pan;
      this.tiltAngle = tilt;
      this.fov = fov;
      //26Apr1999: changed getComponent() to getHandle()
      fireSharedObjectChanged(new SharedObjectEvent(this, NEW_MOVIE));
    }
  }

  public void set_pan(int pan)
  {
    if (panAngle != pan) {
      panAngle = pan;
      //26Apr1999: changed getComponent() to getHandle()
      fireSharedObjectChanged(new SharedObjectEvent(this, NEW_PAN));
    }
  }

  public void set_tilt(int tilt)
  {
    if (tiltAngle != tilt) {
      tiltAngle = tilt;
      //26Apr1999: changed getComponent() to getHandle()
      fireSharedObjectChanged(new SharedObjectEvent(this, NEW_TILT));
    }
  }

  public void set_fov(float fov)
  {
    if (this.fov != fov) {
      this.fov = fov;
      //26Apr1999: changed getComponent() to getHandle()
      fireSharedObjectChanged(new SharedObjectEvent(this, NEW_FOV));
    }
  }

}
