package ar.com.coninf.doconline.model.dao.impl;

import org.springframework.stereotype.Repository;

import ar.com.coninf.doconline.model.dao.LogTimerDao;
import ar.com.coninf.doconline.shared.dto.LogTimer;

@Repository(value="logTimerDao")
public class LogTimerDaoImpl extends GenericDaoImpl<LogTimer> implements LogTimerDao {

}
