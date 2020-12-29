USE [FINN_MACOR2014]
GO

drop table #AST_FEWS_BORRAR
go

--------- borra movimientos que no obtuvieron CUIT en tabla intermedia
			CREATE TABLE #AST_FEWS_BORRAR ( AS_ID INT)
			DELETE FROM #AST_FEWS_BORRAR

			INSERT INTO #AST_FEWS_BORRAR
			select as_id from AST_FEWS_LOG where isnull(RESULTADO,'')<>'A'


select * from #AST_FEWS_BORRAR

			DELETE FROM AST_FEWS_LOG
			WHERE
			(SELECT COUNT(*) FROM #AST_FEWS_BORRAR where #AST_FEWS_BORRAR.AS_ID=AST_FEWS_LOG.AS_ID)<>0

			DELETE FROM AST_FEWS_LOG_iva
			WHERE
			(SELECT COUNT(*) FROM #AST_FEWS_BORRAR where #AST_FEWS_BORRAR.AS_ID=AST_FEWS_LOG_iva.AS_ID)<>0 

			DELETE FROM AST_FEWS_LOG_TRIBUTOS
			WHERE
			(SELECT COUNT(*) FROM #AST_FEWS_BORRAR where #AST_FEWS_BORRAR.AS_ID=AST_FEWS_LOG_TRIBUTOS.AS_ID)<>0 




DECLARE  @RESULTADO VARCHAR(255)
DECLARE @AS_ID INT
DECLARE @AS_cAI VARCHAR(20)
DECLARE @TIEMPO DATETIME


DECLARE PEDIR_CAE CURSOR FOR  
	select as_id, as_Cai 
	from Asiento where  doc_ID>=278 and doc_ID<=292 and
	(isnull(AS_CAI,'')='' or ISNULL(AS_CAI,'')='11111111111111' or ISNULL(AS_CAI,'')='0') 
	
OPEN PEDIR_CAE 

FETCH NEXT FROM PEDIR_CAE  
INTO @AS_id, @AS_CAI  

WHILE @@FETCH_STATUS =0  
BEGIN  

--	SELECT @AS_ID, @AS_CAI
	exec AST_FEWS_PIDECAE  @AS_ID, 1
	
	SET @TIEMPO=GETDATE()
	WHILE DATEDIFF ( second , @tiempo , GETDATE()) <= 5 --- segundos max. de espera
			BEGIN
			   IF DBO.AST_FEWS_GET_RESULTADO_CAE(@AS_ID) <>'0' 
					
				  BREAK
			   ELSE
				  
				  CONTINUE
			end
	
	SET @RESULTADO=''
	set @resultado=
	(SELECT  CASE WHEN ISNULL(CAE,'')<>'' and ISNULL(RESULTADO,'0')='A' 
				then 'CAE:'+ISNULL(CAE,'')+'   '--+ISNULL(OBSERVACION,' ')
				ELSE (CASE isnull(RESULTADO,'') 
						WHEN 'A' THEN 'APROBADA'
						WHEN 'R' THEN ISNULL(ERR_MSG,'Rechazada Sin determinar motivo')
						WHEN '0' THEN ISNULL(ERR_MSG,'Demora demasiado/Sin Conexión - Intente de nuevo más tarde')
						ELSE ISNULL(ERR_MSG,'- OTROS')
					end
			)
			end
	--select *
	 FROM ast_fews_log where as_id=@AS_ID)
	 
	 UPDATE ASIENTO SET
		AS_CAI=(SELECT DBO.AST_FEWS_GET_CAE (@as_id)),
		AS_CAIFechaVto=(SELECT DBO.AST_FEWS_GET_FECHACAE(@as_id)),
		AST_FEWS_ESTADO= (SELECT DBO.AST_FEWS_GET_RESULTADO_CAE (@as_id))--,
		--AST_FEWS_RESULTADO=(SELECT DBO.AST_FEWS_GET_RESULTADO_CAE (@as_id)),
								
		--as_caecodigobarras=''
	FROM Asiento
	WHERE 
			asiento.AS_ID=@as_id
	 
	
    FETCH NEXT FROM PEDIR_CAE 
    INTO @AS_ID, @AS_CAI 
END

CLOSE PEDIR_cAE 
DEALLOCATE PEDIR_cAE


END

GO


