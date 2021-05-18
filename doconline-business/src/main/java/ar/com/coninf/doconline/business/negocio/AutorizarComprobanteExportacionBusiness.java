package ar.com.coninf.doconline.business.negocio;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComFailException;
import com.jacob.com.Dispatch;
import com.jacob.com.LibraryLoader;
import com.jacob.com.Variant;

import ar.com.coninf.doconline.rest.model.enums.ErrorEnum;
import ar.com.coninf.doconline.rest.model.request.RequestAutorizarComprobanteExportacion;
import ar.com.coninf.doconline.rest.model.response.Response;
import ar.com.coninf.doconline.rest.model.response.ResponseAutorizarComprobanteExportacion;
import ar.com.coninf.doconline.rest.model.tx.ComprobanteAsociado;
import ar.com.coninf.doconline.rest.model.tx.DatoOpcional;
import ar.com.coninf.doconline.rest.model.tx.Iva;
import ar.com.coninf.doconline.rest.model.tx.PeriodoComprobanteAsociado;
import ar.com.coninf.doconline.rest.model.tx.Tributo;
import ar.com.coninf.doconline.shared.excepcion.ApplicationException;

@Component("business.autorizarComprobanteExportacionBusiness")
public class AutorizarComprobanteExportacionBusiness extends AbstractBusiness {

	Logger logger = Logger.getLogger(this.getClass());

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
			ActiveXComponent wsaa = new ActiveXComponent("WSAA");

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
			logger.debug("Excepcion: " + excepcion);
			resp.setExcepcionWsaa(excepcion);

