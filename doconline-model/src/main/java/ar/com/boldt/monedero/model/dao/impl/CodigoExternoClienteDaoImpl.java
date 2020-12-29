package ar.com.boldt.monedero.model.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Repository;

import ar.com.boldt.monedero.model.dao.CodigoExternoClienteDao;
import ar.com.boldt.monedero.model.util.GenericRowMapper;
import ar.com.boldt.monedero.shared.dto.CodExternoClienteDto;
import ar.com.coninf.doconline.model.dao.impl.GenericDaoImpl;

@Repository(value="codigoExternoClienteDao")
public class CodigoExternoClienteDaoImpl extends GenericDaoImpl<CodExternoClienteDto> implements CodigoExternoClienteDao {
	
	@Override
	public CodExternoClienteDto getByClienteId(Long clienteId) throws SQLException {
		
		String sql = "SELECT codex.*" +
				" FROM " + getEntity() + " codex" +
				" WHERE codex.id_cliente = ?";
	
		List<CodExternoClienteDto> codExternoClienteDtos = getJdbcTemplate().query(sql,
				new GenericRowMapper<CodExternoClienteDto>(CodExternoClienteDto.class), 
				new Object[] {clienteId});
		
		CodExternoClienteDto codExternoClienteDto = null;
		
		if (codExternoClienteDtos.size() > 0) {
			codExternoClienteDto = codExternoClienteDtos.get(0); 
		}

		return codExternoClienteDto;
	}
}
