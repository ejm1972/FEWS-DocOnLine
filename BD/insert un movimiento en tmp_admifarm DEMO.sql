USE [TMP_ADMIFARM]
GO

declare @NroCbte varchar(8)
declare @AsId varchar(8)
declare @Fec varchar(8)
select @NroCbte = right('00000000'+convert(varchar(8), 1),8)
select @AsId = right('00000000'+convert(varchar(8), 1),8)
select @Fec = convert(varchar(8), GETDATE()-10, 112)

begin tran
	INSERT [dbo].[AST_FEWS_LOG_IVA] 
	([AS_ID], [CUIT_EMPRESA], [TIPO_COMPROBANTE], [PUNTO_VENTA], [NUMERO_COMPROBANTE], [PORCENTAJE], [NETO_GRAVADO], [IMPORTE]) VALUES 
	(@AsId,   N'20225925055', N'01',              N'00004',       @NroCbte,            21.0000,      N'1000.00',     N'210.00')

	INSERT [dbo].[AST_FEWS_LOG] 
	([CUIT_EMPRESA], [TIPODOC_CLIENTE], [NRODOC_CLIENTE], [AS_ID], [TIPO_COMPROBANTE], [PUNTO_VENTA], [NUMERO_COMPROBANTE], [FECHA_COMPROBANTE], [CONCEPTO_FACTURA], [MONEDA], [MONEDA_CTZ], [IMPORTE_TOTAL], [NETO_NOGRAVADO], [NETO_GRAVADO], [NETO_EXENTO], [IVA_TOTAL], [TRIBUTOS_TOTAL], [CAE], [FECHA_VENCIMIENTO], [CODIGO], [DESCRIPCION], [OBSERVACION], [EXCEPCION_WSAA], [EXCEPCION_WSFEV1], [RESULTADO], [ERR_MSG], [OBS], [XML_REQUEST_AFIP], [XML_RESPONSE_AFIP]) VALUES 
	(N'20225925055', N'80',             N'30708074949',   @AsId,   N'01',              N'0001',       @NroCbte,             @Fec,                N'2',               N'PES',   N'1.00',      N'1210.00',      N'0.00',          N'1000.00',     N'0.00',       N'210.00',   N'0.00',          NULL,  NULL,                NULL,     NULL,          NULL,          NULL,             NULL,               NULL,        NULL,      NULL,  NULL,               NULL)
commit tran
go

if 1=2
begin
	declare @AsId varchar(8)
	select @AsId = right('00000000'+convert(varchar(8), 1),8)
	
	begin tran
		delete [AST_FEWS_LOG_IVA] 
		from [AST_FEWS_LOG] few, [AST_FEWS_LOG_IVA] iva 
		where few.[CUIT_EMPRESA] = iva.[CUIT_EMPRESA]
			and few.[TIPO_COMPROBANTE] = iva.[TIPO_COMPROBANTE]
			and few.[PUNTO_VENTA] = iva.[PUNTO_VENTA]
			and few.[NUMERO_COMPROBANTE] = iva.[NUMERO_COMPROBANTE]
			and few.[AS_ID] = @AsId

		delete [AST_FEWS_LOG_TRIBUTOS]  
		from [AST_FEWS_LOG] few, [AST_FEWS_LOG_TRIBUTOS] tri 
		where few.[CUIT_EMPRESA] = tri.[CUIT_EMPRESA]
			and few.[TIPO_COMPROBANTE] = tri.[TIPO_COMPROBANTE]
			and few.[PUNTO_VENTA] = tri.[PUNTO_VENTA]
			and few.[NUMERO_COMPROBANTE] = tri.[NUMERO_COMPROBANTE]
			and few.[AS_ID] = @AsId

		delete from [AST_FEWS_LOG]
		where [AS_ID] = @AsId
	commit tran
end
go


	declare @resultado varchar(1)
	declare @cae bigint
	declare @fecha_vencimiento varchar(8)
	declare @err_msg varchar(1000)
	declare @obs varchar(1000)
	declare @codigo varchar(10)
	declare @descripcion varchar(1000)
	declare @observacion varchar(1000)
	declare @excepcion_wsaa varchar(1000)
	declare @excepcion_wsfev1 varchar(1000)
	declare @xml_request_afip text
	declare @xml_response_afip text
	declare @cuit_empresa varchar(11)
	declare @tipo_comprobante varchar(3)
	declare @punto_venta varchar(4)
	declare @numero_comprobante varchar(8)
	
	select top 1 @resultado=fenc.resultado,
		@cae=fenc.cae,
		@fecha_vencimiento=fenc.fecha_vto,
		@err_msg=fenc.err_msg,
		@obs=fenc.motivo,
		@codigo=fxml.codigo,
		@descripcion=fxml.descripcion,
		@observacion=fxml.observacion,
		@excepcion_wsaa=fxml.excepcion_wsaa,
		@excepcion_wsfev1=fxml.excepcion_wsfev1,
		@xml_request_afip=fxml.xml_request,
		@xml_response_afip=fxml.xml_response,
		@cuit_empresa=cuit_empresa,
		@tipo_comprobante=tipo_comprobante,
		@punto_venta=punto_venta,
		@numero_comprobante=numero_comprobante
	from fews_encabezado fenc, fews_xml fxml, TMP_ADMIFARM.dbo.ast_fews_log astl--FINNEGANS.FINN_Admifarm.dbo.AST_FEWS_LOG
	where fenc.id=@id
		and fenc.id=fxml.id
		and cuit_empresa=fenc.cuit
		and tipo_comprobante=convert(varchar(3), fenc.tipo_cbte)
		and punto_venta=convert(varchar(4), fenc.punto_vta)
		and numero_comprobante=convert(varchar(8), fenc.cbte_nro)