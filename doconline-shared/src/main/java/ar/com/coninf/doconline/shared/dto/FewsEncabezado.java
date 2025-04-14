package ar.com.coninf.doconline.shared.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import ar.com.coninf.doconline.shared.anotacion.InsertOptional;

@Entity
@Table(name = "fews_encabezado")
public class FewsEncabezado implements Serializable {
	private static final long serialVersionUID = 1L;

	@InsertOptional
	private Long id;
	private Integer tipoReg;
	private String webservice;
	private Long idInterfaz;
	private String cuit;
	private Integer concepto;
	private String movimiento;
	private String fechaCbte;
	private Integer tipoCbte;
	private Integer puntoVta;
	private Integer cbteNro;
	
	private Integer tipoDoc;
	private Long nroDoc;
	
	private Integer tipoExpo;
	private String permisoExistente;
	private Integer dstCmp;
	private String idImpositivo;
	
	private String cuitPaisCliente;
	private String nombreCliente;
	private String domicilioCliente;
	private String telefonoCliente;
	private String localidadCliente;
	private String provinciaCliente;
	
	private BigDecimal impTotal;
	private BigDecimal impTotConc;
	private BigDecimal impOpEx;
	private BigDecimal impNeto;
	private BigDecimal impIva;
	private BigDecimal impTrib;
	private BigDecimal imptoLiq;
	private BigDecimal imptoLiqRni;
	private BigDecimal imptoPerc;
	private BigDecimal impIibb;
	private BigDecimal imptoPercMun;
	private BigDecimal impInternos;
	
	private String monedaId;
	private BigDecimal monedaCtz;
	
	private Integer condicionIvaReceptorId;
	private String cancelaMismaMonedaExt;

	private String obsComerciales;
	private String obsGenerales;
	private String formaPago;
	private String incoterms;
	private String incotermsDs;
	private String idiomaCbte;
	private String estadoImportacion;
	
	private String zona;
	private String fechaVencPago;
	private Integer prestaServ;
	private String fechaServDesde;
	private String fechaServHasta;
	
	private Long cae;
	private String fechaVto;
	private String resultado;
	private String reproceso;
	private String motivo;

	private Integer formatoId;
	private String email;
	private String pdf;
	
	private String errCode;
	private String errMsg;
	
	private String datoAdicional1;
	private String datoAdicional2;
	private String datoAdicional3;
	private String datoAdicional4;
	private String datoAdicional5;
	private String datoAdicional6;
	private String datoAdicional7;
	
	@Id
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setId(Integer id) {
		this.id = id.longValue();
	}

	@Column(name = "tipo_reg")
	public Integer getTipoReg() {
		return tipoReg;
	}
	public void setTipoReg(Integer tipoReg) {
		this.tipoReg = tipoReg;
	}

	@Column(name = "webservice")
	public String getWebservice() {
		return webservice;
	}
	public void setWebservice(String webservice) {
		this.webservice = webservice;
	}

	@Column(name = "id_interfaz")
	public Long getIdInterfaz() {
		return idInterfaz;
	}
	public void setIdInterfaz(Integer id) {
		this.idInterfaz = id.longValue();
	}
	public void setIdInterfaz(Long id) {
		this.idInterfaz = id;
	}

	@Column(name = "concepto")
	public Integer getConcepto() {
		return concepto;
	}
	public void setConcepto(Integer concepto) {
		this.concepto = concepto;
	}

	@Column(name = "movimiento")
	public String getMovimiento() {
		return movimiento;
	}
	public void setMovimiento(String movimiento) {
		this.movimiento = movimiento;
	}

	@Column(name = "fecha_cbte")
	public String getFechaCbte() {
		return fechaCbte;
	}
	public void setFechaCbte(String fechaCbte) {
		this.fechaCbte = fechaCbte;
	}

	@Column(name = "tipo_cbte")
	public Integer getTipoCbte() {
		return tipoCbte;
	}
	public void setTipoCbte(Integer tipoCbte) {
		this.tipoCbte = tipoCbte;
	}

