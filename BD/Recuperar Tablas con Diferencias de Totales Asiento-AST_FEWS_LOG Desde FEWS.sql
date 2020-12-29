select AS_NumeroDoc, convert(decimal(20,2), round(AS_Total,2)) - convert(decimal(20,2), IMPORTE_TOTAL) DIFERENCIA, 
	AST_FEWS_LOG.IMPORTE_TOTAL,
	AS_Total, 
	AsientoItem.IA_Importe, 
	AsientoItem.IA_IvaRI, 
	AsientoItem.IA_Importe+AsientoItem.IA_IvaRI 'Neto+IVA Item'
FROM server1.finn_macor2014.dbo.asiento asiento left outer join server1.finn_macor2014.dbo.AST_FEWS_LOG AST_FEWS_LOG on asiento.AS_ID=AST_FEWS_LOG.AS_ID, server1.finn_macor2014.dbo.asientoitem asientoitem
WHERE asientoitem.AIT_ID=15
and asiento.AS_ID=AsientoItem.AS_ID
and (
asiento.AS_NumeroDoc like 'A0004%' or 
asiento.AS_NumeroDoc like 'B0004%'
)
order by asiento.AS_ID desc

/*--------------------------------------------------------------------*/

select DocumentoTipo.DOC_Nombre, 
		asi.AS_ID, 
		asi.AS_NumeroDoc, 
		asi.AS_Fecha, 
		convert(decimal(20,2), round(AS_Total,2)) AS_Total, 
		convert(decimal(20,2), ast.IMPORTE_TOTAL) IMPORTE_TOTAL, 
		convert(decimal(20,2), round(AS_Total,2)) - convert(decimal(20,2), ast.IMPORTE_TOTAL) DIFERENCIA

from server1.finn_macor2014.dbo.asiento asi  inner join server1.finn_macor2014.dbo.AST_FEWS_LOG ast on asi.AS_ID=ast.AS_ID
	, server1.finn_macor2014.dbo.DocumentoTipo DocumentoTipo
where	
--		(
--		abs(AS_Total-IMPORTE_TOTAL)>0.01
--		)
--	and 
		(
		asi.AS_NumeroDoc like 'A0004%' or 
		asi.AS_NumeroDoc like 'B0004%'
		)
		
	and DocumentoTipo.DOC_ID = asi.DOC_ID

	and asi.as_id in (select distinct as_id from server1.finn_macor2014.dbo.asientoitem where AIT_ID=15)

order by asi.AS_ID -- desc

/*
select *
from server1.finn_macor2014.dbo.Asiento
where AS_NumeroDoc = 'B000400001183'

select *
FROM server1.finn_macor2014.dbo.asientoitem
WHERE AS_ID=1540737
and AIT_ID=15

select *
from server1.finn_macor2014.dbo.Asiento
WHERE AS_ID=1540324

select ia_dh, *
FROM server1.finn_macor2014.dbo.asientoitem
WHERE AS_ID=1540737
and CUE_id<>25
*/

select DocumentoTipo.DOC_Nombre, 
		asi.AS_ID, 
		asi.AS_NumeroDoc, 
		asi.AS_Fecha, 
		convert(decimal(20,2), round(AS_Total,2)) AS_Total, 
		convert(decimal(20,2), ast.IMPORTE_TOTAL) IMPORTE_TOTAL, 
		convert(decimal(20,2), round(AS_Total,2)) - convert(decimal(20,2), ast.IMPORTE_TOTAL) DIFERENCIA
		, few.imp_total
		, few.imp_tot_conc
		, few.imp_op_ex
		, few.imp_neto
		, few.imp_iva
		, few.imp_trib

from server1.finn_macor2014.dbo.asiento asi  inner join server1.finn_macor2014.dbo.AST_FEWS_LOG ast on asi.AS_ID=ast.AS_ID
	, server1.finn_macor2014.dbo.DocumentoTipo DocumentoTipo
	, fews_encabezado few
where	
--		(
--		abs(AS_Total-IMPORTE_TOTAL)>0.01
--		)
--	and 
		(
		asi.AS_NumeroDoc like 'A0004%' or 
		asi.AS_NumeroDoc like 'B0004%'
		)
		
	and DocumentoTipo.DOC_ID = asi.DOC_ID

	and asi.as_id in (select distinct as_id from server1.finn_macor2014.dbo.asientoitem where AIT_ID=15)
	
	and convert(int, ast.tipo_comprobante) = convert(int, few.tipo_cbte)
	and convert(int, ast.punto_venta) = convert(int, few.punto_vta)
	and convert(int, ast.numero_comprobante) = convert(int, few.cbte_nro)
	and convert(bigint, ast.cuit_empresa) = convert(bigint, few.cuit)

order by asi.AS_ID -- desc
