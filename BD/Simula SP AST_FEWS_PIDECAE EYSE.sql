USE [FINN_EYSE]
GO

drop table #AST_FEWS_LOG
drop table #AST_FEWS_LOG_IVA
drop table #AST_FEWS_LOG_TRIBUTOS
drop table #AST_FEWS_LOG_DATOS_OPC
drop table #AST_FEWS_LOG_CBTE_ASOC
drop table #AST_FEWS_LOG_PERIODO_ASOC
drop table #ITEMS
drop table #APLICA
go

select * into #AST_FEWS_LOG              from AST_FEWS_LOG              where 1=2
select * into #AST_FEWS_LOG_IVA          from AST_FEWS_LOG_IVA          where 1=2
select * into #AST_FEWS_LOG_TRIBUTOS     from AST_FEWS_LOG_TRIBUTOS     where 1=2
select * into #AST_FEWS_LOG_DATOS_OPC    from AST_FEWS_LOG_DATOS_OPC    where 1=2
select * into #AST_FEWS_LOG_CBTE_ASOC	 from AST_FEWS_LOG_CBTE_ASOC    where 1=2
select * into #AST_FEWS_LOG_PERIODO_ASOC from AST_FEWS_LOG_PERIODO_ASOC where 1=2

USE [FINN_EYSE]
GO
/****** Object:  StoredProcedure [dbo].[AST_FEWS_PIDECAE]    Script Date: 05/06/2021 08:56:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

/*

--- vinculación al servidor de la consola web

USE [master]
EXEC master.dbo.sp_addlinkedserver @server = N'FINNGS-TEST-01', @srvproduct=N'SQL Server'
EXEC master.dbo.sp_addlinkedsrvlogin @rmtsrvname = N'FINNGS-TEST-01', @locallogin = 'sa' , @useself = N'True' 

sp_configure 'show advanced option', '1'

RECONFIGURE; 
EXEC sp_configure;F

USE master; 
GO 
EXEC sp_configure 'lightweight pooling', '0'; 
RECONFIGURE WITH OVERRIDE;
SELECT name FROM [FINNGS-TEST-01].master.sys.databases 


-- generador movimientos de prueba
select 'exec AST_FEWS_PIDECAE', as_id,', true' from Asiento where AS_Fecha>='01-01-2016' and DOC_ID=818


exec AST_FEWS_PIDECAE	611144	, true
SELECT DBO.AST_FEWS_GET_RESULTADO_CAE (582796)
SELECT resultado, cae,* FROM #AST_FEWS_LOG WHERE AS_ID=582796
select * from asiento where as_numero=3114

SELECT MIN(NUMERO_COMPROBANTE) FROM #AST_FEWS_LOG WHERE TIPO_COMPROBANTE='06'

 SELECT  REPLICATE('0',8-LEN(CAST(CAST(NUMERO_COMPROBANTE AS INT)-1664 AS VARCHAR(8))))+
  CAST(CAST(NUMERO_COMPROBANTE AS INT)-1664 AS VARCHAR(8)),* FROM #AST_FEWS_LOG WHERE TIPO_COMPROBANTE='01'
  
  UPDATE #AST_FEWS_LOG
  SET NUMERO_COMPROBANTE=REPLICATE('0',8-LEN(CAST(CAST(NUMERO_COMPROBANTE AS INT)-1262 AS VARCHAR(8))))+
  CAST(CAST(NUMERO_COMPROBANTE AS INT)-1262 AS VARCHAR(8))
  FROM #AST_FEWS_LOG WHERE TIPO_COMPROBANTE='06'
  
    UPDATE #AST_FEWS_LOG_IVA
  SET NUMERO_COMPROBANTE=REPLICATE('0',8-LEN(CAST(CAST(NUMERO_COMPROBANTE AS INT)-1262 AS VARCHAR(8))))+
  CAST(CAST(NUMERO_COMPROBANTE AS INT)-1262 AS VARCHAR(8))
  FROM #AST_FEWS_LOG_IVA WHERE TIPO_COMPROBANTE='06'
  
  
   SELECT  REPLICATE('0',8-LEN(CAST(CAST(NUMERO_COMPROBANTE AS INT)-1664 AS VARCHAR(8))))+
  CAST(CAST(NUMERO_COMPROBANTE AS INT)-1664 AS VARCHAR(8)),* FROM #AST_FEWS_LOG_IVA WHERE TIPO_COMPROBANTE='01'
  
  UPDATE #AST_FEWS_LOG_IVA
  SET CUIT_EMPRESA='30711897581'
  
 SELECT * FROM #AST_FEWS_LOG 
 SELECT * FROM #AST_FEWS_LOG_IVA
  SELECT * FROM #AST_FEWS_LOG_TRIBUTOS
  
 
  
 SELECT * FROM ASIENTO WHERE AS_NUMERO=3115
delete FROM #AST_FEWS_LOG
delete FROM #AST_FEWS_LOG_IVA
delete FROM #AST_FEWS_LOG_TRIBUTOS

-- exec AST_FEWS_PIDECAE 797523, true
select * from asiento where as_numero=3103
 */

/*
select TIPO_COMPROBANTE=(select comprt_alias from comprobantetipo where comprobantetipo.COMPRT_ID=
				(select comprt_id from Talonario where talonario.TAL_ID=
				(SELECT tal_id FROM Documentotipotalonario where DOC_ID=asiento.DOC_ID and cf_id=
				(select cf_id from Tercero where tercero.te_id=asiento.TE_ID)))), as_id
from asiento 
where as_numerodoc = 'A000500000001'
*/

declare @@AS_ID INT = 720612
declare @@FLAG BIT = 1   

BEGIN    
DECLARE @R VARCHAR(100)
SET @R='0'
DECLARE  @FLAG BIT
SET @FLAG=@@FLAG


IF @FLAG=1
BEGIN
	declare @tiempo datetime
	set @tiempo=GETDATE()
	
