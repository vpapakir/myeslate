package widgetESlate;

import gr.cti.eslate.base.container.ESlateComposer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.solr.common.util.Base64;
import org.cbook.cbookif.AssessmentMode;
import org.cbook.cbookif.CBookContext;
import org.cbook.cbookif.CBookEvent;
import org.cbook.cbookif.CBookEventHandler;
import org.cbook.cbookif.CBookEventListener;
import org.cbook.cbookif.CBookWidgetInstanceIF;
import org.cbook.cbookif.Constants;
import org.cbook.cbookif.SuccessStatus;

class ESlateInstance extends JPanel implements CBookWidgetInstanceIF, ActionListener, CBookEventListener, Constants  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2675235738492539378L;
	public String microworldToLoadURLLaunch = "XX";
	public String microworldToLoadContentLaunch = "XX";
	public String microworldToLoadPathLaunch = "XX";
	public String microworldToLoadFileNameLaunch = "XX";
	public String microworldToLoadURLSuspend = "XX";
	public String microworldToLoadContentSuspend = "XX";
	public String microworldToLoadPathSuspend = "XX";
	public String microworldToLoadFileNameSuspend = "XX";
	private String correct;
	private Number maxScore = new Integer(10);
	private Boolean checked;
	private int score, failures;
	private String initial = "";
	private CBookContext context;
	
	private boolean DEBUG_MODE = true;
	private ESlateComposer instanceESC;
	
	private CBookEventHandler handler = new CBookEventHandler(this);
	private boolean showResult = true;
	private Object logging, logid;
	private boolean aftrek;
	private AssessmentMode mode;
	
	public int getScore() {
		if (DEBUG_MODE) {
			JOptionPane.showMessageDialog(new JFrame(),
					"ESlquestionateInstance --> getScore()"/* -> " + hasloaded*/);
		}
		
		return Math.max(0, score - failures);
	}

	public void setScore(int score) {
		if (DEBUG_MODE) {
			JOptionPane.showMessageDialog(new JFrame(),
					"ESlateInstance --> setScore() -> " + score);
		}
		this.score = score;
	}

	public JComponent asComponent() {
		return this;
	}

	ESlateInstance(CBookContext context, ESlateWidget sampleWidget) {
		super(new BorderLayout());
		
		if (DEBUG_MODE) {
			JOptionPane.showMessageDialog(new JFrame(),
					"ESlateInstance --> ESlateInstance()");
		}
		this.context = context;
		initialize();
		// stop();
	}

	private void initialize() {
		
		if (DEBUG_MODE) {
			JOptionPane.showMessageDialog(new JFrame(),
					"ESlateInstance --> initialize()");
		}
		
		setBackground(Color.white);
		correct = "42";
		this.instanceESC = new ESlateComposer();
		add(this.instanceESC,BorderLayout.CENTER);
		this.instanceESC.initialize();
		this.instanceESC.setMenuBarVisible(true);
		this.instanceESC.setContainerTitleEnabled(false);
		this.instanceESC.setControlBarsVisible(true);
		this.instanceESC.setControlBarTitleActive(false);
		this.instanceESC.setVisible(true);
	}


	public void setLaunchData(Map<String, ?> data, Map<String,Number> randomVars) {
		String questionStr = (String) data.get("question");
		initial     = (String) data.get("initial");
		correct     = (String) data.get("answer");
		/*questionStr = convert(questionStr, randomVars);
		initial = convert(initial, randomVars);
		correct = convert(correct, randomVars);*/
		
		microworldToLoadURLLaunch = (String) data.get("microworldurl");
		microworldToLoadPathLaunch = getTmpDir();
		microworldToLoadFileNameLaunch = (String) data.get("microworldfilename");
		microworldToLoadContentLaunch = (String) data.get("microworldcontent");
		
		Path p1 = Paths.get((microworldToLoadPathLaunch + microworldToLoadFileNameLaunch).replace("\\\\", "\\") );
		if( Files.isReadable(p1) ) {
			if (DEBUG_MODE) {
				JOptionPane.showMessageDialog(new JFrame(),
						"ESlateInstance --> setLaunchData(): Restoring from path");
			}
			String [] args2 = {(getTmpDir() + microworldToLoadFileNameLaunch).replace("\\\\", "\\")};
			this.instanceESC.closeMicroworld(false);
			try {
				microworldToLoadContentLaunch = "";
				byte[] fileContents = read(getTmpDir() + microworldToLoadFileNameLaunch);
				microworldToLoadContentLaunch = compressString(encodeMicroworld(fileContents));
		    } catch (Exception ex) {
				if(DEBUG_MODE) {
					JOptionPane.showMessageDialog(new JFrame(),"ESlateInstance --> setLaunchData() --> Error while synchronizing data " + ex.getMessage());
				}
			}
			this.instanceESC.initialize2();
			this.instanceESC.startESlate2(args2, true);				
			boolean hasloaded = this.instanceESC.loadLocalMicroworld((getTmpDir() + microworldToLoadFileNameLaunch).replace("\\\\", "\\"), false, true);
		} else {
			if(microworldToLoadContentSuspend.length() < 5) {
				if (DEBUG_MODE) {
					JOptionPane.showMessageDialog(new JFrame(),
							"ESlateInstance --> setLaunchData(): Loaded from launch data " + microworldToLoadPathLaunch + " " + microworldToLoadFileNameLaunch + " " + microworldToLoadContentLaunch.length() + " " + microworldToLoadContentSuspend.length());
				}
				try {
					BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream((microworldToLoadPathLaunch + microworldToLoadFileNameLaunch).replace("\\\\", "\\")));
					out.write((new String()).getBytes());
					String bytecontent = uncompressString(microworldToLoadContentLaunch);
					byte[] bb = decodeMicroworld(bytecontent);
					out.write(bb);
					out.flush();
					out.close();
					String [] args2 = {(getTmpDir() + microworldToLoadFileNameLaunch).replace("\\\\", "\\")};
					this.instanceESC.closeMicroworld(false);
					this.instanceESC.initialize2();
					this.instanceESC.startESlate2(args2, true);				
					boolean hasloaded = this.instanceESC.loadLocalMicroworld((getTmpDir() + microworldToLoadFileNameLaunch).replace("\\\\", "\\"), false, true);
				} catch (Exception ex1) {
					if (DEBUG_MODE) {
						JOptionPane.showMessageDialog(new JFrame(),
								"ESlateInstance --> setLaunchData() -> " + ex1.getMessage());
					}
				}
			} else {
				if (DEBUG_MODE) {
					JOptionPane.showMessageDialog(new JFrame(),
							"ESlateInstance --> setLaunchData(): Loaded from Suspend data " + microworldToLoadPathLaunch + " " + microworldToLoadFileNameLaunch + " " + microworldToLoadContentLaunch.length() + " " + microworldToLoadContentSuspend.length());
				}
				try {
					BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream((microworldToLoadPathLaunch + microworldToLoadFileNameLaunch).replace("\\\\", "\\")));
					out.write((new String()).getBytes());
					String bytecontent = uncompressString(microworldToLoadContentSuspend);
					byte[] bb = decodeMicroworld(bytecontent);
					out.write(bb);
					out.flush();
					out.close();
					String [] args2 = {(getTmpDir() + microworldToLoadFileNameLaunch).replace("\\\\", "\\")};
					this.instanceESC.closeMicroworld(false);
					this.instanceESC.initialize2();
					this.instanceESC.startESlate2(args2, true);				
					boolean hasloaded = this.instanceESC.loadLocalMicroworld((getTmpDir() + microworldToLoadFileNameLaunch).replace("\\\\", "\\"), false, true);
				} catch (Exception ex1) {
					if (DEBUG_MODE) {
						JOptionPane.showMessageDialog(new JFrame(),
								"ESlateInstance --> setLaunchData() -> " + ex1.getMessage());
					}
				}
			}
		}		
		maxScore    = (Number) data.get("maxScore");
		if(maxScore == null) 
			maxScore = new Integer(10);
	}

	private String convert(String str, Map<String, Number> randomVars) {
		StringBuffer sb = new StringBuffer();
		String[] x = str.split("#");
		boolean flip = false;
		for (int i = 0; i < x.length; i++) {
			if(flip) {
				Object object = randomVars.get(x[i]);
				if(object == null) object = "#" + x[i] + "#";
				sb.append(object);
			} else {
				sb.append(x[i]);
			}
			flip = !flip;
		}
		return sb.toString();
	}

	public void setState(Map<String,?> state) {
		microworldToLoadURLSuspend = (String) state.get("microworldurl");
		microworldToLoadPathSuspend = (String) state.get("microworldpath");
		microworldToLoadFileNameSuspend = (String) state.get("microworldfilename");
		microworldToLoadContentSuspend = (String) state.get("microworldcontent");
		
		if (DEBUG_MODE) {
			JOptionPane.showMessageDialog(new JFrame(),
					"ESlateInstance --> setState(): " + microworldToLoadPathSuspend + " " + microworldToLoadFileNameSuspend + " " + microworldToLoadContentSuspend.length() );
		}
		
		String [] args2 = {(getTmpDir() + microworldToLoadFileNameSuspend).replace("\\\\", "\\")};
		this.instanceESC.closeMicroworld(false);
		try {
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream((getTmpDir() + microworldToLoadFileNameSuspend).replace("\\\\", "\\")));
			out.write((new String()).getBytes());
			String bytecontent = uncompressString(microworldToLoadContentSuspend);
			byte[] bb = decodeMicroworld(bytecontent);
			out.write(bb);
			out.flush();
			out.close();
		} catch (Exception ex1) {
			if (DEBUG_MODE) {
				JOptionPane.showMessageDialog(new JFrame(),
						"DBG: Error while writing encoded data to file: " + ex1.getMessage());
			}
		}		
		this.instanceESC.initialize2();
		this.instanceESC.startESlate2(args2, true);				
		boolean hasloaded = this.instanceESC.loadLocalMicroworld((getTmpDir() + microworldToLoadFileNameSuspend).replace("\\\\", "\\"), false, true);
	}

	

	public Map<String,?> getState() {
		
		this.instanceESC.saveMicroworld(false);
		this.instanceESC.closeMicroworld(false);
		
		if(microworldToLoadURLSuspend.equals("XX") && microworldToLoadPathSuspend.equals("XX") && microworldToLoadFileNameSuspend.equals("XX")) {
			microworldToLoadPathSuspend = getTmpDir();
			microworldToLoadFileNameSuspend = microworldToLoadFileNameLaunch;
		} else {
			microworldToLoadPathLaunch = getTmpDir();
			microworldToLoadFileNameLaunch = microworldToLoadFileNameSuspend; 
		}
		
		try {
			//microworldToLoadContentSuspend = "";
			if(DEBUG_MODE) {
				JOptionPane.showMessageDialog(new JFrame(),"ESlateInstance --> getState() --> Synchronizing data with " + this.microworldToLoadPathSuspend + this.microworldToLoadFileNameSuspend);
			}
			byte[] fileContents = read(getTmpDir() + this.microworldToLoadFileNameSuspend);
			microworldToLoadContentSuspend = compressString(encodeMicroworld(fileContents));
	    } catch (Exception ex) {
			if(DEBUG_MODE) {
				JOptionPane.showMessageDialog(new JFrame(),"ESlateInstance --> getState() --> Error while synchronizing data " + ex.getMessage());
			}
		}
		
		if (DEBUG_MODE) {
			JOptionPane.showMessageDialog(new JFrame(),
					"ESlateInstance --> getState(): Writing " + microworldToLoadPathSuspend + " " + microworldToLoadFileNameSuspend + " " + microworldToLoadContentSuspend.length() );
		}
		
		Hashtable<String,Object> h = new Hashtable<String, Object>();

		h.put("microworldurl", microworldToLoadURLSuspend);
		h.put("microworldpath", microworldToLoadPathSuspend);
		h.put("microworldfilename", microworldToLoadFileNameSuspend);
		h.put("microworldcontent", microworldToLoadContentSuspend);
		
		/*try{
    		File file = new File(getTmpDir() + this.microworldToLoadFileNameSuspend);
    		if(file.delete()){
    			if (DEBUG_MODE) {
    				JOptionPane.showMessageDialog(new JFrame(),
    						"ESlateInstance --> getState(): Successful clean up " + microworldToLoadPathSuspend + " " + microworldToLoadFileNameSuspend + " " + microworldToLoadContentSuspend.length() );
    			}
    		}else{
    			if (DEBUG_MODE) {
    				JOptionPane.showMessageDialog(new JFrame(),
    						"ESlateInstance --> getState(): Failed clean up " + microworldToLoadPathSuspend + " " + microworldToLoadFileNameSuspend + " " + microworldToLoadContentSuspend.length() );
    			}
    		}
    	}catch(Exception e){
    		if (DEBUG_MODE) {
				JOptionPane.showMessageDialog(new JFrame(),
						"ESlateInstance --> getState(): Error while cleaning up " + e.getMessage() );
			}
    	}*/
		
		if(checked != null) 
			h.put("checked", checked);
		return h;
	}

	public void actionPerformed(ActionEvent _) {
		
		if (DEBUG_MODE) {
			JOptionPane.showMessageDialog(new JFrame(),
					"ESlateInstance --> actionPerformed()");
		}
		
		String input = "DUMMY";
		if( aftrek && ! checked.booleanValue() )
			failures += 1;
// Single command, single message
		handler.fire(USER_INPUT, input);

// Do logging via an event listener
		if(isLogging())
		{	
			HashMap<String,Object> map = new HashMap<String, Object>();
			map.put(USER_INPUT, input);
			map.put("success_status", getSuccessStatus());
			map.put("score", new Integer(getScore()));
			map.put(LOG_ID, logid);
			handler.fire(LOGGING, map);
		}
	}

