package ar.com.boldt.monedero.model.dao;

import java.sql.SQLException;
import java.util.List;

import ar.com.boldt.monedero.shared.dto.CelularClienteDto;
import ar.com.coninf.doconline.model.dao.GenericDao;

public interface CelularClienteDao extends GenericDao<CelularClienteDto> {
	
	public List<CelularClienteDto> getByClienteId(Long clienteId) throws SQLException;
}
