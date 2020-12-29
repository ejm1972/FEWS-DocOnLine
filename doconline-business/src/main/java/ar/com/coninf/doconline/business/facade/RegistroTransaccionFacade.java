package ar.com.coninf.doconline.business.facade;

import java.sql.SQLException;
import java.text.ParseException;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ar.com.coninf.doconline.business.negocio.RegistroTransaccionBusiness;
import ar.com.coninf.doconline.shared.dto.RegistroTransaccionDto;

@Component("facade.registroTransaccionFacade")
public class RegistroTransaccionFacade {

	@Autowired
	@Qualifier("business.registroTransaccionBusiness")
	private RegistroTransaccionBusiness registroTransaccionBusiness;
	
	public RegistroTransaccionDto getByKey(RegistroTransaccionDto dto) throws SQLException, ParseException {
		return registroTransaccionBusiness.getByKey(dto);
	}

	public int add(RegistroTransaccionDto dto) throws Throwable {
		return registroTransaccionBusiness.add(dto);
	}
	
	public void validarInterfaz(Long idInterfaz) throws Throwable {
		registroTransaccionBusiness.validarInterfaz(idInterfaz);
	}

	public void validarPermisoInterfaz(Long idInterfaz, String servicio) throws Throwable {
		registroTransaccionBusiness.validarPermisoInterfaz(idInterfaz, servicio);
	}

	public void registrarClienteRollBack(String codCliente) {
		throw new NotImplementedException("registrarClienteRollBack not implemented.");
	}
}