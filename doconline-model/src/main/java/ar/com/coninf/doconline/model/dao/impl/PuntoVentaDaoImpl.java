package ar.com.coninf.doconline.model.dao.impl;

import org.springframework.stereotype.Repository;

import ar.com.coninf.doconline.model.dao.PuntoVentaDao;
import ar.com.coninf.doconline.shared.dto.PuntoVentaDto;

@Repository(value = "puntoVentaDao")
public class PuntoVentaDaoImpl extends GenericDaoImpl<PuntoVentaDto> implements PuntoVentaDao {

}
