package ar.com.coninf.doconline.business.negocio;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComFailException;
import com.jacob.com.Dispatch;
import com.jacob.com.LibraryLoader;
import com.jacob.com.SafeArray;
import com.jacob.com.Variant;

import ar.com.coninf.doconline.rest.model.enums.ErrorEnum;
import ar.com.coninf.doconline.rest.model.request.RequestAutorizarComprobanteExportacion;
import ar.com.coninf.doconline.rest.model.response.Response;
import ar.com.coninf.doconline.rest.model.response.ResponseAutorizarComprobanteExportacion;
import ar.com.coninf.doconline.rest.model.tx.ComprobanteAsociado;
import ar.com.coninf.doconline.rest.model.tx.Item;
import ar.com.coninf.doconline.rest.model.tx.Permiso;
import ar.com.coninf.doconline.shared.excepcion.ApplicationException;

@Component("business.autorizarComprobanteExportacionBusiness")
public class AutorizarComprobanteExportacionBusiness extends AbstractBusiness {

	Logger logger = Logger.getLogger(this.getClass());
	
	ActiveXComponent wsaa;
	ActiveXComponent wsfexv1;
	
	public synchronized ResponseAutorizarComprobanteExportacion autorizarComprobanteExportacion(RequestAutorizarComprobanteExportacion datos) {

		logger.debug("Ejecucion autorizarComprobante() de WS");
		
		ResponseAutorizarComprobanteExportacion resp = new ResponseAutorizarComprobanteExportacion();
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
			float timeout = Float.parseFloat(paramTimeout);			
			String userDir = paramUserDir;
	
			String libFile = System.getProperty("os.arch").equals("amd64") ? "jacob-1.18-M2-x64.dll" : "jacob-1.18-M2-x86.dll";

			System.setProperty(LibraryLoader.JACOB_DLL_PATH, userDir+libFile);
			logger.debug(userDir+libFile);

			LibraryLoader.loadJacobLibrary();

			/* Crear objeto WSAA: Web Service de Autenticacion y Autorizacion */
			wsaa = new ActiveXComponent("WSAA");

			logger.debug("Directorio de Instalacion: " +
					Dispatch.get(wsaa, "InstallDir").toString() + 
					" Version: " +
					Dispatch.get(wsaa, "Version").toString()
					);

			/****************************************************************************************************/			
			wsdl = urlWsaa;
			Dispatch.call(wsaa, "Autenticar", 
					new Variant("wsfex"), 
					new Variant(userDir + archivoCrt), 
					new Variant(userDir + archivoKey), 
					new Variant(wsdl),
					new Variant(proxy));
					
			String excepcion =  Dispatch.get(wsaa, "Excepcion").toString();
			logger.debug("Excepcion WSAA: " + excepcion);
			resp.setExcepcionWsaa(excepcion);

			if (excepcion.equals("")) {

				String token = Dispatch.get(wsaa, "Token").toString();
				logger.debug("Token: " + token);
				String sign = Dispatch.get(wsaa, "Sign").toString();
				logger.debug("Sign: " + sign);
				/****************************************************************************************************/			

				/* Instanciar WSFEv1: WebService de Factura Electronica version 1 */
				wsfexv1 = new ActiveXComponent("WSFEXv1");

				logger.debug("Directorio de Instalacion: " +
						Dispatch.get(wsfexv1, "InstallDir").toString() + 
						" Version: " + 
						Dispatch.get(wsfexv1, "Version").toString()
						);

				/* Establecer parametros de uso: */
				Dispatch.put(wsfexv1, "Cuit", new Variant(datos.getCuit()));
				Dispatch.put(wsfexv1, "Token", new Variant(token));
				Dispatch.put(wsfexv1, "Sign", new Variant(sign));

//********************************************************
				/* Conectar al websrvice (cambiar URL para producción) */
				wsdl = urlWsfexv1;
				Dispatch.call(wsfexv1, "Conectar", 
						new Variant(cache), 
						new Variant(wsdl),
						new Variant(proxy),
						null,
						null,
						new Variant(timeout));
				
				excepcion =  Dispatch.get(wsfexv1, "Excepcion").toString();
				logger.debug("Excepcion WSFEX: " + excepcion);
				resp.setExcepcionWsfexv1(excepcion);

				if (excepcion.equals("")) {

					/* PROCESO DE CAE */             
					//WSFEXv1.GetLastCMP(tipo_cbte, punto_vta)

					String tipo_cbte = datos.getTipoCbte().toString();
					String pto_vta = datos.getPtoVta().toString();
					long cbte_nro = datos.getNroCbte();

					String imp_total = datos.getImpTotal().toString();

					String fecha_cbte = datos.getFechaCbte();
					String moneda_id = datos.getMonedaId();
					String moneda_ctz = datos.getMonedaCtz().toString();

					int tipo_expo = datos.getTipoExpo();
					
					String permiso_existente = datos.getPermisoExistente();
					int dst_cmp = datos.getDstCmp();
					
					String cliente = datos.getCliente();
					String cuit_pais_cliente = datos.getCuitPaisCliente(); 
					String domicilio_cliente = datos.getDomicilioCliente(); 
					
					String id_impositivo = datos.getIdImpositivo(); 
					
					String obs_comerciales = datos.getObsComerciales();
					String obs_general = datos.getObsGenerales();
					
					String forma_pago = datos.getFormaPago();
					String fecha_pago = "";
					if (tipo_expo==2 || tipo_expo==4) {
						fecha_pago = datos.getFechaVencPago();
					}
					
					String incoterms = datos.getIncoterms();
					String incoterms_ds = datos.getIncotermsDs();
					
					String idioma_cbte = datos.getIdiomaCbte();
					
					Variant ok = Dispatch.call(wsfexv1, "CrearFactura",
							new Variant(tipo_cbte), 
							new Variant(pto_vta), 
							new Variant(cbte_nro), 
							new Variant(fecha_cbte), 
							new Variant(imp_total), 
							new Variant(tipo_expo), 
							new Variant(permiso_existente), 
							new Variant(dst_cmp), 
							new Variant(cliente), 
							new Variant(cuit_pais_cliente), 
							new Variant(domicilio_cliente), 
							new Variant(id_impositivo),
							new Variant(moneda_id), 
							new Variant(moneda_ctz), 
							new Variant(obs_comerciales),
							new Variant(obs_general),
							new Variant(forma_pago),
							new Variant(incoterms),
							new Variant(idioma_cbte),
							new Variant(incoterms_ds),
							new Variant(fecha_pago));
					logger.debug("OK: " + ok.toString());
					
					/* Agrego Items de Factura */
					for (int i = 0; datos.getItems()!=null && i<datos.getItems().length; i++) {
						Item aux = datos.getItems()[i];
						if ((datos.getItems()[i]).getCodigo()!=null 
								&& !(datos.getItems()[i]).toString().trim().equals("")) {
							
							Variant codigo = new Variant((datos.getItems()[i]).getCodigo());
							Variant	ds = new Variant((datos.getItems()[i]).getDs());
							Variant	qty = new Variant((datos.getItems()[i]).getQty().toString());
							Variant	umed = new Variant((datos.getItems()[i]).getUmed().toString());
							Variant	precio = new Variant((datos.getItems()[i]).getPrecio().toString());
							Variant	imp_total1 = new Variant((datos.getItems()[i]).getImpTotal().toString());
							Variant	bonif = new Variant((datos.getItems()[i]).getBonif().toString());
								
							Dispatch.call(wsfexv1, "AgregarItem", 
									codigo, ds, qty, umed, precio, imp_total1, bonif);
						}
					}
		    	    
					/* Agrego Permisos de Factura */
					if (permiso_existente.equals("S")) {
						for (int i = 0; datos.getPermisos()!=null && i<datos.getPermisos().length; i++) {
							Permiso aux = datos.getPermisos()[i];
							if ((datos.getPermisos()[i]).getIdPermiso()!=null) { 
								if (!(datos.getPermisos()[i]).toString().trim().equals("")) {
									
									Variant id_permiso = new Variant((datos.getPermisos()[i]).getIdPermiso());
									Variant dst_merc = new Variant((datos.getPermisos()[i]).getDstMerc());
										
									Dispatch.call(wsfexv1, "AgregarPermiso", 
											id_permiso, dst_merc);
								}
							}
						}
					}
		    	    
					/* Agrego Comprobantes Asociados */
					for (int i = 0; datos.getComprobantesAsociados() != null && i < datos.getComprobantesAsociados().length; i++) {
						ComprobanteAsociado aux = datos.getComprobantesAsociados()[i];
						if ((datos.getComprobantesAsociados()[i]).getTipoCbte()!=null) { 
							if (!(datos.getComprobantesAsociados()[i]).toString().trim().equals("")) {
								
								Variant tipo_cbte1 = new Variant((datos.getComprobantesAsociados()[i]).getTipoCbte().toString());
								Variant punto_vta1 = new Variant((datos.getComprobantesAsociados()[i]).getPuntoVta().toString());
								Variant cbte_nro1 = new Variant((datos.getComprobantesAsociados()[i]).getCbteNro().toString());
								Variant cuit1 = new Variant((datos.getComprobantesAsociados()[i]).getCuit());
										
								Dispatch.call(wsfexv1, "AgregarCmpAsoc", 
										tipo_cbte1, punto_vta1, cbte_nro1, cuit1);
							}
						}
					}
	    	    
					/* Obtengo el último ID y le adiciono 1 (advertencia: evitar overflow!): */
					Variant idTransaccion = Dispatch.call(wsfexv1, "GetLastID");
					logger.debug("ID TRANSACCION OBTENIDO: " + idTransaccion.toString());
					Variant id_transaccion = new Variant(datos.getControlTransaccion().getNroTransaccion().toString());
					logger.debug("ID TRANSACCION NUEVO: " + id_transaccion);

					/* Llamo al WebService de Autorización para obtener el CAE: */
					Variant cae = Dispatch.call(wsfexv1, "Authorize", 
							id_transaccion);

					/* Datos devueltos */
					logger.debug("CAE: " + cae);
					resp.setCae(cae.toString());

					String resultado = Dispatch.get(wsfexv1, "Resultado").toString();
					logger.debug("Resultado: " + resultado);
					resp.setResultado(resultado);

					String fechaVencimiento = Dispatch.get(wsfexv1, "Vencimiento").toString();
					logger.debug("Vencimiento: " + fechaVencimiento);
					resp.setFechaVencimiento(fechaVencimiento);
					
					excepcion =  Dispatch.get(wsfexv1, "Excepcion").toString();
					logger.debug("Excepcion WSFEX: " + excepcion);
					resp.setExcepcionWsfexv1(excepcion);

					/* Mostrar mensajes XML enviados y recibidos (depuración) */
					String xmlReq = Dispatch.get(wsfexv1, "XmlRequest").toString();
					logger.debug("XmlRequest: " + xmlReq);
					resp.setXmlRequest(xmlReq);
					String xmlRes = Dispatch.get(wsfexv1, "XmlResponse").toString();
					logger.debug("XmlResponse: " + xmlRes);
					resp.setXmlResponse(xmlRes);
					
					String errmsg =  Dispatch.get(wsfexv1, "ErrMsg").toString();
					logger.debug("ErrMsg: " + errmsg);
					resp.setErrMsg(errmsg);

					String obs =  Dispatch.get(wsfexv1, "Obs").toString();
					logger.debug("Obs: " + obs);
					resp.setObservacion(obs);
					resp.setObs(obs);
					
					Variant aux = Dispatch.get(wsfexv1, "Eventos");
					SafeArray eventos = aux.toSafeArray();
					String [] aux1 = eventos.toStringArray();
					int aux3 = 0;
					for (String aux2 : aux1) {
						logger.debug("Evento[" + aux3 + "]: " + aux2);
					}
					
					if (!resultado.equals("A")) {

						//WSFEXV1 no tiene reprocesar 
						String cae1 = Dispatch.call(wsfexv1, "GetCMP", 
								tipo_cbte, pto_vta, cbte_nro).toString();
						
						xmlReq = Dispatch.get(wsfexv1, "XmlRequest").toString();
						xmlRes = Dispatch.get(wsfexv1, "XmlResponse").toString();

						String fecha_vencimiento1 = Dispatch.get(wsfexv1, "Vencimiento").toString();
						String fecha_cbte1 = Dispatch.get(wsfexv1, "FechaCbte").toString();
						String resultado1 = Dispatch.get(wsfexv1, "Resultado").toString();
						String imp_total1 = Dispatch.get(wsfexv1, "ImpTotal").toString();
						String excepcion1 =  Dispatch.get(wsfexv1, "Excepcion").toString();
						String errmsg1 =  Dispatch.get(wsfexv1, "ErrMsg").toString();
						int tipo_expo1 = 0;
						int dst_cmp1 = 0;		
						String cliente1 = "";			
						String cuit_pais_cliente1 = "";	
						String domicilio_cliente1 = "";	
						String forma_pago1 = "";			
						String idioma_cbte1 = "";			
						String fecha_pago1 = "";			
						String motivos_obs = "";			

						ok = Dispatch.call(wsfexv1, "AnalizarXml");
						if (ok.getBoolean()) {
							motivos_obs = Dispatch.call(wsfexv1, "ObtenerTagXml", "Motivos_Obs").toString();
							tipo_expo1 = Integer.valueOf(Dispatch.call(wsfexv1, "ObtenerTagXml", "Tipo_expo").toString()).intValue();
							dst_cmp1 = Integer.valueOf(Dispatch.call(wsfexv1, "ObtenerTagXml", "Dst_cmp").toString()).intValue();
							cliente1 = Dispatch.call(wsfexv1, "ObtenerTagXml", "Cliente").toString();
							cuit_pais_cliente1 = Dispatch.call(wsfexv1, "ObtenerTagXml", "Cuit_pais_cliente").toString();
							domicilio_cliente1 = Dispatch.call(wsfexv1, "ObtenerTagXml", "Domicilio_cliente").toString();
							forma_pago1 = Dispatch.call(wsfexv1, "ObtenerTagXml", "Forma_pago").toString();
							idioma_cbte1 = Dispatch.call(wsfexv1, "ObtenerTagXml", "Idioma_cbte").toString();
							fecha_pago1 = Dispatch.call(wsfexv1, "ObtenerTagXml", "Fecha_pago").toString();
						}
						
						logger.debug("Reproceso xmlReq:\r\n" + xmlReq);
						logger.debug("Reproceso xmlRes:\r\n" + xmlRes);
						logger.debug("Reproceso CbteTipo         : " + tipo_cbte);
						logger.debug("Reproceso Punto Venta      : " + pto_vta);
						logger.debug("Reproceso CbteNro          : " + cbte_nro);
						logger.debug("Reproceso Fecha Cbte       : " + fecha_cbte1);
						logger.debug("Reproceso ImpTotal         : " + imp_total1);
						logger.debug("Reproceso Tipo_expo        : " + tipo_expo1);
						logger.debug("Reproceso Dst_cmp          : " + dst_cmp1);
						logger.debug("Reproceso Cliente          : " + cliente1);	
						logger.debug("Reproceso Cuit_pais_cliente: " + cuit_pais_cliente1);
						logger.debug("Reproceso Domicilio_cliente: " + domicilio_cliente1);
						logger.debug("Reproceso Forma_pago       : " + forma_pago1);	
						logger.debug("Reproceso Fecha_pago       : " + fecha_pago1);	
						logger.debug("Reproceso Idioma_cbte      : " + idioma_cbte1);	
						logger.debug("Reproceso Resultado        : " + resultado1);
						logger.debug("Reproceso CAE              : " + cae1);
						logger.debug("Reproceso Vencimiento      : " + fecha_vencimiento1);
						logger.debug("Reproceso Obs              : " + motivos_obs);
						logger.debug("Reproceso Excepcion        : " + excepcion1);
						logger.debug("Reproceso ErrMsg           : " + errmsg1);
						
						aux = Dispatch.get(wsfexv1, "Eventos");
						eventos = aux.toSafeArray();
						aux1 = eventos.toStringArray();
						aux3 = 0;
						for (String aux2 : aux1) {
							logger.debug("Reproceso Evento[" + aux3 + "]: " + aux2);
						}
						
						if (resultado1.equals("A") &&
								fecha_cbte.equals(fecha_cbte1) && imp_total.equals(imp_total1) && 
								tipo_expo==tipo_expo1 && dst_cmp==dst_cmp1 && idioma_cbte.equals(idioma_cbte1) &&
								cliente.equals(cliente1) && cuit_pais_cliente.equals(cuit_pais_cliente1) && domicilio_cliente.equals(domicilio_cliente1) && forma_pago.equals(forma_pago1) &&  
								fecha_pago.equals(fecha_pago1)) {
							resp.setResultado(resultado1);
							resp.setCae(cae1);
							if (fecha_vencimiento1.length()==10)
								resp.setFechaVencimiento(fecha_vencimiento1.substring(6).concat(fecha_vencimiento1.substring(3,5)).concat(fecha_vencimiento1.substring(0,2)));
							else
								resp.setFechaVencimiento(fecha_vencimiento1);
							resp.setObs(motivos_obs);
							resp.setObservacion(motivos_obs);
							resp.setExcepcionWsfexv1(excepcion1);
							resp.setErrMsg(errmsg1);						
						} else {
							//Debo enviar un Error cuando no se Autoriza ...
							resp.cargarError(new Response(ErrorEnum.ERROR_ORIGEN_AFIP, obs));
							logger.error("Codigo: " + resp.getCodigo() + " - Descripcion: " + resp.getDescripcion() + " - Observaciones: " + resp.getObservacion() + " - ErrMsg: " + resp.getErrMsg());
						}
					}
					
				} else { //excepcion wsfe
					resp.cargarError(new Response(ErrorEnum.ERROR_CONEXION_WSFE, excepcion));
					logger.error("Codigo: " + resp.getCodigo() + " - Descripcion: " + resp.getDescripcion() + " - Observaciones: " + resp.getObservacion());
				}
				
			} else { //excepcion wsaa
				resp.cargarError(new Response(ErrorEnum.ERROR_CONEXION_WSAA, excepcion));
				logger.error("Codigo: " + resp.getCodigo() + " - Descripcion: " + resp.getDescripcion() + " - Observaciones: " + resp.getObservacion());
			}
			
		} catch (ComFailException e) {
			resp.cargarError(new Response(ErrorEnum.ERROR_COMUNICACION_AFIP, e.getCause()!=null?e.getCause().getMessage():e.getMessage()));
			logger.error("Codigo: " + resp.getCodigo() + " - Descripcion: " + resp.getDescripcion() + " - Observaciones: " + resp.getObservacion());
		} catch (ApplicationException e) {
			resp.cargarError(new Response(e.getError(), e.getMessage()));
			logger.error("Codigo: " + resp.getCodigo() + " - Descripcion: " + resp.getDescripcion() + " - Observaciones: " + resp.getObservacion());
		} catch (Exception e) {
			resp.cargarError(new Response(ErrorEnum.ERROR_INESPERADO, e.getCause()!=null?e.getCause().getMessage():e.getMessage()));
			logger.error("Codigo: " + resp.getCodigo() + " - Descripcion: " + resp.getDescripcion() + " - Observaciones: " + resp.getObservacion());
		} finally {
			if (wsaa!=null)
				wsaa.safeRelease();
			if (wsfexv1!=null)
				wsfexv1.safeRelease();			
		}

		return resp;
	}

}


