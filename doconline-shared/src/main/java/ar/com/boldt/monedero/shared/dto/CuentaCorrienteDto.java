package ar.com.boldt.monedero.shared.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import ar.com.coninf.doconline.shared.anotacion.InsertOptional;
import ar.com.coninf.doconline.shared.anotacion.Key;

@Entity
@Table(name = "CUENTAS_CORRIENTES")
public class CuentaCorrienteDto implements Serializable {

	private static final long serialVersionUID = -3644435725972276889L;

	@Id
	@InsertOptional
	@Column(name = "ID_CUENTA_CORRIENTE", unique = true, nullable = false)
	private Long id;

	@Key
	@Column(name = "ID_CLIENTE")
	private Long clienteId;
	
	@InsertOptional
	@Column(name = "F_VIGENCIA_DESDE")
	private Date fechaVigenciaDesde;

	@InsertOptional
	@Column(name = "F_VIGENCIA_HASTA")
	private Date fechaVigenciaHasta;
	
	@Key
	@Column(name = "ID_TIPO_CTACTE")
	private Long tipoCtaCteId;
	
	@InsertOptional
	@Column(name = "SALDO")
	private BigDecimal saldo;

	private List<MovimientoCtaCteDto> movimientoCtaCteDtos;
	
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
	
	public Long getTipoCtaCteId() {
		return tipoCtaCteId;
	}
	
	public void setTipoCtaCteId(Long tipoCtaCteId) {
		this.tipoCtaCteId = tipoCtaCteId;
	}
	public void setTipoCtaCteId(BigDecimal tipoCtaCteId) {
		this.tipoCtaCteId = tipoCtaCteId.longValue();
	}
	
	public BigDecimal getSaldo() {
		return saldo;
	}
	
	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public List<MovimientoCtaCteDto> getMovimientoCtaCteDtos() {
		return movimientoCtaCteDtos;
	}

	public void setMovimientoCtaCteDtos(
		List<MovimientoCtaCteDto> movimientoCtaCteDtos) {
		this.movimientoCtaCteDtos = movimientoCtaCteDtos;
	}
}
