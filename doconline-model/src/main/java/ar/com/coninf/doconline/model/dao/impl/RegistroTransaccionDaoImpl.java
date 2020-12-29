package ar.com.coninf.doconline.model.dao.impl;

import org.springframework.stereotype.Repository;

import ar.com.coninf.doconline.model.dao.RegistroTransaccionDao;
import ar.com.coninf.doconline.shared.dto.RegistroTransaccionDto;

@Repository(value="registroTransaccionDao")
public class RegistroTransaccionDaoImpl extends GenericDaoImpl<RegistroTransaccionDto> implements RegistroTransaccionDao {
}
