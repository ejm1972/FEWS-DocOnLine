package ar.com.boldt.monedero.model.dao.impl;

import org.springframework.stereotype.Repository;

import ar.com.boldt.monedero.model.dao.ProvinciaDao;
import ar.com.boldt.monedero.shared.dto.ProvinciaDto;
import ar.com.coninf.doconline.model.dao.impl.GenericDaoImpl;

@Repository(value="provinciaDao")
public class ProvinciaDaoImpl extends GenericDaoImpl<ProvinciaDto> implements ProvinciaDao {
}