	@Column(name = "punto_vta")
	public Integer getPuntoVta() {
		return puntoVta;
	}
	public void setPuntoVta(Integer puntoVta) {
		this.puntoVta = puntoVta;
	}

	@Column(name = "cbte_nro")
	public Integer getCbteNro() {
		return cbteNro;
	}
	public void setCbteNro(Integer cbteNro) {
		this.cbteNro = cbteNro;
	}

	@Column(name = "tipo_expo")
	public Integer getTipoExpo() {
		return tipoExpo;
	}
	public void setTipoExpo(Integer tipoExpo) {
		this.tipoExpo = tipoExpo;
	}

	@Column(name = "permiso_existente")
	public String getPermisoExistente() {
		return permisoExistente;
	}
	public void setPermisoExistente(String permisoExistente) {
		this.permisoExistente = permisoExistente;
	}

	@Column(name = "dst_cmp")
	public Integer getDstCmp() {
		return dstCmp;
	}
	public void setDstCmp(Integer dstCmp) {
		this.dstCmp = dstCmp;
	}

	@Column(name = "nombre_cliente")
	public String getNombreCliente() {
		return nombreCliente;
	}
	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	@Column(name = "cuit_pais_cliente")
	public String getCuitPaisCliente() {
		return cuitPaisCliente;
	}
	public void setCuitPaisCliente(String cuitPaisCliente) {
		this.cuitPaisCliente = cuitPaisCliente;
	}
	
	@Column(name = "tipo_doc")
	public Integer getTipoDoc() {
		return tipoDoc;
	}
	public void setTipoDoc(Integer tipoDoc) {
		this.tipoDoc = tipoDoc;
	}

	@Column(name = "nro_doc")
	public Long getNroDoc() {
		return nroDoc;
	}
	public void setNroDoc(Long nroDoc) {
		this.nroDoc = nroDoc;
	}

	@Column(name = "domicilio_cliente")
	public String getDomicilioCliente() {
		return domicilioCliente;
	}
	public void setDomicilioCliente(String domicilioCliente) {
		this.domicilioCliente = domicilioCliente;
	}

	@Column(name = "id_impositivo")
	public String getIdImpositivo() {
		return idImpositivo;
	}
	public void setIdImpositivo(String idImpositivo) {
		this.idImpositivo = idImpositivo;
	}

	@Column(name = "imp_total")
	public BigDecimal getImpTotal() {
		return impTotal;
	}
	public void setImpTotal(BigDecimal impTotal) {
		this.impTotal = impTotal;
	}
	
	@Column(name = "imp_tot_conc")
	public BigDecimal getImpTotConc() {
		return impTotConc;
	}
	public void setImpTotConc(BigDecimal impTotConc) {
		this.impTotConc = impTotConc;
	}
	
	@Column(name = "imp_neto")
	public BigDecimal getImpNeto() {
		return impNeto;
	}
	public void setImpNeto(BigDecimal impNeto) {
		this.impNeto = impNeto;
	}
	
	@Column(name = "impto_liq")
	public BigDecimal getImptoLiq() {
		return imptoLiq;
	}
	public void setImptoLiq(BigDecimal imptoLiq) {
		this.imptoLiq = imptoLiq;
	}
	
	@Column(name = "impto_liq_rni")
	public BigDecimal getImptoLiqRni() {
		return imptoLiqRni;
	}
	public void setImptoLiqRni(BigDecimal imptoLiqRni) {
		this.imptoLiqRni = imptoLiqRni;
	}
	
	@Column(name = "imp_op_ex")
	public BigDecimal getImpOpEx() {
		return impOpEx;
	}
	public void setImpOpEx(BigDecimal impOpEx) {
		this.impOpEx = impOpEx;
	}

	@Column(name = "impto_perc")
	public BigDecimal getImptoPerc() {
		return imptoPerc;
	}
	public void setImptoPerc(BigDecimal imptoPerc) {
		this.imptoPerc = imptoPerc;
	}
	
	@Column(name = "imp_iibb")
	public BigDecimal getImpIibb() {
		return impIibb;
	}
	public void setImpIibb(BigDecimal impIibb) {
		this.impIibb = impIibb;
	}
	
