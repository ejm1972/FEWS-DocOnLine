use fews
go

declare @as_id int
declare @id int
select @as_id = 0
select @id = 0
select resultado,cae,fecha_vencimiento,err_msg,obs,codigo,descripcion,observacion,excepcion_wsaa,excepcion_wsfev1,xml_request_afip,xml_response_afip, * 
from TMP_ADMIFARM.dbo.AST_FEWS_LOG where CUIT_EMPRESA = '30711897581' and PUNTO_VENTA='0002' and fecha_comprobante >= '20160526' 
	and as_id >= @as_id
order by CUIT_EMPRESA, TIPO_COMPROBANTE, PUNTO_VENTA, NUMERO_COMPROBANTE

select * from FEWS_ENCABEZADO where id >= @id
select * from FEWS_XML where id >= @id
select * from FEWS_IVA where id >= @id
select * from FEWS_TRIBUTO where id >= @id

select @id = 1039
select 
resultado=fenc.resultado,
		cae=fenc.cae,
		fecha_vencimiento=fenc.fecha_vto,
		err_msg=fenc.err_msg,
		obs=fenc.motivo,
		codigo=fxml.codigo,
		descripcion=fxml.descripcion,
		observacion=fxml.observacion,
		excepcion_wsaa=fxml.excepcion_wsaa,
		excepcion_wsfev1=fxml.excepcion_wsfev1,
		xml_request_afip=fxml.xml_request,
		xml_response_afip=fxml.xml_response
	
	from fews_encabezado fenc, fews_xml fxml, TMP_ADMIFARM.dbo.ast_fews_log astl--FINNEGANS.FINN_Admifarm.dbo.AST_FEWS_LOG

	where 1=1
		and fenc.id=@id
		and fenc.id=fxml.id
		and cuit_empresa=fenc.cuit
		and convert(int, tipo_comprobante)=fenc.tipo_cbte
		and convert(int, punto_venta)=fenc.punto_vta
		and convert(int, numero_comprobante)=fenc.cbte_nro
	
