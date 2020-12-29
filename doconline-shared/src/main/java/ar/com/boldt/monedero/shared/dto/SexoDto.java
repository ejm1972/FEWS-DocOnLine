package ar.com.boldt.monedero.shared.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SEXOS")
public class SexoDto implements Serializable {

	private static final long serialVersionUID = 5601920894808584437L;

	@Id
	@Column(name = "ID_SEXO", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "SEXO")
	private String nombreSexo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setId(BigDecimal id) {
		this.id = id.longValue();
	}

	public String getNombreSexo() {
		return nombreSexo;
	}

	public void setNombreSexo(String nombreSexo) {
		this.nombreSexo = nombreSexo;
	}
}
