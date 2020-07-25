package gr.cti.eslate.base;

import java.security.*;

/**
 * A security manager that does not allow the installation of other secirity
 * managers.
 *
 * @author      Kriton Kyrimis
 * @author      George Tsironis
 * @version     2.0.0, 18-May-2006
 * @see         gr.cti.eslate.base.ESlateHelpSystemViewer
 */
class ESlateSecurityManager extends SecurityManager
{
  boolean enabled = false;

  public void checkPermission(Permission perm)
  {
    if (enabled) {
      if (perm.getName().equals("setSecurityManager")) {
        super.checkPermission(perm);
      }
    }
  }

  void enable()
  {
    enabled = true;
  }

  void disable()
  {
    enabled = false;
  }
}
