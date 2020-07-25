package gr.cti.eslate.server;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.*;
import java.util.StringTokenizer;
import java.util.Hashtable;
import java.util.Enumeration;

public class FileServer extends HttpServlet {
    private String publicFolderPath;

    public void init(ServletConfig c) throws ServletException {
        super.init(c);
        System.out.println("gr.cti.eslate.server.FileServer initialized");
        try {
            InitialContext iniCtx = new InitialContext();
            Context envCtx = (Context) iniCtx.lookup("java:comp/env");
            publicFolderPath = (String) envCtx.lookup("publicFolderPath");
//            publicFolderPath ="D:\\development\\projects\\E-Slate\\microworlds\\remote";
        } catch (NamingException e) {
//            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    public void doGet(HttpServletRequest	request,HttpServletResponse	response)
            throws ServletException, IOException {
System.out.println("doGet()");
        PrintWriter		out;
        String			title = "File Servlet Output";

        // set content type and other response header fields first
        response.setContentType("text/html");

        // then write the data of the response
        out = response.getWriter();

        out.println("<HTML><HEAD><TITLE>");
        out.println(title);
        out.println("</TITLE></HEAD><BODY>");
        out.println("<H1>" + title + "</H1>");
        out.println("<P>This is output from gr.cti.eslate.server.FileServer.");
//      out.println("<H2>homedir :"+homeDir+"</H2>");
        File test = new File(/*homeDir+System.getProperty("file.separator")+*/publicFolderPath);
        System.out.println("text.exists(): " + test.exists());
        out.println("<p> Exists " + test + "? " + test.exists()+"<p>");
        if (test.exists()) {
            out.print(/*homeDir+System.getProperty("file.separator")+*/publicFolderPath +": ");
            Hashtable contents=null;// = getDirContents(publicFolderPath,out);
//        System.out.println("contents: " + contents);
            if (contents==null) {
                out.println("Empty Dir</body></html>");
                return;
            }
            Enumeration en = contents.keys();
            while (en.hasMoreElements()) {
                String key = (String) en.nextElement();
                String type = "file";
                if (!((Boolean) contents.get(key)).booleanValue())
                    type = "directory";
                out.println("<p>" + type + ": " + key + "<p>");
            }
//      deleteFile("New Directory", "license.htm");
            out.println("===================================================");
        }
/*      rename("GISonWWW", "interactive.html", "interactive.htm");
      contents = getDirContents("GISonWWW");
      enum = contents.keys();
      while (enum.hasMoreElements()) {
          String key = (String) enum.nextElement();
          String type = "file";
          if (!((Boolean) contents.get(key)).booleanValue())
              type = "directory";
          out.println("<p>" + type + ": " + key + "<p>");
      }
*/
        out.println("</BODY></HTML>");
        out.close();
    }

    private String getAction(DataInputStream dis) throws IOException {
        int actionSize = dis.readInt();
        byte[] actionBytes = new byte[actionSize];
        dis.readFully(actionBytes);
        return new String(actionBytes);
    }

    synchronized protected void doPost(HttpServletRequest httpReq, HttpServletResponse httpRes)
            throws ServletException, IOException {
        ServletInputStream sis = httpReq.getInputStream();
        DataInputStream dis = new DataInputStream(new BufferedInputStream(sis, 1024));
        String action = getAction(dis);
        try{
            //Perform the action
            Object result = "Wrong query";
//System.out.println("action :"+action);
            char fileSeparator = action.charAt(action.indexOf('|')+1);
            action = action.replace(' ', '>');
            StringTokenizer strTokens = new StringTokenizer(action.replace(fileSeparator, ' '));
            action = "";
            while (strTokens.hasMoreTokens()) {
                action+=strTokens.nextToken();
                if (strTokens.hasMoreTokens())
                    action+=System.getProperty("file.separator");
            }
//out.write("queryStr after tokenizer:<"+queryStr+">\n");
            action = action.replace('>',' ');
//out.write("queryStr after replace:<"+queryStr+">\n");
////////
//out.write("queryStr :<"+queryStr+">\n");
            if (action.startsWith("dir")) {
                String directoryName = action.substring(action.indexOf('|')+1);
                if (directoryName != null && directoryName.trim().length() != 0)
                    result = getDirContents(directoryName.trim());
                else
                    result = getDirContents(publicFolderPath);
            }else if (action.startsWith("mkdir")) {
                String newDirectoryName = action.substring(action.indexOf('|')+2);
                if (newDirectoryName == null || newDirectoryName.trim().length() == 0)
                    result = Boolean.FALSE;
                else
                    result = new Boolean(createDir(publicFolderPath, newDirectoryName));
            }else if (action.startsWith("rn")) {
                String fileNames = action.substring(action.indexOf('|') + 2);
                String oldFileName = fileNames.substring(0, fileNames.indexOf('|'));
                String newFileName = fileNames.substring(fileNames.indexOf('|') + 1);

                if (fileNames == null || fileNames.trim().length() == 0)
                    result = Boolean.FALSE;
                else
                    result = new Boolean(renameFile(publicFolderPath, oldFileName,newFileName));
            }else if (action.startsWith("openFile")) {
                String pathToFile = action.substring(action.indexOf('|')+2);
//System.out.println("pathToFile after tokenizer :<"+pathToFile+">");
                if (pathToFile == null || pathToFile.trim().length() == 0)
                    result = null;
                else{
                    byte[] fileData = readFile(pathToFile);
                    ServletOutputStream sos = httpRes.getOutputStream ();
                    BufferedOutputStream bos = new BufferedOutputStream (sos);
                    DataOutputStream dos = new DataOutputStream(bos);
                    dos.writeInt(fileData.length);
                    dos.write(fileData, 0, fileData.length);
                    dos.flush();
                    httpRes.setStatus (HttpServletResponse.SC_OK);
//System.out.println("after writing the port");
                    dos.close();
                    return;
                }
            }else if (action.startsWith("saveFile")) {
                String pathToFile = action.substring(action.indexOf('|')+2);
                if (pathToFile == null || pathToFile.trim().length() == 0)
                    result = Boolean.FALSE;
                else{
                    int fileSize = dis.readInt();
                    byte[] fileData = new byte[fileSize];
                    dis.readFully(fileData);
                    saveFile(pathToFile, fileData);
                    result = Boolean.TRUE;
                }
            }

            // send response
            httpRes.setStatus (HttpServletResponse.SC_OK);
            ServletOutputStream sos = httpRes.getOutputStream ();
            BufferedOutputStream bos = new BufferedOutputStream (sos);
            ObjectOutputStream oos = new ObjectOutputStream (bos);
            oos.writeObject(result);
            oos.flush ();
        }catch (Exception exc) {
            System.out.println("Exception while reading: " + exc.getClass() + ", " + exc.getMessage());
            exc.printStackTrace();
        }
    }

    private synchronized Hashtable getDirContents(String dirName) {
        if (dirName == null)
            throw new NullPointerException("gr.cti.eslate.server.FileServer getDirContents(): Directory name should not be null");

        File directory;
        if (!dirName.equals(publicFolderPath) && dirName.trim().length() != 0)
            directory = new File(publicFolderPath + System.getProperty("file.separator") + dirName);
        else
            directory = new File(publicFolderPath);
        if (!directory.exists())
            return null;

        if (!directory.isDirectory())
            return null;
        String[] contents = directory.list();
        Hashtable files = new Hashtable();
        for (int i=0; i<contents.length; i++) {
            File file = new File(directory, contents[i]);
            if (file.isDirectory())
                files.put(contents[i], Boolean.FALSE); //FALSE for directories
            else
                files.put(contents[i], Boolean.TRUE);   //TRUE for files
        }
        return files;
    }

    private boolean createDir(String parentDirName, String dirName/*,FileWriter out*/) {
        File parentDirectory;
//out.write("mesa stin createDir\n");
//out.write("parentDirName :"+parentDirName);
        if (!parentDirName.equals(publicFolderPath))
            parentDirectory = new File(publicFolderPath +/* System.getProperty("file.separator")+*/parentDirName);
        else
            parentDirectory = new File(publicFolderPath);
//out.write("\nparentDirectory :"+parentDirectory);
        if (!parentDirectory.exists())
            return false;
        if (!parentDirectory.isDirectory())
            return false;
//out.write("\nPrin to new File\n");
        File newDirectory = new File(parentDirectory, dirName);
        boolean a=newDirectory.mkdir();
//out.write("mkdir returned :"+a+"\n");
        return a;
    }

    private boolean renameFile(String parentDirName, String oldFileName,String newFileName) {
        File parentDirectory;
        if (!parentDirName.equals(publicFolderPath))
            parentDirectory = new File(publicFolderPath +/* System.getProperty("file.separator")+*/parentDirName);
        else
            parentDirectory = new File(publicFolderPath);
        if (!parentDirectory.exists())
            return false;
        if (!parentDirectory.isDirectory())
            return false;
        File oldFile = new File(parentDirectory, oldFileName);
        if (!oldFile.exists())
            return false;
        File newFile = new File(parentDirectory, newFileName);
        if (newFile.exists())
            return false;
        return oldFile.renameTo(newFile);
    }

    private boolean deleteFile(String parentDirName, String fileName) {
        File parentDirectory;
        if (!parentDirName.equals(publicFolderPath))
            parentDirectory = new File(publicFolderPath + System.getProperty("file.separator") + parentDirName);
        else
            parentDirectory = new File(publicFolderPath);

        if (!parentDirectory.exists())
            return false;
        if (!parentDirectory.isDirectory())
            return false;

        File fileToDelete = new File(parentDirectory, fileName);
        return fileToDelete.delete();
    }

    private byte[] readFile(String fileName) {
        File file = new File(publicFolderPath + System.getProperty("file.separator") + fileName);
//        System.out.println("gr.cti.eslate.server.FileServer openFile() --> file: " + file + " (" + file.exists() + ", " + file.isFile() + ")");
        if (!file.exists() || !file.isFile())
            return null;
        try{
//            System.out.println("Array size: " + contents.length + ", file size: " + file.length());
            FileInputStream fis = new FileInputStream(file);
            byte[] contents = new byte[fis.available()];
            System.out.println("readFile() --> Number of bytes read: " + fis.read(contents));

            try{
                fis.close();
            }catch (IOException exc) {
                System.out.println("IOException while closing file: " + file);
            }
            return contents;
        }catch (FileNotFoundException exc) {
            System.out.println("File " + file + " not found");
        }catch (IOException exc) {
            System.out.println("I/O exception: " + exc.getClass() + ", " + exc.getMessage());
        }
        return null;
    }

    private Boolean saveFile(String fileName, byte[] contents) {
//        System.out.println("gr.cti.eslate.server.FileServer saveFile() --> fileName: " + fileName + ", contents size: " + contents.length);
        File file = new File(publicFolderPath + System.getProperty("file.separator") + fileName);

        if (contents == null)
            return Boolean.FALSE;

        BufferedOutputStream bos = null;
        DataOutputStream dos = null;
        try{
            bos = new BufferedOutputStream(new FileOutputStream(file));
            dos = new DataOutputStream(bos);
            for (int i=0; i<contents.length; i++)
                dos.write(contents, i, 1);
            dos.flush();
            dos.close();
            return Boolean.TRUE;
        }catch (IOException exc) {
            System.out.println("Exception while writing server file: " + exc.getClass() + ", " + exc.getMessage());
            return Boolean.FALSE;
        }
    }
}
