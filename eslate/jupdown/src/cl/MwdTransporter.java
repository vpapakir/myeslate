package cl;

import java.io.InputStream;

public class MwdTransporter {
	public void Download() {
		
	}
	
	public void Upload() {
		
	}
	
	public InputStream readKeyFile() {
		InputStream stream = null;
		
		try {
			stream = MwdTransporter.class.getResourceAsStream("/cl/amazon.prv.ppk");
			//stream = MwdTransporter.class.getClassLoader().getResourceAsStream("/cl/amazon.prv.ppk");
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
        
        return stream;
	}
}
