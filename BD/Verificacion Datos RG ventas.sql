--use VERNAZZA
use FEWS_VERNAZZA
--use FEWS
go

/****** Object:  StoredProcedure [dbo].[RG3685_Verificar_VENTAS_CBTE]    Script Date: 08/25/2016 16:18:46 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[RG3685_Verificar_VENTAS_CBTE]') AND type in (N'P', N'PC'))
DROP PROCEDURE [dbo].[RG3685_Verificar_VENTAS_CBTE]
GO

CREATE PROCEDURE [dbo].[RG3685_Verificar_VENTAS_CBTE]
	@Periodo varchar(6)
AS    
BEGIN
set nocount on

	select 'DIF TOTAL - SUMA PARCIALES' ERROR, c.id, c.movimiento
	,cant_alic 
	,isnull((select count(*) from RG3685_VENTAS_ALICUOTAS where id_ventas_cbte=c.id),0) count_alic
	,convert(int, imp_total) total
	,convert(int, imp_tot_conc)+ 
	convert(int, imp_perc_no_cat)+
	convert(int, imp_op_ex)+
	convert(int, imp_perc_nac)+
	convert(int, imp_perc_iibb)+
	convert(int, imp_perc_mun)+
	convert(int, imp_internos)+
	convert(int, imp_otros_trib) sum_resto
	,convert(int, imp_total - (
	convert(int, imp_tot_conc)+ 
	convert(int, imp_perc_no_cat)+
	convert(int, imp_op_ex)+
	convert(int, imp_perc_nac)+
	convert(int, imp_perc_iibb)+
	convert(int, imp_perc_mun)+
	convert(int, imp_internos)+
	convert(int, imp_otros_trib)+
	isnull((select SUM( convert(int, imp_neto) ) from RG3685_VENTAS_ALICUOTAS where id_ventas_cbte=c.id and id_alic_iva<>'0003'),0)+
	isnull((select SUM( convert(int, imp_iva)  ) from RG3685_VENTAS_ALICUOTAS where id_ventas_cbte=c.id and id_alic_iva<>'0003'),0))) dif_total
	,isnull((select SUM( convert(int, imp_neto) ) from RG3685_VENTAS_ALICUOTAS where id_ventas_cbte=c.id and id_alic_iva<>'0003'),0) sum_neto_alic
	,isnull((select SUM( convert(int, imp_iva)  ) from RG3685_VENTAS_ALICUOTAS where id_ventas_cbte=c.id and id_alic_iva<>'0003'),0) sum_iva_alic
	,isnull((select SUM( convert(int, round(convert(int, imp_neto) * case when id_alic_iva = '0005' then 0.21 else 0 end,0)) ) from RG3685_VENTAS_ALICUOTAS where id_ventas_cbte=c.id and id_alic_iva<>'0003'),0) sum_iva_alic_calc
	,isnull((select SUM( convert(int, imp_iva)  ) from RG3685_VENTAS_ALICUOTAS where id_ventas_cbte=c.id and id_alic_iva<>'0003'),0)
	-isnull((select SUM( convert(int, round(convert(int, imp_neto) * case when id_alic_iva = '0005' then 0.21 else 0 end,0)) ) from RG3685_VENTAS_ALICUOTAS where id_ventas_cbte=c.id and id_alic_iva<>'0003'),0) dif_sum_iva
	from RG3685_VENTAS_CBTE c, RG3685_CABECERA ca
	where c.id_cabecera=ca.id and (ca.periodo=@Periodo or @Periodo = 'TODO')
	and abs(convert(int, imp_total - (
	convert(int, imp_tot_conc)+ 
	convert(int, imp_perc_no_cat)+
	convert(int, imp_op_ex)+
	convert(int, imp_perc_nac)+
	convert(int, imp_perc_iibb)+
	convert(int, imp_perc_mun)+
	convert(int, imp_internos)+
	convert(int, imp_otros_trib)+
	isnull((select SUM( convert(int, imp_neto) ) from RG3685_VENTAS_ALICUOTAS where id_ventas_cbte=c.id /*and id_alic_iva<>'0003'*/),0)+
	isnull((select SUM( convert(int, imp_iva)  ) from RG3685_VENTAS_ALICUOTAS where id_ventas_cbte=c.id /*and id_alic_iva<>'0003'*/),0))))>1
	union all
	select 'DIF CANT ALIC - COUNT ALIC' ERROR, c.id, c.movimiento
	,cant_alic 
	,isnull((select count(*) from RG3685_VENTAS_ALICUOTAS where id_ventas_cbte=c.id),0) count_alic
	,convert(int, imp_total) total
	,convert(int, imp_tot_conc)+ 
	convert(int, imp_perc_no_cat)+
	convert(int, imp_op_ex)+
	convert(int, imp_perc_nac)+
	convert(int, imp_perc_iibb)+
	convert(int, imp_perc_mun)+
	convert(int, imp_internos)+
	convert(int, imp_otros_trib) sum_resto
	,convert(int, imp_total - (
	convert(int, imp_tot_conc)+ 
	convert(int, imp_perc_no_cat)+
	convert(int, imp_op_ex)+
	convert(int, imp_perc_nac)+
	convert(int, imp_perc_iibb)+
	convert(int, imp_perc_mun)+
	convert(int, imp_internos)+
	convert(int, imp_otros_trib)+
	isnull((select SUM( convert(int, imp_neto) ) from RG3685_VENTAS_ALICUOTAS where id_ventas_cbte=c.id and id_alic_iva<>'0003'),0)+
	isnull((select SUM( convert(int, imp_iva)  ) from RG3685_VENTAS_ALICUOTAS where id_ventas_cbte=c.id and id_alic_iva<>'0003'),0))) dif_total
	,isnull((select SUM( convert(int, imp_neto) ) from RG3685_VENTAS_ALICUOTAS where id_ventas_cbte=c.id and id_alic_iva<>'0003'),0) sum_neto_alic
	,isnull((select SUM( convert(int, imp_iva)  ) from RG3685_VENTAS_ALICUOTAS where id_ventas_cbte=c.id and id_alic_iva<>'0003'),0) sum_iva_alic
	,isnull((select SUM( convert(int, round(convert(int, imp_neto) * case when id_alic_iva = '0005' then 0.21 else 0 end,0)) ) from RG3685_VENTAS_ALICUOTAS where id_ventas_cbte=c.id and id_alic_iva<>'0003'),0) sum_iva_alic_calc
	,isnull((select SUM( convert(int, imp_iva)  ) from RG3685_VENTAS_ALICUOTAS where id_ventas_cbte=c.id and id_alic_iva<>'0003'),0)
	-isnull((select SUM( convert(int, round(convert(int, imp_neto) * case when id_alic_iva = '0005' then 0.21 else 0 end,0)) ) from RG3685_VENTAS_ALICUOTAS where id_ventas_cbte=c.id and id_alic_iva<>'0003'),0) dif_sum_iva
	from RG3685_VENTAS_CBTE c, RG3685_CABECERA ca
	where c.id_cabecera=ca.id and (ca.periodo=@Periodo or @Periodo = 'TODO')
	and cant_alic<>isnull((select count(*) from RG3685_VENTAS_ALICUOTAS where id_ventas_cbte=c.id),0)
	order by c.movimiento

