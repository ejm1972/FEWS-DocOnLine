USE FINN_EYSE
go
	
begin tran
	delete [AST_FEWS_LOG_DETALLE]   
	from [AST_FEWS_LOG] few, [AST_FEWS_LOG_DETALLE] iva 
	where few.[CUIT_EMPRESA] = iva.[CUIT_EMPRESA]
		and few.[TIPO_COMPROBANTE] = iva.[TIPO_COMPROBANTE]
		and few.[PUNTO_VENTA] = iva.[PUNTO_VENTA]
		and few.[NUMERO_COMPROBANTE] = iva.[NUMERO_COMPROBANTE]
		and (few.[RESULTADO] is null or few.[RESULTADO] not in ('A','D'))

	delete [AST_FEWS_LOG_PERMISO]   
	from [AST_FEWS_LOG] few, [AST_FEWS_LOG_PERMISO] iva 
	where few.[CUIT_EMPRESA] = iva.[CUIT_EMPRESA]
		and few.[TIPO_COMPROBANTE] = iva.[TIPO_COMPROBANTE]
		and few.[PUNTO_VENTA] = iva.[PUNTO_VENTA]
		and few.[NUMERO_COMPROBANTE] = iva.[NUMERO_COMPROBANTE]
		and (few.[RESULTADO] is null or few.[RESULTADO] not in ('A','D'))

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

	declare @Moneda varchar(3) = N'PES'
	declare @MonedaCtz varchar(20) = N'1.00'
	declare @ImpTotal varchar(20) = N'1000.00'
	declare @NetoNoGrav varchar(20) = N'1000.00'
	declare @NetoGrav varchar(20) = N'0.00'
	declare @NetoExen varchar(20) = N'0.00'
	declare @IvaTotal varchar(20) = N'0.00'
	declare @TribTotal varchar(20) = N'0.00'
	
	declare @PermExistente varchar(1) = N'S'
	declare @TipoExp varchar(1) = N'1'
	declare @CuitPais varchar(20) = N'50000000016'
	declare @Cliente varchar(200) = N'Joao Da Silva'
	declare @DomiCliente varchar(20) = N'Rua N°76 km 34.5 Alagoas'
	declare @DstCbt varchar(3) = N'235'
	declare @FormaPago varchar(20) = N'takataka'
	declare @IdImp varchar(1) = N'PJ54482221-l'
	declare @IdiomaCbt varchar(1) = N'1'
	declare @Incoterms varchar(3) = N'FOB'
	declare @IncotermsDs varchar(20) = N'Info complementaria'
	declare @ObsCom varchar(1000) = N'Observaciones comerciales'
	declare @ObsGral varchar(1000) = N'Observaciones generales'

	declare @IdPerm varchar(16) = N'99999AAXX999999A'
	declare @DstMer int = 225

begin tran	
	INSERT [dbo].[AST_FEWS_LOG] 
	([CUIT_EMPRESA], [TIPODOC_CLIENTE], [NRODOC_CLIENTE], [AS_ID], [TIPO_COMPROBANTE], [PUNTO_VENTA], [NUMERO_COMPROBANTE], [FECHA_COMPROBANTE], [MONEDA], [MONEDA_CTZ], [IMPORTE_TOTAL], [NETO_NOGRAVADO], [NETO_GRAVADO], [NETO_EXENTO], [IVA_TOTAL], [TRIBUTOS_TOTAL], [PERMISO_EXISTENTE], [TIPO_EXPORTACION], [CUIT_PAIS_CLIENTE], [CLIENTE], [DOMICILIO_CLIENTE], [DST_COMPROBANTE], [FORMA_PAGO], [ID_IMPOSITIVO], [IDIOMA_COMPROBANTE], [INCOTERMS], [INCOTERMS_DS], [OBS_COMERCIALES], [OBS_GENERALES]) VALUES 
	(@CuitEmpresa,   @TipoDoc,          @DocCliente,      @AsId,   @TipoCbteFC,        @PtoVtaFC,     @NroCbteFC,           @Fec,                @Moneda,   @MonedaCtz,  @ImpTotal,       @NetoNoGrav,      @NetoGrav,      @NetoExen,     @IvaTotal,   @TribTotal,       @PermExistente,      @TipoExp,           @CuitPais,           @Cliente,  @DomiCliente,        @DstCbt,           @FormaPago,   @IdImp,          @IdiomaCbt,           @Incoterms,  @IncotermsDs,   @ObsCom,           @ObsGral) 
	
	INSERT [dbo].[AST_FEWS_LOG_PERMISO] 
	([CUIT_EMPRESA], [AS_ID], [TIPO_COMPROBANTE], [PUNTO_VENTA], [NUMERO_COMPROBANTE], [ID_PERMISO], [DST_MERCADERIA]) VALUES 
	(@CuitEmpresa,   @AsId,   @TipoCbteFC,        @PtoVtaFC,     @NroCbteFC,           @IdPerm,      @DstMer)

	declare @Codigo varchar(30) = N'PRO1'
	declare @Descrip varchar(4000) = N'Producto Tipo 1 Exportacion MERCOSUR ISO 9001'
	declare @Cant varchar(20) = N'10'
	declare @UniMed smallint = 1
	declare @Precio varchar(20) = N'100.00'
	declare @ImpTotalDet varchar(20) = N'1000.00'
	declare @Bonif varchar(20) = N'0.00'

	INSERT [dbo].[AST_FEWS_LOG_DETALLE] 
	([CUIT_EMPRESA], [AS_ID], [TIPO_COMPROBANTE], [PUNTO_VENTA], [NUMERO_COMPROBANTE], [CODIGO], [DESCRIPCION], [CANTIDAD], [UNIDAD_MEDIDA], [PRECIO], [IMPORTE_TOTAL], [BONIFICACION]) VALUES 
	(@CuitEmpresa,   @AsId,   @TipoCbteFC,        @PtoVtaFC,     @NroCbteFC,           @Codigo,  @Descrip,      @Cant,      @UniMed,         @Precio,  @ImpTotalDet,    @Bonif)
