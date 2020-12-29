package ar.com.boldt.monedero.model.dao.impl;

import org.springframework.stereotype.Repository;

import ar.com.boldt.monedero.model.dao.DireccionClienteDao;
import ar.com.boldt.monedero.shared.dto.DireccionClienteDto;
import ar.com.coninf.doconline.model.dao.impl.GenericDaoImpl;

@Repository(value="direccionClienteDao")
public class DireccionClienteDaoImpl extends GenericDaoImpl<DireccionClienteDto> implements DireccionClienteDao {

}
