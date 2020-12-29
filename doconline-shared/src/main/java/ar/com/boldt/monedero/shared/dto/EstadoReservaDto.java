package ar.com.boldt.monedero.shared.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ESTADOS_RESERVAS")
public class EstadoReservaDto implements Serializable {

	private static final long serialVersionUID = -6498276405714583621L;

	@Id
	@Column(name = "ID_ESTADO_RESERVA", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "ESTADO_RESERVA") 
	private String nombreEstadoReserva;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setId(BigDecimal id) {
		this.id = id.longValue();
	}

	public String getNombreEstadoReserva() {
		return nombreEstadoReserva;
	}

	public void setNombreEstadoReserva(String nombreEstadoReserva) {
		this.nombreEstadoReserva = nombreEstadoReserva;
	}
}