	@Column(name = "impto_perc_mun")
	public BigDecimal getImptoPercMun() {
		return imptoPercMun;
	}
	public void setImptoPercMun(BigDecimal imptoPercMun) {
		this.imptoPercMun = imptoPercMun;
	}
	
	@Column(name = "imp_internos")
	public BigDecimal getImpInternos() {
		return impInternos;
	}
	public void setImpInternos(BigDecimal impInternos) {
		this.impInternos = impInternos;
	}
	
	@Column(name = "imp_trib")
	public BigDecimal getImpTrib() {
		return impTrib;
	}
	public void setImpTrib(BigDecimal impTrib) {
		this.impTrib = impTrib;
	}
	
	@Column(name = "imp_iva")
	public BigDecimal getImpIva() {
		return impIva;
	}
	public void setImpIva(BigDecimal impIva) {
		this.impIva = impIva;
	}
	
	@Column(name = "moneda_id")
	public String getMonedaId() {
		return monedaId;
	}
	public void setMonedaId(String monedaId) {
		this.monedaId = monedaId;
	}

	@Column(name = "moneda_ctz")
	public BigDecimal getMonedaCtz() {
		return monedaCtz;
	}
	public void setMonedaCtz(BigDecimal monedaCtz) {
		this.monedaCtz = monedaCtz;
	}
	
	@Column(name = "obs_comerciales")
	public String getObsComerciales() {
		return obsComerciales;
	}
	public void setObsComerciales(String obsComerciales) {
		this.obsComerciales = obsComerciales;
	}

	@Column(name = "obs_generales")
	public String getObsGenerales() {
		return obsGenerales;
	}
	public void setObsGenerales(String obsGenerales) {
		this.obsGenerales = obsGenerales;
	}

