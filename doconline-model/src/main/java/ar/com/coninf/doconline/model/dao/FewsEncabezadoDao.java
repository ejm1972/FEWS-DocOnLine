package ar.com.coninf.doconline.model.dao;

import java.sql.SQLException;
import java.util.List;

import ar.com.coninf.doconline.shared.dto.FewsEncabezado;
import ar.com.coninf.doconline.shared.dto.FewsResultado;

public interface FewsEncabezadoDao extends GenericDao<FewsEncabezado> {

	public List<FewsEncabezado> getFewsPendiente(Long fewsId) throws SQLException;
	public List<FewsResultado> updateLog(Long id) throws SQLException;
	public List<FewsResultado> importLog() throws SQLException;
	public List<FewsResultado> setLogTimerSql() throws SQLException;
	public List<FewsResultado> getLogTimerSql() throws SQLException;
}
