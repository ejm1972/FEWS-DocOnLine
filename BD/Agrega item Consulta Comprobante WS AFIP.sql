use Fews
begin tran
delete item_rol from item where item_rol.id_item = item.id and descripcion = N'Consultar Último Comprobante'
delete item_rol from item where item_rol.id_item = item.id and descripcion = N'Consultar Comprobante'

delete from item where descripcion = N'Consultar Último Comprobante'
delete from item where descripcion = N'Consultar Comprobante'
commit tran


declare @id_item int
declare @orden_item int
declare @id_item_rol int

select @id_item = (select MAX(id) from ITEM)+1
select @orden_item = (select MAX(orden) from ITEM where id_seccion=3)+1
select @id_item_rol = (select MAX(id) from ITEM_ROL)+1

if not exists(select * from ITEM where descripcion = N'Consultar Último Comprobante')
begin
	INSERT [dbo].[ITEM] ([id], [id_seccion], [descripcion], [orden], [url], [icon], [codigo], [TARGET]) 
		VALUES (@id_item, 3, N'Consultar Último Comprobante', @orden_item, N'/secure/facturacion/fewsConsultarUltimoComprobante.xhtml', N'flaticon-log_oper', N'SI_ULT_COM', NULL)
	INSERT [dbo].[ITEM_ROL] ([id], [id_rol], [id_item]) 
		VALUES (@id_item_rol, 1, @id_item)
end

select @id_item = (select MAX(id) from ITEM)+1
select @orden_item = (select MAX(orden) from ITEM where id_seccion=3)+1
select @id_item_rol = (select MAX(id) from ITEM_ROL)+1

if not exists(select * from ITEM where descripcion = N'Consultar Comprobante')
begin
	INSERT [dbo].[ITEM] ([id], [id_seccion], [descripcion], [orden], [url], [icon], [codigo], [TARGET]) 
		VALUES (@id_item, 3, N'Consultar Comprobante', @orden_item, N'/secure/facturacion/fewsConsultarComprobante.xhtml', N'flaticon-log_oper', N'SI_CON_COM', NULL)
	INSERT [dbo].[ITEM_ROL] ([id], [id_rol], [id_item]) 
		VALUES (@id_item_rol, 1, @id_item)
end
