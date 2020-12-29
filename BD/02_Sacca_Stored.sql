USE Sacca
GO



IF OBJECT_ID('SP_DEPURA_CLIENTES') IS NULL
    EXEC('CREATE PROCEDURE SP_DEPURA_CLIENTES AS SET NOCOUNT ON;')
GO

ALTER procedure SP_DEPURA_CLIENTES (@p_id_provincia int)
as
/********************************************************
Proyecto MONEDERO
Funcion   Procedimiento para depurar usuarios sin cta 
          corriente dada de alta por el termino de N dias.
           

********************************************************/
BEGIN

	declare @v_dias_depuracion varchar(10), @v_count int, @v_PROCESO varchar(50), @v_F_PROCESO datetime
	declare @cur_id_cliente int, @cur_ID_COD_EXT int, @v_DESCRIPCION varchar(100), @v_error int

	DECLARE cur_CLIENTES  CURSOR for   
    select cli.id_cliente, cec.ID_COD_EXTERNO_CLIENTE from 
    dbo.clientes cli,
    dbo.COD_EXTERNOS_CLIENTES cec
    where
    cec.ID_CLIENTE = cli.ID_CLIENTE   and
    cec.F_VIGENCIA_DESDE <= getdate() - @v_dias_depuracion and 
    cec.F_VIGENCIA_HASTA = cast('09/09/9999' as datetime)
    and not exists (select ID_CLIENTE from dbo.CUENTAS_CORRIENTES where id_cliente = cli.ID_CLIENTE);        

	select @V_PROCESO = 'SP_DEPURA_CLIENTES'
	select @v_F_PROCESO = getdate()    

    BEGIN
    select @V_dias_depuracion = cast(valor as int)
    from 
    dbo.parametros p,
    dbo.parametros_valor pv
    where p.parametro = 'DIAS_BAJA_CLIENTE'
    and pv.ID_PARAMETRO = p.ID_PARAMETRO
    and pv.ID_PROVINCIA = @p_id_provincia
    and pv.F_VIGENCIA_HASTA =  cast('09/09/9999' as datetime);
	if @@rowcount < 1 
	EXEC	[dbo].[SP_GRABA_LOG]
				@P_PROCESO = @V_PROCESO,
				@p_DESCRIPCION = 'No se encontro el parametro solicitado.',
				@P_F_PROCESO = @V_F_PROCESO ,
				@P_ESTADO_PROCESO = 1
	select @v_error = 1	
	end

	IF @v_error <> 1
	BEGIN
    FETCH cur_CLIENTES into @cur_id_cliente,@cur_ID_COD_EXT 
	WHILE (@@FETCH_STATUS = 0)
	BEGIN   
         
        INSERT INTO dbo.LOG_TRANSACCIONES_HIS
            select
            ID_LOG_TRANSACCION,ID_CLIENTE,F_INICIO_OPERACION,F_FIN_OPERACION,ID_TIPO_TRANSACCION,ENTRADA,SALIDA,ID_REGISTRO_TRANSACCION,getdate() as F_PROCESO            
            from dbo.LOG_TRANSACCIONES
            where ID_CLIENTE =  @cur_id_cliente
            
         DELETE GENERAL.LOG_TRANSACCIONES
         where ID_CLIENTE =  @cur_id_cliente;               
        
         DELETE PADRON.CELULARES_CLIENTES WHERE id_cliente = @cur_id_cliente; 
        
         DELETE PADRON.COD_EXTERNOS_CLIENTES  WHERE ID_COD_EXTERNO_CLIENTE = @cur_ID_COD_EXT ;
        
         DELETE PADRON.CLIENTES WHERE ID_CLIENTE = @cur_id_cliente;
        
        select @v_count = @v_count + 1;
    
    END
	CLOSE cur_CLIENTES
	DEALLOCATE cur_CLIENTES
	END
select @v_DESCRIPCION = 'FIN SE DIERON DE BAJA ' + @v_count + ' REGISTROS'

	EXEC	[dbo].[SP_GRABA_LOG]
				@P_PROCESO = @V_PROCESO,
				@p_DESCRIPCION = @v_DESCRIPCION,
				@P_F_PROCESO = @V_PROCESO ,
				@P_ESTADO_PROCESO = 0	
    
END
GO



IF OBJECT_ID('SP_DEPURA_LOG_TRANSACCIONES') IS NULL
    EXEC('CREATE PROCEDURE SP_DEPURA_LOG_TRANSACCIONES AS SET NOCOUNT ON;')
GO

ALTER procedure SP_DEPURA_LOG_TRANSACCIONES as
/********************************************************
Proyecto MONEDERO
Funcion   Procedimiento para depurar LOG_TRANSACCIONES  
          el termino de N dias.
          

********************************************************/

BEGIN

	declare @V_dias_depuracion varchar(10),@v_count_insert int,@v_count_delete int
	declare @V_PROCESO varchar(50),@V_F_PROCESO datetime, @v_error int, @v_DESCRIPCION  varchar(50)  
	
	select @V_F_PROCESO = getdate()
	select @V_PROCESO = 'SP_DEPURA_LOG_TRANSACCIONES' 
	select @v_error = 0

	EXEC	[dbo].[SP_GRABA_LOG]
				@P_PROCESO = @V_PROCESO,
				@p_DESCRIPCION = 'Inicio proceso.',
				@P_F_PROCESO = @V_F_PROCESO ,
				@P_ESTADO_PROCESO = 0

    BEGIN
    select  @V_dias_depuracion = cast(valor as int)
    from 
    dbo.parametros p,
    dbo.parametros_valor pv
    where p.parametro = 'DEP_REG_LOG_TRANS'
    and pv.ID_PARAMETRO = p.ID_PARAMETRO
    and pv.F_VIGENCIA_HASTA = cast('09/09/9999' as datetime)
	if @@rowcount < 1 
	EXEC	[dbo].[SP_GRABA_LOG]
				@P_PROCESO = @V_PROCESO,
				@p_DESCRIPCION = 'No se encontro el parametro solicitado.',
				@P_F_PROCESO = @V_F_PROCESO ,
				@P_ESTADO_PROCESO = 1
	select @v_error = 1
    END

    IF @v_error = 0
            BEGIN try
            INSERT INTO dbo.LOG_TRANSACCIONES_HIS            
            select
            ID_LOG_TRANSACCION,ID_CLIENTE,F_INICIO_OPERACION,F_FIN_OPERACION,ID_TIPO_TRANSACCION,ENTRADA,SALIDA,ID_REGISTRO_TRANSACCION,getdate()as F_PROCESO            
            from dbo.LOG_TRANSACCIONES
            where f_INICIO_OPERACION < getdate() - @V_dias_depuracion            
                     
            select @v_count_insert = @@rowcount
            end try
            begin catch
            exec dbo.SP_GRABA_LOG            
                @P_PROCESO = @V_PROCESO, 
                @p_DESCRIPCION    =  'ERROR INSERTANDO EN TABLA  GENERAL.LOG_TRANSACCIONES_HIS ',
                @P_F_PROCESO      =  @V_F_PROCESO,
                @P_ESTADO_PROCESO =  1
				
				select @v_error = 1
            
            END catch;
            
            if @v_error = 0 
            BEGIN            
            DELETE GENERAL.LOG_TRANSACCIONES where f_INICIO_OPERACION < getdate() - @V_dias_depuracion;                        
            select @v_count_DELETE = @@rowcount
            end
        
           
           IF @v_count_INSERT != @v_count_DELETE 
			begin
				 exec dbo.SP_GRABA_LOG            
                @P_PROCESO = @V_PROCESO, 
                @p_DESCRIPCION    =  'DIFERENCIA ENTRE REGISTRO BORRADOS E INSERTADOS EN DEPURACION',
                @P_F_PROCESO      =  @V_F_PROCESO,
                @P_ESTADO_PROCESO =  1
             end  

			select @v_DESCRIPCION    =  'FIN SE DEPURARON '+@v_count_DELETE+' REGISTROS'
			begin	
				exec dbo.SP_GRABA_LOG            
                @P_PROCESO = @V_PROCESO, 
                @p_DESCRIPCION    =  @v_DESCRIPCION,
                @P_F_PROCESO      =  @V_F_PROCESO,
                @P_ESTADO_PROCESO =  0
			end	
