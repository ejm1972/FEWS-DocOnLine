select AS_Total, AS_NumeroDoc, AST_FEWS_LOG.IMPORTE_TOTAL, convert(decimal(20,2), round(AS_Total,2)) - convert(decimal(20,2), IMPORTE_TOTAL) DIFERENCIA, AsientoItem.IA_Importe, AsientoItem.IA_IvaRI, AsientoItem.IA_Importe+AsientoItem.IA_IvaRI 'Neto+IVA Item',*
FROM asiento left outer join AST_FEWS_LOG on asiento.AS_ID=AST_FEWS_LOG.AS_ID, asientoitem
WHERE asientoitem.AIT_ID=15
and asiento.AS_ID=AsientoItem.AS_ID
and (
asiento.AS_NumeroDoc like 'A0004%' or 
asiento.AS_NumeroDoc like 'B0004%'
)
order by asiento.AS_ID desc

select DocumentoTipo.DOC_Nombre, asi.AS_ID, asi.AS_NumeroDoc, asi.AS_Fecha, convert(decimal(20,2), round(AS_Total,2)) AS_Total, convert(decimal(20,2), ast.IMPORTE_TOTAL) IMPORTE_TOTAL, convert(decimal(20,2), round(AS_Total,2)) - convert(decimal(20,2), ast.IMPORTE_TOTAL) DIFERENCIA
	, *
from asiento asi  inner join AST_FEWS_LOG ast on asi.AS_ID=ast.AS_ID
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
select *
from Asiento
where AS_NumeroDoc = 'B000400001183'

select *
FROM asientoitem
WHERE AS_ID=1540737

select *
from Asiento
WHERE AS_ID=1540324

select *
FROM asientoitem
WHERE AS_ID=1540324
*/