/*
FEWS Tipos de Comprobantes 
FEWS Tipos de Alicuotas
*/

/*
select 
	fe.id_interfaz,
	fe.tipo_cbte,
	fe.punto_vta,
	fe.cbte_nro,
	fe.movimiento,
	fe.imp_neto,
	fe.imp_tot_conc,
	fe.imp_op_ex,
	fi.iva_id,
	fi.importe,
	case fi.iva_id
		when 4 then '10.50'
		when 5 then '21.00'
		when 6 then '27.00'
	end,
	fe.imp_iva,
	fe.imp_trib,
	fe.imp_total
from FEWS_ENCABEZADO fe left outer join FEWS_IVA fi on fe.id = fi.id
where fecha_cbte between '20210000' and '20211231'
order by fe.id desc
*/

USE [FEWS]
GO

/****** Object:  StoredProcedure [dbo].[FEWS_RESUMEN_AUTORIZACIONES]    Script Date: 01/07/2023 07:46:21 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

/****** Object:  StoredProcedure [dbo].[FEWS_RESUMEN_AUTORIZACIONES]    Script Date: 07/01/2023 14:26:32 ******/
DROP PROCEDURE [dbo].[FEWS_RESUMEN_AUTORIZACIONES]
GO

-- exec FEWS_RESUMEN_AUTORIZACIONES '9901','20210101','20211231','DETALLE'
-- exec FEWS_RESUMEN_AUTORIZACIONES '9901','20210101','20211231','CON_TOTALES'
-- exec FEWS_RESUMEN_AUTORIZACIONES '9901','20210101','20211231','SIN_TOTALES'

CREATE PROCEDURE [dbo].[FEWS_RESUMEN_AUTORIZACIONES]
	@interfaz bigint,
	@fdesde varchar(8),
	@fhasta varchar(8),
	@tipo varchar(20)=null
