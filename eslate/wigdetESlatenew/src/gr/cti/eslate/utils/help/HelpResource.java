package gr.cti.eslate.utils.help;

import java.util.*;

/**
 * English language resources for the help set constructor.
 *
 * @version     2.0.0, 18-May-2006
 * @author      Kriton Kyrimis
 */
public class HelpResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"hsTitle", "Microworld help pages"},
    {"contents", "Contents"},
    {"index", "Index"},
    {"search", "Search"},
    {"selectDir", "Select directory containing help files"},
    {"adjustContents", "Adjust table of contents"},
    {"ok", "OK"},
    {"cancel", "Cancel"},
    {"up","Move up"},
    {"down", "Move down"},
    {"delete", "Remove"},
    {"enterName1", "Enter the name of the class for which to produce help"},
    {"enterName2", "Enter class name"},
    {"confirmOverwriteAll", "The help description files already exist.\nDo you want to recreate them?"},
    {"confirmOverwriteSome", "Some of the help description files already exist.\nDo you want to recreate them?"},
    {"confirm", "Confirmation"},
    {"warning", "Warning"},
    {"noHTML", "This folder does not contain any HTML files"},
    {"noHTMLleft", "You must include at least one HTML file in the help"},
  };
}
