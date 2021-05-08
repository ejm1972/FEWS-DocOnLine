package ar.com.coninf.doconline.ws.ayudante;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ar.com.coninf.doconline.business.facade.ConsultarUltimoComprobanteExportacionFacade;
import ar.com.coninf.doconline.rest.model.request.RequestConsultarUltimoComprobanteExportacion;
import ar.com.coninf.doconline.rest.model.response.Response;
import ar.com.coninf.doconline.rest.model.response.ResponseConsultarUltimoComprobanteExportacion;
import ar.com.coninf.doconline.rest.model.tx.ControlTransaccion;
import ar.com.coninf.doconline.ws.validador.ValidadorWS;

@Component("ayudante.consultarUltimoComprobanteExportacionAyudante")
public class ConsultarUltimoComprobanteExportacionAyudante implements AyudanteWS<RequestConsultarUltimoComprobanteExportacion> {

	@Autowired
	@Qualifier("facade.consultarUltimoComprobanteExportacionFacade")
	private ConsultarUltimoComprobanteExportacionFacade facade;

	@Override
	public ResponseConsultarUltimoComprobanteExportacion hacer(ControlTransaccion ctx, RequestConsultarUltimoComprobanteExportacion datos) {
		ResponseConsultarUltimoComprobanteExportacion resp = new ResponseConsultarUltimoComprobanteExportacion();
		Response respuesta = validarDatos(datos);

		if (respuesta != null) {
			resp.cargarError(respuesta);
			return resp;
		}
		
		resp = facade.consultarUltimoComprobanteExportacion(datos);

		return resp;
	}

	private Response validarDatos(RequestConsultarUltimoComprobanteExportacion datos) {
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