--por ahora no se usa, se envia un default ...
RETURN 0
set nocount off
END
GO


/****** Object:  StoredProcedure [dbo].[RG3685_Verificar_VENTAS_ALICUOTAS]    Script Date: 08/25/2016 16:18:46 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[RG3685_Verificar_VENTAS_ALICUOTAS]') AND type in (N'P', N'PC'))
DROP PROCEDURE [dbo].[RG3685_Verificar_VENTAS_ALICUOTAS]
GO

CREATE PROCEDURE [dbo].[RG3685_Verificar_VENTAS_ALICUOTAS]
	@Periodo varchar(6)
AS    
BEGIN
set nocount on

	select 'DIF IVA - IVA CALC DE NETO' ERROR, c.id, c.movimiento
	,a.id_alic_iva
	,convert(int, imp_neto) neto
	,convert(int, round(convert(int, imp_neto) * case when id_alic_iva = '0005' then 0.21 else 0 end,0)) iva_calc
	,convert(int, imp_iva) iva
	,convert(int, round(convert(int, imp_neto) * case when id_alic_iva = '0005' then 0.21 else 0 end,0)) - convert(int, imp_iva) dif_iva
	from RG3685_VENTAS_ALICUOTAS a, RG3685_VENTAS_CBTE c, RG3685_CABECERA ca
	where a.id_ventas_cbte=c.id and c.id_cabecera=ca.id and (ca.periodo=@Periodo or @Periodo = 'TODO')
	and abs(convert(int, round(convert(int, imp_neto) * case when id_alic_iva = '0005' then 0.21 else 0 end,0)) - convert(int, imp_iva))>1
	order by c.movimiento

--por ahora no se usa, se envia un default ...
RETURN 0
set nocount off
END
GO

declare @per varchar(6)
select @per = '201502'
exec RG3685_Verificar_VENTAS_CBTE @per
exec RG3685_Verificar_VENTAS_ALICUOTAS @per
go

/*
select convert(int, convert(int, imp_neto) * case when id_alic_iva = '0005' then 0.21 else 0 end) iva_calc
,*
from RG3685_VENTAS_ALICUOTAS a, RG3685_VENTAS_CBTE c
where a.id_ventas_cbte=c.id
and c.movimiento='FW066731'
*/