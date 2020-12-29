//package ar.com.coninf.doconline.ws.timer2;
//
//import java.util.Date;
//import java.util.TimerTask;
//
//import org.apache.log4j.Logger;
//
//import ar.com.coninf.doconline.shared.excepcion.ApplicationException;
//
//public class TimerTaskSql extends TimerTask {
//	private Logger logger = Logger.getLogger(this.getClass());
//
//	private int countTimerTaskSql;
//	private AutorizadorSql autorizadorSql;
//	
//	public TimerTaskSql(AutorizadorSql autorizador) {
//		countTimerTaskSql = 0;
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
//			countTimerTaskSql++;
//			
//			logger.info("***");
//			logger.info("*** Ini TimerTaskSql *** " + new Date() + " - " + countTimerTaskSql);
//			logger.info("***");
//
//			autorizadorSql.procesarPendientes();
//			logger.debug("Fin Ejecucion procesarPendientes()");
//			
//			autorizadorSql.logTimerSql();
//			logger.debug("Fin Ejecucion logTimerSql()");
//			
//			logger.info("***");
//			logger.info("*** Fin TimerTaskSql *** - " + countTimerTaskSql);
//			logger.info("***");
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
//				logger.error("*** Fin TimerTaskSql *** - " + countTimerTaskSql);
//				logger.error("***");
//			}
//		}
//
//	}
//	
//	public int getCountTimerTaskSql() {
//		return countTimerTaskSql;
//	}
//
//}
