drop table #pen
go

select CUIT_EMPRESA CE, TIPO_COMPROBANTE TC, PUNTO_VENTA PV, min(NUMERO_COMPROBANTE) NC, convert(int, TIPO_COMPROBANTE ) NTC, convert(int, PUNTO_VENTA) NPV, convert(int, min(NUMERO_COMPROBANTE)) NNC, 0 IDC
, (select top 1 id_interfaz from INTERFACES where CUIT_SUSCRIPCION=CUIT_EMPRESA COLLATE DATABASE_DEFAULT) ID_INTERFAZ
into #pen
from TMP_ADMIFARM.dbo.AST_FEWS_LOG
where RESULTADO is null or RESULTADO <> 'A'
group by CUIT_EMPRESA, TIPO_COMPROBANTE, PUNTO_VENTA

if not exists(select top 1 CE from #pen where ID_INTERFAZ is null)
begin

delete FEWS_XML
from FEWS_XML x, FEWS_ENCABEZADO e, #pen p
where (e.resultado is null or e.resultado <> 'A')
	and e.cuit      =p.CE       COLLATE DATABASE_DEFAULT
	and e.tipo_cbte =p.NTC
	and e.punto_vta =p.NPV	
	and e.cbte_nro  =p.NNC
	and e.id=x.id
	
delete FEWS_TRIBUTO
from FEWS_TRIBUTO x, FEWS_ENCABEZADO e, #pen p
where (e.resultado is null or e.resultado <> 'A')
	and e.cuit      =p.CE       COLLATE DATABASE_DEFAULT
	and e.tipo_cbte =p.NTC
	and e.punto_vta =p.NPV	
	and e.cbte_nro  =p.NNC
	and e.id=x.id
		
delete FEWS_IVA
from FEWS_IVA x, FEWS_ENCABEZADO e, #pen p
where (e.resultado is null or e.resultado <> 'A')
	and e.cuit      =p.CE       COLLATE DATABASE_DEFAULT
	and e.tipo_cbte =p.NTC
	and e.punto_vta =p.NPV	
	and e.cbte_nro  =p.NNC
	and e.id=x.id

delete FEWS_ENCABEZADO
from FEWS_ENCABEZADO e, #pen p
where (e.resultado is null or e.resultado <> 'A')
	and e.cuit      =p.CE       COLLATE DATABASE_DEFAULT
	and e.tipo_cbte =p.NTC
	and e.punto_vta =p.NPV	
	and e.cbte_nro  =p.NNC

insert into FEWS_ENCABEZADO (
			CUIT,
			TIPO_DOC,
			NRO_DOC,
			TIPO_CBTE,
			PUNTO_VTA,
			CBTE_NRO,
			FECHA_CBTE,
			CONCEPTO,
			MONEDA_ID,
			MONEDA_CTZ,
			IMP_TOTAL,
			IMP_NETO,
			IMP_TOT_CONC,
			IMP_OP_EX,
			IMP_IVA,
			IMP_TRIB,
			fecha_serv_desde
			, fecha_serv_hasta
			, fecha_venc_pago
			, movimiento
			, id_interfaz )
		SELECT 
			CUIT_EMPRESA,
			TIPODOC_CLIENTE,
			NRODOC_CLIENTE,
			TIPO_COMPROBANTE,
			PUNTO_VENTA,
			NUMERO_COMPROBANTE,
			FECHA_COMPROBANTE,
			CONCEPTO_FACTURA,
			MONEDA,
			MONEDA_CTZ,
			IMPORTE_TOTAL,
			NETO_GRAVADO,
			NETO_NOGRAVADO,
			NETO_EXENTO,
			IVA_TOTAL,
			TRIBUTOS_TOTAL
			, FECHA_COMPROBANTE
			, FECHA_COMPROBANTE
			, FECHA_COMPROBANTE
			, right('00000'+CONVERT(varchar(5), p.ID_INTERFAZ),5)
								+right('000'+CONVERT(varchar(3), NTC),3)
								+right('0000'+CONVERT(varchar(4), NPV),4)
								+right('00000000'+CONVERT(varchar(8), NNC),8)
			, ID_INTERFAZ
		FROM TMP_ADMIFARM.dbo.AST_FEWS_LOG l, #pen p
		WHERE CUIT_EMPRESA=CE
			and TIPO_COMPROBANTE=TC
			and PUNTO_VENTA=PV
			and NUMERO_COMPROBANTE=NC


	--
	-- OBTIENE EL ID DE LA CONSOLA
	---
	update #pen
	set idc = id
	from FEWS_ENCABEZADO, #pen
	where cuit=CE
		and tipo_cbte=NTC
		and punto_vta=NPV
		and cbte_nro=NNC
		
	---
	--- INSERTA EN FEWS_IVA
	-----

	-- SELECT * FROM [FNNGS-TEST-01].FEWS.DBO.FEWS_IVA
	INSERT INTO FEWS_IVA(
		ID,
		IVA_ID,
		BASE_IMP,
		IMPORTE)

	select
		ID=IDC,
		IVA_ID= CASE AST_FEWS_LOG_IVA.PORCENTAJE
					WHEN 10.5 THEN	4
					WHEN 21 THEN	5
					WHEN 27 THEN	6
				END ,
		BASE_IMP=AST_FEWS_LOG_IVA.NETO_GRAVADO,
		IMPORTE=AST_FEWS_LOG_IVA.IMPORTE
	from TMP_ADMIFARM.dbo.AST_FEWS_LOG_IVA, #pen p
		WHERE CUIT_EMPRESA=CE
			and TIPO_COMPROBANTE=TC
			and PUNTO_VENTA=PV
			and NUMERO_COMPROBANTE=NC

	---
	--- INSERTA E FEWS_TRIBUTO
	-----

	-- SELECT * FROM [FNNGS-TEST-01].FEWS.DBO.FEWS_TRIBUTO
	INSERT INTO FEWS_TRIBUTO(
		ID,
		TRIBUTO_ID,
		TRIBUTO_DESC,
		IMPORTE)

	select
		ID=IDC ,
		TRIBUTO_ID= AST_FEWS_LOG_TRIBUTOS.TRIBUTO_ID ,
		TRIBUTO_DESC=AST_FEWS_LOG_TRIBUTOS.DESCRPTION,
		IMPORTE=AST_FEWS_LOG_TRIBUTOS.IMPORTE
	from TMP_ADMIFARM.dbo.AST_FEWS_LOG_TRIBUTOS, #pen p
		WHERE CUIT_EMPRESA=CE
			and TIPO_COMPROBANTE=TC
			and PUNTO_VENTA=PV
			and NUMERO_COMPROBANTE=NC
end 

/*
select CUIT_EMPRESA, TIPO_COMPROBANTE, PUNTO_VENTA, min(NUMERO_COMPROBANTE) 
into #pen
from FINNEGANS.FINN_Admifarm.dbo.AST_FEWS_LOG
where RESULTADO is null or RESULTADO <> 'A'
group by CUIT_EMPRESA, TIPO_COMPROBANTE, PUNTO_VENTA
*/

