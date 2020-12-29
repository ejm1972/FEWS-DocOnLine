USE [FINN_MACOR2014] 
GO

/****** Object:  StoredProcedure [dbo].[AST_F_MAC_DESP_0011_V11]    Script Date: 06/04/2019 21:20:08 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[AST_F_MAC_DESP_0011_V11]') AND type in (N'P', N'PC'))
DROP PROCEDURE [dbo].[AST_F_MAC_DESP_0011_V11]
GO

USE [FINN_MACOR2014]  
GO

/****** Object:  StoredProcedure [dbo].[AST_F_MAC_DESP_0011_V11]    Script Date: 06/04/2019 21:20:08 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER OFF
GO


--AST_F_MAC_DESP_0011_V11 1535824

CREATE PROCEDURE [dbo].[AST_F_MAC_DESP_0011_V11](   
	@@AS_ID  Int  
)  
AS  
BEGIN  
  
/*******************************************************************************************/  
/*										           */  
/* Creado Por     :  Luciano Najimovich 02/04/1998			                   */  
/*               									   */  
/* Descripcion   :  SP del Reporte De Movimientos Fisicos de Clientes                      */  
/*               									   */  
/********************************************************************************************/  
  
DECLARE @DOC_ID         Int  
DECLARE @Signo          Int  
DECLARE @Signo_Credito  Int  
  
Declare @Numero_Pedido Varchar (255)  
Declare @Numeros_Pedidos_Acum Varchar (255)  
Declare @Bruto_Pedido  Numeric (10,2)  
Declare @Bruto_Pedidos_Acum Numeric (10,2)  
  
     
  
/*Modificado GAT 10/12/04 Verificacion de la relacion de stock*/  
  
  
/*Obtengo la forma de usar la relacion de stock*/  
declare @InvierteRelacionStock varchar(50)  
EXEC SPGetConfiguracion 'InvRelacionStk2' , @InvierteRelacionStock OUTPUT  
select @InvierteRelacionStock= isnull(@InvierteRelacionStock,'False')  
  
-----Declaro un cursor para levantar y concatener en una variable, todos los numeros de los pedidos que está aplicando el remito  
Set @Numeros_Pedidos_Acum  = ''  
Set @Bruto_Pedidos_Acum  = 0   
declare  Cu_Pedido cursor for  
	SELECT DISTINCT Asiento.AS_DocTercero,Asiento.AS_Bruto  
    FROM Asiento,Debito,Credito,CreditoDebitoCancelacion,RemitoItem  
    WHERE   Debito.AS_ID  = @@AS_ID  
		AND  Credito.CRD_ID  = CreditoDebitoCancelacion.CRD_ID  
		AND  CreditoDebitoCancelacion.DEB_ID = Debito.DEB_ID  
		AND  Asiento.AS_ID  = Credito.As_Id  
open Cu_Pedido  
fetch Next From Cu_Pedido Into @Numero_Pedido,@Bruto_Pedido  
  
While (@@Fetch_Status <> -1)    
Begin    
	If (@@Fetch_Status <> -2)    
	Begin   
	  Set @Numeros_Pedidos_Acum = @Numeros_Pedidos_Acum  + isnull (@Numero_Pedido,'')   
	  Set @Bruto_Pedidos_Acum = @Bruto_Pedidos_Acum + isnull (@Bruto_Pedido,0)  
	End  
	fetch Next From Cu_Pedido Into @Numero_Pedido,@Bruto_Pedido  
	if (@@Fetch_Status <> -1) Set @Numeros_Pedidos_Acum = @Numeros_Pedidos_Acum  + ', '  
end  
close Cu_pedido  
deallocate Cu_Pedido  
----Fin cursor, sigo con el reporte normalmente...  

if @InvierteRelacionStock = 'Falso' or @InvierteRelacionStock='False' or @InvierteRelacionStock=0  
    select @InvierteRelacionStock='0'  
else  
   select  @InvierteRelacionStock='-1'  
  
SELECT @Signo_Credito = -1     

SELECT @DOC_ID = DOC_ID  
FROM Asiento  
WHERE AS_ID = @@AS_ID   

