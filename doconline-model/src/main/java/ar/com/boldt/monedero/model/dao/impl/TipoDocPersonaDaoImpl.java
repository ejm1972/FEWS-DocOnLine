package ar.com.boldt.monedero.model.dao.impl;

import org.springframework.stereotype.Repository;

import ar.com.boldt.monedero.model.dao.TipoDocPersonaDao;
import ar.com.boldt.monedero.shared.dto.TipoDocPersonaDto;
import ar.com.coninf.doconline.model.dao.impl.GenericDaoImpl;

@Repository(value="tipoDocPersonaDao")
public class TipoDocPersonaDaoImpl extends GenericDaoImpl<TipoDocPersonaDto> implements TipoDocPersonaDao {
}
