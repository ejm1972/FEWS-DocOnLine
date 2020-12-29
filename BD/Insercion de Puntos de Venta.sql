drop table #tmp
drop table #upv
go

select PUNTO_VENTA pv into #tmp from PUNTO_VENTA where 1=2
go

insert into #tmp (pv) values (9101)
insert into #tmp (pv) values (9401)
insert into #tmp (pv) values (9531)
insert into #tmp (pv) values (9102)
insert into #tmp (pv) values (9402)
insert into #tmp (pv) values (9533)
insert into #tmp (pv) values (9104)
insert into #tmp (pv) values (9404)
insert into #tmp (pv) values (9105)
insert into #tmp (pv) values (9405)
insert into #tmp (pv) values (9535)
go

insert into PUNTO_VENTA (ID_PUNTO_VENTA, ID_INTERFAZ, PUNTO_VENTA, ACTIVADO, FLG_CONTROL)
select (select COUNT(*) from #tmp tmp1 where tmp1.pv<=tmp.pv) id_punto_venta, 1003 id_interfaz, convert(varchar(4), pv) punto_venta, 'S' activado, 'N' flg_control from #tmp
where '1003'+convert(varchar(4), pv) not in (select convert(varchar(4), ID_INTERFAZ)+PUNTO_VENTA from PUNTO_VENTA)
go

select id, ID_PUNTO_VENTA into #upv from PUNTO_VENTA, USUARIO 
where convert(varchar(4), id)+convert(varchar(4), ID_PUNTO_VENTA) not in (select convert(varchar(4), id_usuario)+convert(varchar(4), id_punto_venta) from USUARIO_PUNTO_VENTA)

insert into USUARIO_PUNTO_VENTA (id, id_usuario, id_punto_venta)
select (select count(*) from #upv upv1 where convert(varchar(4), upv1.id)+convert(varchar(4), upv1.ID_PUNTO_VENTA)<=convert(varchar(4), upv.id)+convert(varchar(4), upv.ID_PUNTO_VENTA))+100 id
, id, ID_PUNTO_VENTA
from #upv upv