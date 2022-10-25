use fews_vacia
go
begin tran
truncate table FEWS_AST_FEWS_LOG
commit tran

begin tran
truncate table FEWS_CMP_ASOC
truncate table FEWS_DATO_OPC
truncate table FEWS_DETALLE
truncate table FEWS_IVA
truncate table FEWS_PERIODO_ASOC
truncate table FEWS_PERMISO
truncate table FEWS_QR
truncate table FEWS_TRIBUTO
truncate table FEWS_XML
truncate table FEWS_ENCABEZADO
commit tran

begin tran
truncate table LOG_CONSOLA
truncate table LOG_TIMER
truncate table LOG_TRANSACCION_HIS
truncate table REGISTRO_TRANSACCION_HIS
truncate table LOGS_AUDITORIA
truncate table LOGS_GENERICOS
truncate table LOG_TRANSACCIONES
truncate table REGISTROS_TRANSACCIONES
commit tran