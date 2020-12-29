package ar.com.coninf.doconline.shared.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "dbo.fews_tributo")
public class FewsTributo implements Serializable{
	private static final long serialVersionUID = 1L;

	private Long id;
	private Integer tipoReg;
	private Integer tributoId;
	private String tributoDesc;
	private BigDecimal baseImp;
	private BigDecimal alic;
	private BigDecimal importe;

	@Id
	@Column(name = "id")
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

	@Id
	@Column(name = "tributo_id")
	public Integer getTributoId() {
		return tributoId;
	}
	public void setTributoId(Integer tributoId) {
		this.tributoId = tributoId;
	}

	@Column(name = "tributo_desc")
	public String getTributoDesc() {
		return tributoDesc;
	}
	public void setTributoDesc(String tributoDesc) {
		this.tributoDesc = tributoDesc;
	}
	
	@Column(name = "base_imp")
	public BigDecimal getBaseImp() {
		return baseImp;
	}
	public void setBaseImp(BigDecimal baseImp) {
		this.baseImp = baseImp;
	}
	
	@Column(name = "alic")
	public BigDecimal getAlic() {
		return alic;
	}
	public void setAlic(BigDecimal alic) {
		this.alic = alic;
	}
	
	@Column(name = "importe")
	public BigDecimal getImporte() {
		return importe;
	}
	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}

}
