package ar.com.coninf.doconline.model.dao.impl;

import org.springframework.stereotype.Repository;

import ar.com.coninf.doconline.model.dao.FewsPermisoDao;
import ar.com.coninf.doconline.shared.dto.FewsPermiso;

@Repository(value="fewsPermisoDao")
public class FewsPermisoDaoImpl extends GenericDaoImpl<FewsPermiso> implements FewsPermisoDao {

}
