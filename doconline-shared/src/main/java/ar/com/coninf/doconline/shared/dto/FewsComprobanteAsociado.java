package ar.com.coninf.doconline.shared.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "dbo.fews_cmp_asoc")
public class FewsComprobanteAsociado implements Serializable{
	private static final long serialVersionUID = 1L;

	private Long id;
	private Integer tipoReg;
	private Integer tipoCbte;
	private Integer puntoVta;
	private Integer cbteNro;
	private String cuit;
	private String fechaCbte;
	
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
	@Column(name = "tipo_cbte")
	public Integer getTipoCbte() {
		return tipoCbte;
	}
	public void setTipoCbte(Integer tipoCbte) {
		this.tipoCbte = tipoCbte;
	}

	@Id
	@Column(name = "punto_vta")
	public Integer getPuntoVta() {
		return puntoVta;
	}
	public void setPuntoVta(Integer puntoVta) {
		this.puntoVta = puntoVta;
	}

	@Id
	@Column(name = "cbte_nro")
	public Integer getCbteNro() {
		return cbteNro;
	}
	public void setCbteNro(Integer cbteNro) {
		this.cbteNro = cbteNro;
	}

	@Column(name = "cuit")
	public String getCuit() {
		return cuit;
	}
	public void setCuit(String cuit) {
		this.cuit = cuit;
	}

	@Column(name = "fecha_cbte")
	public String getFechaCbte() {
		return fechaCbte;
	}
	public void setFechaCbte(String fechaCbte) {
		this.fechaCbte = fechaCbte;
	}

}
