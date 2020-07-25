package gr.cti.eslate.mediaPlayer;

import gr.cti.eslate.base.ConnectionEvent;
import gr.cti.eslate.base.ConnectionListener;
import gr.cti.eslate.base.ESlate;
import gr.cti.eslate.base.ESlateHandle;
import gr.cti.eslate.base.ESlateInfo;
import gr.cti.eslate.base.ESlatePart;
import gr.cti.eslate.base.InvalidPlugParametersException;
import gr.cti.eslate.base.Plug;
import gr.cti.eslate.base.PlugExistsException;
import gr.cti.eslate.base.RenamingForbiddenException;
import gr.cti.eslate.base.SharedObjectPlug;
import gr.cti.eslate.base.SingleInputPlug;
import gr.cti.eslate.base.sharedObject.SharedObjectEvent;
import gr.cti.eslate.base.sharedObject.SharedObjectListener;
import gr.cti.eslate.sharedObject.StringSO;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.NoTopOneLineBevelBorder;
import gr.cti.eslate.utils.StorageStructure;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Externalizable;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import wmp.mediaplayer.impl.IMediaPlayerImpl;

import com.jniwrapper.Int32;
import com.jniwrapper.win32.automation.OleMessageLoop;

/**
 * Media player which reproduces video, audio, image & flash files.
 * 
 * @author augril
 */
