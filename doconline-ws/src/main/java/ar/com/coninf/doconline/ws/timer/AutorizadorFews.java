package ar.com.coninf.doconline.ws.timer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ar.com.coninf.doconline.business.enums.TipoComprobanteAFIP;
import ar.com.coninf.doconline.business.facade.LogTransaccionFacade;
import ar.com.coninf.doconline.business.facade.RegistroTransaccionFacade;
import ar.com.coninf.doconline.business.log.LogTransaccionContenido;
import ar.com.coninf.doconline.model.dao.FewsComprobanteAsociadoDao;
import ar.com.coninf.doconline.model.dao.FewsDatoOpcionalDao;
import ar.com.coninf.doconline.model.dao.FewsDetalleDao;
import ar.com.coninf.doconline.model.dao.FewsEncabezadoDao;
import ar.com.coninf.doconline.model.dao.FewsIvaDao;
import ar.com.coninf.doconline.model.dao.FewsPeriodoAsociadoDao;
import ar.com.coninf.doconline.model.dao.FewsPermisoDao;
import ar.com.coninf.doconline.model.dao.FewsQrDao;
import ar.com.coninf.doconline.model.dao.FewsTributoDao;
import ar.com.coninf.doconline.model.dao.FewsXmlDao;
import ar.com.coninf.doconline.model.dao.ParametroDao;
import ar.com.coninf.doconline.rest.model.response.ResponseAutenticacion;
import ar.com.coninf.doconline.rest.model.response.ResponseAutorizarComprobante;
import ar.com.coninf.doconline.rest.model.response.ResponseAutorizarComprobanteExportacion;
import ar.com.coninf.doconline.rest.model.response.ResponseGenerarQr;
import ar.com.coninf.doconline.rest.model.tx.ComprobanteAsociado;
import ar.com.coninf.doconline.rest.model.tx.ControlTransaccion;
import ar.com.coninf.doconline.rest.model.tx.DatoOpcional;
import ar.com.coninf.doconline.rest.model.tx.DatoQr;
import ar.com.coninf.doconline.rest.model.tx.Item;
import ar.com.coninf.doconline.rest.model.tx.Iva;
import ar.com.coninf.doconline.rest.model.tx.PeriodoComprobanteAsociado;
import ar.com.coninf.doconline.rest.model.tx.Permiso;
import ar.com.coninf.doconline.rest.model.tx.Tributo;
import ar.com.coninf.doconline.shared.dto.FewsComprobanteAsociado;
import ar.com.coninf.doconline.shared.dto.FewsDatoOpcional;
import ar.com.coninf.doconline.shared.dto.FewsDetalle;
import ar.com.coninf.doconline.shared.dto.FewsEncabezado;
import ar.com.coninf.doconline.shared.dto.FewsIva;
import ar.com.coninf.doconline.shared.dto.FewsPeriodoAsociado;
import ar.com.coninf.doconline.shared.dto.FewsPermiso;
import ar.com.coninf.doconline.shared.dto.FewsQr;
import ar.com.coninf.doconline.shared.dto.FewsResultado;
import ar.com.coninf.doconline.shared.dto.FewsTributo;
import ar.com.coninf.doconline.shared.dto.FewsXml;
import ar.com.coninf.doconline.shared.dto.RegistroTransaccionDto;
import ar.com.coninf.doconline.ws.DocOnlineServicioWeb;


@Component(value = "timer.AutorizadorFews")
public class AutorizadorFews {
	private Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	@Qualifier("fewsEncabezadoDao")
	private FewsEncabezadoDao fewsEncabezadoDao;

	@Autowired
	@Qualifier("fewsIvaDao")
	private FewsIvaDao fewsIvaDao;

	@Autowired
	@Qualifier("fewsTributoDao")
	private FewsTributoDao fewsTributoDao;

	@Autowired
	@Qualifier("fewsComprobanteAsociadoDao")
	private FewsComprobanteAsociadoDao fewsComprobanteAsociadoDao;

	@Autowired
	@Qualifier("fewsDatoOpcionalDao")
	private FewsDatoOpcionalDao fewsDatoOpcionalDao;
	
	@Autowired
	@Qualifier("fewsXmlDao")
	private FewsXmlDao fewsXmlDao;

	@Autowired
	@Qualifier("fewsQrDao")
	private FewsQrDao fewsQrDao;
	
	@Autowired
	@Qualifier("fewsPeriodoAsociadoDao")
	private FewsPeriodoAsociadoDao fewsPeriodoAsociadoDao;
	
	@Autowired
	@Qualifier("fewsPermisoDao")
	private FewsPermisoDao fewsPermisoDao;
	
	@Autowired
	@Qualifier("fewsDetalleDao")
	private FewsDetalleDao fewsDetalleDao;
	
	@Autowired
	@Qualifier("dolProperties")
	protected Properties dolProperties;

	@Autowired
	@Qualifier("webservice.DocOnlineServicioWeb")
	protected DocOnlineServicioWeb dolsw;

	@Autowired
	@Qualifier("facade.registroTransaccionFacade")
	protected RegistroTransaccionFacade registroTransaccionFacade;
	
	@Autowired
	@Qualifier("facade.logTransaccionFacade")
	protected LogTransaccionFacade logTransaccionFacade;

	@Autowired
	@Qualifier("parametroDao")
	private ParametroDao parametroDao;

	private Integer interfaz;
	private String  clave;
	private LogTransaccionContenido log;
	
	public AutorizadorFews() {

		this.interfaz = 9901;
		this.clave = "1234";

	}

	public AutorizadorFews(Integer interfaz, String clave) {

		this.interfaz = interfaz;
		this.clave = clave;

	}

