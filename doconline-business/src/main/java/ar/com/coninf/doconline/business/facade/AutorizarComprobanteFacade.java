package ar.com.coninf.doconline.business.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ar.com.coninf.doconline.business.negocio.AutorizarComprobanteBusiness;
import ar.com.coninf.doconline.rest.model.request.RequestAutorizarComprobante;
import ar.com.coninf.doconline.rest.model.response.ResponseAutorizarComprobante;

@Component("facade.autorizarComprobanteFacade")
public class AutorizarComprobanteFacade {

	@Autowired
	@Qualifier("business.autorizarComprobanteBusiness")
	private AutorizarComprobanteBusiness autorizarComprobanteBusiness;
	
	public ResponseAutorizarComprobante autorizarComprobante(RequestAutorizarComprobante datos) {
		
		return autorizarComprobanteBusiness.autorizarComprobante(datos);
	}

}
