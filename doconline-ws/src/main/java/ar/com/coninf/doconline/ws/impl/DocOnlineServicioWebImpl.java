package ar.com.coninf.doconline.ws.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

import org.apache.commons.codec.EncoderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.google.zxing.WriterException;

import ar.com.coninf.doconline.model.dao.InterfazDao;
import ar.com.coninf.doconline.rest.model.enums.ErrorEnum;
import ar.com.coninf.doconline.rest.model.request.RequestActualizarItemComprobante;
import ar.com.coninf.doconline.rest.model.request.RequestAutorizarComprobante;
import ar.com.coninf.doconline.rest.model.request.RequestAutorizarComprobanteExportacion;
import ar.com.coninf.doconline.rest.model.request.RequestConsultarComprobante;
import ar.com.coninf.doconline.rest.model.request.RequestConsultarPadronLocal;
import ar.com.coninf.doconline.rest.model.request.RequestConsultarPadronOnline;
import ar.com.coninf.doconline.rest.model.request.RequestConsultarUltimoComprobante;
import ar.com.coninf.doconline.rest.model.request.RequestGenerarQr;
import ar.com.coninf.doconline.rest.model.response.Response;
import ar.com.coninf.doconline.rest.model.response.ResponseActualizarItemComprobante;
import ar.com.coninf.doconline.rest.model.response.ResponseAutenticacion;
import ar.com.coninf.doconline.rest.model.response.ResponseAutorizarComprobante;
import ar.com.coninf.doconline.rest.model.response.ResponseAutorizarComprobanteExportacion;
import ar.com.coninf.doconline.rest.model.response.ResponseConsultarComprobante;
import ar.com.coninf.doconline.rest.model.response.ResponseConsultarPadronLocal;
import ar.com.coninf.doconline.rest.model.response.ResponseConsultarPadronOnline;
import ar.com.coninf.doconline.rest.model.response.ResponseConsultarUltimoComprobante;
import ar.com.coninf.doconline.rest.model.response.ResponseGenerarQr;
import ar.com.coninf.doconline.rest.model.tx.ComprobanteAsociado;
import ar.com.coninf.doconline.rest.model.tx.ControlTransaccion;
import ar.com.coninf.doconline.rest.model.tx.DatoOpcional;
import ar.com.coninf.doconline.rest.model.tx.DatoQr;
import ar.com.coninf.doconline.rest.model.tx.Iva;
import ar.com.coninf.doconline.rest.model.tx.PeriodoComprobanteAsociado;
import ar.com.coninf.doconline.rest.model.tx.Tributo;
import ar.com.coninf.doconline.shared.dto.InterfazDto;
import ar.com.coninf.doconline.shared.excepcion.ApplicationException;
import ar.com.coninf.doconline.ws.DocOnlineServicioWeb;
import ar.com.coninf.doconline.ws.ayudante.AutenticacionAyudante;
import ar.com.coninf.doconline.ws.ayudante.AutenticacionDatos;
import ar.com.coninf.doconline.ws.ayudante.AutorizarComprobanteAyudante;
import ar.com.coninf.doconline.ws.ayudante.AutorizarComprobanteExportacionAyudante;
import ar.com.coninf.doconline.ws.ayudante.ConsultarComprobanteAyudante;
import ar.com.coninf.doconline.ws.ayudante.ConsultarUltimoComprobanteAyudante;
import ar.com.coninf.doconline.ws.ayudante.GenerarQrAyudante;
import ar.com.coninf.doconline.ws.ayudante.SesionDatos;

@Service(value = "webservice.DocOnlineServicioWeb")
public class DocOnlineServicioWebImpl implements DocOnlineServicioWeb {
	
	@Autowired
	@Qualifier("ayudante.autenticacionAyudante")
	private AutenticacionAyudante autenticacionAyudante;

	@Autowired
	@Qualifier("ayudante.autorizarComprobanteAyudante")
	private AutorizarComprobanteAyudante autorizarComprobanteAyudante;

	@Autowired
	@Qualifier("ayudante.consultarUltimoComprobanteAyudante")
	private ConsultarUltimoComprobanteAyudante consultarUltimoComprobanteAyudante;

	@Autowired
	@Qualifier("ayudante.consultarComprobanteAyudante")
	private ConsultarComprobanteAyudante consultarComprobanteAyudante;
	
