package ar.com.coninf.doconline.ws.timer;

import java.util.Date;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import ar.com.coninf.doconline.model.dao.LogTimerDao;
import ar.com.coninf.doconline.shared.dto.LogTimer;
import ar.com.coninf.doconline.shared.excepcion.ApplicationException;

public class TimerTaskControl extends TimerTask {
	private Logger logger = Logger.getLogger(this.getClass());

	private int countTimerTaskControl;
	private LogTimerDao logTimerDao;
	private AutorizadorSql autorizadorSql;
	
	public TimerTaskControl(AutorizadorSql autorizador, LogTimerDao logTimer) {
		countTimerTaskControl = 0;
		autorizadorSql = autorizador;
		logTimerDao = logTimer;
	}

	@Override
	public void run() {
		
		boolean lanzarTimerSql = false;
		ApplicationException applicationException = null;
		
		try {
			
			logger.debug("***");
			logger.debug("Memoria - "+ new Date() + " : " );
			Runtime rt= Runtime.getRuntime();
			logger.debug("Memoria: "+rt.freeMemory()+" libre,");
			logger.debug("Memoria: "+rt.totalMemory() + " total");
			logger.debug("***");
		
			countTimerTaskControl++;

			logger.info("***");
			logger.info("*** Ini TimerTaskControl *** " + new Date() + " - " + countTimerTaskControl);
			logger.info("***");
				
			LogTimer logTimer = new LogTimer();
			logTimer.setObjetoInstanciado("TimerSql");
			LogTimer resultado = logTimerDao.getByKey(logTimer);
			
			if (resultado==null) {
				lanzarTimerSql = true;
			} else {
			
				Date dateActual = new Date();
				Date dateLog = resultado.getFechaEjecucion();
				Long diferencia = dateActual.getTime() - dateLog.getTime();
					
				if (diferencia>180000) {
					lanzarTimerSql = true;
				}
			}
					
			if (lanzarTimerSql) {
				TimerSql timerSql = new TimerSql(autorizadorSql);
				logger.info("-----------------------------------");
				logger.info(new Date());
				logger.info("-----------------------------------");
				logger.info(timerSql);
				logger.info("-----------------------------------");
				timerSql.cancelar();
				timerSql.instanciar();
				timerSql.programar30();		
			}
		
			logger.info("***");
			logger.info("*** Fin TimerTaskControl *** " + new Date() + " - " + countTimerTaskControl);
			logger.info("***");

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
				logger.error("*** Fin TimerTaskControl *** " + new Date() + " - " + countTimerTaskControl);
				logger.error("***");
			}

		}

	}
	
	public int getCountTimerTaskControl() {
		return countTimerTaskControl;
	}

}
