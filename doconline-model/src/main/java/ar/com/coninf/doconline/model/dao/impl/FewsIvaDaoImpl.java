package ar.com.coninf.doconline.model.dao.impl;

import org.springframework.stereotype.Repository;

import ar.com.coninf.doconline.model.dao.FewsIvaDao;
import ar.com.coninf.doconline.shared.dto.FewsIva;

@Repository(value="fewsIvaDao")
public class FewsIvaDaoImpl extends GenericDaoImpl<FewsIva> implements FewsIvaDao {

}
