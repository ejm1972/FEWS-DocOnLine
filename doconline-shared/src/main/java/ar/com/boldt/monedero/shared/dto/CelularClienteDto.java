package ar.com.boldt.monedero.shared.dto;

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
@Table(name = "CELULARES_CLIENTES")
public class CelularClienteDto implements Serializable {

	private static final long serialVersionUID = 1056541947544190897L;

	@Id
	@InsertOptional
	@Column(name = "ID_CELULAR_CLIENTE", unique = true, nullable = false)
	private Long id;

	@Key
	@Column(name = "NRO_CELULAR")
	private String numeroCelular;
	
	@Column(name = "ID_CLIENTE")
	private Long clienteId;

	@InsertOptional
	@Column(name = "F_VIGENCIA_DESDE")
	private Date fechaVigenciaDesde;

	@InsertOptional
	@Column(name = "F_VIGENCIA_HASTA")
	private Date fechaVigenciaHasta;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setId(BigDecimal id) {
		this.id = id.longValue();
	}

	public String getNumeroCelular() {
		return numeroCelular;
	}

	public void setNumeroCelular(String numeroCelular) {
		this.numeroCelular = numeroCelular;
	}

	public Long getClienteId() {
		return clienteId;
	}

	public void setClienteId(Long clienteId) {
		this.clienteId = clienteId;
	}
	public void setClienteId(BigDecimal clienteId) {
		this.clienteId = clienteId.longValue();
	}

	public Date getFechaVigenciaDesde() {
		return fechaVigenciaDesde;
	}

	public void setFechaVigenciaDesde(Date fechaVigenciaDesde) {
		this.fechaVigenciaDesde = fechaVigenciaDesde;
	}
	public void setFechaVigenciaDesde(Timestamp fechaVigenciaDesde) {
		this.fechaVigenciaDesde = fechaVigenciaDesde;
	}

	public Date getFechaVigenciaHasta() {
		return fechaVigenciaHasta;
	}

	public void setFechaVigenciaHasta(Date fechaVigenciaHasta) {
		this.fechaVigenciaHasta = fechaVigenciaHasta;
	}
	public void setFechaVigenciaHasta(Timestamp fechaVigenciaHasta) {
		this.fechaVigenciaHasta = fechaVigenciaHasta;
	}
}
