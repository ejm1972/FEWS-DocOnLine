package ar.com.boldt.monedero.model.dao.impl;

import org.springframework.stereotype.Repository;

import ar.com.boldt.monedero.model.dao.SexoDao;
import ar.com.boldt.monedero.shared.dto.SexoDto;
import ar.com.coninf.doconline.model.dao.impl.GenericDaoImpl;

@Repository(value="sexoDao")
public class SexoDaoImpl extends GenericDaoImpl<SexoDto> implements SexoDao {
}
