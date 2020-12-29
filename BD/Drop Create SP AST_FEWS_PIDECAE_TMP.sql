USE [FINN_MACOR2014]
GO

/****** Object:  StoredProcedure [dbo].[AST_FEWS_PIDECAE_TMP]    Script Date: 08/29/2019 00:41:10 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[AST_FEWS_PIDECAE_TMP]') AND type in (N'P', N'PC'))
DROP PROCEDURE [dbo].[AST_FEWS_PIDECAE_TMP]
GO

USE [FINN_MACOR2014]
GO

/****** Object:  StoredProcedure [dbo].[AST_FEWS_PIDECAE_TMP]    Script Date: 08/29/2019 00:41:10 ******/
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
EXEC sp_configure;

USE master; 
GO 
EXEC sp_configure 'lightweight pooling', '0'; 
RECONFIGURE WITH OVERRIDE;
SELECT name FROM [FINNGS-TEST-01].master.sys.databases 


-- generador movimientos de prueba
select 'exec AST_FEWS_PIDECAE_TMP', as_id,', true' from Asiento where AS_Fecha>='01-01-2016' and DOC_ID=818


exec AST_FEWS_PIDECAE_TMP	1537272	, true

select * from asientoitem where as_id=611144
SELECT DBO.AST_FEWS_GET_RESULTADO_CAE (582796)
SELECT resultado, cae,* FROM FINN_MACOR_TMP..AST_FEWS_LOG WHERE AS_ID=582796
select * from asiento where as_numero=3114

SELECT MIN(NUMERO_COMPROBANTE) FROM FINN_MACOR_TMP..AST_FEWS_LOG WHERE TIPO_COMPROBANTE='06'

 SELECT  REPLICATE('0',8-LEN(CAST(CAST(NUMERO_COMPROBANTE AS INT)-1664 AS VARCHAR(8))))+
  CAST(CAST(NUMERO_COMPROBANTE AS INT)-1664 AS VARCHAR(8)),* FROM FINN_MACOR_TMP..AST_FEWS_LOG WHERE TIPO_COMPROBANTE='01'
  
  UPDATE FINN_MACOR_TMP..AST_FEWS_LOG
  SET NUMERO_COMPROBANTE=REPLICATE('0',8-LEN(CAST(CAST(NUMERO_COMPROBANTE AS INT)-1262 AS VARCHAR(8))))+
  CAST(CAST(NUMERO_COMPROBANTE AS INT)-1262 AS VARCHAR(8))
  FROM FINN_MACOR_TMP..AST_FEWS_LOG WHERE TIPO_COMPROBANTE='06'
  
    UPDATE FINN_MACOR_TMP..AST_FEWS_LOG_IVA
  SET NUMERO_COMPROBANTE=REPLICATE('0',8-LEN(CAST(CAST(NUMERO_COMPROBANTE AS INT)-1262 AS VARCHAR(8))))+
  CAST(CAST(NUMERO_COMPROBANTE AS INT)-1262 AS VARCHAR(8))
  FROM FINN_MACOR_TMP..AST_FEWS_LOG_IVA WHERE TIPO_COMPROBANTE='06'
  
  
   SELECT  REPLICATE('0',8-LEN(CAST(CAST(NUMERO_COMPROBANTE AS INT)-1664 AS VARCHAR(8))))+
  CAST(CAST(NUMERO_COMPROBANTE AS INT)-1664 AS VARCHAR(8)),* FROM FINN_MACOR_TMP..AST_FEWS_LOG_IVA WHERE TIPO_COMPROBANTE='01'
  
  UPDATE FINN_MACOR_TMP..AST_FEWS_LOG_IVA
  SET CUIT_EMPRESA='30711897581'
  
  select * from asiento where as_numero=14
  
 exec AST_FEWS_PIDECAE_TMP  1489703, true 
 SELECT resultado, obs, cae,* FROM FINN_MACOR_TMP..AST_FEWS_LOG where as_id=1489703
 SELECT * FROM FINN_MACOR_TMP..AST_FEWS_LOG_IVA where as_id=1489703
  SELECT * FROM FINN_MACOR_TMP..AST_FEWS_LOG_TRIBUTOS where as_id=1489703
 
 delete FROM FINN_MACOR_TMP..AST_FEWS_LOG where as_id=1489699
