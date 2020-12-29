package ar.com.boldt.monedero.shared.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PARAMETROS_VALOR")
public class ParametroValorDto implements Serializable {

	private static final long serialVersionUID = 2315085238461231052L;

	@Id
	@Column(name = "ID_PARAMETRO_VALOR", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "ID_PARAMETRO")
	private Long parametroId;
	@Column(name = "VALOR")
	private String valor;
	@Column(name = "F_VIGENCIA_DESDE")
	private Date fechaVigenciaDesde;
	@Column(name = "F_VIGENCIA_HASTA")
	private Date fechaVigenciaHasta;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public void setId(BigDecimal id) {
		this.id = id.longValue();
	}

	public Long getParametroId() {
		return parametroId;
	}
	
	public void setParametroId(Long parametroId) {
		this.parametroId = parametroId;
	}
	
	public String getValor() {
		return valor;
	}
	
	public void setValor(String valor) {
		this.valor = valor;
	}
	
	public Date getFechaVigenciaDesde() {
		return fechaVigenciaDesde;
	}
	
	public void setFechaVigenciaDesde(Date fechaVigenciaDesde) {
		this.fechaVigenciaDesde = fechaVigenciaDesde;
	}
	
	public Date getFechaVigenciaHasta() {
		return fechaVigenciaHasta;
	}
	
	public void setFechaVigenciaHasta(Date fechaVigenciaHasta) {
		this.fechaVigenciaHasta = fechaVigenciaHasta;
	}
}
