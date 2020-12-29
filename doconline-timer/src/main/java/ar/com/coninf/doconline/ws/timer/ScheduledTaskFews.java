package ar.com.coninf.doconline.ws.timer;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import ar.com.coninf.doconline.shared.excepcion.ApplicationException;

public class ScheduledTaskFews {
	private Logger logger = Logger.getLogger(this.getClass());

	private int countScheduledTaskFews;

	@Autowired
	@Qualifier("timer.AutorizadorSql")
	private AutorizadorSql autorizadorSql;
	
	public ScheduledTaskFews() {
		countScheduledTaskFews = 0;
	}

	public ScheduledTaskFews(AutorizadorSql autorizador) {
		countScheduledTaskFews = 0;
		autorizadorSql = autorizador;
	}
	
	public void run() {
		
		ApplicationException applicationException = null;
		
		try {
						
			logger.debug("***");
			logger.debug("Memoria - "+ new Date() + " : " );
			Runtime rt= Runtime.getRuntime();
			logger.debug("Memoria: "+rt.freeMemory()+" libre,");
			logger.debug("Memoria: "+rt.totalMemory() + " total");
			logger.debug("***");
			
			countScheduledTaskFews++;
			
			logger.info("***");
			logger.info("*** Ini ScheduledTaskFews *** " + new Date() + " - " + countScheduledTaskFews);
			logger.info("***");

			autorizadorSql.procesarPendientes();
			logger.debug("Fin Ejecucion procesarPendientes()");
			
			autorizadorSql.logTimerSql();
			logger.debug("Fin Ejecucion logTimerSql()");
			
			logger.debug("***");
			logger.debug("*** Fin ScheduledTaskFews *** - " + countScheduledTaskFews);
			logger.debug("***");

		} catch (ApplicationException e) {
			applicationException = e;
		} catch (Exception e) {
			applicationException = new ApplicationException(e);
		} finally {
			if (applicationException!=null) {
				logger.error("***");
				logger.error(applicationException.toString());
				logger.error(applicationException.getMessage());
				logger.error(applicationException.getCause()!=null ? applicationException.getCause().toString() : applicationException.toString());
				logger.error("***");
				logger.error("*** Fin ScheduledTaskFews de applicationException!=null *** - " + countScheduledTaskFews);
				logger.error("***");
			}
		}

	}
	
	public int getCountScheduledTaskFews() {
		return countScheduledTaskFews;
	}

}
