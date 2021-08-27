package ar.com.coninf.doconline.model.dao.impl;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.sourceforge.jtds.jdbc.CLOB;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;

import ar.com.coninf.doconline.model.dao.GenericDao;
import ar.com.coninf.doconline.model.dao.ResultSetMapper;
import ar.com.coninf.doconline.model.util.GenericRowMapper;
import ar.com.coninf.doconline.shared.anotacion.InsertOptional;
import ar.com.coninf.doconline.shared.anotacion.Key;

public abstract class GenericDaoImpl<T> implements GenericDao<T> {
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final Class<?> clazz;
	
	private Map<String, Field> propertyId = new HashMap<String, Field>();
	private Map<String, Field> propertyMap = new HashMap<String, Field>();
	private Map<String, Field> propertyKey = new HashMap<String, Field>();
	private Map<String, String> generatedValues = new HashMap<String, String>();
	private Map<String, Field> insertOptionalMap = new HashMap<String, Field>();
	
	public GenericDaoImpl() {
        ParameterizedType superclass = (ParameterizedType) getClass().getGenericSuperclass();
		this.clazz = (Class<?>) ((ParameterizedType) superclass).getActualTypeArguments()[0];

		for (Method method : clazz.getDeclaredMethods()) {
			if (method.isAnnotationPresent(Transient.class)) {
				continue;
			}

			if (method.isAnnotationPresent(Column.class)) {
				Column column = method.getAnnotation(Column.class);
				String fieldName = method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4); 
				Field fieldId = null;
				
				for (Field field : clazz.getDeclaredFields()) {
					if (field.getName().toString().equals(fieldName)) {
						propertyMap.put(column.name(), field);
						fieldId = field;
					}
				}
				
				if (method.isAnnotationPresent(Id.class)) {
					propertyId.put(column.name(), fieldId);
				}
			}

		}
		
