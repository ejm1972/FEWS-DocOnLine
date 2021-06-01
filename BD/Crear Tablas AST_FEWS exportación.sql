/****** Object:  Table [dbo].[AST_FEWS_LOG_DETALLE]    Script Date: 05/25/2016 10:40:21 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[AST_FEWS_LOG_DETALLE](
    [AS_ID] VARCHAR(20) NULL,
    [CUIT_EMPRESA] VARCHAR(20) NULL,
    [TIPO_COMPROBANTE] VARCHAR(3) NULL,
    [PUNTO_VENTA] VARCHAR(10) NULL,
    [NUMERO_COMPROBANTE] VARCHAR(20) NULL,
	
	[CODIGO] [varchar](30) NULL,					--Código de articulo
	[DESCRIPCION] [varchar](4000) NULL,				--Descripcion del artículo
	[CANTIDAD] [varchar](20) NULL,					--Cantidad
	[UNIDAD_MEDIDA] [smallint] NULL,				--Unidad de Medida según tabla AFIP
	[PRECIO] [varchar](20) NULL,					--Precio Unitario
	[IMPORTE_TOTAL] [varchar](20) NULL,				--Importe Total Precio * Cantidad - Bonificación
	[BONIFICACION] [varchar](20) NULL				--Bonificación del Item
	
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO

/****** Object:  Table [dbo].[AST_FEWS_LOG_PERMISO]    Script Date: 05/25/2016 10:40:21 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[AST_FEWS_LOG_PERMISO](
    [AS_ID] VARCHAR(20) NULL,
    [CUIT_EMPRESA] VARCHAR(20) NULL,
    [TIPO_COMPROBANTE] VARCHAR(3) NULL,
    [PUNTO_VENTA] VARCHAR(10) NULL,
    [NUMERO_COMPROBANTE] VARCHAR(20) NULL,
	
	[ID_PERMISO] [varchar](16) NULL,				--Identificador de Permiso
	[DST_MERCADERIA] [smallint] NULL				--Destino de la mercadería según tabla AFIP
	
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO

/****** Object:  Table [dbo].[AST_FEWS_LOG]    Script Date: 05/25/2016 10:40:21 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
ALTER TABLE [dbo].[AST_FEWS_LOG] 
	ADD [TIPO_EXPORTACION] [varchar](1) NULL		--Código de exportación según tabla AFIP (Idem CONCEPTOS_FACTURA)	1= Exportación definitiva de bienes
													--																	2= Servicios
													--																	4= Otros
ALTER TABLE [dbo].[AST_FEWS_LOG] 
	ADD [PERMISO_EXISTENTE] [varchar](1) NULL		--S o N para indicar si se tiene o no Permiso (si es S debe figurar en la tabla [AST_FEWS_LOG_PERMISO]

ALTER TABLE [dbo].[AST_FEWS_LOG] 
	ADD [DST_COMPROBANTE] [varchar](3) NULL			--Código de Destino del Comprobante según tabla AFIP

ALTER TABLE [dbo].[AST_FEWS_LOG] 
	ADD [CLIENTE] [varchar](200) NULL				--Nombre del Cliente

ALTER TABLE [dbo].[AST_FEWS_LOG] 
	ADD [CUIT_PAIS_CLIENTE] [varchar](20) NULL		--Cuit Pais del Cliente

ALTER TABLE [dbo].[AST_FEWS_LOG] 
	ADD [DOMICILIO_CLIENTE] [varchar](300) NULL		--Domicilio del Cliente

ALTER TABLE [dbo].[AST_FEWS_LOG] 
	ADD [ID_IMPOSITIVO] [varchar](50) NULL			--Identificador Impositivo, dato que debe ser cargado 

ALTER TABLE [dbo].[AST_FEWS_LOG] 
	ADD [OBS_COMERCIALES] [varchar](1000) NULL		--Cualquier texto con relación a lo comercial

ALTER TABLE [dbo].[AST_FEWS_LOG] 
	ADD [OBS_GENERALES] [varchar](1000) NULL		--Cualquier texto en general

ALTER TABLE [dbo].[AST_FEWS_LOG] 
	ADD [FORMA_PAGO] [varchar](50) NULL				--Texto indicando Forma de Pago

ALTER TABLE [dbo].[AST_FEWS_LOG] 
	ADD [INCOTERMS] [varchar](3) NULL				--Incoterms según tabla AFIP							No es obligatorio si TIPO_EXPORTACION = 4

ALTER TABLE [dbo].[AST_FEWS_LOG] 
	ADD [INCOTERMS_DS] [varchar](20) NULL			--Descripción adicional relacionada con el Incoterms	No es obligatorio

ALTER TABLE [dbo].[AST_FEWS_LOG] 
	ADD [IDIOMA_COMPROBANTE] [varchar](1) NULL		--Código de idioma del comprobante según tabla AFIP

ALTER TABLE [dbo].[AST_FEWS_LOG] 
	ADD [EXCEPCION_WSFEXV1] [varchar](1000) NULL	--Texto de errores exclusivo para la respuesta del WSFEXv1 que actualiza la Consola 
GO
SET ANSI_PADDING OFF
GO
