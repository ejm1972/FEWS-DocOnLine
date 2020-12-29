package ar.com.coninf.doconline.model.dao;

import java.sql.ResultSet;
import java.util.List;

public interface ResultSetMapper<T> {
	public void setResultSet(ResultSet rs);
	public void setClazz(Class<T> clazz);

	public int recCount();
	public boolean skip(int registros);
	public boolean skip();
	public int recNo();
	public boolean goBottom();
	public boolean goTop();
	public int getRegistrosProcesados();
	public T scatterName();
	public boolean eof();
	
	public List<T> getListObject();
	public boolean goTo(int registro);
}
