package start.webrm;

import java.net.MalformedURLException;
import java.net.URL;

import org.cbook.cbookif.rm.ResourceContainer;
import org.cbook.cbookif.rm.ResourceManager;

public class WebManager implements ResourceManager {

	private URL root;
	private String widget;
	private String unit, instance, student;
	
	
	@Override
	public ResourceContainer getWidgetContainer() {
		URL widget = null;
		try {
			widget = new URL(root, "Widget/" + this.widget + "/");
		} catch (MalformedURLException _) {
		}
		return new WebContainer(widget, "/");
	}

	public WebManager(URL root, String widget, String unit, String instance,
			String student) {
		super();
		this.root = root;
		this.widget = widget;
		this.unit = unit;
		this.instance = instance;
		this.student = student;
	}

	@Override
	public ResourceContainer getInstanceContainer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResourceContainer getStudentContainer() {
		// TODO Auto-generated method stub
		return null;
	}

}
