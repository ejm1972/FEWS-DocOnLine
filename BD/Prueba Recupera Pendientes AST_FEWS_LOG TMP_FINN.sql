use fews
go

drop table #tmp
go

select CUIT_EMPRESA CE, 
	TIPO_COMPROBANTE TC, 
	PUNTO_VENTA PV, 
	min(NUMERO_COMPROBANTE) NC, 
	convert(int, TIPO_COMPROBANTE ) NTC, 
	convert(int, PUNTO_VENTA) NPV, 
	convert(int, min(NUMERO_COMPROBANTE)) NNC, 
	0 IDC
	, (select top 1 id_interfaz from INTERFACES where CUIT_SUSCRIPCION=CUIT_EMPRESA COLLATE DATABASE_DEFAULT) ID_INTERFAZ
into #tmp
from TMP_FINN.dbo.AST_FEWS_LOG
where RESULTADO is null or RESULTADO <> 'A'
group by CUIT_EMPRESA, TIPO_COMPROBANTE, PUNTO_VENTA

select * from #tmp

select *
from TMP_FINN.dbo.AST_FEWS_LOG l, #tmp t
where l.CUIT_EMPRESA=CE
	and TIPO_COMPROBANTE=TC
	and PUNTO_VENTA=PV
	and NUMERO_COMPROBANTE=NC
order by TIPO_COMPROBANTE desc, PUNTO_VENTA, NUMERO_COMPROBANTE desc

select *
from TMP_FINN.dbo.AST_FEWS_LOG_IVA l, #tmp t
where l.CUIT_EMPRESA=CE
	and TIPO_COMPROBANTE=TC
	and PUNTO_VENTA=PV
	and NUMERO_COMPROBANTE=NC
order by TIPO_COMPROBANTE desc, PUNTO_VENTA, NUMERO_COMPROBANTE desc

select *
from TMP_FINN.dbo.AST_FEWS_LOG_TRIBUTOS l, #tmp t
where l.CUIT_EMPRESA=CE
	and TIPO_COMPROBANTE=TC
	and PUNTO_VENTA=PV
	and NUMERO_COMPROBANTE=NC
order by TIPO_COMPROBANTE desc, PUNTO_VENTA, NUMERO_COMPROBANTE desc

select *
from TMP_FINN.dbo.AST_FEWS_LOG_CBTE_ASOC l, #tmp t
where l.CUIT_EMPRESA=CE
	and TIPO_COMPROBANTE=TC
	and PUNTO_VENTA=PV
	and NUMERO_COMPROBANTE=NC
order by TIPO_COMPROBANTE desc, PUNTO_VENTA, NUMERO_COMPROBANTE desc

select *
from TMP_FINN.dbo.AST_FEWS_LOG_DATOS_OPC l, #tmp t
where l.CUIT_EMPRESA=CE
	and TIPO_COMPROBANTE=TC
	and PUNTO_VENTA=PV
	and NUMERO_COMPROBANTE=NC
order by TIPO_COMPROBANTE desc, PUNTO_VENTA, NUMERO_COMPROBANTE desc

--exec AST_UPD_FEWS_ENCABEZADO_MACOR 

/*
SELECT resultado, obs, cae,* FROM TMP_FINN.dbo.AST_FEWS_LOG order by as_id desc 
*/

/*
			DELETE FROM AST_FEWS_LOG WHERE CUIT_EMPRESA=CE and TIPO_COMPROBANTE=TC and PUNTO_VENTA=PV and NUMERO_COMPROBANTE=NC
			DELETE FROM AST_FEWS_LOG_IVA WHERE AS_ID=@@AS_ID
			DELETE FROM AST_FEWS_LOG_TRIBUTOS WHERE AS_ID=@@AS_ID
			DELETE FROM AST_FEWS_LOG_CBTE_ASOC WHERE AS_ID=@@AS_ID
			DELETE FROM AST_FEWS_LOG_DATOS_OPC WHERE AS_ID=@@AS_ID
			
delete from TMP_FINN.dbo.AST_FEWS_LOG where as_id in (1512043)
delete from TMP_FINN.dbo.AST_FEWS_LOG_TRIBUTOS where as_id in (1512043)
delete from TMP_FINN.dbo.AST_FEWS_LOG_IVA where as_id in (1512043)
delete from TMP_FINN.dbo.AST_FEWS_LOG_CBTE_ASOC where as_id in (1512043)
delete from TMP_FINN.dbo.AST_FEWS_LOG_DATOS_OPC where as_id in (1512043)
*/

/*
select *
from TMP_FINN.dbo.AST_FEWS_LOG l
where TIPO_COMPROBANTE='01'
order by TIPO_COMPROBANTE desc, PUNTO_VENTA, NUMERO_COMPROBANTE desc

select *
from TMP_FINN.dbo.AST_FEWS_LOG_IVA l
where TIPO_COMPROBANTE='01'
order by TIPO_COMPROBANTE desc, PUNTO_VENTA, NUMERO_COMPROBANTE desc

select *
from TMP_FINN.dbo.AST_FEWS_LOG_TRIBUTOS l
where TIPO_COMPROBANTE='01'
order by TIPO_COMPROBANTE desc, PUNTO_VENTA, NUMERO_COMPROBANTE desc

select *
from TMP_FINN.dbo.AST_FEWS_LOG_CBTE_ASOC l
where TIPO_COMPROBANTE='01'
order by TIPO_COMPROBANTE desc, PUNTO_VENTA, NUMERO_COMPROBANTE desc

select *
from TMP_FINN.dbo.AST_FEWS_LOG_DATOS_OPC l
where TIPO_COMPROBANTE='01'
order by TIPO_COMPROBANTE desc, PUNTO_VENTA, NUMERO_COMPROBANTE desc*/

/*
select as_numerodoc, as_cai, *
from TMP_FINN.dbo.asiento
where SUBSTRING(as_numerodoc,1,1) in ('A','B')
order by as_id desc
*/

select e.movimiento, e.cae, e.fecha_vto, convert(datetime, e.fecha_vto, 110), * 
from FEWS_ENCABEZADO e
	, FEWS_XML x
	, TMP_FINN.dbo.AST_FEWS_LOG l
	, #tmp p
where e.id=x.id
	and CUIT_EMPRESA=CE
	and TIPO_COMPROBANTE=TC
	and PUNTO_VENTA=PV
	and NUMERO_COMPROBANTE=NC
	and e.resultado='A' 
	and e.cuit=p.CE COLLATE DATABASE_DEFAULT 
	and e.tipo_cbte=p.NTC 
	and e.punto_vta=p.NPV 
	and e.cbte_nro=p.NNC

/*
select as_cai, as_caifechavto, *
--update TMP_FINN.dbo.asiento set as_cai=69159889082364, as_caifechavto=convert(datetime,'20190421', 110)
from TMP_FINN.dbo.asiento
where as_numerodoc = 'A000400000077' and as_id=1513265
*/