END
GO



IF OBJECT_ID('SP_DEPURA_MOVIMIENTOS') IS NULL
    EXEC('CREATE PROCEDURE SP_DEPURA_MOVIMIENTOS AS SET NOCOUNT ON;')
GO

ALTER procedure SP_DEPURA_MOVIMIENTOS ( @p_id_provincia  int ) as
/********************************************************
Proyecto MONEDERO
Funcion   Procedimiento para depurar MOVIMIENTOS  
          el termino de N dias.
           

********************************************************/

DECLARE
@V_dias_depuracion varchar(4000),
@v_count_insert int,
@v_count_delete int,
@V_PROCESO VARCHAR(50) ,
--@ERROR_GENERAL EXCEPTION
@v_paso int,
@v_getdate datetime,
@V_ERROR_MESSAGE VARCHAR(500)
       
set @V_PROCESO	= 'CTATE.SP_DEPURA_MOVIMIENTOS'
set @v_paso 	= 0

BEGIN
BEGIN TRY
BEGIN TRANSACTION
	select @v_getdate = getdate()
	exec SP_GRABA_LOG  @P_PROCESO =  @V_PROCESO, @p_DESCRIPCION =  'INICIO', @P_F_PROCESO = @v_getdate, @P_ESTADO_PROCESO = 0;

    SET @v_paso = 10;
    BEGIN TRY
		select @V_dias_depuracion = ISNULL(VALOR,-1) --to_number(valor) 
		from 
				parametros p,
				parametros_valor pv
		where 
		    p.parametro		= 'DEP_MOVIMIENTOS_CTACTE'
		and pv.ID_PARAMETRO = p.ID_PARAMETRO
		and pv.ID_PROVINCIA = @p_id_provincia
		and pv.F_VIGENCIA_HASTA = '09/09/9999';

		IF @V_dias_depuracion = -1
		BEGIN
            SET @v_paso = 20     
            ROLLBACK TRANSACTION
            EXEC SP_GRABA_LOG @P_PROCESO = @V_PROCESO, @p_DESCRIPCION = 'No se encontro el parametro solicitado.', @P_F_PROCESO = @v_getdate, @P_ESTADO_PROCESO =  1
            COMMIT
            RETURN
		END
	END TRY
	BEGIN CATCH
			select @V_ERROR_MESSAGE = 'ERROR BUSCANDO PARAMETRO ' + ERROR_MESSAGE()
			ROLLBACK TRANSACTION
            SET @v_paso = 30
            EXEC SP_GRABA_LOG @P_PROCESO = @V_PROCESO, @p_DESCRIPCION = @V_ERROR_MESSAGE , @P_F_PROCESO = @v_getdate, @P_ESTADO_PROCESO =  1
            COMMIT
            RETURN 
	END CATCH

    SET @v_paso = 40
    BEGIN TRY
            INSERT INTO MOVIMIENTOS_CTACTE_HIS
            select
					ID_MOVIMIENTO_CTACTE,ID_TIPO_MOVIMIENTO_CTACTE,MONTO,ID_CUENTA_CORRIENTE,F_MOVIMIENTO,COD_EXTERNO_RECIBIDO, COD_EXTERNO,ID_RESERVA,
					CONVERT (CHAR(10), GETDATE(),103) F_PROCESO, SALDO,MOTIVO, ID_INTERFAZ, COD_OPERACION, ID_CANAL_VENTA
            from MOVIMIENTOS_CTACTE
            where f_movimiento < GETDATE()- @V_dias_depuracion            
            
            SET @v_count_insert = @@ROWCOUNT 
	END TRY
	BEGIN CATCH 
			select @V_ERROR_MESSAGE = 'ERROR INSERTANDO EN TABLA  MOVIMIENTOS_CTACTE_HIS ' + ERROR_MESSAGE()
			ROLLBACK TRANSACTION
            EXEC SP_GRABA_LOG @P_PROCESO = @V_PROCESO, @p_DESCRIPCION = @V_ERROR_MESSAGE , @P_F_PROCESO = @v_getdate, @P_ESTADO_PROCESO =  1
            COMMIT
            RETURN 
    END CATCH

    SET @v_paso = 50
    BEGIN TRY
            DELETE MOVIMIENTOS_CTACTE  where f_movimiento < GETDATE() - @V_dias_depuracion;           
            SET @v_count_DELETE = @@ROWCOUNT
    END TRY
    BEGIN CATCH
   			select @V_ERROR_MESSAGE = 'ERROR BORRANDO TABLA  MOVIMIENTOS_CTACTE ' + ERROR_MESSAGE()
   			ROLLBACK TRANSACTION
            EXEC SP_GRABA_LOG @P_PROCESO = @V_PROCESO, @p_DESCRIPCION = @V_ERROR_MESSAGE , @P_F_PROCESO = @v_getdate, @P_ESTADO_PROCESO =  1
            COMMIT
            RETURN 
    END CATCH
  
    IF @v_count_INSERT != @v_count_DELETE 
    BEGIN
  			select @V_ERROR_MESSAGE = 'DIFERENCIA ENTRE REGISTRO BORRADOS E INSERTADOS EN DEPURACION'
  			ROLLBACK TRANSACTION
            EXEC SP_GRABA_LOG @P_PROCESO = @V_PROCESO, @p_DESCRIPCION = @V_ERROR_MESSAGE , @P_F_PROCESO = @v_getdate, @P_ESTADO_PROCESO =  1
            COMMIT
            RETURN 
    END 
        
    SET @v_paso = 60
    select @V_ERROR_MESSAGE = 'FIN. SE DEPURARON '+ CONVERT(VARCHAR,@v_count_DELETE) + ' REGISTROS'
    EXEC SP_GRABA_LOG @P_PROCESO = @V_PROCESO, @p_DESCRIPCION = @V_ERROR_MESSAGE , @P_F_PROCESO = @v_getdate, @P_ESTADO_PROCESO =  0
    COMMIT TRANSACTION
    
END TRY
BEGIN CATCH    
    select @V_ERROR_MESSAGE = 'FIN ERROR GENERAL EN PASO ' + @v_paso + '.  Error: ' + ERROR_MESSAGE()
    ROLLBACK TRANSACTION                
    BEGIN TRANSACTION
    EXEC SP_GRABA_LOG @P_PROCESO = @V_PROCESO, @p_DESCRIPCION = @V_ERROR_MESSAGE , @P_F_PROCESO = @v_getdate, @P_ESTADO_PROCESO =  1
    COMMIT TRANSACTION
END CATCH    
END
GO



IF OBJECT_ID('SP_DEPURA_RESERVAS') IS NULL
    EXEC('CREATE PROCEDURE SP_DEPURA_RESERVAS AS SET NOCOUNT ON;')
GO

ALTER procedure  SP_DEPURA_RESERVAS (@p_id_provincia  int) as
/********************************************************
Proyecto MONEDERO
Funcion   Procedimiento para depurar RESERVAS
          el termino de N dias.
         
********************************************************/
DECLARE
@V_dias_depuracion	INT,
@V_dias_vencimiento INT,
@v_count_insert		INT,
@v_count_delete		INT,
@V_PROCESO			VARCHAR(50),
@v_paso				INT,
@v_getdate			datetime,
@V_ERROR_MESSAGE	VARCHAR(500)

