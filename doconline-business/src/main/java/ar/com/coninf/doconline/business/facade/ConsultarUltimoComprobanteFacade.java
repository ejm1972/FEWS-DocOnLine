package ar.com.coninf.doconline.business.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ar.com.coninf.doconline.business.negocio.ConsultarUltimoComprobanteBusiness;
import ar.com.coninf.doconline.rest.model.request.RequestConsultarUltimoComprobante;
import ar.com.coninf.doconline.rest.model.response.ResponseConsultarUltimoComprobante;

@Component("facade.consultarUltimoComprobanteFacade")
public class ConsultarUltimoComprobanteFacade {

	@Autowired
	@Qualifier("business.consultarUltimoComprobanteBusiness")
	private ConsultarUltimoComprobanteBusiness consultarUltimoComprobanteBusiness;
	
	public ResponseConsultarUltimoComprobante consultarUltimoComprobante(RequestConsultarUltimoComprobante datos) {
		
		return consultarUltimoComprobanteBusiness.consultarUltimoComprobante(datos);
	}

}
