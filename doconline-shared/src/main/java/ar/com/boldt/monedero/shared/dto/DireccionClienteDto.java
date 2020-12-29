package ar.com.boldt.monedero.shared.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.com.coninf.doconline.shared.anotacion.InsertOptional;

@Entity
@Table(name = "DIRECCIONES_CLIENTES")
public class DireccionClienteDto implements Serializable {

	private static final long serialVersionUID = 1056541947544190897L;

	@Id
	@Column(name = "ID_DIRECCION", unique = true, nullable = false)
	private Long id;

	@Column(name = "CALLE")
	private String calle;
	
	@Column(name = "NUMERO")
	private String numero;
	
	@InsertOptional
	@Column(name = "PISO")
	private String piso;
	
	@InsertOptional
	@Column(name = "DEPARTAMENTO")
	private String departamento;
	
	@Column(name = "ID_LOCALIDAD")
	private Long idLocalidad;

	@InsertOptional
	@Column(name = "LOCALIDAD_ALTERNATIVA")
	private String localidadAlternativa;
	
	@Column(name = "COD_POSTAL")
	private String codPostal;

	@Transient
	LocalidadDto localidadDto;

	public LocalidadDto getLocalidadDto() {
		return localidadDto;
	}

	public void setLocalidadDto(LocalidadDto localidadDto) {
		this.localidadDto = localidadDto;
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

	public String getCalle() {
		return calle;
	}

	public void setCalle(String calle) {
		this.calle = calle;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getPiso() {
		return piso;
	}

	public void setPiso(String piso) {
		this.piso = piso;
	}

	public String getDepartamento() {
		return departamento;
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

	public Long getIdLocalidad() {
		return idLocalidad;
	}

	public void setIdLocalidad(Long idLocalidad) {
		this.idLocalidad = idLocalidad;
	}
	
	public String getLocalidadAlternativa() {
		return localidadAlternativa;
	}

	public void setLocalidadAlternativa(String localidadAlternativa) {
		this.localidadAlternativa = localidadAlternativa;
	}

	public String getCodPostal() {
		return codPostal;
	}

	public void setCodPostal(String codPostal) {
		this.codPostal = codPostal;
	}
}
