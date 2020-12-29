package ar.com.coninf.doconline.ws.ayudante;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ar.com.coninf.doconline.business.facade.ConsultarComprobanteFacade;
import ar.com.coninf.doconline.rest.model.request.RequestConsultarComprobante;
import ar.com.coninf.doconline.rest.model.response.Response;
import ar.com.coninf.doconline.rest.model.response.ResponseConsultarComprobante;
import ar.com.coninf.doconline.rest.model.tx.ControlTransaccion;
import ar.com.coninf.doconline.ws.validador.ValidadorWS;

@Component("ayudante.consultarComprobanteAyudante")
public class ConsultarComprobanteAyudante implements AyudanteWS<RequestConsultarComprobante> {

	@Autowired
	@Qualifier("facade.consultarComprobanteFacade")
	private ConsultarComprobanteFacade facade;

	@Override
	public ResponseConsultarComprobante hacer(ControlTransaccion ctx, RequestConsultarComprobante datos) {
		ResponseConsultarComprobante resp = new ResponseConsultarComprobante();
		Response respuesta = validarDatos(datos);

		if (respuesta != null) {
			resp.cargarError(respuesta);
			return resp;
		}
		
		resp = facade.consultarComprobante(datos);

		return resp;
	}

	private Response validarDatos(RequestConsultarComprobante datos) {
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
