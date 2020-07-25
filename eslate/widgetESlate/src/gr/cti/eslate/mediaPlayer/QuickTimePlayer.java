package gr.cti.eslate.mediaPlayer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.File;

import javax.swing.JPanel;

import quicktime.QTException;
import quicktime.QTSession;
import quicktime.app.view.QTComponent;
import quicktime.app.view.QTFactory;
import quicktime.io.QTFile;
import quicktime.std.StdQTConstants;
import quicktime.std.image.GraphicsImporter;
import quicktime.std.movies.Movie;
import quicktime.std.movies.MovieController;
import quicktime.std.movies.media.DataRef;

/**
 * QuickTime player.
 * 
 * @author augril
 */
public class QuickTimePlayer extends JPanel implements IPlayer {
	private static final long serialVersionUID = 1L;

	/**
	 * QuickTime movie.
	 */
	private Movie movie;

	/**
	 * QuickTime component.
	 */
	private QTComponent qtComponent = null;

	/**
	 * Create a new QuickTime player.
	 */
	public QuickTimePlayer() {
		super(new BorderLayout());

		initialize();
	}

	/**
	 * Initialize QuickTime player.
	 */
	public void initialize() {
		try {
			QTSession.open();
		} catch (QTException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create a new QuickTime movie from a file.
	 * 
	 * @param file
	 *            The file to find the movie.
	 */
	private void createMovie(File file) {
		try {
			String url = "file://" + file.getAbsolutePath();
			// Create the DataRef that contains the information about where the
			// movie is
			DataRef dataRef = new DataRef(url);

			// Create the movie
			movie = Movie.fromDataRef(dataRef, StdQTConstants.newMovieActive);

			// Create the movie controller
			MovieController movieController = new MovieController(movie);

			// Create and add a QTComponent if we haven't done so yet, otherwise
			// set qtc's movie controller
			if (qtComponent == null) {
				qtComponent = QTFactory.makeQTComponent(movieController);
				add((Component) qtComponent, BorderLayout.CENTER);
			} else {
				qtComponent.setImage(null);
				qtComponent.setMovieController(movieController);
			}
//			movie.start();
		} catch (QTException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create a new QuickTime image from a file.
	 * 
	 * @param file
	 *            The file to find the image.
	 */
	private void createImage(File file) {
		try {
			QTFile qtFile = new QTFile(file);
			GraphicsImporter graphicsImporter = new GraphicsImporter(qtFile);
			// Create and add a QTComponent if we haven't done so yet, otherwise
			// set qtc's image
			if (qtComponent == null) {
				qtComponent = QTFactory.makeQTComponent(graphicsImporter);
				add((Component) qtComponent, BorderLayout.CENTER);
			} else {
				try {
					qtComponent.setMovieController(null);
				} catch (Exception e) {
					// e.printStackTrace();
				}
				qtComponent.setImage(graphicsImporter);
			}
		} catch (QTException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open a QuickTime file.
	 * 
	 * @param file
	 *            The file to open.
	 */
	public void open(File file) {
		stop();

		String name = file.getName();
		int pos = name.lastIndexOf(".");
		String extension = pos != -1 ? name.substring(pos + 1) : "";
		if (extension.equalsIgnoreCase("mov"))
			createMovie(file);
		else
			createImage(file);
	}

	/**
	 * Exit QuickTime player.
	 */
	public void exit() {
		close();
		QTSession.close();
	}

	/**
	 * Play QuickTime player.
	 */
	public void start() {
		try {
			if (movie != null)
				movie.start();
		} catch (QTException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Pause QuickTime player.
	 */
	public void pause() {
		try {
			if (movie != null)
				movie.setRate(0);
		} catch (QTException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Stop QuickTime player.
	 */
	public void stop() {
		try {
			if (movie != null)
				movie.stop();
		} catch (QTException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Close media file.
	 */
	public void close() {
		try {
			if (qtComponent != null) {
				try {
					qtComponent.setMovieController(null);
				} catch (Exception e) {
				}
				qtComponent.setImage(null);
			}
		} catch (QTException e) {
			 e.printStackTrace();
		}
	}
}
