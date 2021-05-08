package ar.com.coninf.doconline.ws.ayudante;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ar.com.coninf.doconline.business.facade.ConsultarComprobanteExportacionFacade;
import ar.com.coninf.doconline.rest.model.request.RequestConsultarComprobanteExportacion;
import ar.com.coninf.doconline.rest.model.response.Response;
import ar.com.coninf.doconline.rest.model.response.ResponseConsultarComprobanteExportacion;
import ar.com.coninf.doconline.rest.model.tx.ControlTransaccion;
import ar.com.coninf.doconline.ws.validador.ValidadorWS;

@Component("ayudante.consultarComprobanteExportacionAyudante")
public class ConsultarComprobanteAyudanteExportacion implements AyudanteWS<RequestConsultarComprobanteExportacion> {

	@Autowired
	@Qualifier("facade.consultarComprobanteExportacionFacade")
	private ConsultarComprobanteExportacionFacade facade;

	@Override
	public ResponseConsultarComprobanteExportacion hacer(ControlTransaccion ctx, RequestConsultarComprobanteExportacion datos) {
		ResponseConsultarComprobanteExportacion resp = new ResponseConsultarComprobanteExportacion();
		Response respuesta = validarDatos(datos);

		if (respuesta != null) {
			resp.cargarError(respuesta);
			return resp;
		}
		
		resp = facade.consultarComprobanteExportacion(datos);

		return resp;
	}

	private Response validarDatos(RequestConsultarComprobanteExportacion datos) {
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