	private void registrarAuditoriaIni(ControlTransaccion ctx, FewsEncabezado selected, String ivas, String tributos, String comprobantesAsociados, String datosOpcionales, String periodosAsociados, String permisos, String items) {

		logger.debug("Ejecucion AutorizadorFews.registrarAuditoriaIni()");

		log.setCtxInterfaz(ctx.getInterfaz().toString());
		log.setCtxNroTransaccion(ctx.getNroTransaccion().toString());

		log.setCtxServicio("DocOnlineServicioWebImpl - autorizarComprobante");
		log.setOperacion("autorizarComprobante");
		log.setCtxFechaTransaccion(new Date());
		log.setFechaInicioOp(new Date());

		BigDecimal big100 = new BigDecimal("100");
		String concepto = selected.getConcepto()==null?"":selected.getConcepto().toString();
		String tipoDoc = selected.getTipoDoc().toString();
		String nroDoc = selected.getNroDoc().toString();
		String tipoCbte = selected.getTipoCbte().toString();
		String ptoVta = selected.getPuntoVta().toString();
		String nroCbte = selected.getCbteNro().toString();
		
		String fechaCbte = selected.getFechaCbte();
		String fechaVencPago = selected.getFechaVencPago();
		String fechaServDesde = selected.getFechaServDesde();
		String fechaServHasta = selected.getFechaServHasta();

		String monedaId = selected.getMonedaId();
		String monedaCtz = selected.getMonedaCtz().multiply(big100).setScale(0,BigDecimal.ROUND_HALF_UP).toString();
		
		String impTotal = selected.getImpTotal().multiply(big100).setScale(0,BigDecimal.ROUND_HALF_UP).toString();
		String impTotConcNoGrav = selected.getImpTotConc().multiply(big100).setScale(0,BigDecimal.ROUND_HALF_UP).toString();
		String impNeto = selected.getImpNeto().multiply(big100).setScale(0,BigDecimal.ROUND_HALF_UP).toString();
		String impIva = selected.getImpIva().multiply(big100).setScale(0,BigDecimal.ROUND_HALF_UP).toString();
		String impTrib = selected.getImpTrib().multiply(big100).setScale(0,BigDecimal.ROUND_HALF_UP).toString();
		String impOpEx = selected.getImpOpEx().multiply(big100).setScale(0,BigDecimal.ROUND_HALF_UP).toString();
		
		String tipoExpo = selected.getTipoExpo()==null?"":selected.getTipoExpo().toString();
		String permisoExistente = selected.getPermisoExistente()==null?"":selected.getPermisoExistente();
		String dstCmp = selected.getDstCmp()==null?"":selected.getDstCmp().toString();
		String idImpositivo = selected.getIdImpositivo()==null?"":selected.getIdImpositivo();
		
		String cuitPaisCliente = selected.getCuitPaisCliente()==null?"":selected.getCuitPaisCliente();
		String nombreCliente = selected.getNombreCliente()==null?"":selected.getNombreCliente();
		String domicilioCliente = selected.getDomicilioCliente()==null?"":selected.getDomicilioCliente();
		
		String obsComerciales = selected.getObsComerciales()==null?"":selected.getObsComerciales();
		String obsGenerales = selected.getObsGenerales()==null?"":selected.getObsGenerales();
		String formaPago = selected.getFormaPago()==null?"":selected.getFormaPago();
		String incoterms = selected.getIncoterms()==null?"":selected.getIncoterms();
		String incotermsDs = selected.getIncotermsDs()==null?"":selected.getIncotermsDs();
		String idiomaCbte = selected.getIdiomaCbte()==null?"":selected.getIdiomaCbte();

		log.setTipoCbte(tipoCbte);
		ptoVta = String.valueOf("00000").concat(ptoVta);
		ptoVta = ptoVta.substring(ptoVta.length()-5);
		log.setPtoVtaCbte(ptoVta);
		nroCbte = String.valueOf("00000000").concat(nroCbte);
		nroCbte = nroCbte.substring(nroCbte.length()-8);		
		log.setNroCbte(nroCbte);
		String pref = TipoComprobanteAFIP.findPref4Cod(tipoCbte);
		String cbte = pref+"-"+ptoVta+"-"+nroCbte;
		log.setCbte(cbte);
		log.setFechaCbte(fechaCbte);
		log.setImpTotal(impTotal);
		
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("lanzador: AutorizadorFews.procesarPendientes.autorizarComprobante");
		
		sb.append(", concepto: "+concepto+", tipoDoc: "+tipoDoc+", nroDoc: "+nroDoc);
		sb.append(", tipoCbte: "+tipoCbte+", ptoVta: "+ptoVta+", nroCbte: "+nroCbte+", fechaCbte: "+fechaCbte);
		
		sb.append(", fechaVencPago: "+fechaVencPago+", fechaServDesde: "+fechaServDesde+", fechaServHasta: "+fechaServHasta);
		
		sb.append(", monedaId: "+monedaId+", monedaCtz: "+monedaCtz);
		
		sb.append(", impTotal: "+impTotal+", impTotConcNoGrav: "+impTotConcNoGrav+", impOpEx: "+impOpEx);
		sb.append(", impNeto: "+impNeto+", impIva: "+impIva+", impTrib: "+impTrib);
		
		sb.append(", tipoExpo: "+tipoExpo+", permisoExistente: "+permisoExistente+", dstCmp: "+dstCmp+", idImpositivo: "+idImpositivo);
		sb.append(", cuitPaisCliente: "+cuitPaisCliente+", nombreCliente: "+nombreCliente+", domicilioCliente: "+domicilioCliente);
		sb.append(", obsComerciales: "+obsComerciales);
		sb.append(", obsGenerales: "+obsGenerales);
		sb.append(", incoterms: "+incoterms+", incotermsDs: "+incotermsDs);
		sb.append(", formaPago: "+formaPago+", idiomaCbte: "+idiomaCbte);
		
		if (!ivas.equals("")) {
			sb.append(", ivas: "+ivas);
		}
		if (!tributos.equals("")) {
			sb.append(", tributos: "+tributos);
		}
		if (!comprobantesAsociados.equals("")) {
			sb.append(", comprobantesAsociados: "+comprobantesAsociados);
		}
		if (!datosOpcionales.equals("")) {
			sb.append(", datosOpcionales: "+datosOpcionales);
		}
		if (!periodosAsociados.equals("")) {
			sb.append(", peridosAsociados: "+periodosAsociados);
		}
		if (!permisos.equals("")) {
			sb.append(", permisos: "+permisos);
		}
		if (!items.equals("")) {
			sb.append(", items: "+items);
		}
		sb.append("}");
		
		log.setXmlEntrada(sb.toString());
	}

