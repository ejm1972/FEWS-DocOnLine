/****** Script for SelectTopNRows command from SSMS  ******/
SELECT max(id), 
	CUIT_EMPRESA, TIPO_COMPROBANTE, PUNTO_VENTA, NUMERO_COMPROBANTE
	, MIN(creado) INICIO
	, MAX(creado) FINAL
	, DATEDIFF(S,  MIN(creado), MAX(creado))/60 as Minutos
	, DATEDIFF(SS,  MIN(creado), MAX(creado)) % 60 as Segundos
	, sum(case when TIPO in ('IIN', 'FIN') then 1 else 0 end) as Cantidad_IN
	, sum(case when TIPO in ('IUD', 'FUD') then 1 else 0 end) as Cantidad_UD
FROM dbo.FEWS_AST_FEWS_LOG
where CUIT_EMPRESA <> '20225925055'
group by CUIT_EMPRESA, TIPO_COMPROBANTE, PUNTO_VENTA, NUMERO_COMPROBANTE
order by max(id) desc

/*
update [FEWS_AST_FEWS_LOG]
set intento = t.minimo
from [FEWS_AST_FEWS_LOG] f,
(SELECT CUIT_EMPRESA, TIPO_COMPROBANTE, PUNTO_VENTA, NUMERO_COMPROBANTE, MIN(creado) as minimo
FROM [fews].[dbo].[FEWS_AST_FEWS_LOG]
group by CUIT_EMPRESA, TIPO_COMPROBANTE, PUNTO_VENTA, NUMERO_COMPROBANTE) t
where f.CUIT_EMPRESA = t.CUIT_EMPRESA
	and f.TIPO_COMPROBANTE = t.TIPO_COMPROBANTE
	and f.PUNTO_VENTA = t.PUNTO_VENTA
	and f.NUMERO_COMPROBANTE = t.NUMERO_COMPROBANTE
	and f.intento is null
*/

/****** Script for SelectTopNRows command from SSMS  ******/
SELECT max(id), 
	CUIT_EMPRESA, TIPO_COMPROBANTE, PUNTO_VENTA, NUMERO_COMPROBANTE, INTENTO
	, MIN(creado) INICIO
	, MAX(creado) FINAL
	, DATEDIFF(S,  MIN(creado), MAX(creado))/60 as Minutos
	, DATEDIFF(SS,  MIN(creado), MAX(creado)) % 60 as Segundos
	, sum(case when TIPO in ('IIN', 'FIN') then 1 else 0 end) as Cantidad_IN
	, sum(case when TIPO in ('IUD', 'FUD') then 1 else 0 end) as Cantidad_UD
FROM dbo.FEWS_AST_FEWS_LOG
where CUIT_EMPRESA <> '20225925055'
	and TIPO_COMPROBANTE = '03'
	and PUNTO_VENTA = '0005'
	and NUMERO_COMPROBANTE = '00002895'
group by CUIT_EMPRESA, TIPO_COMPROBANTE, PUNTO_VENTA, NUMERO_COMPROBANTE, INTENTO
order by max(id) desc