use FEWS
go

declare @Interfaz int
declare @Param bigint

select @Interfaz = 5001

begin tran

delete FROM [PUNTO_VENTA] where ID_INTERFAZ=@Interfaz

select @Param=ID_PARAMETRO from PARAMETROS where PARAMETRO='MODO_INTRZ_'+CONVERT(varchar(4), @Interfaz)
delete FROM [PARAMETROS_VALOR] where ID_PARAMETRO=@Param
delete FROM [PARAMETROS] where ID_PARAMETRO=@Param
  
delete FROM [INTERFACES] where ID_INTERFAZ=@Interfaz

commit tran