	private void registrarAuditoriaFin(ResponseAutorizarComprobante resp) {

		logger.debug("Ejecucion AutorizadorFews.registrarAuditoriaFin()");
		
		log.setCodigo(resp.getCodigo().toString());
		log.setDescripcion(resp.getDescripcion());
		log.setObservacion(resp.getObservacion());

		log.setExcepcionWsaa(resp.getExcepcionWsaa());
		log.setExcepcionWsfev1(resp.getExcepcionWsfev1());
		
		log.setErrMsg(resp.getErrMsg());
		log.setObs(resp.getObs());
		
		log.setXmlRequest(resp.getXmlRequest());
		log.setXmlResponse(resp.getXmlResponse());

		log.setCae(resp.getCae());
		log.setFechaVencimiento(resp.getFechaVencimiento());
		log.setResultado(resp.getResultado());

		log.setFechaFinOp(new Date());

		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("lanzador: AutorizadorFews.procesarPendientes.autorizarComprobante");
		sb.append(", codigo: "+resp.getCodigo().toString()+", descripcion: "+resp.getDescripcion()+", observacion: "+resp.getObservacion()+", esReintento: "+resp.getEsReintento().toString());
		sb.append(", resultado: "+resp.getResultado()+", cae: "+resp.getCae()+", fechaVencimiento: "+resp.getFechaVencimiento());
		sb.append(", obs: "+resp.getObs()+", errMsg: "+resp.getErrMsg()+", excepcionWsaa: "+resp.getExcepcionWsaa()+", excepcionWsfev1: "+resp.getExcepcionWsfev1());
		sb.append(", xmlRequest: "+resp.getXmlRequest());
		sb.append(", xmlResponse: "+resp.getXmlResponse());
		sb.append("}");

		log.setXmlSalida(sb.toString());

		logTransaccionFacade.registrarAuditoria(log);
	}

	private void registrarAuditoriaFin(ResponseAutorizarComprobanteExportacion resp) {

		logger.debug("Ejecucion AutorizadorFews.registrarAuditoriaFin()");
		
		//Lo Agrego para no poner mas if o generar otro registrarAuditoriaIni solo por esto
		log.setCtxServicio("DocOnlineServicioWebImpl - autorizarComprobanteExportacion");
		log.setOperacion("autorizarComprobanteExportacion");

		log.setCodigo(resp.getCodigo().toString());
		log.setDescripcion(resp.getDescripcion());
		log.setObservacion(resp.getObservacion());

		log.setExcepcionWsaa(resp.getExcepcionWsaa());
		log.setExcepcionWsfexv1(resp.getExcepcionWsfexv1());
		
		log.setErrMsg(resp.getErrMsg());
		log.setObs(resp.getObs());
		
		log.setXmlRequest(resp.getXmlRequest());
		log.setXmlResponse(resp.getXmlResponse());

		log.setCae(resp.getCae());
		log.setFechaVencimiento(resp.getFechaVencimiento());
		log.setResultado(resp.getResultado());

		log.setFechaFinOp(new Date());

		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("lanzador: AutorizadorFews.procesarPendientes.autorizarComprobante");
		sb.append(", codigo: "+resp.getCodigo().toString()+", descripcion: "+resp.getDescripcion()+", observacion: "+resp.getObservacion()+", esReintento: "+resp.getEsReintento().toString());
		sb.append(", resultado: "+resp.getResultado()+", cae: "+resp.getCae()+", fechaVencimiento: "+resp.getFechaVencimiento());
		sb.append(", obs: "+resp.getObs()+", errMsg: "+resp.getErrMsg()+", excepcionWsaa: "+resp.getExcepcionWsaa()+", excepcionWsfexv1: "+resp.getExcepcionWsfexv1());
		sb.append(", xmlRequest: "+resp.getXmlRequest());
		sb.append(", xmlResponse: "+resp.getXmlResponse());
		sb.append("}");

		log.setXmlSalida(sb.toString());

		logTransaccionFacade.registrarAuditoria(log);
	}

	//LOGS_GENERICOS y su correspondiente vista en la consola
	private void registrarAuditoriaIniFin(Integer intefaz, String ejecucion, String lanzador, Exception exception) {

		logger.debug("Ejecucion AutorizadorFews.registrarAuditoriaIniFin()" + ejecucion);

		Date fecha = new Date();
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("lanzador: " + lanzador);
		sb.append("}");
		
		log.setCtxInterfaz(intefaz.toString());
		log.setCtxNroTransaccion("0");
		log.setCtxServicio(lanzador);
		log.setOperacion("logErroresSinTransacciones");
		log.setCtxFechaTransaccion(fecha);
		log.setFechaInicioOp(fecha);

		log.setTipoCbte("-");
		log.setPtoVtaCbte("-");
		log.setNroCbte("-");
		log.setCbte(lanzador.substring(0, 30));
		log.setFechaCbte("-");
		log.setImpTotal("-");
		
		log.setCodigo("-");
		log.setDescripcion(exception.getMessage());
		log.setObservacion(exception.getCause()!=null?exception.getCause().getMessage():"-");

		log.setExcepcionWsaa("-");
		log.setExcepcionWsfev1("-");
		log.setExcepcionWsfexv1("-");
		
		log.setErrMsg("-");
		log.setObs("-");
		
		log.setXmlRequest("-");
		log.setXmlResponse("-");

		log.setCae("-");
		log.setFechaVencimiento("-");
		log.setResultado("-");

		log.setFechaFinOp(new Date());

		log.setXmlEntrada(sb.toString());
		log.setXmlSalida(sb.toString());

		RegistroTransaccionDto registro = new RegistroTransaccionDto();
		registro.setCodigoTransaccion(0);
		registro.setFechaTransaccion(fecha);
		registro.setInterfazId(intefaz);
		registro.setServicio(lanzador);
		registro.setDatosTransaccion(sb.toString().getBytes());

		try {
			registroTransaccionFacade.add(registro);
			logger.info("Registro de Error registrado. Servicio: " + lanzador);

			logTransaccionFacade.registrarAuditoria(log);

		} catch (Throwable e) {
			logger.error(e);
		}

	}

	public void logTimerSql() {
		
		logger.debug("Ejecucion AutorizadorFews.logTimerSql()");
		
		try {

			List<FewsResultado> lista = fewsEncabezadoDao.setLogTimerSql();
			if (lista==null)
				logger.debug("Lista de Resultado NULL");
			else
				logger.debug("Lista de Resultado "+lista.size());

		} catch (Exception e) {
			logger.error(e);
		}
	
	}

