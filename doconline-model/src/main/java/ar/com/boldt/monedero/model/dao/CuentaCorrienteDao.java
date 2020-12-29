package ar.com.boldt.monedero.model.dao;

import java.sql.SQLException;
import java.util.List;

import ar.com.boldt.monedero.shared.dto.CuentaCorrienteDto;
import ar.com.coninf.doconline.model.dao.GenericDao;

public interface CuentaCorrienteDao extends GenericDao<CuentaCorrienteDto> {
	public List<CuentaCorrienteDto> getByCuentasCorrientes(Long clienteId) throws SQLException;
	public CuentaCorrienteDto getByClienteAndMoneda(Long clienteId, Long monedaId) throws SQLException;
	public CuentaCorrienteDto getByUsuarioAndMoneda(String usuario,	Long monedaId) throws SQLException;
}
