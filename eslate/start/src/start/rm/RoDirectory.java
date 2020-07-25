package start.rm;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import org.cbook.cbookif.rm.ReadOnlyException;
import org.cbook.cbookif.rm.Resource;
import org.cbook.cbookif.rm.ResourceContainer;
import org.cbook.cbookif.rm.ResourceException;

class RoDirectory extends Directory {

	RoDirectory(File file, Directory parent, String type) {
		super(file, parent, type);
	}

	@Override
	public Resource create(String name, InputStream in, String mimetype)
			throws ResourceException {
		throw new ReadOnlyException();
	}


	@Override
	public Resource create(String name, URL in)
			throws ResourceException {
		throw new ReadOnlyException();
	}

	@Override
	public Resource create(String name, Resource in) throws ResourceException {
		throw new ReadOnlyException();
	}

	@Override
	public ResourceContainer createContainer(String name)
			throws ResourceException {
		throw new ReadOnlyException();
	}

	@Override
	Directory newDirectory(File dir) {
		return new RoDirectory(dir, this, null);
	}

}
