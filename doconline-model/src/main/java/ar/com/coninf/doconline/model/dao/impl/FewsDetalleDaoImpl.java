package ar.com.coninf.doconline.model.dao.impl;

import org.springframework.stereotype.Repository;

import ar.com.coninf.doconline.model.dao.FewsDetalleDao;
import ar.com.coninf.doconline.shared.dto.FewsDetalle;

@Repository(value="fewsDetalleDao")
public class FewsDetalleDaoImpl extends GenericDaoImpl<FewsDetalle> implements FewsDetalleDao {

}