SELECT @Signo = DOC_Signo  
FROM DocumentoTipo  
WHERE DOC_ID = @DOC_ID  
    
Declare @CPG_Nombre Varchar (255)  
Set @CPG_Nombre  = ''  
SELECT TOP 1 @CPG_Nombre = CondicionPago.CPG_Nombre
FROM Asiento left outer join CondicionPago on Asiento.CPG_ID = CondicionPago.CPG_ID, Debito, Credito, CreditoDebitoCancelacion
    WHERE   Debito.AS_ID = @@AS_ID
		AND  Credito.CRD_ID = CreditoDebitoCancelacion.CRD_ID
		AND  CreditoDebitoCancelacion.DEB_ID = Debito.DEB_ID
		AND  Asiento.AS_ID = Credito.AS_ID
ORDER BY Asiento.AS_Fecha, Asiento.AS_ID
	
IF @Signo = @Signo_Credito        
BEGIN      
    SELECT  

	CPG_Nombre = isnull(@CPG_Nombre,isnull(CondicionPago.CPG_Nombre, CondicionPagoTercero.CPG_Nombre)) ,

     Signo = IsNull(@Signo,0),ASiento.AS_ID, Asiento.AS_Descrip, Asiento.AS_FEcha,Asiento.AS_Numero, Asiento.AS_NumeroDoc, Asiento.AS_DocTercero,  
     RemitoItem.RI_Orden,RemitoItem.RI_Descrip,RemitoItem.RI_CantVenta,RemitoItem.RI_CantProduccion,  
     'TE_Nombre' = Tercero.TE_RazonSocial,  
     Tercero.TE_Ident,  
     Domicilio = Tercero.TE_Calle + ' ' + Tercero.TE_CalleNumero + ' '  + Tercero.TE_Piso + ' ' + TErcero.TE_Departamento,  
     Tercero.TE_Localidad,   
     Tercero.TE_Cuit,  
     Estado.EST_Nombre,  
     ORIGEN=Lugar.LU_nombre,  
     ProductoVenta.PV_Nombre,  
     Unidad.UN_Nombre,  
     UnidadStock=Unidad2.UN_Nombre,  
     DESTINO=Tercero.TE_Nombre,  
     ENTREGAEN=MovimientoStock.MS_lugarEntrega,  
     TransportistaNombre = Transportista.TE_Nombre,  
     TransportistaDomicilio = Transportista.TE_Calle + ' ' + transportista.TE_CalleNumero + ' '  + transportista.TE_Piso + ' ' + Transportista.TE_Departamento,  
     CantidadEnvase = RI_CantidadEnvase,  
     RelacionEnvaseVenta = RI_RelacionEnvaseVenta1 ,  
     RI_NumeroDespacho = RI_NumeroDespacho,  
     MS_Patente,   
     PR_RelacionStock2Stock1=(case when @InvierteRelacionStock = '-1' then   
  			      	case when ISNULL(PR_RelacionStock2Stock1, 0) <> 0 then  
    			      		1 / PR_RelacionStock2Stock1  
       				else 0  
                		end   
              		      else  
			 	PR_RelacionStock2Stock1  
         		      end),  
     TransportistaCuit = Transportista.TE_Cuit,  
     TransportistaTeIden= Transportista.TE_Ident,  
     Tercero.TE_Alias,  
     Tercero.TE_CodPostal,    
     ProductoVenta.PV_Alias,    
     TransportistaCuit = Transportista.TE_Cuit,  
     Asiento.AST_ID,  
     Provincia = (SELECT PROV_Nombre FROM Provincia WHERE PROV_ID = Tercero.PROV_ID),  
--Traigo la OC del Pedido  
     NroOc_Pedido = @Numeros_Pedidos_Acum,    
--Traigo el Bruto del Pedido  
     Bruto_Pedido = @Bruto_Pedidos_Acum,    
     Unidad.UN_Alias,  
     Asiento.Cant_Bult,  
     Asiento.Peso_Aprox,  
     UN_Alias2 = Unidad2.UN_Alias,  
     'Lote Numero' = RTRIM(Lot_numero),
     'Lote Fecha' = Lot_fechaalta,
     'Lote FechaVenc' = Lot_fechaVencimiento,
     'Lote Descripcion' = Lot_Descrip,
     'Lote FechaAux' = Lot_fechaAuxiliar,
