package ar.com.coninf.doconline.shared.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import ar.com.coninf.doconline.shared.anotacion.InsertOptional;
import ar.com.coninf.doconline.shared.anotacion.Key;

@Entity
@Table(name = "PUNTO_VENTA")
public class PuntoVentaDto implements Serializable {

	private static final long serialVersionUID = 8064007187634940297L;

	@Id
	@InsertOptional
	@Column(name = "ID_PUNTO_VENTA", unique = true, nullable = false)
	private Long idPuntoVenta;
	
	@Key
	@Column(name = "ID_INTERFAZ")
	private Long idInterfaz;
	
	@Key
	@Column(name = "PUNTO_VENTA")
	private String puntoVenta;
	
	@Column(name = "ACTIVADO")
	private String activo;

	@Column(name = "FLG_CONTROL")
	private String flgControl;

	public Long getIdPuntoVenta() {
		return idPuntoVenta;
	}

	public void setIdPuntoVenta(Long idPuntoVenta) {
		this.idPuntoVenta = idPuntoVenta;
	}
	public void setIdPuntoVenta(BigDecimal idPuntoVenta) {
		this.idPuntoVenta = idPuntoVenta.longValue();
	}
	public void setIdPuntoVenta(Integer idPuntoVenta) {
		this.idPuntoVenta = idPuntoVenta.longValue();
	}
	
	public Long getIdInterfaz() {
		return idInterfaz;
	}

	public void setIdInterfaz(BigDecimal idInterfaz) {
		this.idInterfaz = idInterfaz.longValue();
	}
	public void setIdInterfaz(Long idInterfaz) {
		this.idInterfaz = idInterfaz;
	}
	public void setIdInterfaz(Integer idInterfaz) {
		this.idInterfaz = idInterfaz.longValue();
	}
	
	public String getPuntoVenta() {
		return puntoVenta;
	}

	public void setPuntoVenta(String puntoVenta) {
		this.puntoVenta = puntoVenta;
	}

	public String getFlgControl() {
		return flgControl;
	}

	public void setFlgControl(String flgControl) {
		this.flgControl = flgControl;
	}

	public String getActivo() {
		return activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}

}
