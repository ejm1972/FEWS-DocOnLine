package ar.com.coninf.doconline.business.negocio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import ar.com.boldt.monedero.shared.constant.MonederoParametros;
import ar.com.coninf.doconline.model.dao.ParametroDao;
import ar.com.coninf.doconline.model.dao.PuntoVentaDao;
import ar.com.coninf.doconline.rest.model.enums.ErrorEnum;
import ar.com.coninf.doconline.shared.dto.PuntoVentaDto;
import ar.com.coninf.doconline.shared.excepcion.ApplicationException;


public abstract class AbstractBusiness {

	@Value("#{wsProperties.url_wsaa_homo}")
	protected String urlWsaaHomo;
	
	@Value("#{wsProperties.url_wsaa_prod}")
	protected String urlWsaaProd;
	
	@Value("#{wsProperties.url_wsfev1_homo}")
	protected String urlWsfev1Homo;
	
	@Value("#{wsProperties.url_wsfev1_prod}")
	protected String urlWsfev1Prod;
	
	protected String urlWsaa;
	protected String urlWsfev1;
	protected String urlProxy;
	protected String flagHomologacion;
	protected String archivoKey;
	protected String archivoCrt;
	
	protected String paramTimeout;
	protected String paramUserDir;
	
	@Autowired
	@Qualifier("puntoVentaDao")
	private PuntoVentaDao puntoVentaDao;

	@Autowired
	@Qualifier("parametroDao")
	private ParametroDao parametroDao;
	
	protected void validarPuntoVenta(Integer interfaz, Integer puntoVenta) {
		
		PuntoVentaDto puntoVentaDto = new PuntoVentaDto();
		puntoVentaDto.setIdInterfaz(interfaz);
		String ptovta = String.valueOf("00000").concat(String.valueOf(puntoVenta.intValue()));
		ptovta = ptovta.substring(ptovta.length()-5);
		puntoVentaDto.setPuntoVenta(ptovta);
		PuntoVentaDto puntoventa = puntoVentaDao.getByKeyGen(puntoVentaDto);
		if (puntoventa == null || puntoventa.getIdPuntoVenta() == null || puntoventa.getActivo() == null || !puntoventa.getActivo().equals("S")) {
			throw new ApplicationException(ErrorEnum.ERROR_PUNTO_DE_VENTA_INVALIDO, "error.ws-puntoventa_invalido");
		}
		
	}

	protected void validarHomologacion(Integer interfaz) {
		
		String modoInterfaz = parametroDao.getValorVigente("MODO_INTRFZ_"+String.valueOf(interfaz.intValue()));
		flagHomologacion = modoInterfaz==null||!modoInterfaz.equals("PROD")?"SI":"NO";

		if (flagHomologacion.equals("SI")) {
			urlWsaa = urlWsaaHomo;
			urlWsfev1 = urlWsfev1Homo;
		} else {
			urlWsaa = urlWsaaProd;
			urlWsfev1 = urlWsfev1Prod;
		}

	}

	protected void validarProxy() {
		
		String proxyParametro = parametroDao.getValorVigente(MonederoParametros._PROXY_);
		urlProxy = proxyParametro==null?"":proxyParametro;
	
	}

	protected void validarTimeOut() {
		
		String timeoutParametro = parametroDao.getValorVigente(MonederoParametros._TIMEOUT_);
		paramTimeout = timeoutParametro==null?"30":timeoutParametro;
	}
	
	protected void validarUserDir() {
		
		String userDirParametro = parametroDao.getValorVigente(MonederoParametros._USERDIR_);
		paramUserDir = userDirParametro==null?"c:/coninf/DocOnline/":userDirParametro;
	}

