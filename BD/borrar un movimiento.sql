use FEWS
go

declare @movimiento varchar(20)
select @movimiento = 'FA000001'

select resultado, cae, fecha_vto, motivo, err_msg, * from FEWS_ENCABEZADO e where e.resultado is null and e.movimiento=@movimiento 

declare @borrar varchar(1)
select @borrar = 'N'

select @borrar = (case when resultado is null and cae is null and fecha_vto is null then 'S' else 'N' end) from FEWS_ENCABEZADO e where e.resultado is null and e.movimiento=@movimiento
select @borrar

if @borrar='S'
begin

begin tran

delete FEWS_CMP_ASOC from FEWS_CMP_ASOC i, FEWS_ENCABEZADO e where i.id = e.id and e.resultado is null and e.movimiento=@movimiento
delete FEWS_DETALLE from FEWS_DETALLE i, FEWS_ENCABEZADO e where i.id = e.id and e.resultado is null and e.movimiento=@movimiento
delete FEWS_IVA from FEWS_IVA i, FEWS_ENCABEZADO e where i.id = e.id and e.resultado is null and e.movimiento=@movimiento
delete FEWS_PERMISO from FEWS_PERMISO i, FEWS_ENCABEZADO e where i.id = e.id and e.resultado is null and e.movimiento=@movimiento
delete FEWS_TRIBUTO from FEWS_TRIBUTO i, FEWS_ENCABEZADO e where i.id = e.id and e.resultado is null and e.movimiento=@movimiento
delete FEWS_XML from FEWS_XML i, FEWS_ENCABEZADO e where i.id = e.id and e.resultado is null and e.movimiento=@movimiento
delete from FEWS_ENCABEZADO where resultado is null and movimiento=@movimiento

commit tran

end