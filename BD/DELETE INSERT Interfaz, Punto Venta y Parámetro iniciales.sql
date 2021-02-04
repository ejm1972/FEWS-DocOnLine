use FEWS
go

declare @Interfaz int
declare @IdPV bigint
declare @Nombre varchar(100)
declare @PuntoVenta varchar(5)
declare @Pass varchar(100)
declare @Cuit varchar(20)
declare @Param bigint
declare @Param_valor bigint
declare @Modo_Intrfz varchar(4)

select @Interfaz = 5001
select @Nombre = 'Perway'
select @PuntoVenta = '00003'
select @Pass = 'D404559F602EAB6FD602AC7680DACBFAADD13630335E951F097AF3900E9DE176B6DB28512F2E000B9D04FBA5133E8B1C6E8DF59DB3A8AB9D60BE4B97CC9E81DB'
select @Cuit = '30679388726'
select @Modo_Intrfz = 'PROD' --o 'HOMO'

begin tran

if not exists(select ID_INTERFAZ from INTERFACES where ID_INTERFAZ=@Interfaz)
	INSERT INTO [INTERFACES]([ID_INTERFAZ],[INTERFAZ],[CLAVE],[ACTIVADO],[CANT_OPERACIONES],[CANT_ACUM_OPERACIONES],[ID_CANAL_ACCESO],[FLG_CONTROL],[CUIT_SUSCRIPCION])
	VALUES (@Interfaz,@Nombre,@Pass,'S',0,0,3,'N',@Cuit)

select @Param=ID_PARAMETRO from PARAMETROS where PARAMETRO='MODO_INTRFZ_'+CONVERT(varchar(4), @Interfaz)
if @Param is null
begin
	select @Param = isnull(MAX(ID_PARAMETRO),0)+1 from PARAMETROS
	
	INSERT INTO [PARAMETROS]([ID_PARAMETRO],[PARAMETRO],[DESCRIPCION],[TXT_AYUDA])
	VALUES (@Param,'MODO_INTRFZ_'+CONVERT(varchar(4), @Interfaz),'Modo para '+CONVERT(varchar(4), @Interfaz)+'-'+@Nombre,'Flag de Modo de Trabajo para Interfaz '+@Nombre+' [HOMO|PROD]')
end

select @Param_valor = ID_PARAMETRO_VALOR from PARAMETROS_VALOR where ID_PARAMETRO=@Param
if @Param_valor is null
begin
	select @Param_valor = isnull(MAX(ID_PARAMETRO_VALOR),0)+1 from PARAMETROS_VALOR

	INSERT INTO [PARAMETROS_VALOR]([ID_PARAMETRO_VALOR],[ID_PARAMETRO],[VALOR],[F_VIGENCIA_DESDE],[F_VIGENCIA_HASTA])
	VALUES(@Param_valor,@Param,@Modo_Intrfz,'2019-01-01 00:00:00.000','9999-09-09 00:00:00.000')
end


select @IdPV = ID_PUNTO_VENTA from PUNTO_VENTA where PUNTO_VENTA=@PuntoVenta
if @IdPV is null
begin
	select @IdPV = isnull(MAX(ID_PUNTO_VENTA),0)+1 from PUNTO_VENTA

	INSERT [PUNTO_VENTA]([ID_PUNTO_VENTA],[ID_INTERFAZ],[PUNTO_VENTA],[ACTIVADO],[FLG_CONTROL])
	VALUES(@IdPV,@Interfaz,@PuntoVenta,'S','N')
end

commit tran

