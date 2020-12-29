--use VERNAZZA
use FEWS_VERNAZZA
--use FEWS
go

/****** Object:  StoredProcedure [dbo].[RG3685_Borrar_COMPRAS_CBTE]    Script Date: 08/25/2016 16:18:46 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[RG3685_Borrar_COMPRAS_CBTE]') AND type in (N'P', N'PC'))
DROP PROCEDURE [dbo].[RG3685_Borrar_COMPRAS_CBTE]
GO

CREATE PROCEDURE [dbo].[RG3685_Borrar_COMPRAS_CBTE]
	@Movimiento varchar(8)
AS    
BEGIN
set nocount on

begin tran

	delete RG3685_COMPRAS_ALICUOTAS
	from RG3685_COMPRAS_ALICUOTAS a, RG3685_COMPRAS_CBTE c
	where a.id_compras_cbte=c.id
	and c.movimiento=@Movimiento
	
	delete RG3685_COMPRAS_CBTE
	from RG3685_COMPRAS_CBTE c
	where c.movimiento=@Movimiento

commit tran

--por ahora no se usa, se envia un default ...
RETURN 0
set nocount off
END
GO

exec RG3685_Borrar_COMPRAS_CBTE ''
go

/*
select convert(int, convert(int, imp_neto) * case when id_alic_iva = '0004' then 0.105 when id_alic_iva = '0005' then 0.21 when id_alic_iva = '0006' then 0.27 else 0 end) iva_calc
,*
from RG3685_COMPRAS_ALICUOTAS a, RG3685_COMPRAS_CBTE c, RG3685_CABECERA ca
where a.id_compras_cbte=c.id and c.id_cabecera=ca.id
and c.movimiento='FA119988'
*/