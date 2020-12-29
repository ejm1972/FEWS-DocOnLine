package ar.com.coninf.doconline.business.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ar.com.coninf.doconline.business.log.LogTransaccionContenido;
import ar.com.coninf.doconline.business.negocio.LogTransaccionBusiness;

@Component("facade.logTransaccionFacade")
public class LogTransaccionFacade {

	@Autowired
	@Qualifier("business.logTransaccionBusiness")
	private LogTransaccionBusiness logTransaccionBusiness;
	
	public void registrarAuditoria(LogTransaccionContenido log){
		logTransaccionBusiness.registrarAuditoria(log);
	}
}