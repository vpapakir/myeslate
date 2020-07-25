package gr.cti.structfile;

import java.util.*;

/**
 * English language localized strings for class StructFile.
 *
 * @author      Kriton Kyrimis
 * @version     3.0.0, 18-May-2006
 */
public class StructFileResources extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"writeProtected1", "File "},
    {"writeProtected2", " is write protected"},
    {"couldNotDelete", "Cannot delete old version of "},
    {"cannotOpen1", "Cannot open file "},
    {"cannotOpen2", ""},
    {"badMode", "Mode must be OLD or NEW"},
    {"notOpen", "The file is not open"},
    {"badEntry", "Entry must not be null or empty"},
    {"nullEntry", "Entry must not be null"},
    {"nullFiles", "Files must not be null"},
    {"equalFiles", "Files must be different"},
    {"noOpenDir", "Opening directories is not allowed"},
    {"notFound1", "Entry "},
    {"notFound2", " not found"},
    {"fileOrDir", "Entry type must be either Entry.FILE or Entry.DIRECTORY"},
    {"anotherEntry1", "There is another entry named "},
    {"anotherEntry2", ""},
    {"isCurrentDir1", "Entry "},
    {"isCurrentDir2", " is the current directory and cannot be deleted"},
    {"notDir1", "Entry "},
    {"notDir2", " is not a directory"},
    {"badPath", "Path must be a vector of successive names"},
    {"rootDir", "Current directory is the root directory"},
    {"alreadyOpen", "The structured file is already open"},
    {"nullDestination", "Destination structured file is null"},
    {"cantCdDest1", "Cannot change to directory "},
    {"cantCdDest2", " in destination file "},
    {"cantCdSrc1", "Cannot change to directory "},
    {"cantCdSrc2", " in source file "},
    {"nullBuffer", "Buffer is null"},
  };
}
