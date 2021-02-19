USE [fews]
GO

/****** Object:  StoredProcedure [dbo].[AST_UPD_FEWS_ENCABEZADO_ZBC]    Script Date: 12/05/2018 13:04:30 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[AST_UPD_FEWS_ENCABEZADO_ZBC]') AND type in (N'P', N'PC'))
DROP PROCEDURE [dbo].[AST_UPD_FEWS_ENCABEZADO_ZBC]
GO

USE [fews]
GO

/****** Object:  StoredProcedure [dbo].[AST_UPD_FEWS_ENCABEZADO_ZBC]    Script Date: 12/05/2018 13:04:30 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE  PROCEDURE [dbo].[AST_UPD_FEWS_ENCABEZADO_ZBC]  
 -- Add the parameters for the stored procedure here    

AS    
BEGIN
SET NOCOUNT ON

begin tran
--Borro todos los comprobantes pendientes
delete FEWS_QR
from FEWS_QR x, FEWS_ENCABEZADO e
where (e.resultado is null or e.resultado <> 'A')
	and e.id=x.id
	
delete FEWS_XML
from FEWS_XML x, FEWS_ENCABEZADO e
where (e.resultado is null or e.resultado <> 'A')
	and e.id=x.id

delete FEWS_DATO_OPC
from FEWS_DATO_OPC x, FEWS_ENCABEZADO e
where (e.resultado is null or e.resultado <> 'A')
	and e.id=x.id

delete FEWS_CMP_ASOC
from FEWS_CMP_ASOC x, FEWS_ENCABEZADO e
where (e.resultado is null or e.resultado <> 'A')
	and e.id=x.id

delete FEWS_TRIBUTO
from FEWS_TRIBUTO x, FEWS_ENCABEZADO e
where (e.resultado is null or e.resultado <> 'A')
	and e.id=x.id
	
delete FEWS_IVA
from FEWS_IVA x, FEWS_ENCABEZADO e
where (e.resultado is null or e.resultado <> 'A')
	and e.id=x.id

delete FEWS_ENCABEZADO
from FEWS_ENCABEZADO e
where (e.resultado is null or e.resultado <> 'A')
commit tran

--Recupera lo proximo a autorizarse en AST por cada CUIT, TIPO COMPROBANTE, PUNTO VENTA
select CUIT_EMPRESA CE, TIPO_COMPROBANTE TC, PUNTO_VENTA PV, min(NUMERO_COMPROBANTE) NC, convert(int, TIPO_COMPROBANTE ) NTC, convert(int, PUNTO_VENTA) NPV, convert(int, min(NUMERO_COMPROBANTE)) NNC, 0 IDC
, (select top 1 id_interfaz from INTERFACES where CUIT_SUSCRIPCION=CUIT_EMPRESA COLLATE DATABASE_DEFAULT) ID_INTERFAZ
into #pen
from DRGFINNEGANS.FINN_ZBC.dbo.AST_FEWS_LOG
where RESULTADO is null or RESULTADO <> 'A'
group by CUIT_EMPRESA, TIPO_COMPROBANTE, PUNTO_VENTA

--Verifica que todos los CUIT EMPRESA tengan ID_INTERFAZ
if not exists(select top 1 CE from #pen where ID_INTERFAZ is null)
begin
	
	begin tran
	
	--Inserto los encabezados que no existen
	INSERT INTO FEWS_ENCABEZADO (
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
		l.MONEDA_CTZ,
		IMPORTE_TOTAL,
		NETO_GRAVADO,
		NETO_NOGRAVADO,
		NETO_EXENTO,
		IVA_TOTAL,
		TRIBUTOS_TOTAL
		, FECHA_COMPROBANTE
		, FECHA_COMPROBANTE
		, isnull(l.FECHA_VENC_PAGO, case when TIPO_COMPROBANTE in ('201','202','203','206','207','208','211','212','213') and FECHA_COMPROBANTE<convert(varchar(8), GETDATE(), 112) then convert(varchar(8), GETDATE(), 112) else FECHA_COMPROBANTE end)
		, right('00000'+CONVERT(varchar(5), p.ID_INTERFAZ),5)
			+right('000'+CONVERT(varchar(3), NTC),3)
			+right('0000'+CONVERT(varchar(4), NPV),4)
			+right('00000000'+CONVERT(varchar(8), NNC),8)
		, p.ID_INTERFAZ
	FROM DRGFINNEGANS.FINN_ZBC.dbo.AST_FEWS_LOG l
		, #pen p left outer join FEWS_ENCABEZADO e on e.cuit=p.CE COLLATE DATABASE_DEFAULT and e.tipo_cbte=p.NTC and e.punto_vta=p.NPV and e.cbte_nro=p.NNC
	WHERE CUIT_EMPRESA=CE
		and TIPO_COMPROBANTE=TC
		and PUNTO_VENTA=PV
		and NUMERO_COMPROBANTE=NC
		and e.id is null

	--Obtiene los ID de los encabezados insertados / actualizados
	update #pen
	set idc = id
	from FEWS_ENCABEZADO, #pen
	where cuit=CE COLLATE DATABASE_DEFAULT
		and tipo_cbte=NTC
		and punto_vta=NPV
		and cbte_nro=NNC
		
	--INSERTA EN FEWS_IVA
	INSERT INTO FEWS_IVA(
		ID,
		IVA_ID,
		BASE_IMP,
		IMPORTE)
	SELECT
		ID=IDC,
		IVA_ID= CASE AST_FEWS_LOG_IVA.PORCENTAJE
					WHEN 10.5 THEN	4
					WHEN 21 THEN	5
					WHEN 27 THEN	6
				END ,
		BASE_IMP=AST_FEWS_LOG_IVA.NETO_GRAVADO,
		IMPORTE=AST_FEWS_LOG_IVA.IMPORTE
	FROM DRGFINNEGANS.FINN_ZBC.dbo.AST_FEWS_LOG_IVA
		, #pen p left outer join FEWS_ENCABEZADO e on e.cuit=p.CE COLLATE DATABASE_DEFAULT and e.tipo_cbte=p.NTC and e.punto_vta=p.NPV and e.cbte_nro=p.NNC
	WHERE CUIT_EMPRESA=CE
		and TIPO_COMPROBANTE=TC
		and PUNTO_VENTA=PV
		and NUMERO_COMPROBANTE=NC
		and (e.resultado is null or e.resultado <> 'A')

	--INSERTA EN FEWS_TRIBUTO
	INSERT INTO FEWS_TRIBUTO(
		ID,
		TRIBUTO_ID,
		TRIBUTO_DESC,
		
		ALIC,
		BASE_IMP,
		
		IMPORTE)
	SELECT
		ID=IDC ,
		TRIBUTO_ID= AST_FEWS_LOG_TRIBUTOS.TRIBUTO_ID ,
		TRIBUTO_DESC=AST_FEWS_LOG_TRIBUTOS.DESCRPTION,

		ALIC=AST_FEWS_LOG_TRIBUTOS.PORCENTAJE,
		BASE_IMP=AST_FEWS_LOG_TRIBUTOS.NETO_GRAVADO,

		IMPORTE=AST_FEWS_LOG_TRIBUTOS.IMPORTE
	FROM DRGFINNEGANS.FINN_ZBC.dbo.AST_FEWS_LOG_TRIBUTOS
		, #pen p left outer join FEWS_ENCABEZADO e on e.cuit=p.CE COLLATE DATABASE_DEFAULT and e.tipo_cbte=p.NTC and e.punto_vta=p.NPV and e.cbte_nro=p.NNC
	WHERE CUIT_EMPRESA=CE
		and TIPO_COMPROBANTE=TC
		and PUNTO_VENTA=PV
		and NUMERO_COMPROBANTE=NC
		and (e.resultado is null or e.resultado <> 'A')

	--INSERTA EN FEWS_XML
	INSERT INTO FEWS_XML(
		ID)
	SELECT
		ID=IDC
	FROM #pen p left outer join FEWS_ENCABEZADO e on e.cuit=p.CE COLLATE DATABASE_DEFAULT and e.tipo_cbte=p.NTC and e.punto_vta=p.NPV and e.cbte_nro=p.NNC
	WHERE (e.resultado is null or e.resultado <> 'A')
		
	--INSERTA EN FEWS_CMP_ASOC
	INSERT INTO FEWS_CMP_ASOC(
		id,
		tipo_cbte,
		punto_vta,
		cbte_nro,
		cuit,
		fecha_cbte)
	SELECT
		ID=IDC,
		tipo_cbte=AST_FEWS_LOG_CBTE_ASOC.TIPO_COMPROBANTE_ASOC,
		punto_vta=AST_FEWS_LOG_CBTE_ASOC.PUNTO_VENTA_ASOC,
		cbte_nro=AST_FEWS_LOG_CBTE_ASOC.NUMERO_COMPROBANTE_ASOC,
		cuit=AST_FEWS_LOG_CBTE_ASOC.CUIT_ASOC,
		fecha_cbte=AST_FEWS_LOG_CBTE_ASOC.FECHA_COMPROBANTE_ASOC
	FROM DRGFINNEGANS.FINN_ZBC.dbo.AST_FEWS_LOG_CBTE_ASOC
		, #pen p left outer join FEWS_ENCABEZADO e on e.cuit=p.CE COLLATE DATABASE_DEFAULT and e.tipo_cbte=p.NTC and e.punto_vta=p.NPV and e.cbte_nro=p.NNC
	WHERE CUIT_EMPRESA=CE
		and TIPO_COMPROBANTE=TC
		and PUNTO_VENTA=PV
		and NUMERO_COMPROBANTE=NC
		and (e.resultado is null or e.resultado <> 'A')

	--INSERTA EN FEWS_DATO_OPC
	INSERT INTO FEWS_DATO_OPC(
		id,
		opcional_id,
		valor)
	SELECT
		ID=IDC,
		opcional_id=AST_FEWS_LOG_DATOS_OPC.ID_OPCIONAL,
		valor=AST_FEWS_LOG_DATOS_OPC.VALOR
	FROM DRGFINNEGANS.FINN_ZBC.dbo.AST_FEWS_LOG_DATOS_OPC
		, #pen p left outer join FEWS_ENCABEZADO e on e.cuit=p.CE COLLATE DATABASE_DEFAULT and e.tipo_cbte=p.NTC and e.punto_vta=p.NPV and e.cbte_nro=p.NNC
	WHERE CUIT_EMPRESA=CE
		and TIPO_COMPROBANTE=TC
		and PUNTO_VENTA=PV
		and NUMERO_COMPROBANTE=NC
		and (e.resultado is null or e.resultado <> 'A')
	
	--INSERTA EN FEWS_QR
	INSERT INTO FEWS_QR(
		ID)
	SELECT
		ID=IDC
	FROM #pen p left outer join FEWS_ENCABEZADO e on e.cuit=p.CE COLLATE DATABASE_DEFAULT and e.tipo_cbte=p.NTC and e.punto_vta=p.NPV and e.cbte_nro=p.NNC
	WHERE (e.resultado is null or e.resultado <> 'A')
	
	commit tran
end 

--Actualizo los encabezados que existen
UPDATE DRGFINNEGANS.FINN_ZBC.dbo.AST_FEWS_LOG
SET resultado='D',
	cae=0,
	err_msg='ERROR - COMPROBANTE PENDIENTE YA EXISTE AUTORIZADO - VERIFIQUE LA CONSOLA',
	obs='CAE: '+ CONVERT(varchar(20), e.cae) + ' VEN: '+ e.fecha_vto + ' TOTAL: ' + convert(varchar(20), e.imp_total),
	codigo='14002',
	descripcion='ERROR - COMPROBANTE PENDIENTE YA EXISTE AUTORIZADO - VERIFIQUE LA CONSOLA'
FROM FEWS_ENCABEZADO e
	, FEWS_XML x
	, DRGFINNEGANS.FINN_ZBC.dbo.AST_FEWS_LOG l
	, #pen p
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

--por ahora no se usa, se envia un default ...
select 0 _codigo_, 'OK' _descripcion_, 'FINALIZADO' _observacion_

--por ahora no se usa, se envia un default ...
RETURN 0	
SET NOCOUNT OFF
END

GO