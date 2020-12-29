package ar.com.boldt.monedero.ws.handler;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ar.com.coninf.doconline.business.servicio.ValidadorPinServices;
import ar.com.coninf.doconline.rest.model.enums.ErrorEnum;
import ar.com.coninf.doconline.rest.model.response.Response;
import ar.com.coninf.doconline.shared.excepcion.ApplicationException;
import ar.com.coninf.doconline.shared.recurso.ApplicationResourceBundle;
import ar.com.coninf.doconline.shared.servicio.MailService;

@Component("handler.applicationException")
public class ApplicationExceptionHandler {
	@Autowired
	@Qualifier("mailService")
	private MailService mailService;
	
	@Autowired
	@Qualifier("services.validadorPinServices")
	private ValidadorPinServices validadorPinServices;

	public Response manejarExcepcion(Throwable exception){
		ApplicationResourceBundle bundle = ApplicationResourceBundle.getInstance("messageResources_es_AR");
		String observacion = null;
		ErrorEnum errorWS = ErrorEnum.ERROR_INESPERADO;
		Response respuesta = new Response(errorWS);
		try{
			if (exception instanceof ApplicationException) {
				ApplicationException applicationException = (ApplicationException) exception;
				if (applicationException.getKey() != null) {
					if (applicationException.getArgs() != null) {
						observacion = bundle.getString(applicationException.getKey(), applicationException.getArgs());
					} else {
						observacion = bundle.getString(applicationException.getKey());
					}
				}
				if (applicationException.getError() != null) {
					respuesta.setCodigo(applicationException.getError().getCod());
					respuesta.setDescripcion(applicationException.getError().getDesc());
					respuesta.setObservacion(observacion);
				} else if(exception.getCause() != null) {
					observacion = exception.getCause().toString();
					respuesta = new Response(errorWS, observacion);
				} else {
					observacion = exception.toString();
					respuesta = new Response(errorWS, observacion);
				}
			} else {
				exception.printStackTrace();
			}
			
			if(exception.getCause() != null){
				exception.getCause().printStackTrace();
			}
		} catch (Throwable e) {
			Logger.getLogger(this.getClass()).error("Ocurrio un error grave durante el manejo de excepciones.");
			Logger.getLogger(this.getClass()).error(e.getCause() != null ? e.getCause().toString() : e.toString());
			e.printStackTrace();
		}
		
		Logger.getLogger(this.getClass()).info("Codigo: " + respuesta.getCodigo() + " - Descripcion: " + respuesta.getDescripcion() + " - Observaciones: " + respuesta.getObservacion());
		
		return respuesta;
	}
	
}
