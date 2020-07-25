package gr.cti.eslate.base;

import java.util.*;

/**
 * English language localized strings for the E-Slate base classes.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.23, 23-Jan-2008
 */
public class ESlateResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    //
    // Resources for ESlateMicroworld
    //
    {"componentName",   "Microworld"},
    {"credits1", "Part of the E-Slate environment (http://E-Slate.cti.gr)"},
    {"credits2", "Development: K. Kyrimis"},
    {"credits3", "Copyright Ερευνητικό Ακαδημαϊκό Ινστιτούτο Τεχνολογίας Υπολογιστών-ΕΑ.ΙΤΥ 1993-2008.\nΑνάπτυξη τρέχουσας έκδοσης από τις Conceptum AE και ΕΧΟDUS AE.\nΤο Αβάκιο 2.0 υπόκειται σε διπλή άδεια (dual license), ήτοι ισχύουν οι όροι  της GNU-GPL License και της  L-GPL license.\n\nΕπιτρέπεται η παραγωγή, διανομή και εμπορική εκμετάλλευση κλειστού ή ανοιχτού κώδικα παράγωγων προϊόντων\nβασισμένων στην πλατφόρμα Αβάκιο 2.0 και κάθε επόμενης έκδοσής της καθώς επίσης και να συμπεριλαμβάνεται\nκαι να διανέμεται μαζί με τα προϊόντα αυτά η πλατφόρμα σε μορφή πηγαίου ή/και εκτελέσιμου κώδικα."},
    {"version", "version"},
    {"notSelf", "Cannot connect a plug to itself"},
    {"singleConnected1", "Plug \""},
    {"singleConnected2", "\" is already connected.\nConnecting this plug more than once is not allowed."},
    {"noProtocolDefined", "There is no protocol defined between the two plugs"},
    {"incompatible1", "Plugs have types that do not match and cannot be connected."},
    {"incompatible2", "Connect plugs that have the same color and matching nubs/sockets."},
    {"noShobjEither", "No shared object provided by either component"},
    {"cantOpen1", "Can't open file "},
    {"cantOpen2", ""},
    {"cantCreate1", "Can't create file "},
    {"cantCreate2", ""},
    {"loadPage", "Load page"},
    {"savePage", "Save page"},
    {"saveFailed", "Save operation failed"},
    {"notArchive", "The specified file is not a saved page archive"},
    {"badVersion", "Incompatible save file version"},
    {"required1", "--version "},
    {"required2", " required"},
    {"loadFailed", "Load operation failed"},
    {"notThis", "The specified file does not correspond to a saved archive of this page"},
    {"component", "Component"},
    {"dontHavePlug", "does not have a plug internally named"},
    {"sltDesc", "saved pages"},
    {"open", "Open"},
    {"save", "Save"},
    {"plugView", "Plug editor"},
    {"loadingPage", "Loading page"},
    {"loadingPleaseWait", "Loading page--please wait"},
    {"savingPage", "Saving page"},
    {"savingPleaseWait", "Saving page--please wait"},
    {"loading", "Loading page..."},
    {"saving", "Saving page..."},
    {"pleaseWait", "Please wait"},
    {"nullOldName", "Old name is null"},
    {"nullNewName", "New name is null"},
    {"noSuchComponent", "There is no component named"},
    {"microworldNameUsed", "This name is used by another microworld"},
    {"microworld", "Microworld"},
    {"defaultMicroworldName", "-=+*Microworld*+=-"},
    {"nullFolder", "Folders must not be null"},
    {"folderNotExist1", "Folder "},
    {"folderNotExist2", " does not exist"},
    {"folderNotDir1", "File "},
    {"folderNotDir2", " is not a directory"},
    {"cantDeleteInfo", "Cannot delete component information from microworld file"},
    {"notOpenLoad", "Microworld file is not open for reading"},
    {"notOpenSave", "Microworld file is not open for writing"},
    {"notExtSerializable", "Component is neither Externalizable nor Serializable"},
    {"connectSound", "connect.wav"},
    {"disconnectSound", "disconnect.au"},
    {"cantconnectSound", "cantconnect.wav"},
    {"failedToLoad", "The state of the following components failed to load:"},
    {"failedToSave", "Failed to save the state of the following components:"},
    {"failedToConnect", "The following problems were encountered while reconnecting components:"},
    {"restoringState", "Reading microworld state"},
    {"restoringComponentState", "Reading state of component"},
    {"restoringConnections", "Restoring connections"},
    {"savingState", "Saving microworld state"},
    {"savingComponentState", "Saving state of component"},
    {"copyComponentData", "Preserving component data"},
    {"deleteRedundantComponentData", "Deleting redundant component data"},
    {"noTarget", "Target plug does not belong to any component"},
    {"noOrig", "Original component not specified"},
    {"noOrigPlug", "Original plug not specified"},
