USE [fews]
GO

/****** Object:  Table [dbo].[FEWS_QR]    Script Date: 03/02/2021 21:35:41 ******/
IF  not EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[FEWS_QR]') AND type in (N'U'))
begin 

CREATE TABLE [dbo].[FEWS_QR](
	[id] [int] NOT NULL,
	[texto_qr] [varchar](1000) NULL,
	[imagen_qr] [varbinary](max) NULL,
 CONSTRAINT [PK_FEWS_QR] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

ALTER TABLE [dbo].[FEWS_QR]  WITH CHECK ADD  CONSTRAINT [FK_FEWS_ENCABEZADO_FEWS_QR] FOREIGN KEY([id])
REFERENCES [dbo].[FEWS_ENCABEZADO] ([id])

ALTER TABLE [dbo].[FEWS_QR] CHECK CONSTRAINT [FK_FEWS_ENCABEZADO_FEWS_QR]

end

begin tran

declare @Param bigint
declare @Param_valor bigint
declare @CodigoParam varchar(100)

select @Param = null
select @CodigoParam = 'QR_IMAGE_WIDTH'
select @Param=ID_PARAMETRO from PARAMETROS where PARAMETRO=@CodigoParam
if @Param is null
begin
	select @Param = isnull(MAX(ID_PARAMETRO),0)+1 from PARAMETROS
	
	INSERT INTO [PARAMETROS] ([ID_PARAMETRO],[PARAMETRO],[DESCRIPCION],[TXT_AYUDA])
	VALUES (@Param,@CodigoParam,'Ancho Imagen QR','Ancho Imagen QR')
end

select @Param_valor = null
select @Param_valor = ID_PARAMETRO_VALOR from PARAMETROS_VALOR where ID_PARAMETRO=@Param
if @Param_valor is null
begin
	select @Param_valor = isnull(MAX(ID_PARAMETRO_VALOR),0)+1 from PARAMETROS_VALOR

	INSERT INTO [PARAMETROS_VALOR]([ID_PARAMETRO_VALOR],[ID_PARAMETRO],[VALOR],[F_VIGENCIA_DESDE],[F_VIGENCIA_HASTA])
	VALUES(@Param_valor,@Param,'300','2021-01-01 00:00:00.000','9999-09-09 00:00:00.000')
end

select @Param = null
select @CodigoParam = 'QR_IMAGE_HEIGHT'
select @Param=ID_PARAMETRO from PARAMETROS where PARAMETRO=@CodigoParam
if @Param is null
begin
	select @Param = isnull(MAX(ID_PARAMETRO),0)+1 from PARAMETROS
	
	INSERT INTO [PARAMETROS] ([ID_PARAMETRO],[PARAMETRO],[DESCRIPCION],[TXT_AYUDA])
	VALUES (@Param,@CodigoParam,'Alto Imagen QR','Alto Imagen QR')
end

select @Param_valor = null
select @Param_valor = ID_PARAMETRO_VALOR from PARAMETROS_VALOR where ID_PARAMETRO=@Param
if @Param_valor is null
begin
	select @Param_valor = isnull(MAX(ID_PARAMETRO_VALOR),0)+1 from PARAMETROS_VALOR

	INSERT INTO [PARAMETROS_VALOR]([ID_PARAMETRO_VALOR],[ID_PARAMETRO],[VALOR],[F_VIGENCIA_DESDE],[F_VIGENCIA_HASTA])
	VALUES(@Param_valor,@Param,'300','2021-01-01 00:00:00.000','9999-09-09 00:00:00.000')
end

select @Param = null
select @CodigoParam = 'QR_IMAGE_FORMAT'
select @Param=ID_PARAMETRO from PARAMETROS where PARAMETRO=@CodigoParam
if @Param is null
begin
	select @Param = isnull(MAX(ID_PARAMETRO),0)+1 from PARAMETROS
	
	INSERT INTO [PARAMETROS] ([ID_PARAMETRO],[PARAMETRO],[DESCRIPCION],[TXT_AYUDA])
	VALUES (@Param,@CodigoParam,'Formato de Imagen QR','Formato de Imagen QR')
end

select @Param_valor = null
select @Param_valor = ID_PARAMETRO_VALOR from PARAMETROS_VALOR where ID_PARAMETRO=@Param
if @Param_valor is null
begin
	select @Param_valor = isnull(MAX(ID_PARAMETRO_VALOR),0)+1 from PARAMETROS_VALOR

	INSERT INTO [PARAMETROS_VALOR]([ID_PARAMETRO_VALOR],[ID_PARAMETRO],[VALOR],[F_VIGENCIA_DESDE],[F_VIGENCIA_HASTA])
	VALUES(@Param_valor,@Param,'JPG','2021-01-01 00:00:00.000','9999-09-09 00:00:00.000')
end

select @Param = null
select @CodigoParam = 'QR_URL'
select @Param=ID_PARAMETRO from PARAMETROS where PARAMETRO=@CodigoParam
if @Param is null
begin
	select @Param = isnull(MAX(ID_PARAMETRO),0)+1 from PARAMETROS
	
	INSERT INTO [PARAMETROS] ([ID_PARAMETRO],[PARAMETRO],[DESCRIPCION],[TXT_AYUDA])
	VALUES (@Param,@CodigoParam,'URL para generacion de QR','URL para generacion de QR')
end

select @Param_valor = null
select @Param_valor = ID_PARAMETRO_VALOR from PARAMETROS_VALOR where ID_PARAMETRO=@Param
if @Param_valor is null
begin
	select @Param_valor = isnull(MAX(ID_PARAMETRO_VALOR),0)+1 from PARAMETROS_VALOR

	INSERT INTO [PARAMETROS_VALOR]([ID_PARAMETRO_VALOR],[ID_PARAMETRO],[VALOR],[F_VIGENCIA_DESDE],[F_VIGENCIA_HASTA])
	VALUES(@Param_valor,@Param,'https://www.afip.gob.ar/fe/qr/','2021-01-01 00:00:00.000','9999-09-09 00:00:00.000')
end

commit tran

begin tran

declare @Transac bigint
select @Transac = 2001
if not exists(select top 1 ID_TIPO_TRANSACCION from TIPOS_TRANSACCIONES where ID_TIPO_TRANSACCION=2001)
begin
	
	insert [TIPOS_TRANSACCIONES] ([ID_TIPO_TRANSACCION], [TRANSACCION], [TIPO_TRANSACCION], [TRANSACCION_REST], [URL_REST]) 
	VALUES (2001, N'generarQr', N'Generar QR', N'generarQr', N'/generar/qr')

end

commit tran

begin tran
delete item_rol from item where item_rol.id_item = item.id and descripcion = N'Generar QR'

delete from item where descripcion = N'Generar QR'
commit tran


declare @id_item int
declare @orden_item int
declare @id_item_rol int

select @id_item = (select MAX(id) from ITEM)+1
select @orden_item = (select MAX(orden) from ITEM where id_seccion=3)+1
select @id_item_rol = (select MAX(id) from ITEM_ROL)+1

if not exists(select * from ITEM where descripcion = N'Generar QR')
begin
	INSERT [dbo].[ITEM] ([id], [id_seccion], [descripcion], [orden], [url], [icon], [codigo], [TARGET]) 
		VALUES (@id_item, 3, N'Generar QR', @orden_item, N'/secure/facturacion/fewsGenerarQr.xhtml', N'flaticon-log_oper', N'SI_GEN_QR', NULL)
	INSERT [dbo].[ITEM_ROL] ([id], [id_rol], [id_item]) 
		VALUES (@id_item_rol, 1, @id_item)
end

--rollback tran