SELECT @v_count_insert =0, @v_count_delete =0, @V_PROCESO = 'CTACTE.SP_DEPURA_RESERVAS',@v_paso=0, @v_getdate = getdate()
BEGIN
BEGIN TRY
BEGIN TRANSACTION

	exec SP_GRABA_LOG  @P_PROCESO =  @V_PROCESO, @p_DESCRIPCION =  'INICIO', @P_F_PROCESO = @v_getdate, @P_ESTADO_PROCESO = 0;
    
    SET @v_paso = 10
    BEGIN TRY
		select @V_dias_depuracion = CONVERT(INT,valor)
		from	parametros p,
				parametros_valor pv
		where p.parametro = 'DEP_RESERVAS'
		and pv.ID_PARAMETRO = p.ID_PARAMETRO
		and pv.ID_PROVINCIA = @p_id_provincia

		IF @@ROWCOUNT = 0
		BEGIN
            SET @v_paso = 20     
            ROLLBACK TRANSACTION
            EXEC SP_GRABA_LOG @P_PROCESO = @V_PROCESO, @p_DESCRIPCION = 'No se encontro el parametro DEP_RESERVAS.', @P_F_PROCESO = @v_getdate, @P_ESTADO_PROCESO =  1
            COMMIT
            RETURN
		END

		select @V_dias_vencimiento = CONVERT(INT,valor)
		from	parametros p,
				parametros_valor pv
		where p.parametro = 'DIAS_VEN_RES'
		and pv.ID_PARAMETRO = p.ID_PARAMETRO
		and pv.ID_PROVINCIA = @p_id_provincia
		
		IF @@ROWCOUNT = 0
		BEGIN
            SET @v_paso = 20     
            ROLLBACK TRANSACTION
            EXEC SP_GRABA_LOG @P_PROCESO = @V_PROCESO, @p_DESCRIPCION = 'No se encontro el parametro DIAS_VEN_RES.', @P_F_PROCESO = @v_getdate, @P_ESTADO_PROCESO =  1
            COMMIT
            RETURN
		END
		
    END TRY
	BEGIN CATCH
			select @V_ERROR_MESSAGE = 'ERROR BUSCANDO PARAMETRO ' + ERROR_MESSAGE()
			ROLLBACK TRANSACTION
            SET @v_paso = 30
            EXEC SP_GRABA_LOG @P_PROCESO = @V_PROCESO, @p_DESCRIPCION = @V_ERROR_MESSAGE , @P_F_PROCESO = @v_getdate, @P_ESTADO_PROCESO =  1
            COMMIT
            RETURN 
	END CATCH
    
    SET @v_paso = 30
    BEGIN TRY
            update RESERVAS set f_vencimiento = f_reserva + @V_dias_vencimiento WHERE f_vencimiento is null
                      
            SET @v_count_insert = @@ROWCOUNT 
    END TRY
   	BEGIN CATCH 
			select @V_ERROR_MESSAGE = 'ERROR ACTUALIZANDO RESERVAS SIN FECHA DE VENCIMIENTO ' + ERROR_MESSAGE()
			ROLLBACK TRANSACTION
            EXEC SP_GRABA_LOG @P_PROCESO = @V_PROCESO, @p_DESCRIPCION = @V_ERROR_MESSAGE , @P_F_PROCESO = @v_getdate, @P_ESTADO_PROCESO =  1
            COMMIT
            RETURN 
    END CATCH
    
    SET @v_paso = 40
    BEGIN TRY
            INSERT INTO RESERVAS_HIS

            select ID_RESERVA,CODIGO_RESERVA,F_RESERVA,ID_ESTADO_RESERVA,F_EXTRACCION,ID_CLIENTE,F_VENCIMIENTO,
					CONVERT (CHAR(10), GETDATE(),103) F_PROCESO
            from RESERVAS
			where (F_vencimiento < GETDATE() - @v_dias_depuracion) 
--            where (F_extraccion < GETDATE() -  @V_dias_depuracion) or (f_vencimiento < GETDATE() -  @V_dias_depuracion )    
            and ID_ESTADO_RESERVA in (2,3)      
                        
            SET @v_count_insert = @@ROWCOUNT 
    END TRY
   	BEGIN CATCH 
			select @V_ERROR_MESSAGE = 'ERROR INSERTANDO EN TABLA  RESERVAS_HIS ' + ERROR_MESSAGE()
			ROLLBACK TRANSACTION
            EXEC SP_GRABA_LOG @P_PROCESO = @V_PROCESO, @p_DESCRIPCION = @V_ERROR_MESSAGE , @P_F_PROCESO = @v_getdate, @P_ESTADO_PROCESO =  1
            COMMIT
            RETURN 
    END CATCH

    SET @v_paso = 50
    BEGIN TRY
			  DELETE RESERVAS where (F_vencimiento < GETDATE() - @v_dias_depuracion) and ID_ESTADO_RESERVA in (2,3)
 --           DELETE RESERVAS  where (F_extraccion < GETDATE() -  @V_dias_depuracion) or (f_vencimiento < GETDATE() -  @V_dias_depuracion )    
--            and ID_ESTADO_RESERVA in (2,3);
            SET @v_count_DELETE = @@ROWCOUNT
    END TRY
    BEGIN CATCH
   			select @V_ERROR_MESSAGE = 'ERROR BORRANDO TABLA RESERVAS ' + ERROR_MESSAGE()
   			ROLLBACK TRANSACTION
            EXEC SP_GRABA_LOG @P_PROCESO = @V_PROCESO, @p_DESCRIPCION = @V_ERROR_MESSAGE , @P_F_PROCESO = @v_getdate, @P_ESTADO_PROCESO =  1
            COMMIT
            RETURN 
    END CATCH
	
	IF @v_count_INSERT != @v_count_DELETE 
    BEGIN
  			select @V_ERROR_MESSAGE = 'DIFERENCIA ENTRE REGISTRO BORRADOS E INSERTADOS EN DEPURACION'
  			ROLLBACK TRANSACTION
            EXEC SP_GRABA_LOG @P_PROCESO = @V_PROCESO, @p_DESCRIPCION = @V_ERROR_MESSAGE , @P_F_PROCESO = @v_getdate, @P_ESTADO_PROCESO =  1
            COMMIT
            RETURN 
    END 

    SET @v_paso = 60
    select @V_ERROR_MESSAGE = 'FIN. SE DEPURARON '+ CONVERT(VARCHAR,@v_count_DELETE) + ' REGISTROS'
    EXEC SP_GRABA_LOG @P_PROCESO = @V_PROCESO, @p_DESCRIPCION = @V_ERROR_MESSAGE , @P_F_PROCESO = @v_getdate, @P_ESTADO_PROCESO =  0
    COMMIT TRANSACTION
END TRY
BEGIN CATCH    
    select @V_ERROR_MESSAGE = 'FIN ERROR GENERAL EN PASO ' + @v_paso + '.  Error: ' + ERROR_MESSAGE()
    ROLLBACK TRANSACTION     
    BEGIN TRANSACTION           
    EXEC SP_GRABA_LOG @P_PROCESO = @V_PROCESO, @p_DESCRIPCION = @V_ERROR_MESSAGE , @P_F_PROCESO = @v_getdate, @P_ESTADO_PROCESO =  1
    COMMIT TRANSACTION
END CATCH    
END
GO



