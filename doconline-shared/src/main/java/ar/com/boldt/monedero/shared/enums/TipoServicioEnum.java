package ar.com.boldt.monedero.shared.enums;

public enum TipoServicioEnum {

	PUBLICO(1, "ar.com.coninf.doconline.ws.impl.DocOnlineServicioWebPublicoImpl"),
	PRIVADO(2, "ar.com.coninf.doconline.ws.impl.DocOnlineServicioWebPrivadoImpl"),
	TODO(3, "*");
	
	private int cod;
	private String desc;
	
	TipoServicioEnum(int codigo, String descripcion){
		cod = codigo;
		desc = descripcion;
	}

	public int getCodigo() {
		return cod;
	}

	public void setCodigo(int cod) {
		this.cod = cod;
	}

	public String getDescripcion() {
		return desc;
	}

	public void setDescripcion(String desc) {
		this.desc = desc;
	}
	
	public static long find(String ws) {
		for (TipoServicioEnum t : values()) {
			if (t.getDescripcion().equals(ws)) {
				return t.getCodigo();
			}
		}
		
		return -1;
	}
}
