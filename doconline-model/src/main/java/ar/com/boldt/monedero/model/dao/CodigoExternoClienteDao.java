package ar.com.boldt.monedero.model.dao;

import java.sql.SQLException;

import ar.com.boldt.monedero.shared.dto.CodExternoClienteDto;
import ar.com.coninf.doconline.model.dao.GenericDao;

public interface CodigoExternoClienteDao extends GenericDao<CodExternoClienteDto> {
	
	public CodExternoClienteDto getByClienteId(Long clienteId) throws SQLException;
}
