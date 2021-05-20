package ar.com.coninf.doconline.model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.sourceforge.jtds.jdbc.CLOB;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import ar.com.coninf.doconline.model.dao.LogTransaccionDao;
import ar.com.coninf.doconline.shared.dto.LogTransaccionDto;

@Repository(value="logTransaccionDao")
public class LogTransaccionDaoImpl extends GenericDaoImpl<LogTransaccionDto> implements LogTransaccionDao {

	@Override
	public int insertarAuditoria(LogTransaccionDto log) throws SQLException {
		PreparedStatement statement = null;
		ResultSet generatedKeys = null;
		int affectedRows = 0;
		Connection connection = null;
		try{
			long tiempoOp = log.getFechaFinOp().getTime()-log.getFechaInicioOp().getTime();
			String tiempoOracle = tiempoOp + "/86400000)";
			String sysdateInicio = "getdate()-(" + tiempoOracle;
			String insert = "insert into LOG_TRANSACCIONES(F_INICIO_OPERACION,F_FIN_OPERACION,ID_TIPO_TRANSACCION,XML_ENTRADA,XML_SALIDA,ID_REGISTRO_TRANSACCION,CODIGO,DESCRIPCION,OBSERVACION,EXCEPCION_WSAA,EXCEPCION_WSFEV1,EXCEPCION_WSFEXV1,RESULTADO,ERR_MSG,OBS,XML_REQUEST_AFIP,XML_RESPONSE_AFIP,TIPO_COMPROBANTE,PUNTO_VENTA,NUMERO_COMPROBANTE,COMPROBANTE,FECHA_COMPROBANTE,IMPORTE_TOTAL,CAE,FECHA_VENCIMIENTO)" +
					" values ("+sysdateInicio+", getdate(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			connection = DataSourceUtils.getConnection(getJdbcTemplate().getDataSource());
			statement = connection.prepareStatement(insert, new String[] {"ID_LOG_TRANSACCION"});
//			if(log.getClienteId() == null){
//				statement.setNull(1, Types.NUMERIC);
//			}else{
//				statement.setLong(1, log.getClienteId());
//			}
			statement.setLong(1, log.getTipoTransaccionId());
			CLOB c = CLOB.createTemporary(connection);
			c.putChars(1, log.getXmlEntrada());
			statement.setClob(2, c);
			c = CLOB.createTemporary(connection);
			c.putChars(1, log.getXmlSalida());
			statement.setClob(3, c);
			statement.setLong(4, log.getRegistroTransaccionId());
			
			statement.setString(5, log.getCodigo());
			statement.setString(6, log.getDescripcion());
			statement.setString(7, log.getObservacion());
			statement.setString(8, log.getExcepcionWsaa());
			statement.setString(9, log.getExcepcionWsfev1());
			statement.setString(10, log.getExcepcionWsfexv1());
			statement.setString(11, log.getResultado());
			statement.setString(12, log.getErrMsg());
			statement.setString(13, log.getObs());
			c = CLOB.createTemporary(connection);
			c.putChars(1, log.getXmlRequestAfip());
			statement.setClob(14, c);
			c = CLOB.createTemporary(connection);
			c.putChars(1, log.getXmlResponseAfip());
			statement.setClob(15, c);
			statement.setString(16, log.getTipoComprobante());
			statement.setString(17, log.getPuntoVenta());
			statement.setString(18, log.getNumeroComprobante());
			statement.setString(19, log.getComprobante());
			statement.setString(20, log.getFechaComprobante());
			statement.setString(21, log.getImporteTotal());
			statement.setString(22, log.getCae());
			statement.setString(23, log.getFechaVencimiento());
			affectedRows = statement.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Error al insertar en la tabla LOG_TRANSACCIONES.");
			}
			generatedKeys = statement.getGeneratedKeys();
	        if (generatedKeys.next()) {
	        	log.setId(generatedKeys.getLong(1));
	        } else {
	            throw new SQLException("No se obtuve key.");
	        }
	 	} finally {
	        
	 		if (generatedKeys != null) try { generatedKeys.close(); } catch (SQLException e) {}
	        if (statement != null) try { statement.close(); } catch (SQLException e) {}
	        if (connection != null) DataSourceUtils.releaseConnection(connection, getJdbcTemplate().getDataSource());
	        
	    }
		return affectedRows;
	}
}