IF OBJECT_ID('SP_DEPURA_TRANSACCIONES') IS NULL
    EXEC('CREATE PROCEDURE SP_DEPURA_TRANSACCIONES AS SET NOCOUNT ON;')
GO

ALTER  procedure  SP_DEPURA_TRANSACCIONES as
/********************************************************
Proyecto MONEDERO
Funcion   Procedimiento para depurar transacciones
          el termino de N dias.
Autor     Mmazzucco
Fecha     21/12/2012               

********************************************************/
BEGIN
	declare @V_dias_depuracion varchar(10),@v_count_insert int,@v_count_delete int
	declare @V_PROCESO varchar(50),@V_F_PROCESO datetime, @v_error int, @v_DESCRIPCION  varchar(50)  
	
	select @V_F_PROCESO = getdate()
	select @V_PROCESO = 'SP_DEPURA_TRANSACCIONES' 
	select @v_error = 0

	EXEC	[dbo].[SP_GRABA_LOG]
				@P_PROCESO = @V_PROCESO,
				@p_DESCRIPCION = 'Inicio proceso.',
				@P_F_PROCESO = @V_F_PROCESO ,
				@P_ESTADO_PROCESO = 0

    BEGIN
    select  @V_dias_depuracion = cast(valor as int)
    from 
    dbo.parametros p,
    dbo.parametros_valor pv
    where p.parametro = 'DEP_REG_TRANSAC'
    and pv.ID_PARAMETRO = p.ID_PARAMETRO
    and pv.F_VIGENCIA_HASTA = cast('09/09/9999' as datetime)
	if @@rowcount < 1 
	EXEC	[dbo].[SP_GRABA_LOG]
				@P_PROCESO = @V_PROCESO,
				@p_DESCRIPCION = 'No se encontro el parametro solicitado.',
				@P_F_PROCESO = @V_F_PROCESO ,
				@P_ESTADO_PROCESO = 1
	select @v_error = 1
    END

    IF @v_error = 0
            BEGIN try
            INSERT INTO dbo.REGISTROS_TRANSACCIONES_HIS            
            select
            ID_REGISTRO_TRANSACCION,ID_INTERFAZ,ID_CANAL_VENTA, F_TRANSACCION,COD_TRANSACCION,DATOS_TRANSACCION,SERVICIO,getdate() as F_PROCESO
            from dbo.REGISTROS_TRANSACCIONES
            where F_TRANSACCION < getdate() -  @V_dias_depuracion            
                                
            select @v_count_insert = @@rowcount
            end try
            begin catch
            exec dbo.SP_GRABA_LOG            
                @P_PROCESO = @V_PROCESO, 
                @p_DESCRIPCION    =  'ERROR INSERTANDO EN TABLA  REGISTROS_TRANSACCIONES_HIS ',
                @P_F_PROCESO      =  @V_F_PROCESO,
                @P_ESTADO_PROCESO =  1
				
				select @v_error = 1
            
            END catch;
            
            if @v_error = 0 
            BEGIN            
             DELETE GENERAL.REGISTROS_TRANSACCIONES where F_TRANSACCION <  getdate() - @V_dias_depuracion;                        
            select @v_count_DELETE = @@rowcount
            end
        
           IF @v_count_INSERT != @v_count_DELETE 
			begin
				 exec dbo.SP_GRABA_LOG            
                @P_PROCESO = @V_PROCESO, 
                @p_DESCRIPCION    =  'DIFERENCIA ENTRE REGISTRO BORRADOS E INSERTADOS EN DEPURACION',
                @P_F_PROCESO      =  @V_F_PROCESO,
                @P_ESTADO_PROCESO =  1
             end  

			select @v_DESCRIPCION    =  'FIN SE DEPURARON '+@v_count_DELETE+' REGISTROS'
			begin	
				exec dbo.SP_GRABA_LOG            
                @P_PROCESO = @V_PROCESO, 
                @p_DESCRIPCION    =  @v_DESCRIPCION,
                @P_F_PROCESO      =  @V_F_PROCESO,
                @P_ESTADO_PROCESO =  0
			end	
END
GO



IF OBJECT_ID('SP_DEPURADOR_MOVI_RESERVAS') IS NULL
    EXEC('CREATE PROCEDURE SP_DEPURADOR_MOVI_RESERVAS AS SET NOCOUNT ON;')
GO

ALTER procedure SP_DEPURADOR_MOVI_RESERVAS ( @p_id_provincia  int ) as
/********************************************************
Proyecto MONEDERO
Funcion   Procedimiento para depurar movimineros y RESERVAS          
     
********************************************************/
BEGIN
	exec SP_DEPURA_MOVIMIENTOS @p_id_provincia
	exec SP_DEPURA_RESERVAS @p_id_provincia
END
GO



IF OBJECT_ID('SP_GRABA_LOG') IS NULL
    EXEC('CREATE PROCEDURE SP_GRABA_LOG AS SET NOCOUNT ON;')
GO

ALTER PROCEDURE SP_GRABA_LOG
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



IF OBJECT_ID('SP_VENCIMIENTO_RESERVAS') IS NULL
    EXEC('CREATE PROCEDURE SP_VENCIMIENTO_RESERVAS AS SET NOCOUNT ON;')
GO

ALTER procedure SP_VENCIMIENTO_RESERVAS ( @p_id_provincia  int ) as

DECLARE
@V_dias_depuracion	varchar(4000),
@v_count			INT, 
@V_PROCESO			VARCHAR(50), 
@v_ID_TIPO_MOVIMIENTO_CTACTE INT, 
@v_paso				INT ,
@v_getdate			DATETIME,
@V_ERROR_MESSAGE	VARCHAR(500),
@C_id_reserva		INT, 
@C_fecha_reserva	DATETIME,
@C_monto			NUMERIC, 
@C_ID_CUENTA_CORRIENTE NUMERIC, 
@C_COD_EXTERNO		VARCHAR(13),
@C_OPERACION		VARCHAR(10),
@C_id_canal_venta	BIGINT

