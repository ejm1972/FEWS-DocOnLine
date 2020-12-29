USE [FINN_MACOR2014]
GO

/****** Object:  StoredProcedure [dbo].[AST_P_FAF_TR_0010]    Script Date: 06/14/2019 15:07:56 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[AST_P_FAF_TR_0010]') AND type in (N'P', N'PC'))
DROP PROCEDURE [dbo].[AST_P_FAF_TR_0010]
GO

USE [FINN_MACOR2014]
GO

/****** Object:  StoredProcedure [dbo].[AST_P_FAF_TR_0010]    Script Date: 06/14/2019 15:07:56 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER OFF
GO


/*JES 10/01/2003*/
--AST_P_FAF_TR_0010  '20190103 00:00:00.000', '20190103 00:00:00.000', '0', '0', '0', 0, '4', '0'
CREATE PROCEDURE [dbo].[AST_P_FAF_TR_0010]
( 
	@@FechaDesde 	datetime,
	@@FechaHasta 	datetime,
	@@EST_ID        Varchar(255),
	@@TE_ID		VarChar(255),
	@@CC_ID         VarChar(255),
	@@DOC_ID        int,
	@@AST_ID        int,
	@@US_ID		int
)    
AS
BEGIN
	SET NOCOUNT ON
  
	/*todo esto es para resolver el tema de arboles*/
	DECLARE @ReportCode DateTime
	DECLARE @CC_ID int
	DECLARE @NOD_ID_CircuitoContable int
        DECLARE @NOD_ID_Tercero int 
	DECLARE @TE_ID int
	DECLARE @PRESTACION_VER smallint

	Declare @EST_ID int,
			@NOD_ID_Estado int

	SELECT @PRESTACION_VER = 1

DECLARE @SIGNO_CREDITO int
DECLARE @SIGNO_DEBITO int 

SELECT @SIGNO_CREDITO = -1
SELECT @SIGNO_DEBITO = 0

	Declare @AST_COB Int
	Set 	@AST_COB = 4
	
	SELECT @ReportCode = GetDate()

   	Exec SpArbNodoHojaConvertID @@CC_ID,@CC_ID OUTPUT,@NOD_ID_CircuitoContable OUTPUT
   	Exec SpArbNodoHojaConvertID @@TE_ID,@TE_ID OUTPUT,@NOD_ID_Tercero OUTPUT
   	Exec SpArbNodoHojaConvertID @@EST_ID,@EST_ID OUTPUT,@NOD_ID_Estado OUTPUT

	/*Filtro*/
	IF IsNull(@NOD_ID_CircuitoContable,0) <> 0 Exec SpArbNodoGetSeleccion @ReportCode,@NOD_ID_CircuitoContable
	IF IsNull(@NOD_ID_Tercero,0) <> 0 Exec SpArbNodoGetSeleccion @ReportCode,@NOD_ID_Tercero
	IF IsNull(@NOD_ID_Estado,0) <> 0 Exec SpArbNodoGetSeleccion @ReportCode,@NOD_ID_Estado

If @EST_ID = 0
	Select @EST_ID = Null

