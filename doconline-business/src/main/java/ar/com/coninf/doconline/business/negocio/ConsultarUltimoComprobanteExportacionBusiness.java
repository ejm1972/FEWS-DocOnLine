package ar.com.coninf.doconline.business.negocio;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComFailException;
import com.jacob.com.Dispatch;
import com.jacob.com.LibraryLoader;
import com.jacob.com.Variant;

import ar.com.coninf.doconline.rest.model.enums.ErrorEnum;
import ar.com.coninf.doconline.rest.model.request.RequestConsultarUltimoComprobanteExportacion;
import ar.com.coninf.doconline.rest.model.response.Response;
import ar.com.coninf.doconline.rest.model.response.ResponseConsultarUltimoComprobanteExportacion;
import ar.com.coninf.doconline.shared.excepcion.ApplicationException;

@Component("business.consultarUltimoComprobanteExportacionBusiness")
public class ConsultarUltimoComprobanteExportacionBusiness extends AbstractBusiness {

	Logger logger = Logger.getLogger(this.getClass());

	public synchronized ResponseConsultarUltimoComprobanteExportacion consultarUltimoComprobanteExportacion(RequestConsultarUltimoComprobanteExportacion datos) {
		
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
		String userDir = System.getProperty("user.dir");
		userDir = paramUserDir;

		ResponseConsultarUltimoComprobanteExportacion resp = new ResponseConsultarUltimoComprobanteExportacion();
		resp.setEsReintento(false);
		resp.cargarError(new Response(ErrorEnum.SIN_ERROR));
		
		try {
			String libFile = System.getProperty("os.arch").equals("amd64") ? "jacob-1.18-M2-x64.dll" : "jacob-1.18-M2-x86.dll";
			
			System.setProperty("com.jacob.debug", "false");
			System.setProperty(LibraryLoader.JACOB_DLL_PATH, userDir+libFile);
			logger.info(userDir+libFile);
            
			LibraryLoader.loadJacobLibrary();

			/* Crear objeto WSAA: Web Service de Autenticacion y Autorizacion */
			ActiveXComponent wsaa = new ActiveXComponent("WSAA");

			logger.info(Dispatch.get(wsaa, "InstallDir").toString() + 
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
			logger.info("Excepcion: " + excepcion);
			resp.setExcepcionWsaa(excepcion);

			if (excepcion.equals("")) {

				String token = Dispatch.get(wsaa, "Token").toString();
				logger.info("Token: " + token);
				String sign = Dispatch.get(wsaa, "Sign").toString();
				logger.info("Sign: " + sign);
				/****************************************************************************************************/			

				/* Instanciar WSFEXv1: WebService de Factura Electronica version 1 */
				ActiveXComponent wsfexv1 = new ActiveXComponent("WSFEXv1");

				/* Establecer parametros de uso: */
				Dispatch.put(wsfexv1, "Cuit", new Variant(datos.getCuit()));
				Dispatch.put(wsfexv1, "Token", new Variant(token));
				Dispatch.put(wsfexv1, "Sign", new Variant(sign));

				/* Conectar al websrvice (cambiar URL para producción) */
				wsdl = urlWsfexv1;
				Dispatch.call(wsfexv1, "Conectar", 
						new Variant(cache), 
						new Variant(wsdl),
						new Variant(proxy));

				//Ver Excepcion
				excepcion =  Dispatch.get(wsfexv1, "Excepcion").toString();
				logger.info("Excepcion: " + excepcion);
				resp.setExcepcionWsfexv1(excepcion);

				if (excepcion.equals("")) {

					/* Consultar Ultimo comprobante autorizado en AFIP */
					String tipo_cbte = datos.getTipoCbte().toString();
					String pto_vta = datos.getPtoVta().toString();
					Variant ult = Dispatch.call(wsfexv1, "GetLastCMP", 
							new Variant(tipo_cbte), 
							new Variant(pto_vta));
					
					logger.info("Ultimo comprobante: " + ult.toString());
					resp.setUltimoComprobante(ult.toString());

					//Ver Excepcion
					excepcion =  Dispatch.get(wsfexv1, "Excepcion").toString();
					logger.info("Excepcion: " + excepcion);
					resp.setExcepcionWsfexv1(excepcion);
					
					/* Mostrar mensajes XML enviados y recibidos (depuración) */
					String xmlReq = Dispatch.get(wsfexv1, "XmlRequest").toString();
					logger.info("XmlRequest: " + xmlReq);
					resp.setXmlRequest(xmlReq);
					String xmlRes = Dispatch.get(wsfexv1, "XmlResponse").toString();
					logger.info("XmlResponse: " + xmlRes);
					resp.setXmlResponse(xmlRes);

					String errmsg =  Dispatch.get(wsfexv1, "ErrMsg").toString();
					logger.info("ErrMsg: " + errmsg);
					resp.setErrMsg(errmsg);

					String obs =  Dispatch.get(wsfexv1, "Obs").toString();
					logger.info("Obs: " + obs);
					resp.setObservacion(obs);

				} else { //excepcion wsfe
					resp.cargarError(new Response(ErrorEnum.ERROR_CONEXION_WSFE));
				}
				
				wsfexv1.safeRelease();
				
			} else { //excepcion wsaa
				resp.cargarError(new Response(ErrorEnum.ERROR_CONEXION_WSAA));
			}
			
			wsaa.safeRelease();

		} catch (ComFailException e){
			throw new ApplicationException(ErrorEnum.ERROR_COMUNICACION_AFIP, e.getCause()!=null?e.getCause().toString():e.toString());
		} catch (ApplicationException e) {
			throw e;
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
		
		return resp;
	}

}