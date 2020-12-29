package ar.com.coninf.doconline.ws.ayudante;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ar.com.coninf.doconline.business.facade.AutenticacionFacade;
import ar.com.coninf.doconline.rest.model.enums.ErrorEnum;
import ar.com.coninf.doconline.rest.model.response.Response;
import ar.com.coninf.doconline.rest.model.response.ResponseAutenticacion;
import ar.com.coninf.doconline.rest.model.tx.ControlTransaccion;
import ar.com.coninf.doconline.ws.validador.ValidadorWS;

@Component("ayudante.autenticacionAyudante")
public class AutenticacionAyudante implements AyudanteWS<AutenticacionDatos> {

	@Autowired
	@Qualifier("facade.autenticacionFacade")
	private AutenticacionFacade facade;

	public AutenticacionAyudante() {
		
	}
	
	@Override
	public ResponseAutenticacion hacer(ControlTransaccion ctx, AutenticacionDatos datos) {
		Response respuestaWS;
		ResponseAutenticacion resp = new ResponseAutenticacion();
		resp.cargarError(new Response(ErrorEnum.SIN_ERROR));
		if(datos instanceof SesionDatos){
			SesionDatos datosSesion = (SesionDatos)datos;
			respuestaWS = validarCierreSesion(datosSesion);
			if (respuestaWS != null) {
				resp.cargarError(respuestaWS);
				return resp;
			}
			facade.cerrarSesion(datosSesion.getInterfaz(), datosSesion.getIdSesion());
		}else if(datos instanceof AutenticacionDatos){
			respuestaWS = validarIncioSesion(datos);
			if (respuestaWS != null) {
				resp.cargarError(respuestaWS);
				return resp;
			}
			String idSesion = facade.iniciarSesion(datos.getInterfaz(), datos.getClave());
			resp.setIdSesion(idSesion);
		}else{
			resp.cargarError(new Response(ErrorEnum.ERROR_INESPERADO));
		}
		return resp;
	}

	private Response validarIncioSesion(AutenticacionDatos datos){
		ValidadorWS validar = new ValidadorWS();
		validar.validarObligatorio("interfaz", datos.getInterfaz());
		validar.validarObligatorio("clave", datos.getClave());
		return validar.obtenerRespuesta();
	}
	
	private Response validarCierreSesion(SesionDatos datos){
		ValidadorWS validar = new ValidadorWS();
		validar.validarObligatorio("interfaz", datos.getInterfaz());
		validar.validarObligatorio("idSesion", datos.getIdSesion());
		return validar.obtenerRespuesta();
	}
}
