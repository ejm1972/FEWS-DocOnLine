package ar.com.coninf.doconline.model.dao.impl;

import org.springframework.stereotype.Repository;

import ar.com.coninf.doconline.model.dao.FewsQrDao;
import ar.com.coninf.doconline.shared.dto.FewsQr;

@Repository(value="fewsQrDao")
public class FewsQrDaoImpl extends GenericDaoImpl<FewsQr> implements FewsQrDao {
}
