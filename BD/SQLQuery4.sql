exec AST_UPD_FEWS_ENCABEZADO_tmp
go

exec FEWS_PENDIENTES
go

select CAE, CODIGO, DESCRIPCION, *
from TMP_ADMIFARM.dbo.AST_FEWS_LOG
where (RESULTADO is null or RESULTADO <> 'A')
order by AS_ID desc
go

--exec FEWS_UPD_AST_FEWS_LOG_tmp xx
go 
