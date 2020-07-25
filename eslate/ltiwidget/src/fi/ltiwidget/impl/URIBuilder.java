package fi.ltiwidget.impl;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/** 
 * Very basic trampoline to get the arguments to the LTI tool
 * @version 0.1
*/
public class URIBuilder {

	private static final String UTF8 = "UTF-8";

	private final URI bootstrap;
	
	/**
	 * Construct URL builder
	 * @param bootstrap URL to send parameters to
	 */
	public URIBuilder(URI bootstrap) {
		this.bootstrap = bootstrap;
	}

	/**
	 * Construct an launch URL.
	 * This url will call the LTI tool via the bootstrap
	 * @param params key-value pairs
	 * @return
	 * @throws IllegalArgumentException
	 */
	public URI getRequestURI(Map<String,String> params) throws IllegalArgumentException {
		StringBuilder query = new StringBuilder(); 

		for (Map.Entry<String, String> entry : params.entrySet()) {
			if(entry.getValue() != null)
			try {
				query.append('&').append(entry.getKey()).append('=').append(URLEncoder.encode(entry.getValue(),UTF8));
			} catch (UnsupportedEncodingException _) {
			}
		}
		if( query.length() > 0 ) 
			query.setCharAt(0, '?');		
		try {
			return new URI( bootstrap + query.toString());
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(e.getMessage(),e);		}
	}
	
	
}
