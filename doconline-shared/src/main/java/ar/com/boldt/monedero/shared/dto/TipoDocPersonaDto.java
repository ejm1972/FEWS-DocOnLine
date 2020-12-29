package ar.com.boldt.monedero.shared.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TIPOS_DOC_PERSONAS")
public class TipoDocPersonaDto implements Serializable {

	private static final long serialVersionUID = -9055470641003811500L;

	@Id
	@Column(name = "ID_TIPO_DOC_PERSONA", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "TIPO_DOCUMENTO")
	private String tipoDocumento;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setId(BigDecimal id) {
		this.id = id.longValue();
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
}