	@Column(name = "forma_pago")
	public String getFormaPago() {
		return formaPago;
	}
	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}

	@Column(name = "incoterms")
	public String getIncoterms() {
		return incoterms;
	}
	public void setIncoterms(String incoTerms) {
		this.incoterms = incoTerms;
	}

	@Column(name = "incoterms_ds")
	public String getIncotermsDs() {
		return incotermsDs;
	}
	public void setIncotermsDs(String incoTermsDs) {
		this.incotermsDs = incoTermsDs;
	}

	@Column(name = "idioma_cbte")
	public String getIdiomaCbte() {
		return idiomaCbte;
	}
	public void setIdiomaCbte(String idiomaCbte) {
		this.idiomaCbte = idiomaCbte;
	}

	@Column(name = "zona")
	public String getZona() {
		return zona;
	}
	public void setZona(String zona) {
		this.zona = zona;
	}

	@Column(name = "fecha_venc_pago")
	public String getFechaVencPago() {
		return fechaVencPago;
	}
	public void setFechaVencPago(String fechaVencPago) {
		this.fechaVencPago = fechaVencPago;
	}

	@Column(name = "presta_serv")
	public Integer getPrestaServ() {
		return prestaServ;
	}
	public void setPrestaServ(Integer prestaServ) {
		this.prestaServ = prestaServ;
	}

	@Column(name = "fecha_serv_desde")
	public String getFechaServDesde() {
		return fechaServDesde;
	}
	public void setFechaServDesde(String fechaServDesde) {
		this.fechaServDesde = fechaServDesde;
	}

	@Column(name = "fecha_serv_hasta")
	public String getFechaServHasta() {
		return fechaServHasta;
	}
	public void setFechaServHasta(String fechaServHasta) {
		this.fechaServHasta = fechaServHasta;
	}

	@Column(name = "fecha_vto")
	public String getFechaVto() {
		return fechaVto;
	}
	public void setFechaVto(String fechaVto) {
		this.fechaVto = fechaVto;
	}

	@Column(name = "reproceso")
	public String getReproceso() {
		return reproceso;
	}
	public void setReproceso(String reproceso) {
		this.reproceso = reproceso;
	}

	@Column(name = "motivo")
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	@Column(name = "telefono_cliente")
	public String getTelefonoCliente() {
		return telefonoCliente;
	}
	public void setTelefonoCliente(String telefonoCliente) {
		this.telefonoCliente = telefonoCliente;
	}

	@Column(name = "localidad_cliente")
	public String getLocalidadCliente() {
		return localidadCliente;
	}
	public void setLocalidadCliente(String localidadCliente) {
		this.localidadCliente = localidadCliente;
	}

	@Column(name = "provincia_cliente")
	public String getProvinciaCliente() {
		return provinciaCliente;
	}
	public void setProvinciaCliente(String provinciaCliente) {
		this.provinciaCliente = provinciaCliente;
	}

	@Column(name = "formato_id")
	public Integer getFormatoId() {
		return formatoId;
	}
	public void setFormatoId(Integer formatoId) {
		this.formatoId = formatoId;
	}

	@Column(name = "email")
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "pdf")
	public String getPdf() {
		return pdf;
	}
	public void setPdf(String pdf) {
		this.pdf = pdf;
	}

	@Column(name = "err_code")
	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	@Column(name = "dato_adicional1")
	public String getDatoAdicional1() {
		return datoAdicional1;
	}
	public void setDatoAdicional1(String datoAdicional1) {
		this.datoAdicional1 = datoAdicional1;
	}

	@Column(name = "dato_adicional2")
	public String getDatoAdicional2() {
		return datoAdicional2;
	}
	public void setDatoAdicional2(String datoAdicional2) {
		this.datoAdicional2 = datoAdicional2;
	}

	@Column(name = "dato_adicional3")
	public String getDatoAdicional3() {
		return datoAdicional3;
	}
	public void setDatoAdicional3(String datoAdicional3) {
		this.datoAdicional3 = datoAdicional3;
	}

	@Column(name = "dato_adicional4")
	public String getDatoAdicional4() {
		return datoAdicional4;
	}
	public void setDatoAdicional4(String datoAdicional4) {
		this.datoAdicional4 = datoAdicional4;
	}

	@Column(name = "dato_adicional5")
	public String getDatoAdicional5() {
		return datoAdicional5;
	}
	public void setDatoAdicional5(String datoAdicional5) {
		this.datoAdicional5 = datoAdicional5;
	}

	@Column(name = "dato_adicional6")
	public String getDatoAdicional6() {
		return datoAdicional6;
	}
	public void setDatoAdicional6(String datoAdicional6) {
		this.datoAdicional6 = datoAdicional6;
	}

	@Column(name = "dato_adicional7")
	public String getDatoAdicional7() {
		return datoAdicional7;
	}
	public void setDatoAdicional7(String datoAdicional7) {
		this.datoAdicional7 = datoAdicional7;
	}

	@Column(name = "cae")
	public Long getCae() {
		return cae;
	}
	public void setCae(Long cae) {
		this.cae = cae;
	}

	@Column(name = "resultado")
	public String getResultado() {
		return resultado;
	}
	public void setResultado(String resultado) {
		this.resultado = resultado;
	}
	
	@Column(name = "err_msg")
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	
	@Column(name = "estado_importacion")
	public String getEstadoImportacion() {
		return estadoImportacion;
	}
	public void setEstadoImportacion(String estadoImportacion) {
		this.estadoImportacion = estadoImportacion;
	}

	@Column(name = "cuit")
	public String getCuit() {
		return cuit;
	}
	public void setCuit(String cuit) {
		this.cuit = cuit;
	}
	
	@Column(name = "condicion_iva_receptor_id")
	public Integer getCondicionIvaReceptorId() {
		return condicionIvaReceptorId;
	}
	public void setCondicionIvaReceptorId(Integer condicionIvaReceptorId) {
		this.condicionIvaReceptorId = condicionIvaReceptorId;
	}

	@Column(name = "cancela_misma_moneda_ext")
	public String getCancelaMismaMonedaExt() {
		return cancelaMismaMonedaExt;
	}
	public void setCancelaMismaMonedaExt(String cancelaMismaMonedaExt) {
		this.cancelaMismaMonedaExt = cancelaMismaMonedaExt;
	}
}

