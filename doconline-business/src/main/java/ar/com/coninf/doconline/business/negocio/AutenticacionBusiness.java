package ar.com.coninf.doconline.business.negocio;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ar.com.coninf.doconline.business.sesion.ControlSesion;
import ar.com.coninf.doconline.business.util.EncriptacionUtiles;
import ar.com.coninf.doconline.model.dao.InterfazDao;
import ar.com.coninf.doconline.model.dao.ParametroDao;
import ar.com.coninf.doconline.rest.model.enums.ErrorEnum;
import ar.com.coninf.doconline.shared.constante.MonederoParametros;
import ar.com.coninf.doconline.shared.dto.InterfazDto;
import ar.com.coninf.doconline.shared.excepcion.ApplicationException;

@Component("business.autenticacionBusiness")
public class AutenticacionBusiness {
	
	private Map<Integer, ControlSesion> tabla = new Hashtable<Integer, ControlSesion>();
	private Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	@Qualifier("interfazDao")
	private InterfazDao interfazDao;
	
	@Autowired
	@Qualifier("parametroDao")
	private ParametroDao parametroDao;

	public String iniciarSesion(Integer interfazId, String clave) {
		ControlSesion control = new ControlSesion();
		try {
			InterfazDto interfaz = interfazDao.getById(new Long(interfazId));
			if(interfaz == null){
				throw new ApplicationException(ErrorEnum.ERROR_INTERFAZ_INVALIDA, "error.ws-interfaz_invalida", null);
			}
			byte[] hash = EncriptacionUtiles.computeHash(clave);
			String claveEncrip = EncriptacionUtiles.byteArrayToHexString(hash);
			if(!interfaz.getClave().equals(claveEncrip)){
				throw new ApplicationException(ErrorEnum.ERROR_AUTENTICACION_CLAVE, "error.ws-autenticacion-clave-incorrecta", null);
			}
			
			if(tabla.containsKey(interfazId) && !tabla.get(interfazId).sesionExpirada()){
				log.debug("La interfaz contiene una sesion activa.");
				return tabla.get(interfazId).getIdSesion();
			}

			if (!interfaz.getActivo().equals("S")) {
				throw new ApplicationException(ErrorEnum.ERROR_INTERFAZ_BLOQUEADA, "error.ws-interfaz_bloqueada", new Object[]{interfazId});
			}

			interfaz.setCantidadOperaciones(0);
			interfaz.setMontoOperaciones(0.0);
			interfazDao.update(interfaz);
				
			String vigSesion = parametroDao.getValorVigente(MonederoParametros.MIN_VIG_SESION);
			log.info("Parametro " + MonederoParametros.MIN_VIG_SESION + ": (" + vigSesion + ")");
			int vigSesMin = Integer.parseInt(vigSesion);
			
			Calendar ahora = Calendar.getInstance();
			control.setFechaHoraIngreso(ahora.getTime());
			ahora.add(Calendar.MINUTE, vigSesMin);
			control.setExpira(ahora.getTimeInMillis());
			control.setIdSesion(UUID.randomUUID().toString().replaceAll("-", ""));
			
			tabla.put(interfazId, control);
		
		} catch (ApplicationException e1){
			throw e1;
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
		return control.getIdSesion();
	}

	public void cerrarSesion(Integer interfazId, String idSesion) {
		try {
			InterfazDto interfaz = interfazDao.getById(new Long(interfazId));
			if(interfaz == null){
				throw new ApplicationException(ErrorEnum.ERROR_INTERFAZ_INVALIDA, "error.ws-interfaz_invalida", null);
			}
			
			ControlSesion ctrlSesion = tabla.get(interfazId);
			if(ctrlSesion == null){
				throw new ApplicationException(ErrorEnum.ERROR_SESION, "error.ws-autenticacion-sesion-ausente", new Object[]{interfazId});
			}
			
			if(!ctrlSesion.getIdSesion().equals(idSesion)){
				throw new ApplicationException(ErrorEnum.ERROR_SESION, "error.ws-autenticacion-sesion-invalida", null);
			}
			
			if(!ctrlSesion.sesionExpirada()){
				log.info("La interfaz contiene una sesion activa.");
			}
			
			ControlSesion remove = tabla.remove(interfazId);
			if(remove == null){
				log.info("No existía una sesión para la interfaz [" + interfazId + "]");
			}else{
				log.info("Sesión cerrada para la interfaz [" + interfazId + "]");
			}
			
		} catch (ApplicationException e1){
			throw e1;
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
	}

	public Boolean validarSesion(Integer interfazId, String idSesion) {
		try {
			InterfazDto interfaz = interfazDao.getById(new Long(interfazId));
			if(interfaz == null){
				throw new ApplicationException(ErrorEnum.ERROR_INTERFAZ_INVALIDA, "error.ws-interfaz_invalida", null);
			}
			
			ControlSesion ctrlSesion = tabla.get(interfazId);
			if(ctrlSesion == null){
				throw new ApplicationException(ErrorEnum.ERROR_SESION, "error.ws-autenticacion-sesion-ausente", new Object[]{interfazId});
			}
			
			if(!ctrlSesion.getIdSesion().equals(idSesion)){
				throw new ApplicationException(ErrorEnum.ERROR_SESION, "error.ws-autenticacion-sesion-invalida", null);
			}
			
			if(ctrlSesion.sesionExpirada()){
				throw new ApplicationException(ErrorEnum.ERROR_SESION, "error.ws-autenticacion-sesion-expirada", null);
			}
			
		} catch (ApplicationException e1){
			throw e1;
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
		return Boolean.TRUE;
	}

}
