package ar.com.coninf.doconline.ws.timer;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.apache.axis.AxisFault;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ar.com.coninf.doconline.model.dao.FewsComprobanteAsociadoDao;
import ar.com.coninf.doconline.model.dao.FewsDatoOpcionalDao;
import ar.com.coninf.doconline.model.dao.FewsEncabezadoDao;
import ar.com.coninf.doconline.model.dao.FewsIvaDao;
import ar.com.coninf.doconline.model.dao.FewsTributoDao;
import ar.com.coninf.doconline.model.dao.FewsXmlDao;
import ar.com.coninf.doconline.rest.model.response.xsd.ResponseAutenticacion;
import ar.com.coninf.doconline.rest.model.response.xsd.ResponseAutorizarComprobante;
import ar.com.coninf.doconline.rest.model.tx.xsd.ComprobanteAsociado;
import ar.com.coninf.doconline.rest.model.tx.xsd.ControlTransaccion;
import ar.com.coninf.doconline.rest.model.tx.xsd.DatoOpcional;
import ar.com.coninf.doconline.rest.model.tx.xsd.Iva;
import ar.com.coninf.doconline.rest.model.tx.xsd.Tributo;
import ar.com.coninf.doconline.shared.dto.FewsComprobanteAsociado;
import ar.com.coninf.doconline.shared.dto.FewsDatoOpcional;
import ar.com.coninf.doconline.shared.dto.FewsEncabezado;
import ar.com.coninf.doconline.shared.dto.FewsIva;
import ar.com.coninf.doconline.shared.dto.FewsResultado;
import ar.com.coninf.doconline.shared.dto.FewsTributo;
import ar.com.coninf.doconline.shared.dto.FewsXml;
import ar.com.coninf.doconline.shared.excepcion.ApplicationException;
import ar.com.coninf.doconline.ws.impl.DocOnlineServicioWeb;
import ar.com.coninf.doconline.ws.impl.DocOnlineServicioWebLocator;
import ar.com.coninf.doconline.ws.impl.DocOnlineServicioWebPortType;
import ar.com.coninf.doconline.ws.impl.DocOnlineServicioWebSoap11BindingStub;

@Component(value = "timer.AutorizadorSql")
public class AutorizadorSql {
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
	@Qualifier("dolProperties")
	protected Properties dolProperties;

	@Autowired
	@Qualifier("webserviceclient.DocOnlineServicioWebLocator")
	protected DocOnlineServicioWeb docOnlineServicioWeb;
	
	private Integer interfaz;
	private String  clave;

	public AutorizadorSql() {

		this.interfaz = 9901;
		this.clave = "1234";

	}

	public AutorizadorSql(Integer interfaz, String clave) {

		this.interfaz = interfaz;
		this.clave = clave;

	}

	public void logTimerSql() {
		
		logger.debug("Ejecucion logTimerSql()");
		
		try {

			List<FewsResultado> lista = fewsEncabezadoDao.setLogTimerSql();
			if (lista==null)
				logger.info("Lista de Resultado NULL");
			else
				logger.info("Lista de Resultado "+String.valueOf(lista.size()));

		} catch (SQLException e) {
			logger.error(e);
		} catch (ApplicationException e) {
			logger.error(e);
		} catch (Exception e) {
			logger.error(e);
		}
	
	}

	public void procesarPendientes() {

		try {

			logger.debug("Ejecucion procesarPendientes()");
			
			fewsEncabezadoDao.importLog();
			logger.debug("Fin Ejecucion importLog()");
			
			List<FewsEncabezado> pendientes = fewsEncabezadoDao.getFewsPendiente((long) -1);
			logger.debug("Fin Ejecucion getFewsPendiente()");
			
			for (int i=0; i < pendientes.size(); i++) {

				autorizarComprobante(pendientes.get(i));
				logger.debug("Fin Ejecucion AutorizadorSql.autorizarComprobante()");
				
			}

		} catch (SQLException e) {
			logger.error(e);
		} catch (ApplicationException e) {
			logger.error(e);
		} catch (Exception e) {
			logger.error(e);
		}

	}

