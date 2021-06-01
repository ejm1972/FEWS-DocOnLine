USE [fews]
GO

if not exists(select top 1 * from sys.columns c, sys.objects o where o.name = N'FEWS_ENCABEZADO' and c.object_id=o.object_id and c.name = N'CUIT_PAIS_CLIENTE')
	ALTER TABLE [FEWS_ENCABEZADO]
    ADD [cuit_pais_cliente] [varchar](20) NULL
go

if not exists(select top 1 * from sys.columns c, sys.objects o where o.name = N'FEWS_AST_FEWS_LOG' and c.object_id=o.object_id and c.name = N'TIPO_EXPORTACION')
	ALTER TABLE [FEWS_AST_FEWS_LOG] 
	ADD [tipo_exportacion] [varchar](1) NULL
go

if not exists(select top 1 * from sys.columns c, sys.objects o where o.name = N'FEWS_AST_FEWS_LOG' and c.object_id=o.object_id and c.name = N'PERMISO_EXISTENTE')
	ALTER TABLE [FEWS_AST_FEWS_LOG] 
	ADD [permiso_existente] [varchar](1) NULL
go

if not exists(select top 1 * from sys.columns c, sys.objects o where o.name = N'FEWS_AST_FEWS_LOG' and c.object_id=o.object_id and c.name = N'DST_COMPROBANTE')
	ALTER TABLE [FEWS_AST_FEWS_LOG] 
	ADD [dst_comprobante] [varchar](3) NULL
go

if not exists(select top 1 * from sys.columns c, sys.objects o where o.name = N'FEWS_AST_FEWS_LOG' and c.object_id=o.object_id and c.name = N'CLIENTE')
	ALTER TABLE [FEWS_AST_FEWS_LOG] 
	ADD [cliente] [varchar](200) NULL
go

if not exists(select top 1 * from sys.columns c, sys.objects o where o.name = N'FEWS_AST_FEWS_LOG' and c.object_id=o.object_id and c.name = N'CUIT_PAIS_CLIENTE')
	ALTER TABLE [FEWS_AST_FEWS_LOG] 
	ADD [cuit_pais_cliente] [varchar](20) NULL
go

if not exists(select top 1 * from sys.columns c, sys.objects o where o.name = N'FEWS_AST_FEWS_LOG' and c.object_id=o.object_id and c.name = N'DOMICILIO_CLIENTE')
	ALTER TABLE [FEWS_AST_FEWS_LOG] 
	ADD [domicilio_cliente] [varchar](300) NULL
go

if not exists(select top 1 * from sys.columns c, sys.objects o where o.name = N'FEWS_AST_FEWS_LOG' and c.object_id=o.object_id and c.name = N'ID_IMPOSITIVO')
	ALTER TABLE [FEWS_AST_FEWS_LOG] 
	ADD [id_impositivo] [varchar](50) NULL
go

if not exists(select top 1 * from sys.columns c, sys.objects o where o.name = N'FEWS_AST_FEWS_LOG' and c.object_id=o.object_id and c.name = N'OBS_COMERCIALES')
	ALTER TABLE [FEWS_AST_FEWS_LOG] 
	ADD [obs_comerciales] [varchar](1000) NULL
go

if not exists(select top 1 * from sys.columns c, sys.objects o where o.name = N'FEWS_AST_FEWS_LOG' and c.object_id=o.object_id and c.name = N'OBS_GENERALES')
	ALTER TABLE [FEWS_AST_FEWS_LOG] 
	ADD [obs_generales] [varchar](1000) NULL
go

if not exists(select top 1 * from sys.columns c, sys.objects o where o.name = N'FEWS_AST_FEWS_LOG' and c.object_id=o.object_id and c.name = N'FORMA_PAGO')
	ALTER TABLE [FEWS_AST_FEWS_LOG] 
	ADD [forma_pago] [varchar](50) NULL
go

