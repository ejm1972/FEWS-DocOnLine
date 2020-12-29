package ar.com.boldt.monedero.model.dao;

import java.sql.SQLException;
import java.util.List;

import ar.com.boldt.monedero.shared.dto.MovimientoCtaCteDto;
import ar.com.coninf.doconline.model.dao.GenericDao;

public interface MovimientoCuentaCorrienteDao extends GenericDao<MovimientoCtaCteDto> {
	public List<MovimientoCtaCteDto> getByCuentaCorrienteId(Long ctaCteId, Integer maxCantMovimientos) throws SQLException;
}
