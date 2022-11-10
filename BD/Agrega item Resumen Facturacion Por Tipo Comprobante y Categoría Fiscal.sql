use Fews
begin tran
delete item_rol from item where item_rol.id_item = item.id and descripcion = N'Reporte Resumen Categoría Fiscal'
delete from item where descripcion = N'Reporte Resumen Categoría Fiscal'
commit tran


declare @id_item int
declare @orden_item int
declare @id_item_rol int

select @id_item = (select MAX(id) from ITEM)+1
select @orden_item = (select MAX(orden) from ITEM where id_seccion=3)+1
select @id_item_rol = (select MAX(id) from ITEM_ROL)+1

if not exists(select * from ITEM where descripcion = N'Reporte Resumen Categoría Fiscal')
begin
	INSERT [dbo].[ITEM] ([id], [id_seccion], [descripcion], [orden], [url], [icon], [codigo], [TARGET]) 
		VALUES (@id_item, 3, N'Reporte Resumen Categoría Fiscal', @orden_item, N'/secure/facturacion/fewsReporteCatFis.xhtml', N'flaticon-log_oper', N'SI_REP_CFI', NULL)
	INSERT [dbo].[ITEM_ROL] ([id], [id_rol], [id_item]) 
		VALUES (@id_item_rol, 1, @id_item)
end
