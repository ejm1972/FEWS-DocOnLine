package ar.com.coninf.doconline.ws.timer;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

public class ConnectionTaskFews {
	private Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private  ScheduledTaskFews scheduledTaskFews; 

	@Autowired
	private ThreadPoolTaskScheduler poolSchedulerFews;
	
	private int countConnectionTaskFews;
	private int countScheduledTaskFews;
	private int countScheduledTaskFewsAnterior;
	
	public ConnectionTaskFews() {
		countConnectionTaskFews = 0;
		countScheduledTaskFews = 0;
		countScheduledTaskFewsAnterior = 0;
	}

    public void run() {

    	countScheduledTaskFews = scheduledTaskFews.getCountScheduledTaskFews();
    	countConnectionTaskFews++;

    	logger.info(" *** ");
    	logger.info(" *** " + poolSchedulerFews.getThreadNamePrefix()  + poolSchedulerFews.getActiveCount() + " *** ");
    	logger.info(" *** connectionTaskFews 60 segundos *** " + countConnectionTaskFews + " *** Anterior: " + countScheduledTaskFewsAnterior + " *** Actual: " + countScheduledTaskFews + " " + new Date());
    	logger.info(" *** ");

//    	if (countScheduledTaskFewsAnterior!=0 && countScheduledTaskFewsAnterior==countScheduledTaskFews) {
//    		logger.info(" *** connectionTaskFews 60 segundos *** " + countConnectionTaskFews + " *** Anterior: " + countScheduledTaskFewsAnterior + " *** Actual: " + countScheduledTaskFews + " " + new Date());
//    		logger.info(" *** ");
//    	}

    	countScheduledTaskFewsAnterior = countScheduledTaskFews;
    }

    public int getCountConnectionTaskFews() {
		return countConnectionTaskFews;
	}

    public int getCountScheduledTaskFews() {
		return countScheduledTaskFews;
	}

    public int getCountScheduledTaskFewsAnterior() {
		return countScheduledTaskFewsAnterior;
	}
    
}
