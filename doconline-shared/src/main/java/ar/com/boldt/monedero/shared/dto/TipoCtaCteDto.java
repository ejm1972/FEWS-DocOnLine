package ar.com.boldt.monedero.shared.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TIPOS_CTACTE")
public class TipoCtaCteDto implements Serializable {

	private static final long serialVersionUID = 3673870268295979081L;

	@Id
	@Column(name = "ID_TIPO_CTACTE", unique = true, nullable = false)
	private Long id;

	@Column(name = "TIPO_CTA_CORRIENTE")
	private String tipoCtaCte;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public void setId(BigDecimal id) {
		this.id = id.longValue();
	}

	public String getTipoCtaCte() {
		return tipoCtaCte;
	}

	public void setTipoCtaCte(String tipoCtaCte) {
		this.tipoCtaCte = tipoCtaCte;
	}
}
