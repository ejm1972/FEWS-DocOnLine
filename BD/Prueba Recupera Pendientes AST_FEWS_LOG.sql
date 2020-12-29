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
from SERVER1.FINN_MACOR2014.dbo.AST_FEWS_LOG
where RESULTADO is null or RESULTADO <> 'A'
group by CUIT_EMPRESA, TIPO_COMPROBANTE, PUNTO_VENTA

select * from #tmp

select *
from SERVER1.FINN_MACOR2014.dbo.AST_FEWS_LOG l, #tmp t
where l.CUIT_EMPRESA=CE
	and TIPO_COMPROBANTE=TC
	and PUNTO_VENTA=PV
	and NUMERO_COMPROBANTE=NC
order by TIPO_COMPROBANTE desc, PUNTO_VENTA, NUMERO_COMPROBANTE desc

/*
select *
from SERVER1.FINN_MACOR2014.dbo.AST_FEWS_LOG_IVA l, #tmp t
where l.CUIT_EMPRESA=CE
	and TIPO_COMPROBANTE=TC
	and PUNTO_VENTA=PV
	and NUMERO_COMPROBANTE=NC
order by TIPO_COMPROBANTE desc, PUNTO_VENTA, NUMERO_COMPROBANTE desc

select *
from SERVER1.FINN_MACOR2014.dbo.AST_FEWS_LOG_TRIBUTOS l, #tmp t
where l.CUIT_EMPRESA=CE
	and TIPO_COMPROBANTE=TC
	and PUNTO_VENTA=PV
	and NUMERO_COMPROBANTE=NC
order by TIPO_COMPROBANTE desc, PUNTO_VENTA, NUMERO_COMPROBANTE desc

select *
from SERVER1.FINN_MACOR2014.dbo.AST_FEWS_LOG_CBTE_ASOC l, #tmp t
where l.CUIT_EMPRESA=CE
	and TIPO_COMPROBANTE=TC
	and PUNTO_VENTA=PV
	and NUMERO_COMPROBANTE=NC
order by TIPO_COMPROBANTE desc, PUNTO_VENTA, NUMERO_COMPROBANTE desc

select *
from SERVER1.FINN_MACOR2014.dbo.AST_FEWS_LOG_DATOS_OPC l, #tmp t
where l.CUIT_EMPRESA=CE
	and TIPO_COMPROBANTE=TC
	and PUNTO_VENTA=PV
	and NUMERO_COMPROBANTE=NC
order by TIPO_COMPROBANTE desc, PUNTO_VENTA, NUMERO_COMPROBANTE desc
*/

--exec AST_UPD_FEWS_ENCABEZADO_MACOR 

/*
SELECT resultado, obs, cae,* FROM SERVER1.FINN_MACOR2014.dbo.AST_FEWS_LOG order by as_id desc 
*/

/*
			DELETE FROM AST_FEWS_LOG WHERE CUIT_EMPRESA=CE and TIPO_COMPROBANTE=TC and PUNTO_VENTA=PV and NUMERO_COMPROBANTE=NC
			DELETE FROM AST_FEWS_LOG_IVA WHERE AS_ID=@@AS_ID
			DELETE FROM AST_FEWS_LOG_TRIBUTOS WHERE AS_ID=@@AS_ID
			DELETE FROM AST_FEWS_LOG_CBTE_ASOC WHERE AS_ID=@@AS_ID
			DELETE FROM AST_FEWS_LOG_DATOS_OPC WHERE AS_ID=@@AS_ID
			
delete from SERVER1.FINN_MACOR2014.dbo.AST_FEWS_LOG where as_id in (1543517)
delete from SERVER1.FINN_MACOR2014.dbo.AST_FEWS_LOG_TRIBUTOS where as_id in (1543517)
delete from SERVER1.FINN_MACOR2014.dbo.AST_FEWS_LOG_IVA where as_id in (1543517)
delete from SERVER1.FINN_MACOR2014.dbo.AST_FEWS_LOG_CBTE_ASOC where as_id in (1543517)
delete from SERVER1.FINN_MACOR2014.dbo.AST_FEWS_LOG_DATOS_OPC where as_id in (1543517)
*/

