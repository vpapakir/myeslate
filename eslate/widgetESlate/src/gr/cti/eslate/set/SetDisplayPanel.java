package gr.cti.eslate.set;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * This class implements a panel containing a set panel sandwitched between
 * two panels where the queries that generated the sets in the set panel can
 * be displayed.
 * @author      Kriton Kyrimis
 * @version     2.0.0, 29-May-2006
 * @see         gr.cti.eslate.set.Set
 * @see         gr.cti.eslate.set.SetPanel
 */
class SetDisplayPanel extends JPanel
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  JPanel topPanel;
  JPanel bottomPanel;
  SetPanel setPanel;

  /**
   * Create a set display panel.
   * @param     s               The displayed set panel.
   * @param     showLabels      Specifies whether the panels where the query
   *                            texts are displayed are visible initially.
   */
  SetDisplayPanel(SetPanel s, boolean showLabels)
  {
    Color bgColor = s.bgColor;
    topPanel = new JPanel();
    topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
    topPanel.setBackground(bgColor);
    setPanel = s;
    bottomPanel = new JPanel();
    bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
    bottomPanel.setBackground(bgColor);

    SetLabel label = new SetLabel(bgColor);
    s.label[0] = label;
    topPanel.add(Box.createHorizontalGlue());
    topPanel.add(label);
    topPanel.add(Box.createHorizontalGlue());
    label.showCard(SetLabel.NOLABEL);
    label.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        setPanel.removeOval(0);
      }
    });

    label = new SetLabel(bgColor);
    s.label[1] = label;
    bottomPanel.add(label);
    bottomPanel.add(Box.createHorizontalGlue());
    label.showCard(SetLabel.NOLABEL);
    label.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        setPanel.removeOval(1);
      }
    });

    label = new SetLabel(bgColor);
    s.label[2] = label;
    bottomPanel.add(label);
    label.showCard(SetLabel.NOLABEL);
    label.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        setPanel.removeOval(2);
      }
    });

    setLayout(new BorderLayout());
    if (showLabels) {
      add(topPanel, BorderLayout.NORTH);
    }
    add(setPanel, BorderLayout.CENTER);
    if (showLabels) {
      add(bottomPanel, BorderLayout.SOUTH);
    }

    MouseListener ml = new MouseAdapter()
    {
      public void mousePressed(MouseEvent e)
      {
        if (setPanel.set.selecting || setPanel.set.deleteQuery) {
          //MouseEvent me = new MouseEvent(
            //(Component)(e.getSource()), e.getID(), e.getWhen(),
            //e.getModifiers(), 0, 0, e.getClickCount(), e.isPopupTrigger()
          //);
          MouseEvent me = SwingUtilities.convertMouseEvent(
            (Component)(e.getSource()), e, setPanel
          );
          setPanel.mousePressed(me);
        }
      }
      public void mouseReleased(MouseEvent e)
      {
        if (setPanel.set.selecting || setPanel.set.deleteQuery) {
          //MouseEvent me = new MouseEvent(
            //(Component)(e.getSource()), e.getID(), e.getWhen(),
            //e.getModifiers(), 0, 0, e.getClickCount(), e.isPopupTrigger()
          //);
          MouseEvent me = SwingUtilities.convertMouseEvent(
            (Component)(e.getSource()), e, setPanel
          );
          setPanel.mouseReleased(me);
        }
      }
    };
    topPanel.addMouseListener(ml);
    bottomPanel.addMouseListener(ml);
    for (int i=0; i<3; i++) {
      setPanel.label[i].bg.addMouseListener(ml);
    }
  }

  /**
   * Hide the panels where the queries are displayed.
   */
  void hideLabels()
  {
    removeAll();
    add(setPanel, BorderLayout.CENTER);
    revalidate();
  }

  /**
   * Show the panels where the queries are displayed.
   */
  void showLabels()
  {
    removeAll();
    add(topPanel, BorderLayout.NORTH);
    add(setPanel, BorderLayout.CENTER);
    add(bottomPanel, BorderLayout.SOUTH);
    revalidate();
  }

  /**
   * Change the top and bottom panels' bacjground color.
   * @param     color   The new background color.
   */
  public void recolor(Color color)
  {
    topPanel.setBackground(color);
    bottomPanel.setBackground(color);
    topPanel.repaint();
    bottomPanel.repaint();
  }
}
