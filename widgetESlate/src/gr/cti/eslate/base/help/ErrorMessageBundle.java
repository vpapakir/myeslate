package gr.cti.eslate.base.help;

import java.util.ListResourceBundle;

/**
 * English language error message resources for the E-Slate help system.
 *
 * @author      George Dimitrakopoulos
 * @author      Kriton Kyrimis
 * @version     2.0.0, 19-May-2006
 */
public class ErrorMessageBundle extends ListResourceBundle
{
  public Object [][] getContents()
  {
    return contents;
  }

  static final Object[][] contents =
  {
    {"fileNotFound", "File not Found"},
    {"folderNotFound", "Forder not found"},
    {"comp", "for the component with helpset file"},
    {"attentionFile", "Check if you have declared the file in its corresponding helpset file with a different name"},
    {"attentionFolder", "Check if you have declared the folder in its corresponding helpset file with a different name or if it was not made properly by jhindexer"},
    {"none", "\nNone of the expected files\n"},
    {"or", "or"},
    {"dir", "was found in directory help of component "},
    {"orDir", "or in directory"},
    {"inClassPath", "in the class path"},
    {"notCorrespond", "does not correspond"},
    {"mapref",  "with the one declared in the field mapref of "},
    {"file", "file "},
    {"either1", "\nEither file "},
    {"either2", "was not found"},
    {"or2", "or"},
    {"search", "for the search function of the component or it was not made properly with jhindexer"},
    {"wrong", "There a mistake in the component's help"},
    {"attention", "Attention"},
    {"OK", "OK"},
    {"continue", "Help will continue for the rest of the components"},
    {"impossible", "Help System cannot be loaded"},
    {"toc", "Table of contents is empty"},
    {"rightInfo", "or if it contains proper data"},
    {"linkNotFound", "the relevant document was not found"},
    {"noHelpSupported", "Help is not available"},
    {"noStartingPage", "The starting page is not declared properly"},
    {"noStartingPageDetails", "The symbolic name used in the field <ÇÏÌÅID>...</HOMEID> of the"},
    {"noStartingPageDetails2", "does not correspond to existing data"}
  };
}