	@Autowired
	@Qualifier("interfazDao")
	private InterfazDao interfazDao;

	@Autowired
	@Qualifier("ayudante.generarQrAyudante")
	private GenerarQrAyudante generarQrAyudante;
	
	@Autowired
	@Qualifier("ayudante.autorizarComprobanteExportacionAyudante")
	private AutorizarComprobanteExportacionAyudante autorizarComprobanteExportacionAyudante;
	
	public ResponseAutenticacion iniciarSesion(Integer interfaz, String clave) {
		AutenticacionDatos datos = new AutenticacionDatos();
		
		datos.setInterfaz(interfaz);
		datos.setClave(clave);
		
		return autenticacionAyudante.hacer(null, datos);
	}
	
	public Response cerrarSesion(Integer interfaz, String idSesion) {
		
		SesionDatos datos = new SesionDatos();
		datos.setInterfaz(interfaz);
		datos.setIdSesion(idSesion);
		
		return autenticacionAyudante.hacer(null, datos);
	}

	public ResponseAutorizarComprobante autorizarComprobante(ControlTransaccion ctx, 
			Integer concepto, Integer tipoDoc, Long nroDoc, Integer tipoCbte, Integer ptoVta, Long nroCbte, 
			BigDecimal impTotal, BigDecimal impTotConcNoGrav, BigDecimal impNeto, BigDecimal impIva, BigDecimal impTrib, BigDecimal impOpEx, 
			String fechaCbte, String fechaVencPago, String fechaServDesde, String fechaServHasta,
			String monedaId, BigDecimal monedaCtz, 
			Tributo[] tributos, Iva[] ivas, ComprobanteAsociado[] comprobantesAsociados, 
			DatoOpcional[] datosOpcionales, PeriodoComprobanteAsociado[] periodosAsociados)  {

		RequestAutorizarComprobante datos = new RequestAutorizarComprobante();
		
		datos.setControlTransaccion(ctx);

		//ACA SE TIENE QUE RECUPERAR EL CUIT DE LA SUSCRIPCION
		InterfazDto interfaz = null;
		try {

			interfaz = interfazDao.getById(ctx.getInterfaz());
			if (interfaz == null) {					
				throw new ApplicationException(ErrorEnum.ERROR_INTERFAZ_INVALIDA, "error.ws-interfaz_invalida");
			} else {
				if (ctx.getInterfaz().equals(9901)
						|| ctx.getInterfaz().equals(9902) 
						|| ctx.getInterfaz().equals(2001)
						|| ctx.getInterfaz().equals(2002) 
						|| ctx.getInterfaz().equals(2003)
						|| ctx.getInterfaz().equals(2005)
						|| ctx.getInterfaz().equals(2006)
						|| ctx.getInterfaz().equals(2007)
						|| ctx.getInterfaz().equals(2008)
						|| ctx.getInterfaz().equals(2009)
						|| ctx.getInterfaz().equals(4001)
						|| ctx.getInterfaz().equals(5001)
						|| ctx.getInterfaz().equals(5002)) {
					datos.setCuit(interfaz.getCuitSuscripcion());
				} else {
					throw new ApplicationException(ErrorEnum.ERROR_INTERFAZ_INVALIDA, "error.ws-interfaz_invalida");
				}
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e);
		}

		BigDecimal big100 = new BigDecimal("100");
		
		datos.setConcepto(concepto);
		datos.setTipoDoc(tipoDoc);
		datos.setNroDoc(nroDoc);
		datos.setTipoCbte(tipoCbte);
		datos.setPtoVta(ptoVta);
		datos.setNroCbte(nroCbte);
		datos.setImpTotal(impTotal.divide(big100));
		datos.setImpTotConcNoGrav(impTotConcNoGrav.divide(big100));
		datos.setImpNeto(impNeto.divide(big100));
		datos.setImpIva(impIva.divide(big100));
		datos.setImpTrib(impTrib.divide(big100));
		datos.setImpOpEx(impOpEx.divide(big100));
		datos.setFechaCbte(fechaCbte);
		datos.setFechaServDesde(fechaServDesde);
		datos.setFechaServHasta(fechaServHasta);
		datos.setFechaVencPago(fechaVencPago);
		datos.setMonedaId(monedaId);
		datos.setMonedaCtz(monedaCtz.divide(big100));
		
		BigDecimal sumaTotal = BigDecimal.ZERO;
		sumaTotal = sumaTotal.add(impTotConcNoGrav);
		sumaTotal = sumaTotal.add(impNeto);
		sumaTotal = sumaTotal.add(impIva);
		sumaTotal = sumaTotal.add(impTrib);
		sumaTotal = sumaTotal.add(impOpEx);
		
		if (!sumaTotal.equals(impTotal)) {
			throw new ApplicationException(ErrorEnum.ERROR_DIFERENCIA_TOTAL_FACTURA, "error.ws-diferencia_total_factura");
		}
		
		BigDecimal sumaTributos = BigDecimal.ZERO;
		int iTributo = -1;
		for (int i = 0; tributos != null && i < tributos.length; i++) {
			if (!(tributos[i]).toString().trim().equals("")) {
				if (tributos[i].getTributoDesc().equals("PERCEP_IB")
					|| tributos[i].getTributoDesc().equals("PERCEPCION_IB")	) {
					iTributo = i;
				}
				tributos[i].setTributoBaseImp(tributos[i].getTributoBaseImp().divide(big100));
				tributos[i].setTributoImporte(tributos[i].getTributoImporte().divide(big100));
				tributos[i].setTributoAlic(tributos[i].getTributoAlic().divide(big100));
			}
			sumaTributos = sumaTributos.add(tributos[i].getTributoImporte());
		}
		if (sumaTributos.equals(BigDecimal.ZERO) && !datos.getImpTrib().equals(BigDecimal.ZERO)) {
			throw new ApplicationException(ErrorEnum.ERROR_DIFERENCIA_TOTAL_TRIBUTO, "error.ws-diferencia_total_tributo");
		}
		if (!sumaTributos.equals(datos.getImpTrib()) && iTributo >= 0) {
			BigDecimal nuevaPercepcion = tributos[iTributo].getTributoImporte().add(datos.getImpTrib().subtract(sumaTributos));
			tributos[iTributo].setTributoImporte(nuevaPercepcion);
		}
		
		BigDecimal sumaIvas = BigDecimal.ZERO;
		for (int i = 0; ivas != null && i < ivas.length; i++) {
			if (!(ivas[i]).toString().trim().equals("")) {
				if (ivas[i]!=null && ivas[i].getIvaId()!=null) {
					ivas[i].setIvaBaseImp(ivas[i].getIvaBaseImp().divide(big100));
					ivas[i].setIvaImporte(ivas[i].getIvaImporte().divide(big100));
					sumaIvas = sumaIvas.add(ivas[i].getIvaImporte());
				}
			}
		}
		if (sumaIvas.equals(BigDecimal.ZERO) && !datos.getImpIva().equals(BigDecimal.ZERO)) {
			throw new ApplicationException(ErrorEnum.ERROR_DIFERENCIA_TOTAL_IVA, "error.ws-diferencia_total_iva");
		}
		if (!sumaIvas.equals(datos.getImpIva())) {
			BigDecimal nuevoIva = ivas[0].getIvaImporte().add(datos.getImpIva().subtract(sumaIvas));
			ivas[0].setIvaImporte(nuevoIva);
		}

		for (int i = 0; comprobantesAsociados != null && i < comprobantesAsociados.length; i++) {
			if (!(comprobantesAsociados[i]).toString().trim().equals("")) {
			}
		}

		for (int i = 0; datosOpcionales != null && i < datosOpcionales.length; i++) {
			if (!(datosOpcionales[i]).toString().trim().equals("")) {
			}
		}
		
		for (int i = 0; periodosAsociados != null && i < periodosAsociados.length; i++) {
			if (!(periodosAsociados[i]).toString().trim().equals("")) {
			}
		}
		
		datos.setTributos(tributos);
		datos.setIvas(ivas);
		datos.setDatosOpcionales(datosOpcionales);
		
		//SOLO DEBE ENVIARSE COMPROBANTE ASOC O PERIODO ASOC
		datos.setComprobantesAsociados(comprobantesAsociados);
		datos.setPeriodoComprobanteAsociados(periodosAsociados);
		
		return autorizarComprobanteAyudante.hacer(ctx, datos);
	}

