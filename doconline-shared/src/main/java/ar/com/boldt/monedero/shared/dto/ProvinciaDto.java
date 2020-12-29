package ar.com.boldt.monedero.shared.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PROVINCIAS")
public class ProvinciaDto implements Serializable {

	private static final long serialVersionUID = -3043806241994134663L;

	@Id
	@Column(name = "ID_PROVINCIA", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "PROVINCIA")
	private String nombreProvincia;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setId(BigDecimal id) {
		this.id = id.longValue();
	}
	
	public String getNombreProvincia() {
		return nombreProvincia;
	}

	public void setNombreProvincia(String nombreProvincia) {
		this.nombreProvincia = nombreProvincia;
	}
}
