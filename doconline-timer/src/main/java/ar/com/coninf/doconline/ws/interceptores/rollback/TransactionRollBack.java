package ar.com.coninf.doconline.ws.interceptores.rollback;

import ar.com.coninf.doconline.rest.model.response.Response;

public abstract class TransactionRollBack {
	
	private static final String paquete = "ar.com.coninf.doconline.ws.interceptores.rollback.";
	
	private static TransactionRollBack servicioRollBack;
	protected static Response respuesta;
	
	@SuppressWarnings("rawtypes")
	public static TransactionRollBack getInstance(String servicio, Response resp) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		String clase = servicio.substring(0, 1).toUpperCase().concat(servicio.substring(1, servicio.length())).concat("RollBack");
		Class manager = Class.forName(paquete.concat(clase));
		servicioRollBack = (TransactionRollBack)manager.newInstance();
		respuesta = resp;
		return servicioRollBack;
	}
	
	protected abstract Object[] obtenerArgumentos();
	
}
