use fews_vacia
go
begin tran
delete from FEWS_AST_FEWS_LOG
commit tran

begin tran
delete from FEWS_CMP_ASOC
delete from FEWS_DATO_OPC
delete from FEWS_DETALLE
delete from FEWS_IVA
delete from FEWS_PERIODO_ASOC
delete from FEWS_PERMISO
delete from FEWS_QR
delete from FEWS_TRIBUTO
delete from FEWS_XML
delete from FEWS_ENCABEZADO
commit tran

begin tran
delete from LOG_CONSOLA
delete from LOG_TIMER
delete from LOG_TRANSACCION_HIS
delete from REGISTRO_TRANSACCION_HIS
delete from LOGS_AUDITORIA
delete from LOGS_GENERICOS
delete from LOG_TRANSACCIONES
delete from REGISTROS_TRANSACCIONES
commit tran