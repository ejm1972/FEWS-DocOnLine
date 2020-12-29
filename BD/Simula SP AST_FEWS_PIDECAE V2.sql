USE [FINN_MACOR2014]
GO

delete FROM [FINN_MACOR_TMP].[dbo].[AST_FEWS_LOG]
delete FROM [FINN_MACOR_TMP].[dbo].[AST_FEWS_LOG_IVA]
delete FROM [FINN_MACOR_TMP].[dbo].[AST_FEWS_LOG_TRIBUTOS]
delete FROM [FINN_MACOR_TMP].[dbo].[AST_FEWS_LOG_DATOS_OPC]
delete FROM [FINN_MACOR_TMP].[dbo].[AST_FEWS_LOG_CBTE_ASOC]
go

declare @@RECNO INT
select @@RECNO = 0
declare @@AS_ID_CURSOR INT
-----Declaro un cursor para levantar todas las diferentes y reprocesarlas     
declare  cursor_diferentes cursor for  
select asi.AS_ID
from asiento asi  inner join AST_FEWS_LOG ast on asi.AS_ID=ast.AS_ID
where
	--abs(AS_Total-IMPORTE_TOTAL)>0.01
	--and 
		(asi.AS_NumeroDoc like 'A0004%'
		or 
		asi.AS_NumeroDoc like 'B0004%')
order by asi.AS_ID desc
/*
select asi.AS_ID
from asiento asi
where	(
		asi.AS_NumeroDoc like 'A0004%' or 
		asi.AS_NumeroDoc like 'B0004%'
		)
order by asi.AS_ID desc
*/
open cursor_diferentes  
fetch Next From cursor_diferentes Into @@AS_ID_CURSOR  

while (@@Fetch_Status <> -1)    
begin

drop table #EXPOFISCAL
drop table #ITEMS
drop table #APLICA

declare @@AS_ID INT
declare @@FLAG BIT

select @@AS_ID = 0
select @@FLAG  = 1

DECLARE @R VARCHAR(100)
SET @R='0'
DECLARE  @FLAG BIT
SET @FLAG=@@FLAG

DECLARE @@NRODOC VARCHAR(20)

select @@AS_ID = @@AS_ID_CURSOR
--from Asiento
--where AS_NumeroDoc = 'B000400001183'

declare @@TipoComprobante VARCHAR(3)
select @@TipoComprobante = comprt_codigo, @@NRODOC = AS_NumeroDoc
from  Asiento, Talonario, documentotipo
where Asiento.AS_ID = @@AS_ID
	and isnull(talonario.comprt_codigo,0)<>0
	and left(asiento.as_numerodoc,5)=talonario.tal_prefijo
	and Documentotipo.DOC_ID=asiento.doc_id
	and asiento.DOC_ID>=278
	and asiento.DOC_ID<=295
	and (documentotipo.TAL_ID_A=talonario.TAL_ID
		or
		documentotipo.TAL_ID_B=talonario.TAL_ID
		or
		documentotipo.TAL_ID_E=talonario.TAL_ID
		)

select @@RECNO = @@RECNO + 1
--select @@TipoComprobante, @@NRODOC, @@RECNO
print @@TipoComprobante + ' - ' + @@NRODOC + ' - ' + convert(varchar(20), @@RECNO)

