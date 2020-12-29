package ar.com.boldt.monedero.model.dao.impl;

import org.springframework.stereotype.Repository;

import ar.com.boldt.monedero.model.dao.TipoMovimientoCuentaCorrienteDao;
import ar.com.boldt.monedero.shared.dto.TipoMovimientoCtaCteDto;
import ar.com.coninf.doconline.model.dao.impl.GenericDaoImpl;

@Repository(value="tipoMovimientoCuentaCorrienteDao")
public class TipoMovimientoCuentaCorrienteDaoImpl extends GenericDaoImpl<TipoMovimientoCtaCteDto> implements TipoMovimientoCuentaCorrienteDao {
}
