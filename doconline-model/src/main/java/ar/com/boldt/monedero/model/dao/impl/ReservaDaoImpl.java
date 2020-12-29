package ar.com.boldt.monedero.model.dao.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import ar.com.boldt.monedero.model.dao.ReservaDao;
import ar.com.boldt.monedero.model.util.GenericRowMapper;
import ar.com.boldt.monedero.shared.dto.ClienteDto;
import ar.com.boldt.monedero.shared.dto.ReservaDto;
import ar.com.coninf.doconline.model.dao.impl.GenericDaoImpl;

@Repository(value="reservaDao")
public class ReservaDaoImpl extends GenericDaoImpl<ReservaDto> implements ReservaDao {

	@Override
	public ReservaDto getReservaCliente(ClienteDto cli, String codReserva) {
		
		//¡¡¡CUIDADO!!! - en vez de * puse r.*, cc.*, mcc.*, c.* porque tanto la tabla
		// movimientos_ctacte como cuentas_corrientes tienen un campo saldo y
		//yo necesito sacarlo de la tabla cuentas_corrientes por eso la pongo primero.
		String sql = "select r.*, cc.*, mcc.*, c.* from " + getEntity() + " r" +
				" inner join movimientos_ctacte mcc" +
				" on mcc.id_reserva = r.id_reserva" +
				" inner join cuentas_corrientes cc" +
				" on cc.id_cuenta_corriente = mcc.id_cuenta_corriente" +
				" inner join clientes c" +
				" on c.id_cliente = cc.id_cliente" +
				" where c.usuario = ? " +
				" and r.codigo_reserva = ?";

		List<ReservaDto> reservas = getJdbcTemplate().query(sql,
				new GenericRowMapper<ReservaDto>(ReservaDto.class), 
				new Object[] {cli.getUsuario(), codReserva});
		ReservaDto reserva = null;
		if (reservas.size() > 0) {
			reserva = reservas.get(0); 
		}

		return reserva;
	}
	
	
	
	
	@Override
	public Timestamp getFecha() {
		String sqlfecha = "select getdate() as sysdate";
		Map<String, Object> queryForMap = getJdbcTemplate().queryForMap(sqlfecha);
		return (Timestamp)queryForMap.get("sysdate");
		
	}


}
