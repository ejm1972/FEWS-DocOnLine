/*
EYSE - Cuit 30-56850666-5
00007 FEX
00008 FE

DBA - Cuit 30-53784674-3
00005 FE
*/

use FEWS
go

declare @Interfaz int
declare @Nombre varchar(100)
declare @Pass varchar(100)
declare @Cuit varchar(20)
declare @Param bigint
declare @Param_valor bigint
declare @Modo_Intrfz varchar(4)

select @Interfaz = 4001
select @Nombre = 'EYSE'
select @Pass = 'D404559F602EAB6FD602AC7680DACBFAADD13630335E951F097AF3900E9DE176B6DB28512F2E000B9D04FBA5133E8B1C6E8DF59DB3A8AB9D60BE4B97CC9E81DB'
select @Cuit = '30568506665'
select @Modo_Intrfz = 'HOMO'

declare @IdPV bigint
declare @PuntoVenta varchar(5)

begin tran

if not exists(select ID_INTERFAZ from INTERFACES where ID_INTERFAZ=@Interfaz)
	INSERT INTO [INTERFACES]
           ([ID_INTERFAZ] 
           ,[INTERFAZ]
           ,[CLAVE]
           ,[ACTIVADO]
           ,[CANT_OPERACIONES]
           ,[CANT_ACUM_OPERACIONES]
           ,[ID_CANAL_ACCESO]
           ,[FLG_CONTROL]
           ,[CUIT_SUSCRIPCION])
    VALUES
           (@Interfaz
           ,@Nombre
           ,@Pass
           ,'S'
           ,0
           ,0
           ,3
           ,'N'
           ,@Cuit)

select @Param=ID_PARAMETRO from PARAMETROS where PARAMETRO='MODO_INTRFZ_'+CONVERT(varchar(4), @Interfaz)
if @Param is null
begin
	select @Param = isnull(MAX(ID_PARAMETRO),0)+1 from PARAMETROS
	
	INSERT INTO [PARAMETROS]
           ([ID_PARAMETRO]
           ,[PARAMETRO]
           ,[DESCRIPCION]
           ,[TXT_AYUDA])
     VALUES
           (@Param
           ,'MODO_INTRFZ_'+CONVERT(varchar(4), @Interfaz)
           ,'Modo para '+CONVERT(varchar(4), @Interfaz)+'-'+@Nombre
           ,'Flag de Modo de Trabajo para Interfaz '+@Nombre+' [HOMO|PROD]')
end

select @Param_valor = ID_PARAMETRO_VALOR from PARAMETROS_VALOR where ID_PARAMETRO=@Param
if @Param_valor is null
begin
	select @Param_valor = isnull(MAX(ID_PARAMETRO_VALOR),0)+1 from PARAMETROS_VALOR

	INSERT INTO [PARAMETROS_VALOR]
           ([ID_PARAMETRO_VALOR]
           ,[ID_PARAMETRO]
           ,[VALOR]
           ,[F_VIGENCIA_DESDE]
           ,[F_VIGENCIA_HASTA])
     VALUES
           (@Param_valor
           ,@Param
           ,@Modo_Intrfz
           ,'2019-01-01 00:00:00.000'
           ,'9999-09-09 00:00:00.000')
end


select @PuntoVenta = '00007'
select @IdPV = ID_PUNTO_VENTA from PUNTO_VENTA where PUNTO_VENTA=@PuntoVenta
if @IdPV is null
begin
	select @IdPV = isnull(MAX(ID_PUNTO_VENTA),0)+1 from PUNTO_VENTA

	INSERT [PUNTO_VENTA]
           ([ID_PUNTO_VENTA]
           ,[ID_INTERFAZ]
           ,[PUNTO_VENTA]
           ,[ACTIVADO]
           ,[FLG_CONTROL])
     VALUES
           (@IdPV
           ,@Interfaz
           ,@PuntoVenta
           ,'S'
           ,'N')
end

