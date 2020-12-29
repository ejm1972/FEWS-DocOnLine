package ar.com.coninf.doconline.model.dao.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;

import ar.com.coninf.doconline.model.dao.ObjectManager;
import ar.com.coninf.doconline.model.dao.ResultSetMapper;


public class ResultSetMapperImpl<T> implements ResultSetMapper<T> {
	private Class<T> clazz;
	private Map<String, Field> propertyMap;
	private T bean = null;
	private ObjectManager objectManager;
	private ResultSet rs;
	private int registrosProcesados;
	private int registrosTotales;
	private boolean eof;
	
	public ResultSetMapperImpl(ResultSet rs) {
		this(rs, null);
	}

	public ResultSetMapperImpl(Class<T> clazz) {
		this(null, clazz);
	}

	public ResultSetMapperImpl(ResultSet rs, Class<T> clazz) {
		setResultSet(rs);
		setClazz(clazz);
	}

	private void setData() {
        objectManager = new ObjectManagerImpl();
        propertyMap = new HashMap<String, Field>();

        if (clazz != null) {
	    	if (clazz.isAnnotationPresent(Entity.class)) {
				for (Method method : clazz.getDeclaredMethods()) {
					if (method.isAnnotationPresent(Column.class)) {
						Column column = method.getAnnotation(Column.class);
						String fieldName = method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4); 
	
						for (Field field : clazz.getDeclaredFields()) {
							if (field.getName().toString().equals(fieldName))
								propertyMap.put(column.name(), field);
						}
						
					}
				}
				
				if (propertyMap.isEmpty()) {
					for (Field field : clazz.getDeclaredFields()) {
						if (field.isAnnotationPresent(Column.class)) {
							Column column = field.getAnnotation(Column.class);
							propertyMap.put(column.name(), field);
						}
					}
				}
			}
        }
	}
	
	@Override
	public void setResultSet(ResultSet rs) {
		registrosProcesados = 0;
		registrosTotales = 0;
		
		this.rs = rs;
		
		try {
			rs.last();
			registrosTotales = rs.getRow();
			eof = !rs.first();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void setClazz(Class<T> clazz) {
		this.clazz = clazz;
		setData();
	}

	@Override
	public boolean goTo(int registro) {
		try {
			eof = !rs.absolute(registro);			
			mapRowToObject();
		} catch (SQLException e) {
			eof = true;
		}
		return eof;
	}
	
	@Override
	public boolean skip(int registros) {
		try {
			registrosProcesados += registros;
			eof = !rs.absolute(rs.getRow() + registros);	
			mapRowToObject();
		} catch (SQLException e) {
			eof = true;
		}
		return eof;
	}
	
	@Override
	public boolean skip() {
		return skip(1);
	}

	@Override
	public int recNo() {
		int recno = 0;
		try {
			recno = rs.getRow();			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return recno;
	}
	
	@Override
	public boolean goBottom() {
		try {
			eof = !rs.last();
			mapRowToObject();
		} catch (SQLException e) {
			eof = true;
		}
		
		return eof;
	}
	
	@Override
	public boolean goTop() {
		try {
			eof = !rs.first();
			mapRowToObject();
		} catch (SQLException e) {
			eof = true;
		}
		
		return eof;
	}

	@Override
	public boolean eof() {
		return eof;
	}
	
	@Override
	public int recCount() {
		return registrosTotales;
	}

	@Override
	public int getRegistrosProcesados() {
		return registrosProcesados;
	}
	
	@Override
	public T scatterName() {
		if (bean == null) {
			mapRowToObject();
		}
		return bean;
	}
	
	private void mapRowToObject() {
		try {
			bean = (T) this.clazz.newInstance();

			if (rs != null && !eof) {
				for (Map.Entry<String, Field> entry : propertyMap.entrySet()) {
					Field field = entry.getValue();
					Object columnValue = rs.getObject(entry.getKey());
					
					if (field != null && columnValue != null) {
						objectManager.set(bean, field, columnValue);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<T> getListObject() {
		List<T> outputList = null;
		
		try {
			if (rs != null) {
				while (eof = !rs.next()) {
					T bean = (T) clazz.newInstance();

					for (Map.Entry<String, Field> entry : propertyMap.entrySet()) {
						Field field = entry.getValue();
						Object columnValue = rs.getObject(entry.getKey());
						
						if (field != null && columnValue != null) {
							objectManager.set(bean, field, columnValue);
						}
					}
						
					if (outputList == null) {
						outputList = new ArrayList<T>();
					}
					
					outputList.add(bean);
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return outputList;
	}
}