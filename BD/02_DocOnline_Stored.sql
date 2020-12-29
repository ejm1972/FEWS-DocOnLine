USE [master]
GO
ALTER DATABASE [DocOnline] SET  READ_WRITE 
GO
USE [DocOnline]
GO

/****** Object:  StoredProcedure [dbo].[SP_GRABA_LOG]    Script Date: 9/19/2013 11:07:53 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[SP_GRABA_LOG]
(
@P_PROCESO        varchar(50),    
@p_DESCRIPCION    varchar(100),
@P_F_PROCESO      datetime,
@P_ESTADO_PROCESO int
) as

    --
    -- 
    -- Detalle: Este proc. graba logs genéricos en una tabla genérica de logs
    --

BEGIN

		
		Insert Into dbo.Logs_genericos
			(				
				PROCESO,
				DESCRIPCION,
				F_PROCESO,
				ESTADO_PROCESO
			)
		Values
			(				
				@P_PROCESO        ,    
				@p_DESCRIPCION    ,
				@P_F_PROCESO      ,
				@P_ESTADO_PROCESO 
			)
End 


GO
