package ar.com.coninf.doconline.business.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ar.com.coninf.doconline.business.negocio.AutenticacionBusiness;

@Component("facade.autenticacionFacade")
public class AutenticacionFacade {
	
	@Autowired
	@Qualifier("business.autenticacionBusiness")
	private AutenticacionBusiness autenticacionBusiness;

	public String iniciarSesion(Integer interfazId, String clave){
		return autenticacionBusiness.iniciarSesion(interfazId, clave);
	}
	
	public void cerrarSesion(Integer interfazId, String idSesion){
		autenticacionBusiness.cerrarSesion(interfazId, idSesion);
	}
	
	public Boolean validarSesion(Integer interfazId, String idSesion){
		return autenticacionBusiness.validarSesion(interfazId, idSesion);
	}
}
