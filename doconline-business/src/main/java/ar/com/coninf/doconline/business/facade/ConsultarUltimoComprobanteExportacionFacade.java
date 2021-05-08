package ar.com.coninf.doconline.business.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ar.com.coninf.doconline.business.negocio.ConsultarUltimoComprobanteExportacionBusiness;
import ar.com.coninf.doconline.rest.model.request.RequestConsultarUltimoComprobanteExportacion;
import ar.com.coninf.doconline.rest.model.response.ResponseConsultarUltimoComprobanteExportacion;

@Component("facade.consultarUltimoComprobanteExportacionFacade")
public class ConsultarUltimoComprobanteExportacionFacade {

	@Autowired
	@Qualifier("business.consultarUltimoComprobanteExportacionBusiness")
	private ConsultarUltimoComprobanteExportacionBusiness consultarUltimoComprobanteExportacionBusiness;
	
	public ResponseConsultarUltimoComprobanteExportacion consultarUltimoComprobanteExportacion(RequestConsultarUltimoComprobanteExportacion datos) {
		
		return consultarUltimoComprobanteExportacionBusiness.consultarUltimoComprobanteExportacion(datos);
	}

}
