package ar.com.coninf.doconline.ws.timer;

import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import ar.com.coninf.doconline.shared.excepcion.ApplicationException;

public class ScheduledTaskFews {
	private Logger logger = Logger.getLogger(this.getClass());

	private int countScheduledTaskFews;

	@Autowired
	ThreadPoolTaskScheduler poolSchedulerFews;
	
	@Autowired
	@Qualifier("dolProperties")
	protected Properties dolProperties;

	@Autowired
	@Qualifier("timer.AutorizadorFews")
	private AutorizadorFews autorizadorFews;

	public ScheduledTaskFews() {
		countScheduledTaskFews = 0;
	}

	public ScheduledTaskFews(AutorizadorFews autorizador) {
		countScheduledTaskFews = 0;
		autorizadorFews = autorizador;
	}

	public void run() {

		String ejecuta2001 = dolProperties.getProperty("interfaz_2001");
		String ejecuta2006 = dolProperties.getProperty("interfaz_2006");
		String ejecuta3001 = dolProperties.getProperty("interfaz_3001");
		String ejecuta9901 = dolProperties.getProperty("interfaz_9901");

		if (ejecuta2001==null && ejecuta2006==null && ejecuta3001==null && ejecuta9901==null) {

			logger.info("***");
			logger.info("*** scheduledTaskFews " + poolSchedulerFews.getThreadNamePrefix() + "* shutdown *** " + new Date() + " - " + countScheduledTaskFews);
			logger.info("***");

			poolSchedulerFews.shutdown();

			poolSchedulerFews.destroy();

		} else {

			ApplicationException applicationException = null;

			try {
				countScheduledTaskFews++;

				logger.info("***");
				logger.info("*** Ini scheduledTaskFews *** " + new Date() + " - " + countScheduledTaskFews);
				logger.info("***");

				autorizadorFews.procesarPendientes();
				logger.info("Fin Ejecucion procesarPendientes()");

				autorizadorFews.logTimerSql();
				logger.info("Fin Ejecucion logTimerSql()");

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
				}

			}

			logger.info("***");
			logger.info("*** Fin scheduledTaskFews *** " + new Date() + " - " + countScheduledTaskFews);
			logger.info("***");

		}

	}

	public int getCountScheduledTaskFews() {
		return countScheduledTaskFews;
	}

}
