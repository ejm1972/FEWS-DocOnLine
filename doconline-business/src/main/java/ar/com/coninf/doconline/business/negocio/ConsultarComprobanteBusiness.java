package ar.com.coninf.doconline.business.negocio;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import ar.com.coninf.doconline.rest.model.enums.ErrorEnum;
import ar.com.coninf.doconline.rest.model.request.RequestConsultarComprobante;
import ar.com.coninf.doconline.rest.model.response.Response;
import ar.com.coninf.doconline.rest.model.response.ResponseConsultarComprobante;
import ar.com.coninf.doconline.shared.excepcion.ApplicationException;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComFailException;
import com.jacob.com.Dispatch;
import com.jacob.com.LibraryLoader;
import com.jacob.com.Variant;

@Component("business.consultarComprobanteBusiness")
public class ConsultarComprobanteBusiness extends AbstractBusiness {

	Logger logger = Logger.getLogger(this.getClass());

	public synchronized ResponseConsultarComprobante consultarComprobante(RequestConsultarComprobante datos) {
		
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

		ResponseConsultarComprobante resp = new ResponseConsultarComprobante();
		resp.esReintento = false;
		resp.cargarError(new Response(ErrorEnum.SIN_ERROR));
		
		try {
			String libFile = System.getProperty("os.arch").equals("amd64") ? "jacob-1.18-M2-x64.dll" : "jacob-1.18-M2-x86.dll";
			
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

				/* Instanciar WSFEv1: WebService de Factura Electronica version 1 */
				ActiveXComponent wsfev1 = new ActiveXComponent("WSFEv1");

				/* Establecer parametros de uso: */
				Dispatch.put(wsfev1, "Cuit", new Variant(datos.getCuit()));
				Dispatch.put(wsfev1, "Token", new Variant(token));
				Dispatch.put(wsfev1, "Sign", new Variant(sign));

				/* Conectar al websrvice (cambiar URL para producción) */
				wsdl = urlWsfev1;
				Dispatch.call(wsfev1, "Conectar", 
						new Variant(cache), 
						new Variant(wsdl),
						new Variant(proxy));

				//Ver Excepcion
				excepcion =  Dispatch.get(wsfev1, "Excepcion").toString();
				logger.info("Excepcion: " + excepcion);
				resp.setExcepcionWsfev1(excepcion);

				if (excepcion.equals("")) {

					/* Consultar Comprobante autorizado en AFIP */
					String tipo_cbte = datos.getTipoCbte().toString();
					String pto_vta = datos.getPtoVta().toString();
					String cbte_nro = datos.getCbte().toString();
					Variant cae = Dispatch.call(wsfev1, "CompConsultar", 
							new Variant(tipo_cbte), 
							new Variant(pto_vta),
							new Variant(cbte_nro));
					
					String fechaCbte = Dispatch.get(wsfev1, "FechaCbte").toString();
					String vencimiento =  Dispatch.get(wsfev1, "Vencimiento").toString();
					String impTotal =  Dispatch.get(wsfev1, "ImpTotal").toString();
					
					logger.info("CAE: " + cae.toString());
					logger.info("Venc: " + vencimiento);
					logger.info("Fecha Cbte: " + fechaCbte);
					logger.info("Imp Total: " + impTotal);
					
					resp.setCae(cae.toString());
					resp.setFechaVencimiento(vencimiento);
					resp.setFechaCbte(fechaCbte);
					resp.setImpTotal(impTotal);
					
					//Ver Excepcion
					excepcion =  Dispatch.get(wsfev1, "Excepcion").toString();
					logger.info("Excepcion: " + excepcion);
					resp.setExcepcionWsfev1(excepcion);
					
					/* Mostrar mensajes XML enviados y recibidos (depuración) */
					String xmlReq = Dispatch.get(wsfev1, "XmlRequest").toString();
					logger.info("XmlRequest: " + xmlReq);
					resp.setXmlRequest(xmlReq);
					String xmlRes = Dispatch.get(wsfev1, "XmlResponse").toString();
					logger.info("XmlResponse: " + xmlRes);
					resp.setXmlResponse(xmlRes);

					String errmsg =  Dispatch.get(wsfev1, "ErrMsg").toString();
					logger.info("ErrMsg: " + errmsg);
					resp.setErrMsg(errmsg);

					String obs =  Dispatch.get(wsfev1, "Obs").toString();
					logger.info("Obs: " + obs);
					resp.setObservacion(obs);

				} else { //excepcion wsfe
					resp.cargarError(new Response(ErrorEnum.ERROR_CONEXION_WSFE));
				}
				
				wsfev1.safeRelease();
				
			} else { //excepcion wsaa
				resp.cargarError(new Response(ErrorEnum.ERROR_CONEXION_WSAA));
			}
			
			wsaa.safeRelease();

		} catch (ComFailException e){
			throw new ApplicationException(ErrorEnum.ERROR_COMUNICACION_AFIP, e.getCause()!=null?e.getCause().toString():e.toString());
		} catch (ApplicationException e){
			throw e;
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
		
		return resp;
	}

}