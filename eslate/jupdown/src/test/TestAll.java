/**
 * 
 */
package test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

import cl.MwdTransporter;

/**
 * @author vpapakir
 *
 */
class TestAll {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MwdTransporter m1 = new MwdTransporter();
		InputStream str = m1.readKeyFile();
		System.out.println("END: " + str.toString());
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(str));
        StringBuilder out = new StringBuilder();
        String line = "";
        String output = "";
        try {
        while ((line = reader.readLine()) != null) {
            out.append(line);
            output = output + line;
        }
        //System.out.println(out.toString());   //Prints the string content read from input stream
        reader.close();
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
		
		String user = "ec2-user";
		String host = "54.69.173.4";
		JSch jsch=new JSch();
		try {
			jsch.addIdentity("./amazon.prv.ppk");
			Session session=jsch.getSession(user, host, 22);
			UserInfo ui=new MyUserInfo();
		    session.setUserInfo(ui);
		    session.connect();
		    Channel channel=session.openChannel("shell");
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static class MyUserInfo implements UserInfo, UIKeyboardInteractive{
		
		public String passphrase;

		@Override
		public String getPassphrase() {
			return passphrase;
		}

		@Override
		public String getPassword() {
			return null;
		}

		@Override
		public boolean promptPassphrase(String arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean promptPassword(String arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean promptYesNo(String arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void showMessage(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public String[] promptKeyboardInteractive(String arg0, String arg1,
				String arg2, String[] arg3, boolean[] arg4) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
}
