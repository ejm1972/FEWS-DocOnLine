package ar.com.boldt.monedero.shared.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import ar.com.coninf.doconline.shared.anotacion.Key;

@Entity
@Table(name = "PARAMETROS")
public class ParametroDto implements Serializable {

	private static final long serialVersionUID = 2315085238461231052L;

	@Id
	@Column(name = "ID_PARAMETRO", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "PARAMETRO")
	@Key
	private String nombreParametro;
	@Column(name = "DESCRIPCION")
	private String descripcion;
	@Column(name = "ID_UNIDAD")
	private Long unidadId;
	
	public ParametroDto(){
		
	}
	
	public ParametroDto(String param){
		this.nombreParametro = param;
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
	
	public String getNombreParametro() {
		return nombreParametro;
	}
	
	public void setNombreParametro(String nombreParametro) {
		this.nombreParametro = nombreParametro;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public Long getUnidadId() {
		return unidadId;
	}
	
	public void setUnidadId(Long unidadId) {
		this.unidadId = unidadId;
	}
	
	public void setUnidadId(BigDecimal unidadId) {
		this.unidadId = unidadId.longValue();
	}
}
