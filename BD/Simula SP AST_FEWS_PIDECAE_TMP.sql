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
		(asi.AS_NumeroDoc like 'A0005%'
		or 
		asi.AS_NumeroDoc like 'B0005%')
order by asi.AS_ID desc

open cursor_diferentes  
fetch Next From cursor_diferentes Into @@AS_ID_CURSOR  
while (@@Fetch_Status <> -1)    
begin

	exec dbo.AST_FEWS_PIDECAE_TMP @@AS_ID_CURSOR, true
	
	fetch Next From cursor_diferentes Into @@AS_ID_CURSOR 
end  
close cursor_diferentes  
deallocate cursor_diferentes  
----Fin cursor, sigo con el reporte normalmente...  

select DocumentoTipo.DOC_Nombre, asi.AS_ID, asi.AS_NumeroDoc, asi.AS_Fecha, convert(decimal(20,2), round(AS_Total,2)) AS_Total, convert(decimal(20,2), ast.IMPORTE_TOTAL) IMPORTE_TOTAL, convert(decimal(20,2), round(AS_Total,2)) - convert(decimal(20,2), ast.IMPORTE_TOTAL) DIFERENCIA, *
from asiento asi  inner join FINN_MACOR_TMP..AST_FEWS_LOG ast on asi.AS_ID=ast.AS_ID
	, DocumentoTipo
where	--(
		--abs(AS_Total-IMPORTE_TOTAL)>0.01
		--)
	--and
		(
		asi.AS_NumeroDoc like 'A0005%' or 
		asi.AS_NumeroDoc like 'B0005%'
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
