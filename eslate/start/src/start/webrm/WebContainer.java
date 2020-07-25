package start.webrm;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.cbook.cbookif.rm.ReadOnlyException;
import org.cbook.cbookif.rm.Resource;
import org.cbook.cbookif.rm.ResourceContainer;
import org.cbook.cbookif.rm.ResourceException;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

public class WebContainer implements ResourceContainer {

	
	
	private static final String UTF8 = "UTF-8";
	private WebContainer parent;
	private URL url;
	private String name;

	public WebContainer() {
		// TODO Auto-generated constructor stub
	}

	public WebContainer(URL url, String name) {
		this.url = url;
		this.name = name;
	}

	public WebContainer(URL url, String name, WebContainer parent) {
		this(url, name);
		this.parent = parent;
	}

	@Override
	public boolean isContainer() {
		return true;
	}

	@Override
	public ResourceContainer getParent() {
		return parent;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public URL getURL() throws ResourceException {
		return url;
	}

	@Override
	public InputStream getStream() throws ResourceException {
		try {
			return url.openStream();
		} catch (IOException e) {
			throw new ResourceException(e);
		}
	}

	Reader getReader() throws ResourceException {
		try {
			return new InputStreamReader(getStream(), UTF8);
		} catch (UnsupportedEncodingException _) {
			return null;
		}
	}

	@Override
	public String getMimeType() {
		return "application/folder";
	}

	@Override
	public void remove() throws ResourceException {
		throw readonly();

	}

	@Override
	public void setName(String name) throws ResourceException {
		throw readonly();
	}

	@Override
	public Resource open(String name) throws ResourceException {
		if(name.startsWith("/"))
			return getRoot().open(name.substring(1));
		while(name.startsWith("../"))
		{
			name = name.substring(3);
			if(parent != null)
				return parent.open(name);
		}
		while(name.startsWith("./"))
		{
			name = name.substring(2);
		}
		int i = name.indexOf('/');
		if(i >= 0) {
			String dir = name.substring(0,i);
			name = name.substring(i+1);
			return openContainer(dir).open(name);
		}
		if(".".equals(name) || "".equals(name))
			return this;
		if("..".equals(name))
		{
			if(parent == null) return this; else return parent;
		}
		
		try {
			
			URL u = new URL(url, name + "?stat");
			Reader reader = new InputStreamReader(u.openStream(), UTF8);
			JSONObject o = (JSONObject) JSONValue.parse(reader);
			return open(o);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	ResourceContainer getRoot() {
		if(parent == null) return this;
		return parent.getRoot();
	}

	@Override
	public Resource create(String name, InputStream in, String mimetype)
			throws ResourceException {
		return null;
	}


	@Override
	public Resource create(String name, URL in)
			throws ResourceException {
		throw readonly();
	}

	@Override
	public Resource create(String name, Resource resource)
			throws ResourceException {
		throw readonly();
	}

	protected ResourceException readonly() {
		return new ReadOnlyException(name);
	}

	@Override
	public ResourceContainer createContainer(String name)
			throws ResourceException {
		return null;
	}

	@Override
	public ResourceContainer openContainer(String name)
			throws ResourceException {
		Resource r = open(name);
		if(r instanceof ResourceContainer)
			return (ResourceContainer) r;
		return null;
	}

	@Override
	public Resource[] list() throws ResourceException {
		try {
			Reader in  = getReader();
			List list = (List) JSONValue.parse(in);
			Resource[] result = new Resource[list.size()];
			for (int i = 0; i < result.length; i++) {
				JSONObject object = (JSONObject) list.get(i);
				result[i] = open(object);
			}
			return result;
		} catch (Exception e) {
			throw new ResourceException(e);
		}
	}

	Resource open(JSONObject object) throws MalformedURLException {
		String type = object.get("type").toString();
		String name = object.get("name").toString();
		String ref = (String) object.get("ref");
		Resource r;
		if("DIR".equals(type))
			r = new WebContainer(new URL(ref), name, this);
		else if("FILE".equals(type))
			r = new WebResource(new URL(ref), name, this, (String) object.get("mimetype"));
		else
			r = new WebResource(new URL(ref), name, this, (String) object.get("mimetype"));
		return r;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WebContainer other = (WebContainer) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (parent == null) {
			if (other.parent != null)
				return false;
		} else if (!parent.equals(other.parent))
			return false;
		return true;
	}

}
