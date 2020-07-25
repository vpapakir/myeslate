package start.rm;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.cbook.cbookif.rm.Resource;
import org.cbook.cbookif.rm.ResourceContainer;
import org.cbook.cbookif.rm.ResourceException;

class File implements Resource {

	final java.io.File file;
	Directory parent;
	final String type;
	
	public File(java.io.File file, Directory parent, String type) {
		this.file = file;
		this.parent = parent;
		this.type = type;
	}

	@Override
	public boolean isContainer() {
		return false;
	}

	@Override
	public ResourceContainer getParent() {
		return parent;
	}

	@Override
	public String getName() {
		return file.getName();
	}

	@Override
	public URL getURL() throws ResourceException {
		try {
			return file.toURI().toURL();
		} catch (MalformedURLException e) {
			throw new ResourceException(e);
		}
	}

	@Override
	public InputStream getStream() throws ResourceException {
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new ResourceException(e);
		}
	}

	@Override
	public String getMimeType() {
		return type;
	}

	@Override
	public void remove() throws ResourceException {
		file.delete();
		parent = null;
	}

	@Override
	public void setName(String name) throws ResourceException {
		file.renameTo(new java.io.File(file.getParentFile(), name));
	}

	@Override
	public String toString() {
		return "File [file=" + file + ", type=" + type + "]";
	}


}