if not exists(select top 1 * from sys.columns c, sys.objects o where o.name = N'FEWS_AST_FEWS_LOG' and c.object_id=o.object_id and c.name = N'INCOTERMS')
	ALTER TABLE [FEWS_AST_FEWS_LOG] 
	ADD [incoterms] [varchar](3) NULL
go

if not exists(select top 1 * from sys.columns c, sys.objects o where o.name = N'FEWS_AST_FEWS_LOG' and c.object_id=o.object_id and c.name = N'INCOTERMS_DS')
	ALTER TABLE [FEWS_AST_FEWS_LOG] 
	ADD [incoterms_ds] [varchar](20) NULL
go

if not exists(select top 1 * from sys.columns c, sys.objects o where o.name = N'FEWS_AST_FEWS_LOG' and c.object_id=o.object_id and c.name = N'IDIOMA_COMPROBANTE')
	ALTER TABLE [FEWS_AST_FEWS_LOG] 
	ADD [idioma_comprobante] [varchar](1) NULL
go

if not exists(select top 1 * from sys.columns c, sys.objects o where o.name = N'FEWS_AST_FEWS_LOG' and c.object_id=o.object_id and c.name = N'EXCEPCION_WSFEXV1')
	ALTER TABLE [FEWS_AST_FEWS_LOG] 
	ADD [excepcion_wsfexv1] [varchar](1000) NULL
go

if not exists(select top 1 * from sys.columns c, sys.objects o where o.name = N'FEWS_XML' and c.object_id=o.object_id and c.name = N'EXCEPCION_WSFEXV1')
	ALTER TABLE [FEWS_XML]
	ADD [excepcion_wsfexv1] [varchar](1000) NULL
go

if not exists(select top 1 * from sys.columns c, sys.objects o where o.name = N'LOG_TRANSACCIONES' and c.object_id=o.object_id and c.name = N'EXCEPCION_WSFEXV1')
	ALTER TABLE [LOG_TRANSACCIONES]
	ADD EXCEPCION_WSFEXV1 [varchar](1000) NULL
go

begin tran

declare @Transac bigint
select @Transac = 3001
if not exists(select top 1 ID_TIPO_TRANSACCION from TIPOS_TRANSACCIONES where ID_TIPO_TRANSACCION=3001)
begin
	
	insert [TIPOS_TRANSACCIONES] ([ID_TIPO_TRANSACCION], [TRANSACCION], [TIPO_TRANSACCION], [TRANSACCION_REST], [URL_REST]) 
	values (3001, N'autorizarComprobanteExportacion', N'Autorizar Comprobante Exportacion', N'autorizarComprobanteExportacion', N'/autorizar/autorizarComprobante/exportacion')

end

select @Transac = 3002
if not exists(select top 1 ID_TIPO_TRANSACCION from TIPOS_TRANSACCIONES where ID_TIPO_TRANSACCION=2001)
begin
	
	insert [TIPOS_TRANSACCIONES] ([ID_TIPO_TRANSACCION], [TRANSACCION], [TIPO_TRANSACCION], [TRANSACCION_REST], [URL_REST]) 
	values (3002, N'consultarUltimoComprobanteExportacion', N'Consultar Ultimo Comprobante Exportacion', N'consultarUltimoComprobanteExportarcion', N'/consultar/ultimoComprobante/exportacion')

end

select @Transac = 3003
if not exists(select top 1 ID_TIPO_TRANSACCION from TIPOS_TRANSACCIONES where ID_TIPO_TRANSACCION=2001)
begin
	
	insert [TIPOS_TRANSACCIONES] ([ID_TIPO_TRANSACCION], [TRANSACCION], [TIPO_TRANSACCION], [TRANSACCION_REST], [URL_REST]) 
	values (3003, N'consultarComprobanteExportacion', N'Consultar Comprobante Exportacion', N'consultarComprobanteExportacion', N'/consultar/comprobante/exportacion')

end

commit tran