delete FROM FINN_MACOR_TMP..AST_FEWS_LOG_IVA where as_id=1489699
  delete  FROM FINN_MACOR_TMP..AST_FEWS_LOG_TRIBUTOS where as_id=1489699
  
 
  
 SELECT * FROM ASIENTO WHERE AS_NUMERO=3115
delete FROM FINN_MACOR_TMP..AST_FEWS_LOG
delete FROM FINN_MACOR_TMP..AST_FEWS_LOG_IVA
delete FROM FINN_MACOR_TMP..AST_FEWS_LOG_TRIBUTOS

-- exec AST_FEWS_PIDECAE_TMP  1485969, true
select * from asiento where as_numero=3103
 */


CREATE PROCEDURE [dbo].[AST_FEWS_PIDECAE_TMP]  
 -- Add the parameters for the stored procedure here    
 @@AS_ID INT  ,   
 @@FLAG BIT
AS    

BEGIN    
DECLARE @R VARCHAR(100)
SET @R='0'
DECLARE  @FLAG BIT
SET @FLAG=@@FLAG

declare @@TipoComprobante VARCHAR(3)
select @@TipoComprobante = comprt_codigo
from  Asiento, Talonario, documentotipo
where Asiento.AS_ID = @@AS_ID
	and isnull(talonario.comprt_codigo,0)<>0
	and left(asiento.as_numerodoc,5)=talonario.tal_prefijo
	and Documentotipo.DOC_ID=asiento.doc_id
	and asiento.DOC_ID>=278
	and asiento.DOC_ID<=305
	and (documentotipo.TAL_ID_A=talonario.TAL_ID
		or
		documentotipo.TAL_ID_B=talonario.TAL_ID
		or
		documentotipo.TAL_ID_E=talonario.TAL_ID
		)

IF @FLAG=1 
BEGIN
	declare @tiempo datetime
	set @tiempo=GETDATE()
	
-- 	exec AST_FEWS_PIDECAE_TMP	582789	, true
	
	IF isnull((SELECT ISNULL(cae,'') FROM FINN_MACOR_TMP..AST_FEWS_LOG WHERE isnull(AS_ID,-1)=@@AS_ID and isnull(resultado,'')='A'),'')=''
	
	  -- (SELECT ISNULL(cae,'') FROM FINN_MACOR_TMP..FINN_MACOR_TMP..AST_FEWS_LOG WHERE isnull(AS_ID,-1)=611169 and isnull(resultado,'')='A')
	BEGIN
 --SELECT * FROM FINN_MACOR_TMP..FINN_MACOR_TMP..AST_FEWS_LOG
			--- SI HAY REINTENTO, SE BORRAN LOS REGISTROS ANTERIORIES Y SE VUELVEN A GENERAR
			---

