package ar.com.boldt.monedero.shared.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.com.coninf.doconline.shared.anotacion.InsertOptional;
import ar.com.coninf.doconline.shared.anotacion.Key;

@Entity
@Table(name = "CLIENTES")
public class ClienteDto {
	
	@Id
	@InsertOptional
	@Column(name = "ID_CLIENTE", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "USUARIO")
	private String usuario;
	
	@Key
	@Column(name = "ID_TIPO_DOC_PERSONA")
	private Long tipoDocumentoId;

	@Key
	@Column(name = "NRO_DOC")
	private String numeroDocumento;

	@Key
	@Column(name = "ID_SEXO")
	private Long sexoId;

	@Column(name = "PIN")
	private String pin;
	
	@Column(name = "F_VIGENCIA_PIN")
	private Date fechaVencimientoPin;
	
	@Transient
	@Column(name = "F_VIGENCIA_DESDE")
	private Date fechaAlta;
	
	@Transient
	@Column(name = "F_VIGENCIA_HASTA")
	private Date fechaBaja;
	
	@Transient
	@Column(name = "COD_EXTERNO")
	private String codigoExterno;
	
	@Transient
	@Column(name = "ID_PROVINCIA")
	private Long idProvincia;

	@InsertOptional
	@Column(name = "email")
	private String eMail;
	
	@InsertOptional
	@Column(name = "accesos_fallidos")
	private Integer accesosFallidos;
	
	@Column(name = "flg_habilitado")
	private Integer flgHabilitado;
	
	@Column(name = "ID_TIPO_ESTADO_CLIENTE")
	private Long flgEstado;

	@InsertOptional
	@Column(name = "NOMBRE")
	private String nombre;

	@InsertOptional
	@Column(name = "APELLIDO")
	private String apellido;

	@InsertOptional
	@Column(name = "NACIONALIDAD")
	private Long nacionalidad;

	@InsertOptional
	@Column(name = "F_NACIMIENTO")
	private Date fecNacimiento;

	@InsertOptional
	@Column(name = "ID_ESTADO_CIVIL")
	private Long IdEstadoCivil;

	@InsertOptional
	@Column(name = "CUIT_CUIL")
	private String cuitCuil;

	@InsertOptional
	@Column(name = "ID_DIRECCION")
	private Long IdDireccion;

	@InsertOptional
	@Column(name = "OCUPACION")
	private String ocupacion;

	@InsertOptional
	@Column(name = "PEP")
	private Integer pep;

	@Transient
	TipoDocPersonaDto tipoDocPersonaDto;

	@Transient
	CodExternoClienteDto codExternoClienteDto;

	@Transient
	SexoDto sexoDto;

	@Transient
	String pinDecriptado;
	
	@Transient
	private List<CelularClienteDto> celulares;

	@Transient
	TipoEstadoClienteDto tipoEstadoClienteDto;

	@Transient
	PaisDto nacionalidadDto;

	@Transient
	EstadoCivilDto estadoCivilDto;

	@Transient
	DireccionClienteDto direccionClienteDto;

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getUsuario() {
		return usuario;
	}
	
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public void setId(BigDecimal id) {
		this.id = id.longValue();
	}

	public Long getTipoDocumentoId() {
		return tipoDocumentoId;
	}
	
	public void setTipoDocumentoId(Long tipoDocumentoId) {
		this.tipoDocumentoId = tipoDocumentoId;
	}

	public void setTipoDocumentoId(BigDecimal tipoDocumentoId) {
		this.tipoDocumentoId = tipoDocumentoId.longValue();
	}
	
