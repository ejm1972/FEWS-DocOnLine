select imp_neto, impto_liq, base_imp, importe, base_imp*0.21 iva_calc, imp_total, imp_trib, imp_op_ex, base_imp+importe+imp_trib+imp_op_ex tot_cal, e.id
from FEWS_ENCABEZADO e, FEWS_IVA i
where e.id=i.id
and e.movimiento='F3000085'

select max(e.imp_total) tot, max(imp_neto) neto, max(impto_liq) iva, max(imp_neto)+max(impto_liq)+MAX(imp_op_ex)+MAX(imp_trib) tot_calc
,sum(precio) neto_itm, sum(i.imp_total)+MAX(imp_trib) tot_itm, sum(i.imp_iva) iva_itm
,sum(round(PRECIO*0.21,3)) calc
from FEWS_ENCABEZADO e, FEWS_DETALLE i
where e.id=i.id
and e.movimiento='F3000085'

select e.imp_total tot, imp_neto neto, impto_liq iva, precio neto_itm, i.imp_total tot_itm, i.imp_iva iva_itm, round(precio*0.21,3) iva_itm_calc
from FEWS_ENCABEZADO e, FEWS_DETALLE i
where e.id=i.id
and e.movimiento='F3000085'