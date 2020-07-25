package gr.cti.eslate.utils.help;

import java.io.*;
import java.util.*;

import gr.cti.eslate.utils.*;

/**
 * This class contains methods that build the files required by JavaHelp.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.23, 23-Jan-2008
 */
class Files
{
  /**
   * The name of the helpset file.
   */
  private static String helpSet;
  /**
   * The name of the map file.
   */
  private static String map;
  /**
   * The name of the table of contents file.
   */
  private static String toc;
  /**
   * The name of the index file.
   */
  private static String index;
  /**
   * The name of the folder containing the serach indices.
   */
  private static String javaHelpSearch;
  /**
   * The name of the order hints file.
   */
  private static String orderHints;

  /**
   * Localized resources.
   */
  private static ResourceBundle resources =
    ResourceBundle.getBundle(
      "gr.cti.eslate.utils.help.HelpResource", Locale.getDefault()
    );

  /**
   * Build the helpset file.
   * @param     dir             The folder containing the help.
   * @param     encoding        The encoding used by the help files.
   * @param     ft              The tree structure of the help.
   * @param     overwrite       Specifies if the helpset file will be
   *                            overwritten if it exists.
   * @exception IOException     Thrown if something goes wrong.
   */
  static void makeHelpSet(File dir, String encoding, FileTree ft,
                          boolean overwrite)
    throws IOException
  {
    File f = new File(dir, helpSet);
    if (!overwrite && f.exists()) {
      return;
    }
    FileOutputStream fos = new FileOutputStream(f);
    BufferedOutputStream bos = new BufferedOutputStream(fos);
    PrintStream out = new PrintStream(bos);

    try {
      out.println("<?xml version='1.0' encoding='" + encoding + "' ?>");
      out.println("");
      out.println("<!DOCTYPE helpset PUBLIC \"-//Sun Microsystems Inc.//DTD JavaHelp HelpSet Version 1.0//EN\" \"http://java.sun.com/products/javahelp/helpset_1_0.dtd\">");
      out.println("");
      out.println("<helpset version=\"1.0\">");
      out.println("");
      out.println("  <!-- title -->");
      out.println("  <title>" + resources.getString("hsTitle") + "</title>");
      out.println("");
      out.println("  <!-- maps -->");
      out.println("  <maps>");
      FileTree ft2 = ft.getFirst();
      String homeID;
      if (ft2 != null) {
        homeID = ft2.mapName;
      }else{
        homeID = "homeID";
      }
      out.println("     <homeID>" + homeID +"</homeID>");
      out.println("     <mapref location=\"" + map + "\"/>");
      out.println("  </maps>");
      out.println("");
      out.println("  <!-- views -->");
      out.println("  <view>");
      out.println("    <name>TOC</name>");
      out.println("    <label>" + resources.getString("contents") + "</label>");
      out.println("    <type>javax.help.TOCView</type>");
      out.println("    <data>" + toc + "</data>");
      out.println("  </view>");
      out.println("");
      out.println("  <view>");
      out.println("    <name>Index</name>");
      out.println("    <label>" + resources.getString("index") + "</label>");
      out.println("    <type>javax.help.IndexView</type>");
      out.println("    <data>" + index + "</data>");
      out.println("  </view> ");
      out.println("");
      out.println(" <view>");
      out.println("    <name>Search</name>");
      out.println("    <label>" + resources.getString("search") + "</label>");
      out.println("    <type>javax.help.SearchView</type>");
      out.println("    <data engine=\"com.sun.java.help.search.DefaultSearchEngine\">");
      out.println("      " + javaHelpSearch);
      out.println("    </data>");
      out.println("  </view>");
      out.println("</helpset>");
    } finally {
      out.close();
    }
  }

