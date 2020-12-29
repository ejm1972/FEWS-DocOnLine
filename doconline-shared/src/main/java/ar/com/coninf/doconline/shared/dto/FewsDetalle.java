package ar.com.coninf.doconline.shared.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "fews_detalle")
public class FewsDetalle implements Serializable{
	private static final long serialVersionUID = 1L;

	private Long id;
	private Integer tipoReg; 
	private Integer itm;
	private String codigo;
	private BigDecimal qty;
	private Integer umed;
	private BigDecimal precio;
	private BigDecimal impTotal;
	private Integer ivaId;
	private String ds;
	private String ncm;
	private String sec;
	private BigDecimal bonif;
	private BigDecimal impIva;
	private Integer uMtx;
	private String codMtx;
	private String datoA;
	private String datoB;
	private String datoC;
	private String datoD;
	private String datoE;
	
	@Id
	@Column(name = "id")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "tipo_reg")
	public Integer getTipoReg() {
		return tipoReg;
	}
	public void setTipoReg(Integer tipoReg) {
		this.tipoReg = tipoReg;
	}

	@Id
	@Column(name = "itm")
	public Integer getItm() {
		return itm;
	}
	public void setItm(Integer itm) {
		this.itm = itm;
	}
	
	@Id
	@Column(name = "codigo")
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	@Column(name = "ds")
	public String getDs() {
		return ds;
	}
	public void setDs(String ds) {
		this.ds = ds;
	}
	
	@Column(name = "qty")
	public BigDecimal getQty() {
		return qty;
	}
	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}
	
	@Column(name = "umed")
	public Integer getUmed() {
		return umed;
	}
	public void setUmed(Integer umed) {
		this.umed = umed;
	}
	
	@Column(name = "precio")
	public BigDecimal getPrecio() {
		return precio;
	}
	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}
	
	@Column(name = "imp_total")
	public BigDecimal getImpTotal() {
		return impTotal;
	}
	public void setImpTotal(BigDecimal impTotal) {
		this.impTotal = impTotal;
	}
	
	@Column(name = "iva_id")
	public Integer getIvaId() {
		return ivaId;
	}
	public void setIvaId(Integer ivaId) {
		this.ivaId = ivaId;
	}
	
	@Column(name = "bonif")
	public BigDecimal getBonif() {
		return bonif;
	}
	public void setBonif(BigDecimal bonif) {
		this.bonif = bonif;
	}
	
	@Column(name = "ncm")
	public String getNcm() {
		return ncm;
	}
	public void setNcm(String ncm) {
		this.ncm = ncm;
	}
	
	@Column(name = "sec")
	public String getSec() {
		return sec;
	}
	public void setSec(String sec) {
		this.sec = sec;
	}
	
	@Column(name = "u_mtx")
	public Integer getuMtx() {
		return uMtx;
	}
	public void setuMtx(Integer uMtx) {
		this.uMtx = uMtx;
	}
	
	@Column(name = "cod_mtx")
	public String getCodMtx() {
		return codMtx;
	}
	public void setCodMtx(String codMtx) {
		this.codMtx = codMtx;
	}
	
	@Column(name = "dato_a")
	public String getDatoA() {
		return datoA;
	}
	public void setDatoA(String datoA) {
		this.datoA = datoA;
	}
	
	@Column(name = "dato_b")
	public String getDatoB() {
		return datoB;
	}
	public void setDatoB(String datoB) {
		this.datoB = datoB;
	}
	
	@Column(name = "dato_c")
	public String getDatoC() {
		return datoC;
	}
	public void setDatoC(String datoC) {
		this.datoC = datoC;
	}
	
	@Column(name = "dato_d")
	public String getDatoD() {
		return datoD;
	}
	public void setDatoD(String datoD) {
		this.datoD = datoD;
	}
	
	@Column(name = "dato_e")
	public String getDatoE() {
		return datoE;
	}
	public void setDatoE(String datoE) {
		this.datoE = datoE;
	}
	
	@Column(name = "imp_iva")
	public BigDecimal getImpIva() {
		return impIva;
	}
	public void setImpIva(BigDecimal impIva) {
		this.impIva = impIva;
	}

}
