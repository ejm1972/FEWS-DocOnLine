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
@Table(name = "COD_EXTERNOS_CLIENTES")
public class CodExternoClienteDto implements Serializable {

	private static final long serialVersionUID = -2167533945562213338L;

	@Id
	@InsertOptional
	@Column(name = "ID_COD_EXTERNO_CLIENTE", unique = true, nullable = false)
	private Long id;

	@Key
	@Column(name = "ID_CLIENTE")
	private Long clienteId;

	@Key
	@Column(name = "COD_EXTERNO")
	private String codigoExterno;

	@InsertOptional
	@Column(name = "F_VIGENCIA_DESDE")
	private Date fechaVigenciaDesde;

	@InsertOptional
	@Column(name = "F_VIGENCIA_HASTA")
	private Date fechaVigenciaHasta;

	@InsertOptional
	@Column(name = "ID_PROVINCIA")
	private Long provinciaId;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public void setId(BigDecimal id) {
		this.id = id.longValue();
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
	
	public String getCodigoExterno() {
		return codigoExterno;
	}
	
	public void setCodigoExterno(String codigoExterno) {
		this.codigoExterno = codigoExterno;
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
	
	public Long getProvinciaId() {
		return provinciaId;
	}
	
	public void setProvinciaId(Long provinciaId) {
		this.provinciaId = provinciaId;
	}
	public void setProvinciaId(BigDecimal provinciaId) {
		this.provinciaId = provinciaId.longValue();
	}
}
