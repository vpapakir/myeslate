package start.rm;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;

import org.cbook.cbookif.rm.Resource;
import org.cbook.cbookif.rm.ResourceContainer;
import org.cbook.cbookif.rm.ResourceException;

class Directory extends File implements ResourceContainer {

	Directory(java.io.File file, Directory parent, String type) {
		super(file, parent, type);
	}

	@Override
	public Resource open(String name) throws ResourceException {
		return new File(newFile(name), this, null);
	}

	java.io.File newFile(String name) {
		return new java.io.File(file, name);
	}

	@Override
	public Resource create(String name, InputStream in, String mimetype)
			throws ResourceException {
		File result = new File(newFile(name), this, mimetype);
		try {
			copy( new FileOutputStream( result.file ), in );
		} catch (FileNotFoundException e) {
			throw new ResourceException(e);
		} catch (IOException e) {
			throw new ResourceException(e);
		}
		return result;
	}

	private void copy(OutputStream out, InputStream in) throws IOException {
		int b;
		while( -1 != (b = in.read())) out.write(b);
		in.close();
		out.close();
	}
	
	@Override
	public Resource create(String name, URL in)
			throws ResourceException {
		URLConnection u;
		try {
			u = in.openConnection();
		} catch (IOException e1) {
			throw new ResourceException(e1);
		}
		File result = new File(newFile(name), this, u.getContentType());
		try {
			copy(new FileOutputStream(result.file), u.getInputStream());
		} catch (FileNotFoundException e) {
			throw new ResourceException(e);
		} catch (IOException e) {
			throw new ResourceException(e);
		}
		return result;
	}

	@Override
	public Resource create(String name, Resource in)
			throws ResourceException {
		File result = new File(newFile(name), this, in.getMimeType());
		try {
			copy(new FileOutputStream(result.file), in.getStream());
		} catch (FileNotFoundException e) {
			throw new ResourceException(e);
		} catch (IOException e) {
			throw new ResourceException(e);
		}		
		return result;
	}

	@Override
	public ResourceContainer createContainer(String name)
			throws ResourceException {
		Directory result = new Directory(newFile(name), this, null);
		result.file.mkdir();
		return result;
	}

	@Override
	public ResourceContainer openContainer(String name)
			throws ResourceException {
		Directory result = new Directory(newFile(name), this, null);
		return result;
	}

	@Override
	public Resource[] list() throws ResourceException {
		java.io.File[] list = file.listFiles();
		Resource[] result = new Resource[list.length];
		for (int i = 0; i < result.length; i++) {
			if(list[i].isDirectory())
				result[i] = newDirectory(list[i]);
			else
				result[i] = new File(list[i], this, null);
		}
		return result;
	}

	@Override
	public boolean isContainer() {
		return true;
	}

	@Override
	public String toString() {
		return "Directory [dir=" + file + "]";
	}

	Directory newDirectory(java.io.File dir) {
		return new RoDirectory(dir, this, null);
	}


}
