package ar.com.boldt.monedero.model.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Repository;

import ar.com.boldt.monedero.model.dao.CelularClienteDao;
import ar.com.boldt.monedero.model.util.GenericRowMapper;
import ar.com.boldt.monedero.shared.dto.CelularClienteDto;
import ar.com.coninf.doconline.model.dao.impl.GenericDaoImpl;

@Repository(value="celularClienteDao")
public class CelularClienteDaoImpl extends GenericDaoImpl<CelularClienteDto> implements CelularClienteDao {
	
	@Override
	public List<CelularClienteDto> getByClienteId(Long clienteId) throws SQLException {
		
		String sql = "SELECT cel.*" +
				" FROM " + getEntity() + " cel" +
				" WHERE cel.ID_CLIENTE = ?";
	
		List<CelularClienteDto> celularClienteDtos = getJdbcTemplate().query(sql,
				new GenericRowMapper<CelularClienteDto>(CelularClienteDto.class), 
				new Object[] {clienteId});
		
		return celularClienteDtos;
	}
}
