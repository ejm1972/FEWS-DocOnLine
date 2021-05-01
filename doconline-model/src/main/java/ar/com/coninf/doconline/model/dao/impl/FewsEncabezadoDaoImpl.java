package ar.com.coninf.doconline.model.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import ar.com.boldt.monedero.model.util.GenericRowMapper;
import ar.com.coninf.doconline.model.dao.FewsEncabezadoDao;
import ar.com.coninf.doconline.shared.dto.FewsEncabezado;
import ar.com.coninf.doconline.shared.dto.FewsResultado;

@Repository(value="fewsEncabezadoDao")
public class FewsEncabezadoDaoImpl extends GenericDaoImpl<FewsEncabezado> implements FewsEncabezadoDao {
	private Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	@Qualifier("dolProperties")
	protected Properties dolProperties;

	@Override
	public List<FewsEncabezado> getFewsPendiente(Long fewsId) throws SQLException {

		logger.debug("Ejecucion fewsEncabezadoDaoImpl.getFewsPendiente() - exec fews_pendientes");
		
		String sql = "exec fews_pendientes";

		List<FewsEncabezado> fewsList = getJdbcTemplate().query(sql,
				new GenericRowMapper<FewsEncabezado>(FewsEncabezado.class), 
				new Object[] {});

		return fewsList;
	}

	@Override
	public List<FewsResultado> updateLog(Long id, Long interfaz) throws SQLException {

		String destino = dolProperties.getProperty("actualizador_log_"+interfaz.toString());
		String sql = "exec " + destino + " ?";
		logger.debug("Ejecucion fewsEncabezadoDaoImpl.updateLog() - "+ sql + " -> " + id.toString()+","+interfaz.toString());

		List<FewsResultado> fewsList = getJdbcTemplate().query(sql,
				new GenericRowMapper<FewsResultado>(FewsResultado.class), 
				new Object[] {id});

		return fewsList;
	}

	@Override
	public List<FewsResultado> importLog(Long interfaz) throws SQLException {

		String destino = dolProperties.getProperty("importador_log_"+interfaz.toString());
		String sql = "exec " + destino;
		logger.debug("Ejecucion fewsEncabezadoDaoImpl.importLog() - " + sql + " -> " + interfaz.toString());

		List<FewsResultado> fewsList = getJdbcTemplate().query(sql,
				new GenericRowMapper<FewsResultado>(FewsResultado.class), 
				new Object[] {});

		return fewsList;
	}

	@Override
	public List<FewsResultado> setLogTimerSql() throws SQLException {

		String sql = "exec fews_upd_log_Timer 'TimerSql', 'UPD'";
		logger.debug("Ejecucion fewsEncabezadoDaoImpl.setLogTimerSql() - exec fews_upd_log_Timer 'TimerSql', 'UPD'");

		List<FewsResultado> fewsList = getJdbcTemplate().query(sql,
				new GenericRowMapper<FewsResultado>(FewsResultado.class), 
				new Object[] {});
		
		return fewsList;
	}

	@Override
	public List<FewsResultado> getLogTimerSql() throws SQLException {

		String sql = "exec fews_upd_log_Timer 'TimerSql', 'CTR'";
		logger.debug("Ejecucion fewsEncabezadoDaoImpl.getLogTimerSql() - exec fews_upd_log_Timer 'TimerSql', 'CTR'");

		List<FewsResultado> fewsList = getJdbcTemplate().query(sql,
				new GenericRowMapper<FewsResultado>(FewsResultado.class), 
				new Object[] {});

		return fewsList;
	}

}
