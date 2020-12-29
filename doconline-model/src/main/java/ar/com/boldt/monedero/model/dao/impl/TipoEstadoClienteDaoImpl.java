package ar.com.boldt.monedero.model.dao.impl;

import org.springframework.stereotype.Repository;

import ar.com.boldt.monedero.model.dao.TipoEstadoClienteDao;
import ar.com.boldt.monedero.shared.dto.TipoEstadoClienteDto;
import ar.com.coninf.doconline.model.dao.impl.GenericDaoImpl;

@Repository(value="tipoEstadoClienteDao")
public class TipoEstadoClienteDaoImpl extends GenericDaoImpl<TipoEstadoClienteDto> implements TipoEstadoClienteDao {
}
