------------------------------ FACTURAS

declare @as_id int
set @as_id=787748
select as_id ,doc_nombre, as_numerodoc  from documentotipo , asiento where documentotipo.doc_id=asiento.doc_id and as_id=@as_id
exec AST_FEWS_PIDECAE @as_id, true
select 'AST_FEWS_LOG',* FROM AST_FEWS_LOG WHERE AS_ID=@AS_ID
select 'AST_FEWS_LOG_IVA',*  FROM AST_FEWS_LOG_IVA WHERE AS_ID=@AS_ID
select 'AST_FEWS_LOG_TRIBUTOS',*  FROM AST_FEWS_LOG_TRIBUTOS WHERE AS_ID=@AS_ID
select 'AST_FEWS_LOG_CBTE_ASOC',*  FROM AST_FEWS_LOG_CBTE_ASOC WHERE AS_ID=@AS_ID
select 'AST_FEWS_LOG_DATOS_OPC',*  FROM AST_FEWS_LOG_DATOS_OPC WHERE AS_ID=@AS_ID

declare @as_id int
set @as_id=787749
select as_id ,doc_nombre, as_numerodoc  from documentotipo , asiento where documentotipo.doc_id=asiento.doc_id and as_id=@as_id
exec AST_FEWS_PIDECAE @as_id, true
select 'AST_FEWS_LOG',* FROM AST_FEWS_LOG WHERE AS_ID=@AS_ID
select 'AST_FEWS_LOG_IVA',*  FROM AST_FEWS_LOG_IVA WHERE AS_ID=@AS_ID
select 'AST_FEWS_LOG_TRIBUTOS',*  FROM AST_FEWS_LOG_TRIBUTOS WHERE AS_ID=@AS_ID
select 'AST_FEWS_LOG_CBTE_ASOC',*  FROM AST_FEWS_LOG_CBTE_ASOC WHERE AS_ID=@AS_ID
select 'AST_FEWS_LOG_DATOS_OPC',*  FROM AST_FEWS_LOG_DATOS_OPC WHERE AS_ID=@AS_ID


declare @as_id int
set @as_id= 787750
select as_id ,doc_nombre, as_numerodoc  from documentotipo , asiento where documentotipo.doc_id=asiento.doc_id and as_id=@as_id
exec AST_FEWS_PIDECAE @as_id, true
select 'AST_FEWS_LOG',* FROM AST_FEWS_LOG WHERE AS_ID=@AS_ID
select 'AST_FEWS_LOG_IVA',*  FROM AST_FEWS_LOG_IVA WHERE AS_ID=@AS_ID
select 'AST_FEWS_LOG_TRIBUTOS',*  FROM AST_FEWS_LOG_TRIBUTOS WHERE AS_ID=@AS_ID
select 'AST_FEWS_LOG_CBTE_ASOC',*  FROM AST_FEWS_LOG_CBTE_ASOC WHERE AS_ID=@AS_ID
select 'AST_FEWS_LOG_DATOS_OPC',*  FROM AST_FEWS_LOG_DATOS_OPC WHERE AS_ID=@AS_ID



declare @as_id int
set @as_id= 787751
select as_id ,doc_nombre, as_numerodoc  from documentotipo , asiento where documentotipo.doc_id=asiento.doc_id and as_id=@as_id
exec AST_FEWS_PIDECAE @as_id, true
select 'AST_FEWS_LOG',* FROM AST_FEWS_LOG WHERE AS_ID=@AS_ID
select 'AST_FEWS_LOG_IVA',*  FROM AST_FEWS_LOG_IVA WHERE AS_ID=@AS_ID
select 'AST_FEWS_LOG_TRIBUTOS',*  FROM AST_FEWS_LOG_TRIBUTOS WHERE AS_ID=@AS_ID
select 'AST_FEWS_LOG_CBTE_ASOC',*  FROM AST_FEWS_LOG_CBTE_ASOC WHERE AS_ID=@AS_ID
select 'AST_FEWS_LOG_DATOS_OPC',*  FROM AST_FEWS_LOG_DATOS_OPC WHERE AS_ID=@AS_ID


