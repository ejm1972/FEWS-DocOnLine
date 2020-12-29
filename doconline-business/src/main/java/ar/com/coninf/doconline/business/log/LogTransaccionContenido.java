package ar.com.coninf.doconline.business.log;

import java.util.Date;


public class LogTransaccionContenido {

	private Date fechaInicioOp;
	private Date fechaFinOp;
	private String operacion;
	private String xmlEntrada;
	private String xmlSalida;
	private String ctxNroTransaccion;
	private String ctxInterfaz;
	private String ctxServicio;
	private Date ctxFechaTransaccion;
	
	private String codigo;
	private String descripcion;
	private String observacion;

	private String excepcionWsaa;
	
	private String excepcionWsfev1;
	private String errMsg;
	private String obs;
	private String xmlRequest;
	private String xmlResponse;

	private String tipoCbte;
	private String ptoVtaCbte;
	private String nroCbte;
	
	private String cae;
	private String fechaVencimiento;

	private String resultado;

	private String cbte;
	private String fechaCbte;
	private String impTotal;

	public String getXmlEntrada() {
		return xmlEntrada;
	}
	public Date getFechaInicioOp() {
		return fechaInicioOp;
	}
	public void setFechaInicioOp(Date fechaInicioOp) {
		this.fechaInicioOp = fechaInicioOp;
	}
	public Date getFechaFinOp() {
		return fechaFinOp;
	}
	public void setFechaFinOp(Date fechaFinOp) {
		this.fechaFinOp = fechaFinOp;
	}
	public String getOperacion() {
		return operacion;
	}
	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}
	public void setXmlEntrada(String xmlEntrada) {
		this.xmlEntrada = xmlEntrada;
	}
	public String getXmlSalida() {
		return xmlSalida;
	}
	public void setXmlSalida(String xmlSalida) {
		this.xmlSalida = xmlSalida;
	}
	public String getCtxNroTransaccion() {
		return ctxNroTransaccion;
	}
	public void setCtxNroTransaccion(String ctxNroTransaccion) {
		this.ctxNroTransaccion = ctxNroTransaccion;
	}
	public String getCtxInterfaz() {
		return ctxInterfaz;
	}
	public void setCtxInterfaz(String ctxInterfaz) {
		this.ctxInterfaz = ctxInterfaz;
	}
	public String getCtxServicio() {
		return ctxServicio;
	}
	public void setCtxServicio(String ctxServicio) {
		this.ctxServicio = ctxServicio;
	}
	public Date getCtxFechaTransaccion() {
		return ctxFechaTransaccion;
	}
	public void setCtxFechaTransaccion(Date ctxFechaTransaccion) {
		this.ctxFechaTransaccion = ctxFechaTransaccion;
	}
	
	/*-------------------------------------------------------*/
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
	/*-------------------------------------------------------*/
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
	/*-------------------------------------------------------*/
	public String getXmlRequest() {
		return xmlRequest;
	}
	public void setXmlRequest(String xmlRequest) {
		this.xmlRequest = xmlRequest;
	}
	public String getXmlResponse() {
		return xmlResponse;
	}
	public void setXmlResponse(String xmlResponse) {
		this.xmlResponse = xmlResponse;
	}
	/*-------------------------------------------------------*/
	public String getTipoCbte() {
		return tipoCbte;
	}
	public void setTipoCbte(String tipoCbte) {
		this.tipoCbte = tipoCbte;
	}
	public String getPtoVtaCbte() {
		return ptoVtaCbte;
	}
	public void setPtoVtaCbte(String ptoVtaCbte) {
		this.ptoVtaCbte = ptoVtaCbte;
	}
	public String getNroCbte() {
		return nroCbte;
	}
	public void setNroCbte(String nroCbte) {
		this.nroCbte = nroCbte;
	}
	/*-------------------------------------------------------*/
	public String getCbte() {
		return cbte;
	}
	public void setCbte(String cbte) {
		this.cbte = cbte;
	}
	/*-------------------------------------------------------*/
	public String getFechaCbte() {
		return fechaCbte;
	}
	public void setFechaCbte(String fechaCbte) {
		this.fechaCbte = fechaCbte;
	}
	public String getImpTotal() {
		return impTotal;
	}
	public void setImpTotal(String impTotal) {
		this.impTotal = impTotal;
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
	/*-------------------------------------------------------*/

}
