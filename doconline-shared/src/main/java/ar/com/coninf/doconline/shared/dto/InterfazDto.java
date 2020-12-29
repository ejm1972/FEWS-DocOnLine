package ar.com.coninf.doconline.shared.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import ar.com.coninf.doconline.shared.anotacion.InsertOptional;

@Entity
@Table(name = "INTERFACES")
public class InterfazDto implements Serializable {

	private static final long serialVersionUID = 8064007187634940297L;

	@Id
	@InsertOptional
	@Column(name = "ID_INTERFAZ", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "INTERFAZ")
	private String nombreInterfaz;
	
	@Column(name = "CLAVE")
	private String clave;
	
	@Column(name = "ID_CANAL_ACCESO")
	private Long idTipoServicio;

	@Column(name = "ACTIVADO")
	private String activo;

	@Column(name = "CANT_OPERACIONES")
	private Integer cantidadOperaciones;

	@Column(name = "CANT_ACUM_OPERACIONES")
	private Double montoOperaciones;

	@Column(name = "FLG_CONTROL")
	private String controlOperaciones;

	@Column(name = "CUIT_SUSCRIPCION")
	private String cuitSuscripcion;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setId(BigDecimal id) {
		this.id = id.longValue();
	}
	
	public String getNombreInterfaz() {
		return nombreInterfaz;
	}

	public void setNombreInterfaz(String nombreInterfaz) {
		this.nombreInterfaz = nombreInterfaz;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}
	
	public Long getIdTipoServicio() {
		return idTipoServicio;
	}

	public void setIdTipoServicio(Long idTipoServicio) {
		this.idTipoServicio = idTipoServicio;
	}

	public void setIdTipoServicio(BigDecimal idTipoServicio) {
		this.idTipoServicio = idTipoServicio.longValue();
	}

	public Integer getCantidadOperaciones() {
		return cantidadOperaciones;
	}

	public void setCantidadOperaciones(Integer cantidadOperaciones) {
		this.cantidadOperaciones = cantidadOperaciones;
	}

	public void setCantidadOperaciones(BigDecimal cantidadOperaciones) {
		this.cantidadOperaciones = cantidadOperaciones.intValue();
	}

	public Double getMontoOperaciones() {
		return montoOperaciones;
	}

	public void setMontoOperaciones(Double montoOperaciones) {
		this.montoOperaciones = montoOperaciones;
	}

	public void setMontoOperaciones(BigDecimal montoOperaciones) {
		this.montoOperaciones = montoOperaciones.doubleValue();
	}

	public String getControlOperaciones() {
		return controlOperaciones;
	}

	public void setControlOperaciones(String controlOperaciones) {
		this.controlOperaciones = controlOperaciones;
	}

	public String getActivo() {
		return activo;
	}
	public void setActivo(String activo) {
		this.activo = activo;
	}

	public String getCuitSuscripcion() {
		return cuitSuscripcion;
	}
	public void setCuitSuscripcion(String cuit) {
		this.cuitSuscripcion = cuit;
	}
}
