package ar.com.coninf.doconline.model.dao.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import ar.com.coninf.doconline.model.dao.ObjectManager;

public class ObjectManagerImpl implements ObjectManager {
	private final String SECURITY = "error de seguridad"; 
	private final String NO_SUCH_METHOD = "no existe el metodo"; 
	private final String ILLEGAL_ARGUMENT = "argumento ilegal"; 
	private final String ILLEGAL_ACCCES = "acceso ilegal";
	private final String INVOCATION_TARGET = "error en invocacion de destino";
	private final String CANT_SET_ON_STATIC_FIELD = "no se puede hacer set sobre propiedad estatica";
	private final String NO_SUCH_FIELD = "no existe la propiedad";
	private final String INITIATION = "clase no encontrada";
	private final String CLASS_NOT_FOUND = "error de instanciamiento";

	@Override
	public String getClassName(Object obj){
		return obj.getClass().getName();
	}
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> T invoke (Object object, String methodName, Object parameters[]) throws Exception{
		T retObj;
		
		Class cls = object.getClass();
		Class parameterType[] = new Class[parameters.length];
		for (Integer index=0;index<parameters.length;index++){
			parameterType[index] = parameters[index].getClass();
		}

		Method meth;
		try {
			meth = cls.getMethod(methodName, parameterType);
		} catch (SecurityException e) {
			throw new Exception(SECURITY+"("+methodName+")\r\n"+e.getMessage());
		} catch (NoSuchMethodException e) {
			throw new Exception(NO_SUCH_METHOD+"("+methodName+")\r\n"+e.getMessage());
		}
		
		
		try {
			retObj  = (T) meth.invoke(object, parameters);
		} catch (IllegalArgumentException e) {
			throw new Exception(ILLEGAL_ARGUMENT+"("+methodName+"\r\n"+e.getMessage());
		} catch (IllegalAccessException e) {
			throw new Exception(ILLEGAL_ACCCES+"("+methodName+"\r\n"+e.getMessage());
		} catch (InvocationTargetException e) {
			throw new Exception(INVOCATION_TARGET+"("+methodName+"\r\n"+e.getMessage());
		}
        return retObj;
		
		
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T invoke (Object object, String methodName) throws Exception{
		
		Object parameters[] = new Object[0];
		
		return (T) invoke( object, methodName, parameters);
		
	}
	

	@Override
	@SuppressWarnings("rawtypes")
	public void set(Object object, String fieldName, Object value) throws Exception{
		
		
		
		Class cls = object.getClass();
		
		
		Field field;
		try {
			field = cls.getDeclaredField(fieldName);
			if (Modifier.toString(field.getModifiers()).contains("static")){
				throw new Exception(CANT_SET_ON_STATIC_FIELD+"("+fieldName+")");
			}
		} catch (SecurityException e) {
			throw new Exception(SECURITY+"("+fieldName+")\r\n"+e.getMessage());
		} catch (NoSuchFieldException e) {
			throw new Exception(NO_SUCH_FIELD+"("+fieldName+")\r\n"+e.getMessage());
		}
		
		
		boolean isAccesible = field.isAccessible();
		field.setAccessible(true);
		try {
			field.set(object, value);
		} catch (IllegalArgumentException e) {
			throw new Exception(ILLEGAL_ARGUMENT+"("+fieldName+")\r\n"+e.getMessage());
		} catch (IllegalAccessException e) {
			throw new Exception(ILLEGAL_ACCCES+"("+fieldName+")\r\n"+e.getMessage());
		}
		field.setAccessible(isAccesible);
		
		
	}

	@Override
	public void set(Object object, Field field, Object value) throws Exception {

		if (Modifier.toString(field.getModifiers()).contains("static")){
			throw new Exception(CANT_SET_ON_STATIC_FIELD);
		}
		
		boolean isAccesible = field.isAccessible();
		field.setAccessible(true);
		try {
			field.set(object, value);
		} catch (IllegalArgumentException e) {
			throw new Exception(ILLEGAL_ARGUMENT+"("+field.getName()+")\r\n"+e.getMessage());
		} catch (IllegalAccessException e) {
			throw new Exception(ILLEGAL_ACCCES+"("+field.getName()+")\r\n"+e.getMessage());
		}
		field.setAccessible(isAccesible);
		
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T get(Object object, Field field) throws Exception{

		T retObj;
		boolean isAccesible = field.isAccessible();

		field.setAccessible(true);
		try {
			retObj = (T) field.get(object);
		} catch (IllegalArgumentException e) {
			throw new Exception(ILLEGAL_ARGUMENT+"("+field.getName()+")\r\n"+e.getMessage());
		} catch (IllegalAccessException e) {
			throw new Exception(ILLEGAL_ACCCES+"("+field.getName()+")\r\n"+e.getMessage());
		}

		field.setAccessible(isAccesible);
		
		return retObj;
	}
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> T get(Object object, String fieldName) throws Exception{
	
		T retObj;
		Class cls = object.getClass();
		
		Field field;
		try {
			field = cls.getDeclaredField(fieldName);
		} catch (SecurityException e) {
			throw new Exception(SECURITY+"("+fieldName+")\r\n"+e.getMessage());
		} catch (NoSuchFieldException e) {
			throw new Exception(NO_SUCH_FIELD+"("+fieldName+")\r\n"+e.getMessage());
		}
		
		boolean isAccesible = field.isAccessible();
		field.setAccessible(true);
		try {
			retObj = (T) field.get(object);
		} catch (IllegalArgumentException e) {
			throw new Exception(ILLEGAL_ARGUMENT+"("+fieldName+")\r\n"+e.getMessage());
		} catch (IllegalAccessException e) {
			throw new Exception(ILLEGAL_ACCCES+"("+fieldName+")\r\n"+e.getMessage());
		}

		field.setAccessible(isAccesible);
        return retObj;
	
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> T cloneObject(Object object) throws Exception{
		
		T returnObject = (T) createObject(object.getClass().getName());

		Field field[] = object.getClass().getDeclaredFields();
	
		for (Integer index=0;index<field.length;index++){
			set(returnObject, field[index], get(object, field[index]));
		}
		
		return returnObject;
		
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T cloneObject(Object object, Field field[])  throws Exception{
		
		T returnObject = (T) createObject(object.getClass().getName());

		for (Integer index=0;index<field.length;index++){
			set(returnObject, field[index], get(object, field[index]));
		}
		
		return returnObject;
		
	}
	
	
	@Override
	@SuppressWarnings("rawtypes")
	public Field[] getDeclaredFieldsWithInheritance(Object object){

		List <Field> fieldToReturn = new ArrayList<Field>();
		
		
		Class cls = object.getClass();
		
		
		while (cls != null) {
			// ////lee fields annotations
			Field fieldList[] = cls.getDeclaredFields();
			for (int index = 0; index < fieldList.length; index++) {
				fieldToReturn.add(fieldList[index]);
			}
			cls = cls.getSuperclass();
		}
		
		Field fieldArray[] = new Field[fieldToReturn.size()];
		
		return fieldToReturn.toArray(fieldArray);
		
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Method> getMeothdWithAnnotation(Object object, Class annotation){
		
		List<Method> methodWithAnnotation = new ArrayList<Method>();
		
		
		Class cls = object.getClass();
			
		Method methodList[] = cls.getDeclaredMethods();
		
		for (int index=0;index<methodList.length; index++){
			
			Annotation annotationMethod = (Annotation) methodList[index].getAnnotation(annotation);
			
			if (annotationMethod!=null){
				methodWithAnnotation.add(methodList[index]);
			}
		}
		
		return methodWithAnnotation;
		
		
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T createObject(String className) throws Exception{
		
		Object parameters[] = new Object[0];
		return (T) createObject (className, parameters);
		
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> T createObject(String className, Object parameters[]) throws Exception{
		
		Class cls ;
		Constructor ct;
		
		try {
				
				cls = Class.forName(className);
			
	           Class partypes[] = new Class[parameters.length];
	           Object arglist[] = new Object[parameters.length];

	           for (Integer index = 0;index<parameters.length;index++){
	        	   Object obj = parameters[index];
	        	   partypes[index] = obj.getClass();
	        	   arglist[index] = obj;
	           }

	           ct = cls.getConstructor(partypes);
	           return (T) ct.newInstance(arglist);
	           
		} catch (IllegalArgumentException e) {
			throw new Exception(ILLEGAL_ARGUMENT+"("+className+")\r\n"+e.getMessage());
		} catch (IllegalAccessException e) {
			throw new Exception(ILLEGAL_ACCCES+"("+className+")\r\n"+e.getMessage());
		} catch (ClassNotFoundException e) {
			throw new Exception(CLASS_NOT_FOUND+"("+className+")("+className+")\r\n"+e.getMessage());
		} catch (SecurityException e) {
			throw new Exception(SECURITY+"("+className+")\r\n"+e.getMessage());
		} catch (NoSuchMethodException e) {
			throw new Exception(NO_SUCH_METHOD+"("+className+")\r\n"+e.getMessage());
		} catch (InstantiationException e) {
			throw new Exception(INITIATION+"("+className+")\r\n"+e.getMessage());
		} catch (InvocationTargetException e) {
			throw new Exception(INVOCATION_TARGET+"("+className+")\r\n"+e.getMessage());
		}
		
	}
}
