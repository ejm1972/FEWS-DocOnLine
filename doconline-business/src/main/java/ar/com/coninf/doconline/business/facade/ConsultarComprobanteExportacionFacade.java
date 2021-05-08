package ar.com.coninf.doconline.business.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ar.com.coninf.doconline.business.negocio.ConsultarComprobanteExportacionBusiness;
import ar.com.coninf.doconline.rest.model.request.RequestConsultarComprobanteExportacion;
import ar.com.coninf.doconline.rest.model.response.ResponseConsultarComprobanteExportacion;

@Component("facade.consultarComprobanteExportacionFacade")
public class ConsultarComprobanteExportacionFacade {

	@Autowired
	@Qualifier("business.consultarComprobanteExportacionBusiness")
	private ConsultarComprobanteExportacionBusiness consultarComprobanteExportacionBusiness;
	
	public ResponseConsultarComprobanteExportacion consultarComprobanteExportacion(RequestConsultarComprobanteExportacion datos) {
		
		return consultarComprobanteExportacionBusiness.consultarComprobanteExportacion(datos);
	}

}