//    {"noOrigPlug2", "Original component does not have such a plug"},
    {"notInMW", "does not exist in this microworld"},
    {"noHandle", "One of the two plugs does not belong to a component"},
    {"differentMicroworlds", "The plugs belong to different microworlds and cannot be connected"},
    {"noKey", "No key specified"},
    {"badKey", "Wrong key specified"},
    {"fromESlateHandle", "This method can only be invoked from class ESlateHandle"},
    {"selectDirToEditHelp", "Select folder in which to extract help files"},
    {"editAndHitOK1", "Edit the files in folder "},
    {"editAndHitOK2", " and press \"OK\""},
    {"editHelp", "Edit help files"},
    {"removeHelpDir1", "Delete folder "},
    {"removeHelpDir2", "?"},
    {"removeHelp", "Delete temporary files"},
    //
    // Resources for ESlateHandle
    //
    {"error", "Error"},
    {"showPlugs", "Show plugs"},
    {"help", "Help"},
    {"about", "About this component"},
    {"nullPlug", "Plug is null"},
    {"aTopPlugNamed", "A top-level plug named"},
    {"aTopPlugInternallyNamed", "A top-level plug internally named"},
    {"alreadyAttached", "is already attached to this component"},
    {"doesntHavePlug", "This component does not have a plug named"},
    {"nameUsed1", "The name "},
    {"nameUsed2", " is used by another component"},
    {"noHelp", "No help available for this component"},
    {"noHelpSystem", "The help system is not available"},
    {"noInfo", "No information available for this component"},
    {"helpFor", "Help for component"},
    {"nullMicroworld", "Microworld must not be null"},
    {"renamingForbidden", "Renaming components is currently forbidden"},
    {"renamingForbiddenHere", "Renaming this component is currently forbidden"},
    {"renamingMWForbidden", "Renaming this microworld is currently forbidden"},
    {"nullName", "Component name must not be empty or null"},
    {"cantContain1", "Illegal name \""},
    {"cantContain2", "\"-names cannot contain the character \""},
    {"cantContain3", "\""},
    {"nullHandle", "Handle must not be null"},
    {"reparentQuery1", "Component"},
    {"reparentQuery2", "has been removed from its parent."},
    {"reparentQuery3", "Do you want to destroy it or transfer it to one of the following components?"},
    {"reparentOK", "Transfer"},
    {"reparentCancel", "Destroy"},
    {"query", "Query"},
    {"noMicroworldFile", "No microworld file is open"},
    {"needMWToUnpack", "This component needs to unpack private file into the microworld file, but no microworld file is open"},
    {"compoNullName", "[Component with null name]"},
    //
    // Resources for Plug
    //
    {"noListener", "Shared object listener is null"},
    {"noSharedObject", "No shared object class specified"},
    {"class", "Class"},
    {"notShobjSubClass", "is not a subclass of class SharedObject"},
    {"nullProtocol", "Provided protocol was null"},
    {"notInterface", "is not a Java interface"},
    {"noComponent", "No component connected to this plug via the protocol mechanism"},
    {"manyComponents", "More than one component connected to this plug via the protocol mechanism"},
    {"aPlugNamed", "A plug named"},
    {"aPlugInternallyNamed", "A plug internally named"},
    {"alreadyAttachedToPlug", "is already attached to this plug"},
    {"noSubPlug", "This plug does not have a sub-plug named"},
    {"plug", "Plug"},
    {"notConnected", "is not connected to this plug"},
    {"notConnectedToInput", "is not connected to the input part of this plug"},
    {"notConnectedToOutput", "is not connected to the output part of this plug"},
    {"cantConnect1", "Plug "},
    {"cantConnect2", " cannot be connected to plug "},
    {"onlyIO", "Only the input or output part of a plug can be disconnected"},
    {"badRole", "Illegal role specified"},
    {"noImplementor", "Plug does not have a protocol implementor"},
    {"nullInterface", "Provided interface was null"},
    {"notImplements", "Protocol implementor does not implement interface"},
    {"nullKey", "Resource bundle key must not be null"},
    {"badShObj", "Shared object belongs to wrong class"},
    //
    // Resources for MasterControl
    //
    {"MCcomponentName", "Master Control component"},
    {"MCname", "Master_Control"},
    {"part", "Part of the E-Slate platform (http://E-Slate.cti.gr/)"},
    {"design", "Design & development: K. Kyrimis (1998-2000)"},
    {"funding", "Project \"IMEL\" (EC DGXXII, Socrates 25136-CP-1-96-1-GR-ODL)"},
    {"copyright", "© Computer Technology Institute"},
    {"version", "version"},
    {"MChelpfile", "help/mastercontrol.html"},
    //
    // Resources for Console
    //
    {"CcomponentName", "Console component"},
    {"Cname", "Console"},
    {"Chelpfile", "help/console.html"},
    {"saveText", "Save text"},
    {"copyText", "Copy text"},
    {"clearText", "Erase text"},
    {"logDesc", "Console output"},
    //
    // Resources for EORReachedException
    //
    {"eorText", "Component attempted to read more data than available"},
    //
    // Resources for PlugViewDesktopPane
    //
    {"noActive", "No active component"},
    {"order", "Order"},
    //
    // Resources for PlugViewDesktop
    //
    {"prefs", "Preferences"},
    {"resizeFrames", "Resize frames automatically"},
    {"showExist", "Highlight existing connections"},
    {"showNew", "Highlight possible connections"},
    {"autoOpen", "Open nodes automatically"},
    {"delayedAutoOpen", "Open nodes automatically after delay"},
    {"autoOpenCompatible", "Open compatible nodes automatically"},
    {"tools", "Tools"},
    {"edit", "Edit"},
    {"undoConnect", "Undo connection of plug "},
    {"redoConnect", "Redo connection of plug "},
    {"undoDisconnect", "Undo disconnection of plug "},
    {"redoDisconnect", "Redo disconnection of plug "},
    {"fromPlug", " from plug "},
    {"toPlug", " to plug "},
    {"undo", "Undo"},
    {"redo", "Redo"},
    //
    // Resources for PlugViewFrame
    //
    {"disconnect", "Disconnect"},
    {"disconnectAllplugs", "All components"},
    {"fromPlug", "Plug"},
    {"ofComponent", "of component"},
    {"confirm", "Confirm"},
    {"confirmDisconnect1", "Are you sure you want to disconnect plug"},
    {"confirmDisconnect2", "from all components?"},
    {"confirmDisconnect3", "from plug"},
    {"confirmDisconnect4", "of component"},
    {"confirmDisconnect5", "?"}
  };
}