BEGIN
BEGIN TRY
	BEGIN TRANSACTION

		SELECT @v_count=0, @V_PROCESO='CTACTE.SP_VENCIMIENTO_RESERVAS', @v_ID_TIPO_MOVIMIENTO_CTACTE=5, @v_paso=0, @v_getdate = getdate();
		exec SP_GRABA_LOG  @P_PROCESO =  @V_PROCESO, @p_DESCRIPCION =  'INICIO', @P_F_PROCESO = @v_getdate, @P_ESTADO_PROCESO = 0;

		print 'error'
		SET @v_paso =10
		BEGIN TRY
 
			select @V_dias_depuracion = ISNULL(VALOR,-1)
				from 
					parametros p,
					parametros_valor pv
				where p.parametro = 'DIAS_VEN_RES'
					and pv.ID_PARAMETRO = p.ID_PARAMETRO
					and pv.ID_PROVINCIA = @p_id_provincia
					and pv.F_VIGENCIA_HASTA = '09/09/9999'
		
			IF @V_dias_depuracion = -1
				BEGIN
					ROLLBACK TRANSACTION
					SET @v_paso = 20     
					EXEC SP_GRABA_LOG @P_PROCESO = @V_PROCESO, @p_DESCRIPCION = 'No se encontro el parametro solicitado.', @P_F_PROCESO = @v_getdate, @P_ESTADO_PROCESO =  1
					RETURN
				END
		END TRY
		BEGIN CATCH	
			ROLLBACK TRANSACTION
			SET @v_paso = 30
			select @V_ERROR_MESSAGE = 'ERROR BUSCANDO PARAMETRO ' + ERROR_MESSAGE()
			EXEC SP_GRABA_LOG @P_PROCESO = @V_PROCESO, @p_DESCRIPCION = @V_ERROR_MESSAGE , @P_F_PROCESO = @v_getdate, @P_ESTADO_PROCESO =  1
			RETURN 
		END CATCH

		SET @v_paso = 40
		DECLARE CUR_RESERVAS CURSOR FOR 
			select res.id_reserva, abs(ctacte.monto) monto, res.f_reserva, ctacte.ID_CUENTA_CORRIENTE, ctacte.COD_EXTERNO, ctacte.COD_OPERACION, ctacte.ID_CANAL_VENTA  
				from	reservas res, 
						MOVIMIENTOS_CTACTE ctacte
				where res.id_estado_reserva = 1  
					and res.f_vencimiento < GETDATE()
					and ctacte.ID_RESERVA = res.ID_RESERVA;
	
		OPEN CUR_RESERVAS
		FETCH NEXT FROM CUR_RESERVAS 
			INTO @C_id_reserva, @C_monto, @C_FECHA_RESERVA, @C_ID_CUENTA_CORRIENTE, @C_COD_EXTERNO, @C_OPERACION, @C_id_canal_venta 
		WHILE @@FETCH_STATUS = 0
			BEGIN
				SET @v_paso = 50
				BEGIN TRY
					insert into MOVIMIENTOS_CTACTE
						(ID_TIPO_MOVIMIENTO_CTACTE,MONTO,ID_CUENTA_CORRIENTE,F_MOVIMIENTO,COD_EXTERNO,ID_INTERFAZ, ID_RESERVA, COD_OPERACION, ID_CANAL_VENTA)
					values
						(@v_ID_TIPO_MOVIMIENTO_CTACTE,@C_monto,@C_ID_CUENTA_CORRIENTE,GETDATE(),@C_cod_externo, 1,@C_id_reserva, @C_operacion, @C_id_canal_venta);

					update TOTALES_RESERVAS_EXTRACCIONES set cantidad_reservas = cantidad_reservas -1, monto_reservas = monto_reservas - @C_monto
						where CONVERT(varchar, fecha , 101) = CONVERT(varchar, @C_FECHA_RESERVA , 101);
				END TRY
				BEGIN CATCH
					select @V_ERROR_MESSAGE = 'ERROR BUSCANDO PARAMETRO ' + ERROR_MESSAGE()
					ROLLBACK TRANSACTION
					EXEC SP_GRABA_LOG @P_PROCESO = @V_PROCESO, @p_DESCRIPCION = @V_ERROR_MESSAGE , @P_F_PROCESO = @v_getdate, @P_ESTADO_PROCESO =  1
					RETURN 
				END CATCH            


				SET @v_paso = 60
        
				UPDATE reservas 
					set ID_ESTADO_RESERVA = 3 , f_vencimiento = GETDATE()
					where id_reserva = @C_id_reserva; 
            
				IF @@ROWCOUNT =0
					BEGIN
						ROLLBACK TRANSACTION
						select @V_ERROR_MESSAGE = 'NO SE MODIFICARON REGISTROS EN LA TABLA RESERVAS '
						EXEC SP_GRABA_LOG @P_PROCESO = @V_PROCESO, @p_DESCRIPCION = @V_ERROR_MESSAGE , @P_F_PROCESO = @v_getdate, @P_ESTADO_PROCESO =  1
						RETURN 
					END
				FETCH NEXT FROM CUR_RESERVAS 
					INTO @C_id_reserva, @C_monto, @C_FECHA_RESERVA, @C_ID_CUENTA_CORRIENTE, @C_COD_EXTERNO, @C_OPERACION, @C_id_canal_venta 
				SET @v_count = @v_count + 1
    
			END 
		CLOSE CUR_RESERVAS;
		DEALLOCATE CUR_RESERVAS;

		SET @v_paso = 70
		select @V_ERROR_MESSAGE = 'FIN, SE VENCIERON '+ CONVERT(VARCHAR,@v_count) + ' REGISTROS'
		EXEC SP_GRABA_LOG @P_PROCESO = @V_PROCESO, @p_DESCRIPCION = @V_ERROR_MESSAGE , @P_F_PROCESO = @v_getdate, @P_ESTADO_PROCESO =  0
		COMMIT TRANSACTION  
	END TRY
	BEGIN CATCH    
		select @V_ERROR_MESSAGE = 'FIN ERROR GENERAL EN PASO ' + CAST(@v_paso AS VARCHAR) + '.  Error: ' + ERROR_MESSAGE()
		ROLLBACK TRANSACTION                
		BEGIN TRANSACTION
		EXEC SP_GRABA_LOG @P_PROCESO = @V_PROCESO, @p_DESCRIPCION = @V_ERROR_MESSAGE , @P_F_PROCESO = @v_getdate, @P_ESTADO_PROCESO =  1
		COMMIT TRANSACTION
	END CATCH    
END

GO



IF OBJECT_ID('INS_PARAMETRO') IS NULL
    EXEC('CREATE PROCEDURE INS_PARAMETRO AS SET NOCOUNT ON;')
GO

ALTER PROCEDURE INS_PARAMETRO
	@PARAMETRO	VARCHAR(50),
	@DESCRIPCION_CORTA	VARCHAR(100),
	@DESCRIPCION_LARGA VARCHAR(1000),
	@VALOR		VARCHAR(4000),
	@PROVINCIA	varchar(20)
AS
BEGIN
	DECLARE @idParametro bigint
	DECLARE @idParametroValor bigint
	DECLARE @idProvincia bigint
	
	select @idProvincia = id_provincia from provincias where provincia = @PROVINCIA
	
	if (@PARAMETRO is null) OR (@PROVINCIA is null) or (@idProvincia is null)
		begin
			RAISERROR ('El parámetro y la provincia son obligatorios, o bien la provincia no existe', 16, 1)
			return -1
		end
	
	select @idParametro = id_parametro from parametros where parametro = @PARAMETRO
	if (@idParametro is null)
		begin
			select @idParametro = coalesce(max(id_parametro),0) + 1 from parametros
			insert into parametros (id_parametro, parametro, descripcion, txt_ayuda)
				values (@idParametro, @PARAMETRO, @DESCRIPCION_CORTA, @DESCRIPCION_LARGA)
			select @idParametroValor = coalesce(max(id_parametro_valor),0) + 1 from parametros_valor
			insert into parametros_valor (id_parametro_valor, id_parametro, valor, f_vigencia_desde, f_vigencia_hasta, id_provincia)
				values (@idParametroValor, @idParametro, @VALOR, '01/01/2013', '9/9/9999', @idProvincia)
		end
	else
		begin
			update parametros set descripcion = @DESCRIPCION_CORTA
								, txt_ayuda = @DESCRIPCION_LARGA
					where parametro = @PARAMETRO
			update parametros_valor set valor = @VALOR
					where id_parametro = @idParametro and id_provincia = @idProvincia
		end
END
GO



IF OBJECT_ID('INS_USUARIO') IS NULL
    EXEC('CREATE PROCEDURE INS_USUARIO AS SET NOCOUNT ON;')
GO

ALTER PROCEDURE INS_USUARIO
	@USUARIO varchar(20),
	@PASSWORD varchar(40),
	@ROL varchar(15),
	@APELLIDO varchar(40)=Null,
	@NOMBRE varchar(40)=Null,
	@EMAIL varchar(40)=Null,
	@DESCRIPCION_ROL varchar(40)=Null
