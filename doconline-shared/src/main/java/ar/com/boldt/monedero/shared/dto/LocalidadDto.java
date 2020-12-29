package ar.com.boldt.monedero.shared.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "LOCALIDADES")
public class LocalidadDto implements Serializable {

	private static final long serialVersionUID = 5601920894808584437L;

	@Id
	@Column(name = "ID_LOCALIDAD", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "NOMBRE")
	private String nombre;

	@Column(name = "ID_PROVINCIA")
	private String idProvincia;

	@Transient
	ProvinciaDto provinciaDto;

	public String getIdProvincia() {
		return idProvincia;
	}

	public void setIdProvincia(String idProvincia) {
		this.idProvincia = idProvincia;
	}

	public ProvinciaDto getProvinciaDto() {
		return provinciaDto;
	}

	public void setProvinciaDto(ProvinciaDto provinciaDto) {
		this.provinciaDto = provinciaDto;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setId(BigDecimal id) {
		this.id = id.longValue();
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
