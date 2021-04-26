USE FINN_ADMIFARM
go

if 1=1
begin
	declare @AsId varchar(8)
	
	begin tran
		select @AsId = right('00000000'+convert(varchar(8), 1),8)

		delete [AST_FEWS_LOG_CBTE_ASOC]   
		from [AST_FEWS_LOG] few, [AST_FEWS_LOG_CBTE_ASOC] iva 
		where few.[CUIT_EMPRESA] = iva.[CUIT_EMPRESA]
			and few.[TIPO_COMPROBANTE] = iva.[TIPO_COMPROBANTE]
			and few.[PUNTO_VENTA] = iva.[PUNTO_VENTA]
			and few.[NUMERO_COMPROBANTE] = iva.[NUMERO_COMPROBANTE]
			and few.[AS_ID] = @AsId

		delete [AST_FEWS_LOG_DATOS_OPC]  
		from [AST_FEWS_LOG] few, [AST_FEWS_LOG_DATOS_OPC] iva 
		where few.[CUIT_EMPRESA] = iva.[CUIT_EMPRESA]
			and few.[TIPO_COMPROBANTE] = iva.[TIPO_COMPROBANTE]
			and few.[PUNTO_VENTA] = iva.[PUNTO_VENTA]
			and few.[NUMERO_COMPROBANTE] = iva.[NUMERO_COMPROBANTE]
			and few.[AS_ID] = @AsId

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

		select @AsId = right('00000000'+convert(varchar(8), 2),8)

		delete [AST_FEWS_LOG_CBTE_ASOC]   
		from [AST_FEWS_LOG] few, [AST_FEWS_LOG_CBTE_ASOC] iva 
		where few.[CUIT_EMPRESA] = iva.[CUIT_EMPRESA]
			and few.[TIPO_COMPROBANTE] = iva.[TIPO_COMPROBANTE]
			and few.[PUNTO_VENTA] = iva.[PUNTO_VENTA]
			and few.[NUMERO_COMPROBANTE] = iva.[NUMERO_COMPROBANTE]
			and few.[AS_ID] = @AsId

		delete [AST_FEWS_LOG_DATOS_OPC]  
		from [AST_FEWS_LOG] few, [AST_FEWS_LOG_DATOS_OPC] iva 
		where few.[CUIT_EMPRESA] = iva.[CUIT_EMPRESA]
			and few.[TIPO_COMPROBANTE] = iva.[TIPO_COMPROBANTE]
			and few.[PUNTO_VENTA] = iva.[PUNTO_VENTA]
			and few.[NUMERO_COMPROBANTE] = iva.[NUMERO_COMPROBANTE]
			and few.[AS_ID] = @AsId

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

		select @AsId = right('00000000'+convert(varchar(8), 3),8)

		delete [AST_FEWS_LOG_CBTE_ASOC]   
		from [AST_FEWS_LOG] few, [AST_FEWS_LOG_CBTE_ASOC] iva 
		where few.[CUIT_EMPRESA] = iva.[CUIT_EMPRESA]
			and few.[TIPO_COMPROBANTE] = iva.[TIPO_COMPROBANTE]
			and few.[PUNTO_VENTA] = iva.[PUNTO_VENTA]
			and few.[NUMERO_COMPROBANTE] = iva.[NUMERO_COMPROBANTE]
			and few.[AS_ID] = @AsId

		delete [AST_FEWS_LOG_DATOS_OPC]  
		from [AST_FEWS_LOG] few, [AST_FEWS_LOG_DATOS_OPC] iva 
		where few.[CUIT_EMPRESA] = iva.[CUIT_EMPRESA]
			and few.[TIPO_COMPROBANTE] = iva.[TIPO_COMPROBANTE]
			and few.[PUNTO_VENTA] = iva.[PUNTO_VENTA]
			and few.[NUMERO_COMPROBANTE] = iva.[NUMERO_COMPROBANTE]
			and few.[AS_ID] = @AsId

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