	public void autorizarComprobante(FewsEncabezado selected) {

		logger.debug("Ejecucion AutorizadorSql.autorizarComprobante()");
		
		try {

			List<FewsIva> listFewsIva = fewsIvaDao.getListById(selected.getId());
			List<FewsTributo> listFewsTributo = fewsTributoDao.getListById(selected.getId());
			List<FewsDatoOpcional> listFewsDatoOpcional = fewsDatoOpcionalDao.getListById(selected.getId());
			List<FewsComprobanteAsociado> listFewsComprobanteAsociado = fewsComprobanteAsociadoDao.getListById(selected.getId());

			FewsXml fewsXml = fewsXmlDao.getById(selected.getId());

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
			BigDecimal impTotal = selected.getImpTotal().multiply(big100).setScale(0,BigDecimal.ROUND_HALF_UP);
			BigDecimal impTotConcNoGrav = selected.getImpTotConc().multiply(big100).setScale(0,BigDecimal.ROUND_HALF_UP);
			BigDecimal impNeto = selected.getImpNeto().multiply(big100).setScale(0,BigDecimal.ROUND_HALF_UP);
			BigDecimal impIva = selected.getImpIva().multiply(big100).setScale(0,BigDecimal.ROUND_HALF_UP);
			BigDecimal impTrib = selected.getImpTrib().multiply(big100).setScale(0,BigDecimal.ROUND_HALF_UP);
			BigDecimal impOpEx = selected.getImpOpEx().multiply(big100).setScale(0,BigDecimal.ROUND_HALF_UP);
			BigDecimal monedaCtz = selected.getMonedaCtz().multiply(big100).setScale(0,BigDecimal.ROUND_HALF_UP);
			Tributo[] tributos = new Tributo[listFewsTributo.size()];
			Iva[] ivas = new Iva[listFewsIva.size()];
			DatoOpcional[] datosOpcionales = new DatoOpcional[listFewsDatoOpcional.size()];
			ComprobanteAsociado[] comprobantesAsociados = new ComprobanteAsociado[listFewsComprobanteAsociado.size()];

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
				sbTributo.append("{tributoId: "+tributos[i].getTributoId().toString()+", tributoDesc: "+tributos[i].getTributoDesc().toString()+", tributoAlic: "+tributos[i].getTributoAlic().toString()+", tributoBaseImp: "+tributos[i].getTributoBaseImp().toString()+", tributoImporte: "+tributos[i].getTributoImporte().toString()+"}");
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

			ResponseAutenticacion respI = new ResponseAutenticacion();
			ResponseAutorizarComprobante respA = new ResponseAutorizarComprobante();
			Integer interfaz = this.interfaz;
			String clave = this.clave;
			String idSesion;

			Long nroTransaccion = System.currentTimeMillis();
			nroTransaccion = nroTransaccion - (nroTransaccion/1000000000)*1000000000;
			ControlTransaccion ctx = new ControlTransaccion() ;

			try {

				DocOnlineServicioWeb w = new DocOnlineServicioWebLocator();
				DocOnlineServicioWebPortType ws = new DocOnlineServicioWebSoap11BindingStub(new URL(w.getDocOnlineServicioWebHttpSoap11EndpointAddress()),w);

				respI = ws.iniciarSesion(interfaz, clave);
				logger.debug("Fin Ejecucion iniciarSesion() de WS");

				logger.debug("Codigo:"+respI.getCodigo());
				logger.debug("Descripcion:"+respI.getDescripcion());
				logger.debug("Observacion:"+respI.getObservacion());
				logger.debug("EsReintento:"+respI.getEsReintento());
				logger.debug("IdSesion:"+respI.getIdSesion());

				if (respI.getCodigo().equals(0)) {
					idSesion = respI.getIdSesion();
					ctx.setIdSesion(idSesion);
					ctx.setInterfaz(interfaz);
					ctx.setNroTransaccion(nroTransaccion);
	
					try {
						nroTransaccion = System.currentTimeMillis();
						nroTransaccion = nroTransaccion - ((nroTransaccion/100000000)*100000000);
						ctx.setNroTransaccion(nroTransaccion);
	
						respA = ws.autorizarComprobante(ctx, 
									concepto, tipoDoc, nroDoc, 
									tipoCbte, ptoVta, nroCbte, 
									impTotal, impTotConcNoGrav, impNeto, impIva, impTrib, impOpEx, 
									fechaCbte, fechaVencPago, fechaServDesde, fechaServHasta, 
									monedaId, monedaCtz, 
									tributos, ivas, comprobantesAsociados, datosOpcionales);
							logger.debug("Ejecucion autorizarComprobante() de WS");

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
		
							if (!(xmlRequest==null)) {
								fewsXml.setXmlRequest(xmlRequest.toCharArray());
							} else {
								fewsXml.setXmlRequest(descripcion);
							}
							if (!(xmlResponse==null)) {
								fewsXml.setXmlResponse(xmlResponse.toCharArray());
							} else {
								fewsXml.setXmlResponse(descripcion);
							}
							fewsXml.setCodigo(codigo);
							fewsXml.setDescripcion(descripcion);
							fewsXml.setObservacion(observacion);
							fewsXml.setExcepcionWsaa(excepcionWsaa==null?descripcion:excepcionWsaa);
							fewsXml.setExcepcionWsfev1(excepcionWsfev1==null?descripcion:excepcionWsfev1);
	
							try {
								
								fewsXmlDao.update(fewsXml);
								
								fewsEncabezadoDao.update(selected);
	
								fewsEncabezadoDao.updateLog(selected.getId());
								logger.debug("Fin Ejecucion updateLog()");
								
							} catch (SQLException e) {
								logger.error(e);
							} catch (ApplicationException e) {
								logger.error(e);
							} catch (Exception e) {
								logger.error(e);
							}
	
					} catch (AxisFault e) {
						logger.error(e);
					} catch (RemoteException e) {
						logger.error(e);
					} catch (ApplicationException e) {
						logger.error(e);
					} catch (Exception e) {
						logger.error(e);
					}

				} else {

					try {

						fewsXml.setCodigo(respI.getCodigo());
						fewsXml.setDescripcion(respI.getDescripcion());
						fewsXml.setObservacion(respI.getObservacion());
		
						fewsXmlDao.update(fewsXml);

						fewsEncabezadoDao.updateLog(selected.getId());
						logger.debug("Fin Ejecucion updateLog()");

					} catch (SQLException e) {
						logger.error(e);
					} catch (ApplicationException e) {
						logger.error(e);
					} catch (Exception e) {
						logger.error(e);
					}

				}

			} catch (AxisFault e) {
				logger.error(e);
			} catch (RemoteException e) {
				logger.error(e);
			} catch (MalformedURLException e) {
				logger.error(e);
			} catch (ApplicationException e) {
				logger.error(e);
			} catch (Exception e) {
				logger.error(e);
			}
			
		} catch (ApplicationException e) {
			logger.error(e);
		} catch (Exception e) {
			logger.error(e);
		}

	}
	
} 
