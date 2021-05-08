package ar.com.coninf.doconline.business.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ar.com.coninf.doconline.business.negocio.AutorizarComprobanteExportacionBusiness;
import ar.com.coninf.doconline.rest.model.request.RequestAutorizarComprobanteExportacion;
import ar.com.coninf.doconline.rest.model.response.ResponseAutorizarComprobanteExportacion;

@Component("facade.autorizarComprobanteExportacionFacade")
public class AutorizarComprobanteExportacionFacade {

	@Autowired
	@Qualifier("business.autorizarComprobanteExportacionBusiness")
	private AutorizarComprobanteExportacionBusiness autorizarComprobanteExportacionBusiness;
	
	public ResponseAutorizarComprobanteExportacion autorizarComprobanteExportacion(RequestAutorizarComprobanteExportacion datos) {
		
		return autorizarComprobanteExportacionBusiness.autorizarComprobanteExportacion(datos);
	}

}
