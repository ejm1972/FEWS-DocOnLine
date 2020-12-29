//package ar.com.coninf.doconline.ws.timer2;
//
//import java.sql.Connection;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.GregorianCalendar;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Properties;
//import java.util.concurrent.ScheduledFuture;
//
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.datasource.DataSourceUtils;
//import org.springframework.scheduling.TaskScheduler;
//import org.springframework.scheduling.Trigger;
//import org.springframework.scheduling.TriggerContext;
//import org.springframework.scheduling.annotation.SchedulingConfigurer;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
//import org.springframework.scheduling.config.ScheduledTaskRegistrar;
//
//import ar.com.coninf.doconline.shared.excepcion.ApplicationException;
//
//public class SchedulerTaskFews implements SchedulingConfigurer {
//
//	private Logger logger = Logger.getLogger(this.getClass());
//
//	@Autowired
//	@Qualifier("dolProperties")
//	protected Properties dolProperties;
//	
//	@Autowired
//	@Qualifier("timer.AutorizadorSql")
//	private AutorizadorSql autorizadorSql;
//	
//	@Autowired
//	private ThreadPoolTaskScheduler poolSchedulerFews;
//
//	private ScheduledTaskRegistrar scheduledTaskRegistrar;
//	private ScheduledFuture<?> scheduledFutureFews;
//	private int countScheduledTaskFews;
//	private int delayScheduledTaskFews;
//
//	public SchedulerTaskFews() {
//		countScheduledTaskFews = 0;
//		delayScheduledTaskFews = 3;
//	}
//
//    //@Scheduled(fixedDelay = 1000, initialDelay = 30000)
//	public void scheduledTaskFews() {
//		
//		String ejecuta2001 = dolProperties.getProperty("interfaz_2001");
//		String ejecuta2006 = dolProperties.getProperty("interfaz_2006");
//		String ejecuta3001 = dolProperties.getProperty("interfaz_3001");
//		String ejecuta9901 = dolProperties.getProperty("interfaz_9901");
//
//		if (ejecuta2001==null && ejecuta2006==null && ejecuta3001==null && ejecuta9901==null) {
//		
//			logger.info("***");
//			logger.info("*** scheduledTaskFews " + poolSchedulerFews.getThreadNamePrefix() + "* shutdown *** " + new Date() + " - " + countScheduledTaskFews);
//			logger.info("***");
//			
//			poolSchedulerFews.shutdown();
//			
//			poolSchedulerFews.destroy();
//			
//		} else {
//
//		ApplicationException applicationException = null;
//		
//		try {
//			countScheduledTaskFews++;
//			
//			logger.info("***");
//			logger.info("*** Ini scheduledTaskFews *** " + new Date() + " - " + countScheduledTaskFews);
//			logger.info("***");
//
//			autorizadorSql.procesarPendientes();
//			logger.debug("Fin Ejecucion procesarPendientes()");
//			
//			autorizadorSql.logTimerSql();
//			logger.debug("Fin Ejecucion logTimerSql()");
//			
//			logger.debug("***");
//			logger.debug("*** Fin scheduledTaskFews *** - " + countScheduledTaskFews);
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
//				logger.error("*** Fin scheduledTaskFews de applicationException!=null *** - " + countScheduledTaskFews);
//				logger.error("***");
//			}
//		}
//		
//		}
//
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
//    public TaskScheduler poolSchedulerFews2() {
//        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
//        scheduler.setThreadNamePrefix("poolSchedulerFews2");
//        scheduler.setPoolSize(1);
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
//            taskRegistrar.setScheduler(poolSchedulerFews2());
//        }
//
//        if (scheduledFutureFews == null || scheduledFutureFews.isCancelled()) {
//        	
//        	scheduledFutureFews = taskRegistrar.getScheduler().schedule(
//        			
//                	new Runnable() {
//                    	@Override 
//                    	public void run() {
//                    		scheduledTaskFews();
//                    	}
//                	},
//                	
//                    new Trigger() {
//                        @Override 
//                        public Date nextExecutionTime(TriggerContext triggerContext) {
//                            Calendar nextExecutionTime =  new GregorianCalendar();
//                            Date lastActualExecutionTime = triggerContext.lastActualExecutionTime();
//                            nextExecutionTime.setTime(lastActualExecutionTime != null ? lastActualExecutionTime : new Date());
//                            nextExecutionTime.add(Calendar.SECOND, delayScheduledTaskFews); // This is where we set the next execution time.
//                            return nextExecutionTime.getTime();
//                        }
//                    }       			
//        			);
//        	
//        }
//        
//    }
//
//    public int getCountScheduledTaskFews() {
//		return countScheduledTaskFews;
//	}
//
//}