	public ResponseConsultarUltimoComprobante consultarUltimoComprobante(ControlTransaccion ctx, Integer tipoCbte, Integer ptoVta) {
		
		RequestConsultarUltimoComprobante datos = new RequestConsultarUltimoComprobante();
		
		datos.setControlTransaccion(ctx);
		
			//ACA SE TIENE QUE RECUPERAR EL CUIT DE LA SUSCRIPCION
			InterfazDto interfaz = null;
			try {

				interfaz = interfazDao.getById(ctx.getInterfaz());
				if(interfaz == null){					
					throw new ApplicationException(ErrorEnum.ERROR_INTERFAZ_INVALIDA, "error.ws-interfaz_invalida");
				} else {
					
					if (ctx.getInterfaz().equals(9901)
							|| ctx.getInterfaz().equals(9902) 
							|| ctx.getInterfaz().equals(2001)
							|| ctx.getInterfaz().equals(2002) 
							|| ctx.getInterfaz().equals(2003)
							|| ctx.getInterfaz().equals(2005)
							|| ctx.getInterfaz().equals(2006)
							|| ctx.getInterfaz().equals(2007)
							|| ctx.getInterfaz().equals(2008)
							|| ctx.getInterfaz().equals(2009)
							|| ctx.getInterfaz().equals(4001)
							|| ctx.getInterfaz().equals(5001)
							|| ctx.getInterfaz().equals(5002)) {
						datos.setCuit(interfaz.getCuitSuscripcion());
					} else {
						throw new ApplicationException(ErrorEnum.ERROR_INTERFAZ_INVALIDA, "error.ws-interfaz_invalida");
					}
				}
			
			} catch (SQLException e) {
				e.printStackTrace();
				throw new ApplicationException(e);
			}

		datos.setTipoCbte(tipoCbte);
		datos.setPtoVta(ptoVta);

		return consultarUltimoComprobanteAyudante.hacer(ctx, datos);
	};

