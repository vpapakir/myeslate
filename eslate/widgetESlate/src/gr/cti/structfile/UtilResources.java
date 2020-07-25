package gr.cti.structfile;

import java.util.*;

/**
 * English language localized strings for structured file utilities.
 *
 * @author      Kriton Kyrimis
 * @version     3.0.0, 18-May-2006
 */
public class UtilResources extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    // Resources for List
    {"listUsage", "Usage: List file"},

    // Resources for Defragment
    {"defragUsage", "Usage: Defragment file temporary_file"},
    {"deleteFailed1", "Failed to delete file "},
    {"deleteFailed2", ""},
    {"renameFailed1", "Failed to rename file "},
    {"renameFailed2", " to "},
    {"renameFailed3", ""},

    // Resources for FileTool
    {"fileToolName", "Structured file maintenance"},
    {"file", "File"},
    {"new", "New"},
    {"open", "Open"},
    {"close", "Close"},
    {"newFolder", "New folder"},
    {"delete", "Delete"},
    {"addFiles", "Add files"},
    {"addFolder", "Add folder"},
    {"optimize", "Optimize file"},
    {"exit", "Exit"},
    {"help", "Help"},
    {"about", "About..."},
    {"createFile", "Create structured file"},
    {"create", "Create"},
    {"openFile", "Open structured file"},
    {"ok", "OK"},
    {"error", "Error"},
    {"yes", "Yes"},
    {"no", "No"},
    {"confirm", "Confirmation"},
    {"confirmDelete1", "Are you sure you want to delete file"},
    {"confirmDelete2", "?"},
    {"confirmDeleteAll1", "Are you sure you want to delete folder"},
    {"confirmDeleteAll2", "and all its contents?"},
    {"confirmDeleteContents", "Are you sure you want to delete all the contents of this file?"},
    {"notDir1", "Entry"},
    {"notDir2", "is not a directory"},
    {"newFolderName", "New Folder"},
    {"noEntry", "No entry is selected"},
    {"add", "Add"},
    {"notExist1", "File"},
    {"notExist2", "does not exist"},
    {"notExist3", "Folder"},
    {"notExist4", "does not exist"},
    {"confirmOverWrite1", "Are you sure you want to overwrite file"},
    {"confirmOverWrite2", "?"},
    {"confirmOverWrite3", "Are you sure you want to replace folder"},
    {"confirmOverWrite4", "with file"},
    {"confirmOverWrite5", "?"},
    {"confirmOverWrite6", "Are you sure you want to overwrite file"},
    {"confirmOverWrite7", "with folder"},
    {"confirmOverWrite8", "?"},
    {"fileToolAbout0", "About FileTool"},
    {"fileToolAbout1", "Structured file maintenance tool, version"},
    {"fileToolAbout2", "Design & development: K. Kyrimis)"},
    {"fileToolAbout3", "© 199-2002 Computer Technology Institute"},
    {"formatVersion1", "File is in version "},
    {"formatVersion2", " format"},
  };
}
