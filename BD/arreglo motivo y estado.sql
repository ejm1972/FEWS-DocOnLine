use FEWS_AJMF
go

if not exists(select * from sys.columns c, sys.objects o where o.name = 'interfaces' and c.object_id=o.object_id and c.name = 'cuit_suscripcion')
	ALTER TABLE interfaces 
	    ADD CUIT_SUSCRIPCION varchar(20) 

if not exists(select * from sys.columns c, sys.objects o where o.name = 'fews_encabezado' and c.object_id=o.object_id and c.name = 'estado_importacion')
	ALTER TABLE fews_encabezado 
	    ADD estado_importacion varchar(1000) 

if not exists(select * from sys.columns c, sys.objects o where o.name = 'fews_encabezado' and c.object_id=o.object_id and c.name = 'imp_iva')
	ALTER TABLE fews_encabezado 
		ADD imp_iva numeric(15,3)

if not 'bigint'=(select t.name from sys.columns c, sys.objects o, sys.types t where o.name = 'fews_encabezado' and c.object_id=o.object_id and c.system_type_id=t.system_type_id and c.user_type_id=t.user_type_id and c.name = 'cae')
	ALTER TABLE fews_encabezado 
		ALTER COLUMN cae bigint

if not 'varchar'=(select t.name from sys.columns c, sys.objects o, sys.types t where o.name = 'fews_encabezado' and c.object_id=o.object_id and c.system_type_id=t.system_type_id and c.user_type_id=t.user_type_id and c.name = 'motivo') or
not 1000=(select c.max_length from sys.columns c, sys.objects o, sys.types t where o.name = 'fews_encabezado' and c.object_id=o.object_id and c.system_type_id=t.system_type_id and c.user_type_id=t.user_type_id and c.name = 'motivo')
	ALTER TABLE fews_encabezado 
		ALTER COLUMN motivo varchar(1000)
go

begin tran
update fews_encabezado
set imp_iva = impto_liq + impto_liq_rni
commit tran

begin tran
update INTERFACES
set CUIT_SUSCRIPCION =
case
	when ID_INTERFAZ = 1001 then '30549220955'
	when ID_INTERFAZ = 1002 then '30710388314'
	when ID_INTERFAZ = 1003 then '30641803681'
	when ID_INTERFAZ = 1004 then '30696362242'
	when ID_INTERFAZ = 1 then '20225925055'
	when ID_INTERFAZ = 2 then '30710092792'
	when ID_INTERFAZ = 4 then '27000000001'
	when ID_INTERFAZ = 9901 then '20225925055'
	when ID_INTERFAZ = 9902 then '30710092792'
	when ID_INTERFAZ = 9904 then '27000000001'
end
commit tran

