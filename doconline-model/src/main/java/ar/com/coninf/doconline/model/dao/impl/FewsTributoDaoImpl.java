package ar.com.coninf.doconline.model.dao.impl;

import org.springframework.stereotype.Repository;

import ar.com.coninf.doconline.model.dao.FewsTributoDao;
import ar.com.coninf.doconline.shared.dto.FewsTributo;

@Repository(value="fewsTributoDao")
public class FewsTributoDaoImpl extends GenericDaoImpl<FewsTributo> implements FewsTributoDao {

}
