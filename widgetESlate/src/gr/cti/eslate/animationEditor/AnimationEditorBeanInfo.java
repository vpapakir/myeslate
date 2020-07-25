package gr.cti.eslate.animationEditor;

import javax.swing.*;

import gr.cti.eslate.utils.*;

/**
 * BeanInfo for Animation Viewer component.
 *
 * @author	Augustine Grillakis
 * @version	1.0.0, 28-Apr-2002
 */
public class AnimationEditorBeanInfo extends ESlateBeanInfo {
  /**
   * Construct the BeanInfo.
   */
  public AnimationEditorBeanInfo() {
    super();
    try {
      set16x16ColorIcon(
        new ImageIcon(getClass().getResource("images/animationEditor.gif"))
      );
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

