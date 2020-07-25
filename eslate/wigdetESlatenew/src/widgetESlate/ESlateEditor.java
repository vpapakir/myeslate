package widgetESlate;

import gr.cti.eslate.base.container.ESlateComposer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Hashtable;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.solr.common.util.Base64;
import org.cbook.cbookif.CBookContext;
import org.cbook.cbookif.CBookWidgetEditIF;
import org.cbook.cbookif.Constants;
import org.cbook.cbookif.rm.Resource;
import org.cbook.cbookif.rm.ResourceContainer;
import org.cbook.cbookif.rm.ResourceException;
import org.cbook.cbookif.rm.ResourceManager;

import widgetESlate.cloud.CloudUp;

class ESlateEditor extends JPanel implements CBookWidgetEditIF {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6883354605558419275L;
	JTextField question;
	JTextField initial;
	JTextField answer;
	JTextField maxScore;
	ResourceManager rm;
	Resource [] mwdz;
	private boolean CLOUD_MODE = true;
	private boolean DEBUG_MODE = false;
	private JScrollPane scrollPane;
	JPanel eslatePanel;
	ResourceContainer rc;
	public String microworldToLoadURL = "XX";
	public String microworldToLoadContent = "XX";
	public String microworldToLoadPath = "XX";
	public String microworldToLoadFileName = "XX";
	private ESlateComposer editorESC;
	
	public JComponent asComponent() {
		return this;
	}

	@Override
	public void setSize(Dimension d) {
		//Thread.currentThread().dumpStack();
		super.setSize(d);
	}

	@Override
	public void setSize(int width, int height) {
		//Thread.currentThread().dumpStack();
		super.setSize(width, height);
	}



	public Map<String, ?> getLaunchData() {
		Hashtable<String,Object> h = new Hashtable<String, Object>();
		h.put("maxScore", new Integer(maxScore.getText()));
		h.put("microworldurl", microworldToLoadURL);
		
		editorESC.saveMicroworld(false);
		
		if (CLOUD_MODE) {
			if (DEBUG_MODE) {
				JOptionPane.showMessageDialog(new JFrame(),
						"ESlateEditor --> getLaunchData() --> CLOUD MODE");
			}
			try {
				if (DEBUG_MODE) {
					JOptionPane.showMessageDialog(new JFrame(),
							"ESlateEditor --> getLaunchData() --> Reading contents from: " + microworldToLoadPath + microworldToLoadFileName);
				}
		        /*byte[] fileContents = read(microworldToLoadPath + microworldToLoadFileName);
		        microworldToLoadContent = compressString(encodeMicroworld(fileContents));*/
				CloudUp cup1 = new CloudUp();
				String res = cup1.uploadMWD(microworldToLoadPath + microworldToLoadFileName);
				if(!res.equals("NODATA")) {
					microworldToLoadContent = res; 
				}
			} catch (Exception ex) {
				if (DEBUG_MODE) {
					JOptionPane.showMessageDialog(new JFrame(),
							"ESlateEditor --> getLaunchData() --> Error while updating data");
				}
			}
			h.put("microworldcontent", microworldToLoadContent);
			h.put("microworldfilename", microworldToLoadFileName);
			h.put("microworldpath", microworldToLoadPath);
			
			editorESC.closeMicroworld(false);
		} else { /* non-cloud mode */
			try {
				if (DEBUG_MODE) {
					JOptionPane.showMessageDialog(new JFrame(),
							"ESlateEditor --> getLaunchData() --> Reading contents from: " + microworldToLoadPath + microworldToLoadFileName);
				}
		        byte[] fileContents = read(microworldToLoadPath + microworldToLoadFileName);
		        microworldToLoadContent = compressString(encodeMicroworld(fileContents));
			} catch (Exception ex) {
				if (DEBUG_MODE) {
					JOptionPane.showMessageDialog(new JFrame(),
							"ESlateEditor --> getLaunchData() --> Error while updating data");
				}
			}
			h.put("microworldcontent", microworldToLoadContent);
			h.put("microworldfilename", microworldToLoadFileName);
			h.put("microworldpath", microworldToLoadPath);
			
			editorESC.closeMicroworld(false);
		}
		
		return h;
	}
	