AS
BEGIN
	declare @idRol bigint
	declare @idUsuario bigint

	select @idRol=id from rol where codigo=@ROL
	if (@idRol is null)
		begin
			select @idRol = coalesce(max(id),0) + 1 from rol
			if (@DESCRIPCION_ROL is null) select @DESCRIPCION_ROL = @ROL
			insert into rol (id, codigo, descripcion, visible)
				values (@idRol, @ROL, @DESCRIPCION_ROL, 1)
		end
	select @idUsuario= id from usuario where usuario = @USUARIO
	if (@idUsuario is null)
		begin
			if (@NOMBRE is null) select @NOMBRE =@USUARIO
			if (@APELLIDO is null) select @APELLIDO = @USUARIO
			insert into usuario (id_rol, usuario, password, activo, apellido, nombre, mail, visible)
				values (@idRol, @USUARIO, @PASSWORD, 1, @APELLIDO, @NOMBRE, @EMAIL, 1)
		end
	else
		begin
			if (@NOMBRE is null) select @NOMBRE = nombre from usuario where id = @idUsuario
			if (@APELLIDO is null) select @APELLIDO = apellido from usuario where id = @idUsuario
			if (@EMAIL is null) select @EMAIL = mail from usuario where id = @idUsuario
			update usuario set	password = @PASSWORD,
								id_rol = @idRol,
								apellido = @APELLIDO,
								nombre = @NOMBRE,
								mail = @EMAIL
			where id = @idUsuario
		end
END
GO



IF OBJECT_ID('INS_SECCION') IS NULL
    EXEC('CREATE PROCEDURE INS_SECCION AS SET NOCOUNT ON;')
GO

ALTER PROCEDURE INS_SECCION
	@SECCION varchar(100),
	@ORDEN_SECCION int,
	@CODIGO_SECCION varchar(15),
	@URL_SECCION varchar(150),
	@BORRAR tinyint = null
AS
BEGIN
	declare @idSeccion bigint
	declare @idItem bigint

	if (@BORRAR is not null)
		delete from seccion where descripcion = @SECCION

	select @idSeccion = id from seccion where descripcion = @SECCION
	if (@idSeccion is null)
		begin
			select @idSeccion = coalesce(max(id), 0) + 1 from seccion
			insert into seccion (id, descripcion, orden, codigo, url)
				values (@idSeccion, @SECCION, @ORDEN_SECCION, @CODIGO_SECCION, @URL_SECCION)
		end
	else
		begin
			update seccion set
					codigo = @CODIGO_SECCION,
					orden = @ORDEN_SECCION,
					url = @URL_SECCION
			where id = @idSeccion
		end
END
GO



IF OBJECT_ID('INS_ITEM') IS NULL
    EXEC('CREATE PROCEDURE INS_ITEM AS SET NOCOUNT ON;')
GO

ALTER PROCEDURE INS_ITEM
	@ITEM varchar(40),
	@CODIGO varchar(40),
	@ORDEN int,
	@URL varchar(150),
	@ICON varchar(80),
	@TARGET varchar(10),
	@SECCION varchar(100),
	@BORRAR tinyint = null
AS
BEGIN
	declare @idSeccion bigint
	declare @idItem bigint

	if (@BORRAR is not null)
		delete from item where descripcion = @ITEM

	select @idSeccion = id from seccion where descripcion = @SECCION
	if (@idSeccion is null)
		begin
			RAISERROR ('La sección no existe', 16, 1)
			return -1
		end

	select @idItem = id from item where descripcion = @ITEM
	if (@idItem is null)
		begin
			select @idItem = coalesce(max(id),0) + 1 from item
			insert into item (id, id_seccion, descripcion, orden, url, icon, codigo, target)
				values (@idItem, @idSeccion, @ITEM, @ORDEN, @URL, @ICON, @CODIGO, @TARGET)
		end
	else
		update item set 
			id_seccion = @idSeccion,
			orden = @ORDEN,
			url = @URL,
			icon = @ICON,
			codigo = @CODIGO,
			target = @TARGET
		where id = @idItem
END
GO



IF OBJECT_ID('INS_ITEM_ROL') IS NULL
    EXEC('CREATE PROCEDURE INS_ITEM_ROL AS SET NOCOUNT ON;')
GO

ALTER PROCEDURE INS_ITEM_ROL
	@ITEM varchar(40),
	@ROL varchar(15),
	@BORRAR tinyint = null
AS
BEGIN
	declare @idItem bigint
	declare @idRol bigint
	declare @idItemRol bigint
	declare @existe tinyint

	select @idItem = id from item where codigo = @ITEM
	select @idRol = id from rol where codigo = @ROL
	if (@idRol is null) or (@idItem is null)
		begin
			RAISERROR ('El rol o el item no existen', 16, 1)
			return -1
		end
	select @existe = count(*) from item_rol where id_item = @idItem and id_rol = @idRol
	if (@existe = 0 and @BORRAR is null)
		begin
			select @idItemRol = coalesce(max(id) , 0) + 1 from item_rol
			insert into item_rol (id, id_rol, id_item)
				values (@idItemRol, @idRol, @idItem)
		end
	if (@existe > 0 and @BORRAR is not null)
		begin
			delete from item_rol where id_item = @idItem and id_rol = @idRol
		end
END
GO



IF OBJECT_ID('INS_ESTADOS_RESERVAS') IS NULL
    EXEC('CREATE PROCEDURE INS_ESTADOS_RESERVAS AS SET NOCOUNT ON;')
GO

ALTER PROCEDURE INS_ESTADOS_RESERVAS
	@ID bigint,
	@DESCRIPCION varchar(30),
	@BORRAR tinyint = null
AS
BEGIN
	declare @idEstadoReserva bigint

	if (@BORRAR is not null)
		delete from estados_reservas where id_estado_reserva = @ID
	else
		begin
			select @idEstadoReserva = id_estado_reserva from estados_reservas where id_estado_reserva = @ID
			if (@idEstadoReserva is null)
				insert into estados_reservas (id_estado_reserva, estado_reserva) values (@ID, @DESCRIPCION)
			else
				update estados_reservas set estado_reserva = @DESCRIPCION where id_estado_reserva = @ID
		end
END
GO



IF OBJECT_ID('INS_ESTADOS_CIVILES') IS NULL
    EXEC('CREATE PROCEDURE INS_ESTADOS_CIVILES AS SET NOCOUNT ON;')
GO

ALTER PROCEDURE INS_ESTADOS_CIVILES
	@ID bigint,
	@DESCRIPCION varchar(30),
	@BORRAR tinyint = null
AS
BEGIN
	declare @idEstadoCivil bigint

	if (@BORRAR is not null)
		delete from estados_civiles where id = @ID
	else
		begin
			select @idEstadoCivil = id from estados_civiles where id = @ID
			if (@idEstadoCivil) is null
				insert into estados_civiles (id, descripcion) values (@ID, @DESCRIPCION)
			else
				update estados_civiles set descripcion = @DESCRIPCION where id = @ID
		end
END
GO



IF OBJECT_ID('INS_TIPO_DOCUMENTO') IS NULL
    EXEC('CREATE PROCEDURE INS_TIPO_DOCUMENTO AS SET NOCOUNT ON;')
GO

ALTER PROCEDURE INS_TIPO_DOCUMENTO
	@ID bigint,
	@DESCRIPCION varchar(30),
	@BORRAR tinyint = null
AS
BEGIN
	declare @idTipoDocumento bigint

	if (@BORRAR is not null)
		delete from tipos_doc_personas where id_tipo_doc_persona = @ID
	else
		begin
			select @idTipoDocumento = id_tipo_doc_persona from tipos_doc_personas where id_tipo_doc_persona = @ID
			if (@idTipoDocumento) is null
				insert into tipos_doc_personas (id_tipo_doc_persona, tipo_documento) values (@ID, @DESCRIPCION)
			else
				update tipos_doc_personas set tipo_documento = @DESCRIPCION where id_tipo_doc_persona = @ID
		end
END
GO