	public void procesarPendientes(List<Long> interfaces) {

		logger.debug("Ejecucion AutorizadorFews.procesarPendientes()");
		
		this.log = new LogTransaccionContenido();
		
		List<FewsEncabezado> pendientes = new ArrayList<FewsEncabezado>();

		for (Long intrfz : interfaces) {
			
			try {

				fewsEncabezadoDao.importLog(intrfz);
				logger.debug("Fin Ejecucion fewsEncabezadoDao.importLog()="+intrfz);
			
			} catch (Exception e) {
				logger.error(e);
				String lanzador = "AutorizadorFews.procesarPendientes()";
				String ejecucion = "importLog()="+intrfz;
				registrarAuditoriaIniFin(interfaz, ejecucion, lanzador, e);
				logger.debug("Fin Ejecucion AutorizadorFews.registrarAuditoriaIniFin()->"+ejecucion);
			}

			try {

				pendientes = fewsEncabezadoDao.getFewsPendiente((long) -1);
				logger.debug("Fin Ejecucion fewsEncabezadoDao.getFewsPendiente()");
			
			} catch (Exception e) {
				logger.error(e);
				String lanzador = "AutorizadorFews.procesarPendientes()";
				String ejecucion = "importLog()="+intrfz;
				registrarAuditoriaIniFin(interfaz, ejecucion, lanzador, e);
				logger.debug("Fin Ejecucion AutorizadorFews.registrarAuditoriaIniFin()->"+ejecucion);
			}

			for (int i=0; i < pendientes.size(); i++) {

				autorizarComprobante(pendientes.get(i));
				logger.debug("Fin Ejecucion AutorizadorFews.autorizarComprobante()="+pendientes.get(i).getId().toString()+","+pendientes.get(i).getIdInterfaz().toString());
				
			}
		}

	}