	public int getMaxScore() {
		if (DEBUG_MODE) {
			JOptionPane.showMessageDialog(new JFrame(),
					"ESlateEditor --> getMaxScore()");
		}
		return Integer.parseInt(maxScore.getText());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setLaunchData(Map<String, ?> data) {
		if (DEBUG_MODE) {
			JOptionPane.showMessageDialog(new JFrame(),
					"ESlateEditor --> setLaunchData()");
		}
		
		microworldToLoadURL = (String.valueOf(data.get("microworldurl")));
		microworldToLoadContent = (String.valueOf(data.get("microworldcontent")));
		microworldToLoadPath = (String.valueOf(data.get("microworldpath")));
		microworldToLoadFileName = (String.valueOf(data.get("microworldfilename")));
		
		if (DEBUG_MODE) {
			JOptionPane.showMessageDialog(new JFrame(),
					"DBG: " + microworldToLoadURL + " " + microworldToLoadPath + " " + microworldToLoadFileName );
		}
		
		// testing
		/*if(CLOUD_MODE) {
			try {
				if(microworldToLoadPath.equals("nodata") && microworldToLoadFileName.equals("nodata") ) {
					rc = rm.getInstanceContainer().createContainer("mwdcontainer");
				}
				Resource r = rm.getInstanceContainer().create(microworldToLoadFileName, new URL(microworldToLoadURL).openStream(), "application/e-slate");
				JOptionPane.showMessageDialog(new JFrame(),
						"DBG CLOUD 1: " + r.getURL().toString() );
				Resource[] rr = rm.getInstanceContainer().list();
				if(rr.length > 0) {
					String name = rr[0].getName();
					String ur = rr[0].getURL().toString();
					String mm = rr[0].getMimeType();
					JOptionPane.showMessageDialog(new JFrame(),
							"DBG CLOUD 2: " + name + " " + ur + " " + mm );
				}
			} catch (ResourceException e) {
				JOptionPane.showMessageDialog(new JFrame(),
						"DBG CLOUD ResourceException: ");
			} catch (MalformedURLException e) {
				JOptionPane.showMessageDialog(new JFrame(),
						"DBG CLOUD MalformedURLException: ");
			} catch (IOException e) {
				JOptionPane.showMessageDialog(new JFrame(),
						"DBG CLOUD IOException: ");
			}  catch (Exception e) {
				JOptionPane.showMessageDialog(new JFrame(),
						"DBG CLOUD General Exception: " + e.getMessage());
			}
		}*/
		
		if(CLOUD_MODE) {
			if(microworldToLoadPath.equals("nodata") && microworldToLoadFileName.equals("nodata") ) {
				AccessController.doPrivileged(new PrivilegedAction() {
					public Object run() {
						try {
							if (DEBUG_MODE) {
								JOptionPane.showMessageDialog(new JFrame(),
										"DBG: Downloading a fresh new microworld");
							}
							URL url = new URL(microworldToLoadURL);
							URLConnection conn = url.openConnection();
							conn.connect();
							microworldToLoadPath = getTmpDir();
							microworldToLoadFileName = System.currentTimeMillis()
									+ microworldToLoadURL.substring(
											microworldToLoadURL.lastIndexOf('.'),
											microworldToLoadURL.length());
							Download dl = new Download(url, microworldToLoadPath
									+ microworldToLoadFileName);
							dl.run();
							// microworldToLoadPath = dl.fullpath;
							try { /*
								 * Encode the downloaded microworld and keep it in a
								 * string variable for later storing it in state
								 */
								microworldToLoadContent = "";
								/*byte[] fileContents = read(microworldToLoadPath
										+ microworldToLoadFileName);
								microworldToLoadContent = compressString(encodeMicroworld(fileContents));*/
								CloudUp cup1 = new CloudUp();
								String res = cup1.uploadMWD( microworldToLoadPath + microworldToLoadFileName );
								if(!res.equals("NODATA")) {
									microworldToLoadContent = res; 
								}
							} catch (Exception ex) {
								microworldToLoadContent = "error";
								if (DEBUG_MODE) {
									JOptionPane.showMessageDialog(new JFrame(),
											ex.getMessage());
								}
							}
						} catch (ConnectException e) {
							if (DEBUG_MODE) {
								JOptionPane.showMessageDialog(new JFrame(),
										e.getMessage());
							}
						} catch (MalformedURLException e) {
							if (DEBUG_MODE) {
								JOptionPane.showMessageDialog(new JFrame(),
										e.getMessage());
							}
						} catch (FileNotFoundException e) {
							if (DEBUG_MODE) {
								JOptionPane.showMessageDialog(new JFrame(),
										e.getMessage());
							}
						} catch (IOException e) {
							if (DEBUG_MODE) {
								JOptionPane.showMessageDialog(new JFrame(),
										e.getMessage());
							}
						} catch (NullPointerException e) {
							if (DEBUG_MODE) {
								JOptionPane.showMessageDialog(new JFrame(),
										e.getMessage());
							}
						}
						return null;
					}
				});
			} else {
				if (DEBUG_MODE) {
					JOptionPane.showMessageDialog(new JFrame(),
							"DBG: Non-default launch data detected. Restoring...");
				}
				String[] args = { (getTmpDir() + microworldToLoadFileName)
						.replace("\\\\", "\\") };
				editorESC.initialize2();
				editorESC.startESlate2(args, true);
				boolean hasloaded = editorESC.loadLocalMicroworld(
						(getTmpDir() + microworldToLoadFileName).replace(
								"\\\\", "\\"), false, true);
				editorESC.setVisible(true);
			}
		} else { /* CLASSICAL MODE */
			if(microworldToLoadPath.equals("nodata") && microworldToLoadFileName.equals("nodata") ) {
				AccessController.doPrivileged(new PrivilegedAction() {
					public Object run() {
						try {
							if (DEBUG_MODE) {
								JOptionPane.showMessageDialog(new JFrame(),
										"DBG: Downloading a fresh new microworld");
							}
							URL url = new URL(microworldToLoadURL);
							URLConnection conn = url.openConnection();
							conn.connect();
							microworldToLoadPath = getTmpDir();
							microworldToLoadFileName = System.currentTimeMillis()
									+ microworldToLoadURL.substring(
											microworldToLoadURL.lastIndexOf('.'),
											microworldToLoadURL.length());
							Download dl = new Download(url, microworldToLoadPath
									+ microworldToLoadFileName);
							dl.run();
							// microworldToLoadPath = dl.fullpath;
							try { /*
								 * Encode the downloaded microworld and keep it in a
								 * string variable for later storing it in state
								 */
								microworldToLoadContent = "";
								byte[] fileContents = read(microworldToLoadPath
										+ microworldToLoadFileName);
								microworldToLoadContent = compressString(encodeMicroworld(fileContents));
							} catch (Exception ex) {
								microworldToLoadContent = "error";
								if (DEBUG_MODE) {
									JOptionPane.showMessageDialog(new JFrame(),
											ex.getMessage());
								}
							}
						} catch (ConnectException e) {
							if (DEBUG_MODE) {
								JOptionPane.showMessageDialog(new JFrame(),
										e.getMessage());
							}
						} catch (MalformedURLException e) {
							if (DEBUG_MODE) {
								JOptionPane.showMessageDialog(new JFrame(),
										e.getMessage());
							}
						} catch (FileNotFoundException e) {
							if (DEBUG_MODE) {
								JOptionPane.showMessageDialog(new JFrame(),
										e.getMessage());
							}
						} catch (IOException e) {
							if (DEBUG_MODE) {
								JOptionPane.showMessageDialog(new JFrame(),
										e.getMessage());
							}
						} catch (NullPointerException e) {
							if (DEBUG_MODE) {
								JOptionPane.showMessageDialog(new JFrame(),
										e.getMessage());
							}
						}
						return null;
					}
				});
			} else {
				if (DEBUG_MODE) {
					JOptionPane.showMessageDialog(new JFrame(),
							"DBG: Non-default launch data detected. Restoring...");
				}
				/*try {
					BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream((microworldToLoadPath + microworldToLoadFileName).replace("\\\\", "\\")));
					out.write((new String()).getBytes());
					String bytecontent = uncompressString(microworldToLoadContent);
					byte[] bb = decodeMicroworld(bytecontent);
					out.write(bb);
					out.flush();
					out.close();
				} catch (Exception ex1) {
					if (DEBUG_MODE) {
						JOptionPane.showMessageDialog(new JFrame(),
								"DBG: Error while writing encoded data to file: " + ex1.getMessage());
					}
				}*/
				String[] args = { (getTmpDir() + microworldToLoadFileName)
						.replace("\\\\", "\\") };
				editorESC.initialize2();
				editorESC.startESlate2(args, true);
				boolean hasloaded = editorESC.loadLocalMicroworld(
						(getTmpDir() + microworldToLoadFileName).replace(
								"\\\\", "\\"), false, true);
				editorESC.setVisible(true);
			}
		}
	}

	ESlateEditor(CBookContext context, ESlateWidget sampleWidget) {
		super();
		
		
		if (DEBUG_MODE) {
			JOptionPane.showMessageDialog(new JFrame(),
					"ESlateEditor --> ESlateEditor()");
		}
		eslatePanel = new JPanel();
		setBackground(Color.white);
		if (DEBUG_MODE) {
			JOptionPane.showMessageDialog(new JFrame(),
					"DBG: Creating composer instance...");
		}
		editorESC = new ESlateComposer();
		if (DEBUG_MODE) {
			JOptionPane.showMessageDialog(new JFrame(),
					"DBG: Initializing composer instance...");
		}
		editorESC.initialize();
		if (DEBUG_MODE) {
			JOptionPane.showMessageDialog(new JFrame(),
					"DBG: Setting composer instance...");
		}
		editorESC.setMenuBarVisible(true);
		editorESC.setContainerTitleEnabled(false);
		editorESC.setControlBarsVisible(true);
		editorESC.setControlBarTitleActive(false);
		if (DEBUG_MODE) {
			JOptionPane.showMessageDialog(new JFrame(),
					"DBG: Initializing2 composer instance...");
		}
		editorESC.initialize2();
		if (DEBUG_MODE) {
			JOptionPane.showMessageDialog(new JFrame(),
					"DBG: Starting composer instance...");
		}
		if (DEBUG_MODE) {
			JOptionPane.showMessageDialog(new JFrame(),
					"DBG: Creating composer instance containers...");
		}
		eslatePanel.setLayout(new BorderLayout());
		scrollPane = new JScrollPane(editorESC);
		eslatePanel.add(scrollPane, BorderLayout.CENTER);
		scrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.setLayout(new BorderLayout());
		/*jb.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if (DEBUG_MODE) {
					JOptionPane.showMessageDialog(new JFrame(),
							"DBG: Pressed...");
				}
				add(eslatePanel, BorderLayout.CENTER);
				revalidate();
			}
		});
		add(jb, BorderLayout.NORTH);*/
		question = new JTextField("question"); add(question); question.setVisible(false);
		initial = new JTextField("initial");   add(initial); initial.setVisible(false);
		answer  = new JTextField("answer");    add(answer); answer.setVisible(false);
		maxScore = new JTextField("10");	   add(maxScore); maxScore.setVisible(false);
		setPreferredSize(new Dimension(500,500));
		
		/*rm = (ResourceManager) context.getProperty(Constants.RESOURCE_MANAGER);
		if(rm != null) {
			try {
				mwdz = rm.getWidgetContainer().openContainer(rm.IMAGES).list();
				JOptionPane.showMessageDialog(new JFrame(),
						"ESlateEditor --> Number of resources: " + mwdz.length);
			} catch (ResourceException e) {
				JOptionPane.showMessageDialog(new JFrame(),
						"ESlateEditor --> Error while counting resources: " + e.getMessage());
			} catch (NullPointerException e) {
				JOptionPane.showMessageDialog(new JFrame(),
						"ESlateEditor --> Error while counting resources: " + e.getMessage());
			}
			
			
		}*/
		
	}

	
	public String[] getAcceptedCmds() {
		if (DEBUG_MODE) {
			JOptionPane.showMessageDialog(new JFrame(),
					"ESlateEditor --> getAcceptedCmds()");
		}
		return new String[] { ESlateInstance.USER_INPUT, "check" };
	}

	public String[] getSendCmds() {
		if (DEBUG_MODE) {
			JOptionPane.showMessageDialog(new JFrame(),
					"ESlateEditor --> getSendCmds()");
		}
		return new String[] { ESlateInstance.USER_INPUT, ESlateInstance.LOGGING };
	}

	public void start() {
		String[] args = { (getTmpDir() + microworldToLoadFileName)
				.replace("\\\\", "\\") };
		editorESC.startESlate2(args, true);
		boolean hasloaded = editorESC.loadLocalMicroworld(
				(getTmpDir() + microworldToLoadFileName).replace(
						"\\\\", "\\"), false, true);
		if (DEBUG_MODE) {
			JOptionPane.showMessageDialog(new JFrame(),
					"ESlateEditor --> start() " + hasloaded + ":" + microworldToLoadPath + " " + microworldToLoadFileName + " " + microworldToLoadContent.length());
		}
		add(eslatePanel, BorderLayout.CENTER);
		revalidate();
	}

	public void stop() {
		if (DEBUG_MODE) {
			JOptionPane.showMessageDialog(new JFrame(),
					"ESlateEditor --> stop()");
		}
	}
	
	public void destroy() {
		if (DEBUG_MODE) {
			JOptionPane.showMessageDialog(new JFrame(),
					"ESlateEditor --> stop()");
		}		
	}

	private Dimension instanceSize = new Dimension(300,300);
	
	public void setInstanceWidth(int width) {
		instanceSize.width = width;
	}

	public void setInstanceHeight(int height) {
		instanceSize.height = height;
	}

	public Dimension getInstanceSize() {
		return instanceSize;
	}
	
	private String getTmpDir() {
		return System.getProperty("java.io.tmpdir")
				+ System.getProperty("file.separator");
	}
	
	byte[] read(String aInputFileName) {
		File file = new File(aInputFileName);
		byte[] result = new byte[(int) file.length()];
		try {
			InputStream input = null;
			try {
				int totalBytesRead = 0;
				input = new BufferedInputStream(new FileInputStream(file));
				while (totalBytesRead < result.length) {
					int bytesRemaining = result.length - totalBytesRead;
					int bytesRead = input.read(result, totalBytesRead,
							bytesRemaining);
					if (bytesRead > 0) {
						totalBytesRead = totalBytesRead + bytesRead;
					}
				}
			} finally {
				input.close();
			}
		} catch (FileNotFoundException ex) {
		} catch (IOException ex) {
		}
		return result;
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
	
	public String compressString(String srcTxt) throws IOException {
		ByteArrayOutputStream rstBao = new ByteArrayOutputStream();
		GZIPOutputStream zos = new GZIPOutputStream(rstBao);
		zos.write(srcTxt.getBytes());
		IOUtils.closeQuietly(zos);

		byte[] bytes = rstBao.toByteArray();
		return Base64.byteArrayToBase64(bytes, 0, bytes.length);
	}

	public static String encodeMicroworld(byte[] microworldByteArray) {
		return org.apache.commons.codec.binary.Base64
				.encodeBase64URLSafeString(microworldByteArray);
	}

}
