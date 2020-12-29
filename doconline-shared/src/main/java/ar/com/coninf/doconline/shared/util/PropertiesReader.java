package ar.com.coninf.doconline.shared.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertiesReader extends Properties {

	private static final long serialVersionUID = 1L;

	Logger logger = Logger.getLogger(PropertiesReader.class);
	
	private String fileName;
	
	public PropertiesReader(String fileName) throws IOException {
		this.fileName = fileName;

		if(fileName == null)
			logger.error("fileName cannot be null.");
		
		super.load(getPropertiesInputStream());
	}

	private InputStream getPropertiesInputStream() {
		return getClass().getClassLoader().getResourceAsStream(fileName);
	}
	
	public String get(String key) {
		if(fileName == null)
			logger.error("fileName cannot be null.");
		
		return (String) super.get(key);
	}
}