--Traigo el Bruto del Pedido  
     TipoPrecio = (SELECT DISTINCT PEVI_TipoPrecio  
			    FROM Asiento,Debito,Credito,CreditoDebitoCancelacion,PedidoVentaItem  ,Lotepartida
			    WHERE debito.AS_ID = @@AS_ID  
				    AND Credito.CRD_ID = CreditoDebitoCancelacion.CRD_ID  
				    AND CreditoDebitoCancelacion.DEB_ID = Debito.DEB_ID  
				    AND Asiento.AS_ID = credito.As_Id  
				    AND PedidoVentaItem.PEV_ID = Asiento.AS_ID AND PedidoVentaItem.PV_ID = ProductoVenta.PV_ID) 
    FROM  
		Asiento
			left outer join Tercero 
								left outer join CondicionPago CondicionPagoTercero on Tercero.CPG_ID=CondicionPagoTercero.CPG_id 
				on Asiento.TE_id=Tercero.TE_id
			left outer join CondicionPago on Asiento.CPG_ID=CondicionPago.CPG_ID
		, MovimientoStock 
			left outer join Tercero Transportista on MovimientoStock.TE_ID_Transporte=Transportista.TE_ID
		, RemitoItem
			left outer join LotePartida on RemitoItem.RI_Discriminador1=LotePartida.Lot_id
		, Producto
			left outer join Unidad on Producto.UN_ID_Stock=Unidad.UN_ID
			left outer join Unidad Unidad2 on Producto.UN_ID_Stock2=Unidad2.UN_ID
		, Estado, Lugar, ProductoVenta, ProductoVentaProducto

    WHERE  
		Asiento.AS_ID=@@AS_ID AND   
		Asiento.EST_ID=Estado.EST_ID AND  
		Asiento.AS_ID= MovimientoStock.AS_ID AND  
		MovimientoStock.MS_ID=RemitoItem.MS_ID AND  
		RemitoItem.PV_ID=ProductoVenta.PV_ID AND  
		MovimientoStock.LU_ID_ORIGEN=Lugar.LU_ID AND  
		ProductoVenta.PV_ID=ProductoVentaProducto.PV_ID AND  
		ProductoVentaProducto.PR_ID=Producto.PR_ID
    ORDER BY RemitoItem.RI_Orden  
END  

ELSE

