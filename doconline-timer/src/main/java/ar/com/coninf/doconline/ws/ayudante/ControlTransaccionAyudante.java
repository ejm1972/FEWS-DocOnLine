package ar.com.coninf.doconline.ws.ayudante;

import org.springframework.stereotype.Component;

import ar.com.coninf.doconline.rest.model.response.Response;
import ar.com.coninf.doconline.rest.model.tx.ControlTransaccion;
import ar.com.coninf.doconline.ws.validador.ValidadorWS;

@Component("ayudante.controlTransaccionAyudante")
public class ControlTransaccionAyudante {

	public static Response hacer(ControlTransaccion ctx) {
		ValidadorWS validar = new ValidadorWS();
		
		// Validar obligatoriedad
		validar.validarObligatorio("interfaz", ctx.getInterfaz());
		validar.validarObligatorio("nroTransaccion", ctx.getNroTransaccion());
		validar.validarObligatorio("idSesion", ctx.getIdSesion());
		
		Response respuesta = validar.obtenerRespuesta();
		
		if (respuesta != null) {
			return respuesta;
		}
		
		return null;
	}

}