  /**
   * Build the table of contents.
   * @param     dir             The folder containing the help.
   * @param     encoding        The encoding used by the help files.
   * @param     ft              The tree structure of the help.
   * @param     overwrite       Specifies if the table of contents will be
   *                            overwritten if it exists.
   * @exception IOException     Thrown if something goes wrong.
   */
  static void makeTOC(File dir, String encoding, FileTree ft, boolean overwrite)
    throws IOException
  {
    File f = new File(dir, toc);
    if (!overwrite && f.exists()) {
      return;
    }
    FileOutputStream fos = new FileOutputStream(f);
    BufferedOutputStream bos = new BufferedOutputStream(fos);
    PrintStream out = new PrintStream(bos);

    try {
      out.println("<?xml version='1.0' encoding='" + encoding + "' ?>");
      out.println("<!DOCTYPE toc PUBLIC \"-//Sun Microsystems Inc.//DTD JavaHelp TOC Version 1.0//EN\" \"http://java.sun.com/products/javahelp/toc_1_0.dtd\">");
      out.println();
      out.println("<toc version=\"1.0\">");
      outTOC(out, ft, 1);
      out.println("</toc>");
    }finally{
      out.close();
    }
  }

  /**
   * Produce the tocitem tags for the table of contents.
   * @param     out     The stream where the tags should be written.
   * @param     ft      The tree structure of the help.
   * @param     indent  The indentation level.
   */
  private static void outTOC(PrintStream out, FileTree ft, int indent)
  {
    ArrayList<FileTree> a = ft.contents;
    int n = a.size();
    for (int i=0; i<n; i++) {
      FileTree ft2 = a.get(i);
      for (int j=0; j<indent; j++) {
        out.print("  ");
      }
      if (ft2.type == FileTree.FILE) {
        out.println("<tocitem text=\"" + ft2.description + "\" " +
                    "target=\"" + ft2.mapName + "\"/>");
      }else{
        out.println("<tocitem text=\"" + ft2.description + "\">");
        outTOC(out, ft2, indent+1);
        for (int j=0; j<indent; j++) {
          out.print("  ");
        }
        out.println("</tocitem>");
      }
    }
  }

  /**
   * Build the index file.
   * @param     dir             The folder containing the help.
   * @param     encoding        The encoding used by the help files.
   * @param     ft              The tree structure of the help.
   * @param     overwrite       Specifies if the index file will be
   *                            overwritten if it exists.
   * @exception IOException     Thrown if something goes wrong.
   */
  static void makeIndex(File dir, String encoding, FileTree ft,
                        boolean overwrite)
    throws IOException
  {
    File f = new File(dir, index);
    if (!overwrite && f.exists()) {
      return;
    }
    FileOutputStream fos = new FileOutputStream(f);
    BufferedOutputStream bos = new BufferedOutputStream(fos);
    PrintStream out = new PrintStream(bos);

    try {
      out.println("<?xml version='1.0' encoding='" + encoding + "'  ?>");
      out.println("<!DOCTYPE index PUBLIC \"-//Sun Microsystems Inc.//DTD JavaHelp Index Version 1.0//EN\" \"http://java.sun.com/products/javahelp/index_1_0.dtd\">");
      out.println();
      out.println("<index version=\"1.0\">");
      outIndex(out, ft);
      out.println("</index>");
    }finally{
      out.close();
    }
  }

  /**
   * Produce the indexitem tags for the index file.
   * @param     out     The stream where the tags should be written.
   * @param     ft      The tree structure of the help.
   */
  private static void outIndex(PrintStream out, FileTree ft)
  {
    ArrayList<FileTree> allFTs = new ArrayList<FileTree>();
    collectFTs(ft, allFTs);
    Object[] a = allFTs.toArray();
    Arrays.sort(a);
    int n = a.length;
    for (int i=0; i<n; i++) {
      FileTree ft2 = (FileTree)a[i];
      out.println("  <indexitem text=\"" + ft2.description + "\" " +
                  "target=\"" + ft2.mapName + "\"/>");
    }
  }

  /**
   * Rcursively builds a list of all the nodes of the tree structure of the
   * help that correspond to files.
   * @param     ft      The node below which the nodes will be accumulated.
   * @param     allFTs  The list in which the nodes are collected.
   */
  private static void collectFTs(FileTree ft, ArrayList<FileTree> allFTs)
  {
    ArrayList<FileTree> a = ft.contents;
    int n = a.size();
    for (int i=0; i<n; i++) {
      FileTree ft2 = a.get(i);
      if (ft2.type == FileTree.FILE) {
        allFTs.add(ft2);
      }else{
        collectFTs(ft2, allFTs);
      }
    }
  }

