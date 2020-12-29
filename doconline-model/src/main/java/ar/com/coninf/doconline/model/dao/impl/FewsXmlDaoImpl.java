package ar.com.coninf.doconline.model.dao.impl;

import org.springframework.stereotype.Repository;

import ar.com.coninf.doconline.model.dao.FewsXmlDao;
import ar.com.coninf.doconline.shared.dto.FewsXml;

@Repository(value="fewsXmlDao")
public class FewsXmlDaoImpl extends GenericDaoImpl<FewsXml> implements FewsXmlDao {
}