Declare @AS_ID Int
Declare @FIRST Int
Declare @PEV_ID Int	
Declare @DOCUMENTO Varchar(500)
Create Table #TRANS(
	AS_ID		Int Null, 
	AS_Fecha	DateTime Null, 
	DOC_Nombre	Varchar(50) Null, 
	Numero		int Null, 
	Comprobante	Varchar(15) Null, 
	Cuenta		Varchar(50) Null, 
	ImporteItem Money Null, 
	Bruto		Money Null, 
	ImporteIva	Money Null, 
	Otros		Money Null, 
	Total		Money Null, 
	EST_Nombre	Varchar(50) Null, 
	Cliente		Varchar(150) Null, 
	Sucursal	Varchar(50) Null, 
	Observaciones	Varchar(255) Null, 
	Origen		Varchar(50) Null, 
	Destino		Varchar(50) Null, 
	DOC_Pedido	Varchar(500) Null)


	Insert Into #TRANS	
	SELECT  
		Asiento.AS_ID,
		Fecha = AS_Fecha,
    		Documento = Doc_nombre,
    		Numero= convert(int, AS_Numero),
 		Comprobante =
			CASE 
				WHEN Asiento.AST_id not in(2,12) then
					Substring(AS_NumeroDoc, 1, 1) + '-' + Substring(AS_NumeroDoc, 2, 4) + '-' +Substring(AS_NumeroDoc, 6, 8)
				ELSE
					Substring(AS_DocTercero, 1, 1) + '-' + Substring(AS_DocTercero, 2, 4) + '-' +Substring(AS_DocTercero, 6, 8)
			END,

			Cuenta = cuenta.CUE_nombre,
			ImporteItem = AsientoItem.IA_Importe,

    		Bruto = CASE DOC_Signo WHEN @SIGNO_CREDITO THEN IsNULL(Asiento.AS_Bruto,0) ELSE IsNULL(Asiento.AS_Bruto,0) * -1 END,
     		ImporteIva = CASE DOC_Signo WHEN @SIGNO_CREDITO THEN IsNULL(Asiento.AS_ImporteIva,0) ELSE IsNULL(Asiento.AS_ImporteIva,0)* -1 END,
     		Otros = CASE DOC_Signo WHEN @SIGNO_CREDITO THEN IsNULL(ASiento.As_Nogravado, 0) ELSE IsNULL(ASiento.As_Nogravado, 0) *-1 END,
     		Total = CASE DOC_Signo WHEN @SIGNO_CREDITO THEN IsNull(Asiento.AS_Total,0) ELSE IsNull(Asiento.AS_Total,0) *-1 END,
     		Estado = Estado.EST_Nombre,
     		Cliente = Tercero.TE_Nombre,
      		Sucursal = CS_Nombre,
     		Observaciones = Asiento.AS_Descrip,
      		Origen = Origen.LU_Nombre,
      		Destino = Destino.LU_Nombre, 
		'Doc. Pedido' = ''

    	FROM
     		Asiento

		Inner Join AsientoItem On(Asiento.AS_ID = AsientoItem.AS_ID)
		Inner Join Cuenta On(AsientoItem.CUE_id = cuenta.CUE_id)

      		Left Join Tercero On(Asiento.TE_ID = Tercero.TE_ID)
      		Inner Join AsientoTipo On(Asiento.AST_ID = AsientoTipo.AST_ID)
       		Inner Join DocumentoTipo On(Asiento.DOC_ID = DocumentoTipo.DOC_ID And AsientoTipo.AST_ID = DocumentoTipo.AST_ID)
      		Left Join Estado On(Asiento.EST_ID = Estado.EST_ID)
      		Left Join DocumentoTipoCircuitoContable On(DocumentoTipo.DOC_ID = DocumentoTipoCircuitoContable.DOC_ID)
          LEFT JOIN MovimientoStock ON (Asiento.AS_ID = MovimientoStock.MS_ID)
          LEFT JOIN Lugar Origen ON (MovimientoStock.LU_ID_Origen = Origen.LU_ID ) 
          LEFT JOIN Lugar Destino ON (MovimientoStock.LU_ID_Destino = Destino.LU_ID ) 
          Left Join TerceroSucursal ON (Asiento.CS_ID = TerceroSucursal.CS_ID)  

    	WHERE 	(IsNull(@EST_ID, Estado.EST_ID) = Estado.EST_ID OR Asiento.EST_ID Is Null) And
        		(Tercero.TE_ID = @TE_ID OR @TE_ID=0) AND
        		(DocumentoTipo.DOC_id = @@DOC_ID OR @@DOC_ID=0) AND
        		(AsientoTipo.AST_id = @@AST_ID OR @@AST_ID=0) AND
        		AS_Fecha >= @@FechaDesde AND  
        		AS_Fecha <=@@FechaHasta AND     
        		(DocumentoTipoCircuitoContable.CC_ID = @CC_ID OR @CC_ID = 0) AND
		 	(Exists (SELECT DTC.DOC_ID FROM DocumentoTipoPrestacionAcceso as DTC, UsuarioNivel as NI 
				WHERE NI.NI_ID = DTC.NI_ID AND DTC.DOC_ID = DocumentoTipo.DOC_ID
			 	AND DOCPRES_Prestacion = @PRESTACION_VER
			 	AND NI.US_ID = @@US_ID) OR @@US_ID = 0)

        		/*Filtros*/	
        		AND (DocumentoTipoCircuitoContable.CC_ID  IN (SELECT ID FROM RE_ArbolNodoHojaSeleccion WHERE ReportCode = @ReportCode AND Tabla = 'CircuitoContable' ) OR @NOD_ID_CircuitoContable = 0)
        		AND (Tercero.TE_ID  IN (SELECT ID FROM RE_ArbolNodoHojaSeleccion WHERE ReportCode = @ReportCode AND Tabla = 'Tercero' ) OR @NOD_ID_Tercero = 0)
			AND (@NOD_ID_Estado = 0 Or Asiento.EST_ID IN (SELECT ID FROM RE_ArbolNodoHojaSeleccion WHERE ReportCode = @ReportCode AND Tabla = 'Estado' ))
			
				AND (AsientoItem.AIT_ID <> @AST_COB)   

	Declare CU_TRANS Insensitive Cursor For
	Select AS_ID From #TRANS
	For Read Only
	Open CU_TRANS
	Fetch Next From CU_TRANS Into @AS_ID
	While (@@fetch_status <> -1)
	Begin
		If (@@fetch_status <> -2)
       		Begin
			Select @DOCUMENTO = ''			
			Select @FIRST = -1
			Declare CU_RI Insensitive Cursor For
			Select Distinct PEV_ID
			From RemitoItem
			Inner Join PedidoVentaItem On(RemitoItem.PEVI_ID = PedidoVentaItem.PEVI_ID)
			Where RemitoItem.MS_ID = @AS_ID
			For Read Only
			Open CU_RI
			Fetch Next From CU_RI Into @PEV_ID
			While (@@fetch_status <> -1)
			Begin
				If (@@fetch_status <> -2)
				Begin
					If @FIRST = 0 Select @DOCUMENTO = @DOCUMENTO + '/'
					Select @FIRST = 0
					Select @DOCUMENTO = @DOCUMENTO + (
										Select DocumentoTipo.DOC_Nombre + ' ' + Asiento.AS_Numero
										From Asiento Inner Join DocumentoTipo On(Asiento.DOC_ID = DocumentoTipo.DOC_ID)
										Where Asiento.AS_ID = @PEV_ID)
				End
				Fetch Next From CU_RI Into @PEV_ID
			End
			Close CU_RI
			Deallocate CU_RI

			Update #TRANS Set DOC_Pedido = @DOCUMENTO Where AS_ID = @AS_ID
		End
		Fetch Next From CU_TRANS Into @AS_ID
	End
	Close CU_TRANS
	Deallocate CU_TRANS

	Select * From #TRANS Order By AS_ID

	DELETE FROM RE_ArbolNodoHojaSeleccion WHERE ReportCode = @ReportCode    
     
END

GO