  /**
   * Build the map file.
   * @param     dir             The folder containing the help.
   * @param     encoding        The encoding used by the help files.
   * @param     ft              The tree structure of the help.
   * @param     overwrite       Specifies if the map file will be
   *                            overwritten if it exists.
   * @exception IOException     Thrown if something goes wrong.
   */
  static void makeMap(File dir, String encoding, FileTree ft,
                      boolean overwrite)
    throws IOException
  {
    File f = new File(dir, map);
    if (!overwrite && f.exists()) {
      return;
    }
    FileOutputStream fos = new FileOutputStream(f);
    BufferedOutputStream bos = new BufferedOutputStream(fos);
    PrintStream out = new PrintStream(bos);

    try {
      out.println("<?xml version='1.0' encoding='" + encoding + "' ?>");
      out.println("<!DOCTYPE map PUBLIC \"-//Sun Microsystems Inc.//DTD JavaHelp Map Version 1.0//EN\" \"http://java.sun.com/products/javahelp/map_1_0.dtd\">");
      out.println();
      out.println("<map version=\"1.0\">");
      outMap(out, ft);
      out.println("</map>");
    }finally{
      out.close();
    }
  }

  /**
   * Produce the mapID tags for the map file.
   * @param     out     The stream where the tags should be written.
   * @param     ft      The tree structure of the help.
   */
  private static void outMap(PrintStream out, FileTree ft)
  {
    ArrayList<FileTree> a = ft.contents;
    int n = a.size();
    for (int i=0; i<n; i++) {
      FileTree ft2 = a.get(i);
      if (ft2.type == FileTree.FILE) {
        out.println("  <mapID target=\"" + ft2.mapName + "\" url=\"" +
                    ft2.fileName + "\" />");
      }else{
        outMap(out, ft2);
      }
    }
  }

  /**
   * Build the search index.
   * @param     dir             The folder containing the help.
   * @param     overwrite       Specifies if the search index will be
   *                            overwritten if it exists.
   * @exception IOException     Thrown if something goes wrong.
   */
  static void makeJHIndex(File dir, boolean overwrite) throws IOException
  {
    File f = new File(dir, javaHelpSearch);
    if (!overwrite && f.exists()) {
      return;
    }
    String name = dir.getCanonicalPath();
    File cfg = File.createTempFile("jhindex", null);
    FileOutputStream fos = new FileOutputStream(cfg);
    BufferedOutputStream bos = new BufferedOutputStream(fos);
    PrintStream out = new PrintStream(bos);
    out.println("IndexRemove " + name + File.separatorChar);
    out.close();
    Locale l = Locale.getDefault();
/*
    // Invoke jhindexer's main method directly, rather than executing the
    // launch anywhere wrapper program. There is no reason to start a
    // new JVM, just to execute jhindexer.
    String[] cmd = new String[7];
    cmd[0] = "-c";
    cmd[1] = cfg.getCanonicalPath();
    cmd[2] = "-db";
    cmd[3] = name + File.separatorChar + javaHelpSearch;
    cmd[4] = "-locale";
    cmd[5] = l.getLanguage() + "_" + l.getCountry();
    cmd[6] = name;
    try {
      com.sun.java.help.search.Indexer.main(cmd);
    } catch (Throwable th) {
      //throw new IOException(th.getMessage());
      th.printStackTrace();
    }
*/
    // Invoking jhindexer's main method idrectly more than once fails with a
    // weird exception, so we have to do it the hard way. However, we don't
    // bother with executing the launch anywhere wrapper program; instead, we
    // invoke the jhindexer directly.
    //String[] cmd = new String[10];
    String[] cmd = new String[9];
    // Don't settle for any old java; use the one we are currently running.
    cmd[0] = System.getProperty("java.home") +
             File.separator + "bin" + File.separator + "java";
    // cmd[1] = "-classpath";
    // // Use the current class path, which should contain the JavaHelp classes.
    // cmd[2] = System.getProperty("java.class.path");
    cmd[1] = "com.sun.java.help.search.Indexer";
    cmd[2] = "-c";
    cmd[3] = cfg.getCanonicalPath();
    cmd[4] = "-db";
    cmd[5] = name + File.separatorChar + javaHelpSearch;
    cmd[6] = "-locale";
    cmd[7] = l.getLanguage() + "_" + l.getCountry();
    cmd[8] = name;
    //cmd[9] = "java";
    try {
      Process p = Runtime.getRuntime().exec(cmd);
      p.waitFor();
    } catch (Exception ioe) {
      ioe.printStackTrace();
    }

    cfg.delete();
  }

