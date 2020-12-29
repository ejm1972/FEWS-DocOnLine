package ar.com.coninf.doconline.ws.interceptores;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import ar.com.coninf.doconline.rest.model.response.Response;
import ar.com.coninf.doconline.rest.model.tx.ControlTransaccion;
import ar.com.coninf.doconline.shared.dto.RegistroTransaccionDto;
import ar.com.coninf.doconline.shared.util.MonederoUtiles;
import ar.com.coninf.doconline.ws.handler.ApplicationExceptionHandler;
import ar.com.coninf.doconline.ws.validador.ValidadorWS;


public class ServicioWebInterceptor implements MethodInterceptor {
	
	private Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	@Qualifier("handler.applicationException")
	private ApplicationExceptionHandler appExcepHandler;

	@Override
	public Object invoke(MethodInvocation arg0) throws Throwable {
		Object proceed = null;
		ControlTransaccion ct = null;
		String servicio = MonederoUtiles.generarNombreServicio(arg0.getThis().toString(), arg0.getMethod().getName());

		String ws = arg0.getThis().toString();
		if(ws.indexOf("@") != -1){
			ws = ws.substring(0, ws.indexOf("@"));
		}
		
		Date date = new Date();
		
		Object[] arguments = arg0.getArguments();
		
		for (int i = 0; i < arguments.length; i++) {
			if(arguments[i] instanceof ControlTransaccion){
				ct = (ControlTransaccion) arguments[i];
				break;
			}
		}
		
		try{
			//Valido datos transaccion
			proceed = validarDatosTx(ct);
			
			if(proceed == null){
				//Invoco el servicio
				proceed = ejecutarServicio(arg0, ct, servicio, ws, date);
			}else{
				Object newInstance = Class.forName(arg0.getMethod().getReturnType().getName()).newInstance();
				((Response)newInstance).cargarError((Response)proceed);
				proceed = newInstance;
			}
			
		}catch (Throwable e){
			Response respuesta = (Response)arg0.getMethod().getReturnType().newInstance();
			respuesta.cargarError(appExcepHandler.manejarExcepcion(e));
			proceed = respuesta;
		}
		
		//Registro transaccion
		return registrarTx(proceed, ct, servicio, date);
	}

	private Response validarDatosTx(ControlTransaccion ct) {
		Response resp = null; 
		if(ct != null){
			ValidadorWS validar = new ValidadorWS();
			// Validar obligatoriedad
			validar.validarObligatorio("interfaz", ct.getInterfaz());
			validar.validarObligatorio("nroTransaccion", ct.getNroTransaccion());
			validar.validarObligatorio("idSesion", ct.getIdSesion());
			
			Response respuesta = validar.obtenerRespuesta();
			
			if (respuesta != null) {
				resp = respuesta;
			}
		}
		return resp;
	}

	private Object ejecutarServicio(MethodInvocation arg0, ControlTransaccion ct, String servicio, String ws, Date date)
			throws ParseException, Throwable, SQLException, IOException,
			ClassNotFoundException {
		
		return arg0.proceed();
	}

	private Object registrarTx(Object proceed, ControlTransaccion ct,
			String servicio, Date date) {
		
		if (ct != null && !((Response)proceed).esReintento) {
			try {
				RegistroTransaccionDto registro = new RegistroTransaccionDto();
				registro.setCodigoTransaccion(ct.getNroTransaccion());
				registro.setFechaTransaccion(date);
				registro.setInterfazId(ct.getInterfaz());
				registro.setServicio(servicio);
	
				byte[] serializar = MonederoUtiles.serializar((Serializable) proceed);
				registro.setDatosTransaccion(serializar);
	
			} catch(Throwable e) {
				log.error("No se pudo registrar la transaccion [" + ct.getNroTransaccion() + "]. Servicio: " + servicio);
				log.error(e.getMessage());
				if(proceed instanceof Response){
					Response resp = (Response)proceed;
					if(resp.getCodigo() != null && resp.getCodigo().equals(0)){
						log.info("Se procede a realizar un rollback de la operacion.");
					}
				}
			}
		}
		return proceed;
	}

}
