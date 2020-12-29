package ar.com.coninf.doconline.shared.recurso;

import java.util.Locale;
import java.util.ResourceBundle;

public class ApplicationResourceBundle {
	
	private static ApplicationResourceBundle instance;
	private static String res;
	private static Locale loc;
	
	private ResourceBundle bundle;
	
	ApplicationResourceBundle(String resource){
		bundle = ResourceBundle.getBundle(resource);
	}
	
	ApplicationResourceBundle(String resource, Locale locale){
		bundle = ResourceBundle.getBundle(resource, locale);
	}
	
	public static ApplicationResourceBundle getInstance(String resource){
		controlResource(resource, null);
		if(instance == null){
			instance = new ApplicationResourceBundle(resource);
			res = resource;
		}
		return instance;
	}
	
	public static ApplicationResourceBundle getInstance(String resource, Locale locale){
		controlResource(resource, null);
		if(instance == null){
			instance = new ApplicationResourceBundle(resource, locale);
			res = resource;
			loc = locale;
		}
		return instance;
	}
	
	private static void controlResource(String resource, Locale locale) {
		if(res != null && !res.equals(resource) || loc != null && !loc.equals(locale)){
			instance = null;
		}
	}
	
	public String getString(String key){
		String mensaje = bundle.getString(key);
		return mensaje;
	}
	
	public String getString(String key, Object[] args){
		String mensaje = bundle.getString(key);
		for (int i = 0; i < args.length; i++) {
			String arg = String.valueOf(args[i]);
			mensaje = mensaje.replace("{"+i+"}", arg);
		}
		return mensaje;
	}
}
