package ar.com.boldt.monedero.model.dao.impl;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import ar.com.boldt.monedero.model.dao.ClienteDao;
import ar.com.boldt.monedero.model.util.GenericRowMapper;
import ar.com.boldt.monedero.shared.constant.MonederoConstantes;
import ar.com.boldt.monedero.shared.dto.ClienteDto;
import ar.com.boldt.monedero.shared.enums.TipoEstadoClienteEnum;
import ar.com.coninf.doconline.model.dao.impl.GenericDaoImpl;
import ar.com.coninf.doconline.rest.model.enums.ErrorEnum;
import ar.com.coninf.doconline.shared.excepcion.ApplicationException;

@Repository(value="clienteDao")
public class ClienteDaoImpl extends GenericDaoImpl<ClienteDto> implements ClienteDao {
	
	private Map<String, Field> propertyKey = new HashMap<String, Field>();

	@Override
	public ClienteDto getVigenteByKey(Long tipoDocumentoId,
			Integer numeroDocumento, Long sexoId) throws SQLException {

		String sql = "select cli.*, codex.F_VIGENCIA_DESDE, codex.F_VIGENCIA_HASTA, codex.COD_EXTERNO, codex.ID_PROVINCIA " +
				" from " + getEntity() + " cli, COD_EXTERNOS_CLIENTES codex " +
				"where cli.NRO_DOC = ? and cli.ID_SEXO = ? and cli.ID_TIPO_DOC_PERSONA = ? " +
				"and codex.ID_CLIENTE = cli.ID_CLIENTE and " +
				"f_vigencia_hasta = cast(? as datetime('dd/mm/yyyy))";
		
		List<ClienteDto> clientes = getJdbcTemplate().query(sql,
				new GenericRowMapper<ClienteDto>(ClienteDto.class), 
				new Object[] {numeroDocumento, sexoId, tipoDocumentoId, 
				MonederoConstantes.FECHA_VIGENCIA_HASTA});
		ClienteDto cliente = null;
		if (clientes.size() > 0) {
			cliente = clientes.get(0); 
		}
		
		if (cliente != null) {
			if (cliente.getFlgEstado() == TipoEstadoClienteEnum.BLOQUEADO.getCodigo()) {
				throw new ApplicationException(ErrorEnum.ERROR_CLIENTE_BLOQUEADO, "error.ws-cliente_bloqueado");
			}
		}

		return cliente;
	}

	@Override
	public ClienteDto getVigenteById(Long id) throws SQLException {
		
		String sql = "select cli.*, codex.F_VIGENCIA_DESDE, codex.F_VIGENCIA_HASTA, codex.COD_EXTERNO, codex.ID_PROVINCIA " +
				" from " + getEntity() + " cli, COD_EXTERNOS_CLIENTES codex " +
				"where cli.ID_CLIENTE = ? and " +
				" codex.ID_CLIENTE = cli.ID_CLIENTE and " +
				"f_vigencia_hasta = ?";
		
		List<ClienteDto> clientes = getJdbcTemplate().query(sql,
				new GenericRowMapper<ClienteDto>(ClienteDto.class), 
				new Object[] {id, MonederoConstantes.FECHA_VIGENCIA_HASTA});
		ClienteDto cliente = null;
		if (clientes.size() > 0) {
			cliente = clientes.get(0); 
		}

		if (cliente!=null) {
			if (cliente.getFlgEstado() == TipoEstadoClienteEnum.BLOQUEADO.getCodigo()) {
				throw new ApplicationException(ErrorEnum.ERROR_CLIENTE_BLOQUEADO, "error.ws-cliente_bloqueado");
			}
		}
		
		return cliente;
	}

	@Override
	public ClienteDto getByUsername(String username) throws SQLException {
		
		String sql = "select cli.*" +
			" from " + getEntity() + " cli" +
			" where cli.USUARIO = ?";
		
		List<ClienteDto> clientes = getJdbcTemplate().query(sql,
			new GenericRowMapper<ClienteDto>(ClienteDto.class), 
			new Object[] {username});
		
		ClienteDto cliente = null;
		
		if (clientes.size() > 0) {
			cliente = clientes.get(0); 
		}

		if (cliente != null) {
			if (cliente.getFlgEstado() == TipoEstadoClienteEnum.BLOQUEADO.getCodigo()) {
				throw new ApplicationException(ErrorEnum.ERROR_CLIENTE_BLOQUEADO, "error.ws-cliente_bloqueado");
			}
		}
		
		return cliente;
	}

	@Override
	public ClienteDto getVigenteByUsername(String username) throws SQLException {
		
		String sql = "select cli.*, codex.F_VIGENCIA_DESDE, codex.F_VIGENCIA_HASTA, codex.COD_EXTERNO, codex.ID_PROVINCIA " +
				" from " + getEntity() + " cli, COD_EXTERNOS_CLIENTES codex " +
				"where cli.USUARIO = ? and " +
				" codex.ID_CLIENTE = cli.ID_CLIENTE and " +
				"f_vigencia_hasta = ?";
		
		List<ClienteDto> clientes = getJdbcTemplate().query(sql,
				new GenericRowMapper<ClienteDto>(ClienteDto.class), 
				new Object[] {username, MonederoConstantes.FECHA_VIGENCIA_HASTA});
		ClienteDto cliente = null;
		if (clientes.size() > 0) {
			cliente = clientes.get(0); 
		}

		if (cliente != null) {
			if (cliente.getFlgEstado() == TipoEstadoClienteEnum.BLOQUEADO.getCodigo()) {
				throw new ApplicationException(ErrorEnum.ERROR_CLIENTE_BLOQUEADO, "error.ws-cliente_bloqueado");
			}
		}
		
		return cliente;
	}
	
	@Override
	public ClienteDto getById(Long id) throws SQLException {
		
		String sql = "Select * From " + getEntity() + " Where ID_CLIENTE= ?" ;
		
		List<ClienteDto> clientes = getJdbcTemplate().query(sql, 
				new GenericRowMapper<ClienteDto>(ClienteDto.class), 
				new Object[] {id});
		ClienteDto cliente = null;
		if (clientes.size() > 0) {
			cliente = clientes.get(0); 
		}
		
		if (cliente != null) {
			if (cliente.getFlgEstado() == TipoEstadoClienteEnum.BLOQUEADO.getCodigo()) {
				throw new ApplicationException(ErrorEnum.ERROR_CLIENTE_BLOQUEADO, "error.ws-cliente_bloqueado");
			}
		}

		return cliente;
	}

	@Override
	public ClienteDto getByKey(ClienteDto cli) throws SQLException {
		Object[] fields = new Object[propertyKey.size()];
		
		String sql = "Select * From " + getEntity() + " Where" + 
				" NRO_DOC = " + cli.getNumeroDocumento().toString() + 
				" and ID_SEXO = " + cli.getSexoId().toString() +
				" and ID_TIPO_DOC_PERSONA = " + cli.getTipoDocumentoId().toString();
		
		List<ClienteDto> list = getJdbcTemplate().query(sql, 
				fields,
				new GenericRowMapper<ClienteDto>(ClienteDto.class));
		ClienteDto cliente = null;
		if (list.size() > 0) {
			cliente = list.get(0);
		}
		
		if (cliente != null) {
			if (cliente.getFlgEstado() == TipoEstadoClienteEnum.BLOQUEADO.getCodigo()) {
				throw new ApplicationException(ErrorEnum.ERROR_CLIENTE_BLOQUEADO, "error.ws-cliente_bloqueado");
			}
		}

		return cliente;
	}
}
