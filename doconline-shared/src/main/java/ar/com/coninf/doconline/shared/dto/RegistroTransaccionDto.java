package ar.com.coninf.doconline.shared.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import ar.com.coninf.doconline.shared.anotacion.InsertOptional;
import ar.com.coninf.doconline.shared.anotacion.Key;

@Entity
@Table(name = "REGISTROS_TRANSACCIONES")
public class RegistroTransaccionDto implements Serializable {

	private static final long serialVersionUID = 2900654042493529862L;

	@Id
	@InsertOptional
	@Column(name = "ID_REGISTRO_TRANSACCION", unique = true, nullable = false)
	private Long id;
	
	@Key
	@Column(name = "COD_TRANSACCION")
	private Long codigoTransaccion;

	@Key
	@Column(name = "ID_INTERFAZ")
	private Integer interfazId;

	@Key
	@Column(name = "SERVICIO")
	private String servicio;

	@Key
	@Column(name = "F_TRANSACCION")
	private Date fechaTransaccion;
	
	@Column(name = "DATOS_TRANSACCION")
	private byte[] datosTransaccion;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setId(BigDecimal id) {
		this.id = id.longValue();
	}
	
	public Integer getInterfazId() {
		return interfazId;
	}
	
	public void setInterfazId(Integer interfazId) {
		this.interfazId = interfazId;
	}
	
	public void setInterfazId(Long interfazId) {
		this.interfazId = interfazId.intValue();
	}
	
	public void setInterfazId(BigDecimal interfazId) {
		this.interfazId = interfazId.intValue();
	}
	
	public Date getFechaTransaccion() {
		return fechaTransaccion;
	}
	
	public void setFechaTransaccion(Date fechaTransaccion) {
		this.fechaTransaccion = fechaTransaccion;
	}
	
	public void setFechaTransaccion(Timestamp fechaTransaccion) {
		this.fechaTransaccion = fechaTransaccion;
	}
	
	public Long getCodigoTransaccion() {
		return codigoTransaccion;
	}
	
	public void setCodigoTransaccion(Integer codigoTransaccion) {
		this.codigoTransaccion = codigoTransaccion.longValue();
	}
	
	public void setCodigoTransaccion(Long codigoTransaccion) {
		this.codigoTransaccion = codigoTransaccion;
	}

	public void setCodigoTransaccion(BigDecimal codigoTransaccion) {
		this.codigoTransaccion = codigoTransaccion.longValue();
	}

	public byte[] getDatosTransaccion() {
		return datosTransaccion;
	}

	public void setDatosTransaccion(byte[] datosTransaccion) {
		this.datosTransaccion = datosTransaccion;
	}

	public String getServicio() {
		return servicio;
	}
	
	public void setServicio(String servicio) {
		this.servicio = servicio;
	}
}
