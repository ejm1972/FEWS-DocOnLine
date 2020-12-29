declare @id_item int
declare @orden_item int
declare @id_item_rol int

select @id_item = (select MAX(id) from ITEM)+1
select @orden_item = (select MAX(orden) from ITEM where id_seccion=3)+1
select @id_item_rol = (select MAX(id) from ITEM_ROL)+1

if not exists(select * from ITEM where descripcion = N'Facturas')
begin
	INSERT [dbo].[ITEM] ([id], [id_seccion], [descripcion], [orden], [url], [icon], [codigo], [TARGET]) 
		VALUES (@id_item, 3, N'Facturas', @orden_item, N'/secure/facturacion/fewsMovimientos.xhtml', N'flaticon-log_oper', N'SI_REG_FAC', NULL)
	INSERT [dbo].[ITEM_ROL] ([id], [id_rol], [id_item]) 
		VALUES (@id_item_rol, 1, @id_item)
end