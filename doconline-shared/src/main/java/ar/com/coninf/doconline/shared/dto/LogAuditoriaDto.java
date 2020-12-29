package ar.com.coninf.doconline.shared.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "LOGS_AUDITORIA")
public class LogAuditoriaDto implements Serializable {

	private static final long serialVersionUID = 7761586339588476560L;

	@Id
	@Column(name = "ID_LOG_AUDITORIA", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "F_LOG")
	private Date fechaLog;
	@Column(name = "COD_CONSOLA")
	private Integer codigoConsola;
	@Column(name = "CONSOLA")
	private String nombreConsola;
	@Column(name = "OPERADOR")
	private String nombreOperador;
	@Column(name = "OPERACION")
	private String nombreOperacion;
	@Column(name = "RESULTADO")
	private String nombreResultado;
	@Column(name = "COD_RESULTADO")
	private Integer codigoResultado;
	@Column(name = "SERVIDOR")
	private String nombreServidor;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public void setId(BigDecimal id) {
		this.id = id.longValue();
	}

	public Date getFechaLog() {
		return fechaLog;
	}
	
	public void setFechaLog(Date fechaLog) {
		this.fechaLog = fechaLog;
	}
	
	public Integer getCodigoConsola() {
		return codigoConsola;
	}
	
	public void setCodigoConsola(Integer codigoConsola) {
		this.codigoConsola = codigoConsola;
	}
	
	public String getNombreConsola() {
		return nombreConsola;
	}
	
	public void setNombreConsola(String nombreConsola) {
		this.nombreConsola = nombreConsola;
	}
	
	public String getNombreOperador() {
		return nombreOperador;
	}
	
	public void setNombreOperador(String nombreOperador) {
		this.nombreOperador = nombreOperador;
	}
	
	public String getNombreOperacion() {
		return nombreOperacion;
	}
	
	public void setNombreOperacion(String nombreOperacion) {
		this.nombreOperacion = nombreOperacion;
	}
	
	public String getNombreResultado() {
		return nombreResultado;
	}
	
	public void setNombreResultado(String nombreResultado) {
		this.nombreResultado = nombreResultado;
	}
	
	public Integer getCodigoResultado() {
		return codigoResultado;
	}
	
	public void setCodigoResultado(Integer codigoResultado) {
		this.codigoResultado = codigoResultado;
	}
	
	public String getNombreServidor() {
		return nombreServidor;
	}
	
	public void setNombreServidor(String nombreServidor) {
		this.nombreServidor = nombreServidor;
	}
}
