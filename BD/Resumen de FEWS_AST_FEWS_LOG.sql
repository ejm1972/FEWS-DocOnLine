
/****** Script for SelectTopNRows command from SSMS  ******/
SELECT max(id), 
	CUIT_EMPRESA, TIPO_COMPROBANTE, PUNTO_VENTA, NUMERO_COMPROBANTE
	, MAX(creado), MIN(creado)
	, DATEDIFF(S,  MIN(creado), MAX(creado))/60 as Minutos
	, DATEDIFF(SS,  MIN(creado), MAX(creado)) % 60 as Segundos
	, sum(case when TIPO in ('IIN', 'FIN') then 1 else 0 end) as Cantidad_IN
	, sum(case when TIPO in ('IUD', 'FUD') then 1 else 0 end) as Cantidad_UD
FROM [fews].[dbo].[FEWS_AST_FEWS_LOG]
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

