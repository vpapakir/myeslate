package gr.cti.eslate.editor;

import org.netbeans.editor.*;

/**
 * Find dialog panel, extending the original netbeans class by localizing the
 * checboxes.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 24-May-2006
 */
public class FindDialogPanel extends org.netbeans.editor.ext.FindDialogPanel
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  /**
   * Create the panel.
   */
  public FindDialogPanel() {
    super();
    localize();
  }

  /**
   * Add the extra localization
   */
  private void localize()
  {
    highlightSearch.setText(LocaleSupport.getString("find-highlight-search"));
    incSearch.setText(LocaleSupport.getString("find-inc-search"));
    matchCase.setText(LocaleSupport.getString("find-match-case"));
    smartCase.setText(LocaleSupport.getString("find-smart-case"));
    wholeWords.setText(LocaleSupport.getString("find-whole-words"));
    bwdSearch.setText(LocaleSupport.getString("find-backward-search"));
    wrapSearch.setText(LocaleSupport.getString("find-wrap search"));
  }
}
