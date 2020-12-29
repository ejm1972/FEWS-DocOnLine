//package ar.com.coninf.doconline.ws.timer2;
//
//import java.sql.SQLException;
//import java.util.Date;
//import java.util.concurrent.ScheduledFuture;
//
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.scheduling.TaskScheduler;
//import org.springframework.scheduling.annotation.SchedulingConfigurer;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
//import org.springframework.scheduling.config.ScheduledTaskRegistrar;
//
//public class ConnectionTaskFews implements SchedulingConfigurer {
//
//	private Logger logger = Logger.getLogger(this.getClass());
//
//	@Autowired
//	private  ScheduledTaskFews scheduledTaskFews; 
//
//	@Autowired
//	private ThreadPoolTaskScheduler poolSchedulerFews;
//	
//	@Autowired
//	private JdbcTemplate jdbcTemplate;
//
//	private ScheduledTaskRegistrar scheduledTaskRegistrar;
//	private ScheduledFuture<?> connectionFutureFews;
//	private int countConnectionTaskFews;
//	private int countScheduledTaskFews;
//	private int countScheduledTaskFewsAnterior;
//	
//	private ConnectionFutureFewsTask connectionFutureFewsTask = new ConnectionFutureFewsTask();
//	
//	public ConnectionTaskFews() {
//		countConnectionTaskFews = 0;
//		countScheduledTaskFews = 0;
//		countScheduledTaskFewsAnterior = 0;
//	}
//
//    public void cancelFuture(boolean mayInterruptIfRunning, ScheduledFuture<?> future) {
//        logger.info("Cancelling a future " + new Date());
//        future.cancel(mayInterruptIfRunning); // set to false if you want the running task to be completed first.
//    }
//
//    public void activateFuture(ScheduledFuture<?> future) {
//        logger.info("Re-Activating a future " + new Date());
//        configureTasks(scheduledTaskRegistrar);
//    }
//
//    @Bean
//    public TaskScheduler poolSchedulerFews() {
//        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
//        scheduler.setThreadNamePrefix("poolConnectionFews");
//        scheduler.setPoolSize(2);
//        scheduler.initialize();
//        return scheduler;
//    }
//
//    @Override
//    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
//    
//    	if (scheduledTaskRegistrar == null) {
//            scheduledTaskRegistrar = taskRegistrar;
//        }
//        
//    	if (taskRegistrar.getScheduler() == null) {
//            taskRegistrar.setScheduler(poolSchedulerFews());
//        }
//
//        if (connectionFutureFews == null || connectionFutureFews.isCancelled()) {
//        	connectionFutureFews = taskRegistrar.getScheduler().scheduleWithFixedDelay(connectionFutureFewsTask, 60000);
//        }
//    
//    }
//
//    public int getCountConnectionTaskFews() {
//		return countConnectionTaskFews;
//	}
//
//    public int getCountScheduledTaskFews() {
//		return countScheduledTaskFews;
//	}
//
//    public int getCountScheduledTaskFewsAnterior() {
//		return countScheduledTaskFewsAnterior;
//	}
//
//    public class ConnectionFutureFewsTask implements Runnable {
//
//		@Override
//		public void run() {
//			
//			countScheduledTaskFews = scheduledTaskFews.getCountScheduledTaskFews();
//			countConnectionTaskFews++;
//			
//			logger.info(" *** ");
//			logger.info(" *** ConnectionFutureFewsTask 60 segundos *** " + countConnectionTaskFews + " *** Anterior: " + countScheduledTaskFewsAnterior + " *** Actual: " + countScheduledTaskFews + " " + new Date());
//			logger.info(" *** ");
//			
//			if (countScheduledTaskFewsAnterior!=0 && countScheduledTaskFewsAnterior==countScheduledTaskFews) {
//				logger.info(" *** ConnectionFutureFewsTask 60 segundos *** " + countConnectionTaskFews + " *** Anterior: " + countScheduledTaskFewsAnterior + " *** Actual: " + countScheduledTaskFews + " " + new Date());
//				logger.info(" *** ");
//				logger.info(jdbcTemplate);
//				logger.info(jdbcTemplate.getDataSource());
//				try {
//					logger.info(jdbcTemplate.getDataSource().getConnection());
//				} catch (SQLException e) {
//					logger.info(e);
//				}
//				logger.info(" *** ");
//			}
//    		
//			countScheduledTaskFewsAnterior = countScheduledTaskFews;
//		}
//    	
//    }
//}
