
/****** Script for SelectTopNRows command from SSMS  ******/
SELECT TOP 1000 *
FROM [fews].[dbo].[FEWS_AST_FEWS_LOG]
where CUIT_EMPRESA <> '20225925055'
order by id desc

/****** Script for SelectTopNRows command from SSMS  ******/
SELECT TOP 10000 *
FROM [fews].[dbo].[FEWS_AST_FEWS_LOG]
where CUIT_EMPRESA = '20225925055'
order by id desc

/****** Script for SelectTopNRows command from SSMS  ******/
SELECT TOP 100 *
FROM [fews].[dbo].[FEWS_ENCABEZADO]
order by id desc