--INSERT INTO [DocOnline].[dbo].[PARAMETROS] ([ID_PARAMETRO],[PARAMETRO],[DESCRIPCION],[TXT_AYUDA]) VALUES
--(2,'URL_WSAA_HOMO','WSAA para Homologación','Direccion webservice de autenticación para Homologación AFIP')
--INSERT INTO [DocOnline].[dbo].[PARAMETROS] ([ID_PARAMETRO],[PARAMETRO],[DESCRIPCION],[TXT_AYUDA]) VALUES
--(3,'URL_WSAA_PROD','WSAA para Producción','Dirección webservice de autenticación para Producción AFIP')
--INSERT INTO [DocOnline].[dbo].[PARAMETROS] ([ID_PARAMETRO],[PARAMETRO],[DESCRIPCION],[TXT_AYUDA]) VALUES
--(4,'URL_WSFEV1_HOMO','WSFEV1 para Homologación','Direccion webservice de Factura Electrónica para Homologación AFIP')
--INSERT INTO [DocOnline].[dbo].[PARAMETROS] ([ID_PARAMETRO],[PARAMETRO],[DESCRIPCION],[TXT_AYUDA]) VALUES
--(5,'URL_WSFEV1_PROD','WSFEV1 para Producción','Direccion webservice de Factura Electrónica para Producción AFIP')
INSERT INTO [DocOnline].[dbo].[PARAMETROS] ([ID_PARAMETRO],[PARAMETRO],[DESCRIPCION],[TXT_AYUDA]) VALUES
(6,'_PROXY_','Proxy de conexión','Dirección Proxy para conexión a Internet')
INSERT INTO [DocOnline].[dbo].[PARAMETROS] ([ID_PARAMETRO],[PARAMETRO],[DESCRIPCION],[TXT_AYUDA]) VALUES
(7,'MODO_INTRFZ_1001','Modo para 1001','Flag de Modo de Trabajo para Interfaz 1001 [HOMO|PROD]')
INSERT INTO [DocOnline].[dbo].[PARAMETROS] ([ID_PARAMETRO],[PARAMETRO],[DESCRIPCION],[TXT_AYUDA]) VALUES
(8,'MODO_INTRFZ_1002','Modo para 1002','Flag de Modo de Trabajo para Interfaz 1002 [HOMO|PROD]')
INSERT INTO [DocOnline].[dbo].[PARAMETROS] ([ID_PARAMETRO],[PARAMETRO],[DESCRIPCION],[TXT_AYUDA]) VALUES
(9,'MODO_INTRFZ_1','Modo para 1','Flag de Modo de Trabajo para Interfaz 1 [HOMO|PROD]')
GO
--INSERT INTO [DocOnline].[dbo].[PARAMETROS_VALOR] ([ID_PARAMETRO_VALOR],[ID_PARAMETRO],[VALOR],[F_VIGENCIA_DESDE],[F_VIGENCIA_HASTA],[ID_PROVINCIA]) VALUES
--(2,2,  'https://wsaahomo.afip.gov.ar/ws/services/LoginCms',      '2015-01-01 00:00:00.000','9999-09-09 00:00:00.000',0)
--INSERT INTO [DocOnline].[dbo].[PARAMETROS_VALOR] ([ID_PARAMETRO_VALOR],[ID_PARAMETRO],[VALOR],[F_VIGENCIA_DESDE],[F_VIGENCIA_HASTA],[ID_PROVINCIA]) VALUES
--(3,3,  'https://wsaa.afip.gov.ar/ws/services/LoginCms',          '2015-01-01 00:00:00.000','9999-09-09 00:00:00.000',0)
--INSERT INTO [DocOnline].[dbo].[PARAMETROS_VALOR] ([ID_PARAMETRO_VALOR],[ID_PARAMETRO],[VALOR],[F_VIGENCIA_DESDE],[F_VIGENCIA_HASTA],[ID_PROVINCIA]) VALUES
--(4,4,  'https://wswhomo.afip.gov.ar/wsfev1/service.asmx?WSDL',   '2015-01-01 00:00:00.000','9999-09-09 00:00:00.000',0)
--INSERT INTO [DocOnline].[dbo].[PARAMETROS_VALOR] ([ID_PARAMETRO_VALOR],[ID_PARAMETRO],[VALOR],[F_VIGENCIA_DESDE],[F_VIGENCIA_HASTA],[ID_PROVINCIA]) VALUES
--(5,5,  'https://servicios1.afip.gov.ar/wsfev1/service.asmx?WSDL','2015-01-01 00:00:00.000','9999-09-09 00:00:00.000',0)
INSERT INTO [DocOnline].[dbo].[PARAMETROS_VALOR] ([ID_PARAMETRO_VALOR],[ID_PARAMETRO],[VALOR],[F_VIGENCIA_DESDE],[F_VIGENCIA_HASTA],[ID_PROVINCIA]) VALUES
(6,6,  '','2015-01-01 00:00:00.000','9999-09-09 00:00:00.000',0)
INSERT INTO [DocOnline].[dbo].[PARAMETROS_VALOR] ([ID_PARAMETRO_VALOR],[ID_PARAMETRO],[VALOR],[F_VIGENCIA_DESDE],[F_VIGENCIA_HASTA],[ID_PROVINCIA]) VALUES
(7,7,  'PROD','2015-01-01 00:00:00.000','9999-09-09 00:00:00.000',0)
INSERT INTO [DocOnline].[dbo].[PARAMETROS_VALOR] ([ID_PARAMETRO_VALOR],[ID_PARAMETRO],[VALOR],[F_VIGENCIA_DESDE],[F_VIGENCIA_HASTA],[ID_PROVINCIA]) VALUES
(8,8,  'PROD','2015-01-01 00:00:00.000','9999-09-09 00:00:00.000',0)
INSERT INTO [DocOnline].[dbo].[PARAMETROS_VALOR] ([ID_PARAMETRO_VALOR],[ID_PARAMETRO],[VALOR],[F_VIGENCIA_DESDE],[F_VIGENCIA_HASTA],[ID_PROVINCIA]) VALUES
(9,9,  'HOMO','2015-01-01 00:00:00.000','9999-09-09 00:00:00.000',0)
GO