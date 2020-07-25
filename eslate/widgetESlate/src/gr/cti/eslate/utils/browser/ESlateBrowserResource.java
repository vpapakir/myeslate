package gr.cti.eslate.utils.browser;

import java.util.*;

/**
 * English language localized strings for ESlateBrowser and its subclasses.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.12, 31-Oct-2006
 */
public class ESlateBrowserResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    // Location
    {"changedLocation", "Changed location"},
    {"changingLocation", "Changing location"},
    // Title
    {"changedTitle", "Changed title"},
    // Status text
    {"changedStatusText", "Changed status text"},
    // WebBrowserEvents
    {"documentCompleted", "Document completed"},
    {"downloadCompleted", "Download completed"},
    {"downloadError", "Download error"},
    {"downloadProgress", "Download progress"},
    {"downloadStarted", "Download started"},
    {"statusTextChange", "Status text change"},
    {"titleChange", "Title change"},
    // HistoryChanged
    {"historyChanged", "History changed"},
    // LinkFollowed
    {"linkFollowed", "Link followed"}
  };
}
