package ar.com.coninf.doconline.business.enums;


public enum TipoComprobanteAFIP {

	facturaA("1","Factura A","FA"),
	facturaB("6","Factura B","FB"),
	facturaC("11","Factura C","FC"),
	facturaCreditoA("201","Fac. de Credito A","FCA"),
	facturaCreditoB("206","Fac. de Credito B","FCB"),
	facturaCreditoC("211","Fac. de Credito C","FCC"),
	notaDebitoA("2","Nota de Debito A","NDA"),
	notaDebitoB("7","Nota de Debito B","NDB"),
	notaDebitoC("12","Nota de Debito C","NDC"),
	notaDebitoCreditoA("202","N. Debito de Credito A","NDCA"),
	notaDebitoCreditoB("207","N. Debito de Credito B","NDCB"),
	notaDebitoCreditoC("212","N. Debito de Credito C","NDCC"),
	notaCreditoA("3","Nota de Credito A","NCA"),
	notaCreditoB("8","Nota de Credito B","NCB"),
	notaCreditoC("13","Nota de Credito C","NCC"),
	notaCreditoCreditoA("203","N. Credito de Credito A","NCCA"),
	notaCreditoCreditoB("208","N. Credito de Credito B","NCCB"),
	notaCreditoCreditoC("213","N. Credito de Credito C","NCCC"),
	reciboA("4","Recibo A","RA"),
	reciboB("9","Recibo B","RB"),
	reciboC("15","Recibo C","RC"),
	notaVentaContadoA("5","N. V. Contado A","VA"),
	notaVentaContadoB("10","N. V. Contado B","VB"),
	notaVentaContadoC("16","N. V. Contado C","VC");
	
	private String cod;
	private String desc;
	private String pref;
	
	TipoComprobanteAFIP(String codigo, String descripcion, String prefijo){
		cod = codigo;
		desc = descripcion;
		pref = prefijo;
	}

	public String getCodigo() {
		return cod;
	}
	public void setCodigo(String cod) {
		this.cod = cod;
	}

	public String getDescripcion() {
		return desc;
	}
	public void setDescripcion(String desc) {
		this.desc = desc;
	}
	
	public String getPrefijo() {
		return pref;
	}
	public void setPrefijo(String pref) {
		this.pref = pref;
	}

	//Busqueda de Codigo por Descripcion
	public static String findCod4Desc(String ws) {
		for (TipoComprobanteAFIP t : values()) {
			if (t.getDescripcion().equals(ws)) {
				return t.getCodigo();
			}
		}
		
		return ws;
	}
	
	//Busqueda de Prefijo por Codigo
	public static String findPref4Cod(String ws) {
		for (TipoComprobanteAFIP t : values()) {
			if (t.getCodigo().equals(ws)) {
				return t.getPrefijo();
			}
		}
		
		return ws;
	}

}
