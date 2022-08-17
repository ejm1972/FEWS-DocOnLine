package ar.com.coninf.doconline.shared.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "LOGS_GENERICOS")
public class LogGenerico implements Serializable {
	private static final long serialVersionUID = 7761586339588476560L;
	
	@Id
	@Column(name = "ID_LOG", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "PROCESO")
	private String proceso;
	
	@Column(name = "DESCRIPCION")
	private String descripcion;
	
	@Column(name = "F_PROCESO")
	private Date fechaProceso;
	
	@Column(name = "ESTADO_PROCESO")
	private Integer estadoProceso;
	
}
