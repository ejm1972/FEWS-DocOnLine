package ar.com.boldt.monedero.model.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Repository;

import ar.com.boldt.monedero.model.dao.MovimientoCuentaCorrienteDao;
import ar.com.boldt.monedero.model.util.GenericRowMapper;
import ar.com.boldt.monedero.shared.dto.MovimientoCtaCteDto;
import ar.com.coninf.doconline.model.dao.impl.GenericDaoImpl;

@Repository(value="movimientoCuentaCorrienteDao")
public class MovimientoCuentaCorrienteDaoImpl extends GenericDaoImpl<MovimientoCtaCteDto> implements MovimientoCuentaCorrienteDao {

	@Override
	public List<MovimientoCtaCteDto> getByCuentaCorrienteId(Long ctaCteId, Integer maxCantMovimientos) throws SQLException {

//		String sql = "SELECT s.* FROM" +
//			" (SELECT m.*, tm.desc_corta" +
//			" FROM " + getEntity() + " m" +
//			" INNER JOIN TIPOS_MOVIMIENTOS_CTACTE tm" +
//			" ON tm.ID_TIPO_MOVIMIENTO_CTACTE = m.ID_TIPO_MOVIMIENTO_CTACTE" +
//			" WHERE m.id_cuenta_corriente = ? ORDER BY f_movimiento desc) s" +
//			" WHERE rownum <= ? ORDER BY f_movimiento";

		String sql = "select top %d * from ( " + 
						" select * " +    
						" from (select m.ID_MOVIMIENTO_CTACTE, m.ID_TIPO_MOVIMIENTO_CTACTE, m.monto, m.id_cuenta_corriente, m.f_movimiento, m.cod_externo, m.id_reserva, m.saldo, tm.desc_corta,m.ID_INTERFAZ, m.cod_operacion " +           
								" from CUENTAS_CORRIENTES cte, " +  
									" movimientos_ctacte m, " + 
									" TIPOS_MOVIMIENTOS_CTACTE tm " + 
								" where m.id_cuenta_corriente = ? and " + 
									" m.id_cuenta_corriente = cte.ID_CUENTA_CORRIENTE and " +
									" tm.ID_TIPO_MOVIMIENTO_CTACTE = m.ID_TIPO_MOVIMIENTO_CTACTE " +  
								" union all " +
								" select m.ID_MOVIMIENTO_CTACTE, m.ID_TIPO_MOVIMIENTO_CTACTE, m.monto, m.id_cuenta_corriente, r.F_extraccion, m.cod_externo, m.id_reserva, " +  
									" (select SALDO from movimientos_ctacte  where id_movimiento_ctacte = ( " + 
									" select max(id_movimiento_ctacte) from movimientos_ctacte  where id_cuenta_corriente = m.id_cuenta_corriente " + 
									" and f_movimiento between r.F_RESERVA and r.F_EXTRACCION)) saldo, 'Extrac reserva',m.ID_INTERFAZ, m.cod_operacion " + 
								" from CUENTAS_CORRIENTES cte, " +
									" movimientos_ctacte m, " + 
									" reservas r " + 
								" where m.id_cuenta_corriente = ? and " +
									" m.id_cuenta_corriente = cte.ID_CUENTA_CORRIENTE and " + 
									" m.id_reserva = r.ID_RESERVA and r.ID_ESTADO_RESERVA = 2) A " + 
						" ) B " + 
					" order by f_movimiento desc";
	
	sql = String.format(sql, maxCantMovimientos);	
	
	List<MovimientoCtaCteDto> movimientoCtaCteDtos = getJdbcTemplate().query(sql,
				new GenericRowMapper<MovimientoCtaCteDto>(MovimientoCtaCteDto.class), 
				new Object[] {ctaCteId, ctaCteId});
		
		return movimientoCtaCteDtos;
	}
}

