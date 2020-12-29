USE [FEWS_VERNAZZA]
GO

/****** Object:  StoredProcedure [dbo].[FEWS_UPD_LOG_TIMER]    Script Date: 05/29/2016 23:27:21 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[FEWS_UPD_LOG_TIMER]') AND type in (N'P', N'PC'))
DROP PROCEDURE [dbo].[FEWS_UPD_LOG_TIMER]
GO

USE [FEWS_VERNAZZA]
GO

/****** Object:  StoredProcedure [dbo].[FEWS_UPD_LOG_TIMER]    Script Date: 05/29/2016 23:27:21 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[FEWS_UPD_LOG_TIMER]
	@objeto varchar(100),
	@opcion varchar(3)
AS    
BEGIN
set nocount on

	declare @cod int
	select @cod = 0
	
	if @opcion<>'UPD'
	begin
		if isnull((select top 1 datediff(ss, fecha_ejecucion, getdate()) from LOG_TIMER where objeto_instanciado = @objeto),999)>30
			select @cod = 20001
	end
	else
	begin
		if exists(select top 1 * from LOG_TIMER where objeto_instanciado = @objeto)
			update LOG_TIMER
			set fecha_ejecucion = getdate()
			where objeto_instanciado = @objeto
		else
			insert into LOG_TIMER (objeto_instanciado, fecha_ejecucion)
			values (@objeto, getdate())
	end

--por ahora no se usa, se envia un default ...
select @cod _codigo_, 'OK' _descripcion_, 'FINALIZADO' _observacion_

--por ahora no se usa, se envia un default ...
RETURN 0
set nocount off
END
GO


