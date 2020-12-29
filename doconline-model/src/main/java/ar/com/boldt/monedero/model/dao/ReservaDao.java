package ar.com.boldt.monedero.model.dao;

import java.sql.Timestamp;

import ar.com.boldt.monedero.shared.dto.ClienteDto;
import ar.com.boldt.monedero.shared.dto.ReservaDto;
import ar.com.coninf.doconline.model.dao.GenericDao;

public interface ReservaDao extends GenericDao<ReservaDto> {

	public ReservaDto getReservaCliente(ClienteDto cli, String codReserva);
	
	public Timestamp getFecha();
	
	

}
