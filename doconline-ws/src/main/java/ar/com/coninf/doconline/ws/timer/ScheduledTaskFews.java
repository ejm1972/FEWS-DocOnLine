package ar.com.coninf.doconline.ws.timer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import ar.com.coninf.doconline.shared.excepcion.ApplicationException;

public class ScheduledTaskFews {
	private Logger logger = Logger.getLogger(this.getClass());

	private int countScheduledTaskFews;
	
	private List<Long> interfaces;

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

		String ejecuta9901 = dolProperties.getProperty("interfaz_9901");
		String ejecuta9902 = dolProperties.getProperty("interfaz_9902");
		String ejecuta2001 = dolProperties.getProperty("interfaz_2001");
		String ejecuta2006 = dolProperties.getProperty("interfaz_2006");
		String ejecuta4001 = dolProperties.getProperty("interfaz_4001");
		String ejecuta5001 = dolProperties.getProperty("interfaz_5001");
		String ejecuta5002 = dolProperties.getProperty("interfaz_5002");
		String ejecuta6001 = dolProperties.getProperty("interfaz_6001");
		String ejecuta6002 = dolProperties.getProperty("interfaz_6002");
		String ejecuta7001 = dolProperties.getProperty("interfaz_7001");
		
		if (ejecuta9901==null && ejecuta9902==null && ejecuta2001==null && ejecuta2006==null && ejecuta4001==null && ejecuta5001==null && ejecuta5002==null && ejecuta6001==null && ejecuta6002==null && ejecuta7001==null) {

			logger.info("***");
			logger.info("*** scheduledTaskFews " + poolSchedulerFews.getThreadNamePrefix() + poolSchedulerFews.getActiveCount() + " shutdown *** " + new Date() + " - " + countScheduledTaskFews);
			logger.info("***");

			poolSchedulerFews.shutdown();

			poolSchedulerFews.destroy();

		} else {
			
			if (interfaces==null) {
				interfaces = new ArrayList<>();
				if (ejecuta9901!=null)
					interfaces.add(9901L);
				if (ejecuta9902!=null)
					interfaces.add(9902L);
				if (ejecuta2001!=null)
					interfaces.add(2001L);
				if (ejecuta2006!=null)
					interfaces.add(2006L);
				if (ejecuta4001!=null)
					interfaces.add(4001L);
				if (ejecuta5001!=null)
					interfaces.add(5001L);
				if (ejecuta5002!=null)
					interfaces.add(5002L);
				if (ejecuta6001!=null)
					interfaces.add(6001L);
				if (ejecuta6002!=null)
					interfaces.add(6002L);
				if (ejecuta7001!=null)
					interfaces.add(7001L);
			}

			ApplicationException applicationException = null;

			try {
				countScheduledTaskFews++;

				logger.info("***");
				logger.info("*** Ini scheduledTaskFews " + poolSchedulerFews.getThreadNamePrefix() +  + poolSchedulerFews.getActiveCount() + " *** " + new Date() + " - " + countScheduledTaskFews);
				logger.info("***");

				autorizadorFews.procesarPendientes(interfaces);
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
			logger.info("*** Fin scheduledTaskFews " + poolSchedulerFews.getThreadNamePrefix()  + poolSchedulerFews.getActiveCount() + " *** " + new Date() + " - " + countScheduledTaskFews);
			logger.info("***");

		}

	}

	public int getCountScheduledTaskFews() {
		return countScheduledTaskFews;
	}

}
