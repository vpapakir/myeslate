package gr.cti.eslate.scripting.logo;

import java.util.*;

/**
 * English language localized strings for the E-Slate handle primitive group.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 * @see         gr.cti.eslate.scripting.logo.ESlatePrimitives
 */
public class ESlateResource extends ListResourceBundle
{
  public Object[][] getContents()
  {
    return contents;
  }

  static final Object[][] contents = {
    {"SHOWHELP", "SHOWHELP"},
    {"SHOWINFO", "SHOWINFO"},
    {"SHOWPLUGVIEW","SHOWPLUGVIEW"},
    {"PLUGS", "PLUGS"},
    {"CONNECT", "CONNECT"},
    {"DISCONNECT", "DISCONNECT"},
    {"DISCONNECTPLUG", "DISCONNECTPLUG"},
    {"CONNECTED", "CONNECTED"},
    {"LISTCONNECTIONS", "LISTCONNECTIONS"},
    {"SETRENAMINGALLOWEDFROMBAR", "SETRENAMINGALLOWEDFROMBAR"},
    {"RENAMINGALLOWEDFROMBAR", "RENAMINGALLOWEDFROMBAR"},
    {"SETNATIVEPROGRAMFOLDERS", "SETNATIVEPROGRAMFOLDERS"},
    {"NATIVEPROGRAMFOLDERS", "NATIVEPROGRAMFOLDERS"},
    {"NATIVEPROGRAM", "NATIVEPROGRAM"},
    {"noComponent", "There is no component named"},
    {"noPlug", "You did not specify a plug for component"},
    {"component", "Component"},
    {"dontHavePlug", "does not have such a plug"},
    {"incompatible", "The plugs are incompatible"},
    {"alreadyConnected", "The plugs are already connected"},
    {"notConnected", "The plugs are not connected"},
    {"progNotFound1", "Program "},
    {"progNotFound2", " not found"}
  };
}
