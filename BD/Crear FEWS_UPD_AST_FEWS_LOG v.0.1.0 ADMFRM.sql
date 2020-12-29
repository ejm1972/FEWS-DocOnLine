USE [FEWS_ADMFRM]
GO

/****** Object:  StoredProcedure [dbo].[FEWS_UPD_AST_FEWS_LOG]    Script Date: 08/22/2019 11:02:31 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[FEWS_UPD_AST_FEWS_LOG]') AND type in (N'P', N'PC'))
DROP PROCEDURE [dbo].[FEWS_UPD_AST_FEWS_LOG]
GO

USE [FEWS_ADMFRM]
GO

/****** Object:  StoredProcedure [dbo].[FEWS_UPD_AST_FEWS_LOG]    Script Date: 08/22/2019 11:02:31 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO


--exec FEWS_UPD_AST_FEWS_LOG 15820
CREATE PROCEDURE [dbo].[FEWS_UPD_AST_FEWS_LOG]
	@id int
AS
BEGIN 
SET NOCOUNT ON
	
insert into [FEWS_AST_FEWS_LOG] (
[TIPO],[CUIT_EMPRESA],[TIPODOC_CLIENTE],[NRODOC_CLIENTE],[AS_ID],[TIPO_COMPROBANTE],[PUNTO_VENTA],[NUMERO_COMPROBANTE],[FECHA_COMPROBANTE],[CONCEPTO_FACTURA],[MONEDA],  [MONEDA_CTZ],[IMPORTE_TOTAL],[NETO_NOGRAVADO],[NETO_GRAVADO],[NETO_EXENTO],[IVA_TOTAL],[TRIBUTOS_TOTAL],  [CAE],[FECHA_VENCIMIENTO],[CODIGO],[DESCRIPCION],[OBSERVACION],[EXCEPCION_WSAA],[EXCEPCION_WSFEV1],  [RESULTADO],  [ERR_MSG],[OBS],[XML_REQUEST_AFIP],[XML_RESPONSE_AFIP]) select
'IUD' ,[CUIT_EMPRESA],[TIPODOC_CLIENTE],[NRODOC_CLIENTE],[AS_ID],[TIPO_COMPROBANTE],[PUNTO_VENTA],[NUMERO_COMPROBANTE],[FECHA_COMPROBANTE],[CONCEPTO_FACTURA],[MONEDA],l.[MONEDA_CTZ],[IMPORTE_TOTAL],[NETO_NOGRAVADO],[NETO_GRAVADO],[NETO_EXENTO],[IVA_TOTAL],[TRIBUTOS_TOTAL],l.[CAE],[FECHA_VENCIMIENTO],[CODIGO],[DESCRIPCION],[OBSERVACION],[EXCEPCION_WSAA],[EXCEPCION_WSFEV1],l.[RESULTADO],l.[ERR_MSG],[OBS],[XML_REQUEST_AFIP],[XML_RESPONSE_AFIP]
from fews_encabezado fenc
	, [afgpm-finnegans].FINN_Admifarm.dbo.ast_fews_log l
where fenc.id=@id
	and cuit_empresa=fenc.cuit collate database_default
	and convert(int, tipo_comprobante)=fenc.tipo_cbte
	and convert(int, punto_venta)=fenc.punto_vta
	and convert(int, numero_comprobante)=fenc.cbte_nro

	update [afgpm-finnegans].FINN_Admifarm.dbo.AST_FEWS_LOG
	set  
		resultado=fenc.resultado,
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
		, [afgpm-finnegans].FINN_Admifarm.dbo.ast_fews_log astl
	where fenc.id=@id
		and fenc.id=fxml.id
		and cuit_empresa=fenc.cuit collate database_default
		and convert(int, tipo_comprobante)=fenc.tipo_cbte
		and convert(int, punto_venta)=fenc.punto_vta
		and convert(int, numero_comprobante)=fenc.cbte_nro
	
insert into [FEWS_AST_FEWS_LOG] (
[TIPO],[CUIT_EMPRESA],[TIPODOC_CLIENTE],[NRODOC_CLIENTE],[AS_ID],[TIPO_COMPROBANTE],[PUNTO_VENTA],[NUMERO_COMPROBANTE],[FECHA_COMPROBANTE],[CONCEPTO_FACTURA],[MONEDA],  [MONEDA_CTZ],[IMPORTE_TOTAL],[NETO_NOGRAVADO],[NETO_GRAVADO],[NETO_EXENTO],[IVA_TOTAL],[TRIBUTOS_TOTAL],  [CAE],[FECHA_VENCIMIENTO],[CODIGO],[DESCRIPCION],[OBSERVACION],[EXCEPCION_WSAA],[EXCEPCION_WSFEV1],  [RESULTADO],  [ERR_MSG],[OBS],[XML_REQUEST_AFIP],[XML_RESPONSE_AFIP]) select
'FUD' ,[CUIT_EMPRESA],[TIPODOC_CLIENTE],[NRODOC_CLIENTE],[AS_ID],[TIPO_COMPROBANTE],[PUNTO_VENTA],[NUMERO_COMPROBANTE],[FECHA_COMPROBANTE],[CONCEPTO_FACTURA],[MONEDA],l.[MONEDA_CTZ],[IMPORTE_TOTAL],[NETO_NOGRAVADO],[NETO_GRAVADO],[NETO_EXENTO],[IVA_TOTAL],[TRIBUTOS_TOTAL],l.[CAE],[FECHA_VENCIMIENTO],[CODIGO],[DESCRIPCION],[OBSERVACION],[EXCEPCION_WSAA],[EXCEPCION_WSFEV1],l.[RESULTADO],l.[ERR_MSG],[OBS],[XML_REQUEST_AFIP],[XML_RESPONSE_AFIP]
from fews_encabezado fenc
	, [afgpm-finnegans].FINN_Admifarm.dbo.ast_fews_log l
where fenc.id=@id
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


