package ar.com.coninf.doconline.shared.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class MonederoUtiles {
	static PropertiesReader pr;
	
	public MonederoUtiles() {
		try {
			pr = new PropertiesReader("utils.properties");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String convertirFechayyyyMMdd(Date fecha) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		sdf.setLenient(Boolean.FALSE);
		String date = sdf.format(fecha);
		return date;
	}	
	
	public static String convertirFechaddMMyyyy(Date fecha) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		sdf.setLenient(Boolean.FALSE);
		String date = sdf.format(fecha);
		return date;
	}
	
	public static String convertirFechaddMMyyyyHoraHHmmssSSSS(Date fecha) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SSSS");
		sdf.setLenient(Boolean.FALSE);
		String date = sdf.format(fecha);
		return date;
	}
	
	public static String formatearAFechaConHora(Date fecha) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy hh:mm");      
		String dateWithoutTime =sdf.format(fecha);
		return dateWithoutTime;
	}
	
	public static Date formatearAFechaSinHora(Date fecha) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");      
		Date dateWithoutTime = sdf.parse(sdf.format(fecha));
		return dateWithoutTime;
	}

	public static byte[] serializar(Serializable object) throws IOException {
		ByteArrayOutputStream bs= new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream (bs);
		os.writeObject(object);
		os.close();
		return bs.toByteArray();
	}
	
	public static Serializable desSerializar(byte[] serial) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bs= new ByteArrayInputStream(serial);
		ObjectInputStream is = new ObjectInputStream(bs);
		Serializable obj = (Serializable) is.readObject();
		is.close();
		return obj;
	}
	
	/**
	 * Devuelve la diferencia entre la fecha1 - fecha2
	 * @param fecha1
	 * @param fecha2
	 * @return cero si las fechas son iguales, positivo si fecha1 > fecha2 y negativo si fecha1 < fecha2
	 */
	public static long diferenciaEnDias(Date fecha1, Date fecha2) {

		Calendar fecha1Calendar = Calendar.getInstance();
		fecha1Calendar.setTime(fecha1);
		long fecha1Mills = fecha1Calendar.getTimeInMillis();

		Calendar fecha2Calendar = Calendar.getInstance();
		fecha2Calendar.setTime(fecha2);
		long fecha2Mills = fecha2Calendar.getTimeInMillis();

		long diff = fecha1Mills - fecha2Mills;
		long diffDays = diff / (24 * 60 * 60 * 1000);

		return diffDays;
	}

	/**
	 * Devuelve la diferencia entre la fecha1 - fecha2
	 * @param fecha1
	 * @param fecha2
	 * @return cero si las fechas son iguales, positivo si fecha1 > fecha2 y negativo si fecha1 < fecha2
	 */
	public static long diferenciaEnHoras(Date fecha1, Date fecha2) {

		Calendar fecha1Calendar = Calendar.getInstance();
		fecha1Calendar.setTime(fecha1);
		long fecha1Mills = fecha1Calendar.getTimeInMillis();

		Calendar fecha2Calendar = Calendar.getInstance();
		fecha2Calendar.setTime(fecha2);
		long fecha2Mills = fecha2Calendar.getTimeInMillis();

		long diff = fecha1Mills - fecha2Mills;
		long diffDays = diff / (60 * 60 * 1000);

		return diffDays;
	}

	/**
	 * Genera el nombre de servicio que se guarda con el control de transaccion
	 * @param servicioClassName
	 * @param operacion
	 * @return nombre del servicio
	 */
	public static String generarNombreServicio(String servicioClassName, String operacion) {
		String[] s = servicioClassName.split("\\.");
		String servicio = s[s.length - 1];
		if(servicio.indexOf("@") != -1){
			servicio = servicio.substring(0, servicio.indexOf("@"));
		}
		servicio += " - " + operacion;
		return servicio;
	}
	
	/**
	 * Genera un Nro de Pin aleatorio entre 0000 a 9999
	 * @return nuevoPin
	 */
	public static String generaNuevoPin() {
		Integer cantRango = new Integer(pr.get("pin_cantidadRango"));
		Integer termInicio = new Integer(pr.get("pin_terminoInicio"));
		Integer cantCifra = new Integer(pr.get("pin_cantidadCifras"));
		
		Random r = new Random(Calendar.getInstance().getTimeInMillis());
		
		Integer pinInteger = (int)(r.nextDouble() * cantRango + termInicio);
		
		String pinString = StringUtils.leftPad(pinInteger.toString(), cantCifra, "0");
		
		return pinString;		
	}
}
