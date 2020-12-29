package ar.com.coninf.doconline.model.dao;

import java.sql.SQLException;
import java.util.List;

public interface GenericDao<T> {
	public int add(T entity) throws SQLException;
	public int update(T entity) throws SQLException;
	public int delete(Long id) throws SQLException;
	public int delete(Integer id) throws SQLException;
	public T getById(Long id) throws SQLException;
	public T getById(Integer id) throws SQLException;
	public int getRowCount() throws SQLException;
	public ResultSetMapper<T> getAll() throws SQLException;
	public List<T> getAllList() throws SQLException;
	public T getByKey(T entity) throws SQLException;
	public T getByKeyGen(T entity);
	public List<T> getListById(Long id) throws SQLException;
	public List<T> getListById(Integer id) throws SQLException;
}
