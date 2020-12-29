package ar.com.coninf.doconline.ws.validador;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import ar.com.coninf.doconline.rest.model.enums.ErrorEnum;
import ar.com.coninf.doconline.rest.model.response.Response;

public class ValidadorWS {

	private List<String> obligatorio = new ArrayList<String>();
	private List<String> formato = new ArrayList<String>();
	private List<String> longitud = new ArrayList<String>();
	private List<String> rango = new ArrayList<String>();
	private Boolean tieneErrores = Boolean.FALSE;

	
	
	public void validarObligatorio(String campo, Object valor){
		if(valor == null){
			obligatorio.add(campo);
			tieneErrores = Boolean.TRUE;
		}
		String valorS = String.valueOf(valor);
		if(valorS.trim().equals("")){
			obligatorio.add(campo);
			tieneErrores = Boolean.TRUE;
		}
	}
		
	public void validarFormatoNumero(String campo, Object valor, String regExp){
		if(campo == null || valor == null){
			return;
		}
		String valorS = String.valueOf(valor);
		Pattern p = null;
		if(regExp != null && !regExp.trim().equals("")){
			p = Pattern.compile(regExp);
		}
		if(p == null){
			p = Pattern.compile("^[0-9]+$");
		}
		if(!p.matcher(valorS).matches()){
			formato.add(campo);
			tieneErrores = Boolean.TRUE;
		}
	}
	
	public void validarFormatoFecha(String campo, Object valor, String patron){
		if(campo == null || valor == null){
			return;
		}
		String valorS = String.valueOf(valor);
		SimpleDateFormat sdf = null;
		if(patron == null || patron.trim().equals("")){
			patron = "dd/MM/yyyy";
		}
		sdf = new SimpleDateFormat(patron);
		if(patron.length() != valorS.length()){
			formato.add(campo);
			tieneErrores = Boolean.TRUE;
			return;
		}
		sdf.setLenient(Boolean.FALSE);
		try {
			sdf.parse(valorS);
		} catch (ParseException e) {
			formato.add(campo);
			tieneErrores = Boolean.TRUE;
		}
	}
	
	public void validarFormatoEMail(String campo, Object valor, String regExp){
		if(campo == null || valor == null){
			return;
		}
		String valorS = String.valueOf(valor);
		Pattern p = null;
		if(regExp != null && !regExp.trim().equals("")){
			p = Pattern.compile(regExp);
		}
		if(p == null){
			p = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
		}
		if(!p.matcher(valorS).matches()){
			formato.add(campo);
			tieneErrores = Boolean.TRUE;
		}
	}

	public void validarLongitud(String campo, Object valor, int largoMin, int largoMax){
		if(campo == null || valor == null){
			return;
		}
		String valorS = String.valueOf(valor);
		if(valorS.length() > largoMax || valorS.length() < largoMin){
			longitud.add(campo);
			tieneErrores = Boolean.TRUE;
		}
	}
	
	public void validarRango(String campo, BigDecimal valor, BigDecimal valorMin, BigDecimal valorMax){
		if(campo == null || valor == null){
			return;
		}
		if(valorMin != null && valor.compareTo(valorMin) < 0) {
			rango.add(campo);
			tieneErrores = Boolean.TRUE;
		}
		if(valorMax != null && valor.compareTo(valorMax) > 0) {
			rango.add(campo);
			tieneErrores = Boolean.TRUE;
		}
	}

	public void validarCuitCuil(String campo, String valor){
		if(campo == null || valor == null){
			return;
		}
		
		if (valor.length() != 11) {
			longitud.add(campo);
			tieneErrores = Boolean.TRUE;
		} else if (valor.substring(1,2).matches("20|27|24|23|30|33")) {
		    int[] mult = { 5, 4, 3, 2, 7, 6, 5, 4, 3, 2, 1 };
		    int total = 0;
		    for (int i = 0; i < mult.length; i++) {
		      total += Integer.valueOf(valor.substring(i,1)) * mult[i];
		    }
		    int mod = total % 11;
		    if (mod != 0) {
				formato.add(campo);
				tieneErrores = Boolean.TRUE;
		    }
		} else {
			formato.add(campo);
			tieneErrores = Boolean.TRUE;
		}
	}

	public Response obtenerRespuesta(){
		Response respuesta =  null;
		if(!tieneErrores){
			return respuesta;
		}
		if(obligatorio.size() > 0){
			String obs = obligatorio.toString().replaceAll("\\[", "").replaceAll("\\]", "");
			respuesta = new Response(ErrorEnum.ERROR_CAMPO_OBLIGATORIO, obs);
			return respuesta;
		}
		if(formato.size() > 0){
			String obs = formato.toString().replaceAll("\\[", "").replaceAll("\\]", "");
			respuesta = new Response(ErrorEnum.ERROR_FORMATO_INVALIDO, obs);
			return respuesta;
		}
		if(longitud.size() > 0){
			String obs = longitud.toString().replaceAll("\\[", "").replaceAll("\\]", "");
			respuesta = new Response(ErrorEnum.ERROR_LONGITUD_INVALIDA, obs);
			return respuesta;
		}
		if(rango.size() > 0){
			String obs = rango.toString().replaceAll("\\[", "").replaceAll("\\]", "");
			respuesta = new Response(ErrorEnum.ERROR_RANGO_INVALIDA, obs);
			return respuesta;
		}
		return respuesta;
	}
}
