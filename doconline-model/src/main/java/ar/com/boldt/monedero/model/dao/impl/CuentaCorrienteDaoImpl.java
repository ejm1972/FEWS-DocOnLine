package ar.com.boldt.monedero.model.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Repository;

import ar.com.boldt.monedero.model.dao.CuentaCorrienteDao;
import ar.com.boldt.monedero.model.util.GenericRowMapper;
import ar.com.boldt.monedero.shared.dto.CuentaCorrienteDto;
import ar.com.coninf.doconline.model.dao.impl.GenericDaoImpl;

@Repository(value="cuentaCorrienteDao")
public class CuentaCorrienteDaoImpl extends GenericDaoImpl<CuentaCorrienteDto> implements CuentaCorrienteDao {

	@Override
	public List<CuentaCorrienteDto> getByCuentasCorrientes(Long clienteId) throws SQLException {
		
		String sql = "SELECT CtaCte.*" +
				" FROM " + getEntity() + " CtaCte" +
				" WHERE CtaCte.ID_CLIENTE = ?";

		List<CuentaCorrienteDto> cuentasCorrienteDto = getJdbcTemplate().query(sql,
				new GenericRowMapper<CuentaCorrienteDto>(CuentaCorrienteDto.class), 
				new Object[] {clienteId});
		
		return cuentasCorrienteDto;
	}

	@Override
	public CuentaCorrienteDto getByClienteAndMoneda(Long clienteId,
		Long monedaId) throws SQLException {
		
		String sql = "SELECT CtaCte.*" +
				" FROM " + getEntity() + " CtaCte" +
				" WHERE CtaCte.ID_CLIENTE = ?" +
				" AND CtaCte.ID_TIPO_CTACTE = ?";

		List<CuentaCorrienteDto> cuentasCorrienteDto = getJdbcTemplate().query(sql,
				new GenericRowMapper<CuentaCorrienteDto>(CuentaCorrienteDto.class), 
				new Object[] {clienteId, monedaId});
		
		CuentaCorrienteDto cuentaCorrienteDto = null;
		
		if (cuentasCorrienteDto.size() > 0) {
			cuentaCorrienteDto = cuentasCorrienteDto.get(0); 
		}

		return cuentaCorrienteDto;
	}
	
	@Override
	public CuentaCorrienteDto getByUsuarioAndMoneda(String usuario,
		Long monedaId) throws SQLException {
		
		String sql = "SELECT CtaCte.*" +
				" FROM " + getEntity() + " CtaCte" +
				" INNER JOIN dbo.CLIENTES Cli" +
				" ON CtaCte.ID_CLIENTE = Cli.ID_CLIENTE" +								
				" AND Cli.USUARIO = ?" +
 				" AND CtaCte.ID_TIPO_CTACTE = ?";

		List<CuentaCorrienteDto> cuentasCorrienteDto = getJdbcTemplate().query(sql,
				new GenericRowMapper<CuentaCorrienteDto>(CuentaCorrienteDto.class), 
				new Object[] {usuario, monedaId});
		
		CuentaCorrienteDto cuentaCorrienteDto = null;
		
		if (cuentasCorrienteDto.size() > 0) {
			cuentaCorrienteDto = cuentasCorrienteDto.get(0); 
		}

		return cuentaCorrienteDto;
	}
}