	public void autorizarComprobante(FewsEncabezado selected) {

		logger.debug("Ejecucion AutorizadorFews.autorizarComprobante()");

		try {

			List<FewsIva> listFewsIva = fewsIvaDao.getListById(selected.getId());
			List<FewsTributo> listFewsTributo = fewsTributoDao.getListById(selected.getId());
			List<FewsDatoOpcional> listFewsDatoOpcional = fewsDatoOpcionalDao.getListById(selected.getId());
			List<FewsComprobanteAsociado> listFewsComprobanteAsociado = fewsComprobanteAsociadoDao.getListById(selected.getId());
			List<FewsPeriodoAsociado> listFewsPeriodoAsociado = fewsPeriodoAsociadoDao.getListById(selected.getId());
			FewsXml fewsXml = fewsXmlDao.getById(selected.getId());
			FewsQr fewsQr = fewsQrDao.getById(selected.getId());
			List<FewsPermiso> listFewsPermiso = fewsPermisoDao.getListById(selected.getId());
			List<FewsDetalle> listFewsDetalle = fewsDetalleDao.getListById(selected.getId());
			
			interfaz = selected.getIdInterfaz().intValue();
			clave = dolProperties.getProperty("interfaz_"+interfaz); 

			BigDecimal big100 = new BigDecimal("100");

			Integer concepto = selected.getConcepto();
			Integer tipoDoc = selected.getTipoDoc();
			Long nroDoc = selected.getNroDoc();
			Integer tipoCbte = selected.getTipoCbte();
			Integer ptoVta = selected.getPuntoVta();
			Long nroCbte = selected.getCbteNro().longValue();

			String fechaCbte = selected.getFechaCbte();
			String fechaVencPago = selected.getFechaVencPago();
			String fechaServDesde = selected.getFechaServDesde();
			String fechaServHasta = selected.getFechaServHasta();
			
			String monedaId = selected.getMonedaId();
			BigDecimal monedaCtz = selected.getMonedaCtz().multiply(big100).setScale(0,BigDecimal.ROUND_HALF_UP);

			BigDecimal impTotal = selected.getImpTotal().multiply(big100).setScale(0,BigDecimal.ROUND_HALF_UP);
			BigDecimal impTotConcNoGrav = selected.getImpTotConc().multiply(big100).setScale(0,BigDecimal.ROUND_HALF_UP);
			BigDecimal impNeto = selected.getImpNeto().multiply(big100).setScale(0,BigDecimal.ROUND_HALF_UP);
			BigDecimal impIva = selected.getImpIva().multiply(big100).setScale(0,BigDecimal.ROUND_HALF_UP);
			BigDecimal impTrib = selected.getImpTrib().multiply(big100).setScale(0,BigDecimal.ROUND_HALF_UP);
			BigDecimal impOpEx = selected.getImpOpEx().multiply(big100).setScale(0,BigDecimal.ROUND_HALF_UP);
			
			Tributo[] tributos = new Tributo[listFewsTributo.size()];
			Iva[] ivas = new Iva[listFewsIva.size()];
			DatoOpcional[] datosOpcionales = new DatoOpcional[listFewsDatoOpcional.size()];
			ComprobanteAsociado[] comprobantesAsociados = new ComprobanteAsociado[listFewsComprobanteAsociado.size()];
			PeriodoComprobanteAsociado[] periodosAsociados = new PeriodoComprobanteAsociado[listFewsPeriodoAsociado.size()];
			
			Integer tipoExpo = selected.getTipoExpo();
			String permisoExistente = selected.getPermisoExistente();
			Integer dstCmp = selected.getDstCmp();
			String cliente = selected.getNombreCliente();
			String cuitPaisCliente = selected.getCuitPaisCliente();
			String domicilioCliente = selected.getDomicilioCliente();
			String idImpositivo = selected.getIdImpositivo();
			String obsComerciales = selected.getObsComerciales();
			String obsGenerales = selected.getObsGenerales();
			String formaPago = selected.getFormaPago(); 
			String incoterms = selected.getIncoterms();
			String incotermsDs = selected.getIncotermsDs();
			String idiomaCbte = selected.getIdiomaCbte();
			
			Permiso[] permisos = new Permiso[listFewsPermiso.size()];
			Item[] items = new Item[listFewsDetalle.size()];

			logger.debug("Comprobante:"+selected.getTipoComprobante()+"-"+selected.getNumeroPuntoVenta()+"-"+selected.getNumeroComprobante());
			logger.debug("Fecha:"+selected.getFechaCbte());
			logger.debug("ImpTotal:"+selected.getImpTotal().toString());
			logger.debug("ImpTotConc:"+selected.getImpTotConc().toString());
			logger.debug("ImpNeto:"+selected.getImpNeto().toString());
			logger.debug("ImpIva:"+selected.getImpIva().toString());
			logger.debug("ImpTrib:"+selected.getImpTrib().toString());
			logger.debug("ImpOpEx:"+selected.getImpOpEx().toString());
			logger.debug("MonedaCtz:"+selected.getMonedaCtz().toString());

			StringBuilder sbIva = new StringBuilder("[");
			int size;
			size = listFewsIva.size();
			for (int i = 0;  i < size; i++) {
				ivas[i] = new Iva();
				ivas[i].setIvaId( listFewsIva.get(i).getIvaId() );
				ivas[i].setIvaBaseImp( listFewsIva.get(i).getBaseImp().multiply(big100).setScale(0,BigDecimal.ROUND_HALF_UP) );
				ivas[i].setIvaImporte( listFewsIva.get(i).getImporte().multiply(big100).setScale(0,BigDecimal.ROUND_HALF_UP) );
				if (i>0) {
					sbIva.append(",");
				}
				sbIva.append("{ivaId: "+ivas[i].getIvaId().toString()+", ivaBaseImp: "+ivas[i].getIvaBaseImp().toString()+", ivaImporte: "+ivas[i].getIvaImporte().toString()+"}");
			}
			sbIva.append("]");
			logger.debug("Ivas:"+sbIva.toString());

			StringBuilder sbTributo = new StringBuilder("[");
			size = listFewsTributo.size();
			for (int i = 0;  i < size; i++) {			
				tributos[i] = new Tributo();
				tributos[i].setTributoId( listFewsTributo.get(i).getTributoId() );
				tributos[i].setTributoDesc( listFewsTributo.get(i).getTributoDesc() );
				tributos[i].setTributoAlic( listFewsTributo.get(i).getAlic().multiply(big100).setScale(0,BigDecimal.ROUND_HALF_UP) );
				tributos[i].setTributoBaseImp( listFewsTributo.get(i).getBaseImp().multiply(big100).setScale(0,BigDecimal.ROUND_HALF_UP) );
				tributos[i].setTributoImporte( listFewsTributo.get(i).getImporte().multiply(big100).setScale(0,BigDecimal.ROUND_HALF_UP) );
				if (i>0) {
					sbTributo.append(",");
				}
				sbTributo.append("{tributoId: "+tributos[i].getTributoId().toString()+", tributoDesc: "+tributos[i].getTributoDesc()+", tributoAlic: "+tributos[i].getTributoAlic().toString()+", tributoBaseImp: "+tributos[i].getTributoBaseImp().toString()+", tributoImporte: "+tributos[i].getTributoImporte().toString()+"}");
			}
			sbTributo.append("]");
			logger.debug("Tributos:"+sbTributo.toString());

			StringBuilder sbComprobanteAsociado = new StringBuilder("[");
			size = listFewsComprobanteAsociado.size();
			for (int i = 0;  i < size; i++) {			
				comprobantesAsociados[i] = new ComprobanteAsociado();
				comprobantesAsociados[i].setTipoCbte( listFewsComprobanteAsociado.get(i).getTipoCbte() );
				comprobantesAsociados[i].setPuntoVta( listFewsComprobanteAsociado.get(i).getPuntoVta() );
				comprobantesAsociados[i].setCbteNro( listFewsComprobanteAsociado.get(i).getCbteNro() );
				comprobantesAsociados[i].setCuit( listFewsComprobanteAsociado.get(i).getCuit() );
				comprobantesAsociados[i].setFechaCbte( listFewsComprobanteAsociado.get(i).getFechaCbte() );
				if (i>0) {
					sbComprobanteAsociado.append(",");
				}
				sbComprobanteAsociado.append("{tipoCbte: "+comprobantesAsociados[i].getTipoCbte().toString()+", puntoVta: "+comprobantesAsociados[i].getPuntoVta().toString()+", cbteNro: "+comprobantesAsociados[i].getCbteNro().toString()+", cuit: "+comprobantesAsociados[i].getCuit()+", fechaCbte: "+comprobantesAsociados[i].getFechaCbte()+"}");
			}
			sbComprobanteAsociado.append("]");
			logger.debug("ComprobantesAsociados:"+sbComprobanteAsociado.toString());

			StringBuilder sbDatoOpcional = new StringBuilder("[");
			size = listFewsDatoOpcional.size();
			for (int i = 0;  i < size; i++) {			
				datosOpcionales[i] = new DatoOpcional();
				datosOpcionales[i].setOpcionalId( listFewsDatoOpcional.get(i).getOpcionalId() );
				datosOpcionales[i].setValor( listFewsDatoOpcional.get(i).getValor() );
				if (i>0) {
					sbDatoOpcional.append(",");
				}
				sbDatoOpcional.append("{opcionalId: "+datosOpcionales[i].getOpcionalId().toString()+", valor: "+datosOpcionales[i].getValor()+"}");
			}
			sbDatoOpcional.append("]");
			logger.debug("DatosOpcionales:"+sbDatoOpcional.toString());

			StringBuilder sbPeriodoAsociado = new StringBuilder("[");
			size = listFewsPeriodoAsociado.size();
			for (int i = 0;  i < size; i++) {			
				periodosAsociados[i] = new PeriodoComprobanteAsociado();
				periodosAsociados[i].setFechaDesde( listFewsPeriodoAsociado.get(i).getFechaDesde() );
				periodosAsociados[i].setFechaHasta( listFewsPeriodoAsociado.get(i).getFechaHasta() );
				if (i>0) {
					sbPeriodoAsociado.append(",");
				}
				sbPeriodoAsociado.append("{fechaDesde: "+periodosAsociados[i].getFechaDesde()+", fechaHasta: "+periodosAsociados[i].getFechaHasta()+"}");
			}
			sbPeriodoAsociado.append("]");
			logger.debug("PeriodoComprobanteAsociado:"+sbPeriodoAsociado.toString());
			
			StringBuilder sbPermiso = new StringBuilder("[");
			size = listFewsPermiso.size();
			for (int i = 0;  i < size; i++) {			
				permisos[i] = new Permiso();
				permisos[i].setDstMerc( listFewsPermiso.get(i).getDstMerc() );
				permisos[i].setIdPermiso( listFewsPermiso.get(i).getIdPermiso() );
				if (i>0) {
					sbPermiso.append(",");
				}
				sbPermiso.append("{idPermiso: "+permisos[i].getIdPermiso()+", dstMerc: "+permisos[i].getDstMerc().toString()+"}");
			}
			sbPermiso.append("]");
			logger.debug("Permiso:"+sbPermiso.toString());
			
			StringBuilder sbItem = new StringBuilder("[");
			size = listFewsDetalle.size();
			for (int i = 0;  i < size; i++) {			
				items[i] = new Item();
				items[i].setCodigo( listFewsDetalle.get(i).getCodigo() );
				items[i].setDs( listFewsDetalle.get(i).getDs() );
				items[i].setQty( listFewsDetalle.get(i).getQty() );
				items[i].setPrecio( listFewsDetalle.get(i).getPrecio().multiply(big100).setScale(0,BigDecimal.ROUND_HALF_UP) );
				items[i].setBonif( listFewsDetalle.get(i).getBonif().multiply(big100).setScale(0,BigDecimal.ROUND_HALF_UP) );
				items[i].setUmed( listFewsDetalle.get(i).getUmed() );
				items[i].setImpTotal( listFewsDetalle.get(i).getImpTotal().multiply(big100).setScale(0,BigDecimal.ROUND_HALF_UP) );
				if (i>0) {
					sbItem.append(",");
				}
				sbItem.append("{codigo: "+items[i].getCodigo()+", ds: "+items[i].getDs()+", umed: "+items[i].getUmed().toString());
				sbItem.append(", qty: "+items[i].getQty().toString()+", precio: "+items[i].getPrecio().toString()+", bonif: "+items[i].getBonif().toString());
				sbItem.append(", importeTotal: "+items[i].getImpTotal().toString()+"}");
			}
			sbItem.append("]");
			logger.debug("Items:"+sbItem.toString());

			ResponseAutenticacion respI = new ResponseAutenticacion();
			respI.setEsReintento(false);

			respI = dolsw.iniciarSesion(interfaz, clave);
			logger.debug("Fin Ejecucion dolsw.iniciarSesion() de WS");

			logger.debug("Codigo:"+respI.getCodigo());
			logger.debug("Descripcion:"+respI.getDescripcion());
			logger.debug("Observacion:"+respI.getObservacion());
			logger.debug("EsReintento:"+respI.getEsReintento());
			logger.debug("IdSesion:"+respI.getIdSesion());

			if (respI.getCodigo().equals(0)) {

				String idSesion;

				Long nroTransaccion = System.currentTimeMillis();
				nroTransaccion = nroTransaccion - (nroTransaccion/1000000000)*1000000000;
				ControlTransaccion ctx = new ControlTransaccion() ;

				idSesion = respI.getIdSesion();
				
				ctx.setIdSesion(idSesion);
				ctx.setInterfaz(interfaz);
				ctx.setNroTransaccion(nroTransaccion);

				nroTransaccion = System.currentTimeMillis();
				nroTransaccion = nroTransaccion - ((nroTransaccion/100000000)*100000000);
				ctx.setNroTransaccion(nroTransaccion);

				registrarAuditoriaIni(ctx, selected, sbIva.toString(), sbTributo.toString(), sbComprobanteAsociado.toString(), sbDatoOpcional.toString(), sbPeriodoAsociado.toString(), sbPermiso.toString(), sbItem.toString());
				logger.debug("Fin Ejecucion AutorizadorFews.registrarAuditoriaIni()");
				
				switch (selected.getTipoCbte()) {
				case 19:
				case 20:
				case 21:
				case 22:
					autorizaWSFEXV1(ctx, selected, fewsXml, fewsQr, 
							concepto,
							tipoDoc,
							nroDoc,
							tipoCbte,
							ptoVta,
							nroCbte,
							
							fechaCbte,
							fechaVencPago,
							fechaServDesde,
							fechaServHasta,
							
							monedaId,
							monedaCtz,

							tipoExpo, 
							permisoExistente, 
							dstCmp, 
							cliente, 
							cuitPaisCliente, 
							domicilioCliente, 
							idImpositivo, 
							obsComerciales, 
							obsGenerales, 
							formaPago, 
							incoterms, 
							incotermsDs, 
							idiomaCbte,
							
							impTotal, 
							impTotConcNoGrav,
							impNeto, 
							impIva,
							impTrib, 
							impOpEx,
							
							tributos, 
							ivas, 
							datosOpcionales,
							comprobantesAsociados, 
							periodosAsociados,
							permisos, 
							items);
					break;
				default:
					autorizaWSFEV1(ctx, selected, fewsXml, fewsQr, concepto,
							 tipoDoc,
							 nroDoc,
							 tipoCbte,
							 ptoVta,
							 nroCbte,
							 fechaCbte,
							 fechaVencPago,
							 fechaServDesde,
							 fechaServHasta,
							 monedaId,
							 monedaCtz,
							 							 
							 impTotal,
							 impTotConcNoGrav,
							 impNeto,
							 impIva,
							 impTrib,
							 impOpEx,
							 
							 tributos,
							 ivas,
							 datosOpcionales,
							 comprobantesAsociados,
							 periodosAsociados);
					break;
				}
				
			} else {
				
				fewsXml.setCodigo(respI.getCodigo());
				fewsXml.setDescripcion(respI.getDescripcion());
				fewsXml.setObservacion(respI.getObservacion());

				fewsXmlDao.update(fewsXml);

				fewsEncabezadoDao.updateLog(selected.getId(), selected.getIdInterfaz());
				logger.debug("Fin Ejecucion fewsEncabezadoDao.updateLog()");

			}

		} catch (Exception e) {
			logger.error(e);
		}

	}
	