/*
select *
from SERVER1.FINN_MACOR2014.dbo.AST_FEWS_LOG l
where TIPO_COMPROBANTE IS NULL 
order by TIPO_COMPROBANTE desc, PUNTO_VENTA, NUMERO_COMPROBANTE desc

select *
from SERVER1.FINN_MACOR2014.dbo.AST_FEWS_LOG_IVA l
where TIPO_COMPROBANTE='01'
order by TIPO_COMPROBANTE desc, PUNTO_VENTA, NUMERO_COMPROBANTE desc

select *
from SERVER1.FINN_MACOR2014.dbo.AST_FEWS_LOG_TRIBUTOS l
where TIPO_COMPROBANTE='01'
order by TIPO_COMPROBANTE desc, PUNTO_VENTA, NUMERO_COMPROBANTE desc
*/

/*
select as_numerodoc, as_cai, *
from SERVER1.FINN_MACOR2014.dbo.asiento
where SUBSTRING(as_numerodoc,1,1) in ('A','B')
order by as_id desc
*/

	select e.movimiento, e.cae, e.fecha_vto, convert(datetime, e.fecha_vto, 110), l.tipodoc_cliente, l.nrodoc_cliente, l.fecha_comprobante, l.moneda, l.importe_total, l.iva_total, e.fecha_cbte, e.tipo_doc, e.nro_doc, e.imp_total, e.imp_iva
		, resultado=e.resultado,
		cae=e.cae,
		fecha_vencimiento=e.fecha_vto,
		err_msg=e.err_msg,
		obs=e.motivo,
		codigo=x.codigo,
		descripcion=x.descripcion,
		observacion=x.observacion,
		excepcion_wsaa=x.excepcion_wsaa,
		excepcion_wsfev1=x.excepcion_wsfev1,
		xml_request_afip=x.xml_request,
		xml_response_afip=x.xml_response
	--update SERVER1.FINN_MACOR2014.dbo.AST_FEWS_LOG
	--set resultado=fenc.resultado,
	--	cae=fenc.cae,
	--	fecha_vencimiento=fenc.fecha_vto,
	--	err_msg=fenc.err_msg,
	--	obs=fenc.motivo,
	--	codigo=fxml.codigo,
	--	descripcion=fxml.descripcion,
	--	observacion=fxml.observacion,
	--	excepcion_wsaa=fxml.excepcion_wsaa,
	--	excepcion_wsfev1=fxml.excepcion_wsfev1,
	--	xml_request_afip=fxml.xml_request,
	--	xml_response_afip=fxml.xml_response
	FROM FEWS_ENCABEZADO e
		, FEWS_XML x
		, SERVER.FINN_MACOR2014.dbo.AST_FEWS_LOG l
		, #tmp p
	WHERE e.id=x.id
		and CUIT_EMPRESA=CE
		and TIPO_COMPROBANTE=TC
		and PUNTO_VENTA=PV
		and NUMERO_COMPROBANTE=NC
		and e.resultado='A' 
		and e.cuit=p.CE COLLATE DATABASE_DEFAULT 
		and e.tipo_cbte=p.NTC 
		and e.punto_vta=p.NPV 
		and e.cbte_nro=p.NNC
		and e.tipo_doc=l.TIPODOC_CLIENTE
		and e.nro_doc=l.NRODOC_CLIENTE
		and e.fecha_cbte=l.FECHA_COMPROBANTE COLLATE DATABASE_DEFAULT
		and e.moneda_id=l.MONEDA COLLATE DATABASE_DEFAULT
		and e.imp_total=l.IMPORTE_TOTAL
		and e.imp_tot_conc=l.NETO_NOGRAVADO
		and e.imp_neto=l.NETO_GRAVADO
		and e.imp_op_ex=l.NETO_EXENTO
		and e.imp_iva=l.IVA_TOTAL
		and e.imp_trib=l.TRIBUTOS_TOTAL

/*
select as_cai, as_caifechavto, *
--update SERVER1.FINN_MACOR2014.dbo.asiento set as_cai=69159889082364, as_caifechavto=convert(datetime,'20190421', 110)
from SERVER1.FINN_MACOR2014.dbo.asiento
where as_numerodoc = 'A000400000077' and as_id=1513265
*/

/*
dbo.FEWS_PENDIENTES
dbo.AST_UPD_FEWS_ENCABEZADO_MACOR
*/