as
begin

	if @tipo='CON_TOTALES' or @tipo='DETALLE' 
	begin
		select
			right('000000'+convert(varchar(6), id_interfaz),6)
				+right('00000'+convert(varchar(5), punto_vta),5)
				+right('000'+convert(varchar(3), tipo_cbte),3)
				+right('00000000'+convert(varchar(8), cbte_nro),8) orden,
			'Detalle' titulo,
			convert(bigint, max(fe.id)) id,
			fe.id_interfaz,
			fe.tipo_cbte,
			fe.punto_vta,
			fe.cbte_nro,
			fe.fecha_cbte,
			@fdesde fecha_desde,
			@fhasta fecha_hasta,
			min(fe.cbte_nro) primer_comprobante,
			max(fe.cbte_nro) ultimo_comprobante,
			sum(fe.imp_neto) importe_neto,
			sum(fe.imp_tot_conc) importe_no_gravado,
			sum(fe.imp_op_ex) importe_exento,
			sum(case when fi.iva_id = 4 then isnull(fi.importe, 0) else 0 end) importe_iva_10_50,
			sum(case when fi.iva_id = 5 then isnull(fi.importe, 0) else 0 end) importe_iva_21_00,
			sum(case when fi.iva_id = 6 then isnull(fi.importe, 0) else 0 end) importe_iva_27_00,
			sum(case when fi.iva_id not in (4,5,6) then isnull(fi.importe, 0) else 0 end) importe_iva_otras_alic,
			sum(fe.imp_iva) importe_iva,
			sum(fe.imp_trib) importe_tributo,
			sum(fe.imp_total) importe_total,
			right('000000'+convert(varchar(6), id_interfaz),6) interfaz_000000,
			right('00000'+convert(varchar(5), punto_vta),5) punto_vta_00000,
			right('000'+convert(varchar(3), tipo_cbte),3) tipo_cbte_000,
			right('00000000'+convert(varchar(8), cbte_nro),8) cbte_nro_00000000,
			right('00000'+convert(varchar(5), punto_vta),5)+right('000'+convert(varchar(3), tipo_cbte),3)  punto_vta_00000_tipo_cbte_000,
			max(isnull(ftca.nombre,right('000'+convert(varchar(3), tipo_cbte),3))) tipo_cbte_nombre,
			max(isnull(ftca.descripcion,right('000'+convert(varchar(3), tipo_cbte),3))) tipo_cbte_descripcion,
			max(isnull(ftca.prefijo,right('000'+convert(varchar(3), tipo_cbte),3))) tipo_cbte_prefijo
		into #det
		from FEWS_ENCABEZADO fe left outer join FEWS_IVA fi on fe.id = fi.id
			left outer join FEWS_TIPO_COMPROBANTE_AFIP ftca on fe.tipo_cbte = ftca.id_afip
		where fe.id_interfaz = @interfaz
			and fecha_cbte between @fdesde and @fhasta
			and @tipo is not null
			and @tipo = 'DETALLE'
		group by fe.id_interfaz, fe.tipo_cbte, fe.punto_vta, fe.cbte_nro, fe.fecha_cbte
	
		select
			right('000000'+convert(varchar(6), id_interfaz),6)
				+right('00000'+convert(varchar(5), punto_vta),5)
				+right('000'+convert(varchar(3), tipo_cbte),3)
				+'99999999' orden,
			'Total Tipo Comprobante y Punto Venta' titulo,
			convert(bigint, max(fe.id)*10) id,
			fe.id_interfaz,
			fe.tipo_cbte,
			fe.punto_vta,
			null cbte_nro,
			null fecha_cbte,
			@fdesde fecha_desde,
			@fhasta fecha_hasta,
			min(fe.cbte_nro) primer_comprobante,
			max(fe.cbte_nro) ultimo_comprobante,
			sum(fe.imp_neto) importe_neto,
			sum(fe.imp_tot_conc) importe_no_gravado,
			sum(fe.imp_op_ex) importe_exento,
			sum(case when fi.iva_id = 4 then isnull(fi.importe, 0) else 0 end) importe_iva_10_50,
			sum(case when fi.iva_id = 5 then isnull(fi.importe, 0) else 0 end) importe_iva_21_00,
			sum(case when fi.iva_id = 6 then isnull(fi.importe, 0) else 0 end) importe_iva_27_00,
			sum(case when fi.iva_id not in (4,5,6) then isnull(fi.importe, 0) else 0 end) importe_iva_otras_alic,
			sum(fe.imp_iva) importe_iva,
			sum(fe.imp_trib) importe_tributo,
			sum(fe.imp_total) importe_total,
			right('000000'+convert(varchar(6), id_interfaz),6) interfaz_000000,
			right('00000'+convert(varchar(5), punto_vta),5) punto_vta_00000,
			right('000'+convert(varchar(3), tipo_cbte),3) tipo_cbte_000,
			null cbte_nro_00000000,
			right('00000'+convert(varchar(5), punto_vta),5)+right('000'+convert(varchar(3), tipo_cbte),3)  punto_vta_00000_tipo_cbte_000,
			max(isnull(ftca.nombre,right('000'+convert(varchar(3), tipo_cbte),3))) tipo_cbte_nombre,
			max(isnull(ftca.descripcion,right('000'+convert(varchar(3), tipo_cbte),3))) tipo_cbte_descripcion,
			max(isnull(ftca.prefijo,right('000'+convert(varchar(3), tipo_cbte),3))) tipo_cbte_prefijo
		into #com
		from FEWS_ENCABEZADO fe left outer join FEWS_IVA fi on fe.id = fi.id
			left outer join FEWS_TIPO_COMPROBANTE_AFIP ftca on fe.tipo_cbte = ftca.id_afip
		where fe.id_interfaz = @interfaz
			and fecha_cbte between @fdesde and @fhasta
		group by fe.id_interfaz, fe.tipo_cbte, fe.punto_vta

		select
			right('000000'+convert(varchar(6), id_interfaz),6)
				+'9999999999999999' orden,
			'Total General' titulo,
			convert(bigint, max(fe.id)*100) id,
			fe.id_interfaz,
			null tipo_cbte,
			null punto_venta,
			null cbte_nro,
			null fecha_cbte,
			@fdesde fecha_desde,
			@fhasta fecha_hasta,
			null primer_comprobante,
			null ultimo_comprobante,
			sum(fe.imp_neto) importe_neto,
			sum(fe.imp_tot_conc) importe_no_gravado,
			sum(fe.imp_op_ex) importe_exento,
			sum(case when fi.iva_id = 4 then isnull(fi.importe, 0) else 0 end) importe_iva_10_50,
			sum(case when fi.iva_id = 5 then isnull(fi.importe, 0) else 0 end) importe_iva_21_00,
			sum(case when fi.iva_id = 6 then isnull(fi.importe, 0) else 0 end) importe_iva_27_00,
			sum(case when fi.iva_id not in (4,5,6) then isnull(fi.importe, 0) else 0 end) importe_iva_otras_alic,
			sum(fe.imp_iva) importe_iva,
			sum(fe.imp_trib) importe_tributo,
			sum(fe.imp_total) importe_total,
			right('000000'+convert(varchar(6), id_interfaz),6) interfaz_000000,
			null punto_vta_00000,
			null tipo_cbte_000,
			null cbte_nro_00000000,
			'99999999' punto_vta_00000_tipo_cbte_000,
			'' tipo_cbte_nombre,
			'' tipo_cbte_descripcion,
			'' tipo_cbte_prefijo
		into #tot
		from FEWS_ENCABEZADO fe left outer join FEWS_IVA fi on fe.id = fi.id
			left outer join FEWS_TIPO_COMPROBANTE_AFIP ftca on fe.tipo_cbte = ftca.id_afip
		where fe.id_interfaz = @interfaz
			and fecha_cbte between @fdesde and @fhasta
		group by fe.id_interfaz

		select *
		from #det
		union all
		select *
		from #com
		union all
		select *
		from #tot
		order by orden
	end 
	else if @tipo='SIN_TOTALES'
	begin
		select
			right('000000'+convert(varchar(6), id_interfaz),6)
				+right('00000'+convert(varchar(5), punto_vta),5)
				+right('000'+convert(varchar(3), tipo_cbte),3)
				+right('00000000'+convert(varchar(8), cbte_nro),8) orden,
			@tipo titulo,
			convert(bigint, max(fe.id)) id,
			fe.id_interfaz,
			fe.tipo_cbte,
			fe.punto_vta,
			fe.cbte_nro,
			fe.fecha_cbte,
			@fdesde fecha_desde,
			@fhasta fecha_hasta,
			min(fe.cbte_nro) primer_comprobante,
			max(fe.cbte_nro) ultimo_comprobante,
			sum(fe.imp_neto) importe_neto,
			sum(fe.imp_tot_conc) importe_no_gravado,
			sum(fe.imp_op_ex) importe_exento,
			sum(case when fi.iva_id = 4 then isnull(fi.importe, 0) else 0 end) importe_iva_10_50,
			sum(case when fi.iva_id = 5 then isnull(fi.importe, 0) else 0 end) importe_iva_21_00,
			sum(case when fi.iva_id = 6 then isnull(fi.importe, 0) else 0 end) importe_iva_27_00,
			sum(case when fi.iva_id not in (4,5,6) then isnull(fi.importe, 0) else 0 end) importe_iva_otras_alic,
			sum(fe.imp_iva) importe_iva,
			sum(fe.imp_trib) importe_tributo,
			sum(fe.imp_total) importe_total,
			right('000000'+convert(varchar(6), id_interfaz),6) interfaz_000000,
			right('00000'+convert(varchar(5), punto_vta),5) punto_vta_00000,
			right('000'+convert(varchar(3), tipo_cbte),3) tipo_cbte_000,
			right('00000000'+convert(varchar(8), cbte_nro),8) cbte_nro_00000000,
			right('00000'+convert(varchar(5), punto_vta),5)+right('000'+convert(varchar(3), tipo_cbte),3)  punto_vta_00000_tipo_cbte_000,
			max(isnull(ftca.nombre,right('000'+convert(varchar(3), tipo_cbte),3))) tipo_cbte_nombre,
			max(isnull(ftca.descripcion,right('000'+convert(varchar(3), tipo_cbte),3))) tipo_cbte_descripcion,
			max(isnull(ftca.prefijo,right('000'+convert(varchar(3), tipo_cbte),3))) tipo_cbte_prefijo
		into #det1
		from FEWS_ENCABEZADO fe left outer join FEWS_IVA fi on fe.id = fi.id
			left outer join FEWS_TIPO_COMPROBANTE_AFIP ftca on fe.tipo_cbte = ftca.id_afip
		where fe.id_interfaz = @interfaz
			and fecha_cbte between @fdesde and @fhasta
			and @tipo is not null
		group by fe.id_interfaz, fe.tipo_cbte, fe.punto_vta, fe.cbte_nro, fe.fecha_cbte
	
		select *
		from #det1
		order by orden
	end

end
GO

-- exec FEWS_RESUMEN_AUTORIZACIONES '9901','20210101','20211231','DETALLE'
-- exec FEWS_RESUMEN_AUTORIZACIONES '9901','20210101','20211231','SIN_TOTALES'