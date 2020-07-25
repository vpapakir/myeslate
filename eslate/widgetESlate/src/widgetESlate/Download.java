/*
 * Created on 21 Ιουλ 2011
 *
 */
package widgetESlate;

import java.io.*;
import java.net.*;
import java.util.*;

// This class downloads a file from a URL.
public class Download extends Observable implements Runnable {
    
    // Max size of download buffer.
    private static final int MAX_BUFFER_SIZE = 4096;
    
    // These are the status names.
    public static final String STATUSES[] = {"Downloading",
    "Paused", "Complete", "Cancelled", "Error"};
    
    // These are the status codes.
    public static final int DOWNLOADING = 0;
    public static final int PAUSED = 1;
    public static final int COMPLETE = 2;
    public static final int CANCELLED = 3;
    public static final int ERROR = 4;
    
    private URL url; // download URL
    private String filenameToSave = null;
    private int size; // size of download in bytes
    private int downloaded; // number of bytes downloaded
    private int status; // current status of download
    public String fullpath;
    
    // Constructor for Download.
    public Download(URL url, String filenameToSave) {
        this.url = url;
        this.filenameToSave = filenameToSave;
        size = -1;
        downloaded = 0;
        status = DOWNLOADING;
        
//        // Begin the download.
//        download();
    }
    
    // Get this download's URL.
    public String getUrl() {
        return url.toString();
    }
    
    // Get this download's size.
    public int getSize() {
        return size;
    }
    
    // Get this download's progress.
    public double getProgress() {
        return ((double) downloaded / size) * 100;
    }
    
    // Get this download's status.
    public int getStatus() {
        return status;
    }
    
    // Pause this download.
    public void pause() {
        status = PAUSED;
        stateChanged();
    }
    
    // Resume this download.
    public void start() {
        status = DOWNLOADING;
        stateChanged();
        download();
    }
    
    // Resume this download.
    public void resume() {
        status = DOWNLOADING;
        stateChanged();
        download();
    }
    
    // Cancel this download.
    public void cancel() {
        status = CANCELLED;
        stateChanged();
    }
    
    // Mark this download as having an error.
    private void error() {
        status = ERROR;
        stateChanged();
    }
    
    // Start or resume downloading.
    private void download() {
        Thread thread = new Thread(this);
        thread.start();
    }
    
    // Get file name portion of URL.
    private String getFileName(URL url) {
        String fileName = url.getFile();
        return fileName.substring(fileName.lastIndexOf('/') + 1);
    }
    
    // Download file.
    public void run() {
        //RandomAccessFile file = null;
        InputStream stream = null;
        DataOutputStream dos = null;
        
        try {
            // Open connection to URL.
            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();
            
            // Specify what portion of file to download.
            connection.setRequestProperty("Range",
                    "bytes=" + downloaded + "-");
            
            // Connect to server.
            connection.connect();
            
            // Make sure response code is in the 200 range.
            if (connection.getResponseCode() / 100 != 2) {
                error();
            }
            
            // Check for valid content length.
            int contentLength = connection.getContentLength();
            if (contentLength < 1) {
                error();
            }
            
      /* Set the size for this download if it
         hasn't been already set. */
            if (size == -1) {
                size = contentLength;
                System.out.println("Size is :"+contentLength);
                stateChanged();
            }
            
            // Open file and seek to the end of it.
            System.out.println("URL to get :"+url);
            //file = new RandomAccessFile(getFileName(url), "rw");
            //file.seek(downloaded);
            String property = "java.io.tmpdir";
            String tempDir = System.getProperty(property);
            File f = new File(/*tempDir+File.separator+*/filenameToSave);
            this.fullpath = /*tempDir+File.separator+*/filenameToSave;
            System.out.println("tempDir :"+tempDir);
            if (f.exists()){
                System.out.println("exists!");
                System.out.println("Deleted :"+f.delete());
            }
            dos = new DataOutputStream(new FileOutputStream(f));
            byte[] buffer = new byte[MAX_BUFFER_SIZE];
            stream = connection.getInputStream();
            while (status == DOWNLOADING) {
                // Read from server into buffer.
                int read = stream.read(buffer);
                if (read ==-1)
                    break;
                
                // Write buffer to file.
                //file.write(buffer, 0, read);
                dos.write(buffer,0, read);
                downloaded += read;
                stateChanged();
//                System.out.println("downloading :"+getProgress());
//                System.out.println("downloaded :"+downloaded);
            }
            System.out.println("Doneee! :");
            
      /* Change status to complete if this point was
         reached because downloading has finished. */
            if (status == DOWNLOADING) {
                status = COMPLETE;
                System.out.println("Complete! :"+getProgress());
                // Close file.
                //if (file != null) {
                    try {
                        dos.flush();
                        dos.close();
                        //file.close();
                        System.out.println("FILE CLOSED!");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                //}
                
                // Close connection to server.
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (Exception e) {}
                }
                stateChanged();
            }
        } catch (Exception e) {
            error();
            e.printStackTrace();
        } finally {
            // Close file.
            //if (file != null) {
                try {
                    dos.flush();
                    dos.close();
                    //file.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            //}
            
            // Close connection to server.
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e) {}
            }
        }
    }
    
    // Notify observers that this download's status has changed.
    private void stateChanged() {
        setChanged();
        notifyObservers();
    }
}