BEGIN   
    SELECT  

	CPG_Nombre = isnull(CondicionPago.CPG_Nombre, CondicionPagoTercero.CPG_Nombre) ,

    Signo = IsNull(@Signo,0), ASiento.AS_ID, Asiento.AS_Descrip, Asiento.AS_FEcha,Asiento.AS_Numero, Asiento.AS_NumeroDoc, Asiento.AS_DocTercero,  
     RemitoItem.RI_Orden,RemitoItem.RI_Descrip,RemitoItem.RI_CantVenta,RemitoItem.RI_CantProduccion,  
     Estado.EST_Nombre,  
     Tercero.TE_Nombre,  
     Tercero.TE_Ident,  
     Domicilio = Tercero.TE_Calle + ' ' + Tercero.TE_CalleNumero + ' '  + Tercero.TE_Piso + ' ' + Tercero.TE_Departamento ,  
     Tercero.TE_Localidad,  
     Tercero.TE_Cuit,  
     ORIGEN=Tercero.TE_Nombre,  
     ProductoVenta.PV_Nombre,   
     Unidad.UN_Nombre,  
     UnidadStock=Unidad2.UN_Nombre,  
     DESTINO=Lugar.LU_Nombre,   
     ENTREGAEN=MovimientoStock.MS_lugarEntrega,  
     TransportistaNombre = Transportista.TE_Nombre,  
     TransportistaDomicilio = Transportista.TE_Calle + ' ' + transportista.TE_CalleNumero + ' '  + transportista.TE_Piso + ' ' + Transportista.TE_Departamento,  
     CantidadEnvase = RI_CantidadEnvase,  
     RelacionEnvaseVenta = RI_RelacionEnvaseVenta1 ,  
     RI_NumeroDespacho = RI_NumeroDespacho,  
     MS_Patente,   
     PR_RelacionStock2Stock1=(case when @InvierteRelacionStock = '-1' then   
  			      	case when ISNULL(PR_RelacionStock2Stock1, 0) <> 0 then  
    					1 / PR_RelacionStock2Stock1  
       					else 0  
                		end   
              		     else  
                     		PR_RelacionStock2Stock1  
         		     end),  
     TransportistaCuit = Transportista.TE_Cuit,  
     TransportistaTeIden= Transportista.TE_Ident,  
     Tercero.TE_Alias,  
     Tercero.TE_CodPostal,  
     ProductoVenta.PV_Alias,    
     TransportistaCuit = Transportista.TE_Cuit,  
     Asiento.AST_ID,  
     Provincia = (SELECT PROV_Nombre FROM Provincia WHERE PROV_ID = Tercero.PROV_ID),  
 --Traigo la OC del Pedido  
     NroOc_Pedido = @Numeros_Pedidos_Acum,    
 --Traigo el Bruto del Pedido  
     Bruto_Pedido = @Bruto_Pedidos_Acum,    
     Unidad.UN_Alias,  
     Asiento.Cant_Bult,  
     Asiento.Peso_Aprox,  
     'Lote Numero' = RTRIM(Lot_numero),
     'Lote Fecha' = Lot_fechaalta,
     'Lote FechaVenc' = Lot_fechaVencimiento,
     'Lote Descripcion' = Lot_Descrip,
     'Lote FechaAux' = Lot_fechaAuxiliar,
     UN_Alias2 = Unidad2.UN_Alias,  
     TipoPrecio = (SELECT DISTINCT PEVI_TipoPrecio  
	    		FROM Asiento,Debito,Credito,CreditoDebitoCancelacion,PedidoVentaItem  
	    		WHERE Debito.AS_ID = @@AS_ID  
	    		AND Credito.CRD_ID = CreditoDebitoCancelacion.CRD_ID  
	    		AND CreditoDebitoCancelacion.DEB_ID = Debito.DEB_ID  
	  		AND Asiento.AS_ID = Credito.As_Id  
	  		AND PedidoVentaItem.PEV_ID = Asiento.AS_ID AND PedidoVentaItem.PV_ID = ProductoVenta.PV_ID),
    'cap'=2
    
    FROM  
		Asiento
			left outer join Tercero 
								left outer join CondicionPago CondicionPagoTercero on Tercero.CPG_ID=CondicionPagoTercero.CPG_id 
				on Asiento.TE_id=Tercero.TE_id
			left outer join CondicionPago on Asiento.CPG_ID=CondicionPago.CPG_ID
		, MovimientoStock 
			left outer join Tercero Transportista on MovimientoStock.TE_ID_Transporte=Transportista.TE_ID
		, RemitoItem
			left outer join LotePartida on RemitoItem.RI_Discriminador1=LotePartida.Lot_id
		, Producto
			left outer join Unidad on Producto.UN_ID_Stock=Unidad.UN_ID
			left outer join Unidad Unidad2 on Producto.UN_ID_Stock2=Unidad2.UN_ID
		, Estado, Lugar, ProductoVenta, ProductoVentaProducto

    WHERE  
		Asiento.AS_ID=@@AS_ID AND   
		Asiento.EST_ID=Estado.EST_ID AND  
		Asiento.AS_ID= MovimientoStock.AS_ID AND  
		MovimientoStock.MS_ID=RemitoItem.MS_ID AND  
		RemitoItem.PV_ID=ProductoVenta.PV_ID AND  
		MovimientoStock.LU_ID_ORIGEN=Lugar.LU_ID AND  
		ProductoVenta.PV_ID=ProductoVentaProducto.PV_ID AND  
		ProductoVentaProducto.PR_ID=Producto.PR_ID
	ORDER BY RemitoItem.RI_Orden  
END  
  
END  

GO


