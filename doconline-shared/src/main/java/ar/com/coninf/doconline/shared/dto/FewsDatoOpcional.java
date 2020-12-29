package ar.com.coninf.doconline.shared.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "dbo.fews_dato_opc")
public class FewsDatoOpcional implements Serializable{
	private static final long serialVersionUID = 1L;

	private Long id;
	private Integer tipoReg;
	private Integer opcionalId;
	private String valor;
	
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
	@Column(name = "opcional_id")
	public Integer getOpcionalId() {
		return opcionalId;
	}
	public void setOpcionalId(Integer opcionalId) {
		this.opcionalId = opcionalId;
	}
	
	@Column(name = "valor")
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}

}
