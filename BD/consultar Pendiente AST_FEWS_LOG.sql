USE FEWS
GO

drop table #pen
go

--Recupera lo proximo a autorizarse en AST por cada CUIT, TIPO COMPROBANTE, PUNTO VENTA
select CUIT_EMPRESA CE, TIPO_COMPROBANTE TC, PUNTO_VENTA PV, min(NUMERO_COMPROBANTE) NC, convert(int, TIPO_COMPROBANTE ) NTC, convert(int, PUNTO_VENTA) NPV, convert(int, min(NUMERO_COMPROBANTE)) NNC, 0 IDC
, (select top 1 id_interfaz from INTERFACES where CUIT_SUSCRIPCION=CUIT_EMPRESA COLLATE DATABASE_DEFAULT) ID_INTERFAZ
into #pen
from FAF_HIDROTERMIA.dbo.AST_FEWS_LOG
where RESULTADO is null or RESULTADO <> 'A'
group by CUIT_EMPRESA, TIPO_COMPROBANTE, PUNTO_VENTA

select * from #pen
go