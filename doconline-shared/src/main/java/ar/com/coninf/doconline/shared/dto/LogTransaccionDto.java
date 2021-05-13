package ar.com.coninf.doconline.shared.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import ar.com.coninf.doconline.shared.anotacion.InsertOptional;

@Entity
@Table(name = "LOG_TRANSACCIONES")
public class LogTransaccionDto implements Serializable {

	
	private static final long serialVersionUID = -898598838564582168L;

	@Id
	@InsertOptional
	@Column(name = "ID_LOG_TRANSACCION", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "F_INICIO_OPERACION")
	private Timestamp fechaInicioOp;
	
	@Column(name = "F_FIN_OPERACION")
	private Timestamp fechaFinOp;
	
	@Column(name = "ID_TIPO_TRANSACCION")
	private Long tipoTransaccionId;
	
	@Column(name = "XML_ENTRADA")
	private char[] xmlEntrada;
	
	@Column(name = "XML_SALIDA")
	private char[] xmlSalida;
	
	@Column(name = "ID_REGISTRO_TRANSACCION")
	private Long registroTransaccionId;

	@Column(name = "CODIGO")
	private String codigo;
	
	@Column(name = "DESCRIPCION")
	private String descripcion;
	
	@Column(name = "OBSERVACION")
	private String observacion;
	
	@Column(name = "EXCEPCION_WSAA")
	private String excepcionWsaa;

	@Column(name = "EXCEPCION_WSFEV1")
	private String excepcionWsfev1;
	
	@Column(name = "RESULTADO")
	private String resultado;
	
	@Column(name = "ERR_MSG")
	private String errMsg;
	
	@Column(name = "OBS")
	private String obs;
	
	@Column(name = "XML_REQUEST_AFIP")
	private char[] xmlRequestAfip;
	
	@Column(name = "XML_RESPONSE_AFIP")
	private char[] xmlResponseAfip;
	
	@Column(name = "TIPO_COMPROBANTE")
	private String tipoComprobante;
	
	@Column(name = "PUNTO_VENTA")
	private String puntoVenta;
	
	@Column(name = "NUMERO_COMPROBANTE")
	private String numeroComprobante;
	
	@Column(name = "COMPROBANTE")
	private String comprobante;

	@Column(name = "FECHA_COMPROBANTE")
	private String fechaComprobante;
	
	@Column(name = "IMPORTE_TOTAL")
	private String importeTotal;
	
	@Column(name = "CAE")
	private String cae;
	
	@Column(name = "FECHA_VENCIMIENTO")
	private String fechaVencimiento;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public void setId(BigDecimal id) {
		this.id = id.longValue();
	}

	public Timestamp getFechaInicioOp() {
		return fechaInicioOp;
	}

	public void setFechaInicioOp(Timestamp fechaInicioOp) {
		this.fechaInicioOp = fechaInicioOp;
	}

	public Timestamp getFechaFinOp() {
		return fechaFinOp;
	}

	public void setFechaFinOp(Timestamp fechaFinOp) {
		this.fechaFinOp = fechaFinOp;
	}

	public Long getTipoTransaccionId() {
		return tipoTransaccionId;
	}

	public void setTipoTransaccionId(Long tipoTransaccionId) {
		this.tipoTransaccionId = tipoTransaccionId;
	}
	
	public void setTipoTransaccionId(BigDecimal tipoTransaccionId) {
		this.tipoTransaccionId = tipoTransaccionId.longValue();
	}

	public char[] getXmlEntrada() {
		return xmlEntrada;
	}

	public void setXmlEntrada(char[] xmlEntrada) {
		this.xmlEntrada = xmlEntrada;
	}

	public char[] getXmlSalida() {
		return xmlSalida;
	}

	public void setXmlSalida(char[] xmlSalida) {
		this.xmlSalida = xmlSalida;
	}

	public Long getRegistroTransaccionId() {
		return registroTransaccionId;
	}

	public void setRegistroTransaccionId(Long registroTransaccionId) {
		this.registroTransaccionId = registroTransaccionId;
	}
	
	public void setRegistroTransaccionId(BigDecimal registroTransaccionId) {
		this.registroTransaccionId = registroTransaccionId.longValue();
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getExcepcionWsaa() {
		return excepcionWsaa;
	}

	public void setExcepcionWsaa(String excepcionWsaa) {
		this.excepcionWsaa = excepcionWsaa;
	}

	public String getExcepcionWsfev1() {
		return excepcionWsfev1;
	}

	public void setExcepcionWsfev1(String excepcionWsfev1) {
		this.excepcionWsfev1 = excepcionWsfev1;
	}

	public String getResultado() {
		return resultado;
	}

	public void setResultado(String resultado) {
		this.resultado = resultado;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public char[] getXmlRequestAfip() {
		return xmlRequestAfip;
	}

	public void setXmlRequestAfip(char[] xmlRequestAfip) {
		this.xmlRequestAfip = xmlRequestAfip;
	}

	public char[] getXmlResponseAfip() {
		return xmlResponseAfip;
	}

	public void setXmlResponseAfip(char[] xmlResponseAfip) {
		this.xmlResponseAfip = xmlResponseAfip;
	}

	public String getTipoComprobante() {
		return tipoComprobante;
	}

	public void setTipoComprobante(String tipoComprobante) {
		this.tipoComprobante = tipoComprobante;
	}

	public String getPuntoVenta() {
		return puntoVenta;
	}

	public void setPuntoVenta(String puntoVenta) {
		this.puntoVenta = puntoVenta;
	}

	public String getNumeroComprobante() {
		return numeroComprobante;
	}

	public void setNumeroComprobante(String numeroComprobante) {
		this.numeroComprobante = numeroComprobante;
	}

	public String getComprobante() {
		return comprobante;
	}

	public void setComprobante(String comprobante) {
		this.comprobante = comprobante;
	}

	public String getFechaComprobante() {
		return fechaComprobante;
	}

	public void setFechaComprobante(String fechaComprobante) {
		this.fechaComprobante = fechaComprobante;
	}

	public String getImporteTotal() {
		return importeTotal;
	}

	public void setImporteTotal(String importeTotal) {
		this.importeTotal = importeTotal;
	}

	public String getCae() {
		return cae;
	}

	public void setCae(String cae) {
		this.cae = cae;
	}

	public String getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(String fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

}
