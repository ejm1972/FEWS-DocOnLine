package ar.com.boldt.monedero.model.dao;

import java.sql.SQLException;

import ar.com.boldt.monedero.shared.dto.ClienteDto;
import ar.com.coninf.doconline.model.dao.GenericDao;

public interface ClienteDao extends GenericDao<ClienteDto> {
	public ClienteDto getVigenteByKey(Long tipoDocumentoId,	Integer numeroDocumento, Long sexoId) throws SQLException;
	public ClienteDto getVigenteById(Long id) throws SQLException;
	public ClienteDto getByUsername(String username) throws SQLException;
	public ClienteDto getVigenteByUsername(String username) throws SQLException;
	public ClienteDto getByKey(ClienteDto cliente) throws SQLException;
	public ClienteDto getById(Long id) throws SQLException;
}
