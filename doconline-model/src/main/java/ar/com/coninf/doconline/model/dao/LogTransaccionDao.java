package ar.com.coninf.doconline.model.dao;

import java.sql.SQLException;

import ar.com.coninf.doconline.shared.dto.LogTransaccionDto;

public interface LogTransaccionDao extends GenericDao<LogTransaccionDto> {
	
	public int insertarAuditoria(LogTransaccionDto log) throws SQLException;
}
