package ar.com.coninf.doconline.model.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.springframework.jdbc.core.RowMapper;

import ar.com.coninf.doconline.model.dao.ObjectManager;
import ar.com.coninf.doconline.model.dao.impl.ObjectManagerImpl;

public class GenericRowMapper<T> implements RowMapper<T> {
	private Class<T> clazz;
	private Map<String, Field> propertyMap;
	private Map<String, Field> transientMap;
	private ObjectManager objectManager;
	
	public GenericRowMapper(Class<T> clazz) {
		this.clazz = clazz;
		setData();
	}
	
	public T mapRow(ResultSet rs, int rowNum) throws SQLException {
		T bean = null;
		
		try {
			bean = (T) this.clazz.newInstance();

			if (rs != null) {
				for (Map.Entry<String, Field> entry : propertyMap.entrySet()) {
					Field field = entry.getValue();
					Object columnValue = null;
					
					try {
						columnValue = rs.getObject(entry.getKey());
					} catch (SQLException e) {
						if (transientMap.get(entry.getKey()) != null) {
							continue;
						}
						
						throw e;
					}
					
					if (field != null && columnValue != null) {
						String methodName = "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
						if(columnValue instanceof Blob){
							columnValue = convertirAByteArray((Blob)columnValue);
						}else if(columnValue instanceof Clob){
							columnValue = convertirACharArray((Clob)columnValue);
						}else if(columnValue instanceof Timestamp){
							columnValue = ((Timestamp)columnValue).clone();
						}
						if(columnValue != null){
							objectManager.invoke(bean, methodName, new Object[] {columnValue});
						}
					}
					
				}
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return bean;
	}

	private byte[] convertirAByteArray(Blob columnValue) throws SQLException, IOException {
		InputStream inStream = columnValue.getBinaryStream();
        int size = (int)columnValue.length();
        if(size == 0){
        	return null;
        }
        byte[] buffer = new byte[size];
        inStream.read(buffer);
        return buffer;
	}
	
	private char[] convertirACharArray(Clob columnValue) throws SQLException, IOException {
		Reader characterStream = columnValue.getCharacterStream();
        int size = (int)columnValue.length();
        if(size == 0){
        	return null;
        }
        char[] buffer = new char[size];
        characterStream.read(buffer);
        return buffer;
	}

	private void setData() {
        objectManager = new ObjectManagerImpl();
        propertyMap = new HashMap<String, Field>();
        transientMap = new HashMap<String, Field>();

        if (clazz != null) {
	    	if (clazz.isAnnotationPresent(Entity.class)) {
				for (Method method : clazz.getDeclaredMethods()) {
					if (method.isAnnotationPresent(Column.class)) {
						Column column = method.getAnnotation(Column.class);
						String fieldName = method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4); 
						Field fieldT = null;
						
						for (Field field : clazz.getDeclaredFields()) {
							if (field.getName().toString().equals(fieldName)) {
								propertyMap.put(column.name(), field);
								fieldT = field;
							}
						}
						
						if (method.isAnnotationPresent(Transient.class)) {
							transientMap.put(column.name(), fieldT);
						}
					}
				}
				
				if (propertyMap.isEmpty()) {
					for (Field field : clazz.getDeclaredFields()) {
						if (field.isAnnotationPresent(Column.class)) {
							Column column = field.getAnnotation(Column.class);
							propertyMap.put(column.name(), field);

							if (field.isAnnotationPresent(Transient.class)) {
								transientMap.put(column.name(), field);
							}
						}
					}
				}
			}
        }
	}
}
