package gr.cti.eslate.scripting.logo;

import java.util.*;

/**
 * Greek language localized strings for the E-Slate handle primitive group.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 * @see         gr.cti.eslate.scripting.logo.ESlatePrimitives
 */
public class ESlateResource_el extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"SHOWHELP", "ΕΜΦΑΝΙΣΗΒΟΗΘΕΙΑΣ"},
    {"SHOWINFO", "ΕΜΦΑΝΙΣΗΠΛΗΡΟΦΟΡΙΩΝ"},
    {"SHOWPLUGVIEW","ΕΠΙΘΕΩΡΗΣΗΣΥΝΔΕΣΜΩΝ"},
    {"PLUGS", "ΣΥΝΔΕΣΜΟΙ"},
    {"CONNECT", "ΣΥΝΔΕΣΕ"},
    {"DISCONNECT", "ΑΠΟΣΥΝΔΕΣΕ"},
    {"DISCONNECTPLUG", "ΑΠΟΣΥΝΔΕΣΕΣΥΝΔΕΣΜΟ"},
    {"CONNECTED", "ΣΥΝΔΕΕΤΑΙ"},
    {"LISTCONNECTIONS", "ΚΑΤΑΛΟΓΟΣΣΥΝΔΕΣΕΩΝ"},
    {"SETRENAMINGALLOWEDFROMBAR", "ΘΕΣΕΕΠΙΤΡΕΠΕΤΑΙΗΜΕΤΟΝΟΜΑΣΙΑΑΠΟΜΠΑΡΑ"},
    {"RENAMINGALLOWEDFROMBAR", "ΕΠΙΤΡΕΠΕΤΑΙΗΜΕΤΟΝΟΜΑΣΙΑΑΠΟΜΠΑΡΑ"},
    {"SETNATIVEPROGRAMFOLDERS", "ΘΕΣΕΚΑΤΑΛΟΓΟΥΣΤΟΠΙΚΩΝΠΡΟΓΡΑΜΜΑΤΩΝ"},
    {"NATIVEPROGRAMFOLDERS", "ΚΑΤΑΛΟΓΟΙΤΟΠΙΚΩΝΠΡΟΓΡΑΜΜΑΤΩΝ"},
    {"NATIVEPROGRAM", "ΕΞΩΤΕΡΙΚΟΠΡΟΓΡΑΜΜΑ"},
    {"noComponent", "Δεν υπάρχει ψηφίδα με όνομα"},
    {"noPlug", "Δεν ορίσατε σύνδεσμο για την ψηφίδα"},
    {"component", "Η ψηφίδα"},
    {"dontHavePlug", "δεν έχει τέτοιο σύνδεσμο"},
    {"incompatible", "Οι σύνδεσμοι είναι ασύμβατοι"},
    {"alreadyConnected", "Οι σύνδεσμοι είναι ήδη συνδεδεμένοι"},
    {"notConnected", "Οι σύνδεσμοι δεν είναι συνδεδεμένοι"},
    {"progNotFound1", "Το πρόγραμμα "},
    {"progNotFound2", " δεν βρέθηκε"}
  };
}
