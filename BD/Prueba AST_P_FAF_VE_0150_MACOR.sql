USE [FINN_MACOR2014]
GO

declare @@FDesde		DateTime
declare @@FHasta		DateTime
declare @@TE_ID 		VarChar(255)
declare @@VEN_ID		VarChar(255)
declare @@COB_ID		VarChar(255)
declare @@CC_ID			VarChar(255)	
declare @@TasaFinanciera    Money

Declare @@AS_ID Int

select @@FDesde = '20190103'
select @@FHasta = '20190103'
select @@TE_ID = ''
select @@VEN_ID = ''
select @@COB_ID = ''
select @@CC_ID = ''	
select @@TasaFinanciera = 15

select @@AS_ID = 1493656

	Declare @ReportCode DateTime
	Declare @TE_ID Int
	Declare @NOD_ID_Tercero Int
	Declare @CC_ID Int
	Declare @NOD_ID_CircuitoContable Int
	Declare @VEN_ID Int
	Declare @NOD_ID_Vendedor Int
	Declare @COB_ID Int
	Declare @NOD_ID_Cobrador Int
	
	Set	@ReportCode = GetDate()
	
	Exec SpArbNodoHojaConvertID @@TE_ID, @TE_ID OutPut, @NOD_ID_Tercero OutPut
	Exec SpArbNodoHojaConvertID @@VEN_ID, @VEN_ID OutPut, @NOD_ID_Vendedor OutPut
	Exec SpArbNodoHojaConvertID @@COB_ID, @COB_ID OutPut, @NOD_ID_Cobrador OutPut
	Exec SpArbNodoHojaConvertID @@CC_ID, @CC_ID OutPut, @NOD_ID_CircuitoContable OutPut

	If IsNull(@NOD_ID_Tercero, 0) <> 0 Exec SpArbNodoGetSeleccion @ReportCode, @NOD_ID_Tercero
	If IsNull(@NOD_ID_Cobrador, 0) <> 0 Exec SpArbNodoGetSeleccion @ReportCode, @NOD_ID_Cobrador
	If IsNull(@NOD_ID_Vendedor, 0) <> 0 Exec SpArbNodoGetSeleccion @ReportCode, @NOD_ID_Vendedor
	If IsNull(@NOD_ID_CircuitoContable, 0) <> 0 Exec SpArbNodoGetSeleccion @ReportCode, @NOD_ID_CircuitoContable
	
	If IsNull(@NOD_ID_Tercero, 0) <> 0 Exec SpArbNodoGetGrupo @ReportCode, @NOD_ID_Tercero
	If IsNull(@NOD_ID_Vendedor, 0) <> 0 Exec SpArbNodoGetGrupo @ReportCode, @NOD_ID_Vendedor

	Declare @AIT_BAN Int
	Set 	@AIT_BAN = 11
	Declare @AIT_TAR Int
	Set 	@AIT_TAR = 12
	Declare @AIT_EFE Int
	Set 	@AIT_EFE = 13
	Declare @AST_COB Int
	Set 	@AST_COB = 4
	
	SELECT
		/*
		Cobranza.AS_ID,
		Recibo = Cobranza.AS_NumeroDoc,
		Factura = Factura.AS_NumeroDoc,
		FechaCobroReal = Case AIT_ID When @AIT_BAN Then DF_Fecha Else DEB_Fecha End,
		FechaVtoOriginal = CRD_Fecha,
		FechaFactura = Factura.AS_Fecha,
		Tercero.TE_ID,
		*/
		Cliente = TE_Nombre,
		/*
		Vendedor.VEN_ID,
		Vendedor = VEN_Nombre,
		*/
		CobranzaItem.AIT_ID,
		'Total Cobrado' = sum (Round(IA_Importe * CAN_Importe / Cobranza.AS_Total, 2)),
		'Días sum' = sum(DATEDIFF(day, CRD_Fecha,  Case AIT_ID When @AIT_BAN Then DF_Fecha Else DEB_Fecha End)) ,
        'Días'= ROUND(
				CAST(
					sum(Round(IA_Importe * CAN_Importe / Cobranza.AS_Total, 2) * 
					DATEDIFF(day, Factura.AS_Fecha,  Case AIT_ID When @AIT_BAN Then DF_Fecha Else DEB_Fecha End)) 
					/ sum(Round(IA_Importe * CAN_Importe / Cobranza.AS_Total, 2)) 
				AS REAL)
				,1)
	/*	'Costo Financiero' = Case AIT_ID When @AIT_BAN Then IA_Importe * DateDiff(d, CRD_Fecha, DF_Fecha) / 100 / 365 * @@TasaFinanciera Else IA_Importe * DateDiff(d, CRD_Fecha, DEB_Fecha) / 100 / 365 * @@TasaFinanciera End, 
		'Grupo de clientes' = GrupoTercero.Nodo,
		'Grupo de vendedores' = GrupoVendedor.Nodo*/
 --'DEBITO', debito.*, 'CREDITO', credito.*, 'COBRANZA', cobranza.*, 'FACTURA', factura.*, 'ITMCOB', CobranzaItem.*
	From	Asiento Cobranza
		Inner Join DocumentoTipo On(Cobranza.DOC_ID = DocumentoTipo.DOC_ID) 
		Inner Join DocumentoTipoCircuitoContable On(DocumentoTipo.DOC_ID = DocumentoTipoCircuitoContable.DOC_ID)
		Inner Join Tercero On(Cobranza.TE_ID = Tercero.TE_ID)
		Left Join Cobrador On(Cobranza.COB_ID = Cobrador.COB_ID)
		Inner Join AsientoItem CobranzaItem On(Cobranza.AS_ID = CobranzaItem.AS_ID)
		Left Join DocumentoFisico On(CobranzaItem.DF_ID = DocumentoFisico.DF_ID)
		
		Inner Join Debito On(Cobranza.AS_ID = Debito.AS_ID)
		Inner Join CreditoDebitoCancelacion On(Debito.DEB_ID = CreditoDebitoCancelacion.DEB_ID)
		Inner Join Credito On(CreditoDebitoCancelacion.CRD_ID = Credito.CRD_ID)
		Inner Join Asiento Factura On(Credito.AS_ID = Factura.AS_ID)
		Left Join Vendedor On(Factura.VEN_ID = Vendedor.VEN_ID)
		
		Left Join RE_ArbolNodoHojaGrupo GrupoTercero On(Cobranza.TE_ID = GrupoTercero.ID And GrupoTercero.Tabla = 'Tercero' And GrupoTercero.ReportCode = @ReportCode)
		Left Join RE_ArbolNodoHojaGrupo GrupoVendedor On(Factura.VEN_ID = GrupoVendedor.ID And GrupoVendedor.Tabla = 'Vendedor' And GrupoVendedor.ReportCode = @ReportCode)

	WHERE 	    (Cobranza.AS_Fecha <= @@FHasta)
		AND (Cobranza.AS_Fecha >= @@FDesde)
		AND (Cobranza.AST_ID = @AST_COB)
		AND (Cobranza.TE_ID = @TE_ID Or @TE_ID = 0)
		AND (Factura.VEN_ID = @VEN_ID Or @VEN_ID = 0)
		AND (IsNull(Cobranza.AS_Activo,0) <> 0)
		AND (DocumentoTipoCircuitoContable.CC_ID = @CC_ID OR @CC_ID = 0)
