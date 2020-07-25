package gr.cti.eslate.base.help;

import java.net.*;
import java.util.*;
import javax.help.*;
import javax.help.plaf.basic.*;
import javax.help.search.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.tree.*;

/**
 * UI for the help's search tab.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 19-May-2006
 */
public class ESlateSearchNavigatorUI extends BasicSearchNavigatorUI
{
  public ESlateSearchNavigatorUI(JHelpSearchNavigator b)
  {
    super(b);
  }

  public static ComponentUI createUI(JComponent x)
  {
    return new ESlateSearchNavigatorUI((JHelpSearchNavigator)x);
  }

  public void installUI(JComponent c)
  {
    super.installUI(c);
    updateColors();
  }

  /**
   * Set the correct colors for the component, based on the currently
   * installed look and feel.
   */
  public void updateColors()
  {
    tree.setBackground(UIManager.getColor("Panel.background"));

    JComponent c = (JComponent)(tree.getCellRenderer());
    if (c instanceof BasicSearchCellRenderer) {
      BasicSearchCellRenderer r = (BasicSearchCellRenderer)c;
      r.setBackgroundNonSelectionColor(UIManager.getColor("Panel.background"));
      r.setBackgroundSelectionColor(UIManager.getColor("control"));
      r.setTextNonSelectionColor(UIManager.getColor("controlText"));
      r.setTextSelectionColor(UIManager.getColor("controlText"));
    }
  }

  public void itemsFound(SearchEvent e)
  {
    //super.itemsFound(e);
    SearchTOCItem tocitem;
    ArrayList<DefaultMutableTreeNode> nodes =
      new ArrayList<DefaultMutableTreeNode>();

    // Add all the children of the topnode to the Vector of nodes.
    Enumeration children = topNode.children();
    while (children.hasMoreElements()) {
      DefaultMutableTreeNode node =
        (DefaultMutableTreeNode)children.nextElement();
      nodes.add(node);
    }
    HelpModel helpmodel = searchnav.getModel();
    HelpSet hs = helpmodel.getHelpSet();
    /*javax.help.Map map = */hs.getCombinedMap();
    Enumeration itemEnum = e.getSearchItems();
    while (itemEnum.hasMoreElements()) {
      SearchItem item = (SearchItem) itemEnum.nextElement();
      URL url;
      try {
        if (System.getProperty("java.version").startsWith("1.1.")) {
          url = new URL(item.getBase(), fixFilenameString(item.getFilename()));
          item = new SearchItem(
            item.getBase(), item.getTitle(), item.getLang(),
            fixFilenameString(item.getFilename()), item.getConfidence(),
            item.getBegin(), item.getEnd(), new Vector()
          );
        }else{
          url = new URL(item.getBase(), item.getFilename());
        }
      }catch (MalformedURLException me) {
        continue;
      }
      boolean foundNode = false;
      DefaultMutableTreeNode node = null;
      Iterator<DefaultMutableTreeNode> nodesIterator = nodes.iterator();
      while (nodesIterator.hasNext()) {
        node = nodesIterator.next();
        tocitem = (SearchTOCItem) node.getUserObject();
        URL testURL = tocitem.getURL();
        //System.out.println("testURL: " + testURL + " url: " + url);
        //if (testURL != null) {
        //  System.out.println("sameFile(): " + url.sameFile(testURL));
        //}
        if (testURL != null && url != null && url.sameFile(testURL)) {
          tocitem = (SearchTOCItem) node.getUserObject();
          tocitem.addSearchHit(new SearchHit(
            item.getConfidence(), item.getBegin(), item.getEnd())
          );
          foundNode = true;
          break;
        }
      }
      if (!foundNode) {
        tocitem = new SearchTOCItem(item);
        node = new DefaultMutableTreeNode(tocitem);
        nodes.add(node);
      }
    }
    topNode.removeAllChildren();
    // Create an array of the elements for sorting & copy the elements
    // into the array.
    int nNodes = nodes.size();
    DefaultMutableTreeNode[] array = new DefaultMutableTreeNode[nNodes];
    for (int i=0; i<nNodes; i++) {
      array[i] = nodes.get(i);
    }
    // Sort the array (Quick Sort)
    quicksort(array, 0, array.length - 1);
    // Reload the topNode. Everthing is in order now.
    for (int i=0; i < array.length ; i++) {
      topNode.add((DefaultMutableTreeNode)array[i]);
    }
    // Tell the tree to repaint itself
    ((DefaultTreeModel)tree.getModel()).reload();
    tree.invalidate();
    tree.repaint();
    ((DefaultTreeModel)tree.getModel()).reload();
  }

  private String fixFilenameString(String oldString)
  {
    String newString;
    newString = oldString.replace('\\','/');
    return newString;
  }

  private void quicksort(DefaultMutableTreeNode a[], int lo0, int hi0)
  {
    int lo = lo0;
    int hi = hi0;
    int mid;

    if ( hi0 > lo0) {
      // Arbitrarily establishing partition element as the midpoint of
      // the array.
      mid = ( lo0 + hi0 ) / 2;

      // Loop through the array until indices cross.
      while ( lo <= hi ) {
        // Find the first element that is greater than or equal to
        // the partition element starting from the left Index.
        while( ( lo < hi0 ) && ( compare(a[lo],a[mid]) > 0 )) {
          ++lo;
        }
        // Find an element that is smaller than or equal to
        // the partition element starting from the right Index.
        while( ( hi > lo0 ) && ( compare(a[hi],a[mid]) < 0 )) {
          --hi;
        }
        // If the indexes have not crossed, swap.
        if( lo <= hi ) {
          swap(a, lo, hi);
          ++lo;
          --hi;
        }
      }
      // If the right index has not reached the left side of array
      // must now sort the left partition.
      if ( lo0 < hi ) {
        quicksort( a, lo0, hi );
      }
      // If the left index has not reached the right side of array
      // must now sort the right partition.
      if ( lo < hi0 ) {
        quicksort( a, lo, hi0 );
      }
    }
  }

  private void swap(DefaultMutableTreeNode a[], int i, int j)
  {
    DefaultMutableTreeNode T;
    T = a[i];
    a[i] = a[j];
    a[j] = T;
  }

  private int compare (DefaultMutableTreeNode node1,
                       DefaultMutableTreeNode node2)
  {
    SearchTOCItem item1, item2;
    double confidence1, confidence2;
    int hits1, hits2;

    item1 = (SearchTOCItem) node1.getUserObject();
    confidence1 = item1.getConfidence();
    hits1 = item1.hitCount();

    item2 = (SearchTOCItem) node2.getUserObject();
    confidence2 = item2.getConfidence();
    hits2 = item2.hitCount();

    // Confidence is a penality. The lower the better>
    if (confidence1 > confidence2) {
      // node1 is less than node2
      return -1;
    }else{
      if (confidence1 < confidence2) {
        // node1 is greater than node2
        return 1;
      }else{
        // Confidences are the same check the hits
        if (hits1 < hits2) {
          // node1 is less than node2
          return -1;
        }else{
          if (hits1 > hits2) {
            // node2 is greater than node2
            return 1;
          }
        }
      }
    }
    // nodes1 and nodes2 are equivalent
    return 0;
  }
}
