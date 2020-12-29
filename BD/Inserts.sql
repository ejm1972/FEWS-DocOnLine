/*
delete from FEWS_DETALLE
delete from FEWS_IVA
delete from FEWS_TRIBUTO
delete from FEWS_XML
delete from FEWS_ENCABEZADO
*/
declare @@ID_MOV int

begin tran
insert into FEWS_ENCABEZADO (
[tipo_reg]
,[webservice]
,[movimiento]
,[fecha_cbte]
,[tipo_cbte]
,[punto_vta]
,[cbte_nro]
,[nombre_cliente]
,[tipo_doc]
,[nro_doc]
,[domicilio_cliente]
,[id_impositivo]
,[imp_total]
,[imp_tot_conc]
,[imp_neto]
,[impto_liq]
,[impto_liq_rni]
,[imp_op_ex]
,[impto_perc]
,[imp_iibb]
,[impto_perc_mun]
,[imp_internos]
,[imp_trib]
,[moneda_id]
,[moneda_ctz]
,[obs_comerciales]
,[obs_generales] 
,[forma_pago]
,[fecha_venc_pago]
,[presta_serv]
,[fecha_serv_desde]
,[fecha_serv_hasta],	
[cae]
,[fecha_vto]
,[resultado]
,[reproceso]
,[motivo]
,[telefono_cliente]
,[localidad_cliente]
,[provincia_cliente]
,[err_code]
,[err_msg]
,[dato_adicional1]
,[dato_adicional2]
,[dato_adicional3]
,[dato_adicional4]
,[dato_adicional5]
,[dato_adicional6]
,[dato_adicional7]
) values (	
1
,'WSFE'
,'FA000001'
,'20160328'
,1
,1
,1
,'<nombre_cliente, varchar(200),>'
,'80'
,201 --20000000001	
,'<domicilio_cliente, varchar(300),>'
,'<id_impositivo, varchar(50),>'
,122	--<imp_total, numeric(15,3),>
,0		--<imp_tot_conc, numeric(15,3),>
,100	--<imp_neto, numeric(15,3),>
,22		--<impto_liq, numeric(15,3),>
,0		--<impto_liq_rni, numeric(15,3),>
,0		--<imp_op_ex, numeric(15,3),>
,0		--<impto_perc, numeric(15,2),>
,0		--<imp_iibb, numeric(15,3),>
,0		--<impto_perc_mun, numeric(15,3),>
,0		--<imp_internos, numeric(15,3),>
,1		--<imp_trib, numeric(15,3),>
,'PES'
,1
,'<obs_comerciales, varchar(1000),>'
,'<obs, varchar(1000),>'
,'<forma_pago, varchar(50),>'
,'20160328'
,1
,'20160328'
,'20160328'
,123456789
,'20160410'
,'A'
,'N'
,'<motivo, varchar(40),>'
,'<telefono_cliente, varchar(50),>'
,'<localidad_cliente, varchar(50),>'
,'<provincia_cliente, varchar(50),>'
,'000000'
,'<err_msg, varchar(1000),>'
,'<dato_adic1, varchar(30),>'
,'<dato_adic2, varchar(30),>'
,'<dato_adic3, varchar(30),>'
,'<dato_adicional4, varchar(100),>'
,'<dato_adicional5, varchar(100),>'
,'<dato_adicional6, varchar(100),>'
,'<dato_adicional7, varchar(1000),>'
)

select @@ID_MOV = @@IDENTITY

INSERT INTO FEWS_DETALLE
([id]
,[tipo_reg]
,itm
,[codigo]
,[qty]
,[umed]
,[precio]
,[imp_total]
,[iva_id]
,[bonif]
,[imp_iva]
,[dato_a]
,[dato_b]
,[dato_c]
,[dato_d]
,[dato_e])
VALUES
(@@ID_MOV
,2
,1
,'<codigo, varchar(30),>'
,1 --<qty, numeric(12,2),>
,99 --<umed, int,>
,100 --<precio, numeric(12,3),>
,100 --<imp_total, numeric(14,3),>
,5 --<iva_id, int,>
,0 --<bonif, numeric(15,2),>
,21 --<imp_iva, numeric(15,2),>
,'<d_a, v(15)>'
,'<d_b, v(15)>'
,'<d_c, varchar(50),>'
,'<d_d, varchar(50),>'
,'<dato_e, varchar(100),>'
)

INSERT INTO [FEWS].[dbo].[FEWS_IVA]
([id]
,[tipo_reg]
,[iva_id]
,[base_imp]
,[importe])
VALUES
(@@ID_MOV
,3
,5
,100
,21
)

INSERT INTO [FEWS].[dbo].[FEWS_TRIBUTO]
([id]
,[tipo_reg]
,[tributo_id]
,[tributo_desc] 
,[base_imp]
,[alic]
,[importe])
VALUES
(@@ID_MOV
,3
,1
,'PERCEP IIBB'
,100 --<base_imp, numeric(15,3),>
,1 --<alic, numeric(15,2),>
,1 --<importe, numeric(15,3),>)
)

INSERT INTO [FEWS].[dbo].[FEWS_XML]
([id]
,[xml_response]
,[xml_request])
VALUES
(@@ID_MOV
,'<xml_response, text,>'
,'<xml_request, text,>'
)
commit tran
--rollback tran

select * from FEWS_ENCABEZADO
select * from FEWS_DETALLE
select * from FEWS_IVA
select * from FEWS_TRIBUTO
select * from FEWS_XML

update FEWS_ENCABEZADO 
set resultado = null, fecha_vto = null, cae = null
where movimiento = 'FA000001'