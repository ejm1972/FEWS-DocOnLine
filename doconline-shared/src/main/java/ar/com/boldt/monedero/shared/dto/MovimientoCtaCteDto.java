package ar.com.boldt.monedero.shared.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.com.coninf.doconline.shared.anotacion.InsertOptional;

@Entity
@Table(name = "MOVIMIENTOS_CTACTE")
public class MovimientoCtaCteDto implements Serializable {

	private static final long serialVersionUID = 996465547470097663L;

	@Id
	@InsertOptional
	@Column(name = "ID_MOVIMIENTO_CTACTE", unique = true, nullable = false)
	private Long id;

	@Column(name = "ID_TIPO_MOVIMIENTO_CTACTE")
	private Long tipoMovimientoCtaCteId;
	
	@Column(name = "MONTO")
	private BigDecimal monto;
	
	@Column(name = "ID_CUENTA_CORRIENTE")
	private Long cuentaCorrienteId;
	
	@InsertOptional
	@Column(name = "F_MOVIMIENTO")
	private Date fechaMovimiento;
	
	@Column(name = "COD_EXTERNO")
	private String codigoExterno;
	
	@Column(name = "ID_INTERFAZ")
	private Long idInterfaz;
	
	@Column(name = "COD_OPERACION")
	private String codigoOperacion;
	
	@InsertOptional
	@Column(name = "ID_RESERVA")
	private Long reservaId;

	@InsertOptional
	@Column(name = "SALDO")
	private BigDecimal saldo;
	
	@Transient
	@InsertOptional
	@Column(name = "DESC_CORTA")
	private String descCorta;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public void setId(BigDecimal id) {
		this.id = id.longValue();
	}	

	public Long getTipoMovimientoCtaCteId() {
		return tipoMovimientoCtaCteId;
	}
	
	public void setTipoMovimientoCtaCteId(Long tipoMovimientoCtaCteId) {
		this.tipoMovimientoCtaCteId = tipoMovimientoCtaCteId;
	}
	
	public void setTipoMovimientoCtaCteId(BigDecimal tipoMovimientoCtaCteId) {
		this.tipoMovimientoCtaCteId = tipoMovimientoCtaCteId.longValue();
	}
	
	public BigDecimal getMonto() {
		return monto;
	}
	
	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}
	
	public Long getCuentaCorrienteId() {
		return cuentaCorrienteId;
	}
	
	public void setCuentaCorrienteId(Long cuentaCorrienteId) {
		this.cuentaCorrienteId = cuentaCorrienteId;
	}
	
	public void setCuentaCorrienteId(BigDecimal cuentaCorrienteId) {
		this.cuentaCorrienteId = cuentaCorrienteId.longValue();
	}
	
	public Date getFechaMovimiento() {
		return fechaMovimiento;
	}
	
	public void setFechaMovimiento(Date fechaMovimiento) {
		this.fechaMovimiento = fechaMovimiento;
	}
	
	public void setFechaMovimiento(Timestamp fechaMovimiento) {
		this.fechaMovimiento = fechaMovimiento;
	}
	
	public String getCodigoExterno() {
		return codigoExterno;
	}
	
	public void setCodigoExterno(String codigoExterno) {
		this.codigoExterno = codigoExterno;
	}
	
	public Long getReservaId() {
		return reservaId;
	}
	
	public void setReservaId(Long reservaId) {
		this.reservaId = reservaId;
	}
	
	public void setReservaId(BigDecimal reservaId) {
		this.reservaId = reservaId.longValue();
	}
	
	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}
	
	public String getDescCorta() {
		return descCorta;
	}
	
	public void setDescCorta(String descCorta) {
		this.descCorta = descCorta;
	}
	public Long getIdInterfaz() {
		return idInterfaz;
	}
	public void setIdInterfaz(Long idInterfaz) {
		this.idInterfaz = idInterfaz;
	}
	public void setIdInterfaz(BigDecimal idInterfaz) {
		this.idInterfaz = idInterfaz.longValue();
	}
	
	public String getCodigoOperacion() {
		return codigoOperacion;
	}
	public void setCodigoOperacion(String codigoOperacion) {
		this.codigoOperacion = codigoOperacion;
	}
}
