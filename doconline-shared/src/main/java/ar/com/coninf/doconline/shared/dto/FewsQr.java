package ar.com.coninf.doconline.shared.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "dbo.fews_qr")
public class FewsQr implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	private Long id;
	
	@Column(name = "texto_qr")
	private String textoQr;
	
	@Column(name = "imagen_qr")
	private byte[] imagenQr;
	
}
