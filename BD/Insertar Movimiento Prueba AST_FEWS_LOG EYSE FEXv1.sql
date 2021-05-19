USE FINN_EYSE
go
	
begin tran
	delete [AST_FEWS_LOG_PERIODO_ASOC]   
	from [AST_FEWS_LOG] few, [AST_FEWS_LOG_PERIODO_ASOC] iva 
	where few.[CUIT_EMPRESA] = iva.[CUIT_EMPRESA]
		and few.[TIPO_COMPROBANTE] = iva.[TIPO_COMPROBANTE]
		and few.[PUNTO_VENTA] = iva.[PUNTO_VENTA]
		and few.[NUMERO_COMPROBANTE] = iva.[NUMERO_COMPROBANTE]
		and (few.[RESULTADO] is null or few.[RESULTADO] not in ('A','D'))

	delete [AST_FEWS_LOG_CBTE_ASOC]   
	from [AST_FEWS_LOG] few, [AST_FEWS_LOG_CBTE_ASOC] iva 
	where few.[CUIT_EMPRESA] = iva.[CUIT_EMPRESA]
		and few.[TIPO_COMPROBANTE] = iva.[TIPO_COMPROBANTE]
		and few.[PUNTO_VENTA] = iva.[PUNTO_VENTA]
		and few.[NUMERO_COMPROBANTE] = iva.[NUMERO_COMPROBANTE]
		and (few.[RESULTADO] is null or few.[RESULTADO] not in ('A','D'))

	delete [AST_FEWS_LOG_DATOS_OPC]  
	from [AST_FEWS_LOG] few, [AST_FEWS_LOG_DATOS_OPC] iva 
	where few.[CUIT_EMPRESA] = iva.[CUIT_EMPRESA]
		and few.[TIPO_COMPROBANTE] = iva.[TIPO_COMPROBANTE]
		and few.[PUNTO_VENTA] = iva.[PUNTO_VENTA]
		and few.[NUMERO_COMPROBANTE] = iva.[NUMERO_COMPROBANTE]
		and (few.[RESULTADO] is null or few.[RESULTADO] not in ('A','D'))

	delete [AST_FEWS_LOG_IVA] 
	from [AST_FEWS_LOG] few, [AST_FEWS_LOG_IVA] iva 
	where few.[CUIT_EMPRESA] = iva.[CUIT_EMPRESA]
		and few.[TIPO_COMPROBANTE] = iva.[TIPO_COMPROBANTE]
		and few.[PUNTO_VENTA] = iva.[PUNTO_VENTA]
		and few.[NUMERO_COMPROBANTE] = iva.[NUMERO_COMPROBANTE]
		and (few.[RESULTADO] is null or few.[RESULTADO] not in ('A','D'))

	delete [AST_FEWS_LOG_TRIBUTOS]  
	from [AST_FEWS_LOG] few, [AST_FEWS_LOG_TRIBUTOS] tri 
	where few.[CUIT_EMPRESA] = tri.[CUIT_EMPRESA]
		and few.[TIPO_COMPROBANTE] = tri.[TIPO_COMPROBANTE]
		and few.[PUNTO_VENTA] = tri.[PUNTO_VENTA]
		and few.[NUMERO_COMPROBANTE] = tri.[NUMERO_COMPROBANTE]
		and (few.[RESULTADO] is null or few.[RESULTADO] not in ('A','D'))

	delete from [AST_FEWS_LOG]
	where [RESULTADO] is null or [RESULTADO] not in ('A','D')
commit tran

	declare @Id int						= convert(int, (select isnull(max([AS_ID]),'0') id from [AST_FEWS_LOG]))
	select @Id							= @Id + 1
	declare @AsId varchar(8)			= right('00000000'+convert(varchar(8), @Id),8)
	declare @Fec varchar(8)				= convert(varchar(8), GETDATE()-5, 112)
	declare @FecActual varchar(8)		= convert(varchar(8), GETDATE(), 112)
	declare @TipoDoc varchar(3)			= N'80'
	declare @DocCliente varchar(11)		= '30708074949' --'30708074949'
	
	declare @TipoCbteFC varchar(3)		= '19'
	declare @PtoVtaFC varchar(5)		= N'00001'
	declare @CbteFC int					= 1
	declare @NroCbteFC varchar(8)		= right('00000000'+convert(varchar(8), @CbteFC),8)

	declare @CuitEmpresa varchar(11)	= N'20225925055'

