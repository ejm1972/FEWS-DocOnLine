package ar.com.boldt.monedero.model.dao.impl;

import org.springframework.stereotype.Repository;

import ar.com.boldt.monedero.model.dao.LocalidadDao;
import ar.com.boldt.monedero.shared.dto.LocalidadDto;
import ar.com.coninf.doconline.model.dao.impl.GenericDaoImpl;

@Repository(value="localidadDao")
public class LocalidadDaoImpl extends GenericDaoImpl<LocalidadDto> implements LocalidadDao {
}
