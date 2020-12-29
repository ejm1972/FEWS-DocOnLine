--use VERNAZZA
use FEWS_VERNAZZA
--use FEWS
go

/****** Object:  StoredProcedure [dbo].[RG3685_Borrar_Periodo_COMPRAS]    Script Date: 08/25/2016 16:18:46 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[RG3685_Borrar_Periodo_COMPRAS]') AND type in (N'P', N'PC'))
DROP PROCEDURE [dbo].[RG3685_Borrar_Periodo_COMPRAS]
GO

CREATE PROCEDURE [dbo].[RG3685_Borrar_Periodo_COMPRAS]
	@Periodo varchar(6)
AS    
BEGIN
set nocount on

begin tran

	--delete RG3685_CF_IMPORT_SERVICIOS
	--from RG3685_CF_IMPORT_SERVICIOS i, RG3685_CABECERA ca
	--where i.id_cabecera=ca.id and ca.periodo=@Periodo

	--delete RG3685_COMPRAS_IMPORTACIONES
	--from RG3685_COMPRAS_IMPORTACIONES a, RG3685_COMPRAS_CBTE c, RG3685_CABECERA ca
	--where a.id_compras_cbte=c.id and c.id_cabecera=ca.id and ca.periodo=@Periodo

	delete RG3685_COMPRAS_ALICUOTAS
	from RG3685_COMPRAS_ALICUOTAS a 
			left outer join RG3685_COMPRAS_CBTE c on a.id_compras_cbte=c.id
			left outer join RG3685_CABECERA ca on c.id_cabecera=ca.id
	where ca.periodo=@Periodo or c.id is null or ca.id is null
	
	delete RG3685_COMPRAS_CBTE
	from RG3685_COMPRAS_CBTE c left outer join RG3685_CABECERA ca on c.id_cabecera=ca.id
	where ca.periodo=@Periodo or ca.id is null

commit tran

--por ahora no se usa, se envia un default ...
RETURN 0
set nocount off
END
GO

exec RG3685_Borrar_Periodo_COMPRAS '201601'
go

select *
from RG3685_COMPRAS_ALICUOTAS a, RG3685_COMPRAS_CBTE c, RG3685_CABECERA ca
where a.id_compras_cbte=c.id and c.id_cabecera=ca.id 
	and ca.periodo='201601' 
	and c.cbte_nro='00000000000000001850'

select *
from RG3685_COMPRAS_CBTE c, RG3685_CABECERA ca
where c.id_cabecera=ca.id 
	and ca.periodo='201601' 
	
/*

*/