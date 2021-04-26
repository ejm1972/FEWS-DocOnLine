package ar.com.coninf.doconline.shared.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "dbo.fews_periodo_asoc")
public class FewsPeriodoAsociado implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Integer tipoReg;
	private String fechaDesde;
	private String fechaHasta;
	
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

	@Column(name = "fecha_desde")
	public String getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(String fecha) {
		this.fechaDesde = fecha;
	}

	@Column(name = "fecha_hasta")
	public String getFechaHasta() {
		return fechaHasta;
	}
	public void setFechaHasta(String fecha) {
		this.fechaHasta = fecha;
	}
	
}
