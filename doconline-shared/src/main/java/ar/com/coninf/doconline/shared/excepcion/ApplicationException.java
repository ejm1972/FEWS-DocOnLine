package ar.com.coninf.doconline.shared.excepcion;

import ar.com.coninf.doconline.rest.model.enums.ErrorEnum;
import ar.com.coninf.doconline.shared.dto.InterfazDto;


public class ApplicationException extends RuntimeException {

	private static final long serialVersionUID = 3619092359170709555L;
	private ErrorEnum error;
	private String key;
	private Object[] args;
	
	public ApplicationException(Throwable e){
		super(e);
	}


	public ApplicationException(String clave, Object[] args){
		key = clave;
		this.args = args;
	}

	public ApplicationException(ErrorEnum error, String clave, Object[] args){
		this.error = error;
		key = clave;
		this.args = args;
	}

	public ApplicationException(ErrorEnum error, String clave){
		this.error = error;
		key = clave;
	}

	public ApplicationException(ErrorEnum error, String clave, Object[] args, InterfazDto interfaz) {
		this.error = error;
		key = clave;
		this.args = args;
		//cli = cliente;
	}

	public String getKey() {
		return key;
	}

	public Object[] getArgs() {
		return args;
	}


	public ErrorEnum getError() {
		return error;
	}

//	public ClienteDto getCli() {
//		return cli;
//	}
}
