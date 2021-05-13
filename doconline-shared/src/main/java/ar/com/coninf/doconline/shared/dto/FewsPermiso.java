package ar.com.coninf.doconline.shared.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "dbo.fews_permiso")
public class FewsPermiso implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Integer tipoReg;
	private String idPermiso;
	private Integer dstMerc;
	
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
	@Column(name = "id_permiso")
	public String getIdPermiso() {
		return idPermiso;
	}
	public void setIdPermiso(String idPermiso) {
		this.idPermiso = idPermiso;
	}
	
	@Column(name = "dst_merc")
	public Integer getDstMerc() {
		return dstMerc;
	}
	public void setDstMerc(Integer dstMerc) {
		this.dstMerc = dstMerc;
	}

}
