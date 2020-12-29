package ar.com.coninf.doconline.business.negocio;

import java.sql.SQLException;
import java.text.ParseException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ar.com.boldt.monedero.shared.enums.TipoServicioEnum;
import ar.com.coninf.doconline.model.dao.InterfazDao;
import ar.com.coninf.doconline.model.dao.PuntoVentaDao;
import ar.com.coninf.doconline.model.dao.RegistroTransaccionDao;
import ar.com.coninf.doconline.rest.model.enums.ErrorEnum;
import ar.com.coninf.doconline.shared.dto.InterfazDto;
import ar.com.coninf.doconline.shared.dto.RegistroTransaccionDto;
import ar.com.coninf.doconline.shared.excepcion.ApplicationException;
import ar.com.coninf.doconline.shared.util.MonederoUtiles;

@Component("business.registroTransaccionBusiness")
public class RegistroTransaccionBusiness {
	
	private Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	@Qualifier("registroTransaccionDao")
	private RegistroTransaccionDao registroTransaccionDao;

	@Autowired
	@Qualifier("interfazDao")
	private InterfazDao interfazDao;

	@Autowired
	@Qualifier("puntoVentaDao")
	private PuntoVentaDao puntoVentaDao;

	public RegistroTransaccionDto getByKey(RegistroTransaccionDto dto) throws SQLException, ParseException {
		dto.setFechaTransaccion(MonederoUtiles.formatearAFechaSinHora(dto.getFechaTransaccion()));
		return registroTransaccionDao.getByKey(dto);
	}

	public int add(RegistroTransaccionDto dto) throws Throwable {
		dto.setFechaTransaccion(MonederoUtiles.formatearAFechaSinHora(dto.getFechaTransaccion()));
		return registroTransaccionDao.add(dto);
	}

	public void validarInterfaz(Long idInterfaz) throws SQLException {
		// ---- Busco la interfaz
		InterfazDto interfaz = interfazDao.getById(idInterfaz);
		if (interfaz == null || interfaz.getId() == null) {
			throw new ApplicationException(ErrorEnum.ERROR_CONTROLADO, "error.ws-interfaz_invalida");
		}
	}

	public void validarPermisoInterfaz(Long idInterfaz, String servicio) throws SQLException {
		// ---- Busco la interfaz
		InterfazDto interfaz = interfazDao.getById(idInterfaz);
		if (interfaz == null || interfaz.getId() == null) {
			throw new ApplicationException(ErrorEnum.ERROR_CONTROLADO, "error.ws-interfaz_invalida");
		}
		
		Long idTipoServicio = TipoServicioEnum.find(servicio);
		 
		if (interfaz.getIdTipoServicio() != TipoServicioEnum.TODO.getCodigo() &&
				!interfaz.getIdTipoServicio().equals(idTipoServicio)) {
			throw new ApplicationException(ErrorEnum.ERROR_OPERACION_NO_PERMITIDA, "error.ws-interfaz_operacion_no_permitida");
		}
	
		if (!interfaz.getActivo().equals("S")) {
			log.warn("Interfaz " + idInterfaz + " Bloqueada. Servicio " + servicio);
			throw new ApplicationException(ErrorEnum.ERROR_INTERFAZ_BLOQUEADA, "error.ws-interfaz_bloqueada", new Object[]{idInterfaz});
		}
	}
}
