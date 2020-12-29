package ar.com.coninf.doconline.ws.timer;

import java.util.Timer;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class TimerSql {
	private Logger logger = Logger.getLogger(this.getClass());
	
	private static Timer timer;
	private static TimerTaskSql timerTaskSql;
	private static int countTimerSql = 0;

	@Autowired
	@Qualifier("timer.AutorizadorSql")
	private AutorizadorSql autorizadorSql;

	public TimerSql() {

		countTimerSql++;
	
	}

	public TimerSql(AutorizadorSql aut) {

		countTimerSql++;
		autorizadorSql = aut;
	
	}

	public void instanciar() {

		if (timer==null) {
			timer = new Timer("TimerSQL"+String.valueOf(countTimerSql));					// Instantiate Timer Object
		}
		
		if (timerTaskSql==null) {				
			timerTaskSql = new TimerTaskSql(autorizadorSql);								// Instantiate Timer Object
		}

	}

	public void cancelar() {
		
		logger.info("***");
		if (timerTaskSql!=null) {				
			logger.info("*** Cancelando timerTaskSql *** - " + String.valueOf(timerTaskSql.getCountTimerTaskSql()) + " ***");
			timerTaskSql.cancel();
			timerTaskSql=null;
		} else {
			logger.info("*** timerTaskSql No Instanciada ***");
		}

		logger.info("***");
		if (timer!=null) {
			logger.info("*** Cancelando timer de timerSql *** - " + String.valueOf(countTimerSql) + " ***");
			timer.cancel();
			timer=null;
		} else {
			logger.info("*** timerSql No Instanciado ***");
		}
		logger.info("***");
	
	}
	
	public void programar30() { 
		
		timer.schedule(timerTaskSql, 30000, 1000); 		 					// Create Repetitively task for every n secs
		logger.info("***");
		logger.info("*** Activacion TimerSQL 30 ***");
		logger.info("***");
	
	}

	public void programar01() {

		timer.schedule(timerTaskSql, 1000, 1000); 		 	// Create Repetitively task for every n secs
		logger.info("***");
		logger.info("*** Activacion TimeSQL 01 ***");
		logger.info("***");
	
	}

	public static int getN() { return countTimerSql; }

	public static void setN(int nro) { TimerSql.countTimerSql = nro; }

	public static int getCountTimerSql() {
		return countTimerSql;
	}

}
