package ar.com.boldt.monedero.model.dao;

import java.math.BigInteger;
import java.sql.SQLException;

import ar.com.boldt.monedero.shared.dto.CodigoOperacionMovimientoDto;
import ar.com.coninf.doconline.model.dao.GenericDao;

public interface CodigoOperacionMovimientoDao extends GenericDao<CodigoOperacionMovimientoDto>  {

	public BigInteger getCodigo() throws SQLException;
	
}
