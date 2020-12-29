use FEWS
go

declare @movimiento varchar(20)
select @movimiento = '09901001000400000001'

select * from FEWS_ENCABEZADO where resultado is null and movimiento=@movimiento

select * from FEWS_IVA i, FEWS_ENCABEZADO e where i.id = e.id and e.resultado is null and e.movimiento=@movimiento

select * from FEWS_TRIBUTO i, FEWS_ENCABEZADO e where i.id = e.id and e.resultado is null and e.movimiento=@movimiento

select * from FEWS_DETALLE i, FEWS_ENCABEZADO e where i.id = e.id and e.resultado is null and e.movimiento=@movimiento

select * from FEWS_CMP_ASOC i, FEWS_ENCABEZADO e where i.id = e.id and e.resultado is null and e.movimiento=@movimiento
select * from FEWS_PERMISO i, FEWS_ENCABEZADO e where i.id = e.id and e.resultado is null and e.movimiento=@movimiento

select * from FEWS_XML i, FEWS_ENCABEZADO e where i.id = e.id and e.resultado is null and e.movimiento=@movimiento