select @PuntoVenta = '00008'
select @IdPV = ID_PUNTO_VENTA from PUNTO_VENTA where PUNTO_VENTA=@PuntoVenta
if @IdPV is null
begin
	select @IdPV = isnull(MAX(ID_PUNTO_VENTA),0)+1 from PUNTO_VENTA

	INSERT [PUNTO_VENTA]
           ([ID_PUNTO_VENTA]
           ,[ID_INTERFAZ]
           ,[PUNTO_VENTA]
           ,[ACTIVADO]
           ,[FLG_CONTROL])
     VALUES
           (@IdPV
           ,@Interfaz
           ,@PuntoVenta
           ,'S'
           ,'N')
end

commit tran

select @Interfaz = 4002
select @Nombre = 'DBA'
select @Pass = 'D404559F602EAB6FD602AC7680DACBFAADD13630335E951F097AF3900E9DE176B6DB28512F2E000B9D04FBA5133E8B1C6E8DF59DB3A8AB9D60BE4B97CC9E81DB'
select @Cuit = '30537846743'
select @Modo_Intrfz = 'HOMO'

begin tran

if not exists(select ID_INTERFAZ from INTERFACES where ID_INTERFAZ=@Interfaz)
	INSERT INTO [INTERFACES]
           ([ID_INTERFAZ] 
           ,[INTERFAZ]
           ,[CLAVE]
           ,[ACTIVADO]
           ,[CANT_OPERACIONES]
           ,[CANT_ACUM_OPERACIONES]
           ,[ID_CANAL_ACCESO]
           ,[FLG_CONTROL]
           ,[CUIT_SUSCRIPCION])
    VALUES
           (@Interfaz
           ,@Nombre
           ,@Pass
           ,'S'
           ,0
           ,0
           ,3
           ,'N'
           ,@Cuit)

select @Param=ID_PARAMETRO from PARAMETROS where PARAMETRO='MODO_INTRFZ_'+CONVERT(varchar(4), @Interfaz)
if @Param is null
begin
	select @Param = isnull(MAX(ID_PARAMETRO),0)+1 from PARAMETROS
	
	INSERT INTO [PARAMETROS]
           ([ID_PARAMETRO]
           ,[PARAMETRO]
           ,[DESCRIPCION]
           ,[TXT_AYUDA])
     VALUES
           (@Param
           ,'MODO_INTRFZ_'+CONVERT(varchar(4), @Interfaz)
           ,'Modo para '+CONVERT(varchar(4), @Interfaz)+'-'+@Nombre
           ,'Flag de Modo de Trabajo para Interfaz '+@Nombre+' [HOMO|PROD]')
end

select @Param_valor = ID_PARAMETRO_VALOR from PARAMETROS_VALOR where ID_PARAMETRO=@Param
if @Param_valor is null
begin
	select @Param_valor = isnull(MAX(ID_PARAMETRO_VALOR),0)+1 from PARAMETROS_VALOR

	INSERT INTO [PARAMETROS_VALOR]
           ([ID_PARAMETRO_VALOR]
           ,[ID_PARAMETRO]
           ,[VALOR]
           ,[F_VIGENCIA_DESDE]
           ,[F_VIGENCIA_HASTA])
     VALUES
           (@Param_valor
           ,@Param
           ,@Modo_Intrfz
           ,'2019-01-01 00:00:00.000'
           ,'9999-09-09 00:00:00.000')
end


select @PuntoVenta = '00005'
select @IdPV = ID_PUNTO_VENTA from PUNTO_VENTA where PUNTO_VENTA=@PuntoVenta
if @IdPV is null
begin
	select @IdPV = isnull(MAX(ID_PUNTO_VENTA),0)+1 from PUNTO_VENTA

	INSERT [PUNTO_VENTA]
           ([ID_PUNTO_VENTA]
           ,[ID_INTERFAZ]
           ,[PUNTO_VENTA]
           ,[ACTIVADO]
           ,[FLG_CONTROL])
     VALUES
           (@IdPV
           ,@Interfaz
           ,@PuntoVenta
           ,'S'
           ,'N')
end

commit tran
