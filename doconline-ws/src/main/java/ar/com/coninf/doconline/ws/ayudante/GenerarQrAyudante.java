package ar.com.coninf.doconline.ws.ayudante;

import java.io.IOException;

import org.apache.commons.codec.EncoderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.google.zxing.WriterException;

import ar.com.coninf.doconline.business.facade.GenerarQrFacade;
import ar.com.coninf.doconline.rest.model.request.RequestGenerarQr;
import ar.com.coninf.doconline.rest.model.response.Response;
import ar.com.coninf.doconline.rest.model.response.ResponseGenerarQr;
import ar.com.coninf.doconline.rest.model.tx.ControlTransaccion;
import ar.com.coninf.doconline.ws.validador.ValidadorWS;

@Component("ayudante.generarQrAyudante")
public class GenerarQrAyudante implements AyudanteWS<RequestGenerarQr> {

	@Autowired
	@Qualifier("facade.generarQrFacade")
	private GenerarQrFacade facade;

	@Override
	public ResponseGenerarQr hacer(ControlTransaccion ctx, RequestGenerarQr datos) throws IOException, WriterException, EncoderException {
		ResponseGenerarQr resp = new ResponseGenerarQr();
		Response respuesta = validarDatos(datos);

		if (respuesta != null) {
			resp.cargarError(respuesta);
			return resp;
		}
		
		resp = facade.generarQr(datos);

		return resp;
	}

	private Response validarDatos(RequestGenerarQr datos) {
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
