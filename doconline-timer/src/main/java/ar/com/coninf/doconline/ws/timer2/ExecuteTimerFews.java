//package ar.com.coninf.doconline.ws.timer2;
//
//import java.util.Date;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//
//import org.apache.log4j.Logger;
//
//public class ExecuteTimerFews extends HttpServlet {
//	private static final long serialVersionUID = 1L;
//	
//	private static int n = 0;
//	
//	private Logger logger = Logger.getLogger(this.getClass());
//	
//	public void init() throws ServletException {
//
//		logger.info("-----------------------------------");
//		logger.info("Ejecucion ExecuteTimerFews init ... " + ++n + " - " + new Date());
//		logger.info("-----------------------------------");
//
////		//separar clase de servlet de la de timer, luego ver el tema del contexto spring para usar los bean
////		ClassPathXmlApplicationContext appCtx = new ClassPathXmlApplicationContext(new String[]{"ctx/application-context.xml"}, true);
////		
////		AutorizadorSql aut = (AutorizadorSql) appCtx.getBean("timer.AutorizadorSql");
////		
////		appCtx.close();
////		
////		String ejecuta2001 = aut.dolProperties.getProperty("interfaz_2001");
////		String ejecuta2006 = aut.dolProperties.getProperty("interfaz_2006");
////		String ejecuta3001 = aut.dolProperties.getProperty("interfaz_3001");
////		String ejecuta9901 = aut.dolProperties.getProperty("interfaz_9901");
////		
////		if (ejecuta2001!=null || ejecuta2006!=null || ejecuta3001!=null || ejecuta9901!=null) {
////			
////			//TimerFews timerFews = new TimerFews(aut, jdbc);
////			//timerFews.instanciar();
////			//timerFews.programar30();
////		}
//		
//	}
//
//}
