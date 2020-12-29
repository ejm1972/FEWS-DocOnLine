package ar.com.coninf.doconline.model.dao;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public interface ObjectManager {
	public String getClassName(Object obj);
	public <T> T invoke(Object object, String methodName, Object parameters[]) throws Exception;
	public <T> T invoke(Object object, String methodName) throws Exception;
	public void set(Object object, String fieldName, Object value) throws Exception;
	public void set(Object object, Field field, Object value) throws Exception;
	public <T> T get(Object object, Field field) throws Exception;
	public <T> T get(Object object, String fieldName) throws Exception;
	public <T> T cloneObject(Object object) throws Exception;
	public <T> T cloneObject(Object object, Field field[]) throws Exception;
	public Field[] getDeclaredFieldsWithInheritance(Object object);

	@SuppressWarnings({ "rawtypes" })
	public List<Method> getMeothdWithAnnotation(Object object, Class annotation);
	public <T> T createObject(String className) throws Exception;
	public <T> T createObject(String className, Object parameters[]) throws Exception;
} 