IF @FLAG=1 
BEGIN
	declare @tiempo datetime
	set @tiempo=GETDATE()
	
	IF isnull((SELECT ISNULL(cae,'') FROM FINN_MACOR_TMP..AST_FEWS_LOG WHERE isnull(AS_ID,-1)=@@AS_ID and isnull(resultado,'')='A'),'')=''
	BEGIN
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
			DEB_HAB VARCHAR(1),
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

			INSERT INTO #ITEMS (DEB_HAB, PR_ID, PR_LEVASTOCK, TI_ID, TI_PORCENTAJE, PRECIO, CANTIDAD, IMPORTE, GRAVADO, NO_GRAVADO,
								CUE_ID,EXFISC_CONCEPTONOMBRE, CONC_ID)
			SELECT 
				VentaItem.IA_DH DEB_HAB,
				PR_ID=PV_ID,
				(SELECT Pv_LLEVASTOCK FROM ProductoVenta WHERE PRODUCTOVENTA.PV_ID=VENTAITEM.Pv_ID),
				(SELECT TI_ID_RI FROM ProductoVENTA WHERE PRODUCTOVENTA.PV_ID=VENTAITEM.PV_ID),
				PORCE=ia_porcivari,
				ia_precio,
				
				VTAI_Cantidad=IA_CantVenta*(case when @@TipoComprobante in ('03','08') and VentaItem.IA_DH='H' and VentaItem.CUE_id<>25 then -1 
												when @@TipoComprobante in ('01','02','06','07') and VentaItem.IA_DH='D' and VentaItem.CUE_id<>25 then -1 
												else 1 end) ,
				VTAI_Importe=IA_Importe*(case when @@TipoComprobante in ('03','08') and VentaItem.IA_DH='H' and VentaItem.CUE_id<>25 then -1 
												when @@TipoComprobante in ('01','02','06','07') and VentaItem.IA_DH='D' and VentaItem.CUE_id<>25 then -1 
												else 1 end)    ,
				GRAVADO= CASE ISNULL((SELECT TI_PORCENTAJE FROM TasaImpositiva WHERE TI_ID=(SELECT TI_ID FROM Producto WHERE PRODUCTO.PR_ID=VENTAITEM.PR_ID)),-1)
							WHEN 0 THEN 0
							WHEN -1 THEN 0
							ELSE ia_Importe
						END*(case when @@TipoComprobante in ('03','08') and VentaItem.IA_DH='H' and VentaItem.CUE_id<>25 then -1 
												when @@TipoComprobante in ('01','02','06','07') and VentaItem.IA_DH='D' and VentaItem.CUE_id<>25 then -1 
												else 1 end),
				NO_GRAVADO= CASE (SELECT TI_PORCENTAJE FROM TasaImpositiva WHERE TI_ID=(SELECT TI_ID FROM Producto WHERE PRODUCTO.PR_ID=VENTAITEM.PR_ID))
							WHEN 0 THEN ia_Importe
							ELSE 0
						END*(case when @@TipoComprobante in ('03','08') and VentaItem.IA_DH='H' and VentaItem.CUE_id<>25 then -1 
												when @@TipoComprobante in ('01','02','06','07') and VentaItem.IA_DH='D' and VentaItem.CUE_id<>25 then -1 
												else 1 end),

				CUE_id=VentaItem.CUE_id,
				EXFISC_CONCEPTONOMBRE= ISNULL((sELECT #EXPOFISCAL.conceptonombre FROM #EXPOFISCAL WHERE #EXPOFISCAL.cue_id=VentaItem.CUE_id),99),
				CONC_ID=cue_id
			FROM asientoitem as VentaItem
			WHERE
				 AS_ID=@@AS_ID

			--SELECT * FROM #ITEMS
			
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
									                         and asiento.DOC_ID<=295 ---- Agregado 7-8-2019. Factura de crèdito
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
					TIPO_COMPROBANTE=AST_FEWS_LOG.TIPO_COMPROBANTE,
					PUNTO_VENTA=AST_FEWS_LOG.PUNTO_VENTA,
					NUMERO_COMPROBANTE=AST_FEWS_LOG.NUMERO_COMPROBANTE,
					PORCENTAJE=#ITEMS.TI_PORCENTAJE,
					NETO_GRAVADO=ISNULL((SELECT SUM(a.importe) FROM #ITEMS  as a where a.PR_ID is not null and ISNULL(a.ti_porcentaje,0)<>0 and ISNULL(a.ti_porcentaje,0)=#ITEMS.TI_PORCENTAJE),0),
					--ISNULL((SELECT SUM(GRAVADO) FROM #ITEMS),0),
					IMPORTE=sum(#ITEMS.IMPORTE)
					
			FROM #ITEMS, FINN_MACOR_TMP..AST_FEWS_LOG AST_FEWS_LOG 
			WHERE
				AST_FEWS_LOG.AS_ID=@@AS_ID AND 
				#ITEMS.EXFISC_CONCEPTONOMBRE='IVA' AND
				#ITEMS.TI_PORCENTAJE<>0
				-- AND
				--#ITEMS.IMPORTE<>0
			GROUP BY	AST_FEWS_LOG.TIPO_COMPROBANTE, 
						AST_FEWS_LOG.PUNTO_VENTA,
						AST_FEWS_LOG.NUMERO_COMPROBANTE,
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
						
						-- 	exec AST_FEWS_PIDECAE	1485969, true
						-- select * from FINN_MACOR_TMP..AST_FEWS_LOG
			SELECT 
					AS_ID=@@AS_ID,
					CUIT_EMPRESA= @EmpresaCuit,
					TIPO_COMPROBANTE=AST_FEWS_LOG.TIPO_COMPROBANTE,
					PUNTO_VENTA=AST_FEWS_LOG.PUNTO_VENTA,
					NUMERO_COMPROBANTE=AST_FEWS_LOG.NUMERO_COMPROBANTE,
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
				FROM #ITEMS, FINN_MACOR_TMP..AST_FEWS_LOG AST_FEWS_LOG
				WHERE AST_FEWS_LOG.AS_ID=@@AS_ID AND 
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
				isnull((select top 1 Debito.DEB_FechaReal from Debito where debito.AS_ID=AST_FEWS_LOG.AS_ID), (select a.as_FEcha +1 from Asiento as a  where a.AS_ID=AST_FEWS_LOG.AS_ID))
				,112)
				--end
			FROM FINN_MACOR_TMP..AST_FEWS_LOG AST_FEWS_LOG
			WHERE AST_FEWS_LOG.AS_ID=@@AS_ID 
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
			TIPO_COMPROBANTE=AST_FEWS_LOG.TIPO_COMPROBANTE,
			PUNTO_VENTA=AST_FEWS_LOG.PUNTO_VENTA,
			NUMERO_COMPROBANTE=AST_FEWS_LOG.NUMERO_COMPROBANTE,
			ID_OPCIONAL= 2101,
			VALOR= @EmpresaCBU
		FROM FINN_MACOR_TMP..AST_FEWS_LOG AST_FEWS_LOG
		WHERE 
			AST_FEWS_LOG.AS_ID=@@AS_ID
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
	
	
		
		IF (SELECT ISNULL(TIPO_COMPROBANTE,0) FROM FINN_MACOR_TMP..AST_FEWS_LOG AST_FEWS_LOG WHERE AST_FEWS_LOG.AS_ID=@@AS_ID  AND TIPO_COMPROBANTE IN (203,208,202,207)) <>0 -- TABLAS DE FACTURA DE CRÈDITO
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
			
			FROM FINN_MACOR_TMP..AST_FEWS_LOG AST_FEWS_LOG, #aplica
			WHERE 
			AST_FEWS_LOG.AS_ID=@@AS_ID 
			AND  TIPO_COMPROBANTE IN (203,208,202,207)
	END
END

	fetch Next From cursor_diferentes Into @@AS_ID_CURSOR 
end  
close cursor_diferentes  
deallocate cursor_diferentes  
----Fin cursor, sigo con el reporte normalmente...  

select DocumentoTipo.DOC_Nombre, asi.AS_ID, asi.AS_NumeroDoc, asi.AS_Fecha, convert(decimal(20,2), round(AS_Total,2)) AS_Total, convert(decimal(20,2), ast.IMPORTE_TOTAL) IMPORTE_TOTAL, convert(decimal(20,2), round(AS_Total,2)) - convert(decimal(20,2), ast.IMPORTE_TOTAL) DIFERENCIA, *
from asiento asi  inner join FINN_MACOR_TMP..AST_FEWS_LOG ast on asi.AS_ID=ast.AS_ID
	, DocumentoTipo
where	(
		abs(AS_Total-IMPORTE_TOTAL)>0.01
		)
	and (
		asi.AS_NumeroDoc like 'A0004%' or 
		asi.AS_NumeroDoc like 'B0004%'
		)
	and DocumentoTipo.DOC_ID = asi.DOC_ID
order by asi.AS_ID desc

/*
select * from [AST_FEWS_LOG]
*/

/*
select * from [FINN_MACOR_TMP].[dbo].[AST_FEWS_LOG]
select * from [FINN_MACOR_TMP].[dbo].[AST_FEWS_LOG_IVA]
select * from [FINN_MACOR_TMP].[dbo].[AST_FEWS_LOG_TRIBUTOS]
select * from [FINN_MACOR_TMP].[dbo].[AST_FEWS_LOG_DATOS_OPC]
select * from [FINN_MACOR_TMP].[dbo].[AST_FEWS_LOG_CBTE_ASOC]
*/

/*
select tmp.IMPORTE_TOTAL, ast.IMPORTE_TOTAL, 
		tmp.IVA_TOTAL, ast.IVA_TOTAL, 
		tmp.NETO_GRAVADO, ast.NETO_GRAVADO, 
		tmp.NETO_NOGRAVADO, ast.NETO_NOGRAVADO, 
		tmp.TRIBUTOS_TOTAL, ast.TRIBUTOS_TOTAL, *
from [FINN_MACOR_TMP].[dbo].[AST_FEWS_LOG] tmp, [AST_FEWS_LOG] ast
where tmp.AS_ID collate database_default=ast.AS_ID
	and
		(abs(convert(float, tmp.IMPORTE_TOTAL)-convert(float, ast.IMPORTE_TOTAL))>0.02 
	or abs(convert(float, tmp.IVA_TOTAL)-convert(float, ast.IVA_TOTAL))>0.02 
	or abs(convert(float, tmp.NETO_GRAVADO)-convert(float, ast.NETO_GRAVADO))>0.02 
	or abs(convert(float, tmp.NETO_NOGRAVADO)-convert(float, ast.NETO_NOGRAVADO))>0.02 
	or abs(convert(float, tmp.TRIBUTOS_TOTAL)-convert(float, ast.TRIBUTOS_TOTAL))>0.02)
*/
