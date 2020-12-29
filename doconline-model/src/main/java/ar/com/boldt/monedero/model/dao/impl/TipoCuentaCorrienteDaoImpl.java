package ar.com.boldt.monedero.model.dao.impl;

import org.springframework.stereotype.Repository;

import ar.com.boldt.monedero.model.dao.TipoCuentaCorrienteDao;
import ar.com.boldt.monedero.shared.dto.TipoCtaCteDto;
import ar.com.coninf.doconline.model.dao.impl.GenericDaoImpl;

@Repository(value="tipoCuentaCorrienteDao")
public class TipoCuentaCorrienteDaoImpl extends GenericDaoImpl<TipoCtaCteDto> implements TipoCuentaCorrienteDao {
}
