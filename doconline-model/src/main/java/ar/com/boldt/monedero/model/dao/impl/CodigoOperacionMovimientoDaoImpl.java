package ar.com.boldt.monedero.model.dao.impl;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import ar.com.boldt.monedero.model.dao.CodigoOperacionMovimientoDao;
import ar.com.boldt.monedero.shared.dto.CodigoOperacionMovimientoDto;
import ar.com.coninf.doconline.model.dao.impl.GenericDaoImpl;


@Repository(value="codigoOperacionMovimientoDao")
public class CodigoOperacionMovimientoDaoImpl extends GenericDaoImpl<CodigoOperacionMovimientoDto> implements CodigoOperacionMovimientoDao {
	

	public BigInteger getCodigo() throws SQLException {
			BigInteger idGenerado = null;
			PreparedStatement statement = null;
			ResultSet generatedKeys = null;
			int affectedRows = 0;
			Connection connection = null;
			try {
					connection = DataSourceUtils.getConnection(getJdbcTemplate().getDataSource());
					String insert = "insert into NUM_CODIGO_OPERACION DEFAULT VALUES";
					statement = connection.prepareStatement(insert, new String[] {"NUMERO"});
					affectedRows = statement.executeUpdate();
					if (affectedRows == 0) {
						throw new SQLException("Error al intentar generar un codigo de movimiento.");
					}
			        generatedKeys = statement.getGeneratedKeys();
			        if (generatedKeys.next()) {
			        	idGenerado = generatedKeys.getBigDecimal(1).toBigInteger();
			        } else {
			            throw new SQLException("No se obtuvo Codigo Operacion.");
			        }
		    } catch (Exception e) {
		    	Logger.getLogger(this.getClass()).error("Ocurrio un error durante la obtencion de un nuevo codigo de operacion");
				e.printStackTrace();
		    } finally {
		        if (generatedKeys != null) try { generatedKeys.close(); } catch (SQLException e) {}
		        if (statement != null) try { statement.close(); } catch (SQLException e) {}
		        if (connection != null) DataSourceUtils.releaseConnection(connection, getJdbcTemplate().getDataSource());
		    }
			return idGenerado;
		}
}
