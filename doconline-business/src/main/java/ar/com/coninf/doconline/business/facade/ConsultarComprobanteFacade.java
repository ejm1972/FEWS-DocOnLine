package ar.com.coninf.doconline.business.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ar.com.coninf.doconline.business.negocio.ConsultarComprobanteBusiness;
import ar.com.coninf.doconline.rest.model.request.RequestConsultarComprobante;
import ar.com.coninf.doconline.rest.model.response.ResponseConsultarComprobante;

@Component("facade.consultarComprobanteFacade")
public class ConsultarComprobanteFacade {

	@Autowired
	@Qualifier("business.consultarComprobanteBusiness")
	private ConsultarComprobanteBusiness consultarComprobanteBusiness;
	
	public ResponseConsultarComprobante consultarComprobante(RequestConsultarComprobante datos) {
		
		return consultarComprobanteBusiness.consultarComprobante(datos);
	}

}