	private void autorizaWSFEV1(ControlTransaccion ctx, FewsEncabezado selected, FewsXml fewsXml, FewsQr fewsQr, 
			Integer concepto,
			Integer tipoDoc,
			Long nroDoc,
			Integer tipoCbte,
			Integer ptoVta,
			Long nroCbte,
			String fechaCbte,
			String fechaVencPago,
			String fechaServDesde,
			String fechaServHasta,
			String monedaId,
			BigDecimal monedaCtz,
			BigDecimal impTotal,
			BigDecimal impTotConcNoGrav,
			BigDecimal impNeto,
			BigDecimal impIva,
			BigDecimal impTrib,
			BigDecimal impOpEx,
			Tributo[] tributos,
			Iva[] ivas,
			DatoOpcional[] datosOpcionales,
			ComprobanteAsociado[] comprobantesAsociados,
			PeriodoComprobanteAsociado[] periodosAsociados) {
		
		ResponseAutorizarComprobante respA = new ResponseAutorizarComprobante();
		respA.setEsReintento(false);
		
		try {
			respA = dolsw.autorizarComprobante(ctx, 
					concepto, tipoDoc, nroDoc, 
					tipoCbte, ptoVta, nroCbte, 
					impTotal, impTotConcNoGrav, impNeto, impIva, impTrib, impOpEx, 
					fechaCbte, fechaVencPago, fechaServDesde, fechaServHasta, 
					monedaId, monedaCtz, 
					tributos, ivas, comprobantesAsociados, datosOpcionales, periodosAsociados);
			logger.debug("Ejecucion dolws.autorizarComprobante() de WS");
	
			logger.debug("Codigo:"+respA.getCodigo());
			logger.debug("Descripcion:"+respA.getDescripcion());
			logger.debug("Observacion:"+respA.getObservacion());
			logger.debug("EsReintento:"+respA.getEsReintento());
	
			logger.debug("ExcepcionWSAA:"+respA.getExcepcionWsaa());
			logger.debug("ExcepcionWSFEV1:"+respA.getExcepcionWsfev1());
			logger.debug("CAE:"+respA.getCae());
			logger.debug("FechaVencimiento:"+respA.getFechaVencimiento());
			logger.debug("Resultado:"+respA.getResultado());
			logger.debug("ErrMsg:"+respA.getErrMsg());
			logger.debug("Obs:"+respA.getObs());
			logger.debug("XMLRequest:"+respA.getXmlRequest());
			logger.debug("XMLResponse:"+respA.getXmlResponse());
	
			String codigo = respA.getCodigo().toString();
			String descripcion = respA.getDescripcion();
			String observacion = respA.getObservacion();
	
			String excepcionWsaa = respA.getExcepcionWsaa();
			String excepcionWsfev1 = respA.getExcepcionWsfev1();
	
			String cae = respA.getCae();
			String fechaVto = respA.getFechaVencimiento();
			String resultado = respA.getResultado();
			String errMsg = respA.getErrMsg();
			String obs = respA.getObs();
			String xmlRequest=respA.getXmlRequest();
			String xmlResponse=respA.getXmlResponse();
	
			cae=cae==null||cae.equals("")?"0":cae;
			selected.setCae(Long.valueOf(cae));
			selected.setFechaVto(fechaVto);
			selected.setResultado(resultado);
			selected.setErrMsg(errMsg);
			selected.setMotivo(obs);
	
			if (xmlRequest!=null) {
				fewsXml.setXmlRequest(xmlRequest.toCharArray());
			} else {
				fewsXml.setXmlRequest(descripcion);
			}
			if (xmlResponse!=null) {
				fewsXml.setXmlResponse(xmlResponse.toCharArray());
			} else {
				fewsXml.setXmlResponse(descripcion);
			}
			fewsXml.setCodigo(codigo);
			fewsXml.setDescripcion(descripcion);
			fewsXml.setObservacion(observacion);
			fewsXml.setExcepcionWsaa(excepcionWsaa==null?descripcion:excepcionWsaa);
			fewsXml.setExcepcionWsfev1(excepcionWsfev1==null?descripcion:excepcionWsfev1);
	
			if (respA.getCodigo().equals(0)) {
				ResponseAutenticacion respI = dolsw.iniciarSesion(interfaz, clave);
				logger.debug("Fin Ejecucion dolsw.iniciarSesion() de WS para QR");
	
				logger.debug("Codigo:"+respI.getCodigo());
				logger.debug("Descripcion:"+respI.getDescripcion());
				logger.debug("Observacion:"+respI.getObservacion());
				logger.debug("EsReintento:"+respI.getEsReintento());
				logger.debug("IdSesion:"+respI.getIdSesion());
	
				if (respI.getCodigo().equals(0)) {
					DatoQr datoQr = new DatoQr();
					datoQr.setVer(1);
					datoQr.setFecha(fechaCbte);
					datoQr.setCuit(Long.valueOf(selected.getCuit()));
					datoQr.setPtoVta(ptoVta);
					datoQr.setTipoCmp(tipoCbte);
					datoQr.setNroCmp(nroCbte);
					datoQr.setImporte(impTotal.longValue());
					datoQr.setMoneda(monedaId);
					datoQr.setCtz(monedaCtz.longValue());
					datoQr.setTipoDocRec(tipoDoc);
					datoQr.setNroDocRec(nroDoc);
					datoQr.setTipoCodAut("E"); 				//E-> CAE o A->CAEA
					datoQr.setCodAut(cae);
	
					ResponseGenerarQr respG = dolsw.generarQr(ctx, datoQr);
					if (respG.getCodigo().equals(0)) {
						
						fewsQr.setTextoQr(respG.getTextoQr());
						fewsQr.setImagenQr(respG.getImagenQr());
						
						fewsQrDao.update(fewsQr);
					}
				}
			}
	
			fewsXmlDao.update(fewsXml);
	
			fewsEncabezadoDao.update(selected);
	
			fewsEncabezadoDao.updateLog(selected.getId(), selected.getIdInterfaz());
			logger.debug("Fin Ejecucion fewsEncabezadoDao.updateLog()="+selected.getId().toString()+","+selected.getIdInterfaz().toString());
	
		} catch (Exception e) {
	
			logger.error(e);
	
		} finally {
	
			registrarAuditoriaFin(respA);
			logger.debug("Fin Ejecucion AutorizadorFews.registrarAuditoriaFin()");
	
		}
	}

