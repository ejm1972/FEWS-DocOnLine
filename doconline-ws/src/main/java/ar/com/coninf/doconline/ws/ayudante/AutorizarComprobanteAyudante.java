package ar.com.coninf.doconline.ws.ayudante;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ar.com.coninf.doconline.business.facade.AutorizarComprobanteFacade;
import ar.com.coninf.doconline.rest.model.request.RequestAutorizarComprobante;
import ar.com.coninf.doconline.rest.model.response.Response;
import ar.com.coninf.doconline.rest.model.response.ResponseAutorizarComprobante;
import ar.com.coninf.doconline.rest.model.tx.ControlTransaccion;
import ar.com.coninf.doconline.ws.validador.ValidadorWS;

@Component("ayudante.autorizarComprobanteAyudante")
public class AutorizarComprobanteAyudante implements AyudanteWS<RequestAutorizarComprobante> {

	@Autowired
	@Qualifier("facade.autorizarComprobanteFacade")
	private AutorizarComprobanteFacade facade;

	@Override
	public ResponseAutorizarComprobante hacer(ControlTransaccion ctx, RequestAutorizarComprobante datos) {
		ResponseAutorizarComprobante resp = new ResponseAutorizarComprobante();
		Response respuesta = validarDatos(datos);

		if (respuesta != null) {
			resp.cargarError(respuesta);
			return resp;
		}
		
		resp = facade.autorizarComprobante(datos);

		return resp;
	}

	private Response validarDatos(RequestAutorizarComprobante datos) {
		ValidadorWS validar = new ValidadorWS();

		//Validar obligatoriedad
		//validar.validarObligatorio("idTipoDoc", datos.getIdTipoDoc());

		//Validar largo

		//Validar colecciones

		//Validar Formato
		//validar.validarCuitCuil("cuitCuil", datos.getCuitCuil());

		return validar.obtenerRespuesta();
	}	
}