-- 	exec AST_FEWS_PIDECAE	582789	, true
	
	IF isnull((SELECT ISNULL(cae,'') FROM #AST_FEWS_LOG WHERE isnull(AS_ID,-1)=@@AS_ID and isnull(resultado,'')='A'),'')=''
	
	  -- (SELECT ISNULL(cae,'') FROM #AST_FEWS_LOG WHERE isnull(AS_ID,-1)=611169 and isnull(resultado,'')='A')
	BEGIN
 --SELECT * FROM #AST_FEWS_LOG
			--- SI HAY REINTENTO, SE BORRAN LOS REGISTROS ANTERIORIES Y SE VUELVEN A GENERAR
			---
			DELETE FROM #AST_FEWS_LOG WHERE AS_ID=@@AS_ID
			DELETE FROM #AST_FEWS_LOG_IVA WHERE AS_ID=@@AS_ID
			DELETE FROM #AST_FEWS_LOG_TRIBUTOS WHERE AS_ID=@@AS_ID
			DELETE FROM #AST_FEWS_LOG_CBTE_ASOC WHERE AS_ID=@@AS_ID
			DELETE FROM #AST_FEWS_LOG_DATOS_OPC WHERE AS_ID=@@AS_ID
			DELETE FROM #AST_FEWS_LOG_PERIODO_ASOC WHERE AS_ID=@@AS_ID

			--- OBTIENE EL CUIT DE LA EMPRESA LOGEADA
			declare @EmpresaCuit varchar(11)
			
			SET @EmpresaCuit= (select VALOR from configuracion where aspecto='empresacuit' AND 
GRUPO ='FAF_'+CAST((select EMPR_ID from CIRCUITOCONTABLE WHERE CC_ID=
(SELECT CC_ID FROM DocumentoTipoCircuitoContable WHERE DOC_ID=
((SELECT DOC_ID FROM ASIENTO WHERE AS_ID=@@AS_ID)))) AS VARCHAR(4)))



		 -----------------------			
		 ----------------------- test de fc de credito
		 -----------------------
		 --SET @EmpresaCuit= case when (select (select comprt_alias from comprobantetipo where comprobantetipo.COMPRT_ID=
			--				(select comprt_id from Talonario where talonario.TAL_ID=
			--				(SELECT tal_id FROM Documentotipotalonario where DOC_ID=asiento.DOC_ID and cf_id=
			--				(select cf_id from Tercero where tercero.te_id=asiento.TE_ID))))
			--						from asiento where asiento.as_id=@@AS_ID) IN (201, 202, 203, 206, 207, 208)
		 --                   then '20225925055'
		 --                   else @EmpresaCuit
		 --                   end

			declare @EmpresaCBU varchar(22)
			
			SET @EmpresaCBU = (select CBU  from Empresa where Empresa.EMPR_ID=CAST((select EMPR_ID from CIRCUITOCONTABLE WHERE CC_ID=
					(SELECT CC_ID FROM DocumentoTipoCircuitoContable WHERE DOC_ID=
					((SELECT DOC_ID FROM ASIENTO WHERE AS_ID=@@AS_ID)))) AS VARCHAR(4)))

			-- select * from asiento
			----
			----	TOMA LOS PARAMETROS DEFINIDOS EN EXPOSICION FISCAL
			----
			DECLARE @EXPOFISCAL INT
			SET @EXPOFISCAL=(SELECT EXFISC_ID FROM ExposicionFiscal WHERE EXFISC_aLIAS='FEWS')

			---		
			---   RECOPILA INFORMACIÓN DE ITEMS DE LA FACTURA
			---
			CREATE TABLE #ITEMS(
			PR_ID INT,
			PR_LEVASTOCK INT,
			TI_ID INT,
			TI_PORCENTAJE REAL,
			PRECIO MONEY,
			CANTIDAD REAL,
			IMPORTE MONEY,
			GRAVADO MONEY,
			NO_GRAVADO MONEY,
			CUE_ID INT,
			EXFISC_CONCEPTONOMBRE VARCHAR(200),
			CONC_ID INT
			)


			INSERT INTO #ITEMS (PR_ID, PR_LEVASTOCK, TI_ID, TI_PORCENTAJE, PRECIO, CANTIDAD, IMPORTE, GRAVADO, NO_GRAVADO,
								CUE_ID,EXFISC_CONCEPTONOMBRE, CONC_ID)
			SELECT 
			PR_ID,
			(SELECT PR_LLEVASTOCK FROM Producto WHERE PRODUCTO.PR_ID=VENTAITEM.PR_ID),
			(SELECT TI_ID FROM Producto WHERE PRODUCTO.PR_ID=VENTAITEM.PR_ID),
			PORCE=CASE ISNULL(VENTAITEM.PR_ID,0)
					WHEN 0 THEN VENTAITEM.VTAI_AUX1
					ELSE  (SELECT TI_PORCENTAJE FROM TasaImpositiva WHERE TI_ID=(SELECT TI_ID FROM Producto WHERE PRODUCTO.PR_ID=VENTAITEM.PR_ID))
					END,
			VTAI_PRECIO,
			VTAI_Cantidad,
			VTAI_Importe,
			GRAVADO= CASE ISNULL((SELECT TI_PORCENTAJE FROM TasaImpositiva WHERE TI_ID=(SELECT TI_ID FROM Producto WHERE PRODUCTO.PR_ID=VENTAITEM.PR_ID)),-1)
						WHEN 0 THEN 0
						WHEN -1 THEN 0
						ELSE VTAI_Importe
					END,
			NO_GRAVADO= CASE (SELECT TI_PORCENTAJE FROM TasaImpositiva WHERE TI_ID=(SELECT TI_ID FROM Producto WHERE PRODUCTO.PR_ID=VENTAITEM.PR_ID))
						WHEN 0 THEN VTAI_Importe
						ELSE  case when (select EXPOSICIONFISCALCUENTA.EXFISCCUE_ConceptoNombre FROM  exposicionfiscalCUENTA WHERE exposicionfiscalCUENTA.CUE_id=VENTAITEM.CUE_id AND EXFISC_ID=@EXPOFISCAL)<>'IVA'
								THEN VTAI_Importe
							ELSE 
								0
							 END
					END,
			CUE_id=VentaItem.CUE_id,
			EXFISC_CONCEPTONOMBRE=(select EXPOSICIONFISCALCUENTA.EXFISCCUE_ConceptoNombre FROM  exposicionfiscalCUENTA WHERE exposicionfiscalCUENTA.CUE_id=VENTAITEM.CUE_id AND EXFISC_ID=@EXPOFISCAL),
			CONC_ID=VENTAITEM.CONC_ID


			FROM VentaItem
			WHERE
			--VENTAITEM.PR_ID IS NOT NULL AND
			 VTA_ID=@@AS_ID

			SELECT * FROM #ITEMS
			
			
			--- 
			--- ESTABLECE SI LA FACTURA ES DE PRODUCTO O SERVICIO
			---

			DECLARE @PRODUCTOS INT
			DECLARE @SERVICIOS INT

			SET @PRODUCTOS=(SELECT COUNT(*) FROM #ITEMS WHERE PR_LEVASTOCK=1)
			SET @SERVICIOS=(SELECT COUNT(*) FROM #ITEMS WHERE PR_LEVASTOCK=0)

			--SELECT @PRODUCTOS, @SERVICIOS

			---
			--- INSERTA EN 	#AST_FEWS_LOG
			---
						
			INSERT INTO #AST_FEWS_LOG(
				CUIT_EMPRESA,
				TIPODOC_CLIENTE,
				NRODOC_CLIENTE,
				AS_ID,
				TIPO_COMPROBANTE,
				PUNTO_VENTA,
				NUMERO_COMPROBANTE,
				FECHA_COMPROBANTE,
				CONCEPTO_FACTURA,
				MONEDA,
				MONEDA_CTZ,
				IMPORTE_TOTAL,
				NETO_NOGRAVADO,
				NETO_GRAVADO,
				NETO_EXENTO,
				IVA_TOTAL,
				TRIBUTOS_TOTAL,
				resultado)

			 SELECT 
				CUIT_EMPRESA= @EmpresaCuit,
				TIPODOC_CLIENTE=(select te_TIPODOC from Tercero where tercero.te_id=asiento.te_id),
				NRODOC_CLIENTE=(select te_cuit from Tercero where tercero.te_id=asiento.te_id),
				AS_ID=ASIENTO.AS_ID,
				
				TIPO_COMPROBANTE=(select comprt_alias from comprobantetipo where comprobantetipo.COMPRT_ID=
				(select comprt_id from Talonario where talonario.TAL_ID=
				(SELECT tal_id FROM Documentotipotalonario where DOC_ID=asiento.DOC_ID and cf_id=
				(select cf_id from Tercero where tercero.te_id=asiento.TE_ID)))),
				
				PUNTO_VENTA=(select RIGHT(Talonario.TAL_PREFIJO, 4) from Talonario where talonario.TAL_ID=
				(SELECT tal_id FROM Documentotipotalonario where DOC_ID=asiento.DOC_ID and cf_id=
				(select cf_id from Tercero where tercero.te_id=asiento.TE_ID))),
				
				NUMERO_COMPROBANTE=RIGHT(ASIENTO.AS_NUMERODOC,8),
				FECHA_COMPROBANTE=CONVERT(VARCHAR(8),Asiento.as_FECHA,112),
				CONCEPTO_FACTURA=CASE
								WHEN  @PRODUCTOS>0 AND @SERVICIOS=0 THEN 1
								WHEN  @PRODUCTOS=0 AND @SERVICIOS>0 THEN 2
								ELSE 3
								END,
					--SI TODOS LOS ITEMS SON STOKEABLES = 1
					--SI TODOS LOS ITEMS NO SON STOCKEABLES =2
					--SI ES UN MIX=3
				MONEDA=(SELECT MON_CODIGOAFIP FROM MONEDA WHERE MONEDA.MON_ID=ASIENTO.MON_ID),
						-- SELECT * FROM MONEDA
				MONEDA_CTZ=CASE (SELECT MON_CODIGOAFIP FROM MONEDA WHERE MONEDA.MON_ID=ASIENTO.MON_ID)
								WHEN 'PES' THEN 1
								ELSE ISNULL(ASIENTO.AS_MonedaCambio,1)
							END,
				IMPORTE_TOTAL=case mon_id
								when -1 then ISNULL(ASIENTO.AS_TOTAL,0)
								else ISNULL(ASIENTO.AS_TOTAL,0)/ISNULL(asiento.AS_MonedaCambio,1)
								end,
				NETO_NOGRAVADO=ISNULL((SELECT SUM(NO_GRAVADO) FROM #ITEMS where TI_PORCENTAJE=0),0),
				NETO_GRAVADO=ISNULL((SELECT SUM(GRAVADO) FROM #ITEMS),0),
				NETO_EXENTO=0,
				--(case asiento.mon_id
				--				when -1 then ISNULL(ASIENTO.AS_TOTAL,0)
				--				else ISNULL(ASIENTO.AS_TOTAL,0)/ISNULL(asiento.AS_MonedaCambio,1)
				--				end)-ISNULL((SELECT SUM(NO_GRAVADO) FROM #ITEMS),0)-ISNULL((SELECT SUM(GRAVADO) FROM #ITEMS),0),
				IVA_TOTAL=ISNULL((SELECT SUM(IMPORTE) FROM #ITEMS WHERE EXFISC_CONCEPTONOMBRE='IVA'),0),
				TRIBUTOS_TOTAL=ISNULL((SELECT SUM(NO_GRAVADO) FROM #ITEMS WHERE EXFISC_CONCEPTONOMBRE<>'IVA' AND PR_ID IS NULL and TI_PORCENTAJE<>0),0),
				RESULTADO='0'

			 FROM Asiento 
			 WHERE 
			 ASIENTO.AS_ID=@@AS_ID  
	select * from #AST_FEWS_LOG		 where AS_ID=@@AS_ID 

------------------------------------------------------------------------------------------------------ caso eyse
	update #AST_FEWS_LOG
set NETO_NOGRAVADO=IMPORTE_TOTAL
from #AST_FEWS_LOG
where
cast(IMPORTE_TOTAL as money)+ cast(neto_nogravado as money)+cast(NETO_GRAVADO as money)+ cast(NETO_EXENTO as money)+
 cast(IVA_TOTAL as money)+ cast(TRIBUTOS_TOTAL as money) = cast(IMPORTE_TOTAL as money)
 and TIPO_COMPROBANTE in ('02','07', '202')
 and AS_ID=@@AS_ID
 ----------------------------------------------------------------------------------------------------------
			--- 
			--- INSERTA EN #AST_FEWS_LOG_IVA 
			---

			 INSERT INTO #AST_FEWS_LOG_IVA(
					AS_ID,
					CUIT_EMPRESA,
					TIPO_COMPROBANTE,
					PUNTO_VENTA,
					NUMERO_COMPROBANTE,
					PORCENTAJE,
					NETO_GRAVADO,
					IMPORTE)
					
			SELECT 
					AS_ID=@@AS_ID,
					CUIT_EMPRESA= @EmpresaCuit,
					TIPO_COMPROBANTE=#AST_FEWS_LOG.TIPO_COMPROBANTE,
					PUNTO_VENTA=#AST_FEWS_LOG.PUNTO_VENTA,
					NUMERO_COMPROBANTE=#AST_FEWS_LOG.NUMERO_COMPROBANTE,
					PORCENTAJE=#ITEMS.TI_PORCENTAJE,
					NETO_GRAVADO=ISNULL((SELECT SUM(i.GRAVADO) FROM #ITEMS as i where i.TI_PORCENTAJE=#items.ti_porcentaje),0),
					IMPORTE=#ITEMS.IMPORTE
					
			FROM #ITEMS, #AST_FEWS_LOG WHERE
			#AST_FEWS_LOG.AS_ID=@@AS_ID AND 
			#ITEMS.EXFISC_CONCEPTONOMBRE='IVA' AND
			#ITEMS.TI_PORCENTAJE<>0 
			and #ITEMS.IMPORTE<>0
			--AND (SELECT COUNT(*) FROM Concepto WHERE CONCEPTO.CONC_ID=#ITEMS.CONC_ID AND LEFT(CONC_ALIAS,3)='IVA')<>0 -- MODIFICACION PARA EYSE
			
			
			INSERT INTO #AST_FEWS_LOG_TRIBUTOS(
						AS_ID,
						CUIT_EMPRESA,
						TIPO_COMPROBANTE,
						PUNTO_VENTA,
						NUMERO_COMPROBANTE,
						TRIBUTO_ID,
						DESCRPTION, --ALIAS DE IMPUESTO
						PORCENTAJE, -- FORMATO 0.2100
						NETO_GRAVADO,
						IMPORTE)
						
			SELECT 
					AS_ID=@@AS_ID,
					CUIT_EMPRESA= @EmpresaCuit,
					TIPO_COMPROBANTE=#AST_FEWS_LOG.TIPO_COMPROBANTE,
					PUNTO_VENTA=#AST_FEWS_LOG.PUNTO_VENTA,
					NUMERO_COMPROBANTE=#AST_FEWS_LOG.NUMERO_COMPROBANTE,
					TRIBUTO_ID=ISNULL(#ITEMS.EXFISC_CONCEPTONOMBRE,0),
					DESCRPTION=(SELECT CONC_ALIAS FROM Concepto WHERE CONCEPTO.CONC_ID=#ITEMS.CONC_ID), --ALIAS DE IMPUESTO
					PORCENTAJE=#ITEMS.TI_PORCENTAJE,
					NETO_GRAVADO=ISNULL((SELECT SUM(GRAVADO) FROM #ITEMS),0),
					IMPORTE=#ITEMS.IMPORTE
				FROM #ITEMS, #AST_FEWS_LOG WHERE
			#AST_FEWS_LOG.AS_ID=@@AS_ID AND 
				#ITEMS.PR_ID IS NULL AND
				ISNULL(#ITEMS.EXFISC_CONCEPTONOMBRE,0)<>'IVA' AND
				ISNULL(#ITEMS.EXFISC_CONCEPTONOMBRE,0)<>0 AND
				#ITEMS.TI_PORCENTAJE<>0
				--AND (SELECT COUNT(*) FROM Concepto WHERE CONCEPTO.CONC_ID=#ITEMS.CONC_ID AND LEFT(CONC_ALIAS,3)<>'IVA')<>0 -- MODIFICACION PARA EYSE
			

			--SELECT * FROM #AST_FEWS_LOG_PERIODO_ASOC

			
			
				
				
		--------- TABLAS DE FACTURA DE CRÈDITO  #AST_FEWS_LOG_DATOS_OPC
		
			/*		
			
			------------------------------- FACTURAS
			
			declare @as_id int
			set @as_id=787748
			 select as_id ,doc_nombre, as_numerodoc  from documentotipo , asiento where documentotipo.doc_id=asiento.doc_id and as_id=@as_id
			exec AST_FEWS_PIDECAE @as_id, true
			select '#AST_FEWS_LOG',* FROM #AST_FEWS_LOG WHERE AS_ID=@AS_ID
			select '#AST_FEWS_LOG_IVA',*  FROM #AST_FEWS_LOG_IVA WHERE AS_ID=@AS_ID
			select '#AST_FEWS_LOG_TRIBUTOS',*  FROM #AST_FEWS_LOG_TRIBUTOS WHERE AS_ID=@AS_ID
			select '#AST_FEWS_LOG_CBTE_ASOC',*  FROM #AST_FEWS_LOG_CBTE_ASOC WHERE AS_ID=@AS_ID
			select '#AST_FEWS_LOG_DATOS_OPC',*  FROM #AST_FEWS_LOG_DATOS_OPC WHERE AS_ID=@AS_ID
			
			declare @as_id int
			set @as_id=787749
			 select as_id ,doc_nombre, as_numerodoc  from documentotipo , asiento where documentotipo.doc_id=asiento.doc_id and as_id=@as_id
			 exec AST_FEWS_PIDECAE @as_id, true
			select '#AST_FEWS_LOG',* FROM #AST_FEWS_LOG WHERE AS_ID=@AS_ID
			select '#AST_FEWS_LOG_IVA',*  FROM #AST_FEWS_LOG_IVA WHERE AS_ID=@AS_ID
			select '#AST_FEWS_LOG_TRIBUTOS',*  FROM #AST_FEWS_LOG_TRIBUTOS WHERE AS_ID=@AS_ID
			select '#AST_FEWS_LOG_CBTE_ASOC',*  FROM #AST_FEWS_LOG_CBTE_ASOC WHERE AS_ID=@AS_ID
			select '#AST_FEWS_LOG_DATOS_OPC',*  FROM #AST_FEWS_LOG_DATOS_OPC WHERE AS_ID=@AS_ID


			declare @as_id int
			set @as_id= 787750
			 select as_id ,doc_nombre, as_numerodoc  from documentotipo , asiento where documentotipo.doc_id=asiento.doc_id and as_id=@as_id
			 exec AST_FEWS_PIDECAE @as_id, true
			select '#AST_FEWS_LOG',* FROM #AST_FEWS_LOG WHERE AS_ID=@AS_ID
			select '#AST_FEWS_LOG_IVA',*  FROM #AST_FEWS_LOG_IVA WHERE AS_ID=@AS_ID
			select '#AST_FEWS_LOG_TRIBUTOS',*  FROM #AST_FEWS_LOG_TRIBUTOS WHERE AS_ID=@AS_ID
			select '#AST_FEWS_LOG_CBTE_ASOC',*  FROM #AST_FEWS_LOG_CBTE_ASOC WHERE AS_ID=@AS_ID
			select '#AST_FEWS_LOG_DATOS_OPC',*  FROM #AST_FEWS_LOG_DATOS_OPC WHERE AS_ID=@AS_ID



			declare @as_id int
			set @as_id= 787751
			 select as_id ,doc_nombre, as_numerodoc  from documentotipo , asiento where documentotipo.doc_id=asiento.doc_id and as_id=@as_id
			 --exec AST_FEWS_PIDECAE @as_id, true
			select '#AST_FEWS_LOG',* FROM #AST_FEWS_LOG WHERE AS_ID=@AS_ID
			select '#AST_FEWS_LOG_IVA',*  FROM #AST_FEWS_LOG_IVA WHERE AS_ID=@AS_ID
			select '#AST_FEWS_LOG_TRIBUTOS',*  FROM #AST_FEWS_LOG_TRIBUTOS WHERE AS_ID=@AS_ID
			select '#AST_FEWS_LOG_CBTE_ASOC',*  FROM #AST_FEWS_LOG_CBTE_ASOC WHERE AS_ID=@AS_ID
			select '#AST_FEWS_LOG_DATOS_OPC',*  FROM #AST_FEWS_LOG_DATOS_OPC WHERE AS_ID=@AS_ID
			
			delete FROM #AST_FEWS_LOG WHERE AS_ID=@AS_ID
			delete  FROM #AST_FEWS_LOG_IVA WHERE AS_ID=@AS_ID
			delete  FROM #AST_FEWS_LOG_TRIBUTOS WHERE AS_ID=@AS_ID
			delete   FROM #AST_FEWS_LOG_CBTE_ASOC WHERE AS_ID=@AS_ID
			delete  FROM #AST_FEWS_LOG_DATOS_OPC WHERE AS_ID=@AS_ID
			
			
			------------------------------ NC SOBRE TOTAL FC
			declare @as_id int
			set @as_id= 787752
			 select as_id ,doc_nombre, as_numerodoc  from documentotipo , asiento where documentotipo.doc_id=asiento.doc_id and as_id=@as_id
			 exec AST_FEWS_PIDECAE @as_id, true
			select '#AST_FEWS_LOG',* FROM #AST_FEWS_LOG WHERE AS_ID=@AS_ID
			select '#AST_FEWS_LOG_IVA',*  FROM #AST_FEWS_LOG_IVA WHERE AS_ID=@AS_ID
			select '#AST_FEWS_LOG_TRIBUTOS',*  FROM #AST_FEWS_LOG_TRIBUTOS WHERE AS_ID=@AS_ID
			select '#AST_FEWS_LOG_CBTE_ASOC',*  FROM #AST_FEWS_LOG_CBTE_ASOC WHERE AS_ID=@AS_ID
			select '#AST_FEWS_LOG_DATOS_OPC',*  FROM #AST_FEWS_LOG_DATOS_OPC WHERE AS_ID=@AS_ID
			
			
			
			------------------------ NOTA DE CREDITO
			declare @as_id int
			set @as_id= 787753
			 select as_id ,doc_nombre, as_numerodoc  from documentotipo , asiento where documentotipo.doc_id=asiento.doc_id and as_id=@as_id
			 exec AST_FEWS_PIDECAE @as_id, true
			select '#AST_FEWS_LOG',* FROM #AST_FEWS_LOG WHERE AS_ID=@AS_ID
			select '#AST_FEWS_LOG_IVA',*  FROM #AST_FEWS_LOG_IVA WHERE AS_ID=@AS_ID
			select '#AST_FEWS_LOG_TRIBUTOS',*  FROM #AST_FEWS_LOG_TRIBUTOS WHERE AS_ID=@AS_ID
			select '#AST_FEWS_LOG_CBTE_ASOC',*  FROM #AST_FEWS_LOG_CBTE_ASOC WHERE AS_ID=@AS_ID
			select '#AST_FEWS_LOG_DATOS_OPC',*  FROM #AST_FEWS_LOG_DATOS_OPC WHERE AS_ID=@AS_ID


            -------------------------- NOTA DE DEBITO
			declare @as_id int
			set @as_id= 787754
			 select as_id ,doc_nombre, as_numerodoc  from documentotipo , asiento where documentotipo.doc_id=asiento.doc_id and as_id=@as_id
			 exec AST_FEWS_PIDECAE @as_id, true
			select '#AST_FEWS_LOG',* FROM #AST_FEWS_LOG WHERE AS_ID=@AS_ID
			select '#AST_FEWS_LOG_IVA',*  FROM #AST_FEWS_LOG_IVA WHERE AS_ID=@AS_ID
			select '#AST_FEWS_LOG_TRIBUTOS',*  FROM #AST_FEWS_LOG_TRIBUTOS WHERE AS_ID=@AS_ID
			select '#AST_FEWS_LOG_CBTE_ASOC',*  FROM #AST_FEWS_LOG_CBTE_ASOC WHERE AS_ID=@AS_ID
			select '#AST_FEWS_LOG_DATOS_OPC',*  FROM #AST_FEWS_LOG_DATOS_OPC WHERE AS_ID=@AS_ID
			
	
	        ------- NOTA DE DEBITO
			declare @as_id int
			set @as_id= 787755
			 select as_id ,doc_nombre, as_numerodoc  from documentotipo , asiento where documentotipo.doc_id=asiento.doc_id and as_id=@as_id
			 exec AST_FEWS_PIDECAE @as_id, true
			select '#AST_FEWS_LOG',* FROM #AST_FEWS_LOG WHERE AS_ID=@AS_ID
			select '#AST_FEWS_LOG_IVA',*  FROM #AST_FEWS_LOG_IVA WHERE AS_ID=@AS_ID
			select '#AST_FEWS_LOG_TRIBUTOS',*  FROM #AST_FEWS_LOG_TRIBUTOS WHERE AS_ID=@AS_ID
			select '#AST_FEWS_LOG_CBTE_ASOC',*  FROM #AST_FEWS_LOG_CBTE_ASOC WHERE AS_ID=@AS_ID
			select '#AST_FEWS_LOG_DATOS_OPC',*  FROM #AST_FEWS_LOG_DATOS_OPC WHERE AS_ID=@AS_ID	

			*/


		insert into #AST_FEWS_LOG_DATOS_OPC
          (AS_ID,
          CUIT_EMPRESA,
          TIPO_COMPROBANTE,
          PUNTO_VENTA,
          NUMERO_COMPROBANTE,
          ID_OPCIONAL,
          VALOR)
		Select 
			@@AS_ID,
			CUIT_EMPRESA= @EmpresaCuit,
			TIPO_COMPROBANTE=#AST_FEWS_LOG.TIPO_COMPROBANTE,
			PUNTO_VENTA=#AST_FEWS_LOG.PUNTO_VENTA,
			NUMERO_COMPROBANTE=#AST_FEWS_LOG.NUMERO_COMPROBANTE,
			ID_OPCIONAL= case when  TIPO_COMPROBANTE IN(203,208,202,207) then 22 else 2101 end,
		--	VALOR=  case when  TIPO_COMPROBANTE IN(203,208,202,207) then 'S' else  @EmpresaCBU end
		    VALOR=  case when  TIPO_COMPROBANTE IN( 203,208,207) 
				then 'N' 
				else  
				case when  TIPO_COMPROBANTE IN(202) 
					then 'S'
					else @EmpresaCBU 
				end
		    end
		FROM #AST_FEWS_LOG
		WHERE 
			#AST_FEWS_LOG.AS_ID=@@AS_ID
			AND  TIPO_COMPROBANTE IN ( 201,203,208,202,207)
		------------------------------------------------ RESOLUCIÓN PARA FCE,M VIGENTE A PARTIR DEL 1-4-2021
		UNION ALL
				Select 
			@@AS_ID,
			CUIT_EMPRESA= @EmpresaCuit,
			TIPO_COMPROBANTE=#AST_FEWS_LOG.TIPO_COMPROBANTE,
			PUNTO_VENTA=#AST_FEWS_LOG.PUNTO_VENTA,
			NUMERO_COMPROBANTE=#AST_FEWS_LOG.NUMERO_COMPROBANTE,
			ID_OPCIONAL= 27,
		--	VALOR=  case when  TIPO_COMPROBANTE IN(203,208,202,207) then 'S' else  @EmpresaCBU end
		    VALOR=  'ADC'
		FROM #AST_FEWS_LOG
		WHERE 
			#AST_FEWS_LOG.AS_ID=@@AS_ID
			AND  TIPO_COMPROBANTE IN (201, 206, 211)
		
			
	--------------- FECHA DE VENCIMIENTO DEN FCE		
			UPDATE #AST_FEWS_LOG
			SET FECHA_VENC_PAGO=	
				--case when CONCEPTO_FACTURA <> 1 then 
				-- select 
			CONVERT(VARCHAR(8),
				isnull((select top 1 credito.crd_FechaReal from credito where credito.AS_ID=#AST_FEWS_LOG.AS_ID order by credito.crd_FechaReal desc), (select a.as_FEcha +1 from Asiento as a  where a.AS_ID=#AST_FEWS_LOG.AS_ID))
				,112)
				--end
			FROM #AST_FEWS_LOG
			WHERE 
			#AST_FEWS_LOG.AS_ID=@@AS_ID 
			AND   TIPO_COMPROBANTE IN (201,206)
		
		--------- TABLAS DE FACTURA DE CRÈDITO SOLAMENTE NC/ND
		
		create table #aplica(
				as_id_aplica INT,
				TIPO_COMPROBANTE_ASOC varchar(3),
				PUNTO_VENTA_ASOC varchar(10) NULL,
				NUMERO_COMPROBANTE_ASOC varchar(20) NULL,
				CUIT_ASOC varchar(20) NULL,
				FECHA_COMPROBANTE_ASOC varchar(10) NULL,
				te_id int null,
				doc_id int null)
				
		insert into  #aplica(as_id_aplica, te_id,doc_id)
		select  DISTINCT
		as_id =credito.as_id ,
		te_id =(select te_id from Asiento as a where a.as_id=credito.as_id ),
		doc_id =(select doc_id from Asiento as a where a.as_id=credito.as_id )
		from debito, credito, CreditoDebitoCancelacion
		where 
			 debito.AS_ID = @@AS_ID
			 and CreditoDebitoCancelacion.deb_id=debito.DEB_ID
			 and credito.crd_id=CreditoDebitoCancelacion.crd_id 
			 and (select COUNT(*) from #AST_FEWS_LOG_CBTE_ASOC where #AST_FEWS_LOG_CBTE_ASOC.AS_ID=@@AS_ID
			 )=0
			 
		update #aplica
		set 
			-- select 
			 TIPO_COMPROBANTE_asoc =isnull((select isnull(comprt_alias,'01') from comprobantetipo where comprobantetipo.COMPRT_ID=
				(select comprt_id from Talonario where talonario.TAL_ID=
				(SELECT tal_id FROM Documentotipotalonario where DOC_ID=#aplica.DOC_ID and cf_id=
				(select cf_id from Tercero where tercero.te_id=#aplica.TE_ID)))),'01'),
				--select * from documentotipo where doc_nombre like '%agsf%'
				PUNTO_VENTA_asoc=substring((select ASIENTO.AS_NUMERODOC from Asiento where asiento.AS_ID= #aplica.as_id_aplica),2,4),
				NUMERO_COMPROBANTE_asoc=RIGHT((select ASIENTO.AS_NUMERODOC from Asiento where asiento.AS_ID= #aplica.as_id_aplica),8),
				CUIT_ASOC=(SELECT TE_CUIT FROM TERCERO WHERE TE_ID =(select ASIENTO.TE_ID from Asiento where asiento.AS_ID= #aplica.as_id_aplica)),
				FECHA_COMPROBANTE_ASOC =(select CONVERT(VARCHAR(8),Asiento.as_FECHA,112) from Asiento where asiento.AS_ID= #aplica.as_id_aplica)
				

	-- select *
	from  #aplica
	------------------------------------------------------------- BUSCA LA FACTURA EN CASO DE QUE HAYA UN PASO INTERMEDIO ( EJ Pedidod de NC)
		
	declare @as_id_intermedio int
	set @as_id_intermedio=isnull((	select as_id_aplica from #aplica
	where TIPO_COMPROBANTE_ASOC is null),0)
	
	select @as_id_intermedio
	
	delete from #aplica where @as_id_intermedio<>0
	
	insert into  #aplica(as_id_aplica, te_id,doc_id)
	select  top 1
		as_id =credito.as_id ,
		te_id =(select te_id from Asiento as a where a.as_id=credito.as_id ),
		doc_id =(select doc_id from Asiento as a where a.as_id=credito.as_id )
		
		from debito, credito, CreditoDebitoCancelacion, #AST_FEWS_LOG
		where 
			 debito.AS_ID =   @@AS_ID
			 and CreditoDebitoCancelacion.deb_id=debito.DEB_ID
			 and credito.crd_id=CreditoDebitoCancelacion.crd_id 
			 and (select COUNT(*) from #AST_FEWS_LOG_CBTE_ASOC where #AST_FEWS_LOG_CBTE_ASOC.AS_ID=@as_id_intermedio
			 )=0
			 AND CREDITO.AS_ID=#AST_FEWS_LOG.AS_ID
			 AND @as_id_intermedio<>0
			 
			update #aplica
		set 
			-- select 
			 TIPO_COMPROBANTE_asoc =isnull((select isnull(comprt_alias,'01') from comprobantetipo where comprobantetipo.COMPRT_ID=
				(select comprt_id from Talonario where talonario.TAL_ID=
				(SELECT tal_id FROM Documentotipotalonario where DOC_ID=#aplica.DOC_ID and cf_id=
				(select cf_id from Tercero where tercero.te_id=#aplica.TE_ID)))),'01'),
				--select * from documentotipo where doc_nombre like '%agsf%'
				PUNTO_VENTA_asoc=substring((select ASIENTO.AS_NUMERODOC from Asiento where asiento.AS_ID= #aplica.as_id_aplica),2,4),
				NUMERO_COMPROBANTE_asoc=RIGHT((select ASIENTO.AS_NUMERODOC from Asiento where asiento.AS_ID= #aplica.as_id_aplica),8),
				CUIT_ASOC=(SELECT TE_CUIT FROM TERCERO WHERE TE_ID =(select ASIENTO.TE_ID from Asiento where asiento.AS_ID= #aplica.as_id_aplica)),
				FECHA_COMPROBANTE_ASOC =(select CONVERT(VARCHAR(8),Asiento.as_FECHA,112) from Asiento where asiento.AS_ID= #aplica.as_id_aplica)
			 
				

	-- select *
	from  #aplica
	--WHERE @as_id_intermedio<>0
	
	--SELECT * FROM #APLICA
			 
	
	-- drop table #aplica
	
	--------------------------------------------------------------------------------------------------------------------
	
	-- select * from comprobantetipo
		
		IF (SELECT ISNULL(TIPO_COMPROBANTE,0) FROM #AST_FEWS_LOG WHERE #AST_FEWS_LOG.AS_ID=@@AS_ID  AND TIPO_COMPROBANTE IN (03, 07, 08)) <>0 -- TABLAS DE FACTURA DE CRÈDITO
			INSERT INTO #AST_FEWS_LOG_CBTE_ASOC(
				AS_ID,
				CUIT_EMPRESA,
				TIPO_COMPROBANTE,
				PUNTO_VENTA,
				NUMERO_COMPROBANTE,
				TIPO_COMPROBANTE_ASOC,
				PUNTO_VENTA_ASOC,
				NUMERO_COMPROBANTE_ASOC,
				CUIT_ASOC,
				FECHA_COMPROBANTE_ASOC)
			
			SELECT top 1
				AS_ID,
				CUIT_EMPRESA,
				TIPO_COMPROBANTE,
				PUNTO_VENTA,
				NUMERO_COMPROBANTE,
				TIPO_COMPROBANTE_ASOC,
				PUNTO_VENTA_ASOC,
				NUMERO_COMPROBANTE_ASOC,
				CUIT_EMPRESA,--CUIT_ASOC,
				FECHA_COMPROBANTE_ASOC
			
			FROM #AST_FEWS_LOG , #aplica
			WHERE 
			#AST_FEWS_LOG.AS_ID=@@AS_ID 
			AND  TIPO_COMPROBANTE IN (03, 07, 08)
			and TIPO_COMPROBANTE_ASOC in (01,06, 07, 08, 09, 10, 35, 40, 61, 64, 88, 991)
			
-------------------------------------------------------------------------------------------------------------------------ND MIPYME EYSE
		IF (SELECT ISNULL(TIPO_COMPROBANTE,0) FROM #AST_FEWS_LOG WHERE #AST_FEWS_LOG.AS_ID=@@AS_ID  AND TIPO_COMPROBANTE IN (202,207)) <>0 -- TABLAS DE FACTURA DE CRÈDITO
			INSERT INTO #AST_FEWS_LOG_CBTE_ASOC(
				AS_ID,
				CUIT_EMPRESA,
				TIPO_COMPROBANTE,
				PUNTO_VENTA,
				NUMERO_COMPROBANTE,
				TIPO_COMPROBANTE_ASOC,
				PUNTO_VENTA_ASOC,
				NUMERO_COMPROBANTE_ASOC,
				CUIT_ASOC,
				FECHA_COMPROBANTE_ASOC)
				
				/*
							 TIPO_COMPROBANTE_asoc =isnull((select isnull(comprt_alias,'01') from comprobantetipo where comprobantetipo.COMPRT_ID=
				(select comprt_id from Talonario where talonario.TAL_ID=
				(SELECT tal_id FROM Documentotipotalonario where DOC_ID=#aplica.DOC_ID and cf_id=
				(select cf_id from Tercero where tercero.te_id=#aplica.TE_ID)))),'01'),
				--select * from documentotipo where doc_nombre like '%agsf%'
				PUNTO_VENTA_asoc=substring((select ASIENTO.AS_NUMERODOC from Asiento where asiento.AS_ID= #aplica.as_id_aplica),2,4),
				NUMERO_COMPROBANTE_asoc=RIGHT((select ASIENTO.AS_NUMERODOC from Asiento where asiento.AS_ID= #aplica.as_id_aplica),8),
				CUIT_ASOC=(SELECT TE_CUIT FROM TERCERO WHERE TE_ID =(select ASIENTO.TE_ID from Asiento where asiento.AS_ID= #aplica.as_id_aplica)),
				FECHA_COMPROBANTE_ASOC =(select CONVERT(VARCHAR(8),Asiento.as_FECHA,112) from Asiento where asiento.AS_ID= #aplica.as_id_aplica)
				*/
			
			SELECT top 1
				#AST_FEWS_LOG.AS_ID,
				CUIT_EMPRESA,
				TIPO_COMPROBANTE,
				PUNTO_VENTA,
				NUMERO_COMPROBANTE,
				TIPO_COMPROBANTE_ASOC=isnull((select isnull(comprt_alias,'01') from comprobantetipo where comprobantetipo.COMPRT_ID=
				(select comprt_id from Talonario where talonario.TAL_ID=
				(SELECT tal_id FROM Documentotipotalonario where DOC_ID=A.DOC_ID and cf_id=
				(select cf_id from Tercero where tercero.te_id=A.TE_ID)))),'01'),
				PUNTO_VENTA_ASOC=substring(A.AS_NumeroDoc,2,4),
				NUMERO_COMPROBANTE_ASOC=RIGHT(A.AS_NUMERODOC,8),
				CUIT_ASOC=(SELECT TE_CUIT FROM TERCERO WHERE TE_ID =(select ASIENTO.TE_ID from Asiento where asiento.AS_ID= A.AS_ID)),
				FECHA_COMPROBANTE_ASOC=(select CONVERT(VARCHAR(8),Asiento.as_FECHA,112) from Asiento where asiento.AS_ID= A.AS_ID)
			
			FROM #AST_FEWS_LOG , ASIENTO, Asiento AS A
			WHERE 
			#AST_FEWS_LOG.AS_ID=@@AS_ID 
			AND  TIPO_COMPROBANTE IN (202,207)
			and -----TIPO_COMPROBANTE_ASOC 
			isnull((select isnull(comprt_alias,'01') from comprobantetipo where comprobantetipo.COMPRT_ID=
				(select comprt_id from Talonario where talonario.TAL_ID=
				(SELECT tal_id FROM Documentotipotalonario where DOC_ID=A.DOC_ID and cf_id=
				(select cf_id from Tercero where tercero.te_id=A.TE_ID)))),'01')
			
			in (201, 203, 206, 208)
			AND #AST_FEWS_LOG.AS_ID=ASIENTO.AS_ID
			----------------------------------------------- BUSCA EL ENLACE CON LOS CAMPOS ADICIONALES DE EYSE
			AND asiento.DocumentoAsociadoTipo=a.DOC_ID
            and cast(asiento.DocumentoAsociadoNro as int)=a.as_numero
			
	
		
	
	
	
	
	------------------------------------------------------------------------------- TABLA PERIODO		
			-- SELECT * FROM ComprobanteTipo ORDER BY COMPRT_ALIAS
			-- SELECT * FROM #AST_FEWS_LOG_PERIODO_ASOC
		IF (SELECT ISNULL(TIPO_COMPROBANTE,0) FROM #AST_FEWS_LOG WHERE #AST_FEWS_LOG.AS_ID=@@AS_ID  AND TIPO_COMPROBANTE IN (02, 03, 07, 08)) <>0 -- ND/NC 
			INSERT INTO #AST_FEWS_LOG_PERIODO_ASOC(
				AS_ID,
				CUIT_EMPRESA,
				TIPO_COMPROBANTE,
				PUNTO_VENTA,
				NUMERO_COMPROBANTE,
				FECHA_DESDE, 
				FECHA_HASTA)
			
			SELECT top 1
				AS_ID,
				CUIT_EMPRESA,
				TIPO_COMPROBANTE,
				PUNTO_VENTA,
				NUMERO_COMPROBANTE,
			    FDESDE=SUBSTRING(FECHA_COMPROBANTE,1,6)+'01',
			    FECHA_COMPROBANTE
			
			FROM #AST_FEWS_LOG , #aplica
			WHERE 
			#AST_FEWS_LOG.AS_ID=@@AS_ID 
			AND  TIPO_COMPROBANTE IN (02, 03, 07, 08)
			
		

			 

						
	END


END
--ELSE 
select 0
end

select '#AST_FEWS_LOG',*				FROM #AST_FEWS_LOG				WHERE AS_ID=@@AS_ID
select '#AST_FEWS_LOG_IVA',*			FROM #AST_FEWS_LOG_IVA			WHERE AS_ID=@@AS_ID
select '#AST_FEWS_LOG_TRIBUTOS',*		FROM #AST_FEWS_LOG_TRIBUTOS		WHERE AS_ID=@@AS_ID
select '#AST_FEWS_LOG_CBTE_ASOC',*		FROM #AST_FEWS_LOG_CBTE_ASOC	WHERE AS_ID=@@AS_ID
select '#AST_FEWS_LOG_DATOS_OPC',*		FROM #AST_FEWS_LOG_DATOS_OPC	WHERE AS_ID=@@AS_ID	
select '#AST_FEWS_LOG_PERIODO_ASOC',*	FROM #AST_FEWS_LOG_PERIODO_ASOC	WHERE AS_ID=@@AS_ID