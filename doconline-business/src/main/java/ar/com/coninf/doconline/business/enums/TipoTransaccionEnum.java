package ar.com.coninf.doconline.business.enums;

public enum TipoTransaccionEnum {

	autorizarComprobante(1001L),
	consultarUltimoComprobante(1002L),
	consultarComprobante(1003L),
	generarQr(2001L),
	autorizarComprobanteExportacion(3001L)
	;
	
	private Long idTransaccion;
	
	TipoTransaccionEnum(Long idTx){
		idTransaccion = idTx;
	}
	public Long getIdTransaccion() {
		return idTransaccion;
	}
	
}
