USE [FEWS]
GO

/****** Object:  StoredProcedure [dbo].[FEWS_UPD_AST_FEWS_LOG_tmp]    Script Date: 06/06/2016 11:21:14 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[FEWS_UPD_AST_FEWS_LOG_tmp]') AND type in (N'P', N'PC'))
DROP PROCEDURE [dbo].[FEWS_UPD_AST_FEWS_LOG_tmp]
GO

USE [FEWS]
GO

/****** Object:  StoredProcedure [dbo].[FEWS_UPD_AST_FEWS_LOG_tmp]    Script Date: 06/06/2016 11:21:14 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO


CREATE PROCEDURE [dbo].[FEWS_UPD_AST_FEWS_LOG_tmp]
	@id int
AS
BEGIN 
SET NOCOUNT ON
	
	if exists(select top 1 as_id 
			from TMP_ADMIFARM.dbo.AST_FEWS_LOG l, FEWS_ENCABEZADO e 
			where 1=1
			and l.CUIT_EMPRESA=e.cuit
			and convert(int, l.TIPO_COMPROBANTE)=e.tipo_cbte
			and convert(int, l.PUNTO_VENTA)=e.punto_vta
			and convert(int, l.NUMERO_COMPROBANTE)=e.cbte_nro)
	begin
	
		update TMP_ADMIFARM.dbo.ast_fews_log --FINNEGANS.FINN_Admifarm.dbo.AST_FEWS_LOG
		set RESULTADO=e.resultado,
			CAE=e.cae,
			FECHA_VENCIMIENTO=e.fecha_vto,
			ERR_MSG=e.err_msg,
			OBS=e.motivo,
			CODIGO=x.codigo,
			DESCRIPCION=x.descripcion,
			OBSERVACION=x.observacion,
			EXCEPCION_WSAA=x.excepcion_wsaa,
			EXCEPCION_WSFEV1=x.excepcion_wsfev1,
			XML_REQUEST_AFIP=x.xml_request,
			XML_RESPONSE_AFIP=x.xml_response
		from FEWS_ENCABEZADO e, FEWS_XML x, TMP_ADMIFARM.dbo.AST_FEWS_LOG l --FINNEGANS.FINN_Admifarm.dbo.AST_FEWS_LOG
		where 1=1
			and e.id=@id
			and e.id=x.id
			and l.CUIT_EMPRESA=e.cuit
			and convert(int, l.TIPO_COMPROBANTE)=e.tipo_cbte
			and convert(int, l.PUNTO_VENTA)=e.punto_vta
			and convert(int, l.NUMERO_COMPROBANTE)=e.cbte_nro
	end
	else
	begin
		
		insert into TMP_ADMIFARM.dbo.AST_FEWS_LOG --FINNEGANS.FINN_Admifarm.dbo.AST_FEWS_LOG
		()
		select *
		from FEWS_ENCABEZADO e, FEWS_XML x
		where 1=1
			and e.id=@id
			and e.id=x.id
			
INSERT INTO [TMP_ADMIFARM].[dbo].[AST_FEWS_LOG]
           ([CUIT_EMPRESA]
           ,[TIPODOC_CLIENTE]
           ,[NRODOC_CLIENTE]
           ,[AS_ID]
           ,[TIPO_COMPROBANTE]
           ,[PUNTO_VENTA]
           ,[NUMERO_COMPROBANTE]
           ,[FECHA_COMPROBANTE]
           ,[CONCEPTO_FACTURA]
           ,[MONEDA]
           ,[MONEDA_CTZ]
           ,[IMPORTE_TOTAL]
           ,[NETO_NOGRAVADO]
           ,[NETO_GRAVADO]
           ,[NETO_EXENTO]
           ,[IVA_TOTAL]
           ,[TRIBUTOS_TOTAL]
           ,[CAE]
           ,[FECHA_VENCIMIENTO]
           ,[CODIGO]
           ,[DESCRIPCION]
           ,[OBSERVACION]
           ,[EXCEPCION_WSAA]
           ,[EXCEPCION_WSFEV1]
           ,[RESULTADO]
           ,[ERR_MSG]
           ,[OBS]
           ,[XML_REQUEST_AFIP]
           ,[XML_RESPONSE_AFIP])
     VALUES
           (<CUIT_EMPRESA, varchar(20),>
           ,<TIPODOC_CLIENTE, varchar(20),>
           ,<NRODOC_CLIENTE, varchar(20),>
           ,<AS_ID, varchar(20),>
           ,<TIPO_COMPROBANTE, varchar(3),>
           ,<PUNTO_VENTA, varchar(10),>
           ,<NUMERO_COMPROBANTE, varchar(20),>
           ,<FECHA_COMPROBANTE, varchar(10),>
           ,<CONCEPTO_FACTURA, varchar(2),>
           ,<MONEDA, varchar(20),>
           ,<MONEDA_CTZ, varchar(20),>
           ,<IMPORTE_TOTAL, varchar(20),>
           ,<NETO_NOGRAVADO, varchar(20),>
           ,<NETO_GRAVADO, varchar(20),>
           ,<NETO_EXENTO, varchar(20),>
           ,<IVA_TOTAL, varchar(20),>
           ,<TRIBUTOS_TOTAL, varchar(20),>
           ,<CAE, varchar(20),>
           ,<FECHA_VENCIMIENTO, varchar(10),>
           ,<CODIGO, varchar(10),>
           ,<DESCRIPCION, varchar(1000),>
           ,<OBSERVACION, varchar(1000),>
           ,<EXCEPCION_WSAA, varchar(1000),>
           ,<EXCEPCION_WSFEV1, varchar(1000),>
           ,<RESULTADO, varchar(10),>
           ,<ERR_MSG, varchar(1000),>
           ,<OBS, varchar(1000),>
           ,<XML_REQUEST_AFIP, text,>
           ,<XML_RESPONSE_AFIP, text,>)
GO

SELECT TOP 1000 [id]
      ,[xml_request]
      ,[xml_response]
      ,[ts]
      ,[codigo]
      ,[descripcion]
      ,[observacion]
      ,[excepcion_wsaa]
      ,[excepcion_wsfev1]
      ,[resultado]
      ,[err_msg]
      ,[obs]
  FROM [FEWS].[dbo].[FEWS_XML]

SELECT TOP 1000 [id]
      ,[tipo_reg]
      ,[webservice]
      ,[id_interfaz]
      ,[movimiento]
      ,[cuit]
      ,[concepto]
      ,[fecha_cbte]
      ,[tipo_cbte]
      ,[punto_vta]
      ,[cbte_nro]
      ,[tipo_expo]
      ,[permiso_existente]
      ,[dst_cmp]
      ,[nombre_cliente]
      ,[tipo_doc]
      ,[nro_doc]
      ,[domicilio_cliente]
      ,[id_impositivo]
      ,[imp_total]
      ,[imp_tot_conc]
      ,[imp_neto]
      ,[imp_iva]
      ,[imp_op_ex]
      ,[imp_trib]
      ,[impto_liq]
      ,[impto_liq_rni]
      ,[impto_perc]
      ,[imp_iibb]
      ,[impto_perc_mun]
      ,[imp_internos]
      ,[moneda_id]
      ,[moneda_ctz]
      ,[obs_comerciales]
      ,[obs_generales]
      ,[forma_pago]
      ,[incoterms]
      ,[incoterms_ds]
      ,[idioma_cbte]
      ,[zona]
      ,[fecha_venc_pago]
      ,[presta_serv]
      ,[fecha_serv_desde]
      ,[fecha_serv_hasta]
      ,[cae]
      ,[fecha_vto]
      ,[resultado]
      ,[reproceso]
      ,[motivo]
      ,[telefono_cliente]
      ,[localidad_cliente]
      ,[provincia_cliente]
      ,[formato_id]
      ,[email]
      ,[pdf]
      ,[err_code]
      ,[err_msg]
      ,[dato_adicional1]
      ,[dato_adicional2]
      ,[dato_adicional3]
      ,[dato_adicional4]
      ,[dato_adicional5]
      ,[dato_adicional6]
      ,[dato_adicional7]
      ,[estado_importacion]
  FROM [FEWS].[dbo].[FEWS_ENCABEZADO]
  
  			

		insert into TMP_ADMIFARM.dbo.AST_FEWS_LOG_IVA --FINNEGANS.FINN_Admifarm.dbo.AST_FEWS_LOG
		()
		select *
		from FEWS_ENCABEZADO e, FEWS_IVA i
		where 1=1
			and e.id=@id
			and e.id=i.id
		INSERT INTO [TMP_ADMIFARM].[dbo].[AST_FEWS_LOG_IVA]
           ([AS_ID]
           ,[CUIT_EMPRESA]
           ,[TIPO_COMPROBANTE]
           ,[PUNTO_VENTA]
           ,[NUMERO_COMPROBANTE]
           ,[PORCENTAJE]
           ,[NETO_GRAVADO]
           ,[IMPORTE])
     VALUES
           (<AS_ID, varchar(20),>
           ,<CUIT_EMPRESA, varchar(20),>
           ,<TIPO_COMPROBANTE, varchar(3),>
           ,<PUNTO_VENTA, varchar(10),>
           ,<NUMERO_COMPROBANTE, varchar(20),>
           ,<PORCENTAJE, numeric(12,4),>
           ,<NETO_GRAVADO, varchar(20),>
           ,<IMPORTE, varchar(20),>)
GO

		IVA_ID= CASE AST_FEWS_LOG_IVA.PORCENTAJE
					WHEN 10.5 THEN	4
					WHEN 21 THEN	5
					WHEN 27 THEN	6
				END ,

SELECT TOP 1000 [id]
      ,[tipo_reg]
      ,[iva_id]
      ,[base_imp]
      ,[importe]
  FROM [FEWS].[dbo].[FEWS_IVA]
  
  

		insert into TMP_ADMIFARM.dbo.AST_FEWS_LOG_TRIBUTOS --FINNEGANS.FINN_Admifarm.dbo.AST_FEWS_LOG
		()
		select *
		from FEWS_ENCABEZADO e, FEWS_TRIBUTO t
		where 1=1
			and e.id=@id
			and e.id=t.id
INSERT INTO [TMP_ADMIFARM].[dbo].[AST_FEWS_LOG_TRIBUTOS]
           ([AS_ID]
           ,[CUIT_EMPRESA]
           ,[TIPO_COMPROBANTE]
           ,[PUNTO_VENTA]
           ,[NUMERO_COMPROBANTE]
           ,[TRIBUTO_ID]
           ,[DESCRPTION]
           ,[PORCENTAJE]
           ,[NETO_GRAVADO]
           ,[IMPORTE])
     VALUES
           (<AS_ID, varchar(20),>
           ,<CUIT_EMPRESA, varchar(20),>
           ,<TIPO_COMPROBANTE, varchar(3),>
           ,<PUNTO_VENTA, varchar(10),>
           ,<NUMERO_COMPROBANTE, varchar(20),>
           ,<TRIBUTO_ID, varchar(20),>
           ,<DESCRPTION, varchar(50),>
           ,<PORCENTAJE, numeric(12,4),>
           ,<NETO_GRAVADO, varchar(20),>
           ,<IMPORTE, varchar(20),>)
GO


SELECT TOP 1000 [id]
      ,[tipo_reg]
      ,[tributo_id]
      ,[tributo_desc]
      ,[base_imp]
      ,[alic]
      ,[importe]
  FROM [FEWS].[dbo].[FEWS_TRIBUTO]
  
  	end

	--por ahora no se usa, se envia un default ...
	select 0 _codigo_, 'OK' _descripcion_, 'FINALIZADO' _observacion_

	--por ahora no se usa, se envia un default ...
	RETURN 0	
SET NOCOUNT OFF
END

GO


