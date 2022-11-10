use fews
go

declare @Interfaz int
declare @IdPV bigint
declare @Nombre varchar(100)
declare @PuntoVenta varchar(50)
declare @Pass varchar(100)
declare @Cuit varchar(20)
declare @Param bigint
declare @Param_valor bigint
declare @Modo_Intrfz varchar(4)

select @Interfaz = 7001
select @Nombre = 'COMPULIDER'
select @Pass = 'D404559F602EAB6FD602AC7680DACBFAADD13630335E951F097AF3900E9DE176B6DB28512F2E000B9D04FBA5133E8B1C6E8DF59DB3A8AB9D60BE4B97CC9E81DB'
select @Cuit = '30517537620'
select @Modo_Intrfz = 'HOMO' --'HOMO' o 'PROD'

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
	VALUES(@Param_valor,@Param,@Modo_Intrfz,convert(datetime,'2000-01-01 00:00:00',120),convert(datetime,'9999-12-31 00:00:00',120))
end


select @PuntoVenta = '00011'
select @IdPV = null
select @IdPV = ID_PUNTO_VENTA from PUNTO_VENTA where PUNTO_VENTA=@PuntoVenta
select @IdPV, @PuntoVenta
if @IdPV is null
begin
	INSERT [PUNTO_VENTA]([ID_PUNTO_VENTA],[ID_INTERFAZ],[PUNTO_VENTA],[ACTIVADO],[FLG_CONTROL])
	VALUES(2,@Interfaz,@PuntoVenta,'S','N')
end

commit tran