--------- borra movimientos que no obtuvieron CUIT en tabla intermedia
			--CREATE TABLE #AST_FEWS_BORRAR ( AS_ID INT)
			--DELETE FROM #AST_FEWS_BORRAR

			--INSERT INTO #AST_FEWS_BORRAR
			--select * from FINN_MACOR_TMP..FINN_MACOR_TMP..AST_FEWS_LOG where isnull(RESULTADO,'')<>'A'


			--DELETE FROM FINN_MACOR_TMP..FINN_MACOR_TMP..AST_FEWS_LOG
			--WHERE
			--(SELECT COUNT(*) FROM #AST_FEWS_BORRAR where #AST_FEWS_BORRAR.AS_ID=FINN_MACOR_TMP..FINN_MACOR_TMP..AST_FEWS_LOG.AS_ID)<>0

			--DELETE FROM FINN_MACOR_TMP..FINN_MACOR_TMP..AST_FEWS_LOG_iva
			--WHERE
			--(SELECT COUNT(*) FROM #AST_FEWS_BORRAR where #AST_FEWS_BORRAR.AS_ID=FINN_MACOR_TMP..FINN_MACOR_TMP..AST_FEWS_LOG_iva.AS_ID)<>0 

			--DELETE FROM FINN_MACOR_TMP..AST_FEWS_LOG_TRIBUTOS
			--WHERE
			--(SELECT COUNT(*) FROM #AST_FEWS_BORRAR where #AST_FEWS_BORRAR.AS_ID=FINN_MACOR_TMP..AST_FEWS_LOG_TRIBUTOS.AS_ID)<>0 
			
			--- OBTIENE EL CUIT DE LA EMPRESA LOGEADA
			declare @EmpresaCuit varchar(11)
			
			SET @EmpresaCuit= (select VALOR from configuracion where aspecto='empresacuit' AND 
					GRUPO ='FAF_'+CAST((select EMPR_ID from CIRCUITOCONTABLE WHERE CC_ID=
					(SELECT CC_ID FROM DocumentoTipoCircuitoContable WHERE DOC_ID=
					((SELECT DOC_ID FROM ASIENTO WHERE AS_ID=@@AS_ID)))) AS VARCHAR(4)))
					
		 -----------------------			
		 ----------------------- test de fc de credito
		 -----------------------
		 SET @EmpresaCuit= case when (select count(*) from asiento where asiento.DOC_ID in (293,294,295) and asiento.AS_ID=@@AS_ID)<>0
		                    then '20225925055'
		                    else @EmpresaCuit
		                    end

			declare @EmpresaCBU varchar(20)
			
			SET @EmpresaCBU = (select AST_CBU  from Empresa where Empresa.EMPR_ID=CAST((select EMPR_ID from CIRCUITOCONTABLE WHERE CC_ID=
					(SELECT CC_ID FROM DocumentoTipoCircuitoContable WHERE DOC_ID=
					((SELECT DOC_ID FROM ASIENTO WHERE AS_ID=@@AS_ID)))) AS VARCHAR(4)))


			
			----
			----	TOMA LOS PARAMETROS DEFINIDOS EN EXPOSICION FISCAL
			----
			create  table #EXPOFISCAL (cue_id int, conceptonombre varchar(10))
			-- DELETE #EXPOFISCAL
			INSERT INTO #EXPOFISCAL
			sELECT 
			CUE_ID,
			'02'
			from Cuenta where CUE_nombre like '%percep%'
			UNION ALL
			sELECT 
			CUE_ID,
			'IVA'
			from Cuenta where CUE_nombre like '%IVA%'
			--SELECT * FROM #EXPOFISCAL


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
			PR_ID=PV_ID,
			(SELECT Pv_LLEVASTOCK FROM ProductoVenta WHERE PRODUCTOVENTA.PV_ID=VENTAITEM.Pv_ID),
			(SELECT TI_ID_RI FROM ProductoVENTA WHERE PRODUCTOVENTA.PV_ID=VENTAITEM.PV_ID),
			PORCE=ia_porcivari,
			--(SELECT TOP 1 TI_PORCENTAJE FROM TasaImpositiva WHERE TI_ID=(SELECT TI_ID FROM ProductoVENTA WHERE PRODUCTOVENTA.PV_ID=VENTAITEM.PV_ID)),
				
				--CASE ISNULL(VENTAITEM.PR_ID,0)
				--	WHEN 0 THEN VENTAITEM.VTAI_AUX1
				--	ELSE  (SELECT TI_PORCENTAJE FROM TasaImpositiva WHERE TI_ID=(SELECT TI_ID FROM Producto WHERE PRODUCTO.PR_ID=VENTAITEM.PR_ID))
				--	END,
			ia_precio,

			VTAI_Cantidad=IA_CantVenta*(case when @@TipoComprobante in ('03','08','203','208') and VentaItem.IA_DH='H' and VentaItem.CUE_id<>25 then -1 
											when @@TipoComprobante in ('01','02','06','07','201','202','206','207') and VentaItem.IA_DH='D' and VentaItem.CUE_id<>25 then -1 
											else 1 end) ,
			VTAI_Importe=IA_Importe*(case when @@TipoComprobante in ('03','08','203','208') and VentaItem.IA_DH='H' and VentaItem.CUE_id<>25 then -1 
											when @@TipoComprobante in ('01','02','06','07','201','202','206','207') and VentaItem.IA_DH='D' and VentaItem.CUE_id<>25 then -1 
											else 1 end)    ,
			GRAVADO= CASE ISNULL((SELECT TI_PORCENTAJE FROM TasaImpositiva WHERE TI_ID=(SELECT TI_ID FROM Producto WHERE PRODUCTO.PR_ID=VENTAITEM.PR_ID)),-1)
						WHEN 0 THEN 0
						WHEN -1 THEN 0
						ELSE ia_Importe
					END*(case when @@TipoComprobante in ('03','08','203','208') and VentaItem.IA_DH='H' and VentaItem.CUE_id<>25 then -1 
											when @@TipoComprobante in ('01','02','06','07','201','202','206','207') and VentaItem.IA_DH='D' and VentaItem.CUE_id<>25 then -1 
											else 1 end),
			NO_GRAVADO= CASE (SELECT TI_PORCENTAJE FROM TasaImpositiva WHERE TI_ID=(SELECT TI_ID FROM Producto WHERE PRODUCTO.PR_ID=VENTAITEM.PR_ID))
						WHEN 0 THEN ia_Importe
						ELSE 0
					END*(case when @@TipoComprobante in ('03','08','203','208') and VentaItem.IA_DH='H' and VentaItem.CUE_id<>25 then -1 
											when @@TipoComprobante in ('01','02','06','07','201','202','206','207') and VentaItem.IA_DH='D' and VentaItem.CUE_id<>25 then -1 
											else 1 end),

			CUE_id=VentaItem.CUE_id,
			EXFISC_CONCEPTONOMBRE= ISNULL((sELECT #EXPOFISCAL.conceptonombre FROM #EXPOFISCAL WHERE #EXPOFISCAL.cue_id=VentaItem.CUE_id),99),
			---(select EXPOSICIONFISCALCUENTA.EXFISCCUE_ConceptoNombre FROM  exposicionfiscalCUENTA WHERE exposicionfiscalCUENTA.CUE_id=VENTAITEM.CUE_id AND EXFISC_ID=@EXPOFISCAL),
			CONC_ID=cue_id

			FROM asientoitem as VentaItem
			WHERE
			--VENTAITEM.PR_ID IS NOT NULL AND
			 AS_ID=@@AS_ID

			---SELECT * FROM #ITEMS
			
			
			--- 
			--- ESTABLECE SI LA FACTURA ES DE PRODUCTO O SERVICIO
			---
			DECLARE @PRODUCTOS INT

			DECLARE @SERVICIOS INT

			SET @PRODUCTOS=(SELECT COUNT(*) FROM #ITEMS WHERE PR_LEVASTOCK=1)
			SET @SERVICIOS=(SELECT COUNT(*) FROM #ITEMS WHERE PR_LEVASTOCK=0)

			--SELECT @PRODUCTOS, @SERVICIOS

			---
			--- INSERTA EN 	FINN_MACOR_TMP..AST_FEWS_LOG
			---
						
			INSERT INTO FINN_MACOR_TMP..AST_FEWS_LOG(
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
				
				TIPO_COMPROBANTE=(select comprt_codigo from  Talonario,documentotipo
									where 
									ISNULL(talonario.comprt_codigo,0)<>0
									and left(asiento.as_numerodoc,5)=talonario.tal_prefijo
									and Documentotipo.DOC_ID=asiento.doc_id
									and asiento.DOC_ID>=278 -----  and asiento.DOC_ID<=292
									                         and asiento.DOC_ID<=305 ---- Agregado 7-8-2019. Factura de crèdito
									and (documentotipo.TAL_ID_A=talonario.TAL_ID
									OR
											documentotipo.TAL_ID_B=talonario.TAL_ID
											or
										documentotipo.TAL_ID_E=talonario.TAL_ID
										)),
				--select comprt_codigo,* from documentotipo where DOC_ID>=278
				PUNTO_VENTA=substring(ASIENTO.AS_NUMERODOC,2,4),
				
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
								when -1 then round(ISNULL((SELECT SUM(importe) FROM #ITEMS where CUE_id<>25),0),2)
								else round(ISNULL((SELECT SUM(importe) FROM #ITEMS where CUE_id<>25),0)/ISNULL(asiento.AS_MonedaCambio,1),2)
								end,
				NETO_NOGRAVADO=ISNULL((SELECT SUM(importe) FROM #ITEMS where PR_ID is not null and ISNULL(ti_porcentaje,0)=0),0),
				NETO_GRAVADO=ISNULL((SELECT SUM(importe) FROM #ITEMS where PR_ID is not null and ISNULL(ti_porcentaje,0)<>0),0),
				NETO_EXENTO=0,
				--(case asiento.mon_id
				--				when -1 then ISNULL(ASIENTO.AS_TOTAL,0)
				--				else ISNULL(ASIENTO.AS_TOTAL,0)/ISNULL(asiento.AS_MonedaCambio,1)
				--				end)-ISNULL((SELECT SUM(NO_GRAVADO) FROM #ITEMS),0)-ISNULL((SELECT SUM(GRAVADO) FROM #ITEMS),0),
				IVA_TOTAL=ISNULL((SELECT SUM(IMPORTE) FROM #ITEMS WHERE EXFISC_CONCEPTONOMBRE='IVA'),0),
				TRIBUTOS_TOTAL=ISNULL((SELECT SUM(IMPORTE) FROM #ITEMS WHERE EXFISC_CONCEPTONOMBRE='02' AND PR_ID IS NULL),0),
				RESULTADO='0'

			 FROM Asiento 
			 WHERE 
			 ASIENTO.AS_ID=@@AS_ID  
			 
			 
			--- 
			--- INSERTA EN FINN_MACOR_TMP..AST_FEWS_LOG_IVA 
			---

			 INSERT INTO FINN_MACOR_TMP..AST_FEWS_LOG_IVA(
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
					TIPO_COMPROBANTE=FINN_MACOR_TMP..AST_FEWS_LOG.TIPO_COMPROBANTE,
					PUNTO_VENTA=FINN_MACOR_TMP..AST_FEWS_LOG.PUNTO_VENTA,
					NUMERO_COMPROBANTE=FINN_MACOR_TMP..AST_FEWS_LOG.NUMERO_COMPROBANTE,
					PORCENTAJE=#ITEMS.TI_PORCENTAJE,
					NETO_GRAVADO=ISNULL((SELECT SUM(a.importe) FROM #ITEMS  as a where a.PR_ID is not null and ISNULL(a.ti_porcentaje,0)<>0 and ISNULL(a.ti_porcentaje,0)=#ITEMS.TI_PORCENTAJE),0),
					--ISNULL((SELECT SUM(GRAVADO) FROM #ITEMS),0),
					IMPORTE=sum(#ITEMS.IMPORTE)
					
			FROM #ITEMS, FINN_MACOR_TMP..AST_FEWS_LOG WHERE
			FINN_MACOR_TMP..AST_FEWS_LOG.AS_ID=@@AS_ID AND 
			#ITEMS.EXFISC_CONCEPTONOMBRE='IVA' AND
			#ITEMS.TI_PORCENTAJE<>0-- AND
			--#ITEMS.IMPORTE<>0
			GROUP BY	FINN_MACOR_TMP..AST_FEWS_LOG.TIPO_COMPROBANTE, 
						FINN_MACOR_TMP..AST_FEWS_LOG.PUNTO_VENTA,
						FINN_MACOR_TMP..AST_FEWS_LOG.NUMERO_COMPROBANTE,
						#ITEMS.TI_PORCENTAJE
			
			
			INSERT INTO FINN_MACOR_TMP..AST_FEWS_LOG_TRIBUTOS(
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
						
						-- 	exec AST_FEWS_PIDECAE_TMP	1485969, true
						-- select * from FINN_MACOR_TMP..AST_FEWS_LOG
			SELECT 
					AS_ID=@@AS_ID,
					CUIT_EMPRESA= @EmpresaCuit,
					TIPO_COMPROBANTE=FINN_MACOR_TMP..AST_FEWS_LOG.TIPO_COMPROBANTE,
					PUNTO_VENTA=FINN_MACOR_TMP..AST_FEWS_LOG.PUNTO_VENTA,
					NUMERO_COMPROBANTE=FINN_MACOR_TMP..AST_FEWS_LOG.NUMERO_COMPROBANTE,
					TRIBUTO_ID=ISNULL(#ITEMS.EXFISC_CONCEPTONOMBRE,0),
					DESCRPTION=(SELECT cue_alias FROM cuenta WHERE cuenta.cue_id=#ITEMS.CONC_ID), --ALIAS DE IMPUESTO
					PORCENTAJE= 
					case
					when ISNULL((SELECT SUM(importe) FROM #ITEMS where PR_ID is not null and ISNULL(ti_porcentaje,0)<>0),0)<>0
					then 
						ROUND(#ITEMS.IMPORTE/ISNULL((SELECT SUM(importe) FROM #ITEMS where PR_ID is not null and ISNULL(ti_porcentaje,0)<>0),0)*100,2)
					else 0
					end,
					--#ITEMS.TI_PORCENTAJE,
					NETO_GRAVADO=0,--ISNULL((SELECT SUM(GRAVADO) FROM #ITEMS),0),
					IMPORTE=#ITEMS.IMPORTE
				FROM #ITEMS, FINN_MACOR_TMP..AST_FEWS_LOG WHERE
			FINN_MACOR_TMP..AST_FEWS_LOG.AS_ID=@@AS_ID AND 
				#ITEMS.PR_ID IS NULL AND
				ISNULL(#ITEMS.EXFISC_CONCEPTONOMBRE,0)<>'IVA' AND
				ISNULL(#ITEMS.EXFISC_CONCEPTONOMBRE,0)<>'99' AND
				ISNULL(#ITEMS.EXFISC_CONCEPTONOMBRE,0)<>0 
				
				--AND
				--#ITEMS.TI_PORCENTAJE<>0
				
	 --------------- FECHA DE VENCIMIENTO DEN FCE		
			UPDATE FINN_MACOR_TMP..AST_FEWS_LOG
			SET FECHA_VENC_PAGO=	
				--case when CONCEPTO_FACTURA <> 1 then 
				CONVERT(VARCHAR(8),
				isnull((select top 1 Debito.DEB_FechaReal from Debito where debito.AS_ID=FINN_MACOR_TMP..AST_FEWS_LOG.AS_ID), (select a.as_FEcha +1 from Asiento as a  where a.AS_ID=FINN_MACOR_TMP..AST_FEWS_LOG.AS_ID))
				,112)
				--end
			FROM FINN_MACOR_TMP..AST_FEWS_LOG
			WHERE 
			FINN_MACOR_TMP..AST_FEWS_LOG.AS_ID=@@AS_ID 
			AND   TIPO_COMPROBANTE IN (201,202,203,208,202,207)
			
		
		--------- TABLAS DE FACTURA DE CRÈDITO  FINN_MACOR_TMP..AST_FEWS_LOG_DATOS_OPC
		insert into FINN_MACOR_TMP..AST_FEWS_LOG_DATOS_OPC
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
			TIPO_COMPROBANTE=FINN_MACOR_TMP..AST_FEWS_LOG.TIPO_COMPROBANTE,
			PUNTO_VENTA=FINN_MACOR_TMP..AST_FEWS_LOG.PUNTO_VENTA,
			NUMERO_COMPROBANTE=FINN_MACOR_TMP..AST_FEWS_LOG.NUMERO_COMPROBANTE,
			ID_OPCIONAL= 2101,
			VALOR= @EmpresaCBU
		FROM FINN_MACOR_TMP..AST_FEWS_LOG
		WHERE 
			FINN_MACOR_TMP..AST_FEWS_LOG.AS_ID=@@AS_ID
			AND  TIPO_COMPROBANTE IN (201,202,203,208,202,207)
		
		--------- TABLAS DE FACTURA DE CRÈDITO SOLAMENTE NC/ND
		
		create table #aplica(
				as_id_aplica INT,
				TIPO_COMPROBANTE_ASOC varchar(3),
				PUNTO_VENTA_ASOC varchar(10) NULL,
				NUMERO_COMPROBANTE_ASOC varchar(20) NULL,
				CUIT_ASOC varchar(20) NULL,
				FECHA_COMPROBANTE_ASOC varchar(10) NULL)
				
		insert into  #aplica(as_id_aplica)
		select  
		credito.as_id 
		from debito, credito, CreditoDebitoCancelacion
		where 
			 debito.AS_ID =@@AS_ID
			 and CreditoDebitoCancelacion.deb_id=debito.DEB_ID
			 and credito.crd_id=CreditoDebitoCancelacion.crd_id 
			 
		update #aplica
		set 
			 
			 TIPO_COMPROBANTE_asoc =(select comprt_codigo from  Talonario,documentotipo
									where 
									ISNULL(talonario.comprt_codigo,0)<>0
									and left((select ASIENTO.AS_NUMERODOC from Asiento where asiento.AS_ID= #aplica.as_id_aplica),5)=talonario.tal_prefijo
									and Documentotipo.DOC_ID=(select ASIENTO.doc_id from Asiento where asiento.AS_ID= #aplica.as_id_aplica)
									and (select ASIENTO.doc_id from Asiento where asiento.AS_ID= #aplica.as_id_aplica)>=278 -----  and asiento.DOC_ID<=292
									                         and (select ASIENTO.doc_id from Asiento where asiento.AS_ID= #aplica.as_id_aplica)<=295 ---- Agregado 7-8-2019. Factura de crèdito
									and (documentotipo.TAL_ID_A=talonario.TAL_ID
									OR
											documentotipo.TAL_ID_B=talonario.TAL_ID
											or
										documentotipo.TAL_ID_E=talonario.TAL_ID
										)),
				--select comprt_codigo,* from documentotipo where DOC_ID>=278
				PUNTO_VENTA_asoc=substring((select ASIENTO.AS_NUMERODOC from Asiento where asiento.AS_ID= #aplica.as_id_aplica),2,4),
				NUMERO_COMPROBANTE_asoc=RIGHT((select ASIENTO.AS_NUMERODOC from Asiento where asiento.AS_ID= #aplica.as_id_aplica),8),
				CUIT_ASOC=(SELECT TE_CUIT FROM TERCERO WHERE TE_ID =(select ASIENTO.TE_ID from Asiento where asiento.AS_ID= #aplica.as_id_aplica)),
				FECHA_COMPROBANTE_ASOC =(select CONVERT(VARCHAR(8),Asiento.as_FECHA,112) from Asiento where asiento.AS_ID= #aplica.as_id_aplica)
				
	from  #aplica
	
	
		
		IF (SELECT ISNULL(TIPO_COMPROBANTE,0) FROM FINN_MACOR_TMP..AST_FEWS_LOG WHERE FINN_MACOR_TMP..AST_FEWS_LOG.AS_ID=@@AS_ID  AND TIPO_COMPROBANTE IN (203,208,202,207)) <>0 -- TABLAS DE FACTURA DE CRÈDITO
			INSERT INTO FINN_MACOR_TMP..AST_FEWS_LOG_CBTE_ASOC(
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
			
			SELECT 
				AS_ID,
				CUIT_EMPRESA,
				TIPO_COMPROBANTE,
				PUNTO_VENTA,
				NUMERO_COMPROBANTE,
				TIPO_COMPROBANTE_ASOC,
				PUNTO_VENTA_ASOC,
				NUMERO_COMPROBANTE_ASOC,
				CUIT_ASOC,
				FECHA_COMPROBANTE_ASOC
			
			FROM FINN_MACOR_TMP..AST_FEWS_LOG , #aplica
			WHERE 
			FINN_MACOR_TMP..AST_FEWS_LOG.AS_ID=@@AS_ID 
			AND  TIPO_COMPROBANTE IN (203,208,202,207)
			

			 

						
	END


END
--ELSE 
select 0
end




GO