begin tran	
	INSERT [dbo].[AST_FEWS_LOG] 
	([CUIT_EMPRESA], [TIPODOC_CLIENTE], [NRODOC_CLIENTE], [AS_ID], [TIPO_COMPROBANTE], [PUNTO_VENTA], [NUMERO_COMPROBANTE], [FECHA_COMPROBANTE], [MONEDA], [MONEDA_CTZ], [IMPORTE_TOTAL], [NETO_NOGRAVADO], [NETO_GRAVADO], [NETO_EXENTO], [IVA_TOTAL], [TRIBUTOS_TOTAL], [PERMISO_EXISTENTE], [TIPO_EXPORTACION], [CUIT_PAIS_CLIENTE], [CLIENTE], [DOMICILIO_CLIENTE], [DST_COMPROBANTE], [FORMA_PAGO], [ID_IMPOSITIVO], [IDIOMA_COMPROBANTE], [INCOTERMS], [INCOTERMS_DS], [OBS_COMERCIALES], [OBS_GENERALES]) VALUES 
	(@CuitEmpresa,   @TipoDoc,          @DocCliente,      @AsId,   @TipoCbteFC,        @PtoVtaFC,     @NroCbteFC,           @Fec,                N'PES',   N'1.00',      N'1000.00',      N'0.00',          N'0.00',        N'0.00',       N'0.00',     N'0.00',          )

	INSERT [dbo].[AST_FEWS_LOG_PERMISO] 
	([CUIT_EMPRESA], [AS_ID], [TIPO_COMPROBANTE], [PUNTO_VENTA], [NUMERO_COMPROBANTE], [ID_PERMISO], [DST_MERCADERIA]) VALUES 
	(@CuitEmpresa,   @AsId,   @TipoCbteFC,        @PtoVtaFC,     @NroCbteFC,           @,            @)
commit tran

	select @Id							= convert(int, (select isnull(max([AS_ID]),'0') id from [AST_FEWS_LOG]))
	select @Id							= @Id + 1
	select @AsId						= right('00000000'+convert(varchar(8), @Id),8)

	declare @TipoCbteNC varchar(3)		= '21'
	declare @PtoVtaNC varchar(5)		= N'00001'
	declare @CbteNC int					= 1
	declare @NroCbteNC varchar(8)		= right('00000000'+convert(varchar(8), @CbteNC),8)
	
begin tran	
	INSERT [dbo].[AST_FEWS_LOG] 
	([CUIT_EMPRESA], [TIPODOC_CLIENTE], [NRODOC_CLIENTE], [AS_ID], [TIPO_COMPROBANTE], [PUNTO_VENTA], [NUMERO_COMPROBANTE], [FECHA_COMPROBANTE], [MONEDA], [MONEDA_CTZ], [IMPORTE_TOTAL], [NETO_NOGRAVADO], [NETO_GRAVADO], [NETO_EXENTO], [IVA_TOTAL], [TRIBUTOS_TOTAL], [PERMISO_EXISTENTE], [TIPO_EXPORTACION], [CUIT_PAIS_CLIENTE], [CLIENTE], [DOMICILIO_CLIENTE], [DST_COMPROBANTE], [FORMA_PAGO], [ID_IMPOSITIVO], [IDIOMA_COMPROBANTE], [INCOTERMS], [INCOTERMS_DS], [OBS_COMERCIALES], [OBS_GENERALES]) VALUES 
	(@CuitEmpresa,   @TipoDoc,          @DocCliente,      @AsId,   @TipoCbteNC,        @PtoVtaNC,     @NroCbteNC,           @Fec,                N'PES',   N'1.00',      N'100.00',       N'0.00',          N'0.00',        N'0.00',       N'0.00',     N'0.00',          )

	INSERT [dbo].[AST_FEWS_LOG_PERMISO] 
	([CUIT_EMPRESA], [AS_ID], [TIPO_COMPROBANTE], [PUNTO_VENTA], [NUMERO_COMPROBANTE], [ID_PERMISO], [DST_MERCADERIA]) VALUES 
	(@CuitEmpresa,   @AsId,   @TipoCbteNC,        @PtoVtaNC,     @NroCbteNC,           @,            @)

	INSERT [dbo].[AST_FEWS_LOG_CBTE_ASOC]  
	([AS_ID], [CUIT_EMPRESA], [TIPO_COMPROBANTE], [PUNTO_VENTA], [NUMERO_COMPROBANTE], [TIPO_COMPROBANTE_ASOC], [PUNTO_VENTA_ASOC], [NUMERO_COMPROBANTE_ASOC], [CUIT_ASOC] ) VALUES 
	(@AsId,   @CuitEmpresa,   @TipoCbteNC,        @PtoVtaNC,     @NroCbteNC,           @TipoCbteFC,             @PtoVtaFC,          @NroCbteFC,                @CuitEmpresa)
commit tran

go
