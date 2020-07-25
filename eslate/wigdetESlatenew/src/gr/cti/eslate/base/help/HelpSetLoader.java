package gr.cti.eslate.base.help;

import javax.help.HelpSet;
import java.io.*;
import java.net.URL;
import java.net.*;
import java.util.*;
import java.util.jar.*;
import gr.cti.eslate.base.ESlateMicroworld;

/**
 * The class HelpSetLoader gives out methods that load the helpset file
 *
 * @author      George Dimitrakopoulos
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 */
public class HelpSetLoader
{
    private static ResourceBundle info = ResourceBundle.getBundle(
      "gr.cti.eslate.base.help.MessageBundle",
      ESlateMicroworld.getCurrentLocale()
    );
    private static ResourceBundle errorInfo = ResourceBundle.getBundle(
      "gr.cti.eslate.base.help.ErrorMessageBundle",
      ESlateMicroworld.getCurrentLocale()
    );

  /**
   * The static load method loads the helpset you composed
   * @param     beanClass   The class next to which lies the help folder of the
   *                    component.
   * @return            The HelpSet object.
   */
  public static HelpSet load(Class beanClass)
  {
    /*
    if (ESlateMicroworld.getCurrentLocale().getLanguage().equals("el") ||
        ESlateMicroworld.getCurrentLocale().getLanguage().equals("el_GR")) {
      UIManager.put("Label.font", new Font("Helvetica", Font.PLAIN, 12));
    }
    */
    HelpSystemViewer.showInfoWindow(
      info.getString("waitDialogTitle"), info.getString("waitLabel"),
      //new JLabel(info.getString("waitLabel").),
      null
    );
    HelpSet tempHs = gr.cti.eslate.base.help.HelpSetLoader.getMasterHelpSet();

    String helpSetName = null;
    ClassLoader loader = beanClass.getClassLoader();
    String componentName = getComponentName(beanClass.getName());
    String language = ESlateMicroworld.getCurrentLocale().getLanguage();
    String country = ESlateMicroworld.getCurrentLocale().getCountry();
    URL helpSetURL = null;

    // Try locating the help set in the component's "help" subfolder.
    String tmpName = null;
    tmpName = "help/" + componentName + "HelpSet" + "_" +
              language+"_" + country + ".hs";
    if (beanClass.getResource(tmpName) != null) {
      helpSetName = tmpName;
    }else{
      tmpName = "help/" + componentName + "HelpSet" + "_" + language + ".hs";
      if (beanClass.getResource(tmpName) != null) {
        helpSetName = tmpName;
      }else{
        tmpName = "help/" + componentName + "HelpSet.hs";
        if (beanClass.getResource(tmpName) != null) {
          helpSetName = "help/"+componentName+"HelpSet.hs";
        }
      }
    }
    if (helpSetName != null) {
      helpSetURL = beanClass.getResource(helpSetName);
    }else{
      // Try locating the help set in the class path, in a catalog having the
      // same name as the component.
      String classPath = System.getProperty("java.class.path");
      String pathSep = System.getProperty("path.separator");
      //String fileSep = System.getProperty("file.separator");
      StringTokenizer st = new StringTokenizer(classPath, pathSep);
      String[] tryName = new String[3];
      tryName[0] = componentName + "/" + componentName + "HelpSet" + "_" +
                   language+"_" + country + ".hs";
      tryName[1] = componentName + "/" + componentName + "HelpSet" + "_" +
                   language + ".hs";
      tryName[2] = componentName + "/" + componentName + "HelpSet.hs";
      while (st.hasMoreTokens()) {
        String s = st.nextToken();
        for (int i=0; i<3; i++) {
          if (isJar(s)) {
            File f = new File(s);
            try {
              JarFile jf = new JarFile(f);
              JarEntry je = jf.getJarEntry(tryName[i]);
              if (je != null) {
                try {
                  helpSetURL =
                    new URL("jar:" + f.toURL().toString() + "!/" + tryName[i]);
                  helpSetName = tryName[i];
                  break;
                } catch (MalformedURLException mue) {
                }
              }
            } catch (IOException ioe) {
            }
          }else{
            File f = new File(s, tryName[i]);
            if (f.exists()) {
              try {
                helpSetURL = f.toURL();
                helpSetName = tryName[i];
                break;
              } catch (MalformedURLException mue) {
              }
            }
          }
        }
        if (helpSetURL != null) {
          break;
        }
      }
    }

    if (helpSetURL == null) {
      HelpSystemViewer.hideInfoWindow();
      System.out.println(
        errorInfo.getString("none") + componentName + "HelpSet" + "_" +
        language + "_" + country + ".hs " +
        errorInfo.getString("or") + "\n" +
        componentName + "HelpSet" + "_" + language + ".hs " + 
        errorInfo.getString("or") + "\n" +
        componentName + "HelpSet" + ".hs" + "\n"+
        errorInfo.getString("dir") + componentName + "\n" +
        errorInfo.getString("orDir") + " " + componentName + " " +
        errorInfo.getString("inClassPath") + "\n"
      );
      //return gr.cti.eslate.base.help.HelpSetLoader.getMasterHelpSet();
      throw new RuntimeException(errorInfo.getString("noHelpSupported"));
    }

    try {
      tempHs = new HelpSet(loader, helpSetURL);
      return tempHs;
    } catch (Exception e) {
      HelpSystemViewer.hideInfoWindow();
      System.out.println(
        errorInfo.getString("either1") + helpSetName +
        errorInfo.getString("either2") + "\n" +
        errorInfo.getString("or2") + " " + errorInfo.getString("file") +
        componentName + "Map\n" +
        errorInfo.getString("notCorrespond") +
        errorInfo.getString("mapref") +
        helpSetName + "\n"
      );
      //return gr.cti.eslate.base.help.HelpSetLoader.getMasterHelpSet();
    }
    return tempHs;
  }

