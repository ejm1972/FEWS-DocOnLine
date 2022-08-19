USE [fews]
GO

declare @Param bigint
declare @Param_valor bigint

begin tran

select @Param=ID_PARAMETRO from PARAMETROS where PARAMETRO='CANTIDAD_REINTENTOS_ERROR'
if @Param is null
begin
	select @Param = isnull(MAX(ID_PARAMETRO),0)+1 from PARAMETROS
	
	INSERT INTO [PARAMETROS]([ID_PARAMETRO],[PARAMETRO],[DESCRIPCION],[TXT_AYUDA])
	VALUES (@Param,'CANTIDAD_REINTENTOS_ERROR','Cantidad de Reintentos ante errores','Cantidad de Reintentos ante errores')
end

select @Param_valor = ID_PARAMETRO_VALOR from PARAMETROS_VALOR where ID_PARAMETRO=@Param
if @Param_valor is null
begin
	select @Param_valor = isnull(MAX(ID_PARAMETRO_VALOR),0)+1 from PARAMETROS_VALOR

	INSERT INTO [PARAMETROS_VALOR]([ID_PARAMETRO_VALOR],[ID_PARAMETRO],[VALOR],[F_VIGENCIA_DESDE],[F_VIGENCIA_HASTA])
	VALUES(@Param_valor,@Param,'10',convert(datetime,'2000-01-01 00:00:00',120),convert(datetime,'9999-12-31 00:00:00',120))
end

commit tran