// boilerplate	
	public void addCBookEventListener(CBookEventListener listener,
			String command) {
		if (DEBUG_MODE) {
			JOptionPane.showMessageDialog(new JFrame(),
					"ESlateInstance --> addCBookEventListener()");
		}
		handler.addCBookEventListener(listener, command);
	}

	public void removeCBookEventListener(CBookEventListener listener,
			String command) {
		if (DEBUG_MODE) {
			JOptionPane.showMessageDialog(new JFrame(),
					"ESlateInstance --> addCBookEventListener()");
		}
		handler.removeCBookEventListener(listener, command);
	}

	public void acceptCBookEvent(CBookEvent event) {
		
		if (DEBUG_MODE) {
			JOptionPane.showMessageDialog(new JFrame(),
					"ESlateInstance --> acceptCBookEvent()");
		}
		
		String command = event.getCommand();
		if(USER_INPUT.equals(command))
		{
		}
		if("check".equals(command))
		{
			// no parameters: kijkna()
			// met parameter "checked" Boolean zet nagekeken, save in state!
			Object checked = event.getParameter("checked");
			if(Boolean.TRUE.equals(checked))
				showResult = true;
			if(Boolean.FALSE.equals(checked))
				showResult = false;
			String input = "DUMMY";
			handler.fire(USER_INPUT, input);
			repaint();
		}
	}

	public SuccessStatus getSuccessStatus() {
		
		if (DEBUG_MODE) {
			JOptionPane.showMessageDialog(new JFrame(),
					"ESlateInstance --> getSuccessStatus()");
		}
		
		if(checked == null)
			return SuccessStatus.UNKNOWN;
		if(checked.booleanValue())
			return SuccessStatus.PASSED;
		return SuccessStatus.FAILED;
	}

	public void setAssessmentMode(AssessmentMode mode) {
		
		if (DEBUG_MODE) {
			JOptionPane.showMessageDialog(new JFrame(),
					"ESlateInstance --> setAssessmentMode()");
		}
		
		this.mode = mode;
		showResult = 
				mode == AssessmentMode.OEFENEN ||
				mode == AssessmentMode.OEFENEN_STRAFPUNTEN;
		aftrek = mode == AssessmentMode.OEFENEN_STRAFPUNTEN;
	}

	public void init() {
//		logging = context.getProperty(LOGGING);
//		logid = context.getProperty(LOG_ID);
//		if(isLogging())
//			System.err.println("log to " + logid);
//		Color foreground = (Color)context.getProperty(FOREGROUND);
//		setForeground(foreground);
//		Color background = (Color)context.getProperty(BACKGROUND);
//		setBackground(background);
//		Font font = (Font) context.getProperty(FONT);
//		setFont(font);
		
		if (DEBUG_MODE) {
			JOptionPane.showMessageDialog(new JFrame(),
					"ESlateInstance --> init()");
		}
		
		
		
	}

	private boolean isLogging() {
//		if (DEBUG_MODE) {
//			JOptionPane.showMessageDialog(new JFrame(),
//					"ESlateInstance --> isLogging()");
//		}
		return Boolean.TRUE.equals(logging);
	}

	public void start() {
		
		if (DEBUG_MODE) {
			JOptionPane.showMessageDialog(new JFrame(),
					"ESlateInstance --> start()");
		}
		
	}

	public void stop() {
		
		if (DEBUG_MODE) {
			JOptionPane.showMessageDialog(new JFrame(),
					"ESlateInstance --> stop()");
		}
		
		//this.instanceESC.saveMicroworld(false);
		/*if(!this.microworldToLoadPath.equals("XX")) {
			try {
				
				microworldToLoadContent = "";
				if(DEBUG_MODE) {
					JOptionPane.showMessageDialog(new JFrame(),"ESlateInstance --> stop() --> Synchronizing data with " + this.microworldToLoadPath + this.microworldToLoadFileName);
				}
				byte[] fileContents = read(this.microworldToLoadPath + this.microworldToLoadFileName);
				microworldToLoadContent = compressString(encodeMicroworld(fileContents));
				mwdToLoadContent.setText(microworldToLoadContent);
		    } catch (Exception ex) {
				if(DEBUG_MODE) {
					JOptionPane.showMessageDialog(new JFrame(),"ESlateInstance --> stop() --> Error while synchronizing data " + ex.getMessage());
				}
			}
		}
		check.setEnabled(false);
		answer.setEnabled(false);*/
	}

	public void destroy() {
		
		if (DEBUG_MODE) {
			JOptionPane.showMessageDialog(new JFrame(),
					"ESlateInstance --> destroy()");
		}
		
		//this.instanceESC.saveMicroworld(false);
//		if(!this.microworldToLoadPath.equals("XX")) {
//			try {
//				
//				microworldToLoadContent = "";
//				if(DEBUG_MODE) {
//					JOptionPane.showMessageDialog(new JFrame(),"ESlateInstance --> stop() --> Synchronizing data with " + this.microworldToLoadPath + this.microworldToLoadFileName);
//				}
//				byte[] fileContents = read(this.microworldToLoadPath + this.microworldToLoadFileName);
//				microworldToLoadContent = compressString(encodeMicroworld(fileContents));
//				mwdToLoadContent.setText(microworldToLoadContent);
//		    } catch (Exception ex) {
//				if(DEBUG_MODE) {
//					JOptionPane.showMessageDialog(new JFrame(),"ESlateInstance --> stop() --> Error while synchronizing data " + ex.getMessage());
//				}
//			}
//		}
		
	}

	public void reset() {
		setAssessmentMode(mode);
		setState(Collections.<String, String> emptyMap());
		failures = 0;
	}

	public CBookEventListener asEventListener() {
		
		if (DEBUG_MODE) {
			JOptionPane.showMessageDialog(new JFrame(),
					"ESlateInstance --> asEventListener()");
		}
		
		return this;
	}
	
	public String compressString(String srcTxt) throws IOException {
	    ByteArrayOutputStream rstBao = new ByteArrayOutputStream();
	    GZIPOutputStream zos = new GZIPOutputStream(rstBao);
	    zos.write(srcTxt.getBytes());
	    IOUtils.closeQuietly(zos);
	     
	    byte[] bytes = rstBao.toByteArray();
	    return Base64.byteArrayToBase64(bytes,0,bytes.length);
	}
	
	private static byte[] decodeMicroworld(String microworldDataString) {
		return org.apache.commons.codec.binary.Base64
				.decodeBase64(microworldDataString);
	}
	
	public String uncompressString(String zippedBase64Str) throws IOException {
		String result = null;
		byte[] bytes = Base64.base64ToByteArray(zippedBase64Str);
		GZIPInputStream zi = null;
		try {
			zi = new GZIPInputStream(new ByteArrayInputStream(bytes));
			result = IOUtils.toString(zi);
		} finally {
			IOUtils.closeQuietly(zi);
		}
		return result;
	}
	
	public static String encodeMicroworld(byte[] microworldByteArray) {
        return  org.apache.commons.codec.binary.Base64.encodeBase64URLSafeString(microworldByteArray);
    }
	
	private String getTmpDir() {
		return System.getProperty("java.io.tmpdir")
				+ System.getProperty("file.separator");
	}
	
	byte[] read(String aInputFileName){
	    File file = new File(aInputFileName);
	    byte[] result = new byte[(int)file.length()];
	    try {
	      InputStream input = null;
	      try {
	        int totalBytesRead = 0;
	        input = new BufferedInputStream(new FileInputStream(file));
	        while(totalBytesRead < result.length){
	          int bytesRemaining = result.length - totalBytesRead;
	          int bytesRead = input.read(result, totalBytesRead, bytesRemaining); 
	          if (bytesRead > 0){
	            totalBytesRead = totalBytesRead + bytesRead;
	          }
	        }
	      }
	      finally {
	        input.close();
	      }
	    }
	    catch (FileNotFoundException ex) {
	    }
	    catch (IOException ex) {
	    }
	    return result;
	  }

}