commit tran

--	select @Id							= convert(int, (select isnull(max([AS_ID]),'0') id from [AST_FEWS_LOG]))
--	select @Id							= @Id + 1
--	select @AsId						= right('00000000'+convert(varchar(8), @Id),8)

--	declare @TipoCbteNC varchar(3)		= '21'
--	declare @PtoVtaNC varchar(5)		= N'00001'
--	declare @CbteNC int					= 1
--	declare @NroCbteNC varchar(8)		= right('00000000'+convert(varchar(8), @CbteNC),8)
	
--begin tran	
--	INSERT [dbo].[AST_FEWS_LOG] 
--	([CUIT_EMPRESA], [TIPODOC_CLIENTE], [NRODOC_CLIENTE], [AS_ID], [TIPO_COMPROBANTE], [PUNTO_VENTA], [NUMERO_COMPROBANTE], [FECHA_COMPROBANTE], [MONEDA], [MONEDA_CTZ], [IMPORTE_TOTAL], [NETO_NOGRAVADO], [NETO_GRAVADO], [NETO_EXENTO], [IVA_TOTAL], [TRIBUTOS_TOTAL], [PERMISO_EXISTENTE], [TIPO_EXPORTACION], [CUIT_PAIS_CLIENTE], [CLIENTE], [DOMICILIO_CLIENTE], [DST_COMPROBANTE], [FORMA_PAGO], [ID_IMPOSITIVO], [IDIOMA_COMPROBANTE], [INCOTERMS], [INCOTERMS_DS], [OBS_COMERCIALES], [OBS_GENERALES]) VALUES 
--	(@CuitEmpresa,   @TipoDoc,          @DocCliente,      @AsId,   @TipoCbteNC,        @PtoVtaNC,     @NroCbteNC,           @Fec,                N'PES',   N'1.00',      N'100.00',       N'0.00',          N'0.00',        N'0.00',       N'0.00',     N'0.00',          )

--	INSERT [dbo].[AST_FEWS_LOG_PERMISO] 
--	([CUIT_EMPRESA], [AS_ID], [TIPO_COMPROBANTE], [PUNTO_VENTA], [NUMERO_COMPROBANTE], [ID_PERMISO], [DST_MERCADERIA]) VALUES 
--	(@CuitEmpresa,   @AsId,   @TipoCbteNC,        @PtoVtaNC,     @NroCbteNC,           @,            @)

--	INSERT [dbo].[AST_FEWS_LOG_CBTE_ASOC]  
--	([AS_ID], [CUIT_EMPRESA], [TIPO_COMPROBANTE], [PUNTO_VENTA], [NUMERO_COMPROBANTE], [TIPO_COMPROBANTE_ASOC], [PUNTO_VENTA_ASOC], [NUMERO_COMPROBANTE_ASOC], [CUIT_ASOC] ) VALUES 
--	(@AsId,   @CuitEmpresa,   @TipoCbteNC,        @PtoVtaNC,     @NroCbteNC,           @TipoCbteFC,             @PtoVtaFC,          @NroCbteFC,                @CuitEmpresa)
--commit tran

go
