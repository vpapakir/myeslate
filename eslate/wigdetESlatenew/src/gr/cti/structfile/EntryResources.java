package gr.cti.structfile;

import java.util.*;

/**
 * English language localized strings for class Entry.
 *
 * @author      Kriton Kyrimis
 * @version     3.0.0, 18-May-2006
 */
public class EntryResources extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"badName", "Entry name must be a non-empty string"},
    {"badLocation", "Location must be greater or equal to 0"},
    {"badSize", "Size must be greater or equal to 0"},
    {"badType", "Entry type must be either FILE or DIRECTORY"},
    {"root", "<root>"},
    {"file", "FILE"},
    {"directory", "DIRECTORY"},
    {"entry", "Entry"},
    {"size", "size"},
    {"type", "type"},
    {"location", "location"},
    {"parent", "parent"},
    {"notDir1", "Entry "},
    {"notDir2", " is not a directory"},
  };
}
