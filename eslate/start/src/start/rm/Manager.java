package start.rm;

import java.io.InputStreamReader;
import java.io.Reader;

import org.cbook.cbookif.rm.Resource;
import org.cbook.cbookif.rm.ResourceContainer;
import org.cbook.cbookif.rm.ResourceManager;

public class Manager implements ResourceManager {

	private ResourceManager delegate;
	public Manager(ResourceManager delegate) {
		super();
		this.delegate = delegate;
	}

	@Override
	public ResourceContainer getWidgetContainer() {
		if(delegate != null)
			return delegate.getWidgetContainer();
		Directory directory = new RoDirectory(new java.io.File("resources/widgetcontainer"), null, null);
		directory.parent = directory;
		directory.file.mkdirs();
		return directory;
	}

	@Override
	public ResourceContainer getInstanceContainer() {
		Directory directory = new Directory(new java.io.File("resources/instancecontainer"), null, null);
		directory.parent = directory;
		directory.file.mkdirs();
		return directory;
	}
	
	@Override
	public ResourceContainer getStudentContainer() {
		Directory directory = new Directory(new java.io.File("resources/studentcontainer"), null, null);
		directory.parent = directory;
		directory.file.mkdirs();
		return directory;
	}


	public static void main(String[] args) throws Exception {
		ResourceManager m = new Manager(null);
		ResourceContainer container = m.getInstanceContainer();
		Resource[] resources = container.list();
		for (int i = 0; i < resources.length; i++) {
			System.out.println(resources[i]);
			if(!resources[i].isContainer())
			{
				Reader r = new InputStreamReader (resources[i].getStream());
				int c;
				while( -1 !=  (c = r.read()) )
						System.out.print((char)c);
				r.close();
				System.out.flush();
			} else
			{
				ResourceContainer r = (ResourceContainer) resources[i];
				r.create("hopla", resources[1-i]);
			}
		}
		
	}

}
