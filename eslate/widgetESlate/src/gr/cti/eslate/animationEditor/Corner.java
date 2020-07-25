//package gr.cti.eslate.animationEditor;
//
//import java.awt.*;
//import javax.swing.*;
//
///**
// * This class implements a corner for the scroll pane.
// * @author	Augustine Grillakis
// * @version	1.0.0, 17-Apr-2002
// */
//// * @see     gr.cti.eslate.animationEditor.AnimationEditor
//public class Corner extends JComponent {
//  int position;
//
//  /**
//   * Create a corner.
//   * @param position  One of the 4 corners.
//   */
//  public Corner (int position) {
//    this.position = position;
//  }
//
//  /**
//   * Custom paintComponent.
//   * @param g The graphics class.
//   */
//  public void paintComponent(Graphics g) {
//    switch (position) {
//      case 1:
//        g.setColor(Color.black);
//        break;
//      case 2:
//        g.setColor(Color.yellow);
//        break;
//      case 3:
//        g.setColor(Color.black);
//        break;
//      case 4:
//        g.setColor(Color.lightGray);
//        break;
//    }
//    g.fillRect(0, 0, getSize().width, getSize().height);
//  }
//}
