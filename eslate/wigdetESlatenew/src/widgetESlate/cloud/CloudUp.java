/**
 * 
 */
package widgetESlate.cloud;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

/**
 * @author vpapakir
 *
 */
public class CloudUp {
	
	String server = "54.69.173.4";
	int port = 21;
	String user = "ec2ftp";
	String pass = "ec2P@ssw0rd";
	private SecureRandom random = new SecureRandom();

	public String nextSessionId() {
		return new BigInteger(130, random).toString(32);
	}
	
	public String uploadMWD(String mwdpath) {
		String MWDname = this.nextSessionId()+".mwd";
		
		FTPClient ftpClient = new FTPClient();
		try {

			ftpClient.connect(server, port);
			ftpClient.login(user, pass);
			ftpClient.enterLocalPassiveMode();

			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

			// APPROACH #1: uploads first file using an InputStream
			File firstLocalFile = new File(mwdpath);
			if(firstLocalFile.length() < 100) { /*less than 100 bytes, which means no file or currupted file*/
				return "NODATA";
			}

			String firstRemoteFile = MWDname;
			InputStream inputStream = new FileInputStream(firstLocalFile);

			System.out.println("Start uploading first file");
			boolean done = ftpClient.storeFile(firstRemoteFile, inputStream);
			inputStream.close();
			if (done) {
				System.out.println("The first file is uploaded successfully.");
			} else {
				System.out.println("The first file has not been uploaded.");
			}

			// APPROACH #2: uploads second file using an OutputStream
			/*File secondLocalFile = new File("C:/Users/vpapakir/Downloads/workspace_clean/jupdown/lib/jsch-0.1.51.jar");
			String secondRemoteFile = "jsch-0.1.51.jar";
			inputStream = new FileInputStream(secondLocalFile);

			System.out.println("Start uploading second file");
			OutputStream outputStream = ftpClient.storeFileStream(secondRemoteFile);
	        byte[] bytesIn = new byte[4096];
	        int read = 0;

	        while ((read = inputStream.read(bytesIn)) != -1) {
	        	outputStream.write(bytesIn, 0, read);
	        }
	        inputStream.close();
	        outputStream.close();

	        boolean completed = ftpClient.completePendingCommand();
			if (completed) {
				System.out.println("The second file is uploaded successfully.");
			} else {
				System.out.println("The second file has not been uploaded.");
			}*/

		} catch (Exception ex) {
			return "NODATA";
		} finally {
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		return MWDname;
	}
}
