package ar.com.boldt.monedero.model.dao.impl;

import org.springframework.stereotype.Repository;

import ar.com.boldt.monedero.model.dao.EstadoCivilDao;
import ar.com.boldt.monedero.shared.dto.EstadoCivilDto;
import ar.com.coninf.doconline.model.dao.impl.GenericDaoImpl;

@Repository(value="estadoCivilDao")
public class EstadoCivilDaoImpl extends GenericDaoImpl<EstadoCivilDto> implements EstadoCivilDao {
}