public class MediaPlayer extends JPanel implements IPlayer, Externalizable,
		ESlatePart {
	/** Used for serialization. */
	private static final long serialVersionUID = 1L;
	/** Used for externalization. */
	private static final int STORAGE_VERSION = 1;
	/** E-Slate component version. */
	private final static String VERSION = "1.0.0";
	/** Media file extensions */
	private static final String[] VIDEO_FILES_EXTENSIONS = new String[] {
			"mpeg", "mpg", "avi", "wmv", "mov" };
	private static final String[] AUDIO_FILES_EXTENSIONS = new String[] {
			"mp3", "wav", "au" };
	private static final String[] IMAGE_FILES_EXTENSIONS = new String[] {
			"bmp", "jpeg", "jpg", "gif", "png", "jp2" };
	private static final String[] FLASH_FILES_EXTENSIONS = new String[] { "swf" };
	/** Menu bar with the file open menu. */
	private JMenuBar menuBar;
	/** File open menu item. */
	private JMenuItem menuItemOpen;
	/** Menu item from replay. */
	private JCheckBoxMenuItem checkBoxMenuItemReplay;
	/** Close menu item. */
	private JMenuItem menuItemClose;
	/** Active media player. */
	private IPlayer activePlayer;
	/** Flash player. */
	private FlashPlayer flashPlayer;
	/** Windows media player. */
	private WindowsMediaPlayer windowsMediaPlayer;
	/** Quick time player. */
	private QuickTimePlayer quickTimePlayer;
	/** E-Slate handle. */
	private ESlateHandle handle = null;
	/** File to open. */
	private File file;
	/** Media player listeners. */
	private ArrayList<MediaPlayerListener> mediaPlayerListeners;
	/** Plugs of media player. */
    private SingleInputPlug plugFile;
    /** Current file chooser directory. */
    private File currentDirectory;
    /** Current file chooser extensions. */
    private String[] currentExtensions;
    

	/**
	 * Construct a new media player.
	 */
	public MediaPlayer() {
		super(new BorderLayout());

		initializeCommon();
		getESlateHandle();
	}

	/**
	 * ESR2 contructor.
	 * 
	 * @param oi
	 *            The input stream from which the component is constructed.
	 * @throws Exception
	 *             If something goes wrong during deserialization.
	 */
	public MediaPlayer(ObjectInput oi) throws Exception {
		this();
		initialize(oi);
	}

	/**
	 * Common initialization.
	 */
	private void initializeCommon() {
		initialize();
	}

	/**
	 * Initialize from saved condition.
	 * 
	 * @param oi
	 *            Saved condition.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void initialize(ObjectInput oi) throws IOException,
			ClassNotFoundException {
		StorageStructure fieldMap = (StorageStructure) oi.readObject();

		file = (File) fieldMap.get("File");
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				open(file);
				MediaPlayer.this.validate();
			}
		});
	}

	/**
	 * Initialize media player.
	 */
	private void initialize() {
		initializeComponents();
		layoutComponents();
		attachListeners();
	}

	/**
	 * Initialize the components.
	 */
	private void initializeComponents() {
		setBorder(new NoTopOneLineBevelBorder(NoTopOneLineBevelBorder.RAISED));

		mediaPlayerListeners = new ArrayList<MediaPlayerListener>();

		flashPlayer = new FlashPlayer();
		windowsMediaPlayer = new WindowsMediaPlayer(this);
		try {
			quickTimePlayer = new QuickTimePlayer();
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}

		// Create the menu bar with its menus
		// Prevent hiding of popup menu under OleContainer component
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		menuBar = new JMenuBar();
		JMenu menu = new JMenu(BundleMessages.getMessage("file"));
		menuItemOpen = new JMenuItem(BundleMessages.getMessage("open"));
		menu.add(menuItemOpen);
		checkBoxMenuItemReplay = new JCheckBoxMenuItem(BundleMessages.getMessage("replay"));
		menu.add(checkBoxMenuItemReplay);
		menu.addSeparator();
		menuItemClose = new JMenuItem(BundleMessages.getMessage("close"));
		menu.add(menuItemClose);
		menuBar.add(menu);
	}

	/**
	 * Layout the components.
	 */
	private void layoutComponents() {
		setPreferredSize(new Dimension(640, 480));

		add(menuBar, BorderLayout.NORTH);
	}

	/**
	 * Attach various listeners.
	 */
	private void attachListeners() {
		menuItemOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setMultiSelectionEnabled(false);
				chooser.setAcceptAllFileFilterUsed(false);
				MediaPlayerFileFilter videoFileFilter = new MediaPlayerFileFilter(
						VIDEO_FILES_EXTENSIONS, BundleMessages
								.getMessage("Video files")
								+ " (*.mpeg, *.mpg, *.avi, *.wmv, *.mov)");
				MediaPlayerFileFilter audioFileFilter = new MediaPlayerFileFilter(
						AUDIO_FILES_EXTENSIONS, BundleMessages
								.getMessage("Audio files")
								+ " (*.mp3, *.wav, *.au)");
				MediaPlayerFileFilter imageFileFilter = new MediaPlayerFileFilter(
								IMAGE_FILES_EXTENSIONS,
								BundleMessages.getMessage("Image files")
										+ " (*.bmp, *.jpeg, *.jpg, *.gif, *.png, *.jp2)");
				MediaPlayerFileFilter flashFileFilter = new MediaPlayerFileFilter(
						FLASH_FILES_EXTENSIONS, BundleMessages
								.getMessage("Flash files")
								+ " (*.swf)");
				chooser.addChoosableFileFilter(videoFileFilter);
				chooser.addChoosableFileFilter(audioFileFilter);
				chooser.addChoosableFileFilter(imageFileFilter);
				chooser.addChoosableFileFilter(flashFileFilter);
				if (AUDIO_FILES_EXTENSIONS.equals(currentExtensions))
					chooser.setFileFilter(audioFileFilter);
				else if (IMAGE_FILES_EXTENSIONS.equals(currentExtensions))
					chooser.setFileFilter(imageFileFilter);
				else if (FLASH_FILES_EXTENSIONS.equals(currentExtensions))
					chooser.setFileFilter(flashFileFilter);
				else
					chooser.setFileFilter(videoFileFilter);
				chooser.setCurrentDirectory(currentDirectory);

				if (chooser.showOpenDialog(MediaPlayer.this) == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					if (file != null) {
						currentDirectory = chooser.getCurrentDirectory();
						open(file);
					}
				}
			}
		});

		checkBoxMenuItemReplay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				IMediaPlayerImpl mediaPlayerImpl = windowsMediaPlayer.getMediaPlayer();
				if (mediaPlayerImpl == null)
					return;
				mediaPlayerImpl.setPlayCount(new Int32(checkBoxMenuItemReplay.isSelected()?0:1)); // 0 == play continuously
			}
		});
		
		menuItemClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
	}

	/**
	 * Parse filename and open the file according to its extension.
	 * 
	 * @param file
	 */
	public void open(File file) {
		if (!file.exists())
			return;
		this.file = file;

		String name = file.getName();
		int pos = name.lastIndexOf(".");
		String extension = pos != -1 ? name.substring(pos + 1) : "";

		for (int i = 0; i < VIDEO_FILES_EXTENSIONS.length; i++) {
			String fileExtension = VIDEO_FILES_EXTENSIONS[i];
			if (fileExtension.equalsIgnoreCase(extension)) {
				currentExtensions = VIDEO_FILES_EXTENSIONS;
				if (extension.equalsIgnoreCase("mov"))
					openQuickTime(file);
				else
					openMediaPlayer(file);
				break;
			}
		}
		for (int i = 0; i < AUDIO_FILES_EXTENSIONS.length; i++) {
			String fileExtension = AUDIO_FILES_EXTENSIONS[i];
			if (fileExtension.equalsIgnoreCase(extension)) {
				currentExtensions = AUDIO_FILES_EXTENSIONS;
				openMediaPlayer(file);
				break;
			}
		}
		for (int i = 0; i < IMAGE_FILES_EXTENSIONS.length; i++) {
			String fileExtension = IMAGE_FILES_EXTENSIONS[i];
			if (fileExtension.equalsIgnoreCase(extension)) {
				currentExtensions = IMAGE_FILES_EXTENSIONS;
				openQuickTime(file);
				break;
			}
		}
		for (int i = 0; i < FLASH_FILES_EXTENSIONS.length; i++) {
			String fileExtension = FLASH_FILES_EXTENSIONS[i];
			if (fileExtension.equalsIgnoreCase(extension)) {
				currentExtensions = FLASH_FILES_EXTENSIONS;
				openFlash(file);
				break;
			}
		}
	}

	/**
	 * Initialize the Flash player and open the swf file.
	 * 
	 * @param file
	 *            The file to open.
	 */
	private void openFlash(final File file) {
		checkBoxMenuItemReplay.setSelected(false);
		checkBoxMenuItemReplay.setEnabled(false);
		close();
		if (!(activePlayer instanceof FlashPlayer)) {
			if (activePlayer != null) {
				((Component) activePlayer).setVisible(false);
				// remove((Component) activePlayer);
			}
			activePlayer = flashPlayer;
			// if (((Component) activePlayer).getParent() == null) {
			add((Component) activePlayer, BorderLayout.CENTER);
			revalidate();
			// }
		}
		((Component) activePlayer).setVisible(true);
		// MediaPlayer.this.revalidate();
		OleMessageLoop.invokeLater(new Runnable() {
			public void run() {
				activePlayer.open(file);
			}
		});
	}

	/**
	 * Initialize Media player and open the video/audio file.
	 * 
	 * @param file
	 *            The file to open.
	 */
	private void openMediaPlayer(final File file) {
		checkBoxMenuItemReplay.setEnabled(true);
		close();
		if (!(activePlayer instanceof WindowsMediaPlayer)) {
			if (activePlayer != null) {
				((Component) activePlayer).setVisible(false);
				// remove((Component) activePlayer);
			}
			activePlayer = windowsMediaPlayer;
			// if (((Component) activePlayer).getParent() == null) {
			add((Component) activePlayer, BorderLayout.CENTER);
			revalidate();
			// }
		}
		((Component) activePlayer).setVisible(true);
		// MediaPlayer.this.revalidate();
		OleMessageLoop.invokeLater(new Runnable() {
			public void run() {
				activePlayer.open(file);
			}
		});
	}

	/**
	 * Initialize QuickTime player and open the mov or image file.
	 * 
	 * @param file
	 *            The file to open.
	 */
	private void openQuickTime(File file) {
		checkBoxMenuItemReplay.setSelected(false);
		checkBoxMenuItemReplay.setEnabled(false);
		close();
		if (!(activePlayer instanceof QuickTimePlayer)) {
			if (activePlayer != null) {
				((Component) activePlayer).setVisible(false);
				// remove((Component) activePlayer);
			}
			activePlayer = quickTimePlayer;
			// if (((Component) activePlayer).getParent() == null) {
			add((Component) activePlayer, BorderLayout.CENTER);
			revalidate();
			// }
		}
		((Component) activePlayer).setVisible(true);
		// MediaPlayer.this.revalidate();
		activePlayer.open(file);
	}

	/**
	 * Exit media player.
	 */
	public void exit() {
		if (activePlayer != null)
			activePlayer.exit();
	}

	/**
	 * Start media player.
	 */
	public void start() {
		if (activePlayer != null) {
			activePlayer.start();
			firePlayerStarted();
		}
	}

	/**
	 * Pause media player.
	 */
	public void pause() {
		if (activePlayer != null) {
			activePlayer.pause();
			firePlayerPaused();
		}
	}

	/**
	 * Stop media player.
	 */
	public void stop() {
		if (activePlayer != null) {
			activePlayer.stop();
			firePlayerStopped();
		}
	}

	/**
	 * Close media file.
	 */
	public void close() {
		if (activePlayer != null) {
			activePlayer.close();
			((Component) activePlayer).setVisible(false);
		}
	}

	/**
	 * Returns the component's E-Slate handle.
	 * 
	 * @return The requested handle. If the component's constructor has not been
	 *         called, this method returns null.
	 */
	public ESlateHandle getESlateHandle() {
		if (handle == null)
			initESlate();
		return handle;
	}

	/**
	 * Initializes the E-Slate functionality of the component.
	 */
	private void initESlate() {
		handle = ESlate.registerPart(this);
		handle.setInfo(getInfo());
		try {
			handle.setUniqueComponentName(BundleMessages.getMessage("name"));
		} catch (RenamingForbiddenException e) {
			e.printStackTrace();
		}
		
//      add(handle.getMenuPanel(), BorderLayout.NORTH);
		
		try {
			// Plug for files
			SharedObjectListener expressionListener = new SharedObjectListener() {
				public void handleSharedObjectEvent(SharedObjectEvent e) {
					String path = ((StringSO) (e.getSharedObject())).getString();
					open(new File(path));
				}
			};
			plugFile = new SingleInputPlug(handle, BundleMessages.getResourceBundle(),
					"filePlug", new Color(139, 117, 0),
					StringSO.class, expressionListener);
			plugFile.setNameLocaleIndependent("expressionPlug");
			handle.addPlug(plugFile);
			plugFile.addConnectionListener(new ConnectionListener() {
				public void handleConnectionEvent(ConnectionEvent e) {
					if (e.getType() == Plug.INPUT_CONNECTION) {
						StringSO sharedObject = (StringSO) ((SharedObjectPlug) e.getPlug()).getSharedObject();
						String path = sharedObject.getString();
						open(new File(path));
					}
				}
			});
		} catch (InvalidPlugParametersException e) {
			e.printStackTrace();
		} catch (PlugExistsException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns Copyright information.
	 * 
	 * @return The Copyright information.
	 */
	private ESlateInfo getInfo() {
		String[] info = { BundleMessages.getMessage("credits1"),
				BundleMessages.getMessage("credits2"),
				BundleMessages.getMessage("credits3") };
		return new ESlateInfo(BundleMessages.getMessage("componentName") + ", "
				+ BundleMessages.getMessage("version") + " " + VERSION, info);
	}

	/**
	 * Get the played file.
	 * 
	 * @return The played file.
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Set the played file.
	 * 
	 * @param file
	 *            File to play.
	 */
	public void setFile(File file) {
		open(file);
	}

	/**
	 * Add a listener for media player events.
	 * 
	 * @param listener
	 *            The listener to add.
	 */
	public void addMediaPlayerListener(MediaPlayerListener listener) {
		synchronized (mediaPlayerListeners) {
			if (!mediaPlayerListeners.contains(listener)) {
				mediaPlayerListeners.add(listener);
			}
		}
	}

	/**
	 * Remove a listener for media player events.
	 * 
	 * @param listener
	 *            The listener to remove.
	 */
	public void removeMediaPlayerListener(MediaPlayerListener listener) {
		synchronized (mediaPlayerListeners) {
			int ind = mediaPlayerListeners.indexOf(listener);
			if (ind >= 0) {
				mediaPlayerListeners.remove(ind);
			}
		}
	}

	/**
	 * Fires all listeners registered for media player start play events.
	 */
	void firePlayerStarted() {
		ArrayList listeners;
		synchronized (mediaPlayerListeners) {
			listeners = (ArrayList) (mediaPlayerListeners.clone());
		}
		int size = listeners.size();
		for (int i = 0; i < size; i++) {
			MediaPlayerListener l = (MediaPlayerListener) (listeners.get(i));
			MediaPlayerEvent e = new MediaPlayerEvent(this);
			l.playerStarted(e);
		}
	}

	/**
	 * Fires all listeners registered for media player pause play events.
	 */
	void firePlayerPaused() {
		ArrayList listeners;
		synchronized (mediaPlayerListeners) {
			listeners = (ArrayList) (mediaPlayerListeners.clone());
		}
		int size = listeners.size();
		for (int i = 0; i < size; i++) {
			MediaPlayerListener l = (MediaPlayerListener) (listeners.get(i));
			MediaPlayerEvent e = new MediaPlayerEvent(this);
			l.playerPaused(e);
		}
	}

	/**
	 * Fires all listeners registered for media player start play events.
	 */
	void firePlayerStopped() {
		ArrayList listeners;
		synchronized (mediaPlayerListeners) {
			listeners = (ArrayList) (mediaPlayerListeners.clone());
		}
		int size = listeners.size();
		for (int i = 0; i < size; i++) {
			MediaPlayerListener l = (MediaPlayerListener) (listeners.get(i));
			MediaPlayerEvent e = new MediaPlayerEvent(this);
			l.playerStopped(e);
		}
	}

	/**
	 * Fires all listeners registered for media player finish play events.
	 */
	void firePlayerFinished() {
		ArrayList listeners;
		synchronized (mediaPlayerListeners) {
			listeners = (ArrayList) (mediaPlayerListeners.clone());
		}
		int size = listeners.size();
		for (int i = 0; i < size; i++) {
			MediaPlayerListener l = (MediaPlayerListener) (listeners.get(i));
			MediaPlayerEvent e = new MediaPlayerEvent(this);
			l.playerFinished(e);
		}
	}

	/**
	 * Get menu item for repaly.
	 * @return Menu item for replay.
	 */
	JCheckBoxMenuItem getCheckBoxMenuItemReplay() {
		return checkBoxMenuItemReplay;
	}

	/**
	 * The object implements the writeExternal method to save its contents by
	 * calling the methods of DataOutput for its primitive values or calling the
	 * writeObject method of ObjectOutput for objects, strings, and arrays.
	 * 
	 * @serialData Overriding methods should use this tag to describe the data
	 *             layout of this Externalizable object. List the sequence of
	 *             element types and, if possible, relate the element to a
	 *             public/protected field and/or method of this Externalizable
	 *             class.
	 * 
	 * @param out
	 *            the stream to write the object to
	 * @exception IOException
	 *                Includes any I/O exceptions that may occur
	 */
	public void writeExternal(ObjectOutput out) throws IOException {
		ESlateFieldMap2 fieldMap = new ESlateFieldMap2(STORAGE_VERSION);

		fieldMap.put("File", file);

		out.writeObject(fieldMap);
		out.flush();
	}

	/**
	 * The object implements the readExternal method to restore its contents by
	 * calling the methods of DataInput for primitive types and readObject for
	 * objects, strings and arrays. The readExternal method must read the values
	 * in the same sequence and with the same types as were written by
	 * writeExternal.
	 * 
	 * @param in
	 *            the stream to read data from in order to restore the object
	 * @exception IOException
	 *                if I/O errors occur
	 * @exception ClassNotFoundException
	 *                If the class for an object being restored cannot be found.
	 */
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		initialize(in);
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			final MediaPlayer mediaPlayer = new MediaPlayer();

			JFrame frame = new JFrame("Media player");
			frame.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					mediaPlayer.exit();
				}

				public void windowClosed(WindowEvent e) {
					System.exit(0);
				}
			});
			frame.setContentPane(mediaPlayer);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
			frame.toFront();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
