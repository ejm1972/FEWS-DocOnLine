package ar.com.coninf.doconline.business.negocio;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import ar.com.coninf.doconline.rest.model.enums.ErrorEnum;
import ar.com.coninf.doconline.rest.model.request.RequestAutorizarComprobante;
import ar.com.coninf.doconline.rest.model.response.Response;
import ar.com.coninf.doconline.rest.model.response.ResponseAutorizarComprobante;
import ar.com.coninf.doconline.rest.model.tx.ComprobanteAsociado;
import ar.com.coninf.doconline.rest.model.tx.DatoOpcional;
import ar.com.coninf.doconline.rest.model.tx.Iva;
import ar.com.coninf.doconline.rest.model.tx.Tributo;
import ar.com.coninf.doconline.shared.excepcion.ApplicationException;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComFailException;
import com.jacob.com.Dispatch;
import com.jacob.com.LibraryLoader;
import com.jacob.com.Variant;

@Component("business.autorizarComprobanteBusiness")
public class AutorizarComprobanteBusiness extends AbstractBusiness {

	Logger logger = Logger.getLogger(this.getClass());

	public synchronized ResponseAutorizarComprobante autorizarComprobante(RequestAutorizarComprobante datos) {

		logger.debug("Ejecucion autorizarComprobante() de WS");
		
		ResponseAutorizarComprobante resp = new ResponseAutorizarComprobante();
		resp.setEsReintento(false);
		resp.cargarError(new Response(ErrorEnum.SIN_ERROR));

		try {

			validarPuntoVenta(datos.getControlTransaccion().getInterfaz(), datos.getPtoVta());
			validarHomologacion(datos.getControlTransaccion().getInterfaz());
			validarProxy();
			validarTimeOut();
			validarUserDir();
			
			archivoKey = "privada-"+datos.getCuit()+".key";
			archivoCrt = "certificado-"+datos.getCuit()+".crt";
			
			String cache = "";
			String proxy = urlProxy;
			String wsdl = "";
			String timeout = paramTimeout;			
			String userDir = paramUserDir;
	
			String libFile = System.getProperty("os.arch").equals("amd64") ? "jacob-1.18-M2-x64.dll" : "jacob-1.18-M2-x86.dll";

			System.setProperty(LibraryLoader.JACOB_DLL_PATH, userDir+libFile);
			logger.debug(userDir+libFile);

			LibraryLoader.loadJacobLibrary();

			/* Crear objeto WSAA: Web Service de Autenticacion y Autorizacion */
			ActiveXComponent wsaa = new ActiveXComponent("WSAA");

			logger.debug("Directorio de Instalacion: " +
					Dispatch.get(wsaa, "InstallDir").toString() + 
					" Version: " +
					Dispatch.get(wsaa, "Version").toString()
					);

			/****************************************************************************************************/			
			wsdl = urlWsaa;
			Dispatch.call(wsaa, "Autenticar", 
					new Variant("wsfe"), 
					new Variant(userDir + archivoCrt), 
					new Variant(userDir + archivoKey), 
					new Variant(wsdl),
					new Variant(proxy));
					
			String excepcion =  Dispatch.get(wsaa, "Excepcion").toString();
			logger.debug("Excepcion: " + excepcion);
			resp.setExcepcionWsaa(excepcion);

			if (excepcion.equals("")) {

				String token = Dispatch.get(wsaa, "Token").toString();
				logger.debug("Token: " + token);
				String sign = Dispatch.get(wsaa, "Sign").toString();
				logger.debug("Sign: " + sign);
				/****************************************************************************************************/			

				/* Instanciar WSFEv1: WebService de Factura Electronica version 1 */
				ActiveXComponent wsfev1 = new ActiveXComponent("WSFEv1");

				logger.debug("Directorio de Instalacion: " +
						Dispatch.get(wsfev1, "InstallDir").toString() + 
						" Version: " + 
						Dispatch.get(wsfev1, "Version").toString()
						);

				/* Establecer parametros de uso: */
				Dispatch.put(wsfev1, "Cuit", new Variant(datos.getCuit()));
				Dispatch.put(wsfev1, "Token", new Variant(token));
				Dispatch.put(wsfev1, "Sign", new Variant(sign));

				/* Conectar al websrvice (cambiar URL para producción) */
				wsdl = urlWsfev1;
				Dispatch.call(wsfev1, "Conectar", 
						new Variant(cache), 
						new Variant(wsdl),
						new Variant(proxy),
						null,
						null,
						new Variant(timeout));
				
				excepcion =  Dispatch.get(wsfev1, "Excepcion").toString();
				logger.debug("Excepcion: " + excepcion);
				resp.setExcepcionWsfev1(excepcion);

				if (excepcion.equals("")) {

					/* PROCESO DE CAE */             
					String concepto = datos.getConcepto().toString();
					String tipo_doc = datos.getTipoDoc().toString(), nro_doc = datos.getNroDoc().toString();

					String tipo_cbte = datos.getTipoCbte().toString();
					String pto_vta = datos.getPtoVta().toString();
					int cbte_nro = Integer.parseInt(datos.getNroCbte().toString()), cbt_desde = cbte_nro, cbt_hasta = cbte_nro;

					String imp_total = datos.getImpTotal().toString();
					String imp_tot_conc = datos.getImpTotConcNoGrav().toString();
					String imp_neto = datos.getImpNeto().toString();
					String imp_iva = datos.getImpIva().toString(), imp_trib = datos.getImpTrib().toString(), imp_op_ex = datos.getImpOpEx().toString();

					String fecha_cbte = datos.getFechaCbte();
					String fecha_venc_pago = "", fecha_serv_desde = "", fecha_serv_hasta = "";
					
					if (concepto.equals("2") || concepto.equals("3")) {
						fecha_serv_desde = datos.getFechaServDesde();
						fecha_serv_hasta = datos.getFechaServHasta();
						fecha_venc_pago = datos.getFechaVencPago();
					}

					if (tipo_cbte.equals("201")
							|| tipo_cbte.equals("206")
							|| tipo_cbte.equals("211")
							) {
						fecha_venc_pago = datos.getFechaVencPago();
					}

					if (tipo_cbte.equals("202")
							|| tipo_cbte.equals("203")
							|| tipo_cbte.equals("207")
							|| tipo_cbte.equals("208")
							|| tipo_cbte.equals("212")
							|| tipo_cbte.equals("213")
							) {
						fecha_venc_pago = "";
					}

					String moneda_id = datos.getMonedaId(), moneda_ctz = datos.getMonedaCtz().toString();

					Variant ok = Dispatch.call(wsfev1, "CrearFactura",
							new Variant(concepto), new Variant(tipo_doc), 
							new Variant(nro_doc), new Variant(tipo_cbte), 
							new Variant(pto_vta), 
							new Variant(cbt_desde), new Variant(cbt_hasta), 
							new Variant(imp_total), new Variant(imp_tot_conc), 
							new Variant(imp_neto), new Variant(imp_iva), 
							new Variant(imp_trib), new Variant(imp_op_ex), 
							new Variant(fecha_cbte), new Variant(fecha_venc_pago), 
							new Variant(fecha_serv_desde), new Variant(fecha_serv_hasta),
							new Variant(moneda_id), new Variant(moneda_ctz));
					logger.debug("CrearFactura: " + ok.toString());

					/* Agrego Otros Tributos */
					for (int i = 0; datos.getTributos() != null && i < datos.getTributos().length; i++) {
						@SuppressWarnings("unused")
						Tributo aux = datos.getTributos()[i];
						if ((datos.getTributos()[i]).getTributoId()!=null) { 
							if (!(datos.getTributos()[i]).toString().trim().equals("")) {
								
								Variant tributo_id = new Variant((datos.getTributos()[i]).getTributoId()),
										tributo_desc = new Variant((datos.getTributos()[i]).getTributoDesc()),
										tributo_base_imp = new Variant((datos.getTributos()[i]).getTributoBaseImp().toString()),
										tributo_alic = new Variant((datos.getTributos()[i]).getTributoAlic().toString()),
										tributo_importe = new Variant((datos.getTributos()[i]).getTributoImporte().toString());
								
								Dispatch.call(wsfev1, "AgregarTributo", 
										tributo_id, tributo_desc, tributo_base_imp, 
										tributo_alic, tributo_importe);
							}
						}
					}

					/* Agrego Tasas de IVA */
					for (int i = 0; datos.getIvas() != null && i < datos.getIvas().length; i++) {
						@SuppressWarnings("unused")
						Iva aux = datos.getIvas()[i];
						if ((datos.getIvas()[i]).getIvaId()!=null) {
							if (!(datos.getIvas()[i]).toString().trim().equals("")) {
								
								Variant iva_id = new Variant((datos.getIvas()[i]).getIvaId()), /* 21% */
										iva_base_imp = new Variant((datos.getIvas()[i]).getIvaBaseImp().toString()),
										iva_importe = new Variant((datos.getIvas()[i]).getIvaImporte().toString());
								
								Dispatch.call(wsfev1, "AgregarIva", 
										iva_id, iva_base_imp, iva_importe);
							}
						}
					}

					/* Agrego Comprobantes Asociados */
					for (int i = 0; datos.getComprobantesAsociados() != null && i < datos.getComprobantesAsociados().length; i++) {
						ComprobanteAsociado aux = datos.getComprobantesAsociados()[i];
						if ((datos.getComprobantesAsociados()[i]).getTipoCbte()!=null) { 
							if (!(datos.getComprobantesAsociados()[i]).toString().trim().equals("")) {
								
								Variant tipo_cbte1 = new Variant((datos.getComprobantesAsociados()[i]).getTipoCbte().toString()),
										punto_vta1 = new Variant((datos.getComprobantesAsociados()[i]).getPuntoVta().toString()),
										cbte_nro1 = new Variant((datos.getComprobantesAsociados()[i]).getCbteNro().toString()),
										cuit1 = new Variant((datos.getComprobantesAsociados()[i]).getCuit()),
										fecha_cbte1 = new Variant((datos.getComprobantesAsociados()[i]).getFechaCbte());
									
								Dispatch.call(wsfev1, "AgregarCmpAsoc", 
										tipo_cbte1, punto_vta1, cbte_nro1, cuit1, fecha_cbte1);
							}
						}
					}

					/* Agrego Datos Opcionales */
					for (int i = 0; datos.getDatosOpcionales() != null && i < datos.getDatosOpcionales().length; i++) {
						DatoOpcional aux = datos.getDatosOpcionales()[i];
						if ((datos.getDatosOpcionales()[i]).getOpcionalId()!=null) { 
							if (!(datos.getDatosOpcionales()[i]).toString().trim().equals("")) {
								
								Variant opcional_id1 = new Variant((datos.getDatosOpcionales()[i]).getOpcionalId().toString()), 
										valor1 = new Variant((datos.getDatosOpcionales()[i]).getValor());
								
								Dispatch.call(wsfev1, "AgregarOpcional", 
										opcional_id1, valor1);
							}					
						}
					}

					/* Habilito reprocesamiento automático (predeterminado): */
					Dispatch.put(wsfev1, "Reprocesar", new Variant(true));

					/* Solicito CAE (llamando al webservice de AFIP): */
					Variant cae = Dispatch.call(wsfev1, "CAESolicitar");

					excepcion =  Dispatch.get(wsfev1, "Excepcion").toString();
					logger.debug("Excepcion: " + excepcion);
					resp.setExcepcionWsfev1(excepcion);

					/* Mostrar mensajes XML enviados y recibidos (depuración) */
					String xmlReq = Dispatch.get(wsfev1, "XmlRequest").toString();
					logger.debug("XmlRequest: " + xmlReq);
					resp.setXmlRequest(xmlReq);
					String xmlRes = Dispatch.get(wsfev1, "XmlResponse").toString();
					logger.debug("XmlResponse: " + xmlRes);
					resp.setXmlResponse(xmlRes);

					String errmsg =  Dispatch.get(wsfev1, "ErrMsg").toString();
					logger.debug("ErrMsg: " + errmsg);
					resp.setErrMsg(errmsg);

					String obs =  Dispatch.get(wsfev1, "Obs").toString();
					logger.debug("Obs: " + obs);
					resp.setObservacion(obs);
					resp.setObs(obs);

					/* Datos devueltos */
					logger.debug("CAE: " + cae.toString());
					resp.setCae(cae.toString());

					String resultado = Dispatch.get(wsfev1, "Resultado").toString();
					logger.debug("Resultado: " + resultado);
					resp.setResultado(resultado);

					/* Mostrar Vencimiento */
					Variant fechaVencimiento = Dispatch.get(wsfev1, "Vencimiento");
					logger.debug("Vencimiento: " + resultado);
					resp.setFechaVencimiento(fechaVencimiento.toString());
					
					if (!resultado.equals("A") && !resultado.equals("P")) {
						//Debo enviar un Error cuando no se Autoriza ...
						resp.cargarError(new Response(ErrorEnum.ERROR_ORIGEN_AFIP, obs));
						logger.error("Codigo: " + resp.getCodigo() + " - Descripcion: " + resp.getDescripcion() + " - Observaciones: " + resp.getObservacion() + " - ErrMsg: " + resp.getErrMsg());
					}

				} else { //excepcion wsfe
					resp.cargarError(new Response(ErrorEnum.ERROR_CONEXION_WSFE, excepcion));
					logger.error("Codigo: " + resp.getCodigo() + " - Descripcion: " + resp.getDescripcion() + " - Observaciones: " + resp.getObservacion());
				}
				
				wsfev1.safeRelease();
				
			} else { //excepcion wsaa
				resp.cargarError(new Response(ErrorEnum.ERROR_CONEXION_WSAA, excepcion));
				logger.error("Codigo: " + resp.getCodigo() + " - Descripcion: " + resp.getDescripcion() + " - Observaciones: " + resp.getObservacion());
			}
			
			wsaa.safeRelease();
	
		} catch (ComFailException e) {
			resp.cargarError(new Response(ErrorEnum.ERROR_COMUNICACION_AFIP, e.getCause()!=null?e.getCause().toString():e.toString()));
			logger.error("Codigo: " + resp.getCodigo() + " - Descripcion: " + resp.getDescripcion() + " - Observaciones: " + resp.getObservacion());
		} catch (ApplicationException e) {
			resp.cargarError(new Response(ErrorEnum.ERROR_COMUNICACION_AFIP, e.getCause()!=null?e.getCause().toString():e.toString()));
			logger.error("Codigo: " + resp.getCodigo() + " - Descripcion: " + resp.getDescripcion() + " - Observaciones: " + resp.getObservacion());
		} catch (Exception e) {
			resp.cargarError(new Response(ErrorEnum.ERROR_INESPERADO, e.getCause()!=null?e.getCause().toString():e.toString()));
			logger.error("Codigo: " + resp.getCodigo() + " - Descripcion: " + resp.getDescripcion() + " - Observaciones: " + resp.getObservacion());
		}

		return resp;
	}

}


