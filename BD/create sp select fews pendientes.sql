USE [FEWS]
GO

/****** Object:  StoredProcedure [dbo].[FEWS_PENDIENTES]    Script Date: 05/28/2016 02:24:06 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[FEWS_PENDIENTES]') AND type in (N'P', N'PC'))
DROP PROCEDURE [dbo].[FEWS_PENDIENTES]
GO

USE [FEWS]
GO

/****** Object:  StoredProcedure [dbo].[FEWS_PENDIENTES]    Script Date: 05/28/2016 02:24:06 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO


CREATE  PROCEDURE [dbo].[FEWS_PENDIENTES]  
 -- Add the parameters for the stored procedure here    

AS    
BEGIN
set nocount on

select fenc.*
from FEWS_ENCABEZADO fenc ,
(select cuit, tipo_cbte, punto_vta, min(cbte_nro) cbte_nro 
from FEWS_ENCABEZADO fenc
where fenc.resultado<>'A' or fenc.resultado is null 
group by cuit, tipo_cbte, punto_vta) pen
where fenc.cuit=pen.cuit
	and fenc.tipo_cbte=pen.tipo_cbte
	and fenc.punto_vta=pen.punto_vta
	and fenc.cbte_nro=pen.cbte_nro
order by fenc.cuit, fenc.tipo_cbte, fenc.punto_vta

set nocount on
END
GO


