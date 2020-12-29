package ar.com.coninf.doconline.shared.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "fews_iva")
public class FewsIva implements Serializable{
	private static final long serialVersionUID = 1L;

	private Long id;
	private Integer tipoReg;
	private Integer ivaId;
	private BigDecimal baseImp;
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
	@Column(name = "iva_id")
	public Integer getIvaId() {
		return ivaId;
	}
	public void setIvaId(Integer ivaId) {
		this.ivaId = ivaId;
	}
	
	@Column(name = "base_imp")
	public BigDecimal getBaseImp() {
		return baseImp;
	}
	public void setBaseImp(BigDecimal baseImp) {
		this.baseImp = baseImp;
	}
	
	@Column(name = "importe")
	public BigDecimal getImporte() {
		return importe;
	}
	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}

}