  /**
   * Checks whether a given file is a jar file.
   * @param     f       The name of the file.
   * @return    True if the file ends in ".jar" or ".zip" regardless of case,
   *            false otherwise.
   */
  private static boolean isJar(String f)
  {
    f = f.toLowerCase();
    if (f.endsWith(".jar") || f.endsWith(".zip")) {
      return true;
    }else{
      return false;
    }
  }
       
  /**
   * This static method loads an empty helpset on which real helpsets can be
   * added.
   * @return    The HelpSet object.
   */
  public static HelpSet getMasterHelpSet()
  {
    HelpSet tempHs;
    try {
      URL url = gr.cti.eslate.base.help.HelpSetLoader.class.getResource(
        "MasterHelp/masterHelpSet.hs"
      );
      tempHs = new HelpSet(null, url);
      return tempHs;
    } catch (Exception ee) {
      return null;
    }
  }

  private static String getComponentName(String name)
  {
    String className = name;
    String prefix;
    int lastDot = className.lastIndexOf(".");
    if (lastDot < 0) {
      prefix = className;
    }else{
      prefix = className.substring(lastDot + 1);
    }
    return prefix;
  }
/*
  public static void main(String args[])
  {
    HelpSet master = HelpSetLoader.getMasterHelpSet();
    HelpSet hs1 = HelpSetLoader.load(gr.cti.eslate.clock.Clock.class);
    HelpSet hs2 = HelpSetLoader.load(
      gr.cti.eslate.set.Set.class
    );
    master.add(hs1);
    master.add(hs2);
    HelpSystemViewer hv = new HelpSystemViewer(master, 500, 700);
    hv.setLocation(100, 100);
    hv.setVisible(true);
    hv.addWindowListener(new java.awt.event.WindowAdapter(){
      public void windowClosing(java.awt.event.WindowEvent e)
      {
        try {
          System.exit(0);
        } catch (Throwable th) {
          th.printStackTrace();
        }
      }
    });
    hv.show();
  }
*/
}