		for (Field field : clazz.getDeclaredFields()) {
			if (field.isAnnotationPresent(Transient.class)) {
				continue;
			}

			if (field.isAnnotationPresent(Column.class)) {
				Column column = field.getAnnotation(Column.class);
				propertyMap.put(column.name(), field);
				
				if (field.isAnnotationPresent(Id.class)) {
					propertyId.put(column.name(), field);
				}

				if (field.isAnnotationPresent(GeneratedValue.class)) {
					GeneratedValue gv = field.getAnnotation(GeneratedValue.class);
					generatedValues.put(column.name(), gv.generator());
				}

				if (field.isAnnotationPresent(InsertOptional.class)) {
					insertOptionalMap.put(column.name(), field);
				}

				if (field.isAnnotationPresent(Key.class)) {
					propertyKey.put(column.name(), field);
				}
			}
		}
	}

	protected String getEntity() {
		return clazz.getAnnotation(Table.class).name();
	}
	
	@Override
	public int delete(Long id) throws SQLException {
		return getJdbcTemplate().update("delete from " + getEntity() + " Where " + getIdColumn() + " = ?", id);
	}

	@Override
	public int delete(Integer id) throws SQLException {
		return getJdbcTemplate().update("delete from " + getEntity() + " Where " + getIdColumn() + " = ?", id);
	}

	@Override
	public T getById(Long id) throws SQLException {
		String sql = "Select * From " + getEntity() + " Where " + getIdColumn() + " = ?" ;
		T entity = null;
		
		@SuppressWarnings("unchecked")
		List<T> entities = getJdbcTemplate().query(sql, new GenericRowMapper<T>((Class<T>) clazz), id);
		
		if (entities.size() > 0) {
			entity = entities.get(0); 
		}
		
		return entity;
	}

	@Override
	public T getById(Integer id) throws SQLException {
		String sql = "Select * From " + getEntity() + " Where " + getIdColumn() + " = ?" ;
		T entity = null;
		
		@SuppressWarnings("unchecked")
		List<T> entities = getJdbcTemplate().query(sql, new GenericRowMapper<T>((Class<T>) clazz), id);
		
		if (entities.size() > 0) {
			entity = entities.get(0); 
		}
		
		return entity;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> getListById(Long id) throws SQLException {
		String sql = "Select * From " + getEntity() + " Where id = ?" ;

		List<T> entities = getJdbcTemplate().query(sql, new GenericRowMapper<T>((Class<T>) clazz), id);
		
		return entities;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> getListById(Integer id) throws SQLException {
		String sql = "Select * From " + getEntity() + " Where id = ?" ;

		List<T> entities = getJdbcTemplate().query(sql, new GenericRowMapper<T>((Class<T>) clazz), id);
		
		return entities;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getByKey(T entity) throws SQLException {
		Object[] fields = new Object[propertyKey.size()];
		String sql = "Select * From " + getEntity() + " Where " + getWhere(entity, propertyKey, fields);
		T retObj = null;
		
		try {
			retObj = (T) clazz.newInstance();
		} catch (InstantiationException e2) {
			e2.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<T> list = getJdbcTemplate().query(sql, fields,
				new GenericRowMapper<T>((Class<T>) clazz));
		
		if (list.size() > 0) {
			retObj = list.get(0);
		}
		
		return retObj;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getByKeyGen(T entity) {
		Object[] fields = new Object[propertyKey.size()];
		String sql = "Select * From " + getEntity() + " Where " + getWhere(entity, propertyKey, fields);
		T retObj = null;
		
		try {
			retObj = (T) clazz.newInstance();
		} catch (InstantiationException e2) {
			e2.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<T> list = getJdbcTemplate().query(sql, fields,
				new GenericRowMapper<T>((Class<T>) clazz));
		
		if (list.size() > 0) {
			retObj = list.get(0);
		}
		
		return retObj;
	}

	@SuppressWarnings("deprecation")
	@Override
	public int getRowCount() throws SQLException {
		String sql = "Select Count(*) From " + getEntity();
		int total = getJdbcTemplate().queryForInt(sql);
		return total;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> getAllList() throws SQLException {
		String sql = "Select * From " + getEntity();
		
		List<T> entities = getJdbcTemplate().query(sql, new GenericRowMapper<T>((Class<T>) clazz));
		
		return entities;
	}

	@SuppressWarnings("unchecked")
	public ResultSetMapper<T> getAll() throws SQLException {
		Connection connection = DataSourceUtils.getConnection(getJdbcTemplate().getDataSource());
		Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

		String sql = "Select * From " + getEntity();
		ResultSet rs = statement.executeQuery(sql);
		
        if (statement != null) try { statement.close(); } catch (SQLException e) {}
        if (connection != null) DataSourceUtils.releaseConnection(connection, getJdbcTemplate().getDataSource());
		return new ResultSetMapperImpl<T>(rs, (Class<T>) clazz);
	}

	@Override
	public int update(T entity) throws SQLException {
		Object[] fields = new Object[propertyMap.size()];
		String sql = "update " + getEntity() + " Set " + getSet(entity, propertyMap, fields) + " Where " + getIdColumn() + " = ?" ;
		
		fields[propertyMap.size() - 1] = getIdValue(entity);
		
		int affectedRows = definirCamposYEjecutar(entity, fields, sql);
		
		return affectedRows;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public int add(T entity) throws SQLException {
		Object[] fields = new Object[propertyMap.size()];
		StringBuilder val = new StringBuilder();
		String insert = getAdd(entity, propertyMap, val, fields);
		String values = val.toString();
		
		Iterator<Entry<String, String>> it = generatedValues.entrySet().iterator();

		while(it.hasNext()){
		      Map.Entry e = (Map.Entry) it.next();
		      insert += (insert.isEmpty() ? "" : ", ") + e.getKey();
		      values += (values.isEmpty() ? "" : ", ") + e.getValue();
		}
		
		int affectedRows = 0;
		String sql = null;
		try {
		
			 sql = "insert into " + getEntity() + "(" +
						insert + ") values (" + values + ")";
		
		
			affectedRows = definirCamposYEjecutar(entity, fields, sql);
		}catch(SQLException ex){
			logger.error(sql);
			throw ex;
		}
		
		return affectedRows;
	}

	/**
	 * Utilizado para definir los campos del insert y del update. Luego ejecuta la sentencia
	 * @param entity
	 * @param fields
	 * @param sql
	 * @return la cantidad de registros afectados.
	 * @throws SQLException
	 */
	private int definirCamposYEjecutar(T entity, Object[] fields, String sql) throws SQLException {
		PreparedStatement statement = null;
		ResultSet generatedKeys = null;
		int affectedRows = 0;
		boolean esInsert = sql.startsWith("insert");
		Connection connection = null; 
		try {
			connection = DataSourceUtils.getConnection(getJdbcTemplate().getDataSource());
			if(esInsert){
				statement = connection.prepareStatement(sql, new String[] {getIdColumn()});
			}else{
				statement = connection.prepareStatement(sql);
			}
			for (int i = 0; i < fields.length; i++) {
				Object field = fields[i];
				if (field != null) {
					if (field instanceof Integer) {
						statement.setInt(i + 1, (Integer) field);
					} else if (field instanceof Long) {
						statement.setLong(i + 1, (Long) field);
					} else if (field instanceof Timestamp || field instanceof Date) {
						statement.setTimestamp(i + 1, (Timestamp) field);
					} else if (field instanceof java.util.Date) {
						statement.setDate(i + 1, new Date(((java.util.Date)field).getTime()));
					} else if (field instanceof Double) {
						statement.setDouble(i + 1, (Double) field);
					} else if (field instanceof BigDecimal) {
						statement.setBigDecimal(i + 1, (BigDecimal) field);
					} else if (field instanceof String) {
						statement.setString(i + 1, (String) field);
					} else if (field instanceof byte[]) {
						statement.setBytes(i + 1, (byte[]) field);
					} else if (field instanceof char[]) {
						CLOB c = CLOB.createTemporary(connection);
						c.putChars(1, (char[]) field);
						statement.setClob(i + 1, c);
					}
				}else if(!esInsert){
					statement.setNull(i + 1, Types.VARCHAR);//No importa el tipo de dato que se defina.
				}
			}

			affectedRows = statement.executeUpdate();
	        if (affectedRows == 0) {
	        	if(esInsert){
	        		throw new SQLException("Error al insertar.");
	        	}else{
	        		throw new SQLException("Error al actualizar.");
	        	}
	        }
	
	        if(esInsert){
		        generatedKeys = statement.getGeneratedKeys();
		        if (generatedKeys.next()) {
		        	setIdValue(entity, generatedKeys.getLong(1));
		        } else {
		            throw new SQLException("No se obtuve key.");
		        }
	        }

	    } finally {
	        if (generatedKeys != null) try { generatedKeys.close(); } catch (SQLException e) {}
	        if (statement != null) try { statement.close(); } catch (SQLException e) {}
	        if (connection != null) DataSourceUtils.releaseConnection(connection, getJdbcTemplate().getDataSource());
	    }
		return affectedRows;
	}

	/*
	@Override
	public int add(final T entity) throws SQLException {
		Object[] fields = new Object[propertyMap.size()];
		KeyHolder keyHolder = new GeneratedKeyHolder();
		final String addFields = getAdd(entity, propertyMap, fields);
		
		int rows = getJdbcTemplate().update(new PreparedStatementCreator() {
			  public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				  String sql = "insert into " + getEntity() + "(" + addFields + ")";
				  PreparedStatement ps = connection.prepareStatement(sql, new String[] {getIdColumn()});
				  ps.setLong(1, fields);
				  ps.setInt(2, cliente.getNumeroDocumento());
				  ps.setLong(3, cliente.getSexoId());
				  ps.setString(4, cliente.getPin());
			    
				  return ps;
			  }
			}, keyHolder);
		
		Long generatedId = new Long(keyHolder.getKey().longValue());
		entity.setId(generatedId);
		
		return rows;
	}
	*/
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private String getAdd(T entity, Map<String, Field> map, StringBuilder values, Object[] fields) {
		int i = 0;
		String insert = "";
		
		Iterator<Entry<String, Field>> it = map.entrySet().iterator();
		
		while (it.hasNext()) {
			Map.Entry e = (Map.Entry)it.next();
			Field field = (Field) e.getValue();
			String column = (String) e.getKey();
			Object value = null;
			
			boolean isAccesible = field.isAccessible();
			field.setAccessible(true);

			try {
				value = ((T) field.get(entity));
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			}

			field.setAccessible(isAccesible);

			if ((insertOptionalMap == null || !insertOptionalMap.containsKey(column)) && 
					(generatedValues == null || !generatedValues.containsKey(column))) {
				insert += (insert.isEmpty() ? "" : ", ") + column;
				values.append((values.length() == 0 ? "" : ", ") + "?");
				fields[i++] = value;
			} else {
				if (value != null) {
					insert += (insert.isEmpty() ? "" : ", ") + column;
					values.append((values.length() == 0 ? "" : ", ") + "?");
					fields[i++] = value;
				}
			}
		}
		
		return insert;
	}

	private String getSet(T entity, Map<String, Field> map, Object[] fields) {
		return getWhere(entity, map, fields, ", ", propertyId, false);
	}

	private String getWhere(T entity, Map<String, Field> map, Object[] fields) {
		return getWhere(entity, map, fields, " and ", null, false);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String getWhere(T entity, Map<String, Field> map, Object[] fields, 
					String separator, Map<String, Field> fieldsExcept, boolean fieldsExceptOptional) {
		int i = 0;
		String where = "";
		
		Iterator<Entry<String, Field>> it = map.entrySet().iterator();
		
		while (it.hasNext()) {
			Map.Entry e = (Map.Entry)it.next();
			Field field = (Field) e.getValue();
			String column = (String) e.getKey();
			Object value = null;
			
			boolean isAccesible = field.isAccessible();
			field.setAccessible(true);

			try {
				value = ((T) field.get(entity));
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			}

			field.setAccessible(isAccesible);

			if (fieldsExcept == null || !fieldsExcept.containsKey(column)) {
				where += (where.isEmpty() ? "" : separator) + column + " = ? ";
				fields[i++] = value;
			} else {
				if (fieldsExceptOptional && value != null) {
					where += (where.isEmpty() ? "" : separator) + column + " = ? ";
					fields[i++] = value;
				}
			}
		}
		
		return where;
	}
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@SuppressWarnings("unchecked")
	private Object getIdValue(T entity) {
		Object retObj = null;
		
		try {
			Field field = getIdField();
			
			boolean isAccesible = field.isAccessible();
			field.setAccessible(true);
			retObj = (T) field.get(entity);
			field.setAccessible(isAccesible);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return retObj;
	}
	
	private void setIdValue(T entity, Long value) {
		try {
			Field field = getIdField();
			
			boolean isAccesible = field.isAccessible();
			field.setAccessible(true);
			field.set(entity, value);
			field.setAccessible(isAccesible);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	private void setIdValue(T entity, Integer value) {
		try {
			Field field = getIdField();
			
			boolean isAccesible = field.isAccessible();
			field.setAccessible(true);
			field.set(entity, value);
			field.setAccessible(isAccesible);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	private Field getIdField() {
		Field field = null;
		Iterator<Entry<String, Field>> it = propertyId.entrySet().iterator();
		
		while (it.hasNext()) {
			Map.Entry e = (Map.Entry)it.next();
			field = (Field) e.getValue();
		}
		
		return field;
	}

	@SuppressWarnings("rawtypes")
	private String getIdColumn() {
		String idColumn = null;
		
		Iterator<Entry<String, Field>> it = propertyId.entrySet().iterator();
		
		while (it.hasNext()) {
			Map.Entry e = (Map.Entry)it.next();
			idColumn = (String) e.getKey();
		}
		
		return idColumn;
	}
}
