//package ar.com.coninf.doconline.ws.timer2;
//
//import java.util.Timer;
//
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//
//import ar.com.coninf.doconline.model.dao.LogTimerDao;
//
//public class TimerControl {
//	private Logger logger = Logger.getLogger(this.getClass());
//	
//	private static Timer timer;
//	private static TimerTaskControl timerTaskControl;
//	private static int countTimerControl = 0;
//
//	@Autowired
//	@Qualifier("logTimerDao")
//	private LogTimerDao logTimerDao;
//	
//	@Autowired
//	@Qualifier("timer.AutorizadorSql")
//	private AutorizadorSql autorizadorSql;
//	
//	public TimerControl() {
//
//		countTimerControl++;
//	
//	}
//
//	public TimerControl(LogTimerDao logDao, AutorizadorSql autDao) {
//
//		countTimerControl++;
//		logTimerDao = logDao;
//		autorizadorSql = autDao;
//
//	}
//
//	public void instanciar() {
//
//		if (timer==null) {
//			timer = new Timer("TimerControl"+String.valueOf(countTimerControl));						// Instantiate Timer Object
//		}
//		
//		if (timerTaskControl==null) {
//			timerTaskControl = new TimerTaskControl(autorizadorSql, logTimerDao);						// Instantiate Timer Object
//		}
//
//	}
//
//	public void cancelar() {
//	
//		logger.info("***");
//		logger.info("*** Cancelando timerTaskControl ***");
//		timerTaskControl.cancel();
//		timerTaskControl=null;
//		logger.info("***");
//		logger.info("*** Cancelando timer de timerControl ***");
//		timer.cancel();
//		timer=null;
//		logger.info("***");
//	
//	}
//
//	public void programar60() { 
//		
//		timer.schedule(timerTaskControl, 60000, 60000); 		 	// Create Repetitively task for every n secs
//		logger.info("***");
//		logger.info("*** Activacion TimerControl 60 ***");
//		logger.info("***");
//		
//	}
//
//	public void programar01() { 
//		
//		timer.schedule(timerTaskControl, 1000, 60000); 		 	// Create Repetitively task for every n secs
//		logger.info("***");
//		logger.info("*** Activacion TimerControl 01 ***");
//		logger.info("***");
//		
//	}
//
//	public static int getN() { return countTimerControl; }
//
//	public static void setN(int nro) { TimerControl.countTimerControl = nro; }
//	
//}
