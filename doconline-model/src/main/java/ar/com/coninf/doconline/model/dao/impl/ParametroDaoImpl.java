package ar.com.coninf.doconline.model.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import ar.com.boldt.monedero.shared.constant.MonederoConstantes;
import ar.com.boldt.monedero.shared.dto.ParametroDto;
import ar.com.coninf.doconline.model.dao.ParametroDao;
import ar.com.coninf.doconline.shared.util.MonederoUtiles;

@Repository(value="parametroDao")
public class ParametroDaoImpl extends GenericDaoImpl<ParametroDto> implements ParametroDao {

	@Override
	public String getValorVigente(String nombreParam) {
		String sql = "select pv.valor from parametros p " +
					"inner join parametros_valor pv on pv.id_parametro = p.id_parametro " +
					"where p.parametro = ? " +
					"and pv.f_vigencia_hasta = ?";
		
		List<String> valores = getJdbcTemplate().queryForList(sql,
				new Object[] {nombreParam, MonederoConstantes.FECHA_VIGENCIA_HASTA},
				String.class);
		String valor = null;
		if (valores.size() > 0) {
			valor = valores.get(0); 
		}

		return valor;
	}

	@Override
	public String getValor(String nombreParam, Date fecha) {
		String sql = "select pv.valor from parametros p " +
				"inner join parametros_valor pv on pv.id_parametro = p.id_parametro " +
				"where p.parametro = ? " +
				"and pv.f_vigencia_desde <= ? " +
				"and pv.f_vigencia_hasta >= ? ";
	
		List<String> valores = getJdbcTemplate().queryForList(sql,
				new Object[] {nombreParam, MonederoUtiles.convertirFechayyyyMMdd(fecha), MonederoUtiles.convertirFechayyyyMMdd(fecha)},
				String.class);
		String valor = null;
		if (valores.size() > 0) {
			valor = valores.get(0); 
		}
	
		return valor;
	}

	
}