	private void autorizaWSFEXV1(ControlTransaccion ctx, FewsEncabezado selected, FewsXml fewsXml, FewsQr fewsQr, 
			Integer concepto,
			Integer tipoDoc,
			Long nroDoc,
			Integer tipoCbte,
			Integer ptoVta,
			Long nroCbte,
			String fechaCbte,
			String fechaVencPago,
			String fechaServDesde,
			String fechaServHasta,
			String monedaId,
			BigDecimal monedaCtz,

			Integer tipoExpo, String permisoExistente, Integer dstCmp, 
			String cliente, String cuitPaisCliente, String domicilioCliente, String idImpositivo, 
			String obsComerciales, String obsGenerales, String formaPago, 
			String incoterms, String incotermsDs, String idiomaCbte,
			
			BigDecimal impTotal,
			BigDecimal impTotConcNoGrav,
			BigDecimal impNeto,
			BigDecimal impIva,
			BigDecimal impTrib,
			BigDecimal impOpEx,
			
			Tributo[] tributos,
			Iva[] ivas,
			DatoOpcional[] datosOpcionales,
			ComprobanteAsociado[] comprobantesAsociados,
			PeriodoComprobanteAsociado[] periodosAsociados,
			Permiso[] permisos,
			Item[] items) {

		ResponseAutorizarComprobanteExportacion respA = new ResponseAutorizarComprobanteExportacion();
		respA.setEsReintento(false);
		
		try {
			respA = dolsw.autorizarComprobanteExportacion(ctx, 
					concepto, tipoDoc, nroDoc, 
					tipoCbte, ptoVta, nroCbte, 
					impTotal, impTotConcNoGrav, impNeto, impIva, impTrib, impOpEx, 
					fechaCbte, fechaVencPago, fechaServDesde, fechaServHasta, 
					monedaId, monedaCtz, 
					tipoExpo, permisoExistente, dstCmp, 
					cliente, cuitPaisCliente, domicilioCliente, idImpositivo, 
					obsComerciales, obsGenerales, formaPago, 
					incoterms, incotermsDs, idiomaCbte,
					tributos, ivas, comprobantesAsociados, 
					datosOpcionales, periodosAsociados, 
					permisos, items); 			
			logger.debug("Ejecucion dolws.autorizarComprobanteExportacion() de WS");
	
			logger.debug("Codigo:"+respA.getCodigo());
			logger.debug("Descripcion:"+respA.getDescripcion());
			logger.debug("Observacion:"+respA.getObservacion());
			logger.debug("EsReintento:"+respA.getEsReintento());
	
			logger.debug("ExcepcionWSAA:"+respA.getExcepcionWsaa());
			logger.debug("ExcepcionWSFEXV1:"+respA.getExcepcionWsfexv1());
			logger.debug("CAE:"+respA.getCae());
			logger.debug("FechaVencimiento:"+respA.getFechaVencimiento());
			logger.debug("Resultado:"+respA.getResultado());
			logger.debug("ErrMsg:"+respA.getErrMsg());
			logger.debug("Obs:"+respA.getObs());
			logger.debug("XMLRequest:"+respA.getXmlRequest());
			logger.debug("XMLResponse:"+respA.getXmlResponse());
	
			String codigo = respA.getCodigo().toString();
			String descripcion = respA.getDescripcion();
			String observacion = respA.getObservacion();
	
			String excepcionWsaa = respA.getExcepcionWsaa();
			String excepcionWsfexv1 = respA.getExcepcionWsfexv1();
	
			String cae = respA.getCae();
			String fechaVto = respA.getFechaVencimiento();
			String resultado = respA.getResultado();
			String errMsg = respA.getErrMsg();
			String obs = respA.getObs();
			String xmlRequest=respA.getXmlRequest();
			String xmlResponse=respA.getXmlResponse();
	
			cae=(cae==null||cae.equals("")||cae.equals("null")?"0":cae);
			selected.setCae(Long.valueOf(cae));
			selected.setFechaVto(fechaVto);
			selected.setResultado(resultado);
			selected.setErrMsg(errMsg);
			selected.setMotivo(obs);
	
			if (xmlRequest!=null) {
				fewsXml.setXmlRequest(xmlRequest.toCharArray());
			} else {
				fewsXml.setXmlRequest(descripcion);
			}
			if (xmlResponse!=null) {
				fewsXml.setXmlResponse(xmlResponse.toCharArray());
			} else {
				fewsXml.setXmlResponse(descripcion);
			}
			fewsXml.setCodigo(codigo);
			fewsXml.setDescripcion(descripcion);
			fewsXml.setObservacion(observacion);
			fewsXml.setExcepcionWsaa(excepcionWsaa==null?descripcion:excepcionWsaa);
			fewsXml.setExcepcionWsfexv1(excepcionWsfexv1==null?descripcion:excepcionWsfexv1);
	
			if (respA.getCodigo().equals(0)) {
				ResponseAutenticacion respI = dolsw.iniciarSesion(interfaz, clave);
				logger.debug("Fin Ejecucion dolsw.iniciarSesion() de WS para QR");
	
				logger.debug("Codigo:"+respI.getCodigo());
				logger.debug("Descripcion:"+respI.getDescripcion());
				logger.debug("Observacion:"+respI.getObservacion());
				logger.debug("EsReintento:"+respI.getEsReintento());
				logger.debug("IdSesion:"+respI.getIdSesion());
	
				if (respI.getCodigo().equals(0)) {
					DatoQr datoQr = new DatoQr();
					datoQr.setVer(1);
					datoQr.setFecha(fechaCbte);
					datoQr.setCuit(Long.valueOf(selected.getCuit()));
					datoQr.setPtoVta(ptoVta);
					datoQr.setTipoCmp(tipoCbte);
					datoQr.setNroCmp(nroCbte);
					datoQr.setImporte(impTotal.longValue());
					datoQr.setMoneda(monedaId);
					datoQr.setCtz(monedaCtz.longValue());
					datoQr.setTipoDocRec(tipoDoc);
					datoQr.setNroDocRec(nroDoc);
					datoQr.setTipoCodAut("E"); 				//E-> CAE o A->CAEA
					datoQr.setCodAut(cae);
	
					ResponseGenerarQr respG = dolsw.generarQr(ctx, datoQr);
					if (respG.getCodigo().equals(0)) {
						
						fewsQr.setTextoQr(respG.getTextoQr());
						fewsQr.setImagenQr(respG.getImagenQr());
						
						fewsQrDao.update(fewsQr);
					}
				}
			}
	
			fewsXmlDao.update(fewsXml);
	
			fewsEncabezadoDao.update(selected);
	
			fewsEncabezadoDao.updateLog(selected.getId(), selected.getIdInterfaz());
			logger.debug("Fin Ejecucion fewsEncabezadoDao.updateLog()="+selected.getId().toString()+","+selected.getIdInterfaz().toString());
	
		} catch (Exception e) {
	
			logger.error(e);
	
		} finally {
	
			registrarAuditoriaFin(respA);
			logger.debug("Fin Ejecucion AutorizadorFews.registrarAuditoriaFin()");
	
		}
	}

} 