IF OBJECT_ID('INS_SEXO') IS NULL
    EXEC('CREATE PROCEDURE INS_SEXO AS SET NOCOUNT ON;')
GO

ALTER PROCEDURE INS_SEXO
	@ID bigint,
	@DESCRIPCION varchar(30),
	@BORRAR tinyint = null
AS
BEGIN
	declare @idSexo bigint

	if (@BORRAR is not null)
		delete from sexos where id_sexo = @ID
	else
		begin
			select @idSexo = id_sexo from sexos where id_sexo = @ID
			if (@idSexo) is null
				insert into sexos (id_sexo, sexo) values (@ID, @DESCRIPCION)
			else
				update sexos set sexo = @DESCRIPCION where id_sexo = @ID
		end
END
GO



IF OBJECT_ID('INS_CANAL_VENTA') IS NULL
    EXEC('CREATE PROCEDURE INS_CANAL_VENTA AS SET NOCOUNT ON;')
GO

ALTER PROCEDURE INS_CANAL_VENTA
	@ID bigint,
	@DESCRIPCION varchar(30),
	@BORRAR tinyint = null
AS
BEGIN
	declare @idCanalVenta bigint

	if (@BORRAR is not null)
		delete from canales_venta where id_canal_venta = @ID
	else
		begin
			select @idCanalVenta = id_canal_venta from canales_venta where id_canal_venta = @ID
			if (@idCanalVenta) is null
				insert into canales_venta (id_canal_venta, descripcion) values (@ID, @DESCRIPCION)
			else
				update canales_venta set descripcion = @DESCRIPCION where id_canal_venta = @ID
		end
END
GO



IF OBJECT_ID('INS_TIPO_CTA') IS NULL
    EXEC('CREATE PROCEDURE INS_TIPO_CTA AS SET NOCOUNT ON;')
GO

ALTER PROCEDURE INS_TIPO_CTA
	@ID bigint,
	@DESCRIPCION varchar(30),
	@BORRAR tinyint = null
AS
BEGIN
	declare @idTipoCta bigint

	if (@BORRAR is not null)
		delete from tipos_ctacte where id_tipo_ctacte = @ID
	else
		begin
			select @idTipoCta = id_tipo_ctacte from tipos_ctacte where id_tipo_ctacte = @ID
			if (@idTipoCta) is null
				insert into tipos_ctacte (id_tipo_ctacte, tipo_cta_corriente) values (@ID, @DESCRIPCION)
			else
				update tipos_ctacte set tipo_cta_corriente = @DESCRIPCION where id_tipo_ctacte = @ID
		end
END
GO



IF OBJECT_ID('INS_CANAL_ACCESO') IS NULL
    EXEC('CREATE PROCEDURE INS_CANAL_ACCESO AS SET NOCOUNT ON;')
GO

ALTER PROCEDURE INS_CANAL_ACCESO
	@ID bigint,
	@DESCRIPCION varchar(30),
	@BORRAR tinyint = null
AS
BEGIN
	declare @idCanalAcceso bigint

	if (@BORRAR is not null)
		delete from canales_acceso where id_canal_acceso = @ID
	else
		begin
			select @idCanalAcceso = id_canal_acceso from canales_acceso where id_canal_acceso = @ID
			if (@idCanalAcceso) is null
				insert into canales_acceso (id_canal_acceso, canal_acceso) values (@ID, @DESCRIPCION)
			else
				update canales_acceso set canal_acceso = @DESCRIPCION where id_canal_acceso = @ID
		end
END
GO



IF OBJECT_ID('INS_ESTADO_CLIENTE') IS NULL
    EXEC('CREATE PROCEDURE INS_ESTADO_CLIENTE AS SET NOCOUNT ON;')
GO

ALTER PROCEDURE INS_ESTADO_CLIENTE
	@ID bigint,
	@NOMBRE varchar(20),
	@DESCRIPCION varchar(40),
	@BORRAR tinyint = null
AS
BEGIN
	declare @idEstadoCliente bigint

	if (@BORRAR is not null)
		delete from tipo_estado_clientes where id = @ID
	else
		begin
			select @idEstadoCliente = id from tipo_estado_clientes where id = @ID
			if (@idEstadoCliente) is null
				insert into tipo_estado_clientes (id, nombre, descripcion) values (@ID, @NOMBRE, @DESCRIPCION)
			else
				update tipo_estado_clientes set nombre = @NOMBRE, descripcion = @DESCRIPCION where id = @ID
		end
END
GO



IF OBJECT_ID('INS_PAIS') IS NULL
    EXEC('CREATE PROCEDURE INS_PAIS AS SET NOCOUNT ON;')
GO

ALTER PROCEDURE INS_PAIS
	@NOMBRE varchar(20),
	@BORRAR tinyint = null
AS
BEGIN
	declare @idPais bigint

	if (@BORRAR is not null)
		delete from paises where nombre = @NOMBRE
	else
		begin
			select @idPais = id_pais from paises where nombre = @NOMBRE
			if (@idPais) is null
				begin
					select @idPais = coalesce(max(id_pais),0) + 1 from paises
					insert into paises (id_pais, nombre) values (@idPais, @NOMBRE)
				end
		end
END
GO



IF OBJECT_ID('INS_PROVINCIA') IS NULL
    EXEC('CREATE PROCEDURE INS_PROVINCIA AS SET NOCOUNT ON;')
GO

ALTER PROCEDURE INS_PROVINCIA
	@ID bigint,
	@NOMBRE varchar(20),
	@CODIGO varchar(2),
	@PAIS varchar(50),
	@BORRAR tinyint = null
AS
BEGIN
	declare @idProvincia bigint
	declare @idPais bigint

	select @idPais = id_pais from paises where nombre = @PAIS
	if (@idPais is null)
		begin
			RAISERROR ('El país no existe', 16, 1)
			return -1
		end
	if (@BORRAR is not null)
		delete from provincias where id_provincia = @ID
	else
		begin
			select @idProvincia = id_provincia from provincias where id_provincia = @ID
			if (@idProvincia) is null
				insert into provincias (id_provincia, provincia, cod_provincia, id_pais) 
					values (@ID, @NOMBRE, @CODIGO, @idPais)
			else
				update provincias set	provincia = @NOMBRE, 
										cod_provincia = @CODIGO,
										id_pais = @idPais
				where id_provincia = @ID
		end
END
GO



IF OBJECT_ID('INS_LOCALIDAD') IS NULL
    EXEC('CREATE PROCEDURE INS_LOCALIDAD AS SET NOCOUNT ON;')
GO

ALTER PROCEDURE INS_LOCALIDAD
	@NOMBRE varchar(80),
	@PROVINCIA varchar(20),
	@BORRAR tinyint = null
AS
BEGIN
	declare @idLocalidad bigint
	declare @idProvincia bigint

	select @idProvincia = id_provincia from provincias where provincia = @PROVINCIA
	if (@idProvincia is null)
		begin
			RAISERROR ('La provincia no existe', 16, 1)
			return -1
		end
	if (@BORRAR is not null)
		delete from localidades where nombre = @NOMBRE
	else
		begin
			select @idLocalidad = id_localidad from localidades where nombre = @NOMBRE
			if (@idLocalidad) is null
				begin
					select @idLocalidad = coalesce(max(id_localidad), 0) + 1 from localidades
					insert into localidades (id_localidad, nombre, id_provincia) 
						values (@idLocalidad, @NOMBRE, @idProvincia)
				end
			else
				update localidades set	id_provincia = @idProvincia
					where nombre = @NOMBRE
		end
END
GO



IF OBJECT_ID('INS_INTERFAZ') IS NULL
    EXEC('CREATE PROCEDURE INS_INTERFAZ AS SET NOCOUNT ON;')
