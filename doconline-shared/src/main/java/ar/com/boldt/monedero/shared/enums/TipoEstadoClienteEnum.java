package ar.com.boldt.monedero.shared.enums;

public enum TipoEstadoClienteEnum {

	HABILITADO(0, "Habilitado"),
	INHABILITADO(1, "Inhabilitado"),
	ACTIVO(1, "Activo"),
	BLOQUEADO(2, "Bloqueado");
	
	private int cod;
	private String desc;
	
	TipoEstadoClienteEnum(int codigo, String descripcion){
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
		for (TipoEstadoClienteEnum t : values()) {
			if (t.getDescripcion().equals(ws)) {
				return t.getCodigo();
			}
		}
		
		return -1;
	}
}