	//	@Autowired
//	@Qualifier("services.validadorPinServices")
//	private ValidadorPinServices validadorPinServices;
//
//	protected void validarClave(String clave, boolean exigirAlMenosUnCaracterQueSeaLetra, boolean exigirAlMenosUnCaracterQueSeaNumero) {
//		
//		int claveLength = clave.length();
//		
//		if (claveLength < 4 || claveLength > 8) {
//			throw new ApplicationException(ErrorEnum.ERROR_LONGITUD_CLAVE_INCORRECTA, "error.mon-longitud_clave_incorrecta", null);
//		} else if (clave.matches(".*[^A-Za-z0-9].*")) { //Si contiene al menos un caracter que no sea ni letra ni numero
//			throw new ApplicationException(ErrorEnum.ERROR_FORMATO_CLAVE_INCORRECTO, "error.mon-formato_clave_incorrecto", null);
//		} else if (exigirAlMenosUnCaracterQueSeaLetra && !clave.matches(".*[A-Za-z].*")) { //Si no contiene al menos un caracter que sea letra 
//			throw new ApplicationException(ErrorEnum.ERROR_FORMATO_CLAVE_INCORRECTO, "error.mon-formato_clave_incorrecto", null);
//		} else if (exigirAlMenosUnCaracterQueSeaNumero && !clave.matches(".*[0-9].*")) { //Si no contiene al menos un caracter que sea numero
//			throw new ApplicationException(ErrorEnum.ERROR_FORMATO_CLAVE_INCORRECTO, "error.mon-formato_clave_incorrecto", null);			
//		}
//	}
//	
//	protected void validarClienteInhabilitado(ClienteDto clienteDto) {
//		
//		if (clienteDto == null) {
//			throw new ApplicationException(ErrorEnum.ERROR_CLIENTE_INEXISTENTE, "error.mon-cliente_inexistente");
//		} else if (clienteDto.getFlgHabilitado() == TipoHabilitadoClienteEnum.INHABILITADO.getCodigo()) {
//			throw new ApplicationException(ErrorEnum.ERROR_CLIENTE_INHABILITADO, "error.mon-cliente_inhabilitado");
//		}
//	}
//
//	protected void validarClienteBloqueado(ClienteDto clienteDto) {
//		
//		if (clienteDto == null) {
//			throw new ApplicationException(ErrorEnum.ERROR_CLIENTE_INEXISTENTE, "error.mon-cliente_inexistente");
//		} else if (clienteDto.getFlgEstado() == TipoEstadoClienteEnum.BLOQUEADO.getCodigo()) {
//			throw new ApplicationException(ErrorEnum.ERROR_CLIENTE_BLOQUEADO, "error.mon-cliente_bloqueado");
//		}
//	}
//	
//	protected void validarPinValido(ClienteDto clienteDtoInformado, ClienteDto clienteDtoConsultado) throws NoSuchAlgorithmException {
//		
//		String pin = EncriptacionUtiles.byteArrayToHexString(EncriptacionUtiles.computeHash(clienteDtoInformado.getPin()));
//
//		if (!pin.equals(clienteDtoConsultado.getPin())) {
//			throw new ApplicationException(ErrorEnum.ERROR_PIN_INVALIDO, "error.mon-pin_invalido", null, clienteDtoConsultado);
//		} else {
//			validadorPinServices.blanqueoIntentos(clienteDtoConsultado);
//		}
//	}
//
//	protected void validarPinVencido_AceptacionDeTerminosYCondiciones_EMailCargado(ClienteDto clienteDto,
//		boolean validarPinVencido, boolean validarAceptacionDeTerminosYCondiciones,
//		boolean validarEMailCargado) throws NoSuchAlgorithmException {
//		
//		boolean pinInvalido = false;
//		boolean aceptacionDeTerminosYCondicionesInvalida = false;
//		boolean eMailInvalido = false;
//		
//		if (validarPinVencido) {
//			
//			try {
//				validarPinVencido(clienteDto);
//			} catch (ApplicationException e) {
//				pinInvalido = true;
//			}
//		}
//		
//		if (validarAceptacionDeTerminosYCondiciones) {
//			
//			try {
//				validarAceptacionDeTerminosYCondiciones(clienteDto);
//			} catch (ApplicationException e) {
//				aceptacionDeTerminosYCondicionesInvalida = true;
//			}
//		}
//		
//		if (validarEMailCargado) {
//			
//			try {
//				validarClienteConMail(clienteDto);
//			} catch (ApplicationException e) {
//				eMailInvalido = true;
//			}
//		}
//		
//		if (pinInvalido && aceptacionDeTerminosYCondicionesInvalida && eMailInvalido) {
//			throw new ApplicationException(ErrorEnum.ERROR_PIN_VENCIDO_TERMINOS_Y_CONDICIONES_NO_ACEPTADOS_E_MAIL_NO_CARGADO, "error.mon-pin_vencido_terminos_y_condiciones_no_aceptados_e_mail_no_cargado");
//		} else if (pinInvalido && aceptacionDeTerminosYCondicionesInvalida) {
//			throw new ApplicationException(ErrorEnum.ERROR_PIN_VENCIDO_TERMINOS_Y_CONDICIONES_NO_ACEPTADOS, "error.mon-pin_vencido_terminos_y_condiciones_no_aceptados");
//		} else if (aceptacionDeTerminosYCondicionesInvalida && eMailInvalido) {
//			throw new ApplicationException(ErrorEnum.ERROR_TERMINOS_Y_CONDICIONES_NO_ACEPTADOS_E_MAIL_NO_CARGADO, "error.mon-terminos_y_condiciones_no_aceptados_e_mail_no_cargado");
//		} else if (pinInvalido && eMailInvalido) {
//			throw new ApplicationException(ErrorEnum.ERROR_PIN_VENCIDO_E_MAIL_NO_CARGADO, "error.mon-pin_vencido_e_mail_no_cargado");
//		} else if (pinInvalido) {
//			throw new ApplicationException(ErrorEnum.ERROR_PIN_VENCIDO, "error.mon-pin_vencido");
//		} else if (aceptacionDeTerminosYCondicionesInvalida) {
//			throw new ApplicationException(ErrorEnum.ERROR_TERMINOS_Y_CONDICIONES_NO_ACEPTADOS, "error.mon-terminos_y_condiciones_no_aceptados");			
//		} else if (eMailInvalido) {
//			throw new ApplicationException(ErrorEnum.ERROR_SIN_EMAIL, "error.mon-cliente_sin_email");			
//		}
//	}
//	
//	protected void validarPinVencido(ClienteDto clienteDto) throws NoSuchAlgorithmException {
//
//		Date fechaVencimientoPin = clienteDto.getFechaVencimientoPin();
//		Date fechaActual = Calendar.getInstance().getTime();
//							
//		if (fechaActual.getTime() > fechaVencimientoPin.getTime()) {
//			throw new ApplicationException(ErrorEnum.ERROR_PIN_VENCIDO, "error.mon-pin_vencido");				
//		}
//	}
//	
//	protected void validarAceptacionDeTerminosYCondiciones(ClienteDto clienteDto) {
//		
//		Long aceptacionTerminosCondicionesId = clienteDto.getAceptacionTerminosCondicionesId();
//		
//		if (aceptacionTerminosCondicionesId == null) {
//			throw new ApplicationException(ErrorEnum.ERROR_TERMINOS_Y_CONDICIONES_NO_ACEPTADOS, "error.mon-terminos_y_condiciones_no_aceptados");
//		}
//	}
//	
//	protected void validarClienteConMail(ClienteDto clienteDto) {
//		
//		if (clienteDto == null) {
//			throw new ApplicationException(ErrorEnum.ERROR_CLIENTE_INEXISTENTE, "error.mon-cliente_inexistente");
//		} else if (clienteDto.getEMail()==null || clienteDto.getEMail().isEmpty()) {
//			throw new ApplicationException(ErrorEnum.ERROR_SIN_EMAIL, "error.mon-cliente_sin_email");		}
//	}
}
