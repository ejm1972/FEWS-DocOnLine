use FEWS
go

if not exists(select * from sys.columns c, sys.objects o where o.name = 'log_transacciones' and c.object_id=o.object_id and c.name = 'comprobante')
	ALTER TABLE LOG_TRANSACCIONES
	    ADD COMPROBANTE varchar(30) 
go

update LOG_TRANSACCIONES
set COMPROBANTE = NUMERO_COMPROBANTE
where COMPROBANTE is null
go