------------------------------ NC SOBRE TOTAL FC
declare @as_id int
set @as_id= 787752
select as_id ,doc_nombre, as_numerodoc  from documentotipo , asiento where documentotipo.doc_id=asiento.doc_id and as_id=@as_id
exec AST_FEWS_PIDECAE @as_id, true
select 'AST_FEWS_LOG',* FROM AST_FEWS_LOG WHERE AS_ID=@AS_ID
select 'AST_FEWS_LOG_IVA',*  FROM AST_FEWS_LOG_IVA WHERE AS_ID=@AS_ID
select 'AST_FEWS_LOG_TRIBUTOS',*  FROM AST_FEWS_LOG_TRIBUTOS WHERE AS_ID=@AS_ID
select 'AST_FEWS_LOG_CBTE_ASOC',*  FROM AST_FEWS_LOG_CBTE_ASOC WHERE AS_ID=@AS_ID
select 'AST_FEWS_LOG_DATOS_OPC',*  FROM AST_FEWS_LOG_DATOS_OPC WHERE AS_ID=@AS_ID



------------------------ NOTA DE CREDITO
declare @as_id int
set @as_id= 787753
select as_id ,doc_nombre, as_numerodoc  from documentotipo , asiento where documentotipo.doc_id=asiento.doc_id and as_id=@as_id
exec AST_FEWS_PIDECAE @as_id, true
select 'AST_FEWS_LOG',* FROM AST_FEWS_LOG WHERE AS_ID=@AS_ID
select 'AST_FEWS_LOG_IVA',*  FROM AST_FEWS_LOG_IVA WHERE AS_ID=@AS_ID
select 'AST_FEWS_LOG_TRIBUTOS',*  FROM AST_FEWS_LOG_TRIBUTOS WHERE AS_ID=@AS_ID
select 'AST_FEWS_LOG_CBTE_ASOC',*  FROM AST_FEWS_LOG_CBTE_ASOC WHERE AS_ID=@AS_ID
select 'AST_FEWS_LOG_DATOS_OPC',*  FROM AST_FEWS_LOG_DATOS_OPC WHERE AS_ID=@AS_ID


            -------------------------- NOTA DE DEBITO
declare @as_id int
set @as_id= 787754
select as_id ,doc_nombre, as_numerodoc  from documentotipo , asiento where documentotipo.doc_id=asiento.doc_id and as_id=@as_id
exec AST_FEWS_PIDECAE @as_id, true
select 'AST_FEWS_LOG',* FROM AST_FEWS_LOG WHERE AS_ID=@AS_ID
select 'AST_FEWS_LOG_IVA',*  FROM AST_FEWS_LOG_IVA WHERE AS_ID=@AS_ID
select 'AST_FEWS_LOG_TRIBUTOS',*  FROM AST_FEWS_LOG_TRIBUTOS WHERE AS_ID=@AS_ID
select 'AST_FEWS_LOG_CBTE_ASOC',*  FROM AST_FEWS_LOG_CBTE_ASOC WHERE AS_ID=@AS_ID
select 'AST_FEWS_LOG_DATOS_OPC',*  FROM AST_FEWS_LOG_DATOS_OPC WHERE AS_ID=@AS_ID


       ------- NOTA DE DEBITO
declare @as_id int
set @as_id= 787755
select as_id ,doc_nombre, as_numerodoc  from documentotipo , asiento where documentotipo.doc_id=asiento.doc_id and as_id=@as_id
exec AST_FEWS_PIDECAE @as_id, true
select 'AST_FEWS_LOG',* FROM AST_FEWS_LOG WHERE AS_ID=@AS_ID
select 'AST_FEWS_LOG_IVA',*  FROM AST_FEWS_LOG_IVA WHERE AS_ID=@AS_ID
select 'AST_FEWS_LOG_TRIBUTOS',*  FROM AST_FEWS_LOG_TRIBUTOS WHERE AS_ID=@AS_ID
select 'AST_FEWS_LOG_CBTE_ASOC',*  FROM AST_FEWS_LOG_CBTE_ASOC WHERE AS_ID=@AS_ID
select 'AST_FEWS_LOG_DATOS_OPC',*  FROM AST_FEWS_LOG_DATOS_OPC WHERE AS_ID=@AS_ID