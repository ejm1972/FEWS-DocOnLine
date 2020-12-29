package ar.com.coninf.doconline.ws.ayudante;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ar.com.coninf.doconline.business.facade.ConsultarUltimoComprobanteFacade;
import ar.com.coninf.doconline.rest.model.request.RequestConsultarUltimoComprobante;
import ar.com.coninf.doconline.rest.model.response.Response;
import ar.com.coninf.doconline.rest.model.response.ResponseConsultarUltimoComprobante;
import ar.com.coninf.doconline.rest.model.tx.ControlTransaccion;
import ar.com.coninf.doconline.ws.validador.ValidadorWS;

@Component("ayudante.consultarUltimoComprobanteAyudante")
public class ConsultarUltimoComprobanteAyudante implements AyudanteWS<RequestConsultarUltimoComprobante> {

	@Autowired
	@Qualifier("facade.consultarUltimoComprobanteFacade")
	private ConsultarUltimoComprobanteFacade facade;

	@Override
	public ResponseConsultarUltimoComprobante hacer(ControlTransaccion ctx, RequestConsultarUltimoComprobante datos) {
		ResponseConsultarUltimoComprobante resp = new ResponseConsultarUltimoComprobante();
		Response respuesta = validarDatos(datos);

		if (respuesta != null) {
			resp.cargarError(respuesta);
			return resp;
		}
		
		resp = facade.consultarUltimoComprobante(datos);

		return resp;
	}

	private Response validarDatos(RequestConsultarUltimoComprobante datos) {
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
