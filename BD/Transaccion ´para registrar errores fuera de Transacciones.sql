USE [fews]
GO

begin tran

declare @Transac bigint
select @Transac = 9999
if not exists(select top 1 ID_TIPO_TRANSACCION from TIPOS_TRANSACCIONES where ID_TIPO_TRANSACCION=9999)
begin
	
	insert [TIPOS_TRANSACCIONES] ([ID_TIPO_TRANSACCION], [TRANSACCION], [TIPO_TRANSACCION], [TRANSACCION_REST], [URL_REST]) 
	values (9999, N'logErroresSinTransacciones', N'logErroresSinTransacciones', N'logErroresSinTransacciones', N'logErroresSinTransacciones')

end

commit tran
