package ar.com.boldt.monedero.model.dao.impl;

import org.springframework.stereotype.Repository;

import ar.com.boldt.monedero.model.dao.PaisDao;
import ar.com.boldt.monedero.shared.dto.PaisDto;
import ar.com.coninf.doconline.model.dao.impl.GenericDaoImpl;

@Repository(value="paisDao")
public class PaisDaoImpl extends GenericDaoImpl<PaisDto> implements PaisDao {
}
