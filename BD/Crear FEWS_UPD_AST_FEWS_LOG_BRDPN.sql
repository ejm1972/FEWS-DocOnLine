USE [fews]
GO

/****** Object:  StoredProcedure [dbo].[FEWS_UPD_AST_FEWS_LOG_BRDPN]    Script Date: 12/05/2018 13:04:30 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[FEWS_UPD_AST_FEWS_LOG_BRDPN]') AND type in (N'P', N'PC'))
DROP PROCEDURE [dbo].[FEWS_UPD_AST_FEWS_LOG_BRDPN]
GO

USE [fews]
GO

/****** Object:  StoredProcedure [dbo].[FEWS_UPD_AST_FEWS_LOG_BRDPN]    Script Date: 12/05/2018 13:04:30 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [FEWS_UPD_AST_FEWS_LOG_BRDPN]
	@id int
AS
BEGIN 
SET NOCOUNT ON

declare @intento datetime = getdate()

insert into [FEWS_AST_FEWS_LOG] (
[TIPO],[CUIT_EMPRESA],[TIPODOC_CLIENTE],[NRODOC_CLIENTE],[AS_ID],[TIPO_COMPROBANTE],[PUNTO_VENTA],[NUMERO_COMPROBANTE],[FECHA_COMPROBANTE],[CONCEPTO_FACTURA],[MONEDA],  [MONEDA_CTZ]
	,[IMPORTE_TOTAL],[NETO_NOGRAVADO],[NETO_GRAVADO],[NETO_EXENTO],[IVA_TOTAL],[TRIBUTOS_TOTAL]
	,[TIPO_EXPORTACION],  [PERMISO_EXISTENTE],[DST_COMPROBANTE],[CLIENTE],  [CUIT_PAIS_CLIENTE],  [DOMICILIO_CLIENTE],  [ID_IMPOSITIVO],  [OBS_COMERCIALES],  [OBS_GENERALES],  [FORMA_PAGO],  [INCOTERMS],  [INCOTERMS_DS],[IDIOMA_COMPROBANTE]
	,  [CAE],[FECHA_VENCIMIENTO],[CODIGO],[DESCRIPCION],[OBSERVACION],[EXCEPCION_WSAA],[EXCEPCION_WSFEV1],[EXCEPCION_WSFEXV1],  [RESULTADO],  [ERR_MSG],[OBS],[XML_REQUEST_AFIP],[XML_RESPONSE_AFIP],[INTENTO]) select
'IUD' ,[CUIT_EMPRESA],[TIPODOC_CLIENTE],[NRODOC_CLIENTE],[AS_ID],[TIPO_COMPROBANTE],[PUNTO_VENTA],[NUMERO_COMPROBANTE],[FECHA_COMPROBANTE],[CONCEPTO_FACTURA],[MONEDA],l.[MONEDA_CTZ]
	,[IMPORTE_TOTAL],[NETO_NOGRAVADO],[NETO_GRAVADO],[NETO_EXENTO],[IVA_TOTAL],[TRIBUTOS_TOTAL]
	,[TIPO_EXPORTACION],l.[PERMISO_EXISTENTE],[DST_COMPROBANTE],[CLIENTE],l.[CUIT_PAIS_CLIENTE],l.[DOMICILIO_CLIENTE],l.[ID_IMPOSITIVO],l.[OBS_COMERCIALES],l.[OBS_GENERALES],l.[FORMA_PAGO],l.[INCOTERMS],l.[INCOTERMS_DS],[IDIOMA_COMPROBANTE]
	,l.[CAE],[FECHA_VENCIMIENTO],[CODIGO],[DESCRIPCION],[OBSERVACION],[EXCEPCION_WSAA],[EXCEPCION_WSFEV1],[EXCEPCION_WSFEXV1],l.[RESULTADO],l.[ERR_MSG],[OBS],[XML_REQUEST_AFIP],[XML_RESPONSE_AFIP],@intento
from fews_encabezado fenc
	, BRDPN.FINN_BRD.dbo.ast_fews_log l
where fenc.id=@id
	and cuit_empresa=fenc.cuit collate database_default
	and convert(int, tipo_comprobante)=fenc.tipo_cbte
	and convert(int, punto_venta)=fenc.punto_vta
	and convert(int, numero_comprobante)=fenc.cbte_nro
	
	update BRDPN.FINN_BRD.dbo.AST_FEWS_LOG
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

		,qr=fqr.imagen_qr

	from fews_encabezado fenc
		, fews_xml fxml
		, fews_qr fqr
		, BRDPN.FINN_BRD.dbo.ast_fews_log astl

	where 1=1
		and fenc.id=@id
		and fenc.id=fxml.id
		and fenc.id=fqr.id
		and cuit_empresa=fenc.cuit collate database_default
		and convert(int, tipo_comprobante)=fenc.tipo_cbte
		and convert(int, punto_venta)=fenc.punto_vta
		and convert(int, numero_comprobante)=fenc.cbte_nro
	
insert into [FEWS_AST_FEWS_LOG] (
[TIPO],[CUIT_EMPRESA],[TIPODOC_CLIENTE],[NRODOC_CLIENTE],[AS_ID],[TIPO_COMPROBANTE],[PUNTO_VENTA],[NUMERO_COMPROBANTE],[FECHA_COMPROBANTE],[CONCEPTO_FACTURA],[MONEDA],  [MONEDA_CTZ]
	,[IMPORTE_TOTAL],[NETO_NOGRAVADO],[NETO_GRAVADO],[NETO_EXENTO],[IVA_TOTAL],[TRIBUTOS_TOTAL]
	,[TIPO_EXPORTACION],  [PERMISO_EXISTENTE],[DST_COMPROBANTE],[CLIENTE],  [CUIT_PAIS_CLIENTE],  [DOMICILIO_CLIENTE],  [ID_IMPOSITIVO],  [OBS_COMERCIALES],  [OBS_GENERALES],  [FORMA_PAGO],  [INCOTERMS],  [INCOTERMS_DS],[IDIOMA_COMPROBANTE]
	,  [CAE],[FECHA_VENCIMIENTO],[CODIGO],[DESCRIPCION],[OBSERVACION],[EXCEPCION_WSAA],[EXCEPCION_WSFEV1],[EXCEPCION_WSFEXV1],  [RESULTADO],  [ERR_MSG],[OBS],[XML_REQUEST_AFIP],[XML_RESPONSE_AFIP],[INTENTO]) select
'FUD' ,[CUIT_EMPRESA],[TIPODOC_CLIENTE],[NRODOC_CLIENTE],[AS_ID],[TIPO_COMPROBANTE],[PUNTO_VENTA],[NUMERO_COMPROBANTE],[FECHA_COMPROBANTE],[CONCEPTO_FACTURA],[MONEDA],l.[MONEDA_CTZ]
	,[IMPORTE_TOTAL],[NETO_NOGRAVADO],[NETO_GRAVADO],[NETO_EXENTO],[IVA_TOTAL],[TRIBUTOS_TOTAL]
	,[TIPO_EXPORTACION],l.[PERMISO_EXISTENTE],[DST_COMPROBANTE],[CLIENTE],l.[CUIT_PAIS_CLIENTE],l.[DOMICILIO_CLIENTE],l.[ID_IMPOSITIVO],l.[OBS_COMERCIALES],l.[OBS_GENERALES],l.[FORMA_PAGO],l.[INCOTERMS],l.[INCOTERMS_DS],[IDIOMA_COMPROBANTE]
	,l.[CAE],[FECHA_VENCIMIENTO],[CODIGO],[DESCRIPCION],[OBSERVACION],[EXCEPCION_WSAA],[EXCEPCION_WSFEV1],[EXCEPCION_WSFEXV1],l.[RESULTADO],l.[ERR_MSG],[OBS],[XML_REQUEST_AFIP],[XML_RESPONSE_AFIP],@intento
from fews_encabezado fenc
	, BRDPN.FINN_BRD.dbo.ast_fews_log l
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

--exec FEWS_UPD_AST_FEWS_LOG_BRDPN 1

--select * from BRDPN.FINN_BRD.dbo.ast_fews_log where cuit_empresa='30711897581' and tipo_comprobante='01' and PUNTO_VENTA='0002' and numero_comprobante='00000003'