GO

ALTER PROCEDURE INS_INTERFAZ
	@ID bigint,
	@NOMBRE varchar(50),
	@CLAVE varchar(128),
	@CANAL_ACCESO varchar(100),
	@BORRAR tinyint = null
AS
BEGIN
	declare @idInterfaz bigint
	declare @idCanal bigint

	select @idCanal = id_canal_acceso from canales_acceso where canal_acceso = @CANAL_ACCESO
	if (@idCanal is null)
		begin
			RAISERROR ('El canal de acceso no existe', 16, 1)
			return -1
		end
	if (@BORRAR is not null)
		delete from interfaces where id_interfaz = @ID
	else
		begin
			select @idInterfaz = id_interfaz from interfaces where id_interfaz = @ID
			if (@idInterfaz) is null
				insert into interfaces (id_interfaz, interfaz, clave, activado, cant_operaciones, cant_acum_operaciones, id_canal_acceso, flg_control) 
					values (@ID, @NOMBRE, @CLAVE, 'S', 0, 0, @idCanal, 'N')
			else
				update interfaces set	interfaz = @NOMBRE, 
										clave = @CLAVE,
										id_canal_acceso = @idCanal
				where id_interfaz = @ID
		end
END
GO



IF OBJECT_ID('INS_TIPO_MOVIMIENTO') IS NULL
    EXEC('CREATE PROCEDURE INS_TIPO_MOVIMIENTO AS SET NOCOUNT ON;')
GO

ALTER PROCEDURE INS_TIPO_MOVIMIENTO
	@ID bigint,
	@CODIGO varchar(20),
	@CODIGO_EXPORTACION varchar(1),
	@NOMBRE varchar(30),
	@FACTOR smallint,
	@DESCRIPCION varchar(14),
	@BORRAR tinyint = null
AS
BEGIN
	declare @idTipoMovimiento bigint

	if (@BORRAR is not null)
		delete from tipos_movimientos_ctacte where id_tipo_movimiento_ctacte = @ID
	else
		begin
			select @idTipoMovimiento = id_tipo_movimiento_ctacte from tipos_movimientos_ctacte where id_tipo_movimiento_ctacte = @ID
			if (@idTipoMovimiento) is null
				insert into tipos_movimientos_ctacte (id_tipo_movimiento_ctacte, codigo, codigo_exportacion, tipo_movimiento, factor, desc_corta) 
					values (@ID, @CODIGO, @CODIGO_EXPORTACION, @NOMBRE, @FACTOR, @DESCRIPCION)
			else
				update tipos_movimientos_ctacte set		tipo_movimiento = @NOMBRE, 
														factor = @FACTOR,
														codigo = @CODIGO, 
														codigo_exportacion = @CODIGO_EXPORTACION, 
														desc_corta = @DESCRIPCION 
				where id_tipo_movimiento_ctacte = @ID
		end
END
GO



IF OBJECT_ID('INS_TIPO_TRANSACCION') IS NULL
    EXEC('CREATE PROCEDURE INS_TIPO_TRANSACCION AS SET NOCOUNT ON;')
GO

ALTER PROCEDURE INS_TIPO_TRANSACCION
	@ID bigint,
	@NOMBRE varchar(50),
	@TRANSACCION varchar(50),
	@TRANSACCION_REST varchar(50),
	@URL_REST varchar(80),
	@BORRAR tinyint = null
AS
BEGIN
	declare @idTipoTransaccion bigint

	if (@BORRAR is not null)
		delete from tipos_transacciones where id_tipo_transaccion = @ID
	else
		begin
			select @idTipoTransaccion = id_tipo_transaccion from tipos_transacciones where id_tipo_transaccion = @ID
			if (@idTipoTransaccion) is null
				insert into tipos_transacciones (id_tipo_transaccion, nombre, transaccion, transaccion_rest, url_rest) 
					values (@ID, @NOMBRE, @TRANSACCION, @TRANSACCION_REST, @URL_REST)
			else
				update tipos_transacciones set		nombre = @NOMBRE, 
													transaccion = @TRANSACCION,
													transaccion_rest = @TRANSACCION_REST,
													url_rest = @URL_REST
				where id_tipo_transaccion = @ID
		end
END
GO



IF OBJECT_ID('INS_PERMISO_TRANSACCION_CANAL_VENTA') IS NULL
    EXEC('CREATE PROCEDURE INS_PERMISO_TRANSACCION_CANAL_VENTA AS SET NOCOUNT ON;')
GO

ALTER PROCEDURE INS_PERMISO_TRANSACCION_CANAL_VENTA
	@ID bigint,
	@NOMBRE varchar(50),
	@TRANSACCION varchar(50),
	@TRANSACCION_REST varchar(50),
	@URL_REST varchar(80),
	@BORRAR tinyint = null
AS
BEGIN
	declare @idTipoTransaccion bigint

	if (@BORRAR is not null)
		delete from tipos_transacciones where id_tipo_transaccion = @ID
	else
		begin
			select @idTipoTransaccion = id_tipo_transaccion from tipos_transacciones where id_tipo_transaccion = @ID
			if (@idTipoTransaccion) is null
				insert into tipos_transacciones (id_tipo_transaccion, nombre, transaccion, transaccion_rest, url_rest) 
					values (@ID, @NOMBRE, @TRANSACCION, @TRANSACCION_REST, @URL_REST)
			else
				update tipos_transacciones set		nombre = @NOMBRE, 
													transaccion = @TRANSACCION,
													transaccion_rest = @TRANSACCION_REST,
													url_rest = @URL_REST
				where id_tipo_transaccion = @ID
		end
END
GO



IF OBJECT_ID('INS_PERMISO_TRANSACCION_CANAL_VENTA') IS NULL
    EXEC('CREATE PROCEDURE INS_PERMISO_TRANSACCION_CANAL_VENTA AS SET NOCOUNT ON;')
GO

ALTER PROCEDURE INS_PERMISO_TRANSACCION_CANAL_VENTA
	@CANAL_VENTA varchar(50),
	@TRANSACCION varchar(50),
	@BORRAR tinyint = null
AS
BEGIN
	declare @id bigint
	declare @idTipoTransaccion bigint
	declare @idCanalVenta bigint

	select @idTipoTransaccion = id_tipo_transaccion from tipos_transacciones where nombre = @TRANSACCION
	select @idCanalVenta = id_canal_venta from canales_venta where descripcion = @CANAL_VENTA
	if (@idTipoTransaccion is null or @idCanalVenta is null)
		begin
			RAISERROR ('El canal de venta o la transacción son inexistentes', 16, 1)
			return -1
		end
	select @id = id from canales_venta_tipos_transacciones where id_canal_venta = @idCanalVenta and id_tipo_transaccion = @idTipoTransaccion

	if (@BORRAR is not null)
		delete from canales_venta_tipos_transacciones where id_canal_venta = @idCanalVenta and id_tipo_transaccion = @idTipoTransaccion
	else
		begin
			select @id = id from canales_venta_tipos_transacciones where id_canal_venta = @idCanalVenta and id_tipo_transaccion = @idTipoTransaccion
			if (@id is null)
				begin
					select @id = coalesce(max(id), 0) + 1 from canales_venta_tipos_transacciones
					insert into canales_venta_tipos_transacciones (id, id_canal_venta, id_tipo_transaccion) 
						values (@id, @idCanalVenta, @idTipoTransaccion)
				end
			else
				begin
					update canales_venta_tipos_transacciones set	
								id_canal_venta = @idCanalVenta,
								id_tipo_transaccion = @idTipoTransaccion	
						where id = @id
				end
		end
END
GO





