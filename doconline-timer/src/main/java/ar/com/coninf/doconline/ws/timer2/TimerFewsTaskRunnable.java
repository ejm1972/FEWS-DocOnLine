//package ar.com.coninf.doconline.ws.timer2;
//
//import java.util.Date;
//
//import org.apache.log4j.Logger;
//import org.springframework.stereotype.Component;
//
//import ar.com.coninf.doconline.shared.excepcion.ApplicationException;
//
//@Component(value = "timer.TimerFewsTaskRunnable2")
//public class TimerFewsTaskRunnable implements Runnable {
//	private Logger logger = Logger.getLogger(this.getClass());
//
//	private int countTimerFewsTask;
//	private AutorizadorSql autorizadorSql;
//	
//	public TimerFewsTaskRunnable() {
//
//		countTimerFewsTask = 0;
//	
//	}
//
//	public TimerFewsTaskRunnable(AutorizadorSql autorizador) {
//		countTimerFewsTask = 0;
//		autorizadorSql = autorizador;
//	}
//	
//	@Override
//	public void run() {
//		
//		ApplicationException applicationException = null;
//		
//		try {
//						
//			logger.debug("***");
//			logger.debug("Memoria - "+ new Date() + " : " );
//			Runtime rt= Runtime.getRuntime();
//			logger.debug("Memoria: "+rt.freeMemory()+" libre,");
//			logger.debug("Memoria: "+rt.totalMemory() + " total");
//			logger.debug("***");
//			
//			countTimerFewsTask++;
//			
//			logger.info("***");
//			logger.info("*** Ini TimerFewsTask *** " + new Date() + " - " + countTimerFewsTask);
//			logger.info("***");
//
//			autorizadorSql.procesarPendientes();
//			logger.debug("Fin Ejecucion procesarPendientes()");
//			
//			autorizadorSql.logTimerSql();
//			logger.debug("Fin Ejecucion logTimerSql()");
//			
//			logger.debug("***");
//			logger.debug("*** Fin TimerFewsTask *** - " + countTimerFewsTask);
//			logger.debug("***");
//
//		} catch (ApplicationException e) {
//			applicationException = e;
//		} catch (Exception e) {
//			applicationException = new ApplicationException(e);
//		} finally {
//			if (applicationException!=null) {
//				logger.error("***");
//				logger.error(applicationException.toString());
//				logger.error(applicationException.getMessage());
//				logger.error(applicationException.getCause()!=null ? applicationException.getCause().toString() : applicationException.toString());
//				logger.error("***");
//				logger.error("*** Fin TimerFewsTask de applicationException!=null *** - " + countTimerFewsTask);
//				logger.error("***");
//			}
//		}
//
//	}
//	
//	public int getCountTimerFewsTask() {
//		return countTimerFewsTask;
//	}
//
//}
