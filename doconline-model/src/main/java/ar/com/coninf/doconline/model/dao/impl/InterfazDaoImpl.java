package ar.com.coninf.doconline.model.dao.impl;

import org.springframework.stereotype.Repository;

import ar.com.coninf.doconline.model.dao.InterfazDao;
import ar.com.coninf.doconline.shared.dto.InterfazDto;

@Repository(value = "interfazDao")
public class InterfazDaoImpl extends GenericDaoImpl<InterfazDto> implements InterfazDao {
}
