--use VERNAZZA
use FEWS_VERNAZZA
--use FEWS
go

/****** Object:  StoredProcedure [dbo].[RG3685_Verificar_COMPRAS_CBTE]    Script Date: 08/25/2016 16:18:46 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[RG3685_Verificar_COMPRAS_CBTE]') AND type in (N'P', N'PC'))
DROP PROCEDURE [dbo].[RG3685_Verificar_COMPRAS_CBTE]
GO

CREATE PROCEDURE [dbo].[RG3685_Verificar_COMPRAS_CBTE]
	@Periodo varchar(6)
AS    
BEGIN
set nocount on

	select 'DIF IVA - SUMA IVA ALIC' ERROR, c.id, movimiento
	,tipo_cbte
	,cbte_nro
	,cant_alic 
	,tipo_doc_vendedor
	,nro_doc_vendedor
	,nombre_vendedor
	,isnull((select count(*) from RG3685_COMPRAS_ALICUOTAS where id_compras_cbte=c.id),0) count_alic
	,convert(int, imp_total) total
	,convert(int, imp_tot_conc)+ 
	 convert(int, imp_op_ex)+
	 convert(int, imp_perc_iva)+
	 convert(int, imp_perc_nac)+
	 convert(int, imp_perc_iibb)+
	 convert(int, imp_perc_mun)+
	 convert(int, imp_internos)+
	 convert(int, imp_otros_trib) sum_resto
	,convert(int, imp_cf_cble) cf_cble
	,convert(int, imp_total) - ( 
	 convert(int, imp_tot_conc)+ 
	 convert(int, imp_op_ex)+
	 convert(int, imp_perc_iva)+
	 convert(int, imp_perc_nac)+
	 convert(int, imp_perc_iibb)+
	 convert(int, imp_perc_mun)+
	 convert(int, imp_internos)+
	 convert(int, imp_otros_trib)+
	 convert(int, imp_cf_cble)+
	 isnull((select SUM( convert(int, imp_neto) ) from RG3685_COMPRAS_ALICUOTAS where id_compras_cbte=c.id),0)) dif_total
	,isnull((select SUM( convert(int, imp_neto) ) from RG3685_COMPRAS_ALICUOTAS where id_compras_cbte=c.id),0) sum_neto_alic
	,isnull((select SUM( convert(int, round(convert(int, imp_neto) * case when id_alic_iva = '0004' then 0.105 when id_alic_iva = '0005' then 0.21 when id_alic_iva = '0006' then 0.27 else 0 end,0)) ) from RG3685_COMPRAS_ALICUOTAS where id_compras_cbte=c.id),0) sum_iva_alic
	,convert(int, imp_cf_cble) - isnull((select SUM( convert(int, round(convert(int, imp_neto) * case when id_alic_iva = '0004' then 0.105 when id_alic_iva = '0005' then 0.21 when id_alic_iva = '0006' then 0.27 else 0 end,0)) ) from RG3685_COMPRAS_ALICUOTAS where id_compras_cbte=c.id),0) dif_iva
	from RG3685_COMPRAS_CBTE c, RG3685_CABECERA ca
	where c.id_cabecera=ca.id and (ca.periodo=@Periodo or @Periodo = 'TODO')
	and abs(convert(int, imp_cf_cble) - isnull((select SUM( convert(int, round(convert(int, imp_neto) * case when id_alic_iva = '0004' then 0.105 when id_alic_iva = '0005' then 0.21 when id_alic_iva = '0006' then 0.27 else 0 end,0)) ) from RG3685_COMPRAS_ALICUOTAS where id_compras_cbte=c.id),0))>1
	union all
	select 'DIF TOTAL - SUMA PARCIALES' ERROR, c.id, movimiento
	,tipo_cbte
	,cbte_nro
	,cant_alic 
	,tipo_doc_vendedor
	,nro_doc_vendedor
	,nombre_vendedor
	,isnull((select count(*) from RG3685_COMPRAS_ALICUOTAS where id_compras_cbte=c.id),0) count_alic
	,convert(int, imp_total) total
	,convert(int, imp_tot_conc)+ 
	 convert(int, imp_op_ex)+
	 convert(int, imp_perc_iva)+
	 convert(int, imp_perc_nac)+
	 convert(int, imp_perc_iibb)+
	 convert(int, imp_perc_mun)+
	 convert(int, imp_internos)+
	 convert(int, imp_otros_trib) sum_resto
	,convert(int, imp_cf_cble) cf_cble
	,convert(int, imp_total) - ( 
	 convert(int, imp_tot_conc)+ 
	 convert(int, imp_op_ex)+
	 convert(int, imp_perc_iva)+
	 convert(int, imp_perc_nac)+
	 convert(int, imp_perc_iibb)+
	 convert(int, imp_perc_mun)+
	 convert(int, imp_internos)+
	 convert(int, imp_otros_trib)+
	 convert(int, imp_cf_cble)+
	 isnull((select SUM( convert(int, imp_neto) ) from RG3685_COMPRAS_ALICUOTAS where id_compras_cbte=c.id),0)) dif_total
	,isnull((select SUM( convert(int, imp_neto) ) from RG3685_COMPRAS_ALICUOTAS where id_compras_cbte=c.id),0) sum_neto_alic
	,isnull((select SUM( convert(int, round(convert(int, imp_neto) * case when id_alic_iva = '0004' then 0.105 when id_alic_iva = '0005' then 0.21 when id_alic_iva = '0006' then 0.27 else 0 end,0)) ) from RG3685_COMPRAS_ALICUOTAS where id_compras_cbte=c.id),0) sum_iva_alic
	,convert(int, imp_cf_cble) - isnull((select SUM( convert(int, round(convert(int, imp_neto) * case when id_alic_iva = '0004' then 0.105 when id_alic_iva = '0005' then 0.21 when id_alic_iva = '0006' then 0.27 else 0 end,0)) ) from RG3685_COMPRAS_ALICUOTAS where id_compras_cbte=c.id),0) dif_iva
	from RG3685_COMPRAS_CBTE c, RG3685_CABECERA ca
	where c.id_cabecera=ca.id and (ca.periodo=@Periodo or @Periodo = 'TODO')
	and abs(convert(int, imp_total) - (
	convert(int, imp_tot_conc)+ 
	convert(int, imp_op_ex)+
	convert(int, imp_perc_iva)+
	convert(int, imp_perc_nac)+
	convert(int, imp_perc_iibb)+
	convert(int, imp_perc_mun)+
	convert(int, imp_internos)+
	convert(int, imp_otros_trib)+
	convert(int, imp_cf_cble)+
	isnull((select SUM( convert(int, imp_neto) ) from RG3685_COMPRAS_ALICUOTAS where id_compras_cbte=c.id),0)))>1
	and not (tipo_cbte='011' and cant_alic=0) 
	union all
	select 'DIF CANT ALIC - COUNT ALIC' ERROR, c.id, movimiento
	,tipo_cbte
	,cbte_nro
	,cant_alic 
	,tipo_doc_vendedor
	,nro_doc_vendedor
	,nombre_vendedor
	,isnull((select count(*) from RG3685_COMPRAS_ALICUOTAS where id_compras_cbte=c.id),0) count_alic
	,convert(int, imp_total) total
	,convert(int, imp_tot_conc)+ 
	 convert(int, imp_op_ex)+
	 convert(int, imp_perc_iva)+
	 convert(int, imp_perc_nac)+
	 convert(int, imp_perc_iibb)+
	 convert(int, imp_perc_mun)+
	 convert(int, imp_internos)+
	 convert(int, imp_otros_trib) sum_resto
	,convert(int, imp_cf_cble) cf_cble
	,convert(int, imp_total) - ( 
	 convert(int, imp_tot_conc)+ 
	 convert(int, imp_op_ex)+
	 convert(int, imp_perc_iva)+
	 convert(int, imp_perc_nac)+
	 convert(int, imp_perc_iibb)+
	 convert(int, imp_perc_mun)+
	 convert(int, imp_internos)+
	 convert(int, imp_otros_trib)+
	 convert(int, imp_cf_cble)+
	 isnull((select SUM( convert(int, imp_neto) ) from RG3685_COMPRAS_ALICUOTAS where id_compras_cbte=c.id),0)) dif_total
	,isnull((select SUM( convert(int, imp_neto) ) from RG3685_COMPRAS_ALICUOTAS where id_compras_cbte=c.id),0) sum_neto_alic
	,isnull((select SUM( convert(int, round(convert(int, imp_neto) * case when id_alic_iva = '0004' then 0.105 when id_alic_iva = '0005' then 0.21 when id_alic_iva = '0006' then 0.27 else 0 end,0)) ) from RG3685_COMPRAS_ALICUOTAS where id_compras_cbte=c.id),0) sum_iva_alic
	,convert(int, imp_cf_cble) - isnull((select SUM( convert(int, round(convert(int, imp_neto) * case when id_alic_iva = '0004' then 0.105 when id_alic_iva = '0005' then 0.21 when id_alic_iva = '0006' then 0.27 else 0 end,0)) ) from RG3685_COMPRAS_ALICUOTAS where id_compras_cbte=c.id),0) dif_iva
	from RG3685_COMPRAS_CBTE c, RG3685_CABECERA ca
	where c.id_cabecera=ca.id and (ca.periodo=@Periodo or @Periodo = 'TODO')
	and cant_alic<>isnull((select count(*) from RG3685_COMPRAS_ALICUOTAS where id_compras_cbte=c.id),0)
	union all
	select 'FAC A SIN CUIT' ERROR, c.id, movimiento
	,tipo_cbte
	,cbte_nro
	,cant_alic 
	,tipo_doc_vendedor
	,nro_doc_vendedor
	,nombre_vendedor
	,isnull((select count(*) from RG3685_COMPRAS_ALICUOTAS where id_compras_cbte=c.id),0) count_alic
	,convert(int, imp_total) total
	,convert(int, imp_tot_conc)+ 
	 convert(int, imp_op_ex)+
	 convert(int, imp_perc_iva)+
	 convert(int, imp_perc_nac)+
	 convert(int, imp_perc_iibb)+
	 convert(int, imp_perc_mun)+
	 convert(int, imp_internos)+
	 convert(int, imp_otros_trib) sum_resto
	,convert(int, imp_cf_cble) cf_cble
	,convert(int, imp_total) - ( 
	 convert(int, imp_tot_conc)+ 
	 convert(int, imp_op_ex)+
	 convert(int, imp_perc_iva)+
	 convert(int, imp_perc_nac)+
	 convert(int, imp_perc_iibb)+
	 convert(int, imp_perc_mun)+
	 convert(int, imp_internos)+
	 convert(int, imp_otros_trib)+
	 convert(int, imp_cf_cble)+
	 isnull((select SUM( convert(int, imp_neto) ) from RG3685_COMPRAS_ALICUOTAS where id_compras_cbte=c.id),0)) dif_total
	,isnull((select SUM( convert(int, imp_neto) ) from RG3685_COMPRAS_ALICUOTAS where id_compras_cbte=c.id),0) sum_neto_alic
	,isnull((select SUM( convert(int, round(convert(int, imp_neto) * case when id_alic_iva = '0004' then 0.105 when id_alic_iva = '0005' then 0.21 when id_alic_iva = '0006' then 0.27 else 0 end,0)) ) from RG3685_COMPRAS_ALICUOTAS where id_compras_cbte=c.id),0) sum_iva_alic
	,convert(int, imp_cf_cble) - isnull((select SUM( convert(int, round(convert(int, imp_neto) * case when id_alic_iva = '0004' then 0.105 when id_alic_iva = '0005' then 0.21 when id_alic_iva = '0006' then 0.27 else 0 end,0)) ) from RG3685_COMPRAS_ALICUOTAS where id_compras_cbte=c.id),0) dif_iva
	from RG3685_COMPRAS_CBTE c, RG3685_CABECERA ca
	where c.id_cabecera=ca.id and (ca.periodo=@Periodo or @Periodo = 'TODO')
	and tipo_cbte='001' and tipo_doc_vendedor<>'80'
	union all
	select 'NRO COMP 0' ERROR, c.id, movimiento
	,tipo_cbte
	,cbte_nro
	,cant_alic 
	,tipo_doc_vendedor
	,nro_doc_vendedor
	,nombre_vendedor
	,isnull((select count(*) from RG3685_COMPRAS_ALICUOTAS where id_compras_cbte=c.id),0) count_alic
	,convert(int, imp_total) total
	,convert(int, imp_tot_conc)+ 
	 convert(int, imp_op_ex)+
	 convert(int, imp_perc_iva)+
	 convert(int, imp_perc_nac)+
	 convert(int, imp_perc_iibb)+
	 convert(int, imp_perc_mun)+
	 convert(int, imp_internos)+
	 convert(int, imp_otros_trib) sum_resto
	,convert(int, imp_cf_cble) cf_cble
	,convert(int, imp_total) - ( 
	 convert(int, imp_tot_conc)+ 
	 convert(int, imp_op_ex)+
	 convert(int, imp_perc_iva)+
	 convert(int, imp_perc_nac)+
	 convert(int, imp_perc_iibb)+
	 convert(int, imp_perc_mun)+
	 convert(int, imp_internos)+
	 convert(int, imp_otros_trib)+
	 convert(int, imp_cf_cble)+
	 isnull((select SUM( convert(int, imp_neto) ) from RG3685_COMPRAS_ALICUOTAS where id_compras_cbte=c.id),0)) dif_total
	,isnull((select SUM( convert(int, imp_neto) ) from RG3685_COMPRAS_ALICUOTAS where id_compras_cbte=c.id),0) sum_neto_alic
	,isnull((select SUM( convert(int, round(convert(int, imp_neto) * case when id_alic_iva = '0004' then 0.105 when id_alic_iva = '0005' then 0.21 when id_alic_iva = '0006' then 0.27 else 0 end,0)) ) from RG3685_COMPRAS_ALICUOTAS where id_compras_cbte=c.id),0) sum_iva_alic
	,convert(int, imp_cf_cble) - isnull((select SUM( convert(int, round(convert(int, imp_neto) * case when id_alic_iva = '0004' then 0.105 when id_alic_iva = '0005' then 0.21 when id_alic_iva = '0006' then 0.27 else 0 end,0)) ) from RG3685_COMPRAS_ALICUOTAS where id_compras_cbte=c.id),0) dif_iva
	from RG3685_COMPRAS_CBTE c, RG3685_CABECERA ca
	where c.id_cabecera=ca.id and (ca.periodo=@Periodo or @Periodo = 'TODO')
	and cbte_nro='00000000000000000000'
	order by c.movimiento

--por ahora no se usa, se envia un default ...
RETURN 0
set nocount off
END
GO

/****** Object:  StoredProcedure [dbo].[RG3685_Verificar_COMPRAS_ALICUOTAS]    Script Date: 08/25/2016 16:18:46 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[RG3685_Verificar_COMPRAS_ALICUOTAS]') AND type in (N'P', N'PC'))
DROP PROCEDURE [dbo].[RG3685_Verificar_COMPRAS_ALICUOTAS]
GO

CREATE PROCEDURE [dbo].[RG3685_Verificar_COMPRAS_ALICUOTAS]
	@Periodo varchar(6)
AS    
BEGIN
set nocount on

	select 'DIF IVA - IVA CALC DE NETO' ERROR, c.id, c.movimiento
	,a.id_alic_iva
	,convert(int, imp_neto) neto
	,convert(int, round(convert(int, imp_neto) * case when id_alic_iva = '0004' then 0.105 when id_alic_iva = '0005' then 0.21 when id_alic_iva = '0006' then 0.27 else 0 end,0)) iva_calc
	,convert(int, imp_iva) iva
	,convert(int, round(convert(int, imp_neto) * case when id_alic_iva = '0004' then 0.105 when id_alic_iva = '0005' then 0.21 when id_alic_iva = '0006' then 0.27 else 0 end,0)) - convert(int, imp_iva) dif_iva
	from RG3685_COMPRAS_ALICUOTAS a, RG3685_COMPRAS_CBTE c, RG3685_CABECERA ca
	where a.id_compras_cbte=c.id and c.id_cabecera=ca.id and (ca.periodo=@Periodo or @Periodo = 'TODO')
	and abs(convert(int, round(convert(int, imp_neto) * case when id_alic_iva = '0004' then 0.105 when id_alic_iva = '0005' then 0.21 when id_alic_iva = '0006' then 0.27 else 0 end,0)) - convert(int, imp_iva))>1
	order by c.movimiento

--por ahora no se usa, se envia un default ...
RETURN 0
set nocount off
END
GO

declare @per varchar(6)
select @per = '201501'
exec RG3685_Verificar_COMPRAS_CBTE @per
exec RG3685_Verificar_COMPRAS_ALICUOTAS @per
go

/*
select convert(int, convert(int, imp_neto) * case when id_alic_iva = '0004' then 0.105 when id_alic_iva = '0005' then 0.21 when id_alic_iva = '0006' then 0.27 else 0 end) iva_calc
,*
from RG3685_COMPRAS_ALICUOTAS a, RG3685_COMPRAS_CBTE c, RG3685_CABECERA ca
where a.id_compras_cbte=c.id and c.id_cabecera=ca.id
--and c.movimiento='FA119988'
and c.cbte_nro like '%112779%'
*/