  /**
   * Build the order hints file.
   * @param     dir             The folder containing the help.
   * @param     ft              The tree structure of the help.
   * @param     overwrite       Specifies if the order hints file will be
   *                            overwritten if it exists.
   * @exception IOException     Thrown if something goes wrong.
   */
  static void makeOrderHints(File dir, FileTree ft, boolean overwrite)
    throws IOException
  {
    File f = new File(dir, orderHints);
    if (!overwrite && f.exists()) {
      return;
    }
    FileOutputStream fos = new FileOutputStream(f);
    BufferedOutputStream bos = new BufferedOutputStream(fos);
    ObjectOutputStream oos = new ObjectOutputStream(bos);
    try {
      OrderHint oh = new OrderHint(ft, 0);
      oh.writeExternal(oos);
    }finally{
      oos.close();
    }
  }

  /**
   * Read the order hints file.
   * @param     dir     The folder containing the help.
   * @return    The OrderHint structure contained in the order hints file.
   * @exception IOException     Thrown if something goes wrong.
   */
  static OrderHint readOrderHints(File dir)
    throws IOException, ClassNotFoundException
  {
    File f = new File(dir, orderHints);
    FileInputStream fis = new FileInputStream(f);
    BufferedInputStream bis = new BufferedInputStream(fis);
    ObjectInputStream ois = new ObjectInputStream(bis);
    OrderHint oh = null;
    try{
      oh = new OrderHint();
      oh.readExternal(ois);
    }finally{
      ois.close();
    }
    return oh;
  }

  /**
   * Deletes the JavaHelp files that were created during a previous run.
   * @param     dir             The folder containing the help.
   */
  static void deleteOldFiles(File dir)
  {
    new File(dir, helpSet).delete();
    new File(dir, map).delete();
    new File(dir, toc).delete();
    new File(dir, index).delete();
    new DirFile(dir, javaHelpSearch).delete();
    new File(dir, orderHints).delete();
  }

  /**
   * Sets the names of the JavaHelp files and folders that will be produced.
   * @param     cName   The name of the class for which the JavaHelp files
   * will be produced.
   */
  static void setNames(String cName)
  {
    cName = ClassNameString.getClassNameStringWOPackage(cName);
    String suffix = "_" + System.getProperty("user.language");
    helpSet = cName + "HelpSet" + suffix + ".hs";
    map = cName + "Map" + suffix + ".jhm";
    toc = cName + "TOC" + suffix + ".xml";
    index = cName + "Index" + suffix + ".xml";
    javaHelpSearch = cName + "JavaHelpSearch" + suffix;
    orderHints = cName + "OrderHints" + suffix;
  }

  /**
   * Checks if the files and folders that will be produced already exist in a
   * directory. The <code>setNames</code> method must have been invoked before
   * invoking this method.
   * @param     dir     The directory to check.
   * @return    True if any of the files exist, false otherwise.
   */
  static boolean filesExist(File dir)
  {
    return
      new File(dir, helpSet).exists() ||
      new File(dir, map).exists() ||
      new File(dir, toc).exists() ||
      new File(dir, index).exists() ||
      new File(dir, javaHelpSearch).exists() ||
      new File(dir, orderHints).exists();
  }

  /**
   * Checks if the files and folders that will be produced already exist in a
   * directory. The <code>setNames</code> method must have been invoked before
   * invoking this method.
   * @param     dir     The directory to check.
   * @return    True if all of the files exist, false otherwise.
   */
  static boolean allFilesExist(File dir)
  {
    return
      new File(dir, helpSet).exists() &&
      new File(dir, map).exists() &&
      new File(dir, toc).exists() &&
      new File(dir, index).exists() &&
      new File(dir, javaHelpSearch).exists() &&
      new File(dir, orderHints).exists();
  }

}
