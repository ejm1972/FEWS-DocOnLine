package ar.com.coninf.doconline.ws.ayudante;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ar.com.coninf.doconline.business.facade.AutorizarComprobanteExportacionFacade;
import ar.com.coninf.doconline.rest.model.request.RequestAutorizarComprobanteExportacion;
import ar.com.coninf.doconline.rest.model.response.Response;
import ar.com.coninf.doconline.rest.model.response.ResponseAutorizarComprobanteExportacion;
import ar.com.coninf.doconline.rest.model.tx.ControlTransaccion;
import ar.com.coninf.doconline.ws.validador.ValidadorWS;

@Component("ayudante.autorizarComprobanteExportacionAyudante")
public class AutorizarComprobanteExportacionAyudante implements AyudanteWS<RequestAutorizarComprobanteExportacion> {

	@Autowired
	@Qualifier("facade.autorizarComprobanteExportacionFacade")
	private AutorizarComprobanteExportacionFacade facade;

	@Override
	public ResponseAutorizarComprobanteExportacion hacer(ControlTransaccion ctx, RequestAutorizarComprobanteExportacion datos) {
		ResponseAutorizarComprobanteExportacion resp = new ResponseAutorizarComprobanteExportacion();
		Response respuesta = validarDatos(datos);

		if (respuesta != null) {
			resp.cargarError(respuesta);
			return resp;
		}
		
		resp = facade.autorizarComprobanteExportacion(datos);

		return resp;
	}

	private Response validarDatos(RequestAutorizarComprobanteExportacion datos) {
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