			if (excepcion.equals("")) {

				String token = Dispatch.get(wsaa, "Token").toString();
				logger.debug("Token: " + token);
				String sign = Dispatch.get(wsaa, "Sign").toString();
				logger.debug("Sign: " + sign);
				/****************************************************************************************************/			

				/* Instanciar WSFEv1: WebService de Factura Electronica version 1 */
				ActiveXComponent wsfexv1 = new ActiveXComponent("WSFEXv1");

				logger.debug("Directorio de Instalacion: " +
						Dispatch.get(wsfexv1, "InstallDir").toString() + 
						" Version: " + 
						Dispatch.get(wsfexv1, "Version").toString()
						);

				/* Establecer parametros de uso: */
				Dispatch.put(wsfexv1, "Cuit", new Variant(datos.getCuit()));
				Dispatch.put(wsfexv1, "Token", new Variant(token));
				Dispatch.put(wsfexv1, "Sign", new Variant(sign));

				/* Conectar al websrvice (cambiar URL para producción) */
				wsdl = urlWsfev1;
				Dispatch.call(wsfexv1, "Conectar", 
						new Variant(cache), 
						new Variant(wsdl),
						new Variant(proxy),
						null,
						null,
						new Variant(timeout));
				
				excepcion =  Dispatch.get(wsfexv1, "Excepcion").toString();
				logger.debug("Excepcion: " + excepcion);
				resp.setExcepcionWsfexv1(excepcion);

				if (excepcion.equals("")) {

					/* PROCESO DE CAE */             
					//WSFEXv1.GetLastCMP(tipo_cbte, punto_vta)

					String tipo_cbte = datos.getTipoCbte().toString();
					String pto_vta = datos.getPtoVta().toString();
					int cbte_nro = Integer.parseInt(datos.getNroCbte().toString());

					String imp_total = datos.getImpTotal().toString();

					String fecha_cbte = datos.getFechaCbte();
					String moneda_id = datos.getMonedaId();
					String moneda_ctz = datos.getMonedaCtz().toString();

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
							new Variant(incoterms_ds),
							new Variant(idioma_cbte));
				    		
		    	    ' lo agrego a la factura (internamente, no se llama al WebService):
		    	    ok = WSFEXv1.AgregarItem(codigo, ds, qty, umed, precio, imp_total, bonif)
		    	    ok = WSFEXv1.AgregarItem(codigo, ds, qty, umed, precio, imp_total, bonif)
		    	    ok = WSFEXv1.AgregarItem(codigo, "Descuento", 0, 99, 0, "-250.00", 0)
		    	    ok = WSFEXv1.AgregarItem("--", "texto adicional", 0, 0, 0, 0, 0)
		    	    
		    	    ' Agrego un permiso (ver manual para el desarrollador)
		    	    If permiso_existente = "S" Then
		    	        id = "99999AAXX999999A"
		    	        dst = 225 ' país destino de la mercaderia
		    	        ok = WSFEXv1.AgregarPermiso(id, dst)
		    	    End If
		    	    
		    	    ' Agrego un comprobante asociado (ver manual para el desarrollador)
		    	    If tipo_cbte <> 19 Then
		    	        tipo_cbte_asoc = 19
		    	        punto_vta_asoc = 2
		    	        cbte_nro_asoc = 1
		    	        cuit_asoc = "20111111111" ' CUIT Asociado Nuevo!
		    	        ok = WSFEXv1.AgregarCmpAsoc(tipo_cbte_asoc, punto_vta_asoc, cbte_nro_asoc, cuit_asoc)
		    	    End If
		    	    
		    	    'id = "99000000000100" ' número propio de transacción
		    	    ' obtengo el último ID y le adiciono 1 (advertencia: evitar overflow!)
		    	    id = CStr(CDec(WSFEXv1.GetLastID()) + CDec(1))
		    	    
		    	    ' Deshabilito errores no capturados:
		    	    WSFEXv1.LanzarExcepciones = False
		    	    
		    	    ' Llamo al WebService de Autorización para obtener el CAE
		    	    CAE = WSFEXv1.Authorize(CDec(id))
		    	    
				    If WSFEXv1.Excepcion <> "" Then
				        MsgBox WSFEXv1.Traceback, vbExclamation, WSFEXv1.Excepcion
				    End If
				    If WSFEXv1.ErrMsg <> "" And WSFEXv1.ErrCode <> "0" Then
				        MsgBox WSFEXv1.ErrMsg, vbExclamation, "Error de AFIP"
				    End If
				        
				    ' Verifico que no haya rechazo o advertencia al generar el CAE
				    If CAE = "" Or WSFEXv1.Resultado <> "A" Then
				        MsgBox "No se asignó CAE (Rechazado). Observación (motivos): " & WSFEXv1.obs, vbInformation + vbOKOnly
				    ElseIf WSFEXv1.obs <> "" And WSFEXv1.obs <> "00" Then
				        MsgBox "Se asignó CAE pero con advertencias. Observación (motivos): " & WSFEXv1.obs, vbInformation + vbOKOnly
				    End If
				    
				    Debug.Print "Numero de comprobante:", WSFEXv1.CbteNro
				    
				    ' Imprimo pedido y respuesta XML para depuración (errores de formato)
				    Debug.Print WSFEXv1.XmlRequest
				    Debug.Print WSFEXv1.XmlResponse
				    Debug.Assert False
				    
				    MsgBox "Resultado:" & WSFEXv1.Resultado & " CAE: " & CAE & " Venc: " & WSFEXv1.Vencimiento & " Reproceso: " & WSFEXv1.Reproceso & " Obs: " & WSFEXv1.obs, vbInformation + vbOKOnly
				    
				    ' Muestro los eventos (mantenimiento programados y otros mensajes de la AFIP)
				    For Each evento In WSFEXv1.Eventos
				        If evento <> "0: " Then
				            MsgBox "Evento: " & evento, vbInformation
				        End If
				    Next
				    
				    ' vuelvo a habilitar el control de errores tradicional
				    WSFEXv1.LanzarExcepciones = True
				    
				    ' Buscar la factura
				    cae2 = WSFEXv1.GetCMP(tipo_cbte, punto_vta, cbte_nro)
				    
				    Debug.Print "Fecha Comprobante:", WSFEXv1.FechaCbte
				    Debug.Print "Fecha Vencimiento CAE", WSFEXv1.Vencimiento
				    Debug.Print "Importe Total:", WSFEXv1.ImpTotal
				    Debug.Print WSFEXv1.XmlResponse
				    
				    If CAE <> cae2 Then
				        MsgBox "El CAE de la factura no concuerdan con el recuperado en la AFIP!"
				    Else
				        MsgBox "El CAE de la factura concuerdan con el recuperado de la AFIP"
				    End If
				    
				    ' analizo la respuesta xml para obtener campos específicos:
				    If WSFEXv1.Version >= "1.06a" Then
				        ok = WSFEXv1.AnalizarXml("XmlResponse")
				        If ok Then
				            Debug.Print "CAE:", WSFEXv1.ObtenerTagXml("Cae"), WSFEXv1.CAE
				            Debug.Print "CbteFch:", WSFEXv1.ObtenerTagXml("Fecha_cbte"), WSFEXv1.FechaCbte
				            Debug.Print "Moneda:", WSFEXv1.ObtenerTagXml("Moneda_Id")
				            Debug.Print "Cotizacion:", WSFEXv1.ObtenerTagXml("Moneda_ctz")
				            Debug.Print "Cuit_pais_cliente:", WSFEXv1.ObtenerTagXml("Cuit_pais_cliente")
				            Debug.Print "Id_impositivo:", WSFEXv1.ObtenerTagXml("Id_impositivo")
				            
				            ' recorro el detalle de items (artículos)
				            For i = 0 To 100
				                ' salgo del bucle si no hay más items (ObtenerTagXml devuelve nulo):
				                If IsNull(WSFEXv1.ObtenerTagXml("Items", "Item", i)) Then Exit For
				                Debug.Print i, "Articulo (codigo):", WSFEXv1.ObtenerTagXml("Items", "Item", i, "Pro_codigo")
				                Debug.Print i, "Articulo (ds):", WSFEXv1.ObtenerTagXml("Items", "Item", i, "Pro_ds")
				                Debug.Print i, "Articulo (qty):", WSFEXv1.ObtenerTagXml("Items", "Item", i, "Pro_qty")
				                Debug.Print i, "Articulo (umed):", WSFEXv1.ObtenerTagXml("Items", "Item", i, "Pro_umed")
				                Debug.Print i, "Articulo (precio):", WSFEXv1.ObtenerTagXml("Items", "Item", i, "Pro_precio_uni")
				                Debug.Print i, "Articulo (bonif):", WSFEXv1.ObtenerTagXml("Items", "Item", i, "Pro_bonificacion")
				                Debug.Print i, "Articulo (subtotal):", WSFEXv1.ObtenerTagXml("Items", "Item", i, "Pro_total_item")
				            Next
				        Else
				            ' hubo error, muestro mensaje
				            Debug.Print WSFEXv1.Excepcion
				        End If
				    End If
				    
				    
				    Exit Sub
				ManejoError:
				    ' Si hubo error:
				    If Not WSFEXv1 Is Nothing Then
				        ' Depuración (grabar a un archivo los detalles del error)
				        fd = FreeFile
				        Open "c:\excepcion.txt" For Append As fd
				        Print #fd, WSFEXv1.Excepcion
				        Print #fd, WSFEXv1.Traceback
				        Print #fd, WSFEXv1.XmlRequest
				        Print #fd, WSFEXv1.XmlResponse
				        Close fd
				        Debug.Print WSFEXv1.Traceback
				        Debug.Print WSFEXv1.XmlRequest
				        Debug.Print WSFEXv1.XmlResponse
				        MsgBox WSFEXv1.Excepcion & vbCrLf & WSFEXv1.ErrMsg, vbCritical + vbOKOnly, "Excepcion WSFEXv1"
				    End If
				    Debug.Print Err.Description            ' descripción error afip
				    Debug.Print Err.Number - vbObjectError ' codigo error afip
				    Select Case MsgBox(Err.Description, vbCritical + vbRetryCancel, "Error:" & Err.Number - vbObjectError & " en " & Err.Source)
				        Case vbRetry
				            Debug.Assert False
				            Resume
				        Case vbCancel
				            Debug.Print Err.Description
				    End Select
				    'Debug.Assert False
						    
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
									
								Dispatch.call(wsfexv1, "AgregarCmpAsoc", 
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
								
								Dispatch.call(wsfexv1, "AgregarOpcional", 
										opcional_id1, valor1);
							}					
						}
					}

					/* Agrego Periodo Comprobantes Opcionales */
					for (int i = 0; datos.getPeriodoComprobanteAsociados() != null && i < datos.getPeriodoComprobanteAsociados().length; i++) {
						PeriodoComprobanteAsociado aux = datos.getPeriodoComprobanteAsociados()[i];
						if ((datos.getPeriodoComprobanteAsociados()[i]).getFechaDesde()!=null
								&& (datos.getPeriodoComprobanteAsociados()[i]).getFechaHasta()!=null) { 
							if (!(datos.getPeriodoComprobanteAsociados()[i]).toString().trim().equals("")) {
								
								Variant fecheDesde = new Variant((datos.getPeriodoComprobanteAsociados()[i]).getFechaDesde()); 
								Variant	fecheHasta = new Variant((datos.getPeriodoComprobanteAsociados()[i]).getFechaHasta());
								
								Dispatch.call(wsfexv1, "AgregarPeriodoComprobantesAsociados", 
										fecheDesde, fecheHasta);
							}					
						}
					}
					
					/* Habilito reprocesamiento automático (predeterminado): */
					Dispatch.put(wsfexv1, "Reprocesar", new Variant(true));

					/* Solicito CAE (llamando al webservice de AFIP): */
					Variant cae = Dispatch.call(wsfexv1, "CAESolicitar");

					excepcion =  Dispatch.get(wsfexv1, "Excepcion").toString();
					logger.debug("Excepcion: " + excepcion);
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

					/* Datos devueltos */
					logger.debug("CAE: " + cae.toString());
					resp.setCae(cae.toString());

					String resultado = Dispatch.get(wsfexv1, "Resultado").toString();
					logger.debug("Resultado: " + resultado);
					resp.setResultado(resultado);

					/* Mostrar Vencimiento */
					Variant fechaVencimiento = Dispatch.get(wsfexv1, "Vencimiento");
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
				
				wsfexv1.safeRelease();
				
			} else { //excepcion wsaa
				resp.cargarError(new Response(ErrorEnum.ERROR_CONEXION_WSAA, excepcion));
				logger.error("Codigo: " + resp.getCodigo() + " - Descripcion: " + resp.getDescripcion() + " - Observaciones: " + resp.getObservacion());
			}
			
			wsaa.safeRelease();
	
		} catch (ComFailException e) {
			resp.cargarError(new Response(ErrorEnum.ERROR_COMUNICACION_AFIP, e.getCause()!=null?e.getCause().getMessage():e.getMessage()));
			logger.error("Codigo: " + resp.getCodigo() + " - Descripcion: " + resp.getDescripcion() + " - Observaciones: " + resp.getObservacion());
		} catch (ApplicationException e) {
			resp.cargarError(new Response(e.getError(), e.getMessage()));
			logger.error("Codigo: " + resp.getCodigo() + " - Descripcion: " + resp.getDescripcion() + " - Observaciones: " + resp.getObservacion());
		} catch (Exception e) {
			resp.cargarError(new Response(ErrorEnum.ERROR_INESPERADO, e.getCause()!=null?e.getCause().getMessage():e.getMessage()));
			logger.error("Codigo: " + resp.getCodigo() + " - Descripcion: " + resp.getDescripcion() + " - Observaciones: " + resp.getObservacion());
		}

		return resp;
	}

}


