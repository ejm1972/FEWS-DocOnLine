//package ar.com.coninf.doconline.ws.timer2;
//
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.ScheduledFuture;
//import java.util.concurrent.TimeUnit;
//
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Component;
//
//@Component(value = "timer.TimerFews2")
//public class TimerFews {
//	private Logger logger = Logger.getLogger(this.getClass());
//	
//	private static ScheduledExecutorService timerFewsExecutorService;
//	private static ScheduledFuture<?> timerFewsScheduledFuture;
//	private static TimerFewsTaskRunnable timerFewsTask;
//	private static int countTimerFews = 0;
//
//	@Autowired
//	@Qualifier("timer.AutorizadorSql")
//	private AutorizadorSql autorizadorSql;
//	
//	public TimerFews() {
//
//		countTimerFews++;
//	
//	}
//
//	public TimerFews(AutorizadorSql aut) {
//
//		countTimerFews++;
//		autorizadorSql = aut;
//	
//	}
//
//	public void instanciar() {
//
//		if (timerFewsExecutorService==null) {
//			timerFewsExecutorService = Executors.newScheduledThreadPool(10);						// Instantiate Timer Object
//		}
//		
//		if (timerFewsTask==null) {				
//			timerFewsTask = new TimerFewsTaskRunnable(autorizadorSql);			   					// Instantiate Timer Object
//		}
//
//	}
//
//	public void cancelar() {
//		
//		logger.info("***");
//		if (timerFewsTask!=null) {				
//			logger.info("*** Cancelando timerFewsTask *** - " + String.valueOf(timerFewsTask.getCountTimerFewsTask()) + " ***");
//		} else {
//			logger.info("*** timerFewsTask No Instanciada ***");
//		}
//
//		logger.info("***");
//		if (timerFewsScheduledFuture!=null) {
//			logger.info("*** Cancelando timerFewsScheduledFuture *** - " + String.valueOf(countTimerFews) + " ***");
//			timerFewsScheduledFuture.cancel(true);
//			timerFewsScheduledFuture = null;
//		} else {
//			logger.info("*** timerFewsScheduledFuture No Instanciado ***");
//		}
//				
//		logger.info("***");
//		if (timerFewsExecutorService!=null) {
//			logger.info("*** Cancelando timerFewsExecutorService *** - " + String.valueOf(countTimerFews) + " ***");
//			timerFewsExecutorService.shutdown();
//			timerFewsExecutorService = null;
//		} else {
//			logger.info("*** timerFewsExecutorService No Instanciado ***");
//		}
//		logger.info("***");
//	
//	}
//
//	public void programar30() { 
//			
//			//timerFewsScheduledFuture = timerFewsExecutorService.scheduleAtFixedRate(timerFewsTask, 30, 1, TimeUnit.SECONDS);
//			timerFewsScheduledFuture = timerFewsExecutorService.scheduleWithFixedDelay(timerFewsTask, 30, 1, TimeUnit.SECONDS);
//			logger.info("***");
//			logger.info("*** Activacion timerFewsScheduledFuture de timerFewsExecutorService a 30s ***");
//			logger.info("***");
//		
//	}
//
//	public void programar01() {
//
//		//timerFewsScheduledFuture = timerFewsExecutorService.scheduleAtFixedRate(timerFewsTask, 1, 1, TimeUnit.SECONDS);
//		timerFewsScheduledFuture = timerFewsExecutorService.scheduleWithFixedDelay(timerFewsTask, 1, 1, TimeUnit.SECONDS);
//		logger.info("***");
//		logger.info("*** Activacion timerFewsScheduledFuture de timerFewsExecutorService a 1s ***");
//		logger.info("***");
//	
//	}
//
//	public static int getCountTimerFews() {
//		return countTimerFews;
//	}
//
//}
