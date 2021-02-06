USE [fews]
GO

begin tran

declare @Param bigint
declare @Param_valor bigint
declare @CodigoParam varchar(100)

select @Param = null
select @CodigoParam = '_TIMEOUT_'
select @Param=ID_PARAMETRO from PARAMETROS where PARAMETRO=@CodigoParam
if @Param is null
begin
	select @Param = isnull(MAX(ID_PARAMETRO),0)+1 from PARAMETROS
	
	INSERT INTO [PARAMETROS] ([ID_PARAMETRO],[PARAMETRO],[DESCRIPCION],[TXT_AYUDA])
	VALUES (@Param,@CodigoParam,'Tiempo de espera para AFIP en Segundos','Tiempo de espera para AFIP en Segundos')
end

select @Param_valor = null
select @Param_valor = ID_PARAMETRO_VALOR from PARAMETROS_VALOR where ID_PARAMETRO=@Param
if @Param_valor is null
begin
	select @Param_valor = isnull(MAX(ID_PARAMETRO_VALOR),0)+1 from PARAMETROS_VALOR

	INSERT INTO [PARAMETROS_VALOR]([ID_PARAMETRO_VALOR],[ID_PARAMETRO],[VALOR],[F_VIGENCIA_DESDE],[F_VIGENCIA_HASTA])
	VALUES(@Param_valor,@Param,'30','2021-01-01 00:00:00.000','9999-09-09 00:00:00.000')
end

select @Param = null
select @CodigoParam = '_USERDIR_'
select @Param=ID_PARAMETRO from PARAMETROS where PARAMETRO=@CodigoParam
if @Param is null
begin
	select @Param = isnull(MAX(ID_PARAMETRO),0)+1 from PARAMETROS
	
	INSERT INTO [PARAMETROS] ([ID_PARAMETRO],[PARAMETRO],[DESCRIPCION],[TXT_AYUDA])
	VALUES (@Param,@CodigoParam,'Carpeta de Usuario para fews','Carpeta de Usuario para fews')
end

select @Param_valor = null
select @Param_valor = ID_PARAMETRO_VALOR from PARAMETROS_VALOR where ID_PARAMETRO=@Param
if @Param_valor is null
begin
	select @Param_valor = isnull(MAX(ID_PARAMETRO_VALOR),0)+1 from PARAMETROS_VALOR

	INSERT INTO [PARAMETROS_VALOR]([ID_PARAMETRO_VALOR],[ID_PARAMETRO],[VALOR],[F_VIGENCIA_DESDE],[F_VIGENCIA_HASTA])
	VALUES(@Param_valor,@Param,'c:/coninf/fews/','2021-01-01 00:00:00.000','9999-09-09 00:00:00.000')
end

commit tran