	public String getNumeroDocumento() {
		return numeroDocumento;
	}
	
	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}
	
	public TipoDocPersonaDto getTipoDocPersonaDto() {
		return tipoDocPersonaDto;
	}

	public void setTipoDocPersonaDto(TipoDocPersonaDto tipoDocPersonaDto) {
		this.tipoDocPersonaDto = tipoDocPersonaDto;
	}

	public Long getSexoId() {
		return sexoId;
	}
	
	public PaisDto getNacionalidadDto() {
		return nacionalidadDto;
	}

	public void setNacionalidadDto(PaisDto nacionalidadDto) {
		this.nacionalidadDto = nacionalidadDto;
	}

	public EstadoCivilDto getEstadoCivilDto() {
		return estadoCivilDto;
	}

	public void setEstadoCivilDto(EstadoCivilDto estadoCivilDto) {
		this.estadoCivilDto = estadoCivilDto;
	}

	public DireccionClienteDto getDireccionClienteDto() {
		return direccionClienteDto;
	}

	public void setDireccionClienteDto(DireccionClienteDto direccionClienteDto) {
		this.direccionClienteDto = direccionClienteDto;
	}

	public void setSexoId(Long sexoId) {
		this.sexoId = sexoId;
	}
	
	public void setSexoId(BigDecimal sexoId) {
		this.sexoId = sexoId.longValue();
	}

	public String getPin() {
		return pin;
	}
	
	public void setPin(String pin) {
		this.pin = pin;
	}

	public Date getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public void setFechaAlta(Timestamp fechaAlta) {
		this.fechaAlta = fechaAlta;
	}
	
	public Date getFechaBaja() {
		return fechaBaja;
	}

	public void setFechaBaja(Date fechaBaja) {
		this.fechaBaja = fechaBaja;
	}

	public void setFechaBaja(Timestamp fechaBaja) {
		this.fechaBaja = fechaBaja;
	}

	public String getCodigoExterno() {
		return codigoExterno;
	}

	public void setCodigoExterno(String codigoExterno) {
		this.codigoExterno = codigoExterno;
	}

	public Long getIdProvincia() {
		return idProvincia;
	}

	public void setIdProvincia(Long idProvincia) {
		this.idProvincia = idProvincia;
	}
	
	public void setIdProvincia(BigDecimal idProvincia) {
		this.idProvincia = idProvincia.longValue();
	}

	public List<CelularClienteDto> getCelulares() {
		return celulares;
	}

	public void setCelulares(List<CelularClienteDto> celulares) {
		this.celulares = celulares;
	}
	
	public SexoDto getSexoDto() {
		return sexoDto;
	}

	public void setSexoDto(SexoDto sexoDto) {
		this.sexoDto = sexoDto;
	}

	public String getPinDecriptado() {
		return pinDecriptado;
	}
	
	public void setPinDecriptado(String pin) {
		this.pinDecriptado = pin;
	}
	
	public CodExternoClienteDto getCodExternoClienteDto() {
		return codExternoClienteDto;
	}

	public void setCodExternoClienteDto(CodExternoClienteDto codExternoClienteDto) {
		this.codExternoClienteDto = codExternoClienteDto;
	}

	public String getEMail() {
		return eMail;
	}

	public void setEMail(String eMail) {
		this.eMail = eMail;
	}

	public Integer getAccesosFallidos() {
		return accesosFallidos;
	}

	public void setAccesosFallidos(Integer accesosFallidos) {
		this.accesosFallidos = accesosFallidos;
	}

	public Long getFlgEstado() {
		return flgEstado;
	}

	public void setFlgEstado(Long flgEstado) {
		this.flgEstado = flgEstado;
	}

	public Date getFechaVencimientoPin() {
		return fechaVencimientoPin;
	}

	public void setFechaVencimientoPin(Date fechaVencimientoPin) {
		this.fechaVencimientoPin = fechaVencimientoPin;
	}

	public void setFechaVencimientoPin(Timestamp fechaVencimientoPin) {
		this.fechaVencimientoPin = fechaVencimientoPin;
	}
	public TipoEstadoClienteDto getTipoEstadoClienteDto() {
		return tipoEstadoClienteDto;
	}

	public void setTipoEstadoClienteDto(TipoEstadoClienteDto tipoEstadoClienteDto) {
		this.tipoEstadoClienteDto = tipoEstadoClienteDto;
	}

	public Integer getFlgHabilitado() {
		return flgHabilitado;
	}

	public void setFlgHabilitado(Integer flgHabilitado) {
		this.flgHabilitado = flgHabilitado;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public Long getNacionalidad() {
		return nacionalidad;
	}

	public void setNacionalidad(Long nacionalidad) {
		this.nacionalidad = nacionalidad;
	}

	public Date getFecNacimiento() {
		return fecNacimiento;
	}

	public void setFecNacimiento(Date fecNacimiento) {
		this.fecNacimiento = fecNacimiento;
	}

	public Long getIdEstadoCivil() {
		return IdEstadoCivil;
	}

	public void setIdEstadoCivil(Long idEstadoCivil) {
		IdEstadoCivil = idEstadoCivil;
	}

	public String getCuitCuil() {
		return cuitCuil;
	}

	public void setCuitCuil(String cuitCuil) {
		this.cuitCuil = cuitCuil;
	}

	public Long getIdDireccion() {
		return IdDireccion;
	}

	public void setIdDireccion(Long idDireccion) {
		IdDireccion = idDireccion;
	}

	public String getOcupacion() {
		return ocupacion;
	}

	public void setOcupacion(String ocupacion) {
		this.ocupacion = ocupacion;
	}

	public Integer getPep() {
		return pep;
	}

	public void setPep(Integer pep) {
		this.pep = pep;
	}
}
