USE [fews]
GO

/****** Object:  StoredProcedure [dbo].[FEWS_UPD_AST_FEWS_LOG_HIDROTERMIA]    Script Date: 12/05/2018 13:04:30 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[FEWS_UPD_AST_FEWS_LOG_HIDROTERMIA]') AND type in (N'P', N'PC'))
DROP PROCEDURE [dbo].[FEWS_UPD_AST_FEWS_LOG_HIDROTERMIA]
GO

USE [fews]
GO

/****** Object:  StoredProcedure [dbo].[FEWS_UPD_AST_FEWS_LOG_HIDROTERMIA]    Script Date: 12/05/2018 13:04:30 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [FEWS_UPD_AST_FEWS_LOG_HIDROTERMIA]
	@id int
AS
BEGIN 
SET NOCOUNT ON
	
	update [LQ5\LQ5].FAF_HIDROTERMIA.dbo.AST_FEWS_LOG
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
	
	from fews_encabezado fenc
		, fews_xml fxml
		, [LQ5\LQ5].FAF_HIDROTERMIA.dbo.ast_fews_log astl

	where 1=1
		and fenc.id=@id
		and fenc.id=fxml.id
		and cuit_empresa=fenc.cuit collate database_default
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

--exec FEWS_UPD_AST_FEWS_LOG_HIDROTERMIA 1

--select * from [LQ5\LQ5].FAF_HIDROTERMIA.dbo.ast_fews_log where cuit_empresa='30711897581' and tipo_comprobante='01' and PUNTO_VENTA='0002' and numero_comprobante='00000003'

