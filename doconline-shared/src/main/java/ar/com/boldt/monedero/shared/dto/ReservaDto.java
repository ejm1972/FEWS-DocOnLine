package ar.com.boldt.monedero.shared.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.com.coninf.doconline.rest.model.tx.ITransaccionable;
import ar.com.coninf.doconline.shared.anotacion.InsertOptional;
import ar.com.coninf.doconline.shared.anotacion.Key;

@Entity
@Table(name = "RESERVAS")
public class ReservaDto implements ITransaccionable {

	private static final long serialVersionUID = 5782761859173499532L;

	@Id
	@InsertOptional
	@Column(name = "ID_RESERVA", unique = true, nullable = false)
	private Long id;

	@Key
	@Column(name = "CODIGO_RESERVA")
	private String codigoReserva;

	@Column(name = "F_RESERVA")
	@InsertOptional
	private Date fechaReserva;

	@Key
	@Column(name = "ID_ESTADO_RESERVA")
	private Long estadoReservaId;
	
	@Column(name = "F_EXTRACCION")
	@InsertOptional
	private Timestamp fechaExtraccion;

	@Column(name = "F_VENCIMIENTO")
	@InsertOptional
	private Date fechaVencimiento;

	@Key
	@Column(name = "ID_CLIENTE")
	private Long clienteId;
	
	@Column(name = "ID_TIPO_CTACTE")
	@InsertOptional
	@Transient
	private Long tipoCtaCteId;

	@Column(name = "MONTO")
	@InsertOptional
	@Transient	
	private BigDecimal monto;

	@Column(name = "SALDO")
	@InsertOptional
	@Transient	
	private BigDecimal saldo;
	
	@Column(name = "COD_OPERACION")
	@InsertOptional
	@Transient
	private String codigoOperacion;
	
	@Transient
	private String rawCodigoReserva;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setId(BigDecimal id) {
		this.id = id.longValue();
	}
	
	public String getCodigoReserva() {
		return codigoReserva;
	}
	
	public void setCodigoReserva(String codigoReserva) {
		this.codigoReserva = codigoReserva;
	}

	public Date getFechaReserva() {
		return fechaReserva;
	}
	
	public void setFechaReserva(Date fechaReserva) {
		this.fechaReserva = fechaReserva;
	}
	
	public void setFechaReserva(Timestamp fechaReserva) {
		this.fechaReserva = fechaReserva;
	}
	
	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}
	
	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}
	
	public void setFechaVencimiento(Timestamp fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}
	
	public Long getEstadoReservaId() {
		return estadoReservaId;
	}
	
	public void setEstadoReservaId(Long estadoReservaId) {
		this.estadoReservaId = estadoReservaId;
	}
	
	public void setEstadoReservaId(BigDecimal estadoReservaId) {
		this.estadoReservaId = estadoReservaId.longValue();
	}
	
	public Timestamp getFechaExtraccion() {
		return fechaExtraccion;
	}
	
	public void setFechaExtraccion(Timestamp fechaExtraccion) {
		this.fechaExtraccion = fechaExtraccion;
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
	
	public Long getTipoCtaCteId() {
		return tipoCtaCteId;
	}

	public void setTipoCtaCteId(Long tipoCtaCteId) {
		this.tipoCtaCteId = tipoCtaCteId;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}
	
	public String getCodigoOperacion() {
		return codigoOperacion;
	}

	public void setCodigoOperacion(String codigoOperacion) {
		this.codigoOperacion = codigoOperacion;
	}

	public String getRawCodigoOperacion() {
		return rawCodigoReserva;
	}

	public void setRawCodigoReserva(String rawCodigoReserva) {
		this.rawCodigoReserva = rawCodigoReserva;
	}
}
