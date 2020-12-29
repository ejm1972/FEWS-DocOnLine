package ar.com.coninf.doconline.model.dao;

import java.util.Date;

import ar.com.boldt.monedero.shared.dto.ParametroDto;

public interface ParametroDao extends GenericDao<ParametroDto>{

	public String getValorVigente(String nombreParam);

	public String getValor(String nombreParam, Date fecha);
}
