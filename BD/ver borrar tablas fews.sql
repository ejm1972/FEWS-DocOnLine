select fxml.* from FEWS_ENCABEZADO fenc
left outer join FEWS_XML fxml on fenc.id = fxml.id
where fenc.resultado<>'A' or fenc.resultado is null 

select fxml.* from FEWS_ENCABEZADO fenc
left outer join FEWS_IVA fxml on fenc.id = fxml.id
where fenc.resultado<>'A' or fenc.resultado is null 

select fxml.* from FEWS_ENCABEZADO fenc
left outer join FEWS_TRIBUTO fxml on fenc.id = fxml.id
where fenc.resultado<>'A' or fenc.resultado is null 

select * from FEWS_ENCABEZADO fenc
where fenc.resultado<>'A' or fenc.resultado is null 

/*
delete FEWS_XML
from FEWS_ENCABEZADO fenc
left outer join FEWS_XML fxml on fenc.id = fxml.id
where fenc.resultado<>'A' or fenc.resultado is null 

delete FEWS_IVA
from FEWS_ENCABEZADO fenc
left outer join FEWS_IVA fxml on fenc.id = fxml.id
where fenc.resultado<>'A' or fenc.resultado is null 

delete FEWS_TRIBUTO 
from FEWS_ENCABEZADO fenc
left outer join FEWS_TRIBUTO fxml on fenc.id = fxml.id
where fenc.resultado<>'A' or fenc.resultado is null 

delete FEWS_ENCABEZADO 
from FEWS_ENCABEZADO fenc
where fenc.resultado<>'A' or fenc.resultado is null 
*/