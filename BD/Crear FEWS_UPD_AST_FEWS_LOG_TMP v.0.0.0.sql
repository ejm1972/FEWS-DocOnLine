drop procedure FEWS_UPD_AST_FEWS_LOG_TMP
go

CREATE PROCEDURE [FEWS_UPD_AST_FEWS_LOG_TMP]
	@id int
AS
BEGIN 
SET NOCOUNT ON
	
	
	update TMP_FINN.dbo.ast_fews_log --FINNEGANS.FINN_XXXX.dbo.AST_FEWS_LOG
	set resultado=fenc.resultado,
		cae=fenc.cae,
		fecha_vencimiento=fenc.fecha_vto,
		err_msg=fenc.err_msg,
		obs=fenc.motivo,
		codigo=fxml.codigo,
		descripcion=fxml.descripcion,
		observacion=fxml.observacion,
		excepcion_wsaa=fxml.excepcion_wsaa,
		excepcion_wsfev1=fxml.excepcion_wsfev1,
		xml_request_afip=fxml.xml_request,
		xml_response_afip=fxml.xml_response
	
	from fews_encabezado fenc, fews_xml fxml, TMP_FINN.dbo.ast_fews_log astl--FINNEGANS.FINN_XXXX.dbo.AST_FEWS_LOG

	where 1=1
		and fenc.id=@id
		and fenc.id=fxml.id
		and cuit_empresa=fenc.cuit
		and convert(int, tipo_comprobante)=fenc.tipo_cbte
		and convert(int, punto_venta)=fenc.punto_vta
		and convert(int, numero_comprobante)=fenc.cbte_nro
		
	--por ahora no se usa, se envia un default ...
	select 0 _codigo_, 'OK' _descripcion_, 'FINALIZADO' _observacion_

	--por ahora no se usa, se envia un default ...
	RETURN 0	
SET NOCOUNT OFF
END
GO

--exec FEWS_UPD_AST_FEWS_LOG_TMP 1

--select * from DRGFINNEGANS.FINN_ZBC.dbo.ast_fews_log where cuit_empresa='30711897581' and tipo_comprobante='01' and PUNTO_VENTA='0002' and numero_comprobante='00000003'

