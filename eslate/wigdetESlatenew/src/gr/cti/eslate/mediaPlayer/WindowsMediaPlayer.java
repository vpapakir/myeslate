package gr.cti.eslate.mediaPlayer;

import java.io.File;

import wmp.mediaplayer.MPDisplaySizeConstants;
import wmp.mediaplayer._MediaPlayerEvents;
import wmp.mediaplayer.impl.IMediaPlayerImpl;
import wmp.mediaplayer.server._MediaPlayerEventsServer;
import wmp.stdole.OLE_XPOS_PIXELS;
import wmp.stdole.OLE_YPOS_PIXELS;

import com.jniwrapper.Int16;
import com.jniwrapper.Int32;
import com.jniwrapper.win32.automation.IDispatch;
import com.jniwrapper.win32.automation.OleContainer;
import com.jniwrapper.win32.automation.impl.IDispatchImpl;
import com.jniwrapper.win32.automation.server.IDispatchVTBL;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.com.IClassFactory;
import com.jniwrapper.win32.com.server.CoClassMetaInfo;
import com.jniwrapper.win32.com.server.IClassFactoryServer;
import com.jniwrapper.win32.com.types.IID;
import com.jniwrapper.win32.ole.IConnectionPoint;
import com.jniwrapper.win32.ole.IConnectionPointContainer;
import com.jniwrapper.win32.ole.IEnumConnections;
import com.jniwrapper.win32.ole.impl.IConnectionPointContainerImpl;
import com.jniwrapper.win32.ole.impl.IOleObjectImpl;
import com.jniwrapper.win32.ole.types.OleVerbs;

/**
 * Windows media player.
 * 
 * @author augril
 */
public class WindowsMediaPlayer extends OleContainer implements IPlayer {
	private static final long serialVersionUID = 1L;

	/**
	 * Media player panel container.
	 */
	private static MediaPlayer MEDIA_PLAYER;

	/**
	 * Windows media player implementation.
	 */
	private IMediaPlayerImpl mediaPlayer;

	/**
	 * Create a new windows media player.
	 */
	public WindowsMediaPlayer(MediaPlayer mp) {
		MEDIA_PLAYER = mp;

		initialize();
	}

	/**
	 * Initialize windows media player.
	 */
	public void initialize() {
		createOleObject();
		setupListener();
	}

	/**
	 * Creates embedded object.
	 */
	private void createOleObject() {
		createObject("MediaPlayer.MediaPlayer");
	}

	/**
	 * Activates embedded object.
	 */
	private void showOleObject() {
		doVerb(OleVerbs.INPLACEACTIVATE);
	}

	/**
	 * Destroys embedded object.
	 */
	private void destroyOleObject() {
		destroyObject();
	}

	/**
	 * Loads file into embedded object.
	 * 
	 * @param file
	 *            The file to load.
	 */
	public void open(File file) {
		showOleObject();

		mediaPlayer = new IMediaPlayerImpl(getOleObject());

		mediaPlayer.setDisplaySize(new MPDisplaySizeConstants(
				MPDisplaySizeConstants.mpFitToSize));
		mediaPlayer.setPlayCount(new Int32(MEDIA_PLAYER.getCheckBoxMenuItemReplay().isSelected()?0:1)); // 0 == play continuously
		mediaPlayer.setFileName(new BStr(file.getAbsolutePath()));
		mediaPlayer.stop();

		double duration = mediaPlayer.getDuration().getValue();
		System.out.println("Duration of clip \"" + file.getAbsolutePath()
				+ "\": " + Math.round(duration) + " seconds");
	}

	/**
	 * Exit windows media player.
	 */
	public void exit() {
		destroyOleObject();
	}

	/**
	 * Start windows media player.
	 */
	public void start() {
		mediaPlayer.play();
	}

	/**
	 * Pause windows media player.
	 */
	public void pause() {
		mediaPlayer.pause();
	}

	/**
	 * Stop windows media player.
	 */
	public void stop() {
		mediaPlayer.stop();
	}

	/**
	 * Close media file.
	 */
	public void close() {
		mediaPlayer.setFileName(null);
	}

	/**
	 * Get Windows media player implementation.
	 * @return Windows media player implementation.
	 */
	IMediaPlayerImpl getMediaPlayer() {
		return mediaPlayer;
	}

	private void setupListener() {
		IClassFactoryServer server = new IClassFactoryServer(
				MediaPlayerListener.class);
		server.registerInterface(IDispatch.class, new IDispatchVTBL(server));
		server.registerInterface(_MediaPlayerEvents.class, new IDispatchVTBL(
				server));
		server.setDefaultInterface(IDispatch.class);

		IClassFactory classFactory = server.createIClassFactory();
		IDispatchImpl handler = new IDispatchImpl();
		classFactory.createInstance(null, handler.getIID(), handler);

		IOleObjectImpl oleObject = getOleObject();
		IConnectionPointContainer connectionPointContainer = new IConnectionPointContainerImpl(
				oleObject);
		IConnectionPoint connectionPoint = connectionPointContainer
				.findConnectionPoint(new IID(
						_MediaPlayerEvents.INTERFACE_IDENTIFIER));

		IEnumConnections enumConnections = connectionPoint.enumConnections();
		enumConnections.reset();

		connectionPoint.advise(handler);
	}

	public static class MediaPlayerListener extends _MediaPlayerEventsServer {
		public MediaPlayerListener(CoClassMetaInfo coClassMetaInfo) {
			super(coClassMetaInfo);
		}

		public void playStateChange(Int32 int32, Int32 int33) {
			System.out.println("New state of Media Player component is "
					+ int33.getValue());
			if (int33.getValue() == 0)
				MEDIA_PLAYER.firePlayerStopped();
			else if (int33.getValue() == 1)
				MEDIA_PLAYER.firePlayerPaused();
			else if (int33.getValue() == 2)
				MEDIA_PLAYER.firePlayerStarted();
		}

		public void click(Int16 int16, Int16 int17,
				OLE_XPOS_PIXELS ole_xpos_pixels, OLE_YPOS_PIXELS ole_ypos_pixels) {
			System.out.println("Clicked on Media Player");
		}

		public void endOfStream(Int32 result) {
			MEDIA_PLAYER.firePlayerFinished();
		}

	}
}
