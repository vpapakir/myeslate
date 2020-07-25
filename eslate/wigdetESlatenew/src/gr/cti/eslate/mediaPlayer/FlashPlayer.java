package gr.cti.eslate.mediaPlayer;

import flash.shockwaveflashobjects.IShockwaveFlash;
import flash.shockwaveflashobjects.ShockwaveFlash;

import java.io.File;

import com.jniwrapper.win32.automation.OleContainer;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.ole.IOleObject;
import com.jniwrapper.win32.ole.OleFunctions;
import com.jniwrapper.win32.ole.impl.IOleObjectImpl;
import com.jniwrapper.win32.ole.types.OleVerbs;

/**
 * Flash player.
 * 
 * @author augril
 */
public class FlashPlayer extends OleContainer  implements IPlayer {
	private static final long serialVersionUID = 1L;

	/**
	 * Shokwave Flash object.
	 */
	private IShockwaveFlash _flash;

	/**
	 * Create new flash player.
	 */
	public FlashPlayer() {
		initialize();
	}

	/**
	 * Initialize flash player.
	 */
	public void initialize() {
		// initialize OLE
		OleFunctions.oleInitialize();
		
		createOleObject();
	}

	/**
	 * Creates embedded object.
	 */
	private void createOleObject() {
		IShockwaveFlash flash = ShockwaveFlash.create(ClsCtx.INPROC_SERVER);
		IOleObject oleObject = new IOleObjectImpl(flash);

		_flash = flash;

		insertObject(oleObject);
	}

	/**
	 * Activates embedded object.
	 */
	private void showOleObject() {
		doVerb(OleVerbs.SHOW);
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
		
		// load movie
		_flash.setMovie(new BStr(file.getAbsolutePath()));
		_flash.rewind();
		_flash.play();

		// get count of frames
		long framesCount = _flash.getTotalFrames().getValue();
		System.out.println("framesCount = " + framesCount);
	}
	
	/**
	 * Exit flash player.
	 */
	public void exit() {
		destroyOleObject();
		OleFunctions.oleUninitialize();
	}

	/**
	 * Start flash player.
	 */
	public void start() {
		_flash.play();
	}
	
	/**
	 * Pause flash player.
	 */
	public void pause() {
	}
	
	/**
	 * Stop flash player.
	 */
	public void stop() {
		_flash.stop();
	}
	
	/**
	 * Close media file.
	 */
	public void close() {
//		try {
//			_flash.setMovie(null);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		_flash.stop();
	}
}
