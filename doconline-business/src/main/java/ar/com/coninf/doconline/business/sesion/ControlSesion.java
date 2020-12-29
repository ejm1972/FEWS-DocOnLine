package ar.com.coninf.doconline.business.sesion;

import java.util.Calendar;
import java.util.Date;

public class ControlSesion {

	private String idSesion;
	private Date fechaHoraIngreso;
	private long expira;

	public String getIdSesion() {
		return idSesion;
	}
	public void setIdSesion(String idSesion) {
		this.idSesion = idSesion;
	}
	public Date getFechaHoraIngreso() {
		return fechaHoraIngreso;
	}
	public void setFechaHoraIngreso(Date fechaHoraIngreso) {
		this.fechaHoraIngreso = fechaHoraIngreso;
	}
	public long getExpira() {
		return expira;
	}
	public void setExpira(long expira) {
		this.expira = expira;
	}
	public Boolean sesionExpirada(){
		Boolean expirada = Boolean.FALSE;
		if(Calendar.getInstance().getTimeInMillis() > expira){
			expirada = Boolean.TRUE;
		}
		return expirada;
	}
}
