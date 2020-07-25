package gr.cti.eslate.mediaPlayer;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * Media player file filter.
 * 
 * @author augril
 */
class MediaPlayerFileFilter extends FileFilter {
	private String[] fileExtensions;

	private String description;

	MediaPlayerFileFilter(String[] fileExtensions, String description) {
		this.fileExtensions = fileExtensions;
		this.description = description;
	}

	public boolean accept(File pathname) {
		if (pathname.isFile()) {
			String name = pathname.getName();
			int pos = name.lastIndexOf(".");
			String extension = pos != -1 ? name.substring(pos + 1) : "";

			for (int i = 0; i < fileExtensions.length; i++) {
				String fileExtension = fileExtensions[i];
				if (fileExtension.equalsIgnoreCase(extension))
					return true;
			}
			return false;
		}
		return true;
	}

	public String getDescription() {
		return description;
	}
}
