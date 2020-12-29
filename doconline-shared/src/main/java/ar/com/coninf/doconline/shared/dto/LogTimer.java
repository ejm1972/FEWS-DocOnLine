package ar.com.coninf.doconline.shared.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import ar.com.coninf.doconline.shared.anotacion.InsertOptional;
import ar.com.coninf.doconline.shared.anotacion.Key;

@Entity
@Table(name = "dbo.log_timer")
public class LogTimer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Key
	@Column(name = "objeto_instanciado")
	private String objetoInstanciado;
	
	@Column(name = "fecha_ejecucion")
	private Date fechaEjecucion;

	@InsertOptional
	@Column(name = "mensaje", columnDefinition = "text", nullable = true)
	private char[] mensaje;

	@InsertOptional
	@Column(name = "clase", nullable = true)
	private String clase;

	@InsertOptional
	@Column(name = "metodo", nullable = true)
	private String metodo;
	
	@InsertOptional
	@Column(name = "espera", nullable = true)
	private Integer espera;

	@InsertOptional
	@Column(name = "periodo", nullable = true)
	private Integer periodo;

	public String getObjetoInstanciado() {
		return objetoInstanciado;
	}
	public void setObjetoInstanciado(String objetoInstanciado) {
		this.objetoInstanciado = objetoInstanciado;
	}

	public Date getFechaEjecucion() {
		return fechaEjecucion;
	}
	public void setFechaEjecucion(Date fechaEjecucion) {
		this.fechaEjecucion = fechaEjecucion;
	}
	public void setFechaEjecucion(Timestamp fechaEjecucion) {
		this.fechaEjecucion = fechaEjecucion;
	}

	public char[] getMensaje() {
		return mensaje;
	}
	public void setMensaje(char[] mensaje) {
		this.mensaje = mensaje;
	}

	public String getClase() {
		return clase;
	}
	public void setClase(String clase) {
		this.clase = clase;
	}

	public String getMetodo() {
		return metodo;
	}
	public void setMetodo(String metodo) {
		this.metodo = metodo;
	}

	public Integer getEspera() {
		return espera;
	}
	public void setEspera(Integer espera) {
		this.espera = espera;
	}

	public Integer getPeriodo() {
		return periodo;
	}
	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
	}
	
}
