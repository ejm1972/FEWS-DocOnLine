CREATE PROCEDURE [dbo].[SP_DEPURA_LOG_TRANSACCIONES]  as
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


    select  @V_dias_depuracion = cast(valor as int)
    from 
    dbo.parametros p,
    dbo.parametros_valor pv
    where p.parametro = 'DEP_REG_LOG_TRANS'
    and pv.ID_PARAMETRO = p.ID_PARAMETRO
    and pv.F_VIGENCIA_HASTA = cast('09/09/9999' as datetime)
	if @@rowcount < 1 
	begin
		EXEC [dbo].[SP_GRABA_LOG]
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
				where f_INICIO_OPERACION < getdate() - cast(@V_dias_depuracion as int)           
	                     
				select @v_count_insert = @@rowcount
            end try
            begin catch
				exec dbo.SP_GRABA_LOG            
					@P_PROCESO = @V_PROCESO, 
					@p_DESCRIPCION    =  'ERROR INSERTANDO EN TABLA  DBO.LOG_TRANSACCIONES_HIS ',
					@P_F_PROCESO      =  @V_F_PROCESO,
					@P_ESTADO_PROCESO =  1
					
					select @v_error = 1
            END catch
            
            if @v_error = 0 
            BEGIN            
				DELETE dbo.LOG_TRANSACCIONES where f_INICIO_OPERACION < getdate() - cast (@V_dias_depuracion as int)                        
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
		   ELSE
			BEGIN
				select @v_DESCRIPCION    =  'FIN SE DEPURARON '+cast(@v_count_DELETE as varchar)+' REGISTROS'
				exec dbo.SP_GRABA_LOG            
						@P_PROCESO = @V_PROCESO, 
						@p_DESCRIPCION    =  @v_DESCRIPCION,
						@P_F_PROCESO      =  @V_F_PROCESO,
						@P_ESTADO_PROCESO =  0
			END
END;

CREATE   PROCEDURE [dbo].[SP_DEPURA_TRANSACCIONES]  as
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

    select  @V_dias_depuracion = valor 
		from 
		dbo.parametros p,
		dbo.parametros_valor pv
		where p.parametro = 'DEP_REG_TRANSAC'
		and pv.ID_PARAMETRO = p.ID_PARAMETRO
		and pv.F_VIGENCIA_HASTA = cast('09/09/9999' as datetime)
	if @@rowcount < 1 
		BEGIN
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
            where F_TRANSACCION < getdate() -  cast(@V_dias_depuracion as int)           
                                
            select @v_count_insert = @@rowcount
        end try
        begin catch
            exec dbo.SP_GRABA_LOG            
                @P_PROCESO = @V_PROCESO, 
                @p_DESCRIPCION    =  'ERROR INSERTANDO EN TABLA  REGISTROS_TRANSACCIONES_HIS ',
                @P_F_PROCESO      =  @V_F_PROCESO,
                @P_ESTADO_PROCESO =  1
				select @v_error = 1
            
        END catch
        SELECT @v_count_DELETE = 0
        if @v_error = 0 
            BEGIN            
				DELETE DBO.REGISTROS_TRANSACCIONES where F_TRANSACCION <  getdate() - CAST(@V_dias_depuracion AS INT)                        
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

		select @v_DESCRIPCION    =  'FIN SE DEPURARON '+cast(@v_count_DELETE as varchar)+' REGISTROS'
		begin	
			exec dbo.SP_GRABA_LOG            
            @P_PROCESO = @V_PROCESO, 
            @p_DESCRIPCION    =  @v_DESCRIPCION,
            @P_F_PROCESO      =  @V_F_PROCESO,
            @P_ESTADO_PROCESO =  0
		end	
END;