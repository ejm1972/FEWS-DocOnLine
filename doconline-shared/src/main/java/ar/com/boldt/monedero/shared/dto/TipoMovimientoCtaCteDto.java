package ar.com.boldt.monedero.shared.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TIPOS_MOVIMIENTOS_CTACTE")
public class TipoMovimientoCtaCteDto implements Serializable {
	private static final long serialVersionUID = 2676054550147244946L;

	@Id
	@Column(name = "ID_TIPO_MOVIMIENTO_CTACTE", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "TIPO_MOVIMIENTO")
	private String nombreTipoMovimiento;
	
	@Column(name = "FACTOR")
	private Integer factor;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public void setId(BigDecimal id) {
		this.id = id.longValue();
	}

	public String getNombreTipoMovimiento() {
		return nombreTipoMovimiento;
	}

	public void setNombreTipoMovimiento(String nombreTipoMovimiento) {
		this.nombreTipoMovimiento = nombreTipoMovimiento;
	}

	public Integer getFactor() {
		return factor;
	}

	public void setFactor(Integer factor) {
		this.factor = factor;
	}
	public void setFactor(BigDecimal factor) {
		this.factor = factor.intValue();
	}
}