--AND (CobranzaItem.AIT_ID = @AIT_BAN Or CobranzaItem.AIT_ID = @AIT_TAR Or CobranzaItem.AIT_ID = @AIT_EFE)                 
AND (CobranzaItem.AIT_ID <> @AST_COB)                 
	
		AND (Cobranza.TE_ID  IN (SELECT ID FROM RE_ArbolNodoHojaSeleccion WHERE ReportCode = @ReportCode AND Tabla = 'Tercero' ) OR @NOD_ID_TERCERO = 0)
		AND (Factura.VEN_ID IN (SELECT ID FROM RE_ArbolNodoHojaSeleccion WHERE ReportCode = @ReportCode AND Tabla = 'Vendedor') OR @NOD_ID_Vendedor = 0)
		AND (DocumentoTipoCircuitoContable.CC_ID IN (SELECT ID FROM RE_ArbolNodoHojaSeleccion WHERE ReportCode = @ReportCode AND Tabla = 'CircuitoContable')  OR @NOD_ID_CircuitoContable = 0)

AND Cobranza.AS_ID = @@AS_ID 
	
	--ORDER BY TE_Nombre,Cobranza.AS_NumeroDoc
             group by  TERCERO.TE_ID, TERCERO.TE_NOMBRE, CobranzaItem.AIT_ID

	Delete From RE_ArbolNodoHojaSeleccion Where ReportCode = @ReportCode
	Delete From RE_ArbolNodoHojaGrupo Where ReportCode = @ReportCode

GO


