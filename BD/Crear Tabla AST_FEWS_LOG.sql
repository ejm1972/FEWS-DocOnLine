USE [FINN_MACOR_TMP]
GO

/****** Object:  Table [dbo].[AST_FEWS_LOG]    Script Date: 03/25/2019 08:02:44 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[AST_FEWS_LOG]') AND type in (N'U'))
DROP TABLE [dbo].[AST_FEWS_LOG]
GO

USE [FINN_MACOR_TMP]
GO

/****** Object:  Table [dbo].[AST_FEWS_LOG]    Script Date: 03/25/2019 08:02:44 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

SET ANSI_PADDING ON
GO

CREATE TABLE [dbo].[AST_FEWS_LOG](
	[CUIT_EMPRESA] [varchar](20) NULL,
	[TIPODOC_CLIENTE] [varchar](20) NULL,
	[NRODOC_CLIENTE] [varchar](20) NULL,
	[AS_ID] [varchar](20) NULL,
	[TIPO_COMPROBANTE] [varchar](3) NULL,
	[PUNTO_VENTA] [varchar](10) NULL,
	[NUMERO_COMPROBANTE] [varchar](20) NULL,
	[FECHA_COMPROBANTE] [varchar](10) NULL,
	[CONCEPTO_FACTURA] [varchar](2) NULL,
	[MONEDA] [varchar](20) NULL,
	[MONEDA_CTZ] [varchar](20) NULL,
	[IMPORTE_TOTAL] [varchar](20) NULL,
	[NETO_NOGRAVADO] [varchar](20) NULL,
	[NETO_GRAVADO] [varchar](20) NULL,
	[NETO_EXENTO] [varchar](20) NULL,
	[IVA_TOTAL] [varchar](20) NULL,
	[TRIBUTOS_TOTAL] [varchar](20) NULL,
	[CAE] [varchar](20) NULL,
	[FECHA_VENCIMIENTO] [varchar](10) NULL,
	[CODIGO] [varchar](10) NULL,
	[DESCRIPCION] [varchar](1000) NULL,
	[OBSERVACION] [varchar](1000) NULL,
	[EXCEPCION_WSAA] [varchar](1000) NULL,
	[EXCEPCION_WSFEV1] [varchar](1000) NULL,
	[RESULTADO] [varchar](10) NULL,
	[ERR_MSG] [varchar](1000) NULL,
	[OBS] [varchar](1000) NULL,
	[XML_REQUEST_AFIP] [text] NULL,
	[XML_RESPONSE_AFIP] [text] NULL
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO

SET ANSI_PADDING OFF
GO


