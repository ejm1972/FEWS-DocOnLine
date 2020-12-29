package ar.com.coninf.doconline.ws.impl;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ar.com.coninf.doconline.rest.model.enums.ErrorEnum;
import ar.com.coninf.doconline.rest.model.response.Response;
import ar.com.coninf.doconline.ws.DocOnlineServicioWeb;
import ar.com.coninf.doconline.ws.timer.AutorizadorSql;

@Service(value = "webservice.DocOnlineServicioWeb")
public class DocOnlineServicioWebImpl implements DocOnlineServicioWeb {

	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	@Qualifier("timer.AutorizadorSql")
	private AutorizadorSql autorizadorSql;

	@Override
	public Response scheduleTimerSql(String mensaje) {
		
		//TimerSql timerSql = new TimerSql(autorizadorSql);
		
		logger.info("-----------------------------------");
		logger.info(mensaje + " " + new Date());
		logger.info("-----------------------------------");
		//logger.info(timerSql);
		logger.info("-----------------------------------");
		//timerSql.cancelar();
		//timerSql.instanciar();
		//timerSql.programar01();		
		
		return new Response(ErrorEnum.SIN_ERROR);
	}

}