	@Override
	public ResponseConsultarComprobante consultarComprobante(ControlTransaccion ctx, Integer tipoCbte, Integer ptoVta, Integer cbte) {
		
		RequestConsultarComprobante datos = new RequestConsultarComprobante();
		
		datos.setControlTransaccion(ctx);
			
			//ACA SE TIENE QUE RECUPERAR EL CUIT DE LA SUSCRIPCION
			InterfazDto interfaz = null;
			try {

				interfaz = interfazDao.getById(ctx.getInterfaz());
				if(interfaz == null){					
					throw new ApplicationException(ErrorEnum.ERROR_INTERFAZ_INVALIDA, "error.ws-interfaz_invalida");
				} else {
					
					if (ctx.getInterfaz().equals(9901)
							|| ctx.getInterfaz().equals(9902) 
							|| ctx.getInterfaz().equals(2001)
							|| ctx.getInterfaz().equals(2002) 
							|| ctx.getInterfaz().equals(2003)
							|| ctx.getInterfaz().equals(2005)
							|| ctx.getInterfaz().equals(2006)
							|| ctx.getInterfaz().equals(2007)
							|| ctx.getInterfaz().equals(2008)
							|| ctx.getInterfaz().equals(2009)
							|| ctx.getInterfaz().equals(4001)
							|| ctx.getInterfaz().equals(5001)
							|| ctx.getInterfaz().equals(5002)) {
						datos.setCuit(interfaz.getCuitSuscripcion());
					} else {
						throw new ApplicationException(ErrorEnum.ERROR_INTERFAZ_INVALIDA, "error.ws-interfaz_invalida");
					}
				}
			
			} catch (SQLException e) {
				e.printStackTrace();
				throw new ApplicationException(e);
			}

		datos.setTipoCbte(tipoCbte);
		datos.setPtoVta(ptoVta);
		datos.setCbte(cbte);
		
		return consultarComprobanteAyudante.hacer(ctx, datos);
	}

	@Override
	public ResponseActualizarItemComprobante actualizarItemComprobante(RequestActualizarItemComprobante request) {
		ResponseActualizarItemComprobante resp = new ResponseActualizarItemComprobante();
		resp.setEsReintento(false);
		resp.cargarError(new Response(ErrorEnum.SIN_ERROR));
		return resp;
	}