if 1=1
begin
	declare @PtoVta varchar(5) = '00001'
	declare @Fec varchar(8) = convert(varchar(8), GETDATE()-5, 112)
	declare @FecActual varchar(8) = convert(varchar(8), GETDATE(), 112)
	declare @DocCliente varchar(11) = '30708074949' --'30708074949'
	declare @TipoCbte varchar(3)
	declare @NroCbte varchar(8)
	declare @AsId varchar(8)
	
	declare @NroCbte201 varchar(8) = right('00000000'+convert(varchar(8), 3),8)
	select @AsId = right('00000000'+convert(varchar(8), 1),8)
	select @TipoCbte = '201'
	begin tran
		INSERT [dbo].[AST_FEWS_LOG_IVA] 
		([AS_ID], [CUIT_EMPRESA], [TIPO_COMPROBANTE], [PUNTO_VENTA], [NUMERO_COMPROBANTE], [PORCENTAJE], [NETO_GRAVADO], [IMPORTE]) VALUES 
		(@AsId,   N'20225925055', @TipoCbte,          @PtoVta,      @NroCbte201,             21.0000,      N'1000.00',     N'210.00')

		INSERT [dbo].[AST_FEWS_LOG_DATOS_OPC]  
		([AS_ID], [CUIT_EMPRESA], [TIPO_COMPROBANTE], [PUNTO_VENTA], [NUMERO_COMPROBANTE], [ID_OPCIONAL], [VALOR]) VALUES 
		(@AsId,   N'20225925055', @TipoCbte,          @PtoVta,      @NroCbte201,             '2101',        N'1234567890123456789012')
		INSERT [dbo].[AST_FEWS_LOG_DATOS_OPC]  
		([AS_ID], [CUIT_EMPRESA], [TIPO_COMPROBANTE], [PUNTO_VENTA], [NUMERO_COMPROBANTE], [ID_OPCIONAL], [VALOR]) VALUES 
		(@AsId,   N'20225925055', @TipoCbte,          @PtoVta,      @NroCbte201,             '27',          N'ADC')

		INSERT [dbo].[AST_FEWS_LOG] 
		([CUIT_EMPRESA], [TIPODOC_CLIENTE], [NRODOC_CLIENTE], [AS_ID], [TIPO_COMPROBANTE], [PUNTO_VENTA], [NUMERO_COMPROBANTE], [FECHA_COMPROBANTE], [CONCEPTO_FACTURA], [MONEDA], [MONEDA_CTZ], [IMPORTE_TOTAL], [NETO_NOGRAVADO], [NETO_GRAVADO], [NETO_EXENTO], [IVA_TOTAL], [TRIBUTOS_TOTAL], [CAE], [FECHA_VENCIMIENTO], [CODIGO], [DESCRIPCION], [OBSERVACION], [EXCEPCION_WSAA], [EXCEPCION_WSFEV1], [RESULTADO], [ERR_MSG], [OBS], [XML_REQUEST_AFIP], [XML_RESPONSE_AFIP]) VALUES 
		(N'20225925055', N'80',             @DocCliente,      @AsId,   @TipoCbte,          @PtoVta,      @NroCbte201,             @Fec,                N'2',               N'PES',   N'1.00',      N'1210.00',      N'0.00',          N'1000.00',     N'0.00',       N'210.00',   N'0.00',          NULL,  NULL,                NULL,     NULL,          NULL,          NULL,             NULL,               NULL,        NULL,      NULL,  NULL,               NULL)
	commit tran

	declare @NroCbte202 varchar(8) = right('00000000'+convert(varchar(8), 2),8)
	select @AsId = right('00000000'+convert(varchar(8), 2),8)
	select @TipoCbte = '202'
	begin tran
		INSERT [dbo].[AST_FEWS_LOG_IVA] 
		([AS_ID], [CUIT_EMPRESA], [TIPO_COMPROBANTE], [PUNTO_VENTA], [NUMERO_COMPROBANTE], [PORCENTAJE], [NETO_GRAVADO], [IMPORTE]) VALUES 
		(@AsId,   N'20225925055', @TipoCbte,          @PtoVta,       @NroCbte202,             21.0000,      N'100.00',      N'21.00')

		INSERT [dbo].[AST_FEWS_LOG_CBTE_ASOC]  
		([AS_ID], [CUIT_EMPRESA], [TIPO_COMPROBANTE], [PUNTO_VENTA], [NUMERO_COMPROBANTE], [TIPO_COMPROBANTE_ASOC], [PUNTO_VENTA_ASOC], [NUMERO_COMPROBANTE_ASOC], [CUIT_ASOC],    [FECHA_COMPROBANTE_ASOC]) VALUES 
		(@AsId,   N'20225925055', @TipoCbte,          @PtoVta,       @NroCbte202,             N'201',                  @PtoVta,           @NroCbte201,               N'20225925055', @Fec)

		INSERT [dbo].[AST_FEWS_LOG_DATOS_OPC]  
		([AS_ID], [CUIT_EMPRESA], [TIPO_COMPROBANTE], [PUNTO_VENTA], [NUMERO_COMPROBANTE], [ID_OPCIONAL], [VALOR]) VALUES 
		(@AsId,   N'20225925055', @TipoCbte,          @PtoVta,       @NroCbte202,             '22',          N'N')

		INSERT [dbo].[AST_FEWS_LOG] 
		([CUIT_EMPRESA], [TIPODOC_CLIENTE], [NRODOC_CLIENTE], [AS_ID], [TIPO_COMPROBANTE], [PUNTO_VENTA], [NUMERO_COMPROBANTE], [FECHA_COMPROBANTE], [CONCEPTO_FACTURA], [MONEDA], [MONEDA_CTZ], [IMPORTE_TOTAL], [NETO_NOGRAVADO], [NETO_GRAVADO], [NETO_EXENTO], [IVA_TOTAL], [TRIBUTOS_TOTAL], [CAE], [FECHA_VENCIMIENTO], [CODIGO], [DESCRIPCION], [OBSERVACION], [EXCEPCION_WSAA], [EXCEPCION_WSFEV1], [RESULTADO], [ERR_MSG], [OBS], [XML_REQUEST_AFIP], [XML_RESPONSE_AFIP]) VALUES 
		(N'20225925055', N'80',             @DocCliente,      @AsId,   @TipoCbte,          @PtoVta,       @NroCbte202,             @Fec,                N'2',               N'PES',   N'1.00',      N'121.00',       N'0.00',          N'100.00',      N'0.00',       N'21.00',    N'0.00',          NULL,  NULL,                NULL,     NULL,          NULL,          NULL,             NULL,               NULL,        NULL,      NULL,  NULL,               NULL)
	commit tran

	declare @NroCbte203 varchar(8) = right('00000000'+convert(varchar(8), 2),8)
	select @AsId = right('00000000'+convert(varchar(8), 3),8)
	select @TipoCbte = '203'
	begin tran
		INSERT [dbo].[AST_FEWS_LOG_IVA] 
		([AS_ID], [CUIT_EMPRESA], [TIPO_COMPROBANTE], [PUNTO_VENTA], [NUMERO_COMPROBANTE], [PORCENTAJE], [NETO_GRAVADO], [IMPORTE]) VALUES 
		(@AsId,   N'20225925055', @TipoCbte,          @PtoVta,       @NroCbte203,             21.0000,      N'100.00',      N'21.00')

		INSERT [dbo].[AST_FEWS_LOG_CBTE_ASOC]  
		([AS_ID], [CUIT_EMPRESA], [TIPO_COMPROBANTE], [PUNTO_VENTA], [NUMERO_COMPROBANTE], [TIPO_COMPROBANTE_ASOC], [PUNTO_VENTA_ASOC], [NUMERO_COMPROBANTE_ASOC], [CUIT_ASOC],    [FECHA_COMPROBANTE_ASOC]) VALUES 
		(@AsId,   N'20225925055', @TipoCbte,          @PtoVta,       @NroCbte203,             N'201',                  @PtoVta,           @NroCbte201,               N'20225925055', @Fec)
		
		INSERT [dbo].[AST_FEWS_LOG_DATOS_OPC]  
		([AS_ID], [CUIT_EMPRESA], [TIPO_COMPROBANTE], [PUNTO_VENTA], [NUMERO_COMPROBANTE], [ID_OPCIONAL], [VALOR]) VALUES 
		(@AsId,   N'20225925055', @TipoCbte,          @PtoVta,       @NroCbte203,             '22',          N'N')

		INSERT [dbo].[AST_FEWS_LOG] 
		([CUIT_EMPRESA], [TIPODOC_CLIENTE], [NRODOC_CLIENTE], [AS_ID], [TIPO_COMPROBANTE], [PUNTO_VENTA], [NUMERO_COMPROBANTE], [FECHA_COMPROBANTE], [CONCEPTO_FACTURA], [MONEDA], [MONEDA_CTZ], [IMPORTE_TOTAL], [NETO_NOGRAVADO], [NETO_GRAVADO], [NETO_EXENTO], [IVA_TOTAL], [TRIBUTOS_TOTAL], [CAE], [FECHA_VENCIMIENTO], [CODIGO], [DESCRIPCION], [OBSERVACION], [EXCEPCION_WSAA], [EXCEPCION_WSFEV1], [RESULTADO], [ERR_MSG], [OBS], [XML_REQUEST_AFIP], [XML_RESPONSE_AFIP]) VALUES 
		(N'20225925055', N'80',             @DocCliente,      @AsId,   @TipoCbte,          @PtoVta,       @NroCbte203,             @Fec,                N'2',               N'PES',   N'1.00',      N'121.00',       N'0.00',          N'100.00',      N'0.00',       N'21.00',    N'0.00',          NULL,  NULL,                NULL,     NULL,          NULL,          NULL,             NULL,               NULL,        NULL,      NULL,  NULL,               NULL)
	commit tran
end
go