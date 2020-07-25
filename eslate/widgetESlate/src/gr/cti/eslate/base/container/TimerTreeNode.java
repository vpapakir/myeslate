package gr.cti.eslate.base.container;

import javax.swing.tree.*;

/**
 * This class represents nodes in the tree of the PerformanceManager's GUI.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
class TimerTreeNode extends DefaultMutableTreeNode
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * The PerformanceTimerGroup corresponding to this node.
   */
  PerformanceTimerGroup timer;
  /*
   * The ClassTimer corresponding to this node.
   */
  ClassTimer classTimer;

  /**
   * Create a TimerTreeNode.
   * @param     ptg     The PerformanceTimerGroup corresponding to this node.
   */
  TimerTreeNode(PerformanceTimerGroup ptg)
  {
    super();
    timer = ptg;
    classTimer = null;
  }
  
  /**
   * Create a TimerTreeNode.
   * @param     ct      The ClassTimer corresponding to this node.
   */
  TimerTreeNode(ClassTimer ct)
  {
    super();
    timer = null;
    classTimer = ct;
  }
}