	@Override
	public ResponseConsultarPadronLocal consultarPadronLocal(RequestConsultarPadronLocal request) {
		ResponseConsultarPadronLocal resp = new ResponseConsultarPadronLocal();
		resp.setEsReintento(false);
		resp.cargarError(new Response(ErrorEnum.SIN_ERROR));
		return resp;
	}

	@Override
	public ResponseConsultarPadronOnline consultarPadronOnline(RequestConsultarPadronOnline request) {
		ResponseConsultarPadronOnline resp = new ResponseConsultarPadronOnline();
		resp.setEsReintento(false);
		resp.cargarError(new Response(ErrorEnum.SIN_ERROR));
		return resp;
	}
	
	@Override
	public ResponseGenerarQr generarQr(ControlTransaccion ctx, DatoQr datoQr) throws IOException, WriterException, EncoderException {
		RequestGenerarQr datos = new RequestGenerarQr();
		datos.setControlTransaccion(ctx);
		datos.setDatoQr(datoQr);
		return generarQrAyudante.hacer(ctx, datos);
	}

	public ResponseAutorizarComprobanteExportacion autorizarComprobanteExportacion(ControlTransaccion ctx, 
			Integer concepto, Integer tipoDoc, Long nroDoc, Integer tipoCbte, Integer ptoVta, Long nroCbte, 
			BigDecimal impTotal, BigDecimal impTotConcNoGrav, BigDecimal impNeto, BigDecimal impIva, BigDecimal impTrib, BigDecimal impOpEx, 
			String fechaCbte, String fechaVencPago, String fechaServDesde, String fechaServHasta,
			String monedaId, BigDecimal monedaCtz, 
			Tributo[] tributos, Iva[] ivas, ComprobanteAsociado[] comprobantesAsociados, 
			DatoOpcional[] datosOpcionales, PeriodoComprobanteAsociado[] periodosAsociados)  {

		RequestAutorizarComprobanteExportacion datos = new RequestAutorizarComprobanteExportacion();
		
		datos.setControlTransaccion(ctx);

		//ACA SE TIENE QUE RECUPERAR EL CUIT DE LA SUSCRIPCION
		InterfazDto interfaz = null;
		try {

			interfaz = interfazDao.getById(ctx.getInterfaz());
			if (interfaz == null) {					
				throw new ApplicationException(ErrorEnum.ERROR_INTERFAZ_INVALIDA, "error.ws-interfaz_invalida");
			} else {
				if (ctx.getInterfaz().equals(9901)
						|| ctx.getInterfaz().equals(9902) 
						|| ctx.getInterfaz().equals(2001)
						|| ctx.getInterfaz().equals(2002) 
						|| ctx.getInterfaz().equals(2003)
						|| ctx.getInterfaz().equals(2005)
						|| ctx.getInterfaz().equals(2006)
						|| ctx.getInterfaz().equals(2007)
						|| ctx.getInterfaz().equals(2008)
						|| ctx.getInterfaz().equals(2009)
						|| ctx.getInterfaz().equals(4001)
						|| ctx.getInterfaz().equals(5001)
						|| ctx.getInterfaz().equals(5002)) {
					datos.setCuit(interfaz.getCuitSuscripcion());
				} else {
					throw new ApplicationException(ErrorEnum.ERROR_INTERFAZ_INVALIDA, "error.ws-interfaz_invalida");
				}
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e);
		}

		BigDecimal big100 = new BigDecimal("100");
		
		datos.setConcepto(concepto);
		datos.setTipoDoc(tipoDoc);
		datos.setNroDoc(nroDoc);
		datos.setTipoCbte(tipoCbte);
		datos.setPtoVta(ptoVta);
		datos.setNroCbte(nroCbte);
		datos.setImpTotal(impTotal.divide(big100));
		datos.setImpTotConcNoGrav(impTotConcNoGrav.divide(big100));
		datos.setImpNeto(impNeto.divide(big100));
		datos.setImpIva(impIva.divide(big100));
		datos.setImpTrib(impTrib.divide(big100));
		datos.setImpOpEx(impOpEx.divide(big100));
		datos.setFechaCbte(fechaCbte);
		datos.setFechaServDesde(fechaServDesde);
		datos.setFechaServHasta(fechaServHasta);
		datos.setFechaVencPago(fechaVencPago);
		datos.setMonedaId(monedaId);
		datos.setMonedaCtz(monedaCtz.divide(big100));
		
		BigDecimal sumaTotal = BigDecimal.ZERO;
		sumaTotal = sumaTotal.add(impTotConcNoGrav);
		sumaTotal = sumaTotal.add(impNeto);
		sumaTotal = sumaTotal.add(impIva);
		sumaTotal = sumaTotal.add(impTrib);
		sumaTotal = sumaTotal.add(impOpEx);
		
		if (!sumaTotal.equals(impTotal)) {
			throw new ApplicationException(ErrorEnum.ERROR_DIFERENCIA_TOTAL_FACTURA, "error.ws-diferencia_total_factura");
		}
		
		BigDecimal sumaTributos = BigDecimal.ZERO;
		int iTributo = -1;
		for (int i = 0; tributos != null && i < tributos.length; i++) {
			if (!(tributos[i]).toString().trim().equals("")) {
				if (tributos[i].getTributoDesc().equals("PERCEP_IB")
					|| tributos[i].getTributoDesc().equals("PERCEPCION_IB")	) {
					iTributo = i;
				}
				tributos[i].setTributoBaseImp(tributos[i].getTributoBaseImp().divide(big100));
				tributos[i].setTributoImporte(tributos[i].getTributoImporte().divide(big100));
				tributos[i].setTributoAlic(tributos[i].getTributoAlic().divide(big100));
			}
			sumaTributos = sumaTributos.add(tributos[i].getTributoImporte());
		}
		if (sumaTributos.equals(BigDecimal.ZERO) && !datos.getImpTrib().equals(BigDecimal.ZERO)) {
			throw new ApplicationException(ErrorEnum.ERROR_DIFERENCIA_TOTAL_TRIBUTO, "error.ws-diferencia_total_tributo");
		}
		if (!sumaTributos.equals(datos.getImpTrib()) && iTributo >= 0) {
			BigDecimal nuevaPercepcion = tributos[iTributo].getTributoImporte().add(datos.getImpTrib().subtract(sumaTributos));
			tributos[iTributo].setTributoImporte(nuevaPercepcion);
		}
		
		BigDecimal sumaIvas = BigDecimal.ZERO;
		for (int i = 0; ivas != null && i < ivas.length; i++) {
			if (!(ivas[i]).toString().trim().equals("")) {
				if (ivas[i]!=null && ivas[i].getIvaId()!=null) {
					ivas[i].setIvaBaseImp(ivas[i].getIvaBaseImp().divide(big100));
					ivas[i].setIvaImporte(ivas[i].getIvaImporte().divide(big100));
					sumaIvas = sumaIvas.add(ivas[i].getIvaImporte());
				}
			}
		}
		if (sumaIvas.equals(BigDecimal.ZERO) && !datos.getImpIva().equals(BigDecimal.ZERO)) {
			throw new ApplicationException(ErrorEnum.ERROR_DIFERENCIA_TOTAL_IVA, "error.ws-diferencia_total_iva");
		}
		if (!sumaIvas.equals(datos.getImpIva())) {
			BigDecimal nuevoIva = ivas[0].getIvaImporte().add(datos.getImpIva().subtract(sumaIvas));
			ivas[0].setIvaImporte(nuevoIva);
		}

		for (int i = 0; comprobantesAsociados != null && i < comprobantesAsociados.length; i++) {
			if (!(comprobantesAsociados[i]).toString().trim().equals("")) {
			}
		}

		for (int i = 0; datosOpcionales != null && i < datosOpcionales.length; i++) {
			if (!(datosOpcionales[i]).toString().trim().equals("")) {
			}
		}
		
		for (int i = 0; periodosAsociados != null && i < periodosAsociados.length; i++) {
			if (!(periodosAsociados[i]).toString().trim().equals("")) {
			}
		}
		
		datos.setTributos(tributos);
		datos.setIvas(ivas);
		datos.setDatosOpcionales(datosOpcionales);
		
		//SOLO DEBE ENVIARSE COMPROBANTE ASOC O PERIODO ASOC
		datos.setComprobantesAsociados(comprobantesAsociados);
		datos.setPeriodoComprobanteAsociados(periodosAsociados);
		
		return autorizarComprobanteExportacionAyudante.hacer(ctx, datos);
	}

}
